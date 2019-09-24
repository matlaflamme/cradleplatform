alert("File found");

Vue.component('patient-table', {
    data: function() {
        console.log("helooooo");
        return { rows: //Request data from the server from this function. Get a JSON file as a response?
                [{ patientID: 5003, name: 'Matt', referralDate: 'Sept 22, 2019', bloodPressure: '120/80', heartRate: '72'},
                { patientID: 500, name: 'Laf', referralDate: 'Sept 24, 2019', bloodPressure: '160/100', heartRate: '86'}]
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
        '<th>{{row.patientID}}</th>' +
        '<td>{{row.name}}</td>' +
        '<td>{{row.referralDate}}</td>' +
        '<td>{{row.bloodPressure}}</td>' +
        '<td>{{row.heartRate}}</td>' +
        '<td><button v-on:click="viewPatientData(row.patientID)" class="btn btn-primary">View Patient Data</button></td>' +
        '<td><button v-on:click="addNewReading(row.patientID)" class="btn btn-secondary">Add a new Reading</button></td>' +
        '</tr>' +
        '</tbody>' +
        '</table>',
    methods: {
        viewPatientData: function (id) {
            console.log("View patient:"+id+" details");
        },
        addNewReading: function(id) {
            console.log("Add a new reading for patient:"+id)
        }
    }
});

Vue.component('button-counter', {
    data: function () {
        return {
            count: 0
        }
    },
    template: '<button v-on:click="count++">You clicked me {{ count }} times.</button>'
});

new Vue({
    el: '#newReferralTable',
    methods: {
        viewPatientData: function() {
            console.log("ot wprls");
        },
        addNewReading: function(patientID) {
            console.log(patientID);
        }
    }

});

new Vue({el: '#components-demo'});
