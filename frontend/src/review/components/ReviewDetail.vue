<template>
  <div v-if="review" class="review-details">
    <!-- <h1 class="review-title">{{ review.title }}</h1>

    <h2 class="review-device">
      {{ review.device.manufacturer }} {{ review.device.model }} ({{ review.device.deviceType }})
    </h2> -->
    <h1 class="review-title">
      Обзоры на смартфон {{ review.device.manufacturer }} {{ review.device.model }}
    </h1>

    <!-- <p class="review-content">
      {{ review.content }}
    </p> -->

    <div v-if="review.externalReviews?.length" class="external-reviews">
      <div 
        v-for="ext in review.externalReviews" 
        :key="ext.id" 
        class="external-review-card"
      >
        <h3 class="external-review-title">
          <a :href="ext.url" target="_blank" rel="noopener noreferrer">
            {{ ext.title }}
          </a>
        </h3>

        <p class="external-review-content">
          {{ ext.content }}
        </p>

        <div class="external-review-meta">
          <div class="external-review-info">
            <div class="author-date">
              <div class="author">Автор: {{ ext.author }}</div>
              <div class="date">Дата: {{ ext.date }}</div>
            </div>
            <a
              :href="ext.url"
              target="_blank"
              rel="noopener noreferrer"
              class="source"
            >
              Источник: {{ ext.source }}
            </a>
          </div>
        </div>
      </div>
    </div>
    <div v-else class="no-external-reviews">
      Обзоры не найдены.
    </div>


    <div class="review-footer">
      <span class="review-author">Смартфон добавил пользователь: {{ review.author.username }}</span>
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
    const response = await fetch(`http://reviewphoneserve:8080/reviews/${route.params.id}`, {
      method: 'GET',
      credentials: 'include'
    })

    if (!response.ok) throw new Error('Ошибка загрузки обзора')

    review.value = await response.json()
    console.log(review)
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






.external-reviews {
  margin-top: 2rem;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.external-review-card {
  border: 1px solid #eee;
  padding: 1.5rem;
  margin-top: 1.5rem;
  border-radius: 12px;
  background: #fafafa;
  max-width: 900px;
  width: 100%;
  box-sizing: border-box;
  text-align: center;
}

.external-review-title {
  font-size: 1.3rem;
  font-weight: 600;
  margin-bottom: 1rem;
}


.external-review-title a {
  color: #3498db;
  text-decoration: none;
}

.external-review-title a:hover {
  text-decoration: underline;
}

.external-review-content {
  font-size: 1rem;
  color: #333;
  line-height: 1.6;
  text-align: justify;
  margin-bottom: 1rem;
}

.external-review-meta {
  margin-top: 1rem;
  font-size: 0.85rem;
  color: #777;
}

.external-review-info {
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
  font-size: 0.9rem;
  color: #555;
}


.author-date {
  display: flex;
  flex-direction: column;
  gap: 0.1rem;
  text-align: left;
  margin: 0;
  padding: 0;
}

.author,
.date {
  margin: 0;
  padding: 0;
}


.source {
  color: #3498db;
  text-decoration: none;
  white-space: nowrap;
  margin-left: 1rem;
}

.source:hover {
  text-decoration: underline;
}


.no-external-reviews {
  margin-top: 2rem;
  text-align: center;
  font-style: italic;
  color: #888;
}

</style>
