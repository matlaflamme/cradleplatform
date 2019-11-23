Vue.prototype.$http = axios;
Vue.component('new_account_form', {
    vuetify: new Vuetify(),
    props: {

    },
    data: () => ({
		selectedHealthCentre: null,
		healthCentreList: ['Empty'],
        snackbar: false,
        errSnackbar: false,
        valid: true,
        name: '',
        nameRules: [
            v => !!v || 'Name is required'
        ],
        username: '',
        usernameRules: [
            v => !!v || 'Username is required',
            v => (v && v.length <= 25) || 'Username must be less than 30 characters', //an example to show we can limit characters
            v => (v && v.length >= 6) || 'Username must be at least 6 characters'
        ],
        password: '',
        showPassword: false,
        passwordRules: [
            v => !!v || 'Password is required',
            v => (v && v.length >= 8)  || 'Password must be at least 8 characters'
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
        row: '',
        rowRules: [
            v => !!v || 'Role is required'
        ]
    }),
	mounted() {
    	this.getAllHealthCentreOptions();
	},
    methods: {
		validate() {
			if (this.$refs.newAccountForm.validate()) {
				this.submit();
			}
		},
		reset() {
			this.$refs.newAccountForm.reset();
		},
		resetValidation() {
			this.$refs.newAccountForm.resetValidation()
		},
		submit() {
			console.log("selected: " + this.selectedHealthCentre.id);
			axios.post('/api/user/add',
				{
					username: this.username,
					password: this.password,
					roles: this.row,
					worksAtHealthCentreId: this.selectedHealthCentre.id
				}
			).then(response => {
				this.snackbar = true;
				// console.log(response) // this makes the password public
			}).catch(error => {
				this.errSnackbar = true;
			});
		},
		getAllHealthCentreOptions() {
			axios.get('/api/hc/all').then(res => {
				this.healthCentreList = res.data;
			}).catch(error => {
				console.error(error);
			})
		},
    },
    template:
	`
    <div> 
    <v-card class="overflow-hidden" raised min-width="550"> 
        <v-card-title>
            <span class="title">Create a new account</span>
        </v-card-title>
		<v-form
			  ref="newAccountForm"
			  v-model="valid"
			  lazy-validation
			  class="ma-5 px-3">
			<v-radio-group v-model="row" row required :rules="rowRules">
			<v-radio label="VHT" value="ROLE_VHT"></v-radio>
			<v-radio label="Health Clinic Worker" value="ROLE_HEALTHWORKER"></v-radio>
			<v-radio label="Admin" value="ROLE_ADMIN"></v-radio>
			</v-radio-group>
			<v-layout wrap align-center id="new">
				<v-flex xs12 sm6 d-flex>
					<v-select 
						v-model="selectedHealthCentre"
						:items="healthCentreList"
						label="Select Health Centre"
						return-object> 
						<template v-slot:selection="data">
							{{data.item.id}} - {{data.item.name}}
						</template>
						<template v-slot:item="data">
							{{data.item.id}} - {{data.item.name}}
						</template>
					</v-select>
				</v-flex>
		  	</v-layout>
			<v-text-field
				v-model="name"
				:counter="10"
				label="Name">
				
				</v-text-field>
					<v-text-field
						v-model="username"
						:rules="usernameRules"
						label="Username"
						required>
				</v-text-field>
			<v-text-field
				v-model="password"
				:rules="passwordRules"
				label="Password"
				:append-icon="showPassword ? 'visibility' : 'visibility_off'"
				:type="showPassword ? 'text' : 'password'"
				@click:append="showPassword = !showPassword"
				required>
			</v-text-field>
			<v-text-field
				v-model="region"
				label="Region">
			</v-text-field>
			<v-text-field
				v-model="zone"
				label="Zone">	
			</v-text-field>
			<v-btn
				:disabled="!valid"
				color="success"
				class="mr-4"
				@click="validate">
			Submit
			</v-btn>
			<v-btn
				color="error"
				class="mr-4"
				@click="reset">
			Clear Form
			</v-btn>
		</v-form>
    </v-card>
    <v-snackbar v-model="snackbar">
		New user successfully created
            <v-btn
                color="pink"
                @click="snackbar = false"> 
                Close 
            </v-btn>
    </v-snackbar>
    <v-snackbar v-model="errSnackbar">
        Username already exists
			<v-btn
					color="pink"
					@click="snackbar = false">
			Close
			</v-btn>
    </v-snackbar>
    </div>
    `
});

new Vue({
    el: '#app',
    vuetify: new Vuetify(),
    data: () => ({
        drawer: null
    })
});