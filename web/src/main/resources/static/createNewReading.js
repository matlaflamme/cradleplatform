Vue.prototype.$http = axios;
var readingInput = new Vue ({
    el: '#readingInput',
    data: {
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
            console.log("Start")
            axios.post('http://localhost:8080/api/patient/reading',
                {
                    patientID: this.patientID,
                    heartRate: this.heartRate,
                    systolic: this.systolic,
                    diastolic: this.diastolic,
                    colour: this.colour,
                    timestamp: this.timestamp
                }
            ).then(response => {console.log(response)})
            console.log("End")
        }
    },
    mounted() {
        let urlQuery = new URLSearchParams(location.search); //retrieves everything after the '?' in url
        let id = urlQuery.get('id'); //search for 'id=' in query and return the value
        axios.get('http://localhost:8080/api/patient/'+ id).then(response => {this.patientID = id})
    }
});