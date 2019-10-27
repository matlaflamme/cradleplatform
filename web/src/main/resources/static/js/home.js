let authInput = new Vue({
    el: '#authInput',
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
        createAccount: function() {
            //links to new account page
        }
    }
});
