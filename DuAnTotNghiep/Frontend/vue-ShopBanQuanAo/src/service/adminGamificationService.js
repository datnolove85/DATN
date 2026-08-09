import axios from 'axios'

const ADMIN_API_URL = 'http://localhost:8080/api/admin/gamification'

export default {
  // Lấy danh sách toàn bộ phần thưởng minigame
  getAllPhanThuong() {
    return axios.get(`${ADMIN_API_URL}/phan-thuong`)
  },

  // Thêm mới phần thưởng
  createPhanThuong(data) {
    return axios.post(`${ADMIN_API_URL}/phan-thuong`, data)
  },

  // Cập nhật phần thưởng theo ID
  updatePhanThuong(id, data) {
    return axios.put(`${ADMIN_API_URL}/phan-thuong/${id}`, data)
  },

  // Xóa phần thưởng theo ID
  deletePhanThuong(id) {
    return axios.delete(`${ADMIN_API_URL}/phan-thuong/${id}`)
  },

  // Lấy lịch sử biến động xu toàn hệ thống
  getAllLichSuXu() {
    return axios.get(`${ADMIN_API_URL}/lich-su-xu`)
  },
  getAllKhoVoucher() {
    return axios.get(`${ADMIN_API_URL}/kho-voucher`)
  },
  // ==========================================
  // IV. CẤU HÌNH ĐIỂM DANH HẰNG NGÀY
  // ==========================================
  getCauHinhDiemDanh() {
    return axios.get(`${ADMIN_API_URL}/cau-hinh-diem-danh`)
  },
  createCauHinhDiemDanh(data) {
    return axios.post(`${ADMIN_API_URL}/cau-hinh-diem-danh`, data)
  },
  updateCauHinhDiemDanh(id, data) {
    return axios.put(`${ADMIN_API_URL}/cau-hinh-diem-danh/${id}`, data)
  },
  deleteCauHinhDiemDanh(id) {
    return axios.delete(`${ADMIN_API_URL}/cau-hinh-diem-danh/${id}`)
  },
  generateCauHinhCapSoCong(data) {
    // data chứa { totalDays, initialValue, step }
    return axios.post(`${ADMIN_API_URL}/cau-hinh-diem-danh/generate`, null, {
      params: data,
    })
  },
  // ==========================================
  // V. QUẢN LÝ SỐ LƯỢT LẬT THẺ
  // ==========================================

  // Cập nhật số lượt lật thẻ cho 1 khách hàng cụ thể
  updateSoLuotLatTheKhachHang(idKhachHang, soLuot) {
    return axios.post(`${ADMIN_API_URL}/so-luot-lat-the/khach-hang/${idKhachHang}`, null, {
      params: { soLuot },
    })
  },

  // Cập nhật số lượt lật thẻ cho toàn bộ hệ thống
  updateSoLuotLatTheToanBo(soLuot) {
    return axios.post(`${ADMIN_API_URL}/so-luot-lat-the/toan-bo`, null, {
      params: { soLuot },
    })
  },
}
