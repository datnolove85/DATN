import axios from 'axios'

const CLIENT_API_URL = 'http://localhost:8080/api/gamification'
const ADMIN_API_URL = 'http://localhost:8080/api/admin/gamification'

export default {
  // Lấy trạng thái ví xu và kiểm tra điểm danh hôm nay
  getTrangThai(idKhachHang) {
    return axios.get(`${CLIENT_API_URL}/trang-thai/${idKhachHang}`)
  },

  // Thực hiện điểm danh hàng ngày
  diemDanh(idKhachHang) {
    return axios.post(`${CLIENT_API_URL}/diem-danh/${idKhachHang}`)
  },

  // Quay thưởng minigame (loaiGame: 'vong_quay' hoặc 'lat_the')
  quayThuong(idKhachHang, loaiGame) {
    return axios.post(`${CLIENT_API_URL}/quay-thuong`, null, {
      params: { idKhachHang, loaiGame },
    })
  },

  // Lấy lịch sử biến động xu của khách hàng
  getLichSuXu(idKhachHang) {
    return axios.get(`${CLIENT_API_URL}/lich-su-xu/${idKhachHang}`)
  },

  // Lấy danh sách phần thưởng minigame
  getDanhSachPhanThuong() {
    return axios.get(`${ADMIN_API_URL}/phan-thuong`)
  },

  // ✨ BỔ SUNG: Lấy danh sách kho voucher có thể đổi bằng xu ✨
  getKhoVoucher() {
    return axios.get(`${CLIENT_API_URL}/kho-voucher`)
  },

  // ✨ BỔ SUNG: Thực hiện đổi xu lấy voucher ✨
  doiVoucher(idKhachHang, idKhoVoucher) {
    return axios.post(`${CLIENT_API_URL}/doi-voucher`, null, {
      params: { idKhachHang, idKhoVoucher },
    })
  },

  // ✨ BỔ SUNG: Lấy danh sách voucher cá nhân khách hàng đã đổi ✨
  getVoucherCuaToi(idKhachHang) {
    return axios.get(`${CLIENT_API_URL}/voucher-cua-toi/${idKhachHang}`)
  },
  getCauHinhDiemDanh() {
    // Gọi sang admin hoặc API cấu hình chung tùy theo route backend của bạn
    return axios.get(`http://localhost:8080/api/admin/gamification/cau-hinh-diem-danh`)
  },
}
