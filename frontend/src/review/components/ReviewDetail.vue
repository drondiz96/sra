<template>
  <div v-if="review" class="review-details">
    <h1 class="review-title">{{ review.title }}</h1>

    <h2 class="review-device">
      {{ review.device.manufacturer }} {{ review.device.model }} ({{ review.device.deviceType }})
    </h2>

    <p class="review-content">
      {{ review.content }}
    </p>

    <div class="review-footer">
      <span class="review-author">Автор: {{ review.author.username }}</span>
      <span class="review-date">{{ formatDate(review.dateOfCreation) }}</span>
    </div>

    <div v-if="review.device.imageUrl" class="review-image">
      <img :src="review.device.imageUrl" alt="Изображение устройства" />
    </div>

    <CommentsSection 
      :reviewId="route.params.id"
      :showInput="true"
      title="Отзывы пользователей"
    />
  </div>

  <div v-else class="loading">
    Загрузка...
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import CommentsSection from '@/review/components/comments/CommentsSection.vue'

const route = useRoute()
const review = ref(null)

const fetchReview = async () => {
  try {
    const response = await fetch(`http://localhost:8080/reviews/${route.params.id}`, {
      method: 'GET',
      credentials: 'include'
    })

    if (!response.ok) throw new Error('Ошибка загрузки обзора')

    review.value = await response.json()
  } catch (e) {
    console.error(e)
  }
}

onMounted(fetchReview)

const formatDate = (dateStr) => {
  return new Date(dateStr).toLocaleString()
}
</script>

<style scoped>
.review-details {
  margin: 4rem auto 0 auto;
  padding: 2rem;
  background: white;
  border-radius: 12px;
  box-shadow: 0 2px 15px rgba(0, 0, 0, 0.05);
  max-width: 1200px;
  box-sizing: border-box;
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.review-title {
  font-size: 1.8rem;
  font-weight: bold;
  color: #2c3e50;
  margin-bottom: 0.5rem;
}

.review-device {
  font-size: 1.2rem;
  font-weight: 500;
  color: #555;
  margin-bottom: 1rem;
}

.review-content {
  font-size: 1.1rem;
  line-height: 1.7;
  color: #333;
  text-align: justify;
  padding: 0.5rem 0;
}

.review-footer {
  display: flex;
  justify-content: flex-start;
  gap: 1.5rem;
  font-size: 0.9rem;
  color: #777;
  margin-top: auto;
}

.review-image {
  margin-top: 1.5rem;
}

.review-image img {
  max-width: 100%;
  border-radius: 10px;
  box-shadow: 0 1px 8px rgba(0, 0, 0, 0.05);
}

.loading {
  text-align: center;
  padding: 2rem;
  color: #666;
  font-style: italic;
}
</style>
