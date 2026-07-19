<template>
  <div class="max-w-[1400px] mx-auto px-4 md:px-8 py-12">
    <h2 class="text-3xl font-black text-slate-800 uppercase mb-8">Ưu đãi nổi bật</h2>
    
    <div v-if="loading" class="text-center text-slate-500 py-10">Đang tải voucher...</div>
    <div v-else-if="vouchers.length === 0" class="text-center text-slate-500 py-10">Hiện chưa có voucher nào.</div>
    <div v-else class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
      
      <!-- Voucher Cards -->
      <div 
        v-for="v in vouchers" 
        :key="v.id"
        class="border border-slate-300 rounded-sm bg-slate-50 p-4 flex flex-col justify-between hover:shadow-md transition-shadow"
      >
        <div>
          <h3 class="text-xl font-bold text-slate-800">{{ v.tenVoucher }}</h3>
          <p class="text-sm text-slate-600 mt-1">
            Giảm {{ formatDiscount(v.loaiGiamGia, v.giaTriGiam) }} cho đơn từ {{ formatPrice(v.giaTriDonHangToiThieu) }}đ
          </p>
        </div>
        
        <div class="flex justify-between items-end mt-6">
          <div class="text-xs text-slate-600 font-medium space-y-1">
            <div>HSD: {{ formatDate(v.ngayKetThuc) }}</div>
            <a href="#" class="font-bold underline hover:text-slate-800">Điều kiện</a>
          </div>
          <button class="bg-[#2f404d] hover:bg-slate-800 text-white font-bold text-sm px-5 py-2 rounded-sm transition">
            Dùng mã
          </button>
        </div>
      </div>

    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getAllVoucher } from '@/service/VoucherService'

const vouchers = ref([])
const loading = ref(true)

const loadVouchers = async () => {
  loading.value = true
  try {
    const data = await getAllVoucher()
    // Lấy tối đa 4 voucher đang hoạt động
    vouchers.value = data.filter(v => v.trangThai === 1).slice(0, 4)
  } catch (error) {
    console.error("Lỗi lấy voucher:", error)
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadVouchers()
})

const formatPrice = (value) => {
  if (!value) return '0'
  return new Intl.NumberFormat('vi-VN').format(value)
}

const formatDiscount = (loai, giaTri) => {
  if (!giaTri) return '0đ'
  // Nếu loại giảm giá là % hoặc giá trị < 100 thì ngầm định là %
  if (loai === '%' || loai?.toLowerCase() === 'phần trăm' || giaTri <= 100) {
    return giaTri + '%'
  }
  return formatPrice(giaTri) + 'đ'
}

const formatDate = (dateString) => {
  if (!dateString) return 'Vô thời hạn'
  const date = new Date(dateString)
  if (isNaN(date)) return dateString
  return date.toISOString().split('T')[0] // Trả về dạng YYYY-MM-DD
}
</script>
