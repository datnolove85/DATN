const Api = 'http://localhost:8080/voucher'

const parseError = async (res, fallback) => {
  const text = await res.text()
  try {
    const json = JSON.parse(text)
    return json.message || fallback
  } catch {
    return text || fallback
  }
}

export const getAllVoucher = async () => {
  const res = await fetch(Api)
  if (!res.ok) throw new Error(await parseError(res, 'Không tải được danh sách voucher'))
  return await res.json()
}

export const getVoucherPageService = async (pageNo = 0, pageSize = 5) => {
  const res = await fetch(`${Api}/page?pageNo=${pageNo}&pageSize=${pageSize}`)
  if (!res.ok) throw new Error(await parseError(res, 'Không tải được danh sách voucher'))
  return await res.json()
}

export const searchVoucherService = async (keyword = '') => {
  const res = await fetch(`${Api}/search?keyword=${encodeURIComponent(keyword)}`)
  if (!res.ok) throw new Error(await parseError(res, 'Không tìm kiếm được voucher'))
  return await res.json()
}

export const detailVoucherService = async (id) => {
  const res = await fetch(`${Api}/detail/${id}`)
  if (!res.ok) throw new Error(await parseError(res, 'Không lấy được chi tiết voucher'))
  return await res.json()
}

export const addVoucherService = async (data) => {
  const res = await fetch(`${Api}/add`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(data),
  })
  if (!res.ok) throw new Error(await parseError(res, 'Thêm voucher thất bại'))
  return await res.json()
}

export const updateVoucherService = async (id, data) => {
  const res = await fetch(`${Api}/update/${id}`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(data),
  })
  if (!res.ok) throw new Error(await parseError(res, 'Cập nhật voucher thất bại'))
  return await res.json()
}

export const deleteVoucherService = async (id) => {
  const res = await fetch(`${Api}/delete/${id}`, { method: 'DELETE' })
  if (!res.ok) throw new Error(await parseError(res, 'Xóa voucher thất bại'))
}

export const phanTrangVoucherService = getVoucherPageService
