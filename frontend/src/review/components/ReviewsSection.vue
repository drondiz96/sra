<template>
  <div class="reviews-section">
    <div class="reviews-header">
      <h2>Обзоры</h2>
      <button class="add-review-btn" @click="addReview">+ Добавить обзор</button>
    </div>

    <div v-if="reviews && reviews.length === 0" class="no-reviews">
      Пока нет обзоров. Будьте первым, кто добавит!
    </div>

    <div v-else class="reviews-grid">
      <div 
        v-for="(review, index) in reviews" 
        :key="index" 
        class="review-card"
        @click="toggleReview(index)"
      >
        <div class="review-header">
          <h3>{{ review.title }}</h3>
          <div class="review-controls">
            <a 
              :href="review.url" 
              target="_blank" 
              class="review-link"
              @click.stop
            >Смотреть обзор</a>
            <span class="toggle-icon">
              {{ expandedReviews[index] ? '▼' : '▶' }}
            </span>
          </div>
        </div>

        <transition name="slide">
          <div v-if="expandedReviews[index]" class="review-content">
            <p class="review-summary">{{ review.summary }}</p>
            <div class="review-footer">
              <span class="review-author">Автор: {{ review.author }}</span>
              <span class="review-rating">Оценка: {{ review.rating }}/5</span>
            </div>
          </div>
        </transition>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, defineProps, computed } from 'vue'

const props = defineProps({
  reviews: {
    type: Array,
    default: () => []
  }
})

const reviews = computed(() => props.reviews)

const expandedReviews = ref({})

const toggleReview = (index) => {
  expandedReviews.value = {
    ...expandedReviews.value,
    [index]: !expandedReviews.value[index]
  }
}

const addReview = () => {
  // Здесь можно открыть модалку, перейти на страницу, вызвать emit и т.д.
  alert('Добавление обзора пока не реализовано(ReviewsSection.vue).')
}
</script>


<style scoped>
.reviews-section {
  margin-top: 3rem;
  padding: 25px;
  background: white;
  border-radius: 12px;
  box-shadow: 0 2px 15px rgba(0, 0, 0, 0.05);
  box-sizing: border-box;
}

.reviews-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1.5rem;
}

.reviews-header h2 {
  font-size: 1.4rem;
  color: #2c3e50;
  margin: 0;
}

.add-review-btn {
  background-color: #3498db;
  color: white;
  border: none;
  border-radius: 8px;
  padding: 0.5rem 1rem;
  font-size: 0.9rem;
  cursor: pointer;
  transition: background 0.2s ease;
}

.add-review-btn:hover {
  background-color: #2980b9;
}

.no-reviews {
  text-align: center;
  color: #999;
  font-style: italic;
  font-size: 0.95rem;
}

.reviews-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1rem;
}

.review-card {
  cursor: pointer;
  transition: box-shadow 0.2s ease;
  border: 1px solid #e0e0e0;
  border-radius: 12px;
  padding: 1.2rem;
  background: white;
  box-shadow: 0 1px 4px rgba(0,0,0,0.05);
}

.review-card:hover {
  box-shadow: 0 3px 10px rgba(0,0,0,0.1);
}

.review-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  flex-wrap: wrap;
  gap: 0.5rem;
}

.review-header h3 {
  font-size: 1.1rem;
  color: #2c3e50;
  margin: 0;
  max-width: 60%;
  word-break: break-word;
}

.review-controls {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 0.5rem;
}

.review-link {
  color: #3498db;
  font-weight: 500;
  text-decoration: none;
  font-size: 0.9rem;
  white-space: nowrap;
}

.review-link:hover {
  text-decoration: underline;
}

.toggle-icon {
  font-size: 0.85rem;
  color: #7f8c8d;
}

.review-content {
  margin-top: 1rem;
  overflow: hidden;
}

.review-summary {
  color: #444;
  line-height: 1.6;
  font-size: 0.95rem;
  text-align: justify;
}

.review-footer {
  display: flex;
  justify-content: space-between;
  margin-top: 1rem;
  font-size: 0.8rem;
  color: #666;
}

/* Анимация раскрытия */
.slide-enter-active,
.slide-leave-active {
  transition: all 0.3s ease;
}

.slide-enter-from,
.slide-leave-to {
  opacity: 0;
  transform: translateY(-5px);
}

/* Мобильные устройства */
@media (max-width: 768px) {
  .reviews-grid {
    grid-template-columns: 1fr;
  }

  .review-header h3 {
    max-width: 100%;
  }

  .review-controls {
    flex-direction: row;
    justify-content: flex-start;
    width: 100%;
  }
}
</style>