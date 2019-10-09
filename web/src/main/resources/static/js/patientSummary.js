//This component is used for the center column "Past Readings" table of this page
Vue.component('patient_readings', {
    data: function() {
        return { rows: null//Request data from the server from this function. Get a JSON file as a response?
                //[{ date: '2/11/1995', time: '14:33', bp: '100/88', heartRate: '80', light: 'Green'}]
        }
    },
    template: '<table class="table table-striped table-hover">\n' +
        '<thead>' +
        '<tr>' +
        '<th>Date</th>' +
        '<th>Systolic</th>' +
        '<th>Diastolic</th>' +
        '<th>Heart Rate</th>' +
        '<th>Light</th>' +
        '</tr>' +
        '</thead>' +
        '<tbody>' +
        '<tr v-for="row in rows">' +
        '<th>{{row.timestamp}}</th>' +
        '<td>{{row.systolic}}</td>' +
        '<td>{{row.diastolic}}</td>' +
        '<td>{{row.heartRate}}</td>' +
        '<td>{{row.colour}}</td>' +
        '</tr>' +
        '</tbody>' +
        '</table>',
    mounted() {
        let urlQuery = new URLSearchParams(location.search); //retrieves everything after the '?' in url
        let id = urlQuery.get('id'); //search for 'id=' in query and return the value
        axios.get('/api/patient/'+ id + '/readings').then(response => (this.rows = response.data))
    }
});

//Needed for the patientReadings component
new Vue({
    el: '#patientReadings',
    methods: {
    },
    mounted() {

    }

});

//this object is used for the "new reading" button in the navbar
let newReadingRedirect = new Vue({
    el: '#newReading',
    methods: {
        goToNewReading: function() {
            let urlQuery = new URLSearchParams(location.search); //retrieves everything after the '?' in url
            let id = urlQuery.get('id'); //search for 'id=' in query and return the value
            window.location.assign("/createNewReading?id=" + id);
        }
    }
});

//This component is responsible for the left column of this page
Vue.component('basic_info', {
    data: function() {
        return {
            patientData: //starter data so that "patientData" is not null when the page is loaded
                {"id":"","name":"","villageNumber":"","birthYear":0,"sex":0,
                "gestationalAge":0,"medicalHistory":null,"drugHistory":null,"otherSymptoms":null,"pregnant":null,
                "readings":[{"id":0,"patientId":"","systolic":0,"diastolic":0,"heartRate":0,"colour":0,
                    "timestamp":""}]} //data in the form of json string
        }
    },
    template:
        '<div>'+
            '<div class="patientInfo">\n' +
            '    <h6>Patient Name:</h6>\n' +
            '    <p class="patientName">{{patientData.name}}<br></p>\n' +
            '    <h6>ID:</h6>\n' +
            '    <p class="patientId">{{patientData.id}}<br></p>\n' +
            '    <h6>Date Of Birth:</h6>\n' +
            '    <p class="DOB">{{patientData.birthYear}}<br></p>\n' +
            '    <h6>Sex:</h6>\n' +
            '    <p class="patientSex">{{getPatientSex(patientData.sex)}}<br></p>' + //calls getPatientSex method
            '</div>\n' +
            '    <h2>Latest Reading</h2>\n' +
            '    <img src="/img/cardiology.png" height="50" width="50" style="margin-bottom: 12px">\n' +
            '    <p id="heart_beat">{{patientData.readings[0].heartRate}}</p>\n' +
            '    <span id="light" ref="light" class="dot"></span>\n' +
            '    <br>\n' +
            '    <h6 style="display:inline-block;">Systolic:</h6>\n' +
            '    <p id="BP" style="display:inline-block">{{patientData.readings[0].systolic}}</p>\n' +
            '    <br>\n' +
            '    <h6 style="display:inline-block;">Diastolic:</h6>\n' +
            '    <p id="BP" style="display:inline-block">{{patientData.readings[0].diastolic}}</p>\n' +
            '    <br>\n' +
            '    <h6 style="display:inline-block;">Gestational Age:</h6>\n' +
            '    <p class="GestationalAge">{{patientData.gestationalAge}}</p>\n' +
            '    <p> Weeks</p>\n' +
            '    <h2>Symptoms</h2>\n' +
            '    <div class= "info" id="symptoms">Received overcame oh sensible so at an.\n' +
            '            Formed do change merely to county it. Am separate contempt\n' +
            '            domestic to to oh. On relation my so addition branched.</div>\n' +
            '    <h2>Current Medications</h2>\n' +
            '    <ul id="currentMedication">\n' +
            '        <li >item 1 </li>\n' +
            '        <li >item 2</li>\n' +
            '        <li >item 3</li>\n' +
            '        <li >item 4</li>\n' +
            '    </ul>' +
        '</div>',
    mounted() {
        let urlQuery = new URLSearchParams(location.search);
        let id = urlQuery.get('id');
        axios.get('/api/patient/' + id).then(response => {
            this.patientData = response.data;
            this.setLight(response.data) //update light colour based on response from get request
        });

    },
    methods: {
        getPatientSex: function(sexVal) {
            if (sexVal === 1) {
                return "Female";
            }
            else {
                return "Male";
            }
        },
        setLight(pData) {
            let digit = pData.readings[0].colour;
            let color = 'green';
            switch (digit) {
                case 0:
                    color = 'green';
                    break;
                case 1:
                case 2:
                    color = 'yellow';
                    break;
                case 3:
                case 4:
                    color = 'red';
                    break;
            }
           this.$refs.light.setAttribute("style", "background-color:" +  color + ";");
        }
    }
});

//needed for the "basic info" component
new Vue({
    el: '#basicInfo',
    methods: {
    },
    mounted() {
    },
    computed: {

    }
});

