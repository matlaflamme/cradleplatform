Vue.prototype.$http = axios;

Vue.component('users_table' , {
    vuetify: new Vuetify(),
    props: {

    },
    data: () => ({
        headers: [ //Value is the key for the items (in html, it'll use this key to know what column data will go in)
                { text: 'Username', align: 'left', value: 'username'},
                { text: 'User Role', value: 'role' },
                { text: 'Actions', value: 'actions'},
        ],
            rows: [] //empty to start

    }),
    methods: {

    },
    template:
    `<v-data-table
        :headers="headers"
        :items="rows"
        :items-per-page="5"
        class="elevation-1"
        >
        </v-data-table>`
    ,
});

new Vue({
    el: '#app',
    vuetify: new Vuetify(),
    data: () => ({
        drawer: null
    })
});