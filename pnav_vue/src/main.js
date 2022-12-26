import Vue from "vue";
import App from "./App.vue";
import router from "./router";
import Vant from 'vant'
import 'vant/lib/index.css';
import 'amfe-flexible';
import VueResource from "vue-resource";

Vue.config.productionTip = false;

new Vue({
  router,
  render: (h) => h(App),
    methods:{
      show: function(){
        console.log(this.number);
      },
      testGoto: function() {
        this.$router.push('/packageList')
      }
    }
}).$mount("#app");

Vue.use(Vant)
Vue.use(VueResource)