Vue.component('vht_details', {
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
        username: null,
        id: null
    }),
    template: //this is the main window
        '<div v-if="stats">' +
            '<v-row>' +
                '<v-col md="6" cols="12">' +
                    '<strong class="font-weight-light display-2">VHT Performance</strong>' +
                    //'<span class="font-weight-light">{{username}}</span>' +
                '</v-col>' +
                '<v-col md="6" cols="12">' +
                    '<span id="header-content" class="display-1 font-weight-regular">Username: ' +
                    '<span class="font-weight-light display-1">{{username}}</span></span>' +
                '</v-col>' +
            '</v-row>' +
            '<v-row>' + //v-if is there so the component only renders once the axios request has returned
                '<v-col md="4" cols="12">' +
                    '<v-row>\n' +
                        '<v-col cols="12" md="12">\n' +
                            '<info_card id="info_card" :value="currNumReferrals" title="Referrals this month" :icon="referralsIcon" :subtitle="getSubtitle(currNumReferrals, prevNumReferrals)"></info_card>\n' +
                        '</v-col>\n' +
                        '<v-col cols="12" md="12">\n' +
                            '<info_card id="info_card2" :value="currNumReadings" title="Readings this month" :icon="readingsIcon" :subtitle="getSubtitle(currNumReadings, prevNumReadings)"></info_card>\n' +
                        '</v-col>\n' +
                        '<v-col cols="12" md="12">\n' +
                            '<info_card id="info_card3" :value="currNumPatientsSeen" title="Patients seen this month" :icon="patientsIcon" :subtitle="getSubtitle(currNumPatientsSeen, prevNumPatientsSeen)"></info_card>\n' +
                        '</v-col>\n' +
                    '</v-row>' +
                '</v-col>' +
                '<v-col md="8" cols="12">\n' +
                    '<v-spacer class="pt-5"></v-spacer>' +
                    '<mychart :prevChartValues="prevChartValues()" :currChartValues="currChartValues()"></mychart>\n' +
                '</v-col>\n' +
            '</v-row>' +
        '</div>',
    mounted() {
        axios.get('/api/stats/overview/' + this.username).then((response) => {
            this.stats = response.data;
            this.getValuesFromDB();
            console.log(response.data);
            this.readingsIcon = this.getIcons(this.currNumReadings, this.prevNumReadings);
            this.referralsIcon = this.getIcons(this.currNumReferrals, this.prevNumReferrals);
            this.patientsIcon = this.getIcons(this.currNumPatientsSeen, this.prevNumPatientsSeen);
        });
    },
    created() {
        console.log("created");
        let urlQuery = new URLSearchParams(location.search);
        this.username = urlQuery.get('username');
        this.id = urlQuery.get('id');
        console.log(this.username)
    },
    methods: {
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
