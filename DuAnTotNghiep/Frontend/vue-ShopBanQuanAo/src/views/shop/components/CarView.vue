<template>
  <div class="min-h-screen bg-slate-100/70 py-6 px-4 font-sans text-slate-800">
    <!-- Toast Notification -->
    <Transition name="toast">
      <div
        v-if="toast.show"
        class="fixed top-5 right-5 z-50 flex items-center gap-3 px-4 py-3 rounded-xl shadow-lg border text-sm font-medium transition-all"
        :class="
          toast.type === 'error'
            ? 'bg-red-50 border-red-200 text-red-700'
            : 'bg-emerald-50 border-emerald-200 text-emerald-700'
        "
      >
        <span>{{ toast.type === 'error' ? '⚠️' : '✅' }}</span>
        <span>{{ toast.message }}</span>
      </div>
    </Transition>

    <div class="max-w-5xl mx-auto space-y-4">
      <!-- PHẦN 1: TIÊU ĐỀ & ĐIỀU HƯỚNG CHUNG -->
      <div
        class="bg-white px-6 py-4 rounded-2xl border border-slate-200/80 shadow-sm flex items-center justify-between"
      >
        <div class="flex items-center gap-3">
          <div
            class="w-9 h-9 rounded-xl bg-indigo-50 flex items-center justify-center text-indigo-600 font-bold text-base"
          >
            🛒
          </div>
          <div>
            <h1 class="text-base font-bold text-slate-900 leading-tight">Giỏ hàng của bạn</h1>
            <p class="text-xs text-slate-500 mt-0.5">Kiểm tra lại sản phẩm trước khi thanh toán</p>
          </div>
        </div>

        <button
          @click="router.push('/san-pham')"
          class="text-xs font-semibold text-indigo-600 hover:text-indigo-700 hover:bg-indigo-50 px-3 py-2 rounded-xl transition"
        >
          ← Tiếp tục mua sắm
        </button>
      </div>

      <!-- Trạng thái giỏ hàng trống -->
      <div
        v-if="cart.length === 0"
        class="bg-white rounded-2xl border border-slate-200/80 p-10 text-center shadow-sm"
      >
        <div class="text-4xl mb-3">🛒</div>
        <h3 class="text-base font-bold text-slate-800">Giỏ hàng trống</h3>
        <p class="text-slate-500 text-xs mt-1 mb-5">Bạn chưa có sản phẩm nào trong giỏ hàng.</p>
        <button
          @click="router.push('/')"
          class="px-5 py-2.5 bg-indigo-600 hover:bg-indigo-700 text-white text-xs font-semibold rounded-xl shadow-sm transition"
        >
          Khám phá ngay
        </button>
      </div>

      <!-- BỐ CỤC CHÍNH (2 CỘT CÂN ĐỐI) -->
      <div v-else class="grid grid-cols-1 lg:grid-cols-12 gap-4 items-start">
        <!-- CỘT TRÁI: DANH SÁCH SẢN PHẨM (8 CỘT) -->
        <div class="lg:col-span-8 space-y-3">
          <!-- Phần đầu danh sách: Chọn tất cả & Thao tác nhanh -->
          <div
            class="bg-white rounded-2xl shadow-sm border border-slate-200/80 px-4 py-3 flex justify-between items-center text-xs sm:text-sm"
          >
            <label
              class="flex items-center gap-2.5 cursor-pointer select-none font-medium text-slate-700"
            >
              <input
                type="checkbox"
                :checked="allSelected"
                @change="toggleSelectAll"
                class="w-4 h-4 rounded accent-indigo-600 cursor-pointer"
              />
              <span>Chọn tất cả ({{ cart.length }} sản phẩm)</span>
            </label>

            <button
              @click="deleteSelectedItems"
              class="text-red-500 hover:text-red-700 font-medium transition flex items-center gap-1"
            >
              <span>🗑️</span> Xóa đã chọn
            </button>
          </div>

          <!-- Danh sách từng sản phẩm được phân chia rõ ràng -->
          <div
            v-for="item in cart"
            :key="item.id"
            class="bg-white rounded-2xl border border-slate-200/80 shadow-sm p-4 hover:border-indigo-300 transition"
          >
            <div class="flex items-start gap-3">
              <!-- Checkbox chọn sản phẩm -->
              <input
                type="checkbox"
                v-model="item.selected"
                class="w-4 h-4 mt-8 accent-indigo-600 cursor-pointer flex-shrink-0 rounded"
              />

              <!-- Hình ảnh sản phẩm -->
              <div
                class="w-20 h-20 rounded-xl overflow-hidden bg-slate-100 border border-slate-200 flex-shrink-0 relative"
              >
                <img
                  v-if="item.anh"
                  :src="'http://localhost:8080' + item.anh"
                  class="w-full h-full object-cover"
                />
                <div v-else class="w-full h-full flex items-center justify-center text-xl">📦</div>
              </div>

              <!-- Chi tiết thông tin sản phẩm -->
              <div class="flex-grow min-w-0">
                <div class="flex justify-between items-start gap-2">
                  <h3
                    class="text-xs sm:text-sm font-semibold text-slate-800 line-clamp-2 leading-snug"
                  >
                    {{ item.tenSanPham }}
                  </h3>
                  <button
                    @click="deleteItem(item.id)"
                    class="text-slate-400 hover:text-red-500 transition p-1"
                    title="Xóa"
                  >
                    ✕
                  </button>
                </div>

                <!-- Nhóm thông tin phân loại (Mã, Màu, Size, Kho) -->
                <div class="flex flex-wrap items-center gap-1.5 mt-1.5 text-[11px]">
                  <span class="px-2 py-0.5 bg-slate-100 text-slate-600 rounded font-medium">
                    Mã: {{ item.maSanPhamChiTiet }}
                  </span>
                  <span class="px-2 py-0.5 bg-indigo-50 text-indigo-700 rounded font-medium">
                    {{ item.mauSac }} / Size {{ item.kichCo }}
                  </span>
                  <span
                    v-if="item.soLuongTon !== undefined"
                    :class="[
                      'px-2 py-0.5 rounded font-medium',
                      item.soLuongTon > 0
                        ? 'bg-emerald-50 text-emerald-700'
                        : 'bg-red-50 text-red-600',
                    ]"
                  >
                    {{ item.soLuongTon > 0 ? `Kho: ${item.soLuongTon}` : 'Hết hàng' }}
                  </span>
                </div>

                <!-- Nhóm giá tiền và Bộ đếm số lượng -->
                <div class="flex justify-between items-center mt-3 pt-2 border-t border-slate-100">
                  <div>
                    <span class="text-xs font-bold text-red-600">
                      {{ Number(item.giaBan).toLocaleString('vi-VN') }}đ
                    </span>
                  </div>

                  <!-- Bộ tăng giảm số lượng -->
                  <div
                    class="flex items-center border border-slate-200 rounded-lg overflow-hidden bg-slate-50"
                  >
                    <button
                      @click="updateQuantity(item, -1)"
                      class="w-6 h-6 flex items-center justify-center bg-white hover:bg-slate-100 text-slate-600 font-bold text-xs disabled:opacity-40"
                      :disabled="item.soLuong <= 1"
                    >
                      -
                    </button>
                    <input
                      v-model.number="item.soLuong"
                      @input="validateQuantity(item)"
                      class="w-8 text-center text-xs font-bold bg-transparent outline-none text-slate-800"
                      type="number"
                      min="1"
                      :max="item.soLuongTon"
                    />
                    <button
                      @click="updateQuantity(item, 1)"
                      class="w-6 h-6 flex items-center justify-center bg-white hover:bg-slate-100 text-slate-600 font-bold text-xs disabled:opacity-40"
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
        <div class="lg:col-span-4 space-y-3">
          <!-- Box Tổng quan đơn hàng -->
          <div
            class="bg-white rounded-2xl border border-slate-200/80 p-4 sticky top-4 shadow-sm space-y-3"
          >
            <h3
              class="text-xs uppercase tracking-wider font-bold text-slate-500 pb-2 border-b border-slate-100"
            >
              Thông tin đơn hàng
            </h3>

            <div class="space-y-2 text-xs">
              <div class="flex justify-between text-slate-600">
                <span>Tạm tính</span>
                <span class="font-semibold text-slate-800"
                  >{{ totalAmount.toLocaleString('vi-VN') }}đ</span
                >
              </div>
              <div class="flex justify-between text-slate-600">
                <span>Giảm giá voucher</span>
                <span class="font-semibold text-emerald-600">0đ</span>
              </div>
              <div class="flex justify-between text-slate-600">
                <span>Phí vận chuyển</span>
                <span class="font-semibold text-slate-800">Miễn phí</span>
              </div>
            </div>

            <div class="border-t border-slate-100 pt-3 flex justify-between items-center">
              <span class="text-xs font-bold text-slate-700">Tổng thanh toán</span>
              <span class="text-base font-black text-red-600">
                {{ totalAmount.toLocaleString('vi-VN') }}đ
              </span>
            </div>

            <button
              @click="checkout"
              class="w-full py-3 bg-indigo-600 hover:bg-indigo-700 text-white rounded-xl font-semibold text-xs tracking-wide shadow-sm transition active:scale-95"
            >
              Tiến hành thanh toán ({{ totalQuantity }})
            </button>
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
  if (!confirm('Bạn có chắc chắn muốn xóa sản phẩm này khỏi giỏ hàng không?')) return

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
  transition: all 0.3s ease;
}
.toast-enter-from,
.toast-leave-to {
  opacity: 0;
  transform: translateY(-20px);
}
</style>
