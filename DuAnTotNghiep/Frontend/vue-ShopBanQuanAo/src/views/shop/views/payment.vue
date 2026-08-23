<template>
  <div
    class="payment-page relative min-h-screen bg-[#F8FAFC] text-slate-800 font-sans selection:bg-indigo-100 selection:text-indigo-900 antialiased"
  >
    <!-- Ambient Dynamic Background Glowing -->
    <div class="pointer-events-none fixed inset-0 overflow-hidden z-0">
      <div
        class="absolute -left-32 -top-32 h-96 w-96 rounded-full bg-indigo-300/30 blur-[100px] animate-pulse"
      ></div>
      <div
        class="absolute right-0 top-1/4 h-[500px] w-[500px] rounded-full bg-sky-300/25 blur-[120px]"
      ></div>
      <div
        class="absolute bottom-10 left-1/3 h-80 w-80 rounded-full bg-violet-200/30 blur-[90px]"
      ></div>
    </div>

    <div class="relative z-10 mx-auto max-w-7xl px-4 py-6 sm:px-6 lg:px-8 lg:py-8">
      <!-- HERO BENTO BANNER (MINIMALIST CLEAN STYLE) -->
      <section
        class="relative overflow-hidden rounded-3xl bg-white p-4 sm:p-6 text-slate-900 shadow-sm border border-slate-200/80 backdrop-blur-xl"
      >
        <div class="relative grid gap-4 lg:grid-cols-[1fr_auto] lg:items-center">
          <div>
            <h1 class="text-xl font-bold tracking-tight text-slate-900 sm:text-2xl">
              Xác Nhận & Thanh Toán
            </h1>

            <p class="mt-1.5 max-w-xl text-xs leading-relaxed text-slate-600 sm:text-sm">
              Kiểm tra lại thông tin đơn hàng và hoàn tất thủ tục thanh toán an toàn bên dưới.
            </p>
          </div>

          <!-- Order Code Card -->
          <div
            class="flex flex-col rounded-2xl border border-slate-200 bg-slate-50/80 p-3.5 shadow-sm"
          >
            <p class="text-[10px] font-bold uppercase tracking-widest text-slate-500">
              Mã đơn hàng
            </p>

            <div class="mt-1 flex items-center justify-between gap-4">
              <span class="break-all font-mono text-sm font-bold text-slate-900 tracking-wider">
                {{ orderCode || '---' }}
              </span>

              <button
                v-if="orderCode"
                type="button"
                class="grid h-7 w-7 shrink-0 place-items-center rounded-xl border border-slate-200 bg-white text-slate-600 transition-all hover:bg-slate-100 hover:text-indigo-600 active:scale-95 shadow-sm"
                title="Sao chép mã đơn"
                @click="copyOrderCode"
              >
                <Check v-if="copied" :size="14" class="text-emerald-600" />
                <Copy v-else :size="14" />
              </button>
            </div>

            <div
              class="mt-2.5 flex items-center gap-2 text-[11px] font-bold"
              :class="orderCancelled ? 'text-rose-600' : 'text-emerald-600'"
            >
              <span class="relative flex h-2 w-2">
                <span
                  v-if="!orderCancelled"
                  class="absolute inline-flex h-full w-full animate-ping rounded-full bg-emerald-400 opacity-75"
                ></span>
                <span
                  class="relative inline-flex h-2 w-2 rounded-full"
                  :class="orderCancelled ? 'bg-rose-500' : 'bg-emerald-500'"
                ></span>
              </span>
              {{ orderCancelled ? 'Đơn hàng đã bị hủy' : 'Đã sẵn sàng thanh toán' }}
            </div>
          </div>
        </div>
      </section>

      <!-- STEPPER PROGRESS BAR (ĐÃ GIẢM KHOẢNG TRẮNG) -->
      <div
        class="mx-auto mt-3 grid max-w-xl grid-cols-3 rounded-2xl border border-slate-200/80 bg-white/80 p-1.5 shadow-sm backdrop-blur-md"
      >
        <div
          class="flex items-center justify-center gap-2 rounded-xl px-2 py-2 text-xs font-bold text-emerald-600 sm:text-sm"
        >
          <span
            class="grid h-5 w-5 place-items-center rounded-full bg-emerald-100 text-emerald-700"
          >
            <Check :size="12" />
          </span>
          <span class="hidden sm:inline">Thông tin</span>
        </div>

        <div
          class="flex items-center justify-center gap-2 rounded-xl bg-indigo-600 px-2 py-2 text-xs font-bold text-white shadow-md shadow-indigo-500/20 sm:text-sm"
        >
          <span class="grid h-5 w-5 place-items-center rounded-full bg-white/20 text-white text-xs"
            >2</span
          >
          Thanh toán
        </div>

        <div
          class="flex items-center justify-center gap-2 rounded-xl px-2 py-2 text-xs font-semibold text-slate-500 sm:text-sm"
        >
          <span
            class="grid h-5 w-5 place-items-center rounded-full bg-slate-100 text-slate-500 text-xs"
            >3</span
          >
          <span class="hidden sm:inline">Hoàn tất</span>
        </div>
      </div>

      <!-- LOADING STATE (ĐÃ GIẢM MARGIN) -->
      <div v-if="loading" class="mt-4 grid gap-8 lg:grid-cols-[1fr_380px]">
        <div class="space-y-6">
          <div class="h-64 animate-pulse rounded-3xl bg-slate-200/60"></div>
          <div class="h-96 animate-pulse rounded-3xl bg-slate-200/60"></div>
        </div>
        <div class="h-[480px] animate-pulse rounded-3xl bg-slate-200/60"></div>
      </div>

      <!-- ERROR STATE -->
      <div
        v-else-if="loadError"
        class="mx-auto mt-4 max-w-xl rounded-3xl border border-rose-100 bg-white p-8 text-center shadow-xl shadow-slate-900/5"
      >
        <div class="mx-auto grid h-16 w-16 place-items-center rounded-2xl bg-rose-50 text-rose-600">
          <X :size="30" />
        </div>
        <h2 class="mt-5 text-xl font-bold text-slate-900">Không thể tải đơn hàng</h2>
        <p class="mt-2 text-slate-600 text-sm">{{ loadError }}</p>
        <button
          type="button"
          class="mt-6 inline-flex items-center gap-2 rounded-2xl bg-indigo-600 px-7 py-3 text-sm font-bold text-white shadow-lg shadow-indigo-500/20 transition hover:bg-indigo-700 active:scale-95"
          @click="router.push('/san-pham')"
        >
          Quay lại cửa hàng
        </button>
      </div>

      <!-- MAIN CONTENT AREA (ĐÃ GIẢM MARGIN) -->
      <div v-else-if="orderInfo" class="mt-4 grid items-start gap-8 lg:grid-cols-[1fr_380px]">
        <!-- LEFT COLUMN: PAYMENT & ITEMS -->
        <div class="space-y-8">
          <!-- PAYMENT METHOD SELECTOR -->
          <section
            class="rounded-3xl border border-slate-200/80 bg-white/90 p-6 sm:p-8 shadow-xl shadow-slate-200/50 backdrop-blur-xl"
          >
            <div class="flex items-start justify-between gap-4">
              <div>
                <h2 class="mt-1 text-xl font-bold tracking-tight text-slate-900 sm:text-2xl">
                  Phương thức thanh toán
                </h2>
                <p class="mt-1 text-sm text-slate-600">
                  Lựa chọn hình thức thanh toán thuận tiện và an toàn nhất cho bạn.
                </p>
              </div>
              <div
                class="hidden h-12 w-12 place-items-center rounded-2xl bg-indigo-50 text-indigo-600 sm:grid shadow-sm"
              >
                <CreditCard :size="22" />
              </div>
            </div>

            <div class="mt-6 grid gap-4 sm:grid-cols-2">
              <!-- COD Card -->
              <div
                class="group relative cursor-pointer overflow-hidden rounded-2xl border-2 p-5 transition-all duration-300"
                :class="[
                  paymentMethod === 'COD'
                    ? 'border-indigo-600 bg-indigo-50/40 text-slate-900 shadow-lg shadow-indigo-500/10 ring-2 ring-indigo-600/20'
                    : 'border-slate-200/80 bg-white text-slate-900 hover:border-slate-300 hover:bg-slate-50/50',
                  orderCancelled ? 'pointer-events-none opacity-50' : '',
                ]"
                @click="paymentMethod = 'COD'"
              >
                <div class="flex items-start gap-4">
                  <div
                    class="grid h-12 w-12 shrink-0 place-items-center rounded-xl transition-colors"
                    :class="
                      paymentMethod === 'COD'
                        ? 'bg-indigo-600 text-white shadow-md shadow-indigo-500/30'
                        : 'bg-slate-100 text-slate-600'
                    "
                  >
                    <Truck :size="22" />
                  </div>
                  <div class="flex-1">
                    <div class="flex items-center justify-between">
                      <span class="font-bold text-sm sm:text-base">Thanh toán khi nhận (COD)</span>
                      <span
                        class="h-4 w-4 rounded-full border-2 flex items-center justify-center"
                        :class="
                          paymentMethod === 'COD'
                            ? 'border-indigo-600 bg-indigo-600'
                            : 'border-slate-300'
                        "
                      >
                        <span
                          v-if="paymentMethod === 'COD'"
                          class="h-1.5 w-1.5 rounded-full bg-white"
                        ></span>
                      </span>
                    </div>
                    <p
                      class="mt-1 text-xs leading-relaxed"
                      :class="paymentMethod === 'COD' ? 'text-slate-700' : 'text-slate-600'"
                    >
                      Thanh toán bằng tiền mặt khi giao hàng tận nơi.
                    </p>
                  </div>
                </div>
              </div>

              <!-- VNPAY Card -->
              <div
                class="group relative cursor-pointer overflow-hidden rounded-2xl border-2 p-5 transition-all duration-300"
                :class="[
                  paymentMethod === 'VNPAY'
                    ? 'border-indigo-600 bg-indigo-50/40 text-slate-900 shadow-lg shadow-indigo-500/10 ring-2 ring-indigo-600/20'
                    : 'border-slate-200/80 bg-white text-slate-900 hover:border-slate-300 hover:bg-slate-50/50',
                  orderCancelled ? 'pointer-events-none opacity-50' : '',
                ]"
                @click="paymentMethod = 'VNPAY'"
              >
                <div class="flex items-start gap-4">
                  <div
                    class="grid h-12 w-12 shrink-0 place-items-center rounded-xl transition-colors"
                    :class="
                      paymentMethod === 'VNPAY'
                        ? 'bg-indigo-600 text-white shadow-md shadow-indigo-500/30'
                        : 'bg-indigo-50 text-indigo-600'
                    "
                  >
                    <CreditCard :size="22" />
                  </div>
                  <div class="flex-1">
                    <div class="flex items-center justify-between">
                      <span class="font-bold text-sm sm:text-base">Cổng VNPAY QR</span>
                      <span
                        class="h-4 w-4 rounded-full border-2 flex items-center justify-center"
                        :class="
                          paymentMethod === 'VNPAY'
                            ? 'border-indigo-600 bg-indigo-600'
                            : 'border-slate-300'
                        "
                      >
                        <span
                          v-if="paymentMethod === 'VNPAY'"
                          class="h-1.5 w-1.5 rounded-full bg-white"
                        ></span>
                      </span>
                    </div>
                    <p class="mt-1 text-xs leading-relaxed text-slate-600">
                      Quét mã QR qua ứng dụng ngân hàng hoặc thẻ quốc tế.
                    </p>
                  </div>
                </div>
              </div>
            </div>
          </section>

          <!-- ORDERED PRODUCTS LIST -->
          <section
            class="rounded-3xl border border-slate-200/80 bg-white/90 p-6 sm:p-8 shadow-xl shadow-slate-200/50 backdrop-blur-xl"
          >
            <div class="flex items-center justify-between gap-4">
              <div>
                <span class="text-[11px] font-bold uppercase tracking-[0.2em] text-slate-500"
                  >Sản phẩm</span
                >
                <h2 class="mt-1 text-xl font-bold tracking-tight text-slate-900 sm:text-2xl">
                  Chi tiết đơn hàng
                </h2>
              </div>
              <span
                class="rounded-full bg-indigo-50 px-3.5 py-1.5 text-xs font-bold text-indigo-600"
              >
                {{ orderItems.length }} sản phẩm
              </span>
            </div>

            <div class="mt-6 space-y-4">
              <article
                v-for="item in orderItems"
                :key="item.id"
                class="group relative grid gap-4 rounded-2xl border border-slate-200/60 bg-white p-4 transition-all duration-300 hover:border-indigo-200 hover:shadow-lg sm:grid-cols-[110px_1fr] sm:p-5"
              >
                <!-- Image Container -->
                <div
                  class="h-24 w-24 overflow-hidden rounded-xl border border-slate-100 bg-slate-50 shadow-inner group-hover:scale-105 transition-transform duration-300"
                >
                  <img
                    :src="
                      item.anh
                        ? item.anh.startsWith('http')
                          ? item.anh
                          : 'http://localhost:8080' + item.anh
                        : '/no-image.png'
                    "
                    :alt="item.tenSanPham || 'Sản phẩm'"
                    class="h-full w-full object-cover object-center"
                  />
                </div>

                <!-- Product Info -->
                <div class="flex flex-col justify-between">
                  <div>
                    <div class="flex items-start justify-between gap-2">
                      <h3
                        class="line-clamp-2 text-sm font-bold text-slate-900 group-hover:text-indigo-600 transition-colors"
                      >
                        {{ item.tenSanPham }}
                      </h3>
                      <span
                        v-if="item.maSanPhamChiTiet"
                        class="font-mono text-[11px] text-slate-600 bg-slate-100 px-2 py-0.5 rounded-md border border-slate-200 font-semibold"
                      >
                        #{{ item.maSanPhamChiTiet }}
                      </span>
                    </div>

                    <!-- Badges -->
                    <div class="mt-2 flex flex-wrap gap-1.5">
                      <span
                        v-if="item.tenMauSac"
                        class="inline-flex items-center gap-1 rounded-lg bg-slate-100 px-2.5 py-1 text-xs font-semibold text-slate-700"
                      >
                        🎨 {{ item.tenMauSac }}
                      </span>

                      <span
                        v-if="item.tenKichThuoc"
                        class="inline-flex items-center gap-1 rounded-lg bg-slate-100 px-2.5 py-1 text-xs font-semibold text-slate-700"
                      >
                        📏 {{ item.tenKichThuoc }}
                      </span>
                    </div>
                  </div>

                  <!-- Price Calculation -->
                  <div
                    class="mt-3 flex items-center justify-between border-t border-slate-100 pt-3"
                  >
                    <div class="flex items-center gap-2 text-xs text-slate-600 font-semibold">
                      <span
                        >Đơn giá:
                        <strong class="text-slate-900">{{
                          formatMoney(item.donGia || item.thanhTien / item.soLuong)
                        }}</strong></span
                      >
                      <span>•</span>
                      <span
                        >SL: <strong class="text-slate-900">x{{ item.soLuong }}</strong></span
                      >
                    </div>

                    <div class="text-right font-bold text-base text-indigo-600">
                      {{ formatMoney(item.thanhTien) }}
                    </div>
                  </div>
                </div>
              </article>
            </div>
          </section>
        </div>

        <!-- RIGHT COLUMN: STICKY SUMMARY SIDEBAR -->
        <aside class="space-y-6 lg:sticky lg:top-8">
          <section
            class="overflow-hidden rounded-3xl border border-slate-200/80 bg-white shadow-xl shadow-slate-200/50 backdrop-blur-xl"
          >
            <!-- Header -->
            <div class="border-b border-slate-100 bg-slate-50/50 px-6 py-5">
              <div class="flex items-center gap-3">
                <span
                  class="grid h-10 w-10 place-items-center rounded-2xl bg-indigo-600 text-white shadow-md shadow-indigo-500/20"
                >
                  <ReceiptText :size="20" />
                </span>
                <div>
                  <h2 class="text-base font-bold text-slate-900">Tóm tắt thanh toán</h2>
                  <p class="text-xs text-slate-500 font-mono font-semibold">Mã: {{ orderCode }}</p>
                </div>
              </div>
            </div>

            <!-- Price breakdown -->
            <div class="space-y-3.5 px-6 py-6 text-sm">
              <div class="flex items-center justify-between text-slate-700 font-medium">
                <span>Tiền hàng</span>
                <span class="font-bold text-slate-900">{{
                  formatMoney(orderInfo.tongTienHang)
                }}</span>
              </div>

              <div class="flex items-center justify-between text-slate-700 font-medium">
                <span>Giảm giá</span>
                <span class="font-bold text-emerald-600"
                  >-{{ formatMoney(orderInfo.tongGiamGia) }}</span
                >
              </div>

              <div class="flex items-center justify-between text-slate-700 font-medium">
                <span>Phí vận chuyển</span>
                <span class="font-bold text-slate-900">{{
                  formatMoney(orderInfo.phiVanChuyen)
                }}</span>
              </div>

              <div class="border-t border-dashed border-slate-200 pt-4">
                <div class="flex items-baseline justify-between">
                  <span class="font-bold text-slate-900 text-base">Tổng thanh toán</span>
                  <span class="text-2xl font-bold text-indigo-600">
                    {{ formatMoney(orderInfo.tongThanhToan) }}
                  </span>
                </div>
              </div>
            </div>

            <!-- Action Buttons -->
            <div class="border-t border-slate-100 bg-slate-50/50 p-6 space-y-3">
              <button
                type="button"
                :disabled="payDisabled"
                class="relative w-full overflow-hidden rounded-2xl bg-indigo-600 py-4 px-6 text-center text-sm font-bold text-white shadow-lg shadow-indigo-500/25 transition-all duration-300 hover:bg-indigo-700 active:scale-[0.98] disabled:cursor-not-allowed disabled:opacity-50"
                @click="pay"
              >
                <div class="flex items-center justify-center gap-2">
                  <LoaderCircle v-if="isPaying" :size="18" class="animate-spin" />
                  <PackageCheck
                    v-else-if="paymentMethod === 'COD'"
                    :size="18"
                    class="text-indigo-200"
                  />
                  <CreditCard v-else :size="18" class="text-indigo-200" />
                  <span>{{ payButtonLabel }}</span>
                </div>
              </button>

              <button
                type="button"
                :disabled="isPaying || isCancelling || orderCancelled"
                class="w-full rounded-2xl border border-slate-200 bg-white py-3.5 px-5 text-sm font-semibold text-slate-700 transition-all duration-300 hover:border-rose-200 hover:bg-rose-50 hover:text-rose-600 active:scale-[0.98] disabled:cursor-not-allowed disabled:opacity-50"
                @click="handleCancel"
              >
                <div class="flex items-center justify-center gap-2">
                  <LoaderCircle v-if="isCancelling" :size="16" class="animate-spin" />
                  <X v-else :size="16" />
                  <span>{{ isCancelling ? 'Đang hủy đơn...' : 'Hủy đơn hàng' }}</span>
                </div>
              </button>
            </div>
          </section>

          <!-- Security Badge Card -->
          <div class="rounded-2xl border border-indigo-100 bg-indigo-50/60 p-4 backdrop-blur-md">
            <div class="flex gap-3">
              <span
                class="grid h-9 w-9 shrink-0 place-items-center rounded-xl bg-indigo-600 text-white shadow-sm"
              >
                <ShieldCheck :size="18" />
              </span>
              <div>
                <h4 class="font-bold text-xs text-indigo-950">Bảo mật giao dịch tuyệt đối</h4>
                <p class="mt-0.5 text-[11px] leading-relaxed text-indigo-900 font-medium">
                  An tâm mua sắm với công nghệ mã hóa hiện đại.
                </p>
              </div>
            </div>
          </div>
        </aside>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import {
  Check,
  Copy,
  CreditCard,
  LoaderCircle,
  Lock,
  PackageCheck,
  ReceiptText,
  ShieldCheck,
  Truck,
  X,
} from 'lucide-vue-next'
import { huyHoaDonOnline } from '@/service/HoaDonService'
import { useToast } from 'vue-toastification'
import { thanhToan } from '@/service/PaymentService'

const toast = useToast()
const route = useRoute()
const router = useRouter()

const orderId = route.query.id

const loading = ref(true)
const loadError = ref('')
const isPaying = ref(false)
const isCancelling = ref(false)
const copied = ref(false)

const orderItems = ref([])
const orderInfo = ref(null)
const paymentMethod = ref('COD')

const orderCode = computed(() => orderInfo.value?.maHoaDon || route.query.maHoaDon || '')
const orderCancelled = computed(() => orderInfo.value?.trangThai === 'da_huy')

const payButtonLabel = computed(() => {
  if (isPaying.value) return 'Đang xử lý...'
  if (orderCancelled.value) return 'Đơn hàng đã bị hủy'

  switch (paymentMethod.value) {
    case 'COD':
      return 'Xác nhận đặt hàng'
    case 'VNPAY':
      return 'Thanh toán qua VNPay'
    default:
      return 'Chọn phương thức thanh toán!'
  }
})

const payDisabled = computed(() => {
  if (isPaying.value || isCancelling.value || orderCancelled.value) return true
  return false
})

const formatMoney = (money) => Number(money || 0).toLocaleString('vi-VN') + ' đ'

const copyOrderCode = async () => {
  if (!orderCode.value) return

  try {
    await navigator.clipboard.writeText(orderCode.value)
    copied.value = true
    toast.success('Đã sao chép mã đơn hàng')

    window.setTimeout(() => {
      copied.value = false
    }, 1600)
  } catch (error) {
    console.error(error)
    toast.error('Không thể sao chép mã đơn hàng')
  }
}

const loadOrder = async () => {
  if (!orderId) {
    throw new Error('Thiếu mã định danh đơn hàng')
  }

  const res = await fetch(`http://localhost:8080/hoadon/${orderId}`)

  if (!res.ok) {
    throw new Error('Máy chủ không trả về thông tin đơn hàng')
  }

  const data = await res.json()

  if (!data?.id) {
    throw new Error('Đơn hàng không có dữ liệu hợp lệ')
  }

  orderInfo.value = data
  orderItems.value = Array.isArray(data.sanPhams) ? data.sanPhams : []

  if (data.trangThai === 'da_huy') {
    return
  }
}

onMounted(async () => {
  try {
    await loadOrder()
  } catch (error) {
    console.error(error)
    loadError.value = error.message || 'Không tải được thông tin đơn hàng'
    toast.error(loadError.value)
  } finally {
    loading.value = false
  }
})

const pay = async () => {
  if (payDisabled.value) return

  try {
    isPaying.value = true

    const body = {
      idHoaDon: Number(orderId),
      method: paymentMethod.value,
      orderInfo: `Thanh toan don hang ${orderCode.value}`,
    }

    const res = await thanhToan(body)

    if (res && res.paymentUrl && paymentMethod.value === 'VNPAY') {
      sessionStorage.removeItem('checkoutData')
      toast.success('Đang chuyển hướng đến cổng thanh toán VNPay...')
      window.location.href = res.paymentUrl
      return
    }

    sessionStorage.removeItem('checkoutData')
    toast.success(res.message || 'Đã ghi nhận phương thức thanh toán thành công!')
    router.push('/san-pham')
  } catch (error) {
    console.error(error)
    toast.error(error.message || 'Thanh toán thất bại')
  } finally {
    isPaying.value = false
  }
}

const handleCancel = async () => {
  if (isPaying.value || isCancelling.value || orderCancelled.value) return

  try {
    isCancelling.value = true

    await huyHoaDonOnline(Number(orderId))

    if (orderInfo.value) {
      orderInfo.value.trangThai = 'da_huy'
    }

    toast.success('Đã hủy đơn hàng')
    router.push('/san-pham')
  } catch (error) {
    console.error(error)
    toast.error(error.message || 'Hủy đơn thất bại')
  } finally {
    isCancelling.value = false
  }
}
</script>

<style scoped>
.payment-page {
  background-image:
    linear-gradient(rgba(248, 250, 252, 0.85), rgba(248, 250, 252, 0.95)),
    radial-gradient(circle at 1px 1px, rgba(99, 102, 241, 0.1) 1px, transparent 0);
  background-size:
    auto,
    24px 24px;
}
</style>
