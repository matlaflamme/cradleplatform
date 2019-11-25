
Vue.prototype.$http = axios;
import {TrafficLightCalc} from './trafficLightCalc.js'
import {getReadingColorIcon} from './GetReadingColorIcon.js'
import {getReadingAdvice} from './GetReadingAdvice.js'
Vue.component('new_reading',{
    vuetify: new Vuetify(),
    data: () => ({
		finished: false, // reading is validated and saved to server
		retestStep: 0, // 0..2
		retestDialog: false,
		waitTime: null, // For first yellow reading - retest logic
        enabled: false,
        noSymptoms: true,
        readingId: 0,
        selectedHealthCentre: null,
        healthCentreList: ['Empty'],
        customSymptom: "",
        currentStep: 0,
        sex: 0,
        error_snackbar: false,
		success_snackbar: false,
		wait_snackbar: false,
		timer: null,
        symptoms: [],
        medications: [],
        pregnant: false,
		trafficIconCurrent: null,
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
        submit: function(sendRef, readingFinished) {
            console.log(this.colour);
            //do input validation in a different function
            let NUMBER_OF_DAYS_IN_WEEK = 7;
            axios.post('/api/reading/save', {
            	patientId: this.patientID,
				heartRate: parseInt(this.heartRate),
				systolic: parseInt(this.systolic),
				diastolic: parseInt(this.diastolic),
				colour: this.colour,
				pregnant: this.pregnant,
				gestationalAge: parseInt(this.gestationalAge) * NUMBER_OF_DAYS_IN_WEEK, //convert weeks to days
				timestamp: getCurrentDate(),
				symptoms: this.symptoms,
				otherSymptoms: this.checkCustomSymptoms()
            }).then(res => {
            	console.log(res);
            	if (readingFinished) {
					this.finished = true;
				}
            	this.success_snackbar = true;
            	let urlQuery = new URLSearchParams(location.search); //retrieves everything after the '?' in url
				let id = urlQuery.get('id'); //search for 'id=' in query and return the value
				if (this.medications !== null && this.medications.length > 0) {
					axios.post('/api/patient/'+ id + '/addMedications', this.medications).then(response => {
						console.log(response)
					}).catch(error => {
						console.log(error);
					});
				}
					console.log(this.medications);
				this.readingId = res.data.id;
				if(sendRef) {
                    this.sendReferral()
                }
                }).catch(error => {
                	console.log(error)
                	this.error_snackbar = true;
				});
        },
		// validate to be saved
        validate(sendRef, readingFinished, bypass) {
        	this.retestDialog = !readingFinished;
        	if (bypass) { // remove retest tracking for this patient
        		localStorage.removeItem(this.patientID);
        		localStorage.removeItem(this.patientID+1);
			}
			if (this.$refs.newReadingForm.validate(this)) {
				if (this.symptoms.includes("No Symptoms")) {
					this.symptoms = []; //If no symptom is selected we have to return an empty list
				}
			}
			this.submit(sendRef, readingFinished);
        },
		equalDates(dt2, dt1) {
			return (dt2-dt1 === 0)
		},
		retestValidator(sendRef) {
        	if (localStorage.getItem(this.patientID+1) !== null) {
        		// third reading
        		this.validate(sendRef, true, false); // finished
				this.clearLocalStorage();
				return;
			}
        	if (localStorage.getItem(this.patientID) !== null) {
        		// second reading
        		let patientObj = JSON.parse(localStorage.getItem(this.patientID));
        		console.log("testing 123: " + patientObj.pid);
        		if (patientObj.colour === 1 || patientObj.colour === 2) { // 1st reading was yellow
        			// check timer
					if (!this.equalDates(patientObj.date, new Date().toLocaleTimeString())) {
						this.retestDialog = true;
						this.wait_snackbar = true;
						return;
					} else {
						if (patientObj.colour === 1 || patientObj.colour === 2) { // 2nd reading yellow
							if (this.colour === 1 || this.colour === 2) {
								this.validate(sendRef, true, false);
								this.clearLocalStorage();
								return;
							} else { // 2nd reading green or red
								localStorage.setItem(this.patientID+1, JSON.stringify(this.buildCurrentPatientObject()));
								this.validate(sendRef,false, false); // save but reading not done
								return;
							}
						}
					}
				} else { // 1st reading was red
					if (this.colour === 3 || this.colour === 4) {
						// 2nd reading is read. reading process is done
						this.validate(sendRef, true, false);
						this.clearLocalStorage();
						return;
					} else {
						this.validate(sendRef, false, false);
						this.retestDialog = true;
						return;
					}
				}
			} else {
        		console.log("First reading");
        		// this is the first reading
				if (this.colour === 0) { // GREEN
					console.log("Green");
					this.validate(sendRef, true, false);
					return;
				} else {
					if (this.colour === 1 || this.colour === 2) { // YELLOW
						console.log("YELLOW");
						localStorage.setItem(this.patientID, JSON.stringify(this.buildCurrentPatientObject(true))); // wait time 15 mins
					} else { // RED
						console.log("RED");
						localStorage.setItem(this.patientID, JSON.stringify(this.buildCurrentPatientObject()));
					}
					this.validate(sendRef,false, false);
				}

			}
		},
		// builds patient object as needed for retest logic
		buildCurrentPatientObject(wait) {
        	if (wait) {
				let oldDate = new Date();
				return {
					pid: this.patientID,
					colour: this.colour,
					date: new Date(oldDate.getTime() + 15*60000).toLocaleTimeString()
				}
			} else {
        		return {
        			pid: this.patientID,
					colour: this.colour
				}
			}

		},
		clearLocalStorage() {
        	this.retestDialog = false;
        	localStorage.clear();
		},
		printLocalStorage() { // displays all objects in local storage for testing purposes
        	let values = [], keys = Object.keys(localStorage), i = keys.length;
        	while (i--) {
        		values.push(localStorage.getItem(keys[i]))
			}
			console.log(values);
		},
        reset () {
            this.$refs.newReadingForm.reset();
            this.finished = false;
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
                console.log(response.data)
                this.healthCentreList = response.data;
                this.healthCentreList.unshift({id: "0", name: "No Health Centre"});
            }).catch(error => {
                console.error(error);
            });

            //this.healthCentreList = [{id: "002", name: "SFU"}, {id: "001", name: "MyCenter"}]
        },
        hasMedications() {
            console.log(this.medications.length);
            return this.medications.length !== 0;
        },
        toPatient() {
            window.location.assign('/patientSummary?id=' + this.patientID)
        },
        sendReferral() {
            console.log("send that referral");
            console.log(this.getVHTName());
            console.log(this.getPatientName());
            console.log(this.patientID);
                axios.post('/api/referral/new', {
                patientId: this.patientID,
                readingId: this.readingId,
                healthCentreId: this.selectedHealthCentre.id

            }).then(response => {
                console.log(response)
                })

        },
        getVHTName() {
            axios.get('/api/user/whoami').then(response => {
                console.log("Username: " + response);
                return response.data
            })
        },
        getPatientName() {
            axios.get('/api/patient/' + this.patientID + '/info').then(response => {
                console.log(response.data);
                return response.data.name;
            })
        },
        healthCenterSelected() {
            if (this.selectedHealthCentre === null) {
                return false;
            }
            else return this.selectedHealthCentre.name !== "No Health Centre";
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
	computed: {
    	colour: function() {
			return new TrafficLightCalc().getColour(this.systolic, this.diastolic, this.heartRate)
		},
		advice: function() {
    		return getReadingAdvice(this.colour);
		},
		waitTime: function() {
    		let patientObj = JSON.parse(localStorage.getItem(this.patientID));
    		if (patientObj === null) {
    			console.log("Nothing stored for this patient");
    			return null;
			}
    		console.log("DATE: " + patientObj.date);
    		return patientObj.date;
		},
		trafficIconRetestDialog: function() {
    		if (localStorage.getItem(this.patientID+1) !== null) {
				let patientObj = JSON.parse(localStorage.getItem(this.patientID+1));
				return getReadingColorIcon(patientObj.colour)
			} else if (localStorage.getItem(this.patientID) !== null) {
    			let patientObj = JSON.parse(localStorage.getItem(this.patientID));
				return getReadingColorIcon(patientObj.colour)
			} else {
    			return null;
			}
		}
	},
	watch: {
    	colour: function() {
    		this.finished = false; // reading changed
    		this.trafficIconCurrent = getReadingColorIcon(this.colour);
		},
		symptoms: function() {
    		this.finished = false;
		},
		medications: function() {
			this.finished = false;
		},
		patientID: function() {
    		if (localStorage.getItem(this.patientID) !== null) {
    			this.retestDialog = true;
			}
    		this.finished = false;
			this.waitTime = JSON.parse(localStorage.getItem(this.patientID)).date
		},
		pregnant: function() {
    		this.finished = false;
		},
		noSymptoms: function() {
    		this.finished = false;
		},
		retestDialog: function() {
    		if (this.retestDialog) {
    			this.finished = false;
			}
    		this.waitTime = JSON.parse(localStorage.getItem(this.patientID)).date
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
					<img id="light" ref="light" v-if="trafficIconCurrent" :src=trafficIconCurrent height="35" width="45" style="margin-bottom: 7px">
					</li>
				</ul>
				<ul>
					<li v-for="symptom in symptoms">{{symptom}}</li>
				</ul>
				<ul>
					<li v-for="medication in medications">
						<ul>
							<li>name: {{medication.medication}}</li>
							<li>dose: {{medication.dose}}</li>
							<li>frequency: {{medication.frequency}}</li>
						</ul>
					</li>
				</ul>
				</v-card-text>
				<v-card-actions>
				<v-spacer></v-spacer>
				</v-card-actions>
			</v-card>
		</div>
        <div class="customDiv">
        <v-stepper v-model="currentStep">
        	<v-stepper-header>
        		<v-stepper-step :complete="currentStep > 1" step="1" editable>Vitals</v-stepper-step>
               	<v-divider></v-divider>
              	<v-stepper-step :complete="currentStep > 2" step="2" editable>Symptoms</v-stepper-step>
               	<v-divider></v-divider>
            	<v-stepper-step :complete="currentStep > 3" step="3" editable>Medications</v-stepper-step>
            	<v-divider></v-divider>
            	<v-stepper-step step="4" editable>Review</v-stepper-step>
            </v-stepper-header>
            <v-stepper-items>
<!--        //This part is the first tab-->
				<v-stepper-content step="1">
					<v-card
					<v-card  :elevation= "0" min-width="500" max-width="500">
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
					<v-btn color="primary" @click="currentStep = 2">Continue</v-btn>
					<v-btn color="error" @click="reset">reset</v-btn>
				</v-stepper-content>
				<!--        This part is the second tab-->
				<v-stepper-content step="2">
					<v-card  :elevation= "0" min-width="500" max-width="500">
					<v-container>
					  <v-checkbox v-model="noSymptoms" label="No Symptoms"></v-checkbox>
					  <v-checkbox v-model="symptoms" label="Headache" :disabled="noSymptoms" value="Headache"></v-checkbox>
					  <v-checkbox v-model="symptoms" label="Blurred Vision" :disabled="noSymptoms" value="Blurred Vision"></v-checkbox>
					  <v-checkbox v-model="symptoms" label="Abdominal Pain" :disabled="noSymptoms" value="Abdominal Pain"></v-checkbox>
					  <v-checkbox v-model="symptoms" label="Bleeding" :disabled="noSymptoms" value="Bleeding"></v-checkbox> 
					  <v-checkbox v-model="symptoms" label="Feverish" :disabled="noSymptoms" value="Feverish"></v-checkbox>
					 <v-checkbox v-model="symptoms" label="Unwell" :disabled="noSymptoms" value="Unwell"></v-checkbox>
					 <v-checkbox v-model="enabled" :disabled="noSymptoms" label="Other:"></v-checkbox>
					 <v-text-field :disabled="!enabled" label="Other symptoms" v-model="customSymptom"></v-text-field>
					</v-container>
						 </v-card>
						 <v-btn
						   color="primary"
							@click="currentStep = 3"
						  >
							Continue
						  </v-btn>
				</v-stepper-content>
	<!--        This part is the third tab-->
				<v-stepper-content step="3">
					<v-card  :elevation= "0" min-width="500">
						<ul>
							<li v-for="(input, index) in medications">
								<v-text-field v-model="input.medication"
											  label="Medication"
											  required>
								{{input.medication }}  
								</v-text-field>
								<v-text-field v-model="input.dosage"
											  label="Dose"
											  required>
								{{input.dosage}}  
								</v-text-field>
								<v-text-field v-model="input.usageFrequency"
											  label="Usage frequency"
											  required>
								- {{input.usageFrequency}}  
								</v-text-field>
								<v-btn color="error" small @click="deleteRow(index)">delete</v-btn>
							</li>
						</ul>
					</v-card>
					<v-btn @click="addRow"> Add new medication</v-btn>
					<v-btn color="primary" @click="currentStep = 4">Continue</v-btn>
				</v-stepper-content>
<!--				This part is the 4th step-->
				<v-stepper-content step="4">
					<v-card  :elevation= "0" min-width="500">
<!--						//review items go here-->
						<v-list-item>
							<v-list-content>
								<v-list-item-title>Heart Rate</v-list-item-title>
								<v-list-item-subtitle>{{heartRate}}</v-list-item-subtitle>
							</v-list-content>
						</v-list-item>
						<v-list-item>
							<v-list-content>
								<v-list-item-title>Systolic</v-list-item-title>
								<v-list-item-subtitle>{{systolic}}</v-list-item-subtitle>
							</v-list-content>
						</v-list-item>
						<v-list-item>
							<v-list-content>
								<v-list-item-title>Diastolic</v-list-item-title>
								<v-list-item-subtitle>{{diastolic}}</v-list-item-subtitle>
							</v-list-content>
						</v-list-item>
						<v-list-item>
							<v-list-content>
								<v-list-item-title>Gestational Age</v-list-item-title>
								<v-list-item-subtitle v-if="pregnant">{{gestationalAge}} weeks</v-list-item-subtitle>
								<v-list-item-subtitle v-if="!pregnant">Not pregnant</v-list-item-subtitle>
							</v-list-content>
						</v-list-item>
						<v-spacer></v-spacer>
						<v-list-item>
							<v-list-content dense>
								<v-list-item-title>Symptoms</v-list-item-title>
								<ul v-if="!noSymptoms">
									<li v-for="symptom in symptoms">{{symptom}}</li>
									<li v-if="enabled">{{customSymptom}}</li>
								</ul>
								<ul v-if="noSymptoms">
									<li>No symptoms recorded</li>
								</ul>
							</v-list-content>
						</v-list-item>
						<v-list-item>
							<v-list-content>
								<v-list-item-title>Medications</v-list-item-title>
								<ul v-if="hasMedications" className="list-group">
									<li className="list-group-item" class="pb-1" v-for="medication in medications">
										<v-list-item dense>
											<v-list-content dense>
												<v-list-item-title>{{medication.medication}}</v-list-item-title>
												<v-list-item-subtitle>{{medication.dosage}}</v-list-item-subtitle>
												<v-list-item-subtitle>{{medication.usageFrequency}}</v-list-item-subtitle>
											</v-list-content>
										</v-list-item>
									</li>
								</ul>
								<ul v-if="!hasMedications" className="list-group">
									<li className="list-group-item" class="pb-1">No medications</li>
								</ul>
							</v-list-content>
						</v-list-item>
<!--						//referral stuff here-->
						<v-divider></v-divider>
						<v-list-item>
							<v-list-item-content>
								<v-list-item-title>Make a referral (optional)</v-list-item-title>
								<v-layout wrap align-center id="new">
									<v-flex xs12 sm6 d-flex>
										<v-select v-model="selectedHealthCentre"
												  :items="healthCentreList"
												  label="Select Health Centre"
											 	  return-object> 
											<template v-slot:selection="data">{{data.item.id}} - {{data.item.name}}
											</template>
											<template v-slot:item="data">{{data.item.id}} - {{data.item.name}}
											</template>
										</v-select>
									</v-flex>
								</v-layout>
							</v-list-item-content>
						</v-list-item>
		          	</v-card>
		        <v-btn v-if="!healthCenterSelected()" color="primary" @click="retestValidator(false)">Save reading</v-btn>
		        <v-btn color="error" small @click="clearLocalStorage">Clear Retests</v-btn>
<!--		        // <v-btn color="error" small @click="printLocalStorage">Print Retests</v-btn>-->
		        <v-btn v-if="healthCenterSelected()" @click="retestValidator(true)" color="error">Send referral</v-btn>
		        <v-icon v-if="finished" large color="green darken-2" >mdi-check</v-icon>
		        <v-btn v-if="finished" color="secondary" @click="toPatient">Back to patient</v-btn>
		        </v-stepper-content>
		  	</v-stepper-items>
		</v-stepper>
		</div>
		<div class="customDiv" v-if="finished" >
			<v-card class="list-card" max-width="500">
				<v-card-title>
				<h4>Reading advice:</h4>
				</v-card-title>
				<v-list>
					<v-list-item-content>
					    <li>
                            <img id="light" ref="light" v-if="trafficIconCurrent" :src=trafficIconCurrent height="35" width="45" style="margin-bottom: 7px">
                        </li>
						<v-list-item-title><b>{{advice.analysis}}</b></v-list-item-title>
						<v-list-item-title class="text-justify text-left white-space-wrap">{{advice.summary}}</v-list-item-title>
					</v-list-item-content>
					<v-list-item-content>
						<v-list-item-title>Advice Details:</v-list-item-title>
						<p class="text-justify text-left caption white-space-wrap">{{advice.details}}</p>
					</v-list-item-content>
					<v-list-item-content>
						<v-list-item-title>Condition:</v-list-item-title>
						<v-list-item-title>{{advice.condition}}</v-list-item-title>
					</v-list-item-content>
				</v-list>
			</v-card>
		</div>
		<div class="customDiv" v-if="retestDialog" >
			<v-card class="list-card">
				<v-card-title>
				<h4 v-if="waitTime">Retest Dialog</h4>
				</v-card-title>
				<v-list>
					<v-list-item-content>
						<v-list-item-title>Latest reading result for patient: <b>{{patientID}}</b></v-list-item-title>
						<v-img height="50px" width="60px" padding="5px" align="left" v-if="trafficIconRetestDialog" :src=trafficIconRetestDialog contain></v-img>
					</v-list-item-content>
					<v-list-item-content v-if="waitTime">
						<v-list-item-title>Please perform a retest at: <b>{{waitTime}}</b></v-list-item-title>
					</v-list-item-content>
					<v-list-item-content v-else>
						<v-list-item-title>Please perform a retest <b>immediately</b></v-list-item-title>
					</v-list-item-content>
					
				</v-list>
			</v-card>
		</div>
        <v-snackbar v-model="error_snackbar">
            Error: Patient ID does not exist
            <v-btn color="red" @click="error_snackbar = false">Close</v-btn>
        </v-snackbar>
        <v-snackbar v-model="success_snackbar">
            Success! Reading saved.
            <v-btn color="green" @click="success_snackbar = false">Close</v-btn>
        </v-snackbar>
        <v-snackbar v-model="wait_snackbar">
            Wait until {{waitTime}} to create reading for patient: {{patientID}}
            <v-btn color="green" @click="wait_snackbar = false; validate(false, true, true)">Save anyway</v-btn>
        </v-snackbar>
    </div>
	`
});

function getCurrentDate() {
	let now = new Date(); //new date object
	let date = now.getFullYear() + '-' + (now.getMonth() + 1) +'-' + now.getDate(); //create date string
	let time = now.getHours() + ':' + now.getMinutes() + ":" + now.getSeconds(); //create time string
	return date + ' ' + time; //date and time string returned
}


new Vue({
    el: '#app',
    vuetify: new Vuetify(),
    data: () => ({
        drawer: null
    })
});
