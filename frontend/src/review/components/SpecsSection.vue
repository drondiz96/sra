<template>
  <div class="specs-section">
    <h1>{{ name }}</h1>
    <div class="price">{{ formatPrice(price) }}</div>
    
    <div class="specs-grid">
      <div v-for="(value, key) in specs" :key="key" class="spec-item">
        <span class="spec-label">{{ key }}:</span>
        <span class="spec-value">{{ value }}</span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { defineProps } from 'vue'
import { formatPrice } from '@/review/utils/formatters' // Добавьте эту строку

defineProps({
  name: String,
  price: Number,
  specs: {
    type: Object,
    default: () => ({})
  }
})
</script>

<style scoped>
.specs-section {
  background: white;
  border-radius: 12px;
  box-shadow: 0 2px 15px rgba(0, 0, 0, 0.05);
  padding: 2rem;
  margin-top: 2rem;
  box-sizing: border-box;
}

.specs-section h1 {
  font-size: 1.5rem;
  color: #2c3e50;
  margin-bottom: 1rem;
}

.price {
  font-size: 1.6rem;
  color: #27ae60;
  font-weight: 600;
  margin-bottom: 2rem;
}

.specs-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1rem;
}

.spec-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1rem 1.2rem;
  background: #f9f9f9;
  border: 1px solid #eee;
  border-radius: 10px;
  box-shadow: 0 1px 3px rgba(0,0,0,0.04);
  font-size: 0.95rem;
}

.spec-label {
  font-weight: 500;
  color: #34495e;
}

.spec-value {
  color: #7f8c8d;
  font-weight: 500;
  text-align: right;
}

/* Адаптивность */
@media (max-width: 768px) {
  .specs-section {
    padding: 1.2rem;
  }

  .specs-grid {
    grid-template-columns: 1fr;
  }
}
</style>