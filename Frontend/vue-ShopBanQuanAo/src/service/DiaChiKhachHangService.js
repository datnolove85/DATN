const API = 'http://localhost:8080/api/dia-chi'

function getHeaders() {
  const token = sessionStorage.getItem('token')

  return {
    'Content-Type': 'application/json',
    Authorization: `Bearer ${token}`,
  }
}

async function handleResponse(response) {
  if (!response.ok) {
    const message = await response.text()
    throw new Error(message || 'Có lỗi xảy ra')
  }

  // Trường hợp DELETE hoặc response không có body
  const text = await response.text()
  return text ? JSON.parse(text) : null
}

// Lấy danh sách địa chỉ
export const getAllDiaChi = async () => {
  const response = await fetch(API, {
    method: 'GET',
    headers: getHeaders(),
  })

  return handleResponse(response)
}

// Lấy địa chỉ mặc định
export const getDiaChiMacDinh = async () => {
  const response = await fetch(`${API}/mac-dinh`, {
    method: 'GET',
    headers: getHeaders(),
  })

  return handleResponse(response)
}

// Thêm địa chỉ
export const themDiaChi = async (body) => {
  const response = await fetch(API, {
    method: 'POST',
    headers: getHeaders(),
    body: JSON.stringify(body),
  })

  return handleResponse(response)
}

// Cập nhật địa chỉ
export const capNhatDiaChi = async (id, body) => {
  const response = await fetch(`${API}/${id}`, {
    method: 'PUT',
    headers: getHeaders(),
    body: JSON.stringify(body),
  })

  return handleResponse(response)
}

// Xóa địa chỉ
export const xoaDiaChi = async (id) => {
  const response = await fetch(`${API}/${id}`, {
    method: 'DELETE',
    headers: getHeaders(),
  })

  return handleResponse(response)
}

// Đổi địa chỉ mặc định
export const doiMacDinh = async (id) => {
  const response = await fetch(`${API}/mac-dinh/${id}`, {
    method: 'PUT',
    headers: getHeaders(),
  })

  return handleResponse(response)
}
