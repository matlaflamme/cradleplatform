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

Vue.component('sidebar_item', {
    props: {
        title: String,
        icon: String,
        clickable: String
    },
    template:
    '<v-list-item @click="changeWindow(clickable)">' +
        '<v-list-item-action>' +
            '<v-icon>{{icon}}</v-icon>' +
        '</v-list-item-action>' +
        '<v-list-item-content>' +
            '<v-list-item-title>{{title}}</v-list-item-title>' +
        '</v-list-item-content>' +
    '</v-list-item>',
    methods: {
        changeWindow: function(clicked) { //ideally, a click should call a different function based on prop 'clickable'
            //for now: if/else with clicked value to change window
            console.log("change window to " + clicked);
        },
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