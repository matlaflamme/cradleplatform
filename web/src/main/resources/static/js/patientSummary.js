Vue.component('patient_info', {
    data: function() {
        return { rows: null//Request data from the server from this function. Get a JSON file as a response?
                //[{ date: '2/11/1995', time: '14:33', bp: '100/88', heartRate: '80', light: 'Green'}]
        }
    },
    template: '<table class="table table-striped table-hover">\n' +
        '<thead>' +
        '<tr>' +
        '<th>Reading ID</th>' +
        '<th>Date</th>' +
        '<th>Systolic</th>' +
        '<th>Diastolic</th>' +
        '<th>Heart Rate</th>' +
        '<th>Light</th>' +
        '</tr>' +
        '</thead>' +
        '<tbody>' +
        '<tr v-for="row in rows">' +
        '<th>{{row.id}}</th>' +
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

new Vue({
    el: '#patientReadings',
    methods: {
    },
    mounted() {

    }

});

let newReadingRedirect = new Vue({
    el: '#newReading',
    methods: {
        goToNewReading: function() {
            let urlQuery = new URLSearchParams(location.search); //retrieves everything after the '?' in url
            let id = urlQuery.get('id'); //search for 'id=' in query and return the value
            this.router.push("/createNewReading?id=" + id);
        }
    }
});

