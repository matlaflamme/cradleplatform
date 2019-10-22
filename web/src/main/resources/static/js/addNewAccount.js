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
        ]
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
          console.log("submit to server")
        }
    },
    template:
    `<v-form
      ref="newAccountForm"
      v-model="valid"
      lazy-validation
    >` +
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
    </v-form>`

})

new Vue({
    el: '#app',
    vuetify: new Vuetify(),
    data: () => ({
        drawer: null
    })
});