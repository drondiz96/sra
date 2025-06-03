<template>
  <div class="container">
    <!-- Фильтры слева -->
    <FiltersPanel 
      :dateFilter="dateFilter"
      :manufacturerFilter="manufacturerFilter"
      :modelFilter="modelFilter"
      @update:dateFilter="val => dateFilter = val"
      @update:manufacturerFilter="val => manufacturerFilter = val"
      @update:modelFilter="val => modelFilter = val"
    />

    <div class="content-main">
      <UserProfileCard/>

      <div class="reviews-grid">
        <ReviewList/>
      </div>
    </div>
  </div>
</template>

<script setup>
import FiltersPanel from '@/review/mainpage/FiltersPanel.vue'
import UserProfileCard from '@/review/mainpage/UserPanel.vue'
import ReviewList from './mainpage/ReviewList.vue'
import { ref } from 'vue'

// Получаем данные и фильтры
const dateFilter = ref(null)
const manufacturerFilter = ref('')
const modelFilter = ref('')
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
