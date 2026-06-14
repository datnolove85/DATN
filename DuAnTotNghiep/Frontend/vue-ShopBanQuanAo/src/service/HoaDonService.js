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

export const thanhToanHoaDon = async (payload) => {
  const response = await axios.post('http://localhost:8080/hoadon/thanh-toan', payload)

  return response.data
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
