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
      />
    </div>

    <div v-else class="no-comments">
      Пока нет комментариев. Будьте первым!
    </div>
  </div>
</template>

<script setup>
import { ref, defineEmits, defineProps } from 'vue'
import CommentItem from './CommentItem.vue'

defineProps({
  comments: {
    type: Array,
    required: true
  },
  title: {
    type: String,
    default: 'Комментарии пользователей'
  },
  showInput: {
    type: Boolean,
    default: true
  }
})

const emit = defineEmits(['add-comment'])
const newComment = ref('')

const handleAddComment = () => {
  if (newComment.value.trim()) {
    emit('add-comment', newComment.value.trim())
    newComment.value = ''
  }
}
</script>

<style scoped>
.comments-section {
  margin-top: 4rem;
  padding: 25px;
  background: white;
  border-radius: 12px;
  box-shadow: 0 2px 15px rgba(0, 0, 0, 0.05);
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
  .comments-section {
    padding: 1rem;
  }

  .comment-button {
    width: 100%;
    align-self: stretch;
  }
}
</style>
