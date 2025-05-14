<template>
  <div class="phone-details">
    <!-- Основная секция -->
    <div class="main-section">
      <!-- Фото слева -->
      <div class="image-section">
        <img 
          :src="phone.image" 
          :alt="phone.name" 
          class="phone-image"
        >
      </div>

      <!-- Характеристики справа -->
      <div class="specs-section">
        <h1>{{ phone.name }}</h1>
        <div class="price">{{ formatPrice(phone.price) }}</div>
        
        <div class="specs-grid">
          <div v-for="(value, key) in phone.specs" :key="key" class="spec-item">
            <span class="spec-label">{{ key }}:</span>
            <span class="spec-value">{{ value }}</span>
          </div>
        </div>
      </div>
    </div>


    <!-- Обзоры в два столбца -->
    <div class="reviews-section">
    <h2>Обзоры</h2>
    <div class="reviews-grid">
      <div 
        v-for="(review, index) in phone.reviews" 
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
            >
              Смотреть обзор →
            </a>
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


  <!-- Секция комментариев -->
    <div class="comments-section">
      <h2>Комментарии пользователей ({{ comments.length }})</h2>
      
      <!-- Форма добавления комментария -->
      <div class="comment-form">
        <textarea
          v-model="newComment"
          placeholder="Оставьте ваш отзыв..."
          class="comment-input"
        ></textarea>
        <button 
          @click="addComment"
          :disabled="!newComment.trim()"
          class="comment-button"
        >
          Отправить
        </button>
      </div>

      <!-- Список комментариев -->
      <div class="comments-list">
        <div 
          v-for="(comment, index) in comments"
          :key="index"
          class="comment-card"
        >
          <div class="comment-header">
            <span class="comment-author">Пользователь #{{ index + 1 }}</span>
            <span class="comment-date">{{ comment.date }}</span>
          </div>
          <p class="comment-text">{{ comment.text }}</p>
        </div>
      </div>

      
    </div>
  </div>
</template>


<script setup>
import { ref, watch  } from 'vue'
import phoneImage from '@/assets/iphone16.webp'

const expandedReviews = ref({})

const toggleReview = (index) => {
  expandedReviews.value = {
    ...expandedReviews.value,
    [index]: !expandedReviews.value[index]
  }
}

const phone = ref({
  id: 1,
  name: 'Xiaomi Redmi Note 12 Pro',
  price: 29999,
  image: phoneImage,
  specs: {
    'Дисплей': '6.67" AMOLED 120Hz',
    'Процессор': 'Snapdragon 778G',
    'Память': '8/256 ГБ',
    'Камера': '108 Мп + 8 Мп + 2 Мп',
    'Батарея': '5000 мАч',
    'ОС': 'Android 13'
  },
  reviews: [
    {
      title: 'Подробный обзор от TechRadar',
      url: 'https://www.youtube.com/watch?v=uhfAIfySwhw&t=41s',
      summary: 'Отличный экран и автономность...'.repeat(5),
      author: 'Иван Петров',
      rating: 4.5
    },
    // ... другие обзоры
  ]
})

const comments = ref([
  {
    text: 'Отличный телефон за свои деньги! Камеры просто огонь!',
    date: '15.03.2024'
  },
  {
    text: 'Быстро заряжается, но немного греется при играх',
    date: '14.03.2024'
  }
])

const newComment = ref('')

watch(newComment, () => {
  const textarea = document.querySelector('.comment-input')
  if (textarea) {
    textarea.style.height = 'auto'
    textarea.style.height = `${textarea.scrollHeight}px`
  }
})

const addComment = () => {
  if (newComment.value.trim()) {
    comments.value.unshift({
      text: newComment.value.trim(),
      date: new Date().toLocaleDateString('ru-RU')
    })
    newComment.value = ''
  }
}

const formatPrice = (price) => {
  return new Intl.NumberFormat('ru-RU', { 
    style: 'currency', 
    currency: 'RUB',
    maximumFractionDigits: 0 
  }).format(price)
}
</script>

<style scoped>
.phone-details {
  max-width: 1200px;
  margin: 0 auto;
  padding: 2rem;
}

.main-section {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 3rem;
  margin-bottom: 4rem;
}

.image-section {
  order: 1;
}

.specs-section {
  order: 2;
  padding-left: 2rem;
}

.price {
  font-size: 1.8rem;
  color: #2ecc71;
  margin: 1rem 0 2rem;
  font-weight: 600;
}

.specs-grid {
  display: grid;
  gap: 1.2rem;
}

.spec-item {
  display: flex;
  justify-content: space-between;
  padding: 1rem;
  background: #f8f9fa;
  border-radius: 8px;
}

.spec-label {
  font-weight: 500;
  color: #2c3e50;
}

.spec-value {
  color: #7f8c8d;
  font-weight: 500;
}

.image-section {
  position: sticky;
  top: 2rem;
  height: fit-content;
}

.phone-image {
  width: 100%;
  max-width: 500px;
  border-radius: 12px;
  box-shadow: 0 4px 6px rgba(0,0,0,0.1);
}

.review-card {
  cursor: pointer;
  transition: background 0.2s;
  border: 1px solid #eee;
  border-radius: 8px;
  padding: 1rem;
  margin-bottom: 0.5rem;
}

.review-card:hover {
  background: #f8f9fa;
}

.review-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.review-controls {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.toggle-icon {
  font-size: 0.8rem;
  color: #666;
}

.review-content {
  margin-top: 1rem;
}

.review-summary {
  color: #444;
  line-height: 1.6;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.review-summary.expanded {
  -webkit-line-clamp: unset;
}

.review-footer {
  display: flex;
  justify-content: space-between;
  margin-top: 1rem;
  font-size: 0.8rem;
  color: #666;
}

.slide-enter-active,
.slide-leave-active {
  transition: all 0.3s ease;
  max-height: 500px;
  overflow: hidden;
}

.slide-enter-from,
.slide-leave-to {
  opacity: 0;
  max-height: 0;
}

.review-link {
  color: #3498db;
  text-decoration: none;
}

.review-link:hover {
  text-decoration: underline;
}

/* Стили для секции комментариев */
.comments-section {
  margin-top: 4rem;
  padding: 2rem;
  background: #f8f9fa;
  border-radius: 12px;
}

.comment-form {
  margin: 2rem 0;
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.comment-input {
  width: 100%;
  height: 100px;
  padding: 1rem;
  border: 2px solid #e0e0e0;
  border-radius: 8px;
  resize: vertical;
}

.comment-button {
  align-self: flex-end;
  padding: 0.8rem 2rem;
  background: #3498db;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  transition: background 0.3s;
}

.comment-button:disabled {
  background: #bdc3c7;
  cursor: not-allowed;
}

.comment-button:hover:not(:disabled) {
  background: #2980b9;
}

.comments-list {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.comment-card {
  background: white;
  padding: 12px;
  border-radius: 8px;
  margin-bottom: 8px;
  border: 1px solid #eee;
}

.comment-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 4px;
  font-size: 0.85rem;
}

.comment-author {
  color: #3498db;
  font-weight: 500;
}

.comment-date {
  color: #95a5a6;
  font-size: 0.8rem;
}

.comment-text {
  color: #2c3e50;
  font-size: 0.9rem;
  line-height: 1.4;
  margin: 0;
  text-align: justify;
  text-justify: inter-word;
  hyphens: auto;
  word-break: break-word;
}


@media (max-width: 768px) {
  .main-section {
    grid-template-columns: 1fr;
  }
  
  .image-section,
  .specs-section {
    order: initial;
    padding: 0;
  }
}

@media (max-width: 768px) {
  .main-section {
    grid-template-columns: 1fr;
    gap: 2rem;
  }

  .reviews-grid {
    grid-template-columns: 1fr;
  }

  .image-section {
    order: -1;
  }

  .phone-image {
    max-width: 100%;
  }

  .comment-card {
    padding: 8px;
  }
  
  .comment-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 2px;
  }
  
  .comment-date {
    font-size: 0.75rem;
  }

  .comment-text {
    text-align: left;
    hyphens: none;
  }
}
</style>