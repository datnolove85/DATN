import axios from 'axios'

const API_URL = 'http://localhost:8080/api/yeu-thich'

const yeuThichService = {
  // 1. Hàm chính
  getDanhSachYeuThich: async (idKhachHang) => {
    try {
      const token = sessionStorage.getItem('token')
      const response = await axios.get(`${API_URL}/${idKhachHang}`, {
        headers: { Authorization: `Bearer ${token}` },
      })
      return response.data
    } catch (error) {
      console.error('Lỗi khi lấy danh sách yêu thích:', error)
      throw error
    }
  },

  // Thêm hàm này để tương thích với chỗ nào trong confirmbuy.vue đang gọi .getDanhSach()
  getDanhSach: async (idKhachHang) => {
    return await yeuThichService.getDanhSachYeuThich(idKhachHang)
  },

  // 2. Thả tim hoặc Bỏ tim (Dùng @RequestParam nên body để null, truyền qua params)
  toggleYeuThich: async (idKhachHang, idSanPham) => {
    try {
      const token = sessionStorage.getItem('token')
      const response = await axios.post(`${API_URL}/toggle`, null, {
        headers: { Authorization: `Bearer ${token}` },
        params: { idKhachHang, idSanPham },
      })
      return response.data
    } catch (error) {
      console.error('Lỗi khi thay đổi trạng thái yêu thích:', error)
      throw error
    }
  },

  // 3. Kiểm tra đã thích hay chưa
  kiemTraDaThich: async (idKhachHang, idSanPham) => {
    try {
      const token = sessionStorage.getItem('token')
      const response = await axios.get(`${API_URL}/kiem-tra`, {
        headers: { Authorization: `Bearer ${token}` },
        params: { idKhachHang, idSanPham },
      })
      return response.data
    } catch (error) {
      console.error('Lỗi khi kiểm tra trạng thái yêu thích:', error)
      throw error
    }
  },
}

export default yeuThichService
