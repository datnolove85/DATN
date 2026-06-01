const API = 'http://localhost:8080/danhmuc'

export const getAllDanhMuc = async () => {
  const res = await fetch(API)
  return res.json()
}
