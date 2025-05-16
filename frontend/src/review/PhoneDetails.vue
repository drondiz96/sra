<template>
  <div class="phone-details">
    <div class="main-section">
      <div class="image-section">
        <img :src="phone.image" :alt="phone.name" class="phone-image">
      </div>

      <SpecsSection
        :name="phone.name"
        :price="phone.price"
        :specs="phone.specs"
      />
    </div>

    <ReviewsSection :reviews="phone.reviews" />
    
    <CommentsSection 
      :comments="comments"
      @add-comment="addComment"
    />
  </div>
</template>


<script setup>
import { ref, watch  } from 'vue'
import phoneImage from '@/assets/iphone16.webp'
import SpecsSection from '@/review/components/SpecsSection.vue'
import ReviewsSection from '@/review/components/ReviewsSection.vue'
import CommentsSection from '@/review/components/comments/CommentsSection.vue'


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
    {
      title: 'Подробный обзор от TechRadar',
      url: 'https://www.youtube.com/watch?v=uhfAIfySwhw&t=41s',
      summary: 'Отличный экран и автономность...'.repeat(5),
      author: 'Иван Петров',
      rating: 4.5
    },
]
})

const comments = ref([
  {
    id: 1,
    text: 'Пример комментария 1',
    date: '01.01.2024'
  },
  {
    id: 2,
    text: 'Пример комментария 2', 
    date: '02.01.2024'
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

const addComment = (text) => {
  const newComment = {
    id: Date.now(),
    text: text.trim(),
    date: new Date().toLocaleDateString('ru-RU')
  }
  comments.value = [newComment, ...comments.value]
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

@media (max-width: 768px) {
  .main-section {
    grid-template-columns: 1fr;
    gap: 2rem;
  }
  
  .image-section {
    order: -1;
  }
}
</style>