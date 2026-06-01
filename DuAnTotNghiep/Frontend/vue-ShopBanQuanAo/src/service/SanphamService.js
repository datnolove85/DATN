const API = 'http://localhost:8080/sanpham'

// ================= GET ALL =================
export const getAllSanpham = async () => {
  try {
    const response = await fetch(API)

    if (!response.ok) {
      throw new Error('Lỗi khi lấy danh sách sản phẩm')
    }

    return await response.json()
  } catch (error) {
    console.error('getAllSanpham error:', error)
    throw error
  }
}

// ================= GET BY ID =================
export const getSanphamById = async (id) => {
  try {
    const response = await fetch(`${API}/${id}`)

    if (!response.ok) {
      throw new Error('Không tìm thấy sản phẩm')
    }

    return await response.json()
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
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(data),
    })

    if (!response.ok) {
      throw new Error('Tạo sản phẩm thất bại')
    }

    return await response.json()
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
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(data),
    })

    if (!response.ok) {
      throw new Error('Cập nhật sản phẩm thất bại')
    }

    return await response.json()
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

    if (!response.ok) {
      throw new Error('Xóa sản phẩm thất bại')
    }

    return await response.text() // vì backend trả String
  } catch (error) {
    console.error('deleteSanpham error:', error)
    throw error
  }
}
