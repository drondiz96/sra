<template>
  <div class="user-profile">
    <div class="user-header">
      <img :src="userAvatar" alt="avatar" class="avatar" />
      <div class="user-info">
        <h1>{{ username }}</h1>
        <p class="user-id">ID: {{ user.id }}</p>
      </div>
    </div>

    <div class="user-section">
      <h2>Мои обзоры</h2>
      <div class="section-content">
        <!-- Компонент или список обзоров -->
        <p>Здесь будут ваши обзоры.</p>
      </div>
    </div>

    <div class="user-section">
      <h2>Мои комментарии</h2>
      <div class="section-content">
        <!-- Компонент или список комментариев -->
        <p>Здесь будут ваши комментарии.</p>
      </div>
    </div>

    <div class="user-section">
      <h2>Избранные обзоры</h2>
      <div class="section-content">
        <!-- Компонент или избранные обзоры -->
        <p>Здесь будут избранные обзоры.</p>
      </div>
    </div>

    <div class="user-section">
      <h2>Избранные телефоны</h2>
      <div class="section-content">
        <!-- Компонент или избранные телефоны -->
        <p>Здесь будут избранные телефоны.</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import defaultAvatar from '@/assets/default-avatar.gif'

// Заглушка: в реальности данные нужно получать через API или Vuex
const user = {
  id: 1,
  username: 'Иван Иванов',
  avatar: ''
}

const route = useRoute()
const username = computed(() => route.params.username || user.username)
const userAvatar = computed(() => user.avatar || defaultAvatar)
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
</style>