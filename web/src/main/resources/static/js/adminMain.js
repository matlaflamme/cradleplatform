Vue.component('info_card', {
    props: {
        title: String, //Usage: {{title}} inside of the template is replaced with the prop
                        //Prop is given a value inside of the component's html tag in the html file where its used
    },
    template:
    '<v-card\n' +
        'class="mx-auto"\n' +
        'max-width="344"\n' +
        'raised\n' +
        '>\n' +
        '    <v-list-item three-line>\n' +
        '        <v-list-item-content>\n' +
        '             <div class="overline mb-4">Referrals this week</div>\n' +
        '                 <v-list-item-title class="headline mb-1">{{title}}</v-list-item-title>\n' +
        '                 <v-list-item-subtitle>173 VHTs across 12 zones</v-list-item-subtitle>\n' +
        '             </v-list-item-content>\n' +
        '\n' +
        '         <v-list-item-avatar\n' +
        '            tile\n' +
        '            size="80"\n' +
        '            color="grey"\n' +
        '          ></v-list-item-avatar>\n' +
        '     </v-list-item>\n' +
        '\n' +
        '     <v-card-actions>\n' +
        '         <v-btn text>View more</v-btn>\n' +
        '         <v-btn text>Export</v-btn>\n' +
        '     </v-card-actions>\n' +
        '</v-card>'
});

Vue.component('mychart', {
    template:
    '<div>\n' +
        '<apexchart width="500" type="bar" :options="chartOptions" :series="series"></apexchart>\n' +
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
                    categories: [1991, 1992, 1993, 1994, 1995, 1996, 1997, 1998]
                }
            },
            series: [{
                name: 'series-1',
                data: [30, 40, 35, 50, 49, 60, 70, 91]
            }]
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