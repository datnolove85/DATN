const API = 'http://localhost:8080/hoadon'

// ================= HANDLE RESPONSE =================
const handleResponse = async (response) => {
  const data = await response.json().catch(() => null)

  if (!response.ok) {
    // 1. Ưu tiên lấy message từ backend (ApiException)
    const message = data?.message || `Lỗi hệ thống (${response.status})`
    const error = new Error(message)

    // 2. Gán thêm code lỗi (ví dụ: OUT_OF_STOCK, PRODUCT_DISABLED) để FE linh hoạt xử lý nếu cần
    error.code = data?.code || 'UNKNOWN_ERROR'
    error.data = data

    throw error
  }

  return data
}

export const searchHoadon = async (filters, page = 0, size = 20) => {
  try {
    const response = await fetch(`${API}/search?page=${page}&size=${size}`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(filters),
    })

    return await handleResponse(response)
  } catch (error) {
    console.error('searchHoadon error:', error)
    throw error
  }
}
export const searchHoadonOnline = async (filters, page = 0, size = 20) => {
  try {
    const response = await fetch(`${API}/online/search?page=${page}&size=${size}`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(filters),
    })

    return await handleResponse(response)
  } catch (error) {
    console.error('searchHoadonOnline error:', error)
    throw error
  }
}
// ================= THANH TOAN =================
export const thanhToanHoaDon = async (payload) => {
  try {
    // 1. Lấy token từ sessionStorage (giống như hàm updateTrangThai)
    const token = sessionStorage.getItem('token')

    const response = await fetch(`${API}/thanh-toan`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        // 2. Bổ sung thêm Authorization header ở đây để backend đọc được token
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify(payload),
    })

    return await handleResponse(response)
  } catch (error) {
    throw error
  }
}

export const thanhToanHoaDonOnline = async (payload) => {
  try {
    // Sửa lại URL cho khớp: không truyền id ở URL vì đã có trong payload
    const response = await fetch(`${API}/thanh-toan/online`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(payload),
    })

    return await handleResponse(response)
  } catch (error) {
    console.error('thanhToanHoaDon error:', error)
    throw error
  }
}
// ================= GET ALL =================
export const getAllHoadon = async () => {
  try {
    const response = await fetch(API)
    return await handleResponse(response)
  } catch (error) {
    console.error('getAllHoadon error:', error)
    throw error
  }
}

// ================= GET BY ID =================
export const getHoadonById = async (id) => {
  try {
    const response = await fetch(`${API}/${id}`)
    return await handleResponse(response)
  } catch (error) {
    console.error('getHoadonById error:', error)
    throw error
  }
}

// ================= CREATE =================
export const createHoadon = async (data) => {
  try {
    const response = await fetch(API, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(data),
    })

    return await handleResponse(response)
  } catch (error) {
    console.error('createHoadon error:', error)
    throw error
  }
}

// ================= UPDATE =================
export const updateHoadon = async (id, data) => {
  try {
    const response = await fetch(`${API}/update/${id}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(data),
    })

    return await handleResponse(response)
  } catch (error) {
    console.error('updateHoadon error:', error)
    throw error
  }
}

// ================= DELETE =================
export const deleteHoadon = async (id) => {
  try {
    const response = await fetch(`${API}/delete/${id}`, {
      method: 'DELETE',
    })

    if (!response.ok) {
      throw new Error(await response.text())
    }

    return await response.text()
  } catch (error) {
    console.error('deleteHoadon error:', error)
    throw error
  }
}
// ================= TAO HOA DON CHO =================
export const taoHoaDonCho = async (idNhanVien) => {
  try {
    const response = await fetch(`${API}/hoa-don-cho`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        idNhanVien,
      }),
    })

    return await handleResponse(response)
  } catch (error) {
    console.error('taoHoaDonCho error:', error)
    throw error
  }
}

// ================= TAO HOA DON CHO ONLINE =================
export const taoHoaDonOnline = async (data, token) => {
  const response = await fetch(`http://localhost:8080/hoadon/online`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      ...(token ? { Authorization: `Bearer ${token}` } : {}),
    },
    body: JSON.stringify(data),
  })

  return await handleResponse(response)
}

export const getHoaDonCho = async (idNhanVien) => {
  const response = await fetch(`${API}/hoa-don-cho?idNhanVien=${idNhanVien}`)

  return await handleResponse(response)
}
export const themSanPhamVaoHoaDon = async (data) => {
  try {
    const response = await fetch(`${API}/them-san-pham`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(data),
    })

    return await handleResponse(response)
  } catch (error) {
    console.error('themSanPhamVaoHoaDon error:', error)
    throw error
  }
}

export const giamSoLuongSanPham = async (idHdct) => {
  const response = await fetch(`${API}/chi-tiet/${idHdct}/giam`, {
    method: 'PUT',
  })

  return await handleResponse(response)
}

export const getChiTietHoaDon = async (idHoaDon) => {
  try {
    const response = await fetch(`${API}/${idHoaDon}/chi-tiet`)

    return await handleResponse(response)
  } catch (error) {
    console.error(error)
    throw error
  }
}
export const xoaSanPhamKhoiHoaDon = async (idHoaDonChiTiet) => {
  const response = await fetch(`${API}/chi-tiet/${idHoaDonChiTiet}`, {
    method: 'DELETE',
  })

  return await handleResponse(response)
}
export const tangSoLuongSanPham = async (idHdct) => {
  const response = await fetch(`${API}/chi-tiet/${idHdct}/tang`, {
    method: 'PUT',
  })

  return await handleResponse(response)
}
export const ganKhachHang = async (idHoaDon, idKhachHang) => {
  const response = await fetch('http://localhost:8080/hoadon/gan-khach-hang', {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({
      idHoaDon,
      idKhachHang,
    }),
  })

  if (!response.ok) {
    throw new Error('Không thể gán khách hàng')
  }

  return await response.text()
}
export const huyHoaDon = async (id) => {
  try {
    const token = sessionStorage.getItem('token')

    const response = await fetch(`${API}/huy/${id}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${token}`,
      },
    })

    return await handleResponse(response)
  } catch (error) {
    console.error('huyHoaDon error:', error)
    throw error
  }
}

export const apVoucher = async (idHoaDon, idVoucher, idVoucherKhachHang) => {
  try {
    const params = new URLSearchParams()

    // Chỉ append vào query nếu giá trị tồn tại và không null/undefined
    if (idVoucher !== null && idVoucher !== undefined) {
      params.append('idVoucher', idVoucher)
    }
    if (idVoucherKhachHang !== null && idVoucherKhachHang !== undefined) {
      params.append('idVoucherKhachHang', idVoucherKhachHang)
    }

    const response = await fetch(`${API}/${idHoaDon}/voucher?${params.toString()}`, {
      method: 'POST',
    })

    return await handleResponse(response)
  } catch (error) {
    console.error(error)
    throw error
  }
}
export const boVoucher = async (idHoaDon) => {
  try {
    const response = await fetch(`${API}/${idHoaDon}/voucher`, {
      method: 'DELETE',
    })

    return await handleResponse(response)
  } catch (error) {
    console.error(error)
    throw error
  }
}
export const getTraHangByHoaDon = async (idHoaDon) => {
  try {
    const response = await fetch(`${API}/trahang/${idHoaDon}`, {
      method: 'GET',
    })

    return await handleResponse(response)
  } catch (error) {
    console.error('getTraHangByHoaDon error:', error)
    throw error
  }
}

export const submitTraHang = async (payload) => {
  try {
    const response = await fetch(`${API}/tra-hang`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(payload),
    })

    return await handleResponse(response)
  } catch (error) {
    console.error('submitTraHang error:', error)
    throw error
  }
}
// Hủy hóa đơn online
export const huyHoaDonOnline = async (id) => {
  try {
    const token = sessionStorage.getItem('token')

    const response = await fetch(`${API}/online/${id}/cancel`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${token}`,
      },
    })

    return await handleResponse(response)
  } catch (error) {
    console.error('huyHoaDonOnline error:', error)
    throw error
  }
}
export const hoaDonService = {
  async updateTrangThai(id, trangThai) {
    const token = sessionStorage.getItem('token')

    const response = await fetch(`${API}/${id}/trang-thai`, {
      method: 'PATCH',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify({
        trangThai,
      }),
    })

    if (!response.ok) {
      const errorText = await response.text()
      throw new Error(errorText || 'Lỗi cập nhật trạng thái')
    }

    return await response.text()
  },
}
export const taoQr = async (hoaDonId, soTien) => {
  try {
    // Đính kèm ?amount=... nếu có truyền soTien vào
    const url = soTien
      ? `http://localhost:8080/hoadon/tao-qr/${hoaDonId}?amount=${soTien}`
      : `http://localhost:8080/hoadon/tao-qr/${hoaDonId}`

    const response = await fetch(url, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
    })

    return await handleResponse(response)
  } catch (error) {
    console.error('taoQr error:', error)
    throw error
  }
}
export const capNhatSoLuong = async (idHoaDonChiTiet, soLuong) => {
  try {
    const response = await fetch(`${API}/cap-nhat-so-luong/${idHoaDonChiTiet}?soLuong=${soLuong}`, {
      method: 'PUT',
    })

    return await handleResponse(response)
  } catch (error) {
    console.error('capNhatSoLuong error:', error)
    throw error
  }
}
export const goKhachHang = async (idHoaDon) => {
  const response = await fetch(`http://localhost:8080/hoadon/${idHoaDon}/go-khach-hang`, {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
    },
  })

  return await handleResponse(response)
}

// ================= AP DUNG XU =================
export const apDungXu = async (idHoaDon, soXu) => {
  try {
    const response = await fetch(`${API}/${idHoaDon}/ap-dung-xu?soXu=${soXu}`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
    })

    return await handleResponse(response)
  } catch (error) {
    console.error('apDungXu error:', error)
    throw error
  }
}
