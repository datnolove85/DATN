<script setup>
import { ref, watch } from 'vue'
const props = defineProps({
  show: Boolean,
  isEdit: Boolean,
  dataEdit: Object,
})

const emit = defineEmits(['close', 'success'])

const form = ref({
  tenDotGiamGia: '',
  loaiGiamGia: 'phan_tram', // Mặc định cố định là phần trăm
  giaTriGiam: 0,
  giaTriGiamToiDa: null,
  ngayBatDau: '',
  ngayKetThuc: '',
  moTa: '',
})

const errors = ref({})

const validate = () => {
  errors.value = {}

  // Tên đợt
  if (!form.value.tenDotGiamGia.trim()) {
    errors.value.tenDotGiamGia = 'Vui lòng nhập tên đợt giảm giá.'
  }

  // Giá trị giảm
  if (form.value.giaTriGiam === '' || form.value.giaTriGiam == null) {
    errors.value.giaTriGiam = 'Vui lòng nhập mức giảm.'
  } else if (Number(form.value.giaTriGiam) < 0) {
    errors.value.giaTriGiam = 'Mức giảm không được âm.'
  } else if (Number(form.value.giaTriGiam) >= 100) {
    errors.value.giaTriGiam = 'Mức giảm phải nhỏ hơn 100%.'
  }

  // Giảm tối đa
  if (
    form.value.giaTriGiamToiDa !== null &&
    form.value.giaTriGiamToiDa !== '' &&
    Number(form.value.giaTriGiamToiDa) < 0
  ) {
    errors.value.giaTriGiamToiDa = 'Giá trị giảm tối đa không được âm.'
  }

  // Ngày bắt đầu
  if (!form.value.ngayBatDau) {
    errors.value.ngayBatDau = 'Vui lòng chọn ngày bắt đầu.'
  }

  // Ngày kết thúc
  if (!form.value.ngayKetThuc) {
    errors.value.ngayKetThuc = 'Vui lòng chọn ngày kết thúc.'
  }

  if (form.value.ngayBatDau && form.value.ngayKetThuc) {
    const start = new Date(form.value.ngayBatDau)
    const end = new Date(form.value.ngayKetThuc)

    if (end < start) {
      errors.value.ngayKetThuc = 'Ngày kết thúc phải lớn hơn hoặc bằng ngày bắt đầu.'
    }
  }

  return Object.keys(errors.value).length === 0
}
errors.value = {}
watch(
  () => props.show,
  (value) => {
    if (value) {
      if (props.isEdit) {
        form.value = {
          ...props.dataEdit,
          loaiGiamGia: 'phan_tram', // Đảm bảo luôn là phan_tram khi sửa
          ngayBatDau: props.dataEdit.ngayBatDau ? props.dataEdit.ngayBatDau.substring(0, 10) : '',
          ngayKetThuc: props.dataEdit.ngayKetThuc
            ? props.dataEdit.ngayKetThuc.substring(0, 10)
            : '',
        }
      } else {
        form.value = {
          tenDotGiamGia: '',
          loaiGiamGia: 'phan_tram',
          giaTriGiam: 0,
          giaTriGiamToiDa: null,
          ngayBatDau: '',
          ngayKetThuc: '',
          moTa: '',
        }
      }
    }
  },
)

watch(
  () => form.value.giaTriGiam,
  (val) => {
    if (val > 100) form.value.giaTriGiam = 100
    if (val < 0) form.value.giaTriGiam = 0
  },
)

watch(
  () => form.value.giaTriGiamToiDa,
  (val) => {
    if (val < 0) form.value.giaTriGiamToiDa = 0
  },
)

const submit = () => {
  if (!validate()) return

  const payload = {
    ...form.value,
    loaiGiamGia: 'phan_tram',
    ngayBatDau: form.value.ngayBatDau ? `${form.value.ngayBatDau}T00:00:00Z` : null,
    ngayKetThuc: form.value.ngayKetThuc ? `${form.value.ngayKetThuc}T23:59:59Z` : null,
  }

  emit('success', payload)
}
</script>

<template>
  <div
    v-if="show"
    class="fixed inset-0 z-50 flex items-center justify-center bg-black/40 backdrop-blur-xs p-4 overflow-y-auto"
  >
    <div
      class="bg-white w-full max-w-[600px] rounded-2xl shadow-2xl border border-slate-200 p-6 my-auto"
    >
      <!-- HEADER -->
      <div class="flex justify-between items-center mb-2 pb-2 border-b border-slate-100">
        <h2 class="text-lg font-bold text-slate-800">
          {{ isEdit ? 'Cập nhật đợt giảm giá' : 'Thêm đợt giảm giá mới' }}
        </h2>
        <button
          @click="$emit('close')"
          class="w-8 h-8 rounded-lg bg-slate-100 text-slate-500 hover:bg-slate-200 hover:text-slate-800 font-bold flex items-center justify-center transition"
        >
          ✕
        </button>
      </div>

      <!-- FORM FIELDS -->
      <div class="grid grid-cols-2 gap-x-4 gap-y-3 text-xs">
        <!-- Tên đợt -->
        <div class="col-span-2 space-y-1">
          <label class="font-medium text-slate-700"
            >Tên đợt giảm giá <span class="text-red-500">*</span></label
          >
          <input
            v-model="form.tenDotGiamGia"
            class="w-full rounded-xl border border-slate-200 bg-slate-50/50 px-3.5 py-2.5 text-sm text-slate-700 outline-none focus:bg-white focus:border-indigo-500 focus:ring-1 focus:ring-indigo-500"
            placeholder="Ví dụ: Flash Sale Hè 2026"
          />
          <p v-if="errors.tenDotGiamGia" class="mt-1 text-red-500 text-[11px]">
            {{ errors.tenDotGiamGia }}
          </p>
        </div>

        <!-- Giá trị giảm (%) -->
        <div class="space-y-1">
          <label class="font-medium text-slate-700"
            >Mức giảm (%) <span class="text-red-500">*</span></label
          >
          <div class="relative">
            <input
              type="number"
              min="0"
              max="100"
              v-model.number="form.giaTriGiam"
              class="w-full rounded-xl border border-slate-200 bg-slate-50/50 px-3.5 py-2.5 pr-8 text-sm text-slate-700 outline-none focus:bg-white focus:border-indigo-500 focus:ring-1 focus:ring-indigo-500"
              placeholder="0"
            />

            <span class="absolute right-3.5 top-1/2 -translate-y-1/2 font-bold text-slate-400">
              %
            </span>
          </div>

          <p v-if="errors.giaTriGiam" class="mt-1 text-red-500 text-[11px]">
            {{ errors.giaTriGiam }}
          </p>
        </div>

        <!-- Giảm tối đa -->
        <div class="space-y-1">
          <label class="font-medium text-slate-700">Giảm tối đa (VNĐ)</label>
          <input
            type="number"
            min="0"
            v-model.number="form.giaTriGiamToiDa"
            class="w-full rounded-xl border border-slate-200 bg-slate-50/50 px-3.5 py-2.5 text-sm text-slate-700 outline-none focus:bg-white focus:border-indigo-500 focus:ring-1 focus:ring-indigo-500"
            placeholder="Không giới hạn"
          />

          <p v-if="errors.giaTriGiamToiDa" class="mt-1 text-red-500 text-[11px]">
            {{ errors.giaTriGiamToiDa }}
          </p>
        </div>

        <!-- Ngày bắt đầu -->
        <div class="space-y-1">
          <label class="font-medium text-slate-700"
            >Ngày bắt đầu <span class="text-red-500">*</span></label
          >
          <input
            type="date"
            v-model="form.ngayBatDau"
            class="w-full rounded-xl border border-slate-200 bg-slate-50/50 px-3.5 py-2.5 text-sm text-slate-700 outline-none focus:bg-white focus:border-indigo-500 focus:ring-1 focus:ring-indigo-500"
          />

          <p v-if="errors.ngayBatDau" class="mt-1 text-red-500 text-[11px]">
            {{ errors.ngayBatDau }}
          </p>
        </div>

        <!-- Ngày kết thúc -->
        <div class="space-y-1">
          <label class="font-medium text-slate-700"
            >Ngày kết thúc <span class="text-red-500">*</span></label
          >
          <input
            type="date"
            v-model="form.ngayKetThuc"
            class="w-full rounded-xl border border-slate-200 bg-slate-50/50 px-3.5 py-2.5 text-sm text-slate-700 outline-none focus:bg-white focus:border-indigo-500 focus:ring-1 focus:ring-indigo-500"
          />
          <p v-if="errors.ngayKetThuc" class="mt-1 text-red-500 text-[11px]">
            {{ errors.ngayKetThuc }}
          </p>
        </div>

        <!-- Mô tả -->
        <div class="col-span-2 space-y-1">
          <label class="font-medium text-slate-700">Mô tả chương trình</label>
          <textarea
            v-model="form.moTa"
            rows="3"
            class="w-full rounded-xl border border-slate-200 bg-slate-50/50 px-3.5 py-2.5 text-sm text-slate-700 outline-none focus:bg-white focus:border-indigo-500 focus:ring-1 focus:ring-indigo-500 resize-none"
            placeholder="Nhập thông tin chi tiết về đợt giảm giá..."
          ></textarea>
        </div>
      </div>

      <!-- FOOTER BUTTONS -->
      <div class="flex justify-end gap-3 mt-4 pt-2 border-t border-slate-100">
        <button
          @click="$emit('close')"
          class="px-4 py-2 rounded-xl border border-slate-200 bg-white hover:bg-slate-100 text-slate-700 font-medium text-xs transition"
        >
          Hủy
        </button>

        <button
          @click="submit"
          class="px-5 py-2 rounded-xl bg-indigo-600 hover:bg-indigo-700 text-white font-medium text-xs shadow-sm transition"
        >
          {{ isEdit ? 'Cập nhật' : 'Lưu lại' }}
        </button>
      </div>
    </div>
  </div>
</template>
