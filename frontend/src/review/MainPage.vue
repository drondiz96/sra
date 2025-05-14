<template>
  <div class="container">
    <!-- Фильтры слева -->
    <div class="filters-sidebar">
      <div class="filter-group">
        <h3>Фильтры</h3>
        
        <!-- Поле поиска -->
        <div class="filter-item">
          <input 
            type="text" 
            v-model="searchQuery"
            placeholder="Поиск по модели..."
            @input="updateFilters"
          >
        </div>

        <!-- Фильтр по цене -->
        <div class="filter-item">
          <label>Цена (₽)</label>
          <div class="price-range">
            <input 
              type="number" 
              v-model.number="priceRange[0]" 
              placeholder="От"
              min="0"
              @change="validatePriceInput"
              class="price-input"
            >
            <span class="separator">–</span>
            <input 
              type="number" 
              v-model.number="priceRange[1]" 
              placeholder="До"
              min="0"
              @change="validatePriceInput"
              class="price-input"
            >
          </div>
        </div>
      </div>
    </div>

    <!-- Основной контент справа -->
    <div class="content-main">
      <!-- Плитка пользователя -->
      <div class="user-panel">
        <div class="user-card" 
        @click="navigateToProfile">
          <span class="user-login">{{ user.login }}</span>
        </div>
      </div>

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
import { ref, computed, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import PhoneCard from '@/review/PhoneCard.vue'

const validatePriceInput = () => {
  if (priceRange.value[0] < 0) priceRange.value[0] = null
  if (priceRange.value[1] < 0) priceRange.value[1] = null
  if (priceRange.value[0] && priceRange.value[1] && priceRange.value[0] > priceRange.value[1]) {
    [priceRange.value[0], priceRange.value[1]] = [priceRange.value[1], priceRange.value[0]]
  }
  updateFilters()
}
import phoneImage from '@/assets/iphone16.webp'

const mockPhones = [
  {
    id: 1,
    name: 'Apple iPhone 16 Pro Max',
    price: 129990,
    image: phoneImage,
    specs: ['A18 Bionic', '256 ГБ', '48 Мп'],
    reviewsCount: 3,
    commentsCount: 5431,
    storage: 256,
    processor: 'A18 Bionic 3.89 ГГц',
    camera: '48+48+12 Мп',
    ram: '8 ГБ'
  },
  {
    id: 2,
    name: 'Samsung Galaxy S24 Ultra',
    price: 149990,
    image: phoneImage,
    specs: ['Snapdragon 8 Gen 3', '512 ГБ', '200 Мп'],
    reviewsCount: 5,
    commentsCount: 4923,
    storage: 512,
    processor: 'Snapdragon 8 Gen 3',
    camera: '200MP Main + 12MP Ultra-Wide',
    ram: '12 ГБ'
  },
    {
    id: 2,
    name: 'Samsung Galaxy S24 Ultra',
    price: 149990,
    image: phoneImage,
    specs: ['Snapdragon 8 Gen 3', '512 ГБ', '200 Мп'],
    reviewsCount: 5,
    commentsCount: 4923,
    storage: 512,
    processor: 'Snapdragon 8 Gen 3',
    camera: '200MP Main + 12MP Ultra-Wide',
    ram: '12 ГБ'
  },
]

const router = useRouter()
const route = useRoute()

// Состояние фильтров
const searchQuery = ref('')
const priceRange = ref([null, null])
const phones = ref([])
const isLoading = ref(false)

// Инициализация фильтров из URL
const initFilters = () => {
  const query = route.query
  
  if (query.price) {
    const [min, max] = query.price.split('-').map(v => {
      const num = parseInt(v)
      return isNaN(num) ? null : num
    })
    priceRange.value = [min, max]
  }
  
  searchQuery.value = query.search || ''
}

// Обновление URL при изменении фильтров
const updateFilters = () => {
  const queryParams = {
    search: searchQuery.value || undefined,
    price: priceRange.value.some(v => v) 
      ? `${priceRange.value[0] || ''}-${priceRange.value[1] || ''}`
      : undefined
  }

  router.replace({ query: queryParams })
}

// Фильтрация
const filteredPhones = computed(() => {
  return phones.value.filter(phone => 
    matchesSearch(phone) &&
    matchesPrice(phone)
  )
})

const matchesSearch = (phone) => {
  return phone.name.toLowerCase().includes(searchQuery.value.toLowerCase())
}

const matchesPrice = (phone) => {
  const [min, max] = priceRange.value
  const validMin = typeof min === 'number' && min >= 0
  const validMax = typeof max === 'number' && max >= 0
  
  return (!validMin || phone.price >= min) && 
         (!validMax || phone.price <= max)
}

// Инициализация компонента
const fetchPhones = async () => {
  try {
    isLoading.value = true
    // Имитация загрузки данных
    await new Promise(resolve => setTimeout(resolve, 500))
    phones.value = mockPhones
  } finally {
    isLoading.value = false
  }
}

initFilters()
fetchPhones()

watch(() => route.query, () => {
  initFilters()
  fetchPhones()
})

const user = ref({
  login: "Александр_Иванов"
})
const navigateToProfile = () => {
  router.push({
    name: 'UserProfile',
    params: { username: user.value.login }
  })
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

.filters-sidebar {
  background: white;
  padding: 25px;
  border-radius: 12px;
  box-shadow: 0 2px 15px rgba(0,0,0,0.05);
  position: sticky;
  top: 30px;
  height: fit-content;
}

.filter-group h3 {
  margin: 0 0 25px 0;
  font-size: 1.4rem;
  color: #2c3e50;
}

.filter-item {
  margin-bottom: 25px;
}

input[type="text"], 
input[type="number"],
select {
  width: 100%;
  padding: 12px 15px;
  border: 2px solid #e0e0e0;
  border-radius: 10px;
  font-size: 1rem;
  transition: all 0.3s ease;
}

input:focus,
select:focus {
  outline: none;
  border-color: #3498db;
  box-shadow: 0 0 0 3px rgba(52,152,219,0.1);
}

.price-range {
  display: grid;
  grid-template-columns: 1fr 24px 1fr;
  gap: 12px;
  align-items: center;
}

.price-input {
  text-align: center;
  padding: 12px;
}

.separator {
  color: #666;
  text-align: center;
  font-weight: bold;
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

  .filters-sidebar {
    position: static;
    margin-bottom: 30px;
  }

  .price-range {
    grid-template-columns: 1fr;
  }
  
  .price-input {
    width: 100%;
  }
}

@media (max-width: 480px) {
  .phone-grid {
    grid-template-columns: 1fr;
  }
  
  input[type="text"], 
  input[type="number"],
  select {
    font-size: 0.9rem;
    padding: 10px;
  }
}


/* Добавляем стили для пользовательской панели */
.user-panel {
  position: absolute;
  top: 30px;
  right: 30px;
  z-index: 10;
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
}

.user-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0,0,0,0.15);
}

.user-login {
  font-weight: 500;
  color: #2c3e50;
  font-size: 0.95rem;
}

/* Адаптивность */
@media (max-width: 900px) {
  .user-panel {
    position: static;
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