<template>
  <div :class="['tryon-wrapper', { 'is-modal': isModal }]">
    <div class="tryon-card">
      <button v-if="isModal" class="btn-close" type="button" @click="$emit('close')">✕</button>

      <h2 class="title">✨ Thử đồ AI</h2>
      <p class="subtitle">
        Ảnh người thử được gửi tới backend; token Hugging Face không nằm trên trình duyệt.
      </p>

      <div class="category-selector">
        <label>Phân loại mặc:</label>
        <select v-model="selectedCategory">
          <option value="upper_body">Áo (Upper Body)</option>
          <option value="lower_body">Quần / Chân váy (Lower Body)</option>
          <option value="dresses">Đầm / Váy liền (Dresses)</option>
        </select>
      </div>

      <div class="upload-grid">
        <div class="upload-item">
          <span class="label">1. Ảnh toàn thân của bạn</span>
          <div class="preview-box" @click="triggerHumanInput">
            <img v-if="humanPreview" :src="humanPreview" class="img-preview" alt="Ảnh người thử" />
            <div v-else class="upload-placeholder">
              <span>📷 Chọn ảnh người thử</span>
            </div>
          </div>
          <input
            ref="humanInput"
            type="file"
            accept="image/*"
            class="hidden-input"
            @change="handleHumanFileChange"
          />
        </div>

        <div class="upload-item">
          <span class="label">2. Trang phục lấy từ sản phẩm đang chọn</span>
          <div class="preview-box garment-box">
            <img
              v-if="defaultGarmentUrl"
              :src="defaultGarmentUrl"
              class="img-preview"
              alt="Ảnh sản phẩm"
            />
            <div v-else class="upload-placeholder">
              <span>👕 Hãy chọn biến thể có ảnh</span>
            </div>
          </div>
          <p class="garment-note">Backend sẽ tự lấy ảnh theo SPCT #{{ effectiveSpctId || '—' }}</p>
        </div>
      </div>

      <div v-if="loading" class="progress-container">
        <div class="progress-bar" :style="{ width: progress + '%' }"></div>
        <span class="progress-text">AI đang xử lý... {{ progress }}%</span>
      </div>

      <button
        :disabled="loading || !humanFile || !effectiveSpctId"
        type="button"
        class="btn-primary"
        @click="startTryOn"
      >
        <span v-if="loading">⏳ Đang tạo ảnh thử đồ...</span>
        <span v-else>🚀 Bắt đầu thử đồ</span>
      </button>

      <p v-if="statusMessage" class="status-msg">{{ statusMessage }}</p>
      <p v-if="errorMessage" class="error-msg">{{ errorMessage }}</p>

      <div v-if="resultImg" class="result-box">
        <div class="result-header">
          <h3>Kết quả thử đồ</h3>
          <button class="btn-retry" type="button" :disabled="loading" @click="startTryOn">
            🔄 Thử lại
          </button>
        </div>
        <div class="result-image-wrapper">
          <img :src="resultImg" alt="Kết quả thử đồ AI" />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed, onUnmounted, ref, watch } from 'vue'
import { useRoute } from 'vue-router'
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
let progressTimer = null

const effectiveSpctId = computed(() => {
  const raw = props.spctId ?? route.query.spct
  const id = Number(raw)
  return Number.isInteger(id) && id > 0 ? id : null
})

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
    const img = new Image()
    const sourceUrl = URL.createObjectURL(file)

    img.onload = () => {
      const canvas = document.createElement('canvas')
      canvas.width = 768
      canvas.height = 1024
      const ctx = canvas.getContext('2d')
      ctx.fillStyle = '#FFFFFF'
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

const handleHumanFileChange = async (event) => {
  const file = event.target.files?.[0]
  if (!file) return

  errorMessage.value = ''
  try {
    const processed = await processImageCanvas(file)
    revokeObjectUrl(humanPreview.value)
    humanFile.value = processed
    humanPreview.value = URL.createObjectURL(processed)
  } catch (error) {
    errorMessage.value = error.message || 'Không thể đọc ảnh đã chọn'
  }
}

const startProgressBar = () => {
  progress.value = 0
  clearInterval(progressTimer)
  progressTimer = setInterval(() => {
    if (progress.value < 94) progress.value += 1
  }, 350)
}

const startTryOn = async () => {
  if (!humanFile.value || !effectiveSpctId.value) return

  loading.value = true
  errorMessage.value = ''
  statusMessage.value = 'Backend đang lấy ảnh sản phẩm và gửi hai ảnh lên dịch vụ AI...'
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
    statusMessage.value = 'Tạo ảnh thử đồ thành công.'
  } catch (error) {
    statusMessage.value = ''
    errorMessage.value = error.message || 'Không thể tạo ảnh thử đồ lúc này'
  } finally {
    clearInterval(progressTimer)
    loading.value = false
  }
}

onUnmounted(() => {
  clearInterval(progressTimer)
  revokeObjectUrl(humanPreview.value)
  revokeObjectUrl(resultImg.value)
})
</script>

<style scoped>
.tryon-wrapper {
  display: flex;
  justify-content: center;
  padding: 20px;
  font-family: 'Segoe UI', Roboto, sans-serif;
}
.tryon-wrapper.is-modal {
  position: fixed;
  inset: 0;
  z-index: 9999;
  align-items: flex-start;
  overflow-y: auto;
  background: rgba(15, 23, 42, 0.72);
  padding-top: 42px;
}
.tryon-card {
  position: relative;
  width: 100%;
  max-width: 720px;
  padding: 26px;
  border-radius: 18px;
  background: #fff;
  box-shadow: 0 20px 60px rgba(15, 23, 42, 0.22);
}
.btn-close {
  position: absolute;
  top: 14px;
  right: 14px;
  width: 34px;
  height: 34px;
  border: 0;
  border-radius: 50%;
  background: #f1f5f9;
  cursor: pointer;
}
.title {
  margin: 0 0 6px;
  color: #0f172a;
  font-size: 24px;
  font-weight: 800;
}
.subtitle {
  margin: 0 40px 20px 0;
  color: #64748b;
  font-size: 14px;
}
.category-selector {
  margin-bottom: 16px;
  font-size: 14px;
}
.category-selector select {
  margin-left: 8px;
  padding: 7px 10px;
  border: 1px solid #cbd5e1;
  border-radius: 8px;
}
.upload-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 16px;
}
.label {
  display: block;
  margin-bottom: 7px;
  color: #334155;
  font-size: 13px;
  font-weight: 700;
}
.preview-box {
  height: 270px;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
  border: 2px dashed #cbd5e1;
  border-radius: 14px;
  background: #f8fafc;
  cursor: pointer;
}
.preview-box:hover {
  border-color: #2563eb;
}
.garment-box {
  cursor: default;
  border-style: solid;
}
.img-preview {
  width: 100%;
  height: 100%;
  object-fit: contain;
  background: #fff;
}
.upload-placeholder {
  color: #64748b;
  font-size: 14px;
  text-align: center;
}
.hidden-input {
  display: none;
}
.garment-note {
  margin: 7px 0 0;
  color: #64748b;
  font-size: 12px;
}
.progress-container {
  position: relative;
  height: 28px;
  margin-top: 18px;
  overflow: hidden;
  border-radius: 999px;
  background: #e2e8f0;
}
.progress-bar {
  height: 100%;
  background: linear-gradient(90deg, #2563eb, #7c3aed);
  transition: width 0.25s;
}
.progress-text {
  position: absolute;
  inset: 0;
  display: grid;
  place-items: center;
  color: #fff;
  font-size: 12px;
  font-weight: 700;
}
.btn-primary {
  width: 100%;
  margin-top: 18px;
  padding: 14px;
  border: 0;
  border-radius: 13px;
  background: #2563eb;
  color: #fff;
  font-weight: 800;
  cursor: pointer;
}
.btn-primary:disabled {
  background: #94a3b8;
  cursor: not-allowed;
}
.status-msg {
  margin-top: 12px;
  color: #0369a1;
  font-size: 14px;
}
.error-msg {
  margin-top: 12px;
  padding: 10px 12px;
  border-radius: 10px;
  background: #fef2f2;
  color: #b91c1c;
  font-size: 14px;
}
.result-box {
  margin-top: 22px;
  padding-top: 18px;
  border-top: 1px solid #e2e8f0;
}
.result-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 10px;
}
.result-header h3 {
  margin: 0;
}
.btn-retry {
  padding: 7px 10px;
  border: 1px solid #cbd5e1;
  border-radius: 9px;
  background: #fff;
  cursor: pointer;
}
.result-image-wrapper {
  overflow: hidden;
  border-radius: 14px;
  background: #f8fafc;
  text-align: center;
}
.result-image-wrapper img {
  max-width: 100%;
  max-height: 720px;
  object-fit: contain;
}
@media (max-width: 640px) {
  .tryon-wrapper.is-modal {
    padding: 12px;
  }
  .tryon-card {
    padding: 20px 15px;
  }
  .upload-grid {
    grid-template-columns: 1fr;
  }
  .preview-box {
    height: 240px;
  }
}
</style>
