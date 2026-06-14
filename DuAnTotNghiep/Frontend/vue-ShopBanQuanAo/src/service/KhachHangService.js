const Api = 'http://localhost:8080/khachhang'

export const getALLKhachHang = async () => {
  const res = await fetch(Api)
  if (!res.ok) {
    throw new Error('Lỗi khi lấy danh sách khách hàng')
  }
  return await res.json()
}

export const phanTrangKhachHangService = async (pageNo, pageSize) => {
  const res = await fetch(`${Api}/page?pageNo=${pageNo}&end=${pageSize}`)
  if (!res.ok) {
    throw new Error('Lỗi phân trang')
  }
  return await res.json()
}

export const deleteKhachHangService = async (id) => {
  const res = await fetch(`${Api}/delete/${id}`, {
    method: 'DELETE',
  })
  if (!res.ok) {
    throw new Error('Lỗi xoá')
  }
}

export const searchKhachHangService = async (keyword) => {
  const res = await fetch(`${Api}/search?keyword=${encodeURIComponent(keyword)}`)
  if (!res.ok) {
    throw new Error('lỗi search')
  }
  return await res.json()
}

const buildFormData = (data, file) => {
  const formData = new FormData()
  formData.append('data', new Blob([JSON.stringify(data)], { type: 'application/json' }))
  if (file) {
    formData.append('file', file)
  }
  return formData
}

export const addKhachHangService = async (data, file) => {
  const res = await fetch(`${Api}/add`, {
    method: 'POST',
    body: buildFormData(data, file),
  })
  if (!res.ok) {
    throw new Error('lỗi khi thêm khách hàng')
  }
}
export const addKhachHangQuickService = async (data) => {
  const res = await fetch(`${Api}/add-quick`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(data),
  })

  if (!res.ok) {
    throw new Error('Lỗi khi thêm khách hàng')
  }

  return await res.json()
}
export const updateKhachHangService = async (id, data, file) => {
  const res = await fetch(`${Api}/update/${id}`, {
    method: 'PUT',
    body: buildFormData(data, file),
  })
  if (!res.ok) {
    throw new Error('lỗi khi cập nhập khách hàng')
  }
}

export const detailKhachHangService = async (id) => {
  const res = await fetch(`${Api}/detail/${id}`)
  if (!res.ok) {
    throw new Error('lỗi khi lấy dữ liệu')
  }
  return await res.json()
}

export const exportKhachHangExcelService = async (keyword, gender, status) => {
  const params = new URLSearchParams()
  if (keyword) params.append('keyword', keyword)
  if (gender !== undefined && gender !== null) params.append('gender', gender)
  if (status !== undefined && status !== null) params.append('status', status)
  const res = await fetch(`${Api}/export-excel?${params.toString()}`, {
    method: 'GET',
  })
  if (!res.ok) {
    throw new Error('Lỗi khi xuất file Excel')
  }
  const blob = await res.blob()
  const url = window.URL.createObjectURL(blob)
  const a = document.createElement('a')
  a.href = url
  a.download = `DanhSachKhachHang_${new Date().getTime()}.xlsx` // Đặt tên file
  document.body.appendChild(a)
  a.click()
  a.remove()
  window.URL.revokeObjectURL(url)
}
