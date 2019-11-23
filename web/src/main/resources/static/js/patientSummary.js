Vue.prototype.$http = axios;

function getReadingColorIcon(digit){
    let light = 'white';
    switch (digit) {
        case null:
            light = 'white';
            break;
        case 0:
            light = 'green';
            break;
        case 1:
            light = 'yellow_down';
            break;
        case 2:
            light = 'yellow_up';
            break;
        case 3:
            light = 'red_down';
            break;
        case 4:
            light = 'red_up';
            break;
    }
    return "/img/" + light + ".png";
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
            this.getSexValueAsString();
        });
    },
    methods: {
        getSexValueAsString() {
            if (this.patientInformation.sex === 0) {
                this.patientInformation.sex = "Male";
            }
            else {
                this.patientInformation.sex = "Female";
            }
        }
    },
    template:
        '<div>' +
            '<v-row>' +
                '<v-col md="4" cols="12">' +
                    '<strong class="font-weight-thin display-2">Patient Details</strong>' +
                '</v-col>' +
                '<v-col md="2" cols="12" id="header">' +
                    '<span id="header-content" class="headline">ID: <span class="font-weight-light">{{patientInformation.id}}</span></span>' +
                '</v-col>' +
                '<v-col md="2" cols="12" id="header">' +
                    '<span id="header-content" class="headline">Initials: <span class="font-weight-light">{{patientInformation.name}}</span></span>' +
                '</v-col>' +
                '<v-col md="2" cols="12" id="header">' +
                    '<span id="header-content" class="headline">Sex: <span class="font-weight-light">{{patientInformation.sex}}</span></span>' +
                '</v-col>' +
                '<v-col md="2" cols="12" id="header">' +
                    '<span id="header-content" class="headline">Birth Year: <span class="font-weight-light">{{patientInformation.birthYear}}</span></span>' +
                '</v-col>' +
            '</v-row>' +
        '</div>'
});

// This compnent is for the right of the page, it shows a table of all the patient readings
Vue.component('readings_table' , { //Ideally, the graph would be in a separate component, but that was not working properly
    vuetify: new Vuetify(),
    props: {

    },
    data: () => ({
        headers: [ //Value is the key for the items (in html, it'll use this key to know what column data will go in)
            { text: 'Reading Date', value: 'timestamp'},
            { text: 'Systolic', value: 'systolic' },
            { text: 'Diastolic', value: 'diastolic'},
            { text: 'Heart Rate', value: 'heartRate'},
            { text: 'Traffic Light', value: 'colour'}
        ],
        rows: [], //empty to start
        expanded: [],
        graphSystolic: [],
        graphDiastolic: [],
        graphHeartRate: [],
        labels: [], //labels must be in the same order as graphData
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
        },
        calcGraphData(readings) {
            console.log(readings);
            for (let i = 0; i < readings.length && i < 5; i++ ) {
                this.graphSystolic.push(readings[i].systolic);
                this.graphDiastolic.push(readings[i].diastolic);
                this.graphHeartRate.push(readings[i].heartRate);
                this.labels.push(readings[i].timestamp)
            }
        },
        getSeries() {
            return [
                {name: "Systolic", data: this.graphSystolic},
                {name: "Diastolic", data: this.graphDiastolic},
                {name: "Heart Rate", data: this.graphHeartRate}
            ];
        },
        getChartOptions() {
            return {
                chart: {
                    shadow: {
                        enabled: true,
                        color: '#000',
                        top: 18,
                        left: 7,
                        blur: 10,
                        opacity: 1
                    },
                    toolbar: {
                        show: false
                    }
                },
                colors: [ '#10a892' ,'#77B6EA', '#545454'],
                dataLabels: {
                    enabled: true,
                },
                stroke: {
                    curve: 'smooth'
                },
                title: {
                    text: 'Blood pressure and heart rate over time',
                    align: 'left'
                },
                grid: {
                    borderColor: '#e7e7e7',
                    row: {
                        colors: ['#f3f3f3', 'transparent'], // takes an array which will be repeated on columns
                        opacity: 0.1
                    },
                },
                markers: {

                    size: 6
                },
                xaxis: {
                    categories: this.labels,
                    title: {
                        text: 'Date of reading'
                    }
                },
                legend: {
                    position: 'top',
                    horizontalAlign: 'right',
                    floating: true,
                    offsetY: -25,
                    offsetX: -5
                }
            }
        }
    },
    components: {
        apexchart: VueApexCharts,
    },
    created() {
        let urlQuery = new URLSearchParams(location.search); //retrieves everything after the '?' in url
        let id = urlQuery.get('id'); //search for 'id=' in query and return the value
        axios.get('/api/patient/'+ id + '/readings').then(response => {
            this.rows = response.data;
            this.rows = this.changeDate(response.data);
            this.calcGraphData(this.rows);
            this.rows.forEach((row)=> {
                row.colorstyle = getReadingColorIcon(row.colour);
            })
        });

    },
    template:
    '<div>' +
        '<div>\n' +
            '<apexchart height="350" type="line" :options="getChartOptions()" :series="getSeries()"></apexchart>\n' +
        '</div>' +
        '<v-spacer class="pt-1"></v-spacer>' +
        '<v-data-table' +
        ' :headers="headers"' +
        ' :items="rows"' +
        ' :items-per-page="5"' +
        ' class="elevation-1"' +
        ' :expanded.sync="expanded"' +
        ' show-expand>' +
            '<template v-slot:top>' +
                '<v-toolbar flat color="white">' +
                    '<v-toolbar-title>Past Readings</v-toolbar-title>' +
                '</v-toolbar>' +
            '</template>' +
            '<template slot="rows" slot-scope="props">' +
                '<td>{{props.row.timestamp}}</td>' +
                '<td>{{props.row.systolic}}</td>' +
                '<td>{{props.row.diastolic}}</td>' +
                '<td>{{props.row.heartRate}}</td>' +
                '<td>{{props.row.colour}}</td>' +
            '</template>' +
            '<template v-slot:item.colour="{ item }">' +
                '<td><img id="light" ref="light" :src=item.colorstyle height="50" width="60" style="margin-bottom: 12px"></td>' +
            '</template>' +
            '<template v-slot:expanded-item="{ headers, item }">' + //this section is for what shows in the expanded row
                '<td :colspan="headers.length">' +
                    '<v-list-item>' +
                        '<v-list-item-content>' +
                            '<ul >\n'+
                                '<li >Gestational Age: {{item.gestationalAge}}</li>\n'+
                                '<li>Symptoms'+
                                    '<ul v-for="symptom in item.symptoms">\n'+
                                        '<li>{{symptom}}</li>\n'+
                                    '</ul>\n'+
                                '</li>' +
                                '<li>Other Notes: {{item.readingNotes}}</li>\n'+
                            '</ul>\n' +
                        '</v-list-item-content>' +
                    '</v-list-item>' +
                '</td>'+
            '</template>' +
        '</v-data-table>' +
        // '<v-spacer class="pt-5"></v-spacer>' +
        //'<info_graph :systolicData="graphSystolic" :diastolicData="graphDiastolic" :heartRateData="graphHeartRate" :labels="labels"></info_graph>' +
        // '<div>\n' +
        // '<apexchart height="350" type="line" :options="getChartOptions()" :series="getSeries()"></apexchart>\n' +
        // '</div>' +
    '</div>'
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
                    "gestationalAge":0,"medicalHistory":null,"drugHistory":null,"symptoms":[],"pregnant":null,
                    "readings":[{"id":0,"patientId":"","systolic":0,"diastolic":0,"heartRate":0,"colour":0,
                        "timestamp":""}]}, //data in the form of json string
            hasSymptoms: false,
            symptoms: [],
            takingMedication: false,
            medications: [],
            pregnant: false,
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
            '<p id="heart_beat" class="title">{{patientData.readings[0].heartRate}}</p>\n' +
            '<img id="light" ref="light" :src= this.light height="50" width="60" style="margin-bottom: 12px">\n' +
            '<v-list-item three-line>\n' +
                '<v-list-item-content>\n' +
                    '<p><strong class="font-weight-regular title">Systolic: </strong>{{patientData.readings[0].systolic}}</p>' +
                    '<p><strong class="font-weight-regular title">Diastolic: </strong>{{patientData.readings[0].diastolic}}</p>' +
                    '<p v-if="pregnant"><strong class="font-weight-regular title">Gestational Age: </strong>{{patientData.readings[0].gestationalAge}} weeks</p>' +
                '</v-list-item-content>' +
            '</v-list-item>' +
            '<v-list-item three-line>' +
                '<v-list-item-content>' +
                    '<h3 class="font-weight-light pb-5">Symptoms</h3>\n' +
                    '<ul v-if="hasSymptoms" className="list-group" v-for="symptom in symptoms">\n'+
                        '<li className="list-group-item" class="pb-1">{{symptom}}</li>\n'+
                    '</ul>\n'+
                    '<ul v-if="!hasSymptoms" className="list-group">' +
                        '<li className="list-group-item" class="pb-1">No symptoms recorded</li>' +
                    '</ul>' +
                '</v-list-item-content>' +
            '</v-list-item>' +
            '<v-list-item three-line>' +
                '<v-list-item-content>' +
                    '<h3 class="font-weight-light pb-5">Medications </h3>\n' +
                    '<ul v-if="takingMedication" className="list-group" v-for="(medication, index) in medications">\n'+
                        '<li className="list-group-item" class="pb-1">{{medication.medication}}'+
                            '<v-btn class="removebtn" small outlined color="red" @click="deleteMedicine(index)" style=" display: table-cell;">' +
                                '<v-icon>delete</v-icon>' +
                            '</v-btn>' +
                            '<ul>\n' +
                                '<li className="list-group-item" class="pb-1">Dosage: {{medication.dosage}}\n</li>' +
                                '<li className="list-group-item" class="pb-1">Frequency: {{medication.usageFrequency}}\n</li>\n' +
                            '</ul>' +

                        '</li>' +
                    '</ul>\n'+
                    '<ul v-if="!takingMedication" className="list-group">' +
                        '<li className="list-group-item" class="pb-1">Not currently taking any medications</li>' +
                    '</ul>' +
                '</v-list-item-content>' +
            '</v-list-item>' +
        '</div>',

    mounted() {
        let urlQuery = new URLSearchParams(location.search);
        let id = urlQuery.get('id');
        axios.get('/api/patient/' + id).then(response => {
            this.patientData = response.data;
            console.log(response.data);
            this.light = getReadingColorIcon(response.data.readings[0].colour);
            this.checkSymptoms();
            this.checkMedications();
            this.checkPregnant();
        });

    },
    methods: {
        checkSymptoms() {
            //this.patientData.readings[0].symptoms = ['Headache', 'Feverish', 'Blurred Vision']; //For testing only
            if (this.patientData.readings[0].symptoms.length !== 0) {
                this.symptoms = this.patientData.readings[0].symptoms;
                this.hasSymptoms = true;
           }
        },
        checkMedications() {
            // if (this.patientData.drugHistory !== null) {
                this.medications= [{patientId:"001",medId:7,medication:"Tylenol",dosage:"1 Extra Strength Pill",usageFrequency:"Morning and Evening"}];
                // this.medications = this.patientData.drugHistory;
                this.takingMedication = true;
            // }
        },
        checkPregnant() {
            //this.patientData.readings[0].gestationalAge = 130; //For testing only
            if (this.patientData.readings[0].gestationalAge) {
                this.pregnant = true;
                this.patientData.readings[0].gestationalAge = Math.round(this.patientData.readings[0].gestationalAge / 7);
            }
        },
        getPatientSex: function(sexVal) {
            if (sexVal === 1) {
                return "Female";
            }
            else {
                return "Male";
            }
        },
        deleteMedicine: function(index) {
            this.medications.splice(index,1)

        }
    }
});

Vue.component('info_graph', { //Ideally would use this component instead of what is above. (time permitting)
    props: {
        systolicData: Array,
        diastolicData: Array,
        heartRateData: Array,
        labels: Array
    },
    template:
        '<div>\n' +
            '<apexchart height="350" type="line" :options="chartOptions" :series="series"></apexchart>\n' +
        '</div>',
    components: {
        apexchart: VueApexCharts,
    },
    data: function() {
        return {
            series: [
                {name: 'Systolic', data: this.systolicData},
                {name: 'Diastolic', data: this.diastolicData},
                {name: 'Heart Rate', data: this.heartRateData}
            ],
            chartOptions: {
                chart: {
                    shadow: {
                        enabled: true,
                        color: '#000',
                        top: 18,
                        left: 7,
                        blur: 10,
                        opacity: 1
                    },
                    toolbar: {
                        show: false
                    }
                },
                colors: [ '#10a892' ,'#77B6EA', '#545454'],
                dataLabels: {
                    enabled: true,
                },
                stroke: {
                    curve: 'smooth'
                },
                title: {
                    text: 'Blood pressure and heart rate over time',
                    align: 'left'
                },
                grid: {
                    borderColor: '#e7e7e7',
                    row: {
                        colors: ['#f3f3f3', 'transparent'], // takes an array which will be repeated on columns
                        opacity: 0.1
                    },
                },
                markers: {

                    size: 6
                },
                xaxis: {
                    categories: this.labels,
                    title: {
                        text: 'Date of reading'
                    }
                },
             //   yaxis: {
             //       title: {
             //           text: 'Temperature'
             //        },
             //        min: 5,
             //        max: 40
             //    },
                legend: {
                    position: 'top',
                    horizontalAlign: 'right',
                    floating: true,
                    offsetY: -25,
                    offsetX: -5
                }
            }
        }
    },
    mounted() { //using created so that data is ready for chart on page load (animation works)
        //this.chartOptions.xaxis.categories = this.labels;
        // this.series = [
        //     {name: "Systolic", data: this.systolicData},
        //     {name: "Diastolic", data: this.diastolicData},
        //     {name: "Heart Rate", data: this.heartRateData}
        // ];
    }
});

new Vue({
    el: '#app',
    vuetify: new Vuetify(),
    data: () => ({
        drawer: null
    })
});


