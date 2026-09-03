<!-- components/AddressModal.vue -->
<script setup>
import { ref, watch, onMounted, nextTick } from 'vue'
import { X, LoaderCircle, Navigation } from 'lucide-vue-next'
import L from 'leaflet'
import 'leaflet/dist/leaflet.css'

// Fix lỗi icon mặc định của Leaflet trong Vite/Vue
import markerIcon from 'leaflet/dist/images/marker-icon.png'
import markerShadow from 'leaflet/dist/images/marker-shadow.png'

const DefaultIcon = L.icon({
  iconUrl: markerIcon,
  shadowUrl: markerShadow,
  iconSize: [25, 41],
  iconAnchor: [12, 41],
})
L.Marker.prototype.options.icon = DefaultIcon

const props = defineProps({
  isOpen: Boolean,
  isLoggedIn: Boolean,
  editingAddress: [Object, null],
  addressForm: {
    type: Object,
    required: true,
  },
  provinces: {
    type: Array,
    default: () => [],
  },
  districts: {
    type: Array,
    default: () => [],
  },
  wards: {
    type: Array,
    default: () => [],
  },
  selectedProvince: [Object, null],
  selectedDistrict: [Object, null],
  selectedWard: [Object, null],
  loading: Boolean,
})

const emit = defineEmits([
  'close',
  'save',
  'get-location',
  'update:selectedProvince',
  'update:selectedDistrict',
  'update:selectedWard',
  'province-change',
  'district-change',
  'ward-change',
])

const errors = ref({})
let map = null
let marker = null

// Khởi tạo bản đồ khi modal mở
watch(
  () => props.isOpen,
  async (isOpen) => {
    if (isOpen) {
      await nextTick()
      setTimeout(() => {
        initMap()
      }, 100)
    } else {
      if (map) {
        map.remove()
        map = null
        marker = null
      }
    }
  },
)

// Lắng nghe khi tọa độ thay đổi (ví dụ khi bấm nút "Dùng vị trí hiện tại")
watch(
  () => [props.addressForm.latitude, props.addressForm.longitude],
  ([lat, lng]) => {
    if (lat && lng && map) {
      const latLng = [lat, lng]
      map.setView(latLng, 16)
      if (marker) {
        marker.setLatLng(latLng)
      } else {
        marker = L.marker(latLng).addTo(map)
      }
    }
  },
)

const initMap = () => {
  const defaultLat = props.addressForm.latitude || 21.0285 // Mặc định Hà Nội
  const defaultLng = props.addressForm.longitude || 105.8542

  const container = L.DomUtil.get('map')
  if (container != null) {
    container._leaflet_id = null
  }

  if (!map) {
    map = L.map('map').setView([defaultLat, defaultLng], 15)

    // Sử dụng CartoDB Tile Layer không bị rate-limit như OpenStreetMap
    L.tileLayer('https://{s}.basemaps.cartocdn.com/rastertiles/voyager/{z}/{x}/{y}{r}.png', {
      maxZoom: 19,
      subdomains: 'abcd',
      attribution: '&copy; OpenStreetMap contributors &copy; CARTO',
    }).addTo(map)

    marker = L.marker([defaultLat, defaultLng]).addTo(map)

    // Cho phép click vào bản đồ để chọn tọa độ mới
    map.on('click', (e) => {
      const { lat, lng } = e.latlng
      props.addressForm.latitude = lat
      props.addressForm.longitude = lng
      if (marker) {
        marker.setLatLng([lat, lng])
      } else {
        marker = L.marker([lat, lng]).addTo(map)
      }
    })
  } else {
    map.invalidateSize()
  }
}

const validateForm = () => {
  errors.value = {}
  let isValid = true

  if (!props.addressForm.tenNguoiNhan?.trim()) {
    errors.value.tenNguoiNhan = 'Vui lòng nhập họ và tên'
    isValid = false
  }

  const phoneRegex = /(84|0[35789])+([0-9]{8})\b/
  if (!props.addressForm.soDienThoai?.trim()) {
    errors.value.soDienThoai = 'Vui lòng nhập số điện thoại'
    isValid = false
  } else if (!phoneRegex.test(props.addressForm.soDienThoai)) {
    errors.value.soDienThoai = 'Số điện thoại không hợp lệ'
    isValid = false
  }

  if (!props.selectedProvince) {
    errors.value.province = 'Vui lòng chọn Tỉnh/Thành phố'
    isValid = false
  }

  if (!props.selectedDistrict) {
    errors.value.district = 'Vui lòng chọn Quận/Huyện'
    isValid = false
  }

  if (!props.selectedWard) {
    errors.value.ward = 'Vui lòng chọn Phường/Xã'
    isValid = false
  }

  if (!props.addressForm.diaChiCuThe?.trim()) {
    errors.value.diaChiCuThe = 'Vui lòng nhập địa chỉ cụ thể'
    isValid = false
  }

  return isValid
}

const handleSave = () => {
  if (validateForm()) {
    emit('save')
  }
}
</script>

<template>
  <Teleport to="body">
    <div
      v-if="isOpen && isLoggedIn"
      class="fixed inset-0 z-50 flex items-center justify-center bg-slate-950/60 p-4 backdrop-blur-sm"
      @click.self="emit('close')"
    >
      <div
        class="max-h-[90vh] w-full max-w-4xl overflow-y-auto rounded-2xl border border-white/70 bg-white shadow-2xl"
      >
        <!-- Header modal -->
        <div
          class="flex items-center justify-between border-b border-slate-100 px-5 pt-4 pb-2.5 sm:px-6"
        >
          <div>
            <h2 class="mt-0.5 text-xl font-black text-slate-900">
              {{ editingAddress ? 'Cập nhật địa chỉ' : 'Thêm địa chỉ mới' }}
            </h2>
          </div>
          <button
            type="button"
            class="grid h-9 w-9 place-items-center rounded-xl bg-slate-100 text-slate-500 transition hover:bg-red-50 hover:text-red-600"
            @click="emit('close')"
          >
            <X :size="18" />
          </button>
        </div>

        <!-- Body modal -->
        <div class="grid gap-4 px-5 pt-3 pb-5 sm:px-6 sm:pt-3.5 sm:pb-6 lg:grid-cols-[1fr_340px]">
          <div class="space-y-4">
            <div class="grid gap-4 md:grid-cols-2">
              <label class="block">
                <span class="mb-1.5 block text-xs font-bold text-slate-700">Họ và tên</span>
                <input
                  v-model.trim="addressForm.tenNguoiNhan"
                  placeholder="Họ và tên người nhận"
                  class="w-full rounded-xl border border-slate-300 px-3.5 py-2.5 text-sm outline-none transition focus:border-indigo-500 focus:ring-4 focus:ring-indigo-100"
                />
                <span v-if="errors.tenNguoiNhan" class="mt-1 block text-xs text-rose-500">{{
                  errors.tenNguoiNhan
                }}</span>
              </label>

              <label class="block">
                <span class="mb-1.5 block text-xs font-bold text-slate-700">Số điện thoại</span>
                <input
                  v-model.trim="addressForm.soDienThoai"
                  inputmode="numeric"
                  maxlength="11"
                  placeholder="09xxxxxxxx"
                  class="w-full rounded-xl border border-slate-300 px-3.5 py-2.5 text-sm outline-none transition focus:border-indigo-500 focus:ring-4 focus:ring-indigo-100"
                />
                <span v-if="errors.soDienThoai" class="mt-1 block text-xs text-rose-500">{{
                  errors.soDienThoai
                }}</span>
              </label>
            </div>

            <label class="block">
              <span class="mb-1.5 block text-xs font-bold text-slate-700">Tỉnh / Thành phố</span>
              <select
                :value="selectedProvince?.ProvinceID || ''"
                @change="
                  (e) => {
                    const found = provinces.find((p) => p.ProvinceID == e.target.value)
                    emit('update:selectedProvince', found || null)
                    emit('province-change', found || null)
                    errors.province = ''
                  }
                "
                class="w-full rounded-xl border border-slate-300 p-2.5 text-sm outline-none transition focus:border-indigo-500 focus:ring-4 focus:ring-indigo-100"
              >
                <option :value="''">Chọn tỉnh/thành phố</option>
                <option v-for="p in provinces" :key="p.ProvinceID" :value="p.ProvinceID">
                  {{ p.ProvinceName }}
                </option>
              </select>
              <span v-if="errors.province" class="mt-1 block text-xs text-rose-500">{{
                errors.province
              }}</span>
            </label>

            <div class="grid gap-4 md:grid-cols-2">
              <label class="block">
                <span class="mb-1.5 block text-xs font-bold text-slate-700">Quận / Huyện</span>
                <select
                  :value="selectedDistrict?.DistrictID || ''"
                  @change="
                    (e) => {
                      const found = districts.find((d) => d.DistrictID == e.target.value)
                      emit('update:selectedDistrict', found || null)
                      emit('district-change', found || null)
                      errors.district = ''
                    }
                  "
                  class="w-full rounded-xl border border-slate-300 p-2.5 text-sm outline-none transition focus:border-indigo-500 focus:ring-4 focus:ring-indigo-100"
                >
                  <option :value="''">Chọn quận/huyện</option>
                  <option v-for="d in districts" :key="d.DistrictID" :value="d.DistrictID">
                    {{ d.DistrictName }}
                  </option>
                </select>
                <span v-if="errors.district" class="mt-1 block text-xs text-rose-500">{{
                  errors.district
                }}</span>
              </label>

              <label class="block">
                <span class="mb-1.5 block text-xs font-bold text-slate-700">Phường / Xã</span>
                <!-- components/AddressModal.vue -->
                <select
                  :value="selectedWard?.WardCode || ''"
                  @change="
                    (e) => {
                      const found = wards.find((w) => w.WardCode == e.target.value)
                      emit('update:selectedWard', found || null)
                      emit('ward-change', found || null) /* <-- Thêm dòng này */
                      errors.ward = ''
                    }
                  "
                  class="w-full rounded-xl border border-slate-300 p-2.5 text-sm outline-none transition focus:border-indigo-500 focus:ring-4 focus:ring-indigo-100"
                >
                  <option :value="''">Chọn phường/xã</option>
                  <option v-for="w in wards" :key="w.WardCode" :value="w.WardCode">
                    {{ w.WardName }}
                  </option>
                </select>
                <span v-if="errors.ward" class="mt-1 block text-xs text-rose-500">{{
                  errors.ward
                }}</span>
              </label>
            </div>

            <label class="block">
              <span class="mb-1.5 block text-xs font-bold text-slate-700">Địa chỉ cụ thể</span>
              <textarea
                v-model.trim="addressForm.diaChiCuThe"
                rows="2"
                placeholder="Số nhà, tên đường..."
                class="w-full resize-none rounded-xl border border-slate-300 p-3 text-sm outline-none transition focus:border-indigo-500 focus:ring-4 focus:ring-indigo-100"
              ></textarea>
              <span v-if="errors.diaChiCuThe" class="mt-1 block text-xs text-rose-500">{{
                errors.diaChiCuThe
              }}</span>
            </label>

            <label
              class="flex cursor-pointer items-center gap-3 rounded-xl border border-slate-200 bg-slate-50 p-3.5"
            >
              <input
                v-model="addressForm.macDinh"
                type="checkbox"
                class="h-4 w-4 accent-indigo-600"
              />
              <span class="text-xs font-semibold text-slate-600">Đặt làm địa chỉ mặc định</span>
            </label>
          </div>

          <div class="flex flex-col gap-3">
            <div
              id="map"
              class="h-[260px] overflow-hidden rounded-xl border-2 border-slate-200 bg-slate-100 z-10"
            ></div>

            <button
              type="button"
              class="flex w-full items-center justify-center gap-2 rounded-xl bg-indigo-50 px-4 py-2.5 text-sm font-bold text-indigo-700 transition hover:bg-indigo-100"
              @click="emit('get-location')"
            >
              <LoaderCircle v-if="loading" :size="16" class="animate-spin" />
              <Navigation v-else :size="16" />
              {{ loading ? 'Đang xác định...' : 'Dùng vị trí hiện tại' }}
            </button>

            <div class="mt-auto grid grid-cols-2 gap-2.5 pt-2">
              <button
                type="button"
                class="rounded-xl border border-slate-200 px-4 py-2.5 text-sm font-bold text-slate-600 transition hover:bg-slate-50"
                @click="emit('close')"
              >
                Hủy
              </button>
              <button
                type="button"
                class="rounded-xl bg-indigo-600 px-4 py-2.5 text-sm font-bold text-white shadow-lg shadow-indigo-100 transition hover:bg-indigo-700"
                @click="handleSave"
              >
                Lưu địa chỉ
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </Teleport>
</template>
