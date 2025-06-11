<template>
    <div class="two-factor">
      <h2>Двухфакторная аутентификация</h2>
      <p>На {{ email }} отправлен код подтверждения. Введите его ниже:</p>
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
          />
        </div>
        <button type="submit" :disabled="isLoading">
          {{ isLoading ? 'Проверка...' : 'Подтвердить' }}
        </button>
      </form>
      <p v-if="error" class="error">{{ error }}</p>
    </div>
  </template>
  
  <script>
  export default {
    props: {
      email: {
        type: String,
        required: true,
        validator: (value) => /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(value)
      }
    },
    data() {
      return {
        code: "",
        error: "",
        isLoading: false
      };
    },
    methods: {
      async handleSubmit() {
        if (this.isLoading) return;
        
        this.error = "";
        this.isLoading = true;
        
        try {
          // Валидация кода (6 цифр)
          if (!/^\d{6}$/.test(this.code)) {
            throw new Error("Код должен состоять из 6 цифр");
          }
  
          const response = await fetch("/api/users/createUser/register-verify-email", {
            method: "POST",
            headers: {
              "Content-Type": "application/json",
            },
            body: JSON.stringify({
              email: this.email,
              code: this.code
            }),
          });
  
          if (!response.ok) {
            const errorData = await response.json().catch(() => ({}));
            throw new Error(errorData.message || "Неверный код подтверждения");
          }
  
          // Успешная проверка кода - перенаправляем на страницу входа
          this.$router.push({
            path: '/login',
            query: { 
              verified: 'true',
              email: this.email 
            }
          });
          
        } catch (error) {
          this.error = error.message;
          console.error("Ошибка:", error);
        } finally {
          this.isLoading = false;
        }
      },
    },
  };
  </script>
  
  <style scoped>
  .two-factor {
    max-width: 400px;
    margin: 0 auto;
    padding: 20px;
    border: 1px solid #e0e0e0;
    border-radius: 8px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  }
  
  .form-group {
    margin-bottom: 1rem;
  }
  
  label {
    display: block;
    margin-bottom: 0.5rem;
    font-weight: 500;
  }
  
  input {
    width: 100%;
    padding: 0.75rem;
    border: 1px solid #ddd;
    border-radius: 4px;
    font-size: 1rem;
  }
  
  button {
    width: 100%;
    padding: 0.75rem;
    background-color: #4a6baf;
    color: white;
    border: none;
    border-radius: 4px;
    font-size: 1rem;
    cursor: pointer;
    transition: background-color 0.3s;
  }
  
  button:hover {
    background-color: #3a5a9f;
  }
  
  button:disabled {
    background-color: #cccccc;
    cursor: not-allowed;
  }
  
  .error {
    color: #e74c3c;
    margin-top: 1rem;
    text-align: center;
  }
  </style>