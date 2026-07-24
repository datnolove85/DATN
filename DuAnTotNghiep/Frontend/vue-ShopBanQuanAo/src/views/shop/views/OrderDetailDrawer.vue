<template>
  <!-- Overlay nền tối -->
  <div class="fixed inset-0 bg-slate-900/40 backdrop-blur-xs z-50 flex justify-end">
    <!-- Drawer Panel -->
    <div
      class="w-full max-w-2xl bg-white h-full shadow-2xl flex flex-col justify-between p-6 overflow-y-auto"
    >
      <!-- 1. Header -->
      <div class="flex items-center justify-between pb-4 border-b border-slate-100">
        <div>
          <h2 class="text-lg font-bold text-slate-800">
            Chi tiết đơn hàng #{{ info?.maHoaDon || '...' }}
          </h2>
          <p v-if="info?.ngayTao" class="text-xs text-slate-400 mt-0.5">
            Ngày tạo: {{ formatDate(info.ngayTao) }}
          </p>
        </div>
        <button
          @click="$emit('close')"
          class="p-2 hover:bg-slate-100 rounded-full cursor-pointer text-slate-500 transition-colors"
        >
          ✕
        </button>
      </div>

      <!-- Loading State -->
      <div v-if="loading" class="py-20 text-center text-slate-400 text-sm">
        Đang tải thông tin chi tiết đơn hàng...
      </div>

      <!-- Error State -->
      <div v-else-if="errorMessage" class="py-10 text-center text-red-500 text-sm">
        {{ errorMessage }}
      </div>

      <!-- Content Chi Tiết -->
      <div v-else-if="detail" class="space-y-6 py-4 flex-1">
        <!-- 2. Tiến trình đơn hàng (Theo dõi) -->
        <div v-if="detail.theoDoi" class="bg-slate-50 p-4 rounded-2xl border border-slate-100">
          <h3 class="font-bold text-xs uppercase text-slate-400 mb-3">Trạng thái đơn hàng</h3>

          <div
            v-if="detail.theoDoi.daHuy"
            class="p-3 bg-red-50 text-red-600 rounded-xl text-xs font-semibold text-center"
          >
            ❌ Đơn hàng này đã bị hủy
          </div>

          <div
            v-else-if="detail.theoDoi.giaoThatBai"
            class="p-3 bg-amber-50 text-amber-700 rounded-xl text-xs font-semibold text-center"
          >
            ⚠️ Giao hàng thất bại
          </div>

          <div v-else class="grid grid-cols-5 gap-1 text-center text-[10px] font-bold">
            <div :class="detail.theoDoi.choXacNhan ? 'text-emerald-600' : 'text-slate-300'">
              <div
                class="w-3 h-3 mx-auto rounded-full mb-1"
                :class="detail.theoDoi.choXacNhan ? 'bg-emerald-500' : 'bg-slate-200'"
              ></div>
              Chờ xác nhận
            </div>
            <div :class="detail.theoDoi.daXacNhan ? 'text-emerald-600' : 'text-slate-300'">
              <div
                class="w-3 h-3 mx-auto rounded-full mb-1"
                :class="detail.theoDoi.daXacNhan ? 'bg-emerald-500' : 'bg-slate-200'"
              ></div>
              Đã xác nhận
            </div>
            <div :class="detail.theoDoi.choVanChuyen ? 'text-emerald-600' : 'text-slate-300'">
              <div
                class="w-3 h-3 mx-auto rounded-full mb-1"
                :class="detail.theoDoi.choVanChuyen ? 'bg-emerald-500' : 'bg-slate-200'"
              ></div>
              Chờ vận chuyển
            </div>
            <div :class="detail.theoDoi.dangGiao ? 'text-emerald-600' : 'text-slate-300'">
              <div
                class="w-3 h-3 mx-auto rounded-full mb-1"
                :class="detail.theoDoi.dangGiao ? 'bg-emerald-500' : 'bg-slate-200'"
              ></div>
              Đang giao
            </div>
            <div
              :class="
                detail.theoDoi.giaoThanhCong || detail.theoDoi.hoanThanh
                  ? 'text-emerald-600'
                  : 'text-slate-300'
              "
            >
              <div
                class="w-3 h-3 mx-auto rounded-full mb-1"
                :class="
                  detail.theoDoi.giaoThanhCong || detail.theoDoi.hoanThanh
                    ? 'bg-emerald-500'
                    : 'bg-slate-200'
                "
              ></div>
              Hoàn thành
            </div>
          </div>
        </div>

        <!-- 3. Thông tin người nhận -->
        <div class="bg-slate-50 p-4 rounded-2xl border border-slate-100">
          <h3 class="font-bold text-xs uppercase text-slate-400 mb-2">Thông tin người nhận</h3>
          <p class="font-semibold text-slate-800 text-sm">{{ detail.nguoiNhan?.tenNguoiNhan }}</p>
          <p class="text-xs text-slate-500 mt-0.5">SĐT: {{ detail.nguoiNhan?.soDienThoai }}</p>
          <p class="text-xs text-slate-600 mt-1">Địa chỉ: {{ detail.nguoiNhan?.diaChi }}</p>
        </div>

        <!-- 4. Danh sách sản phẩm -->
        <div>
          <h3 class="font-bold text-xs uppercase text-slate-400 mb-2">
            Sản phẩm ({{ detail.sanPham?.length || 0 }})
          </h3>
          <div class="divide-y divide-slate-100 border-t border-b border-slate-100">
            <div
              v-for="sp in detail.sanPham"
              :key="sp.idHoaDonChiTiet"
              class="py-3 flex items-center gap-3"
            >
              <img
                :src="sp.anh ? `http://localhost:8080${sp.anh}` : '/placeholder.png'"
                class="w-14 h-14 object-cover rounded-xl border border-slate-100 bg-slate-50"
                alt="Hình sản phẩm"
              />
              <div class="flex-1">
                <p class="text-xs font-bold text-slate-800 line-clamp-1">{{ sp.tenSanPham }}</p>
                <div class="flex gap-2 text-[11px] text-slate-400 mt-1">
                  <span>Màu: {{ sp.mauSac }}</span>
                  <span>|</span>
                  <span>Size: {{ sp.kichThuoc }}</span>
                  <span>|</span>
                  <span>SL: x{{ sp.soLuong }}</span>
                </div>
              </div>
              <div class="text-right">
                <p class="text-xs font-bold text-slate-900">{{ formatMoney(sp.thanhTien) }}đ</p>
                <p class="text-[10px] text-slate-400">{{ formatMoney(sp.donGia) }}đ / sp</p>
              </div>
            </div>
          </div>
        </div>

        <!-- 5. Thông tin Trả hàng (Nếu có) -->
        <div
          v-if="detail.traHang?.coTraHang"
          class="bg-amber-50 border border-amber-200 p-4 rounded-2xl"
        >
          <div class="flex justify-between items-center mb-1">
            <h3 class="font-bold text-xs text-amber-800">
              Yêu cầu trả hàng (#{{ detail.traHang.maTraHang }})
            </h3>
            <span
              class="text-[10px] px-2 py-0.5 bg-amber-200 text-amber-800 font-bold rounded-full"
            >
              {{ detail.traHang.trangThai }}
            </span>
          </div>
          <p class="text-xs text-amber-700">Lý do: {{ detail.traHang.lyDo }}</p>
          <p class="text-xs font-bold text-amber-900 mt-1">
            Tổng tiền hoàn: {{ formatMoney(detail.traHang.tongTienHoan) }}đ
          </p>
        </div>

        <!-- 6. Tổng quan thanh toán -->
        <div class="space-y-2 pt-2 border-t border-slate-100 text-xs">
          <div class="flex justify-between text-slate-500">
            <span>Tạm tính</span>
            <span>{{ formatMoney(info?.tongTienHang) }}đ</span>
          </div>
          <div class="flex justify-between text-slate-500">
            <span>Phí vận chuyển</span>
            <span>+{{ formatMoney(info?.phiVanChuyen) }}đ</span>
          </div>
          <div class="flex justify-between text-slate-500">
            <span>Giảm giá</span>
            <span class="text-emerald-600">-{{ formatMoney(info?.tongGiamGia) }}đ</span>
          </div>
          <div
            class="flex justify-between font-bold text-slate-900 text-sm pt-2 border-t border-slate-100"
          >
            <span>Tổng thanh toán</span>
            <span class="text-emerald-600">{{ formatMoney(info?.tongThanhToan) }}đ</span>
          </div>
          <div class="flex justify-between text-[11px] text-slate-400 pt-1">
            <span>Thanh toán: {{ info?.trangThaiThanhToanHienThi }}</span>
            <span v-if="detail.thanhToan?.maGiaoDich">
              Mã GD: {{ detail.thanhToan.maGiaoDich }}
            </span>
          </div>
        </div>
      </div>

      <!-- Footer Action -->
      <div class="pt-4 border-t border-slate-100 flex justify-end">
        <button
          @click="$emit('close')"
          class="px-5 py-2.5 bg-slate-100 hover:bg-slate-200 text-slate-700 text-xs font-bold rounded-xl cursor-pointer transition-colors"
        >
          Đóng
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
// Thay đường dẫn import bên dưới cho khớp với vị trí file donHangService.js trong dự án của bạn
import donHangService from '@/service/DonHangService'

const props = defineProps({
  idHoaDon: {
    type: [Number, String],
    required: true,
  },
})

defineEmits(['close', 'reload'])

const detail = ref(null)
const loading = ref(false)
const errorMessage = ref('')

// Computed lấy phần thông tin chung của đơn hàng
const info = computed(() => detail.value?.thongTinDonHang)

// Gọi hàm từ donHangService
const fetchOrderDetail = async () => {
  loading.value = true
  errorMessage.value = ''

  try {
    const data = await donHangService.layChiTietDonHang(props.idHoaDon)
    detail.value = data
  } catch (err) {
    console.error('Lỗi khi lấy chi tiết đơn hàng:', err)
    errorMessage.value = err.message || 'Có lỗi xảy ra khi lấy thông tin đơn hàng.'
  } finally {
    loading.value = false
  }
}

// Formatters
const formatMoney = (val) => (val != null ? val.toLocaleString('vi-VN') : '0')
const formatDate = (val) => (val ? new Date(val).toLocaleString('vi-VN') : '')

onMounted(() => {
  fetchOrderDetail()
})
</script>
