const API = 'https://provinces.open-api.vn/api'

export const getProvinces = async () => {
  const res = await fetch(`${API}/p`)
  return await res.json()
}

export const getDistricts = async (provinceCode) => {
  const res = await fetch(`${API}/p/${provinceCode}?depth=2`)
  const data = await res.json()
  return data.districts
}

export const getWards = async (districtCode) => {
  const res = await fetch(`${API}/d/${districtCode}?depth=2`)
  const data = await res.json()
  return data.wards
}
