Vue.prototype.$http = axios;
Vue.component('new_hc_form', {
    vuetify: new Vuetify(),
    props: {
email: String, managerPhoneNumber: String
    },
    data: () => ({
        // id, village, zone, initials, age, sex
        snackbar: false,
        valid: true,
        phoneNumber: '',
        phoneNumberRules: [
            v => !!v || 'Phone number is required'
        ],
        name: '',
        nameRules: [
            v => !!v || 'Name is required',
        ],
        location: '',
        locationRules: [
            v => !!v || 'Location is required'
        ],
        email: '',
        emailRules: [
            v => !!v || 'E-mail is required'
        ],
        managerPhoneNumber: '',
        managerPhoneNumberRules: [
            v => !!v || 'Manager phone number is required'
        ],

    }),
    methods: {
        validate () {
            if (this.$refs.newHCForm.validate()) {
                this.submit();
            }
        },
        reset () {
            this.$refs.newHCForm.reset();
        },
        resetValidation () {
            this.$refs.newHCForm.resetValidation()
        },
        submit () {
            axios.post('/api/hc/create',
                {
                    phoneNumber: this.phoneNumber,
                    email: this.email,
                    managerPhoneNumber: this.managerPhoneNumber,
                    location: this.location,
                    name: this.name
                }
            ).then(response => {console.log(response)});
            this.snackbar = true; //@TODO handle error messages (call a function, pass response, create snackbar)
        }
    },
    template:
        '<div>' +
        '<v-card class="overflow-hidden" raised min-width="550" max-height="600"> ' +
        `<v-card-title>
            <span class="title">Create a new health center</span>` +
        '</v-card-title> ' +
        `<v-form
      ref="newHCForm"
      v-model="valid"
      lazy-validation
      class="ma-5 px-3"
    >` +
        ` <v-text-field
        v-model="phoneNumber"
        label="Phone Number"
        :rules="phoneNumberRules"
      ></v-text-field>` +
        `<v-text-field
        v-model="name"
        :rules="nameRules"
        label="Health center name"
        required
      ></v-text-field>` +
        `<v-text-field
        v-model="location"
        label="Location"
        :rules="locationRules"
      ></v-text-field>` +
        `<v-text-field
        v-model="email"
        label="E-mail"
        :rules="emailRules"
      ></v-text-field>` +
        `<v-text-field
        v-model="managerPhoneNumber"
        label="Manager Phone Number"
        :rules="managerPhoneNumberRules"
      ></v-text-field>` +
        `<v-btn
        :disabled="!valid"
        color="success"
        class="mr-4"
        @click="validate"
      >
        Submit
      </v-btn>` +

        `<v-btn
        color="error"
        class="mr-4"
        @click="reset"
      >
        Clear Form
      </v-btn>
    </v-form>` +
        '</v-card>' +
        '<v-snackbar v-model="snackbar">' +
        'New health center successfully created' +
        `<v-btn
                color="green"
                @click="snackbar = false"
            >` +
        'Close' +
        '</v-btn>' +
        '</v-snackbar>' +
        '</div>'
})

new Vue({
    el: '#app',
    vuetify: new Vuetify(),
    data: () => ({
        drawer: null
    })
});
