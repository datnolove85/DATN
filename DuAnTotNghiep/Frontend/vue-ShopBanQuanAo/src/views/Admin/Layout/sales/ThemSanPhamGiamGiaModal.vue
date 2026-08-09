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

const emit = defineEmits(['close', 'success'])

const currentPage = ref(1)
const pageSize = ref(10)
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
    resetFilter()
  },
)

const brands = computed(() => [
  ...new Set(props.danhSachSanPham.map((i) => i.thuongHieu).filter(Boolean)),
])
const productNames = computed(() => [...new Set(props.danhSachSanPham.map((i) => i.tenSanPham))])
const materials = computed(() => [
  ...new Set(props.danhSachSanPham.map((i) => i.chatLieu).filter(Boolean)),
])
const colors = computed(() => [
  ...new Set(props.danhSachSanPham.map((i) => i.mauSac).filter(Boolean)),
])
const sizes = computed(() => [
  ...new Set(props.danhSachSanPham.map((i) => i.kichThuoc).filter(Boolean)),
])

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

  if (filter.value.brand !== 'all') list = list.filter((sp) => sp.thuongHieu === filter.value.brand)
  if (filter.value.material !== 'all')
    list = list.filter((sp) => sp.chatLieu === filter.value.material)
  if (filter.value.color !== 'all') list = list.filter((sp) => sp.mauSac === filter.value.color)
  if (filter.value.size !== 'all') list = list.filter((sp) => sp.kichThuoc === filter.value.size)
  if (filter.value.minPrice !== '')
    list = list.filter((sp) => Number(sp.giaGoc) >= Number(filter.value.minPrice))
  if (filter.value.maxPrice !== '')
    list = list.filter((sp) => Number(sp.giaGoc) <= Number(filter.value.maxPrice))

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
const totalInventory = computed(() =>
  filteredProducts.value.reduce((sum, item) => sum + Number(item.soLuongTon || 0), 0),
)

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
    await themSanPhamGiamGia(props.idDot, {
      idsSanPhamChiTiet: selectedIds.value,
    })
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
  return new Intl.NumberFormat('vi-VN', {
    style: 'currency',
    currency: 'VND',
  }).format(v || 0)
}
</script>

<template>
  <Teleport to="body">
    <div
      v-if="show"
      @click.self="emit('close')"
      class="fixed inset-0 z-[100] flex items-center justify-center bg-slate-900/50 backdrop-blur-sm p-4"
    >
      <div
        class="bg-white w-full max-w-[1350px] h-[88vh] rounded-2xl shadow-2xl flex flex-col overflow-hidden border border-slate-100"
      >
        <!-- MODAL HEADER -->
        <div
          class="px-6 py-3.5 border-b border-slate-200/80 flex justify-between items-center bg-slate-50/70 shrink-0"
        >
          <div>
            <h2 class="text-lg font-bold text-slate-900">Chọn sản phẩm khuyến mãi</h2>
            <p class="text-xs text-slate-500 mt-0.5">
              Lựa chọn các biến thể sản phẩm áp dụng cho đợt giảm giá
            </p>
          </div>
          <button
            @click="emit('close')"
            class="w-8 h-8 rounded-xl bg-slate-200/60 hover:bg-slate-200 text-slate-600 flex items-center justify-center font-bold transition text-sm"
          >
            ✕
          </button>
        </div>

        <!-- MODAL BODY -->
        <div class="flex-1 flex overflow-hidden">
          <!-- LEFT SIDE: FILTERS & TABLE -->
          <div class="flex-[3] flex flex-col overflow-hidden border-r border-slate-200/80 bg-white">
            <!-- SEARCH & ACTIONS BAR -->
            <div class="p-4 border-b border-slate-200/80 space-y-3 shrink-0 bg-white">
              <div class="flex gap-2.5">
                <div class="relative flex-1">
                  <span
                    class="absolute inset-y-0 left-0 flex items-center pl-3.5 pointer-events-none text-slate-400"
                  >
                    <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path
                        stroke-linecap="round"
                        stroke-linejoin="round"
                        stroke-width="2"
                        d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"
                      />
                    </svg>
                  </span>
                  <input
                    v-model="search"
                    placeholder="Tìm tên sản phẩm, mã SP, mã SPCT..."
                    class="w-full pl-10 pr-3.5 py-2 bg-slate-50/50 border border-slate-200 rounded-xl outline-none focus:bg-white focus:ring-2 focus:ring-indigo-500/20 focus:border-indigo-500 text-xs transition"
                  />
                </div>
                <button
                  @click="selectAll"
                  class="px-3.5 py-2 rounded-xl bg-indigo-600 hover:bg-indigo-700 active:scale-95 text-white font-semibold text-xs transition shadow-sm shadow-indigo-200 whitespace-nowrap"
                >
                  Chọn tất cả
                </button>
                <button
                  @click="resetFilter"
                  class="px-3.5 py-2 rounded-xl bg-slate-100 hover:bg-slate-200 active:scale-95 text-slate-700 font-semibold text-xs transition whitespace-nowrap"
                >
                  Reset
                </button>
                <button
                  @click="clearSelected"
                  class="px-3.5 py-2 rounded-xl bg-rose-50 hover:bg-rose-100 active:scale-95 text-rose-600 font-semibold text-xs transition whitespace-nowrap border border-rose-200/60"
                >
                  Bỏ chọn
                </button>
              </div>

              <!-- STATS CARDS -->
              <div class="grid grid-cols-4 gap-2.5">
                <div
                  class="rounded-xl bg-indigo-50/60 border border-indigo-100/60 p-2.5 flex items-center justify-between"
                >
                  <div>
                    <div class="text-[11px] text-slate-500 font-medium">Tổng SP</div>
                    <div class="text-base font-bold text-indigo-600 mt-0.5">
                      {{ totalProducts }}
                    </div>
                  </div>
                  <div class="text-lg">📦</div>
                </div>
                <div
                  class="rounded-xl bg-emerald-50/60 border border-emerald-100/60 p-2.5 flex items-center justify-between"
                >
                  <div>
                    <div class="text-[11px] text-slate-500 font-medium">Đã chọn</div>
                    <div class="text-base font-bold text-emerald-600 mt-0.5">
                      {{ totalSelected }}
                    </div>
                  </div>
                  <div class="text-lg">✅</div>
                </div>
                <div
                  class="rounded-xl bg-amber-50/60 border border-amber-100/60 p-2.5 flex items-center justify-between"
                >
                  <div>
                    <div class="text-[11px] text-slate-500 font-medium">Thương hiệu</div>
                    <div class="text-base font-bold text-amber-600 mt-0.5">{{ totalBrands }}</div>
                  </div>
                  <div class="text-lg">🏷️</div>
                </div>
                <div
                  class="rounded-xl bg-rose-50/60 border border-rose-100/60 p-2.5 flex items-center justify-between"
                >
                  <div>
                    <div class="text-[11px] text-slate-500 font-medium">Tồn kho</div>
                    <div class="text-base font-bold text-rose-600 mt-0.5">{{ totalInventory }}</div>
                  </div>
                  <div class="text-lg">📊</div>
                </div>
              </div>
            </div>

            <!-- FILTERS PANEL -->
            <div class="p-3 border-b border-slate-200/80 bg-slate-50/50 shrink-0 space-y-2">
              <div class="grid grid-cols-5 gap-2">
                <select
                  v-model="filter.product"
                  class="rounded-lg border border-slate-200 px-2.5 py-2 text-xs bg-white outline-none focus:border-indigo-500"
                >
                  <option value="all">Tất cả sản phẩm</option>
                  <option v-for="p in productNames" :key="p" :value="p">{{ p }}</option>
                </select>
                <select
                  v-model="filter.brand"
                  class="rounded-lg border border-slate-200 px-2.5 py-2 text-xs bg-white outline-none focus:border-indigo-500"
                >
                  <option value="all">Thương hiệu</option>
                  <option v-for="b in brands" :key="b" :value="b">{{ b }}</option>
                </select>
                <select
                  v-model="filter.material"
                  class="rounded-lg border border-slate-200 px-2.5 py-2 text-xs bg-white outline-none focus:border-indigo-500"
                >
                  <option value="all">Chất liệu</option>
                  <option v-for="m in materials" :key="m" :value="m">{{ m }}</option>
                </select>
                <select
                  v-model="filter.color"
                  class="rounded-lg border border-slate-200 px-2.5 py-2 text-xs bg-white outline-none focus:border-indigo-500"
                >
                  <option value="all">Màu sắc</option>
                  <option v-for="c in colors" :key="c" :value="c">{{ c }}</option>
                </select>
                <select
                  v-model="filter.size"
                  class="rounded-lg border border-slate-200 px-2.5 py-2 text-xs bg-white outline-none focus:border-indigo-500"
                >
                  <option value="all">Kích thước</option>
                  <option v-for="s in sizes" :key="s" :value="s">{{ s }}</option>
                </select>
              </div>
              <div class="grid grid-cols-3 gap-2">
                <input
                  v-model="filter.minPrice"
                  type="number"
                  placeholder="Giá gốc từ (đ)"
                  class="rounded-lg border border-slate-200 px-2.5 py-2 text-xs bg-white outline-none focus:border-indigo-500"
                />
                <input
                  v-model="filter.maxPrice"
                  type="number"
                  placeholder="Giá gốc đến (đ)"
                  class="rounded-lg border border-slate-200 px-2.5 py-2 text-xs bg-white outline-none focus:border-indigo-500"
                />
                <select
                  v-model="sortBy"
                  class="rounded-lg border border-slate-200 px-2.5 py-2 text-xs bg-white outline-none focus:border-indigo-500"
                >
                  <option value="name">Sắp xếp: Tên A-Z</option>
                  <option value="priceAsc">Giá: Thấp đến cao</option>
                  <option value="priceDesc">Giá: Cao đến thấp</option>
                  <option value="stock">Tồn kho nhiều nhất</option>
                </select>
              </div>
            </div>

            <!-- TABLE CONTENT -->
            <div class="flex-1 overflow-y-auto bg-slate-50/40 p-3">
              <div
                class="bg-white rounded-xl border border-slate-200/80 shadow-2xs overflow-hidden"
              >
                <table class="w-full text-xs">
                  <thead
                    class="sticky top-0 z-10 bg-slate-50 text-slate-600 border-b border-slate-200 font-semibold uppercase tracking-wider text-[11px]"
                  >
                    <tr>
                      <th class="px-3.5 py-2.5 text-center w-12">Chọn</th>
                      <th class="px-3.5 py-2.5 text-left">Sản phẩm</th>
                      <th class="px-3.5 py-2.5 text-center">Thuộc tính</th>
                      <th class="px-3.5 py-2.5 text-center">Tồn kho</th>
                      <th class="px-3.5 py-2.5 text-right">Giá gốc / Sau giảm</th>
                      <th class="px-3.5 py-2.5 text-center">Mức giảm</th>
                    </tr>
                  </thead>
                  <tbody class="divide-y divide-slate-100">
                    <tr
                      v-for="sp in paginatedProducts"
                      :key="sp.idSanPhamChiTiet"
                      @click="toggle(sp.idSanPhamChiTiet)"
                      :class="[
                        'cursor-pointer transition hover:bg-indigo-50/40',
                        selectedIds.includes(sp.idSanPhamChiTiet) ? 'bg-indigo-50/70' : '',
                      ]"
                    >
                      <td class="text-center px-3.5 py-2.5">
                        <input
                          type="checkbox"
                          :checked="selectedIds.includes(sp.idSanPhamChiTiet)"
                          @click.stop="toggle(sp.idSanPhamChiTiet)"
                          class="w-4 h-4 cursor-pointer accent-indigo-600 rounded"
                        />
                      </td>
                      <td class="px-3.5 py-2.5">
                        <div class="flex items-center gap-2.5">
                          <img
                            :src="
                              sp.anh
                                ? `http://localhost:8080${sp.anh}`
                                : 'https://placehold.co/60x60'
                            "
                            class="w-10 h-10 rounded-lg object-cover border border-slate-200 bg-slate-50 shrink-0"
                          />
                          <div class="min-w-0">
                            <div class="font-bold text-slate-900 truncate">{{ sp.tenSanPham }}</div>
                            <div class="text-[11px] text-indigo-600 font-medium mt-0.5">
                              SPCT: {{ sp.maSPCT }}
                            </div>
                          </div>
                        </div>
                      </td>
                      <td class="px-3.5 py-2.5 text-center">
                        <div class="flex flex-wrap justify-center gap-1 max-w-[160px] mx-auto">
                          <span
                            v-if="sp.thuongHieu"
                            class="px-1.5 py-0.5 rounded bg-slate-100 text-slate-700 text-[10px] font-medium border border-slate-200/60"
                            >{{ sp.thuongHieu }}</span
                          >
                          <span
                            v-if="sp.mauSac"
                            class="px-1.5 py-0.5 rounded bg-amber-50 text-amber-700 text-[10px] font-medium border border-amber-200/60"
                            >{{ sp.mauSac }}</span
                          >
                          <span
                            v-if="sp.kichThuoc"
                            class="px-1.5 py-0.5 rounded bg-purple-50 text-purple-700 text-[10px] font-medium border border-purple-200/60"
                            >{{ sp.kichThuoc }}</span
                          >
                          <span
                            v-if="sp.chatLieu"
                            class="px-1.5 py-0.5 rounded bg-emerald-50 text-emerald-700 text-[10px] font-medium border border-emerald-200/60"
                            >{{ sp.chatLieu }}</span
                          >
                        </div>
                      </td>
                      <td class="px-3.5 py-2.5 text-center">
                        <span
                          class="inline-flex px-2 py-0.5 rounded-full text-[11px] font-bold"
                          :class="
                            sp.soLuongTon === 0
                              ? 'bg-rose-50 text-rose-600 border border-rose-200'
                              : 'bg-emerald-50 text-emerald-700 border border-emerald-200'
                          "
                        >
                          {{ sp.soLuongTon }}
                        </span>
                      </td>
                      <td class="px-3.5 py-2.5 text-right whitespace-nowrap">
                        <div class="text-[11px] line-through text-slate-400">
                          {{ money(sp.giaGoc) }}
                        </div>
                        <div class="font-bold text-rose-600 text-xs mt-0.5">
                          {{ money(getDiscountedPrice(sp.giaGoc)) }}
                        </div>
                      </td>
                      <td class="px-3.5 py-2.5 text-center">
                        <span
                          class="inline-flex px-2 py-0.5 rounded-full bg-rose-50 text-rose-600 font-bold text-[11px] border border-rose-200/60"
                        >
                          -{{ props.dotInfo.giaTriGiam
                          }}{{ props.dotInfo.loaiGiamGia === 'phan_tram' ? '%' : 'đ' }}
                        </span>
                      </td>
                    </tr>
                  </tbody>
                </table>
              </div>
            </div>

            <!-- PAGINATION FOOTER -->
            <div
              class="px-4 py-3 border-t border-slate-200/80 bg-white flex justify-between items-center shrink-0"
            >
              <div class="text-xs text-slate-500 font-medium">
                Hiển thị
                <span class="text-slate-800 font-bold">{{ (currentPage - 1) * pageSize + 1 }}</span>
                -
                <span class="text-slate-800 font-bold">{{
                  Math.min(currentPage * pageSize, filteredProducts.length)
                }}</span>
                / <span class="text-slate-800 font-bold">{{ filteredProducts.length }}</span> sản
                phẩm
              </div>
              <div class="flex gap-1.5">
                <button
                  @click="currentPage--"
                  :disabled="currentPage === 1"
                  class="w-7 h-7 flex items-center justify-center border border-slate-200 rounded-lg hover:bg-slate-100 disabled:opacity-40 text-xs font-semibold"
                >
                  ◀
                </button>
                <button
                  v-for="page in totalPages"
                  :key="page"
                  @click="currentPage = page"
                  :class="[
                    'w-7 h-7 flex items-center justify-center rounded-lg text-xs font-semibold transition',
                    page === currentPage
                      ? 'bg-indigo-600 text-white shadow-2xs'
                      : 'border border-slate-200 hover:bg-slate-100 text-slate-700',
                  ]"
                >
                  {{ page }}
                </button>
                <button
                  @click="currentPage++"
                  :disabled="currentPage === totalPages"
                  class="w-7 h-7 flex items-center justify-center border border-slate-200 rounded-lg hover:bg-slate-100 disabled:opacity-40 text-xs font-semibold"
                >
                  ▶
                </button>
              </div>
            </div>
          </div>

          <!-- RIGHT SIDE: SELECTED PRODUCTS SIDEBAR -->
          <div class="w-[340px] bg-slate-50/60 flex flex-col border-l border-slate-200/80">
            <div
              class="px-4 py-3.5 bg-gradient-to-r from-indigo-600 to-indigo-500 text-white flex justify-between items-center shrink-0 shadow-2xs"
            >
              <div>
                <h3 class="text-sm font-bold">Danh sách đã chọn</h3>
                <p class="text-indigo-100 text-[11px] mt-0.5">
                  {{ totalSelected }} sản phẩm sẵn sàng áp dụng
                </p>
              </div>
              <button
                @click="save"
                :disabled="loading || totalSelected == 0"
                class="px-3.5 py-1.5 rounded-xl bg-white text-indigo-600 font-bold text-xs hover:bg-indigo-50 active:scale-95 transition disabled:opacity-50 shadow-2xs"
              >
                {{ loading ? 'Đang lưu...' : 'Áp dụng' }}
              </button>
            </div>

            <div class="flex-1 overflow-y-auto p-3 space-y-2.5">
              <div
                v-if="selectedProducts.length === 0"
                class="h-full flex flex-col items-center justify-center text-center p-6 text-slate-400"
              >
                <div class="text-3xl mb-2">🛒</div>
                <p class="text-xs font-medium">Chưa có sản phẩm nào được chọn</p>
                <p class="text-[11px] text-slate-400 mt-1">
                  Hãy tích chọn các sản phẩm ở danh sách bên trái để thêm vào đợt giảm giá.
                </p>
              </div>

              <div
                v-for="sp in selectedProducts"
                :key="sp.idSanPhamChiTiet"
                class="bg-white rounded-xl border border-slate-200/80 p-2.5 flex gap-2.5 items-center shadow-2xs hover:border-indigo-200 transition"
              >
                <img
                  :src="sp.anh ? `http://localhost:8080${sp.anh}` : 'https://placehold.co/60x60'"
                  class="w-12 h-12 rounded-lg object-cover border border-slate-200 bg-slate-50 shrink-0"
                />
                <div class="flex-1 min-w-0">
                  <div class="font-bold text-slate-900 text-xs truncate">{{ sp.tenSanPham }}</div>
                  <div class="text-indigo-600 text-[11px] font-medium mt-0.5">{{ sp.maSPCT }}</div>
                  <div class="font-bold text-rose-600 text-xs mt-1">
                    {{ money(getDiscountedPrice(sp.giaGoc)) }}
                  </div>
                </div>
                <button
                  @click.stop="toggle(sp.idSanPhamChiTiet)"
                  class="w-6 h-6 rounded-lg bg-rose-50 text-rose-600 hover:bg-rose-100 flex items-center justify-center text-xs font-bold transition shrink-0"
                >
                  ✕
                </button>
              </div>
            </div>

            <div class="p-3 bg-white border-t border-slate-200/80 shrink-0">
              <button
                @click="emit('close')"
                class="w-full py-2.5 rounded-xl border border-slate-200 font-semibold text-xs text-slate-600 hover:bg-slate-100 transition"
              >
                Đóng / Hủy bỏ
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </Teleport>
</template>
