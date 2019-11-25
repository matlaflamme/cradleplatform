Vue.prototype.$http = axios;

let test = new Vue({
    el: '#app',
    vuetify: new Vuetify(),
    data () {
        return {
            headers: [ //Value is the key for the items (in html, it'll use this key to know what column data will go in)
                { text: 'Patient ID', value: 'patientId' },
                { text: 'VHT ID', value: 'referrerUserName' },
                { text: 'Health Centre', value: 'healthCentre' },
                { text: 'Actions', value: 'actions', sortable: false },
                { text: 'Time', value: 'timestamp' },
                { text: 'Close referral', value: 'resolve', sortable: false},
                { text: 'Status', value: 'status' },
            ],
            rows: [], //empty to start
            editedIndex: -1,
            dialog: false,
            currItemResolved: false,
            editedItem: {
                closed: null,
                healthCentre: '',
                healthCentreNumber: '',
                id: 0,
                diagnosisString: '',
                patientId: '',
                readingId: 0,
                referrerUserName: '',
                timestamp: ''
            },
            defaultItem: {
                closed: null,
                healthCentre: '',
                healthCentreNumber: '',
                id: 0,
                diagnosisString: '',
                patientId: '',
                readingId: 0,
                referrerUserName: '',
                timestamp: ''
            }
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
            }, 60000) // 60 seconds
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
            console.log("Called accept()", arguments);
            // TODO: accept referral, pass in health worker Id, time
            // can't retrieve id at the moment
        },
        editItem (item) {
            this.editedIndex = this.rows.indexOf(item);
            this.editedItem = Object.assign({}, item);
            this.dialog = true
        },
        viewItem(item) {
            this.editedIndex = this.rows.indexOf(item);
            this.editedItem = Object.assign({}, item);
            this.currItemResolved = !!this.editedItem.closed;
            this.dialog = true;
        },
        close () {
            this.dialog = false;
            setTimeout(() => {
                this.editedItem = Object.assign({}, this.defaultItem);
                this.editedIndex = -1;
                this.currItemResolved = false;
            }, 300)
        },

        save () {
            axios.post('/api/referral/' + this.editedItem.readingId + '/diagnosis', {
                patientId: this.editedItem.patientId,
                description: this.editedItem.diagnosisString
            }).then(response => {
                console.log(response);
                axios.post('/api/referral/' + this.editedItem.readingId +'/resolve').then(response => {
                    console.log(response);
                    window.location.assign('/referrals')
                })
            });
            this.close();
        },
    },
    computed: {
        formTitle () {
            return this.editedIndex === -1 ? 'New Item' : 'Edit Item'
        },
    },

    watch: {
        dialog (val) {
            val || this.close()
        },
    },
    beforeDestroy() {
        clearInterval(this.polling);
    },

    created() {
        this.polling()
    }

});