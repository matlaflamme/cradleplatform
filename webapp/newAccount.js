let newAccount = new Vue({
    el: '#newAccount',
    data: {
        picked: ''
    },
    methods: {
        changeSelection: function() {
            console.log(this.picked);
            if (this.picked === "worker") {
                newWorker.workerForm = true;
                newClinic.clinicForm = false;
            }
            else if (this.picked === "clinic") {
                newWorker.workerForm = false;
                newClinic.clinicForm = true;
            }
        }
    }
});

let newWorker = new Vue({
    el: '#newWorker',
    data: {
        workerForm : false,
        firstName : '',
        lastName : '',
        idNumber : '',
        phoneNumber : '',
    },
    methods: {
        submitForm() {
            //Create an account
        }

    }
});

let newClinic = new Vue({
    el: '#newClinic',
    data: {
        clinicForm : false,
        clinicName : '',
        clinicLocation : '',
        clinicPhoneNum : ''
    },
    methods: {
        submitForm() {
            //create a new health clinic
        }
    }
});