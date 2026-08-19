<template>
  <div class="pb-16 pt-10">
    <div class="app-container">
      <div
        class="mx-auto max-w-2xl overflow-hidden rounded-[36px] border border-white/70 bg-white shadow-[0_28px_100px_rgba(15,23,42,0.12)]"
      >
        <div
          :class="
            isSuccess
              ? 'from-emerald-500 via-teal-500 to-cyan-500'
              : 'from-rose-500 via-red-500 to-orange-500'
          "
          class="bg-gradient-to-r px-6 py-10 text-center text-white sm:px-10"
        >
          <div
            class="mx-auto grid h-24 w-24 place-items-center rounded-[30px] bg-white/15 text-5xl backdrop-blur"
          >
            {{ isSuccess ? '✓' : '!' }}
          </div>
          <p class="mt-6 text-xs font-black uppercase tracking-[0.24em] text-white/75">
            Kết quả thanh toán
          </p>
          <h1 class="mt-3 text-3xl font-black text-white sm:text-4xl">
            {{ isSuccess ? 'Thanh toán thành công' : 'Thanh toán chưa hoàn tất' }}
          </h1>
          <p class="mx-auto mt-4 max-w-lg text-sm leading-7 text-white/80">
            {{
              isSuccess
                ? 'Cảm ơn bạn đã đặt hàng. Hệ thống đang chuyển bạn tới trang theo dõi đơn hàng.'
                : 'Giao dịch đã bị hủy hoặc xảy ra lỗi. Bạn có thể quay lại giỏ hàng để thử lại.'
            }}
          </p>
        </div>

        <div class="p-6 sm:p-10">
          <div class="grid gap-4 sm:grid-cols-3">
            <div class="rounded-3xl border border-slate-100 bg-slate-50 p-5 text-center">
              <p class="text-xs font-black uppercase tracking-[0.16em] text-slate-400">
                Trạng thái
              </p>
              <p :class="isSuccess ? 'text-emerald-600' : 'text-rose-600'" class="mt-2 font-black">
                {{ isSuccess ? 'Đã xác nhận' : 'Chưa thành công' }}
              </p>
            </div>
            <div class="rounded-3xl border border-slate-100 bg-slate-50 p-5 text-center">
              <p class="text-xs font-black uppercase tracking-[0.16em] text-slate-400">Bảo mật</p>
              <p class="mt-2 font-black text-slate-950">Kết nối an toàn</p>
            </div>
            <div class="rounded-3xl border border-slate-100 bg-slate-50 p-5 text-center">
              <p class="text-xs font-black uppercase tracking-[0.16em] text-slate-400">Hỗ trợ</p>
              <p class="mt-2 font-black text-slate-950">Luôn sẵn sàng</p>
            </div>
          </div>

          <div class="mt-8 flex flex-col gap-3 sm:flex-row sm:justify-center">
            <RouterLink v-if="isSuccess" to="/donhang" class="btn-primary min-w-[200px]"
              >Xem đơn hàng</RouterLink
            >
            <RouterLink v-else to="/don-hang" class="btn-primary min-w-[200px]"
              >Quay lại đơn hàng</RouterLink
            >
            <RouterLink to="/san-pham" class="btn-secondary min-w-[200px]"
              >Tiếp tục mua sắm</RouterLink
            >
          </div>

          <p v-if="isSuccess" class="mt-6 text-center text-sm text-slate-500">
            Tự động chuyển đến trang đơn hàng sau vài giây...
          </p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { RouterLink, useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()
const status = ref(route.query.status)

const isSuccess = computed(
  () => status.value === 'success' || route.query.vnp_ResponseCode === '00',
)

onMounted(() => {
  if (isSuccess.value) {
    setTimeout(() => {
      router.push('/donhang')
    }, 2500)
  }
})
</script>
