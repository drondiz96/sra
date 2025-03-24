import { createRouter, createWebHistory } from "vue-router";
import LoginForm from "@/components/LoginForm.vue";
import RegisterForm from "@/components/RegisterForm.vue"; // Импортируйте новый компонент
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
    path: "/two-factor",
    component: TwoFactor,
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
  const isAuthenticated = localStorage.getItem("token"); // Проверяем наличие токена
  if (to.meta.requiresAuth && !isAuthenticated) {
    next("/login"); // Перенаправляем на страницу входа
  } else {
    next(); // Продолжаем навигацию
  }
});

export default router;