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

      <!-- Список телефонов -->
      <div class="phone-grid">
        <template v-if="!isLoading">
          <PhoneCard 
            v-for="phone in filteredPhones" 
            :key="phone.id" 
            :phone="phone"
          />
        </template>
        <div v-else class="loading">Загрузка...</div>
      </div>
    </div>
  </div>
</template>

<script setup>
import PhoneCard from '@/review/mainpage/PhoneCard.vue'
import FiltersPanel from '@/review/mainpage/FiltersPanel.vue'
import UserProfileCard from '@/review/mainpage/UserPanel.vue'
import { usePhones } from '@/review/data/usePhones'

// Получаем данные и фильтры
const {
  isLoading,
  filteredPhones,
  searchQuery,
  priceRange,
  validatePriceInput,
} = usePhones()

// Временный пользователь, можно заменить позже
const user = {
  id: 1,
  username: 'Иван Иванов',
  avatar: 'https://via.placeholder.com/150'
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

.phone-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
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

@media (max-width: 480px) {
  .phone-grid {
    grid-template-columns: 1fr;
  }
}
</style>
