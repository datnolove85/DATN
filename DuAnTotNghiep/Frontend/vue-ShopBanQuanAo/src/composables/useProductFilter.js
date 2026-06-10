import { computed } from 'vue'

export function useProductFilter(products, filters) {
  return computed(() => {
    return products.value.filter((sp) => {
      const search = filters.search.value.toLowerCase()

      const matchSearch =
        (sp.tenSanPhamChiTiet || sp.tenSanPham || '').toLowerCase().includes(search) ||
        (sp.maSanPhamChiTiet || '').toLowerCase().includes(search)

      const matchCategory =
        !filters.category.value || sp.tenDanhMuc === filters.selectedDM?.value?.tenDanhMuc

      const matchBrand =
        !filters.brand.value || sp.tenThuongHieu === filters.selectedTH?.value?.tenThuongHieu

      const matchColor = !filters.color.value || Number(sp.idMauSac) === Number(filters.color.value)

      const matchSize = !filters.size.value || Number(sp.idKichThuoc) === Number(filters.size.value)

      return matchSearch && matchCategory && matchBrand && matchColor && matchSize
    })
  })
}
