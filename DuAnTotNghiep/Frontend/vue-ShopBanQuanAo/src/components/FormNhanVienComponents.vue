<template>
  <div class="employee-container">
    <div class="form-card">
      <div class="card-header">
        <div class="header-left">
          <h3 class="title">{{ isEdit ? 'CHI TIẾT NHÂN VIÊN' : 'THÊM MỚI NHÂN VIÊN' }}</h3>
          <span class="sub-title">Quản lý thông tin và phân quyền hệ thống nhân sự</span>
        </div>
        <span class="id-badge" v-if="newNhanVien.maNhanVien">ID: {{ newNhanVien.maNhanVien }}</span>
      </div>

      <div class="card-body">
        <!-- Sidebar: Ảnh, Trạng thái & Giới tính gọn gàng -->
        <div class="form-sidebar">
          <div class="avatar-section">
            <div class="avatar-wrapper" @click="$refs.fileInputRef.click()">
              <img
                v-if="previewUrl || newNhanVien.anh"
                :src="
                  previewUrl ||
                  `http://localhost:8080/api/shop-ban-quan-ao/nhan-vien/uploads/${newNhanVien.anh}`
                "
                class="avatar-img"
              />
              <div v-else class="placeholder"><span>Ảnh</span></div>
              <div class="overlay"><span>Đổi ảnh</span></div>
            </div>
            <input
              type="file"
              ref="fileInputRef"
              class="hidden-input"
              @change="handleFile"
              accept="image/*"
            />
          </div>

          <div class="input-group mt-2">
            <label>Trạng thái</label>
            <div class="status-toggle">
              <button
                type="button"
                class="btn-status"
                :class="{ active: newNhanVien.trangThai === 1 }"
                @click="newNhanVien.trangThai = 1"
              >
                <span class="dot"></span> Làm việc
              </button>
              <button
                type="button"
                class="btn-status"
                :class="{ active: newNhanVien.trangThai === 0 }"
                @click="newNhanVien.trangThai = 0"
              >
                <span class="dot"></span> Nghỉ việc
              </button>
            </div>
          </div>

          <div class="input-group mt-2">
            <label>Giới tính <span class="required">*</span></label>
            <div class="radio-group">
              <label class="radio-label">
                <input type="radio" :value="true" v-model="newNhanVien.gioiTinh" /> Nam
              </label>
              <label class="radio-label">
                <input type="radio" :value="false" v-model="newNhanVien.gioiTinh" /> Nữ
              </label>
            </div>
          </div>
        </div>

        <!-- Main Form -->
        <div class="form-main">
          <!-- Thông tin cá nhân & Tài khoản -->
          <div class="form-section">
            <h4 class="section-title">THÔNG TIN CÁ NHÂN & TÀI KHOẢN</h4>
            <div class="grid-row">
              <div class="input-group">
                <label>Họ và tên <span class="required">*</span></label>
                <input
                  type="text"
                  v-model="newNhanVien.tenNhanVien"
                  :class="{ 'input-error': errors.tenNhanVien }"
                  placeholder="Nhập họ và tên..."
                />
                <span class="error-text">{{ errors.tenNhanVien }}</span>
              </div>
              <div class="input-group">
                <label>Ngày sinh <span class="required">*</span></label>
                <input
                  type="date"
                  v-model="newNhanVien.ngaySinh"
                  :class="{ 'input-error': errors.ngaySinh }"
                />
                <span class="error-text">{{ errors.ngaySinh }}</span>
              </div>
            </div>

            <div class="grid-row">
              <div class="input-group">
                <label>Số điện thoại <span class="required">*</span></label>
                <input
                  type="text"
                  v-model="newNhanVien.soDienThoai"
                  :class="{ 'input-error': errors.soDienThoai }"
                  placeholder="090xxxxxxxx"
                />
                <span class="error-text">{{ errors.soDienThoai }}</span>
              </div>
              <div class="input-group">
                <label>Email <span class="required">*</span></label>
                <input
                  type="email"
                  v-model="newNhanVien.email"
                  :class="{ 'input-error': errors.email }"
                  placeholder="email@domain.com"
                />
                <span class="error-text">{{ errors.email }}</span>
              </div>
            </div>

            <div class="grid-row">
              <div class="input-group">
                <label>CCCD <span class="required">*</span></label>
                <input
                  type="text"
                  v-model="newNhanVien.canCuocCongDan"
                  :class="{ 'input-error': errors.canCuocCongDan }"
                  placeholder="12 chữ số"
                />
                <span class="error-text">{{ errors.canCuocCongDan }}</span>
              </div>
              <div class="input-group">
                <label>Mật khẩu <span class="required">*</span></label>
                <input
                  type="password"
                  v-model="newNhanVien.matKhau"
                  :class="{ 'input-error': errors.matKhau }"
                  placeholder="••••••"
                />
                <span class="error-text">{{ errors.matKhau }}</span>
              </div>
            </div>

            <div class="grid-row">
              <div class="input-group">
                <label>Quyền hạn <span class="required">*</span></label>
                <select v-model="newNhanVien.idVaiTro" :class="{ 'input-error': errors.idVaiTro }">
                  <option :value="null">Chọn quyền hạn</option>
                  <option v-for="role in listVaiTro" :key="role.id" :value="role.id">
                    {{ role.tenVaiTro }}
                  </option>
                </select>
                <span class="error-text">{{ errors.idVaiTro }}</span>
              </div>
            </div>
          </div>

          <!-- Địa chỉ liên lạc -->
          <div class="form-section no-border">
            <h4 class="section-title">ĐỊA CHỈ LIÊN LẠC</h4>
            <div class="grid-row three-cols mb-2">
              <select
                v-model="selectedTinh"
                @change="onProvinceChange(true)"
                :class="{ 'input-error': errors.diaChi }"
              >
                <option value="">Tỉnh/Thành phố</option>
                <option v-for="p in provinces" :key="p.code" :value="p.name">{{ p.name }}</option>
              </select>
              <select
                v-model="selectedHuyen"
                @change="onDistrictChange(true)"
                :class="{ 'input-error': errors.diaChi }"
              >
                <option value="">Quận/Huyện</option>
                <option v-for="d in districts" :key="d.code" :value="d.name">{{ d.name }}</option>
              </select>
              <select v-model="selectedXa" :class="{ 'input-error': errors.diaChi }">
                <option value="">Phường/Xã</option>
                <option v-for="w in wards" :key="w.code" :value="w.name">{{ w.name }}</option>
              </select>
            </div>
            <div class="input-group">
              <input
                type="text"
                v-model="diaChiChiTiet"
                :class="{ 'input-error': errors.diaChiChiTiet }"
                placeholder="Số nhà, tên đường cụ thể..."
              />
              <span class="error-text">{{ errors.diaChiChiTiet || errors.diaChi }}</span>
            </div>
          </div>
        </div>
      </div>

      <div class="card-footer">
        <button class="btn-cancel" @click="$router.back()">Quay lại</button>
        <button class="btn-submit" @click="handleSave">
          {{ isEdit ? 'Lưu thay đổi' : 'Thêm nhân viên' }}
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useToast } from 'vue-toastification'
import axios from 'axios'
import {
  addNhanVienService,
  detailNhanVienService,
  updateNhanVienService,
} from '@/service/NhanVienService'
import { getAllVaiTroService } from '@/service/VaiTroService'

const route = useRoute()
const router = useRouter()
const toast = useToast()

const SEPARATOR = ', '
const isEdit = ref(false)
const editId = ref(null)

const listVaiTro = ref([])
const provinces = ref([])
const districts = ref([])
const wards = ref([])

const selectedTinh = ref('')
const selectedHuyen = ref('')
const selectedXa = ref('')
const diaChiChiTiet = ref('')

const newNhanVien = ref({
  anh: '',
  maNhanVien: '',
  tenNhanVien: '',
  idVaiTro: null,
  gioiTinh: true,
  ngaySinh: '',
  email: '',
  soDienThoai: '',
  canCuocCongDan: '',
  matKhau: '',
  diaChi: '',
  trangThai: 1,
})

const errors = ref({
  tenNhanVien: '',
  ngaySinh: '',
  email: '',
  soDienThoai: '',
  canCuocCongDan: '',
  matKhau: '',
  idVaiTro: '',
  diaChi: '',
  diaChiChiTiet: '',
})

const previewUrl = ref(null)
const fileSelected = ref(null)

const validateForm = () => {
  let isValid = true
  Object.keys(errors.value).forEach((key) => (errors.value[key] = ''))

  if (!newNhanVien.value.tenNhanVien?.trim()) {
    errors.value.tenNhanVien = 'Họ tên không được để trống'
    isValid = false
  }

  if (!newNhanVien.value.ngaySinh) {
    errors.value.ngaySinh = 'Vui lòng chọn ngày sinh'
    isValid = false
  } else {
    const age = new Date().getFullYear() - new Date(newNhanVien.value.ngaySinh).getFullYear()
    if (age < 18) {
      errors.value.ngaySinh = 'Nhân viên phải từ 18 tuổi trở lên'
      isValid = false
    }
  }

  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
  if (!newNhanVien.value.email) {
    errors.value.email = 'Email không được để trống'
    isValid = false
  } else if (!emailRegex.test(newNhanVien.value.email)) {
    errors.value.email = 'Email không đúng định dạng'
    isValid = false
  }

  const phoneRegex = /^(0[3|5|7|8|9])([0-9]{8})$/
  if (!newNhanVien.value.soDienThoai) {
    errors.value.soDienThoai = 'Số điện thoại không được để trống'
    isValid = false
  } else if (!phoneRegex.test(newNhanVien.value.soDienThoai)) {
    errors.value.soDienThoai = 'SĐT không hợp lệ (10 số, bắt đầu bằng 0)'
    isValid = false
  }

  if (!newNhanVien.value.canCuocCongDan) {
    errors.value.canCuocCongDan = 'CCCD không được để trống'
    isValid = false
  } else if (!/^\d{12}$/.test(newNhanVien.value.canCuocCongDan)) {
    errors.value.canCuocCongDan = 'CCCD phải gồm 12 ký tự số'
    isValid = false
  }

  if (!isEdit.value || newNhanVien.value.matKhau) {
    if (!newNhanVien.value.matKhau || newNhanVien.value.matKhau.length < 6) {
      errors.value.matKhau = 'Mật khẩu phải từ 6 ký tự'
      isValid = false
    }
  }

  if (!newNhanVien.value.idVaiTro) {
    errors.value.idVaiTro = 'Vui lòng chọn quyền hạn'
    isValid = false
  }

  if (!selectedTinh.value || !selectedHuyen.value || !selectedXa.value) {
    errors.value.diaChi = 'Vui lòng chọn đầy đủ địa chỉ'
    isValid = false
  }

  if (!diaChiChiTiet.value?.trim()) {
    errors.value.diaChiChiTiet = 'Vui lòng nhập địa chỉ cụ thể'
    isValid = false
  }

  return isValid
}

watch(
  () => newNhanVien.value,
  () => {
    Object.keys(errors.value).forEach((key) => {
      if (newNhanVien.value[key]) errors.value[key] = ''
    })
  },
  { deep: true },
)

const handleFile = (event) => {
  const file = event.target.files[0]
  if (file) {
    if (file.size > 2 * 1024 * 1024) {
      toast.warning('Ảnh không được vượt quá 2MB')
      return
    }
    fileSelected.value = file
    previewUrl.value = URL.createObjectURL(file)
  }
}

const fetchProvinces = async () => {
  try {
    const res = await axios.get('https://provinces.open-api.vn/api/p/')
    provinces.value = res.data
  } catch (e) {
    console.error('Lỗi tải tỉnh thành:', e)
  }
}

const onProvinceChange = async (isManual = true) => {
  if (isManual) {
    selectedHuyen.value = ''
    selectedXa.value = ''
    districts.value = []
    wards.value = []
  }
  const p = provinces.value.find((i) => i.name === selectedTinh.value)
  if (p) {
    const res = await axios.get(`https://provinces.open-api.vn/api/p/${p.code}?depth=2`)
    districts.value = res.data.districts
  }
}

const onDistrictChange = async (isManual = true) => {
  if (isManual) {
    selectedXa.value = ''
    wards.value = []
  }
  const d = districts.value.find((i) => i.name === selectedHuyen.value)
  if (d) {
    const res = await axios.get(`https://provinces.open-api.vn/api/d/${d.code}?depth=2`)
    wards.value = res.data.wards
  }
}

const handleSave = async () => {
  if (!validateForm()) {
    toast.error('Vui lòng kiểm tra lại thông tin!')
    return
  }

  const parts = [
    diaChiChiTiet.value,
    selectedXa.value,
    selectedHuyen.value,
    selectedTinh.value,
  ].filter((p) => p)
  newNhanVien.value.diaChi = parts.join(SEPARATOR)

  try {
    if (isEdit.value) {
      await updateNhanVienService(editId.value, newNhanVien.value, fileSelected.value)
      toast.success('Cập nhật nhân viên thành công!')
    } else {
      await addNhanVienService(newNhanVien.value, fileSelected.value)
      toast.success('Thêm nhân viên mới thành công!')
    }
    router.push('/admin/nhan-vien')
  } catch (e) {
    toast.error(e.response?.data?.message || 'Có lỗi xảy ra!')
  }
}

onMounted(async () => {
  const roles = await getAllVaiTroService()
  listVaiTro.value = roles
  await fetchProvinces()

  if (route.params.id) {
    isEdit.value = true
    editId.value = route.params.id
    try {
      const data = await detailNhanVienService(route.params.id)
      newNhanVien.value = data
      if (data.diaChi) {
        const parts = data.diaChi.split(SEPARATOR).map((p) => p.trim())
        if (parts.length >= 4) {
          selectedTinh.value = parts.pop()
          await onProvinceChange(false)
          selectedHuyen.value = parts.pop()
          await onDistrictChange(false)
          selectedXa.value = parts.pop()
          diaChiChiTiet.value = parts.join(SEPARATOR)
        } else {
          diaChiChiTiet.value = data.diaChi
        }
      }
    } catch (error) {
      toast.error('Không thể tải thông tin nhân viên!')
    }
  }
})
</script>

<style scoped>
.employee-container {
  padding: 16px 12px;
  background-color: #f1f5f9;
  min-height: 100vh;
  display: flex;
  justify-content: center;
}

.form-card {
  background: #ffffff;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.03);
  width: 100%;
  max-width: 1050px;
  padding: 24px;
  height: fit-content;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
  padding-bottom: 12px;
  border-bottom: 1px solid #f1f5f9;
}

.header-left .title {
  color: #0f172a;
  font-weight: 700;
  font-size: 18px;
  margin: 0;
}

.sub-title {
  font-size: 11px;
  color: #64748b;
}

.id-badge {
  background-color: #eff6ff;
  color: #1d4ed8;
  padding: 4px 10px;
  border-radius: 6px;
  font-size: 12px;
  font-weight: 600;
  border: 1px solid #bfdbfe;
}

.card-body {
  display: flex;
  gap: 24px;
}

.form-sidebar {
  width: 220px;
  flex-shrink: 0;
}

.form-main {
  flex: 1;
}

.avatar-section {
  display: flex;
  justify-content: center;
  margin-bottom: 14px;
}

.avatar-wrapper {
  width: 110px;
  height: 110px;
  border-radius: 50%;
  border: 2px dashed #cbd5e1;
  position: relative;
  overflow: hidden;
  cursor: pointer;
  background-color: #f8fafc;
  display: flex;
  justify-content: center;
  align-items: center;
  transition: all 0.2s ease;
}

.avatar-wrapper:hover {
  border-color: #3b82f6;
}

.avatar-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.placeholder span {
  color: #94a3b8;
  font-weight: 600;
  font-size: 12px;
}

.hidden-input {
  display: none;
}

.overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.4);
  border-radius: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
  opacity: 0;
  transition: opacity 0.2s ease;
  color: white;
  font-size: 11px;
  font-weight: 600;
}

.avatar-wrapper:hover .overlay {
  opacity: 1;
}

.section-title {
  color: #1e3a8a;
  font-size: 11px;
  font-weight: 700;
  margin-bottom: 10px;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.form-section {
  border-bottom: 1px solid #f1f5f9;
  padding-bottom: 12px;
  margin-bottom: 12px;
}

.no-border {
  border: none;
  margin-bottom: 0;
  padding-bottom: 0;
}

.grid-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 12px;
  margin-bottom: 10px;
}

.three-cols {
  grid-template-columns: 1fr 1fr 1fr;
}

.input-group {
  display: flex;
  flex-direction: column;
  gap: 3px;
}

.input-group label {
  font-size: 11px;
  font-weight: 600;
  color: #475569;
}

.required {
  color: #ef4444;
}

input,
select {
  height: 36px;
  padding: 0 10px;
  border: 1px solid #cbd5e1;
  border-radius: 6px;
  outline: none;
  background-color: #ffffff;
  font-size: 12px;
  transition: all 0.2s;
}

input:focus,
select:focus {
  border-color: #3b82f6;
  box-shadow: 0 0 0 2px rgba(59, 130, 246, 0.1);
}

.input-error {
  border-color: #ef4444 !important;
  background-color: #fef2f2;
}

.error-text {
  color: #ef4444;
  font-size: 10px;
  min-height: 12px;
}

.radio-group {
  display: flex;
  gap: 16px;
  height: 36px;
  align-items: center;
}

.radio-label {
  display: flex;
  align-items: center;
  gap: 5px;
  cursor: pointer;
  font-size: 12px;
  color: #334155;
}

.status-toggle {
  display: flex;
  gap: 6px;
}

.btn-status {
  flex: 1;
  height: 34px;
  background: white;
  border: 1px solid #cbd5e1;
  border-radius: 6px;
  color: #64748b;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 5px;
  font-size: 11px;
  font-weight: 600;
  transition: all 0.2s;
}

.btn-status.active {
  background: #f0fdf4;
  border-color: #22c55e;
  color: #166534;
  box-shadow: 0 1px 2px rgba(34, 197, 94, 0.1);
}

.btn-status .dot {
  width: 5px;
  height: 5px;
  border-radius: 50%;
  background: #cbd5e1;
}

.btn-status.active .dot {
  background: #22c55e;
}

.card-footer {
  margin-top: 14px;
  padding-top: 12px;
  border-top: 1px solid #f1f5f9;
  display: flex;
  justify-content: flex-end;
  align-items: center;
  gap: 10px;
}

.btn-cancel {
  padding: 8px 16px;
  background: white;
  border: 1px solid #cbd5e1;
  border-radius: 6px;
  cursor: pointer;
  font-weight: 600;
  font-size: 12px;
  color: #475569;
  transition: all 0.2s;
}

.btn-cancel:hover {
  background: #f8fafc;
}

.btn-submit {
  padding: 8px 24px;
  background: #2563eb;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-weight: 600;
  font-size: 12px;
  box-shadow: 0 2px 4px rgba(37, 99, 235, 0.2);
  transition: all 0.2s;
}

.btn-submit:hover {
  background: #1d4ed8;
  box-shadow: 0 4px 6px rgba(37, 99, 235, 0.3);
}

.mt-2 {
  margin-top: 8px;
}
.mb-2 {
  margin-bottom: 8px;
}
</style>
