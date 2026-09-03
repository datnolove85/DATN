<template>
  <main class="min-h-screen bg-[#f7f5f2] text-[#24211f]">
    <section class="border-b border-[#e5e0da] bg-white">
      <div class="mx-auto max-w-6xl px-5 py-6 sm:px-8 lg:px-10">
        <div
          id="profile"
          class="flex flex-col gap-5 sm:flex-row sm:items-center sm:justify-between"
        >
          <div class="flex min-w-0 items-center gap-4">
            <div
              class="relative h-16 w-16 shrink-0 overflow-hidden rounded-full border border-[#ddd5cc] bg-[#f1ece6]"
            >
              <img
                v-if="avatarUrl"
                :src="avatarUrl"
                alt="Ảnh đại diện"
                class="h-full w-full object-cover"
                @error="avatarError = true"
              />
              <div
                v-else
                class="grid h-full w-full place-items-center text-lg font-black text-[#7d2d3a]"
              >
                {{ initials }}
              </div>
            </div>
            <div class="min-w-0">
              <div class="flex flex-wrap items-center gap-2">
                <h1 class="truncate text-xl font-black tracking-tight sm:text-2xl">
                  {{ profile.hoTen || 'Khách hàng' }}
                </h1>
                <span
                  class="rounded-full bg-[#f4e8e9] px-2.5 py-1 text-[10px] font-black uppercase tracking-wide text-[#7d2d3a]"
                >
                  {{ profile.hangThanhVien || 'Thành viên' }}
                </span>
              </div>
              <p class="mt-1 text-sm text-[#766e68]">
                {{ profile.email || profile.tenTaiKhoan || 'Chưa cập nhật email' }}
              </p>
            </div>
          </div>

          <div class="flex flex-wrap gap-2">
            <button
              type="button"
              class="rounded-xl border border-[#dcd5ce] bg-white px-4 py-2.5 text-sm font-bold text-[#4d4641] transition hover:border-[#b98b92] hover:text-[#7d2d3a]"
              @click="activeTab = 'profile'"
            >
              Chỉnh sửa hồ sơ
            </button>
            <RouterLink
              to="/donhang"
              class="rounded-xl bg-[#7d2d3a] px-4 py-2.5 text-sm font-bold text-white transition hover:bg-[#682430]"
            >
              Đơn mua
            </RouterLink>
          </div>
        </div>

        <div class="mt-5 grid grid-cols-2 border-t border-[#eee9e4] pt-5 sm:grid-cols-4">
          <div
            v-for="stat in stats"
            :key="stat.label"
            class="border-r border-[#eee9e4] px-4 first:pl-0 last:border-r-0 sm:px-6"
          >
            <p class="text-[10px] font-black uppercase tracking-[0.14em] text-[#958b84]">
              {{ stat.label }}
            </p>
            <p class="mt-1.5 text-lg font-black text-[#2c2825]">{{ stat.value }}</p>
          </div>
        </div>
      </div>
    </section>

    <section class="mx-auto max-w-6xl px-5 py-7 sm:px-8 lg:px-10">
      <div class="grid gap-5 lg:grid-cols-[210px_1fr]">
        <aside>
          <div class="overflow-hidden rounded-2xl border border-[#e4ded8] bg-white">
            <button
              v-for="item in tabs"
              :key="item.key"
              type="button"
              class="flex w-full items-center gap-3 border-b border-[#f0ece8] px-4 py-3.5 text-left text-sm font-bold transition last:border-b-0"
              :class="
                activeTab === item.key
                  ? 'bg-[#f7ecee] text-[#7d2d3a]'
                  : 'text-[#625a54] hover:bg-[#faf8f6]'
              "
              @click="activeTab = item.key"
            >
              <component :is="item.icon" :size="17" />
              <span>{{ item.label }}</span>
            </button>
          </div>
        </aside>

        <div class="min-w-0">
          <Transition name="account-fade" mode="out-in">
            <section v-if="activeTab === 'overview'" key="overview" class="space-y-5">
              <div class="rounded-2xl border border-[#e4ded8] bg-white p-5 sm:p-6">
                <div
                  class="flex flex-wrap items-end justify-between gap-3 border-b border-[#eee9e4] pb-4"
                >
                  <div>
                    <p class="text-[10px] font-black uppercase tracking-[0.16em] text-[#9a8f87]">
                      Tổng quan
                    </p>
                    <h2 class="mt-1 text-xl font-black">Thông tin khách hàng</h2>
                  </div>
                  <span class="text-xs text-[#938980]">{{ profile.maKhachHang || '---' }}</span>
                </div>

                <div class="mt-5 grid gap-x-8 gap-y-5 sm:grid-cols-2">
                  <InfoItem label="Họ và tên" :value="profile.hoTen" />
                  <InfoItem label="Tên tài khoản" :value="profile.tenTaiKhoan" />
                  <InfoItem label="Email" :value="profile.email" />
                  <InfoItem label="Số điện thoại" :value="profile.soDienThoai" />
                  <InfoItem label="Ngày sinh" :value="formatDate(profile.ngaySinh)" />
                  <InfoItem label="Giới tính" :value="profile.gioiTinh ? 'Nam' : 'Nữ'" />
                </div>

                <div class="mt-6 flex flex-wrap gap-2 border-t border-[#eee9e4] pt-5">
                  <button
                    type="button"
                    class="rounded-xl border border-[#dcd5ce] px-4 py-2.5 text-sm font-bold text-[#5f5751] transition hover:border-[#b98b92] hover:text-[#7d2d3a]"
                    @click="activeTab = 'profile'"
                  >
                    Cập nhật thông tin
                  </button>
                  <button
                    type="button"
                    class="rounded-xl border border-[#dcd5ce] px-4 py-2.5 text-sm font-bold text-[#5f5751] transition hover:border-[#b98b92] hover:text-[#7d2d3a]"
                    @click="activeTab = 'addresses'"
                  >
                    Quản lý địa chỉ
                  </button>
                </div>
              </div>
            </section>

            <section
              v-else-if="activeTab === 'profile'"
              key="profile-edit"
              class="rounded-2xl border border-[#e4ded8] bg-white p-5 sm:p-6"
            >
              <div class="border-b border-[#eee9e4] pb-4">
                <p class="text-[10px] font-black uppercase tracking-[0.16em] text-[#9a8f87]">
                  Hồ sơ cá nhân
                </p>
                <h2 class="mt-1 text-xl font-black">Cập nhật thông tin</h2>
              </div>

              <div class="mt-5 grid gap-4 md:grid-cols-2">
                <Field label="Họ và tên" v-model="editForm.hoTen" />
                <Field label="Số điện thoại" v-model="editForm.soDienThoai" />
                <Field label="Email" v-model="editForm.email" type="email" />
                <Field label="Ngày sinh" v-model="editForm.ngaySinh" type="date" />
                <Field
                  label="Giới tính"
                  v-model="editForm.gioiTinh"
                  type="select"
                  :options="[
                    { label: 'Nam', value: true },
                    { label: 'Nữ', value: false },
                  ]"
                />
                <Field label="Tên tài khoản" :model-value="editForm.tenTaiKhoan" disabled />
              </div>

              <div class="mt-6 flex justify-end gap-2 border-t border-[#eee9e4] pt-5">
                <button
                  type="button"
                  class="rounded-xl border border-[#dcd5ce] px-4 py-2.5 text-sm font-bold text-[#5f5751]"
                  @click="resetEditForm"
                >
                  Hủy
                </button>
                <button
                  type="button"
                  class="inline-flex items-center gap-2 rounded-xl bg-[#7d2d3a] px-4 py-2.5 text-sm font-bold text-white transition hover:bg-[#682430] disabled:opacity-50"
                  :disabled="saving"
                  @click="saveProfile"
                >
                  <LoaderCircle v-if="saving" :size="16" class="animate-spin" />
                  {{ saving ? 'Đang lưu...' : 'Lưu thay đổi' }}
                </button>
              </div>
            </section>

            <section
              v-else
              key="addresses"
              class="rounded-2xl border border-[#e4ded8] bg-white p-5 sm:p-6"
            >
              <div
                class="flex flex-wrap items-end justify-between gap-3 border-b border-[#eee9e4] pb-4"
              >
                <div>
                  <p class="text-[10px] font-black uppercase tracking-[0.16em] text-[#9a8f87]">
                    Địa chỉ
                  </p>
                  <h2 class="mt-1 text-xl font-black">Địa chỉ giao hàng</h2>
                </div>
                <button
                  type="button"
                  class="rounded-xl bg-[#7d2d3a] px-4 py-2.5 text-sm font-bold text-white transition hover:bg-[#682430]"
                  @click="openAddressModal()"
                >
                  Thêm địa chỉ
                </button>
              </div>

              <div v-if="addresses.length" class="mt-5 space-y-3">
                <article
                  v-for="address in addresses"
                  :key="address.id"
                  class="rounded-xl border border-[#e9e3de] p-4 transition hover:border-[#cdb2b6]"
                >
                  <div class="flex flex-col gap-3 sm:flex-row sm:items-start sm:justify-between">
                    <div class="min-w-0">
                      <div class="flex flex-wrap items-center gap-2">
                        <p class="font-black">{{ address.tenNguoiNhan || profile.hoTen }}</p>
                        <span
                          v-if="address.macDinh"
                          class="rounded-full bg-[#f7ecee] px-2 py-1 text-[10px] font-black text-[#7d2d3a]"
                          >Mặc định</span
                        >
                      </div>
                      <p class="mt-1 text-sm text-[#6f665f]">
                        {{ address.soDienThoai || profile.soDienThoai }}
                      </p>
                      <p class="mt-1.5 text-sm leading-6 text-[#877c74]">
                        {{ joinAddress(address) }}
                      </p>
                    </div>
                    <div class="flex flex-wrap gap-2">
                      <button
                        v-if="!address.macDinh"
                        type="button"
                        class="rounded-lg border border-[#ddd5ce] px-3 py-2 text-xs font-bold text-[#6a615b] hover:text-[#7d2d3a]"
                        @click="setDefaultAddress(address.id)"
                      >
                        Đặt mặc định
                      </button>
                      <button
                        type="button"
                        class="rounded-lg border border-[#ddd5ce] px-3 py-2 text-xs font-bold text-[#6a615b]"
                        @click="editAddress(address)"
                      >
                        Sửa
                      </button>
                      <button
                        type="button"
                        class="rounded-lg bg-[#faf0f0] px-3 py-2 text-xs font-bold text-[#9a3a48]"
                        @click="deleteAddress(address.id)"
                      >
                        Xóa
                      </button>
                    </div>
                  </div>
                </article>
              </div>
              <div
                v-else
                class="mt-5 rounded-xl border border-dashed border-[#d8cec6] bg-[#fbfaf8] p-8 text-center"
              >
                <MapPinned :size="28" class="mx-auto text-[#988d84]" />
                <p class="mt-2 font-black">Chưa có địa chỉ giao hàng</p>
                <p class="mt-1 text-sm text-[#897e76]">Thêm một địa chỉ để thanh toán nhanh hơn.</p>
              </div>
            </section>
          </Transition>
        </div>
      </div>
    </section>

    <Transition name="modal-fade">
      <div
        v-if="showAddressEditor"
        class="fixed inset-0 z-[80] grid place-items-center bg-black/30 p-4"
        @click.self="showAddressEditor = false"
      >
        <div
          class="w-full max-w-2xl overflow-hidden rounded-2xl border border-[#e4ded8] bg-white shadow-2xl"
        >
          <div class="flex items-start justify-between border-b border-[#eee9e4] px-5 py-4">
            <div>
              <p class="text-[10px] font-black uppercase tracking-[0.16em] text-[#9a8f87]">
                Địa chỉ giao hàng
              </p>
              <h3 class="mt-1 text-lg font-black">
                {{ editingAddress ? 'Chỉnh sửa địa chỉ' : 'Thêm địa chỉ mới' }}
              </h3>
            </div>
            <button
              type="button"
              class="grid h-9 w-9 place-items-center rounded-lg bg-[#f6f3f0] text-[#675f59]"
              @click="showAddressEditor = false"
            >
              <X :size="17" />
            </button>
          </div>
          <div class="max-h-[70vh] overflow-y-auto p-5">
            <div class="grid gap-4 md:grid-cols-2">
              <Field label="Tên người nhận" v-model="addressForm.tenNguoiNhan" />
              <Field label="Số điện thoại" v-model="addressForm.soDienThoai" />
              <Field
                label="Tỉnh / Thành phố"
                v-model="addressForm.thanhPho"
                type="select"
                :options="provinces.map((p) => ({ label: p.name, value: p.name }))"
                @update:modelValue="loadDistricts"
              />
              <Field
                label="Quận / Huyện"
                v-model="addressForm.quan"
                type="select"
                :options="districts.map((d) => ({ label: d.name, value: d.name }))"
                @update:modelValue="loadWards"
              />
              <Field
                label="Phường / Xã"
                v-model="addressForm.phuong"
                type="select"
                :options="wards.map((w) => ({ label: w.name, value: w.name }))"
              />
              <Field label="Địa chỉ cụ thể" v-model="addressForm.diaChiCuThe" />
            </div>
          </div>
          <div class="flex justify-end gap-2 border-t border-[#eee9e4] bg-[#fbfaf8] px-5 py-4">
            <button
              type="button"
              class="rounded-xl border border-[#dcd5ce] px-4 py-2.5 text-sm font-bold text-[#5f5751]"
              @click="showAddressEditor = false"
            >
              Hủy
            </button>
            <button
              type="button"
              class="rounded-xl bg-[#7d2d3a] px-4 py-2.5 text-sm font-bold text-white"
              :disabled="addressSaving"
              @click="saveAddress"
            >
              {{ addressSaving ? 'Đang lưu...' : 'Lưu địa chỉ' }}
            </button>
          </div>
        </div>
      </div>
    </Transition>
  </main>
</template>

<script setup>
import { computed, defineComponent, h, onMounted, reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { useToast } from 'vue-toastification'
import {
  BadgeCheck,
  BookUser,
  Check,
  Crown,
  Heart,
  LoaderCircle,
  LogOut,
  MapPinned,
  Package,
  Pencil,
  Plus,
  ReceiptText,
  ShoppingBag,
  UserRound,
  WalletCards,
  X,
} from 'lucide-vue-next'
import axios from 'axios'
import {
  capNhatDiaChi,
  doiMacDinh,
  getAllDiaChi,
  themDiaChi,
  xoaDiaChi,
} from '@/service/DiaChiKhachHangService'
import { detailKhachHangService, updateKhachHangService } from '@/service/KhachHangService'

const router = useRouter()
const toast = useToast()
const activeTab = ref('overview')
const saving = ref(false)
const addresses = ref([])
const provinces = ref([])
const districts = ref([])
const wards = ref([])
const showAddressEditor = ref(false)
const addressSaving = ref(false)
const editingAddress = ref(null)
const avatarError = ref(false)
const selectedAvatar = ref(null)
const avatarPreview = ref('')

const blankProfile = {
  id: null,
  maKhachHang: '',
  hoTen: '',
  tenTaiKhoan: '',
  email: '',
  soDienThoai: '',
  ngaySinh: '',
  anh: '',
  gioiTinh: true,
  hangThanhVien: '',
  soLanMua: 0,
  tongChiTieu: 0,
  ngayTao: '',
  ngayCapNhat: '',
  ngayHetHanHang: '',
}

const profile = reactive({ ...blankProfile })
const editForm = reactive({ ...blankProfile })
const addressForm = reactive({
  tenNguoiNhan: '',
  soDienThoai: '',
  thanhPho: '',
  quan: '',
  phuong: '',
  diaChiCuThe: '',
})

const userSession = () => {
  try {
    return JSON.parse(sessionStorage.getItem('user') || 'null')
  } catch {
    return null
  }
}

const customerId = computed(() => {
  const user = userSession()
  return user?.idKhachHang || user?.khachHangId || user?.khachHang?.id || user?.id || null
})

const initials = computed(() => {
  const text = profile.hoTen || profile.tenTaiKhoan || 'VL'
  return text
    .trim()
    .split(/\s+/)
    .slice(0, 2)
    .map((part) => part[0])
    .join('')
    .toUpperCase()
})

const avatarUrl = computed(() => {
  if (avatarPreview.value) return avatarPreview.value
  if (avatarError.value || !profile.anh) return ''
  return `http://localhost:8080/khachhang/uploads/${profile.anh}`
})

const memberProgress = computed(() =>
  Math.min(100, Math.max(8, Number(profile.soLanMua || 0) * 12.5)),
)

const quickActions = [
  { label: 'Đơn mua', description: 'Xem trạng thái các đơn hàng', to: '/donhang', icon: Package },
  { label: 'Yêu thích', description: 'Sản phẩm bạn đã lưu', to: '/yeuthich', icon: Heart },
  {
    label: 'Giỏ hàng',
    description: 'Kiểm tra sản phẩm đang chọn',
    to: '/giohang',
    icon: ShoppingBag,
  },
  { label: 'Đổi thông tin', description: 'Cập nhật hồ sơ cá nhân', to: '#profile', icon: BookUser },
]

const tabs = [
  { key: 'overview', label: 'Tổng quan', icon: ReceiptText },
  { key: 'profile', label: 'Thông tin cá nhân', icon: UserRound },
  { key: 'addresses', label: 'Địa chỉ giao hàng', icon: MapPinned },
]

const stats = computed(() => [
  {
    label: 'Tổng đơn',
    value: profile.soLanMua || 0,
    note: 'Số lần mua hàng',
    icon: Package,
    tone: 'bg-[#f7efe7] text-[#8a4c57]',
  },
  {
    label: 'Chi tiêu',
    value: formatMoney(profile.tongChiTieu),
    note: 'Tổng giá trị mua sắm',
    icon: WalletCards,
    tone: 'bg-[#f8f0df] text-[#9c6c2f]',
  },
  {
    label: 'Hạng',
    value: profile.hangThanhVien || 'Mới',
    note: 'Cấp thành viên hiện tại',
    icon: Crown,
    tone: 'bg-[#f2e9df] text-[#8a6049]',
  },
  {
    label: 'Điểm / Xu',
    value: Number(profile.soDuXu || 0).toLocaleString('vi-VN'),
    note: 'Số dư đang có',
    icon: BadgeCheck,
    tone: 'bg-[#f4ece8] text-[#7f3140]',
  },
])

const formatMoney = (value) =>
  Number(value || 0).toLocaleString('vi-VN', { style: 'currency', currency: 'VND' })
const formatDate = (value) =>
  value ? new Date(value).toLocaleDateString('vi-VN') : 'Chưa cập nhật'
const joinAddress = (address) =>
  [address.diaChiCuThe, address.phuong, address.quan, address.thanhPho]
    .filter(Boolean)
    .join(', ') || 'Chưa cập nhật địa chỉ'

const resetEditForm = () => Object.assign(editForm, JSON.parse(JSON.stringify(profile)))

const loadProfile = async () => {
  const id = customerId.value
  if (!id) {
    router.push('/login?redirect=/tai-khoan')
    return
  }
  try {
    const data = await detailKhachHangService(id)
    if (data) Object.assign(profile, data)
    if (profile.ngaySinh) profile.ngaySinh = String(profile.ngaySinh).split('T')[0]
    resetEditForm()
    avatarPreview.value = ''
    sessionStorage.setItem(
      'user',
      JSON.stringify({ ...userSession(), ...profile, idKhachHang: profile.id }),
    )
  } catch (error) {
    toast.error(error?.message || 'Không thể tải thông tin tài khoản')
  }
}

const handleQuickAction = (action) => {
  if (action.to === '#profile') {
    scrollToSection('profile')
    return
  }
  router.push(action.to)
}

const saveProfile = async () => {
  if (
    !profile.id ||
    !editForm.hoTen?.trim() ||
    !editForm.email?.trim() ||
    !editForm.soDienThoai?.trim()
  ) {
    toast.warning('Vui lòng nhập đầy đủ họ tên, email và số điện thoại.')
    return
  }
  saving.value = true
  try {
    await updateKhachHangService(profile.id, { ...editForm }, selectedAvatar.value)
    await loadProfile()
    toast.success('Cập nhật thông tin tài khoản thành công')
    selectedAvatar.value = null
    avatarPreview.value = ''
  } catch (error) {
    toast.error(error?.message || 'Cập nhật tài khoản thất bại')
  } finally {
    saving.value = false
  }
}

const handleAvatarChange = (event) => {
  const file = event.target.files?.[0]
  if (!file) return
  if (file.size > 2 * 1024 * 1024) {
    toast.warning('Ảnh không được vượt quá 2MB')
    return
  }
  selectedAvatar.value = file
  avatarPreview.value = URL.createObjectURL(file)
  avatarError.value = false
}

const scrollToSection = (id) => {
  document.getElementById(id)?.scrollIntoView({ behavior: 'smooth', block: 'start' })
  activeTab.value = 'profile'
}

const loadAddresses = async () => {
  try {
    const data = await getAllDiaChi()
    addresses.value = Array.isArray(data) ? data : []
  } catch (error) {
    console.warn(error)
  }
}

const loadProvinces = async () => {
  try {
    const { data } = await axios.get('https://provinces.open-api.vn/api/p/')
    provinces.value = Array.isArray(data) ? data : []
  } catch (error) {
    console.warn(error)
  }
}

const loadDistricts = async (provinceName) => {
  const province = provinces.value.find((p) => p.name === provinceName)
  addressForm.quan = ''
  addressForm.phuong = ''
  districts.value = []
  wards.value = []
  if (!province) return
  try {
    const { data } = await axios.get(`https://provinces.open-api.vn/api/p/${province.code}?depth=2`)
    districts.value = data?.districts || []
  } catch (error) {
    console.warn(error)
  }
}

const loadWards = async (districtName) => {
  const district = districts.value.find((d) => d.name === districtName)
  addressForm.phuong = ''
  wards.value = []
  if (!district) return
  try {
    const { data } = await axios.get(`https://provinces.open-api.vn/api/d/${district.code}?depth=2`)
    wards.value = data?.wards || []
  } catch (error) {
    console.warn(error)
  }
}

const openAddressModal = () => {
  if (addresses.value.length >= 3) {
    toast.warning('Bạn chỉ được lưu tối đa 3 địa chỉ')
    return
  }
  editingAddress.value = null
  Object.assign(addressForm, {
    tenNguoiNhan: profile.hoTen || '',
    soDienThoai: profile.soDienThoai || '',
    thanhPho: '',
    quan: '',
    phuong: '',
    diaChiCuThe: '',
  })
  districts.value = []
  wards.value = []
  showAddressEditor.value = true
}

const editAddress = async (address) => {
  editingAddress.value = address
  Object.assign(addressForm, {
    tenNguoiNhan: address.tenNguoiNhan || profile.hoTen || '',
    soDienThoai: address.soDienThoai || profile.soDienThoai || '',
    thanhPho: address.thanhPho || '',
    quan: address.quan || '',
    phuong: address.phuong || '',
    diaChiCuThe: address.diaChiCuThe || '',
  })
  await loadDistricts(addressForm.thanhPho)
  const currentDistrict = districts.value.find((d) => d.name === addressForm.quan)
  if (currentDistrict) {
    const currentName = currentDistrict.name
    await loadWards(currentName)
    addressForm.quan = currentName
    addressForm.phuong = address.phuong || ''
  }
  showAddressEditor.value = true
}

const saveAddress = async () => {
  if (
    !addressForm.thanhPho ||
    !addressForm.quan ||
    !addressForm.phuong ||
    !addressForm.diaChiCuThe?.trim()
  ) {
    toast.warning('Vui lòng nhập đầy đủ địa chỉ.')
    return
  }
  addressSaving.value = true
  try {
    if (editingAddress.value) await capNhatDiaChi(editingAddress.value.id, { ...addressForm })
    else await themDiaChi({ ...addressForm })
    await loadAddresses()
    showAddressEditor.value = false
    toast.success(editingAddress.value ? 'Đã cập nhật địa chỉ' : 'Đã thêm địa chỉ')
  } catch (error) {
    toast.error(error?.message || 'Không thể lưu địa chỉ')
  } finally {
    addressSaving.value = false
  }
}

const setDefaultAddress = async (id) => {
  try {
    await doiMacDinh(id)
    await loadAddresses()
    toast.success('Đã đặt địa chỉ mặc định')
  } catch (error) {
    toast.error(error?.message || 'Không thể đổi địa chỉ mặc định')
  }
}

const deleteAddress = async (id) => {
  if (!window.confirm('Bạn có chắc muốn xóa địa chỉ này?')) return
  try {
    await xoaDiaChi(id)
    await loadAddresses()
    toast.success('Đã xóa địa chỉ')
  } catch (error) {
    toast.error(error?.message || 'Không thể xóa địa chỉ')
  }
}

const Field = defineComponent({
  props: {
    label: { type: String, required: true },
    modelValue: [String, Number, Boolean],
    type: { type: String, default: 'text' },
    options: { type: Array, default: () => [] },
    disabled: Boolean,
  },
  emits: ['update:modelValue'],
  setup(props, { emit }) {
    return () =>
      h('label', { class: 'flex flex-col gap-2' }, [
        h(
          'span',
          { class: 'text-xs font-black uppercase tracking-[0.12em] text-[#89756a]' },
          props.label,
        ),
        props.type === 'select'
          ? h(
              'select',
              {
                class:
                  'h-12 rounded-2xl border border-[#decfc3] bg-[#fcfaf7] px-4 text-sm font-semibold text-[#3e322b] outline-none transition focus:border-[#9d5e63] focus:ring-4 focus:ring-[#9d5e63]/10 disabled:cursor-not-allowed disabled:bg-[#f4eee8]',
                value: props.modelValue,
                disabled: props.disabled,
                onChange: (event) =>
                  emit(
                    'update:modelValue',
                    event.target.value === 'true'
                      ? true
                      : event.target.value === 'false'
                        ? false
                        : event.target.value,
                  ),
              },
              props.options.map((option) => h('option', { value: option.value }, option.label)),
            )
          : h('input', {
              class:
                'h-12 rounded-2xl border border-[#decfc3] bg-[#fcfaf7] px-4 text-sm font-semibold text-[#3e322b] outline-none transition focus:border-[#9d5e63] focus:ring-4 focus:ring-[#9d5e63]/10 disabled:cursor-not-allowed disabled:bg-[#f4eee8]',
              type: props.type,
              value: props.modelValue,
              disabled: props.disabled,
              onInput: (event) => emit('update:modelValue', event.target.value),
            }),
      ])
  },
})

const InfoItem = defineComponent({
  props: { label: String, value: String },
  setup(props) {
    return () =>
      h('div', { class: 'border-b border-[#f0e7df] pb-4' }, [
        h(
          'p',
          { class: 'text-[11px] font-black uppercase tracking-[0.12em] text-[#9b877b]' },
          props.label,
        ),
        h(
          'p',
          { class: 'mt-1.5 text-sm font-bold text-[#3a2f29] break-words' },
          props.value || 'Chưa cập nhật',
        ),
      ])
  },
})

onMounted(async () => {
  await Promise.all([loadProfile(), loadAddresses(), loadProvinces()])
})
</script>

<style scoped>
.account-fade-enter-active,
.account-fade-leave-active {
  transition:
    opacity 0.18s ease,
    transform 0.18s ease;
}
.account-fade-enter-from,
.account-fade-leave-to {
  opacity: 0;
  transform: translateY(5px);
}
.modal-fade-enter-active,
.modal-fade-leave-active {
  transition: opacity 0.18s ease;
}
.modal-fade-enter-from,
.modal-fade-leave-to {
  opacity: 0;
}
@media (prefers-reduced-motion: reduce) {
  .account-fade-enter-active,
  .account-fade-leave-active,
  .modal-fade-enter-active,
  .modal-fade-leave-active {
    transition: none !important;
  }
}
</style>
