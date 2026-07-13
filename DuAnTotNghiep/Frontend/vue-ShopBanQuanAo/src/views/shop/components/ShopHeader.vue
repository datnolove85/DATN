<template>
  <header
    :class="[
      'sticky top-0 z-50 transition-all duration-300',
      scrolled ? 'bg-white/95 backdrop-blur-lg shadow-md' : 'bg-white border-b border-gray-100',
    ]"
  >
    <div class="max-w-7xl mx-auto h-20 px-6 flex items-center justify-between gap-6">
      <RouterLink to="/home" class="text-3xl font-black tracking-tight select-none">
        Vel<span class="text-indigo-600">ora</span>
      </RouterLink>

      <nav class="hidden xl:flex items-center gap-8 font-medium text-gray-700">
        <RouterLink
          v-for="item in menus"
          :key="item.name"
          :to="item.path"
          class="hover:text-indigo-600 transition"
        >
          {{ item.name }}
        </RouterLink>
      </nav>

      <div class="flex items-center gap-3">
        <div class="relative" @mouseenter="openCart" @mouseleave="closeCart">
          <button
            type="button"
            class="relative w-10 h-10 rounded-full hover:bg-gray-100 flex items-center justify-center transition"
            @click.stop="toggleCart"
          >
            <ShoppingBag class="w-5 h-5" />

            <span
              v-if="cartCount > 0"
              class="absolute -top-1 -right-1 min-w-[20px] h-5 px-1 rounded-full bg-red-500 text-white text-[11px] flex items-center justify-center"
            >
              {{ cartCount }}
            </span>
          </button>

          <transition
            enter-active-class="transition duration-200"
            leave-active-class="transition duration-150"
            enter-from-class="opacity-0 translate-y-2"
            leave-to-class="opacity-0 translate-y-2"
          >
            <div
              v-if="showCart"
              class="absolute right-0 mt-4 w-[380px] bg-white rounded-2xl shadow-2xl border border-gray-100 overflow-hidden z-50"
            >
              <div
                class="px-5 py-4 border-b border-gray-100 flex justify-between items-center bg-white"
              >
                <h3 class="font-bold text-lg">🛒 Giỏ hàng</h3>

                <span class="text-sm text-gray-500"> {{ cartCount }} sản phẩm </span>
              </div>

              <div v-if="cart.length === 0" class="py-12 text-center text-gray-400 bg-white">
                <ShoppingBag class="mx-auto w-10 h-10 mb-3 text-gray-300" />

                <p>Giỏ hàng đang trống</p>
              </div>

              <div v-else class="max-h-[380px] overflow-y-auto bg-white">
                <div
                  v-for="item in cart"
                  :key="item.id"
                  class="flex gap-3 px-5 py-4 hover:bg-gray-50 transition border-b border-gray-100"
                >
                  <div class="w-16 h-16 rounded-xl bg-gray-100 overflow-hidden flex-shrink-0">
                    <img
                      v-if="item.anh"
                      :src="'http://localhost:8080' + item.anh"
                      class="w-full h-full object-cover"
                    />

                    <div
                      v-else
                      class="w-full h-full flex items-center justify-center text-gray-300"
                    >
                      📦
                    </div>
                  </div>

                  <div class="flex-1 min-w-0">
                    <p class="font-semibold text-sm truncate">
                      {{ item.tenSanPham }}
                    </p>

                    <p class="text-xs text-gray-500 mt-1">{{ item.mauSac }} • {{ item.kichCo }}</p>

                    <div class="flex justify-between items-center mt-2">
                      <span class="text-sm text-gray-500">
                        {{ item.soLuong }} × {{ Number(item.giaBan).toLocaleString('vi-VN') }}đ
                      </span>

                      <span class="font-bold text-indigo-600 text-sm">
                        {{ Number(item.thanhTien).toLocaleString('vi-VN') }}đ
                      </span>
                    </div>
                  </div>
                </div>
              </div>

              <div v-if="cart.length" class="border-t border-gray-100 p-5 bg-gray-50">
                <div class="flex justify-between mb-4">
                  <span class="font-medium"> Tổng tiền </span>

                  <span class="font-bold text-lg text-indigo-600">
                    {{ totalPrice.toLocaleString('vi-VN') }}đ
                  </span>
                </div>

                <RouterLink
                  to="/admin/giohang"
                  @click="closeCartImmediately"
                  class="block w-full text-center py-3 rounded-xl bg-black text-white font-semibold hover:bg-gray-800 transition"
                >
                  Xem giỏ hàng
                </RouterLink>
              </div>
            </div>
          </transition>
        </div>

        <button
          class="w-10 h-10 rounded-full hover:bg-gray-100 flex items-center justify-center transition"
        >
          <User class="w-5 h-5" />
        </button>
      </div>
    </div>
  </header>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { RouterLink } from 'vue-router'
import { Heart, ShoppingBag, User, GitCompare } from 'lucide-vue-next'
import axios from 'axios'
import emitter from '@/utils/emitter'

const menus = [
  {
    name: 'Trang chủ',
    path: '/home',
  },
  {
    name: 'Sản phẩm',
    path: '/san-pham',
  },
  {
    name: 'Khuyến mãi',
    path: '/promotion',
  },
  {
    name: 'Liên hệ',
    path: '/contact',
  },
]

const scrolled = ref(false)

const cart = ref([])

const showCart = ref(false)

let cartTimeout = null // Khai báo biến giữ thời gian chờ

const handleScroll = () => {
  scrolled.value = window.scrollY > 15
}

/**
 * Tổng số lượng sản phẩm
 */
const cartCount = computed(() => {
  return cart.value.reduce((sum, item) => sum + Number(item.soLuong), 0)
})

/**
 * Tổng tiền
 */
const totalPrice = computed(() => {
  return cart.value.reduce((sum, item) => {
    return sum + Number(item.thanhTien)
  }, 0)
})

// const loadCart = async () => {
//   try {
//     const token = sessionStorage.getItem('token')

//     if (!token) {
//       cart.value = []
//       return
//     }

//     // const res = await axios.get('http://localhost:8080/giohang', {
//     //   headers: {
//     //     Authorization: `Bearer ${token}`,
//     //   },
//     // })
//     console.log(res.data)

//     cart.value = res.data || []

//     console.log('Cart:', cart.value)
//   } catch (err) {
//     console.log(err)

//     cart.value = []
//   }
// }

// onMounted(() => {
//   window.addEventListener('scroll', handleScroll)
//   emitter.on('cart-updated', () => {
//     console.log('Header nhận được sự kiện')

//     loadCart()
//   })
//   loadCart()
// })

// onUnmounted(() => {
//   window.removeEventListener('scroll', handleScroll)

//   emitter.off('cart-updated', loadCart)
// })

const openCart = () => {
  // Nếu đang có lệnh đóng giỏ hàng chuẩn bị chạy, thì hủy bỏ lệnh đó đi
  if (cartTimeout) {
    clearTimeout(cartTimeout)
  }
  showCart.value = true
}

const closeCart = () => {
  // Gán thời gian chờ vào biến cartTimeout để có thể hủy nếu người dùng rê chuột lại
  cartTimeout = setTimeout(() => {
    showCart.value = false
  }, 250) // Tăng thời gian delay lên 250ms cho an toàn khi rê chuột qua khoảng trống
}

const toggleCart = () => {
  showCart.value = !showCart.value
}

const closeCartImmediately = () => {
  showCart.value = false
}
</script>
