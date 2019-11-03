Vue.prototype.$http = axios;

Vue.component('users_table' , {
    vuetify: new Vuetify(),
    props: {

    },
    data () {
        return {
            search: '',
            headers: [ //Value is the key for the items (in html, it'll use this key to know what column data will go in)
                { text: 'Username', align: 'left', value: 'username'},
                { text: 'Role', value: 'roles' },
                { text: 'Created', value: 'created'},
                { text: 'Modified', value: 'modified'},
                { text: 'Active', value: 'active', sortable: false},
            ],
            rows: [] //empty to start
        }
    },
    mounted() { //sends request to server. Puts response into the rows variable
        console.log("mounted");
        this.getUsers();

    },
    methods: {
        polling: function() {
            this.polling = setInterval(() => {
                console.log("polling users");
                this.getUsers();
            }, 5000) // 5 seconds
        },
        getUsers: function() {
            axios.get('/api/user/all').then(response => {
                console.log(response.data[0]);
                this.rows = response.data;
            }).catch(error => {
                console.log(error);
            })
        },

        updateActive: function(user) {
            console.log("clicked: " + user.username);
        },

        isActive: function(user) {
            if (user.active) {
                return 'green';
            } else {
                return 'red';
            }
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
    <template>
    <v-card>
    <v-card-title>All Users<v-spacer></v-spacer>
        <v-text-field
        v-model="search"
        append-icon="search"
        label="Search"
        single-line
        hide-details
        ></v-text-field>
        </v-card-title>
    <v-data-table
        :headers="headers"
        :items="rows"
        :search="search"
        class="elevation-1"
        >
        <template slot="rows" slot-scope="props">
            <td>{{props.row.username}}</td>
            <td>{{props.row.roles}}</td>
            <td>{{props.row.created}}</td>
            <td>{{props.row.modified}}</td>
            <template v-slot:item.active="{ item }">
                <v-btn v-if="item.active == true" color="red" v-on:click="updateActive(item)">Disable</v-btn>
                <v-btn v-if="item.active == false" color="green" v-on:click="updateActive(item)">Enable</v-btn>
            </template>
        </template>
        </v-data-table>
        </v-card>
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