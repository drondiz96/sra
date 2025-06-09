<template>
  <div class="login-form">
    <h2>Авторизация</h2>
    <form @submit.prevent="handleSubmit">
      <div class="form-group">
        <label for="username">Имя пользователя:</label>
        <input
          type="text"
          id="username"
          v-model="username"
          required
          placeholder="Введите имя пользователя"
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
      <button type="submit">Войти</button>
    </form>
    <p v-if="error" class="error">{{ error }}</p>
    <p>
      Нет аккаунта?
      <router-link to="/register">Зарегистрируйтесь</router-link>
    </p>
    <a href="/api/oauth2/authorization/google">
      <button>Войти через Google</button>
    </a>
  </div>
</template>

<script>
export default {
  data() {
    return {
      username: "",
      password: "",
      error: "",
    };
  },
  methods: {
    async handleSubmit() {
      try {
        // Отправка данных на бэкенд
        const response = await fetch("/api/users/authenticate", {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            email: this.username,
            password: this.password,
          }),
        });

        if (!response.ok) {
          throw new Error("Ошибка авторизации");
        }

        const data = await response.json();
        console.log("Успешная авторизация:", data);

        // Перенаправление на страницу двухфакторной аутентификации
        this.$router.push({
          path:"/two-factor",
        query: { email: this.username }}
        );
      } catch (error) {
        this.error = "Неверное имя пользователя или пароль";
        console.error("Ошибка:", error);
      }
    },
  },
};
</script>

<style scoped>
.login-form {
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