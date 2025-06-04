<template>
  <div class="reviews-section">
    <div class="reviews-header">
      <h2>Смартфоны</h2>
      <button class="add-review-btn" @click="openForm()">+ Добавить смартфон</button>
    </div>

    <div v-if="reviews.length === 0" class="no-reviews">
      Пока нет смартфонов. Будьте первым, кто добавит!
    </div>

    <div v-else class="reviews-list">
      <div
        v-for="review in reviews"
        :key="review.id"
        class="review-card"
        @click="goToReview(review.id)"
      >
        <div class="review-header">
          <h3>Обзоры на {{ review.device.manufacturer }} {{ review.device.model }}</h3>
          <div class="review-controls">
            <a href="#" class="review-link" @click.stop.prevent="openForm(review)">Редактировать</a>
            <a href="#" class="review-link" @click.stop.prevent="deleteReview(review.id)">Удалить</a>
          </div>
        </div>

        <transition name="slide">
          <div class="review-content">
            <div class="review-footer">
              <span class="review-author">Смартфон добавил пользователь: {{ review.author?.username || '—' }}</span>
            </div>
          </div>
        </transition>
      </div>
    </div>

    <!-- Форма добавления/редактирования -->
    <div v-if="showForm" class="modal-overlay">
      <div class="modal">
        <h3>{{ editingReview ? 'Редактировать смартфон' : 'Добавить смартфон' }}</h3>
        <form @submit.prevent="submitReview">
          <input v-model="form.device.model" placeholder="Модель" required />
          <input v-model="form.device.manufacturer" placeholder="Производитель" required />
          <input type="date" v-model="form.device.dateOfCreation" required />
          <input v-model="form.device.imageUrl" placeholder="URL изображения" />

          <div class="modal-actions">
            <button type="submit">{{ editingReview ? 'Сохранить' : 'Создать' }}</button>
            <button type="button" @click="closeForm">Отмена</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>


<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const reviews = ref([])

const showForm = ref(false)
const editingReview = ref(null)

const form = ref({
  id: 0,
  title: '',
  content: '',
  device: {
    model: '',
    manufacturer: '',
    dateOfCreation: new Date().toISOString().split('T')[0],
    imageUrl: ''
  }
})

const openForm = (review = null) => {
  if (review) {
    editingReview.value = review
    form.value = {
      id: review.id,
      title: review.title,
      content: '',
      device: {
        model: review.device.model || '',
        manufacturer: review.device.manufacturer || '',
        dateOfCreation: review.device.dateOfCreation || new Date().toISOString().split('T')[0],
        imageUrl: review.device.imageUrl || ''
      }
    }
  } else {
    editingReview.value = null
    form.value = {
      id: 0,
      title: '',
      content: '',
      device: {
        model: '',
        manufacturer: '',
        dateOfCreation: new Date().toISOString().split('T')[0],
        imageUrl: ''
      }
    }
  }
  showForm.value = true
}

const closeForm = () => {
  showForm.value = false
  editingReview.value = null
}

const submitReview = async () => {
  try {
    // Устанавливаем служебные поля
    form.value.title = `${form.value.device.manufacturer} ${form.value.device.model}`.trim()
    form.value.content = ''
    const device = {
      ...form.value.device,
      deviceType: 'phone' // устанавливаем вручную
    }

    const payload = {
      id: form.value.id,
      title: form.value.title,
      content: '',
      device
    }

    const method = editingReview.value ? 'PUT' : 'POST'
    const url = 'http://reviewphoneserve:8080/reviews/'
    const response = await fetch(url, {
      method,
      headers: { 'Content-Type': 'application/json' },
      credentials: 'include',
      body: JSON.stringify(payload)
    })

    if (!response.ok) {
      throw new Error(`Ошибка: ${response.status}`)
    }

    await fetchReviews()
    closeForm()
  } catch (err) {
    console.error('Ошибка при сохранении обзора:', err)
  }
}

const deleteReview = async (id) => {
  if (!confirm('Удалить смартфон?')) return
  try {
    const response = await fetch(`http://reviewphoneserve:8080/reviews/${id}`, {
      method: 'DELETE',
      credentials: 'include'
    })
    if (!response.ok) throw new Error(`Ошибка удаления: ${response.status}`)

    await fetchReviews('DEVICE_TYPE', 'phone')
  } catch (err) {
    console.error('Ошибка при удалении обзора:', err)
  }
}
const fetchReviews = async () => {
  console.log('🔍 fetchReviews: начало запроса...')

  try {
    const url = 'http://reviewphoneserve:8080/reviews/filter?filterType=DEVICE_TYPE&value=phone'
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


const goToReview = (id) => {
  router.push(`/reviews/${id}`)
}

onMounted(() => {
  fetchReviews()
})
</script>



<style scoped>
.reviews-section {
  margin-top: 3rem;
  padding: 25px;
  background: white;
  border-radius: 12px;
  box-shadow: 0 2px 15px rgba(0, 0, 0, 0.05);
}

.reviews-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1.5rem;
}

.add-review-btn {
  background-color: #3498db;
  color: white;
  border: none;
  border-radius: 8px;
  padding: 0.5rem 1rem;
  font-size: 0.9rem;
  cursor: pointer;
}

.no-reviews {
  text-align: center;
  color: #999;
  font-style: italic;
}

.reviews-list {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.review-card {
  cursor: pointer;
  border: 1px solid #e0e0e0;
  border-radius: 12px;
  padding: 1.2rem;
  background: white;
  transition: box-shadow 0.2s ease;
}

.review-card:hover {
  box-shadow: 0 3px 10px rgba(0, 0, 0, 0.1);
}

.review-header {
  display: flex;
  justify-content: space-between;
  flex-wrap: wrap;
  gap: 0.5rem;
}

.review-link {
  color: #3498db;
  font-size: 0.9rem;
  cursor: pointer;
  text-decoration: none;
}

.review-link:hover {
  text-decoration: underline;
}

.review-summary {
  color: #444;
  font-size: 0.95rem;
  margin-top: 0.5rem;
}

.review-footer {
  margin-top: 1rem;
  font-size: 0.8rem;
  color: #666;
}

.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.4);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 999;
}

.modal {
  background: white;
  padding: 2rem;
  border-radius: 10px;
  width: 500px;
  max-width: 95%;
  box-shadow: 0 5px 20px rgba(0, 0, 0, 0.2);
}

.modal h3 {
  margin-top: 0;
}

.modal input,
.modal textarea {
  width: 100%;
  margin-top: 10px;
  padding: 0.6rem;
  border: 1px solid #ccc;
  border-radius: 6px;
  font-size: 0.95rem;
  resize: none;
}

.modal-actions {
  margin-top: 1rem;
  display: flex;
  justify-content: flex-end;
  gap: 0.5rem;
}

.modal-actions button {
  padding: 0.5rem 1rem;
  border: none;
  border-radius: 6px;
  cursor: pointer;
}

.modal-actions button:first-child {
  background: #3498db;
  color: white;
}

.modal-actions button:last-child {
  background: #ccc;
}
</style>
