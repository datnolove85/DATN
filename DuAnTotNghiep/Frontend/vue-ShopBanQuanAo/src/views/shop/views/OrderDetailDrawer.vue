<template>
  <!-- Màn che lót nền (Backdrop) -->
  <div
    class="fixed inset-0 bg-slate-900/50 backdrop-blur-xs z-50 flex justify-end transition-opacity"
    @click="$emit('close')"
  >
    <!-- Khung nội dung Drawer -->
    <div
      class="w-full max-w-2xl bg-white h-full shadow-2xl flex flex-col justify-between overflow-y-auto animate-in slide-in-from-right duration-200"
      @click.stop
    >
      <!-- Header -->
      <div
        class="px-6 py-4 border-b border-slate-100 flex items-center justify-between sticky top-0 bg-white z-10 shadow-xs"
      >
        <h2 class="text-lg font-bold text-slate-800">Chi tiết đơn hàng</h2>
        <button
          @click="$emit('close')"
          class="p-2 hover:bg-slate-100 rounded-full cursor-pointer text-slate-400 hover:text-slate-600 transition-colors"
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

      <!-- Content chính -->
      <div v-else-if="detail" class="p-6 space-y-4 flex-1">
        <!-- 1. Mã hóa đơn & Trạng thái badges -->
        <div
          class="flex items-center justify-between bg-white border border-slate-100 p-4 rounded-2xl shadow-xs"
        >
          <div>
            <span class="text-xs text-slate-500">Mã hóa đơn: </span>
            <span class="text-sm font-bold text-slate-800">{{ info?.maHoaDon }}</span>
          </div>
          <div class="flex items-center gap-2">
            <span class="px-3 py-1 rounded-full text-xs font-bold bg-emerald-100 text-emerald-700">
              {{ info?.trangThaiHienThi || 'Giao thành công' }}
            </span>
            <span class="px-3 py-1 rounded-full text-xs font-bold bg-emerald-100 text-emerald-700">
              {{ info?.trangThaiThanhToanHienThi || 'Đã thanh toán' }}
            </span>
          </div>
        </div>

        <!-- 2. Grid 2 cột: Địa chỉ nhận hàng & Thông tin chung -->
        <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
          <!-- Địa chỉ nhận hàng -->
          <div
            class="bg-white border border-slate-100 p-4 rounded-2xl shadow-xs flex flex-col justify-between"
          >
            <div>
              <div class="text-xs font-bold text-slate-400 uppercase mb-2">
                📍 Địa chỉ nhận hàng
              </div>
              <p class="font-bold text-slate-800 text-sm">{{ detail.nguoiNhan?.tenNguoiNhan }}</p>
              <p class="text-xs text-slate-500 mt-1">📞 {{ detail.nguoiNhan?.soDienThoai }}</p>
              <p class="text-xs text-slate-600 mt-1.5 leading-relaxed">
                {{ detail.nguoiNhan?.diaChi }}
              </p>
            </div>
          </div>

          <!-- Thông tin chung -->
          <div class="bg-white border border-slate-100 p-4 rounded-2xl shadow-xs space-y-2.5">
            <div class="text-xs font-bold text-slate-400 uppercase mb-2">ℹ️ Thông tin chung</div>
            <div class="flex justify-between text-xs">
              <span class="text-slate-500">Loại đơn:</span>
              <span class="font-bold text-slate-800 uppercase">{{ info?.loaiHoaDon }}</span>
            </div>
            <div class="flex justify-between text-xs">
              <span class="text-slate-500">Ngày tạo:</span>
              <span class="font-medium text-slate-700">{{ formatDateTime(info?.ngayTao) }}</span>
            </div>
            <div class="flex justify-between text-xs">
              <span class="text-slate-500">Trạng thái TT:</span>
              <span class="font-medium text-slate-700">{{ info?.trangThaiThanhToanHienThi }}</span>
            </div>
          </div>
        </div>

        <!-- 3. Thông tin thanh toán chi tiết -->
        <div class="bg-white border border-slate-100 p-4 rounded-2xl shadow-xs space-y-2.5">
          <div class="text-xs font-bold text-slate-400 uppercase mb-1">
            💳 Thông tin thanh toán chi tiết
          </div>
          <div class="flex justify-between text-xs items-center">
            <span class="text-slate-500">Phương thức thanh toán:</span>
            <span class="font-bold text-slate-800">{{
              detail.thanhToan?.phuongThucThanhToan
            }}</span>
          </div>
          <div class="flex justify-between text-xs items-center">
            <span class="text-slate-500">Trạng thái giao dịch:</span>
            <span
              class="px-2.5 py-0.5 rounded-full text-[11px] font-bold bg-emerald-100 text-emerald-700"
            >
              {{ info?.trangThaiThanhToanHienThi }}
            </span>
          </div>
          <div class="flex justify-between text-xs items-center">
            <span class="text-slate-500">Thời gian thanh toán:</span>
            <span class="font-medium text-slate-700">{{
              formatDateTime(detail.thanhToan?.ngayThanhToan)
            }}</span>
          </div>
          <div class="flex justify-between text-xs items-center">
            <span class="text-slate-500">Số tiền thanh toán:</span>
            <span class="font-bold text-rose-600 text-sm"
              >{{ formatMoney(detail.thanhToan?.soTien) }} đ</span
            >
          </div>
        </div>

        <!-- 4. Sản phẩm đã mua -->
        <div class="bg-white border border-slate-100 p-4 rounded-2xl shadow-xs space-y-3">
          <div class="text-xs font-bold text-slate-400 uppercase">
            🛍️ Sản phẩm đã mua ({{ detail.sanPham?.length || 0 }})
          </div>

          <div
            v-for="sp in detail.sanPham"
            :key="sp.idHoaDonChiTiet"
            class="flex items-center justify-between gap-3 pt-2"
          >
            <div class="flex items-center gap-3">
              <img
                :src="sp.anh ? `http://localhost:8080${sp.anh}` : ''"
                class="w-14 h-14 object-cover rounded-xl border border-slate-100 bg-slate-100 flex items-center justify-center text-[10px] text-slate-400"
                alt="No Image"
              />
              <div>
                <p class="text-xs font-bold text-slate-800">{{ sp.tenSanPham }}</p>
                <div class="flex flex-wrap gap-1 mt-1.5">
                  <span
                    class="px-2 py-0.5 bg-slate-100 text-slate-600 rounded text-[10px] font-medium"
                    >Mã SP: {{ sp.maSanPham }}</span
                  >
                  <span
                    class="px-2 py-0.5 bg-slate-100 text-slate-600 rounded text-[10px] font-medium"
                    >Mã SPCT: {{ sp.maSPCT }}</span
                  >
                  <span
                    class="px-2 py-0.5 bg-slate-100 text-slate-600 rounded text-[10px] font-medium"
                    >Màu: {{ sp.mauSac }}</span
                  >
                  <span
                    class="px-2 py-0.5 bg-slate-100 text-slate-600 rounded text-[10px] font-medium"
                    >Size: {{ sp.kichThuoc }}</span
                  >
                </div>
              </div>
            </div>
            <div class="text-right shrink-0">
              <p class="text-[11px] text-slate-400">
                {{ formatMoney(sp.donGia) }} đ x {{ sp.soLuong }}
              </p>
              <p class="text-xs font-bold text-rose-600 mt-0.5">
                {{ formatMoney(sp.thanhTien) }} đ
              </p>
            </div>
          </div>
        </div>

        <!-- 5. Tổng quan tài chính -->
        <div class="bg-white border border-slate-100 p-4 rounded-2xl shadow-xs space-y-2 text-xs">
          <div class="flex justify-between text-slate-500">
            <span>Tiền hàng</span>
            <span class="font-medium text-slate-700">{{ formatMoney(info?.tongTienHang) }} đ</span>
          </div>
          <div class="flex justify-between text-slate-500">
            <span>Giảm giá voucher</span>
            <span class="font-medium text-slate-700">-{{ formatMoney(info?.tongGiamGia) }} đ</span>
          </div>
          <div class="flex justify-between text-slate-500">
            <span>Phí vận chuyển</span>
            <span class="font-medium text-slate-700">{{ formatMoney(info?.phiVanChuyen) }} đ</span>
          </div>
          <div class="pt-3 border-t border-slate-100 flex justify-between items-center">
            <span class="font-bold text-slate-800 text-sm">Tổng thanh toán</span>
            <span class="font-extrabold text-rose-600 text-base"
              >{{ formatMoney(info?.tongThanhToan) }} đ</span
            >
          </div>
        </div>
      </div>

      <!-- Footer Action -->
      <div class="px-6 py-4 border-t border-slate-100 flex justify-end bg-white">
        <button
          @click="$emit('close')"
          class="px-6 py-2.5 bg-white border border-slate-200 hover:bg-slate-50 text-slate-700 text-xs font-bold rounded-xl cursor-pointer transition-colors shadow-xs"
        >
          Đóng
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import donHangService from '@/service/DonHangService'

const props = defineProps({
  idHoaDon: {
    type: [Number, String],
    required: true,
  },
})

defineEmits(['close'])

const detail = ref(null)
const loading = ref(false)
const errorMessage = ref('')

const info = computed(() => detail.value?.thongTinDonHang)

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

const formatMoney = (val) => (val != null ? val.toLocaleString('vi-VN') : '0')

// Format dạng giờ phút và ngày tháng năm y hệt ảnh (ví dụ: 15:45 19/08/2026)
const formatDateTime = (val) => {
  if (!val) return ''
  const d = new Date(val)
  const time = d.toLocaleTimeString('vi-VN', { hour: '2-digit', minute: '2-digit' })
  const date = d.toLocaleDateString('vi-VN')
  return `${time} ${date}`
}

onMounted(() => {
  fetchOrderDetail()
})
</script>
