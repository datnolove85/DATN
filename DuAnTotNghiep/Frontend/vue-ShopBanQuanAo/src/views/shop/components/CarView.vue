<template>
  <div
    class="min-h-screen bg-gradient-to-br from-slate-50 via-gray-50 to-slate-100 py-8 px-4 sm:px-6 font-sans text-slate-800"
  >
    <!-- Toast Notification -->
    <Transition name="toast">
      <div
        v-if="toast.show"
        class="fixed top-6 right-6 z-50 flex items-center gap-3 px-5 py-3.5 rounded-2xl shadow-xl border text-sm font-semibold backdrop-blur-md transition-all"
        :class="
          toast.type === 'error'
            ? 'bg-red-50/90 border-red-200 text-red-700 shadow-red-100'
            : 'bg-emerald-50/90 border-emerald-200 text-emerald-700 shadow-emerald-100'
        "
      >
        <span class="text-base">{{ toast.type === 'error' ? '⚠️' : '✨' }}</span>
        <span>{{ toast.message }}</span>
      </div>
    </Transition>

    <div class="max-w-6xl mx-auto space-y-6">
      <!-- PHẦN 1: TIÊU ĐỀ & ĐIỀU HƯỚNG CHUNG -->
      <div
        class="bg-white/80 backdrop-blur-md px-6 py-5 rounded-3xl border border-slate-200/60 shadow-sm flex flex-col sm:flex-row items-start sm:items-center justify-between gap-4"
      >
        <div class="flex items-center gap-4">
          <div
            class="w-12 h-12 rounded-2xl bg-gradient-to-tr from-indigo-600 to-violet-500 flex items-center justify-center text-white shadow-md shadow-indigo-200 text-xl"
          >
            🛒
          </div>
          <div>
            <h1 class="text-xl font-extrabold text-slate-900 tracking-tight">Giỏ hàng của bạn</h1>
            <p class="text-xs sm:text-sm text-slate-500 mt-0.5">
              Kiểm tra lại sản phẩm và sẵn sàng thanh toán
            </p>
          </div>
        </div>

        <button
          @click="router.push('/san-pham')"
          class="group inline-flex items-center gap-2 text-xs font-bold text-indigo-600 bg-indigo-50 hover:bg-indigo-600 hover:text-white px-4 py-2.5 rounded-xl transition-all duration-200 shadow-sm"
        >
          <span class="transition-transform group-hover:-translate-x-0.5">←</span> Tiếp tục mua sắm
        </button>
      </div>

      <!-- Trạng thái giỏ hàng trống -->
      <div
        v-if="cart.length === 0"
        class="bg-white rounded-3xl border border-slate-200/60 p-16 text-center shadow-sm space-y-4"
      >
        <div
          class="w-20 h-20 bg-indigo-50 text-indigo-500 rounded-full flex items-center justify-center mx-auto text-3xl shadow-inner"
        >
          🛒
        </div>
        <div class="space-y-1">
          <h3 class="text-lg font-bold text-slate-900">Giỏ hàng đang trống</h3>
          <p class="text-slate-500 text-sm">Bạn chưa có sản phẩm nào trong giỏ hàng lúc này.</p>
        </div>
        <button
          @click="router.push('/')"
          class="px-6 py-3 bg-indigo-600 hover:bg-indigo-700 text-white text-xs font-bold rounded-2xl shadow-lg shadow-indigo-200 transition-all active:scale-95"
        >
          Khám phá sản phẩm ngay
        </button>
      </div>

      <!-- BỐ CỤC CHÍNH (2 CỘT CÂN ĐỐI) -->
      <div v-else class="grid grid-cols-1 lg:grid-cols-12 gap-6 items-start">
        <!-- CỘT TRÁI: DANH SÁCH SẢN PHẨM (8 CỘT) -->
        <div class="lg:col-span-8 space-y-4">
          <!-- Phần đầu danh sách: Chọn tất cả & Thao tác nhanh -->
          <div
            class="bg-white rounded-2xl shadow-sm border border-slate-200/60 px-5 py-3.5 flex justify-between items-center text-xs sm:text-sm"
          >
            <label
              class="flex items-center gap-3 cursor-pointer select-none font-semibold text-slate-700"
            >
              <input
                type="checkbox"
                :checked="allSelected"
                @change="toggleSelectAll"
                class="w-4 h-4 rounded text-indigo-600 focus:ring-indigo-500 border-slate-300 cursor-pointer"
              />
              <span
                >Chọn tất cả (<span class="text-indigo-600 font-bold">{{ cart.length }}</span> sản
                phẩm)</span
              >
            </label>

            <button
              @click="deleteSelectedItems"
              class="text-slate-400 hover:text-red-600 font-semibold transition-colors flex items-center gap-1.5 text-xs bg-slate-50 hover:bg-red-50 px-3 py-1.5 rounded-lg"
            >
              🗑️ Xóa đã chọn
            </button>
          </div>

          <!-- Danh sách từng sản phẩm -->
          <div
            v-for="item in cart"
            :key="item.id"
            class="bg-white rounded-3xl border border-slate-200/60 shadow-sm p-4 sm:p-5 hover:border-indigo-300 hover:shadow-md transition-all duration-200 group"
          >
            <div class="flex items-start gap-4">
              <!-- Checkbox chọn sản phẩm -->
              <input
                type="checkbox"
                v-model="item.selected"
                class="w-4 h-4 mt-9 text-indigo-600 focus:ring-indigo-500 border-slate-300 cursor-pointer flex-shrink-0 rounded"
              />

              <!-- Hình ảnh sản phẩm -->
              <div
                class="w-24 h-24 rounded-2xl overflow-hidden bg-slate-50 border border-slate-100 flex-shrink-0 relative group-hover:scale-[1.02] transition-transform"
              >
                <img
                  v-if="item.anh"
                  :src="'http://localhost:8080' + item.anh"
                  class="w-full h-full object-cover"
                />
                <div v-else class="w-full h-full flex items-center justify-center text-2xl">📦</div>
              </div>

              <!-- Chi tiết thông tin sản phẩm -->
              <div class="flex-grow min-w-0 space-y-2">
                <div class="flex justify-between items-start gap-3">
                  <h3 class="text-xs sm:text-sm font-bold text-slate-900 line-clamp-2 leading-snug">
                    {{ item.tenSanPham }}
                  </h3>
                  <button
                    @click="deleteItem(item.id)"
                    class="text-slate-300 hover:text-red-500 transition-colors p-1 rounded-lg hover:bg-red-50"
                    title="Xóa sản phẩm"
                  >
                    ✕
                  </button>
                </div>

                <!-- Nhóm thông tin phân loại (Mã, Màu, Size, Kho) -->
                <div class="flex flex-wrap items-center gap-1.5 text-[11px]">
                  <span class="px-2.5 py-1 bg-slate-100 text-slate-600 rounded-lg font-medium">
                    Mã: {{ item.maSanPhamChiTiet }}
                  </span>
                  <span
                    class="px-2.5 py-1 bg-indigo-50/80 text-indigo-700 rounded-lg font-semibold"
                  >
                    {{ item.mauSac }} • Size {{ item.kichCo }}
                  </span>
                  <span
                    v-if="item.soLuongTon !== undefined"
                    :class="[
                      'px-2.5 py-1 rounded-lg font-medium',
                      item.soLuongTon > 0
                        ? 'bg-emerald-50 text-emerald-700'
                        : 'bg-red-50 text-red-600',
                    ]"
                  >
                    {{ item.soLuongTon > 0 ? `Kho: ${item.soLuongKhaDung}` : 'Hết hàng' }}
                  </span>
                </div>

                <!-- Nhóm giá tiền và Bộ đếm số lượng -->
                <div
                  class="flex flex-col sm:flex-row justify-between sm:items-center gap-3 pt-2 border-t border-slate-100/80"
                >
                  <div>
                    <span class="text-xs font-extrabold text-red-600 tracking-wide">
                      {{ Number(item.giaBan).toLocaleString('vi-VN') }}đ
                    </span>
                  </div>

                  <!-- Bộ tăng giảm số lượng -->
                  <div
                    class="flex items-center border border-slate-200 rounded-xl overflow-hidden bg-slate-50/60 p-0.5"
                  >
                    <button
                      @click="updateQuantity(item, -1)"
                      class="w-7 h-7 flex items-center justify-center bg-white hover:bg-slate-100 text-slate-600 font-bold text-xs rounded-lg shadow-xs transition disabled:opacity-40"
                      :disabled="item.soLuong <= 1"
                    >
                      -
                    </button>
                    <input
                      v-model.number="item.soLuong"
                      @input="validateQuantity(item)"
                      class="w-10 text-center text-xs font-bold bg-transparent outline-none text-slate-800"
                      type="number"
                      min="1"
                      :max="item.soLuongTon"
                    />
                    <button
                      @click="updateQuantity(item, 1)"
                      class="w-7 h-7 flex items-center justify-center bg-white hover:bg-slate-100 text-slate-600 font-bold text-xs rounded-lg shadow-xs transition disabled:opacity-40"
                      :disabled="item.soLuongTon !== undefined && item.soLuong >= item.soLuongTon"
                    >
                      +
                    </button>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- CỘT PHẢI: TỔNG KẾT THANH TOÁN (4 CỘT) -->
        <div class="lg:col-span-4 space-y-4">
          <!-- Box Tổng quan đơn hàng -->
          <div
            class="bg-white rounded-3xl border border-slate-200/60 p-6 sticky top-6 shadow-sm space-y-5"
          >
            <h3
              class="text-xs uppercase tracking-wider font-extrabold text-slate-400 pb-3 border-b border-slate-100"
            >
              Cộng giỏ hàng
            </h3>

            <div class="space-y-3 text-xs">
              <div class="flex justify-between text-slate-600">
                <span>Tạm tính sản phẩm</span>
                <span class="font-semibold text-slate-800"
                  >{{ totalAmount.toLocaleString('vi-VN') }}đ</span
                >
              </div>
              <div class="flex justify-between text-slate-600">
                <span>Phí vận chuyển</span>
                <span class="font-semibold text-emerald-600">Miễn phí</span>
              </div>
            </div>

            <div class="border-t border-slate-100 pt-4 flex justify-between items-center">
              <span class="text-xs font-bold text-slate-700">Tổng thanh toán</span>
              <span class="text-lg font-black text-red-600">
                {{ totalAmount.toLocaleString('vi-VN') }}đ
              </span>
            </div>

            <button
              @click="checkout"
              class="w-full py-3.5 bg-gradient-to-r from-indigo-600 to-violet-600 hover:from-indigo-700 hover:to-violet-700 text-white rounded-2xl font-bold text-xs tracking-wider shadow-lg shadow-indigo-200 transition-all active:scale-95 flex items-center justify-center gap-2"
            >
              <span>Tiến hành thanh toán</span>
              <span class="bg-white/25 px-2 py-0.5 rounded-lg text-[11px]">{{
                totalQuantity
              }}</span>
            </button>

            <!-- Thêm điểm nhấn an tâm mua sắm -->
            <div
              class="pt-2 border-t border-slate-100 grid grid-cols-2 gap-2 text-[10px] text-slate-400 text-center"
            >
              <div class="bg-slate-50 p-2 rounded-xl">🔒 Bảo mật thanh toán</div>
              <div class="bg-slate-50 p-2 rounded-xl">⚡ Giao hàng siêu tốc</div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'
import emitter from '@/utils/emitter'

const router = useRouter()
const cart = ref([])

// Toast State
const toast = ref({
  show: false,
  message: '',
  type: 'success',
})

const showToast = (message, type = 'success') => {
  toast.value = { show: true, message, type }
  setTimeout(() => {
    toast.value.show = false
  }, 2500)
}

const totalAmount = computed(() => {
  return cart.value
    .filter((item) => item.selected)
    .reduce((sum, item) => sum + Number(item.giaBan) * Number(item.soLuong || 0), 0)
})

const allSelected = computed(() => {
  return cart.value.length > 0 && cart.value.every((item) => item.selected)
})

const toggleSelectAll = (e) => {
  const isChecked = e.target.checked
  cart.value.forEach((item) => {
    item.selected = isChecked
  })
}

const totalQuantity = computed(() => {
  return cart.value
    .filter((item) => item.selected)
    .reduce((sum, item) => sum + Number(item.soLuong || 0), 0)
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

    const oldSelectedMap = new Map(cart.value.map((i) => [i.id, i.selected]))

    cart.value = (res.data || []).map((item) => ({
      ...item,
      selected: oldSelectedMap.get(item.id) || false,
      soLuong: Number(item.soLuong) || 1,
      thanhTien: Number(item.giaBan) * Number(item.soLuong || 1),
    }))
  } catch (err) {
    console.error('Lỗi khi tải dữ liệu giỏ hàng:', err)
    cart.value = []
  }
}

const updateQuantity = (item, change) => {
  const newQty = (item.soLuong || 1) + change
  if (newQty <= 0) return

  if (item.soLuongTon !== undefined && newQty > item.soLuongTon) {
    showToast(`Số lượng vượt quá tồn kho (${item.soLuongTon})!`, 'error')
    return
  }

  item.soLuong = newQty
  item.thanhTien = item.giaBan * item.soLuong
}

const validateQuantity = (item) => {
  if (!item.soLuong || item.soLuong < 1) {
    item.soLuong = 1
  }

  if (item.soLuongTon !== undefined && item.soLuong > item.soLuongTon) {
    showToast(`Số lượng nhập vượt quá tồn kho (${item.soLuongTon})!`, 'error')
    item.soLuong = item.soLuongTon
  }

  item.thanhTien = item.giaBan * item.soLuong
}

const deleteItem = async (id) => {
  try {
    const token = sessionStorage.getItem('token')
    await axios.delete(`http://localhost:8080/giohang/${id}`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    })
    await loadCart()
    emitter.emit('cart-updated')
    showToast('Đã xóa sản phẩm khỏi giỏ hàng')
  } catch (err) {
    console.error('Lỗi khi xóa sản phẩm:', err)
    showToast('Xóa sản phẩm thất bại', 'error')
  }
}

const deleteSelectedItems = async () => {
  const selectedItems = cart.value.filter((item) => item.selected)

  if (selectedItems.length === 0) {
    showToast('Vui lòng chọn sản phẩm muốn xóa!', 'error')
    return
  }

  if (!confirm(`Bạn có chắc muốn xóa ${selectedItems.length} sản phẩm đã chọn không?`)) {
    return
  }

  try {
    const token = sessionStorage.getItem('token')

    for (const item of selectedItems) {
      await axios.delete(`http://localhost:8080/giohang/${item.id}`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      })
    }

    await loadCart()
    emitter.emit('cart-updated')
    showToast('Đã xóa các sản phẩm đã chọn')
  } catch (err) {
    console.error('Lỗi khi xóa sản phẩm đã chọn:', err)
    showToast('Xóa sản phẩm thất bại', 'error')
  }
}

const checkout = () => {
  const selectedItems = cart.value.filter((item) => item.selected)

  if (selectedItems.length === 0) {
    showToast('Vui lòng chọn ít nhất một sản phẩm để thanh toán!', 'error')
    return
  }

  const checkoutData = {
    items: selectedItems.map((item) => ({
      productDetailId: item.idSanPhamChiTiet,
      quantity: item.soLuong,
      tenSanPham: item.tenSanPham,
      maSanPhamChiTiet: item.maSanPhamChiTiet,
      giaBan: item.giaBan,
      mauSac: item.mauSac,
      kichCo: item.kichCo,
      anh: item.anh,
      soLuongTon: item.soLuongTon,
      thanhTien: item.giaBan * item.soLuong,
      soLuongKhaDung: item.soLuongKhaDung,
    })),
  }

  sessionStorage.setItem('checkoutData', JSON.stringify(checkoutData))
  router.push('/xacnhan')
}

onMounted(() => {
  loadCart()
})
</script>

<style scoped>
.toast-enter-active,
.toast-leave-active {
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}
.toast-enter-from,
.toast-leave-to {
  opacity: 0;
  transform: translateY(-20px) scale(0.95);
}
</style>
