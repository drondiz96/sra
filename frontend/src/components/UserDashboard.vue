<template>
  <div class="welcome-screen">
    <h1>Добро пожаловать!</h1>
    <button @click="logout" class="logout-btn">Выйти</button>
  </div>
</template>

<script>
export default {
  name: 'WelcomePage',
  methods: {
    logout() {
      // Удаляем токен аутентификации
      localStorage.removeItem('token');
      // Перенаправляем на страницу входа
      this.$router.push('/login');
      // Можно добавить сообщение о успешном выходе
      alert('Вы успешно вышли из системы');
    }
  }
}
</script>

<style scoped>
.welcome-screen {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f5f5f5;
  font-family: Arial, sans-serif;
  position: relative;
}

h1 {
  color: #2c3e50;
  font-size: 3rem;
  text-align: center;
  margin-bottom: 2rem;
}

.logout-btn {
  position: absolute;
  top: 20px;
  right: 20px;
  padding: 0.5rem 1rem;
  background-color: #e74c3c;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.logout-btn:hover {
  background-color: #c0392b;
}

@keyframes fadeIn {
  from { opacity: 0; }
  to { opacity: 1; }
}
</style>