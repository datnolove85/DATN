<template>
  <div class="container py-5 text-center">
    <div v-if="status === 'success'" class="card shadow p-4 mx-auto" style="max-width: 500px">
      <div class="card-body">
        <h2 class="text-success mb-3">🎉 Thanh toán thành công!</h2>
        <p class="text-muted">Cảm ơn bạn đã đặt hàng. Đơn hàng của bạn đã được xác nhận.</p>
        <div class="spinner-border text-success my-3" role="status">
          <span class="visually-hidden">Loading...</span>
        </div>
      </div>
    </div>

    <div v-else class="card shadow p-4 mx-auto" style="max-width: 500px">
      <div class="card-body">
        <h2 class="text-danger mb-3">❌ Thanh toán thất bại!</h2>
        <p class="text-muted">Giao dịch đã bị hủy hoặc xảy ra lỗi.</p>
        <router-link to="/giohang" class="btn btn-primary mt-3">Quay lại giỏ hàng</router-link>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()
const status = ref(route.query.status)

onMounted(() => {
  // Nếu thành công thì sau 2 giây tự động đá về trang quản lý đơn hàng
  if (status.value === 'success' || route.query.vnp_ResponseCode === '00') {
    setTimeout(() => {
      router.push('/donhang')
    }, 2000)
  }
})
</script>
