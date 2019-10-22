Vue.component('sidebar_template', {
    props: {

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
            '<v-list dense>\n' +
                '<sidebar_item title="Home" icon="home" clickable="admin-home"></sidebar_item>\n' +
                '\n' +
                '<v-subheader class="mt-3 grey--text text--darken-1">Statistical Analysis</v-subheader>\n' +
                '<sidebar_item title="Readings" icon="assessment" clickable="readings"></sidebar_item>\n' +
                '<sidebar_item title="Referrals" icon="assignment" clickable="referrals"></sidebar_item>\n' +
                '<sidebar_item title="VHT Performance" icon="rate_review" clickable="vht-performance"></sidebar_item>\n' +
                '\n' +
                '<v-subheader class="mt-3 grey--text text--darken-1">User Management</v-subheader>\n' +
                '<sidebar_item title="New Health Center" icon="add_circle" clickable="new-health-center"></sidebar_item>\n' +
                '<sidebar_item title="New VHT" icon="person_add" clickable="new-vht"></sidebar_item>\n' +
                '<sidebar_item title="Manage Accounts" icon="supervisor_account" clickable="manage-accounts"></sidebar_item>\n' +
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