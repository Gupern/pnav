import Vue from 'vue';
import VueRouter from 'vue-router';

Vue.use(VueRouter);

const routes = [
  // {
  //   path: "/",
  //   name: "home",
  //   component: HomeView,
  // },
  // {
  // path: "/about",
  // name: "about",
  // route level code-splitting
  // this generates a separate chunk (about.[hash].js) for this route
  // which is lazy-loaded when the route is visited.
  // component: () =>
  // import(/* webpackChunkName: "about" */ "../views/AboutView.vue"),
  // },
  {
    path: '/',
    name: 'loginPage',
    component: () => import('../views/LoginPage.vue'),
  },
  {
    path: '/fundAI',
    name: 'FundAI',
    component: () => import('../views/FundAI.vue'),
  },
  {
    path: '/StockAI',
    name: 'StockAI',
    component: () => import('../views/StockAI.vue'),
  },
];

const router = new VueRouter({
  routes,
});

export default router;
