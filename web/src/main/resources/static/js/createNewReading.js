import { TrafficLightCalc } from '/js/trafficLightCalc.js';

Vue.prototype.$http = axios;
Vue.component('new_reading',{
    vuetify: new Vuetify(),
    data: () => ({
        e1: 0,
        symptoms: [],
        medications:[],
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
            v => (v && v <= 200) || 'Heart Rate is invalid: low',
            v => (v && v >= 40) || 'Heart Rate is invalid: high'
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
        ]
    }),
    methods: {
        submit: function() {
            console.log(new TrafficLightCalc().getColour(this.systolic, this.diastolic, this.heartRate));
            //do input validation in a different function
            axios.post('/api/patient/reading',
                {
                    patientId: this.patientID,
                    heartRate: parseInt(this.heartRate),
                    systolic: parseInt(this.systolic),
                    diastolic: parseInt(this.diastolic),
                    colour: new TrafficLightCalc().getColour(this.systolic, this.diastolic, this.heartRate),
                    pregnant: false,
                    gestationalAge: null,
                    timestamp: getCurrentDate()
                }).catch(error => {
                    console.error(error);
                }
                ).then(response => {
                    console.log(response)
                    if (response.status == 200) {
                        window.location.assign("/patientSummary?id=" + this.patientID);
                    }
                });

                //
        },
        validate() {
            if (this.$refs.newReadingForm.validate()) {
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
            this.medications.push({
            })
        },
        deleteRow(index) {
            this.medications.splice(index,1)
        }

    },
    mounted() {
        let urlQuery = new URLSearchParams(location.search); //retrieves everything after the '?' in url
        let id = urlQuery.get('id'); //search for 'id=' in query and return the value
        this.patientID = id;
    },
    template: //@TODO Fix indentation
    '    <v-stepper v-model="e1">\n' +
        '      <v-stepper-header>\n' +
        '        <v-stepper-step :complete="e1 > 1" step="1" editable>Vitals</v-stepper-step>\n' +
        '        <v-divider></v-divider>\n' +
        '        <v-stepper-step :complete="e1 > 2" step="2" editable>Symptoms</v-stepper-step>\n' +
        '        <v-divider></v-divider>\n' +
        '        <v-stepper-step step="3" editable>Medications</v-stepper-step>\n' +
        '      </v-stepper-header>\n' +
        '      <v-stepper-items>\n' +
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
        '          </v-card>\n' +
        '  \n' +
        '          <v-btn\n' +
        '            color="primary"\n' +
        '            @click="e1 = 2"\n' +
        '          >\n' +
        '            Continue\n' +
        '          </v-btn>\n' +
        '  \n' +
        '          <v-btn\n' +
        '            color="error"'+
        '            @click="reset"\n' +
        '          >\n' +
        '            reset\n' +
        '          </v-btn>\n' +
        '        </v-stepper-content>\n' +
        '  \n' +
        '        <v-stepper-content step="2">\n' +
        '           <v-card  :elevation= "0" min-width="500">\n' +
        '    <v-container>\n' +
        '      <p>{{ symptoms }}</p>\n' +
        '      <v-checkbox v-model="symptoms" label="No Symptoms" value=\'No Symptoms\'></v-checkbox>\n' +
        '      <v-checkbox v-model="symptoms" label="Headaches" value="Headaches"></v-checkbox>\n' +
        '      <v-checkbox v-model="symptoms" label="Blurred Vision" value="Blurred Vission"></v-checkbox>\n' +
        '      <v-checkbox v-model="symptoms" label="Abdominal Pain" value="Abdominal Pain"></v-checkbox>\n' +
        '      <v-checkbox v-model="symptoms" label="Bleeding" value="Bleeding"></v-checkbox> \n' +
        '      <v-checkbox v-model="symptoms" label="Feverish" value="Feverish"></v-checkbox>\n' +
        '      <v-checkbox v-model="symptoms" label="Unwell" value="Unwell"></v-checkbox>' +
        '    </v-container>\n' +
        '          </v-card>\n' +
        '  \n' +
        '          <v-btn\n' +
        '            color="primary"\n' +
        '            @click="e1 = 3"\n' +
        '          >\n' +
        '            Continue\n' +
        '          </v-btn>\n' +
        '  \n' +
        '          <v-btn\n' +
        '            color="error"'+
        '            @click="reset"\n' +
        '          >\n' +
        '            reset\n' +
        '          </v-btn>\n' +
        '        </v-stepper-content>\n' +
        '  \n' +
        '        <v-stepper-content step="3">\n' +
        '          <v-card  :elevation= "0" min-width="500">\n' +
        '    <ul>\n' +
        '      <p>{{ medications }}</p>\n' +
        '      <li v-for="(input, index) in medications">\n' +
        '        <v-text-field\n' +
        '        v-model="input.medicince"\n' +
        '        label="Medication"\n' +
        '        required\n' +
        '      >- {{ input.dose }}  </v-text-field>\n' +
        '        <v-text-field\n' +
        '        v-model="input.dose"\n' +
        '        label="Dose"\n' +
        '        required\n' +
        '      >- {{ input.dose}}  </v-text-field>\n' +
        '        <v-text-field\n' +
        '        v-model="input.frequency"\n' +
        '        label="Usage frequency"\n' +
        '        required\n' +
        '      >- {{ input.frequency}}  </v-text-field>\n' +
        '      <v-btn @click="deleteRow(index)">\n' +
        '      delete</v-btn>' +
        '      </li>\n' +
        '    </ul>\n' +
        '    \n' +
        '          </v-card>\n' +
        '  \n' +
        '      <v-btn @click="addRow">\n' +
        '      Add new medication</v-btn>' +
        '          <v-btn\n' +
        '            color="primary"\n' +
        '            @click="validate"\n' +
        '          >\n' +
        '            Save reading\n' +
        '          </v-btn>\n' +
        '  \n' +
        '          <v-btn\n' +
        '            color="error"'+
        '            @click="reset"\n' +
        '          >\n' +
        '            reset\n' +
        '          </v-btn>\n' +
        '        </v-stepper-content>\n' +
        '      </v-stepper-items>\n' +
        '    </v-stepper>'

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
