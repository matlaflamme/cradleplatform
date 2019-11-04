Vue.component('login' , {
    data: {
        username : '',
        password : ''
    },
    methods: {
        submitInput: function() {
            //signs in
            console.log(this.username);
            console.log(this.password);
        },
    },
    Template:
    '    <v-content>\n' +
        '      <v-container\n' +
        '        fluid\n' +
        '        fill-height\n' +
        '      >\n' +
        '        <v-layout\n' +
        '          align-center\n' +
        '          justify-center\n' +
        '        >\n' +
        '          <v-flex\n' +
        '            xs12\n' +
        '            sm8\n' +
        '            md4\n' +
        '          >\n' +
        '            <v-card class="elevation-12">\n' +
        '              <v-toolbar\n' +
        '                color="primary"\n' +
        '                dark\n' +
        '                flat\n' +
        '              >\n' +
        '                <v-toolbar-title>Login form</v-toolbar-title>\n' +
        '                <v-spacer></v-spacer>\n' +
        '                <v-tooltip bottom>\n' +
        '                  <template v-slot:activator="{ on }">\n' +
        '                    <v-btn\n' +
        '                      :href="source"\n' +
        '                      icon\n' +
        '                      large\n' +
        '                      target="_blank"\n' +
        '                      v-on="on"\n' +
        '                    >\n' +
        '                      <v-icon>mdi-code-tags</v-icon>\n' +
        '                    </v-btn>\n' +
        '                  </template>\n' +
        '                  <span>Source</span>\n' +
        '                </v-tooltip>\n' +
        '                <v-tooltip right>\n' +
        '                  <template v-slot:activator="{ on }">\n' +
        '                    <v-btn\n' +
        '                      icon\n' +
        '                      large\n' +
        '                      href="https://codepen.io/johnjleider/pen/pMvGQO"\n' +
        '                      target="_blank"\n' +
        '                      v-on="on"\n' +
        '                    >\n' +
        '                      <v-icon>mdi-codepen</v-icon>\n' +
        '                    </v-btn>\n' +
        '                  </template>\n' +
        '                  <span>Codepen</span>\n' +
        '                </v-tooltip>\n' +
        '              </v-toolbar>\n' +
        '              <v-card-text>\n' +
        '                <v-form>\n' +
        '                  <v-text-field\n' +
        '                    label="Login"\n' +
        '                    name="login"\n' +
        '                    prepend-icon="person"\n' +
        '                    type="text"\n' +
        '                  ></v-text-field>\n' +
        '\n' +
        '                  <v-text-field\n' +
        '                    id="password"\n' +
        '                    label="Password"\n' +
        '                    name="password"\n' +
        '                    prepend-icon="lock"\n' +
        '                    type="password"\n' +
        '                  ></v-text-field>\n' +
        '                </v-form>\n' +
        '              </v-card-text>\n' +
        '              <v-card-actions>\n' +
        '                <v-spacer></v-spacer>\n' +
        '                <v-btn color="primary">Login</v-btn>\n' +
        '              </v-card-actions>\n' +
        '            </v-card>\n' +
        '          </v-flex>\n' +
        '        </v-layout>\n' +
        '      </v-container>\n' +
        '    </v-content>'
});
