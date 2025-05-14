<template>
  <div class="user-profile">
    <h1>Профиль пользователя {{ $route.params.username }}</h1>
    <!-- Добавьте контент профиля -->
  </div>
</template>

<script setup>
//import { useRoute } from 'vue-router'

//const route = useRoute()
</script>

<style scoped>
.user-profile {
  max-width: 1200px;
  margin: 2rem auto;
  padding: 2rem;
}
</style>