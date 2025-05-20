// src/review/data/usePhones.js
import { ref, computed, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { matchesSearch, matchesPrice } from '@/review/utils/filters'
import phoneImage from '@/assets/iphone16.webp'

const mockResponse = [
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
  }
]

export function usePhones() {
  const phones = ref([]) // изначально пусто
  const isLoading = ref(false)

  const route = useRoute()
  const router = useRouter()

  const searchQuery = ref('')
  const priceRange = ref([null, null])

  const validatePriceInput = () => {
    if (priceRange.value[0] < 0) priceRange.value[0] = null
    if (priceRange.value[1] < 0) priceRange.value[1] = null
    if (priceRange.value[0] && priceRange.value[1] && priceRange.value[0] > priceRange.value[1]) {
      [priceRange.value[0], priceRange.value[1]] = [priceRange.value[1], priceRange.value[0]]
    }
    updateFilters()
  }

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

  const updateFilters = () => {
    const queryParams = {
      search: searchQuery.value || undefined,
      price: priceRange.value.some(v => v)
        ? `${priceRange.value[0] || ''}-${priceRange.value[1] || ''}`
        : undefined
    }

    router.replace({ query: queryParams })
  }

  const filteredPhones = computed(() => {
    return phones.value.filter(phone =>
      matchesSearch(phone, searchQuery.value) &&
      matchesPrice(phone, priceRange.value)
    )
  })

  const fetchPhones = async () => {
    isLoading.value = true
    // Заглушка: «эмуляция ответа сервера»
    await new Promise(resolve => setTimeout(resolve, 500))
    phones.value = mockResponse
    isLoading.value = false
    //alert('Здесь должно быть получение телефонов(usePhones.js)')
  }

  watch(() => route.query, () => {
    initFilters()
    fetchPhones()
  })

  initFilters()
  fetchPhones()

  return {
    phones,
    isLoading,
    filteredPhones,
    searchQuery,
    priceRange,
    validatePriceInput,
    updateFilters
  }
}
