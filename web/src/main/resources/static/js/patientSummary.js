Vue.prototype.$http = axios;

function getReadingColorIcon(digit) {
    let colour = 'green';
    let arrow = null;
    switch (digit) {
        case 0:
            colour = 'green';
            break;
        case 1:
            colour = 'yellow';
            arrow = '/img/arrow_down.png';
            break;
        case 2:
            colour = 'yellow';
            arrow = '/img/arrow_up.png';
            break;
        case 3:
            colour = 'red';
            arrow = '/img/arrow_down.png';
            break;
        case 4:
            colour = 'red';
            arrow = '/img/arrow_up.png';
            break;
    }
    return { 'colour' : colour, 'arrow' : arrow };
};

Vue.component('readings_table' , {
    vuetify: new Vuetify(),
    props: {

    },
    data: () => ({
        headers: [ //Value is the key for the items (in html, it'll use this key to know what column data will go in)
            { text: 'Referral Date', value: 'timestamp'},
            { text: 'Systolic', value: 'systolic' },
            { text: 'Diastolic', value: 'diastolic'},
            { text: 'Heart Rate', value: 'heartRate'},
            { text: 'Traffic Light', value: 'colour'}
        ],
        rows: [] //empty to start

    }),
    methods: {
        changeDate: function(patientData) { //Changes date format to be more readable
            const months = ["Jan", "Feb", "Mar", "Apr", "May", "June", "July", "Aug", "Sep", "Oct", "Nov", "Dec"];
            patientData.forEach(pData => {
                let currDate = new Date(pData.timestamp);
                let formatted_date = months[currDate.getMonth()] + " " + currDate.getDate() + ", " + currDate.getFullYear();
                pData.timestamp = formatted_date;
            });
            return patientData;
        }

    },
    mounted() {
        let urlQuery = new URLSearchParams(location.search); //retrieves everything after the '?' in url
        let id = urlQuery.get('id'); //search for 'id=' in query and return the value
        axios.get('/api/patient/'+ id + '/readings').then(response => {
            this.rows = response.data;
            this.rows = this.changeDate(response.data);
            this.rows.forEach((row)=> {
                let icon = getReadingColorIcon(row.colour);
                row.colorstyle = {"background-color": icon['colour']};
            })
            console.log(this.rows[0].id);
            console.log(response.data);

        })

    },
    template:
        `<v-data-table
        :headers="headers"
        :items="rows"
        :items-per-page="5"
        class="elevation-1"
        >
        <template slot="rows" slot-scope="props">
            <td>{{props.row.timestamp}}</td>
            <td>{{props.row.systolic}}</td>
            <td>{{props.row.diastolic}}</td>
            <td>{{props.row.heartRate}}</td>
            <td>{{props.row.colour}}</td>
        </template>
        <template v-slot:item.colour="{ item }">
            <td><span class="dot" :style="item.colorstyle"></span></td>
        </template>
        </v-data-table>`
    ,
});

// //This component is used for the center column "Past Readings" table of this page
// Vue.component('patient_readings', {
//     vuetify: new Vuetify(),
//     data: function() {
//         return { rows: null//Request data from the server from this function. Get a JSON file as a response?
//             //[{ date: '2/11/1995', time: '14:33', bp: '100/88', heartRate: '80', light: 'Green'}]
//         }
//     },
//     template:
//         '<v-card\n' +
//         'class="mx-auto"\n' +
//         'max-width="400"\n' +
//         'raised\n' +
//         '>\n' +
//         '<h3>Past Readings</h3>\n'+
//         '<table class="table table-striped table-hover">\n' +
//         '<thead>' +
//         '<tr>' +
//         '<th>Date</th>' +
//         '<th>Systolic</th>' +
//         '<th>Diastolic</th>' +
//         '<th>Heart Rate</th>' +
//         '<th>Light</th>' +
//         '</tr>' +
//         '</thead>' +
//         '<tbody>' +
//         '<tr v-for="row in rows">' +
//         '<td>{{row.timestamp}}</td>' +
//         '<td>{{row.systolic}}</td>' +
//         '<td>{{row.systolic}}</td>' +
//         '<td>{{row.heartRate}}</td>' +
//         '<td><span class="dot" :style="row.colorstyle"></span></td>' +
//         '</tr>' +
//         '</tbody>' +
//         '</table>' +
//         '</v-card>\n',
//     mounted() {
//         let urlQuery = new URLSearchParams(location.search); //retrieves everything after the '?' in url
//         let id = urlQuery.get('id'); //search for 'id=' in query and return the value
//         axios.get('/api/patient/'+ id + '/readings').then(response => {
//             this.rows = response.data
//             this.rows.forEach((row)=> {
//                 let icon = getReadingColorIcon(row.colour);
//                 row.colorstyle = {"background-color": icon['colour']};
//             })
//         })
//     }
// });


// This component is for the left side of the page, it reads the patient info and gives a summary on the patient info and last reading
Vue.component('patient_info', {
    props: {
        title: String, //Usage: {{title}} inside of the template is replaced with the prop
        //Prop is given a value inside of the component's html tag in the html file where its used
    },
    data: function() {
        return {
            patientData: //starter data so that "patientData" is not null when the page is loaded
                {"id":"","name":"","villageNumber":"","birthYear":0,"sex":0,
                    "gestationalAge":0,"medicalHistory":null,"drugHistory":null,"otherSymptoms":null,"pregnant":null,
                    "readings":[{"id":0,"patientId":"","systolic":0,"diastolic":0,"heartRate":0,"colour":0,
                        "timestamp":""}]} //data in the form of json string
        }
    },
    template:
        '<v-card\n' +
        'class="mx-auto"\n' +
        'max-width="400"\n' +
        'raised\n' +
        '>\n' +
        '    <v-list-item three-line>\n' +
        '        <v-list-item-content>\n' +
        '       <h3>Patient Information</h3>\n' +
        '       <p><strong>Patient Name: </strong>{{patientData.name}}</p>' +
        '       <p><strong>ID: </strong>{{patientData.id}}</p>' +
        '       <p><strong>Date Of Birth: </strong>{{patientData.birthYear}}</p>' +
        '       <p><strong>Sex: </strong>{{getPatientSex(patientData.sex)}}</p>' +
        '       <h3>Latest Reading</h3>\n' +
        '        </v-list-item-content>' +
        '    </v-list-item three-line>' +
        '    <img src="/img/cardiology.png" height="50" width="50" style="margin-bottom: 12px; margin-left: 30px">\n' +
        '    <p id="heart_beat">{{patientData.readings[0].heartRate}}</p>\n' +
        '    <span id="light" ref="light" class="dot"></span>\n' +
        '    <img id="arrow" ref="arrow" src="/img/arrow_down.png" height="30" width="20" style="margin-bottom: 12px">\n' +
        '    <v-list-item three-line>\n' +
        '        <v-list-item-content>\n' +
        '       <p><strong>Systolic: </strong>{{patientData.readings[0].systolic}}</p>' +
        '       <p><strong>Diastolic: </strong>{{patientData.readings[0].diastolic}}</p>' +
        '       <p><strong>Gestational Age: </strong>{{patientData.gestationalAge}} days</p>' +
        '         <h3>Symptoms</h3>\n' +
        '        <div>Received overcame oh sensible so at an.\n' +
        '            Formed do change merely to county it. Am separate contempt\n' +
        '            domestic to to oh. On relation my so addition branched.</div>\n' +
        '        <h3>Current Medications</h3>\n' +
        '        <ul className="list-group">\n'+
        '        <li className="list-group-item">item 3 </li>\n'+
        '        <li className="list-group-item">item 2</li>\n'+
        '        <li className="list-group-item">item 3</li>\n'+
        '        <li className="list-group-item">item 4</li>\n'+
        '        </ul>\n'+
        '        </v-list-item-content>' +
        '    </v-list-item three-line>' +
        '</v-card>\n',

    mounted() {
        let urlQuery = new URLSearchParams(location.search);
        let id = urlQuery.get('id');
        axios.get('/api/patient/' + id).then(response => {
            this.patientData = response.data;
            this.setLight(response.data) //update light colour based on response from get request
        });

    },
    methods: {
        getPatientSex: function(sexVal) {
            if (sexVal === 1) {
                return "Female";
            }
            else {
                return "Male";
            }
        },
        setLight(pData) {
            let icon = getReadingColorIcon(pData.readings[0].colour);
            this.$refs.light.setAttribute("style", "background-color:" +  icon['colour'] + ";");

            if (icon['arrow'] == null) {
                this.$refs.arrow.hidden = true;
            }
            else {
                this.$refs.arrow.src = icon['arrow'];
            }
        }
    }

});

//This component is for the right side of the page which gives a medical history of the patient
Vue.component('patient_history', {
    props: {
        title: String, //Usage: {{title}} inside of the template is replaced with the prop
        //Prop is given a value inside of the component's html tag in the html file where its used
    },
    template:
        '<v-card\n' +
        'class="mx-auto"\n' +
        'max-width="400"\n' +
        'raised\n' +
        '>\n' +
            '<v-list-item three-line>\n' +
            '<v-list-item-content>\n' +
            '<h3>Past Medications</h3>\n' +
                '<ul id="pastMedication">\n' +
                    '<li >item 1 </li>\n' +
                    '<li >item 2</li>\n' +
                    '<li >item 3</li>\n' +
                    '<li >item 4</li>\n'+
                '</ul>\n' +
                '<h3>Past Symptoms</h3>\n' +
                '<ul id="pastSymptoms">\n'+
                    '<li >item 1 </li>\n'+
                    '<li >item 2</li>\n'+
                    '<li >item 3</li>\n'+
                    '<li >item 4</li>\n'+
                '</ul>\n'+
                '<h3>Medical History</h3>\n'+
                '<div>Received overcame oh sensible so at an.\n' +
                'Formed do change merely to county it. Am separate contempt\n' +
                'domestic to to oh. On relation my so addition branched.</div>\n' +
                '</v-list-item-content>' +
                '</v-list-item three-line>' +
            '</v-card>\n',


});

new Vue({
    el: '#app',
    vuetify: new Vuetify(),
    data: () => ({
        drawer: null
    })
});


