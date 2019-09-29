Vue.component('patient-table', {
    data: function() {
        return {
            rows: null //initially null, gets changed from http request down below
        }
    },
    template: '<table class="table table-striped table-hover">\n' +
        '<thead>' +
        '<tr>' +
        '<th>Patient ID</th>' +
        '<th>Name</th>' +
        '<th>Referral Date</th>' +
        '<th>Blood Pressure</th>' +
        '<th>Heart Rate</th>' +
        '</tr>' +
        '</thead>' +
        '<tbody>' +
        '<tr v-for="row in rows">' +
        '<th>{{row.id}}</th>' +
        '<td>{{row.name}}</td>' +
        //'<td>{{row.referralDate}}</td>' +                 //will be added on future release
        //'<td>{{row.bloodPressure}}</td>' +
        //'<td>{{row.heartRate}}</td>' +
        '<td><button v-on:click="viewPatientData(row.id)" class="btn btn-primary">View Patient Data</button></td>' +
        '<td><button v-on:click="addNewReading(row.id)" class="btn btn-secondary">Add a new Reading</button></td>' +
        '</tr>' +
        '</tbody>' +
        '</table>',
    methods: {
        viewPatientData: function (id) {                            //Gets called when View button is pressed
            console.log("View patient:"+id+" details");
            window.location.replace("/patientSummary?001");
        },
        addNewReading: function(id) {
            console.log("Add a new reading for patient:"+id)
        }
    },
    mounted() { //sends request to server. Puts response into the rows variable
        axios.get('http://localhost:8080/api/patient/all').then(response => (this.rows = response.data))
    }
});

new Vue({
    el: '#newReferralTable',
    methods: {
        viewPatientData: function() {
            console.log("reached");
        },
        addNewReading: function(patientID) {
            console.log(patientID);
        }
    },
    mounted() {
    }

});
