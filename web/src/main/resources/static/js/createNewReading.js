import { TrafficLightCalc } from '/js/trafficLightCalc.js';

Vue.prototype.$http = axios;
Vue.component('new_reading',{
    vuetify: new Vuetify(),
    data: () => ({
		StepIndex: {
			Green: 0,
			Yellow: 1,
			Red: 2
		},
        e1: 0,
        sex: 0,
        snackbar: false,
        symptoms: [],
        medications:[],
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
                    // medications: this.medications //Not implemented in the server yet
                }).catch(error => {
                    console.error(error);
                    this.snackbar = true;
                }
                ).then(response => {
                    console.log(response)
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
		// Saves / Updates patient state for creating a new reading
		saveCreateNewReadingState(patientId, step, colour) {
        	// build patient object
			let patient = {
				pid: patientId,
				step: step,
				colour: colour
			};
			localStorage.setItem(patientId + "createNewReadingState", JSON.stringify(patient));
		},
		// Returns patient state object for creating a new reading
		// if green (retrieve, step1
		retrieveCreateNewReadingState(patientId) {
			return JSON.parse(localStorage.getItem(patientId + "createNewReadingState"));
		}
		// retest(patientId, step, colour) {
        // 	let patient = this.retrieveCreateNewReadingState(patientId);
        // 	if (patient.step == 2) {
		//
		// 	}
		// }

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

        })
    },
    template: //@TODO Fix indentation
	`
    <div>
        <v-stepper v-model="e1">
        	<v-stepper-header>
        		<v-stepper-step :complete="e1 > 1" step="1" editable>Vitals</v-stepper-step>
               	<v-divider></v-divider>
              	<v-stepper-step :complete="e1 > 2" step="2" editable>Symptoms</v-stepper-step>
               	<v-divider></v-divider>
            	<v-stepper-step step="3" editable>Medications</v-stepper-step>
            </v-stepper-header>
            <v-stepper-items>
<!--        //This part is the first tab-->
            <v-stepper-content step="1">
                <v-card
                <v-card  :elevation= "0" min-width="500">
                <v-card-title>
                </v-card-title>
                <v-form
                    ref="newReadingForm"
                    v-model="valid"
                    lazy-validation
                    class="ma-5 px-3"
                    >
                <v-text-field
                v-model="patientID"
                :rules="patientIDRules"
                label="Patient ID"
                required
              ></v-text-field>
                <v-text-field
                v-model="systolic"
                :rules="systolicRules"
                label="Systolic"
                required
              ></v-text-field>
                <v-text-field
                v-model="diastolic"
                :rules="diastolicRules"
                label="Diastolic"
                required
              ></v-text-field>
                <v-text-field
                v-model="heartRate"
               :rules="heartRateRules"
               label="Heart Rate"
                required
             ></v-text-field>
               <template v-if=" sex == 1 || sex == 2 "> <!-- if woman >-->
                    <v-checkbox v-model="pregnant" label="Pregnant"></v-checkbox>
                </template>
                <template v-if= "pregnant === true">
                <v-text-field
                v-model="gestationalAge"
                label="Pregnancy week"
                :rules="gestationalAgeRules"
              ></v-text-field>
                </template>
                 </v-card>
                 <v-btn
                    color="primary"
                   @click="e1 = 2"
                  >
                    Continue
                  </v-btn>
                  <v-btn
                    color="error"
                    @click="reset"
                  >
                    reset
                  </v-btn>
<!--        This part is the second tab-->
                </v-stepper-content>
                <v-stepper-content step="2">
                   <v-card  :elevation= "0" min-width="500">
            <v-container>
              <v-checkbox v-model="symptoms" label="No Symptoms" value="No Symptoms"></v-checkbox>
              <v-checkbox v-model="symptoms" label="Headache" value="Headache"></v-checkbox>
              <v-checkbox v-model="symptoms" label="Blurred Vision" value="Blurred Vision"></v-checkbox>
              <v-checkbox v-model="symptoms" label="Abdominal Pain" value="Abdominal Pain"></v-checkbox>
              <v-checkbox v-model="symptoms" label="Bleeding" value="Bleeding"></v-checkbox> 
              <v-checkbox v-model="symptoms" label="Feverish" value="Feverish"></v-checkbox>
             <v-checkbox v-model="symptoms" label="Unwell" value="Unwell"></v-checkbox>
            </v-container>
                 </v-card>
                 <v-btn
                   color="primary"
                    @click="e1 = 3"
                  >
                    Continue
                  </v-btn>
        // This part is the third tab
                </v-stepper-content>
                <v-stepper-content step="3">
                  <v-card  :elevation= "0" min-width="500">
            <ul>
              <li v-for="(input, index) in medications">
                <v-text-field
                v-model="input.medicince"
                label="Medication"
                required
              >{{input.dose }}  </v-text-field>
                <v-text-field
               v-model="input.dose"
               label="Dose"
               required
              >{{input.dose}}  </v-text-field>
                <v-text-field
                v-model="input.frequency"
                label="Usage frequency"
                required
              >- {{ input.frequency}}  </v-text-field>
              <v-btn color="error" small @click="deleteRow(index)">
              delete</v-btn>
              </li>
            </ul>
                  </v-card>
              <v-btn @click="addRow">
              Add new medication</v-btn>
                  <v-btn
                    color="primary"
                    @click="validate"
                  >\n
                    Save reading\n
                  </v-btn>\n
                </v-stepper-content>
              </v-stepper-items>
            </v-stepper>
        <v-snackbar v-model="snackbar">
            'Patient ID does not exist
            <v-btn
                color="pink"
                @click="snackbar = false"
            >
            'Close' 
            </v-btn>
        </v-snackbar>
    </div>
	`
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
