Vue.prototype.$http = axios;

let test = new Vue({
    el: '#app',
    vuetify: new Vuetify(),
    data () {
        return {
            headers: [ //Value is the key for the items (in html, it'll use this key to know what column data will go in)
                { text: 'Health Centre', align: 'left', value: 'name'},
                { text: 'Location', align: 'left', value: 'location'},
                { text: 'Phone Number', align: 'left', value: 'phoneNumber'},
                { text: 'Email', align: 'left', value: 'email'},
            ],
            cards: [] //empty to start
        }

    },
    mounted() { //sends request to server. Puts response into the rows variable
        console.log("mounted");
        this.update();
    },
    methods: {
        update: function() {
            axios.get('/api/hc/all').then(response => {
                console.log(response);
                this.cards = response.data;
            }).catch(error => {
                console.log(error);
            })
        }
    },
});