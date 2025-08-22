import { createRouter, createWebHistory } from "vue-router";

const routes = [
  {
    path: "/",
    name: "Home",
    component: () => import("../views/Home.vue"),
  },
  {
    path: "/login",
    name: "Login",
    component: () => import("../views/Login.vue"),
  },
  {
    path: "/register",
    name: "Register",
    component: () => import("../views/Register.vue"),
  },
  {
    path: "/boards",
    name: "BoardList",
    component: () => import("../views/BoardList.vue"),
  },
  {
    path: "/boards/:id",
    name: "BoardDetail",
    component: () => import("../views/BoardDetail.vue"),
  },
  {
    path: "/boards/create",
    name: "BoardCreate",
    component: () => import("../views/BoardCreate.vue"),
  },
  {
    path: "/boards/:id/edit",
    name: "BoardEdit",
    component: () => import("../views/BoardCreate.vue"),
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;