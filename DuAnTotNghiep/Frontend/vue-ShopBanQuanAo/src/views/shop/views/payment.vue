<template>
  <div class="bg-gray-100 min-h-screen py-8">
    <div class="max-w-4xl mx-auto px-4">
      <!-- HEADER -->
      <div class="mb-6">
        <h1 class="text-3xl font-bold">Thanh toán đơn hàng</h1>
        <p class="text-gray-500">Mã đơn: {{ orderInfo?.maHoaDon }}</p>
      </div>

      <!-- LOADING -->
      <div v-if="loading" class="text-center py-20">Đang tải đơn hàng...</div>

      <div v-else class="space-y-6">
        <!-- ORDER INFO -->
        <div class="bg-white p-6 rounded-2xl shadow">
          <h2 class="font-bold text-lg mb-4">Thông tin đơn hàng</h2>

          <div class="space-y-2 text-gray-700">
            <div>Tổng tiền hàng: {{ orderInfo?.tongTienHang?.toLocaleString() }}đ</div>

            <div>Giảm giá: -{{ orderInfo?.tongGiamGia?.toLocaleString() }}đ</div>

            <div>Phí vận chuyển: {{ orderInfo?.phiVanChuyen?.toLocaleString() }}đ</div>

            <div class="font-bold text-xl text-red-500">
              Tổng thanh toán: {{ orderInfo?.tongThanhToan?.toLocaleString() }}đ
            </div>
          </div>
        </div>

        <!-- PAYMENT METHOD -->
        <div class="bg-white p-6 rounded-2xl shadow">
          <h2 class="font-bold text-lg mb-4">Chọn phương thức thanh toán</h2>

          <div class="space-y-3">
            <label class="border p-4 rounded-xl flex justify-between cursor-pointer">
              <span>COD - Thanh toán khi nhận hàng</span>
              <input type="radio" value="COD" v-model="method" />
            </label>

            <label class="border p-4 rounded-xl flex justify-between cursor-pointer">
              <span>Chuyển khoản(QR)</span>
              <input type="radio" value="BANK" v-model="method" />
            </label>
          </div>
        </div>

        <!-- ITEMS -->
        <div class="bg-white p-6 rounded-2xl shadow">
          <h2 class="font-bold text-lg mb-4">Sản phẩm</h2>

          <div v-for="item in orderItems" :key="item.id" class="flex justify-between border-b py-3">
            <div>
              <div class="font-semibold">{{ item.tenSanPham }}</div>

              <div class="text-sm text-gray-500">
                {{ item.sanPhamChiTiet?.idMauSac?.tenMauSac }}
                /
                {{ item.sanPhamChiTiet?.idKichThuoc?.tenKichThuoc }}
              </div>

              <div class="text-sm">x{{ item.soLuong }}</div>
            </div>

            <div class="font-bold">{{ item.thanhTien?.toLocaleString() }}đ</div>
          </div>
        </div>

        <!-- BUTTON -->
        <button
          class="w-full bg-black text-white py-4 rounded-xl font-bold text-lg hover:bg-gray-800"
          @click="pay"
        >
          THANH TOÁN
        </button>
        <button
          class="w-full bg-red-500 text-white py-4 rounded-xl font-bold text-lg mt-3"
          @click="handleCancel"
        >
          HỦY ĐƠN HÀNG
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { huyHoaDon, huyHoaDonOnline, thanhToanHoaDon } from '@/service/HoaDonService'

const route = useRoute()
const router = useRouter()

const orderId = route.query.id
const maHD = route.query.maHoaDon

const orderItems = ref([])
const orderInfo = ref(null)
const loading = ref(true)
const method = ref('COD')

// LOAD ORDER
onMounted(async () => {
  try {
    const res = await fetch(`http://localhost:8080/hoadon/${orderId}/chi-tiet`)

    const data = await res.json()
    console.log('RAW DATA:', data)

    // items
    orderItems.value = data

    orderInfo.value = {
      maHoaDon: data[0]?.maHD,
      tongTienHang: data[0]?.thanhTien,
      tongGiamGia: data[0]?.giamGia,
      tongThanhToan: data[0]?.tongThanhToan,
      phiVanChuyen: data[0]?.phiShip ?? 0,
    }
    console.log(orderInfo.value)
  } catch (e) {
    console.log(e)
  } finally {
    loading.value = false
  }
})

// PAY
const pay = async () => {
  try {
    const body = {
      idHoaDon: Number(orderId),
      idPhuongThucThanhToan: method.value === 'COD' ? 1 : 2,

      // Nếu không dùng voucher nữa thì cứ để null
      idVoucher: null,
    }

    console.log(body)

    const res = await thanhToanHoaDon(body)

    console.log(res)

    alert('Thanh toán thành công 🎉')

    router.push('/')
  } catch (e) {
    console.error(e)
    alert(e.message)
  }
}
const handleCancel = async () => {
  try {
    await huyHoaDonOnline(Number(orderId))

    alert('Đã hủy đơn hàng')

    router.push('/')
  } catch (e) {
    console.error(e)

    alert(e.message)
  }
}
</script>
