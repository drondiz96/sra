<template>
  <div 
    class="phone-card"
    @click="navigateToDetails"
  >
    <div class="image-wrapper">
      <img 
        :src="phone.image || '/placeholder-phone.jpg'"
        :alt="phone.name"
        class="phone-image"
      >
      <div class="stats-badge">
        <span class="reviews-count">обзоров {{ phone.reviewsCount }}</span>
        <span class="comments-count">комментариев {{ phone.commentsCount }}</span>
      </div>
    </div>
    
    <div class="content">
      <h3 class="title">{{ phone.name }}</h3>
      <div class="price">{{ formatPrice(phone.price) }}</div>
    </div>
  </div>
</template>

<script setup>
import { defineProps } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const props = defineProps({
  phone: {
    type: Object,
    required: true,
    default: () => ({
      id: 0,
      image: '',
      name: 'Unknown',
      price: 0,
      reviewsCount: 0,
      commentsCount: 0
    })
  }
})

const formatPrice = (price) => {
  return new Intl.NumberFormat('ru-RU', {
    style: 'currency',
    currency: 'RUB',
    maximumFractionDigits: 0
  }).format(price)
}

const navigateToDetails = () => {
  router.push({ name: 'PhoneDetails', params: { id: props.phone.id } })
}
</script>

<style scoped>
.phone-card {
  background: white;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
  transition: transform 0.2s;
  cursor: pointer;
}

.phone-card:hover {
  transform: translateY(-3px);
}

.image-wrapper {
  position: relative;
  aspect-ratio: 4/3;
}

.phone-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 12px 12px 0 0;
}

.stats-badge {
  position: absolute;
  bottom: 10px;
  right: 10px;
  background: rgba(0,0,0,0.7);
  color: white;
  padding: 6px 12px;
  border-radius: 20px;
  display: flex;
  gap: 10px;
  font-size: 0.9rem;
  pointer-events: none;
}

.content {
  padding: 1rem;
}

.title {
  margin: 0 0 0.5rem 0;
  font-size: 1.1rem;
  color: #2c3e50;
}

.price {
  color: #27ae60;
  font-weight: 600;
  margin-bottom: 0.8rem;
}

.specs {
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
}

.spec-item {
  background: #f5f6fa;
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 0.85rem;
  color: #4b6584;
}
</style>