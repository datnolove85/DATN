<script setup>
import { ref, computed, watch } from 'vue'
import { themSanPhamGiamGia } from '@/service/DotGiamGiaService'

const props = defineProps({
  show: Boolean,
  idDot: [Number, String],
  danhSachSanPham: {
    type: Array,
    default: () => [],
  },
  dotInfo: {
    type: Object,
    default: () => ({
      loaiGiamGia: 'phan_tram',
      giaTriGiam: 0,
    }),
  },
})
const currentPage = ref(1)
const pageSize = ref(10)

const totalPages = computed(() => Math.ceil(filteredProducts.value.length / pageSize.value))

const paginatedProducts = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value
  return filteredProducts.value.slice(start, start + pageSize.value)
})

const resetFilter = () => {
  search.value = ''

  filter.value = {
    product: 'all',
    brand: 'all',
    material: 'all',
    color: 'all',
    size: 'all',
    minPrice: '',
    maxPrice: '',
  }

  sortBy.value = 'name'
}
const emit = defineEmits(['close', 'success'])

const loading = ref(false)

const search = ref('')

const selectedIds = ref([])

const sortBy = ref('name')

const filter = ref({
  product: 'all',
  brand: 'all',
  material: 'all',
  color: 'all',
  size: 'all',
  minPrice: '',
  maxPrice: '',
})
watch(
  [search, filter, sortBy],
  () => {
    currentPage.value = 1
  },
  { deep: true },
)

watch(
  () => props.show,
  (show) => {
    if (!show) return

    selectedIds.value = []
    search.value = ''

    filter.value = {
      product: 'all',
      brand: 'all',
      material: 'all',
      color: 'all',
      size: 'all',
      minPrice: '',
      maxPrice: '',
    }

    sortBy.value = 'name'
  },
)

const brands = computed(() => {
  return [...new Set(props.danhSachSanPham.map((i) => i.thuongHieu).filter(Boolean))]
})
const productNames = computed(() => {
  return [...new Set(props.danhSachSanPham.map((i) => i.tenSanPham))]
})
const materials = computed(() => {
  return [...new Set(props.danhSachSanPham.map((i) => i.chatLieu).filter(Boolean))]
})

const colors = computed(() => {
  return [...new Set(props.danhSachSanPham.map((i) => i.mauSac).filter(Boolean))]
})

const sizes = computed(() => {
  return [...new Set(props.danhSachSanPham.map((i) => i.kichThuoc).filter(Boolean))]
})

const getDiscountedPrice = (price) => {
  const value = Number(price || 0)

  if (props.dotInfo.loaiGiamGia === 'phan_tram') {
    return Math.max(0, value * (1 - props.dotInfo.giaTriGiam / 100))
  }

  return Math.max(0, value - props.dotInfo.giaTriGiam)
}

const filteredProducts = computed(() => {
  let list = [...props.danhSachSanPham]

  const keyword = search.value.trim().toLowerCase()

  if (filter.value.product !== 'all') {
    list = list.filter((sp) => sp.tenSanPham === filter.value.product)
  }
  if (keyword) {
    list = list.filter(
      (sp) =>
        sp.tenSanPham?.toLowerCase().includes(keyword) ||
        sp.maSanPham?.toLowerCase().includes(keyword) ||
        sp.maSPCT?.toLowerCase().includes(keyword) ||
        sp.thuongHieu?.toLowerCase().includes(keyword) ||
        sp.chatLieu?.toLowerCase().includes(keyword) ||
        sp.mauSac?.toLowerCase().includes(keyword) ||
        sp.kichThuoc?.toLowerCase().includes(keyword),
    )
  }

  if (filter.value.brand !== 'all') {
    list = list.filter((sp) => sp.thuongHieu === filter.value.brand)
  }

  if (filter.value.material !== 'all') {
    list = list.filter((sp) => sp.chatLieu === filter.value.material)
  }

  if (filter.value.color !== 'all') {
    list = list.filter((sp) => sp.mauSac === filter.value.color)
  }

  if (filter.value.size !== 'all') {
    list = list.filter((sp) => sp.kichThuoc === filter.value.size)
  }

  if (filter.value.minPrice !== '') {
    list = list.filter((sp) => Number(sp.giaGoc) >= Number(filter.value.minPrice))
  }

  if (filter.value.maxPrice !== '') {
    list = list.filter((sp) => Number(sp.giaGoc) <= Number(filter.value.maxPrice))
  }

  switch (sortBy.value) {
    case 'priceAsc':
      list.sort((a, b) => a.giaGoc - b.giaGoc)

      break

    case 'priceDesc':
      list.sort((a, b) => b.giaGoc - a.giaGoc)

      break

    case 'stock':
      list.sort((a, b) => b.soLuongTon - a.soLuongTon)

      break

    default:
      list.sort((a, b) => a.tenSanPham.localeCompare(b.tenSanPham))
  }

  return list
})

const totalProducts = computed(() => props.danhSachSanPham.length)

const totalSelected = computed(() => selectedProducts.value.length)
const totalBrands = computed(() => brands.value.length)

const totalInventory = computed(() => {
  return filteredProducts.value.reduce(
    (sum, item) => sum + Number(item.soLuongTon || 0),

    0,
  )
})

const selectedProducts = computed(() => {
  return filteredProducts.value.filter((item) => selectedIds.value.includes(item.idSanPhamChiTiet))
})
const toggle = (id) => {
  const index = selectedIds.value.indexOf(id)

  if (index > -1) {
    selectedIds.value.splice(index, 1)
  } else {
    selectedIds.value.push(id)
  }
}

const selectAll = () => {
  selectedIds.value = filteredProducts.value.map((item) => item.idSanPhamChiTiet)
}

const clearSelected = () => {
  selectedIds.value = []
}

const save = async () => {
  if (selectedIds.value.length === 0) return

  loading.value = true

  try {
    await themSanPhamGiamGia(
      props.idDot,

      {
        idsSanPhamChiTiet: selectedIds.value,
      },
    )

    emit('success')

    emit('close')
  } catch (err) {
    console.log(err)

    alert(err.message || 'Có lỗi xảy ra')
  } finally {
    loading.value = false
  }
}

const money = (v) => {
  return new Intl.NumberFormat(
    'vi-VN',

    {
      style: 'currency',

      currency: 'VND',
    },
  ).format(v || 0)
}
</script>
<template>
  <Teleport to="body">
    <div
      v-if="show"
      @click.self="emit('close')"
      class="fixed inset-0 z-[100] flex items-center justify-center bg-black/60 backdrop-blur-sm p-4"
    >
      <div
        class="bg-white w-full max-w-[1400px] h-[90vh] rounded-2xl shadow-2xl flex flex-col overflow-hidden"
      >
        <div class="px-6 py-4 border-b flex justify-between items-center bg-slate-50 shrink-0">
          <div>
            <h2 class="text-xl font-bold text-slate-800">Chọn sản phẩm khuyến mãi</h2>
            <p class="text-sm text-slate-500 mt-1">Lựa chọn sản phẩm áp dụng cho đợt giảm giá</p>
          </div>
          <button
            @click="emit('close')"
            class="w-10 h-10 rounded-full hover:bg-slate-200 transition text-xl"
          >
            ✕
          </button>
        </div>

        <div class="flex-1 flex overflow-hidden">
          <div class="flex-[3] flex flex-col overflow-hidden border-r bg-white">
            <div class="p-4 border-b space-y-4 shrink-0">
              <div class="flex gap-3">
                <input
                  v-model="search"
                  placeholder="🔍 Tìm tên sản phẩm, mã SP, mã SPCT..."
                  class="flex-1 rounded-xl border border-slate-200 px-4 py-3 outline-none focus:ring-2 focus:ring-indigo-500"
                />
                <button
                  @click="selectAll"
                  class="px-5 rounded-xl bg-indigo-600 text-white font-semibold hover:bg-indigo-700 transition"
                >
                  Chọn tất cả
                </button>
                <button
                  @click="resetFilter"
                  class="px-5 rounded-xl bg-slate-100 text-slate-700 font-semibold hover:bg-slate-200 transition"
                >
                  Reset
                </button>
                <button
                  @click="clearSelected"
                  class="px-5 rounded-xl bg-red-500 text-white font-semibold hover:bg-red-600 transition"
                >
                  Bỏ chọn
                </button>
              </div>

              <div class="grid grid-cols-4 gap-3">
                <div class="rounded-xl bg-indigo-50 p-3">
                  <div class="text-xs text-slate-500">Tổng SP</div>
                  <div class="text-xl font-bold text-indigo-600">{{ totalProducts }}</div>
                </div>
                <div class="rounded-xl bg-green-50 p-3">
                  <div class="text-xs text-slate-500">Đã chọn</div>
                  <div class="text-xl font-bold text-green-600">{{ totalSelected }}</div>
                </div>
                <div class="rounded-xl bg-orange-50 p-3">
                  <div class="text-xs text-slate-500">Thương hiệu</div>
                  <div class="text-xl font-bold text-orange-600">{{ totalBrands }}</div>
                </div>
                <div class="rounded-xl bg-pink-50 p-3">
                  <div class="text-xs text-slate-500">Tồn kho</div>
                  <div class="text-xl font-bold text-pink-600">{{ totalInventory }}</div>
                </div>
              </div>
            </div>

            <div class="p-4 border-b bg-slate-50 shrink-0">
              <div class="bg-white rounded-2xl border shadow-sm p-4">
                <div class="grid grid-cols-5 gap-3">
                  <select v-model="filter.product" class="rounded-xl border px-3 py-2.5 text-sm">
                    <option value="all">Sản phẩm</option>
                    <option v-for="p in productNames" :key="p" :value="p">{{ p }}</option>
                  </select>
                  <select v-model="filter.brand" class="rounded-xl border px-3 py-2.5 text-sm">
                    <option value="all">Thương hiệu</option>
                    <option v-for="b in brands" :key="b" :value="b">{{ b }}</option>
                  </select>
                  <select v-model="filter.material" class="rounded-xl border px-3 py-2.5 text-sm">
                    <option value="all">Chất liệu</option>
                    <option v-for="m in materials" :key="m" :value="m">{{ m }}</option>
                  </select>
                  <select v-model="filter.color" class="rounded-xl border px-3 py-2.5 text-sm">
                    <option value="all">Màu sắc</option>
                    <option v-for="c in colors" :key="c" :value="c">{{ c }}</option>
                  </select>
                  <select v-model="filter.size" class="rounded-xl border px-3 py-2.5 text-sm">
                    <option value="all">Kích thước</option>
                    <option v-for="s in sizes" :key="s" :value="s">{{ s }}</option>
                  </select>
                </div>
                <div class="grid grid-cols-3 gap-3 mt-3">
                  <input
                    v-model="filter.minPrice"
                    type="number"
                    placeholder="Giá từ"
                    class="rounded-xl border px-3 py-2.5"
                  />
                  <input
                    v-model="filter.maxPrice"
                    type="number"
                    placeholder="Giá đến"
                    class="rounded-xl border px-3 py-2.5"
                  />
                  <select v-model="sortBy" class="rounded-xl border px-3 py-2.5">
                    <option value="name">Tên A-Z</option>
                    <option value="priceAsc">Giá tăng</option>
                    <option value="priceDesc">Giá giảm</option>
                    <option value="stock">Tồn kho</option>
                  </select>
                </div>
              </div>
            </div>

            <div class="flex-1 overflow-y-auto bg-slate-50 p-4">
              <div class="bg-white rounded-2xl border shadow-sm overflow-hidden">
                <table class="w-full text-sm">
                  <thead class="sticky top-0 z-10 bg-slate-100 text-slate-600">
                    <tr>
                      <th class="px-4 py-3 text-center w-16">Chọn</th>
                      <th class="px-4 py-3 text-left">Sản phẩm</th>
                      <th class="px-4 py-3 text-center">Thuộc tính</th>
                      <th class="px-4 py-3 text-center">Tồn kho</th>
                      <th class="px-4 py-3 text-right">Giá gốc</th>
                      <th class="px-4 py-3 text-center">Giảm giá</th>
                    </tr>
                  </thead>
                  <tbody class="divide-y">
                    <tr
                      v-for="sp in paginatedProducts"
                      :key="sp.idSanPhamChiTiet"
                      @click="toggle(sp.idSanPhamChiTiet)"
                      :class="[
                        'cursor-pointer transition hover:bg-indigo-50',
                        selectedIds.includes(sp.idSanPhamChiTiet) ? 'bg-indigo-50' : '',
                      ]"
                    >
                      <td class="text-center px-4 py-3">
                        <input
                          type="checkbox"
                          :checked="selectedIds.includes(sp.idSanPhamChiTiet)"
                          @click.stop="toggle(sp.idSanPhamChiTiet)"
                          class="w-5 h-5 cursor-pointer accent-indigo-600"
                        />
                      </td>
                      <td class="px-4 py-3">
                        <div class="flex items-center gap-3">
                          <img
                            :src="
                              sp.anh
                                ? `http://localhost:8080${sp.anh}`
                                : 'https://placehold.co/60x60'
                            "
                            class="w-12 h-12 rounded-xl object-cover border"
                          />
                          <div>
                            <div class="font-bold text-slate-800">{{ sp.tenSanPham }}</div>
                            <div class="text-xs text-indigo-600 mt-1">{{ sp.maSPCT }}</div>
                          </div>
                        </div>
                      </td>
                      <td class="px-4 py-3 text-center">
                        <div class="flex flex-wrap justify-center gap-1">
                          <span
                            class="px-2 py-1 rounded-full bg-indigo-100 text-indigo-700 text-[11px]"
                            >{{ sp.thuongHieu }}</span
                          >
                          <span
                            class="px-2 py-1 rounded-full bg-green-100 text-green-700 text-[11px]"
                            >{{ sp.chatLieu }}</span
                          >
                          <span
                            class="px-2 py-1 rounded-full bg-yellow-100 text-yellow-700 text-[11px]"
                            >{{ sp.mauSac }}</span
                          >
                          <span
                            class="px-2 py-1 rounded-full bg-purple-100 text-purple-700 text-[11px]"
                            >{{ sp.kichThuoc }}</span
                          >
                        </div>
                      </td>
                      <td class="px-4 py-3 text-center">
                        <span
                          :class="[
                            'px-3 py-1 rounded-full text-xs font-semibold',
                            sp.soLuongTon === 0
                              ? 'bg-red-100 text-red-600'
                              : 'bg-green-100 text-green-700',
                          ]"
                          >{{ sp.soLuongTon }}</span
                        >
                      </td>
                      <td class="px-4 py-3 text-right">
                        <div class="text-slate-400 line-through text-xs">
                          {{ money(sp.giaGoc) }}
                        </div>
                        <div class="font-bold text-red-600">
                          {{ money(getDiscountedPrice(sp.giaGoc)) }}
                        </div>
                      </td>
                      <td class="px-4 py-3 text-center">
                        <span
                          class="px-3 py-1 rounded-full bg-red-100 text-red-600 font-semibold text-xs"
                          >- {{ props.dotInfo.giaTriGiam
                          }}{{ props.dotInfo.loaiGiamGia === 'phan_tram' ? '%' : 'đ' }}</span
                        >
                      </td>
                    </tr>
                  </tbody>
                </table>
              </div>
            </div>

            <div class="px-5 py-4 border-t bg-white flex justify-between items-center shrink-0">
              <div class="text-sm text-slate-500">
                Hiển thị {{ (currentPage - 1) * pageSize + 1 }} -
                {{ Math.min(currentPage * pageSize, filteredProducts.length) }} /
                {{ filteredProducts.length }}
              </div>
              <div class="flex gap-2">
                <button
                  @click="currentPage--"
                  :disabled="currentPage === 1"
                  class="px-3 py-2 border rounded-lg hover:bg-slate-100 disabled:opacity-40"
                >
                  ◀
                </button>
                <button
                  v-for="page in totalPages"
                  :key="page"
                  @click="currentPage = page"
                  :class="[
                    'px-3 py-2 rounded-lg',
                    page === currentPage ? 'bg-indigo-600 text-white' : 'border hover:bg-slate-100',
                  ]"
                >
                  {{ page }}
                </button>
                <button
                  @click="currentPage++"
                  :disabled="currentPage === totalPages"
                  class="px-3 py-2 border rounded-lg hover:bg-slate-100 disabled:opacity-40"
                >
                  ▶
                </button>
              </div>
            </div>
          </div>

          <div class="w-[380px] bg-slate-50 flex flex-col border-l">
            <div
              class="px-5 py-4 bg-gradient-to-r from-indigo-600 to-indigo-500 text-white flex justify-between items-center shrink-0"
            >
              <div>
                <h3 class="text-lg font-bold">Đã chọn</h3>
                <p class="text-indigo-100 text-sm">{{ totalSelected }} sản phẩm</p>
              </div>
              <button
                @click="save"
                :disabled="loading || totalSelected == 0"
                class="px-4 py-2 rounded-xl bg-white text-indigo-600 font-bold hover:bg-slate-100 transition"
              >
                {{ loading ? 'Lưu...' : 'Áp dụng' }}
              </button>
            </div>

            <div class="flex-1 overflow-y-auto p-4 space-y-3">
              <div
                v-for="sp in selectedProducts"
                :key="sp.idSanPhamChiTiet"
                class="bg-white rounded-2xl border p-3 flex gap-3"
              >
                <img
                  :src="sp.anh ? `http://localhost:8080${sp.anh}` : 'https://placehold.co/80x80'"
                  class="w-16 h-16 rounded-xl object-cover border"
                />
                <div class="flex-1 min-w-0">
                  <div class="font-bold text-sm truncate">{{ sp.tenSanPham }}</div>
                  <div class="text-indigo-600 text-xs">{{ sp.maSPCT }}</div>
                  <div class="flex justify-between items-end mt-2">
                    <div class="font-bold text-red-600">
                      {{ money(getDiscountedPrice(sp.giaGoc)) }}
                    </div>
                    <button
                      @click.stop="toggle(sp.idSanPhamChiTiet)"
                      class="w-7 h-7 rounded-full bg-red-100 text-red-600 hover:bg-red-200 text-xs"
                    >
                      ✕
                    </button>
                  </div>
                </div>
              </div>
            </div>

            <div class="p-4 bg-white border-t shrink-0">
              <button
                @click="emit('close')"
                class="w-full py-3 rounded-xl border font-semibold hover:bg-slate-100 transition"
              >
                Hủy
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </Teleport>
</template>
