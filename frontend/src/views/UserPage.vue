<template>
  <div class="user-profile">
    <div v-if="showPasswordBanner" class="password-banner">
      <strong>Пожалуйста, смените пароль!</strong>
    </div>
    <div class="user-header">
      <img :src="avatar" alt="avatar" class="avatar" />
      <div class="user-info">
        <h1>{{ username }}</h1>
        <p class="user-id">ID: {{ id }}</p>
        <p class="email">почта: {{ email }}</p>
      </div>
    </div>

    <!-- Форма смены пароля -->
    <div class="user-section">
      <h2>Сменить пароль</h2>
      <input
        v-model="newPassword"
        type="password"
        placeholder="Новый пароль"
        class="password-input"
      />
      <button @click="changePassword" class="submit-button">Сменить</button>
      <p v-if="message" class="message">{{ message }}</p>
    </div>
  </div>
</template>


<script setup>
import { ref, onMounted } from 'vue'
import defaultAvatar from '@/assets/default-avatar.gif'

function getCookie(name) {
  const match = document.cookie.match(new RegExp('(^| )' + name + '=([^;]+)'))
  return match ? decodeURIComponent(match[2]) : null
}

const username = ref('Гость')
const id = ref(0)
const email = ref('Guest@guest.com')
const avatar = ref(defaultAvatar)
const newPassword = ref('')
const message = ref('')
const showPasswordBanner = ref(false)

async function fetchUserProfile() {
  const idCookie = getCookie('userId')
  if (!idCookie) return

  try {
    const response = await fetch(`/api/users/${idCookie}`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json'
      },
      credentials: 'include'
    })
    if (!response.ok) throw new Error('Не удалось получить данные пользователя')

    const userData = await response.json()

    username.value = userData.username || getCookie('username') || 'Гость'
    id.value = userData.id || idCookie
    email.value = userData.email || getCookie('userEmail') || 'Guest@guest.com'
    avatar.value = getCookie('userAvatar') || defaultAvatar

    // Вот тут флаг смены пароля!
    if (userData.passwordExpired) {
      showPasswordBanner.value = true
    }
  } catch (err) {
    console.error('Ошибка при загрузке профиля:', err)
  }
}

onMounted(() => {
  fetchUserProfile()
})

async function changePassword() {
  if (!newPassword.value) {
    message.value = 'Введите новый пароль.'
    return
  }

  try {
    const response = await fetch('/api/users/', {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        id: parseInt(id.value),
        username: username.value,
        role: 'ROLE_USER',
        password: newPassword.value,
        email: email.value,
      }),
    })

    if (!response.ok) {
      throw new Error('Ошибка при отправке запроса')
    }

    message.value = 'Пароль успешно изменён!'
    newPassword.value = ''
    showPasswordBanner.value = false // Баннер больше не нужен
    // Можно обновить профиль/куки и т.д.
  } catch (err) {
    console.error(err)
    message.value = 'Ошибка при смене пароля.'
  }
}
</script>


<style scoped>
.user-profile {
  max-width: 1000px;
  margin: 2rem auto;
  padding: 2rem;
  display: flex;
  flex-direction: column;
  gap: 2rem;
}

.user-header {
  display: flex;
  align-items: center;
  gap: 20px;
  border-bottom: 1px solid #ddd;
  padding-bottom: 1rem;
}

.avatar {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  object-fit: cover;
}

.user-info h1 {
  margin: 0;
  font-size: 1.6rem;
  color: #2c3e50;
}

.user-id {
  font-size: 0.9rem;
  color: #888;
}

.user-info p {
  margin: 4px 0;
  font-size: 1rem;
  color: #555;
}

.user-info p.email {
  font-style: italic;
  color: #3b82f6;
  word-break: break-word;
}

.user-section {
  background: #fafafa;
  border-radius: 8px;
  padding: 1rem 1.5rem;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.05);
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.password-input {
  padding: 0.5rem;
  font-size: 1rem;
  border: 1px solid #ccc;
  border-radius: 4px;
}

.submit-button {
  padding: 0.6rem 1.2rem;
  background-color: #3b82f6;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
}

.submit-button:hover {
  background-color: #2563eb;
}

.message {
  font-size: 0.95rem;
  color: green;
}

.password-banner {
  background: #fffae6;
  border: 1px solid #ffb300;
  color: #b26b00;
  padding: 1rem;
  border-radius: 8px;
  margin-bottom: 1.5rem;
  text-align: center;
  font-size: 1.15rem;
}

</style>
