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
            rows: [] //empty to start+
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
                console.log(response.data[0].roles.replace("ROLE_", ""));
                this.rows = response.data;
                for (let i = 0; i < this.rows.length; i++) {
                    this.rows[i].roles = this.rows[i].roles.replace("ROLE_", "");
                }
            }).catch(error => {
                console.log(error);
            })
        },

        updateActive: function(user) {
            let endpoint = "/api/user/" + user.username + "/change-active";
            axios.post(endpoint).then(response => {
                // update user list
                // TODO: rather than all the users, we should just retrieve this user and then modify our front end array "rows"
                this.getUsers();
                console.log("success swapped: " + user.username);
            }).catch(error => {
                console.log(error);
            })
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
        class="elevation-1">
            <template v-slot:body="props">
                <tbody>
                    <tr v-for="(item, index) in props.items" :key="index">
                        <td>{{item.username}}</td>
                        <td>{{item.roles}}</td>
                        <td>{{item.created}}</td>
                        <td>{{item.modified}}</td>
                        <td>
                            <v-btn v-if="item.active == false" color="green" v-on:click="updateActive(item)" >Enable</v-btn>
                            <v-btn v-if="item.active == true" color="red" v-on:click="updateActive(item)" >Disable</v-btn>
                        </td>
                    </tr>
                </tbody>
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