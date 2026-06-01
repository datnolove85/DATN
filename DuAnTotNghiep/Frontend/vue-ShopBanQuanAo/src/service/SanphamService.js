const API = 'http://localhost:8080/sanpham'

// ================= HANDLE RESPONSE =================
const handleResponse = async (response) => {
  const data = await response.json().catch(() => null)

  if (!response.ok) {
    const message = data?.message || 'Có lỗi xảy ra'
    throw new Error(message)
  }

  return data
}

// ================= GET ALL =================
export const getAllSanpham = async () => {
  try {
    const response = await fetch(API)
    return await handleResponse(response)
  } catch (error) {
    console.error('getAllSanpham error:', error)
    throw error
  }
}

// ================= GET BY ID =================
export const getSanphamById = async (id) => {
  try {
    const response = await fetch(`${API}/${id}`)
    return await handleResponse(response)
  } catch (error) {
    console.error('getSanphamById error:', error)
    throw error
  }
}

// ================= CREATE =================
export const createSanpham = async (data) => {
  try {
    const response = await fetch(API, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data),
    })

    return await handleResponse(response)
  } catch (error) {
    console.error('createSanpham error:', error)
    throw error
  }
}

// ================= UPDATE =================
export const updateSanpham = async (id, data) => {
  try {
    const response = await fetch(`${API}/${id}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data),
    })

    return await handleResponse(response)
  } catch (error) {
    console.error('updateSanpham error:', error)
    throw error
  }
}

// ================= DELETE =================
export const deleteSanpham = async (id) => {
  try {
    const response = await fetch(`${API}/${id}`, {
      method: 'DELETE',
    })

    return await response.text()
  } catch (error) {
    console.error('deleteSanpham error:', error)
    throw error
  }
}
