webpackJsonp([5],{"137D":function(e,t){},Ntyz:function(e,t,s){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var r=s("vMJZ"),o=s("T452"),a={name:"user",data:function(){return{loginForm:{username:"",password:""},msg:"",rules:{username:[{required:!0,message:"请输入用户名",trigger:"blur"}],password:[{required:!0,message:"请输入密码",trigger:"blur"}]},showLogin:!1}},mounted:function(){this.showLogin=!0},methods:{submitForm:function(e){var t=this;this.$refs[e].validate(function(e){e&&Object(r.a)(t.loginForm.username,t.loginForm.password).then(function(e){e.data.status===o.a?(t.msg="",t.$router.push({path:"/index"}),e.data.super?localStorage.setItem("super",!0):localStorage.setItem("super",!1)):t.msg=e.data[o.b]})})}}},n={render:function(){var e=this,t=e.$createElement,s=e._self._c||t;return s("div",{staticClass:"login_page"},[s("transition",{attrs:{name:"form-fade",mode:"in-out"}},[s("section",{directives:[{name:"show",rawName:"v-show",value:e.showLogin,expression:"showLogin"}],staticClass:"form_contianer"},[s("div",{staticClass:"manage_tip"},[s("p",[e._v("丁青县公安局档案管理系统")])]),e._v(" "),s("el-form",{ref:"loginForm",attrs:{model:e.loginForm,rules:e.rules}},[s("el-form-item",{attrs:{prop:"username"}},[s("el-input",{attrs:{placeholder:"用户名"},model:{value:e.loginForm.username,callback:function(t){e.$set(e.loginForm,"username",t)},expression:"loginForm.username"}})],1),e._v(" "),s("el-form-item",{attrs:{prop:"password"}},[s("el-input",{attrs:{type:"password",placeholder:"密码"},model:{value:e.loginForm.password,callback:function(t){e.$set(e.loginForm,"password",t)},expression:"loginForm.password"}})],1),e._v(" "),s("el-form-item",{staticStyle:{"margin-bottom":"10px"}},[s("el-button",{staticClass:"submit_btn",attrs:{type:"primary"},on:{click:function(t){e.submitForm("loginForm")}}},[e._v("登陆")])],1),e._v(" "),s("div",{staticClass:"tips",attrs:{type:"error"}},[e._v(e._s(e.msg))]),e._v(" "),s("div",{staticClass:"register"},[e._v("\n          若没有账号，请先"),s("router-link",{staticClass:"span",attrs:{to:"/register"}},[e._v("注册")])],1)],1)],1)])],1)},staticRenderFns:[]};var i=s("VU/8")(a,n,!1,function(e){s("137D")},"data-v-7779c680",null);t.default=i.exports}});
//# sourceMappingURL=5.9c3777a24acb2d2e8992.js.map