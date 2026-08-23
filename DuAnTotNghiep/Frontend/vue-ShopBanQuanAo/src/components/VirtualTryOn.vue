<script>
import { ref } from 'vue'

// Trạng thái chạy ngầm toàn cục (nằm ngoài component, không bị hủy khi đóng modal)
export const tryOnBgState = ref({
  loading: false,
  progress: 0,
  resultImg: '',
  errorMessage: '',
  statusMessage: '',
})

let bgProgressTimer = null

export function executeBackgroundTryOn({ spctId, personImage, category }) {
  if (tryOnBgState.value.loading) return

  tryOnBgState.value.loading = true
  tryOnBgState.value.progress = 4
  tryOnBgState.value.errorMessage = ''
  tryOnBgState.value.statusMessage = ''

  if (tryOnBgState.value.resultImg?.startsWith('blob:')) {
    URL.revokeObjectURL(tryOnBgState.value.resultImg)
  }
  tryOnBgState.value.resultImg = ''

  // Chạy thanh progress ngầm
  clearInterval(bgProgressTimer)
  bgProgressTimer = setInterval(() => {
    if (tryOnBgState.value.progress < 94) {
      const inc = tryOnBgState.value.progress < 45 ? 2 : 1
      tryOnBgState.value.progress = Math.min(94, tryOnBgState.value.progress + inc)
    }
  }, 420)

  // Import động hoặc gọi service
  import('@/service/VirtualTryOnService').then(async ({ createVirtualTryOn }) => {
    try {
      const resultBlob = await createVirtualTryOn({ spctId, personImage, category })
      tryOnBgState.value.progress = 100
      tryOnBgState.value.resultImg = URL.createObjectURL(resultBlob)
      tryOnBgState.value.statusMessage = 'Ảnh thử đồ đã được tạo thành công!'

      toast('✨ AI đã tạo xong ảnh thử đồ cho bạn!')
    } catch (error) {
      tryOnBgState.value.errorMessage = error.message || 'Không thể tạo ảnh thử đồ lúc này'
    } finally {
      clearInterval(bgProgressTimer)
      tryOnBgState.value.loading = false
    }
  })
}
</script>

<script setup>
import { computed, onMounted, onUnmounted, ref, watch } from 'vue'
import { useRoute } from 'vue-router'
import {
  AlertCircle,
  ArrowRight,
  Camera,
  Check,
  CheckCircle2,
  ChevronDown,
  ImagePlus,
  Info,
  LoaderCircle,
  LockKeyhole,
  RotateCcw,
  ScanLine,
  Shirt,
  Sparkles,
  Upload,
  WandSparkles,
  X,
} from 'lucide-vue-next'

const props = defineProps({
  isModal: { type: Boolean, default: false },
  spctId: { type: [Number, String], default: null },
  defaultGarmentUrl: { type: String, default: '' },
  defaultCategory: { type: String, default: 'upper_body' },
})

const emit = defineEmits(['close'])

const route = useRoute()
const humanInput = ref(null)
const humanFile = ref(null)
const humanPreview = ref('')
const selectedCategory = ref(props.defaultCategory)
const dragActive = ref(false)
const categoryDropdownOpen = ref(false)
const categoryDropdownRef = ref(null)

const categoryOptions = [
  { value: 'upper_body', label: 'Áo · Upper Body', icon: Shirt },
  { value: 'lower_body', label: 'Quần / Váy · Lower Body', icon: ScanLine },
  { value: 'dresses', label: 'Đầm liền · Dresses', icon: Sparkles },
]

const selectedCategoryOption = computed(
  () => categoryOptions.find((o) => o.value === selectedCategory.value) || categoryOptions[0],
)

const selectCategory = (val) => {
  selectedCategory.value = val
  categoryDropdownOpen.value = false
}

const closeCategoryDropdownOnOutside = (e) => {
  if (!categoryDropdownRef.value?.contains(e.target)) {
    categoryDropdownOpen.value = false
  }
}

const effectiveSpctId = computed(() => {
  const raw = props.spctId ?? route.query.spct
  const id = Number(raw)
  return Number.isInteger(id) && id > 0 ? id : null
})

const selectedCategoryLabel = computed(() => selectedCategoryOption.value.label)

watch(
  () => props.defaultCategory,
  (val) => {
    if (val) selectedCategory.value = val
  },
)

const revokeObjectUrl = (url) => {
  if (url?.startsWith('blob:')) URL.revokeObjectURL(url)
}

const triggerHumanInput = () => humanInput.value?.click()

const processImageCanvas = (file) =>
  new Promise((resolve, reject) => {
    if (!file.type?.startsWith('image/')) {
      reject(new Error('Vui lòng chọn đúng định dạng ảnh'))
      return
    }
    if (file.size > 10 * 1024 * 1024) {
      reject(new Error('Ảnh vượt quá dung lượng tối đa 10 MB'))
      return
    }

    const img = new Image()
    const sourceUrl = URL.createObjectURL(file)

    img.onload = () => {
      const canvas = document.createElement('canvas')
      canvas.width = 768
      canvas.height = 1024
      const ctx = canvas.getContext('2d')
      if (!ctx) {
        URL.revokeObjectURL(sourceUrl)
        reject(new Error('Trình duyệt không hỗ trợ xử lý ảnh'))
        return
      }

      ctx.fillStyle = '#fffdf9'
      ctx.fillRect(0, 0, canvas.width, canvas.height)
      const scale = Math.min(canvas.width / img.width, canvas.height / img.height)
      const x = (canvas.width - img.width * scale) / 2
      const y = (canvas.height - img.height * scale) / 2
      ctx.drawImage(img, x, y, img.width * scale, img.height * scale)

      canvas.toBlob(
        (blob) => {
          URL.revokeObjectURL(sourceUrl)
          if (!blob) {
            reject(new Error('Không xử lý được ảnh đã chọn'))
            return
          }
          resolve(new File([blob], 'person.jpg', { type: 'image/jpeg' }))
        },
        'image/jpeg',
        0.92,
      )
    }

    img.onerror = () => {
      URL.revokeObjectURL(sourceUrl)
      reject(new Error('Ảnh đã chọn không hợp lệ'))
    }
    img.src = sourceUrl
  })

const setHumanFile = async (file) => {
  if (!file) return
  tryOnBgState.value.errorMessage = ''
  tryOnBgState.value.statusMessage = ''
  try {
    const processed = await processImageCanvas(file)
    revokeObjectUrl(humanPreview.value)
    humanFile.value = processed
    humanPreview.value = URL.createObjectURL(processed)
  } catch (error) {
    tryOnBgState.value.errorMessage = error.message || 'Không thể đọc ảnh đã chọn'
  }
}

const handleHumanFileChange = async (e) => {
  await setHumanFile(e.target.files?.[0])
  e.target.value = ''
}

const handleDrop = async (e) => {
  dragActive.value = false
  await setHumanFile(e.dataTransfer?.files?.[0])
}

const startTryOn = () => {
  if (!humanFile.value || !effectiveSpctId.value) return
  executeBackgroundTryOn({
    spctId: effectiveSpctId.value,
    personImage: humanFile.value,
    category: selectedCategory.value,
  })
}

onMounted(() => {
  window.addEventListener('pointerdown', closeCategoryDropdownOnOutside)
})

onUnmounted(() => {
  window.removeEventListener('pointerdown', closeCategoryDropdownOnOutside)
  revokeObjectUrl(humanPreview.value)
})
</script>

<template>
  <div
    :class="[
      'min-h-full flex items-center justify-center p-3 text-slate-900 bg-gradient-to-br from-rose-50/80 via-slate-50 to-amber-50/60',
      {
        'fixed inset-0 z-50 overflow-y-auto bg-transparent p-3 sm:p-4 flex items-center justify-center':
          isModal,
      },
    ]"
    @click="isModal && emit('close')"
  >
    <section
      class="relative w-full max-w-3xl bg-white border border-slate-200/80 rounded-3xl shadow-2xl p-4 sm:p-5 my-auto max-h-[92vh] overflow-y-auto"
      @click.stop
    >
      <!-- Header cố định dính ở đầu khi cuộn -->
      <header
        class="sticky top-0 z-30 bg-white/95 backdrop-blur-sm pt-1 pb-3 mb-3 border-b border-slate-200 flex flex-col sm:flex-row sm:items-center justify-between gap-2.5 pr-10"
      >
        <div class="flex items-center gap-2.5">
          <div
            class="flex h-10 w-10 shrink-0 items-center justify-center rounded-xl bg-rose-50 text-rose-600 border border-rose-100 shadow-sm"
          >
            <Sparkles :size="20" />
          </div>
          <div>
            <span class="text-[10px] font-bold uppercase tracking-wider text-rose-600"
              >K-Zone AI Fitting Room</span
            >
            <h2 class="text-base sm:text-lg font-bold tracking-tight text-slate-900 leading-tight">
              Phòng thử đồ thông minh
            </h2>
          </div>
        </div>

        <div class="flex items-center gap-2">
          <div
            class="inline-flex items-center gap-1 px-2.5 py-1 rounded-full bg-emerald-50 border border-emerald-100 text-emerald-700 text-[11px] font-semibold"
          >
            <LockKeyhole :size="12" />
            <span>Bảo mật ảnh</span>
          </div>
        </div>

        <!-- Nút đóng modal luôn hiển thị gọn gàng -->
        <button
          v-if="isModal"
          type="button"
          class="absolute top-2 right-1 flex h-7 w-7 items-center justify-center rounded-full border border-slate-200 bg-white text-slate-700 shadow-sm hover:bg-slate-50 hover:text-rose-600 transition-all"
          aria-label="Đóng"
          @click="emit('close')"
        >
          <X :size="16" />
        </button>
      </header>

      <!-- Chọn loại trang phục -->
      <div
        class="flex flex-col sm:flex-row sm:items-center justify-between gap-3 p-3.5 mb-3 rounded-2xl bg-slate-50 border border-slate-200"
      >
        <div>
          <span class="text-[11px] font-bold uppercase tracking-wider text-rose-600"
            >Loại trang phục</span
          >
          <p class="text-xs text-slate-600 font-medium mt-0.5">
            Giúp AI tối ưu hóa vùng xử lý cơ thể.
          </p>
        </div>

        <div ref="categoryDropdownRef" class="relative w-full sm:w-64">
          <button
            type="button"
            class="w-full flex items-center justify-between gap-2.5 px-3.5 py-2 rounded-xl border border-slate-300 bg-white text-left shadow-sm hover:border-rose-400 focus:outline-none focus:ring-2 focus:ring-rose-500/20 transition-all"
            @click="categoryDropdownOpen = !categoryDropdownOpen"
          >
            <div class="flex items-center gap-2 truncate">
              <component
                :is="selectedCategoryOption.icon"
                :size="16"
                class="text-rose-600 shrink-0"
              />
              <span class="text-xs font-semibold text-slate-900 truncate">{{
                selectedCategoryOption.label
              }}</span>
            </div>
            <ChevronDown
              :size="14"
              class="text-slate-500 transition-transform duration-200"
              :class="{ 'rotate-180': categoryDropdownOpen }"
            />
          </button>

          <div
            v-if="categoryDropdownOpen"
            class="absolute top-full left-0 right-0 mt-1 z-30 bg-white rounded-2xl border border-slate-200 shadow-xl p-1.5 space-y-1"
          >
            <button
              v-for="option in categoryOptions"
              :key="option.value"
              type="button"
              class="w-full flex items-center justify-between p-2 rounded-xl text-left transition-colors hover:bg-rose-50"
              :class="{ 'bg-rose-50 text-rose-900 font-bold': selectedCategory === option.value }"
              @click="selectCategory(option.value)"
            >
              <div class="flex items-center gap-2">
                <component :is="option.icon" :size="16" class="text-rose-600" />
                <span class="text-xs text-slate-800 font-medium">{{ option.label }}</span>
              </div>
              <Check v-if="selectedCategory === option.value" :size="14" class="text-rose-600" />
            </button>
          </div>
        </div>
      </div>

      <!-- Workspace Grid (Upload & Garment) -->
      <div class="grid grid-cols-1 md:grid-cols-2 gap-3 mb-3">
        <!-- 01. Ảnh người thử -->
        <div class="p-3.5 rounded-2xl border border-slate-200 bg-white shadow-sm flex flex-col">
          <div class="flex items-center gap-2.5 mb-2.5">
            <span
              class="flex h-6 w-6 items-center justify-center rounded-lg bg-rose-600 text-white text-[11px] font-bold"
              >01</span
            >
            <div>
              <h3 class="text-xs font-bold text-slate-900 leading-tight">Ảnh người thử</h3>
              <p class="text-[11px] text-slate-600 font-medium">Toàn thân, đứng thẳng, đủ sáng</p>
            </div>
          </div>

          <button
            type="button"
            class="relative flex-1 min-h-[190px] max-h-[220px] w-full rounded-xl border-2 border-dashed border-slate-300 hover:border-rose-400 bg-slate-50 hover:bg-rose-50/20 flex flex-col items-center justify-center p-3 transition-all group overflow-hidden"
            @click="triggerHumanInput"
            @dragenter.prevent="dragActive = true"
            @dragover.prevent="dragActive = true"
            @dragleave.prevent="dragActive = false"
            @drop.prevent="handleDrop"
          >
            <div
              v-if="humanPreview"
              class="absolute inset-0 w-full h-full flex items-center justify-center bg-slate-900/5"
            >
              <img :src="humanPreview" alt="Preview" class="w-full h-full object-contain" />
              <div
                class="absolute inset-0 bg-slate-900/40 opacity-0 group-hover:opacity-100 transition-opacity flex items-center justify-center"
              >
                <span
                  class="inline-flex items-center gap-1.5 px-3 py-1.5 rounded-full bg-white text-slate-900 text-xs font-bold shadow-lg"
                >
                  <ImagePlus :size="14" /> Đổi ảnh khác
                </span>
              </div>
              <span
                class="absolute top-2 right-2 inline-flex items-center gap-1 px-2 py-0.5 rounded-full bg-emerald-50 text-emerald-700 text-[11px] font-bold shadow-sm"
              >
                <CheckCircle2 :size="12" /> Sẵn sàng
              </span>
            </div>

            <div v-else class="flex flex-col items-center text-center">
              <div
                class="w-10 h-10 rounded-xl bg-white shadow-md flex items-center justify-center text-rose-600 mb-2 group-hover:scale-105 transition-transform"
              >
                <Camera :size="18" />
              </div>
              <span class="text-xs font-bold text-slate-900">Chọn hoặc kéo thả ảnh vào đây</span>
              <span class="text-[11px] text-slate-500 mt-0.5">JPG, PNG · Tối đa 10MB</span>
              <span
                class="mt-2.5 inline-flex items-center gap-1 px-3 py-1.5 rounded-lg bg-rose-600 text-white text-xs font-bold shadow-sm"
              >
                <Upload :size="12" /> Tải ảnh lên
              </span>
            </div>
          </button>
          <input
            ref="humanInput"
            type="file"
            accept="image/*"
            class="hidden"
            @change="handleHumanFileChange"
          />
        </div>

        <!-- 02. Trang phục đang chọn -->
        <div class="p-3.5 rounded-2xl border border-slate-200 bg-white shadow-sm flex flex-col">
          <div class="flex items-center gap-2.5 mb-2.5">
            <span
              class="flex h-6 w-6 items-center justify-center rounded-lg bg-slate-900 text-white text-[11px] font-bold"
              >02</span
            >
            <div>
              <h3 class="text-xs font-bold text-slate-900 leading-tight">Trang phục</h3>
              <p class="text-[11px] text-slate-600 font-medium">Lấy tự động từ sản phẩm</p>
            </div>
          </div>

          <div
            class="relative flex-1 min-h-[190px] max-h-[220px] w-full rounded-xl border border-slate-200 bg-slate-50 flex items-center justify-center overflow-hidden"
          >
            <div
              v-if="defaultGarmentUrl"
              class="w-full h-full flex items-center justify-center p-2"
            >
              <img
                :src="defaultGarmentUrl"
                alt="Garment"
                class="w-full h-full object-contain rounded-lg"
              />
              <span
                class="absolute top-2 right-2 inline-flex items-center gap-1 px-2 py-0.5 rounded-full bg-amber-50 text-amber-800 text-[11px] font-bold shadow-sm"
              >
                <ScanLine :size="12" /> SPCT #{{ effectiveSpctId || '—' }}
              </span>
            </div>
            <div v-else class="flex flex-col items-center text-center p-3">
              <div
                class="w-10 h-10 rounded-xl bg-white shadow-md flex items-center justify-center text-amber-600 mb-2"
              >
                <Shirt :size="18" />
              </div>
              <span class="text-xs font-bold text-slate-900">Chưa có ảnh trang phục</span>
              <span class="text-[11px] text-slate-500 mt-0.5">Chọn màu/kích thước có ảnh</span>
            </div>
          </div>

          <div class="flex items-center justify-between mt-2.5 text-[11px] font-semibold">
            <span class="text-slate-700 flex items-center gap-1 font-medium">
              <Sparkles :size="12" class="text-rose-600" /> {{ selectedCategoryLabel }}
            </span>
            <span
              :class="effectiveSpctId ? 'text-emerald-600 font-bold' : 'text-amber-600 font-bold'"
            >
              {{ effectiveSpctId ? 'Ảnh hợp lệ' : 'Đang chờ ảnh' }}
            </span>
          </div>
        </div>
      </div>

      <!-- Trạng thái Loading (Chạy ngầm) -->
      <div
        v-if="tryOnBgState.loading"
        class="mb-3 p-3 rounded-xl bg-rose-50 border border-rose-100"
      >
        <div class="flex items-center justify-between mb-1.5">
          <div class="flex items-center gap-2">
            <LoaderCircle :size="14" class="text-rose-600 animate-spin" />
            <span class="text-xs font-bold text-slate-900">AI đang dựng bản phối</span>
          </div>
          <span class="text-xs font-bold text-rose-600">{{ tryOnBgState.progress }}%</span>
        </div>
        <div class="w-full h-1.5 bg-rose-100 rounded-full overflow-hidden">
          <div
            class="h-full bg-rose-600 transition-all duration-300"
            :style="{ width: `${tryOnBgState.progress}%` }"
          ></div>
        </div>
      </div>

      <!-- Nút hành động chính -->
      <button
        type="button"
        class="w-full flex items-center justify-center gap-2 py-3 px-5 rounded-xl bg-gradient-to-r from-rose-600 to-pink-600 text-white font-bold text-xs sm:text-sm shadow-lg shadow-rose-600/20 hover:from-rose-500 hover:to-pink-500 active:scale-[0.99] disabled:opacity-50 disabled:cursor-not-allowed transition-all"
        :disabled="tryOnBgState.loading || !humanFile || !effectiveSpctId"
        @click="startTryOn"
      >
        <LoaderCircle v-if="tryOnBgState.loading" :size="16" class="animate-spin" />
        <WandSparkles v-else :size="16" />
        <span>{{
          tryOnBgState.loading ? 'Đang tạo ảnh thử đồ...' : 'Bắt đầu thử đồ bằng AI'
        }}</span>
        <ArrowRight v-if="!tryOnBgState.loading" :size="14" />
      </button>

      <!-- Gợi ý hành động -->
      <p
        v-if="!humanFile || !effectiveSpctId"
        class="flex items-center justify-center gap-1 text-[11px] text-slate-500 font-medium mt-2 text-center"
      >
        <Info :size="12" />
        <span>Vui lòng chọn ảnh người thử và biến thể sản phẩm để tiếp tục.</span>
      </p>

      <!-- Thông báo thành công / lỗi -->
      <div
        v-if="tryOnBgState.statusMessage"
        class="mt-2.5 flex items-center gap-2 p-2.5 rounded-xl bg-emerald-50 border border-emerald-100 text-emerald-800 text-xs font-semibold"
      >
        <CheckCircle2 :size="14" />
        <span>{{ tryOnBgState.statusMessage }}</span>
      </div>

      <div
        v-if="tryOnBgState.errorMessage"
        class="mt-2.5 flex items-center gap-2 p-2.5 rounded-xl bg-rose-50 border border-rose-100 text-rose-800 text-xs font-semibold"
      >
        <AlertCircle :size="14" />
        <span>{{ tryOnBgState.errorMessage }}</span>
      </div>

      <!-- Khu vực kết quả -->
      <div v-if="tryOnBgState.resultImg" class="mt-4 pt-3 border-t border-slate-200">
        <div class="flex flex-col sm:flex-row sm:items-center justify-between gap-2 mb-2.5">
          <div>
            <span class="text-[10px] font-bold uppercase tracking-wider text-rose-600"
              >Kết quả AI</span
            >
            <h3 class="text-sm font-bold text-slate-900 leading-tight">Bản phối hoàn thiện</h3>
          </div>
          <button
            type="button"
            class="inline-flex items-center justify-center gap-1.5 px-3 py-1.5 rounded-lg border border-slate-300 bg-white text-slate-800 text-xs font-bold hover:bg-slate-50 transition-all shadow-sm"
            :disabled="tryOnBgState.loading"
            @click="startTryOn"
          >
            <RotateCcw :size="12" /> Thử lại
          </button>
        </div>

        <div
          class="relative rounded-xl overflow-hidden border border-slate-200 bg-slate-900/5 p-1.5 shadow-inner flex flex-col items-center"
        >
          <img
            :src="tryOnBgState.resultImg"
            alt="Result"
            class="w-full max-h-[350px] object-contain rounded-lg"
          />
          <div
            class="absolute bottom-2.5 right-2.5 inline-flex items-center gap-1 px-3 py-1 rounded-full bg-slate-900/80 text-white text-[11px] font-bold shadow-md"
          >
            <Sparkles :size="12" /> K-Zone AI Fitting Room
          </div>
        </div>
      </div>
    </section>
  </div>
</template>
