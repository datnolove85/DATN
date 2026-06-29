const API = 'http://localhost:8080/hoadon'

// ================= HANDLE RESPONSE =================
const handleResponse = async (response) => {
  const data = await response.json().catch(() => null)

  if (!response.ok) {
    const message = data?.message || `HTTP Error: ${response.status}`
    throw new Error(message)
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

// ================= THANH TOAN =================
export const thanhToanHoaDon = async (payload) => {
  try {
    // Sửa lại URL cho khớp: không truyền id ở URL vì đã có trong payload
    const response = await fetch(`${API}/thanh-toan`, {
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
export const taoHoaDonCho = async () => {
  try {
    const response = await fetch(`${API}/hoa-don-cho`, {
      method: 'POST',
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
      Authorization: `Bearer ${token}`,
    },
    body: JSON.stringify(data),
  })

  return await handleResponse(response)
}

export const getHoaDonCho = async () => {
  try {
    const response = await fetch(`${API}/hoa-don-cho`)
    return await handleResponse(response)
  } catch (error) {
    console.error(error)
    throw error
  }
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
export const huyHoaDon = (id) => {
  return fetch(`http://localhost:8080/hoadon/huy/${id}`, {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
    },
  }).then((r) => {
    if (!r.ok) throw new Error('Hủy hóa đơn thất bại')
    return r.text()
  })
}

export const apVoucher = async (idHoaDon, idVoucher) => {
  try {
    const response = await fetch(`${API}/${idHoaDon}/voucher?idVoucher=${idVoucher}`, {
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
export const huyHoaDonOnline = async (idHoaDon) => {
  const res = await fetch(`http://localhost:8080/hoadon/online/${idHoaDon}/cancel`, {
    method: 'POST',
  })

  if (!res.ok) {
    const message = await res.text()
    throw new Error(message || 'Hủy hóa đơn thất bại')
  }

  return await res.text()
}
