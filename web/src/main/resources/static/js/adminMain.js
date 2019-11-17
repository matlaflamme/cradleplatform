Vue.component('info_card', {
    props: {
        title: String, //Usage: {{title}} inside of the template is replaced with the prop
        value: String, //Prop is given a value inside of the component's html tag in the html file where its used
        icon: String
    },
    template:
    '<v-card\n' +
        'class="mx-auto"\n' +
        'max-width="344"\n' +
        'raised\n' +
    '>\n' +
        '<v-list-item three-line>\n' +
            '<v-list-item-content>\n' +
                '<div class="title">{{title}}</div>\n' +
                '<v-list-item-title class="display-3">{{value}}</v-list-item-title>\n' +
                '<v-list-item-subtitle class="subtitle-1">173 VHTs across 12 zones</v-list-item-subtitle>\n' +
            '</v-list-item-content>\n' +

            '<v-list-item-icon\n' +
                '<v-icon size="80">{{icon}}</v-icon>' +
            '</v-list-item-icon>\n' +
        '</v-list-item>\n' +
    '</v-card>',
    methods: {
        //will need methods to determine trends? Or do that in parent component
    }
});

Vue.component('mychart', {
    template:
    '<div>\n' +
        '<apexchart height="500" type="bar" :options="chartOptions" :series="series"></apexchart>\n' +
    '</div>',
    components: {
        apexchart: VueApexCharts,
    },
    data: function() {
        return {
            chartOptions: {
                chart: {
                    id: 'vuechart-example'
                },
                xaxis: {
                    categories: []
                }
            },
            series: [{
                name: 'Previous 30-day period',
                data: []
                }, {
                name: 'Current 30-day period',
                data: []
            }]
        }
    },
    created() { //using created so that data is ready for chart on page load (animation works)
        this.chartOptions.xaxis.categories = ["Greens", "Yellows", "Reds"];
        this.series[0].data = [30, 40, 35]; //previous
        this.series[1].data = [50, 50, 50]; //current

        //will be using an axios method to get these numbers
        //axios.get('/api/admin/info').then(response => {
        //
        //});
    }
});

Vue.component('admin_dashboard', {
    data: () => ({
        currNumReds: 0,
        prevNumReds: 0,
        currNumYellows: 0,
        prevNumYellows: 0,
        currNumGreens: 0,
        prevNumGreens: 0,
        currNumReadings: 0,
        prevNumReadings: 0,
        currNumPatientsSeen: 0,
        prevNumPatientsSeen: 0,
        currNumReferrals: 0,
        prevNumReferrals: 0,
        currNumVHTs: 0,
        prevNumVHT: 0
    }),
    template:
        '<v-row>\n' +
            '<v-col cols="12" md="4">\n' +
                '<info_card id="info_card" :value="currNumReferrals" title="Referrals this month" icon="trending_up"></info_card>\n' +
            '</v-col>\n' +
            '<v-col cols="12" md="4">\n' +
                '<info_card id="info_card2" :value="currNumReadings" title="Readings this month" icon="trending_up"></info_card>\n' +
            '</v-col>\n' +
            '<v-col cols="12" md="4">\n' +
                '<info_card id="info_card3" :value="currNumPatientsSeen" title="Patients seen this month" icon="trending_down"></info_card>\n' +
            '</v-col>\n' +
            '<v-col cols="12">\n' +
        '<v-spacer class="pt-5"></v-spacer>' +
                '<mychart></mychart>\n' +
            '</v-col>\n' +
        '</v-row>',
    created() {
        this.setAllValsForTesting(); //remove this after api endpoint is set up
        //axios.get('/api/admin/info').then(response => {
        //@TODO: Determine trends, fix text sizing, fix subtitles
    },
    methods: {
        setAllValsForTesting() {
            this.currNumReds = 17;
            this.prevNumReds = 23;
            this.currNumYellows = 37;
            this.prevNumYellows = 49;
            this.currNumGreens = 86;
            this.prevNumGreens = 63;
            this.currNumReadings = 134;
            this.prevNumReadings = 112;
            this.currNumPatientsSeen = 48;
            this.prevNumPatientsSeen = 41;
            this.currNumReferrals = 35;
            this.prevNumReferrals = 54;
            this.currNumVHTs = 26;
            this.prevNumVHT = 12;
        }
    }
});

new Vue({
    el: '#app',
    vuetify: new Vuetify(),
    data: () => ({
        drawer: null
    })
});

new Vue({
    el: '#info_card',
    vuetify: new Vuetify()
    });

new Vue({
    el: '#info_card2',
    vuetify: new Vuetify()
});

new Vue({
    el: '#info_card3',
    vuetify: new Vuetify()
});