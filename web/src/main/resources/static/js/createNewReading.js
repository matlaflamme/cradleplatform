Vue.prototype.$http = axios;
Vue.component('new_reading',{
    vuetify: new Vuetify(),
    data: () => ({
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
            console.log(new trafficLightCalc(this.systolic, this.diastolic, this.heartRate).getColour());
            //do input validation in a different function
            axios.post('/api/patient/reading',
                {
                    patientId: this.patientID,
                    heartRate: this.heartRate,
                    systolic: this.systolic,
                    diastolic: this.diastolic,
                    colour: new trafficLightCalc(this.systolic, this.diastolic, this.heartRate).getColour(),
                    timestamp: getCurrentDate()
                }
                ).then(response => {console.log(response)});

                window.location.assign("/patientSummary?id=" + this.patientID);
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
        }
    },
    mounted() {
        let urlQuery = new URLSearchParams(location.search); //retrieves everything after the '?' in url
        let id = urlQuery.get('id'); //search for 'id=' in query and return the value
        this.patientID = id;
        console.log(id);
        console.log(this.patientID);
        //axios.get('/api/patient/'+ id).then(response => {this.patientID = id})
    },
    template: //@TODO Fix indentation
        '<v-card class="overflow-hidden"> ' +
        `<v-card-title>
            <span class="title">Add a new reading</span>`+
        '</v-card-title> ' +
        `<v-form
            ref="newReadingForm"
            v-model="valid"
            lazy-validation
            class="ma-5 px-3"
            >` +
        ` <v-text-field
        v-model="patientID"
        :rules="patientIDRules"
        label="Patient ID"
        required
      ></v-text-field>` +
        `<v-text-field
        v-model="heartRate"
        :rules="heartRateRules"
        label="Heart Rate"
        required
      ></v-text-field>` +
        `<v-text-field
        v-model="systolic"
        :rules="systolicRules"
        label="Systolic"
        required
      ></v-text-field>` +
        `<v-text-field
        v-model="diastolic"
        :rules="diastolicRules"
        label="Diastolic"
        required
      ></v-text-field>` +
        `<v-btn
        :disabled="!valid"
        color="success"
        class="mr-4"
        @click="validate"
      >
        Submit
      </v-btn>` +
        `<v-btn
        color="error"
        class="mr-4"
        @click="reset"
      >
        Clear Form
      </v-btn>
    </v-form>` +
        '</v-card>'
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