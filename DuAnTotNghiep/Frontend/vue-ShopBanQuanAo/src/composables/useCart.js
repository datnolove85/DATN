import { ref, computed } from 'vue'

export function useCart() {
  const cart = ref([])

  const addToCart = (product) => {
    const item = cart.value.find((i) => i.product.id === product.id)

    if (item) {
      if (item.soLuong < product.soLuongTon) item.soLuong++
    } else {
      cart.value.push({ product, soLuong: 1 })
    }
  }

  const increaseQty = (i) => {
    const item = cart.value[i]
    if (item.soLuong < item.product.soLuongTon) item.soLuong++
  }

  const decreaseQty = (i) => {
    const item = cart.value[i]
    if (item.soLuong > 1) item.soLuong--
    else cart.value.splice(i, 1)
  }

  const removeFromCart = (i) => cart.value.splice(i, 1)

  const totalCartPrice = computed(() =>
    cart.value.reduce((s, i) => s + i.product.giaBan * i.soLuong, 0),
  )

  return {
    cart,
    addToCart,
    increaseQty,
    decreaseQty,
    removeFromCart,
    totalCartPrice,
  }
}
