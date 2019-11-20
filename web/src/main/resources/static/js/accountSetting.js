Vue.prototype.$http = axios;
Vue.component('change_password', {
    vuetify: new Vuetify(),
    props: {

    },
    data: () => ({
        successSnackbar: false,
        failSnackbar: false,
        valid: true,
        oldPass: '',
        showOldPassword: false,
        oldPassRules: [
            v => !!v || 'current password is required'
        ],
        newPass: '',
        showPassword: false,
        newPassRules: [
            v => !!v || 'new password is required',
            v => (v && v.length < 20) || 'new password must be less than 20 characters', //an example to show we can limit characters
            v => (v && v.length >= 6) || 'new password must be at least 6 characters'
        ],
        confirmPass: '',
        showConfirmPassword: false,
    }),
    methods: {
        validate () {
            if (this.newPass != this.confirmPass){
                this.failSnackbar = true;
            }
            if (this.$refs.change_password.validate()) {
                this.submit();
            }
        },
        reset () {
            this.$refs.change_password.reset();
        },
        resetValidation () {
            this.$refs.newPatchange_passwordientForm.resetValidation()
        },
        submit () {
            console.log(this);
            axios.post('/api/user/',
                {


                }
            ).then(response => {console.log(response)});
            this.successSnackbar = true; //@TODO handle error messages (call a function, pass response, create snackbar)
        }
    },
    template:
        `
        <div>
            <v-card class="overflow-hidden" raised min-width="550" max-height="600">
                <v-card-title><span class="title">Change Password</span></v-card-title>
                <v-form ref="change_password" v-model="valid" lazy-validation class="ma-5 px-3">
                    <v-text-field v-model="oldPass" label="Current password" :rules="oldPassRules" :append-icon="showOldPassword ? 'visibility' : 'visibility_off'"
                    :type="showOldPassword ? 'text' : 'password'"
                    @click:append="showOldPassword = !showOldPassword"></v-text-field>
                    <v-text-field v-model="newPass" :rules="newPassRules" label="New password" :append-icon="showPassword ? 'visibility' : 'visibility_off'"
                    :type="showPassword ? 'text' : 'password'"
                    @click:append="showPassword = !showPassword"required></v-text-field>
                    <v-text-field v-model="confirmPass" :rules="newPassRules" label="Confirm new password" :append-icon="showConfirmPassword ? 'visibility' : 'visibility_off'"
                    :type="showPassword ? 'text' : 'password'"
                    @click:append="showConfirmPassword = !showConfirmPassword"required></v-text-field>
                    <v-btn :disabled="!valid" color="success" class="mr-4" @click="validate">Change Password</v-btn>
                    <v-btn color="error" class="mr-4" @click="reset"> Clear Form</v-btn>
                </v-form>
            </v-card>
            <v-snackbar v-model="successSnackbar">Password successfully changed.
                <v-btn color="green" @click="successSnackbar = false">Close</v-btn>
            </v-snackbar>
            <v-snackbar v-model="failSnackbar">The values in the new password and the confirm password don't match.
                <v-btn color="red" @click="failSnackbar = false">Close</v-btn>
            </v-snackbar>
        </div>`
});

new Vue({
    el: '#app',
    vuetify: new Vuetify(),
    data: () => ({
        drawer: null
    })
});
