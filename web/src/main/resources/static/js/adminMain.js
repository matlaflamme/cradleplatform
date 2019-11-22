Vue.component('info_card', {
    props: {
        title: String, //Usage: {{title}} inside of the template is replaced with the prop
        value: String, //Prop is given a value inside of the component's html tag in the html file where its used
        icon: String,
        subtitle: String
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
                '<v-list-item-subtitle class="subtitle-1">{{subtitle}}</v-list-item-subtitle>\n' +
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
    props: {
        prevChartValues: Array,
        currChartValues: Array
    },
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
        //received its data through props, which are retrieved from the parent components axios request
        this.series = [{
            name: this.series[0].name,
            data: this.prevChartValues
            }, {
            name: this.series[1].name,
            data: this.currChartValues
            }]
    }
});

Vue.component('admin_dashboard', {
    data: () => ({
        stats: null, //initially null so that v-if statement will return false
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
        prevNumVHTs: 0,
        referralsIcon: "",
        readingsIcon: "",
        patientsIcon: "",
    }),
    template: //this is the main window
    '<div v-if="stats">' + //v-if is there so the component only renders once the axios request has returned
        '<v-row>\n' +
            '<v-col cols="12" md="4">\n' +
                '<info_card id="info_card" :value="currNumReferrals" title="Referrals this month" :icon="referralsIcon" :subtitle="getSubtitle(currNumReferrals, prevNumReferrals)"></info_card>\n' +
            '</v-col>\n' +
            '<v-col cols="12" md="4">\n' +
                '<info_card id="info_card2" :value="currNumReadings" title="Readings this month" :icon="readingsIcon" :subtitle="getSubtitle(currNumReadings, prevNumReadings)"></info_card>\n' +
            '</v-col>\n' +
            '<v-col cols="12" md="4">\n' +
                '<info_card id="info_card3" :value="currNumPatientsSeen" title="Patients seen this month" :icon="patientsIcon" :subtitle="getSubtitle(currNumPatientsSeen, prevNumPatientsSeen)"></info_card>\n' +
            '</v-col>\n' +
            '<v-col cols="12">\n' +
                '<v-spacer class="pt-5"></v-spacer>' +
                '<mychart :prevChartValues="prevChartValues()" :currChartValues="currChartValues()"></mychart>\n' +
            '</v-col>\n' +
        '</v-row>' +
    '</div>',
    mounted() {
        //this.setAllValsForTesting(); //remove this after api endpoint is set up only used for testing
        axios.get('/api/stats/overview').then((response) => {
            this.stats = response.data;
            this.getValuesFromDB();

            this.readingsIcon = this.getIcons(this.currNumReadings, this.prevNumReadings);
            this.referralsIcon = this.getIcons(this.currNumReferrals, this.prevNumReferrals);
            this.patientsIcon = this.getIcons(this.currNumPatientsSeen, this.prevNumPatientsSeen);
        });
    },
    methods: {
        setAllValsForTesting() { //dummy data used for testing the layout. To be removed once api endpoint is implemented
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
            this.prevNumVHTs = 12;
        },
        getValuesFromDB() {
                this.currNumGreens = this.stats.stat.numberOfGreens;
                this.currNumYellows = this.stats.stat.numberOfYellows;
                this.currNumReds = this.stats.stat.numberOfReds;
                this.currNumReadings = this.stats.stat.numberOfReadings;
                this.currNumPatientsSeen = this.stats.stat.numberOfPatientsSeen;
                this.currNumVHTs = this.stats.stat.numberOfVHTs;
                this.currNumReferrals = this.stats.stat.numberOfReferrals;

                this.prevNumGreens = this.stats.statTrend.numberOfGreens;
                this.prevNumYellows = this.stats.statTrend.numberOfYellows;
                this.prevNumReds = this.stats.statTrend.numberOfReds;
                this.prevNumReadings = this.stats.statTrend.numberOfReadings;
                this.prevNumPatientsSeen = this.stats.statTrend.numberOfPatientsSeen;
                this.prevNumVHTs = this.stats.statTrend.numberOfVHTs;
                this.prevNumReferrals = this.stats.statTrend.numberOfReferrals;

        },
        getIcons(currVal, prevVal) {
            let up = "trending_up";
            let down = "trending_down";
            let flat = "trending_flat";
            if (currVal < prevVal) {
                return down;
            }
            else if (currVal > prevVal) {
                return up;
            }
            else {
                return flat;
            }
        },
        currChartValues() { //sets up arrays for the data needed in the graph
            console.log(this.currNumReds);
           return [this.currNumGreens, this.currNumYellows, this.currNumReds];
        },
        prevChartValues() {
            return [this.prevNumGreens, this.prevNumYellows, this.prevNumReds];
        },
        getSubtitle(currVal, prevVal) {
            if (currVal === prevVal) {
                return "No change from last month.";
            }
            else if (prevVal === 0) {
                return currVal + " more than last month.";
            }
            else if (currVal === 0) {
                return prevVal + " less than last month.";
            }
            else if (currVal > prevVal) {
                let percentage = Math.round(((currVal / prevVal) - 1) * 100);
                return percentage + "% increase from last month.";
            }
            else {
                let percentage = Math.round((currVal / prevVal) * 100);
                return percentage + "% decrease from last month.";
            }
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