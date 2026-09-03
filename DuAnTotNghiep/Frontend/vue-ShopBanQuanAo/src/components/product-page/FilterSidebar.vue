<template>
  <aside
    class="w-64 shrink-0 font-sans text-xs text-zinc-800 bg-white border border-zinc-200 rounded-sm shadow-sm"
  >
    <!-- Header Sidebar -->
    <div class="border-b border-zinc-200 px-4 py-3">
      <h2 class="text-sm font-bold text-zinc-900 tracking-tight">Bộ lọc tìm kiếm</h2>
    </div>

    <div class="divide-y divide-zinc-100">
      <!-- 1. Hãng sản xuất -->
      <details class="group" open>
        <summary
          class="flex cursor-pointer items-center justify-between px-4 py-3 font-semibold text-zinc-900 list-none select-none hover:text-red-600"
        >
          <span>Hãng sản xuất</span>
          <ChevronDown
            :size="16"
            class="text-zinc-500 transition-transform group-open:rotate-180"
          />
        </summary>
        <div
          class="max-h-64 overflow-y-auto px-4 pb-3 space-y-2.5 text-zinc-600 scrollbar-thin scrollbar-thumb-zinc-300"
        >
          <label
            v-for="brand in brands"
            :key="brand"
            class="flex cursor-pointer items-center gap-2.5 hover:text-zinc-900"
          >
            <input
              v-model="selectedBrands"
              type="checkbox"
              :value="brand"
              class="h-3.5 w-3.5 rounded border-zinc-300 text-red-600 focus:ring-0 accent-red-600"
            />
            <span class="text-xs">{{ brand }}</span>
          </label>
        </div>
      </details>

      <!-- 2. Mức giá -->
      <details class="group" open>
        <summary
          class="flex cursor-pointer items-center justify-between px-4 py-3 font-semibold text-zinc-900 list-none select-none hover:text-red-600"
        >
          <span>Mức giá</span>
          <ChevronDown
            :size="16"
            class="text-zinc-500 transition-transform group-open:rotate-180"
          />
        </summary>
        <div class="px-4 pb-3 space-y-2.5 text-zinc-600">
          <label
            v-for="price in priceRanges"
            :key="price"
            class="flex cursor-pointer items-center gap-2.5 hover:text-zinc-900"
          >
            <input
              v-model="selectedPrices"
              type="checkbox"
              :value="price"
              class="h-3.5 w-3.5 rounded border-zinc-300 text-red-600 focus:ring-0 accent-red-600"
            />
            <span class="text-xs">{{ price }}</span>
          </label>
        </div>
      </details>

      <!-- 3. Loại sản phẩm -->
      <details class="group" open>
        <summary
          class="flex cursor-pointer items-center justify-between px-4 py-3 font-semibold text-zinc-900 list-none select-none hover:text-red-600"
        >
          <span>Loại sản phẩm</span>
          <ChevronDown
            :size="16"
            class="text-zinc-500 transition-transform group-open:rotate-180"
          />
        </summary>
        <div
          class="max-h-64 overflow-y-auto px-4 pb-3 space-y-2.5 text-zinc-600 scrollbar-thin scrollbar-thumb-zinc-300"
        >
          <label
            v-for="type in productTypes"
            :key="type"
            class="flex cursor-pointer items-center gap-2.5 hover:text-zinc-900"
          >
            <input
              v-model="selectedTypes"
              type="checkbox"
              :value="type"
              class="h-3.5 w-3.5 rounded border-zinc-300 text-red-600 focus:ring-0 accent-red-600"
            />
            <span class="text-xs truncate">{{ type }}</span>
          </label>
        </div>
      </details>

      <!-- 4. Tính năng -->
      <details class="group" open>
        <summary
          class="flex cursor-pointer items-center justify-between px-4 py-3 font-semibold text-zinc-900 list-none select-none hover:text-red-600"
        >
          <span>Tính năng</span>
          <ChevronDown
            :size="16"
            class="text-zinc-500 transition-transform group-open:rotate-180"
          />
        </summary>
        <div class="px-4 pb-3 space-y-2.5 text-zinc-600">
          <label
            v-for="feature in features"
            :key="feature"
            class="flex cursor-pointer items-center gap-2.5 hover:text-zinc-900"
          >
            <input
              v-model="selectedFeatures"
              type="checkbox"
              :value="feature"
              class="h-3.5 w-3.5 rounded border-zinc-300 text-red-600 focus:ring-0 accent-red-600"
            />
            <span class="text-xs">{{ feature }}</span>
          </label>
        </div>
      </details>

      <!-- 5. Chất liệu -->
      <details class="group" open>
        <summary
          class="flex cursor-pointer items-center justify-between px-4 py-3 font-semibold text-zinc-900 list-none select-none hover:text-red-600"
        >
          <span>Chất liệu</span>
          <ChevronDown
            :size="16"
            class="text-zinc-500 transition-transform group-open:rotate-180"
          />
        </summary>
        <div class="px-4 pb-3 space-y-2.5 text-zinc-600">
          <label
            v-for="material in materials"
            :key="material"
            class="flex cursor-pointer items-center gap-2.5 hover:text-zinc-900"
          >
            <input
              v-model="selectedMaterials"
              type="checkbox"
              :value="material"
              class="h-3.5 w-3.5 rounded border-zinc-300 text-red-600 focus:ring-0 accent-red-600"
            />
            <span class="text-xs">{{ material }}</span>
          </label>
        </div>
      </details>
    </div>
  </aside>
</template>

<script setup>
import { ref } from 'vue'
import { ChevronDown } from 'lucide-vue-next'

const selectedBrands = ref([])
const selectedPrices = ref([])
const selectedTypes = ref([])
const selectedFeatures = ref([])
const selectedMaterials = ref([])

const brands = [
  'Alice',
  'Elixir',
  'Rode',
  'Akai',
  'Marshall',
  'Fishman',
  'Bose',
  'Novation',
  'Soundcraft',
  'Focusrite',
  'Squier',
  'Fender',
  'Eastman',
  'Ibanez',
  'Mantic',
  'Martin',
  'Enya',
  'Saga',
  'Godin',
  'Taylor',
  'Epiphone',
  'Hanika',
  'Dynatone',
  'Roland',
  'Kawai',
  'Casio',
  'Yamaha',
]

const priceRanges = [
  'Giá dưới 3.000.000đ',
  '3.000.000đ - 5.000.000đ',
  '5.000.000đ - 7.000.000đ',
  '7.000.000đ - 9.000.000đ',
  '9.000.000đ - 11.000.000đ',
  'Giá trên 11.000.000đ',
]

const productTypes = [
  'Phụ Kiện Guitar',
  'Dây Đàn Guitar Điện',
  'Micro',
  'Launch Control',
  'Launchpad',
  'Novation Circuit',
  'Midi Controller',
  'Đàn Piano Grand',
  'Loa',
  'Loa Sub',
  'Mixer',
  'Loa Studio',
  'Loa Passive',
  'Audio interface',
  'Đàn Guitar Silent',
  'Đàn Guitar Acoustic',
  'Đàn Guitar Điện',
  'Đàn Guitar Classic',
  'Đàn Piano Upright',
  'Đàn Piano Điện',
]

const features = ['Mới học', 'Biểu diễn', 'Chuyên nghiệp']

const materials = ['Gỗ tự nhiên', 'Giả gỗ']
</script>
