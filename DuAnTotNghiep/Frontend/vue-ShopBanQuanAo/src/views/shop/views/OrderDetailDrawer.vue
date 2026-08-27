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
        class="px-6 py-4 border-b border-slate-200 flex items-center justify-between sticky top-0 bg-white z-10 shadow-xs"
      >
        <h2 class="text-base font-bold text-slate-900">Chi tiết đơn hàng</h2>
        <button
          @click="$emit('close')"
          class="p-2 hover:bg-slate-100 rounded-full cursor-pointer text-slate-600 hover:text-slate-900 transition-colors"
        >
          ✕
        </button>
      </div>

      <!-- Loading State -->
      <div v-if="loading" class="py-20 text-center text-slate-600 text-sm font-medium">
        Đang tải thông tin chi tiết đơn hàng...
      </div>

      <!-- Error State -->
      <div v-else-if="errorMessage" class="py-10 text-center text-red-600 text-sm font-medium">
        {{ errorMessage }}
      </div>

      <!-- Content chính -->
      <div v-else-if="detail" class="p-6 space-y-4 flex-1">
        <!-- 1. Mã hóa đơn & Trạng thái badges -->
        <div
          class="flex items-center justify-between bg-white border border-slate-200 p-4 rounded-2xl shadow-xs"
        >
          <div>
            <span class="text-xs text-slate-600 font-medium">Mã hóa đơn: </span>
            <span class="text-sm font-bold text-slate-900">{{ info?.maHoaDon }}</span>
          </div>
          <div class="flex items-center gap-2">
            <!-- Badge trạng thái đơn hàng -->
            <span
              class="px-3 py-1 rounded-full text-xs font-bold"
              :class="isCancelled ? 'bg-rose-100 text-rose-800' : 'bg-emerald-100 text-emerald-800'"
            >
              {{ info?.trangThaiHienThi || 'Giao thành công' }}
            </span>
            <!-- Badge trạng thái thanh toán (Chỉ hiện khi đơn chưa hủy để tránh bị lặp) -->
            <span
              v-if="!isCancelled"
              class="px-3 py-1 rounded-full text-xs font-bold"
              :class="paymentBadgeClass"
            >
              {{ paymentStatusDisplay }}
            </span>
          </div>
        </div>

        <!-- 2. Grid 2 cột: Địa chỉ nhận hàng & Thông tin chung -->
        <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
          <!-- Địa chỉ nhận hàng -->
          <div
            class="bg-white border border-slate-200 p-4 rounded-2xl shadow-xs flex flex-col justify-between"
          >
            <div>
              <div class="text-xs font-bold text-slate-700 uppercase mb-2">
                📍 Địa chỉ nhận hàng
              </div>
              <p class="font-bold text-slate-900 text-sm">{{ detail.nguoiNhan?.tenNguoiNhan }}</p>
              <p class="text-xs text-slate-700 font-medium mt-1">
                📞 {{ detail.nguoiNhan?.soDienThoai }}
              </p>
              <p class="text-xs text-slate-800 mt-1.5 leading-relaxed font-medium">
                {{ detail.nguoiNhan?.diaChi }}
              </p>
            </div>
          </div>

          <!-- Thông tin chung -->
          <div class="bg-white border border-slate-200 p-4 rounded-2xl shadow-xs space-y-2.5">
            <div class="text-xs font-bold text-slate-700 uppercase mb-2">ℹ️ Thông tin chung</div>
            <div class="flex justify-between text-xs">
              <span class="text-slate-700 font-medium">Loại đơn:</span>
              <span class="font-bold text-slate-900 uppercase">{{ info?.loaiHoaDon }}</span>
            </div>
            <div class="flex justify-between text-xs">
              <span class="text-slate-700 font-medium">Ngày tạo:</span>
              <span class="font-semibold text-slate-900">
                {{ formatDateTime(info?.ngayTao) }}
              </span>
            </div>
            <!-- Hiển thị Ngày hủy nếu đơn hàng ở trạng thái hủy -->
            <div v-if="isCancelled" class="flex justify-between text-xs">
              <span class="text-slate-700 font-medium">Ngày hủy:</span>
              <span class="font-semibold text-slate-900">
                {{ formatDateTime(info?.ngayCapNhat) }}
              </span>
            </div>
            <div class="flex justify-between text-xs">
              <span class="text-slate-700 font-medium">Trạng thái TT:</span>
              <span class="font-semibold text-slate-900">{{ paymentStatusDisplay }}</span>
            </div>
          </div>
        </div>

        <!-- 3. Thông tin thanh toán chi tiết -->
        <div class="bg-white border border-slate-200 p-4 rounded-2xl shadow-xs space-y-2.5">
          <div class="text-xs font-bold text-slate-700 uppercase mb-1">
            💳 Thông tin thanh toán chi tiết
          </div>
          <div class="flex justify-between text-xs items-center">
            <span class="text-slate-700 font-medium">Phương thức thanh toán:</span>
            <span class="font-bold text-slate-900">{{
              detail.thanhToan?.phuongThucThanhToan
            }}</span>
          </div>
          <div class="flex justify-between text-xs items-center">
            <span class="text-slate-700 font-medium">Trạng thái giao dịch:</span>
            <span
              class="px-2.5 py-0.5 rounded-full text-[11px] font-bold"
              :class="paymentBadgeClass"
            >
              {{ paymentStatusDisplay }}
            </span>
          </div>
          <!-- Chỉ hiển thị thời gian thanh toán nếu đơn đã thực sự thanh toán và không bị hủy -->
          <div
            v-if="!isCancelled && isPaid && detail.thanhToan?.ngayThanhToan"
            class="flex justify-between text-xs items-center"
          >
            <span class="text-slate-700 font-medium">Thời gian thanh toán:</span>
            <span class="font-semibold text-slate-900">{{
              formatDateTime(detail.thanhToan?.ngayThanhToan)
            }}</span>
          </div>
          <div class="flex justify-between text-xs items-center">
            <span class="text-slate-700 font-medium">Số tiền thanh toán:</span>
            <span class="font-bold text-rose-600 text-sm"
              >{{ formatMoney(detail.thanhToan?.soTien) }} đ</span
            >
          </div>
        </div>

        <!-- 4. Sản phẩm đã mua -->
        <div class="bg-white border border-slate-200 p-4 rounded-2xl shadow-xs space-y-3">
          <div class="text-xs font-bold text-slate-700 uppercase">
            🛍️ Sản phẩm đã mua ({{ detail.sanPham?.length || 0 }})
          </div>

          <div
            v-for="sp in detail.sanPham"
            :key="sp.idHoaDonChiTiet"
            class="flex items-center justify-between gap-3 pt-2 border-t border-slate-100 first:border-t-0 first:pt-0"
          >
            <div class="flex items-center gap-3">
              <img
                :src="sp.anh ? `http://localhost:8080${sp.anh}` : ''"
                class="w-14 h-14 object-cover rounded-xl border border-slate-200 bg-slate-100 flex items-center justify-center text-[10px] text-slate-600 font-medium"
                alt="No Image"
              />
              <div>
                <p class="text-xs font-bold text-slate-900">{{ sp.tenSanPham }}</p>
                <div class="flex flex-wrap gap-1 mt-1.5">
                  <span
                    class="px-2 py-0.5 bg-slate-100 text-slate-700 rounded text-[10px] font-semibold"
                    >Mã SP: {{ sp.maSanPham }}</span
                  >
                  <span
                    class="px-2 py-0.5 bg-slate-100 text-slate-700 rounded text-[10px] font-semibold"
                    >Mã SPCT: {{ sp.maSPCT }}</span
                  >
                  <span
                    class="px-2 py-0.5 bg-slate-100 text-slate-700 rounded text-[10px] font-semibold"
                    >Màu: {{ sp.mauSac }}</span
                  >
                  <span
                    class="px-2 py-0.5 bg-slate-100 text-slate-700 rounded text-[10px] font-semibold"
                    >Size: {{ sp.kichThuoc }}</span
                  >
                </div>
              </div>
            </div>
            <div class="text-right shrink-0">
              <p class="text-[11px] text-slate-600 font-medium">
                {{ formatMoney(sp.donGia) }} đ x {{ sp.soLuong }}
              </p>
              <p class="text-xs font-bold text-rose-600 mt-0.5">
                {{ formatMoney(sp.thanhTien) }} đ
              </p>
            </div>
          </div>
        </div>

        <!-- 5. Tổng quan tài chính -->
        <div class="bg-white border border-slate-200 p-4 rounded-2xl shadow-xs space-y-2 text-xs">
          <div class="flex justify-between text-slate-700 font-medium">
            <span>Tiền hàng</span>
            <span class="font-semibold text-slate-900"
              >{{ formatMoney(info?.tongTienHang) }} đ</span
            >
          </div>

          <!-- Hiển thị Giảm giá Voucher nếu có -->
          <div v-if="detail?.voucher" class="flex justify-between text-slate-700 font-medium">
            <span>
              Giảm giá voucher
              <span v-if="detail.voucher.tenVoucher" class="text-[11px] text-slate-500 font-normal"
                >({{ detail.voucher.tenVoucher }})</span
              >
            </span>
            <span class="font-semibold text-emerald-600"
              >-{{ formatMoney(detail.voucher.soTienGiam) }} đ</span
            >
          </div>

          <!-- Hiển thị Giảm giá từ Xu nếu có sử dụng -->
          <div
            v-if="info?.tienGiamDoXu && info.tienGiamDoXu > 0"
            class="flex justify-between text-slate-700 font-medium"
          >
            <span>
              Giảm giá từ xu
              <span class="text-[11px] text-slate-500 font-normal">({{ info.soXuSuDung }} xu)</span>
            </span>
            <span class="font-semibold text-emerald-600"
              >-{{ formatMoney(info.tienGiamDoXu) }} đ</span
            >
          </div>

          <!-- Fallback nếu đơn cũ không dùng voucher/xu mà chỉ có tongGiamGia chung -->
          <div
            v-if="
              !detail?.voucher &&
              (!info?.tienGiamDoXu || info.tienGiamDoXu === 0) &&
              info?.tongGiamGia > 0
            "
            class="flex justify-between text-slate-700 font-medium"
          >
            <span>Giảm giá</span>
            <span class="font-semibold text-emerald-600"
              >-{{ formatMoney(info?.tongGiamGia) }} đ</span
            >
          </div>

          <div class="flex justify-between text-slate-700 font-medium">
            <span>Phí vận chuyển</span>
            <span class="font-semibold text-slate-900"
              >{{ formatMoney(info?.phiVanChuyen) }} đ</span
            >
          </div>

          <div class="pt-3 border-t border-slate-200 flex justify-between items-center">
            <span class="font-bold text-slate-900 text-sm">Tổng thanh toán</span>
            <span class="font-extrabold text-rose-600 text-base"
              >{{ formatMoney(info?.tongThanhToan) }} đ</span
            >
          </div>
        </div>
      </div>

      <!-- Footer Action -->
      <div class="px-6 py-4 border-t border-slate-200 flex justify-end bg-white">
        <button
          @click="$emit('close')"
          class="px-6 py-2.5 bg-white border border-slate-300 hover:bg-slate-100 text-slate-800 text-xs font-bold rounded-xl cursor-pointer transition-colors shadow-xs"
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

// Kiểm tra đơn hàng đã hủy chưa
const isCancelled = computed(() => {
  return info.value?.trangThai === 'da_huy' || detail.value?.theoDoi?.daHuy === true
})

// Kiểm tra đơn đã thanh toán chưa (dựa vào trạng thái thanh toán)
const isPaid = computed(() => {
  return info.value?.trangThaiThanhToan === 'da_thanh_toan'
})

// Hiển thị chữ trạng thái thanh toán / giao dịch (Nếu hủy thì hiện "Đã hủy")
const paymentStatusDisplay = computed(() => {
  if (isCancelled.value) {
    return 'Đã hủy'
  }
  return info.value?.trangThaiThanhToanHienThi || 'Chưa thanh toán'
})

// Màu sắc huy hiệu trạng thái thanh toán / giao dịch
const paymentBadgeClass = computed(() => {
  if (isCancelled.value) {
    return 'bg-rose-100 text-rose-800' // Màu đỏ cho trạng thái đã hủy
  }
  if (isPaid.value) {
    return 'bg-emerald-100 text-emerald-800'
  }
  return 'bg-amber-100 text-amber-800' // Màu cam/vàng cho trạng thái chưa thanh toán
})

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

// Format dạng giờ phút giây và ngày tháng năm (ví dụ: 08:27:08 24/08/2026)
const formatDateTime = (val) => {
  if (!val) return ''
  const d = new Date(val)
  const time = d.toLocaleTimeString('vi-VN', {
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit',
  })
  const date = d.toLocaleDateString('vi-VN')
  return `${time} ${date}`
}

onMounted(() => {
  fetchOrderDetail()
})
</script>
