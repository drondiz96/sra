<template>
  <div class="user-panel">
    <div class="user-card" @click="navigateToProfile">
      <img
        :src="user.avatar"
        alt="Аватар пользователя"
        class="user-avatar"
        @error="onImageError"
      />
      <span class="user-login">{{ user.username || 'Гость' }}</span>
    </div>

    <button
      v-if="user.username !== 'Гость'"
      class="logout-btn"
      @click="logout"
    >
      Выйти
    </button>

    <button
      v-else
      class="login-btn"
      @click="goToLogin"
    >
      Войти
    </button>
  </div>
</template>


<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import defaultAvatar from '@/assets/default-avatar.gif'

const router = useRouter()
const user = ref({
  username: 'Гость',
  avatar: defaultAvatar,
})

const logout = () => {
  const cookiesToDelete = ['userId', 'username', 'userEmail', 'userAvatar']
  cookiesToDelete.forEach(name => {
    document.cookie = `${name}=; Max-Age=0; path=/`
  })

  user.value = {
    username: 'Гость',
    avatar: defaultAvatar
  }

  router.push('/login/')
}

const goToLogin = () => {
  router.push('/login/')
}


function getCookie(name) {
  const match = document.cookie.match(new RegExp('(^| )' + name + '=([^;]+)'))
  return match ? decodeURIComponent(match[2]) : null
}

async function fetchCurrentUser() {
  try {
    const avatar = getCookie('userAvatar')
    const username = getCookie('username')
    if (!username) {
      console.warn('Email в куки не найден')
      return
    }

    user.value = {
      username: username,
      avatar: avatar || defaultAvatar
    }
        
  } catch (error) {
    console.error('Ошибка при загрузке пользователя:', error)
  }
}

const onImageError = (event) => {
  event.target.src = defaultAvatar
}

const navigateToProfile = () => {
  if (!user.value.username || user.value.username === 'Гость') return
router.push({ name: 'UserPage'})
}

onMounted(() => {
  fetchCurrentUser()
})
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

.logout-btn,
.login-btn {
  margin-left: 10px;
  padding: 8px 14px;
  background-color: #3498db;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  transition: background-color 0.2s;
}

.logout-btn {
  background-color: #e74c3c;
}

.logout-btn:hover {
  background-color: #c0392b;
}

.login-btn:hover {
  background-color: #2980b9;
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