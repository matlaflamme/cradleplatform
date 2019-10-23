Vue.prototype.$http = axios;
Vue.component('new_account_form', {
    vuetify: new Vuetify(),
    props: {

    },
    data: () => ({
        valid: true,
        name: '',
        nameRules: [
            v => !!v || 'Name is required'
        ],
        username: '',
        usernameRules: [
            v => !!v || 'Username is required',
            v => (v && v.length <= 30) || 'Username must be less than 30 characters' //an example to show we can limit characters
        ],
        password: '',
        passwordRules: [
            v => !!v || 'Password is required'
        ],
        region: '',
        regionRules: [
            v => !!v || 'Region is required'
        ],
        zone: '',
        zoneRules: [
            v => !!v || 'Zone is required',
            v => (v && v > 0) || 'Zone number can\'t be negative'
        ],
        row: null
    }),
    methods: {
      validate () {
          if (this.$refs.newAccountForm.validate()) {
              this.snackbar = true;
              this.submit();
          }
      },
        reset () {
          this.$refs.newAccountForm.reset();
        },
        resetValidation () {
          this.$refs.newAccountForm.resetValidation()
        },
        submit () {
          //@TODO

            axios.post('/api/user/add',
                {
                    username: this.username,
                    password: this.password,
                    roles: this.row
                }
            ).then(response => {console.log(response)});
        }
    },
    template:
    '<v-card class="overflow-hidden" raised min-width="550" max-height="600"> ' +
        `<v-card-title>
            <span class="title">Create a new account</span>`+
        '</v-card-title> ' +
    `<v-form
      ref="newAccountForm"
      v-model="valid"
      lazy-validation
      class="ma-5 px-3"
    >` +
        '<v-radio-group v-model="row" row>' +
        '<v-radio label="VHT" value="ROLE_VHT"></v-radio>' +
        '<v-radio label="Health Clinic Worker" value="ROLE_HEALTHWORKER"></v-radio>' +
        '<v-radio label="Admin" value="ROLE_ADMIN"></v-radio>' +
        '</v-radio-group>' +
     ` <v-text-field
        v-model="name"
        :counter="10"
        :rules="nameRules"
        label="Name"
        required
      ></v-text-field>` +
        `<v-text-field
        v-model="username"
        :rules="usernameRules"
        label="Username"
        required
      ></v-text-field>` +
        `<v-text-field
        v-model="password"
        :rules="passwordRules"
        label="Password"
        required
      ></v-text-field>` +
        `<v-text-field
        v-model="region"
        :rules="regionRules"
        label="Region"
        required
      ></v-text-field>` +
        `<v-text-field
        v-model="zone"
        :rules="zoneRules"
        label="Zone"
        required
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
        '</v-card>'

})

new Vue({
    el: '#app',
    vuetify: new Vuetify(),
    data: () => ({
        drawer: null
    })
});