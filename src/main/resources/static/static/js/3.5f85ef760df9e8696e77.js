webpackJsonp([3],{A65d:function(s,e,t){s.exports=t.p+"static/img/bg.511ea1c.png"},MH2N:function(s,e){},mrsp:function(s,e,t){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var r=t("vMJZ"),o=t("T452"),a={name:"register",data:function(){return{loginForm:{username:"",password:""},msg:"",rules:{username:[{required:!0,message:"请输入用户名",trigger:"blur"}],password:[{required:!0,message:"请输入密码",trigger:"blur"}]},showLogin:!1}},mounted:function(){this.showLogin=!0},methods:{submitForm:function(s){var e=this;this.$refs[s].validate(function(s){s&&Object(r.d)(e.loginForm.username,e.loginForm.password).then(function(s){s.data.status===o.a?e.msg="注册成功":e.msg=s.data[o.b]})})}}},i={render:function(){var s=this,e=s.$createElement,r=s._self._c||e;return r("div",{staticClass:"login_page"},[r("transition",{attrs:{name:"form-fade",mode:"in-out"}},[r("section",{directives:[{name:"show",rawName:"v-show",value:s.showLogin,expression:"showLogin"}],staticClass:"form_contianer"},[r("div",{staticClass:"manage_tip"},[r("p",[s._v("丁青县公安局档案管理系统")]),s._v(" "),r("img",{staticClass:"logo",attrs:{src:t("A65d"),alt:""}})]),s._v(" "),r("el-form",{ref:"loginForm",attrs:{model:s.loginForm,rules:s.rules}},[r("el-form-item",{attrs:{prop:"username"}},[r("el-input",{attrs:{placeholder:"用户名"},model:{value:s.loginForm.username,callback:function(e){s.$set(s.loginForm,"username",e)},expression:"loginForm.username"}})],1),s._v(" "),r("el-form-item",{attrs:{prop:"password"}},[r("el-input",{attrs:{type:"password",placeholder:"密码"},model:{value:s.loginForm.password,callback:function(e){s.$set(s.loginForm,"password",e)},expression:"loginForm.password"}})],1),s._v(" "),r("el-form-item",{staticStyle:{"margin-bottom":"10px"}},[r("el-button",{staticClass:"submit_btn",attrs:{type:"primary"},on:{click:function(e){s.submitForm("loginForm")}}},[s._v("注册")])],1),s._v(" "),r("div",{directives:[{name:"show",rawName:"v-show",value:s.msg,expression:"msg"}],staticClass:"tips",class:{succ:"注册成功"==s.msg}},[s._v(s._s(s.msg))]),s._v(" "),r("div",{staticClass:"register"},[s._v("\n          已有账号，立即"),r("router-link",{staticClass:"span",attrs:{to:"/login"}},[s._v("登陆")])],1)],1)],1)])],1)},staticRenderFns:[]};var n=t("VU/8")(a,i,!1,function(s){t("MH2N")},"data-v-41861d8a",null);e.default=n.exports}});
//# sourceMappingURL=3.5f85ef760df9e8696e77.js.map