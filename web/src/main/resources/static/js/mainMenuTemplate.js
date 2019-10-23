Vue.component('sidebar_template_admin', {
    props: {
        current_user: String
    },
    data: () => ({
        drawer: null
    }),
    template:
    '<v-navigation-drawer\n' +
        'app\n' +
        'clipped\n' +
        'v-model="drawer"\n' +
        '>\n' +
        '<!--items in sidebar. See sidebar_item component for more\n' +
        'title, icon and clickable are props that are being passed to the component -->\n' +
        '<!--Icons from: https://material.io/resources/icons/?icon=settings_input_antenna&style=baseline -->\n' +
            '<v-list>\n' +
                '<sidebar_item title="Home" icon="home" clickable="admin-home"></sidebar_item>\n' +
                '\n' +
                '<v-subheader class="mt-3 grey--text text--darken-1">Statistical Analysis</v-subheader>\n' +
                '<sidebar_item title="Readings" icon="assessment" clickable="readings"></sidebar_item>\n' +
                '<sidebar_item title="Referrals" icon="assignment" clickable="referrals"></sidebar_item>\n' +
                '<sidebar_item title="VHT Performance" icon="rate_review" clickable="vht-performance"></sidebar_item>\n' +
                '\n' +
                '<v-subheader class="mt-3 grey--text text--darken-1">User Management</v-subheader>\n' +
                '<sidebar_item title="New Health Center" icon="add_circle" clickable="new-health-center"></sidebar_item>\n' +
                '<sidebar_item title="New Account" icon="person_add" clickable="new-vht"></sidebar_item>\n' +
                '<sidebar_item title="Manage Accounts" icon="supervisor_account" clickable="manage-accounts"></sidebar_item>\n' +
                '<v-subheader class="mt-3 grey--text text--darken-1">Currently signed in as: {{current_user}}</v-subheader>\n' +
                '<sidebar_item title="Sign Out" icon="account_circle" clickable="logout"></sidebar_item>\n' +
            '</v-list>\n' +
        '</v-navigation-drawer>'
});

Vue.component('sidebar_template_health', {
    props: {
        current_user: String
    },
    data: () => ({
        drawer: null
    }),
    template:
        '<v-navigation-drawer\n' +
        'app\n' +
        'clipped\n' +
        'v-model="drawer"\n' +
        '>\n' +
        '<!--items in sidebar. See sidebar_item component for more\n' +
        'title, icon and clickable are props that are being passed to the component -->\n' +
        '<!--Icons from: https://material.io/resources/icons/?icon=settings_input_antenna&style=baseline -->\n' +
        '<v-list>\n' +
        '<sidebar_item title="Home" icon="home" clickable="health-home"></sidebar_item>\n' +
        '\n' +
        '<v-subheader class="mt-3 grey--text text--darken-1">Patients</v-subheader>\n' +
        '<sidebar_item title="New Referrals" icon="assignment" clickable="new-referrals"></sidebar_item>\n' +
        '<sidebar_item title="Past Referrals" icon="assignment" clickable="past-referrals"></sidebar_item>\n' +
        '<sidebar_item title="All Referrals" icon="assignment" clickable="all-referrals"></sidebar_item>\n' +
        '\n' +
        '<v-subheader class="mt-3 grey--text text--darken-1">Account</v-subheader>\n' +
        '<sidebar_item title="Account Settings" icon="settings" clickable="account-settings"></sidebar_item>\n' +
        '<v-subheader class="mt-3 grey--text text--darken-1">Currently signed in as: {{current_user}}</v-subheader>\n' +
        '<sidebar_item title="Sign Out" icon="account_circle" clickable="logout"></sidebar_item>\n' +
        '</v-list>\n' +
        '</v-navigation-drawer>'
});

Vue.component('navbar_template', {
    props: {

    },
    template:
    '<v-app-bar\n' +
        'app\n' +
        'clipped-left\n' +
        'color="#0b908f"\n' +
        'dark\n' +
        '>\n' +
            '<v-app-bar-nav-icon @click.stop="changeDrawer"></v-app-bar-nav-icon>\n' +
            '<v-toolbar-title class="mr-5 align-center" color=white>\n' +
                '<span class="title">Cradle Web Platform</span>\n' +
            '</v-toolbar-title>\n' +
        '</v-app-bar>',
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
            else if (clicked === "admin-home") {
                window.location.assign("/admin")
            }
            else if (clicked === "logout") {
                window.location.assign("/logout")
            }
            else if (clicked === "health-home") {
                window.location.assign("/healthworker")
            }
        },
    }
});