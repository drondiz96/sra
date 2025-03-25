<template>
  <div class="two-factor">
    <h2>Двухфакторная аутентификация</h2>
    <p>На вашу почту отправлен код подтверждения. Введите его ниже:</p>
    <form @submit.prevent="handleSubmit">
      <div class="form-group">
        <label for="code">Код подтверждения:</label>
        <input
          type="text"
          id="code"
          v-model="code"
          required
          placeholder="Введите код"
        />
      </div>
      <button type="submit">Подтвердить</button>
    </form>
    <p v-if="error" class="error">{{ error }}</p>
  </div>
</template>

<script>
export default {
  data() {
    return {
      code: "", // Код подтверждения
      error: "", // Сообщение об ошибке
    };
  },
  methods: {
    async handleSubmit() {
      try {

        // Отправка кода на бэкенд для проверки
        const response = await fetch("https://your-backend-api.com/verify-2fa", {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            code: this.code,
          }),
        });

        if (!response.ok) {
          throw new Error("Неверный код подтверждения");
        }

        const data = await response.json();
        console.log("Успешная аутентификация:", data);

        // Сохранение токена (например, в localStorage)
        localStorage.setItem("token", data.token);

        // Перенаправление на защищенную страницу
        this.$router.push("/dashboard");
      } catch (error) {
        this.error = "Неверный код подтверждения";
        console.error("Ошибка:", error);
      }
    },
  },
};
</script>

<style scoped>
.two-factor {
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
</style>