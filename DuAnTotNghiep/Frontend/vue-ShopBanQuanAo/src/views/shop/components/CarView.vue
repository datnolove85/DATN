<template>
  <div
    class="min-h-screen bg-gradient-to-br from-slate-100 via-indigo-50 to-purple-50 py-10 px-4 font-sans"
  >
    <div class="max-w-6xl mx-auto">
      <!-- Tiêu đề với trang trí nhỏ -->
      <div
        class="bg-gradient-to-r from-indigo-600 via-purple-600 to-pink-500 rounded-3xl p-8 text-white mb-8 shadow-xl"
      >
        <div class="flex justify-between items-center">
          <div>
            <h1 class="text-4xl font-black">🛒 Giỏ hàng của bạn</h1>

            <p class="mt-2 text-indigo-100">Kiểm tra lại sản phẩm trước khi thanh toán.</p>
          </div>

          <div class="bg-white/20 backdrop-blur px-6 py-4 rounded-2xl text-center">
            <div class="text-sm">Sản phẩm đã chọn</div>

            <div class="text-3xl font-bold">
              {{ totalQuantity }}
            </div>
          </div>
        </div>
      </div>

      <div class="grid grid-cols-1 lg:grid-cols-3 gap-8">
        <!-- Danh sách sản phẩm -->
        <div class="lg:col-span-2 space-y-4">
          <div class="bg-white rounded-2xl shadow p-5 flex justify-between items-center">
            <div class="flex items-center gap-3">
              <input type="checkbox" :checked="allSelected" @change="toggleSelectAll" />

              <span class="font-semibold"> Chọn tất cả </span>
            </div>

            <button
              @click="deleteSelectedItems"
              class="px-4 py-2 rounded-xl border border-red-200 text-red-500 hover:bg-red-50"
            >
              Xóa đã chọn
            </button>
          </div>

          <div
            v-for="item in cart"
            :key="item.id"
            class="bg-white rounded-3xl border border-slate-200 shadow-sm hover:shadow-xl hover:border-indigo-200 transition-all duration-300 p-6"
          >
            <div class="grid grid-cols-12 gap-6 items-center">
              <!-- Checkbox -->
              <div class="col-span-1 flex justify-center">
                <input type="checkbox" v-model="item.selected" class="w-5 h-5 accent-indigo-600" />
              </div>

              <!-- Ảnh -->
              <div class="col-span-2">
                <div class="w-28 h-28 rounded-2xl overflow-hidden bg-slate-100 border">
                  <img
                    v-if="item.anh"
                    :src="'http://localhost:8080' + item.anh"
                    class="w-full h-full object-cover transition duration-500 hover:scale-110"
                  />

                  <div v-else class="w-full h-full flex items-center justify-center text-4xl">
                    📦
                  </div>
                </div>
              </div>

              <!-- Thông tin -->
              <div class="col-span-5">
                <h3 class="text-xl font-bold text-slate-800 mb-3">
                  {{ item.tenSanPham }}
                </h3>

                <div class="flex flex-wrap gap-2 mb-3">
                  <span class="bg-green-100 text-green-700 px-3 py-1 rounded-full text-xs">
                    ✔ Còn hàng
                  </span>

                  <span class="bg-yellow-100 text-yellow-700 px-3 py-1 rounded-full text-xs">
                    ⭐ 4.9
                  </span>

                  <span class="bg-red-100 text-red-600 px-3 py-1 rounded-full text-xs">
                    🔥 Bán chạy
                  </span>
                </div>

                <div class="flex flex-wrap gap-2 mb-3">
                  <span class="bg-slate-100 text-slate-700 px-3 py-1 rounded-lg text-sm">
                    Mã: {{ item.maSanPhamChiTiet }}
                  </span>

                  <span class="bg-blue-100 text-blue-700 px-3 py-1 rounded-lg text-sm">
                    {{ item.mauSac }}
                  </span>

                  <span class="bg-purple-100 text-purple-700 px-3 py-1 rounded-lg text-sm">
                    Size {{ item.kichCo }}
                  </span>
                </div>

                <div class="space-y-1 text-sm">
                  <p class="text-slate-500">
                    Thương hiệu:
                    <span class="font-semibold text-slate-700"> Nike </span>
                  </p>

                  <p class="text-emerald-600">🚚 Miễn phí vận chuyển</p>
                </div>
              </div>

              <!-- Giá -->
              <!-- Giá + Số lượng -->
              <div class="col-span-3">
                <div class="bg-slate-50 rounded-2xl p-4 border border-slate-100">
                  <div class="text-xs text-slate-400">Đơn giá</div>

                  <div class="text-lg font-bold text-red-600 mb-4">
                    {{ Number(item.giaBan).toLocaleString('vi-VN') }}đ
                  </div>

                  <div class="flex items-center justify-between">
                    <span class="text-xs text-slate-500"> SL </span>

                    <div class="flex items-center bg-white rounded-lg border p-1">
                      <button
                        @click="updateQuantity(item, -1)"
                        class="w-6 h-6 flex items-center justify-center hover:bg-slate-100 rounded"
                      >
                        <Minus class="w-3 h-3" />
                      </button>

                      <input
                        v-model.number="item.soLuong"
                        @change="changeQuantity(item)"
                        class="w-8 text-center text-sm font-bold outline-none"
                        type="number"
                        min="1"
                      />

                      <button
                        @click="updateQuantity(item, 1)"
                        class="w-6 h-6 flex items-center justify-center hover:bg-slate-100 rounded"
                      >
                        <Plus class="w-3 h-3" />
                      </button>
                    </div>
                  </div>

                  <div class="border-t mt-4 pt-3">
                    <div class="text-xs text-slate-400">Thành tiền</div>

                    <div class="text-xl font-black text-red-600">
                      {{ Number(item.thanhTien).toLocaleString('vi-VN') }}đ
                    </div>
                  </div>
                </div>
              </div>

              <!-- Xóa -->
              <!-- Xóa -->
              <div class="col-span-1 flex justify-center items-start pt-2">
                <button
                  @click="deleteItem(item.id)"
                  class="w-11 h-11 rounded-xl border border-red-200 text-red-500 hover:bg-red-50 flex items-center justify-center"
                >
                  <Trash2 class="w-5 h-5" />
                </button>
              </div>
            </div>
          </div>
        </div>

        <!-- Sidebar Thanh toán -->
        <div class="lg:col-span-1">
          <div
            class="bg-slate-900 text-white rounded-3xl p-8 sticky top-8 shadow-2xl shadow-indigo-200"
          >
            <h3 class="text-lg font-bold mb-6 flex items-center gap-2">
              <ShoppingBag class="w-5 h-5" /> Tóm tắt hóa đơn
            </h3>

            <div class="space-y-4 text-slate-400">
              <div class="flex justify-between">
                <span>Tạm tính</span
                ><span class="text-white font-semibold"
                  >{{ totalAmount.toLocaleString('vi-VN') }}đ</span
                >
              </div>
              <div class="flex justify-between">
                <span>Phí vận chuyển</span
                ><span class="text-emerald-400 font-semibold">Miễn phí</span>
              </div>
            </div>

            <div class="border-t border-slate-700 my-6"></div>

            <div class="flex justify-between items-end mb-8">
              <span>Tổng cộng</span>
              <span class="text-3xl font-black text-white"
                >{{ totalAmount.toLocaleString('vi-VN') }}đ</span
              >
            </div>

            <button
              @click="checkout"
              class="w-full py-4 bg-indigo-600 hover:bg-indigo-500 text-white rounded-2xl font-bold text-lg transition-all active:scale-95 shadow-lg shadow-indigo-500/30"
            >
              Thanh toán ngay
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
import { Trash2, Plus, Minus, ShoppingBag } from 'lucide-vue-next'
import axios from 'axios'
import emitter from '@/utils/emitter'

const router = useRouter()
const cart = ref([])

/**
 * Tính tổng thành tiền
 */
const totalAmount = computed(() => {
  return cart.value
    .filter((item) => item.selected)
    .reduce((sum, item) => sum + Number(item.thanhTien), 0)
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

/**
 * Tính tổng số lượng
 */
const totalQuantity = computed(() => {
  return cart.value
    .filter((item) => item.selected)
    .reduce((sum, item) => sum + Number(item.soLuong), 0)
})

/**
 * Gọi API lấy dữ liệu giỏ hàng
 */
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

    // Lưu lại trạng thái checkbox đang chọn để không bị mất khi load lại
    const oldSelectedMap = new Map(cart.value.map((i) => [i.id, i.selected]))

    cart.value = (res.data || []).map((item) => ({
      ...item,
      selected: oldSelectedMap.get(item.id) || false,
    }))
  } catch (err) {
    console.error('Lỗi khi tải dữ liệu giỏ hàng:', err)
    cart.value = []
  }
}

/**
 * Xử lý Tăng / Giảm số lượng (ĐÃ SỬA ĐỂ NÚT TRỪ HOẠT ĐỘNG MƯỢT MÀ)
 */
const updateQuantity = async (item, change) => {
  const newQty = item.soLuong + change

  if (newQty <= 0) {
    await deleteItem(item.id)
    return
  }

  // Cập nhật ngay lập tức trên giao diện (Optimistic UI) tránh bị giật lag
  item.soLuong = newQty
  item.thanhTien = item.soLuong * item.giaBan

  try {
    const token = sessionStorage.getItem('token')
    await axios.put(
      `http://localhost:8080/giohang/update/${item.id}`,
      { soLuong: newQty },
      { headers: { Authorization: `Bearer ${token}` } },
    )

    // Phát sự kiện để cập nhật lại số lượng trên thanh Header chung
    emitter.emit('cart-updated')
  } catch (err) {
    console.error('Lỗi khi cập nhật số lượng:', err)
    // Nếu API lỗi, tải lại giỏ hàng cũ để đồng bộ dữ liệu
    await loadCart()
  }
}

/**
 * Khi gõ trực tiếp số lượng vào ô input
 */
const changeQuantity = async (item) => {
  if (item.soLuong <= 0 || isNaN(item.soLuong)) {
    await deleteItem(item.id)
    return
  }

  item.thanhTien = item.soLuong * item.giaBan

  try {
    const token = sessionStorage.getItem('token')
    await axios.put(
      `http://localhost:8080/giohang/update/${item.id}`,
      { soLuong: item.soLuong },
      {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      },
    )

    emitter.emit('cart-updated')
  } catch (err) {
    console.error('Lỗi khi cập nhật số lượng:', err)
    await loadCart()
  }
}

/**
 * Xóa sản phẩm đơn lẻ
 */
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
  } catch (err) {
    console.error('Lỗi khi xóa sản phẩm:', err)
  }
}

/**
 * Xóa các sản phẩm đã chọn
 */
const deleteSelectedItems = async () => {
  const selectedItems = cart.value.filter((item) => item.selected)

  if (selectedItems.length === 0) {
    alert('Vui lòng chọn sản phẩm muốn xóa!')
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
  } catch (err) {
    console.error('Lỗi khi xóa sản phẩm đã chọn:', err)
  }
}

/**
 * Chuyển hướng sang trang xác nhận thanh toán
 */
const checkout = () => {
  const selectedItems = cart.value.filter((item) => item.selected)

  if (selectedItems.length === 0) {
    alert('Vui lòng chọn ít nhất một sản phẩm!')
    return
  }

  const checkoutData = {
    items: selectedItems.map((item) => ({
      productDetailId: item.idSanPhamChiTiet,
      quantity: item.soLuong,
      tenSanPham: item.tenSanPham,
      maSanPhamChiTiet: item.maSanPhamChiTiet,
      giaBan: item.giaBan,
      thanhTien: item.thanhTien,
      mauSac: item.mauSac,
      kichCo: item.kichCo,
      anh: item.anh,
    })),
  }

  sessionStorage.setItem('checkoutData', JSON.stringify(checkoutData))
  router.push('/xacnhan')
}

onMounted(() => {
  loadCart()
})
</script>
