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
  loaiGiamGia: 'phan_tram',
  giaTriGiam: 0,
  giaTriGiamToiDa: null,
  ngayBatDau: '',
  ngayKetThuc: '',
  moTa: '',
})

watch(
  () => props.show,
  (value) => {
    if (value) {
      if (props.isEdit) {
        form.value = {
          ...props.dataEdit,

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

const submit = () => {
  const payload = {
    ...form.value,

    ngayBatDau: form.value.ngayBatDau ? `${form.value.ngayBatDau}T00:00:00Z` : null,

    ngayKetThuc: form.value.ngayKetThuc ? `${form.value.ngayKetThuc}T23:59:59Z` : null,
  }

  emit('success', payload)
}
</script>

<template>
  <div v-if="show" class="fixed inset-0 bg-black/40 flex items-center justify-center z-50">
    <div class="bg-white w-[650px] rounded-xl shadow-xl p-6">
      <div class="flex justify-between items-center mb-5">
        <h2 class="text-xl font-bold text-slate-800">Thêm đợt giảm giá</h2>

        <button @click="$emit('close')" class="text-slate-400 hover:text-red-500 text-xl">×</button>
      </div>

      <div class="grid grid-cols-2 gap-4">
        <div>
          <label class="text-sm"> Tên đợt </label>

          <input
            v-model="form.tenDotGiamGia"
            class="w-full border rounded-lg px-3 py-2"
            placeholder="Flash Sale"
          />
        </div>

        <div>
          <label class="text-sm"> Loại giảm </label>

          <select v-model="form.loaiGiamGia" class="w-full border rounded-lg px-3 py-2">
            <option value="phan_tram">Phần trăm</option>

            <option value="tien_mat">Tiền mặt</option>
          </select>
        </div>

        <div>
          <label class="text-sm"> Giá trị giảm </label>

          <input
            type="number"
            v-model="form.giaTriGiam"
            class="w-full border rounded-lg px-3 py-2"
          />
        </div>

        <div>
          <label class="text-sm"> Giảm tối đa </label>

          <input
            type="number"
            v-model="form.giaTriGiamToiDa"
            class="w-full border rounded-lg px-3 py-2"
          />
        </div>

        <div>
          <label class="text-sm"> Ngày bắt đầu </label>

          <input type="date" v-model="form.ngayBatDau" class="w-full border rounded-lg px-3 py-2" />
        </div>

        <div>
          <label class="text-sm"> Ngày kết thúc </label>

          <input
            type="date"
            v-model="form.ngayKetThuc"
            class="w-full border rounded-lg px-3 py-2"
          />
        </div>

        <div class="col-span-2">
          <label class="text-sm"> Mô tả </label>

          <textarea
            v-model="form.moTa"
            rows="3"
            class="w-full border rounded-lg px-3 py-2"
          ></textarea>
        </div>
      </div>

      <div class="flex justify-end gap-3 mt-6">
        <button @click="$emit('close')" class="px-4 py-2 rounded-lg border">Hủy</button>

        <button @click="submit" class="px-4 py-2 rounded-lg bg-indigo-600 text-white">
          {{ isEdit ? 'Cập nhật' : 'Lưu' }}
        </button>
      </div>
    </div>
  </div>
</template>
