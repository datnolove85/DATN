<script setup>
import { computed } from 'vue'
import {
  MapPin,
  Plus,
  Check,
  Star,
  Phone,
  Pencil,
  Trash2,
  ChevronUp,
  UserRound,
  ArrowRightLeft,
} from 'lucide-vue-next'

const props = defineProps({
  isLoggedIn: Boolean,
  addresses: Array,
  selectedAddressId: [String, Number],
  showAllAddresses: Boolean,
  addressForm: Object,
  provinces: Array,
  districts: Array,
  wards: Array,
  selectedProvince: Object,
  selectedDistrict: Object,
  selectedWard: Object,
})

const emit = defineEmits([
  'openAddAddress',
  'openEditAddress',
  'deleteAddress',
  'setDefault',
  'selectAddress',
  'update:showAllAddresses',
  'update:addressForm',
  'update:selectedProvince',
  'update:selectedDistrict',
  'update:selectedWard',
  'provinceChange',
  'districtChange',
  'wardChange',
])

// Sắp xếp tự động đưa địa chỉ được chọn hoặc địa chỉ mặc định lên trên cùng
const sortedAddresses = computed(() => {
  if (!props.addresses) return []
  return [...props.addresses].sort((a, b) => {
    if (a.id === props.selectedAddressId) return -1
    if (b.id === props.selectedAddressId) return 1
    if (a.macDinh) return -1
    if (b.macDinh) return 1
    return 0
  })
})

// Lấy ra địa chỉ đang được chọn hiện tại để hiển thị dạng rút gọn
const activeAddress = computed(() => {
  if (!props.addresses || props.addresses.length === 0) return null
  return (
    sortedAddresses.value.find((item) => item.id === props.selectedAddressId) ||
    sortedAddresses.value[0]
  )
})

// Các hàm trung gian giúp chống lỗi cú pháp khi Prettier format code lúc Ctrl + S
const handleProvinceChange = (e) => {
  emit('update:selectedProvince', e.target.value)
  emit('provinceChange', e)
}

const handleDistrictChange = (e) => {
  emit('update:selectedDistrict', e.target.value)
  emit('districtChange', e)
}

const handleWardChange = (e) => {
  emit('update:selectedWard', e.target.value)
  emit('wardChange', e)
}
</script>

<template>
  <!-- MỚI (liền mạch, không viền ngoài) -->
  <section class="bg-transparent">
    <!-- Header -->
    <div class="flex items-center justify-between mb-3">
      <h3 class="text-sm font-bold text-slate-900 flex items-center gap-2"></h3>

      <template v-if="isLoggedIn">
        <button
          v-if="addresses.length < 3"
          type="button"
          class="text-xs font-bold text-indigo-600 hover:underline transition"
          @click="$emit('openAddAddress')"
        >
          + Thêm địa chỉ
        </button>
        <span v-else class="text-[11px] font-semibold text-slate-400 italic"> Đã đầy (3/3) </span>
      </template>
    </div>

    <!-- LOGGED-IN ADDRESSES -->
    <div v-if="isLoggedIn" class="mt-3 space-y-2.5">
      <!-- 1. TRẠNG THÁI MẶC ĐỊNH: Chỉ hiện duy nhất 1 địa chỉ đang chọn kèm nút Thay đổi -->
      <template v-if="!showAllAddresses && activeAddress">
        <div
          class="group relative flex flex-col sm:flex-row sm:items-center justify-between gap-3 rounded-xl border border-indigo-100 bg-gradient-to-r from-indigo-50/30 via-white to-white p-3.5 shadow-2xs transition hover:border-indigo-300"
        >
          <div class="flex items-start gap-2.5 min-w-0">
            <span
              class="mt-0.5 grid h-7 w-7 shrink-0 place-items-center rounded-lg bg-indigo-600 text-white shadow-sm"
            >
              <MapPin :size="14" />
            </span>
            <div class="min-w-0 flex-1">
              <div class="flex flex-wrap items-center gap-2">
                <h3 class="text-xs font-bold text-slate-900 sm:text-sm">
                  {{ activeAddress.tenNguoiNhan }}
                </h3>
                <span
                  v-if="activeAddress.macDinh"
                  class="inline-flex items-center gap-1 rounded-full bg-emerald-100 px-2 py-0.5 text-[10px] font-bold uppercase tracking-wide text-emerald-700"
                >
                  <Star :size="9" />
                  Mặc định
                </span>
              </div>

              <p class="mt-0.5 flex items-center gap-1.5 text-xs font-semibold text-slate-600">
                <Phone :size="12" class="text-indigo-500" />
                {{ activeAddress.soDienThoai }}
              </p>
              <p class="mt-1 flex items-start gap-1.5 text-xs leading-relaxed text-slate-600">
                <span class="font-medium text-slate-700">Địa chỉ:</span>
                <span class="text-slate-500">
                  {{ activeAddress.diaChiCuThe }}, {{ activeAddress.phuong }},
                  {{ activeAddress.quan }}, {{ activeAddress.thanhPho }}
                </span>
              </p>
            </div>
          </div>

          <div
            class="flex shrink-0 items-center justify-end sm:justify-start pt-2 sm:pt-0 border-t sm:border-t-0 border-slate-100"
          >
            <button
              type="button"
              class="inline-flex items-center gap-1.5 rounded-xl border border-slate-200 bg-white px-3 py-1.5 text-xs font-bold text-indigo-600 shadow-2xs transition hover:bg-indigo-50 hover:border-indigo-200"
              @click="$emit('update:showAllAddresses', true)"
            >
              <ArrowRightLeft :size="13" />
              <span
                >Thay đổi địa chỉ
                <span v-if="addresses.length > 1" class="text-slate-400 font-normal"
                  >({{ addresses.length }})</span
                ></span
              >
            </button>
          </div>
        </div>
      </template>

      <!-- 2. TRẠNG THÁI MỞ RỘNG: Hiện danh sách khi người dùng bấm Thay đổi -->
      <template v-else-if="showAllAddresses && addresses.length > 0">
        <div class="space-y-2.5 animate-fadeIn">
          <div class="flex items-center justify-between px-1">
            <span class="text-xs font-bold text-slate-700">Chọn địa chỉ giao hàng khác:</span>
            <button
              type="button"
              class="inline-flex items-center gap-1 text-xs font-semibold text-slate-500 hover:text-indigo-600"
              @click="$emit('update:showAllAddresses', false)"
            >
              <ChevronUp :size="14" />
              Thu gọn
            </button>
          </div>

          <article
            v-for="item in sortedAddresses"
            :key="item.id"
            class="cursor-pointer rounded-xl border-2 p-3 transition"
            :class="
              selectedAddressId === item.id
                ? 'border-indigo-600 bg-indigo-50/40 shadow-sm'
                : 'border-slate-200 hover:border-indigo-200 hover:bg-slate-50/60'
            "
            @click="$emit('selectAddress', item.id)"
          >
            <div class="flex items-start gap-2.5">
              <span
                class="mt-0.5 grid h-4.5 w-4.5 shrink-0 place-items-center rounded-full border-2"
                :class="
                  selectedAddressId === item.id
                    ? 'border-indigo-600 bg-indigo-600 text-white'
                    : 'border-slate-300 text-transparent'
                "
              >
                <Check :size="10" />
              </span>

              <div class="min-w-0 flex-1">
                <div class="flex flex-wrap items-center gap-2">
                  <h3 class="text-xs font-bold text-slate-900 sm:text-sm">
                    {{ item.tenNguoiNhan }}
                  </h3>
                  <span
                    v-if="item.macDinh"
                    class="inline-flex items-center gap-1 rounded-full bg-emerald-100 px-2 py-0.5 text-[10px] font-bold uppercase tracking-wide text-emerald-700"
                  >
                    <Star :size="9" />
                    Mặc định
                  </span>
                </div>

                <p class="mt-0.5 flex items-center gap-1.5 text-xs font-semibold text-slate-600">
                  <Phone :size="12" class="text-indigo-500" />
                  {{ item.soDienThoai }}
                </p>
                <p class="mt-0.5 flex items-start gap-1.5 text-xs leading-relaxed text-slate-500">
                  <MapPin :size="12" class="mt-0.5 shrink-0 text-indigo-500" />
                  <span>
                    {{ item.diaChiCuThe }}, {{ item.phuong }}, {{ item.quan }}, {{ item.thanhPho }}
                  </span>
                </p>

                <div class="mt-2.5 flex flex-wrap gap-1.5">
                  <button
                    type="button"
                    class="inline-flex items-center gap-1 rounded-lg bg-white px-2 py-1 text-[11px] font-semibold text-blue-600 shadow-sm ring-1 ring-slate-200 transition hover:ring-blue-200"
                    @click.stop="$emit('openEditAddress', item)"
                  >
                    <Pencil :size="11" />
                    Sửa
                  </button>
                  <button
                    type="button"
                    class="inline-flex items-center gap-1 rounded-lg bg-white px-2 py-1 text-[11px] font-semibold text-red-600 shadow-sm ring-1 ring-slate-200 transition hover:ring-red-200"
                    @click.stop="$emit('deleteAddress', item.id)"
                  >
                    <Trash2 :size="11" />
                    Xóa
                  </button>
                  <button
                    v-if="!item.macDinh"
                    type="button"
                    class="inline-flex items-center gap-1 rounded-lg bg-white px-2 py-1 text-[11px] font-semibold text-emerald-600 shadow-sm ring-1 ring-slate-200 transition hover:ring-emerald-200"
                    @click.stop="$emit('setDefault', item.id)"
                  >
                    <Star :size="11" />
                    Đặt mặc định
                  </button>
                </div>
              </div>
            </div>
          </article>
        </div>
      </template>

      <!-- Empty state khi chưa có địa chỉ nào -->
      <div
        v-if="addresses.length === 0"
        class="rounded-xl border border-dashed border-slate-300 bg-slate-50 p-4 text-center"
      >
        <MapPin :size="26" class="mx-auto text-slate-300" />
        <p class="mt-1.5 text-xs font-semibold text-slate-600">Bạn chưa có địa chỉ giao hàng</p>
        <button
          type="button"
          class="mt-2.5 rounded-xl bg-indigo-600 px-3.5 py-1.5 text-xs font-bold text-white shadow-sm"
          @click="$emit('openAddAddress')"
        >
          Thêm địa chỉ đầu tiên
        </button>
      </div>
    </div>

    <!-- GUEST FORM (Giữ nguyên không đổi) -->
    <div v-else class="mt-3 space-y-2.5">
      <div class="grid gap-2.5 md:grid-cols-2">
        <label class="block">
          <span class="mb-1 block text-[11px] font-bold text-slate-700">Họ và tên</span>
          <span class="relative block">
            <UserRound
              :size="15"
              class="pointer-events-none absolute left-3 top-1/2 -translate-y-1/2 text-slate-400"
            />
            <input
              :value="addressForm.tenNguoiNhan"
              @input="
                $emit('update:addressForm', { ...addressForm, tenNguoiNhan: $event.target.value })
              "
              maxlength="100"
              placeholder="Nguyễn Văn A"
              class="w-full rounded-xl border border-slate-300 bg-white py-2 pl-9 pr-3 text-xs outline-none transition focus:border-indigo-500 focus:ring-2 focus:ring-indigo-100"
            />
          </span>
        </label>

        <label class="block">
          <span class="mb-1 block text-[11px] font-bold text-slate-700">Số điện thoại</span>
          <span class="relative block">
            <Phone
              :size="15"
              class="pointer-events-none absolute left-3 top-1/2 -translate-y-1/2 text-slate-400"
            />
            <input
              :value="addressForm.soDienThoai"
              @input="
                $emit('update:addressForm', { ...addressForm, soDienThoai: $event.target.value })
              "
              inputmode="numeric"
              maxlength="11"
              placeholder="09xxxxxxxx"
              class="w-full rounded-xl border border-slate-300 bg-white py-2 pl-9 pr-3 text-xs outline-none transition focus:border-indigo-500 focus:ring-2 focus:ring-indigo-100"
            />
          </span>
        </label>
      </div>

      <div class="grid gap-2.5 md:grid-cols-3">
        <label class="block">
          <span class="mb-1 block text-[11px] font-bold text-slate-700">Tỉnh / Thành phố</span>
          <select
            :value="selectedProvince"
            @change="handleProvinceChange"
            class="w-full rounded-xl border border-slate-300 bg-white p-2 text-xs outline-none transition focus:border-indigo-500 focus:ring-2 focus:ring-indigo-100"
          >
            <option :value="null">Chọn tỉnh/thành phố</option>
            <option v-for="p in provinces" :key="p.ProvinceID" :value="p">
              {{ p.ProvinceName }}
            </option>
          </select>
        </label>

        <label class="block">
          <span class="mb-1 block text-[11px] font-bold text-slate-700">Quận / Huyện</span>
          <select
            :value="selectedDistrict"
            :disabled="!selectedProvince"
            @change="handleDistrictChange"
            class="w-full rounded-xl border border-slate-300 bg-white p-2 text-xs outline-none transition focus:border-indigo-500 focus:ring-2 focus:ring-indigo-100 disabled:cursor-not-allowed disabled:bg-slate-100"
          >
            <option :value="null">Chọn quận/huyện</option>
            <option v-for="d in districts" :key="d.DistrictID" :value="d">
              {{ d.DistrictName }}
            </option>
          </select>
        </label>

        <label class="block">
          <span class="mb-1 block text-[11px] font-bold text-slate-700">Phường / Xã</span>
          <select
            :value="selectedWard"
            :disabled="!selectedDistrict"
            @change="handleWardChange"
            class="w-full rounded-xl border border-slate-300 bg-white p-2 text-xs outline-none transition focus:border-indigo-500 focus:ring-2 focus:ring-indigo-100 disabled:cursor-not-allowed disabled:bg-slate-100"
          >
            <option :value="null">Chọn phường/xã</option>
            <option v-for="w in wards" :key="w.WardCode" :value="w">
              {{ w.WardName }}
            </option>
          </select>
        </label>
      </div>

      <label class="block">
        <span class="mb-1 block text-[11px] font-bold text-slate-700">Địa chỉ cụ thể</span>
        <textarea
          :value="addressForm.diaChiCuThe"
          @input="$emit('update:addressForm', { ...addressForm, diaChiCuThe: $event.target.value })"
          rows="2"
          maxlength="250"
          placeholder="Số nhà, tên đường, tòa nhà..."
          class="w-full resize-none rounded-xl border border-slate-300 bg-white p-2.5 text-xs outline-none transition focus:border-indigo-500 focus:ring-2 focus:ring-indigo-100"
        ></textarea>
      </label>
    </div>
  </section>
</template>
