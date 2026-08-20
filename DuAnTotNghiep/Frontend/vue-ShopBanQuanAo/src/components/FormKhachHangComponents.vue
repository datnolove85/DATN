<template>
  <div class="employee-container">
    <div class="form-card">
      <div class="card-header">
        <div class="header-left">
          <h3 class="title">CHI TIẾT KHÁCH HÀNG</h3>
          <span class="sub-title">Quản lý thông tin và dữ liệu tích lũy thành viên</span>
        </div>
        <span class="id-badge">ID: {{ newKhachHang.maKhachHang || '---' }}</span>
      </div>

      <!-- Khu vực hiển thị thông tin thống kê chính -->
      <div class="stats-grid">
        <div class="stat-card">
          <span>Hạng Thành Viên</span>
          <strong>{{ newKhachHang.hangThanhVien || '---' }}</strong>
          <small class="stat-sub">Hạn: {{ formatDate(newKhachHang.ngayHetHanHang) }}</small>
        </div>
        <div class="stat-card">
          <span>Số Xu Tích Lũy</span>
          <strong class="text-amber">{{ newKhachHang.soDuXu?.toLocaleString() || 0 }} Xu</strong>
          <small class="stat-sub"
            >Gần nhất: {{ formatDate(newKhachHang.ngayDiemDanhGanNhat) }}</small
          >
        </div>
        <div class="stat-card">
          <span>Tổng Chi Têu</span>
          <strong class="text-blue">{{ newKhachHang.tongChiTieu?.toLocaleString() || 0 }} đ</strong>
          <small class="stat-sub">Mua cuối: {{ formatDate(newKhachHang.ngayMuaCuoi) }}</small>
        </div>
        <div class="stat-card">
          <span>Minigame & Ưu Đãi</span>
          <strong>{{ newKhachHang.soLuotLatThe || 0 }} Lượt lật thẻ</strong>
          <small class="stat-sub">Tạo: {{ formatDate(newKhachHang.ngayTao) }}</small>
        </div>
      </div>

      <div class="card-body">
        <!-- Sidebar: Ảnh, Trạng thái & Giới tính gọn gàng -->
        <div class="form-sidebar">
          <div class="avatar-section">
            <div class="avatar-wrapper" @click="$refs.fileInputRef.click()">
              <img
                v-if="previewUrl || newKhachHang.anh"
                :src="
                  previewUrl ||
                  `http://localhost:8080/api/shop-ban-quan-ao/khach-hang/uploads/${newKhachHang.anh}`
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

          <div class="input-group">
            <label>Tên tài khoản</label>
            <input type="text" :value="newKhachHang.tenTaiKhoan" disabled class="input-disabled" />
          </div>

          <div class="input-group mt-2">
            <label>Trạng thái</label>
            <div class="status-toggle">
              <button
                type="button"
                class="btn-status"
                :class="{ active: newKhachHang.trangThai === true }"
                @click="newKhachHang.trangThai = true"
              >
                <span class="dot"></span> Hoạt động
              </button>
              <button
                type="button"
                class="btn-status"
                :class="{ active: newKhachHang.trangThai === false }"
                @click="newKhachHang.trangThai = false"
              >
                <span class="dot"></span> Khóa
              </button>
            </div>
          </div>

          <!-- Chuyển giới tính vào sidebar để tối ưu không gian trắng -->
          <div class="input-group mt-2">
            <label>Giới tính</label>
            <div class="radio-group">
              <label class="radio-label">
                <input type="radio" :value="true" v-model="newKhachHang.gioiTinh" /> Nam
              </label>
              <label class="radio-label">
                <input type="radio" :value="false" v-model="newKhachHang.gioiTinh" /> Nữ
              </label>
            </div>
          </div>
        </div>

        <!-- Main Form -->
        <div class="form-main">
          <!-- Thông tin cá nhân -->
          <div class="form-section">
            <h4 class="section-title">THÔNG TIN CÁ NHÂN</h4>
            <div class="grid-row">
              <div class="input-group">
                <label>Họ và tên <span class="required">*</span></label>
                <input type="text" v-model="newKhachHang.hoTen" placeholder="Nhập họ và tên..." />
                <span class="error-text">{{ errors.hoTen }}</span>
              </div>
              <div class="input-group">
                <label>Ngày sinh <span class="required">*</span></label>
                <input type="date" v-model="newKhachHang.ngaySinh" />
                <span class="error-text">{{ errors.ngaySinh }}</span>
              </div>
            </div>

            <div class="grid-row">
              <div class="input-group">
                <label>Số điện thoại <span class="required">*</span></label>
                <input type="text" v-model="newKhachHang.soDienThoai" placeholder="090xxxxxxxx" />
                <span class="error-text">{{ errors.soDienThoai }}</span>
              </div>
              <div class="input-group">
                <label>Email <span class="required">*</span></label>
                <input type="email" v-model="newKhachHang.email" placeholder="email@domain.com" />
                <span class="error-text">{{ errors.email }}</span>
              </div>
            </div>
          </div>

          <!-- Địa chỉ liên lạc -->
          <div class="form-section no-border">
            <h4 class="section-title">ĐỊA CHỈ LIÊN LẠC</h4>
            <div class="grid-row three-cols mb-2">
              <select v-model="newKhachHang.thanhPho" @change="onProvinceChange">
                <option value="">Tỉnh/Thành phố</option>
                <option v-for="p in provinces" :key="p.code" :value="p.name">{{ p.name }}</option>
              </select>
              <select v-model="newKhachHang.quan" @change="onDistrictChange">
                <option value="">Quận/Huyện</option>
                <option v-for="d in districts" :key="d.code" :value="d.name">{{ d.name }}</option>
              </select>
              <select v-model="newKhachHang.phuong">
                <option value="">Phường/Xã</option>
                <option v-for="w in wards" :key="w.code" :value="w.name">{{ w.name }}</option>
              </select>
            </div>
            <div class="input-group">
              <input
                type="text"
                v-model="newKhachHang.diaChiCuThe"
                placeholder="Số nhà, tên đường cụ thể..."
              />
              <span class="error-text">{{ errors.diaChiCuThe }}</span>
            </div>
          </div>
        </div>
      </div>

      <div class="card-footer">
        <button class="btn-cancel" @click="goBack">Quay lại</button>
        <button class="btn-submit" @click="handleSave">Lưu thay đổi</button>
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
  addKhachHangService,
  detailKhachHangService,
  updateKhachHangService,
} from '@/service/KhachHangService'

const route = useRoute()
const router = useRouter()
const toast = useToast()

const isEdit = ref(false)
const editId = ref(null)

const provinces = ref([])
const districts = ref([])
const wards = ref([])

const previewUrl = ref(null)
const fileSelected = ref(null)

const newKhachHang = ref({
  anh: '',
  maKhachHang: '',
  tenTaiKhoan: '',
  hoTen: '',
  ngaySinh: '',
  gioiTinh: true,
  soDienThoai: '',
  email: '',
  trangThai: true,
  thanhPho: '',
  quan: '',
  phuong: '',
  diaChiCuThe: '',
  diaChiGop: '',
  hangThanhVien: 'Bạc',
  soDuXu: 0,
  tongChiTieu: 0,
  soLanMua: 0,
  chuoiDiemDanh: 0,
  soLuotLatThe: 0,
  ngayDiemDanhGanNhat: '',
  ngayMuaCuoi: '',
  ngayHetHanHang: '',
  ngayTao: '',
})

const errors = ref({
  hoTen: '',
  ngaySinh: '',
  soDienThoai: '',
  email: '',
  thanhPho: '',
  quan: '',
  phuong: '',
  diaChiCuThe: '',
})

const loadAddressDependencies = async () => {
  const normalize = (str) => {
    if (!str) return ''
    return str
      .toLowerCase()
      .replace(/^(tỉnh|thành phố|quận|huyện|phường|xã|thị trấn)\s+/i, '')
      .trim()
  }

  if (newKhachHang.value.thanhPho) {
    const province = provinces.value.find(
      (p) => normalize(p.name) === normalize(newKhachHang.value.thanhPho),
    )

    if (province) {
      // Cập nhật lại tên chuẩn từ API để thẻ select nhận diện và hiển thị đúng
      newKhachHang.value.thanhPho = province.name

      const res = await axios.get(`https://provinces.open-api.vn/api/p/${province.code}?depth=2`)
      districts.value = res.data.districts

      if (newKhachHang.value.quan) {
        const district = districts.value.find(
          (d) =>
            normalize(d.name) === normalize(newKhachHang.value.quan) ||
            d.name === newKhachHang.value.quan,
        )

        if (district) {
          newKhachHang.value.quan = district.name
          const resWards = await axios.get(
            `https://provinces.open-api.vn/api/d/${district.code}?depth=2`,
          )
          wards.value = resWards.data.wards

          if (newKhachHang.value.phuong) {
            const ward = wards.value.find(
              (w) =>
                normalize(w.name) === normalize(newKhachHang.value.phuong) ||
                w.name === newKhachHang.value.phuong,
            )
            if (ward) {
              newKhachHang.value.phuong = ward.name
            }
          }
        }
      }
    }
  }
}

const formatDate = (dateString) => {
  if (!dateString) return '---'
  return new Date(dateString).toLocaleDateString('vi-VN')
}

const validateForm = () => {
  let isValid = true
  Object.keys(errors.value).forEach((key) => (errors.value[key] = ''))

  if (!newKhachHang.value.hoTen?.trim()) {
    errors.value.hoTen = 'Họ và tên không được để trống'
    isValid = false
  }
  if (!newKhachHang.value.ngaySinh) {
    errors.value.ngaySinh = 'Vui lòng chọn ngày sinh'
    isValid = false
  }

  const phoneRegex = /^(0[3|5|7|8|9])([0-9]{8})$/
  if (!newKhachHang.value.soDienThoai) {
    errors.value.soDienThoai = 'Số điện thoại không được để trống'
    isValid = false
  } else if (!phoneRegex.test(newKhachHang.value.soDienThoai)) {
    errors.value.soDienThoai = 'SĐT không hợp lệ (10 số, bắt đầu bằng 0)'
    isValid = false
  }

  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
  if (!newKhachHang.value.email) {
    errors.value.email = 'Email không được để trống'
    isValid = false
  } else if (!emailRegex.test(newKhachHang.value.email)) {
    errors.value.email = 'Email không đúng định dạng'
    isValid = false
  }

  if (!newKhachHang.value.thanhPho) errors.value.thanhPho = 'Chọn Tỉnh/Thành'
  if (!newKhachHang.value.quan) errors.value.quan = 'Chọn Quận/Huyện'
  if (!newKhachHang.value.phuong) errors.value.phuong = 'Chọn Phường/Xã'

  return isValid
}

watch(
  () => newKhachHang.value,
  () => {
    Object.keys(errors.value).forEach((key) => {
      if (errors.value[key] && newKhachHang.value[key]) {
        errors.value[key] = ''
      }
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
  } catch (error) {
    console.error('Lỗi lấy tỉnh thành:', error)
  }
}

watch(
  () => newKhachHang.value.thanhPho,
  async (newVal) => {
    if (newVal) {
      const province = provinces.value.find((p) => p.name === newVal)
      if (province) {
        const res = await axios.get(`https://provinces.open-api.vn/api/p/${province.code}?depth=2`)
        districts.value = res.data.districts
      }
    } else {
      districts.value = []
      wards.value = []
    }
  },
)

watch(
  () => newKhachHang.value.quan,
  async (newVal) => {
    if (newVal) {
      const district = districts.value.find((d) => d.name === newVal)
      if (district) {
        const res = await axios.get(`https://provinces.open-api.vn/api/d/${district.code}?depth=2`)
        wards.value = res.data.wards
      }
    } else {
      wards.value = []
    }
  },
)

const onProvinceChange = () => {
  newKhachHang.value.quan = ''
  newKhachHang.value.phuong = ''
  wards.value = []
}

const onDistrictChange = () => {
  newKhachHang.value.phuong = ''
}

const goBack = () => router.push('/admin/khachhang')

const handleSave = async () => {
  if (!validateForm()) {
    toast.error('Vui lòng kiểm tra lại thông tin!')
    return
  }

  const parts = [
    newKhachHang.value.diaChiCuThe,
    newKhachHang.value.phuong,
    newKhachHang.value.quan,
    newKhachHang.value.thanhPho,
  ].filter((p) => p)
  newKhachHang.value.diaChiGop = parts.join(', ')

  try {
    if (isEdit.value) {
      await updateKhachHangService(editId.value, newKhachHang.value, fileSelected.value)
      toast.success('Cập nhật khách hàng thành công!')
    } else {
      await addKhachHangService(newKhachHang.value, fileSelected.value)
      toast.success('Thêm khách hàng thành công!')
    }
    goBack()
  } catch (error) {
    toast.error('Thao tác thất bại: ' + (error.response?.data?.message || error.message))
  }
}

onMounted(async () => {
  await fetchProvinces()

  if (route.params.id) {
    isEdit.value = true
    editId.value = route.params.id
    try {
      const data = await detailKhachHangService(editId.value)
      if (data) {
        newKhachHang.value = data
        if (newKhachHang.value.ngaySinh) {
          newKhachHang.value.ngaySinh = newKhachHang.value.ngaySinh.split('T')[0]
        }
        await loadAddressDependencies()
      }
    } catch (err) {
      toast.error('Lỗi khi tải thông tin chi tiết!')
      console.error(err)
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

.stats-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 10px;
  margin-bottom: 16px;
}

.stat-card {
  background: #f8fafc;
  padding: 10px 12px;
  border-radius: 8px;
  display: flex;
  flex-direction: column;
  border: 1px solid #e2e8f0;
}

.stat-card span {
  font-size: 10px;
  color: #64748b;
  text-transform: uppercase;
  font-weight: 700;
}

.stat-card strong {
  font-size: 14px;
  color: #1e293b;
  margin-top: 2px;
}

.stat-sub {
  font-size: 10px;
  color: #94a3b8;
  margin-top: 1px;
}

.text-amber {
  color: #d97706 !important;
}
.text-blue {
  color: #2563eb !important;
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

.input-disabled {
  background-color: #f1f5f9 !important;
  color: #64748b;
  cursor: not-allowed;
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
  justify-content: flex-end; /* Căn các nút về bên phải */
  align-items: center;
  gap: 10px; /* Tạo khoảng cách giữa nút Quay lại và Lưu thay đổi */
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
</style>
