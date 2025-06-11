<template>
  <div class="login-form">
    <h2>Авторизация</h2>
    <form @submit.prevent="handleSubmit">
      <div class="form-group">
        <label for="username">Почта:</label>
        <input
          type="text"
          id="username"
          v-model="username"
          required
          placeholder="Введите почту"
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

      <!-- === reCAPTCHA v3 === -->
      <div class="windows-recaptcha">
        <div class="captcha-body">
          <button
            type="button"
            class="windows-btn"
            :disabled="!recaptchaScriptLoaded || isVerified"
            @click="executeRecaptcha"
          >
            {{ isVerified ? '✅ Проверено' : 'Подтвердить, что вы не робот' }}
          </button>
          <div class="status-message" v-if="message">
            {{ message }}
          </div>
        </div>
      </div>
      <!-- === /reCAPTCHA === -->

      <button type="submit" :disabled="!isVerified">Войти</button>
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
      // reCAPTCHA
      recaptchaScriptLoaded: false,
      isVerified: false,
      message: "",
      recaptchaToken: ""
    };
  },
  mounted() {
    // Динамически загружаем скрипт reCAPTCHA v3
    const script = document.createElement("script");
    script.src =
      "https://www.google.com/recaptcha/api.js?render=6LfVUP4qAAAAAF7f0AeRHKthKHuL3YDrxAn1sX-d";
    script.async = true;
    script.defer = true;
    script.onload = () => {
      this.recaptchaScriptLoaded = true;
    };
    document.head.appendChild(script);
  },
  methods: {
    // Получаем токен и проверяем на сервере
    async executeRecaptcha() {
      this.message = "";
      try {
        const token = await window.grecaptcha.execute(
          "6LfVUP4qAAAAAF7f0AeRHKthKHuL3YDrxAn1sX-d",
          { action: "login" }
        );
        this.recaptchaToken = token;

        const response = await fetch("/api/verify-recaptcha", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({ token })
        });
        const result = await response.json();

        if (response.ok && result.success) {
          this.isVerified = true;
          this.message = `Проверка пройдена (оценка: ${result.score.toFixed(
            2
          )})`;
        } else {
          this.message =
            "Ошибка проверки: " +
            (result.error || `score=${result.score.toFixed(2)}`);
        }
      } catch (e) {
        console.error(e);
        this.message = "Ошибка при выполнении reCAPTCHA.";
      }
    },

    // Отправка формы только после проверки капчи
    async handleSubmit() {
      this.error = "";

      if (!this.isVerified) {
        this.error = "Сначала подтвердите, что вы не робот.";
        return;
      }

      try {
        const response = await fetch(
          "/api/users/authenticate",
          {
            method: "POST",
            headers: {
              "Content-Type": "application/json"
            },
            body: JSON.stringify({
              email: this.username,
              password: this.password,
              recaptchaToken: this.recaptchaToken
            })
          }
        );

        if (!response.ok) {
          throw new Error("Ошибка авторизации");
        }

        const data = await response.json();
        console.log("Успешная авторизация:", data);

        // Перенаправление на страницу двухфакторной аутентификации
        this.$router.push({
          path: "/two-factor",
          query: { email: this.username }
        });
      } catch (error) {
        this.error = "Неверное имя пользователя или пароль";
        console.error("Ошибка:", error);
      }
    }
  }
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

/* reCAPTCHA styles */
.windows-recaptcha {
  margin: 15px 0;
}

.windows-btn {
  padding: 10px 15px;
  background-color: #0078d7;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
}

.windows-btn:hover:not(:disabled) {
  background-color: #106ebe;
}

.windows-btn:disabled {
  background-color: #cccccc;
  cursor: not-allowed;
}

.status-message {
  margin-top: 8px;
  font-size: 13px;
  color: #555;
}
</style>
