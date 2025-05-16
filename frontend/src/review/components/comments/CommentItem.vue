<template>
  <div class="comment-card">
    <div class="comment-header">
      <span class="comment-author">Пользователь #{{ index + 1 }}</span>
      <span class="comment-date">{{ comment.date }}</span>
    </div>
    <p class="comment-text">{{ comment.text }}</p>
  </div>
</template>

<script setup>
import { defineProps } from 'vue'

defineProps({
  comment: {
    type: Object,
    required: true,
    validator: (c) => 'id' in c && 'text' in c && 'date' in c
  }
})
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

/* Адаптивность */
@media (max-width: 768px) {
  .comment-card {
    padding: 12px;
  }

  .comment-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 4px;
  }

  .comment-date {
    font-size: 0.75rem;
  }
}
</style>