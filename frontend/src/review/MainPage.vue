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
      <UserProfileCard />

      <div class="reviews-grid">
        <ReviewList :reviews="reviews" />
      </div>
    </div>
  </div>
</template>

<script setup>
import FiltersPanel from '@/review/mainpage/FiltersPanel.vue'
import UserProfileCard from '@/review/mainpage/UserPanel.vue'
import ReviewList from './mainpage/ReviewList.vue'

import { ref, watch } from 'vue'

const dateFilter = ref(null)
const manufacturerFilter = ref('')
const modelFilter = ref('')

const reviews = ref([])

const fetchReviews = async () => {
  console.log('🔍 fetchReviews: начало запроса...')

  try {
    const params = new URLSearchParams()


    if (dateFilter.value) {
      params.append('filterType', 'DATE')
      params.append('value', dateFilter.value)

    }
    else if (manufacturerFilter.value) {
      params.append('filterType', 'MANUFACTURER')
      params.append('value', manufacturerFilter.value)
    }
    else if (modelFilter.value) {
      params.append('filterType', 'DEVICE_MODEL')
      params.append('value', modelFilter.value)
    }
    else {
      params.append('filterType', 'DEVICE_TYPE')
      params.append('value', 'phone')
    }
    const url = `http://reviewphoneserve:8080/reviews/filter?${params.toString()}`
    console.log(`📡 Запрос к API: ${url}`)

    const response = await fetch(url, {
      method: 'GET',
      credentials: 'include'
    })

    console.log(`✅ Ответ получен: status = ${response.status}`)
    if (!response.ok) throw new Error(`Ошибка загрузки: ${response.status}`)

    const data = await response.json()
    console.log('📦 Полученные данные:', data)
    reviews.value = data
  } catch (error) {
    console.error('❌ Ошибка при получении обзоров:', error)
  }
}


// Автоматический вызов при изменении фильтров
watch([dateFilter, manufacturerFilter, modelFilter], () => {
  fetchReviews()
})

// Также можно вызвать один раз при монтировании
fetchReviews()
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
