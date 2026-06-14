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
    method: 'PUT', // Hoặc POST tùy vào controller của bạn
    body: formData,
  })

  if (!res.ok) {
    throw new Error('Lỗi cập nhật SPCT')
  }

  return await res.json()
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
