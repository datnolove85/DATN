<template>
  <header
    :class="[
      'sticky top-0 z-50 transition-all duration-300',
      scrolled ? 'bg-white/95 backdrop-blur-lg shadow-md' : 'bg-white border-b border-gray-100',
    ]"
  >
    <div class="max-w-[1700px] mx-auto h-20 px-8 flex items-center">
      <!-- ================= LOGO ================= -->

      <div class="w-56 flex-shrink-0">
        <RouterLink to="/home" class="text-3xl font-black tracking-tight select-none">
          K-<span class="text-indigo-600">Zone</span>
        </RouterLink>
      </div>

      <!-- ================= MENU ================= -->

      <nav
        class="hidden xl:flex flex-1 justify-center items-center gap-12 font-semibold text-gray-700"
      >
        <RouterLink
          v-for="item in menus"
          :key="item.name"
          :to="item.path"
          class="relative py-2 hover:text-indigo-600 transition after:absolute after:left-0 after:bottom-0 after:h-[2px] after:w-0 after:bg-indigo-600 after:transition-all hover:after:w-full"
        >
          {{ item.name }}
        </RouterLink>
      </nav>

      <!-- ================= RIGHT ================= -->

      <div class="flex-1 flex justify-end items-center gap-3 min-w-0">
        <!-- SEARCH -->

        <!-- ORDER -->

        <RouterLink
          to="/donhang"
          class="flex items-center gap-2 px-4 h-11 rounded-full hover:bg-gray-100 transition"
        >
          <Package class="w-5 h-5" />

          <span class="hidden xl:block text-sm font-medium whitespace-nowrap"> Đơn hàng </span>
        </RouterLink>

        <!-- CART -->

        <div class="relative" @mouseenter="openCart" @mouseleave="closeCart">
          <button
            type="button"
            class="relative flex items-center gap-2 px-4 h-11 rounded-full hover:bg-gray-100 transition"
            @click.stop="toggleCart"
          >
            <ShoppingBag class="w-5 h-5" />

            <span class="hidden xl:block text-sm font-medium whitespace-nowrap"> Giỏ hàng </span>

            <span
              v-if="cartCount > 0"
              class="absolute -top-1 -right-1 min-w-[20px] h-5 px-1 rounded-full bg-red-500 text-white text-[11px] flex items-center justify-center"
            >
              {{ cartCount }}
            </span>
          </button>

          <!-- CART DROPDOWN -->

          <transition
            enter-active-class="transition duration-200"
            leave-active-class="transition duration-150"
            enter-from-class="opacity-0 translate-y-2"
            leave-to-class="opacity-0 translate-y-2"
          >
            <div
              v-if="showCart"
              class="absolute right-0 mt-4 w-[380px] bg-white rounded-2xl shadow-2xl border overflow-hidden z-50"
            >
              <div class="px-5 py-4 border-b flex justify-between">
                <h3 class="font-bold text-lg">🛒 Giỏ hàng</h3>

                <span class="text-sm text-gray-500"> {{ cartCount }} sản phẩm </span>
              </div>

              <div v-if="cart.length === 0" class="py-12 text-center text-gray-400">
                <ShoppingBag class="mx-auto w-10 h-10 mb-3" />

                <p>Giỏ hàng đang trống</p>
              </div>

              <!-- LIST CART -->

              <div v-else class="max-h-[380px] overflow-y-auto">
                <div
                  v-for="item in cart"
                  :key="item.id"
                  class="flex gap-3 px-5 py-4 border-b hover:bg-gray-50 transition"
                >
                  <div class="w-16 h-16 rounded-full overflow-hidden bg-gray-100 flex-shrink-0">
                    <img
                      v-if="item.anh"
                      :src="'http://localhost:8080' + item.anh"
                      class="w-full h-full object-cover"
                    />

                    <div v-else class="w-full h-full flex items-center justify-center">📦</div>
                  </div>

                  <div class="flex-1 min-w-0">
                    <p class="font-semibold text-sm truncate">
                      {{ item.tenSanPham }}
                    </p>

                    <p class="text-xs text-gray-500 mt-1">{{ item.mauSac }} • {{ item.kichCo }}</p>

                    <div class="flex justify-between mt-2">
                      <span class="text-xs text-gray-500">
                        {{ item.soLuong }}
                        ×
                        {{ Number(item.giaBan).toLocaleString('vi-VN') }}đ
                      </span>

                      <span class="font-bold text-indigo-600 text-sm">
                        {{ Number(item.thanhTien).toLocaleString('vi-VN') }}đ
                      </span>
                    </div>
                  </div>
                </div>
              </div>

              <!-- TOTAL -->

              <div v-if="cart.length" class="p-5 bg-gray-50 border-t">
                <div class="flex justify-between mb-4">
                  <span> Tổng tiền </span>

                  <span class="font-bold text-indigo-600">
                    {{ totalPrice.toLocaleString('vi-VN') }}đ
                  </span>
                </div>

                <RouterLink
                  to="/giohang"
                  @click="closeCartImmediately"
                  class="block text-center py-3 rounded-xl bg-black text-white font-semibold hover:bg-gray-800 transition"
                >
                  Xem giỏ hàng
                </RouterLink>
              </div>
            </div>
          </transition>
        </div>

        <!-- USER -->

        <div class="relative" @mouseenter="openUser = true" @mouseleave="openUser = false">
          <button
            class="flex items-center gap-3 px-3 py-2 rounded-2xl bg-gray-100 hover:bg-gray-200 transition"
          >
            <div
              class="w-11 h-11 rounded-full bg-gradient-to-r from-indigo-600 to-purple-600 text-white flex items-center justify-center font-bold"
            >
              {{ avatar }}
            </div>
            <div class="hidden lg:block text-left whitespace-nowrap">
              <p class="text-sm font-semibold">
                {{ user?.hoTen }}
              </p>

              <p class="text-xs text-gray-500">Khách hàng</p>
            </div>

            <ChevronDown class="w-4 h-4 text-gray-400" />
          </button>

          <!-- USER MENU -->

          <transition
            enter-active-class="transition duration-200"
            enter-from-class="opacity-0 scale-95"
            leave-active-class="transition duration-150"
            leave-to-class="opacity-0 scale-95"
          >
            <div
              v-if="openUser"
              class="absolute right-0 mt-3 w-72 bg-white rounded-2xl shadow-2xl border overflow-hidden"
            >
              <div class="p-5 bg-gradient-to-r from-indigo-600 to-purple-600 text-white">
                <div class="flex items-center gap-3">
                  <div
                    class="w-14 h-14 rounded-full bg-white/20 flex items-center justify-center font-bold text-xl"
                  >
                    {{ avatar }}
                  </div>

                  <div>
                    <p class="font-bold">
                      {{ user?.hoTen }}
                    </p>

                    <p class="text-sm opacity-80">
                      {{ user?.email }}
                    </p>
                  </div>
                </div>
              </div>
              <RouterLink
                v-for="item in userMenus"
                :key="item.name"
                :to="item.path"
                class="flex items-center gap-3 px-5 py-3 hover:bg-gray-50 transition"
              >
                <component :is="item.icon" class="w-5 h-5 text-indigo-600" />

                <span>
                  {{ item.name }}
                </span>
              </RouterLink>
              <button
                @click="logout"
                class="w-full flex gap-3 px-5 py-3 text-red-500 hover:bg-red-50"
              >
                <LogOut class="w-5 h-5" />

                Đăng xuất
              </button>
            </div>
          </transition>
        </div>
      </div>
    </div>
  </header>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { RouterLink } from 'vue-router'
import {
  User,
  Package,
  MapPin,
  Settings,
  Heart,
  LogOut,
  ShoppingBag,
  ChevronDown,
} from 'lucide-vue-next'
import axios from 'axios'
import emitter from '@/utils/emitter'

import { useRouter } from 'vue-router'

const userMenus = [
  {
    name: 'Thông tin tài khoản',
    path: '/tai-khoan',
    icon: User,
  },
  {
    name: 'Đơn mua của tôi',
    path: '/donhang',
    icon: Package,
  },
]
const router = useRouter()

const openUser = ref(false)

const user = computed(() => {
  const data = sessionStorage.getItem('user')
  return data ? JSON.parse(data) : null
})

const avatar = computed(() => {
  if (!user.value) return '?'
  return user.value.hoTen.substring(0, 2).toUpperCase()
})

const logout = () => {
  sessionStorage.removeItem('token')
  sessionStorage.removeItem('user')
  router.push('/login')
}

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

const loadCart = async () => {
  try {
    const token = sessionStorage.getItem('token')

    if (!token) {
      cart.value = []
      return
    }

    const res = await axios.get('http://localhost:8080/giohang', {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    })

    cart.value = res.data || []
  } catch (err) {
    console.log(err)

    cart.value = []
  }
}

onMounted(() => {
  window.addEventListener('scroll', handleScroll)
  emitter.on('cart-updated', () => {
    console.log('Header nhận được sự kiện')

    loadCart()
  })
  loadCart()
})

onUnmounted(() => {
  window.removeEventListener('scroll', handleScroll)

  emitter.off('cart-updated', loadCart)
})

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
