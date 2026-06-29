<template>
  <div class="bg-gray-100 min-h-screen py-8">
    <div class="max-w-7xl mx-auto px-4">
      <!-- Header -->
      <div class="mb-6">
        <h1 class="text-3xl font-bold text-gray-900">Xác nhận đơn hàng</h1>
        <p class="text-gray-500 mt-1">Kiểm tra thông tin trước khi đặt hàng</p>
      </div>

      <div class="grid grid-cols-1 lg:grid-cols-3 gap-6">
        <!-- LEFT -->
        <div class="lg:col-span-2 space-y-6">
          <!-- ADDRESS -->
          <div class="bg-white rounded-2xl shadow-sm p-6">
            <div class="flex justify-between items-center mb-5">
              <h2 class="font-bold text-lg">Địa chỉ nhận hàng</h2>
              <button @click="isAddressModalOpen = true" class="text-blue-600 hover:underline">
                Thay đổi
              </button>
            </div>

            <div class="space-y-2">
              <div class="font-semibold text-lg">{{ address.name }} | {{ address.phone }}</div>
              <div class="text-gray-600">
                {{ address.address }}
              </div>
            </div>
          </div>

          <!-- PRODUCT -->
          <div class="bg-white rounded-2xl shadow-sm p-6">
            <div class="font-bold text-lg mb-5">Sản phẩm</div>

            <div class="flex gap-5">
              <img
                :src="product?.images?.length ? 'http://localhost:8080' + product.images[0] : ''"
                class="w-28 h-28 rounded-xl object-cover border"
              />

              <div class="flex-1">
                <h3 class="font-semibold text-lg">
                  {{ product?.tenSanPham }}
                </h3>

                <div class="text-gray-500 mt-2">
                  Màu: <span class="font-medium">{{ product?.tenMauSac }}</span>
                </div>

                <div class="text-gray-500">
                  Size: <span class="font-medium">{{ product?.tenKichThuoc }}</span>
                </div>

                <div class="mt-4 flex justify-between items-end">
                  <div>
                    <div class="text-gray-400 line-through">
                      {{ ((product?.giaBan || 0) * 1.2).toLocaleString() }}đ
                    </div>

                    <div class="text-red-500 text-2xl font-bold">
                      {{ (product?.giaBan || 0).toLocaleString() }}đ
                    </div>
                  </div>

                  <div class="flex items-center border rounded-lg overflow-hidden">
                    <button class="px-4 py-2 hover:bg-gray-100" @click="decreaseQty">-</button>

                    <div class="px-5">
                      {{ quantity }}
                    </div>

                    <button class="px-4 py-2 hover:bg-gray-100" @click="increaseQty">+</button>
                  </div>

                  <div class="text-sm text-gray-500 mt-2">Còn lại: {{ stock }} sản phẩm</div>
                </div>
              </div>
            </div>
          </div>

          <!-- SHIPPING -->
          <div class="bg-white rounded-2xl shadow-sm p-6">
            <h2 class="font-bold text-lg mb-5">🚚 Vận chuyển</h2>

            <div class="space-y-3">
              <label class="border rounded-xl p-4 flex justify-between cursor-pointer">
                <div>
                  <div class="font-semibold">Tiêu chuẩn</div>
                  <div class="text-gray-500 text-sm">2-4 ngày</div>
                </div>

                <div class="flex items-center gap-4">
                  <div>25.000đ</div>
                  <input type="radio" :value="25000" v-model="shippingFee" />
                </div>
              </label>

              <label class="border rounded-xl p-4 flex justify-between cursor-pointer">
                <div>
                  <div class="font-semibold">Giao nhanh</div>
                  <div class="text-gray-500 text-sm">Trong ngày</div>
                </div>

                <div class="flex items-center gap-4">
                  <div>40.000đ</div>
                  <input type="radio" :value="40000" v-model="shippingFee" />
                </div>
              </label>
            </div>
          </div>

          <!-- VOUCHER -->
          <div class="bg-white rounded-2xl shadow-sm p-6">
            <h2 class="font-bold text-lg mb-5">🎁 Voucher</h2>

            <select v-model="selectedVoucherId" class="w-full border rounded-xl p-3">
              <option :value="null">Không áp dụng</option>

              <option
                v-for="voucher in vouchers"
                :key="voucher.id"
                :value="voucher.id"
                :disabled="subtotal < voucher.giaTriDonHangToiThieu"
              >
                {{ voucher.tenVoucher }} - {{ voucher.maVoucher }}
              </option>
            </select>
          </div>

          <!-- NOTE -->
          <div class="bg-white rounded-2xl shadow-sm p-6">
            <h2 class="font-bold text-lg mb-5">📝 Ghi chú</h2>

            <textarea
              rows="4"
              v-model="note"
              class="border rounded-xl p-4 w-full resize-none"
              placeholder="Nhập ghi chú..."
            />
          </div>
        </div>

        <!-- RIGHT -->
        <div>
          <div class="bg-white rounded-2xl shadow-sm p-6 sticky top-5">
            <h2 class="font-bold text-xl mb-6">Đơn hàng</h2>

            <div class="space-y-4">
              <div class="flex justify-between">
                <span>Tiền hàng</span>
                <span>{{ subtotal.toLocaleString() }}đ</span>
              </div>

              <div class="flex justify-between">
                <span>Phí ship</span>
                <span>{{ shippingFee.toLocaleString() }}đ</span>
              </div>

              <div class="flex justify-between text-green-600">
                <span>Voucher</span>
                <span>-{{ voucherDiscount.toLocaleString() }}đ</span>
              </div>

              <hr />

              <div class="flex justify-between items-center">
                <div class="font-bold text-lg">Tổng cộng</div>
                <div class="text-red-500 text-3xl font-bold">{{ total.toLocaleString() }}đ</div>
              </div>

              <button
                class="w-full mt-6 bg-black hover:bg-gray-800 text-white py-4 rounded-xl font-bold text-lg"
                @click="placeOrder"
              >
                ĐẶT HÀNG
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
  <div
    v-if="isAddressModalOpen"
    class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4 z-50"
  >
    <div class="bg-white rounded-2xl w-full max-w-md p-6">
      <div v-if="!isAddingNew">
        <h2 class="text-xl font-bold mb-4">Chọn địa chỉ giao hàng</h2>
        <div class="space-y-3 max-h-60 overflow-y-auto">
          <div
            v-for="addr in mockAddressList"
            :key="addr.id"
            @click="selectAddress(addr)"
            class="border p-4 rounded-xl cursor-pointer hover:border-black"
            :class="{ 'border-black bg-gray-50': currentAddress.id === addr.id }"
          >
            <div class="font-semibold">{{ addr.name }} | {{ addr.phone }}</div>
            <div class="text-sm text-gray-600">{{ addr.address }}</div>
          </div>
        </div>
        <button
          @click="isAddingNew = true"
          class="w-full mt-4 py-2 border-2 border-dashed rounded-xl text-gray-500 hover:border-black hover:text-black"
        >
          + Thêm địa chỉ mới
        </button>
      </div>

      <div v-else class="space-y-3">
        <h2 class="text-xl font-bold mb-4">Thêm địa chỉ mới</h2>
        <input
          v-model="addressForm.name"
          placeholder="Họ tên"
          class="w-full border p-3 rounded-lg"
        />
        <input
          v-model="addressForm.phone"
          placeholder="Số điện thoại"
          class="w-full border p-3 rounded-lg"
        />

        <select v-model="addressForm.province" class="w-full border p-3 rounded-lg">
          <option value="" disabled>Chọn Tỉnh/Thành phố</option>
          <option v-for="p in provinces" :key="p.code" :value="p.name">{{ p.name }}</option>
        </select>

        <select
          v-model="addressForm.district"
          class="w-full border p-3 rounded-lg"
          :disabled="!addressForm.province"
        >
          <option value="" disabled>Chọn Quận/Huyện</option>
          <option v-for="d in districts" :key="d.code" :value="d.name">{{ d.name }}</option>
        </select>

        <select
          v-model="addressForm.ward"
          class="w-full border p-3 rounded-lg"
          :disabled="!addressForm.district"
        >
          <option value="" disabled>Chọn Phường/Xã</option>
          <option v-for="w in wards" :key="w.code" :value="w.name">{{ w.name }}</option>
        </select>

        <input
          v-model="addressForm.detail"
          placeholder="Số nhà, tên đường..."
          class="w-full border p-3 rounded-lg"
        />

        <div class="flex gap-3 mt-6">
          <button @click="isAddingNew = false" class="flex-1 py-3 border rounded-xl">Hủy</button>
          <button @click="saveAddress" class="flex-1 bg-black text-white py-3 rounded-xl font-bold">
            Lưu
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getAllVoucher } from '@/service/VoucherService'
import { getSanPhamChiTietById } from '@/service/SanPhamChiTiet'
import { taoHoaDonOnline } from '@/service/HoaDonService'
import axios from 'axios'

//Địa chỉ
const provinces = ref([])
const districts = ref([])
const wards = ref([])

const addressForm = ref({
  name: '',
  phone: '',
  province: '',
  district: '',
  ward: '',
  detail: '',
})

// Load Tỉnh/Thành phố khi component được tạo
onMounted(async () => {
  vouchers.value = await getAllVoucher()
  product.value = await getSanPhamChiTietById(spctId)

  const token = localStorage.getItem('token')
  console.log(token)
  const resDiaChi = await fetch('http://localhost:8080/kh/dia-chi', {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  })
  const data = await resDiaChi.json()

  console.log('Địa chỉ:', data)

  const addr = {
    id: data.id,
    name: data.tenNguoiNhan,
    phone: data.soDienThoai,
    address: `${data.diaChiCuThe}, ${data.phuong}, ${data.quan}, ${data.thanhPho}`,
  }

  // địa chỉ đang chọn
  address.value = addr

  // địa chỉ hiện tại
  currentAddress.value = addr

  // danh sách địa chỉ
  mockAddressList.value = [addr]
})

// Watch để lấy Quận/Huyện khi chọn Tỉnh
watch(
  () => addressForm.value.province,
  async (val) => {
    if (!val) return
    const p = provinces.value.find((p) => p.name === val)
    const res = await axios.get(`https://provinces.open-api.vn/api/p/${p.code}?depth=2`)
    districts.value = res.data.districts
  },
)

// Watch để lấy Phường/Xã khi chọn Quận
watch(
  () => addressForm.value.district,
  async (val) => {
    if (!val) return
    const d = districts.value.find((d) => d.name === val)
    const res = await axios.get(`https://provinces.open-api.vn/api/d/${d.code}?depth=2`)
    wards.value = res.data.wards
  },
)

const saveAddress = () => {
  const fullAddress = `${addressForm.value.detail}, ${addressForm.value.ward}, ${addressForm.value.district}, ${addressForm.value.province}`
  const newEntry = {
    id: Date.now(),
    name: addressForm.value.name,
    phone: addressForm.value.phone,
    address: fullAddress,
  }

  mockAddressList.value.push(newEntry)
  currentAddress.value = newEntry
  isAddingNew.value = false
  // Reset form sau khi lưu
  addressForm.value = { name: '', phone: '', province: '', district: '', ward: '', detail: '' }
}
const route = useRoute()
const router = useRouter()

// SAFE SPCT ID
const spctId = Number(route.query.spct)
const quantity = ref(Number(route.query.qty) || 1)

// ❗ guard tránh NaN
if (!spctId) {
  router.push('/')
}

// MODAL STATE
const isAddressModalOpen = ref(false)

// Hàm chọn địa chỉ
const selectAddress = (addr) => {
  currentAddress.value = addr
  address.value = addr
  isAddressModalOpen.value = false
}

const isAddingNew = ref(false) // Toggle giữa danh sách và form thêm mới

// FORM MỚI
const newAddress = ref({ name: '', phone: '', address: '' })

// HÀM LƯU ĐỊA CHỈ

const address = ref({
  id: null,
  name: '',
  phone: '',
  address: '',
})

const mockAddressList = ref([])
const currentAddress = ref(null)

// PRODUCT
const product = ref(null)

const stock = computed(() => {
  return product.value?.soLuongTon || 0
})

const increaseQty = () => {
  if (quantity.value >= stock.value) {
    alert(`Chỉ còn ${stock.value} sản phẩm trong kho`)
    return
  }

  quantity.value++
}

const decreaseQty = () => {
  if (quantity.value > 1) {
    quantity.value--
  }
}
// VOUCHER
const vouchers = ref([])
const selectedVoucherId = ref(null)

// SHIPPING
const shippingFee = ref(25000)

// NOTE
const note = ref('')

// LOAD PRODUCT + VOUCHER
onMounted(async () => {
  vouchers.value = await getAllVoucher()
  product.value = await getSanPhamChiTietById(spctId)
})

/**
 * SUBTOTAL
 */
const subtotal = computed(() => {
  if (!product.value) return 0
  return (product.value.giaBan || 0) * quantity.value
})

/**
 * SELECTED VOUCHER OBJECT
 */
const selectedVoucher = computed(() => {
  return vouchers.value.find((v) => v.id === selectedVoucherId.value) || null
})

/**
 * CHECK VALID VOUCHER
 */
const isVoucherValid = computed(() => {
  const v = selectedVoucher.value
  if (!v) return false
  return subtotal.value >= v.giaTriDonHangToiThieu
})

/**
 * DISCOUNT
 */
const voucherDiscount = computed(() => {
  const v = selectedVoucher.value
  if (!v || !isVoucherValid.value) return 0

  if (v.loaiGiamGia === 'phan_tram') {
    const d = (subtotal.value * v.giaTriGiam) / 100
    return Math.min(d, v.giaTriGiamToiDa)
  }

  if (v.loaiGiamGia === 'tien_mat') {
    return v.giaTriGiam || 0
  }

  return 0
})

/**
 * TOTAL
 */
const total = computed(() => {
  const s = subtotal.value || 0
  const ship = shippingFee.value || 0
  const d = voucherDiscount.value || 0

  return Math.max(s + ship - d, 0)
})

/**
 * PLACE ORDER (giữ nguyên logic mock của bạn)
 */

const placeOrder = async () => {
  const token = localStorage.getItem('token')

  if (!token) {
    router.push('/login')
    return
  }

  const body = {
    idKhachHang: null, // hoặc lấy từ user login nếu có
    shippingFee: shippingFee.value,
    voucherId: selectedVoucher.value?.id ?? null,
    note: note.value,
    items: [
      {
        productDetailId: spctId,
        quantity: quantity.value,
      },
    ],
  }

  try {
    const res = await taoHoaDonOnline(body, token)
    console.log('SEND ORDER BODY:', JSON.stringify(body, null, 2))
    console.log('ORDER RESULT:', res)

    console.log(res.id)
    alert('Đặt hàng thành công 🎉')

    // backend trả về {id, maHoaDon}
    router.push(`/payment?id=${res.id}&maHoaDon=${res.maHoaDon}`)
  } catch (err) {
    console.error(err)
    alert('Đặt hàng thất bại ❌')
  }
}
</script>
