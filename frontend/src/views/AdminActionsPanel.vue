<template>
  <div class="p-6">
    <div class="flex justify-between items-center mb-4">
      <h1 class="text-2xl font-bold">Панель администратора</h1>
      <button class="bg-gray-700 text-white px-4 py-2 rounded" @click="logout">
        Выйти
      </button>
    </div>
    <table class="table-auto w-full border border-gray-300">
      <thead>
        <tr class="bg-gray-200">
          <th class="px-4 py-2 border">ID</th>
          <th class="px-4 py-2 border">Имя</th>
          <th class="px-4 py-2 border">Email</th>
          <th class="px-4 py-2 border">Действия</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="user in users" :key="user.id" class="border">
          <td class="px-4 py-2 border">{{ user.id }}</td>
          <td class="px-4 py-2 border">{{ user.username }}</td>
          <td class="px-4 py-2 border">{{ user.email }}</td>
          <td class="px-4 py-2 border">
            <button
              class="bg-blue-500 text-white px-2 py-1 rounded mr-2"
              @click="toggleOptions(user.id)"
            >
              Опции
            </button>
            <div v-if="activeUserId === user.id" class="mt-2 space-y-1">
              <button
                class="bg-red-500 text-white px-2 py-1 rounded"
                @click="disableUser(user.email)"
              >
                Отключить пользователя
              </button>
              <button
                class="bg-yellow-500 text-white px-2 py-1 rounded"
                @click="deactivateSessions(user.email)"
              >
                Деактивировать сессии
              </button>
              <button
                class="bg-green-500 text-white px-2 py-1 rounded"
                @click="requestPasswordReset(user.email)"
              >
                Запросить сброс пароля
              </button>
              <button
                class="bg-gray-500 text-white px-2 py-1 rounded"
                @click="unlockUser(user.email)"
              >
                Разблокировать пользователя
              </button>
            </div>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>
<script>
export default {
  name: "AdminUserPanel",
  data() {
    return {
      users: [],
      activeUserId: null,
    };
  },
  created() {
    this.fetchUsers();
  },
  methods: {
    async fetchUsers() {
      try {
        console.debug("GET /users/all");
        const res = await fetch("/api/users/all", {
          credentials: "include",
        });
        console.debug("Response status:", res.status);
        if (res.status === 200) {
          const data = await res.json();
          console.debug("Received users:", data);
          this.users = data;
        } else if (res.status === 403) {
          console.warn("Access denied (403) on /users/all");
          // this.$router.push("/reviews");
        } else {
          console.error("Unexpected response on /users/all:", res.status);
        }
      } catch (err) {
        console.error("Network error on /users/all:", err);
      }
    },

    toggleOptions(userId) {
      this.activeUserId = this.activeUserId === userId ? null : userId;
    },

    async disableUser(email) {
      console.debug("POST /users/lock", { email });
      try {
        const res = await fetch("/api/users/lock", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          credentials: "include",
          body: JSON.stringify({ email }),
        });
        console.debug("Response status:", res.status);
        if (res.status !== 200) {
          console.error("Failed to disable user:", res.status);
        }
      } catch (err) {
        console.error("Error disabling user:", err);
      }
    },

    async unlockUser(email) {
      console.debug("POST /users/unlock", { email });
      try {
        const res = await fetch("/api/users/unlock", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          credentials: "include",
          body: JSON.stringify({ email }),
        });
        console.debug("Response status:", res.status);
        if (res.status !== 200) {
          console.error("Failed to unlock user:", res.status);
        }
      } catch (err) {
        console.error("Error unlocking user:", err);
      }
    },

    async deactivateSessions(email) {
      const payload = { email, expired: "true" };
      console.debug("POST /users/set-password-expired (deactivate sessions)", payload);
      try {
        const res = await fetch("/api/users/set-password-expired", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          credentials: "include",
          body: JSON.stringify(payload),
        });
        console.debug("Response status:", res.status);
        if (res.status !== 200) {
          console.error("Failed to deactivate sessions:", res.status);
        }
      } catch (err) {
        console.error("Error deactivating sessions:", err);
      }
    },

    async requestPasswordReset(email) {
      const payload = { email, expired: "true" };
      console.debug("POST /users/set-password-expired (request password reset)", payload);
      try {
        const res = await fetch("/api/users/set-password-expired", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          credentials: "include",
          body: JSON.stringify(payload),
        });
        console.debug("Response status:", res.status);
        if (res.status !== 200) {
          console.error("Failed to request password reset:", res.status);
        }
      } catch (err) {
        console.error("Error requesting password reset:", err);
      }
    },

    logout() {
      console.debug("Logout requested, redirecting to /reviews");
      this.$router.push("/reviews");
    },
  },
};
</script>


<style scoped>
.table-auto th,
.table-auto td {
  text-align: left;
}
</style>
