Vue.component('login' , {
    data: {
        username : '',
        password : ''
    },
    methods: {
        submitInput: function() {
            //signs in
            console.log(this.username);
            console.log(this.password);
        },
    }
});
