export const matchesSearch = (phone, searchQuery) => {
  // Добавляем проверки на существование данных
  if (!phone?.name || !searchQuery) return true
  return phone.name.toLowerCase().includes(searchQuery.toLowerCase().trim())
}

export const matchesPrice = (phone, priceRange) => {
  const [min, max] = priceRange || []
  const validMin = typeof min === 'number' && min >= 0
  const validMax = typeof max === 'number' && max >= 0

  // Добавляем проверку на существование цены телефона
  if (typeof phone?.price !== 'number') return false
  
  return (!validMin || phone.price >= min) && 
         (!validMax || phone.price <= max)
}