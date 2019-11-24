
Vue.prototype.$http = axios;
import {TrafficLightCalc} from './TrafficLightCalc.js'
import {getReadingColorIcon} from './GetReadingColorIcon.js'
import {getReadingAdvice} from './GetReadingAdvice.js'
Vue.component('new_reading',{
    vuetify: new Vuetify(),
    data: () => ({
		StepIndex: {
			Green: 0,
			Yellow: 1,
			Red: 2
		},
		finished: true, // reading is validated and saved to server
        e1: 0,
        sex: 0,
        snackbar: false,
        symptoms: [],
        medications:[],
        pregnant: false,
		trafficIcon: null,
		readingAdvice: null,
        //For input validation. @TODO rules refuse to recognize these.
        MAX_SYSTOLIC: 300,
        MIN_SYSTOLIC: 10,
        MAX_DIASTOLIC: 300,
        MIN_DIASTOLIC: 10,
        MAX_HEART_RATE: 200,
        MIN_HEART_RATE: 40,
        valid: true,
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
            console.log(this.colour);
            //do input validation in a different function
            let NUMBER_OF_DAYS_IN_WEEK = 7;
            axios.post('/api/reading/save',
                {
                    patientId: this.patientID,
                    heartRate: parseInt(this.heartRate),
                    systolic: parseInt(this.systolic),
                    diastolic: parseInt(this.diastolic),
                    colour: this.colour,
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
	computed: {
    	colour: function() {
			return new TrafficLightCalc().getColour(this.systolic, this.diastolic, this.heartRate)
		},
		advice: function() {
    		return getReadingAdvice(this.colour);
		}
	},
	watch: {
    	colour: function() {
    		this.trafficIcon = getReadingColorIcon(this.colour);
		}
	},
    template:
	`
    <div class="customContainer">
		<div class="customDiv">
			<v-card min-width="220">
				<v-card-title id="summaryCardTitle">
				<h4>Summary</h4>
				</v-card-title>
				<v-card-text>
				<ul>
					<li>Patient ID: {{patientID}}</li>
					<li>Systolic: {{systolic}}</li>
					<li>Diastolic: {{diastolic}}</li>
					<li>Heartrate: {{heartRate}}</li>
					<li>Pregnant: {{pregnant}}</li>	
					<li>Gestational age: {{gestationalAge}}</li>	
					<li>Colour int: {{colour}}</li>
					<li>
					<img id="light" ref="light" v-if="trafficIcon" :src=trafficIcon height="35" width="45" style="margin-bottom: 7px">
					</li>
				</ul>
				<ul>
					<li v-for="symptom in symptoms">{{symptom}}</li>
				</ul>
				<ul>
					<li v-for="medication in medications">
						<ul>
							<li>name: {{medication.medicince}}</li>
							<li>dose: {{medication.dose}}</li>
							<li>frequency: {{medication.frequency}}</li>
						</ul>
					</li>
				</ul>
				<h5>Saved: {{finished}}</h5>
				</v-card-text>
				<v-card-actions>
				<v-spacer></v-spacer>
				</v-card-actions>
			</v-card>
		</div>
        <div class="customDiv">
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
                <v-form ref="newReadingForm"
                    	v-model="valid"
                    	lazy-validation
                    	class="ma-5 px-3">
                <v-text-field v-model="patientID"
                			  :rules="patientIDRules"
                			  label="Patient ID"
                			  required>
				</v-text-field>
                <v-text-field v-model="systolic"
                			  :rules="systolicRules"
                			  label="Systolic" 
                			  required>
				</v-text-field>
                <v-text-field v-model="diastolic"
                			  :rules="diastolicRules"
                			  label="Diastolic"
                  			  required>
             	</v-text-field>
                <v-text-field v-model="heartRate"
                			  :rules="heartRateRules"
                			  label="Heart Rate"
                			  required>
                </v-text-field>
                <template v-if="sex != 0"> <!-- if woman >-->
                    <v-checkbox v-model="pregnant" label="Pregnant"></v-checkbox>
                </template>
                <template v-if= "pregnant === true">
                <v-text-field v-model="gestationalAge"
                			  label="Pregnancy week"
                           	  :rules="gestationalAgeRules">
				</v-text-field>
                </template>
                </v-card>
                <v-btn color="primary" @click="e1 = 2">Continue</v-btn>
                <v-btn color="error" @click="reset">reset</v-btn>
            </v-stepper-content>
            <!--        This part is the second tab-->
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
			</v-stepper-content>
<!--        This part is the third tab-->
            <v-stepper-content step="3">
            <v-card  :elevation= "0" min-width="500">
            <ul>
              	<li v-for="(input, index) in medications">
                <v-text-field v-model="input.medicince"
                			  label="Medication"
                			  required>
				{{input.dose }}  
				</v-text-field>
                <v-text-field v-model="input.dose"
               				  label="Dose"
               				  required>
				{{input.dose}}  
				</v-text-field>
                <v-text-field v-model="input.frequency"
                			  label="Usage frequency"
                			  required>
				- {{ input.frequency}}  
				</v-text-field>
                <v-btn color="error" small @click="deleteRow(index)">delete</v-btn>
                </li>
            </ul>
                  </v-card>
              <v-btn @click="addRow">
              Add new medication</v-btn>
                  <v-btn
                    color="primary"
                    @click="validate"
                  >
                    Save reading
                  </v-btn>\n
                </v-stepper-content>
              </v-stepper-items>
            </v-stepper>
        </div>
		<div class="customDiv" v-if="finished" >
			<v-card class="list-card">
				<v-card-title>
				<h4>Reading Saved. Instructions:</h4>
				</v-card-title>
				<v-list>
					<v-list-item-content>
						<v-list-item-title>{{advice.analysis}}</v-list-item-title>
						<v-list-item-title>{{advice.summary}}</v-list-item-title>
					</v-list-item-content>
					<v-list-item-content>
						<v-list-item-title>Advice Details:</v-list-item-title>
						<v-img class="adviceDetailsImg" v-if="advice.details" :src=advice.details contain></v-img>
					</v-list-item-content>
					<v-list-item-content>
						<v-list-item-title>Condition:</v-list-item-title>
						<v-list-item-title>{{advice.condition}}</v-list-item-title>
					</v-list-item-content>
				</v-list>

			</v-card>
		</div>
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
