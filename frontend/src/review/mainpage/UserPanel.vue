<template>
  <div class="user-panel">
    <button
      v-if="user.username !== 'Гость'"
      class="admin-btn"
      @click="goToAdminPanel"
      title="Админ-панель"
    >
      <svg
        xmlns="http://www.w3.org/2000/svg"
        class="icon"
        viewBox="0 0 24 24"
        fill="none"
        stroke="currentColor"
        stroke-width="2"
        stroke-linecap="round"
        stroke-linejoin="round"
      >
        <circle cx="12" cy="12" r="3" />
        <path
          d="M19.4 15a1.65 1.65 0 0 0 .33 1.82l.06.06a2 2 0 0 1-2.83 2.83l-.06-.06a1.65 1.65 0 0 0-1.82-.33
            1.65 1.65 0 0 0-1 1.51V21a2 2 0 0 1-4 0v-.09a1.65 1.65 0 0 0-1-1.51
            1.65 1.65 0 0 0-1.82.33l-.06.06a2 2 0 0 1-2.83-2.83l.06-.06a1.65 1.65 0 0 0 .33-1.82
            1.65 1.65 0 0 0-1.51-1H3a2 2 0 0 1 0-4h.09a1.65 1.65 0 0 0 1.51-1
            1.65 1.65 0 0 0-.33-1.82l-.06-.06a2 2 0 0 1 2.83-2.83l.06.06a1.65 1.65 0 0 0 1.82.33h.09
            A1.65 1.65 0 0 0 11 3.09V3a2 2 0 0 1 4 0v.09a1.65 1.65 0 0 0 1 1.51h.09
            a1.65 1.65 0 0 0 1.82-.33l.06-.06a2 2 0 0 1 2.83 2.83l-.06.06
            a1.65 1.65 0 0 0-.33 1.82v.09A1.65 1.65 0 0 0 21 12a1.65 1.65 0 0 0-1.6 1z"
        />
      </svg>
    </button>

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

const logout = async () => {

    const response = await fetch(`/api/users/logout`, {
      method: 'POST',
      credentials: 'include'
    })
  
  if (!response.ok) throw new Error(`Ошибка выхода: ${response.status}`)
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

const goToAdminPanel = () => {
  router.push('/admin')
}

function getCookie(name) {
  const match = document.cookie.match(new RegExp('(^| )' + name + '=([^;]+)'))
  return match ? decodeURIComponent(match[2]) : null
}

async function fetchCurrentUser() {
  const router = useRouter();
  
  try {
    // Получаем данные из cookies
    const avatar = getCookie('userAvatar');
    const username = getCookie('username');
    const userId = getCookie('userId');

    if (!userId) {
      console.warn('User ID не найден в куках');
      return;
    }

    // Делаем запрос через fetch
    const response = await fetch(`/api/users/${userId}`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        // Если нужно передавать токен авторизации:
        // 'Authorization': `Bearer ${getCookie('accessToken')}`
      },
      credentials: 'include' // Для отправки cookies
    });

    // Обработка HTTP ошибок
    if (!response.ok) {
      if (response.status === 404) {
        console.error('Пользователь не найден');
        // Очистка данных или перенаправление
      } else {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      return;
    }

    // Парсим JSON ответ
    const userData = await response.json();
    
    // Проверка на просроченный пароль
if (userData.passwordExpired) {
  const alreadyRedirected = localStorage.getItem('passwordExpiredRedirect');
  
  if (!alreadyRedirected) {
    localStorage.setItem('passwordExpiredRedirect', 'true'); // Помечаем, что редирект был
    router.push('/user/me');
  }
}
    // Обновляем данные пользователя
    user.value = {
      id: userData.id,
      username: userData.username || username,
      email: userData.email,
      avatar: avatar || defaultAvatar,
      ...userData
    };
        
  } catch (error) {
    console.error('Ошибка при загрузке пользователя:', error);
    // Дополнительная обработка ошибок сети
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

.admin-btn {
  background: none;
  border: none;
  cursor: pointer;
  padding: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #2ecc71;
  transition: color 0.2s;
}

.admin-btn:hover {
  color: #27ae60;
}

.icon {
  width: 24px;
  height: 24px;
}

</style>