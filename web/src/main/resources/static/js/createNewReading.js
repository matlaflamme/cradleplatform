import { TrafficLightCalc } from '/js/trafficLightCalc.js';

Vue.prototype.$http = axios;
Vue.component('new_reading',{
    vuetify: new Vuetify(),
    data: () => ({
        enabled: false,
        noSymptoms: true,
        selectedHealthCentre: null,
        healthCentreList: ['Empty'],
        customSymptom: "",
        e1: 0,
        sex: 0,
        snackbar: false,
        symptoms: [],
        medications: [],
        pregnant: false,
        //For input validation. @TODO rules refuse to recognize these.
        MAX_SYSTOLIC: 300,
        MIN_SYSTOLIC: 10,
        MAX_DIASTOLIC: 300,
        MIN_DIASTOLIC: 10,
        MAX_HEART_RATE: 200,
        MIN_HEART_RATE: 40,
        valid: true,
        colour: '',
        timestamp: '',
        patientID: '',
        patientIDRules: [
            v => !!v || 'patientID is required',
            v => (v && v > 0) || 'Patient ID can\'t be negative'
        ],
        heartRate: '',
        heartRateRules: [
            v => !!v || 'Heart Rate is required',
            v => (v && v <= 200) || 'Heart Rate is invalid',
            v => (v && v >= 40) || 'Heart Rate is invalid'
        ],
        systolic: '',
        systolicRules: [
            v => !!v || 'Systolic is required',
            v => (v && v <= 300) || 'Systolic is invalid',
            v => (v && v >= 10) || 'Systolic is invalid'
        ],
        diastolic: '',
        diastolicRules: [
            v => !!v || 'Diastolic is required',
            v => (v && v <= 300) || 'Diastolic is invalid',
            v => (v && v >= 10) || 'Diastolic is invalid'
        ],
        gestationalAge: null,
        gestationalAgeRules: [
            v => !!v || 'Diastolic is required',
            v => (v && v <= 42) || 'gestational age is invalid',
            v => (v && v >= 0) || 'gestational age is invalid'
        ]
    }),
    methods: {
        submit: function() {
            console.log(new TrafficLightCalc().getColour(this.systolic, this.diastolic, this.heartRate));
            //do input validation in a different function
            let NUMBER_OF_DAYS_IN_WEEK = 7;
            axios.post('/api/reading/save',
                {
                    patientId: this.patientID,
                    heartRate: parseInt(this.heartRate),
                    systolic: parseInt(this.systolic),
                    diastolic: parseInt(this.diastolic),
                    colour: new TrafficLightCalc().getColour(this.systolic, this.diastolic, this.heartRate),
                    pregnant: this.pregnant,
                    gestationalAge: parseInt(this.gestationalAge) * NUMBER_OF_DAYS_IN_WEEK, //convert weeks to days
                    timestamp: getCurrentDate(),
                    symptoms: this.symptoms,
                    otherSymptoms: this.checkCustomSymptoms()
                    // medications: this.medications //Not implemented in the server yet
                }).catch(error => {
                    console.error(error);
                    this.snackbar = true;
                }
                ).then(response => {
                    console.log(response);
                    let urlQuery = new URLSearchParams(location.search); //retrieves everything after the '?' in url
                    let id = urlQuery.get('id'); //search for 'id=' in query and return the value
                    if (this.medications !== null) {
                        axios.post('/api/patient/' + id + '/addMedications',
                            this.medications //array of medication objects
                        ).then(response => {
                            console.log(response)
                        });
                    }
                console.log(this.medications);
                    if (response.status == 200) {
                        window.location.assign("/patientSummary?id=" + this.patientID);
                    }
                    else {
                        this.snackbar = true;
                    }
                });

                //
        },
        validate() {
            if (this.$refs.newReadingForm.validate(this)) {
                if (this.symptoms.includes("No Symptoms")){
                    this.symptoms = []; //If no symptom is selected we have to return an empty list
                }
                this.submit();
            }
        },
        reset () {
            this.$refs.newReadingForm.reset();
        },
        resetValidation () {
            this.$refs.newReadingForm.resetValidation();
        },
        addRow() {
            this.medications.push({})
        },
        deleteRow(index) {
            this.medications.splice(index,1)
        },
        checkCustomSymptoms() {
            if (this.customSymptom !== "") {
                return this.customSymptom;
            }
            return null;
        },
        getAllHealthCentreOptions() {
            //waiting for merge of updated permissions for this api
            axios.get('/api/hc/all').then(response => {
                this.healthCentreList = response.data;
            }).catch(error => {
                console.error(error);
            });

            //this.healthCentreList = [{id: "002", name: "SFU"}, {id: "001", name: "MyCenter"}]
        },
        hasMedications() {
            console.log(this.medications.length);
            return this.medications.length !== 0;
        }
    },
    mounted() {
        let urlQuery = new URLSearchParams(location.search); //retrieves everything after the '?' in url
        let id = urlQuery.get('id'); //search for 'id=' in query and return the value
        if (id !== "null") {
            this.patientID = id;
        }
        axios.get('/api/patient/'+ id).then(response => {
            this.sex = response.data.sex;
            console.log(this.sex);

        });
        console.log(this.medications)
        this.getAllHealthCentreOptions();
    },
    template: //@TODO Fix indentation
    '<div>' +
    '    <v-stepper v-model="e1">\n' +
        '      <v-stepper-header>\n' +
        '        <v-stepper-step :complete="e1 > 1" step="1" editable>Vitals</v-stepper-step>\n' +
        '        <v-divider></v-divider>\n' +
        '        <v-stepper-step :complete="e1 > 2" step="2" editable>Symptoms</v-stepper-step>\n' +
        '        <v-divider></v-divider>\n' +
        '        <v-stepper-step :complete="e1 > 3" step="3" editable>Medications</v-stepper-step>\n' +
        '        <v-divider></v-divider>\n' +
        '        <v-stepper-step step="4" editable>Review</v-stepper-step>\n' +
        '      </v-stepper-header>\n' +
        '      <v-stepper-items>\n' +
        //This part is the first tab
        '        <v-stepper-content step="1">\n' +
        '          <v-card\n' +
        '        <v-card  :elevation= "0" min-width="500">\n' +
        '        <v-card-title>\n' +
        '        </v-card-title>' +
        '        <v-form\n' +
        '            ref="newReadingForm"\n' +
        '            v-model="valid"\n' +
        '            lazy-validation\n' +
        '            class="ma-5 px-3"\n' +
        '            >' +
        '        <v-text-field\n' +
        '        v-model="patientID"\n' +
        '        :rules="patientIDRules"\n' +
        '        label="Patient ID"\n' +
        '        required\n' +
        '      ></v-text-field>\n' +
        '        <v-text-field\n' +
        '        v-model="systolic"\n' +
        '        :rules="systolicRules"\n' +
        '        label="Systolic"\n' +
        '        required\n' +
        '      ></v-text-field>\n' +
        '        <v-text-field\n' +
        '        v-model="diastolic"\n' +
        '        :rules="diastolicRules"\n' +
        '        label="Diastolic"\n' +
        '        required\n' +
        '      ></v-text-field>\n' +
        '        <v-text-field\n' +
        '        v-model="heartRate"\n' +
        '        :rules="heartRateRules"\n' +
        '        label="Heart Rate"\n' +
        '        required\n' +
        '      ></v-text-field>\n' +
        '        <template v-if=" sex == 1 || sex == 2 ">\n' + // If patient is not a man show the pregnant option
        '            <v-checkbox v-model="pregnant" label="Pregnant"></v-checkbox>' +
        '        </template>' +
        '        <template v-if= "pregnant === true">\n' +
        '        <v-text-field\n' +
        '        v-model="gestationalAge"\n' +
        '        label="Pregnancy week"\n' +
        '        :rules="gestationalAgeRules"\n' +
        '      ></v-text-field>\n' +
        '        </template>' +
        '          </v-card>\n' +
        '          <v-btn\n' +
        '            color="primary"\n' +
        '            @click="e1 = 2"\n' +
        '          >\n' +
        '            Continue\n' +
        '          </v-btn>\n' +
        '          <v-btn\n' +
        '            color="error"'+
        '            @click="reset"\n' +
        '          >\n' +
        '            reset\n' +
        '          </v-btn>\n' +
        //This part is the second tab
        '        </v-stepper-content>\n' +
        '        <v-stepper-content step="2">\n' +
        '           <v-card  :elevation= "0" min-width="500">\n' +
        '    <v-container>\n' +
        '      <v-checkbox v-model="noSymptoms" label="No Symptoms"></v-checkbox>\n' +
        '      <v-checkbox v-model="symptoms" label="Headache" :disabled="noSymptoms" value="Headache"></v-checkbox>\n' +
        '      <v-checkbox v-model="symptoms" label="Blurred Vision" :disabled="noSymptoms" value="Blurred Vision"></v-checkbox>\n' +
        '      <v-checkbox v-model="symptoms" label="Abdominal Pain" :disabled="noSymptoms" value="Abdominal Pain"></v-checkbox>\n' +
        '      <v-checkbox v-model="symptoms" label="Bleeding" :disabled="noSymptoms" value="Bleeding"></v-checkbox> \n' +
        '      <v-checkbox v-model="symptoms" label="Feverish" :disabled="noSymptoms" value="Feverish"></v-checkbox>\n' +
        '      <v-checkbox v-model="symptoms" label="Unwell" :disabled="noSymptoms" value="Unwell"></v-checkbox>' +
        '      <v-checkbox v-model="enabled" :disabled="noSymptoms" label="Other:"></v-checkbox>' +
        '      <v-text-field :disabled="!enabled" label="Other symptoms" v-model="customSymptom"></v-text-field>' +
        '</v-container>\n' +
        '          </v-card>\n' +
        '          <v-btn\n' +
        '            color="primary"\n' +
        '            @click="e1 = 3"\n' +
        '          >\n' +
        '            Continue\n' +
        '          </v-btn>\n' +
        //This part is the third tab
        '        </v-stepper-content>\n' +
        '        <v-stepper-content step="3">\n' +
        '          <v-card  :elevation= "0" min-width="500">\n' +
        '    <ul>\n' +
        '      <li v-for="(input, index) in medications">\n' +
        '        <v-text-field\n' +
        '        v-model="input.medication"\n' +
        '        label="Medication"\n' +
        '        required\n' +
        '      >{{input.medication }}  </v-text-field>\n' +
        '        <v-text-field\n' +
        '        v-model="input.dosage"\n' +
        '        label="Dose"\n' +
        '        required\n' +
        '      >{{input.dosage}}  </v-text-field>\n' +
        '        <v-text-field\n' +
        '        v-model="input.usageFrequency"\n' +
        '        label="Usage frequency"\n' +
        '        required\n' +
        '      >{{ input.usageFrequency}}  </v-text-field>\n' +
        '      <v-btn color="error" small @click="deleteRow(index)">\n' +
        '      delete</v-btn>' +
        '      </li>\n' +
        '    </ul>\n' +
        '          </v-card>\n' +
        '      <v-btn @click="addRow">\n' +
        '      Add new medication</v-btn>' +
        '          <v-btn\n' +
        '            color="primary"\n' +
        '            @click="e1 = 4"\n' +
        '          >\n' +
        '            Continue\n' +
        '          </v-btn>\n' +
        '        </v-stepper-content>\n' +
        //This is the 4th step
        '<v-stepper-content step="4">\n' +
            '<v-card  :elevation= "0" min-width="500">\n' +
                //review items go here
                '<v-list-item>' +
                    '<v-list-content>' +
                        '<v-list-item-title>Heart Rate</v-list-item-title>' +
                        '<v-list-item-subtitle>{{heartRate}}</v-list-item-subtitle>' +
                    '</v-list-content>' +
                '</v-list-item>' +
                '<v-list-item>' +
                    '<v-list-content>' +
                        '<v-list-item-title>Systolic</v-list-item-title>' +
                        '<v-list-item-subtitle>{{systolic}}</v-list-item-subtitle>' +
                    '</v-list-content>' +
                '</v-list-item>' +
                '<v-list-item>' +
                    '<v-list-content>' +
                        '<v-list-item-title>Diastolic</v-list-item-title>' +
                        '<v-list-item-subtitle>{{diastolic}}</v-list-item-subtitle>' +
                    '</v-list-content>' +
                '</v-list-item>' +
                '<v-list-item>' +
                    '<v-list-content>' +
                        '<v-list-item-title>Gestational Age</v-list-item-title>' +
                        '<v-list-item-subtitle v-if="pregnant">{{gestationalAge}} weeks</v-list-item-subtitle>' +
                        '<v-list-item-subtitle v-if="!pregnant">Not pregnant</v-list-item-subtitle>' +
                    '</v-list-content>' +
                '</v-list-item>' +
        '<v-spacer></v-spacer>' +
                '<v-list-item>' +
                    '<v-list-content dense>' +
                        '<v-list-item-title>Symptoms</v-list-item-title>' +
                        '<ul v-if="!noSymptoms">\n'+
                            '<li v-for="symptom in symptoms">{{symptom}}</li>\n'+
                            '<li v-if="enabled">{{customSymptom}}</li>' +
                        '</ul>\n'+
                        '<ul v-if="noSymptoms">' +
                            '<li>No symptoms recorded</li>' +
                        '</ul>' +
                    '</v-list-content>' +
                '</v-list-item>' +
                '<v-list-item>' +
                    '<v-list-content>' +
                        '<v-list-item-title>Medications</v-list-item-title>' +
                        '<ul v-if="hasMedications" className="list-group">\n'+
                            '<li className="list-group-item" class="pb-1" v-for="medication in medications">' +
                                '<v-list-item dense>' +
                                    '<v-list-content dense>' +
                                        '<v-list-item-title>{{medication.medication}}</v-list-item-title>' +
                                        '<v-list-item-subtitle>{{medication.dosage}}</v-list-item-subtitle>' +
                                        '<v-list-item-subtitle>{{medication.usageFrequency}}</v-list-item-subtitle>' +
                                    '</v-list-content>' +
                                '</v-list-item>' +
                            '</li>' +
                        '</ul>\n'+
                        '<ul v-if="!hasMedications" className="list-group">' +
                            '<li className="list-group-item" class="pb-1">No medications</li>' +
                        '</ul>' +
                    '</v-list-content>' +
                '</v-list-item>' +
                //referral stuff here
                '<v-divider></v-divider>' +
                '<v-list-item>' +
                    '<v-list-item-content>' +
                        '<v-list-item-title>Make a referral (optional)</v-list-item-title>' +
                            '<v-layout wrap align-center id="new">\n' +
                                '<v-flex xs12 sm6 d-flex>\n' +
                                    '<v-select \n' +
                                    ' v-model="selectedHealthCentre"\n' +
                                    ' :items="healthCentreList"\n' +
                                    ' label="Select Health Centre"\n' +
                                    ' return-object> \n' +
                                        '<template v-slot:selection="data">\n' +
                                            '{{data.item.id}} - {{data.item.name}}\n' +
                                        '</template>\n' +
                                        '<template v-slot:item="data">\n' +
                                            '{{data.item.id}} - {{data.item.name}}\n' +
                                        '</template>\n' +
                                    '</v-select>\n' +
                                '</v-flex>\n' +
                            '</v-layout>' +
                        '</v-list-item-content>' +
                    '</v-list-item>' +
'          </v-card>\n' +
'          <v-btn\n' +
'            color="primary"\n' +
'            @click="validate"\n' +
'          >\n' +
'            Save reading\n' +
'          </v-btn>\n' +
'        </v-stepper-content>\n' +
'      </v-stepper-items>\n' +
'    </v-stepper>' +
        //stepper is finished, snackbars start here
        '<v-snackbar v-model="snackbar">' +
            'Please check your entries, patient id may not exist' +
            `<v-btn
                color="pink"
                @click="snackbar = false"
            >` +
            'Close' +
            '</v-btn>' +
        '</v-snackbar>' +
    '</div>'

});


function getCurrentDate() {
    let now = new Date(); //new date object
    let date = now.getFullYear() + '-' + (now.getMonth() + 1) +'-' + now.getDate(); //create date string
    let time = now.getHours() + ':' + now.getMinutes() + ":" + now.getSeconds(); //create time string
    console.log(date + ' ' + time);
    return date + ' ' + time; //date and time string returned
}

new Vue({
    el: '#app',
    vuetify: new Vuetify(),
    data: () => ({
        drawer: null
    })
});
