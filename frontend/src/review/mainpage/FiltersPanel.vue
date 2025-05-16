<template>
  <div class="filters-sidebar">
    <div class="filter-group">
      <h3>Фильтры</h3>
      
      <div class="filter-item">
        <input 
          type="text" 
          :value="searchQuery"
          placeholder="Поиск по модели..."
          @input="$emit('update:searchQuery', $event.target.value)"
        >
      </div>

      <div class="filter-item">
        <label>Цена (₽)</label>
        <div class="price-range">
          <input 
            type="number" 
            :value="priceRange[0]"
            placeholder="От"
            min="0"
            @input="handlePriceInput(0, $event)"
            class="price-input"
          >
          <span class="separator">–</span>
          <input 
            type="number" 
            :value="priceRange[1]"
            placeholder="До"
            min="0"
            @input="handlePriceInput(1, $event)"
            class="price-input"
          >
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { defineProps, defineEmits } from 'vue'

const props = defineProps({
  searchQuery: String,
  priceRange: Array
})

const emit = defineEmits([
  'update:searchQuery',
  'update:priceRange',
  'validate-price'
])

const handlePriceInput = (index, event) => {
  const newPriceRange = [...props.priceRange]
  const value = Number(event.target.value)
  newPriceRange[index] = value >= 0 ? value : null
  emit('update:priceRange', newPriceRange)
  emit('validate-price')
}
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
  overflow-wrap: break-word;
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
input[type="number"] {
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

.price-range {
  display: flex;
  align-items: center;
  gap: 12px;
  flex-wrap: wrap;
}

.price-input {
  flex: 1 1 100px;
  text-align: center;
  padding: 12px;
  min-width: 0;
}

.separator {
  color: #666;
  font-weight: bold;
  text-align: center;
}

/* Medium screens and below */
@media (max-width: 900px) {
  .filters-sidebar {
    position: static;
    margin-bottom: 30px;
  }

  .price-range {
    flex-direction: column;
    align-items: stretch;
  }

  .separator {
    display: none;
  }
}
</style>