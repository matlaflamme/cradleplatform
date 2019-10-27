Vue.prototype.$http = axios;
let test = new Vue({
    el: '#app',
    vuetify: new Vuetify(),
    data () {
        return {
            headers: [ //Value is the key for the items (in html, it'll use this key to know what column data will go in)
                { text: 'Patient ID', align: 'left', value: 'patient.id'},
                { text: 'Initials', value: 'patient.name' },
                { text: 'Referral Date', value: 'reading.timestamp'},
                { text: 'Traffic Light', value: 'colour'},
                { text: 'View', value: 'view', sortable: false },
                { text: 'Add', value: 'add', sortable: false },
            ],
            rows: [] //empty to start
        }
    },
    mounted() { //sends request to server. Puts response into the rows variable

        axios.get('/api/patient/all_with_latest_reading').then(response => {
            try {
                this.rows = response.data;
                this.rows.forEach((row)=> {
                    if (row.reading == null) {
                        row.reading = {colour: null, timestamp: null};
                    }
                    let icon = getReadingColorIcon(row.reading.colour);
                    row.reading.colorstyle = {"background-color": icon['colour']};
                });
                this.rows = this.changeDate(response.data);
            }
            catch (err) {
                // unexpected response
                console.error(err);
            }
        })
    },
    methods: {
        viewPatientData: function (id) {  //Gets called when View button is pressed
            console.log("View patient:"+id+" details");
            window.location.assign("/patientSummary?id=" + id);
        },
        addNewReading: function(id) {
            console.log("Add a new reading for patient:"+id);
            window.location.assign("/createNewReading?id=" + id);
        },
        changeDate: function(patientData) { //Changes date format to be more readable
            const months = ["Jan", "Feb", "Mar", "Apr", "May", "June", "July", "Aug", "Sep", "Oct", "Nov", "Dec"];
            patientData.forEach(pData => {
                if (pData.reading.timestamp == null) {
                    pData.reading.timestamp = "N/A"
                } else {
                    let currDate = new Date(pData.reading.timestamp);
                    let formatted_date = months[currDate.getMonth()] + " " + currDate.getDate() + ", " + currDate.getFullYear();
                    pData.reading.timestamp = formatted_date;
                }
            });
            return patientData;
        }
    }
});

//From patientSummary page. Move into common js file to be reused?
function getReadingColorIcon(digit) {
    let colour = 'green';
    switch (digit) {
        case null:
            colour = 'white';
            break;
        case 0:
            colour = 'green';
            break;
        case 1:
            colour = 'yellow';
            break;
        case 2:
            colour = 'yellow';
            break;
        case 3:
            colour = 'red';
            break;
        case 4:
            colour = 'red';
            break;
    }
    return {'colour' : colour};
}
