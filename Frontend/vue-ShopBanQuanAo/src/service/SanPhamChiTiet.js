const API = 'http://localhost:8080/spct'

// ================= GET ALL =================
export const getAllSanPhamChiTiet = async () => {
  const res = await fetch(API)

  if (!res.ok) {
    throw new Error('Lỗi lấy danh sách SPCT')
  }

  return await res.json()
}

export const getSanPhamChiTietByProductId = async (productId) => {
  const res = await fetch(`${API}/sp/${productId}`)

  if (!res.ok) {
    throw new Error('Lỗi lấy danh sách SPCT theo sản phẩm')
  }

  return await res.json()
}

// ================= GET BY ID =================
export const getSanPhamChiTietById = async (id) => {
  const res = await fetch(`${API}/detail/${id}`)

  if (!res.ok) {
    throw new Error('Lỗi lấy chi tiết SPCT')
  }

  return await res.json()
}
// ================= CREATE =================
export const createSanPhamChiTiet = async (formData) => {
  const res = await fetch(API, {
    method: 'POST',
    // KHÔNG ĐỂ 'Content-Type': 'application/json' ở đây.
    // Trình duyệt sẽ tự động thêm Content-Type: multipart/form-data
    // kèm theo boundary khi thấy bạn gửi FormData.
    body: formData,
  })

  if (!res.ok) {
    throw new Error('Lỗi thêm SPCT')
  }

  return await res.json()
}

// ================= UPDATE =================
export const updateSanPhamChiTiet = async (id, formData) => {
  const res = await fetch(`${API}/update/${id}`, {
    method: 'PUT', // ⚠️ Kiểm tra Backend bạn dùng @PutMapping hay @PostMapping nhé
    body: formData,
  })

  // Đọc dữ liệu JSON trả về từ Backend (chứa data và message)
  const data = await res.json()

  if (!res.ok) {
    // 🔴 Ném câu thông báo lỗi từ Backend ra ngoài khối catch
    throw new Error(data.message || data || 'Lỗi cập nhật SPCT')
  }

  return data // Trả về object { data: ..., message: "..." }
}
// ================= DELETE =================
export const deleteSanPhamChiTiet = async (id) => {
  const res = await fetch(`${API}/delete/${id}`, {
    method: 'DELETE',
  })

  if (!res.ok) {
    throw new Error('Lỗi xóa SPCT')
  }

  return await res.text()
}
export const createBulkSPCT = (payload) => {
  return fetch('http://localhost:8080/spct/bulk', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(payload),
  })
}

export const getThongKeSPCT = async (id) => {
  const res = await fetch(`http://localhost:8080/spct/sp/${id}/thong-ke-spct`)

  if (!res.ok) {
    throw new Error('Lỗi lấy thống kê SPCT')
  }

  return await res.text()
}

// ================= SHOP - GET VARIANTS =================
export const getShopVariantsByProductId = async (productId) => {
  const res = await fetch(`${API}/shop/${productId}`)

  if (!res.ok) {
    throw new Error('Lỗi lấy biến thể sản phẩm')
  }

  return await res.json()
}
