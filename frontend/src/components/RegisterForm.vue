<template>
    <div class="register-form">
      <h2>Регистрация</h2>
      <form @submit.prevent="handleSubmit">
        <div class="form-group">
          <label for="username">Почта пользователя:</label>
          <input
            type="text"
            id="username"
            v-model="username"
            required
            placeholder="Введите вашу почту"
          />
        </div>
        <div class="form-group">
          <label for="email">Email:</label>
          <input
            type="email"
            id="email"
            v-model="email"
            required
            placeholder="Введите email"
          />
        </div>
        <div class="form-group">
          <label for="password">Пароль:</label>
          <input
            type="password"
            id="password"
            v-model="password"
            required
            placeholder="Введите пароль"
          />
        </div>
        <div class="form-group">
          <label for="confirmPassword">Подтвердите пароль:</label>
          <input
            type="password"
            id="confirmPassword"
            v-model="confirmPassword"
            required
            placeholder="Подтвердите пароль"
          />
        </div>
        <button type="submit">Зарегистрироваться</button>
      </form>
      <p v-if="error" class="error">{{ error }}</p>
      <p>
        Уже есть аккаунт?
        <router-link to="/login">Войдите</router-link>
      </p>
    </div>
  </template>
  
  <script>
  export default {
    data() {
      return {
        username: "",
        email: "",
        password: "",
        confirmPassword: "",
        error: "",
      };
    },
    methods: {
      async handleSubmit() {
        try {
          // Проверка совпадения паролей
          if (this.password !== this.confirmPassword) {
            throw new Error("Пароли не совпадают");
          }
  
          // Отправка данных на бэкенд
          const response = await fetch("http://localhost:8080/users/createUser", {
            method: "POST",
            headers: {
              "Content-Type": "application/json"
            //  "User-Agent": "PostmanRuntime/7.43.2",
            },
            body: JSON.stringify({
              username: this.username,
              role: "ROLE_USER",
              password: this.password,
              email: this.email,
            }),
          });
  
          if (!response.ok) {
            throw new Error("Ошибка регистрации");
          }
  
          const data = await response.json();
          console.log("Успешная регистрация:", data);
  
          // Перенаправление на страницу входа
          this.$router.push({
            path: '/mailverify',
            query: { email: this.email }
          });
        } catch (error) {
          this.error = error.message;
          console.error("Ошибка:", error);
        }
      },
    },
  };
  </script>
  
  <style scoped>
  .register-form {
    max-width: 300px;
    margin: 0 auto;
    padding: 20px;
    border: 1px solid #ccc;
    border-radius: 5px;
  }
  
  .form-group {
    margin-bottom: 15px;
  }
  
  label {
    display: block;
    margin-bottom: 5px;
  }
  
  input {
    width: 100%;
    padding: 8px;
    box-sizing: border-box;
  }
  
  button {
    width: 100%;
    padding: 10px;
    background-color: #426ab9;
    color: white;
    border: none;
    border-radius: 5px;
    cursor: pointer;
  }
  
  button:hover {
    background-color: #375490;
  }
  
  .error {
    color: red;
    margin-top: 10px;
  }
  
  p {
    margin-top: 10px;
  }
  </style>