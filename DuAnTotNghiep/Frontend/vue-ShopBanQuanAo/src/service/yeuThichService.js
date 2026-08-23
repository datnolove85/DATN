import axios from 'axios'

// Định nghĩa URL gốc của API Backend
const API_URL = 'http://localhost:8080/api/yeu-thich'

const yeuThichService = {
  /**
   * Lấy danh sách sản phẩm yêu thích của khách hàng
   * @param {number} idKhachHang
   * @returns Promise
   */
  getDanhSach: async (idKhachHang) => {
    try {
      const response = await axios.get(`${API_URL}/${idKhachHang}`)
      return response.data
    } catch (error) {
      console.error('Lỗi khi lấy danh sách yêu thích:', error)
      throw error
    }
  },

  /**
   * Thả tim hoặc Bỏ tim sản phẩm (Toggle)
   * @param {number} idKhachHang
   * @param {number} idSanPham
   * @returns Promise (trả về kết quả từ API)
   */
  toggleYeuThich: async (idKhachHang, idSanPham) => {
    try {
      const response = await axios.post(`${API_URL}/toggle`, null, {
        params: {
          idKhachHang: idKhachHang,
          idSanPham: idSanPham,
        },
      })
      return response.data // Trả về dạng { success, message, data }
    } catch (error) {
      console.error('Lỗi khi thay đổi trạng thái yêu thích:', error)
      throw error
    }
  },

  /**
   * Kiểm tra xem khách hàng đã thích sản phẩm này chưa (dùng để đổi màu icon trái tim)
   * @param {number} idKhachHang
   * @param {number} idSanPham
   * @returns Promise<boolean>
   */
  kiemTraDaThich: async (idKhachHang, idSanPham) => {
    try {
      const response = await axios.get(`${API_URL}/kiem-tra`, {
        params: {
          idKhachHang: idKhachHang,
          idSanPham: idSanPham,
        },
      })
      return response.data // Trả về true hoặc false
    } catch (error) {
      console.error('Lỗi khi kiểm tra trạng thái thích:', error)
      return false
    }
  },
}

export default yeuThichService
