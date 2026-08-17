import axios from 'axios'

const API_BASE_URL = 'http://localhost:8080/api/admin/cau-hinh'

export const cauHinhService = {
  // ================= CẤU HÌNH HỆ THỐNG =================

  // Lấy danh sách cấu hình hệ thống
  getCauHinhHeThong: async () => {
    const response = await axios.get(`${API_BASE_URL}/he-thong`)
    return response.data
  },

  // Cập nhật giá trị cấu hình hệ thống theo mã
  updateCauHinhHeThong: async (maCauHinh, giaTriSo) => {
    const response = await axios.put(`${API_BASE_URL}/he-thong/${maCauHinh}`, null, {
      params: { giaTriSo },
    })
    return response.data
  },

  // ================= CẤU HÌNH HẠNG THÀNH VIÊN =================

  // Lấy danh sách các hạng thành viên
  getHangThanhVien: async () => {
    const response = await axios.get(`${API_BASE_URL}/hang-thanh-vien`)
    return response.data
  },

  // Thêm mới hạng thành viên
  createHangThanhVien: async (data) => {
    const response = await axios.post(`${API_BASE_URL}/hang-thanh-vien`, data)
    return response.data
  },

  // Cập nhật hạng thành viên theo ID
  updateHangThanhVien: async (id, data) => {
    const response = await axios.put(`${API_BASE_URL}/hang-thanh-vien/${id}`, data)
    return response.data
  },

  // Xóa hạng thành viên theo ID
  deleteHangThanhVien: async (id) => {
    const response = await axios.delete(`${API_BASE_URL}/hang-thanh-vien/${id}`)
    return response.data
  },
}
