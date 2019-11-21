Vue.prototype.$http = axios;
let test = new Vue({
    el: '#app',
    vuetify: new Vuetify(),
    data: () => ({
        drawer: null
    })
});
