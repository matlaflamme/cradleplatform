Vue.prototype.$http = axios;

Vue.component('adminLogs' , {
    vuetify: new Vuetify(),
    props: {

    },
    data () {
        return {
            smsLogs: [], //empty to start
            alertLogs: [] //empty to start
        }
    },
    mounted() { //sends request to server. Puts response into the rows variable
        console.log("mounted");
        this.getLogs();
        this.getAlerts();

    },
    methods: {
        polling: function() {
            this.polling = setInterval(() => {
                console.log("polling logs");
                this.getLogs();
                this.getAlerts();
            }, 3000) // 5 seconds
        },
        getLogs: function() {
            axios.get('/api/twilio/logs').then(response => {
                console.log("dada: " + response.data.split(','));
                this.smsLogs = response.data.split(',');
            }).catch(error => {
                console.log(error);
            })
        },
        getAlerts: function() {
            axios.get('/api/twilio/alerts').then(response => {
                console.log("dada: " + response.data.split(','));
                this.alertLogs = response.data.split(',');
            }).catch(error => {
                console.log(error);
            })
        }
    },

    beforeDestroy() {
        clearInterval(this.polling);
    },

    created() {
        this.polling()
    },

    template:
        `
        <div>
            <h2>Admin Logs</h2>
<!--                <twilio_log_list v-bind:logs="smsLogs" title="SMS Logs"></twilio_log_list>-->
<!--                <twilio_log_list v-bind:logs="alertLogs" title="Alert Logs"></twilio_log_list>-->
        </div>
        `
    ,
});

new Vue({
    el: '#app',
    vuetify: new Vuetify(),
    data: () => ({
        drawer: null
    })
});