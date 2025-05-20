<template>
  <div class="comments-section">
    <h2>{{ title }} ({{ comments?.length || 0 }})</h2>

    <div v-if="showInput" class="comment-form">
      <textarea
        v-model="newComment"
        placeholder="Оставьте ваш комментарий..."
        class="comment-input"
      ></textarea>
      <button
        @click="handleAddComment"
        :disabled="!newComment.trim()"
        class="comment-button"
      >
        Отправить
      </button>
    </div>

    <div v-if="comments && comments.length" class="comments-list">
      <CommentItem
        v-for="comment in comments"
        :key="comment.id"
        :comment="comment"
        @updated="onCommentUpdated"
        @deleted="onCommentDeleted"
      />
    </div>

    <div v-else class="no-comments">
      Пока нет комментариев. Будьте первым!
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, defineProps } from 'vue'
import { useRoute } from 'vue-router'
import CommentItem from './CommentItem.vue'

defineProps({
  title: {
    type: String,
    default: 'Комментарии пользователей'
  },
  showInput: {
    type: Boolean,
    default: true
  }
})

const route = useRoute()
const comments = ref([])
const newComment = ref('')

const fetchComments = async () => {
  try {
    const response = await fetch(`http://localhost:8080/comments/by-review/${route.params.id}`, {
      method: 'GET',
      credentials: 'include'
    })

    if (!response.ok) throw new Error('Ошибка загрузки комментариев')

    const rawComments = await response.json()
    comments.value = rawComments.map(c => ({
      id: c.id,
      text: c.content,
      date: new Date(c.dateOfCreation).toLocaleString(),
      authorName: c.author?.username ?? null
    }))
  } catch (error) {
    console.error('Ошибка при получении комментариев:', error)
  }
}

const handleAddComment = async () => {
  if (!newComment.value.trim()) return

  try {
    const response = await fetch(`http://localhost:8080/comments/${route.params.id}`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      credentials: 'include',
      body: JSON.stringify({ content: newComment.value.trim() })
    })

    if (!response.ok) throw new Error('Ошибка при добавлении комментария')

    const newC = await response.json()
    comments.value.push({
      id: newC.id,
      text: newC.content,
      date: new Date(newC.dateOfCreation).toLocaleString(),
      authorName: newC.author?.username ?? null
    })

    newComment.value = ''
  } catch (error) {
    console.error('Не удалось добавить комментарий:', error)
  }
}

const onCommentUpdated = (updatedComment) => {
  const index = comments.value.findIndex(c => c.id === updatedComment.id)
  if (index !== -1) {
    comments.value[index].text = updatedComment.text
  }
}

const onCommentDeleted = (deletedId) => {
  comments.value = comments.value.filter(c => c.id !== deletedId)
}

onMounted(fetchComments)
</script>

<style scoped>
.comments-section {
  margin-top: 4rem;
  padding: 25px;
  box-sizing: border-box;
  max-width: 100%;
}

.comments-section h2 {
  font-size: 1.4rem;
  color: #2c3e50;
  margin-bottom: 1.5rem;
}

.comment-form {
  margin: 2rem 0;
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.comment-input {
  height: 100px;
  padding: 1rem;
  border: 2px solid #e0e0e0;
  border-radius: 10px;
  resize: vertical;
  font-size: 1rem;
  transition: all 0.3s ease;
  box-sizing: border-box;
}

.comment-input:focus {
  outline: none;
  border-color: #3498db;
  box-shadow: 0 0 0 3px rgba(52, 152, 219, 0.1);
}

.comment-button {
  align-self: flex-end;
  padding: 12px 24px;
  background: #3498db;
  color: white;
  border: none;
  border-radius: 10px;
  cursor: pointer;
  font-size: 1rem;
  transition: background 0.3s;
}

.comment-button.cancel {
  background: #aaa;
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
  margin-top: 1.5rem;
}

.no-comments {
  text-align: center;
  color: #666;
  padding: 2rem;
  font-style: italic;
}

@media (max-width: 768px) {
  .comment-button {
    width: 100%;
  }
}
</style>
