
Vue.component('info_card', {
    template:
    '<v-card\n' +
        'class="mx-auto"\n' +
        'max-width="344"\n' +
        'raised\n' +
        '>\n' +
        '    <v-list-item three-line>\n' +
        '        <v-list-item-content>\n' +
        '             <div class="overline mb-4">Referrals this week</div>\n' +
        '                 <v-list-item-title class="headline mb-1">76</v-list-item-title>\n' +
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


new Vue({
    el: '#app',
    vuetify: new Vuetify(),
})

new Vue({
    el: '#info_card',
    vuetify: new Vuetify()
    })

new Vue({
    el: '#info_card2',
    vuetify: new Vuetify()
})

new Vue({
    el: '#info_card3',
    vuetify: new Vuetify()
})