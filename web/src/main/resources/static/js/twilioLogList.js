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
    data () {
        return {
            search: '',
            rows: [] //empty to start
        }
    },
    template:
        `
        <template>
            <div id="logs-list">
                <li v-for="(log, x) in logs" :key="x">
                    <h2>{{title}}</h2>
                    <h3>{{log}}</h3>
                </li>
            </div>
        </template>
        `
    ,
});
