<template>
  <div
    class="min-h-screen bg-[#f8fafc] text-slate-800 font-sans antialiased selection:bg-indigo-600 selection:text-white p-4 sm:p-8 animate-fade-in"
  >
    <div class="max-w-[1500px] mx-auto space-y-8">
      <div class="grid grid-cols-1 xl:grid-cols-4 gap-6">
        <div
          class="relative bg-slate-900 rounded-3xl p-8 overflow-hidden shadow-[0_20px_50px_rgba(15,23,42,0.15)] text-white flex flex-col justify-between"
        >
          <div
            class="absolute -right-10 -top-10 w-40 h-40 bg-indigo-600/20 rounded-full blur-3xl"
          ></div>
          <div class="relative z-10 space-y-1">
            <span class="text-[10px] font-bold uppercase tracking-[0.2em] text-indigo-400"
              >Hệ thống quản trị</span
            >
            <h1 class="text-3xl font-black tracking-tight text-white">Bảng Điều Phối</h1>
            <p class="text-xs text-slate-400">Sắp xếp và tối ưu hóa hệ thống.</p>
          </div>
          <button
            @click="openAddModal"
            class="mt-8 relative z-10 flex items-center justify-center gap-2.5 w-full px-6 py-3.5 bg-indigo-600 hover:bg-indigo-500 text-white rounded-2xl text-xs font-bold uppercase tracking-wider shadow-lg transition-all hover:-translate-y-0.5"
          >
            <svg
              xmlns="http://www.w3.org/2000/svg"
              class="w-4 h-4"
              fill="none"
              viewBox="0 0 24 24"
              stroke="currentColor"
              stroke-width="3"
            >
              <path stroke-linecap="round" stroke-linejoin="round" d="M12 4.5v15m7.5-7.5h-15" />
            </svg>
            Khởi tạo mặt hàng
          </button>
        </div>

        <div class="xl:col-span-3 grid grid-cols-3 gap-4">
          <div
            class="bg-white p-6 rounded-2xl border border-slate-200/80 shadow-sm flex items-center justify-between"
          >
            <div>
              <div class="text-[11px] font-bold text-slate-400 uppercase tracking-widest">
                Danh mục
              </div>
              <div class="text-2xl font-black text-slate-800">{{ danhMucs.length }}</div>
            </div>
            <span
              class="text-xl bg-slate-50 w-10 h-10 rounded-xl flex items-center justify-center border"
              >📁</span
            >
          </div>
          <div
            class="bg-white p-6 rounded-2xl border border-slate-200/80 shadow-sm flex items-center justify-between"
          >
            <div>
              <div class="text-[11px] font-bold text-slate-400 uppercase tracking-widest">
                Thương hiệu
              </div>
              <div class="text-2xl font-black text-slate-800">{{ thuongHieus.length }}</div>
            </div>
            <span
              class="text-xl bg-slate-50 w-10 h-10 rounded-xl flex items-center justify-center border"
              >🏷️</span
            >
          </div>
          <div class="bg-white p-6 rounded-2xl border shadow-sm flex items-center justify-between">
            <div>
              <div class="text-[11px] font-bold text-slate-400 uppercase tracking-widest">
                Sản phẩm
              </div>
              <div class="text-2xl font-black text-slate-800">
                {{ tongSanPham }}
              </div>
            </div>
            <span
              class="text-xl bg-slate-50 w-10 h-10 rounded-xl flex items-center justify-center border"
            >
              📦
            </span>
          </div>
          <div class="bg-white p-6 rounded-2xl border shadow-sm flex items-center justify-between">
            <div>
              <div class="text-[11px] font-bold text-slate-400 uppercase tracking-widest">
                SPCT đang kinh doanh
              </div>
              <div class="text-2xl font-black text-green-600">
                {{ tongSPCTDangKD }}
              </div>
            </div>
            <span
              class="text-xl bg-slate-50 w-10 h-10 rounded-xl flex items-center justify-center border"
            >
              ✅
            </span>
          </div>
          <div class="bg-white p-6 rounded-2xl border shadow-sm flex items-center justify-between">
            <div>
              <div class="text-[11px] font-bold text-slate-400 uppercase tracking-widest">
                Tổng SPCT
              </div>
              <div class="text-2xl font-black text-slate-800">
                {{ tongSPCT }}
              </div>
            </div>
            <span
              class="text-xl bg-slate-50 w-10 h-10 rounded-xl flex items-center justify-center border"
            >
              📦
            </span>
          </div>
          <div
            class="bg-white p-6 rounded-2xl border border-slate-200/80 shadow-sm flex items-center justify-between"
          >
            <div>
              <div class="text-[11px] font-bold text-slate-400 uppercase tracking-widest">
                Chất liệu
              </div>
              <div class="text-2xl font-black text-slate-800">{{ chatLieus.length }}</div>
            </div>
            <span
              class="text-xl bg-slate-50 w-10 h-10 rounded-xl flex items-center justify-center border"
              >🧵</span
            >
          </div>
        </div>
      </div>

      <div
        class="bg-white border border-slate-200/80 rounded-2xl shadow-[0_8px_30px_rgb(0,0,0,0.02)] overflow-hidden"
      >
        <div class="p-5 border-b border-slate-100 flex flex-col lg:flex-row items-center gap-4">
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
              class="w-full pl-10 pr-4 py-2.5 bg-slate-50 border rounded-xl text-xs outline-none focus:ring-2 ring-indigo-500/20"
            />
          </div>
          <div class="grid grid-cols-2 sm:grid-cols-4 lg:grid-cols-5 gap-3 w-full flex-1">
            <select
              v-model="filters.idDanhMuc"
              class="border px-3 py-2.5 rounded-xl text-xs bg-slate-50"
            >
              <option :value="null">Danh mục</option>
              <option v-for="i in danhMucs" :key="i.id" :value="i.id">{{ i.tenDanhMuc }}</option>
            </select>
            <select
              v-model="filters.idThuongHieu"
              class="border px-3 py-2.5 rounded-xl text-xs bg-slate-50"
            >
              <option :value="null">Thương hiệu</option>
              <option v-for="i in thuongHieus" :key="i.id" :value="i.id">
                {{ i.tenThuongHieu }}
              </option>
            </select>
            <select
              v-model="filters.idChatLieu"
              class="border px-3 py-2.5 rounded-xl text-xs bg-slate-50"
            >
              <option :value="null">Chất liệu</option>
              <option v-for="i in chatLieus" :key="i.id" :value="i.id">{{ i.tenChatLieu }}</option>
            </select>
            <select
              v-model="filters.trangThai"
              class="border px-3 py-2.5 rounded-xl text-xs bg-slate-50"
            >
              <option :value="null">Trạng thái</option>
              <option :value="true">Kinh doanh</option>
              <option :value="false">Lưu kho</option>
            </select>
            <button
              @click="clearFilters"
              class="border px-3 py-2.5 rounded-xl text-xs bg-rose-50 text-rose-600 hover:bg-rose-100 transition-colors"
            >
              Xóa bộ lọc
            </button>
          </div>
        </div>

        <table class="w-full text-left">
          <thead class="text-[10px] font-bold text-slate-400 uppercase border-b bg-slate-50/50">
            <tr>
              <th class="px-6 py-4">Mã SP</th>
              <th class="px-6 py-4">Sản phẩm</th>
              <th class="px-6 py-4">Danh mục</th>
              <th class="px-6 py-4">Thương hiệu</th>
              <th class="px-6 py-4">Chất liệu</th>
              <th class="px-6 py-4 text-center">Số lượng</th>
              <th class="px-6 py-4">Trạng thái</th>
              <th class="px-6 py-4 text-right">Thao tác</th>
            </tr>
          </thead>
          <tbody class="divide-y divide-slate-100">
            <template v-for="product in filteredProducts" :key="product.id">
              <tr class="hover:bg-slate-50/80 transition-colors">
                <td class="px-6 py-4">
                  <span
                    class="font-mono text-xs bg-indigo-50 text-indigo-600 px-2 py-1 rounded-lg"
                    >{{ product.maSanPham }}</span
                  >
                </td>
                <td class="px-6 py-4 flex items-center gap-3">
                  <div class="w-8 h-8 bg-slate-100 rounded-lg flex items-center justify-center">
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
                  {{ product.trangThai ? 'Kinh doanh' : 'Lưu kho' }}
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
              <tr v-if="expandedProductId === product.id" class="bg-slate-100">
                <td colspan="8" class="px-6 py-4">
                  <div class="flex justify-between items-center mb-4">
                    <div>
                      <h3 class="font-bold text-sm">{{ product.tenSanPham }}</h3>
                      <p class="text-xs text-slate-400">Danh sách biến thể</p>
                    </div>
                    <span class="text-xs text-slate-500 ml-2">
                      ({{ product.thongKe }} đang kinh doanh)
                    </span>
                    <div class="flex gap-2">
                      <button
                        @click="openAddSPCTModal(product)"
                        class="px-4 py-2 bg-indigo-600 hover:bg-indigo-700 text-white rounded-xl text-xs font-bold transition-all"
                      >
                        + Thêm biến thể
                      </button>
                      <button
                        @click="isBulkOpen = true"
                        class="px-4 py-2 bg-slate-800 hover:bg-slate-900 text-white rounded-xl text-xs font-bold transition-all"
                      >
                        ⚡ Tạo hàng loạt
                      </button>
                    </div>
                  </div>
                  <div
                    class="w-full bg-white rounded-2xl border border-slate-200 shadow-sm overflow-hidden"
                  >
                    <table class="w-full text-sm">
                      <thead class="bg-slate-50 border-b">
                        <tr>
                          <th
                            class="px-4 py-3 text-center text-xs font-bold text-slate-400 uppercase"
                          >
                            Hình ảnh
                          </th>
                          <th
                            class="px-4 py-3 text-left text-xs font-bold text-slate-400 uppercase"
                          >
                            Mã SPCT
                          </th>

                          <th
                            class="px-4 py-3 text-left text-xs font-bold text-slate-400 uppercase"
                          >
                            Màu
                          </th>
                          <th
                            class="px-4 py-3 text-left text-xs font-bold text-slate-400 uppercase"
                          >
                            Size
                          </th>
                          <th
                            class="px-4 py-3 text-center text-xs font-bold text-slate-400 uppercase"
                          >
                            Tồn
                          </th>
                          <th
                            class="px-4 py-3 text-right text-xs font-bold text-slate-400 uppercase"
                          >
                            Giá nhập
                          </th>
                          <th
                            class="px-4 py-3 text-right text-xs font-bold text-slate-400 uppercase"
                          >
                            Giá bán
                          </th>
                          <th
                            class="px-4 py-3 text-center text-xs font-bold text-slate-400 uppercase"
                          >
                            Trạng thái
                          </th>
                          <th
                            class="px-4 py-3 text-right text-xs font-bold text-slate-400 uppercase"
                          >
                            Thao tác
                          </th>
                        </tr>
                      </thead>
                      <tbody>
                        <tr
                          v-for="spct in spctList"
                          :key="spct.id"
                          class="border-t hover:bg-slate-50 transition-colors"
                        >
                          <td class="px-4 py-3 text-center">
                            <div class="flex flex-wrap gap-2 justify-center items-center">
                              <div
                                class="w-12 h-12 rounded-lg border border-slate-200 overflow-hidden shadow-sm"
                              >
                                <img
                                  v-if="spct.images?.length"
                                  :src="'http://localhost:8080' + spct.images[0]"
                                  class="w-full h-full object-cover"
                                />

                                <div
                                  v-else
                                  class="w-full h-full flex items-center justify-center bg-slate-100 text-slate-400 text-[10px] font-medium"
                                >
                                  No Image
                                </div>
                              </div>

                              <button
                                @click="openImageManager(spct)"
                                class="bg-indigo-50 text-indigo-600 px-3 py-1.5 rounded-lg text-[10px] font-bold hover:bg-indigo-100"
                              >
                                Quản lý ảnh
                              </button>
                            </div>
                          </td>
                          <td class="px-4 py-3">
                            <span
                              class="font-mono text-xs bg-indigo-50 text-indigo-600 px-2 py-1 rounded-lg"
                              >{{ spct.maSanPhamChiTiet }}</span
                            >
                          </td>

                          <td class="px-4 py-3 text-xs">{{ spct.tenMauSac }}</td>
                          <td class="px-4 py-3 text-xs">{{ spct.tenKichThuoc }}</td>
                          <td class="px-4 py-3 text-center text-xs font-semibold">
                            {{ spct.soLuongTon }}
                          </td>
                          <td class="px-4 py-3 text-right text-xs">
                            {{ formatCurrency(spct.giaNhap) }}
                          </td>
                          <td class="px-4 py-3 text-right text-xs text-indigo-600 font-bold">
                            {{ formatCurrency(spct.giaBan) }}
                          </td>
                          <td class="px-4 py-3 text-center">
                            <span
                              class="px-2 py-1 rounded-full text-[10px] font-bold"
                              :class="
                                spct.trangThai
                                  ? 'bg-emerald-100 text-emerald-600'
                                  : 'bg-slate-100 text-slate-500'
                              "
                              >{{ spct.trangThai ? 'Đang bán' : 'Lưu kho' }}</span
                            >
                          </td>
                          <td class="px-4 py-3">
                            <div class="flex justify-end gap-2">
                              <button
                                @click="editSPCT(spct)"
                                class="px-3 py-1 rounded-lg bg-indigo-50 text-indigo-600 hover:bg-indigo-100 text-[10px] font-bold"
                              >
                                Sửa</button
                              ><button
                                @click="removeSPCT(spct.id)"
                                class="px-3 py-1 rounded-lg bg-rose-50 text-rose-600 hover:bg-rose-100 text-[10px] font-bold"
                              >
                                Xóa
                              </button>
                            </div>
                          </td>
                        </tr>
                        <tr v-if="spctList.length === 0">
                          <td colspan="9" class="text-center py-10 text-slate-400 text-xs">
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
    </div>

    <transition name="fade"
      ><div
        v-if="isModalOpen"
        class="fixed inset-0 z-[999] flex items-center justify-center bg-black/40 backdrop-blur-[2px] p-4"
      >
        <div class="bg-white w-full max-w-2xl rounded-3xl shadow-2xl p-8">
          <div class="flex items-center justify-between mb-6">
            <h2 class="text-xl font-bold">
              {{ isEditMode ? 'Cập nhật sản phẩm' : 'Thêm sản phẩm' }}
            </h2>
            <button
              @click="isModalOpen = false"
              class="w-8 h-8 rounded-full hover:bg-slate-100 flex items-center justify-center"
            >
              ✕
            </button>
          </div>

          <div class="grid grid-cols-2 gap-4">
            <div>
              <label class="text-xs font-bold text-slate-500 uppercase">Mã sản phẩm</label>
              <input
                v-model="form.maSanPham"
                class="w-full border rounded-xl p-3 mt-1 focus:ring-2 ring-indigo-500/20 outline-none"
              />
            </div>
            <div>
              <label class="text-xs font-bold text-slate-500 uppercase">Tên sản phẩm</label>
              <input
                v-model="form.tenSanPham"
                class="w-full border rounded-xl p-3 mt-1 focus:ring-2 ring-indigo-500/20 outline-none"
              />
            </div>

            <div class="col-span-2">
              <label class="text-xs font-bold text-slate-500 uppercase"
                >Trạng thái kinh doanh</label
              >
              <select
                v-model="form.trangThai"
                class="w-full border rounded-xl p-3 mt-1 outline-none focus:ring-2 ring-indigo-500/20"
              >
                <option :value="true">Đang kinh doanh</option>
                <option :value="false">Lưu kho</option>
              </select>
            </div>

            <div>
              <label class="text-xs font-bold text-slate-500 uppercase">Danh mục</label>
              <select
                v-model="form.idDanhMuc"
                class="w-full border rounded-xl p-3 mt-1 outline-none"
              >
                <option :value="null">Chọn danh mục</option>
                <option v-for="dm in danhMucs" :key="dm.id" :value="dm.id">
                  {{ dm.tenDanhMuc }}
                </option>
              </select>
            </div>
            <div>
              <label class="text-xs font-bold text-slate-500 uppercase">Thương hiệu</label>
              <select
                v-model="form.idThuongHieu"
                class="w-full border rounded-xl p-3 mt-1 outline-none"
              >
                <option :value="null">Chọn thương hiệu</option>
                <option v-for="th in thuongHieus" :key="th.id" :value="th.id">
                  {{ th.tenThuongHieu }}
                </option>
              </select>
            </div>
            <div class="col-span-2">
              <label class="text-xs font-bold text-slate-500 uppercase">Chất liệu</label>
              <select
                v-model="form.idChatLieu"
                class="w-full border rounded-xl p-3 mt-1 outline-none"
              >
                <option :value="null">Chọn chất liệu</option>
                <option v-for="cl in chatLieus" :key="cl.id" :value="cl.id">
                  {{ cl.tenChatLieu }}
                </option>
              </select>
            </div>
            <div class="col-span-2">
              <label class="text-xs font-bold text-slate-500 uppercase">Mô tả</label>
              <textarea
                v-model="form.moTa"
                rows="4"
                class="w-full border rounded-xl p-3 mt-1 outline-none focus:ring-2 ring-indigo-500/20"
              ></textarea>
            </div>
          </div>

          <div class="flex justify-end gap-3 mt-8">
            <button
              @click="isModalOpen = false"
              class="px-6 py-3 rounded-xl border hover:bg-slate-50 transition-all font-bold text-xs uppercase"
            >
              Hủy
            </button>
            <button
              @click="handleSubmit"
              class="px-6 py-3 rounded-xl bg-indigo-600 hover:bg-indigo-700 text-white transition-all font-bold text-xs uppercase"
            >
              {{ isEditMode ? 'Cập nhật' : 'Thêm mới' }}
            </button>
          </div>
        </div>
      </div></transition
    >

    <transition name="fade"
      ><div
        v-if="isSPCTModalOpen"
        class="fixed inset-0 z-[999] bg-black/50 backdrop-blur-sm flex items-center justify-center p-4"
      >
        <div class="bg-white w-full max-w-4xl rounded-3xl shadow-2xl overflow-hidden">
          <div class="p-6 border-b flex justify-between items-center">
            <div>
              <h2 class="text-xl font-bold">
                {{ isEditSPCT ? 'Cập nhật biến thể' : 'Thêm biến thể mới' }}
              </h2>
              <p class="text-xs text-slate-400 mt-1">{{ selectedProduct?.tenSanPham }}</p>
            </div>
            <button
              @click="isSPCTModalOpen = false"
              class="w-10 h-10 rounded-xl hover:bg-slate-100 flex items-center justify-center"
            >
              ✕
            </button>
          </div>
          <div class="p-6 space-y-4 max-h-[75vh] overflow-y-auto">
            <div class="grid grid-cols-2 gap-4">
              <div>
                <label class="text-xs font-bold text-slate-500 uppercase">Mã SPCT</label
                ><input
                  v-model="formData.maSanPhamChiTiet"
                  class="w-full border rounded-xl p-3 mt-1 outline-none"
                />
              </div>
              <div>
                <label class="text-xs font-bold text-slate-500 uppercase">Tên SPCT</label
                ><input
                  v-model="formData.tenSanPhamChiTiet"
                  class="w-full border rounded-xl p-3 mt-1 outline-none"
                />
              </div>
            </div>
            <div class="grid grid-cols-2 gap-4">
              <div>
                <label class="text-xs font-bold text-slate-500 uppercase">Màu sắc</label
                ><select
                  v-model="formData.idMauSac"
                  class="w-full border rounded-xl p-3 mt-1 outline-none"
                >
                  <option :value="null">Chọn màu sắc</option>
                  <option v-for="mau in mauSacs" :key="mau.id" :value="mau.id">
                    {{ mau.tenMauSac }}
                  </option>
                </select>
              </div>
              <div>
                <label class="text-xs font-bold text-slate-500 uppercase">Kích thước</label
                ><select
                  v-model="formData.idKichThuoc"
                  class="w-full border rounded-xl p-3 mt-1 outline-none"
                >
                  <option :value="null">Chọn kích thước</option>
                  <option v-for="size in kichThuocs" :key="size.id" :value="size.id">
                    {{ size.tenKichThuoc }}
                  </option>
                </select>
              </div>
            </div>
            <div class="grid grid-cols-3 gap-4">
              <div>
                <label class="text-xs font-bold text-slate-500 uppercase">Giá nhập</label
                ><input
                  v-model.number="formData.giaNhap"
                  placeholder="Giá nhập"
                  type="number"
                  class="w-full border rounded-xl p-3 mt-1 outline-none"
                />
              </div>
              <div>
                <label class="text-xs font-bold text-slate-500 uppercase">Giá bán</label
                ><input
                  v-model.number="formData.giaBan"
                  type="number"
                  placeholder="Giá bán"
                  class="w-full border rounded-xl p-3 mt-1 outline-none"
                />
              </div>
              <div>
                <label class="text-xs font-bold text-slate-500 uppercase">Tồn kho</label
                ><input
                  v-model.number="formData.soLuongTon"
                  type="number"
                  class="w-full border rounded-xl p-3 mt-1 outline-none"
                />
              </div>
            </div>
            <div>
              <label class="text-xs font-bold text-slate-500 uppercase">Trạng thái</label
              ><select
                v-model="formData.trangThai"
                class="w-full border rounded-xl p-3 mt-1 outline-none"
              >
                <option :value="true">Đang kinh doanh</option>
                <option :value="false">Lưu kho</option>
              </select>
            </div>
            <div>
              <label class="text-xs font-bold text-slate-500 uppercase">Hình ảnh</label
              ><input
                type="file"
                multiple
                @change="handleFileUpload"
                class="w-full border rounded-xl p-3 mt-1"
              />
              <div class="flex gap-2 mt-3 flex-wrap">
                <img
                  v-for="(img, i) in previewImages"
                  :key="i"
                  :src="img"
                  class="w-20 h-20 rounded-xl object-cover border"
                />
              </div>
            </div>
          </div>
          <div class="p-6 border-t flex justify-end gap-3">
            <button
              @click="isSPCTModalOpen = false"
              class="px-6 py-3 border rounded-xl font-bold text-xs uppercase"
            >
              Hủy</button
            ><button
              @click="submitSPCT"
              class="px-6 py-3 bg-indigo-600 text-white rounded-xl font-bold text-xs uppercase"
            >
              {{ isEditSPCT ? 'Cập nhật SPCT' : 'Thêm SPCT' }}
            </button>
          </div>
        </div>
      </div></transition
    >

    <div
      v-if="isBulkOpen"
      class="fixed inset-0 z-[999] bg-black/50 flex items-center justify-center p-4"
    >
      <div class="bg-white w-[700px] rounded-3xl p-8 space-y-6">
        <h2 class="text-xl font-bold">Tạo biến thể hàng loạt</h2>
        <div>
          <p class="text-xs font-bold text-slate-500 uppercase mb-3">Màu sắc</p>
          <div class="flex flex-wrap gap-2">
            <label
              v-for="m in mauSacs"
              :key="m.id"
              class="flex items-center gap-2 text-sm bg-slate-50 px-3 py-2 rounded-lg cursor-pointer hover:bg-slate-100"
              ><input
                type="checkbox"
                :value="m.id"
                v-model="selectedColors"
                class="accent-indigo-600"
              />{{ m.tenMauSac }}</label
            >
          </div>
        </div>
        <div>
          <p class="text-xs font-bold text-slate-500 uppercase mb-3">Kích thước</p>
          <div class="flex flex-wrap gap-2">
            <label
              v-for="s in kichThuocs"
              :key="s.id"
              class="flex items-center gap-2 text-sm bg-slate-50 px-3 py-2 rounded-lg cursor-pointer hover:bg-slate-100"
              ><input
                type="checkbox"
                :value="s.id"
                v-model="selectedSizes"
                class="accent-indigo-600"
              />{{ s.tenKichThuoc }}</label
            >
          </div>
        </div>
        <div class="grid grid-cols-2 gap-4">
          <input
            v-model.number="formData.giaNhap"
            type="number"
            placeholder="Giá nhập"
            class="border p-3 rounded-xl outline-none placeholder-gray-400"
          />
          <input
            v-model.number="formData.giaBan"
            class="border p-3 rounded-xl outline-none"
            placeholder="Giá bán"
            type="number"
          />
        </div>
        <div class="bg-slate-50 p-4 rounded-xl max-h-[150px] overflow-auto text-xs text-slate-600">
          <p class="font-bold text-slate-900 mb-2">Preview:</p>
          <div v-for="v in previewVariants" :key="v.key">{{ v.colorName }} - {{ v.sizeName }}</div>
        </div>
        <div class="flex justify-end gap-3">
          <button
            @click="resetBulkForm"
            class="px-6 py-3 border rounded-xl font-bold text-xs uppercase"
          >
            Hủy
          </button>
          <button
            @click="submitBulkSPCT"
            class="px-6 py-3 bg-indigo-600 text-white rounded-xl font-bold text-xs uppercase"
          >
            Tạo {{ previewVariants.length }} biến thể
          </button>
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
        class="bg-white w-full max-w-lg rounded-3xl shadow-2xl p-8 relative z-10 animate-fade-in"
      >
        <div class="flex justify-between items-center mb-6">
          <h2 class="text-lg font-bold">Quản lý ảnh: {{ selectedSPCT?.maSanPhamChiTiet }}</h2>
          <button @click="isImageManagerOpen = false" class="text-slate-400 hover:text-black">
            ✕
          </button>
        </div>

        <div class="grid grid-cols-4 gap-3 mb-6">
          <div
            v-for="(img, index) in currentGallery"
            :key="index"
            class="relative group aspect-square"
          >
            <img :src="baseUrl + img.url" class="w-full h-full object-cover rounded-xl border" />
            <button
              @click="deleteImageAPI(img.id)"
              class="absolute -top-2 -right-2 bg-rose-500 text-white rounded-full w-6 h-6 flex items-center justify-center opacity-100 transition-all shadow-lg"
            >
              <svg class="w-3 h-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path
                  stroke-linecap="round"
                  stroke-linejoin="round"
                  stroke-width="2"
                  d="M6 18L18 6M6 6l12 12"
                />
              </svg>
            </button>
          </div>

          <label
            class="border-2 border-dashed border-slate-200 rounded-xl flex flex-col items-center justify-center cursor-pointer hover:border-indigo-500 hover:text-indigo-600 transition-all"
          >
            <span class="text-xl">+</span>
            <input type="file" multiple class="hidden" @change="handleUpload" />
          </label>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
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
import { uploadImages, deleteImage, getImagesBySPCT } from '@/service/HinhAnhSerivce'
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
// ========================
// FORM SPCT (ONLY ONE SOURCE OF TRUTH)
// ========================
const formData = ref({
  idSanPham: null,

  idMauSac: null,
  idKichThuoc: null,

  maSanPhamChiTiet: '',
  tenSanPhamChiTiet: '',

  giaNhap: null,
  giaBan: null,

  soLuongTon: 0,

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

    maSanPhamChiTiet: '',
    tenSanPhamChiTiet: '',

    giaNhap: 0,
    giaBan: 0,
    soLuongTon: 0,

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

    // gắn thống kê SPCT
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

    loadData() // ✔ phải có ()
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

const previewVariants = computed(() => {
  const result = []

  selectedColors.value.forEach((c) => {
    const color = mauSacs.value.find((m) => m.id === c)

    selectedSizes.value.forEach((s) => {
      const size = kichThuocs.value.find((k) => k.id === s)

      result.push({
        colorId: c,
        sizeId: s,
        colorName: color?.tenMauSac,
        sizeName: size?.tenKichThuoc,
        key: `${c}-${s}`,
      })
    })
  })

  return result
})
const submitBulkSPCT = async () => {
  const duplicated = previewVariants.value.filter((v) =>
    spctList.value.some(
      (item) =>
        Number(item.idMauSac) === Number(v.colorId) &&
        Number(item.idKichThuoc) === Number(v.sizeId),
    ),
  )

  if (duplicated.length > 0) {
    const ds = duplicated.map((v) => `${v.colorName} - ${v.sizeName}`).join(', ')

    toast.error(`Biến thể đã tồn tại: ${ds}`)
    return
  }
  if (Number(formData.value.giaNhap) < 0) {
    toast.error('Giá nhập không được âm')
    return
  }

  if (Number(formData.value.giaBan) < 0) {
    toast.error('Giá bán không được âm')
    return
  }

  if (Number(formData.value.giaNhap) > Number(formData.value.giaBan)) {
    toast.error('Giá nhập không được lớn hơn giá bán')
    return
  }

  if (!selectedColors.value.length || !selectedSizes.value.length) {
    toast.error('Chọn màu và size')
    return
  }

  const payload = previewVariants.value.map((v) => ({
    idSanPham: selectedProduct.value.id,
    idMauSac: v.colorId,
    idKichThuoc: v.sizeId,
    giaNhap: formData.value.giaNhap,
    giaBan: formData.value.giaBan,
    soLuongTon: formData.value.soLuongTon || 0,
  }))

  try {
    await fetch('http://localhost:8080/spct/bulk', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(payload),
    })

    toast.success('Tạo biến thể thành công')
    resetBulkForm()

    await loadSPCT(selectedProduct.value.id)
    await loadData()
  } catch (e) {
    toast.error('Lỗi tạo biến thể')
  }
}

const openBulk = (product) => {
  selectedProduct.value = product
  formData.value.idSanPham = product.id

  selectedColors.value = []
  selectedSizes.value = []

  isBulkOpen.value = true
}

const resetBulkForm = () => {
  selectedColors.value = []
  selectedSizes.value = []

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
  if (!formData.value.maSanPhamChiTiet?.trim()) {
    toast.error('Mã SPCT không được để trống')
    return false
  }

  if (!formData.value.tenSanPhamChiTiet?.trim()) {
    toast.error('Tên SPCT không được để trống')
    return false
  }

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

const isDuplicateCode = () => {
  return spctList.value.some(
    (item) =>
      item.maSanPhamChiTiet?.trim().toLowerCase() ===
        formData.value.maSanPhamChiTiet?.trim().toLowerCase() && item.id !== editingSPCTId.value,
  )
}
const submitSPCT = async () => {
  if (!validateSPCT()) return

  if (isDuplicateCode()) {
    toast.error('Mã SPCT đã tồn tại')
    return
  }

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
      maSanPhamChiTiet: formData.value.maSanPhamChiTiet,
      tenSanPhamChiTiet: formData.value.tenSanPhamChiTiet,
      giaNhap: formData.value.giaNhap,
      giaBan: formData.value.giaBan,
      soLuongTon: formData.value.soLuongTon,
      trangThai: formData.value.trangThai,
    }).forEach(([k, v]) => payload.append(k, v ?? ''))

    selectedFiles.value.forEach((f) => payload.append('files', f))

    try {
      if (isEditSPCT.value) {
        await updateSanPhamChiTiet(editingSPCTId.value, payload)
        toast.success('Cập nhật SPCT thành công')
      } else {
        await createSanPhamChiTiet(payload)
        toast.success('Thêm SPCT thành công')
      }
    } catch (e) {
      console.error(e)
      toast.error('Có lỗi xảy ra')
    }

    // reload SPCT list
    await loadSPCT(formData.value.idSanPham)

    await loadData()

    // reset form
    selectedFiles.value = []
    previewImages.value = []
    isEditSPCT.value = false
    editingSPCTId.value = null
    isSPCTModalOpen.value = false

    Object.assign(formData.value, {
      maSanPhamChiTiet: '',
      tenSanPhamChiTiet: '',
      giaNhap: 0,
      giaBan: 0,
      soLuongTon: 0,
      trangThai: true,
    })
  } catch (e) {
    console.error(e)
  }
}

const editSPCT = (spct) => {
  isEditSPCT.value = true
  editingSPCTId.value = spct.id

  formData.value = {
    ...spct,
  }

  previewImages.value = []

  isSPCTModalOpen.value = true
}

// ========================
// DELETE SPCT (OPTIONAL)
// ========================
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
// ========================
// FILTER PRODUCT LIST
// ========================
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

// ========================
// UTILS
// ========================
const clearFilters = () => {
  filters.search = ''
  filters.idDanhMuc = null
  filters.idThuongHieu = null
  filters.idChatLieu = null
  filters.trangThai = null
}

//Sản phẩm

const isModalOpen = ref(false)
const isEditMode = ref(false)
const isVariantMode = ref(false)

const form = ref({
  idDanhMuc: null,
  idThuongHieu: null,
  idChatLieu: null,
  maSanPham: '',
  tenSanPham: '',
  moTa: '',
})

const resetForm = () => {
  form.value = {
    id: null,
    idDanhMuc: null,
    idThuongHieu: null,
    idChatLieu: null,
    maSanPham: '',
    tenSanPham: '',
    trangThai: true,
    image: '',
  }
}

const openAddModal = () => {
  form.value = {
    idDanhMuc: null,
    idThuongHieu: null,
    idChatLieu: null,
    maSanPham: '',
    tenSanPham: '',
    moTa: '',
  }

  isEditMode.value = false
  isModalOpen.value = true
}
const validateSP = () => {
  if (!form.value.maSanPham?.trim()) {
    toast.error('Mã sản phẩm không được để trống')
    return false
  }

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
      maSanPham: form.value.maSanPham,
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
    await loadSPCT(selectedProduct.value.id)

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
    currentGallery.value = await getImagesBySPCT(spct.id)

    isImageManagerOpen.value = true
  } catch (e) {
    toast.error(e.message)
  }
}

const handleUpload = async (event) => {
  try {
    const files = Array.from(event.target.files)

    await uploadImages(selectedSPCT.value.id, files)

    await reloadGallery()
    toast.success('Thêm ảnh thành công')

    event.target.value = '' // 👈 QUAN TRỌNG

    await loadSPCT(selectedProduct.value.id)
  } catch (e) {
    toast.error(e.message || 'Upload lỗi')
  }
}

const deleteImageAPI = async (id) => {
  try {
    await deleteImage(id)

    currentGallery.value = currentGallery.value.filter((img) => img.id !== id)

    await loadSPCT(selectedProduct.value.id)
    toast.success('Xóa ảnh thành công')
  } catch (e) {
    toast.error(e.message)
  }
}
const reloadGallery = async () => {
  try {
    const id = selectedSPCT.value?.id
    if (!id) return

    const res = await fetch(`${baseUrl}/spct/sp/${id}`)

    if (!res.ok) {
      throw new Error('Load SPCT thất bại')
    }

    const data = await res.json()

    // 🔥 QUAN TRỌNG: images nằm trong spct
    currentGallery.value = data.images || []

    // (optional) sync lại SPCT luôn nếu cần
    selectedSPCT.value = data
  } catch (err) {
    console.error('reloadGallery lỗi:', err)
  }
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
</style>
