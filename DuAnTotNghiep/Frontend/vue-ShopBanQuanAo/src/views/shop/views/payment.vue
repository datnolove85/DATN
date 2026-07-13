```vue
<template>
  <div class="min-h-screen bg-gradient-to-br from-slate-100 via-blue-50 to-indigo-100 py-10">
    <div class="max-w-5xl mx-auto px-4">
      <!-- HEADER -->
      <div
        class="relative overflow-hidden rounded-2xl bg-gradient-to-r from-indigo-700 via-blue-600 to-cyan-500 p-5 shadow-2xl text-white"
      >
        <div class="absolute -right-10 -top-10 w-52 h-52 rounded-full bg-white/10"></div>
        <div class="absolute -left-10 -bottom-20 w-60 h-60 rounded-full bg-white/10"></div>

        <div class="relative flex items-center gap-6">
          <div
            class="w-14 h-14 rounded-xl bg-white/20 backdrop-blur flex items-center justify-center text-3xl shadow-lg"
          >
            💳
          </div>

          <div>
            <h1 class="text-4xl font-black tracking-tight">Thanh toán đơn hàng</h1>

            <p class="mt-3 text-blue-100 text-lg">
              Mã đơn hàng:

              <span class="font-black text-white">
                {{ orderInfo?.maHoaDon }}
              </span>
            </p>
          </div>
        </div>
      </div>

      <!-- LOADING -->

      <div v-if="loading" class="mt-10 bg-white rounded-3xl p-20 text-center shadow-xl">
        <div class="text-5xl mb-5 animate-bounce">⏳</div>

        <div class="text-xl font-bold text-slate-600">Đang tải đơn hàng...</div>
      </div>

      <div v-else class="mt-5 space-y-5">
        <!-- SUMMARY -->

        <div class="bg-white rounded-2xl shadow-lg p-5">
          <div class="flex justify-between items-center mb-7">
            <h2 class="text-2xl font-black text-slate-800">Tổng quan đơn hàng</h2>

            <span class="px-5 py-2 rounded-full bg-green-100 text-green-700 font-bold">
              Chờ thanh toán
            </span>
          </div>

          <div class="grid md:grid-cols-4 gap-5">
            <div class="rounded-3xl bg-slate-50 p-6">
              <p class="text-slate-500">Tiền hàng</p>

              <p class="text-2xl font-black mt-2">
                {{ formatMoney(orderInfo.tongTienHang) }}
              </p>
            </div>

            <div class="rounded-3xl bg-red-50 p-6">
              <p class="text-red-500">Giảm giá</p>

              <p class="text-2xl font-black mt-2 text-red-600">
                -
                {{ formatMoney(orderInfo.tongGiamGia) }}
              </p>
            </div>

            <div class="rounded-3xl bg-blue-50 p-6">
              <p class="text-blue-600">Phí vận chuyển</p>

              <p class="text-2xl font-black mt-2">
                {{ formatMoney(orderInfo.phiVanChuyen) }}
              </p>
            </div>

            <div
              class="rounded-3xl bg-gradient-to-br from-indigo-600 to-blue-600 text-white p-6 shadow-lg"
            >
              <p>Thanh toán</p>

              <p class="text-3xl font-black mt-2">
                {{ formatMoney(orderInfo.tongThanhToan) }}
              </p>
            </div>
          </div>
        </div>

        <!-- PAYMENT METHOD -->

        <div class="bg-white rounded-[32px] shadow-xl p-8">
          <h2 class="text-2xl font-black mb-7 text-slate-800">Phương thức thanh toán</h2>

          <div class="grid md:grid-cols-2 gap-6">
            <!-- COD -->

            <div
              @click="paymentMethod = 'COD'"
              :class="
                paymentMethod === 'COD'
                  ? 'border-indigo-600 bg-indigo-50 ring-2 ring-indigo-200'
                  : 'border-slate-200'
              "
              class="cursor-pointer border-2 rounded-3xl p-6 transition hover:shadow-lg"
            >
              <div class="flex items-center gap-5">
                <div
                  class="w-16 h-16 rounded-2xl bg-orange-100 flex items-center justify-center text-4xl"
                >
                  📦
                </div>

                <div class="flex-1">
                  <h3 class="font-black text-lg">Thanh toán khi nhận hàng</h3>

                  <p class="text-slate-500 mt-1">Ship COD - trả tiền khi nhận hàng</p>
                </div>

                <div v-if="paymentMethod === 'COD'" class="text-indigo-600 text-3xl">✓</div>
              </div>
            </div>

            <!-- BANK -->

            <div
              @click="paymentMethod = 'BANK'"
              :class="
                paymentMethod === 'BANK'
                  ? 'border-indigo-600 bg-indigo-50 ring-2 ring-indigo-200'
                  : 'border-slate-200'
              "
              class="cursor-pointer border-2 rounded-3xl p-6 transition hover:shadow-lg"
            >
              <div class="flex items-center gap-5">
                <div
                  class="w-16 h-16 rounded-2xl bg-blue-100 flex items-center justify-center text-4xl"
                >
                  🏦
                </div>

                <div class="flex-1">
                  <h3 class="font-black text-lg">Chuyển khoản QR</h3>

                  <p class="text-slate-500 mt-1">VietQR / Internet Banking</p>
                </div>

                <div v-if="paymentMethod === 'BANK'" class="text-indigo-600 text-3xl">✓</div>
              </div>
            </div>

            <!-- MOMO -->

            <div class="rounded-3xl border p-6 bg-slate-50 opacity-60">
              <div class="flex items-center gap-5">
                <img
                  src="https://upload.wikimedia.org/wikipedia/vi/f/fe/MoMo_Logo.png"
                  class="w-14 h-14 object-contain"
                />

                <div>
                  <h3 class="font-bold">Ví MoMo</h3>

                  <p class="text-sm text-slate-500">Sắp ra mắt</p>
                </div>
              </div>
            </div>

            <!-- VNPAY -->

            <div class="rounded-3xl border p-6 bg-slate-50 opacity-60">
              <div class="flex items-center gap-5">
                <img
                  src="https://vinadesign.vn/uploads/thumbnails/800/2023/05/logo-vnpay-inkythuatso-01-13-15-26-42.jpg"
                  class="w-14 h-14 object-contain"
                />

                <div>
                  <h3 class="font-bold">VNPay</h3>

                  <p class="text-sm text-slate-500">Sắp ra mắt</p>
                </div>
              </div>
            </div>
          </div>
        </div>
        <!-- QR PAYMENT -->
        <Transition name="fade">
          <div v-if="paymentMethod === 'BANK'" class="bg-white rounded-[32px] shadow-xl p-8">
            <div class="flex items-center justify-between mb-7">
              <h2 class="text-2xl font-black text-slate-800">Thanh toán bằng VietQR</h2>

              <span class="px-4 py-2 rounded-full bg-blue-100 text-blue-700 font-bold"> BANK </span>
            </div>

            <div class="grid md:grid-cols-2 gap-10 items-center">
              <!-- QR IMAGE -->

              <div class="flex justify-center">
                <div class="bg-white p-5 rounded-[32px] shadow-lg border">
                  <img :src="qrUrl" class="w-80 h-80 object-contain" />
                </div>
              </div>

              <!-- BANK INFO -->

              <div class="space-y-6">
                <div>
                  <p class="text-slate-500">Nội dung chuyển khoản</p>

                  <div
                    class="mt-2 bg-slate-100 rounded-2xl p-5 font-black text-2xl text-indigo-600"
                  >
                    {{ orderInfo.maHoaDon }}
                  </div>
                </div>

                <div>
                  <p class="text-slate-500">Số tiền cần chuyển</p>

                  <div class="mt-2 text-4xl font-black text-red-600">
                    {{ formatMoney(orderInfo.tongThanhToan) }}
                  </div>
                </div>

                <div class="rounded-3xl bg-blue-50 border border-blue-200 p-6 leading-7">
                  <p>✅ Quét QR bằng ứng dụng ngân hàng</p>

                  <p>✅ Chuyển đúng số tiền</p>

                  <p>✅ Nội dung chuyển khoản phải chính xác</p>

                  <p>✅ Sau đó bấm xác nhận thanh toán</p>
                </div>
              </div>
            </div>
          </div>
        </Transition>

        <!-- PRODUCTS -->

        <div class="bg-white rounded-[32px] shadow-xl p-8">
          <div class="flex justify-between items-center mb-7">
            <h2 class="text-2xl font-black text-slate-800">Sản phẩm trong đơn</h2>

            <span class="px-5 py-2 rounded-full bg-indigo-100 text-indigo-700 font-bold">
              {{ orderItems.length }} sản phẩm
            </span>
          </div>

          <div class="space-y-5">
            <div
              v-for="item in orderItems"
              :key="item.id"
              class="flex items-center gap-5 border rounded-2xl p-4 hover:shadow-md transition"
            >
              <!-- ẢNH SẢN PHẨM -->
              <div class="w-24 h-24 rounded-2xl overflow-hidden bg-slate-100 flex-shrink-0">
                <img
                  :src="
                    orderProductImage
                      ? 'http://localhost:8080' + orderProductImage
                      : '/no-image.png'
                  "
                  class="w-full h-full object-cover"
                />
              </div>
              <div class="flex-1">
                <h3 class="font-black text-lg text-slate-800">
                  {{ item.tenSanPham }}
                </h3>

                <div class="mt-2 text-slate-500">
                  {{ item.sanPhamChiTiet?.idMauSac?.tenMauSac }}

                  <span class="mx-2"> • </span>

                  {{ item.sanPhamChiTiet?.idKichThuoc?.tenKichThuoc }}
                </div>

                <div class="mt-3 inline-flex px-4 py-2 rounded-full bg-slate-100 font-bold">
                  Số lượng:
                  {{ item.soLuong }}
                </div>
              </div>

              <div class="text-right">
                <p class="text-2xl font-black text-indigo-600">
                  {{ formatMoney(item.thanhTien) }}
                </p>
              </div>
            </div>
          </div>
        </div>

        <!-- ACTION -->

        <div class="bg-white rounded-[32px] shadow-xl p-6 sticky bottom-5">
          <div class="grid md:grid-cols-2 gap-5">
            <button
              @click="pay"
              class="py-5 rounded-3xl bg-gradient-to-r from-indigo-600 to-blue-600 text-white text-xl font-black shadow-lg hover:scale-[1.02] transition"
            >
              {{ paymentMethod === 'COD' ? 'ĐẶT HÀNG' : 'XÁC NHẬN THANH TOÁN' }}
            </button>

            <button
              @click="handleCancel"
              class="py-5 rounded-3xl bg-red-500 hover:bg-red-600 text-white text-xl font-black transition"
            >
              HỦY ĐƠN HÀNG
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { huyHoaDonOnline } from '@/service/HoaDonService'
import { useToast } from 'vue-toastification'
import { thanhToan } from '@/service/PaymentService'

const toast = useToast()
const route = useRoute()
const router = useRouter()

const orderId = route.query.id
const qrUrl = route.query.qrUrl

const loading = ref(true)

const orderItems = ref([])
const orderInfo = ref(null)
console.log('READ =', sessionStorage.getItem('orderProduct'))
const savedProduct = JSON.parse(sessionStorage.getItem('orderProduct'))

const orderProductImage = savedProduct?.images?.[0] || ''

console.log('ẢNH SẢN PHẨM:', orderProductImage)
// Phương thức đang chọn
const paymentMethod = ref('COD')

// Format tiền
const formatMoney = (money) => {
  return Number(money || 0).toLocaleString('vi-VN') + ' đ'
}

// Load hóa đơn
onMounted(async () => {
  try {
    const res = await fetch(`http://localhost:8080/hoadon/${orderId}/chi-tiet`)

    const data = await res.json()

    orderItems.value = data

    orderInfo.value = {
      maHoaDon: data[0]?.maHD,
      tongTienHang: data[0]?.thanhTien,
      tongGiamGia: data[0]?.giamGia,
      tongThanhToan: data[0]?.tongThanhToan,
      phiVanChuyen: data[0]?.phiShip ?? 0,
    }
  } catch (e) {
    console.error(e)
    toast.error('Không tải được thông tin đơn hàng')
  } finally {
    loading.value = false
  }
})

// Thanh toán
const pay = async () => {
  try {
    const body = {
      idHoaDon: Number(orderId),
      method: paymentMethod.value,
    }

    const res = await thanhToan(body)

    console.log(res)

    toast.success(res.message || 'Thanh toán thành công')

    router.push('/san-pham')
  } catch (e) {
    console.error(e)

    toast.error(e.message || 'Thanh toán thất bại')
  }
}

// Hủy đơn
const handleCancel = async () => {
  try {
    await huyHoaDonOnline(Number(orderId))

    toast.success('Đã hủy đơn hàng')

    router.push('/san-pham')
  } catch (e) {
    console.error(e)
    toast.error(e.message || 'Hủy đơn thất bại')
  }
}
</script>
