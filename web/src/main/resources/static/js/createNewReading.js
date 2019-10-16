
Vue.prototype.$http = axios;
var readingInput = new Vue ({
    el: '#readingInput',
    data: {
        errors: [], //array to hold possible invalid input errors
        patientID: '',
        heartRate: '',
        systolic: '',
        diastolic: '',
        colour: '',
        timestamp: ''
        // symptoms: '',
        // diagnosis: '',
        // medication: '',
        // medStart: '',
        // medEnd: ''
    },
    methods: {
        submit: function() {
            console.log(new trafficLightCalc(this.systolic, this.diastolic, this.heartRate).getColour());
            //do input validation in a different function
            if (this.validateInput(this.systolic, this.diastolic, this.heartRate)) {
                //do this if input validation passes
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
                //console.log("End");
                window.location.assign("/patientSummary?id=" + this.patientID);
            }
        },
        validateInput: function(systolic, diastolic, heartRate) {
            //These are used to validate input
            let MAX_SYSTOLIC = 300;
            let MIN_SYSTOLIC = 10;
            let MAX_DIASTOLIC = 300;
            let MIN_DIASTOLIC = 10;
            let MAX_HEART_RATE = 200;
            let MIN_HEART_RATE = 40;

            this.errors = []; //clear previous errors
            let inputOK = true;
            if (systolic > MAX_SYSTOLIC || systolic < MIN_SYSTOLIC || !parseInt(systolic, 10)) {
                inputOK = false;
                this.errors.push("Systolic is invalid");
            }
            if (diastolic > MAX_DIASTOLIC || diastolic < MIN_DIASTOLIC || !parseInt(diastolic, 10)) {
                inputOK = false;
                console.log("dias err");
                this.errors.push("Diastolic is invalid");
            }
            if (heartRate > MAX_HEART_RATE || heartRate < MIN_HEART_RATE || !parseInt(heartRate, 10)) {
                inputOK = false;
                this.errors.push("Heart Rate is invalid")
            }
            console.log(this.errors);
            return inputOK;
        }
    },
    mounted() {
        let urlQuery = new URLSearchParams(location.search); //retrieves everything after the '?' in url
        let id = urlQuery.get('id'); //search for 'id=' in query and return the value
        axios.get('/api/patient/'+ id).then(response => {this.patientID = id})
    }
});

function getCurrentDate() {
    let now = new Date(); //new date object
    let date = now.getFullYear() + '-' + (now.getMonth() + 1) +'-' + now.getDate(); //create date string
    let time = now.getHours() + ':' + now.getMinutes() + ":" + now.getSeconds(); //create time string
    console.log(date + ' ' + time);
    return date + ' ' + time; //date and time string returned
}