<template>
  <header
    :class="[
      'sticky top-0 z-50 transition-all duration-300',
      scrolled
        ? 'bg-white/95 backdrop-blur-md shadow-xs border-b border-slate-200/80'
        : 'bg-white border-b border-gray-100',
    ]"
  >
    <div
      class="max-w-[1700px] mx-auto h-20 px-6 lg:px-8 flex items-center justify-between relative"
    >
      <!-- ================= BÊN TRÁI: Tìm kiếm, Menu, Trang chủ ================= -->
      <div class="flex items-center gap-3 text-slate-700">
        <button
          class="w-10 h-10 hover:text-slate-900 rounded-full hover:bg-slate-100 flex items-center justify-center transition"
          title="Tìm kiếm"
        >
          <Search class="w-5 h-5" />
        </button>

        <button
          class="w-10 h-10 hover:text-slate-900 rounded-full hover:bg-slate-100 flex items-center justify-center transition"
          title="Menu danh mục"
        >
          <Menu class="w-5 h-5" />
        </button>

        <RouterLink
          to="/home"
          class="w-10 h-10 hover:text-slate-900 rounded-full hover:bg-slate-100 flex items-center justify-center transition"
          title="Trang chủ"
        >
          <Home class="w-5 h-5" />
        </RouterLink>
      </div>

      <!-- ================= Ở GIỮA: Logo thương hiệu K-Zone ================= -->
      <div class="absolute left-1/2 -translate-x-1/2">
        <RouterLink to="/home" class="flex items-center gap-2 group">
          <span
            class="text-3xl lg:text-4xl font-black italic tracking-tight text-slate-900 group-hover:text-indigo-600 transition-colors"
            style="font-family: 'Brush Script MT', cursive, sans-serif"
          >
            K-Zone
          </span>
        </RouterLink>
      </div>

      <!-- ================= BÊN PHẢI: Tài khoản, Yêu thích, Giỏ hàng ================= -->
      <div class="flex items-center gap-3 text-slate-700">
        <!-- TÀI KHOẢN (USER DROPDOWN) -->
        <div
          class="relative user-dropdown"
          @mouseenter="openUser = true"
          @mouseleave="openUser = false"
        >
          <button
            class="w-10 h-10 hover:text-slate-900 rounded-full hover:bg-slate-100 flex items-center justify-center transition"
            title="Tài khoản"
          >
            <User class="w-5 h-5" />
          </button>

          <transition
            enter-active-class="transition duration-200 ease-out"
            enter-from-class="opacity-0 scale-95 translate-y-1"
            enter-to-class="opacity-100 scale-100 translate-y-0"
            leave-active-class="transition duration-150 ease-in"
            leave-from-class="opacity-100 scale-100 translate-y-0"
            leave-to-class="opacity-0 scale-95 translate-y-1"
          >
            <div
              v-if="openUser"
              class="absolute right-0 mt-2 w-72 bg-white rounded-2xl shadow-xl shadow-slate-200/50 border border-slate-100 overflow-hidden py-1.5 z-50 text-left"
            >
              <div
                class="px-4 py-3 mx-2 my-1 bg-gradient-to-r from-indigo-600 to-purple-600 text-white rounded-xl shadow-xs"
              >
                <div class="flex items-center gap-3">
                  <div
                    class="w-10 h-10 rounded-full bg-white/20 flex items-center justify-center font-bold text-sm flex-shrink-0"
                  >
                    {{ avatar }}
                  </div>
                  <div class="min-w-0">
                    <p class="font-bold text-xs truncate">{{ user?.hoTen || 'Khách hàng' }}</p>
                    <p class="text-[11px] opacity-80 truncate mt-0.5">
                      {{ user?.email || 'Chưa cập nhật email' }}
                    </p>
                  </div>
                </div>
              </div>

              <div class="h-px bg-slate-100 my-1"></div>

              <div class="px-1.5 space-y-0.5">
                <RouterLink
                  v-for="item in userMenus"
                  :key="item.name"
                  :to="item.path"
                  class="flex items-center gap-3 px-3 py-2.5 rounded-xl text-xs font-medium text-slate-600 hover:text-slate-900 hover:bg-slate-100 transition"
                >
                  <component :is="item.icon" class="w-4 h-4 text-indigo-600" />
                  <span>{{ item.name }}</span>
                </RouterLink>
                <RouterLink
                  to="/minigame"
                  class="flex items-center gap-3 px-3 py-2.5 rounded-xl text-xs font-medium text-slate-600 hover:text-slate-900 hover:bg-slate-100 transition"
                >
                  <Sparkles class="w-4 h-4 text-indigo-600" />
                  <span>Minigame & Ví Xu</span>
                </RouterLink>
              </div>

              <div class="h-px bg-slate-100 my-1"></div>

              <div class="px-1.5">
                <button
                  @click="logout"
                  class="w-full flex items-center gap-3 px-3 py-2.5 rounded-xl text-xs font-medium text-rose-600 hover:bg-rose-50 transition text-left"
                >
                  <LogOut class="w-4 h-4 text-rose-500" />
                  <span>Đăng xuất</span>
                </button>
              </div>
            </div>
          </transition>
        </div>

        <!-- YÊU THÍCH (WISHLIST) -->
        <RouterLink
          to="/yeuthich"
          class="relative w-10 h-10 hover:text-slate-900 rounded-full hover:bg-slate-100 flex items-center justify-center transition"
          title="Yêu thích"
        >
          <Heart class="w-5 h-5" />
          <span
            v-if="wishlistCount > 0"
            class="absolute top-1.5 right-1.5 min-w-[16px] h-4 px-1 rounded-full bg-emerald-600 text-white text-[10px] font-bold flex items-center justify-center ring-2 ring-white"
          >
            {{ wishlistCount }}
          </span>
        </RouterLink>

        <!-- GIỎ HÀNG (CART) - Có ref để định vị hiệu ứng bay -->
        <div class="relative" @mouseenter="openCart" @mouseleave="closeCart">
          <button
            ref="cartBtnRef"
            type="button"
            class="relative w-10 h-10 hover:text-slate-900 rounded-full hover:bg-slate-100 flex items-center justify-center transition"
            @click.stop="toggleCart"
            title="Giỏ hàng"
          >
            <ShoppingBag class="w-5 h-5" />
            <span
              v-if="cartCount > 0"
              class="absolute top-1.5 right-1.5 min-w-[16px] h-4 px-1 rounded-full bg-rose-600 text-white text-[10px] font-bold flex items-center justify-center ring-2 ring-white"
            >
              {{ cartCount }}
            </span>
          </button>

          <!-- CART DROPDOWN -->
          <transition
            enter-active-class="transition duration-200 ease-out"
            enter-from-class="opacity-0 translate-y-2 scale-95"
            enter-to-class="opacity-100 translate-y-0 scale-100"
            leave-active-class="transition duration-150 ease-in"
            leave-from-class="opacity-100 translate-y-0 scale-100"
            leave-to-class="opacity-0 translate-y-2 scale-95"
          >
            <div
              v-if="showCart"
              class="absolute right-0 mt-3 w-[380px] bg-white rounded-2xl shadow-xl shadow-slate-200/50 border border-slate-100 overflow-hidden z-50 text-left"
            >
              <div
                class="px-5 py-4 border-b border-slate-100 flex justify-between items-center bg-slate-50/50"
              >
                <h3 class="font-bold text-slate-800 text-base">🛒 Giỏ hàng</h3>
                <span class="text-xs font-medium text-slate-500">{{ cartCount }} sản phẩm</span>
              </div>

              <div v-if="cart.length === 0" class="py-12 text-center text-slate-400">
                <ShoppingBag class="mx-auto w-10 h-10 mb-3 opacity-50" />
                <p class="text-sm">Giỏ hàng đang trống</p>
              </div>

              <div v-else class="max-h-[360px] overflow-y-auto divide-y divide-slate-100">
                <div
                  v-for="item in cart"
                  :key="item.id"
                  class="flex gap-3 px-5 py-3.5 hover:bg-slate-50/80 transition"
                >
                  <div
                    class="w-14 h-14 rounded-xl overflow-hidden bg-slate-100 flex-shrink-0 border border-slate-100"
                  >
                    <img
                      v-if="item.anh"
                      :src="'http://localhost:8080' + item.anh"
                      class="w-full h-full object-cover"
                      alt="Product"
                    />
                    <div v-else class="w-full h-full flex items-center justify-center text-lg">
                      📦
                    </div>
                  </div>

                  <div class="flex-1 min-w-0 flex flex-col justify-center">
                    <p class="font-semibold text-xs text-slate-800 truncate">
                      {{ item.tenSanPham }}
                    </p>
                    <p class="text-[11px] text-slate-500 mt-0.5">
                      {{ item.mauSac }} • {{ item.kichCo }}
                    </p>

                    <div class="flex justify-between items-center mt-1.5">
                      <span class="text-[11px] text-slate-500">
                        {{ item.soLuong }} × {{ Number(item.giaBan).toLocaleString('vi-VN') }}đ
                      </span>
                      <span class="font-bold text-indigo-600 text-xs">
                        {{ Number(item.thanhTien).toLocaleString('vi-VN') }}đ
                      </span>
                    </div>
                  </div>
                </div>
              </div>

              <div v-if="cart.length" class="p-4 bg-slate-50 border-t border-slate-100">
                <div class="flex justify-between items-center mb-3">
                  <span class="text-xs font-medium text-slate-600">Tổng tiền thanh toán</span>
                  <span class="font-bold text-indigo-600 text-base">
                    {{ totalPrice.toLocaleString('vi-VN') }}đ
                  </span>
                </div>
                <RouterLink
                  to="/giohang"
                  @click="closeCartImmediately"
                  class="block text-center py-2.5 rounded-xl bg-slate-900 text-white text-xs font-semibold hover:bg-slate-800 transition shadow-sm"
                >
                  Xem giỏ hàng chi tiết
                </RouterLink>
              </div>
            </div>
          </transition>
        </div>
      </div>
    </div>
  </header>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { RouterLink, useRouter } from 'vue-router'
import {
  Search,
  Menu,
  Home,
  User,
  Heart,
  Package,
  LogOut,
  ShoppingBag,
  Sparkles,
} from 'lucide-vue-next'
import axios from 'axios'
import emitter from '@/utils/emitter'
import yeuThichService from '@/service/yeuThichService'

const router = useRouter()
const openUser = ref(false)
const scrolled = ref(false)
const cart = ref([])
const wishlist = ref([])
const showCart = ref(false)
const cartBtnRef = ref(null)
let cartTimeout = null

// Lấy id khách hàng từ sessionStorage (đồng bộ logic với trang yêu thích)
const getCustomerId = () => {
  const userStr = sessionStorage.getItem('user')
  if (!userStr) return null
  const userObj = JSON.parse(userStr)
  return (
    userObj?.idKhachHang || userObj?.khachHangId || userObj?.khachHang?.id || userObj?.id || null
  )
}

// Cập nhật tọa độ icon giỏ hàng lưu vào sessionStorage để các trang khác gọi hiệu ứng bay
const updateCartPosition = () => {
  if (cartBtnRef.value) {
    const rect = cartBtnRef.value.getBoundingClientRect()
    const position = {
      x: rect.left + rect.width / 2,
      y: rect.top + rect.height / 2,
    }
    sessionStorage.setItem('cart_icon_pos', JSON.stringify(position))
  }
}

const user = computed(() => {
  const data = sessionStorage.getItem('user')
  return data ? JSON.parse(data) : null
})

const userMenus = computed(() => [
  {
    name: 'Thông tin tài khoản',
    path: `/admin/khach-hang/edit/${user.value?.id || user.value?.idKhachHang || 1}`,
    icon: User,
  },
  {
    name: 'Đơn mua của tôi',
    path: '/donhang',
    icon: Package,
  },
])

const avatar = computed(() => {
  if (!user.value || !user.value.hoTen) return '?'
  return user.value.hoTen.substring(0, 2).toUpperCase()
})

const logout = () => {
  sessionStorage.removeItem('token')
  sessionStorage.removeItem('user')
  router.push('/login')
}

const handleScroll = () => {
  scrolled.value = window.scrollY > 15
}

const cartCount = computed(() => {
  return cart.value.reduce((sum, item) => sum + Number(item.soLuong), 0)
})

const wishlistCount = computed(() => {
  return wishlist.value.length
})

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

const loadWishlist = async () => {
  try {
    const idKhachHang = getCustomerId()
    if (!idKhachHang) {
      wishlist.value = []
      return
    }

    const data = await yeuThichService.getDanhSachYeuThich(idKhachHang)
    wishlist.value = data || []
  } catch (err) {
    console.log(err)
    wishlist.value = []
  }
}

onMounted(() => {
  window.addEventListener('scroll', handleScroll)
  window.addEventListener('resize', updateCartPosition)

  emitter.on('cart-updated', loadCart)
  emitter.on('wishlist-updated', loadWishlist)

  loadCart()
  loadWishlist()

  // Khởi tạo lấy tọa độ sau khi render
  setTimeout(updateCartPosition, 100)
})

onUnmounted(() => {
  window.removeEventListener('scroll', handleScroll)
  window.removeEventListener('resize', updateCartPosition)

  emitter.off('cart-updated', loadCart)
  emitter.off('wishlist-updated', loadWishlist)
})

const openCart = () => {
  if (cartTimeout) clearTimeout(cartTimeout)
  showCart.value = true
}

const closeCart = () => {
  cartTimeout = setTimeout(() => {
    showCart.value = false
  }, 250)
}

const toggleCart = () => {
  showCart.value = !showCart.value
}

const closeCartImmediately = () => {
  showCart.value = false
}
</script>
