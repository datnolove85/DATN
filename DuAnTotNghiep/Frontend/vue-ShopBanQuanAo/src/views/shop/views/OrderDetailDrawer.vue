<template>
  <!-- 1. MÀN CHE LÓT NỀN (Backdrop): Bấm vào bất kỳ đâu trên vùng mờ ngoài này sẽ đóng Drawer -->
  <div
    class="fixed inset-0 bg-slate-900/50 backdrop-blur-xs z-50 flex justify-end transition-opacity"
    @click="$emit('close')"
  >
    <!-- 2. KHUNG NỘI DUNG DRAWER: Bắt buộc có @click.stop để bấm bên trong KHÔNG bị tắt -->
    <div
      class="w-full max-w-2xl bg-white h-full shadow-2xl flex flex-col justify-between p-6 overflow-y-auto animate-in slide-in-from-right duration-200"
      @click.stop
    >
      <!-- Header -->
      <div class="flex items-center justify-between pb-4 border-b border-slate-100">
        <div>
          <h2 class="text-lg font-bold text-slate-800 flex items-center gap-2">
            Chi tiết đơn hàng #{{ info?.maHoaDon || '...' }}
            <span
              v-if="detail?.theoDoi?.daHuy"
              class="px-2.5 py-0.5 rounded-full text-[10px] font-extrabold bg-rose-100 text-rose-700 uppercase"
            >
              Đã Hủy
            </span>
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
        <!-- TH1: Hiển thị Lý do hủy nếu Đơn đã Hủy -->
        <div
          v-if="detail.theoDoi?.daHuy"
          class="bg-rose-50 border border-rose-200 p-4 rounded-2xl space-y-1"
        >
          <div class="flex items-center gap-2 text-rose-700 font-bold text-xs uppercase">
            <span>❌ Đơn hàng này đã bị hủy</span>
          </div>
          <p class="text-xs text-rose-800 font-medium">
            <span class="font-bold">Lý do hủy:</span>
            {{ detail.lyDoHuy || 'Sản phẩm bị lỗi kỹ thuật / Khách hàng hủy' }}
          </p>
        </div>

        <!-- TH2: Tiến trình đơn hàng bình thường -->
        <div v-else-if="detail.theoDoi" class="bg-slate-50 p-4 rounded-2xl border border-slate-100">
          <h3 class="font-bold text-xs uppercase text-slate-400 mb-3">Trạng thái đơn hàng</h3>

          <div class="grid grid-cols-5 gap-1 text-center text-[10px] font-bold">
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
            <div :class="detail.theoDoi.hoanThanh ? 'text-emerald-600' : 'text-slate-300'">
              <div
                class="w-3 h-3 mx-auto rounded-full mb-1"
                :class="detail.theoDoi.hoanThanh ? 'bg-emerald-500' : 'bg-slate-200'"
              ></div>
              Hoàn thành
            </div>
          </div>
        </div>

        <!-- Thông tin người nhận -->
        <div class="bg-slate-50 p-4 rounded-2xl border border-slate-100">
          <h3 class="font-bold text-xs uppercase text-slate-400 mb-2">Thông tin người nhận</h3>
          <p class="font-semibold text-slate-800 text-sm">{{ detail.nguoiNhan?.tenNguoiNhan }}</p>
          <p class="text-xs text-slate-500 mt-0.5">SĐT: {{ detail.nguoiNhan?.soDienThoai }}</p>
          <p class="text-xs text-slate-600 mt-1">Địa chỉ: {{ detail.nguoiNhan?.diaChi }}</p>
        </div>

        <!-- Danh sách sản phẩm -->
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
              </div>
            </div>
          </div>
        </div>

        <!-- Tổng quan thanh toán -->
        <div class="space-y-2 pt-2 border-t border-slate-100 text-xs">
          <div class="flex justify-between text-slate-500">
            <span>Tạm tính</span>
            <span>{{ formatMoney(info?.tongTienHang) }}đ</span>
          </div>
          <div
            class="flex justify-between font-bold text-slate-900 text-sm pt-2 border-t border-slate-100"
          >
            <span>Tổng thanh toán</span>
            <span class="text-emerald-600">{{ formatMoney(info?.tongThanhToan) }}đ</span>
          </div>
        </div>
      </div>

      <!-- Footer Action -->
      <div class="pt-4 border-t border-slate-100 flex justify-end gap-2">
        <button
          @click="$emit('close')"
          class="px-5 py-2.5 bg-slate-100 text-slate-700 text-xs font-bold rounded-xl cursor-pointer"
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

// Gom gọn props (chỉ khai báo 1 lần duy nhất)
const props = defineProps({
  idHoaDon: {
    type: [Number, String],
    required: true,
  },
})

// Gom gọn emits (chỉ khai báo 1 lần duy nhất)
defineEmits(['close', 'reload'])

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
const formatDate = (val) => (val ? new Date(val).toLocaleString('vi-VN') : '')

onMounted(() => {
  fetchOrderDetail()
})
</script>
