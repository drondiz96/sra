import { createRouter, createWebHistory } from "vue-router";
import LoginForm from "@/components/LoginForm.vue";
import RegisterForm from "@/components/RegisterForm.vue";
import MailVerify from "@/components/MailVerify.vue";

import TwoFactor from "@/components/TwoFactor.vue";
import Dashboard from "@/components/UserDashboard.vue";

const routes = [
  {
    path: "/",
    redirect: "/login", // Перенаправление на страницу входа
  },
  {
    path: "/login",
    component: LoginForm,
  },
  {
    path: "/register",
    component: RegisterForm,
  },
  {
    path: "/mailverify",
    component: MailVerify,
    props: (route) => ({ email: route.query.email })
  },
  {
    path: "/two-factor",
    component: TwoFactor
  },
  {
    path: "/dashboard",
    component: Dashboard,
    meta: { requiresAuth: true }, // Защищенный маршрут
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

// Навигационный хук для проверки авторизации
router.beforeEach((to, from, next) => {
  const protectedPages = ['/dashboard', '/profile', '/settings']; // Список защищённых страниц
  const token = localStorage.getItem('token');
  
  // Если запрашивается защищённая страница и нет токена
  if (protectedPages.includes(to.path) && !token) {
    return next('/login'); // Перенаправляем на страницу входа
  }
  
  // Разрешаем переход для всех остальных случаев
  next();
});

export default router;