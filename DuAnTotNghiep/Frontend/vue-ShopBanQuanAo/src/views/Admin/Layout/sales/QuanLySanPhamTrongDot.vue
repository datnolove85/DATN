<script setup>
import { ref, watch, computed } from 'vue'
import {
  getSanPhamTrongDot,
  getSanPhamChuaApDung,
  xoaSanPhamGiamGia,
} from '@/service/DotGiamGiaService'
import ThemSanPhamGiamGiaModal from '@/views/Admin/Layout/sales/ThemSanPhamGiamGiaModal.vue'

const props = defineProps({ dot: Object })
const showAddModal = ref(false)
const danhSachSanPham = ref([])
const loading = ref(false)
const search = ref('')
const products = ref([])
const showModal = ref(false)
const selected = ref(null)

const openAdd = async () => {
  try {
    loading.value = true
    danhSachSanPham.value = await getSanPhamChuaApDung(props.dot.id)
    showAddModal.value = true
  } finally {
    loading.value = false
  }
}

const closeAdd = () => {
  showAddModal.value = false
}
const addSuccess = async () => {
  closeAdd()
  await loadData()
}

const loadData = async () => {
  try {
    loading.value = true
    products.value = await getSanPhamTrongDot(props.dot.id)
  } catch (e) {
    console.log(e)
  } finally {
    loading.value = false
  }
}

const money = (value) => Number(value || 0).toLocaleString('vi-VN')

const filteredProducts = computed(() => {
  const keyword = search.value.toLowerCase().trim()
  return products.value.filter(
    (sp) =>
      sp.tenSanPham?.toLowerCase().includes(keyword) ||
      sp.maSanPham?.toLowerCase().includes(keyword) ||
      sp.maSPCT?.toLowerCase().includes(keyword),
  )
})

const tongSanPham = computed(() => filteredProducts.value.length)
const tongTonKho = computed(() =>
  filteredProducts.value.reduce((sum, item) => sum + Number(item.soLuongTon || 0), 0),
)
const tongGiaTri = computed(() =>
  filteredProducts.value.reduce((sum, item) => sum + Number(item.giaSauGiam || 0), 0),
)

const stockClass = (qty) => {
  if (qty == 0) return 'bg-rose-50 text-rose-600 border border-rose-200'
  if (qty <= 10) return 'bg-amber-50 text-amber-700 border border-amber-200'
  return 'bg-emerald-50 text-emerald-700 border border-emerald-200'
}

const openDelete = (item) => {
  selected.value = item
  showModal.value = true
}
const closeDelete = () => {
  selected.value = null
  showModal.value = false
}

const removeProduct = async () => {
  if (!selected.value) return
  try {
    loading.value = true
    await xoaSanPhamGiamGia(props.dot.id, selected.value.idSanPhamChiTiet)
    await loadData()
    closeDelete()
  } catch (e) {
    console.log(e)
  } finally {
    loading.value = false
  }
}

watch(
  () => props.dot,
  async (val) => {
    if (val) await loadData()
  },
  { immediate: true },
)
</script>

<template>
  <div class="bg-slate-100/70 pb-6">
    <!-- HEADER -->
    <div class="bg-white border-b border-slate-200/80">
      <div class="max-w-7xl mx-auto px-6 py-3.5 flex items-center justify-between">
        <div>
          <div class="flex items-center gap-2">
            <span
              class="px-2 py-0.5 bg-indigo-50 text-indigo-700 rounded text-[11px] font-semibold uppercase tracking-wider"
            >
              Đợt #{{ props.dot?.id }}
            </span>
            <h1 class="text-lg font-bold text-slate-900">Quản lý sản phẩm giảm giá</h1>
          </div>
          <p class="text-slate-500 text-xs mt-0.5">
            Danh sách biến thể sản phẩm đang áp dụng ưu đãi
          </p>
        </div>
        <button
          @click="openAdd"
          class="inline-flex items-center gap-1.5 bg-indigo-600 hover:bg-indigo-700 active:scale-95 text-white px-4 py-2 rounded-xl font-semibold text-xs shadow-sm shadow-indigo-200 transition"
        >
          <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path
              stroke-linecap="round"
              stroke-linejoin="round"
              stroke-width="2"
              d="M12 4v16m8-8H4"
            />
          </svg>
          Thêm sản phẩm
        </button>
      </div>
    </div>

    <!-- CONTENT -->
    <div class="max-w-7xl mx-auto px-6 pt-4 space-y-4">
      <!-- CARD THỐNG KÊ -->
      <div class="grid grid-cols-1 md:grid-cols-3 gap-3">
        <div
          class="bg-white rounded-xl shadow-sm border border-slate-200/80 px-5 py-3.5 flex items-center justify-between"
        >
          <div>
            <div class="text-slate-500 text-xs font-medium">Sản phẩm đang giảm</div>
            <div class="text-2xl font-bold text-indigo-600 mt-0.5">{{ tongSanPham }}</div>
          </div>
          <div
            class="w-10 h-10 rounded-xl bg-indigo-50 flex items-center justify-center text-indigo-600 font-bold"
          >
            📦
          </div>
        </div>

        <div
          class="bg-white rounded-xl shadow-sm border border-slate-200/80 px-5 py-3.5 flex items-center justify-between"
        >
          <div>
            <div class="text-slate-500 text-xs font-medium">Tổng tồn kho</div>
            <div class="text-2xl font-bold text-emerald-600 mt-0.5">{{ money(tongTonKho) }}</div>
          </div>
          <div
            class="w-10 h-10 rounded-xl bg-emerald-50 flex items-center justify-center text-emerald-600 font-bold"
          >
            📊
          </div>
        </div>

        <div
          class="bg-white rounded-xl shadow-sm border border-slate-200/80 px-5 py-3.5 flex items-center justify-between"
        >
          <div>
            <div class="text-slate-500 text-xs font-medium">Tổng giá trị sau giảm</div>
            <div class="text-2xl font-bold text-rose-600 mt-0.5">
              {{ money(tongGiaTri) }} <span class="text-sm font-semibold">đ</span>
            </div>
          </div>
          <div
            class="w-10 h-10 rounded-xl bg-rose-50 flex items-center justify-center text-rose-600 font-bold"
          >
            💰
          </div>
        </div>
      </div>

      <!-- SEARCH BAR -->
      <div
        class="bg-white rounded-xl shadow-sm border border-slate-200/80 px-4 py-3 flex items-center justify-between gap-3"
      >
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
            placeholder="Tìm theo tên sản phẩm, mã SP hoặc mã SPCT..."
            class="w-full pl-10 pr-3.5 py-2 bg-slate-50/50 border border-slate-200 rounded-lg outline-none focus:bg-white focus:ring-2 focus:ring-indigo-500/20 focus:border-indigo-500 text-xs transition"
          />
        </div>
        <div
          class="flex items-center gap-1.5 px-3 py-2 bg-slate-100 rounded-lg text-slate-600 font-medium text-xs whitespace-nowrap"
        >
          <span class="w-1.5 h-1.5 rounded-full bg-indigo-600"></span>
          Hiển thị <b class="text-slate-900">{{ filteredProducts.length }}</b> sản phẩm
        </div>
      </div>

      <!-- TABLE & STATES -->
      <div
        v-if="loading"
        class="bg-white rounded-xl shadow-sm border border-slate-200/80 p-12 text-center"
      >
        <div
          class="w-8 h-8 border-3 border-indigo-600 border-t-transparent rounded-full animate-spin mx-auto"
        ></div>
        <div class="mt-3 text-slate-500 text-xs font-medium">Đang tải danh sách sản phẩm...</div>
      </div>

      <div
        v-else-if="filteredProducts.length > 0"
        class="bg-white rounded-xl shadow-sm border border-slate-200/80 overflow-hidden"
      >
        <div class="overflow-x-auto">
          <table class="w-full text-left border-collapse">
            <thead>
              <tr
                class="bg-slate-50/70 border-b border-slate-200 text-[11px] font-semibold text-slate-500 uppercase tracking-wider"
              >
                <th class="px-4 py-2.5 w-16">Ảnh</th>
                <th class="px-4 py-2.5">Sản phẩm</th>
                <th class="px-4 py-2.5 text-center">Phân loại</th>
                <th class="px-4 py-2.5 text-center">Đơn giá</th>
                <th class="px-4 py-2.5 text-center">Mức giảm</th>
                <th class="px-4 py-2.5 text-center">Tồn kho</th>
                <th class="px-4 py-2.5 text-center w-24">Thao tác</th>
              </tr>
            </thead>
            <tbody class="divide-y divide-slate-100 text-xs">
              <tr
                v-for="sp in filteredProducts"
                :key="sp.idSanPhamChiTiet"
                class="hover:bg-slate-50/80 transition-colors"
              >
                <td class="px-4 py-2.5">
                  <img
                    :src="sp.anh ? `http://localhost:8080${sp.anh}` : 'https://placehold.co/60x60'"
                    class="w-11 h-11 rounded-lg border border-slate-200 object-cover bg-slate-50"
                  />
                </td>
                <td class="px-4 py-2.5">
                  <div class="font-bold text-slate-900 line-clamp-1">{{ sp.tenSanPham }}</div>
                  <div class="text-[11px] text-slate-500">
                    Mã: <span class="text-slate-700 font-medium">{{ sp.maSanPham }}</span>
                  </div>
                  <div class="text-[11px] text-indigo-600 font-medium">SPCT: {{ sp.maSPCT }}</div>
                  <span
                    class="inline-block mt-1 px-1.5 py-0.5 rounded bg-slate-100 text-slate-600 text-[10px] font-medium border border-slate-200/60"
                  >
                    {{ sp.thuongHieu }}
                  </span>
                </td>
                <td class="px-4 py-2.5 text-center">
                  <div class="flex flex-wrap justify-center gap-1 max-w-[150px] mx-auto">
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
                <td class="px-4 py-2.5 text-center whitespace-nowrap">
                  <div class="text-[11px] line-through text-slate-400">
                    {{ money(sp.giaGoc) }} đ
                  </div>
                  <div class="text-rose-600 font-bold text-sm mt-0.5">
                    {{ money(sp.giaSauGiam) }} <span class="text-[10px]">đ</span>
                  </div>
                </td>
                <td class="px-4 py-2.5 text-center">
                  <span
                    class="inline-flex px-2 py-0.5 rounded-full bg-rose-50 text-rose-600 font-bold text-[11px] border border-rose-200/60"
                  >
                    -{{ sp.phanTramGiam }}%
                  </span>
                </td>
                <td class="px-4 py-2.5 text-center">
                  <span
                    class="inline-flex px-2 py-0.5 rounded-full text-[11px] font-bold"
                    :class="stockClass(sp.soLuongTon)"
                  >
                    {{ sp.soLuongTon }}
                  </span>
                </td>
                <td class="px-4 py-2.5 text-center">
                  <button
                    @click="openDelete(sp)"
                    class="px-2.5 py-1 rounded-lg bg-rose-50 text-rose-600 hover:bg-rose-100 active:scale-95 font-semibold text-[11px] transition"
                  >
                    Gỡ
                  </button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>

      <div v-else class="bg-white rounded-xl shadow-sm border border-slate-200/80 p-12 text-center">
        <div class="text-4xl">📦</div>
        <h3 class="text-sm font-bold mt-2 text-slate-800">Chưa có sản phẩm nào trong đợt này</h3>
        <p class="text-slate-500 text-xs mt-1">
          Hãy thêm các biến thể sản phẩm để bắt đầu áp dụng giảm giá.
        </p>
        <button
          @click="openAdd"
          class="mt-4 bg-indigo-600 hover:bg-indigo-700 text-white px-4 py-2 rounded-xl font-semibold text-xs transition"
        >
          + Thêm sản phẩm
        </button>
      </div>

      <!-- DELETE MODAL -->
      <Teleport to="body">
        <div
          v-if="showModal"
          class="fixed inset-0 bg-slate-900/40 backdrop-blur-sm z-[9999] flex items-center justify-center p-4"
        >
          <div
            class="bg-white rounded-2xl shadow-xl w-full max-w-sm overflow-hidden border border-slate-100"
          >
            <div class="p-5 text-center">
              <div
                class="w-12 h-12 bg-amber-50 rounded-full flex items-center justify-center mx-auto text-xl mb-3 border border-amber-100"
              >
                ⚠️
              </div>
              <h3 class="text-base font-bold text-slate-900">Xác nhận gỡ sản phẩm?</h3>
              <p class="text-slate-500 text-xs mt-1.5 leading-relaxed">
                Bạn muốn gỡ <b class="text-slate-800">"{{ selected?.tenSanPham }}"</b> khỏi đợt giảm
                giá này?
              </p>
            </div>
            <div
              class="bg-slate-50 border-t border-slate-100 px-5 py-3 flex items-center justify-end gap-2"
            >
              <button
                @click="closeDelete"
                class="px-3.5 py-1.5 rounded-xl border border-slate-200 text-slate-600 hover:bg-slate-100 font-semibold text-xs"
              >
                Hủy
              </button>
              <button
                @click="removeProduct"
                class="px-3.5 py-1.5 rounded-xl bg-rose-600 text-white hover:bg-rose-700 font-semibold text-xs"
              >
                Xác nhận gỡ
              </button>
            </div>
          </div>
        </div>
      </Teleport>
    </div>

    <ThemSanPhamGiamGiaModal
      :show="showAddModal"
      :idDot="props.dot.id"
      :dotInfo="props.dot"
      :danhSachSanPham="danhSachSanPham"
      @close="closeAdd"
      @success="addSuccess"
    />
  </div>
</template>
