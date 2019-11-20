Vue.prototype.$http = axios;

Vue.component('vht_table' , {
    vuetify: new Vuetify(),
    props: {

    },
    data () {
        return {
            search: '',
            headers: [ //Value is the key for the items (in html, it'll use this key to know what column data will go in)
                { text: 'Username', align: 'left', value: 'username'},
                { text: 'Created', value: 'created'},
                { text: 'Active', value: 'active', sortable: false},
                { text: 'View', value: 'view', sortable: false}
            ],
            rows: [] //empty to start
        }
    },
    mounted() { //sends request to server. Puts response into the rows variable
        this.getUsers();
    },
    methods: {
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
        }
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
                    <tr v-for="(item, index) in props.items" :key="index" v-if="item.roles == 'VHT'">
                        <td>{{item.username}}</td>
                        <td>{{item.created}}</td>
                        <td>{{item.active}}</td>
                        <td>
                            <v-btn outlined color="green" >View Details</v-btn>
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