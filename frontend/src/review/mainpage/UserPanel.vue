<template>
  <div class="user-panel">
    <div class="user-card" @click="navigateToProfile">
      <img
        :src="props.user.avatar"
        alt="Аватар пользователя"
        class="user-avatar"
        @error="onImageError"
      />
      <span class="user-login">{{ props.user.username || 'Гость' }}</span>
    </div>
  </div>
</template>

<script setup>
import { defineProps } from 'vue'
import { useRouter } from 'vue-router'
import defaultAvatar from '@/assets/default-avatar.gif'

const props = defineProps({
  user: {
    type: Object,
    required: false,
    default: () => ({ username: 'Гость' })
  }
})

const router = useRouter()

const navigateToProfile = () => {
  const username = props.user.username || props.user.login
  if (!username || username === 'Гость') return
  router.push({ name: 'UserPage', params: { username } })
}

// fallback на случай ошибки загрузки изображения
const onImageError = (event) => {
  event.target.src = defaultAvatar // добавь этот файл в public/images
}
</script>

<style scoped>
.user-panel {
  display: flex;
  justify-content: flex-end; /* Плитка справа */
  width: 100%;
  margin: 0;
}

.user-card {
  background: white;
  padding: 12px 20px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
  display: flex;
  align-items: center;
  gap: 10px;
  cursor: pointer;
  transition: transform 0.2s, box-shadow 0.2s;
  width: max-content;
}

.user-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0,0,0,0.15);
}

.user-login {
  font-weight: 500;
  color: #2c3e50;
  font-size: 0.95rem;
  white-space: nowrap;
}

.user-avatar {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  object-fit: cover;
}

@media (max-width: 900px) {
  .user-panel {
    justify-content: flex-end;
    margin-bottom: 20px;
  }

  .user-card {
    justify-content: flex-end;
  }
}

@media (max-width: 480px) {
  .user-card {
    width: 100%;
    justify-content: center;
  }
}
</style>