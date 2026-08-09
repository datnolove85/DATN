<template>
  <div :class="['tryon-shell', { 'tryon-shell--modal': isModal }]">
    <section class="tryon-panel" aria-labelledby="tryon-title">
      <div class="ambient ambient--gold"></div>
      <div class="ambient ambient--wine"></div>

      <button
        v-if="isModal"
        type="button"
        class="close-button"
        aria-label="Đóng cửa sổ thử đồ"
        @click="$emit('close')"
      >
        <X :size="20" />
      </button>

      <header class="tryon-header">
        <div class="tryon-brand-mark" aria-hidden="true">
          <Sparkles :size="24" />
        </div>

        <div class="tryon-heading-copy">
          <p class="eyebrow">Velora AI Fitting Room</p>
          <h2 id="tryon-title">Thử đồ bằng AI</h2>
          <p>
            Tải ảnh toàn thân rõ nét, chọn loại trang phục và để hệ thống tạo bản phối thử ngay trên
            sản phẩm bạn đang xem.
          </p>
        </div>

        <div class="privacy-badge">
          <LockKeyhole :size="16" />
          <span>Ảnh của bạn không hiển thị công khai</span>
        </div>
      </header>

      <div class="category-card">
        <div>
          <p class="field-kicker">Loại trang phục</p>
          <p class="field-help">Lựa chọn đúng nhóm giúp AI xác định vùng cơ thể cần xử lý.</p>
        </div>

        <div ref="categoryDropdownRef" class="category-select">
          <button
            type="button"
            class="category-select__trigger"
            :class="{ 'category-select__trigger--open': categoryDropdownOpen }"
            aria-haspopup="listbox"
            :aria-expanded="categoryDropdownOpen"
            aria-controls="tryon-category-listbox"
            @click="categoryDropdownOpen = !categoryDropdownOpen"
          >
            <span class="category-select__icon" aria-hidden="true">
              <component :is="selectedCategoryOption.icon" :size="19" />
            </span>

            <span class="category-select__copy">
              <strong>{{ selectedCategoryOption.label }}</strong>
              <small>{{ selectedCategoryOption.caption }}</small>
            </span>

            <span class="category-select__chevron" :class="{ 'is-open': categoryDropdownOpen }">
              <ChevronDown :size="18" aria-hidden="true" />
            </span>
          </button>

          <Transition name="dropdown-luxury">
            <div
              v-if="categoryDropdownOpen"
              id="tryon-category-listbox"
              class="category-select__menu"
              role="listbox"
              aria-label="Phân loại trang phục"
            >
              <button
                v-for="option in categoryOptions"
                :key="option.value"
                type="button"
                role="option"
                class="category-select__option"
                :class="{ 'is-selected': selectedCategory === option.value }"
                :aria-selected="selectedCategory === option.value"
                @click="selectCategory(option.value)"
              >
                <span class="category-select__option-icon" aria-hidden="true">
                  <component :is="option.icon" :size="19" />
                </span>

                <span class="category-select__option-copy">
                  <strong>{{ option.label }}</strong>
                  <small>{{ option.description }}</small>
                </span>

                <span class="category-select__check" aria-hidden="true">
                  <Check v-if="selectedCategory === option.value" :size="17" />
                </span>
              </button>
            </div>
          </Transition>
        </div>
      </div>

      <div class="workspace-grid">
        <article class="media-card media-card--person">
          <div class="media-card__header">
            <div class="step-number">01</div>
            <div>
              <h3>Ảnh người thử</h3>
              <p>Ưu tiên ảnh toàn thân, đứng thẳng và đủ ánh sáng.</p>
            </div>
          </div>

          <button
            type="button"
            :class="[
              'upload-stage',
              {
                'upload-stage--active': dragActive,
                'upload-stage--filled': humanPreview,
              },
            ]"
            @click="triggerHumanInput"
            @dragenter.prevent="dragActive = true"
            @dragover.prevent="dragActive = true"
            @dragleave.prevent="dragActive = false"
            @drop.prevent="handleDrop"
          >
            <Transition name="image-reveal" mode="out-in">
              <div v-if="humanPreview" key="preview" class="preview-frame">
                <img :src="humanPreview" alt="Ảnh người dùng đã chọn" />
                <div class="preview-overlay">
                  <span class="preview-action">
                    <ImagePlus :size="17" />
                    Chọn ảnh khác
                  </span>
                </div>
                <span class="media-status media-status--success">
                  <CheckCircle2 :size="15" />
                  Đã sẵn sàng
                </span>
              </div>

              <div v-else key="placeholder" class="upload-empty">
                <div class="upload-icon-orbit">
                  <div class="upload-icon">
                    <Camera :size="28" />
                  </div>
                </div>
                <h4>Chọn hoặc kéo ảnh vào đây</h4>
                <p>JPG, PNG hoặc WEBP · Tối đa 10 MB</p>
                <span class="upload-cta">
                  <Upload :size="16" />
                  Tải ảnh toàn thân
                </span>
              </div>
            </Transition>
          </button>

          <input
            ref="humanInput"
            type="file"
            accept="image/*"
            class="hidden-input"
            @change="handleHumanFileChange"
          />
        </article>

        <article class="media-card media-card--garment">
          <div class="media-card__header">
            <div class="step-number">02</div>
            <div>
              <h3>Trang phục đang chọn</h3>
              <p>Ảnh được lấy tự động từ biến thể sản phẩm hiện tại.</p>
            </div>
          </div>

          <div :class="['garment-stage', { 'garment-stage--filled': defaultGarmentUrl }]">
            <Transition name="image-reveal" mode="out-in">
              <div v-if="defaultGarmentUrl" key="garment" class="preview-frame">
                <img :src="defaultGarmentUrl" alt="Trang phục được chọn" />
                <span class="media-status media-status--gold">
                  <ScanLine :size="15" />
                  SPCT #{{ effectiveSpctId || '—' }}
                </span>
              </div>

              <div v-else key="missing" class="upload-empty garment-empty">
                <div class="upload-icon garment-icon">
                  <Shirt :size="28" />
                </div>
                <h4>Chưa có ảnh trang phục</h4>
                <p>Hãy chọn một màu hoặc kích thước có ảnh sản phẩm.</p>
              </div>
            </Transition>
          </div>

          <div class="garment-meta">
            <span>
              <Sparkles :size="15" />
              {{ selectedCategoryLabel }}
            </span>
            <span :class="effectiveSpctId ? 'is-ready' : 'is-waiting'">
              {{ effectiveSpctId ? 'Biến thể hợp lệ' : 'Đang chờ biến thể' }}
            </span>
          </div>
        </article>
      </div>

      <Transition name="status-slide">
        <div v-if="loading" class="processing-card">
          <div class="processing-copy">
            <span class="processing-icon">
              <LoaderCircle :size="18" />
            </span>
            <div>
              <strong>AI đang dựng bản phối</strong>
              <p>Quá trình có thể mất một vài phút tùy tải của dịch vụ.</p>
            </div>
            <span class="progress-value">{{ progress }}%</span>
          </div>
          <div
            class="progress-track"
            role="progressbar"
            :aria-valuenow="progress"
            aria-valuemin="0"
            aria-valuemax="100"
          >
            <div class="progress-fill" :style="{ width: `${progress}%` }"></div>
          </div>
        </div>
      </Transition>

      <button
        type="button"
        class="tryon-action"
        :disabled="loading || !humanFile || !effectiveSpctId"
        @click="startTryOn"
      >
        <span class="action-shine" aria-hidden="true"></span>
        <LoaderCircle v-if="loading" :size="20" class="spin" />
        <WandSparkles v-else :size="20" />
        <span>{{ loading ? 'Đang tạo ảnh thử đồ...' : 'Bắt đầu thử đồ bằng AI' }}</span>
        <ArrowRight v-if="!loading" :size="19" />
      </button>

      <p v-if="!humanFile || !effectiveSpctId" class="action-hint">
        <Info :size="15" />
        <span v-if="!humanFile && !effectiveSpctId"
          >Chọn ảnh người thử và một biến thể sản phẩm để tiếp tục.</span
        >
        <span v-else-if="!humanFile">Bạn cần tải ảnh toàn thân trước khi bắt đầu.</span>
        <span v-else>Biến thể hiện tại chưa hợp lệ hoặc chưa có ảnh.</span>
      </p>

      <Transition name="status-slide">
        <div v-if="statusMessage" class="message message--success">
          <CheckCircle2 :size="18" />
          <span>{{ statusMessage }}</span>
        </div>
      </Transition>

      <Transition name="status-slide">
        <div v-if="errorMessage" class="message message--error">
          <AlertCircle :size="18" />
          <span>{{ errorMessage }}</span>
        </div>
      </Transition>

      <Transition name="result-reveal">
        <section v-if="resultImg" class="result-section">
          <div class="result-header">
            <div>
              <p class="eyebrow">AI Result</p>
              <h3>Kết quả thử đồ</h3>
              <p>Ảnh kết quả chỉ mang tính mô phỏng và có thể khác với sản phẩm thực tế.</p>
            </div>

            <button type="button" class="retry-button" :disabled="loading" @click="startTryOn">
              <RotateCcw :size="17" />
              Thử lại
            </button>
          </div>

          <div class="result-image-frame">
            <img :src="resultImg" alt="Kết quả thử đồ bằng AI" />
            <div class="result-watermark">
              <Sparkles :size="15" />
              Velora AI Fitting Room
            </div>
          </div>
        </section>
      </Transition>
    </section>
  </div>
</template>

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
import { createVirtualTryOn } from '@/service/VirtualTryOnService'

const props = defineProps({
  isModal: { type: Boolean, default: false },
  spctId: { type: [Number, String], default: null },
  defaultGarmentUrl: { type: String, default: '' },
  defaultCategory: { type: String, default: 'upper_body' },
})

defineEmits(['close'])

const route = useRoute()
const humanInput = ref(null)
const humanFile = ref(null)
const humanPreview = ref('')
const selectedCategory = ref(props.defaultCategory)
const resultImg = ref('')
const loading = ref(false)
const progress = ref(0)
const errorMessage = ref('')
const statusMessage = ref('')
const dragActive = ref(false)
const categoryDropdownOpen = ref(false)
const categoryDropdownRef = ref(null)
let progressTimer = null

const categoryOptions = [
  {
    value: 'upper_body',
    label: 'Áo · Upper Body',
    caption: 'Trang phục thân trên',
    description: 'Áo thun, sơ mi, áo khoác và các thiết kế thân trên.',
    icon: Shirt,
  },
  {
    value: 'lower_body',
    label: 'Quần / Chân váy · Lower Body',
    caption: 'Trang phục thân dưới',
    description: 'Quần dài, quần short, chân váy và các thiết kế thân dưới.',
    icon: ScanLine,
  },
  {
    value: 'dresses',
    label: 'Đầm / Váy liền · Dresses',
    caption: 'Trang phục liền thân',
    description: 'Đầm, váy liền và các thiết kế phủ toàn thân.',
    icon: Sparkles,
  },
]

const selectedCategoryOption = computed(
  () =>
    categoryOptions.find((option) => option.value === selectedCategory.value) || categoryOptions[0],
)

const selectCategory = (value) => {
  selectedCategory.value = value
  categoryDropdownOpen.value = false
}

const closeCategoryDropdownOnOutside = (event) => {
  if (!categoryDropdownRef.value?.contains(event.target)) {
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
  (value) => {
    if (value) selectedCategory.value = value
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

  errorMessage.value = ''
  statusMessage.value = ''

  try {
    const processed = await processImageCanvas(file)
    revokeObjectUrl(humanPreview.value)
    humanFile.value = processed
    humanPreview.value = URL.createObjectURL(processed)
  } catch (error) {
    errorMessage.value = error.message || 'Không thể đọc ảnh đã chọn'
  }
}

const handleHumanFileChange = async (event) => {
  await setHumanFile(event.target.files?.[0])
  event.target.value = ''
}

const handleDrop = async (event) => {
  dragActive.value = false
  await setHumanFile(event.dataTransfer?.files?.[0])
}

const startProgressBar = () => {
  progress.value = 4
  clearInterval(progressTimer)
  progressTimer = setInterval(() => {
    if (progress.value < 94) {
      const increment = progress.value < 45 ? 2 : 1
      progress.value = Math.min(94, progress.value + increment)
    }
  }, 420)
}

const startTryOn = async () => {
  if (!humanFile.value || !effectiveSpctId.value) return

  loading.value = true
  errorMessage.value = ''
  statusMessage.value = ''
  revokeObjectUrl(resultImg.value)
  resultImg.value = ''
  startProgressBar()

  try {
    const resultBlob = await createVirtualTryOn({
      spctId: effectiveSpctId.value,
      personImage: humanFile.value,
      category: selectedCategory.value,
    })

    progress.value = 100
    resultImg.value = URL.createObjectURL(resultBlob)
    statusMessage.value = 'Ảnh thử đồ đã được tạo thành công.'
  } catch (error) {
    errorMessage.value = error.message || 'Không thể tạo ảnh thử đồ lúc này'
  } finally {
    clearInterval(progressTimer)
    loading.value = false
  }
}

onMounted(() => {
  window.addEventListener('pointerdown', closeCategoryDropdownOnOutside)
})

onUnmounted(() => {
  window.removeEventListener('pointerdown', closeCategoryDropdownOnOutside)
  clearInterval(progressTimer)
  revokeObjectUrl(humanPreview.value)
  revokeObjectUrl(resultImg.value)
})
</script>

<style scoped>
.tryon-shell {
  --tryon-ivory: #f7f4ee;
  --tryon-paper: #fffdf9;
  --tryon-ink: #181511;
  --tryon-muted: #756d64;
  --tryon-border: #e7dfd4;
  --tryon-border-strong: #d7c9ba;
  --tryon-wine: #7d1f2d;
  --tryon-wine-dark: #58131e;
  --tryon-gold: #bd8c4d;
  --tryon-gold-soft: #ead8bc;

  display: grid;
  min-height: 100%;
  place-items: center;
  padding: clamp(18px, 4vw, 52px);
  background:
    radial-gradient(circle at 8% 4%, rgba(189, 140, 77, 0.11), transparent 24rem),
    radial-gradient(circle at 95% 10%, rgba(125, 31, 45, 0.08), transparent 27rem), transparent;
  color: var(--tryon-ink);
}

.tryon-shell--modal {
  position: fixed;
  inset: 0;
  z-index: 9999;
  min-height: 100vh;
  overflow-y: auto;
  place-items: start center;
  background: rgba(24, 21, 17, 0.72);
  backdrop-filter: blur(18px) saturate(0.85);
}

.tryon-panel {
  position: relative;
  isolation: isolate;
  width: min(1120px, 100%);
  overflow: hidden;
  border: 1px solid rgba(231, 223, 212, 0.95);
  border-radius: clamp(24px, 3vw, 36px);
  background: rgba(255, 253, 249, 0.96);
  box-shadow: 0 32px 90px rgba(48, 35, 24, 0.16);
  padding: clamp(22px, 4vw, 46px);
  animation: panel-enter 620ms cubic-bezier(0.22, 1, 0.36, 1) both;
}

.ambient {
  position: absolute;
  z-index: -1;
  border-radius: 999px;
  filter: blur(70px);
  pointer-events: none;
}

.ambient--gold {
  top: -130px;
  right: 8%;
  width: 300px;
  height: 300px;
  background: rgba(189, 140, 77, 0.12);
  animation: ambient-drift 11s ease-in-out infinite alternate;
}

.ambient--wine {
  bottom: -170px;
  left: -70px;
  width: 360px;
  height: 360px;
  background: rgba(125, 31, 45, 0.08);
  animation: ambient-drift 13s ease-in-out 1.5s infinite alternate-reverse;
}

.close-button {
  position: absolute;
  top: 20px;
  right: 20px;
  z-index: 6;
  display: grid;
  width: 44px;
  height: 44px;
  place-items: center;
  border: 1px solid var(--tryon-border);
  border-radius: 50%;
  background: rgba(255, 253, 249, 0.88);
  color: var(--tryon-ink);
  box-shadow: 0 10px 28px rgba(48, 35, 24, 0.08);
  transition:
    transform 180ms ease,
    border-color 180ms ease,
    background 180ms ease;
}

.close-button:hover {
  transform: rotate(4deg) scale(1.04);
  border-color: var(--tryon-gold);
  background: white;
}

.tryon-header {
  display: grid;
  grid-template-columns: auto minmax(0, 1fr) auto;
  align-items: center;
  gap: 18px;
  padding-right: 44px;
}

.tryon-brand-mark {
  display: grid;
  width: 60px;
  height: 60px;
  place-items: center;
  border: 1px solid rgba(189, 140, 77, 0.26);
  border-radius: 20px;
  background: linear-gradient(145deg, #fffaf1, #f2e6d4);
  color: var(--tryon-wine);
  box-shadow: 0 14px 32px rgba(189, 140, 77, 0.15);
  animation: mark-float 4.8s ease-in-out infinite;
}

.eyebrow,
.field-kicker {
  margin: 0;
  color: var(--tryon-wine);
  font-size: 0.7rem;
  font-weight: 900;
  letter-spacing: 0.2em;
  text-transform: uppercase;
}

.tryon-heading-copy h2 {
  margin: 5px 0 0;
  color: var(--tryon-ink);
  font-family: Inter, 'Segoe UI', ui-sans-serif, system-ui, sans-serif;
  font-size: clamp(2rem, 4vw, 3.35rem);
  font-weight: 700;
  line-height: 1.02;
  letter-spacing: -0.045em;
}

.tryon-heading-copy > p:last-child {
  max-width: 690px;
  margin: 10px 0 0;
  color: var(--tryon-muted);
  font-size: 0.94rem;
  line-height: 1.75;
}

.privacy-badge {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  border: 1px solid rgba(31, 122, 91, 0.18);
  border-radius: 999px;
  background: rgba(31, 122, 91, 0.07);
  padding: 10px 14px;
  color: #1f6a51;
  font-size: 0.75rem;
  font-weight: 800;
  white-space: nowrap;
}

.category-card {
  display: grid;
  grid-template-columns: minmax(0, 1fr) minmax(270px, 380px);
  align-items: center;
  gap: 24px;
  margin-top: 30px;
  border: 1px solid var(--tryon-border);
  border-radius: 22px;
  background: rgba(247, 244, 238, 0.62);
  padding: 18px 20px;
}

.field-help {
  margin: 5px 0 0;
  color: var(--tryon-muted);
  font-size: 0.82rem;
  line-height: 1.55;
}

.category-select {
  position: relative;
  z-index: 30;
  width: 100%;
}

.category-select__trigger {
  display: grid;
  width: 100%;
  min-height: 66px;
  grid-template-columns: auto minmax(0, 1fr) auto;
  align-items: center;
  gap: 13px;
  border: 1px solid var(--tryon-border-strong);
  border-radius: 18px;
  background: linear-gradient(135deg, rgba(255, 253, 249, 0.98), rgba(249, 244, 236, 0.94));
  padding: 9px 11px;
  color: var(--tryon-ink);
  text-align: left;
  box-shadow: 0 10px 30px rgba(48, 35, 24, 0.06);
  transition:
    transform 220ms cubic-bezier(0.22, 1, 0.36, 1),
    border-color 180ms ease,
    box-shadow 220ms ease,
    background 180ms ease;
}

.category-select__trigger:hover,
.category-select__trigger--open {
  transform: translateY(-2px);
  border-color: rgba(125, 31, 45, 0.65);
  background: linear-gradient(135deg, #fffdf9, #fff8ee);
  box-shadow:
    0 0 0 4px rgba(125, 31, 45, 0.055),
    0 18px 42px rgba(83, 48, 34, 0.12);
}

.category-select__trigger:focus-visible {
  outline: none;
  border-color: var(--tryon-wine);
  box-shadow:
    0 0 0 4px rgba(125, 31, 45, 0.1),
    0 18px 42px rgba(83, 48, 34, 0.1);
}

.category-select__icon,
.category-select__option-icon {
  display: grid;
  flex: 0 0 auto;
  width: 42px;
  height: 42px;
  place-items: center;
  border: 1px solid rgba(189, 140, 77, 0.24);
  border-radius: 14px;
  background: linear-gradient(145deg, #fffaf1, #f0dfc5);
  color: var(--tryon-wine);
  box-shadow: inset 0 1px 0 rgba(255, 255, 255, 0.8);
}

.category-select__copy,
.category-select__option-copy {
  display: grid;
  min-width: 0;
  gap: 3px;
}

.category-select__copy strong,
.category-select__option-copy strong {
  overflow: hidden;
  color: var(--tryon-ink);
  font-size: 0.87rem;
  font-weight: 900;
  line-height: 1.3;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.category-select__copy small,
.category-select__option-copy small {
  color: var(--tryon-muted);
  font-size: 0.7rem;
  font-weight: 700;
  line-height: 1.45;
}

.category-select__copy small {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.category-select__chevron {
  display: grid;
  width: 34px;
  height: 34px;
  place-items: center;
  border-radius: 11px;
  color: var(--tryon-wine);
  transition:
    transform 260ms cubic-bezier(0.22, 1, 0.36, 1),
    background 180ms ease;
}

.category-select__trigger:hover .category-select__chevron,
.category-select__chevron.is-open {
  background: rgba(125, 31, 45, 0.07);
}

.category-select__chevron.is-open {
  transform: rotate(180deg);
}

.category-select__menu {
  position: absolute;
  top: calc(100% + 10px);
  right: 0;
  left: 0;
  display: grid;
  gap: 7px;
  overflow: hidden;
  border: 1px solid rgba(215, 201, 186, 0.92);
  border-radius: 20px;
  background: rgba(255, 253, 249, 0.98);
  padding: 8px;
  box-shadow:
    0 28px 70px rgba(48, 35, 24, 0.18),
    0 0 0 1px rgba(255, 255, 255, 0.65) inset;
  backdrop-filter: blur(20px) saturate(1.08);
}

.category-select__menu::before {
  position: absolute;
  inset: 0;
  background:
    radial-gradient(circle at 15% 0%, rgba(189, 140, 77, 0.12), transparent 38%),
    radial-gradient(circle at 100% 100%, rgba(125, 31, 45, 0.07), transparent 42%);
  content: '';
  pointer-events: none;
}

.category-select__option {
  position: relative;
  z-index: 1;
  display: grid;
  width: 100%;
  min-height: 66px;
  grid-template-columns: auto minmax(0, 1fr) 30px;
  align-items: center;
  gap: 12px;
  border: 1px solid transparent;
  border-radius: 15px;
  background: transparent;
  padding: 9px 10px;
  text-align: left;
  transition:
    transform 180ms cubic-bezier(0.22, 1, 0.36, 1),
    border-color 180ms ease,
    background 180ms ease,
    box-shadow 180ms ease;
}

.category-select__option:hover,
.category-select__option:focus-visible {
  transform: translateX(3px);
  border-color: rgba(189, 140, 77, 0.2);
  outline: none;
  background: rgba(247, 244, 238, 0.88);
  box-shadow: 0 9px 24px rgba(48, 35, 24, 0.055);
}

.category-select__option.is-selected {
  border-color: rgba(125, 31, 45, 0.18);
  background: linear-gradient(135deg, rgba(125, 31, 45, 0.08), rgba(189, 140, 77, 0.08));
}

.category-select__option.is-selected .category-select__option-icon {
  border-color: rgba(125, 31, 45, 0.22);
  background: linear-gradient(145deg, var(--tryon-wine-dark), var(--tryon-wine));
  color: white;
  box-shadow: 0 10px 22px rgba(125, 31, 45, 0.18);
}

.category-select__check {
  display: grid;
  width: 28px;
  height: 28px;
  place-items: center;
  border-radius: 50%;
  color: var(--tryon-wine);
}

.dropdown-luxury-enter-active,
.dropdown-luxury-leave-active {
  transform-origin: top center;
  transition:
    opacity 180ms ease,
    transform 260ms cubic-bezier(0.22, 1, 0.36, 1),
    filter 180ms ease;
}

.dropdown-luxury-enter-from,
.dropdown-luxury-leave-to {
  opacity: 0;
  filter: blur(5px);
  transform: translateY(-8px) scale(0.975);
}

.workspace-grid {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 20px;
  margin-top: 20px;
}

.media-card {
  min-width: 0;
  border: 1px solid var(--tryon-border);
  border-radius: 26px;
  background: rgba(255, 255, 255, 0.66);
  padding: 18px;
  box-shadow: 0 14px 38px rgba(48, 35, 24, 0.05);
  animation: card-rise 620ms cubic-bezier(0.22, 1, 0.36, 1) both;
}

.media-card--garment {
  animation-delay: 90ms;
}

.media-card__header {
  display: flex;
  align-items: flex-start;
  gap: 12px;
  min-height: 56px;
  margin-bottom: 14px;
}

.step-number {
  display: grid;
  flex: 0 0 auto;
  width: 42px;
  height: 42px;
  place-items: center;
  border-radius: 14px;
  background: var(--tryon-wine);
  color: white;
  font-size: 0.72rem;
  font-weight: 900;
  letter-spacing: 0.08em;
  box-shadow: 0 10px 24px rgba(125, 31, 45, 0.18);
}

.media-card__header h3 {
  margin: 1px 0 0;
  color: var(--tryon-ink);
  font-size: 1rem;
  font-weight: 900;
}

.media-card__header p {
  margin: 5px 0 0;
  color: var(--tryon-muted);
  font-size: 0.78rem;
  line-height: 1.5;
}

.upload-stage,
.garment-stage {
  position: relative;
  display: grid;
  width: 100%;
  min-height: 390px;
  overflow: hidden;
  place-items: center;
  border: 1.5px dashed var(--tryon-border-strong);
  border-radius: 22px;
  background:
    linear-gradient(rgba(255, 253, 249, 0.84), rgba(255, 253, 249, 0.84)),
    repeating-linear-gradient(45deg, transparent 0 14px, rgba(189, 140, 77, 0.05) 14px 15px);
  padding: 0;
  color: inherit;
  transition:
    transform 220ms cubic-bezier(0.22, 1, 0.36, 1),
    border-color 220ms ease,
    box-shadow 220ms ease,
    background 220ms ease;
}

.upload-stage:hover,
.upload-stage--active {
  transform: translateY(-3px);
  border-color: var(--tryon-gold);
  background:
    linear-gradient(rgba(255, 250, 241, 0.95), rgba(255, 253, 249, 0.95)),
    repeating-linear-gradient(45deg, transparent 0 14px, rgba(189, 140, 77, 0.08) 14px 15px);
  box-shadow: 0 18px 42px rgba(189, 140, 77, 0.13);
}

.upload-stage--filled,
.garment-stage--filled {
  border-style: solid;
  border-color: var(--tryon-border);
  background: #f2eee7;
}

.garment-stage {
  cursor: default;
  border-style: solid;
}

.upload-empty {
  display: grid;
  place-items: center;
  padding: 38px 26px;
  text-align: center;
}

.upload-icon-orbit {
  position: relative;
  display: grid;
  width: 84px;
  height: 84px;
  place-items: center;
  border: 1px solid rgba(189, 140, 77, 0.2);
  border-radius: 50%;
  background: rgba(234, 216, 188, 0.28);
}

.upload-icon-orbit::before {
  position: absolute;
  inset: -8px;
  border: 1px dashed rgba(125, 31, 45, 0.22);
  border-radius: inherit;
  content: '';
  animation: orbit-spin 13s linear infinite;
}

.upload-icon {
  display: grid;
  width: 58px;
  height: 58px;
  place-items: center;
  border-radius: 18px;
  background: var(--tryon-paper);
  color: var(--tryon-wine);
  box-shadow: 0 12px 28px rgba(48, 35, 24, 0.1);
}

.garment-icon {
  margin-bottom: 6px;
  color: var(--tryon-gold);
}

.upload-empty h4 {
  margin: 22px 0 0;
  color: var(--tryon-ink);
  font-size: 1rem;
  font-weight: 900;
}

.upload-empty p {
  max-width: 270px;
  margin: 8px 0 0;
  color: var(--tryon-muted);
  font-size: 0.78rem;
  line-height: 1.55;
}

.upload-cta {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  margin-top: 20px;
  border-radius: 999px;
  background: var(--tryon-wine);
  padding: 10px 16px;
  color: white;
  font-size: 0.78rem;
  font-weight: 900;
  box-shadow: 0 12px 25px rgba(125, 31, 45, 0.18);
}

.preview-frame {
  position: relative;
  width: 100%;
  height: 100%;
  min-height: inherit;
}

.preview-frame img {
  width: 100%;
  height: 100%;
  min-height: inherit;
  object-fit: contain;
  background: linear-gradient(145deg, #f7f4ee, #fffdf9);
}

.preview-overlay {
  position: absolute;
  inset: 0;
  display: grid;
  place-items: center;
  background: rgba(24, 21, 17, 0.42);
  opacity: 0;
  backdrop-filter: blur(3px);
  transition: opacity 200ms ease;
}

.upload-stage:hover .preview-overlay {
  opacity: 1;
}

.preview-action {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  border: 1px solid rgba(255, 255, 255, 0.34);
  border-radius: 999px;
  background: rgba(255, 253, 249, 0.94);
  padding: 11px 16px;
  color: var(--tryon-ink);
  font-size: 0.78rem;
  font-weight: 900;
  transform: translateY(8px);
  transition: transform 200ms ease;
}

.upload-stage:hover .preview-action {
  transform: translateY(0);
}

.media-status {
  position: absolute;
  top: 14px;
  right: 14px;
  display: inline-flex;
  align-items: center;
  gap: 6px;
  border-radius: 999px;
  padding: 8px 11px;
  font-size: 0.7rem;
  font-weight: 900;
  backdrop-filter: blur(10px);
}

.media-status--success {
  border: 1px solid rgba(31, 122, 91, 0.18);
  background: rgba(239, 252, 247, 0.9);
  color: #1f6a51;
}

.media-status--gold {
  border: 1px solid rgba(189, 140, 77, 0.22);
  background: rgba(255, 250, 241, 0.92);
  color: #7b562a;
}

.garment-meta {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  margin-top: 12px;
  color: var(--tryon-muted);
  font-size: 0.72rem;
  font-weight: 800;
}

.garment-meta span {
  display: inline-flex;
  align-items: center;
  gap: 6px;
}

.garment-meta .is-ready {
  color: #1f6a51;
}

.garment-meta .is-waiting {
  color: #9a6a2e;
}

.processing-card {
  margin-top: 20px;
  border: 1px solid rgba(125, 31, 45, 0.14);
  border-radius: 20px;
  background: rgba(125, 31, 45, 0.045);
  padding: 16px;
}

.processing-copy {
  display: grid;
  grid-template-columns: auto minmax(0, 1fr) auto;
  align-items: center;
  gap: 12px;
}

.processing-icon {
  display: grid;
  width: 40px;
  height: 40px;
  place-items: center;
  border-radius: 13px;
  background: var(--tryon-wine);
  color: white;
}

.processing-icon svg {
  animation: spin 1s linear infinite;
}

.processing-copy strong {
  color: var(--tryon-ink);
  font-size: 0.84rem;
}

.processing-copy p {
  margin: 3px 0 0;
  color: var(--tryon-muted);
  font-size: 0.72rem;
}

.progress-value {
  color: var(--tryon-wine);
  font-size: 0.84rem;
  font-weight: 900;
}

.progress-track {
  height: 8px;
  margin-top: 13px;
  overflow: hidden;
  border-radius: 999px;
  background: rgba(125, 31, 45, 0.1);
}

.progress-fill {
  position: relative;
  height: 100%;
  overflow: hidden;
  border-radius: inherit;
  background: linear-gradient(90deg, var(--tryon-wine-dark), var(--tryon-wine), var(--tryon-gold));
  transition: width 300ms ease;
}

.progress-fill::after {
  position: absolute;
  inset: 0;
  background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.48), transparent);
  content: '';
  transform: translateX(-100%);
  animation: progress-shimmer 1.6s linear infinite;
}

.tryon-action {
  position: relative;
  display: flex;
  width: 100%;
  min-height: 58px;
  align-items: center;
  justify-content: center;
  gap: 10px;
  overflow: hidden;
  margin-top: 20px;
  border: 1px solid var(--tryon-wine);
  border-radius: 17px;
  background: linear-gradient(135deg, var(--tryon-wine-dark), var(--tryon-wine));
  padding: 14px 20px;
  color: white;
  font-size: 0.9rem;
  font-weight: 900;
  letter-spacing: 0.01em;
  box-shadow: 0 16px 36px rgba(125, 31, 45, 0.23);
  transition:
    transform 220ms cubic-bezier(0.22, 1, 0.36, 1),
    box-shadow 220ms ease,
    filter 220ms ease;
}

.tryon-action:hover:not(:disabled) {
  transform: translateY(-3px);
  filter: saturate(1.06);
  box-shadow: 0 22px 44px rgba(125, 31, 45, 0.3);
}

.tryon-action:active:not(:disabled) {
  transform: translateY(-1px) scale(0.995);
}

.tryon-action:disabled {
  border-color: #cfc4b8;
  background: #d8d0c6;
  color: #8c8278;
  box-shadow: none;
  cursor: not-allowed;
}

.action-shine {
  position: absolute;
  top: -50%;
  left: -30%;
  width: 24%;
  height: 200%;
  background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.28), transparent);
  transform: rotate(20deg);
  animation: action-shine 4.8s ease-in-out infinite;
}

.tryon-action:disabled .action-shine {
  display: none;
}

.spin {
  animation: spin 1s linear infinite;
}

.action-hint {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 7px;
  margin: 10px 0 0;
  color: var(--tryon-muted);
  font-size: 0.74rem;
  text-align: center;
}

.message {
  display: flex;
  align-items: flex-start;
  gap: 10px;
  margin-top: 14px;
  border-radius: 16px;
  padding: 13px 15px;
  font-size: 0.8rem;
  font-weight: 700;
  line-height: 1.55;
}

.message svg {
  flex: 0 0 auto;
  margin-top: 1px;
}

.message--success {
  border: 1px solid rgba(31, 122, 91, 0.16);
  background: rgba(31, 122, 91, 0.07);
  color: #1f6a51;
}

.message--error {
  border: 1px solid rgba(180, 35, 59, 0.16);
  background: rgba(180, 35, 59, 0.06);
  color: #9f2034;
}

.result-section {
  margin-top: 28px;
  border-top: 1px solid var(--tryon-border);
  padding-top: 28px;
}

.result-header {
  display: flex;
  align-items: flex-end;
  justify-content: space-between;
  gap: 20px;
  margin-bottom: 16px;
}

.result-header h3 {
  margin: 4px 0 0;
  color: var(--tryon-ink);
  font-family: Inter, 'Segoe UI', ui-sans-serif, system-ui, sans-serif;
  font-size: clamp(1.6rem, 3vw, 2.3rem);
  font-weight: 700;
  letter-spacing: -0.035em;
}

.result-header > div > p:last-child {
  margin: 7px 0 0;
  color: var(--tryon-muted);
  font-size: 0.76rem;
}

.retry-button {
  display: inline-flex;
  min-height: 44px;
  align-items: center;
  justify-content: center;
  gap: 8px;
  border: 1px solid var(--tryon-border-strong);
  border-radius: 14px;
  background: var(--tryon-paper);
  padding: 10px 15px;
  color: var(--tryon-ink);
  font-size: 0.78rem;
  font-weight: 900;
  white-space: nowrap;
  transition:
    transform 180ms ease,
    border-color 180ms ease,
    box-shadow 180ms ease;
}

.retry-button:hover:not(:disabled) {
  transform: translateY(-2px);
  border-color: var(--tryon-gold);
  box-shadow: 0 12px 28px rgba(48, 35, 24, 0.08);
}

.result-image-frame {
  position: relative;
  overflow: hidden;
  border: 1px solid var(--tryon-border);
  border-radius: 24px;
  background: linear-gradient(145deg, #eee8df, #fffdf9);
  padding: 10px;
  box-shadow: 0 18px 48px rgba(48, 35, 24, 0.08);
}

.result-image-frame img {
  width: 100%;
  max-height: 820px;
  border-radius: 18px;
  object-fit: contain;
}

.result-watermark {
  position: absolute;
  right: 24px;
  bottom: 24px;
  display: inline-flex;
  align-items: center;
  gap: 7px;
  border: 1px solid rgba(255, 255, 255, 0.3);
  border-radius: 999px;
  background: rgba(24, 21, 17, 0.62);
  padding: 9px 13px;
  color: white;
  font-size: 0.7rem;
  font-weight: 900;
  backdrop-filter: blur(12px);
}

.hidden-input {
  display: none;
}

.image-reveal-enter-active,
.image-reveal-leave-active,
.status-slide-enter-active,
.status-slide-leave-active,
.result-reveal-enter-active,
.result-reveal-leave-active {
  transition:
    opacity 320ms ease,
    transform 420ms cubic-bezier(0.22, 1, 0.36, 1),
    filter 320ms ease;
}

.image-reveal-enter-from,
.image-reveal-leave-to {
  opacity: 0;
  filter: blur(6px);
  transform: scale(0.985);
}

.status-slide-enter-from,
.status-slide-leave-to {
  opacity: 0;
  transform: translateY(8px);
}

.result-reveal-enter-from,
.result-reveal-leave-to {
  opacity: 0;
  filter: blur(8px);
  transform: translateY(16px) scale(0.99);
}

@keyframes panel-enter {
  from {
    opacity: 0;
    filter: blur(7px);
    transform: translateY(18px) scale(0.985);
  }
  to {
    opacity: 1;
    filter: blur(0);
    transform: translateY(0) scale(1);
  }
}

@keyframes card-rise {
  from {
    opacity: 0;
    transform: translateY(16px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes mark-float {
  0%,
  100% {
    transform: translateY(0) rotate(0deg);
  }
  50% {
    transform: translateY(-5px) rotate(2deg);
  }
}

@keyframes ambient-drift {
  from {
    transform: translate3d(-10px, -8px, 0) scale(0.96);
  }
  to {
    transform: translate3d(18px, 15px, 0) scale(1.06);
  }
}

@keyframes orbit-spin {
  to {
    transform: rotate(360deg);
  }
}

@keyframes progress-shimmer {
  to {
    transform: translateX(100%);
  }
}

@keyframes action-shine {
  0%,
  62% {
    left: -35%;
    opacity: 0;
  }
  72% {
    opacity: 1;
  }
  92%,
  100% {
    left: 118%;
    opacity: 0;
  }
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}

@media (max-width: 900px) {
  .tryon-header {
    grid-template-columns: auto minmax(0, 1fr);
  }

  .privacy-badge {
    grid-column: 1 / -1;
    width: fit-content;
  }

  .category-card,
  .workspace-grid {
    grid-template-columns: 1fr;
  }

  .upload-stage,
  .garment-stage {
    min-height: 340px;
  }
}

@media (max-width: 640px) {
  .tryon-shell,
  .tryon-shell--modal {
    padding: 10px;
  }

  .tryon-panel {
    border-radius: 24px;
    padding: 20px 14px;
  }

  .close-button {
    top: 12px;
    right: 12px;
  }

  .tryon-header {
    grid-template-columns: 1fr;
    gap: 12px;
    padding-right: 38px;
  }

  .tryon-brand-mark {
    width: 52px;
    height: 52px;
  }

  .tryon-heading-copy h2 {
    font-size: 2rem;
  }

  .privacy-badge {
    white-space: normal;
  }

  .category-card {
    padding: 15px;
  }

  .media-card {
    border-radius: 22px;
    padding: 13px;
  }

  .upload-stage,
  .garment-stage {
    min-height: 300px;
  }

  .garment-meta,
  .result-header {
    align-items: flex-start;
    flex-direction: column;
  }

  .retry-button {
    width: 100%;
  }
}

@media (prefers-reduced-motion: reduce) {
  .tryon-panel,
  .media-card,
  .tryon-brand-mark,
  .ambient,
  .upload-icon-orbit::before,
  .action-shine,
  .progress-fill::after,
  .processing-icon svg,
  .spin {
    animation: none !important;
  }

  .upload-stage,
  .tryon-action,
  .retry-button,
  .close-button,
  .category-select__trigger,
  .category-select__option,
  .category-select__chevron {
    transition-duration: 1ms !important;
  }
}
</style>
