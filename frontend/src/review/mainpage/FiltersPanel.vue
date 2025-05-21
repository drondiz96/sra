<template>
  <div class="filters-sidebar">
    <div class="filter-group">
      <h3>Фильтры</h3>

      <div class="filter-item">
        <label for="date">Дата</label>
        <input 
          type="date" 
          :value="dateFilter"
          @input="$emit('update:dateFilter', $event.target.value)"
        >
      </div>

      <div class="filter-item">
        <label for="manufacturer">Производитель</label>
        <input 
          type="text" 
          :value="manufacturerFilter"
          placeholder="Напр. Samsung"
          @input="$emit('update:manufacturerFilter', $event.target.value)"
        >
      </div>

      <div class="filter-item">
        <label for="model">Модель</label>
        <input 
          type="text" 
          :value="modelFilter"
          placeholder="Напр. Galaxy S24"
          @input="$emit('update:modelFilter', $event.target.value)"
        >
      </div>
    </div>
  </div>
</template>

<script setup>
import { defineProps, defineEmits } from 'vue'

defineProps({
  dateFilter: String,
  manufacturerFilter: String,
  modelFilter: String,
})

defineEmits([
  'update:dateFilter',
  'update:manufacturerFilter',
  'update:modelFilter',
])
</script>

<style scoped>
.filters-sidebar {
  background: white;
  padding: 20px;
  border-radius: 12px;
  box-shadow: 0 2px 15px rgba(0, 0, 0, 0.05);
  position: sticky;
  top: 30px;
  height: fit-content;
  max-width: 100%;
  box-sizing: border-box;
}

.filter-group h3 {
  margin-bottom: 20px;
  font-size: 1.4rem;
  color: #2c3e50;
}

.filter-item {
  margin-bottom: 20px;
}

input[type="text"],
input[type="date"] {
  width: 100%;
  padding: 12px 15px;
  border: 2px solid #e0e0e0;
  border-radius: 10px;
  font-size: 1rem;
  transition: all 0.3s ease;
  box-sizing: border-box;
}

input:focus {
  outline: none;
  border-color: #3498db;
  box-shadow: 0 0 0 3px rgba(52, 152, 219, 0.1);
}

@media (max-width: 900px) {
  .filters-sidebar {
    position: static;
    margin-bottom: 30px;
  }
}
</style>
