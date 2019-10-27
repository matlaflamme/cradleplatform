Vue.prototype.$http = axios;

let test = new Vue({
    el: '#app',
    vuetify: new Vuetify(),
    data () {
        return {
            headers: [ //Value is the key for the items (in html, it'll use this key to know what column data will go in)
                { text: 'Referral ID', align: 'left', value: 'id'},
                { text: 'Patient ID', value: 'patientId' },
                { text: 'VHT ID', value: 'vhtId' },
                { text: 'Health Centre', value: 'healthCentre' },
                { text: 'Health Centre Number', value: 'healthCentreNumber' },
                { text: 'View', value: 'view', sortable: false },
                { text: 'Accept', value: 'accept', sortable: false },
                { text: 'Time', value: 'timestamp' },
                { text: 'Assigned To', value: 'accepter' },
                { text: 'Closed', value: 'closed' },
            ],
            rows: [] //empty to start
        }

    },
    mounted() { //sends request to server. Puts response into the rows variable
        console.log("mounted");
        this.getReferrals();

    },
    methods: {
        polling: function() {
            this.polling = setInterval(() => {
                console.log("polling referrals");
                this.getReferrals();
            }, 3000) // 3 seconds
        },
        viewPatientData: function (id) {  //Gets called when View button is pressed
            console.log("View patient:"+id+" details");
            window.location.assign("/patientSummary?id=" + id);
        },
        getReferrals: function() {
            axios.get('/api/referral/all').then(response => {
                console.log(response);
                this.rows = response.data;
                console.log("this rows: " + this.rows);
            }).catch(error => {
                console.log(error);
            })
        },
        accept: function() {
            // TODO: accept referral, pass in health worker Id, time
            // can't retrieve id at the moment
        }
    },

    beforeDestroy() {
        clearInterval(this.polling);
    },

    created() {
        this.polling()
    }

});