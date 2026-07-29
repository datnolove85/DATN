<script setup>
import { ref, watch, computed, onMounted } from 'vue'

import {
  getSanPhamTrongDot,
  getSanPhamChuaApDung,
  xoaSanPhamGiamGia,
} from '@/service/DotGiamGiaService'
import ThemSanPhamGiamGiaModal from '@/views/Admin/Layout/sales/ThemSanPhamGiamGiaModal.vue'
const showAddModal = ref(false)

const openAdd = async () => {
  try {
    loading.value = true

    const data = await getSanPhamChuaApDung(props.dot.id)

    console.log(data)

    danhSachSanPham.value = data
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
const props = defineProps({
  dot: Object,
})
const danhSachSanPham = ref([])

props.dot.id

const loading = ref(false)

const search = ref('')

const products = ref([])

const showModal = ref(false)

const selected = ref(null)

//==============================
// LOAD DATA
//==============================

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

//==============================
// FORMAT MONEY
//==============================

const money = (value) => {
  return Number(value || 0).toLocaleString('vi-VN')
}

//==============================
// SEARCH
//==============================

const filteredProducts = computed(() => {
  const keyword = search.value.toLowerCase()

  return products.value.filter((sp) => {
    return (
      sp.tenSanPham?.toLowerCase().includes(keyword) ||
      sp.maSanPham?.toLowerCase().includes(keyword)
    )
  })
})

//==============================
// THỐNG KÊ
//==============================

const tongSanPham = computed(() => filteredProducts.value.length)

const tongTonKho = computed(() => {
  return filteredProducts.value.reduce((sum, item) => {
    return sum + Number(item.soLuongTon || 0)
  }, 0)
})

const tongGiaTri = computed(() => {
  return filteredProducts.value.reduce((sum, item) => {
    return sum + Number(item.giaSauGiam || 0)
  }, 0)
})

//==============================
// BADGE TỒN KHO
//==============================

const stockClass = (qty) => {
  if (qty == 0) return 'bg-red-100 text-red-600'

  if (qty <= 10) return 'bg-yellow-100 text-yellow-700'

  return 'bg-green-100 text-green-700'
}

//==============================
// XÓA KHỎI ĐỢT
//==============================

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

    await loadData() // đợi load xong

    closeDelete()
  } catch (e) {
    console.log(e)
  } finally {
    loading.value = false
  }
}
watch(
  () => props.dot,

  async (value) => {
    if (!value) return

    await loadData()
  },

  {
    immediate: true,
  },
)
//==============================
// QUAY LẠI
//==============================
</script>
<template>
  <div class="h-full bg-slate-100">
    <!-- HEADER -->
    <div class="bg-white border-b shadow-sm">
      <div class="w-full px-10 py-4">
        <div class="flex items-center justify-between">
          <div class="flex items-center gap-4">
            <div>
              <h1 class="text-xl font-bold text-slate-800">Quản lý sản phẩm giảm giá</h1>

              <p class="text-slate-500 mt-1">Danh sách sản phẩm đang áp dụng trong đợt giảm giá</p>
            </div>
          </div>
          <button
            @click="openAdd"
            class="bg-indigo-600 hover:bg-indigo-700 text-white px-5 py-3 rounded-xl font-semibold"
          >
            + Thêm sản phẩm
          </button>
        </div>
      </div>
    </div>

    <!-- CONTENT -->
    <div class="w-full px-10 py-6">
      <!-- CARD THỐNG KÊ -->
      <div class="grid grid-cols-3 gap-4 mb-4">
        <div class="bg-white rounded-xl shadow-sm border p-6">
          <div class="text-slate-500 text-sm">Sản phẩm đang giảm</div>

          <div class="text-3xl font-bold text-indigo-600 mt-2">
            {{ tongSanPham }}
          </div>
        </div>

        <div class="bg-white rounded-xl shadow-sm border p-6">
          <div class="text-slate-500 text-sm">Tổng tồn kho</div>

          <div class="text-3xl font-bold text-green-600 mt-2">
            {{ money(tongTonKho) }}
          </div>
        </div>

        <div class="bg-white rounded-xl shadow-sm border p-6">
          <div class="text-slate-500 text-sm">Tổng giá trị sau giảm</div>

          <div class="text-3xl font-bold text-red-600 mt-2">{{ money(tongGiaTri) }} đ</div>
        </div>
      </div>

      <!-- SEARCH -->

      <div class="bg-white rounded-xl shadow-sm border p-4 mb-4">
        <div class="flex gap-4 items-center">
          <input
            v-model="search"
            placeholder="🔍 Tìm theo mã hoặc tên sản phẩm..."
            class="flex-1 border rounded-lg px-4 py-3 outline-none focus:ring-2 focus:ring-indigo-500"
          />

          <div class="bg-indigo-600 text-white px-5 py-3 rounded-lg font-semibold">
            {{ filteredProducts.length }} sản phẩm
          </div>
        </div>
      </div>
      <!-- DANH SÁCH SẢN PHẨM -->

      <div class="bg-white rounded-xl shadow-sm border overflow-hidden">
        <div class="overflow-x-auto">
          <table class="w-full">
            <thead class="bg-slate-50 border-b">
              <tr class="text-sm text-slate-600">
                <th class="px-4 py-3 text-left w-24">Ảnh</th>

                <th class="px-4 py-3 text-left">Sản phẩm</th>

                <th class="px-4 py-3 text-center">Phân loại</th>

                <th class="px-4 py-3 text-center">Giá</th>

                <th class="px-4 py-3 text-center">Giảm</th>

                <th class="px-4 py-3 text-center">Tồn</th>

                <th class="px-4 py-3 text-center">Thao tác</th>
              </tr>
            </thead>

            <tbody>
              <tr
                v-for="sp in filteredProducts"
                :key="sp.idSanPhamChiTiet"
                class="border-b last:border-0 hover:bg-slate-50 transition"
              >
                <!-- ẢNH -->

                <td class="px-6 py-4">
                  <img
                    :src="sp.anh ? `http://localhost:8080${sp.anh}` : 'https://placehold.co/80x80'"
                    class="w-16 h-16 rounded-xl border object-cover"
                  />
                </td>

                <!-- THÔNG TIN -->

                <td class="px-6 py-4">
                  <div class="font-semibold text-slate-800">
                    {{ sp.tenSanPham }}
                  </div>

                  <div class="text-sm text-slate-500">
                    {{ sp.maSanPham }}
                  </div>

                  <div class="text-xs text-indigo-500">SPCT: {{ sp.maSPCT }}</div>

                  <div class="mt-2">
                    <span class="px-2 py-1 rounded-md bg-indigo-100 text-indigo-700 text-xs">
                      {{ sp.thuongHieu }}
                    </span>
                  </div>
                </td>

                <!-- PHÂN LOẠI -->

                <td class="px-6 py-4 text-center">
                  <div class="flex flex-wrap justify-center gap-1">
                    <span class="px-2 py-1 rounded bg-yellow-100 text-xs">
                      {{ sp.mauSac }}
                    </span>

                    <span class="px-2 py-1 rounded bg-purple-100 text-xs">
                      {{ sp.kichThuoc }}
                    </span>

                    <span class="px-2 py-1 rounded bg-green-100 text-xs">
                      {{ sp.chatLieu }}
                    </span>
                  </div>
                </td>

                <!-- GIÁ -->

                <td class="px-6 py-4 text-center">
                  <div class="text-sm line-through text-slate-400">{{ money(sp.giaGoc) }} đ</div>

                  <div class="text-red-600 font-bold text-lg mt-1">
                    {{ money(sp.giaSauGiam) }} đ
                  </div>
                </td>

                <!-- GIẢM -->

                <td class="px-6 py-4 text-center">
                  <span class="px-3 py-1 rounded-full bg-red-100 text-red-600 font-semibold">
                    -{{ sp.phanTramGiam }}%
                  </span>
                </td>

                <!-- TỒN -->

                <td class="px-6 py-4 text-center">
                  <span
                    class="px-3 py-1 rounded-full text-xs font-semibold"
                    :class="stockClass(sp.soLuongTon)"
                  >
                    {{ sp.soLuongTon }}
                  </span>
                </td>

                <!-- THAO TÁC -->

                <td class="px-6 py-4">
                  <div class="flex justify-center gap-2">
                    <button
                      @click="openDelete(sp)"
                      class="px-3 py-2 rounded-lg bg-red-50 text-red-600 hover:bg-red-100 transition"
                    >
                      Gỡ
                    </button>
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
      <!-- LOADING -->

      <div v-if="loading" class="bg-white rounded-xl shadow-sm border p-16 text-center">
        <div
          class="w-12 h-12 border-4 border-indigo-600 border-t-transparent rounded-full animate-spin mx-auto"
        ></div>

        <div class="mt-5 text-slate-500">Đang tải danh sách sản phẩm...</div>
      </div>
      <!-- EMPTY -->

      <div
        v-else-if="filteredProducts.length == 0"
        class="bg-white rounded-xl shadow-sm border p-20 text-center"
      >
        <div class="text-7xl">📦</div>

        <div class="text-xl font-semibold mt-5 text-slate-700">Chưa có sản phẩm nào</div>

        <div class="text-slate-500 mt-2">Hãy thêm sản phẩm vào đợt giảm giá.</div>

        <button
          class="mt-8 bg-indigo-600 hover:bg-indigo-700 text-white px-6 py-3 rounded-xl font-semibold"
        >
          + Thêm sản phẩm
        </button>
      </div>
      <Teleport to="body">
        <div
          v-if="showModal"
          class="fixed inset-0 bg-black/40 z-[9999] flex items-center justify-center"
        >
          <div class="bg-white rounded-2xl shadow-xl w-[430px] overflow-hidden relative z-[10000]">
            <div class="p-7">
              <div class="text-6xl text-center">⚠️</div>

              <h2 class="text-xl font-bold text-center mt-4">Gỡ sản phẩm?</h2>

              <p class="text-slate-500 text-center mt-3">
                Bạn có chắc muốn gỡ

                <b>
                  {{ selected?.tenSanPham }}
                </b>

                khỏi đợt giảm giá?
              </p>
            </div>

            <div class="bg-slate-50 border-t px-6 py-4 flex justify-end gap-3">
              <button @click="closeDelete" class="px-5 py-2 rounded-lg border">Hủy</button>

              <button
                @click="removeProduct"
                class="px-5 py-2 rounded-lg bg-red-600 text-white hover:bg-red-700"
              >
                Gỡ khỏi đợt
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
