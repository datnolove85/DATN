const API_URL = 'http://localhost:8080/hinhanh'

// Upload thêm ảnh cho SPCT
export const uploadImages = async (idSpct, files) => {
  const formData = new FormData()

  files.forEach((file) => {
    formData.append('files', file)
  })

  const response = await fetch(`${API_URL}/upload/${idSpct}`, {
    method: 'POST',
    body: formData,
  })

  if (!response.ok) {
    throw new Error('Thêm ảnh thất bại')
  }

  return await response.text()
}

// Xóa mềm ảnh
export const deleteImage = async (id) => {
  const response = await fetch(`${API_URL}/${id}`, {
    method: 'DELETE',
  })

  if (!response.ok) {
    throw new Error('Xóa ảnh thất bại')
  }

  return await response.text()
}

export const getImagesBySPCT = async (idSpct) => {
  const response = await fetch(`${API_URL}/spct/${idSpct}`)

  if (!response.ok) {
    throw new Error('Lấy danh sách ảnh thất bại')
  }

  return await response.json()
}
