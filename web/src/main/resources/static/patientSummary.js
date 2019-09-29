Vue.component('patient_info', {
    data: function() {
        return { rows: //Request data from the server from this function. Get a JSON file as a response?
                [{ date: '2/11/1995', time: '14:33', bp: '100/88', heartRate: '80', light: 'Green'}]
        }
    },
    template: '<table class="table table-striped table-hover">\n' +
        '<thead>' +
        '<tr>' +
        '<th>Date</th>' +
        '<th>Time</th>' +
        '<th>Blood Pressure</th>' +
        '<th>heart Rate</th>' +
        '<th>Light</th>' +
        '</tr>' +
        '</thead>' +
        '<tbody>' +
        '<tr v-for="row in rows">' +
        '<th>{{row.date}}</th>' +
        '<td>{{row.time}}</td>' +
        '<td>{{row.bp}}</td>' +
        '<td>{{row.heartRate}}</td>' +
        '<td>{{row.light}}</td>' +
        '</tr>' +
        '</tbody>' +
        '</table>'
});

new Vue({
    el: '#patientReadings',
    methods: {
    }

});
