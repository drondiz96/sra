<template>
  <div class="login-form">
    <h2>Двухфакторная аутентификация</h2>
    <p v-if="validEmail" class="info-message">Код отправлен на {{ email }}</p>
    <p v-else class="error">Ошибка: Не удалось получить email</p>
    
    <form @submit.prevent="handleSubmit">
      <div class="form-group">
        <label for="code">Код подтверждения:</label>
        <input
          type="text"
          id="code"
          v-model="code"
          required
          pattern="\d{6}"
          placeholder="Введите 6-значный код"
          maxlength="6"
          inputmode="numeric"
          :disabled="!validEmail"
        />
      </div>
      <button 
        type="submit" 
        :disabled="isLoading || !validEmail"
      >
        {{ isLoading ? 'Проверка...' : 'Подтвердить' }}
      </button>
    </form>
    <p v-if="error" class="error">{{ error }}</p>
  </div>
</template>

<script>
export default {
  data() {
    return {
      email: '',
      code: "",
      error: "",
      isLoading: false
    };
  },
  computed: {
    validEmail() {
      return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(this.email);
    }
  },
  created() {
    this.email = this.$route.query.email || '';
    if (!this.validEmail) {
      this.error = "Не удалось получить email для подтверждения";
    }
  },
  methods: {
    async handleSubmit() {
      if (this.isLoading || !this.validEmail) return;
      
      this.error = "";
      this.isLoading = true;
      
      try {
        if (!/^\d{6}$/.test(this.code)) {
          throw new Error("Код должен состоять из 6 цифр");
        }

        const response = await fetch(
          "/api/users/authenticate/auth-verify-2fa",
          {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ email: this.email, code: this.code }),
            credentials: 'include' // Важно для получения cookie от сервера
          }
        ).catch(() => {
          throw new Error("Ошибка сети");
        });


        // Обрабатываем текстовые ответы сервера
        if (response.ok) {
          const data = await response.json(); // ожидаем JSON с id, username, email
          document.cookie = `userId=${encodeURIComponent(data.id)}; path=/; max-age=${60*60*24*365}`;
          document.cookie = `username=${encodeURIComponent(data.username)}; path=/; max-age=${60*60*24*365}`;
          document.cookie = `userEmail=${encodeURIComponent(data.email)}; path=/; max-age=${60*60*24*365}`;

          this.$router.push({ path: "/reviews/" });        
        } else {
            const resultText = await response.text();

          if (resultText === "Invalid 2FA code") {
            throw new Error("Неверный код подтверждения");
          }
          throw new Error(resultText || "Ошибка сервера");
        }
        
      } catch (error) {
        this.error = error.message;
        console.error("Ошибка:", error);
      } finally {
        this.isLoading = false;
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

.info-message {
  color: #666;
  margin: 10px 0;
  text-align: center;
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
  border: 1px solid #ccc;
  border-radius: 3px;
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

button:disabled {
  background-color: #cccccc;
  cursor: not-allowed;
}

.error {
  color: red;
  margin-top: 10px;
  text-align: center;
}

p {
  margin-top: 10px;
  text-align: center;
}
</style>