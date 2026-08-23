<script setup>
import { MapPin, Plus, Check, Trash2, Edit3, User, Phone } from 'lucide-vue-next'

defineProps({
  isLoggedIn: Boolean,
  addresses: Array,
  selectedAddressId: [Number, String],
  addressForm: Object,
  provinces: Array,
  districts: Array,
  wards: Array,
  selectedProvince: [Number, String],
  selectedDistrict: [Number, String],
  selectedWard: [Number, String],
  showAllAddresses: Boolean,
  loading: Boolean,
})

defineEmits([
  'select-address',
  'open-add',
  'open-edit',
  'delete',
  'set-default',
  'update:addressForm',
  'province-change',
  'district-change',
  'ward-change',
  'get-location',
])
</script>

<template>
  <section
    class="rounded-[28px] border border-slate-200/80 bg-white p-5 shadow-xl shadow-slate-200/40 sm:p-7"
  >
    <div class="flex items-start justify-between gap-4">
      <div class="flex items-start gap-3">
        <span
          class="grid h-12 w-12 shrink-0 place-items-center rounded-2xl bg-rose-50 text-rose-600"
        >
          <MapPin :size="24" />
        </span>
        <div>
          <p class="text-xs font-black uppercase tracking-[0.16em] text-rose-600">Bước 1</p>
          <h2 class="mt-1 text-2xl font-black tracking-tight">Địa chỉ nhận hàng</h2>
        </div>
      </div>

      <button
        v-if="isLoggedIn"
        type="button"
        class="inline-flex items-center gap-2 rounded-xl bg-indigo-50 px-4 py-2 text-xs font-bold text-indigo-700 transition hover:bg-indigo-100"
        @click="$emit('open-add')"
      >
        <Plus :size="15" />
        Thêm địa chỉ mới
      </button>
    </div>

    <!-- KHÁCH ĐÃ ĐĂNG NHẬP: Danh sách địa chỉ -->
    <div v-if="isLoggedIn" class="mt-6 space-y-4">
      <div
        v-if="addresses.length === 0"
        class="rounded-2xl border border-dashed border-slate-200 p-6 text-center text-sm text-slate-500"
      >
        Bạn chưa có địa chỉ nào được lưu. Vui lòng thêm địa chỉ mới để nhận hàng.
      </div>

      <div
        v-for="addr in addresses"
        :key="addr.id"
        @click="$emit('select-address', addr.id)"
        class="group relative cursor-pointer rounded-2xl border p-4 sm:p-5 transition-all"
        :class="
          selectedAddressId === addr.id
            ? 'border-indigo-600 bg-indigo-50/40 shadow-md shadow-indigo-100'
            : 'border-slate-200/80 bg-white hover:border-slate-300 hover:shadow-sm'
        "
      >
        <div class="flex flex-col gap-3 sm:flex-row sm:items-start sm:justify-between">
          <div class="flex items-start gap-3">
            <div
              class="mt-0.5 flex h-5 w-5 shrink-0 items-center justify-center rounded-full border"
              :class="
                selectedAddressId === addr.id
                  ? 'border-indigo-600 bg-indigo-600 text-white'
                  : 'border-slate-300 bg-white'
              "
            >
              <Check v-if="selectedAddressId === addr.id" :size="12" />
            </div>

            <div>
              <div class="flex flex-wrap items-center gap-2">
                <!-- Sửa hoTen thành tenNguoiNhan -->
                <span class="font-bold text-slate-900">{{ addr.tenNguoiNhan }}</span>
                <span class="text-slate-300">|</span>
                <span class="text-sm font-semibold text-slate-600">{{ addr.soDienThoai }}</span>
                <span
                  v-if="addr.macDinh"
                  class="rounded-full bg-indigo-100 px-2 py-0.5 text-[10px] font-bold text-indigo-700"
                >
                  Mặc định
                </span>
              </div>
              <!-- Đồng bộ các trường địa chỉ cho khớp với xacnhan.vue -->
              <p class="mt-1.5 text-sm leading-relaxed text-slate-600">
                {{ addr.diaChiCuThe }}, {{ addr.phuong }}, {{ addr.quan }},
                {{ addr.thanhPho }}
              </p>
            </div>
          </div>

          <div class="flex items-center gap-2 self-end sm:self-start">
            <button
              type="button"
              class="rounded-lg p-2 text-slate-400 hover:bg-slate-100 hover:text-slate-700 transition"
              @click.stop="$emit('open-edit', addr)"
            >
              <Edit3 :size="16" />
            </button>
            <button
              type="button"
              class="rounded-lg p-2 text-slate-400 hover:bg-rose-50 hover:text-rose-600 transition"
              @click.stop="$emit('delete', addr.id)"
            >
              <Trash2 :size="16" />
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- KHÁCH VÃNG LAI: Form nhập trực tiếp -->
    <div v-else class="mt-6 grid gap-4 sm:grid-cols-2">
      <div>
        <label class="block text-xs font-bold text-slate-700 mb-1">Họ tên người nhận</label>
        <div class="relative">
          <span class="absolute inset-y-0 left-0 flex items-center pl-3 text-slate-400"
            ><User :size="16"
          /></span>
          <input
            type="text"
            :value="addressForm.tenNguoiNhan"
            @input="
              $emit('update:addressForm', { ...addressForm, tenNguoiNhan: $event.target.value })
            "
            placeholder="Nhập họ và tên..."
            class="w-full rounded-xl border border-slate-300 bg-slate-50 py-2.5 pl-10 pr-4 text-sm outline-none focus:border-indigo-500 focus:bg-white"
          />
        </div>
      </div>

      <div>
        <label class="block text-xs font-bold text-slate-700 mb-1">Số điện thoại</label>
        <div class="relative">
          <span class="absolute inset-y-0 left-0 flex items-center pl-3 text-slate-400"
            ><Phone :size="16"
          /></span>
          <input
            type="text"
            :value="addressForm.soDienThoai"
            @input="
              $emit('update:addressForm', { ...addressForm, soDienThoai: $event.target.value })
            "
            placeholder="Nhập số điện thoại..."
            class="w-full rounded-xl border border-slate-300 bg-slate-50 py-2.5 pl-10 pr-4 text-sm outline-none focus:border-indigo-500 focus:bg-white"
          />
        </div>
      </div>

      <div>
        <label class="block text-xs font-bold text-slate-700 mb-1">Tỉnh / Thành phố</label>
        <select
          :value="selectedProvince"
          @change="$emit('province-change', $event.target.value)"
          class="w-full rounded-xl border border-slate-300 bg-slate-50 px-3 py-2.5 text-sm outline-none focus:border-indigo-500 focus:bg-white"
        >
          <option value="">Chọn Tỉnh/Thành</option>
          <option v-for="p in provinces" :key="p.ProvinceID" :value="p.ProvinceID">
            {{ p.ProvinceName }}
          </option>
        </select>
      </div>

      <div>
        <label class="block text-xs font-bold text-slate-700 mb-1">Quận / Huyện</label>
        <select
          :value="selectedDistrict"
          @change="$emit('district-change', $event.target.value)"
          class="w-full rounded-xl border border-slate-300 bg-slate-50 px-3 py-2.5 text-sm outline-none focus:border-indigo-500 focus:bg-white"
        >
          <option value="">Chọn Quận/Huyện</option>
          <option v-for="d in districts" :key="d.DistrictID" :value="d.DistrictID">
            {{ d.DistrictName }}
          </option>
        </select>
      </div>

      <div>
        <label class="block text-xs font-bold text-slate-700 mb-1">Phường / Xã</label>
        <select
          :value="selectedWard"
          @change="$emit('ward-change', $event.target.value)"
          class="w-full rounded-xl border border-slate-300 bg-slate-50 px-3 py-2.5 text-sm outline-none focus:border-indigo-500 focus:bg-white"
        >
          <option value="">Chọn Phường/Xã</option>
          <option v-for="w in wards" :key="w.WardCode" :value="w.WardCode">{{ w.WardName }}</option>
        </select>
      </div>

      <div>
        <label class="block text-xs font-bold text-slate-700 mb-1">Địa chỉ cụ thể</label>
        <input
          type="text"
          :value="addressForm.diaChiCuThe"
          @input="$emit('update:addressForm', { ...addressForm, diaChiCuThe: $event.target.value })"
          placeholder="Số nhà, tên đường..."
          class="w-full rounded-xl border border-slate-300 bg-slate-50 px-3 py-2.5 text-sm outline-none focus:border-indigo-500 focus:bg-white"
        />
      </div>
    </div>
  </section>
</template>
