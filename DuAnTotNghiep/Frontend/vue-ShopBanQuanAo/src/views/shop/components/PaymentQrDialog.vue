<template>
  <el-dialog
    :model-value="visible"
    @update:model-value="handleVisibleChange"
    width="480px"
    :close-on-click-modal="false"
    align-center
    class="payment-dialog rounded-2xl overflow-hidden"
  >
    <!-- Header tùy chỉnh -->
    <template #header>
      <div class="flex items-center gap-3">
        <div class="w-10 h-10 rounded-xl bg-blue-50 flex items-center justify-center text-blue-600">
          <i class="el-icon-eleme text-xl">💳</i>
        </div>
        <div>
          <h3 class="text-lg font-bold text-gray-800 m-0">Thanh toán chuyển khoản</h3>
          <p class="text-xs text-gray-400 m-0">Quét mã QR để hoàn tất giao dịch</p>
        </div>
      </div>
    </template>

    <div class="py-2">
      <!-- Bộ đếm thời gian -->
      <div
        class="flex items-center justify-between bg-amber-50 border border-amber-200/60 rounded-xl px-4 py-3 mb-6"
      >
        <div class="flex items-center gap-2 text-amber-800 text-sm font-medium">
          <span class="relative flex h-2.5 w-2.5">
            <span
              class="animate-ping absolute inline-flex h-full w-full rounded-full bg-amber-400 opacity-75"
            ></span>
            <span class="relative inline-flex rounded-full h-2.5 w-2.5 bg-amber-500"></span>
          </span>
          Thời gian giữ mã QR:
        </div>
        <div
          class="text-base font-bold font-mono"
          :class="timeLeft <= 30 ? 'text-red-600 animate-pulse' : 'text-amber-700'"
        >
          {{ formatTime(timeLeft) }}
        </div>
      </div>

      <!-- Khung chứa mã QR -->
      <div
        class="relative bg-white border border-gray-100 shadow-sm rounded-2xl p-4 text-center mb-6"
      >
        <div class="relative inline-block">
          <img :src="qrUrl" class="w-60 h-60 mx-auto rounded-xl object-contain" alt="QR Code" />

          <!-- Lớp phủ khi hết giờ -->
          <div
            v-if="timeLeft === 0"
            class="absolute inset-0 bg-white/90 backdrop-blur-xs rounded-xl flex flex-col items-center justify-center p-4"
          >
            <span class="text-red-500 font-bold text-base mb-1">Mã QR đã hết hạn</span>
            <span class="text-gray-500 text-xs text-center"
              >Vui lòng tải lại mã mới để tiếp tục thanh toán.</span
            >
          </div>
        </div>

        <div class="text-xs text-gray-400 mt-2">
          Sử dụng ứng dụng Ngân hàng hoặc Ví điện tử để quét mã
        </div>
      </div>

      <!-- Thông tin thanh toán -->
      <div class="bg-gray-50/70 border border-gray-100 rounded-2xl p-4 space-y-3 mb-6">
        <div class="flex items-center justify-between text-sm">
          <span class="text-gray-500">Mã hóa đơn</span>
          <div class="flex items-center gap-2">
            <span class="font-semibold text-gray-800 font-mono">{{ maHoaDon }}</span>
            <!-- Nút copy mã nếu cần -->
            <button
              @click="copyText(maHoaDon)"
              class="text-blue-600 hover:text-blue-700 text-xs cursor-pointer"
            >
              Sao chép
            </button>
          </div>
        </div>

        <div class="border-t border-gray-200/60 pt-3 flex items-center justify-between">
          <span class="text-gray-500 text-sm">Số tiền cần thanh toán</span>
          <span class="text-2xl text-red-600 font-bold tracking-tight">
            {{ formatMoney(tongTien) }}
          </span>
        </div>
      </div>

      <!-- Nút hành động -->
      <div class="flex gap-3">
        <el-button
          size="large"
          class="flex-1 rounded-xl !border-gray-200 !text-gray-600 hover:!bg-gray-50"
          @click="handleClose"
        >
          Hủy bỏ
        </el-button>
        <el-button
          type="primary"
          size="large"
          class="flex-1 rounded-xl !bg-blue-600 hover:!bg-blue-700 font-medium shadow-sm shadow-blue-500/20"
          :disabled="timeLeft === 0"
          @click="emit('paid')"
        >
          Tôi đã chuyển khoản
        </el-button>
      </div>
    </div>
  </el-dialog>
</template>

<script setup>
import { ref, watch, onUnmounted } from 'vue'
import { ElMessage } from 'element-plus'

const props = defineProps({
  visible: {
    type: Boolean,
    default: false,
  },
  qrUrl: {
    type: String,
    default: '',
  },
  maHoaDon: {
    type: String,
    default: '',
  },
  tongTien: {
    type: Number,
    default: 0,
  },
})

const emit = defineEmits(['update:visible', 'paid', 'expired'])

// Bộ đếm thời gian (3 phút = 180 giây)
const TIME_LIMIT = 180
const timeLeft = ref(TIME_LIMIT)
let timer = null

// Bắt đầu đếm ngược
const startTimer = () => {
  clearInterval(timer)
  timeLeft.value = TIME_LIMIT
  timer = setInterval(() => {
    if (timeLeft.value > 0) {
      timeLeft.value--
    } else {
      clearInterval(timer)
      emit('expired')
    }
  }, 1000)
}

// Theo dõi trạng thái mở/đóng modal để chạy/dừng timer
watch(
  () => props.visible,
  (newVal) => {
    if (newVal) {
      startTimer()
    } else {
      clearInterval(timer)
    }
  },
)

onUnmounted(() => {
  clearInterval(timer)
})

// Định dạng thời gian mm:ss
const formatTime = (seconds) => {
  const mins = Math.floor(seconds / 60)
  const secs = seconds % 60
  return `${mins.toString().padStart(2, '0')}:${secs.toString().padStart(2, '0')}`
}

const formatMoney = (value) => {
  return Number(value || 0).toLocaleString('vi-VN') + ' đ'
}

const copyText = (text) => {
  navigator.clipboard.writeText(text)
  ElMessage.success('Đã sao chép mã hóa đơn')
}

const handleVisibleChange = (val) => {
  emit('update:visible', val)
}

const handleClose = () => {
  clearInterval(timer)
  emit('update:visible', false)
}
</script>

<style scoped>
:deep(.el-dialog__header) {
  margin-right: 0;
  border-bottom: 1px solid #f3f4f6;
  padding-bottom: 16px;
}
:deep(.el-dialog__body) {
  padding-top: 20px;
  padding-bottom: 20px;
}
</style>
