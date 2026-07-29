export const apiFetch = (url, options = {}) => {
  const token = sessionStorage.getItem('token')

  return fetch('http://localhost:8080' + url, {
    ...options,
    headers: {
      'Content-Type': 'application/json',
      Authorization: token ? `Bearer ${token}` : '',
      ...options.headers,
    },
  }).then((r) => r.json())
}
