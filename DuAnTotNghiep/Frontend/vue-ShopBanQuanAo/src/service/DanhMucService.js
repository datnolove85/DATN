const API = 'http://localhost:8080/danhmuc'

// ================= GET ALL =================
import { apiFetch } from './api'

export const getAllDanhMuc = () => apiFetch('/danhmuc')

// ================= GET BY ID =================
export const getDanhMucById = async (id) => {
  try {
    const response = await fetch(`${API}/${id}`)

    if (!response.ok) {
      throw new Error('Lỗi khi lấy danh mục')
    }

    return await response.json()
  } catch (error) {
    console.error('getDanhMucById error:', error)
    throw error
  }
}

// ================= CREATE =================
export const createDanhMuc = async (data) => {
  try {
    const response = await fetch(API, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(data),
    })

    if (!response.ok) {
      throw new Error('Lỗi khi thêm danh mục')
    }

    return await response.json()
  } catch (error) {
    console.error('createDanhMuc error:', error)
    throw error
  }
}

// ================= UPDATE =================
export const updateDanhMuc = async (id, data) => {
  try {
    const response = await fetch(`${API}/${id}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(data),
    })

    if (!response.ok) {
      throw new Error('Lỗi khi cập nhật danh mục')
    }

    return await response.json()
  } catch (error) {
    console.error('updateDanhMuc error:', error)
    throw error
  }
}

// ================= DELETE =================
export const deleteDanhMuc = async (id) => {
  try {
    const response = await fetch(`${API}/${id}`, {
      method: 'DELETE',
    })

    if (!response.ok) {
      throw new Error('Lỗi khi xóa danh mục')
    }

    return true
  } catch (error) {
    console.error('deleteDanhMuc error:', error)
    throw error
  }
}
