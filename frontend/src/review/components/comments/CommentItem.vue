<template>
  <div class="comment-card">
    <div v-if="editing">
      <textarea class="edit-textarea" v-model="editContent"></textarea>
      <div class="edit-actions">
        <button class="comment-button" @click="submitEdit">Сохранить</button>
        <button class="comment-button cancel" @click="cancelEdit">Отмена</button>
      </div>
    </div>
    <div v-else>
      <div class="comment-header">
        <span class="comment-author">{{ comment.authorName || ('Пользователь #' + (index + 1)) }}</span>
        <span class="comment-date">{{ comment.date }}</span>
      </div>
      <p class="comment-text">{{ comment.text }}</p>
      <div class="comment-actions">
        <button class="action-button" @click="startEdit">Редактировать</button>

        <button
          v-if="confirmingDelete"
          class="action-button"
          @click="confirmingDelete = false"
        >
          Отмена
        </button>

        <button
          v-if="confirmingDelete"
          class="action-button danger"
          @click="handleDelete"
        >
          Подтвердить
        </button>

        <span v-else>
          <button class="action-button danger" @click="confirmingDelete = true">Удалить</button>
        </span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { defineProps, ref, defineEmits } from 'vue'


const props = defineProps({
  comment: {
    type: Object,
    required: true,
    validator: (c) => 'id' in c && 'text' in c && 'date' in c
  },
  index: {
    type: Number,
    default: 0
  }
})

const emit = defineEmits(['deleted', 'updated'])

const editing = ref(false)
const editContent = ref('')
const confirmingDelete = ref(false)

const startEdit = () => {
  editing.value = true
  editContent.value = props.comment.text
}

const cancelEdit = () => {
  editing.value = false
  editContent.value = ''
}

const submitEdit = async () => {
  const content = editContent.value.trim()
  if (!content) return

  try {
    const response = await fetch(`http://reviewphoneserve:8080/comments/`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      credentials: 'include',
      body: JSON.stringify({ id: props.comment.id, content })
    })

    if (!response.ok) throw new Error('Ошибка при обновлении')

    emit('updated', { id: props.comment.id, text: content })
    editing.value = false
    editContent.value = ''
  } catch (err) {
    console.error('Ошибка обновления:', err)
  }
}

const handleDelete = async () => {
  try {
    const response = await fetch(`http://reviewphoneserve:8080/comments/${props.comment.id}`, {
      method: 'DELETE',
      credentials: 'include'
    })

    if (!response.ok) throw new Error('Ошибка удаления')

    emit('deleted', props.comment.id)
  } catch (err) {
    console.error('Ошибка удаления:', err)
  }
}
</script>

<style scoped>
.comment-card {
  background: white;
  padding: 16px;
  border-radius: 12px;
  margin-bottom: 12px;
  border: 1px solid #e0e0e0;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.05);
  box-sizing: border-box;
}

.comment-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
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
  font-size: 0.95rem;
  line-height: 1.5;
  margin: 0;
  word-break: break-word;
  text-align: left;
}

.comment-actions {
  margin-top: 0.5rem;
  display: flex;
  gap: 0.5rem;
}

.edit-textarea {
  width: 100%;
  box-sizing: border-box; /* Важный момент, чтобы padding и border не ломали ширину */
  border-radius: 12px;
  padding: 12px;
  font-size: 1rem;
  resize: vertical;
  border: 1px solid #3498db; /* Акцент на бордер в режиме редактирования */
  outline: none;
}

.action-button {
  background: transparent;
  border: none;
  color: #3498db;
  cursor: pointer;
  padding: 0;
}

.action-button.danger {
  color: #e74c3c;
}

.action-button:hover {
  text-decoration: underline;
}

.comment-input {
  width: 100%;
  padding: 1rem;
  border: 2px solid #e0e0e0;
  border-radius: 10px;
  resize: vertical;
  font-size: 1rem;
  transition: all 0.3s ease;
}

.comment-button {
  padding: 10px 18px;
  background: #3498db;
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
}

.comment-button.cancel {
  background: #aaa;
}
</style>
