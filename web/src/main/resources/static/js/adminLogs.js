Vue.prototype.$http = axios;

Vue.component('admin_logs' , {
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
                console.log("dada: " + response.data[0]);
                this.smsLogs = response.data[0];
            }).catch(error => {
                console.log(error);
            })
        },
        getAlerts: function() {
            axios.get('/api/twilio/alerts').then(response => {
                console.log("dada: " + response.data[0]);
                this.alertLogs = response.data[0];
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
            <h2>Admin Logs.</h2>
            <h3>Current active number: +12053465536</h3>
                <twilio_log_list v-bind:logs="smsLogs" title="Latest SMS Log"></twilio_log_list>
                <twilio_log_list v-bind:logs="alertLogs" title="Latest Alert Log"></twilio_log_list>
        </div>
        `

});

Vue.component('twilio_log_list' , {
    vuetify: new Vuetify(),
    props: {
        title: {
            type: String,
            required: true
        },
        logs: {
            type: Array,
            required: true
        }
    },
    template:
        `
        <template>
            <div id="logs-list">
                <h2>{{title}}</h2>
                <li v-for="(log, x) in logs" :key="x">
                    {{log}}
                </li>
            </div>
        </template>
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