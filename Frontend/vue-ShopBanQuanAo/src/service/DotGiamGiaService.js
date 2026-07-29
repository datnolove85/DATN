import { apiFetch } from './api'

// ======================================
// ĐỢT GIẢM GIÁ
// ======================================

// Danh sách đợt giảm giá
export const getAllDotGiamGia = () => {
  return apiFetch('/dot-giam-gia')
}

// Chi tiết đợt giảm giá
export const getDotGiamGiaById = (id) => {
  return apiFetch(`/dot-giam-gia/${id}`)
}

// Tạo đợt giảm giá
export const createDotGiamGia = (payload) => {
  return apiFetch('/dot-giam-gia', {
    method: 'POST',
    body: JSON.stringify(payload),
  })
}

// Cập nhật
export const updateDotGiamGia = (id, payload) => {
  return apiFetch(`/dot-giam-gia/${id}`, {
    method: 'PUT',
    body: JSON.stringify(payload),
  })
}

// Xóa
export const deleteDotGiamGia = (id) => {
  return apiFetch(`/dot-giam-gia/${id}`, {
    method: 'DELETE',
  })
}

export const doiTrangThaiDotGiamGia = async (id) => {
  const response = await fetch(`http://localhost:8080/dot-giam-gia/${id}/doi-trang-thai`, {
    method: 'PUT',
  })

  return await response.text()
}

// ======================================
// SẢN PHẨM TRONG ĐỢT GIẢM GIÁ
// ======================================

// Danh sách sản phẩm đang áp dụng
export const getSanPhamTrongDot = (idDot) => {
  return apiFetch(`/dot-giam-gia/${idDot}/san-pham`)
}

// Danh sách sản phẩm CHƯA áp dụng
export const getSanPhamChuaApDung = (idDot) => {
  return apiFetch(`/dot-giam-gia/${idDot}/san-pham-chua-ap-dung`)
}

// Thêm sản phẩm
export const themSanPhamGiamGia = (idDot, payload) => {
  return apiFetch(`/dot-giam-gia/${idDot}/san-pham`, {
    method: 'POST',
    body: JSON.stringify(payload),
  })
}

// Xóa sản phẩm khỏi đợt
export const xoaSanPhamGiamGia = (idDot, idSPCT) => {
  return apiFetch(`/dot-giam-gia/${idDot}/san-pham/${idSPCT}`, {
    method: 'DELETE',
  })
}
