<template>
  <div class="bg-gray-50 min-h-screen py-12 px-4 sm:px-6 lg:px-8 font-sans">
    <div class="max-w-7xl mx-auto">
      <div class="flex items-center gap-4 mb-8">
        <RouterLink
          to="/san-pham"
          class="flex items-center text-gray-500 hover:text-indigo-600 transition gap-1 text-sm font-medium"
        >
          <ArrowLeft class="w-4 h-4" /> Tiếp tục mua sắm
        </RouterLink>
        <h1 class="text-3xl font-bold tracking-tight text-gray-900 ml-auto sm:ml-0">
          Giỏ hàng của bạn
        </h1>
      </div>

      <div
        v-if="cart.length === 0"
        class="bg-white rounded-2xl shadow-sm border border-gray-100 p-16 text-center"
      >
        <div
          class="w-24 h-24 bg-indigo-50 rounded-full flex items-center justify-center mx-auto mb-6"
        >
          <ShoppingBag class="w-12 h-12 text-indigo-600" />
        </div>
        <h2 class="text-2xl font-bold text-gray-900 mb-2">Giỏ hàng đang trống</h2>
        <p class="text-gray-500 mb-8 max-w-md mx-auto">
          Bạn chưa thêm sản phẩm nào vào giỏ hàng. Hãy quay lại cửa hàng để chọn cho mình những sản
          phẩm ưng ý nhé!
        </p>
        <RouterLink
          to="/san-pham"
          class="inline-flex items-center justify-center px-6 py-3 border border-transparent text-base font-semibold rounded-xl text-white bg-indigo-600 hover:bg-indigo-700 shadow-sm transition"
        >
          Mua sắm ngay
        </RouterLink>
      </div>

      <div v-else class="grid grid-cols-1 lg:grid-cols-3 gap-8 items-start">
        <div class="lg:col-span-2 space-y-4">
          <div
            v-for="item in cart"
            :key="item.id"
            class="bg-white rounded-2xl p-5 border border-gray-100 shadow-sm flex flex-col sm:flex-row gap-5 items-start sm:items-center relative group"
          >
            <div
              class="w-24 h-24 rounded-xl bg-gray-100 overflow-hidden flex-shrink-0 border border-gray-100"
            >
              <img
                v-if="item.anh"
                :src="'http://localhost:8080' + item.anh"
                class="w-full h-full object-cover"
              />
              <div
                v-else
                class="w-full h-full flex items-center justify-center text-2xl text-gray-300"
              >
                📦
              </div>
            </div>

            <div class="flex-1 min-w-0">
              <h3 class="font-bold text-base text-gray-900 truncate pr-6">{{ item.tenSanPham }}</h3>

              <p class="text-sm text-gray-500 mt-1">
                Mã CTSP: <span class="font-medium">{{ item.maSanPhamChiTiet }}</span>
              </p>

              <div class="flex flex-wrap gap-2 mt-2">
                <span
                  class="inline-flex items-center px-2.5 py-0.5 rounded-md text-xs font-medium bg-gray-100 text-gray-800"
                >
                  Màu: {{ item.mauSac }}
                </span>
                <span
                  class="inline-flex items-center px-2.5 py-0.5 rounded-md text-xs font-medium bg-indigo-50 text-indigo-700"
                >
                  Size: {{ item.kichCo }}
                </span>
              </div>

              <div class="flex items-baseline gap-2 mt-3 sm:hidden">
                <span class="font-bold text-gray-900"
                  >{{ Number(item.thanhTien).toLocaleString('vi-VN') }}đ</span
                >
                <span class="text-xs text-gray-400"
                  >({{ Number(item.giaBan).toLocaleString('vi-VN') }}đ / cái)</span
                >
              </div>
            </div>

            <div
              class="flex items-center justify-between sm:justify-end gap-8 w-full sm:w-auto border-t sm:border-t-0 pt-3 sm:pt-0"
            >
              <div class="flex items-center border border-gray-200 rounded-lg p-1 bg-gray-50">
                <button
                  @click="updateQuantity(item, -1)"
                  type="button"
                  class="w-8 h-8 rounded-md flex items-center justify-center text-gray-500 hover:bg-white hover:text-indigo-600 transition shadow-none active:scale-95"
                >
                  <Minus class="w-4 h-4" />
                </button>
                <span class="w-10 text-center text-sm font-semibold text-gray-800 select-none">{{
                  item.soLuong
                }}</span>
                <button
                  @click="updateQuantity(item, 1)"
                  type="button"
                  class="w-8 h-8 rounded-md flex items-center justify-center text-gray-500 hover:bg-white hover:text-indigo-600 transition shadow-none active:scale-95"
                >
                  <Plus class="w-4 h-4" />
                </button>
              </div>

              <div class="hidden sm:block text-right min-w-[120px]">
                <p class="font-bold text-base text-indigo-600">
                  {{ Number(item.thanhTien).toLocaleString('vi-VN') }}đ
                </p>
                <p class="text-xs text-gray-400 mt-0.5">
                  {{ Number(item.giaBan).toLocaleString('vi-VN') }}đ / cái
                </p>
              </div>
            </div>

            <button
              @click="deleteItem(item.id)"
              type="button"
              class="absolute top-4 right-4 text-gray-400 hover:text-red-500 p-1 rounded-lg hover:bg-red-50 transition"
              title="Xóa khỏi giỏ hàng"
            >
              <Trash2 class="w-4 h-4" />
            </button>
          </div>
        </div>

        <div class="bg-white rounded-2xl p-6 border border-gray-100 shadow-sm space-y-6">
          <h3 class="text-lg font-bold text-gray-900 border-b border-gray-100 pb-4">
            Tóm tắt đơn hàng
          </h3>

          <div class="space-y-4">
            <div class="flex justify-between text-sm text-gray-600">
              <span>Tổng số lượng</span>
              <span class="font-semibold text-gray-900">{{ totalQuantity }} sản phẩm</span>
            </div>
            <div class="flex justify-between text-sm text-gray-600">
              <span>Tạm tính</span>
              <span class="font-semibold text-gray-900"
                >{{ totalAmount.toLocaleString('vi-VN') }}đ</span
              >
            </div>
            <div class="flex justify-between text-sm text-gray-600">
              <span>Phí vận chuyển</span>
              <span class="text-green-600 font-medium">Miễn phí</span>
            </div>

            <div class="border-t border-gray-100 pt-4 flex justify-between items-baseline">
              <span class="text-base font-bold text-gray-900">Tổng thanh toán</span>
              <span class="text-2xl font-black text-indigo-600"
                >{{ totalAmount.toLocaleString('vi-VN') }}đ</span
              >
            </div>
          </div>

          <button
            type="button"
            class="w-full text-center py-4 rounded-xl bg-black text-white font-semibold hover:bg-gray-900 transition shadow-md hover:shadow-lg active:scale-[0.98]"
          >
            Tiến hành thanh toán
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { RouterLink } from 'vue-router'
import { Trash2, Plus, Minus, ShoppingBag, ArrowLeft } from 'lucide-vue-next'
import axios from 'axios'
import emitter from '@/utils/emitter'

const cart = ref([])

/**
 * Tính tổng thành tiền
 */
const totalAmount = computed(() => {
  return cart.value.reduce((sum, item) => sum + Number(item.thanhTien), 0)
})

/**
 * Tính tổng số lượng
 */
const totalQuantity = computed(() => {
  return cart.value.reduce((sum, item) => sum + Number(item.soLuong), 0)
})

/**
 * Gọi API lấy dữ liệu
 */
const loadCart = async () => {
  try {
    const token = localStorage.getItem('token')
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
    console.error('Lỗi khi tải dữ liệu giỏ hàng:', err)
    cart.value = []
  }
}

/**
 * Tăng / Giảm số lượng
 */
const updateQuantity = async (item, change) => {
  const newQty = item.soLuong + change

  if (newQty <= 0) {
    await deleteItem(item.id)
    return
  }

  try {
    const token = localStorage.getItem('token')
    await axios.put(
      `http://localhost:8080/giohang/update/${item.id}`,
      { soLuong: newQty },
      { headers: { Authorization: `Bearer ${token}` } },
    )
    await loadCart()
    emitter.emit('cart-updated') // Cập nhật lại số lượng trên thanh Header
  } catch (err) {
    console.error('Lỗi khi cập nhật số lượng:', err)
  }
}

/**
 * Xóa sản phẩm
 */
const deleteItem = async (id) => {
  if (!confirm('Bạn có chắc chắn muốn xóa sản phẩm này khỏi giỏ hàng không?')) return

  try {
    const token = localStorage.getItem('token')
    await axios.delete(`http://localhost:8080/giohang/delete/${id}`, {
      headers: { Authorization: `Bearer ${token}` },
    })
    await loadCart()
    emitter.emit('cart-updated')
  } catch (err) {
    console.error('Lỗi khi xóa sản phẩm:', err)
  }
}

onMounted(() => {
  loadCart()
})
</script>
