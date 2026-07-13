const API = 'http://localhost:8080/api/ghn'

export async function getProvinces() {
  const res = await fetch(`${API}/provinces`)

  return await res.json()
}

export async function getDistricts(provinceId) {
  const res = await fetch(`${API}/districts/${provinceId}`)

  return await res.json()
}

export async function getWards(districtId) {
  const res = await fetch(`${API}/wards/${districtId}`)

  return await res.json()
}

export async function getShippingFee(body) {
  const res = await fetch(`${API}/fee`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(body),
  })

  if (!res.ok) {
    throw new Error('Không tính được phí vận chuyển')
  }

  return await res.json()
}
