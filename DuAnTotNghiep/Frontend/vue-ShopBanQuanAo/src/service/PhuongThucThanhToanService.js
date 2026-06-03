const API = 'http://localhost:8080/pttt'

// ================= GET ALL =================
export const getAllPTTT = async () => {
  const res = await fetch(API)
  if (!res.ok) throw new Error('Lỗi load phương thức thanh toán')
  return await res.json()
}
