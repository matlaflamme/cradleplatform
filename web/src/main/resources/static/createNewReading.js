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
            console.log("Start");
            axios.post('/api/patient/reading',
                {
                    patientId: this.patientID,
                    heartRate: this.heartRate,
                    systolic: this.systolic,
                    diastolic: this.diastolic,
                    colour: this.colour,
                    timestamp: getCurrentDate()
                }
            ).then(response => {console.log(response)});
            console.log("End")
            window.location.assign("/patientSummary?id=" + this.patientID);
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