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
              class="w-full pl-10 pr-4 py-2.5 bg-slate-50 border rounded-xl text-xs outline-none"
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
              class="border px-3 py-2.5 rounded-xl text-xs bg-rose-50 text-rose-600 hover:bg-rose-100"
            >
              Xóa bộ lọc
            </button>
          </div>
        </div>

        <table class="w-full text-left">
          <thead class="text-[10px] font-bold text-slate-400 uppercase border-b">
            <tr>
              <th class="px-6 py-4">Mã SP</th>
              <th class="px-6 py-4">Sản phẩm</th>
              <th class="px-6 py-4">Danh mục</th>
              <th class="px-6 py-4">Thương hiệu</th>
              <th class="px-6 py-4">Trạng thái</th>
              <th class="px-6 py-4 text-right">Thao tác</th>
            </tr>
          </thead>
          <tbody class="divide-y divide-slate-100">
            <template v-for="product in filteredProducts" :key="product.id">
              <tr class="hover:bg-slate-50">
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
                <td
                  class="px-6 py-4 text-xs font-bold"
                  :class="product.trangThai ? 'text-emerald-600' : 'text-slate-400'"
                >
                  {{ product.trangThai ? 'Kinh doanh' : 'Lưu kho' }}
                </td>
                <td class="px-6 py-4 text-right flex justify-end gap-2">
                  <button
                    @click="toggleSPCT(product)"
                    class="p-2 hover:bg-emerald-50 rounded-lg text-slate-400 hover:text-emerald-600"
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
                    class="p-2 hover:bg-indigo-50 rounded-lg text-slate-400 hover:text-indigo-600"
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
                    class="p-2 hover:bg-rose-50 rounded-lg text-slate-400 hover:text-rose-600"
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
              <tr v-if="expandedProductId === product.id" class="bg-slate-50">
                <td colspan="6" class="p-5">
                  <div class="flex justify-between items-center mb-4">
                    <div>
                      <h3 class="font-bold">
                        {{ product.tenSanPham }}
                      </h3>

                      <p class="text-xs text-slate-400">Danh sách biến thể</p>
                    </div>

                    <button
                      @click="openAddSPCTModal(product)"
                      class="px-4 py-2 bg-indigo-600 text-white rounded-xl"
                    >
                      + Thêm biến thể
                    </button>
                    <button
                      @click="isBulkOpen = true"
                      class="px-4 py-2 bg-indigo-600 text-white rounded-xl text-sm"
                    >
                      ⚡ Tạo hàng loạt biến thể
                    </button>
                  </div>

                  <div class="overflow-x-auto bg-white rounded-2xl border border-slate-200">
                    <table class="w-full text-sm">
                      <thead class="bg-slate-100">
                        <tr>
                          <th class="px-4 py-3 text-left">Mã SPCT</th>
                          <th class="px-4 py-3 text-left">Tên biến thể</th>
                          <th class="px-4 py-3 text-left">Màu</th>
                          <th class="px-4 py-3 text-left">Size</th>
                          <th class="px-4 py-3 text-center">Tồn</th>
                          <th class="px-4 py-3 text-right">Giá nhập</th>
                          <th class="px-4 py-3 text-right">Giá bán</th>
                          <th class="px-4 py-3 text-center">Trạng thái</th>
                          <th class="px-4 py-3 text-right">Thao tác</th>
                        </tr>
                      </thead>

                      <tbody>
                        <tr
                          v-for="spct in spctList"
                          :key="spct.id"
                          class="border-t hover:bg-slate-50"
                        >
                          <td class="px-4 py-3">
                            <span
                              class="font-mono text-xs bg-indigo-50 text-indigo-600 px-2 py-1 rounded-lg"
                            >
                              {{ spct.maSanPhamChiTiet }}
                            </span>
                          </td>

                          <td class="px-4 py-3 font-medium">
                            {{ spct.tenSanPhamChiTiet }}
                          </td>

                          <td class="px-4 py-3">
                            {{ spct.tenMauSac }}
                          </td>

                          <td class="px-4 py-3">
                            {{ spct.tenKichThuoc }}
                          </td>

                          <td class="px-4 py-3 text-center font-semibold">
                            {{ spct.soLuongTon }}
                          </td>

                          <td class="px-4 py-3 text-right">
                            {{ formatCurrency(spct.giaNhap) }}
                          </td>

                          <td class="px-4 py-3 text-right text-indigo-600 font-bold">
                            {{ formatCurrency(spct.giaBan) }}
                          </td>

                          <td class="px-4 py-3 text-center">
                            <span
                              class="px-2 py-1 rounded-full text-xs font-semibold"
                              :class="
                                spct.trangThai
                                  ? 'bg-emerald-100 text-emerald-600'
                                  : 'bg-slate-100 text-slate-500'
                              "
                            >
                              {{ spct.trangThai ? 'Đang bán' : 'Lưu kho' }}
                            </span>
                          </td>

                          <td class="px-4 py-3">
                            <div class="flex justify-end gap-2">
                              <button
                                @click="editSPCT(spct)"
                                class="px-3 py-1.5 rounded-lg bg-indigo-50 text-indigo-600 hover:bg-indigo-100"
                              >
                                Sửa
                              </button>

                              <button
                                @click="removeSPCT(spct.id)"
                                class="px-3 py-1.5 rounded-lg bg-rose-50 text-rose-600 hover:bg-rose-100"
                              >
                                Xóa
                              </button>
                            </div>
                          </td>
                        </tr>

                        <tr v-if="spctList.length === 0">
                          <td colspan="9" class="text-center py-10 text-slate-400">
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
    <transition name="fade">
      <div
        v-if="isModalOpen"
        class="fixed inset-0 z-[999] flex items-center justify-center bg-black/40"
      >
        <div class="bg-white w-full max-w-2xl rounded-3xl shadow-2xl p-6">
          <div class="flex items-center justify-between mb-6">
            <h2 class="text-xl font-bold">
              {{ isEditMode ? 'Cập nhật sản phẩm' : 'Thêm sản phẩm' }}
            </h2>

            <button @click="isModalOpen = false" class="w-8 h-8 rounded-full hover:bg-slate-100">
              ✕
            </button>
          </div>

          <div class="grid grid-cols-2 gap-4">
            <div>
              <label class="text-xs text-slate-500">Mã sản phẩm</label>
              <input v-model="form.maSanPham" class="w-full border rounded-xl p-3 mt-1" />
            </div>

            <div>
              <label class="text-xs text-slate-500">Tên sản phẩm</label>
              <input v-model="form.tenSanPham" class="w-full border rounded-xl p-3 mt-1" />
            </div>

            <div>
              <label class="text-xs text-slate-500">Danh mục</label>
              <select v-model="form.idDanhMuc" class="w-full border rounded-xl p-3 mt-1">
                <option :value="null">Chọn danh mục</option>

                <option v-for="dm in danhMucs" :key="dm.id" :value="dm.id">
                  {{ dm.tenDanhMuc }}
                </option>
              </select>
            </div>

            <div>
              <label class="text-xs text-slate-500">Thương hiệu</label>
              <select v-model="form.idThuongHieu" class="w-full border rounded-xl p-3 mt-1">
                <option :value="null">Chọn thương hiệu</option>

                <option v-for="th in thuongHieus" :key="th.id" :value="th.id">
                  {{ th.tenThuongHieu }}
                </option>
              </select>
            </div>

            <div class="col-span-2">
              <label class="text-xs text-slate-500">Chất liệu</label>
              <select v-model="form.idChatLieu" class="w-full border rounded-xl p-3 mt-1">
                <option :value="null">Chọn chất liệu</option>

                <option v-for="cl in chatLieus" :key="cl.id" :value="cl.id">
                  {{ cl.tenChatLieu }}
                </option>
              </select>
            </div>

            <div class="col-span-2">
              <label class="text-xs text-slate-500">Mô tả</label>

              <textarea
                v-model="form.moTa"
                rows="4"
                class="w-full border rounded-xl p-3 mt-1"
              ></textarea>
            </div>
          </div>

          <div class="flex justify-end gap-3 mt-6">
            <button @click="isModalOpen = false" class="px-5 py-3 rounded-xl border">Hủy</button>

            <button @click="handleSubmit" class="px-5 py-3 rounded-xl bg-indigo-600 text-white">
              {{ isEditMode ? 'Cập nhật' : 'Thêm mới' }}
            </button>
          </div>
        </div>
      </div>
    </transition>
    <transition name="fade">
      <div
        v-if="isSPCTModalOpen"
        class="fixed inset-0 z-[999] bg-black/50 backdrop-blur-sm flex items-center justify-center p-4"
      >
        <div class="bg-white w-full max-w-4xl rounded-3xl shadow-2xl overflow-hidden">
          <!-- HEADER -->
          <div class="p-6 border-b flex justify-between items-center">
            <div>
              <h2 class="text-xl font-bold">
                {{ isEditSPCT ? 'Cập nhật biến thể' : 'Thêm biến thể mới' }}
              </h2>

              <p class="text-sm text-slate-400">
                {{ selectedProduct?.tenSanPham }}
              </p>
            </div>

            <button
              @click="isSPCTModalOpen = false"
              class="w-10 h-10 rounded-xl hover:bg-slate-100"
            >
              ✕
            </button>
          </div>

          <!-- BODY -->
          <div class="p-6 space-y-4 max-h-[75vh] overflow-y-auto">
            <div class="grid grid-cols-2 gap-4">
              <div>
                <label class="text-xs text-slate-500"> Mã SPCT </label>

                <input
                  v-model="formData.maSanPhamChiTiet"
                  class="w-full border rounded-xl p-3 mt-1"
                />
              </div>

              <div>
                <label class="text-xs text-slate-500"> Tên SPCT </label>

                <input
                  v-model="formData.tenSanPhamChiTiet"
                  class="w-full border rounded-xl p-3 mt-1"
                />
              </div>
            </div>

            <!-- MÀU + SIZE -->

            <div class="grid grid-cols-2 gap-4">
              <div>
                <label class="text-xs text-slate-500"> Màu sắc </label>

                <select v-model="formData.idMauSac" class="w-full border rounded-xl p-3 mt-1">
                  <option :value="null">Chọn màu sắc</option>

                  <option v-for="mau in mauSacs" :key="mau.id" :value="mau.id">
                    {{ mau.tenMauSac }}
                  </option>
                </select>
              </div>

              <div>
                <label class="text-xs text-slate-500"> Kích thước </label>

                <select v-model="formData.idKichThuoc" class="w-full border rounded-xl p-3 mt-1">
                  <option :value="null">Chọn kích thước</option>

                  <option v-for="size in kichThuocs" :key="size.id" :value="size.id">
                    {{ size.tenKichThuoc }}
                  </option>
                </select>
              </div>
            </div>

            <!-- GIÁ -->

            <div class="grid grid-cols-3 gap-4">
              <div>
                <label class="text-xs text-slate-500"> Giá nhập </label>

                <input
                  v-model.number="formData.giaNhap"
                  type="number"
                  class="w-full border rounded-xl p-3 mt-1"
                />
              </div>

              <div>
                <label class="text-xs text-slate-500"> Giá bán </label>

                <input
                  v-model.number="formData.giaBan"
                  type="number"
                  class="w-full border rounded-xl p-3 mt-1"
                />
              </div>

              <div>
                <label class="text-xs text-slate-500"> Tồn kho </label>

                <input
                  v-model.number="formData.soLuongTon"
                  type="number"
                  class="w-full border rounded-xl p-3 mt-1"
                />
              </div>
            </div>

            <!-- TRẠNG THÁI -->

            <div>
              <label class="text-xs text-slate-500"> Trạng thái </label>

              <select v-model="formData.trangThai" class="w-full border rounded-xl p-3 mt-1">
                <option :value="true">Đang kinh doanh</option>
                <option :value="false">Lưu kho</option>
              </select>
            </div>

            <!-- ẢNH -->

            <div>
              <label class="text-xs text-slate-500"> Hình ảnh </label>

              <input
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

          <!-- FOOTER -->

          <div class="p-6 border-t flex justify-end gap-3">
            <button @click="isSPCTModalOpen = false" class="px-5 py-3 border rounded-xl">
              Hủy
            </button>

            <button @click="submitSPCT" class="px-5 py-3 bg-indigo-600 text-white rounded-xl">
              {{ isEditSPCT ? 'Cập nhật SPCT' : 'Thêm SPCT' }}
            </button>
          </div>
        </div>
      </div>
    </transition>
    <div
      v-if="isBulkOpen"
      class="fixed inset-0 z-[999] bg-black/50 flex items-center justify-center"
    >
      <div class="bg-white w-[700px] rounded-2xl p-6 space-y-4">
        <h2 class="text-lg font-bold">Tạo biến thể hàng loạt</h2>

        <!-- MÀU -->
        <div>
          <p class="text-sm font-semibold mb-2">Màu sắc</p>
          <div class="flex flex-wrap gap-3">
            <label v-for="m in mauSacs" :key="m.id" class="flex items-center gap-2 text-sm">
              <input type="checkbox" :value="m.id" v-model="selectedColors" />
              {{ m.tenMauSac }}
            </label>
          </div>
        </div>

        <!-- SIZE -->
        <div>
          <p class="text-sm font-semibold mb-2">Kích thước</p>
          <div class="flex flex-wrap gap-3">
            <label v-for="s in kichThuocs" :key="s.id" class="flex items-center gap-2 text-sm">
              <input type="checkbox" :value="s.id" v-model="selectedSizes" />
              {{ s.tenKichThuoc }}
            </label>
          </div>
        </div>

        <!-- GIÁ CHUNG -->
        <div class="grid grid-cols-2 gap-3">
          <input
            v-model.number="formData.giaNhap"
            class="border p-2 rounded-xl"
            placeholder="Giá nhập"
            type="number"
          />

          <input
            v-model.number="formData.giaBan"
            class="border p-2 rounded-xl"
            placeholder="Giá bán"
            type="number"
          />
        </div>

        <!-- PREVIEW -->
        <div class="bg-slate-50 p-3 rounded-xl max-h-[200px] overflow-auto text-sm">
          <p class="font-semibold mb-2">Preview:</p>

          <div v-for="v in previewVariants" :key="v.key">{{ v.colorName }} - {{ v.sizeName }}</div>
        </div>

        <!-- ACTION -->
        <div class="flex justify-end gap-3">
          <button @click="isBulkOpen = false" class="px-4 py-2 border rounded-xl">Hủy</button>

          <button @click="submitBulkSPCT" class="px-4 py-2 bg-indigo-600 text-white rounded-xl">
            Tạo {{ previewVariants.length }} biến thể
          </button>
        </div>
      </div>
    </div>
    <transition name="fade">
      <div
        v-if="confirmModal.show"
        class="fixed inset-0 z-[999] flex items-center justify-center bg-black/50 backdrop-blur-sm"
      >
        <div class="w-full max-w-md bg-white rounded-3xl shadow-2xl overflow-hidden">
          <div class="p-6">
            <h3 class="text-lg font-bold text-slate-900">Xác nhận xóa sản phẩm</h3>

            <p class="mt-2 text-sm text-slate-500">
              Hành động này không thể hoàn tác. Bạn có chắc chắn muốn xóa?
            </p>
          </div>

          <div class="flex justify-end gap-3 p-6 border-t">
            <button
              @click="cancelDelete"
              class="px-5 py-2 rounded-xl border border-slate-200 hover:bg-slate-50"
            >
              Hủy
            </button>

            <button
              @click="confirmDelete"
              class="px-5 py-2 rounded-xl bg-rose-600 text-white hover:bg-rose-700"
            >
              Xóa
            </button>
          </div>
        </div>
      </div>
    </transition>
  </div>
</template>
<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'

import { getAllSanpham } from '@/service/SanphamService'
import { getAllDanhMuc } from '@/service/DanhMucService'
import { getAllThuongHieu } from '@/service/ThuongHieuService'
import { getAllChatLieu } from '@/service/ChatLieuService'
import { updateSanPhamChiTiet } from '@/service/SanPhamChiTiet'
import { useToast } from 'vue-toastification'
import { getAllMauSac } from '@/service/MauSacService'
import { getAllKichThuoc } from '@/service/KichThuocService'

const toast = useToast()
const baseUrl = 'http://localhost:8080'
import {
  getSanPhamChiTietByProductId,
  createSanPhamChiTiet,
  deleteSanPhamChiTiet,
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
// ========================
// DRAWER STATE (SPCT)
// ========================
const isSPCTModalOpen = ref(false)
const selectedProduct = ref(null)
const expandedProductId = ref(null)

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

  giaNhap: 0,
  giaBan: 0,

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
    ;[
      products.value,
      danhMucs.value,
      thuongHieus.value,
      chatLieus.value,
      mauSacs.value,
      kichThuocs.value,
    ] = await Promise.all([
      getAllSanpham(),
      getAllDanhMuc(),
      getAllThuongHieu(),
      getAllChatLieu(),
      getAllMauSac(),
      getAllKichThuoc(),
    ])
  } catch (err) {
    console.error(err)
  }
}

onMounted(loadData)

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

    isBulkOpen.value = false
    selectedColors.value = []
    selectedSizes.value = []

    await loadSPCT(selectedProduct.value.id)
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

  if (Number(formData.value.giaNhap) <= 0) {
    toast.error('Giá nhập phải lớn hơn 0')
    return false
  }

  if (Number(formData.value.giaBan) <= 0) {
    toast.error('Giá bán phải lớn hơn 0')
    return false
  }

  if (Number(formData.value.giaBan) < Number(formData.value.giaNhap)) {
    toast.error('Giá bán không được nhỏ hơn giá nhập')
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
  try {
    await deleteSanPhamChiTiet(id)
    await loadSPCT(selectedProduct.value.id)
  } catch (e) {
    console.error(e)
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
