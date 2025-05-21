<template>
  <div class="user-profile">
    <div class="user-header">
      <img :src="avatar" alt="avatar" class="avatar" />
      <div class="user-info">
        <h1>{{ username }}</h1>
        <p class="user-id">ID: {{ id }}</p>
        <p class="email">почта: {{ email }}</p>
      </div>
    </div>

    <!-- <div class="user-section">
      <h2>Мои обзоры</h2>
      <div class="section-content">
        <p>Здесь будут ваши обзоры.</p>
      </div>
    </div>

    <div class="user-section">
      <h2>Мои комментарии</h2>
      <div class="section-content">
        <p>Здесь будут ваши комментарии.</p>
      </div>
    </div>

    <div class="user-section">
      <h2>Избранные обзоры</h2>
      <div class="section-content">
        <p>Здесь будут избранные обзоры.</p>
      </div>
    </div>

    <div class="user-section">
      <h2>Избранные телефоны</h2>
      <div class="section-content">
        <p>Здесь будут избранные телефоны.</p>
      </div>
    </div> -->
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

onMounted(() => {
  const idCookie = getCookie('userId')
  const usernameCookie = getCookie('username')
  const emailCookie = getCookie('userEmail')
  const avatarCookie = getCookie('userAvatar')

  if (usernameCookie) username.value = usernameCookie
  if (idCookie) id.value = idCookie
  if (emailCookie) email.value = emailCookie
  if (avatarCookie) avatar.value = avatarCookie
})
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

.user-section {
  background: #fafafa;
  border-radius: 8px;
  padding: 1rem 1.5rem;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.05);
}

.user-section h2 {
  margin-bottom: 1rem;
  font-size: 1.25rem;
  color: #333;
}

.section-content {
  color: #555;
  font-size: 0.95rem;
}

.user-info p {
  margin: 4px 0;
  font-size: 1rem;
  color: #555;
}

.user-info p.email {
  font-style: italic;
  color: #3b82f6;
  word-break: break-all;
}

</style>