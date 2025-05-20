<template>
  <div class="container">
    <!-- Фильтры слева -->
    <FiltersPanel 
      :search-query="searchQuery"
      :price-range="priceRange"
      @update:searchQuery="val => searchQuery = val"
      @update:priceRange="val => priceRange = val"
      @validate-price="validatePriceInput"
    />

    <!-- Основной контент справа -->
    <div class="content-main">
      <!-- Плитка пользователя -->
      <UserProfileCard 
        :user="user"
      />

      <!-- Список обзоров -->
      <div class="reviews-grid">
        <ReviewList />
      </div>
    </div>
  </div>
</template>

<script setup>
import FiltersPanel from '@/review/mainpage/FiltersPanel.vue'
import UserProfileCard from '@/review/mainpage/UserPanel.vue'
import { usePhones } from '@/review/data/usePhones'
import ReviewList from './ReviewList.vue'
import defaultAvatar from '@/assets/default-avatar.gif'

// Получаем данные и фильтры
const {
  // isLoading,
  // filteredPhones,
  searchQuery,
  priceRange,
  validatePriceInput,
} = usePhones()

// Временный пользователь, можно заменить позже
const user = {
  id: 1,
  username: 'Иван Иванов',
  avatar: defaultAvatar
}
</script>

<style scoped>
.container {
  display: grid;
  grid-template-columns: 280px 1fr;
  gap: 30px;
  padding: 30px;
  max-width: 1400px;
  margin: 0 auto;
  min-height: 100vh;
}

.content-main {
  position: relative;
}

.reviews-grid {
  display: grid;
  grid-template-columns: 1fr;
  gap: 25px;
  padding: 15px 0;
}

.loading {
  text-align: center;
  padding: 2rem;
  color: #666;
  font-size: 1.2rem;
}

@media (max-width: 900px) {
  .container {
    grid-template-columns: 1fr;
    padding: 20px;
  }
}
</style>
