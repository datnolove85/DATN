<template>
  <div class="payment-page min-h-screen bg-[#f7f4ee] text-slate-900">
    <div class="pointer-events-none fixed inset-0 overflow-hidden">
      <div class="absolute -left-24 top-20 h-80 w-80 rounded-full bg-amber-200/40 blur-3xl"></div>
      <div class="absolute -right-24 top-0 h-96 w-96 rounded-full bg-amber-200/30 blur-3xl"></div>
      <div class="absolute bottom-0 left-1/3 h-72 w-72 rounded-full bg-rose-100/30 blur-3xl"></div>
    </div>

    <div class="relative mx-auto max-w-7xl px-4 py-6 sm:px-6 lg:px-8 lg:py-10">
      <!-- TOP BAR -->
      <div class="mb-6 flex flex-wrap items-center justify-between gap-4">
        <button
          type="button"
          class="group inline-flex items-center gap-2 text-sm font-semibold text-slate-600 transition hover:text-rose-900"
          @click="router.push('/san-pham')"
        >
          <span
            class="grid h-9 w-9 place-items-center rounded-full border border-slate-200 bg-white shadow-sm transition group-hover:border-amber-200 group-hover:bg-amber-50"
          >
            <ArrowLeft :size="17" />
          </span>
          Tiếp tục mua sắm
        </button>

        <RouterLink
          to="/tra-cuu-don-hang"
          class="inline-flex items-center gap-2 rounded-full border border-slate-200 bg-white px-4 py-2 text-sm font-semibold text-slate-700 shadow-sm transition hover:border-amber-200 hover:text-rose-900"
        >
          <Search :size="16" />
          Tra cứu đơn hàng
        </RouterLink>
      </div>

      <!-- HERO -->
      <section
        class="relative overflow-hidden rounded-[28px] border border-white/70 bg-[#201914] px-6 py-7 text-white shadow-2xl shadow-slate-300/60 sm:px-8 lg:px-10 lg:py-9"
      >
        <div
          class="absolute -right-20 -top-24 h-72 w-72 rounded-full bg-amber-500/30 blur-2xl"
        ></div>
        <div
          class="absolute -bottom-28 left-1/3 h-64 w-64 rounded-full bg-amber-400/20 blur-2xl"
        ></div>

        <div class="relative grid gap-7 lg:grid-cols-[1fr_auto] lg:items-center">
          <div>
            <div
              class="mb-4 inline-flex items-center gap-2 rounded-full border border-white/15 bg-white/10 px-3 py-1.5 text-xs font-bold uppercase tracking-[0.18em] text-amber-100"
            >
              <Lock :size="14" />
              Thanh toán an toàn
            </div>

            <h1 class="max-w-2xl text-3xl font-black tracking-tight sm:text-4xl lg:text-5xl">
              Hoàn tất đơn hàng của bạn
            </h1>

            <p class="mt-3 max-w-xl text-sm leading-6 text-slate-300 sm:text-base">
              Kiểm tra lại đơn hàng, chọn phương thức phù hợp và xác nhận thanh toán.
            </p>
          </div>

          <div
            class="min-w-[250px] rounded-2xl border border-white/15 bg-white/10 p-4 backdrop-blur"
          >
            <p class="text-xs font-bold uppercase tracking-[0.16em] text-slate-300">Mã đơn hàng</p>

            <div class="mt-2 flex items-center justify-between gap-3">
              <span class="break-all text-lg font-black text-white sm:text-xl">
                {{ orderCode || 'Đang cập nhật' }}
              </span>

              <button
                v-if="orderCode"
                type="button"
                class="grid h-10 w-10 shrink-0 place-items-center rounded-xl border border-white/15 bg-white/10 transition hover:bg-white/20"
                title="Sao chép mã đơn"
                @click="copyOrderCode"
              >
                <Check v-if="copied" :size="18" />
                <Copy v-else :size="18" />
              </button>
            </div>

            <div
              class="mt-3 flex items-center gap-2 text-xs"
              :class="orderCancelled ? 'text-red-300' : 'text-emerald-300'"
            >
              <span
                class="h-2 w-2 rounded-full"
                :class="orderCancelled ? 'bg-red-400' : 'bg-emerald-400'"
              ></span>
              {{ orderCancelled ? 'Đơn hàng đã bị hủy' : 'Đơn hàng đã được tạo thành công' }}
            </div>
          </div>
        </div>
      </section>

      <!-- CHECKOUT STEPS -->
      <div
        class="mx-auto mt-5 grid max-w-3xl grid-cols-3 rounded-2xl border border-slate-200/80 bg-white/90 p-2 shadow-sm backdrop-blur"
      >
        <div
          class="flex items-center justify-center gap-2 rounded-xl px-2 py-3 text-xs font-bold text-emerald-700 sm:text-sm"
        >
          <span class="grid h-6 w-6 place-items-center rounded-full bg-emerald-100">
            <Check :size="14" />
          </span>
          <span class="hidden sm:inline">Thông tin</span>
        </div>

        <div
          class="flex items-center justify-center gap-2 rounded-xl bg-amber-50 px-2 py-3 text-xs font-bold text-rose-900 sm:text-sm"
        >
          <span class="grid h-6 w-6 place-items-center rounded-full bg-rose-800 text-white">2</span>
          Thanh toán
        </div>

        <div
          class="flex items-center justify-center gap-2 rounded-xl px-2 py-3 text-xs font-bold text-slate-400 sm:text-sm"
        >
          <span class="grid h-6 w-6 place-items-center rounded-full bg-slate-100">3</span>
          <span class="hidden sm:inline">Hoàn tất</span>
        </div>
      </div>

      <!-- LOADING -->
      <div
        v-if="loading"
        class="mt-8 grid gap-6 lg:grid-cols-[minmax(0,1.45fr)_minmax(340px,0.75fr)]"
      >
        <div class="space-y-6">
          <div class="h-72 animate-pulse rounded-[28px] border border-slate-200 bg-white"></div>
          <div class="h-80 animate-pulse rounded-[28px] border border-slate-200 bg-white"></div>
        </div>
        <div class="h-[460px] animate-pulse rounded-[28px] border border-slate-200 bg-white"></div>
      </div>

      <!-- LOAD ERROR -->
      <div
        v-else-if="loadError"
        class="mx-auto mt-8 max-w-2xl rounded-[28px] border border-red-100 bg-white p-8 text-center shadow-xl shadow-slate-200/60"
      >
        <div class="mx-auto grid h-16 w-16 place-items-center rounded-2xl bg-red-50 text-red-600">
          <X :size="30" />
        </div>
        <h2 class="mt-5 text-2xl font-black">Không thể tải đơn hàng</h2>
        <p class="mt-2 text-slate-500">{{ loadError }}</p>
        <button
          type="button"
          class="mt-6 rounded-xl bg-slate-900 px-6 py-3 font-bold text-white transition hover:bg-rose-900"
          @click="router.push('/san-pham')"
        >
          Quay lại cửa hàng
        </button>
      </div>

      <!-- MAIN CONTENT -->
      <div
        v-else-if="orderInfo"
        class="mt-8 grid items-start gap-6 lg:grid-cols-[minmax(0,1.45fr)_minmax(340px,0.75fr)]"
      >
        <div class="space-y-6">
          <!-- PAYMENT METHOD -->
          <section
            class="rounded-[28px] border border-slate-200/80 bg-white p-5 shadow-xl shadow-slate-200/40 sm:p-7"
          >
            <div class="flex items-start justify-between gap-4">
              <div>
                <p class="text-xs font-black uppercase tracking-[0.16em] text-rose-800">Bước 1</p>
                <h2 class="mt-1 text-2xl font-black tracking-tight text-slate-900">
                  Chọn phương thức thanh toán
                </h2>
                <p class="mt-2 text-sm text-slate-500">
                  Bạn có thể thanh toán khi nhận hàng hoặc qua cổng thanh toán VNPay.
                </p>
              </div>
              <div
                class="hidden h-12 w-12 place-items-center rounded-2xl bg-amber-50 text-rose-800 sm:grid"
              >
                <CreditCard :size="24" />
              </div>
            </div>

            <div class="mt-6 grid gap-4 md:grid-cols-2">
              <!-- Nút COD -->
              <button
                type="button"
                :disabled="orderCancelled"
                class="payment-option group relative overflow-hidden rounded-2xl border-2 p-5 text-left transition disabled:cursor-not-allowed disabled:opacity-55"
                :class="
                  paymentMethod === 'COD'
                    ? 'border-rose-800 bg-amber-50/70 shadow-lg shadow-indigo-100'
                    : 'border-slate-200 bg-white hover:border-amber-200 hover:bg-[#f7f4ee]'
                "
                @click="paymentMethod = 'COD'"
              >
                <div class="flex items-start gap-4">
                  <span
                    class="grid h-12 w-12 shrink-0 place-items-center rounded-2xl bg-amber-50 text-amber-600"
                    ><Truck :size="23"
                  /></span>
                  <span class="min-w-0 flex-1">
                    <span class="block font-black text-slate-900">COD</span>
                    <span class="mt-1 block text-sm leading-5 text-slate-500"
                      >Thanh toán khi nhận hàng.</span
                    >
                  </span>
                </div>
              </button>

              <!-- VNPAY -->
              <button
                type="button"
                :disabled="orderCancelled"
                class="payment-option group relative overflow-hidden rounded-2xl border-2 p-5 text-left transition disabled:cursor-not-allowed disabled:opacity-55"
                :class="
                  paymentMethod === 'VNPAY'
                    ? 'border-rose-800 bg-amber-50/70 shadow-lg shadow-indigo-100'
                    : 'border-slate-200 bg-white hover:border-amber-200 hover:bg-[#f7f4ee]'
                "
                @click="paymentMethod = 'VNPAY'"
              >
                <div class="flex items-start gap-4">
                  <span
                    class="grid h-12 w-12 shrink-0 place-items-center rounded-2xl bg-blue-50 text-blue-600"
                    ><CreditCard :size="23"
                  /></span>
                  <span class="min-w-0 flex-1">
                    <span class="block font-black text-slate-900">VNPAY</span>
                    <span class="mt-1 block text-sm leading-5 text-slate-500"
                      >Thẻ ATM / QR Pay ngân hàng.</span
                    >
                  </span>
                </div>
              </button>
            </div>
          </section>

          <!-- PRODUCTS -->
          <section
            class="rounded-[28px] border border-slate-200/80 bg-white p-5 shadow-xl shadow-slate-200/40 sm:p-7"
          >
            <div class="flex items-center justify-between gap-4">
              <div>
                <p class="text-xs font-black uppercase tracking-[0.16em] text-rose-800">Đơn hàng</p>
                <h2 class="mt-1 text-2xl font-black tracking-tight">Sản phẩm đã chọn</h2>
              </div>
              <span class="rounded-full bg-amber-50 px-3 py-1.5 text-xs font-black text-rose-900">
                {{ orderItems.length }} sản phẩm
              </span>
            </div>

            <div class="mt-6 space-y-4">
              <article
                v-for="item in orderItems"
                :key="item.id"
                class="grid gap-5 rounded-2xl border border-slate-200/80 bg-white p-4 shadow-sm transition-all hover:shadow-md sm:grid-cols-[132px_1fr] sm:p-5"
              >
                <!-- Ảnh sản phẩm -->
                <div
                  class="h-32 w-32 flex-shrink-0 overflow-hidden rounded-2xl border border-slate-100 bg-[#f7f4ee] shadow-inner"
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
                    class="h-full w-full object-cover"
                  />
                </div>

                <!-- Thông tin sản phẩm -->
                <div class="flex min-w-0 flex-1 flex-col justify-between">
                  <div>
                    <!-- Tên & Mã SPCT -->
                    <div class="flex items-start justify-between gap-4">
                      <h3 class="line-clamp-2 text-lg font-bold text-slate-900">
                        {{ item.tenSanPham }}
                      </h3>
                      <span v-if="item.maSanPhamChiTiet" class="text-xs font-medium text-slate-400">
                        #{{ item.maSanPhamChiTiet }}
                      </span>
                    </div>

                    <!-- Thuộc tính (Màu sắc, Kích cỡ) -->
                    <div class="mt-2.5 flex flex-wrap gap-2">
                      <span
                        v-if="item.tenMauSac"
                        class="inline-flex items-center gap-1 rounded-full bg-slate-100 px-3 py-1 text-xs font-semibold text-slate-700"
                      >
                        🎨 {{ item.tenMauSac }}
                      </span>

                      <span
                        v-if="item.tenKichThuoc"
                        class="inline-flex items-center gap-1 rounded-full bg-slate-100 px-3 py-1 text-xs font-semibold text-slate-700"
                      >
                        📏 {{ item.tenKichThuoc }}
                      </span>
                    </div>
                  </div>

                  <!-- Giá, Số lượng & Thành tiền -->
                  <div
                    class="mt-4 flex flex-col gap-3 sm:flex-row sm:items-center sm:justify-between"
                  >
                    <!-- Đơn giá & Số lượng -->
                    <div class="flex items-center gap-4">
                      <div>
                        <p class="text-xs uppercase tracking-wider text-slate-400">Đơn giá</p>
                        <span class="text-base font-bold text-slate-900">
                          {{ formatMoney(item.donGia || item.thanhTien / item.soLuong) }}
                        </span>
                      </div>

                      <div class="h-6 w-px bg-slate-200"></div>

                      <div>
                        <p class="text-xs uppercase tracking-wider text-slate-400">Số lượng</p>
                        <span class="text-base font-bold text-slate-900">
                          x{{ item.soLuong }}
                        </span>
                      </div>
                    </div>

                    <!-- Thành tiền -->
                    <div
                      class="flex items-center justify-between border-t border-dashed border-slate-200 pt-3 sm:border-t-0 sm:pt-0 sm:justify-end sm:gap-2"
                    >
                      <span class="text-sm text-slate-500">Thành tiền:</span>
                      <span class="text-xl font-extrabold text-red-600">
                        {{ formatMoney(item.thanhTien) }}
                      </span>
                    </div>
                  </div>
                </div>
              </article>
            </div>
          </section>
        </div>

        <!-- SUMMARY SIDEBAR -->
        <aside class="space-y-5 lg:sticky lg:top-6">
          <section
            class="overflow-hidden rounded-[28px] border border-slate-200/80 bg-white shadow-2xl shadow-slate-200/60"
          >
            <div class="border-b border-slate-100 px-6 py-5">
              <div class="flex items-center gap-3">
                <span class="grid h-11 w-11 place-items-center rounded-2xl bg-slate-900 text-white">
                  <ReceiptText :size="22" />
                </span>
                <div>
                  <h2 class="text-xl font-black">Tóm tắt thanh toán</h2>
                  <p class="text-xs text-slate-400">Mã đơn: {{ orderCode }}</p>
                </div>
              </div>
            </div>

            <div class="space-y-4 px-6 py-5 text-sm">
              <div class="flex items-center justify-between gap-4">
                <span class="text-slate-500">Tiền hàng</span>
                <span class="font-bold text-slate-800">{{
                  formatMoney(orderInfo.tongTienHang)
                }}</span>
              </div>

              <div class="flex items-center justify-between gap-4">
                <span class="text-slate-500">Giảm giá</span>
                <span class="font-bold text-emerald-600"
                  >-{{ formatMoney(orderInfo.tongGiamGia) }}</span
                >
              </div>

              <div class="flex items-center justify-between gap-4">
                <span class="text-slate-500">Phí vận chuyển</span>
                <span class="font-bold text-slate-800">{{
                  formatMoney(orderInfo.phiVanChuyen)
                }}</span>
              </div>

              <div class="border-t border-dashed border-slate-200 pt-4">
                <div class="flex items-end justify-between gap-4">
                  <span class="font-black text-slate-900">Tổng thanh toán</span>
                  <span class="text-right text-2xl font-black text-rose-900">
                    {{ formatMoney(orderInfo.tongThanhToan) }}
                  </span>
                </div>
                <p class="mt-2 text-right text-xs text-slate-400">Đã bao gồm phí vận chuyển</p>
              </div>
            </div>

            <div class="border-t border-slate-100 bg-[#f7f4ee]/80 p-5">
              <button
                type="button"
                :disabled="payDisabled"
                class="flex w-full items-center justify-center gap-2 rounded-2xl bg-rose-800 px-5 py-4 text-base font-black text-white shadow-lg shadow-rose-200 transition hover:-translate-y-0.5 hover:bg-rose-900 disabled:cursor-not-allowed disabled:opacity-60 disabled:hover:translate-y-0"
                @click="pay"
              >
                <LoaderCircle v-if="isPaying" :size="20" class="animate-spin" />
                <PackageCheck v-else-if="paymentMethod === 'COD'" :size="20" />
                <CreditCard v-else :size="20" />
                {{ payButtonLabel }}
              </button>

              <button
                type="button"
                :disabled="isPaying || isCancelling || orderCancelled"
                class="mt-3 flex w-full items-center justify-center gap-2 rounded-2xl border border-slate-200 bg-white px-5 py-3.5 font-bold text-slate-600 transition hover:border-red-200 hover:bg-red-50 hover:text-red-600 disabled:cursor-not-allowed disabled:opacity-60"
                @click="handleCancel"
              >
                <LoaderCircle v-if="isCancelling" :size="18" class="animate-spin" />
                <X v-else :size="18" />
                {{ isCancelling ? 'Đang hủy đơn...' : 'Hủy đơn hàng' }}
              </button>
            </div>
          </section>

          <section class="rounded-2xl border border-emerald-100 bg-emerald-50/80 p-5">
            <div class="flex gap-3">
              <span
                class="grid h-10 w-10 shrink-0 place-items-center rounded-xl bg-white text-emerald-600 shadow-sm"
              >
                <ShieldCheck :size="21" />
              </span>
              <div>
                <h3 class="font-black text-emerald-900">Thông tin đơn hàng được bảo vệ</h3>
                <p class="mt-1 text-sm leading-5 text-emerald-800/75">
                  Hãy lưu mã đơn để theo dõi trạng thái bằng số điện thoại nhận hàng.
                </p>
              </div>
            </div>
          </section>

          <RouterLink
            to="/donhang"
            class="flex items-center justify-between rounded-2xl border border-slate-200 bg-white p-5 text-slate-700 shadow-sm transition hover:border-amber-200 hover:text-rose-900"
          >
            <span class="flex items-center gap-3 font-bold">
              <ShoppingBag :size="20" />
              Tra cứu đơn sau khi đặt
            </span>
            <ArrowLeft :size="18" class="rotate-180" />
          </RouterLink>
        </aside>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import {
  ArrowLeft,
  Check,
  Copy,
  CreditCard,
  LoaderCircle,
  Lock,
  PackageCheck,
  ReceiptText,
  Search,
  ShieldCheck,
  ShoppingBag,
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

    console.log('=== DỮ LIỆU GỬI LÊN BACKEND ===', body)
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
    linear-gradient(rgba(255, 255, 255, 0.72), rgba(248, 250, 252, 0.9)),
    radial-gradient(circle at 1px 1px, rgba(99, 102, 241, 0.12) 1px, transparent 0);
  background-size:
    auto,
    24px 24px;
}

.payment-option::after {
  position: absolute;
  right: -36px;
  bottom: -52px;
  width: 120px;
  height: 120px;
  content: '';
  border-radius: 9999px;
  background: rgba(99, 102, 241, 0.08);
  transition: transform 220ms ease;
}

.payment-option:hover::after {
  transform: scale(1.15);
}
</style>
