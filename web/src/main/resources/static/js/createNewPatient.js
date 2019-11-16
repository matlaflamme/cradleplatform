Vue.prototype.$http = axios;
Vue.component('new_patient_form', {
    vuetify: new Vuetify(),
    props: {

    },
    data: () => ({
        // id, village, zone, initials, age, sex
        snackbar: false,
        valid: true,
        id: '',
        idRules: [
            v => !!v || 'ID is required'
        ],
        initials: '',
        initialsRules: [
            v => !!v || 'Initials is required',
            v => (v && v.length < 4) || 'Initials must be less than 4 characters', //an example to show we can limit characters
            v => (v && v.length >= 2) || 'Initials must be at least 2 characters'
        ],
        village: '',
        villageRules: [
            v => !!v || 'village is required'
        ],
        zone: '',
        zoneRules: [
            v => !!v || 'Zone is required',
        ],
        select: {},
        items: [
            { sex: 'Male', value: 0 },
            { sex: 'Female', value: 1 },
            { sex: 'Other', value: 2 },
        ],
        sexRules: [
            v => !!v || 'sex is required',
        ],
        birthYear: '',
        birthYearRules: [
            v => !!v || 'Birth year is required'
        ]
    }),
    methods: {
        validate () {
            if (this.$refs.newPatientForm.validate()) {
                this.submit();
            }
        },
        reset () {
            this.$refs.newPatientForm.reset();
        },
        resetValidation () {
            this.$refs.newPatientForm.resetValidation()
        },
        submit () {
            axios.post('/api/patient',
                {
                    id: "090",
                    name: "pn",
                    villageNumber: "89",
                    birthYear: 1995,
                    sex: 1,
                    medicalHistory: null,
                    drugHistory: null,
                    lastUpdated: "2019-10-20 13:12:72"
                }
            ).then(response => {console.log(response)});
            this.snackbar = true; //@TODO handle error messages (call a function, pass response, create snackbar)
        }
    },
    template:
        '<div>' +
        '<v-card class="overflow-hidden" raised min-width="550" max-height="600"> ' +
        `<v-card-title>
            <span class="title">Create a new patient</span>` +
        '</v-card-title> ' +
        `<v-form
      ref="newPatientForm"
      v-model="valid"
      lazy-validation
      class="ma-5 px-3"
    >` +
        ` <v-text-field
        v-model="id"
        :counter="10"
        label="ID"
      ></v-text-field>` +
        `<v-text-field
        v-model="initials"
        :rules="initialsRules"
        label="Initials"
        required
      ></v-text-field>` +
        `<v-text-field
        v-model="village"
        label="Village"
        label="Village"
        :rules="villageRules"
      ></v-text-field>` +
        `<v-text-field
        v-model="zone"
        label="Zone"
        :rules="zoneRules"
      ></v-text-field>` +
        `
      <v-select
            v-model="select"
            :items="items"
            item-text="sex"
            item-value="value"
            label="Sex"
            persistent-hint
            return-object
            single-line
          ></v-select>` +
        `<v-text-field
        v-model="birthYear"
        label="Birth Year"
        :rules="birthYearRules"
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
        'New patient successfully created' +
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
