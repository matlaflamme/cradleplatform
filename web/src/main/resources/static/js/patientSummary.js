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

Vue.component('basic_info', {
    vuetify: new Vuetify(),
    data: () => ({
        patientInformation: {"id":"","name":"","villageNumber":"","birthYear":0,"sex":0,
            "gestationalAge":0,"medicalHistory":null,"drugHistory":null,"otherSymptoms":null,"pregnant":null,
            "readings":[{"id":0,"patientId":"","systolic":0,"diastolic":0,"heartRate":0,"colour":0,
                "timestamp":""}]}
    }),
    mounted() {
        let urlQuery = new URLSearchParams(location.search);
        let id = urlQuery.get('id');
        axios.get('/api/patient/' + id).then(response => {
            this.patientInformation = response.data;
        });
    },
    template:
        '<div>' +
            '<v-row>' +
                '<v-col md="4" cols="12">' +
                    '<strong class="font-weight-thin display-2">Patient Details</strong>' +
                '</v-col>' +
                '<v-col md="3" cols="12" id="header">' +
                    '<span id="header-content" class="headline">ID: <span class="font-weight-light">{{patientInformation.id}}</span></span>' +
                '</v-col>' +
                '<v-col md="3" cols="12" id="header">' +
                    '<span id="header-content" class="headline">Initials: <span class="font-weight-light">{{patientInformation.name}}</span></span>' +
                '</v-col>' +
                '<v-col md="2" cols="12" id="header">' +
                    '<span id="header-content" class="headline">Birth Year: <span class="font-weight-light">{{patientInformation.birthYear}}</span></span>' +
                '</v-col>' +
            '</v-row>' +
        '</div>'
});

// This compnent is for the center of the page, it shows a table of all the patient readings
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
        })

    },
    template:
        `
        <v-data-table
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
        '<div class="mx-auto">' +
            '<v-list-item three-line>\n' +
                '<v-list-item-content>\n' +
                    '<h3 class="font-weight-light">Latest Reading</h3>\n' +
                '</v-list-item-content>' +
            '</v-list-item three-line>' +
            '<img src="/img/cardiology.png" height="50" width="50" style="margin-bottom: 12px; margin-left: 30px">\n' +
            '<p id="heart_beat">{{patientData.readings[0].heartRate}}</p>\n' +
            '<span id="light" ref="light" class="dot"></span>\n' +
            '<img id="arrow" ref="arrow" src="/img/arrow_down.png" height="30" width="20" style="margin-bottom: 12px">\n' +
            '<v-list-item three-line>\n' +
                '<v-list-item-content>\n' +
                    '<p><strong class="font-weight-regular title">Systolic: </strong>{{patientData.readings[0].systolic}}</p>' +
                    '<p><strong class="font-weight-regular title">Diastolic: </strong>{{patientData.readings[0].diastolic}}</p>' +
                    '<p><strong class="font-weight-regular title">Gestational Age: </strong>{{patientData.gestationalAge}} days</p>' +
                '</v-list-item-content>' +
            '</v-list-item>' +
            '<v-list-item three-line>' +
                '<v-list-item-content>' +
                    '<h3 class="font-weight-light">Symptoms</h3>\n' +
                    '<div>Received overcame oh sensible so at an.\n' +
                        'Formed do change merely to county it. Am separate contempt\n' +
                        'domestic to to oh. On relation my so addition branched.</div>\n' +
                '</v-list-item-content>' +
            '</v-list-item>' +
            '<v-list-item three-line>' +
                '<v-list-item-content>' +
                    '<h3 class="font-weight-light">Current Medications</h3>\n' +
                    '<ul className="list-group">\n'+
                        '<li className="list-group-item">item 3 </li>\n'+
                        '<li className="list-group-item">item 2</li>\n'+
                        '<li className="list-group-item">item 3</li>\n'+
                        '<li className="list-group-item">item 4</li>\n'+
                    '</ul>\n'+
                '</v-list-item-content>' +
            '</v-list-item three-line>' +
        '</div>',

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

new Vue({
    el: '#app',
    vuetify: new Vuetify(),
    data: () => ({
        drawer: null
    })
});


