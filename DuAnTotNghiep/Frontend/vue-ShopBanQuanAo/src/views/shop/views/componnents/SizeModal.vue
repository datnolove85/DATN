<template>
  <!-- Lớp nền modal: Thêm @click.self để bấm ra ngoài là đóng tab -->
  <div
    class="fixed inset-0 bg-black/50 flex items-center justify-center p-4 z-50"
    @click.self="$emit('close')"
  >
    <!-- Thẻ chứa nội dung modal -->
    <div
      class="bg-white w-full max-w-2xl rounded-xl shadow-2xl p-6 relative flex flex-col max-h-[90vh] overflow-y-auto"
    >
      <!-- Tiêu đề Modal -->
      <div class="flex justify-between items-center border-b border-gray-100 pb-3 mb-4">
        <h2 class="text-xl font-bold text-gray-800">Bảng size & Công cụ tính size</h2>
        <button
          @click="$emit('close')"
          class="text-gray-400 hover:text-gray-600 text-2xl font-bold"
        >
          &times;
        </button>
      </div>

      <!-- Chọn loại sản phẩm -->
      <div class="mb-4">
        <label class="block text-base font-semibold text-gray-700 mb-1">Chọn loại sản phẩm:</label>
        <select
          v-model="selectedType"
          @change="handleTypeChange"
          class="w-full px-3 py-2.5 border border-blue-300 rounded-lg text-base focus:ring-2 focus:ring-emerald-500 focus:outline-none bg-white"
        >
          <option value="adult">Chung (Người lớn)</option>
          <option value="ao_nam">Áo Nam</option>
          <option value="quan_nam">Quần Nam</option>
          <option value="ao_nu">Áo Nữ</option>
          <option value="quan_nu">Quần Nữ</option>
        </select>
      </div>

      <!-- Bảng Size Tham Khảo -->
      <div class="mb-5">
        <label class="block text-base font-semibold text-gray-800 mb-2"
          >Bảng Size Tham Khảo ({{ currentCategoryName }})</label
        >
        <div class="overflow-x-auto border border-gray-200 rounded-lg">
          <table class="w-full text-center text-base border-collapse">
            <thead>
              <tr class="bg-gray-50 text-gray-700 border-b border-gray-200">
                <th
                  v-for="(col, index) in currentTableColumns"
                  :key="index"
                  class="py-3 px-3 font-semibold border-r border-gray-200 last:border-r-0"
                >
                  {{ col }}
                </th>
              </tr>
            </thead>
            <tbody>
              <tr
                v-for="item in currentTableData"
                :key="item.size"
                :class="{ 'bg-emerald-50 text-emerald-700 font-bold': matchedSize === item.size }"
                class="border-b border-gray-100 transition-colors"
              >
                <!-- Render các cột tùy theo loại sản phẩm -->
                <td class="py-3 px-3 border-r border-gray-100 font-medium">{{ item.size }}</td>
                <td v-if="selectedType === 'adult'" class="py-3 px-3 border-r border-gray-100">
                  {{ item.minH }} - {{ item.maxH }}
                </td>
                <td v-if="selectedType === 'adult'" class="py-3 px-3">
                  {{ item.minW }} - {{ item.maxW }}
                </td>

                <td
                  v-if="selectedType === 'ao_nam' || selectedType === 'ao_nu'"
                  class="py-3 px-3 border-r border-gray-100"
                >
                  {{ item.minH }} - {{ item.maxH }}
                </td>
                <td
                  v-if="selectedType === 'ao_nam' || selectedType === 'ao_nu'"
                  class="py-3 px-3 border-r border-gray-100"
                >
                  {{ item.minW }} - {{ item.maxW }}
                </td>
                <td
                  v-if="selectedType === 'ao_nam' || selectedType === 'ao_nu'"
                  class="py-3 px-3 border-r border-gray-100"
                >
                  {{ item.minChest }} - {{ item.maxChest }}
                </td>
                <td v-if="selectedType === 'ao_nam' || selectedType === 'ao_nu'" class="py-3 px-3">
                  {{ item.minWaist }} - {{ item.maxWaist }}
                </td>

                <td
                  v-if="selectedType === 'quan_nam' || selectedType === 'quan_nu'"
                  class="py-3 px-3 border-r border-gray-100"
                >
                  {{ item.minWaist }} - {{ item.maxWaist }}
                </td>
                <td
                  v-if="selectedType === 'quan_nam' || selectedType === 'quan_nu'"
                  class="py-3 px-3 border-r border-gray-100"
                >
                  {{ item.minHip }} - {{ item.maxHip }}
                </td>
                <td
                  v-if="selectedType === 'quan_nam' || selectedType === 'quan_nu'"
                  class="py-3 px-3"
                >
                  {{ item.minLen }} - {{ item.maxLen }}
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>

      <!-- Khung Nhập Liệu Tính Size -->
      <div class="bg-gray-50/60 p-4 rounded-xl border border-gray-200 mb-4">
        <label class="block text-base font-semibold text-gray-800 mb-3"
          >Tính Size Của Bạn ({{ currentCategoryName }})</label
        >

        <!-- Form cho CHUNG (Người lớn) & ÁO -->
        <template
          v-if="selectedType === 'adult' || selectedType === 'ao_nam' || selectedType === 'ao_nu'"
        >
          <div class="mb-3">
            <label class="block text-sm text-gray-700 mb-1">Chiều cao (cm):</label>
            <input
              type="number"
              v-model.number="form.height"
              @keyup.enter="calculateSize"
              placeholder="Ví dụ: 170"
              class="w-full px-3 py-2.5 bg-white border border-gray-300 rounded-lg text-base focus:ring-2 focus:ring-emerald-500 focus:outline-none"
            />
          </div>
          <div class="mb-3">
            <label class="block text-sm text-gray-700 mb-1">Cân nặng (kg):</label>
            <input
              type="number"
              step="0.1"
              v-model.number="form.weight"
              @keyup.enter="calculateSize"
              placeholder="Ví dụ: 65.5"
              class="w-full px-3 py-2.5 bg-white border border-gray-300 rounded-lg text-base focus:ring-2 focus:ring-emerald-500 focus:outline-none"
            />
          </div>
        </template>

        <!-- Form riêng cho ÁO (Thêm Ngực & Eo) -->
        <template v-if="selectedType === 'ao_nam' || selectedType === 'ao_nu'">
          <div class="mb-3">
            <label class="block text-sm text-gray-700 mb-1">Vòng ngực (cm):</label>
            <input
              type="number"
              v-model.number="form.chest"
              @keyup.enter="calculateSize"
              placeholder="Ví dụ: 92"
              class="w-full px-3 py-2.5 bg-white border border-gray-300 rounded-lg text-base focus:ring-2 focus:ring-emerald-500 focus:outline-none"
            />
          </div>
          <div class="mb-2">
            <label class="block text-sm text-gray-700 mb-1">Vòng eo (cm):</label>
            <input
              type="number"
              v-model.number="form.waist"
              @keyup.enter="calculateSize"
              placeholder="Ví dụ: 78"
              class="w-full px-3 py-2.5 bg-white border border-gray-300 rounded-lg text-base focus:ring-2 focus:ring-emerald-500 focus:outline-none"
            />
          </div>
        </template>

        <!-- Form riêng cho QUẦN (Eo, Hông, Dài quần) -->
        <template v-if="selectedType === 'quan_nam' || selectedType === 'quan_nu'">
          <div class="mb-3">
            <label class="block text-sm text-gray-700 mb-1">Vòng eo (cm):</label>
            <input
              type="number"
              v-model.number="form.waist"
              @keyup.enter="calculateSize"
              placeholder="Ví dụ: 78"
              class="w-full px-3 py-2.5 bg-white border border-gray-300 rounded-lg text-base focus:ring-2 focus:ring-emerald-500 focus:outline-none"
            />
          </div>
          <div class="mb-3">
            <label class="block text-sm text-gray-700 mb-1">Vòng hông (cm):</label>
            <input
              type="number"
              v-model.number="form.hip"
              @keyup.enter="calculateSize"
              placeholder="Ví dụ: 95"
              class="w-full px-3 py-2.5 bg-white border border-gray-300 rounded-lg text-base focus:ring-2 focus:ring-emerald-500 focus:outline-none"
            />
          </div>
          <div class="mb-2">
            <label class="block text-sm text-gray-700 mb-1"
              >{{ selectedType === 'quan_nam' ? 'Chiều dài quần' : 'Dài quần' }} (cm):</label
            >
            <input
              type="number"
              v-model.number="form.length"
              @keyup.enter="calculateSize"
              placeholder="Ví dụ: 100"
              class="w-full px-3 py-2.5 bg-white border border-gray-300 rounded-lg text-base focus:ring-2 focus:ring-emerald-500 focus:outline-none"
            />
          </div>
        </template>
      </div>

      <!-- Khung hiển thị kết quả gợi ý -->
      <div
        v-if="resultText"
        class="bg-blue-50 border border-blue-200 text-blue-900 p-3.5 rounded-lg text-base mb-4"
      >
        <p>
          Dựa trên số đo của bạn, size khuyến nghị là:
          <strong class="text-blue-700 text-lg">{{ matchedSize }}</strong>
        </p>
        <p class="text-sm text-gray-500 mt-1">
          Lưu ý: Đây chỉ là size tham khảo. Vui lòng kiểm tra lại bảng size và ưu tiên số đo vòng
          ngực/vòng eo để có lựa chọn chính xác nhất.
        </p>
      </div>

      <!-- Khu vực nút bấm thao tác -->
      <div class="flex justify-end gap-3">
        <button
          @click="resetForm"
          class="px-5 py-2.5 bg-rose-600 hover:bg-rose-700 text-white text-base font-semibold rounded-lg transition"
        >
          Nhập lại
        </button>
        <button
          @click="calculateSize"
          class="px-5 py-2.5 bg-emerald-600 hover:bg-emerald-700 text-white text-base font-semibold rounded-lg transition"
        >
          Tính Size
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'

defineEmits(['close'])

const selectedType = ref('adult')
const matchedSize = ref(null)
const resultText = ref('')

const form = ref({
  height: '',
  weight: '',
  chest: '',
  waist: '',
  hip: '',
  length: '',
})

const databases = {
  adult: {
    name: 'Chung (Người lớn)',
    columns: ['SIZE', 'CHIỀU CAO (CM)', 'CÂN NẶNG (KG)'],
    data: [
      { size: 'S', minH: 150, maxH: 160, minW: 40, maxW: 50 },
      { size: 'M', minH: 160, maxH: 170, minW: 50, maxW: 60 },
      { size: 'L', minH: 170, maxH: 180, minW: 60, maxW: 70 },
    ],
  },
  ao_nam: {
    name: 'Áo Nam',
    columns: ['SIZE', 'CHIỀU CAO (CM)', 'CÂN NẶNG (KG)', 'VÒNG NGỰC (CM)', 'VÒNG EO (CM)'],
    data: [
      {
        size: 'S',
        minH: 160,
        maxH: 168,
        minW: 50,
        maxW: 58,
        minChest: 86,
        maxChest: 90,
        minWaist: 70,
        maxWaist: 74,
      },
      {
        size: 'M',
        minH: 168,
        maxH: 175,
        minW: 58,
        maxW: 65,
        minChest: 91,
        maxChest: 95,
        minWaist: 75,
        maxWaist: 79,
      },
      {
        size: 'L',
        minH: 175,
        maxH: 180,
        minW: 65,
        maxW: 75,
        minChest: 96,
        maxChest: 100,
        minWaist: 80,
        maxWaist: 84,
      },
      {
        size: 'XL',
        minH: 180,
        maxH: 185,
        minW: 75,
        maxW: 85,
        minChest: 101,
        maxChest: 105,
        minWaist: 85,
        maxWaist: 89,
      },
      {
        size: 'XXL',
        minH: 185,
        maxH: 190,
        minW: 85,
        maxW: 95,
        minChest: 106,
        maxChest: 110,
        minWaist: 90,
        maxWaist: 94,
      },
    ],
  },
  quan_nam: {
    name: 'Quần Nam',
    columns: ['SIZE', 'VÒNG EO (CM)', 'VÒNG HÔNG (CM)', 'CHIỀU DÀI QUẦN (CM)'],
    data: [
      { size: '28', minWaist: 70, maxWaist: 74, minHip: 88, maxHip: 92, minLen: 98, maxLen: 100 },
      { size: '29', minWaist: 74, maxWaist: 78, minHip: 92, maxHip: 96, minLen: 99, maxLen: 101 },
      { size: '30', minWaist: 78, maxWaist: 82, minHip: 96, maxHip: 100, minLen: 100, maxLen: 102 },
      {
        size: '31',
        minWaist: 82,
        maxWaist: 86,
        minHip: 100,
        maxHip: 104,
        minLen: 101,
        maxLen: 103,
      },
      {
        size: '32',
        minWaist: 86,
        maxWaist: 90,
        minHip: 104,
        maxHip: 108,
        minLen: 102,
        maxLen: 104,
      },
      {
        size: '33',
        minWaist: 90,
        maxWaist: 94,
        minHip: 108,
        maxHip: 112,
        minLen: 103,
        maxLen: 105,
      },
      {
        size: '34',
        minWaist: 94,
        maxWaist: 98,
        minHip: 112,
        maxHip: 116,
        minLen: 104,
        maxLen: 106,
      },
    ],
  },
  ao_nu: {
    name: 'Áo Nữ',
    columns: ['SIZE', 'CHIỀU CAO (CM)', 'CÂN NẶNG (KG)', 'VÒNG NGỰC (CM)', 'VÒNG EO (CM)'],
    data: [
      {
        size: 'S',
        minH: 150,
        maxH: 158,
        minW: 40,
        maxW: 48,
        minChest: 80,
        maxChest: 84,
        minWaist: 62,
        maxWaist: 66,
      },
      {
        size: 'M',
        minH: 158,
        maxH: 165,
        minW: 48,
        maxW: 55,
        minChest: 85,
        maxChest: 89,
        minWaist: 67,
        maxWaist: 71,
      },
      {
        size: 'L',
        minH: 165,
        maxH: 172,
        minW: 55,
        maxW: 62,
        minChest: 90,
        maxChest: 94,
        minWaist: 72,
        maxWaist: 76,
      },
      {
        size: 'XL',
        minH: 172,
        maxH: 178,
        minW: 62,
        maxW: 70,
        minChest: 95,
        maxChest: 99,
        minWaist: 77,
        maxWaist: 81,
      },
    ],
  },
  quan_nu: {
    name: 'Quần Nữ',
    columns: ['SIZE', 'VÒNG EO (CM)', 'VÒNG HÔNG (CM)', 'DÀI QUẦN (CM)'],
    data: [
      { size: '26', minWaist: 64, maxWaist: 68, minHip: 88, maxHip: 92, minLen: 92, maxLen: 94 },
      { size: '27', minWaist: 68, maxWaist: 72, minHip: 92, maxHip: 96, minLen: 93, maxLen: 95 },
      { size: '28', minWaist: 72, maxWaist: 76, minHip: 96, maxHip: 100, minLen: 94, maxLen: 96 },
      { size: '29', minWaist: 76, maxWaist: 80, minHip: 100, maxHip: 104, minLen: 95, maxLen: 97 },
    ],
  },
}

const currentCategoryName = computed(() => databases[selectedType.value].name)
const currentTableColumns = computed(() => databases[selectedType.value].columns)
const currentTableData = computed(() => databases[selectedType.value].data)

const handleTypeChange = () => {
  resetForm()
}

const calculateSize = () => {
  const currentList = currentTableData.value
  let bestMatch = null
  let minScore = Infinity

  currentList.forEach((item) => {
    let score = 0

    if (selectedType.value === 'adult') {
      if (form.value.height) {
        if (form.value.height < item.minH) score += item.minH - form.value.height
        else if (form.value.height > item.maxH) score += form.value.height - item.maxH
      }
      if (form.value.weight) {
        if (form.value.weight < item.minW) score += (item.minW - form.value.weight) * 2
        else if (form.value.weight > item.maxW) score += (form.value.weight - item.maxW) * 2
      }
    } else if (selectedType.value === 'ao_nam' || selectedType.value === 'ao_nu') {
      if (form.value.height) {
        if (form.value.height < item.minH) score += item.minH - form.value.height
        else if (form.value.height > item.maxH) score += form.value.height - item.maxH
      }
      if (form.value.weight) {
        if (form.value.weight < item.minW) score += (item.minW - form.value.weight) * 2
        else if (form.value.weight > item.maxW) score += (form.value.weight - item.maxW) * 2
      }
      if (form.value.chest) {
        if (form.value.chest < item.minChest) score += item.minChest - form.value.chest
        else if (form.value.chest > item.maxChest) score += form.value.chest - item.maxChest
      }
      if (form.value.waist) {
        if (form.value.waist < item.minWaist) score += item.minWaist - form.value.waist
        else if (form.value.waist > item.maxWaist) score += form.value.waist - item.maxWaist
      }
    } else if (selectedType.value === 'quan_nam' || selectedType.value === 'quan_nu') {
      if (form.value.waist) {
        if (form.value.waist < item.minWaist) score += (item.minWaist - form.value.waist) * 2
        else if (form.value.waist > item.maxWaist) score += (form.value.waist - item.maxWaist) * 2
      }
      if (form.value.hip) {
        if (form.value.hip < item.minHip) score += item.minHip - form.value.hip
        else if (form.value.hip > item.maxHip) score += form.value.hip - item.maxHip
      }
      if (form.value.length) {
        if (form.value.length < item.minLen) score += item.minLen - form.value.length
        else if (form.value.length > item.maxLen) score += form.value.length - item.maxLen
      }
    }

    if (score < minScore) {
      minScore = score
      bestMatch = item.size
    }
  })

  if (bestMatch) {
    matchedSize.value = bestMatch
    resultText.value = 'Success'
  }
}

const resetForm = () => {
  form.value = { height: '', weight: '', chest: '', waist: '', hip: '', length: '' }
  matchedSize.value = null
  resultText.value = ''
}
</script>
