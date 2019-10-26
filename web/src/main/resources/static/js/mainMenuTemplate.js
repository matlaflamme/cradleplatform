Vue.component('sidebar_template', {
    props: {
        current_user: String
    },
    data: () => ({
        drawer: null
    }),
});

Vue.component('navbar_template', {
    props: {
    },
    methods: {
        changeDrawer: function() {
            this.$emit('drawer')
        }
    }
});

Vue.component('sidebar_item', {
    props: {
        title: String,
        icon: String,
        clickable: String
    },
    template:
        '<v-list-item @click="changeWindow(clickable)">' +
            '<v-list-item-action>' +
                '<v-icon>{{icon}}</v-icon>' +
            '</v-list-item-action>' +
            '<v-list-item-content>' +
                '<v-list-item-title>{{title}}</v-list-item-title>' +
            '</v-list-item-content>' +
        '</v-list-item>',
    methods: {
        changeWindow: function(clicked) { //ideally, a click should call a different function based on prop 'clickable'
            //for now: if/else with clicked value to change window
            console.log("change window to " + clicked);
            if (clicked === "new-vht") { //@TODO: find a better way to do this
                window.location.assign("/createAccount")
            }
            else if (clicked === "home") {
                window.location.assign("/")
            }
            else if (clicked === "logout") {
                window.location.assign("/logout")
            }
        },
    }
});