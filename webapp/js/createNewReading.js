let readingInput = new Vue ({
    el: '#readingInput',
    data: {
        heartRatio: '',
        bloodPressure: '',
        symptoms: '',
        diagnosis: '',
        medication: '',
        medStart: '',
        medEnd: ''
    },
    methods: {
        submitReading: function() {
            //Store into database (POST)
        }
    }
});