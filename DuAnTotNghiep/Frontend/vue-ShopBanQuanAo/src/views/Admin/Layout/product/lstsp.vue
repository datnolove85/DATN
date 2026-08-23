<template>
  <div
    class="product-admin-page min-h-screen bg-[#f6f3ee] text-[#292522] font-sans antialiased selection:bg-[#8b3a4a] selection:text-white p-4 sm:p-8 animate-fade-in"
  >
    <div class="max-w-[1500px] mx-auto space-y-8">
      <!-- ================= BANNER QUẢN LÝ SẢN PHẨM ================= -->
      <div
        class="bg-white p-6 rounded-2xl border border-slate-200 shadow-xs flex flex-col xl:flex-row justify-between items-start xl:items-center gap-5"
      >
        <!-- Nội dung banner -->
        <div class="flex items-start gap-4">
          <!-- Icon -->
          <div class="w-12 h-12 rounded-2xl bg-[#f3e7e8] flex items-center justify-center shrink-0">
            <i class="fa-solid fa-box-open text-[#8b3a4a] text-xl"></i>
          </div>

          <div>
            <h1 class="text-xl font-black text-[#292522] flex items-center gap-2">
              Quản Lý Sản Phẩm
            </h1>

            <p class="text-sm text-slate-500 mt-1 max-w-3xl">
              Quản lý toàn bộ sản phẩm, biến thể, danh mục, thương hiệu và chất liệu trong hệ thống
              bán hàng.
            </p>
          </div>
        </div>

        <!-- Nút thao tác -->
        <button
          @click="openAddModal"
          class="w-full xl:w-auto px-5 py-3 rounded-xl bg-blue-600 text-white text-sm font-semibold hover:bg-blue-700 hover:shadow-lg hover:shadow-blue-600/20 transition-all duration-300 flex items-center justify-center gap-2"
        >
          <i class="fa-solid fa-plus"></i>
          Tạo sản phẩm mới
        </button>
      </div>

      <div class="grid grid-cols-2 lg:grid-cols-4 gap-3">
        <div class="admin-stat">
          <span>Tổng sản phẩm</span><strong>{{ tongSanPham }}</strong
          ><small>Danh mục sản phẩm</small>
        </div>
        <div class="admin-stat">
          <span>Đang kinh doanh</span><strong>{{ tongSanPhamDangKD }}</strong
          ><small>Đang hiển thị bán</small>
        </div>
        <div class="admin-stat">
          <span>Tổng biến thể</span><strong>{{ tongSPCT }}</strong
          ><small>Màu / size / tồn kho</small>
        </div>
        <div class="admin-stat">
          <span>Biến thể hoạt động</span><strong>{{ tongSPCTDangKD }}</strong
          ><small>Sẵn sàng kinh doanh</small>
        </div>
      </div>

      <div
        class="product-admin-panel bg-white border border-[#e8e1d8] rounded-[24px] shadow-[0_14px_45px_rgba(41,37,34,.06)] overflow-hidden"
      >
        <div
          class="p-5 border-b border-[#eee8df] bg-[#fcfaf7] flex flex-col lg:flex-row items-center gap-4"
        >
          <div class="relative w-full lg:max-w-xs">
            <span class="absolute inset-y-0 left-0 flex items-center pl-3.5 text-slate-400"
              ><svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path
                  stroke-width="2"
                  stroke-linecap="round"
                  stroke-linejoin="round"
                  d="M21 21l-5.197-5.197m0 0A7.5 7.5 0 105.196 5.196a7.5 7.5 0 0010.604 10.604z"
                /></svg
            ></span>
            <input
              v-model="filters.search"
              type="text"
              placeholder="Tìm kiếm..."
              class="w-full pl-10 pr-4 py-3 bg-white border border-[#e5ddd3] rounded-xl text-xs outline-none focus:border-[#b28a4a] focus:ring-4 ring-[#b28a4a]/10"
            />
          </div>
          <div class="grid grid-cols-2 sm:grid-cols-4 lg:grid-cols-5 gap-3 w-full flex-1">
            <select
              v-model="filters.idDanhMuc"
              class="border border-[#e5ddd3] px-3 py-2.5 rounded-xl text-xs bg-white focus:border-[#b28a4a] outline-none"
            >
              <option :value="null">Danh mục</option>
              <option v-for="i in danhMucs" :key="i.id" :value="i.id">{{ i.tenDanhMuc }}</option>
            </select>
            <select
              v-model="filters.idThuongHieu"
              class="border border-[#e5ddd3] px-3 py-2.5 rounded-xl text-xs bg-white focus:border-[#b28a4a] outline-none"
            >
              <option :value="null">Thương hiệu</option>
              <option v-for="i in thuongHieus" :key="i.id" :value="i.id">
                {{ i.tenThuongHieu }}
              </option>
            </select>
            <select
              v-model="filters.idChatLieu"
              class="border border-[#e5ddd3] px-3 py-2.5 rounded-xl text-xs bg-white focus:border-[#b28a4a] outline-none"
            >
              <option :value="null">Chất liệu</option>
              <option v-for="i in chatLieus" :key="i.id" :value="i.id">{{ i.tenChatLieu }}</option>
            </select>
            <select
              v-model="filters.trangThai"
              class="border border-[#e5ddd3] px-3 py-2.5 rounded-xl text-xs bg-white focus:border-[#b28a4a] outline-none"
            >
              <option :value="null">Trạng thái</option>
              <option :value="true">Kinh doanh</option>
              <option :value="false">Ngừng kinh doanh</option>
            </select>
            <button
              @click="clearFilters"
              class="border px-3 py-2.5 rounded-xl text-xs bg-rose-50 text-rose-600 hover:bg-rose-100 transition-colors"
            >
              Xóa bộ lọc
            </button>
          </div>
        </div>

        <div class="overflow-x-auto">
          <table class="w-full min-w-[980px] text-left">
            <thead
              class="text-[10px] font-black text-slate-400 uppercase tracking-[.12em] border-b bg-[#faf7f2]"
            >
              <tr>
                <th class="px-5 py-4">Mã SP</th>
                <th class="px-5 py-4">Sản phẩm</th>
                <th class="px-5 py-4">Danh mục</th>
                <th class="px-5 py-4">Thương hiệu</th>
                <th class="px-5 py-4">Chất liệu</th>
                <th class="px-6 py-4 text-center">Số lượng</th>
                <th class="px-5 py-4">Trạng thái</th>
                <th class="px-6 py-4 text-right">Thao tác</th>
              </tr>
            </thead>
            <tbody class="divide-y divide-slate-100">
              <template v-for="product in paginatedProducts" :key="product.id">
                <tr class="product-admin-row hover:bg-[#fcfaf7] transition-colors">
                  <td class="px-5 py-4">
                    <span
                      class="font-mono text-xs bg-indigo-50 text-indigo-600 px-2 py-1 rounded-lg"
                      >{{ product.maSanPham }}</span
                    >
                  </td>
                  <td class="px-5 py-4 flex items-center gap-3">
                    <div
                      class="w-10 h-10 bg-[#f4efe8] rounded-xl flex items-center justify-center text-lg border border-[#e9dfd2]"
                    >
                      👕
                    </div>
                    {{ product.tenSanPham }}
                  </td>
                  <td class="px-6 py-4 text-xs">{{ product.idDanhMuc?.tenDanhMuc }}</td>
                  <td class="px-6 py-4 text-xs">{{ product.idThuongHieu?.tenThuongHieu }}</td>
                  <td class="px-6 py-4 text-xs">{{ product.idChatLieu?.tenChatLieu }}</td>
                  <td class="px-6 py-4 text-center font-semibold">{{ product.soLuong }}</td>
                  <td
                    class="px-6 py-4 text-xs font-bold"
                    :class="product.trangThai ? 'text-emerald-600' : 'text-slate-400'"
                  >
                    {{ product.trangThai ? 'Kinh doanh' : 'Ngừng kinh doanh' }}
                  </td>
                  <td class="px-6 py-4 text-right flex justify-end gap-2">
                    <button
                      @click="toggleSPCT(product)"
                      class="p-2 hover:bg-emerald-50 rounded-lg text-slate-400 hover:text-emerald-600 transition-all"
                    >
                      <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path
                          stroke-width="2"
                          stroke-linecap="round"
                          stroke-linejoin="round"
                          d="M12 4v16m8-8H4"
                        />
                      </svg>
                    </button>
                    <button
                      @click="openEditModal(product)"
                      class="p-2 hover:bg-indigo-50 rounded-lg text-slate-400 hover:text-indigo-600 transition-all"
                    >
                      <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path
                          stroke-width="2"
                          d="M15.232 5.232l3.536 3.536m-2.036-5.036a2.5 2.5 0 113.536 3.536L6.5 21.036H3v-3.572L16.732 3.732z"
                        />
                      </svg>
                    </button>
                    <button
                      @click="triggerDeleteConfirm(product.id)"
                      class="p-2 hover:bg-rose-50 rounded-lg text-slate-400 hover:text-rose-600 transition-all"
                    >
                      <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path
                          stroke-width="2"
                          d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16"
                        />
                      </svg>
                    </button>
                  </td>
                </tr>
                <tr v-if="expandedProductId === product.id" class="bg-slate-50/80">
                  <td colspan="8" class="px-6 py-6">
                    <div class="flex justify-between items-center mb-4">
                      <div>
                        <h3 class="font-bold text-sm text-slate-800">{{ product.tenSanPham }}</h3>
                        <p class="text-xs text-slate-400">Danh sách biến thể chi tiết kho hàng</p>
                      </div>
                      <div class="flex items-center gap-3">
                        <span
                          class="text-xs text-slate-500 bg-white px-3 py-1.5 rounded-xl border border-slate-200"
                        >
                          Đang kinh doanh:
                          <strong class="text-indigo-600">{{ product.thongKe }}</strong>
                        </span>
                        <button
                          @click="isBulkOpen = true"
                          class="px-4 py-2 bg-slate-800 hover:bg-slate-900 text-white rounded-xl text-xs font-bold transition-all shadow-sm"
                        >
                          ⚡ Tạo hàng loạt
                        </button>
                      </div>
                    </div>
                    <div
                      class="w-full bg-white rounded-2xl border border-slate-200/80 shadow-xs overflow-hidden"
                    >
                      <table class="w-full text-sm">
                        <thead class="bg-slate-50/70 border-b border-slate-100">
                          <tr>
                            <th
                              class="px-4 py-3 text-center text-[11px] font-bold text-slate-400 uppercase tracking-wider"
                            >
                              Hình ảnh
                            </th>
                            <th
                              class="px-4 py-3 text-left text-[11px] font-bold text-slate-400 uppercase tracking-wider"
                            >
                              Mã SPCT
                            </th>
                            <th
                              class="px-4 py-3 text-left text-[11px] font-bold text-slate-400 uppercase tracking-wider"
                            >
                              Màu sắc
                            </th>
                            <th
                              class="px-4 py-3 text-left text-[11px] font-bold text-slate-400 uppercase tracking-wider"
                            >
                              Kích thước
                            </th>
                            <th
                              class="px-4 py-3 text-center text-[11px] font-bold text-slate-400 uppercase tracking-wider"
                            >
                              Tồn kho
                            </th>
                            <th
                              class="px-4 py-3 text-center text-[11px] font-bold text-slate-400 uppercase tracking-wider"
                            >
                              Tạm giữ
                            </th>
                            <th
                              class="px-4 py-3 text-center text-[11px] font-bold text-slate-400 uppercase tracking-wider"
                            >
                              Khả dụng
                            </th>
                            <th
                              class="px-4 py-3 text-right text-[11px] font-bold text-slate-400 uppercase tracking-wider"
                            >
                              Giá nhập
                            </th>
                            <th
                              class="px-4 py-3 text-right text-[11px] font-bold text-slate-400 uppercase tracking-wider"
                            >
                              Giá bán
                            </th>
                            <th
                              class="px-4 py-3 text-center text-[11px] font-bold text-slate-400 uppercase tracking-wider"
                            >
                              Trạng thái
                            </th>
                            <th
                              class="px-4 py-3 text-right text-[11px] font-bold text-slate-400 uppercase tracking-wider"
                            >
                              Thao tác
                            </th>
                          </tr>
                        </thead>
                        <tbody class="divide-y divide-slate-100">
                          <tr
                            v-for="spct in spctList"
                            :key="spct.id"
                            class="hover:bg-slate-50/80 transition-colors"
                          >
                            <td class="px-4 py-3 text-center">
                              <div class="flex flex-wrap gap-2 justify-center items-center">
                                <div
                                  class="w-11 h-11 rounded-xl border border-slate-200 overflow-hidden shadow-xs bg-slate-50"
                                >
                                  <img
                                    v-if="spct.images?.length"
                                    :src="'http://localhost:8080' + spct.images[0]"
                                    class="w-full h-full object-cover"
                                  />

                                  <div
                                    v-else
                                    class="w-full h-full flex items-center justify-center bg-slate-100 text-slate-400 text-[9px] font-medium"
                                  >
                                    No Image
                                  </div>
                                </div>

                                <button
                                  @click="openImageManager(spct)"
                                  class="bg-indigo-50 text-indigo-600 px-2.5 py-1.5 rounded-lg text-[10px] font-bold hover:bg-indigo-100 transition-colors"
                                >
                                  Ảnh
                                </button>
                              </div>
                            </td>
                            <td class="px-4 py-3">
                              <span
                                class="font-mono text-xs bg-indigo-50 text-indigo-600 px-2.5 py-1 rounded-lg font-medium whitespace-nowrap"
                              >
                                {{ spct.maSanPhamChiTiet }}
                              </span>
                            </td>

                            <td class="px-4 py-3 text-xs font-medium text-slate-700">
                              {{ spct.tenMauSac }}
                            </td>
                            <td class="px-4 py-3 text-xs font-medium text-slate-700">
                              {{ spct.tenKichThuoc }}
                            </td>

                            <!-- Tồn kho -->
                            <td class="px-4 py-3 text-center text-xs font-semibold text-slate-700">
                              {{ spct.soLuongTon }}
                            </td>

                            <!-- Tạm giữ -->
                            <td class="px-4 py-3 text-center text-xs">
                              <span
                                class="inline-block px-2 py-0.5 rounded-md font-semibold"
                                :class="
                                  spct.soLuongTamGiu > 0
                                    ? 'bg-amber-50 text-amber-600'
                                    : 'text-slate-400'
                                "
                              >
                                {{ spct.soLuongTamGiu }}
                              </span>
                            </td>

                            <!-- Khả dụng -->
                            <td class="px-4 py-3 text-center text-xs">
                              <span
                                class="inline-block px-2.5 py-1 rounded-lg font-bold shadow-xs"
                                :class="
                                  spct.soLuongKhaDung > 0
                                    ? 'bg-emerald-50 text-emerald-600 border border-emerald-100/60'
                                    : 'bg-rose-50 text-rose-600 border border-rose-100/60'
                                "
                              >
                                {{ spct.soLuongKhaDung }}
                              </span>
                            </td>

                            <td class="px-4 py-3 text-right text-xs text-slate-600">
                              {{ formatCurrency(spct.giaNhap) }}
                            </td>
                            <td class="px-4 py-3 text-right text-xs">
                              <!-- Khi biến thể đang trong đợt giảm giá -->
                              <div v-if="spct.dangGiamGia" class="flex flex-col items-end gap-1">
                                <div class="flex items-center gap-1 whitespace-nowrap">
                                  <span
                                    class="inline-flex items-center gap-1 bg-rose-50 text-rose-600 px-1.5 py-0.5 rounded text-[10px] font-bold border border-rose-100"
                                  >
                                    🔥 -{{ spct.phanTramGiam }}%
                                  </span>
                                </div>

                                <!-- Giá sau giảm -->
                                <span class="text-indigo-600 font-bold whitespace-nowrap">
                                  {{ formatCurrency(spct.giaSauGiam) }}
                                </span>

                                <!-- Giá gốc bị gạch ngang -->
                                <span
                                  class="text-[10px] text-slate-400 line-through whitespace-nowrap"
                                >
                                  {{ formatCurrency(spct.giaBan) }}
                                </span>
                              </div>

                              <!-- Khi không giảm giá -->
                              <div v-else class="text-indigo-600 font-bold whitespace-nowrap">
                                {{ formatCurrency(spct.giaBan) }}
                              </div>
                            </td>
                            <td class="px-4 py-3 text-center">
                              <span
                                class="px-2.5 py-1 rounded-full text-[10px] font-bold shadow-2xs"
                                :class="
                                  spct.trangThai
                                    ? 'bg-emerald-100 text-emerald-700'
                                    : 'bg-slate-100 text-slate-500'
                                "
                                >{{ spct.trangThai ? 'Đang bán' : 'Ngừng bán' }}</span
                              >
                            </td>
                            <td class="px-4 py-3">
                              <div class="flex justify-end gap-1.5">
                                <button
                                  @click="editSPCT(spct)"
                                  class="px-3 py-1.5 rounded-lg bg-indigo-50 text-indigo-600 hover:bg-indigo-100 text-[10px] font-bold transition-colors"
                                >
                                  Sửa
                                </button>
                                <button
                                  @click="removeSPCT(spct.id)"
                                  class="px-3 py-1.5 rounded-lg bg-rose-50 text-rose-600 hover:bg-rose-100 text-[10px] font-bold transition-colors"
                                >
                                  Xóa
                                </button>
                              </div>
                            </td>
                          </tr>
                          <tr v-if="spctList.length === 0">
                            <td colspan="11" class="text-center py-10 text-slate-400 text-xs">
                              Chưa có biến thể nào
                            </td>
                          </tr>
                        </tbody>
                      </table>
                    </div>
                  </td>
                </tr>
              </template>
            </tbody>
          </table>
        </div>
        <div
          class="px-6 py-4 bg-white border-t border-slate-200 flex flex-col sm:flex-row justify-between items-center gap-4"
        >
          <div class="text-xs text-slate-500 flex items-center gap-2">
            <span>Hiển thị</span>
            <select
              v-model="pageSize"
              class="bg-slate-50 border border-slate-200 text-slate-700 text-xs rounded-lg px-2 py-1 focus:outline-none focus:border-indigo-500 font-semibold"
            >
              <option :value="10">10</option>
              <option :value="20">20</option>
              <option :value="50">50</option>
            </select>
            <span
              >trong tổng số
              <strong class="text-slate-800">{{ filteredProducts.length }}</strong> sản phẩm</span
            >
          </div>

          <div class="flex items-center gap-1.5">
            <button
              @click="changePage(currentPage - 1)"
              :disabled="currentPage === 1"
              :class="[
                'px-3 py-1.5 rounded-lg text-xs font-bold transition-all border',
                currentPage === 1
                  ? 'bg-slate-50 text-slate-300 border-slate-200 cursor-not-allowed'
                  : 'bg-white text-slate-600 border-slate-200 hover:bg-slate-50 hover:border-slate-300',
              ]"
            >
              Trước
            </button>

            <div class="flex items-center gap-1">
              <button
                v-for="page in totalPages"
                :key="page"
                @click="changePage(page)"
                :class="[
                  'w-8 h-8 rounded-lg text-xs font-bold transition-all flex items-center justify-center',
                  currentPage === page
                    ? 'bg-indigo-600 text-white shadow-sm shadow-indigo-200'
                    : 'bg-white text-slate-600 border border-slate-200 hover:bg-slate-50',
                ]"
              >
                {{ page }}
              </button>
            </div>

            <button
              @click="changePage(currentPage + 1)"
              :disabled="currentPage === totalPages || totalPages === 0"
              :class="[
                'px-3 py-1.5 rounded-lg text-xs font-bold transition-all border',
                currentPage === totalPages || totalPages === 0
                  ? 'bg-slate-50 text-slate-300 border-slate-200 cursor-not-allowed'
                  : 'bg-white text-slate-600 border-slate-200 hover:bg-slate-50 hover:border-slate-300',
              ]"
            >
              Sau
            </button>
          </div>
        </div>
      </div>
    </div>

    <transition name="fade">
      <div
        v-if="isModalOpen"
        class="fixed inset-0 z-[999] flex items-center justify-center bg-black/40 backdrop-blur-[2px] p-4"
      >
        <div class="bg-white w-full max-w-2xl rounded-3xl shadow-2xl overflow-hidden">
          <!-- Header -->
          <div class="flex items-center justify-between px-8 py-6 border-b">
            <h2 class="text-2xl font-bold text-slate-800">
              {{ isEditMode ? 'Cập nhật sản phẩm' : 'Thêm sản phẩm' }}
            </h2>

            <button
              type="button"
              @click="isModalOpen = false"
              class="w-9 h-9 rounded-full hover:bg-slate-100 flex items-center justify-center transition"
            >
              ✕
            </button>
          </div>

          <!-- Form -->
          <form @submit.prevent="handleSubmit" class="p-6">
            <div class="space-y-5">
              <!-- Tên sản phẩm -->
              <div>
                <label
                  class="block text-xs font-semibold uppercase tracking-wider text-slate-500 mb-2"
                >
                  Tên sản phẩm
                </label>

                <input
                  ref="tenSanPhamInput"
                  v-model="form.tenSanPham"
                  type="text"
                  placeholder="Nhập tên sản phẩm..."
                  class="w-full rounded-2xl border border-slate-200 bg-slate-50 px-4 py-3 outline-none transition-all focus:bg-white focus:border-indigo-500 focus:ring-4 focus:ring-indigo-100"
                />
              </div>

              <!-- Danh mục - Thương hiệu -->
              <div class="grid grid-cols-2 gap-4">
                <div>
                  <label
                    class="block text-xs font-semibold uppercase tracking-wider text-slate-500 mb-2"
                  >
                    Danh mục
                  </label>

                  <select
                    v-model="form.idDanhMuc"
                    class="w-full rounded-2xl border border-slate-200 bg-slate-50 px-4 py-3 outline-none transition-all focus:bg-white focus:border-indigo-500 focus:ring-4 focus:ring-indigo-100"
                  >
                    <option :value="null">Chọn danh mục</option>

                    <option v-for="dm in danhMucs" :key="dm.id" :value="dm.id">
                      {{ dm.tenDanhMuc }}
                    </option>
                  </select>
                </div>

                <div>
                  <label
                    class="block text-xs font-semibold uppercase tracking-wider text-slate-500 mb-2"
                  >
                    Thương hiệu
                  </label>

                  <select
                    v-model="form.idThuongHieu"
                    class="w-full rounded-2xl border border-slate-200 bg-slate-50 px-4 py-3 outline-none transition-all focus:bg-white focus:border-indigo-500 focus:ring-4 focus:ring-indigo-100"
                  >
                    <option :value="null">Chọn thương hiệu</option>

                    <option v-for="th in thuongHieus" :key="th.id" :value="th.id">
                      {{ th.tenThuongHieu }}
                    </option>
                  </select>
                </div>
              </div>

              <!-- Chất liệu - Trạng thái -->
              <div class="grid grid-cols-2 gap-4">
                <div>
                  <label
                    class="block text-xs font-semibold uppercase tracking-wider text-slate-500 mb-2"
                  >
                    Chất liệu
                  </label>

                  <select
                    v-model="form.idChatLieu"
                    class="w-full rounded-2xl border border-slate-200 bg-slate-50 px-4 py-3 outline-none transition-all focus:bg-white focus:border-indigo-500 focus:ring-4 focus:ring-indigo-100"
                  >
                    <option :value="null">Chọn chất liệu</option>

                    <option v-for="cl in chatLieus" :key="cl.id" :value="cl.id">
                      {{ cl.tenChatLieu }}
                    </option>
                  </select>
                </div>

                <div>
                  <label
                    class="block text-xs font-semibold uppercase tracking-wider text-slate-500 mb-2"
                  >
                    Trạng thái
                  </label>

                  <select
                    v-model="form.trangThai"
                    class="w-full rounded-2xl border border-slate-200 bg-slate-50 px-4 py-3 outline-none transition-all focus:bg-white focus:border-indigo-500 focus:ring-4 focus:ring-indigo-100"
                  >
                    <option :value="true">🟢 Đang kinh doanh</option>
                    <option :value="false">🔴 Ngừng kinh doanh</option>
                  </select>
                </div>
              </div>

              <!-- Mô tả -->
              <div>
                <label
                  class="block text-xs font-semibold uppercase tracking-wider text-slate-500 mb-2"
                >
                  Mô tả sản phẩm
                </label>

                <textarea
                  v-model="form.moTa"
                  rows="5"
                  placeholder="Nhập mô tả sản phẩm..."
                  class="w-full rounded-2xl border border-slate-200 bg-slate-50 px-4 py-3 resize-none outline-none transition-all focus:bg-white focus:border-indigo-500 focus:ring-4 focus:ring-indigo-100"
                ></textarea>
              </div>
            </div>

            <!-- Footer -->
            <div class="mt-8 pt-5 border-t border-slate-200 flex justify-end gap-3">
              <button
                type="button"
                @click="isModalOpen = false"
                class="px-5 py-2.5 rounded-xl border border-slate-300 text-slate-700 font-semibold hover:bg-slate-100 transition"
              >
                Hủy
              </button>

              <button
                type="submit"
                class="px-6 py-2.5 rounded-xl bg-indigo-600 hover:bg-indigo-700 text-white font-semibold shadow-lg shadow-indigo-200 transition"
              >
                {{ isEditMode ? '💾 Cập nhật' : '➕ Thêm sản phẩm' }}
              </button>
            </div>
          </form>
        </div>
      </div>
    </transition>

    <transition name="fade">
      <div
        v-if="isSPCTModalOpen"
        class="fixed inset-0 z-[999] bg-black/50 backdrop-blur-sm flex items-center justify-center p-4"
      >
        <div class="bg-white w-full max-w-3xl rounded-2xl shadow-2xl overflow-hidden">
          <!-- Header -->
          <div
            class="px-5 py-4 border-b bg-gradient-to-r from-indigo-600 to-indigo-500 text-white flex justify-between items-center"
          >
            <div>
              <h2 class="text-xl font-bold">
                {{ isEditSPCT ? 'Cập nhật biến thể' : 'Thêm biến thể mới' }}
              </h2>

              <p class="text-indigo-100 mt-1 text-sm">
                {{ selectedProduct?.tenSanPham }}
              </p>
            </div>

            <button
              @click="isSPCTModalOpen = false"
              class="w-10 h-10 rounded-xl hover:bg-white/20 transition"
            >
              ✕
            </button>
          </div>

          <!-- Body -->
          <div class="p-5 space-y-5 max-h-[72vh] overflow-y-auto">
            <!-- Product -->
            <div
              class="flex items-center justify-between rounded-xl border border-slate-200 bg-slate-50 px-4 py-3"
            >
              <div class="flex items-center gap-3">
                <div
                  class="w-10 h-10 rounded-lg bg-indigo-100 text-indigo-600 flex items-center justify-center text-lg"
                >
                  👕
                </div>

                <div>
                  <div class="flex items-center gap-2">
                    <span class="text-xs text-slate-500">Sản phẩm</span>

                    <span
                      class="px-2 py-0.5 rounded-full bg-indigo-100 text-indigo-600 text-[10px] font-semibold"
                    >
                      {{ selectedProduct?.maSanPham }}
                    </span>
                  </div>

                  <p class="font-semibold text-slate-800">
                    {{ selectedProduct?.tenSanPham }}
                  </p>
                </div>
              </div>

              <div v-if="isEditSPCT" class="text-right">
                <p class="text-xs text-slate-500">Mã biến thể</p>

                <p class="font-semibold text-indigo-600">
                  {{ formData.maSanPhamChiTiet }}
                </p>
              </div>
            </div>

            <!-- Thuộc tính -->
            <div class="grid md:grid-cols-2 gap-5">
              <div>
                <label class="text-xs font-bold uppercase text-slate-500 mb-2 block">
                  Màu sắc
                </label>

                <select
                  v-model="formData.idMauSac"
                  class="w-full border rounded-xl p-3 outline-none focus:ring-2 ring-indigo-300"
                >
                  <option :value="null">Chọn màu sắc</option>

                  <option v-for="mau in mauSacs" :key="mau.id" :value="mau.id">
                    {{ mau.tenMauSac }}
                  </option>
                </select>
              </div>

              <div>
                <label class="text-xs font-bold uppercase text-slate-500 mb-2 block">
                  Kích thước
                </label>

                <select
                  v-model="formData.idKichThuoc"
                  class="w-full border rounded-xl p-3 outline-none focus:ring-2 ring-indigo-300"
                >
                  <option :value="null">Chọn kích thước</option>

                  <option v-for="size in kichThuocs" :key="size.id" :value="size.id">
                    {{ size.tenKichThuoc }}
                  </option>
                </select>
              </div>
            </div>

            <!-- Giá -->
            <div class="grid md:grid-cols-3 gap-5">
              <!-- Giá nhập -->
              <div class="bg-slate-50 rounded-2xl p-4 border">
                <label class="text-xs font-bold uppercase text-slate-500">Giá nhập</label>
                <input
                  v-model.number="formData.giaNhap"
                  type="number"
                  class="mt-2 w-full bg-white border rounded-xl p-3 outline-none focus:ring-2 ring-indigo-300 text-sm"
                />
              </div>

              <!-- Giá bán -->
              <div class="bg-slate-50 rounded-2xl p-4 border">
                <label
                  class="text-xs font-bold uppercase text-slate-500 flex items-center justify-between"
                >
                  <span>Giá bán</span>
                  <span
                    v-if="formData.dangGiamGia"
                    class="text-rose-500 font-normal text-[10px] normal-case"
                  >
                    (🔒 Đang giảm giá)
                  </span>
                </label>

                <input
                  v-model="formData.giaBan"
                  type="number"
                  :disabled="formData.dangGiamGia"
                  :class="[
                    'mt-2 w-full border rounded-xl p-3 outline-none text-sm transition-all',
                    formData.dangGiamGia
                      ? 'bg-slate-100 text-slate-400 cursor-not-allowed border-slate-200'
                      : 'bg-white border-slate-200 focus:ring-2 ring-indigo-300',
                  ]"
                />
              </div>

              <!-- Tồn kho -->
              <div class="bg-slate-50 rounded-2xl p-4 border">
                <label class="text-xs font-bold uppercase text-slate-500">Tồn kho</label>
                <input
                  v-model.number="formData.soLuongTon"
                  type="number"
                  class="mt-2 w-full bg-white border rounded-xl p-3 outline-none focus:ring-2 ring-indigo-300 text-sm"
                />
              </div>
            </div>

            <!-- Trạng thái -->
            <div>
              <label class="text-xs font-bold uppercase text-slate-500 block mb-2">
                Trạng thái
              </label>

              <select
                v-model="formData.trangThai"
                class="w-full border rounded-xl p-3 outline-none focus:ring-2 ring-indigo-300"
              >
                <option :value="true">🟢 Đang kinh doanh</option>

                <option :value="false">🔴 Ngừng kinh doanh</option>
              </select>
            </div>

            <!-- Upload -->
            <div>
              <label class="text-xs font-bold uppercase text-slate-500 block mb-3">
                Hình ảnh
              </label>

              <label
                class="border-2 border-dashed border-slate-300 rounded-2xl h-28 flex flex-col items-center justify-center cursor-pointer hover:border-indigo-500 transition"
              >
                <div class="text-5xl">📷</div>

                <div class="font-semibold mt-2">Chọn ảnh hoặc kéo thả vào đây</div>

                <div class="text-sm text-slate-400">PNG, JPG (tối đa 5 ảnh)</div>

                <input type="file" multiple class="hidden" @change="handleFileUpload" />
              </label>

              <div v-if="previewImages.length" class="grid grid-cols-5 gap-4 mt-5">
                <div v-for="(img, i) in previewImages" :key="i" class="relative group">
                  <img :src="img" class="w-full h-24 object-cover rounded-xl border" />

                  <div
                    class="absolute inset-0 bg-black/40 opacity-0 group-hover:opacity-100 rounded-xl transition flex items-center justify-center text-white text-xs"
                  >
                    Ảnh {{ i + 1 }}
                  </div>
                </div>
              </div>
            </div>
          </div>

          <!-- Footer -->
          <div class="bg-slate-50 border-t px-8 py-5 flex justify-end gap-3">
            <button
              @click="isSPCTModalOpen = false"
              class="px-6 py-3 rounded-xl border hover:bg-white font-semibold transition"
            >
              Hủy
            </button>

            <button
              @click="submitSPCT"
              class="px-8 py-3 rounded-xl bg-indigo-600 hover:bg-indigo-700 text-white font-semibold shadow-lg transition"
            >
              {{ isEditSPCT ? 'Cập nhật biến thể' : 'Thêm biến thể' }}
            </button>
          </div>
        </div>
      </div>
    </transition>

    <!-- ================= TẠO BIẾN THỂ HÀNG LOẠT ================= -->
    <div
      v-if="isBulkOpen"
      class="fixed inset-0 z-[999] flex items-center justify-center bg-slate-950/60 p-3 sm:p-6 backdrop-blur-sm"
    >
      <div
        class="flex max-h-[94vh] w-full max-w-[1400px] flex-col overflow-hidden rounded-[28px] border border-[#e8e1d8] bg-[#fcfaf7] shadow-[0_30px_100px_rgba(20,15,10,.22)]"
      >
        <!-- Header -->
        <div
          class="flex shrink-0 flex-col gap-4 border-b border-[#e8e1d8] bg-white px-5 py-5 sm:px-7 lg:flex-row lg:items-center lg:justify-between"
        >
          <div>
            <div class="flex items-center gap-3">
              <div
                class="flex h-11 w-11 items-center justify-center rounded-2xl bg-[#f3e7e8] text-[#8b3a4a]"
              >
                <i class="fa-solid fa-layer-group text-lg"></i>
              </div>
              <div>
                <p class="text-[10px] font-black uppercase tracking-[0.18em] text-[#b28a4a]">
                  Quản lý biến thể
                </p>
                <h2 class="text-xl font-black text-[#292522] sm:text-2xl">
                  Tạo biến thể hàng loạt
                </h2>
              </div>
            </div>
            <p class="mt-2 text-xs text-slate-500 sm:text-sm">
              Chọn màu và kích thước, sau đó nhập trực tiếp trên bảng. Nhấn
              <kbd
                class="rounded-md border border-slate-200 bg-slate-50 px-1.5 py-0.5 font-bold text-slate-600"
                >Enter</kbd
              >
              để chuyển sang ô tiếp theo.
            </p>
          </div>

          <button
            type="button"
            @click="resetBulkForm"
            class="flex h-10 w-10 shrink-0 items-center justify-center self-end rounded-full border border-[#e5ddd3] bg-white text-slate-500 transition hover:border-[#8b3a4a] hover:bg-[#f3e7e8] hover:text-[#8b3a4a] lg:self-auto"
            aria-label="Đóng"
          >
            <i class="fa-solid fa-xmark"></i>
          </button>
        </div>

        <!-- Selection -->
        <div class="shrink-0 border-b border-[#e8e1d8] bg-[#fcfaf7] px-5 py-4 sm:px-7">
          <div class="grid gap-4 lg:grid-cols-2">
            <section class="rounded-2xl border border-[#e8e1d8] bg-white p-4">
              <div class="mb-3 flex items-center justify-between gap-3">
                <div>
                  <p class="text-[10px] font-black uppercase tracking-[0.14em] text-slate-400">
                    01 · Màu sắc
                  </p>
                  <p class="mt-1 text-xs text-slate-500">Chọn một hoặc nhiều màu</p>
                </div>
                <button
                  type="button"
                  class="text-[11px] font-bold text-[#8b3a4a] hover:underline"
                  @click="
                    selectedColors = mauSacs.filter((m) => !isColorFull(m.id)).map((m) => m.id)
                  "
                >
                  Chọn tất cả còn trống
                </button>
              </div>
              <div class="flex max-h-28 flex-wrap gap-2 overflow-y-auto pr-1">
                <label
                  v-for="m in mauSacs"
                  :key="m.id"
                  class="group inline-flex cursor-pointer items-center gap-2 rounded-xl border px-3 py-2 text-xs font-semibold transition"
                  :class="
                    isColorFull(m.id)
                      ? 'cursor-not-allowed border-slate-100 bg-slate-50 text-slate-300'
                      : selectedColors.includes(m.id)
                        ? 'border-[#8b3a4a] bg-[#f3e7e8] text-[#8b3a4a] shadow-sm'
                        : 'border-[#e8e1d8] bg-white text-slate-600 hover:border-[#b28a4a] hover:bg-[#fffaf2]'
                  "
                >
                  <input
                    type="checkbox"
                    :value="m.id"
                    v-model="selectedColors"
                    :disabled="isColorFull(m.id)"
                    class="h-4 w-4 rounded border-slate-300 text-[#8b3a4a] accent-[#8b3a4a]"
                  />
                  <span>{{ m.tenMauSac }}</span>
                  <span
                    v-if="isColorFull(m.id)"
                    class="rounded-full bg-slate-100 px-1.5 py-0.5 text-[9px] text-slate-400"
                    >Đã đủ</span
                  >
                </label>
              </div>
            </section>

            <section class="rounded-2xl border border-[#e8e1d8] bg-white p-4">
              <div class="mb-3 flex items-center justify-between gap-3">
                <div>
                  <p class="text-[10px] font-black uppercase tracking-[0.14em] text-slate-400">
                    02 · Kích thước
                  </p>
                  <p class="mt-1 text-xs text-slate-500">Chọn size áp dụng cho các màu đã chọn</p>
                </div>
                <button
                  type="button"
                  class="text-[11px] font-bold text-[#8b3a4a] hover:underline"
                  @click="
                    selectedSizes = kichThuocs.filter((s) => !isSizeFull(s.id)).map((s) => s.id)
                  "
                >
                  Chọn tất cả còn trống
                </button>
              </div>
              <div class="flex max-h-28 flex-wrap gap-2 overflow-y-auto pr-1">
                <label
                  v-for="s in kichThuocs"
                  :key="s.id"
                  class="group inline-flex cursor-pointer items-center gap-2 rounded-xl border px-3 py-2 text-xs font-semibold transition"
                  :class="
                    isSizeFull(s.id)
                      ? 'cursor-not-allowed border-slate-100 bg-slate-50 text-slate-300'
                      : selectedSizes.includes(s.id)
                        ? 'border-[#8b3a4a] bg-[#f3e7e8] text-[#8b3a4a] shadow-sm'
                        : 'border-[#e8e1d8] bg-white text-slate-600 hover:border-[#b28a4a] hover:bg-[#fffaf2]'
                  "
                >
                  <input
                    type="checkbox"
                    :value="s.id"
                    v-model="selectedSizes"
                    :disabled="isSizeFull(s.id)"
                    class="h-4 w-4 rounded border-slate-300 text-[#8b3a4a] accent-[#8b3a4a]"
                  />
                  <span>{{ s.tenKichThuoc }}</span>
                  <span
                    v-if="isSizeFull(s.id)"
                    class="rounded-full bg-slate-100 px-1.5 py-0.5 text-[9px] text-slate-400"
                    >Đã đủ</span
                  >
                </label>
              </div>
            </section>
          </div>
        </div>

        <!-- Summary -->
        <div
          class="flex shrink-0 flex-wrap items-center justify-between gap-3 border-b border-[#e8e1d8] bg-white px-5 py-3 sm:px-7"
        >
          <div class="flex flex-wrap items-center gap-2 text-xs font-semibold text-slate-600">
            <span class="rounded-full bg-[#f3e7e8] px-3 py-1.5 text-[#8b3a4a]"
              >{{ previewVariants.length }} biến thể</span
            >
            <span class="rounded-full bg-emerald-50 px-3 py-1.5 text-emerald-700"
              >{{ previewVariants.filter((v) => !v.existed).length }} mới</span
            >
            <span class="rounded-full bg-rose-50 px-3 py-1.5 text-rose-600"
              >{{ previewVariants.filter((v) => v.existed).length }} đã tồn tại</span
            >
          </div>
          <span class="text-[11px] text-slate-400">Nhập liệu trực tiếp · Enter để chuyển ô</span>
        </div>

        <!-- Management table -->
        <div class="min-h-0 flex-1 overflow-auto bg-[#f8f5f0] px-3 py-3 sm:px-5 lg:px-7">
          <div
            class="min-w-[980px] overflow-hidden rounded-2xl border border-[#e2d9ce] bg-white shadow-sm"
          >
            <div
              class="grid grid-cols-[2.2fr_1.2fr_1.2fr_1.2fr_110px] sticky top-0 z-10 border-b border-[#e8e1d8] bg-[#f7f3ed] px-4 py-3 text-[10px] font-black uppercase tracking-[0.12em] text-slate-500"
            >
              <div>Biến thể</div>
              <div>Ảnh</div>
              <div class="text-center">Giá nhập</div>
              <div class="text-center">Giá bán</div>
              <div class="text-center">Số lượng</div>
            </div>

            <div v-if="previewVariants.length === 0" class="px-6 py-16 text-center">
              <div
                class="mx-auto flex h-14 w-14 items-center justify-center rounded-2xl bg-[#f3e7e8] text-[#8b3a4a]"
              >
                <i class="fa-solid fa-table-list text-xl"></i>
              </div>
              <p class="mt-4 text-sm font-bold text-slate-700">Chưa có biến thể để nhập</p>
              <p class="mt-1 text-xs text-slate-400">Chọn màu sắc và kích thước ở phía trên.</p>
            </div>

            <div
              v-for="(v, index) in previewVariants"
              :key="v.key"
              class="grid grid-cols-[2.2fr_1.2fr_1.2fr_1.2fr_110px] items-center gap-0 border-b border-[#eee8df] px-4 py-3 transition last:border-b-0 hover:bg-[#fffaf5]"
              :class="v.existed ? 'bg-rose-50/50' : ''"
            >
              <div class="flex min-w-0 items-center gap-3 pr-4">
                <div
                  class="flex h-10 w-10 shrink-0 items-center justify-center rounded-xl bg-[#f3e7e8] font-black text-[#8b3a4a]"
                >
                  {{ v.colorName?.charAt(0) }}
                </div>
                <div class="min-w-0">
                  <p class="truncate text-sm font-bold text-slate-800">
                    {{ v.colorName }} · {{ v.sizeName }}
                  </p>
                  <p
                    class="mt-1 text-[10px] font-semibold"
                    :class="v.existed ? 'text-rose-500' : 'text-emerald-600'"
                  >
                    {{
                      v.existed ? 'Đã tồn tại · không tạo lại' : `Dòng ${index + 1} · sẵn sàng nhập`
                    }}
                  </p>
                </div>
              </div>

              <div class="flex items-center gap-2 pr-3">
                <div class="flex min-w-0 gap-1">
                  <img
                    v-for="(img, i) in v.images?.slice(0, 3)"
                    :key="i"
                    :src="img"
                    class="h-9 w-9 rounded-lg border border-[#e8e1d8] object-cover"
                    alt=""
                  />
                </div>
                <label
                  class="inline-flex shrink-0 cursor-pointer items-center gap-1 rounded-lg border border-[#e5ddd3] bg-white px-2 py-2 text-[10px] font-bold text-[#8b3a4a] transition hover:border-[#8b3a4a] hover:bg-[#f3e7e8]"
                >
                  <i class="fa-solid fa-image"></i>
                  <span>Ảnh</span>
                  <input
                    type="file"
                    multiple
                    hidden
                    @change="(e) => handleVariantImageUpload(e, v)"
                  />
                </label>
              </div>

              <div class="px-2">
                <input
                  v-model.number="v.giaNhap"
                  :disabled="v.existed"
                  data-bulk-field
                  :data-bulk-index="index"
                  data-bulk-column="giaNhap"
                  @keydown.enter.prevent="focusNextBulkField($event)"
                  type="number"
                  min="0"
                  step="1000"
                  inputmode="numeric"
                  placeholder="0"
                  class="w-full rounded-xl border border-[#ded5ca] bg-white px-3 py-2.5 text-center text-sm font-semibold text-slate-800 outline-none transition focus:border-[#8b3a4a] focus:ring-4 focus:ring-[#8b3a4a]/10 disabled:cursor-not-allowed disabled:bg-slate-100 disabled:text-slate-400"
                />
              </div>

              <div class="px-2">
                <input
                  v-model.number="v.giaBan"
                  :disabled="v.existed"
                  data-bulk-field
                  :data-bulk-index="index"
                  data-bulk-column="giaBan"
                  @keydown.enter.prevent="focusNextBulkField($event)"
                  type="number"
                  min="0"
                  step="1000"
                  inputmode="numeric"
                  placeholder="0"
                  class="w-full rounded-xl border border-[#ded5ca] bg-white px-3 py-2.5 text-center text-sm font-semibold text-slate-800 outline-none transition focus:border-[#8b3a4a] focus:ring-4 focus:ring-[#8b3a4a]/10 disabled:cursor-not-allowed disabled:bg-slate-100 disabled:text-slate-400"
                />
              </div>

              <div class="px-2">
                <input
                  v-model.number="v.soLuongTon"
                  :disabled="v.existed"
                  data-bulk-field
                  :data-bulk-index="index"
                  data-bulk-column="soLuongTon"
                  @keydown.enter.prevent="focusNextBulkField($event)"
                  type="number"
                  min="0"
                  step="1"
                  inputmode="numeric"
                  placeholder="0"
                  class="w-full rounded-xl border border-[#ded5ca] bg-white px-3 py-2.5 text-center text-sm font-semibold text-slate-800 outline-none transition focus:border-[#8b3a4a] focus:ring-4 focus:ring-[#8b3a4a]/10 disabled:cursor-not-allowed disabled:bg-slate-100 disabled:text-slate-400"
                />
              </div>
            </div>
          </div>
        </div>

        <!-- Footer -->
        <div
          class="flex shrink-0 flex-col-reverse gap-3 border-t border-[#e8e1d8] bg-white px-5 py-4 sm:flex-row sm:items-center sm:justify-between sm:px-7"
        >
          <p class="text-[11px] text-slate-400">
            Mẹo: nhập Giá nhập → Enter → Giá bán → Enter → Số lượng → Enter.
          </p>
          <div class="flex gap-3">
            <button
              type="button"
              @click="resetBulkForm"
              class="rounded-xl border border-[#e0d7cc] bg-white px-5 py-3 text-xs font-black uppercase tracking-wide text-slate-600 transition hover:bg-slate-50"
            >
              Hủy
            </button>
            <button
              type="button"
              @click="submitBulkSPCT"
              class="rounded-xl bg-[#8b3a4a] px-6 py-3 text-xs font-black uppercase tracking-wide text-white shadow-lg shadow-[#8b3a4a]/15 transition hover:-translate-y-0.5 hover:bg-[#702d3a] disabled:cursor-not-allowed disabled:opacity-50"
            >
              Tạo {{ previewVariants.filter((v) => !v.existed).length }} biến thể
            </button>
          </div>
        </div>
      </div>
    </div>
    <div>
      <transition name="fade"
        ><div
          v-if="confirmModal.show"
          class="fixed inset-0 z-[999] flex items-center justify-center bg-black/50 backdrop-blur-sm p-4"
        >
          <div
            class="w-full max-w-sm bg-white rounded-3xl shadow-2xl overflow-hidden p-8 text-center"
          >
            <div
              class="w-16 h-16 bg-rose-50 text-rose-500 rounded-full flex items-center justify-center mx-auto mb-4 text-2xl"
            >
              🗑️
            </div>
            <h3 class="text-lg font-bold text-slate-900">Xác nhận xóa</h3>
            <p class="mt-2 text-sm text-slate-500">
              Hành động này không thể hoàn tác. Bạn có chắc chắn muốn xóa?
            </p>
            <div class="flex justify-center gap-3 mt-8">
              <button
                @click="cancelDelete"
                class="px-6 py-3 rounded-xl border font-bold text-xs uppercase"
              >
                Hủy</button
              ><button
                @click="confirmDelete"
                class="px-6 py-3 rounded-xl bg-rose-600 text-white font-bold text-xs uppercase"
              >
                Xóa vĩnh viễn
              </button>
            </div>
          </div>
        </div></transition
      >
    </div>
    <div
      v-if="isImageManagerOpen"
      class="fixed inset-0 z-[1000] flex items-center justify-center p-4"
    >
      <div
        class="absolute inset-0 bg-black/40 backdrop-blur-sm"
        @click="isImageManagerOpen = false"
      ></div>
      <div
        class="bg-white w-full max-w-3xl rounded-3xl shadow-2xl p-8 relative z-10 animate-fade-in"
      >
        <div class="flex justify-between items-center mb-6">
          <h2 class="text-lg font-bold">Quản lý ảnh: {{ selectedSPCT?.maSanPhamChiTiet }}</h2>
          <button @click="isImageManagerOpen = false" class="text-slate-400 hover:text-black">
            ✕
          </button>
        </div>

        <div class="grid grid-cols-3 gap-4 mb-6">
          <!-- Vòng lặp danh sách ảnh -->
          <div
            v-for="img in currentGallery"
            :key="img.id"
            class="relative group aspect-square overflow-hidden rounded-2xl bg-slate-100 transition-all duration-300 shadow-2xs hover:shadow-md border border-slate-200/80"
          >
            <!-- Ảnh -->
            <img
              :src="baseUrl + img.url"
              class="w-full h-full object-cover transition-transform duration-500 group-hover:scale-105"
            />

            <!-- Gradient phủ nhẹ ở cạnh trên & dưới để làm nổi bật các nút -->
            <div
              class="absolute inset-0 bg-gradient-to-t from-black/50 via-transparent to-black/30 pointer-events-none"
            ></div>

            <!-- 1. Badge Ảnh chính (Góc trên bên trái) -->
            <div
              v-if="img.laAnhChinh"
              class="absolute top-2.5 left-2.5 bg-indigo-600 text-white text-[10px] font-bold px-2.5 py-1 rounded-full shadow-md flex items-center gap-1 z-10"
            >
              ⭐ Ảnh chính
            </div>

            <!-- 2. Nút Xóa ảnh (Góc trên bên phải - Chỉ hiện khi hover để tránh bấm nhầm) -->
            <button
              @click="deleteImageAPI(img.id)"
              class="absolute top-2 right-2 w-7 h-7 rounded-full bg-rose-500 hover:bg-rose-600 text-white flex items-center justify-center shadow-lg transition z-[999]"
            >
              <svg class="w-3.5 h-3.5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path
                  stroke-linecap="round"
                  stroke-linejoin="round"
                  stroke-width="2.5"
                  d="M6 18L18 6M6 6l12 12"
                />
              </svg>
            </button>

            <!-- 3. Nút Đặt làm ảnh chính (Luôn hiển thị ở đáy ảnh, nổi bật dễ thấy) -->
            <button
              v-if="!img.laAnhChinh"
              @click="changeMainImage(img)"
              class="absolute bottom-2.5 left-2.5 right-2.5 bg-white/90 hover:bg-indigo-600 text-slate-800 hover:text-white text-[11px] font-bold rounded-xl py-2 shadow-md backdrop-blur-md transition-all transform active:scale-95 z-10 border border-slate-200/50"
            >
              👑 Đặt làm ảnh chính
            </button>
          </div>

          <!-- Ô Upload ảnh mới -->
          <label
            class="aspect-square border-2 border-dashed border-slate-300 hover:border-indigo-500 bg-slate-50/50 hover:bg-indigo-50/30 rounded-2xl flex flex-col items-center justify-center cursor-pointer transition-all group shadow-2xs"
          >
            <div
              class="w-10 h-10 rounded-full bg-slate-200/70 group-hover:bg-indigo-100 group-hover:text-indigo-600 text-slate-500 flex items-center justify-center transition-all mb-1.5"
            >
              <span class="text-xl font-light">+</span>
            </div>
            <span
              class="text-[11px] font-semibold text-slate-500 group-hover:text-indigo-600 transition-colors"
              >Thêm ảnh</span
            >
            <input type="file" multiple class="hidden" @change="handleUpload" />
          </label>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed, watch, nextTick } from 'vue'
import { useRouter } from 'vue-router'
import Swal from 'sweetalert2'
import stompClient from '@/socket'
import '@/socket'
import { getAllSanpham } from '@/service/SanphamService'
import { getAllDanhMuc } from '@/service/DanhMucService'
import { getAllThuongHieu } from '@/service/ThuongHieuService'
import { getAllChatLieu } from '@/service/ChatLieuService'
import { updateSanPhamChiTiet } from '@/service/SanPhamChiTiet'
import { useToast } from 'vue-toastification'
import { getAllMauSac } from '@/service/MauSacService'
import { getAllKichThuoc } from '@/service/KichThuocService'
import { uploadImages, deleteImage, getImagesBySPCT, setMainImage } from '@/service/HinhAnhSerivce'
const toast = useToast()
const baseUrl = 'http://localhost:8080'
import {
  getSanPhamChiTietByProductId,
  createSanPhamChiTiet,
  deleteSanPhamChiTiet,
  getThongKeSPCT,
} from '@/service/SanPhamChiTiet'

// ========================
// PRODUCT DATA
// ========================
const router = useRouter()
const products = ref([])
const danhMucs = ref([])
const thuongHieus = ref([])
const chatLieus = ref([])
const mauSacs = ref([])
const kichThuocs = ref([])

const tongSanPham = ref(0)
const tongSPCT = ref(0)
const tongSPCTDangKD = ref(0)
const tenSanPhamInput = ref(null)
const tongSanPhamDangKD = ref(0)
// ========================
// DRAWER STATE (SPCT)
// ========================
const isSPCTModalOpen = ref(false)
const selectedProduct = ref(null)
const expandedProductId = ref(null)

const isImageManagerOpen = ref(false)
const currentGallery = ref([]) // Lưu danh sách ảnh {id, url}
const selectedSPCT = ref(null)

const spctList = ref([])
const loadingSpct = ref(false)

const isBulkOpen = ref(false)

const selectedColors = ref([])
const selectedSizes = ref([])

// Phân trang
// --- Bổ sung các biến phân trang ---
const currentPage = ref(1)
const pageSize = ref(10)

// --- Computed cắt danh sách hiển thị theo trang ---
const paginatedProducts = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value
  const end = start + pageSize.value
  return filteredProducts.value.slice(start, end)
})

// --- Computed tính tổng số trang dựa trên kết quả đã lọc ---
const totalPages = computed(() => {
  return Math.ceil(filteredProducts.value.length / pageSize.value) || 1
})

// --- Hàm chuyển trang ---
const changePage = (page) => {
  if (page >= 1 && page <= totalPages.value) {
    currentPage.value = page
  }
}
// ========================
// FORM SPCT (ONLY ONE SOURCE OF TRUTH)
// ========================
const formData = ref({
  idSanPham: null,
  idMauSac: null,
  idKichThuoc: null,
  giaNhap: null,
  giaBan: null,
  soLuongTon: 0,
  soLuongTamGiu: 0,
  soLuongKhaDung: 0,
  trangThai: true,
})
const cancelEditSPCT = () => {
  isEditSPCT.value = false
  editingSPCTId.value = null

  selectedFiles.value = []
  previewImages.value = []

  Object.assign(formData.value, {
    idSanPham: selectedProduct.value?.id,
    idMauSac: null,
    idKichThuoc: null,
    giaNhap: 0,
    giaBan: 0,
    soLuongTon: 0,
    soLuongTamGiu: 0,
    soLuongKhaDung: 0,
    trangThai: true,
  })
}

const selectedFiles = ref([])
const previewImages = ref([])
const isEditSPCT = ref(false)
const editingSPCTId = ref(null)

// ========================
// LOAD PRODUCT LIST
// ========================
const loadData = async () => {
  try {
    const [productList, danhMuc, thuongHieu, chatLieu, mauSac, kichThuoc] = await Promise.all([
      getAllSanpham(),
      getAllDanhMuc(),
      getAllThuongHieu(),
      getAllChatLieu(),
      getAllMauSac(),
      getAllKichThuoc(),
    ])

    productList.sort((a, b) => b.id - a.id)

    products.value = await Promise.all(
      productList.map(async (sp) => {
        const thongKe = await getThongKeSPCT(sp.id)

        return {
          ...sp,
          thongKe,
        }
      }),
    )

    danhMucs.value = danhMuc
    thuongHieus.value = thuongHieu
    chatLieus.value = chatLieu
    mauSacs.value = mauSac
    kichThuocs.value = kichThuoc
  } catch (err) {
    console.error(err)
  }

  tongSanPham.value = products.value.length

  tongSanPhamDangKD.value = products.value.filter((p) => p.trangThai === true).length

  await calcThongKeSPCT()
}

onMounted(() => {
  loadData()

  if (stompClient.connected) {
    subscribe()
  } else {
    stompClient.onConnect = () => {
      subscribe()
    }
  }
})

function subscribe() {
  stompClient.subscribe('/topic/products', (msg) => {
    console.log('NHẬN SOCKET:', msg.body)

    loadData()
  })
}

// ========================
// OPEN DRAWER + LOAD SPCT
// ========================
const toggleSPCT = async (product) => {
  try {
    if (expandedProductId.value === product.id) {
      expandedProductId.value = null
      return
    }

    expandedProductId.value = product.id
    selectedProduct.value = product

    formData.value.idSanPham = product.id

    loadingSpct.value = true

    const data = await getSanPhamChiTietByProductId(product.id)

    spctList.value = Array.isArray(data) ? data : []
  } catch (err) {
    console.error(err)
    spctList.value = []
    toast.error('Không tải được danh sách SPCT')
  } finally {
    loadingSpct.value = false
  }
}

const openAddSPCTModal = (product) => {
  cancelEditSPCT()

  selectedProduct.value = product
  formData.value.idSanPham = product.id

  isSPCTModalOpen.value = true
}

const loadSPCT = async (id) => {
  loadingSpct.value = true
  try {
    const data = await getSanPhamChiTietByProductId(id)
    spctList.value = Array.isArray(data) ? data : []
  } catch (e) {
    console.error(e)
    spctList.value = []
  } finally {
    loadingSpct.value = false
  }
}

const previewVariants = ref([])

const isColorFull = (colorId) => {
  return kichThuocs.value.every((size) =>
    spctList.value.some(
      (spct) =>
        Number(spct.idMauSac) === Number(colorId) && Number(spct.idKichThuoc) === Number(size.id),
    ),
  )
}

const isSizeFull = (sizeId) => {
  return mauSacs.value.every((color) =>
    spctList.value.some(
      (spct) =>
        Number(spct.idMauSac) === Number(color.id) && Number(spct.idKichThuoc) === Number(sizeId),
    ),
  )
}

const buildPreviewVariants = () => {
  const result = []

  selectedColors.value.forEach((c) => {
    const color = mauSacs.value.find((m) => m.id === c)

    selectedSizes.value.forEach((s) => {
      const size = kichThuocs.value.find((k) => k.id === s)

      const old = previewVariants.value.find((i) => i.colorId === c && i.sizeId === s)

      const existed = spctList.value.some(
        (item) => Number(item.idMauSac) === Number(c) && Number(item.idKichThuoc) === Number(s),
      )

      result.push({
        colorId: c,
        sizeId: s,
        colorName: color?.tenMauSac,
        sizeName: size?.tenKichThuoc,
        key: `${c}-${s}`,

        giaNhap: old?.giaNhap ?? 0,
        giaBan: old?.giaBan ?? 0,
        soLuongTon: old?.soLuongTon ?? 0,

        existed,

        images: old?.images ?? [],

        files: old?.files ?? [],
      })
    })
  })

  previewVariants.value = result
}
const validateBulkVariants = () => {
  if (!selectedColors.value.length || !selectedSizes.value.length) {
    toast.error('Vui lòng chọn màu sắc và kích thước')
    return false
  }

  for (const v of previewVariants.value.filter((i) => !i.existed)) {
    if (v.giaNhap === null || v.giaNhap === '' || isNaN(Number(v.giaNhap))) {
      toast.error(`Vui lòng nhập giá nhập cho ${v.colorName} - ${v.sizeName}`)
      return false
    }

    if (v.giaBan === null || v.giaBan === '' || isNaN(Number(v.giaBan))) {
      toast.error(`Vui lòng nhập giá bán cho ${v.colorName} - ${v.sizeName}`)
      return false
    }

    if (v.soLuongTon === null || v.soLuongTon === '' || isNaN(Number(v.soLuongTon))) {
      toast.error(`Vui lòng nhập số lượng cho ${v.colorName} - ${v.sizeName}`)
      return false
    }

    if (Number(v.giaNhap) < 0) {
      toast.error(`Giá nhập của ${v.colorName} - ${v.sizeName} không được âm`)
      return false
    }

    if (Number(v.giaBan) < 0) {
      toast.error(`Giá bán của ${v.colorName} - ${v.sizeName} không được âm`)
      return false
    }

    if (Number(v.soLuongTon) < 0) {
      toast.error(`Số lượng của ${v.colorName} - ${v.sizeName} không được âm`)
      return false
    }

    if (Number(v.giaNhap) > Number(v.giaBan)) {
      toast.error(`Giá nhập của ${v.colorName} - ${v.sizeName} không được lớn hơn giá bán`)
      return false
    }
  }

  return true
}

const submitBulkSPCT = async () => {
  if (!validateBulkVariants()) return

  const payload = previewVariants.value
    .filter((v) => !v.existed)
    .map((v) => ({
      idSanPham: selectedProduct.value.id,
      idMauSac: v.colorId,
      idKichThuoc: v.sizeId,
      giaNhap: v.giaNhap,
      giaBan: v.giaBan,
      soLuongTon: v.soLuongTon,
    }))
  if (payload.length === 0) {
    toast.warning('Không có biến thể mới để tạo')
    return
  }
  try {
    const response = await fetch('http://localhost:8080/spct/bulk', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(payload),
    })

    if (!response.ok) {
      throw new Error('Tạo biến thể thất bại')
    }

    const createdVariants = await response.json()

    for (const created of createdVariants) {
      const variant = previewVariants.value.find(
        (v) => !v.existed && v.colorId === created.colorId && v.sizeId === created.sizeId,
      )

      if (!variant) continue

      if (variant.files?.length) {
        await uploadImages(created.id, variant.files)
      }
    }

    toast.success('Tạo biến thể thành công')

    resetBulkForm()

    await loadSPCT(selectedProduct.value.id)
    await loadData()
  } catch (e) {
    toast.error('Lỗi tạo biến thể')
  }
}

const focusNextBulkField = (event) => {
  const fields = Array.from(
    event.currentTarget
      .closest('.min-w-[980px]')
      ?.querySelectorAll('[data-bulk-field]:not(:disabled)') || [],
  )

  const currentIndex = fields.indexOf(event.currentTarget)
  const nextField = fields[currentIndex + 1]

  if (nextField) {
    nextField.focus()
    nextField.select?.()
    return
  }

  toast.success('Đã điền hết các ô nhập liệu. Bạn có thể kiểm tra và tạo biến thể.')
}

const resetBulkForm = () => {
  selectedColors.value = []
  selectedSizes.value = []

  previewVariants.value = []

  formData.value.giaNhap = null
  formData.value.giaBan = null
  formData.value.soLuongTon = 0

  isBulkOpen.value = false
}

// ========================
// FILE UPLOAD PREVIEW
// ========================
const handleFileUpload = (event) => {
  const files = event.target.files
  if (files.length > 5) {
    toast.error('Chỉ được chọn tối đa 5 ảnh')
    return
  }
  if (!files) return

  selectedFiles.value = Array.from(files)

  previewImages.value = []
  Array.from(files).forEach((file) => {
    if (file.size > 5 * 1024 * 1024) {
      toast.error(`${file.name} vượt quá 5MB`)
      return
    }
    const reader = new FileReader()
    reader.onload = (e) => {
      previewImages.value.push(e.target.result)
    }
    reader.readAsDataURL(file)
  })
}

// ========================
// CREATE SPCT (FORMDATA BACKEND)
// ========================
const validateSPCT = () => {
  if (!formData.value.idMauSac) {
    toast.error('Vui lòng chọn màu sắc')
    return false
  }

  if (!formData.value.idKichThuoc) {
    toast.error('Vui lòng chọn kích thước')
    return false
  }

  const giaNhap = Number(formData.value.giaNhap)
  const giaBan = Number(formData.value.giaBan)

  if (isNaN(giaNhap) || giaNhap < 0) {
    toast.error('Giá nhập không được âm')
    return false
  }

  if (isNaN(giaBan) || giaBan < 0) {
    toast.error('Giá bán không được âm')
    return false
  }

  if (giaNhap > giaBan) {
    toast.error('Giá nhập không được lớn hơn giá bán')
    return false
  }

  return true
}
const isDuplicateVariant = () => {
  return spctList.value.some(
    (item) =>
      Number(item.idMauSac) === Number(formData.value.idMauSac) &&
      Number(item.idKichThuoc) === Number(formData.value.idKichThuoc) &&
      item.id !== editingSPCTId.value,
  )
}
const submitSPCT = async () => {
  if (!validateSPCT()) return

  if (isDuplicateVariant()) {
    toast.error('Màu sắc và kích thước đã tồn tại')
    return
  }

  try {
    const payload = new FormData()

    Object.entries({
      idSanPham: formData.value.idSanPham,
      idMauSac: formData.value.idMauSac,
      idKichThuoc: formData.value.idKichThuoc,
      giaNhap: formData.value.giaNhap,
      giaBan: formData.value.giaBan,
      soLuongTon: formData.value.soLuongTon,
      trangThai: formData.value.trangThai,
    }).forEach(([k, v]) => payload.append(k, v ?? ''))

    selectedFiles.value.forEach((f) => payload.append('files', f))

    let responseData

    if (isEditSPCT.value) {
      responseData = await updateSanPhamChiTiet(editingSPCTId.value, payload)
    } else {
      responseData = await createSanPhamChiTiet(payload)
    }

    const successMessage = responseData?.message || 'Cập nhật SPCT thành công!'
    toast.success(successMessage)

    await loadSPCT(formData.value.idSanPham)
    await loadData()

    selectedFiles.value = []
    previewImages.value = []
    isEditSPCT.value = false
    editingSPCTId.value = null
    isSPCTModalOpen.value = false

    Object.assign(formData.value, {
      giaNhap: 0,
      giaBan: 0,
      soLuongTon: 0,
      soLuongTamGiu: 0,
      soLuongKhaDung: 0,
      trangThai: true,
    })
  } catch (e) {
    console.error('Lỗi khi lưu SPCT:', e)
    toast.error(e.message || 'Có lỗi xảy ra khi cập nhật!')
  }
}
const editSPCT = (spct) => {
  isEditSPCT.value = true
  editingSPCTId.value = spct.id

  formData.value = {
    ...spct,
  }

  previewImages.value = []

  if (spct.dangGiamGia) {
    toast.warning('Sản phẩm đang nằm trong đợt giảm giá! Giá bán sẽ bị khóa không thể sửa.')
  }

  isSPCTModalOpen.value = true
}

const removeSPCT = async (id) => {
  const result = await Swal.fire({
    title: 'Xóa sản phẩm chi tiết?',
    text: 'Thao tác này không thể hoàn tác!',
    icon: 'warning',
    showCancelButton: true,
    confirmButtonText: 'Xóa',
    cancelButtonText: 'Hủy',
    reverseButtons: true,
  })

  if (!result.isConfirmed) return

  try {
    await deleteSanPhamChiTiet(id)
    await loadSPCT(selectedProduct.value.id)

    await loadData()
    toast.success('Ngừng kinh doanh biến thể thành công!')
  } catch (e) {
    toast.error(e.response?.data || e.message || 'Có lỗi xảy ra')
  }
}

const filters = reactive({
  search: '',
  idDanhMuc: null,
  idThuongHieu: null,
  idChatLieu: null,
  trangThai: null,
})

const filteredProducts = computed(() => {
  return products.value.filter((p) => {
    const q = filters.search.toLowerCase()

    const matchSearch =
      !q || p.tenSanPham?.toLowerCase().includes(q) || p.maSanPham?.toLowerCase().includes(q)

    const matchDM = !filters.idDanhMuc || p.idDanhMuc?.id === filters.idDanhMuc
    const matchTH = !filters.idThuongHieu || p.idThuongHieu?.id === filters.idThuongHieu
    const matchCL = !filters.idChatLieu || p.idChatLieu?.id === filters.idChatLieu
    const matchTT = filters.trangThai === null || p.trangThai === filters.trangThai

    return matchSearch && matchDM && matchTH && matchCL && matchTT
  })
})

const clearFilters = () => {
  filters.search = ''
  filters.idDanhMuc = null
  filters.idThuongHieu = null
  filters.idChatLieu = null
  filters.trangThai = null
}

const isModalOpen = ref(false)
const isEditMode = ref(false)

const form = ref({
  tenSanPham: '',
  idDanhMuc: null,
  idThuongHieu: null,
  idChatLieu: null,
  moTa: '',
  trangThai: true,
})

const openAddModal = () => {
  form.value = {
    tenSanPham: '',
    idDanhMuc: null,
    idThuongHieu: null,
    idChatLieu: null,
    moTa: '',
    trangThai: true,
  }

  isEditMode.value = false
  isModalOpen.value = true
}

const validateSP = () => {
  if (!form.value.tenSanPham?.trim()) {
    toast.error('Tên sản phẩm không được để trống')
    return false
  }

  if (!form.value.idDanhMuc) {
    toast.error('Vui lòng chọn danh mục')
    return false
  }

  if (!form.value.idThuongHieu) {
    toast.error('Vui lòng chọn thương hiệu')
    return false
  }

  if (!form.value.idChatLieu) {
    toast.error('Vui lòng chọn chất liệu')
    return false
  }

  return true
}

const isDuplicateMaSP = () => {
  return products.value.some(
    (item) =>
      item.maSanPham?.trim().toLowerCase() === form.value.maSanPham?.trim().toLowerCase() &&
      item.id !== form.value.id,
  )
}

const isDuplicateProduct = () => {
  return products.value.some(
    (item) =>
      item.tenSanPham?.trim().toLowerCase() === form.value.tenSanPham?.trim().toLowerCase() &&
      item.idThuongHieu?.id === Number(form.value.idThuongHieu) &&
      item.idChatLieu?.id === Number(form.value.idChatLieu) &&
      item.id !== form.value.id,
  )
}

const handleSubmit = async () => {
  if (!validateSP()) return

  if (isDuplicateMaSP()) {
    toast.error('Mã sản phẩm đã tồn tại')
    return
  }

  if (isDuplicateProduct()) {
    toast.error('Sản phẩm cùng tên, thương hiệu và chất liệu đã tồn tại')
    return
  }
  try {
    const payload = {
      idDanhMuc: form.value.idDanhMuc,
      idThuongHieu: form.value.idThuongHieu,
      idChatLieu: form.value.idChatLieu,
      tenSanPham: form.value.tenSanPham,
      trangThai: form.value.trangThai,
      moTa: form.value.moTa,
    }

    const url = isEditMode.value
      ? `http://localhost:8080/sanpham/update/${form.value.id}`
      : `http://localhost:8080/sanpham`

    const res = await fetch(url, {
      method: isEditMode.value ? 'PUT' : 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(payload),
    })

    if (!res.ok) throw new Error()

    await loadData()

    if (selectedProduct.value) {
      selectedProduct.value = products.value.find((p) => p.id === selectedProduct.value.id)
    }

    isModalOpen.value = false
    toast.success(isEditMode.value ? 'Cập nhật sản phẩm thành công' : 'Thêm sản phẩm thành công')
  } catch (err) {
    toast.error('Có lỗi xảy ra')
  }
}

const openEditModal = async (product) => {
  const res = await fetch(`http://localhost:8080/sanpham/detail/${product.id}`)
  const detail = await res.json()

  form.value = {
    ...detail,
    idDanhMuc: detail.idDanhMuc?.id,
    idThuongHieu: detail.idThuongHieu?.id,
    idChatLieu: detail.idChatLieu?.id,
  }

  isEditMode.value = true
  isModalOpen.value = true
}

const confirmModal = reactive({
  show: false,
  targetId: null,
})

const triggerDeleteConfirm = (id) => {
  confirmModal.targetId = id
  confirmModal.show = true
}

const confirmDelete = async () => {
  try {
    await fetch(`http://localhost:8080/sanpham/delete/${confirmModal.targetId}`, {
      method: 'DELETE',
    })
    await loadData()

    if (selectedProduct.value) {
      await loadSPCT(selectedProduct.value.id)
    }
    toast.success('🗑️ Xóa sản phẩm thành công')
  } catch (err) {
    console.error(err)
    toast.error('❌ Xóa sản phẩm thất bại')
  } finally {
    confirmModal.show = false
    confirmModal.targetId = null
  }
}

const cancelDelete = () => {
  confirmModal.show = false
  confirmModal.targetId = null
}

const formatCurrency = (val) =>
  new Intl.NumberFormat('vi-VN', {
    style: 'currency',
    currency: 'VND',
  }).format(val || 0)

const openImageManager = async (spct) => {
  selectedSPCT.value = spct

  try {
    await loadGallery()
    isImageManagerOpen.value = true
  } catch (e) {
    toast.error(e.message)
  }
}

const loadGallery = async () => {
  if (!selectedSPCT.value) return
  currentGallery.value = await getImagesBySPCT(selectedSPCT.value.id)
}

const handleUpload = async (event) => {
  try {
    const files = Array.from(event.target.files)
    await uploadImages(selectedSPCT.value.id, files)
    await reloadGallery()
    toast.success('Thêm ảnh thành công')
    event.target.value = ''
    await loadSPCT(selectedProduct.value.id)
  } catch (e) {
    toast.error(e.message || 'Upload lỗi')
  }
}

const deleteImageAPI = async (id) => {
  try {
    await deleteImage(id)
    currentGallery.value = currentGallery.value.filter((img) => img.id !== id)
    await reloadGallery()
    await loadSPCT(selectedProduct.value.id)
    toast.success('Xóa ảnh thành công')
  } catch (e) {
    toast.error(e.message)
  }
}

const reloadGallery = async () => {
  if (!selectedSPCT.value) return
  currentGallery.value = await getImagesBySPCT(selectedSPCT.value.id)
}

const calcThongKeSPCT = async () => {
  let allSPCT = []

  for (const sp of products.value) {
    const list = await getSanPhamChiTietByProductId(sp.id)
    allSPCT = allSPCT.concat(list)
  }

  tongSPCT.value = allSPCT.length
  tongSPCTDangKD.value = allSPCT.filter((i) => i.trangThai === true).length
}

watch(
  [selectedColors, selectedSizes],
  () => {
    buildPreviewVariants()
  },
  {
    deep: true,
  },
)

const handleVariantImageUpload = (event, variant) => {
  const files = Array.from(event.target.files)

  if (!variant.images) variant.images = []
  if (!variant.files) variant.files = []

  files.forEach((file) => {
    variant.files.push(file)
    const reader = new FileReader()

    reader.onload = (e) => {
      variant.images.push(e.target.result)
    }

    reader.readAsDataURL(file)
  })
}

watch(isModalOpen, async (newValue) => {
  if (newValue) {
    await nextTick()
    tenSanPhamInput.value?.focus()
  }
})

const changeMainImage = async (img) => {
  try {
    await setMainImage(img.id)
    await reloadGallery()
    toast.success('Đã đặt làm ảnh chính')
  } catch (e) {
    toast.error(e.message)
  }
}
</script>

<style scoped>
.animate-slide-in-right {
  animation: slideIn 0.4s cubic-bezier(0.16, 1, 0.3, 1);
}
@keyframes slideIn {
  from {
    transform: translateX(100%);
  }
  to {
    transform: translateX(0);
  }
}

.product-admin-page {
  --admin-wine: #8b3a4a;
  --admin-gold: #b28a4a;
  --admin-ink: #292522;
}

.product-admin-panel {
  transition:
    box-shadow 0.25s ease,
    transform 0.25s ease;
}

.product-admin-panel:hover {
  box-shadow: 0 18px 55px rgba(41, 37, 34, 0.08);
}

.admin-stat {
  min-height: 118px;
  padding: 18px;
  border: 1px solid #e8e1d8;
  border-radius: 20px;
  background: rgba(255, 255, 255, 0.86);
  box-shadow: 0 8px 25px rgba(41, 37, 34, 0.04);
}

.admin-stat span {
  display: block;
  font-size: 11px;
  font-weight: 800;
  letter-spacing: 0.08em;
  text-transform: uppercase;
  color: #8a8178;
}
.admin-stat strong {
  display: block;
  margin-top: 10px;
  font-size: 26px;
  line-height: 1;
  font-weight: 900;
  color: var(--admin-ink);
}
.admin-stat small {
  display: block;
  margin-top: 8px;
  color: #9b9289;
  font-size: 11px;
}

.product-admin-row td {
  vertical-align: middle;
}
.product-admin-row button {
  min-width: 36px;
  min-height: 36px;
}

.product-admin-page input:focus,
.product-admin-page select:focus,
.product-admin-page textarea:focus {
  outline: none;
}
</style>
