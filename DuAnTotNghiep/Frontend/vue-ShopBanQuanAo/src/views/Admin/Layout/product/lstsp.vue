<template>
  <div
    class="min-h-screen bg-[#f8fafc] text-slate-800 font-sans antialiased selection:bg-indigo-600 selection:text-white p-4 sm:p-8 animate-fade-in"
  >
    <div class="max-w-[1500px] mx-auto grid grid-cols-1 xl:grid-cols-12 gap-8 items-start">
      <div class="xl:col-span-9 space-y-8">
        <div
          class="relative bg-slate-900 rounded-3xl p-8 overflow-hidden shadow-[0_20px_50px_rgba(15,23,42,0.15)] text-white flex flex-col md:flex-row justify-between items-start md:items-center gap-6"
        >
          <div
            class="absolute -right-10 -top-10 w-40 h-40 bg-indigo-600/20 rounded-full blur-3xl"
          ></div>
          <div
            class="absolute -left-10 -bottom-10 w-40 h-40 bg-emerald-500/10 rounded-full blur-3xl"
          ></div>

          <div class="relative z-10 space-y-1">
            <span class="text-[10px] font-bold uppercase tracking-[0.2em] text-indigo-400"
              >Hệ thống quản trị thế hệ mới</span
            >
            <h1 class="text-3xl font-black tracking-tight text-white">Bảng Điều Phối Sản Phẩm</h1>
            <p class="text-xs text-slate-400">
              Sắp xếp, định danh và tối ưu hóa chuỗi cung ứng thời gian thực.
            </p>
          </div>

          <button
            @click="openAddModal"
            class="relative z-10 shrink-0 flex items-center gap-2.5 px-6 py-3.5 bg-indigo-600 hover:bg-indigo-500 text-white rounded-2xl text-xs font-bold uppercase tracking-wider shadow-lg shadow-indigo-600/20 transition-all duration-300 hover:-translate-y-0.5 active:translate-y-0"
          >
            <svg
              xmlns="http://www.w3.org/2000/svg"
              fill="none"
              viewBox="0 0 24 24"
              stroke-width="3"
              stroke="currentColor"
              class="w-4 h-4"
            >
              <path stroke-linecap="round" stroke-linejoin="round" d="M12 4.5v15m7.5-7.5h-15" />
            </svg>
            Khởi tạo mặt hàng
          </button>
        </div>

        <div
          class="bg-white border border-slate-200/80 rounded-2xl p-5 shadow-[0_8px_30px_rgb(0,0,0,0.01)] flex flex-col lg:flex-row items-center gap-4"
        >
          <div class="relative w-full lg:max-w-xs">
            <span
              class="absolute inset-y-0 left-0 flex items-center pl-3.5 pointer-events-none text-slate-400"
            >
              <svg
                xmlns="http://www.w3.org/2000/svg"
                fill="none"
                viewBox="0 0 24 24"
                stroke-width="2"
                stroke="currentColor"
                class="w-4 h-4"
              >
                <path
                  stroke-linecap="round"
                  stroke-linejoin="round"
                  d="M21 21l-5.197-5.197m0 0A7.5 7.5 0 105.196 5.196a7.5 7.5 0 0010.604 10.604z"
                />
              </svg>
            </span>
            <input
              v-model="filters.search"
              type="text"
              placeholder="Tìm theo tên hoặc mã hàng..."
              class="w-full pl-10 pr-4 py-2.5 bg-slate-50 border border-slate-200 focus:border-indigo-600 focus:bg-white focus:ring-4 focus:ring-indigo-600/5 outline-none rounded-xl text-xs font-medium transition-all placeholder:text-slate-400"
            />
          </div>

          <div class="grid grid-cols-2 sm:grid-cols-4 gap-3 w-full flex-1">
            <select
              v-model="filters.idDanhMuc"
              class="w-full border border-slate-200 px-3 py-2.5 rounded-xl text-xs bg-slate-50 text-slate-600 focus:border-indigo-600 focus:bg-white outline-none font-medium transition-colors"
            >
              <option :value="null">Danh mục</option>
              <option v-for="item in danhMucs" :key="item.id" :value="item.id">
                {{ item.tenDanhMuc }}
              </option>
            </select>

            <select
              v-model="filters.idThuongHieu"
              class="w-full border border-slate-200 px-3 py-2.5 rounded-xl text-xs bg-slate-50 text-slate-600 focus:border-indigo-600 focus:bg-white outline-none font-medium transition-colors"
            >
              <option :value="null">Thương hiệu</option>
              <option v-for="item in thuongHieus" :key="item.id" :value="item.id">
                {{ item.tenThuongHieu }}
              </option>
            </select>

            <select
              v-model="filters.idChatLieu"
              class="w-full border border-slate-200 px-3 py-2.5 rounded-xl text-xs bg-slate-50 text-slate-600 focus:border-indigo-600 focus:bg-white outline-none font-medium transition-colors"
            >
              <option :value="null">Chất liệu</option>
              <option v-for="item in chatLieus" :key="item.id" :value="item.id">
                {{ item.tenChatLieu }}
              </option>
            </select>

            <select
              v-model="filters.trangThai"
              class="w-full border border-slate-200 px-3 py-2.5 rounded-xl text-xs bg-slate-50 text-slate-600 focus:border-indigo-600 focus:bg-white outline-none font-medium transition-colors"
            >
              <option :value="null">Trạng thái</option>
              <option :value="true">Kinh doanh</option>
              <option :value="false">Tạm ngưng</option>
            </select>
          </div>

          <button
            v-if="hasActiveFilters"
            @click="clearFilters"
            class="text-[11px] font-bold text-rose-600 hover:text-rose-700 bg-rose-50 hover:bg-rose-100/80 px-3.5 py-2.5 rounded-xl transition-all shrink-0"
          >
            Xóa bộ lọc
          </button>
        </div>
        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          <div
            v-for="product in filteredProducts"
            :key="product.id"
            @dblclick="goToSPCT(product.id)"
            class="group relative bg-white border border-slate-200/80 rounded-2xl p-6 shadow-[0_8px_30px_rgb(0,0,0,0.02)] hover:shadow-[0_20px_40px_rgba(0,0,0,0.04)] hover:border-indigo-600/50 transition-all duration-300 flex flex-col justify-between overflow-hidden"
          >
            <div
              class="absolute top-0 right-0 w-24 h-24 bg-gradient-to-bl from-slate-50 to-transparent -z-0 transition-all duration-300 group-hover:scale-110"
            ></div>

            <div class="relative z-10 space-y-4 w-full">
              <div class="flex justify-between items-center">
                <span
                  class="font-mono text-[11px] font-bold text-indigo-600 bg-indigo-50 px-2.5 py-1 rounded-lg border border-indigo-100/50"
                >
                  SKU.{{ product.maSanPham }}
                </span>

                <span
                  :class="[
                    'w-2 h-2 rounded-full ring-4',
                    product.trangThai
                      ? 'bg-emerald-500 ring-emerald-500/10'
                      : 'bg-slate-300 ring-slate-300/10',
                  ]"
                  :title="product.trangThai ? 'Đang kích hoạt' : 'Tạm ngưng'"
                ></span>
              </div>

              <div class="flex items-start gap-4 pt-2">
                <div
                  class="w-16 h-16 rounded-2xl bg-slate-50 border border-slate-200 overflow-hidden shrink-0 flex items-center justify-center group-hover:scale-105 transition-transform duration-300"
                >
                  <img
                    v-if="product.image"
                    :src="getImageUrl(product.image)"
                    class="w-full h-full object-cover"
                  />
                  <span v-else class="text-2xl">👕</span>
                </div>
                <div class="space-y-1 min-w-0">
                  <h3
                    class="font-bold text-slate-900 text-base tracking-tight truncate capitalize group-hover:text-indigo-600 transition-colors"
                  >
                    {{ product.tenSanPham }}
                  </h3>
                  <div class="text-[11px] font-medium text-slate-400 flex items-center gap-1.5">
                    <span class="w-1 h-1 rounded-full bg-slate-300"></span>
                    {{ product.idDanhMuc?.tenDanhMuc || 'Chưa phân loại' }}
                  </div>
                </div>
              </div>

              <div class="grid grid-cols-2 gap-2 pt-2 text-[11px]">
                <div class="bg-slate-50/80 p-2 rounded-xl border border-slate-100">
                  <div class="text-slate-400 font-medium scale-90 origin-left">THƯƠNG HIỆU</div>
                  <div class="font-semibold text-slate-700 truncate mt-0.5">
                    {{ product.idThuongHieu?.tenThuongHieu || 'N/A' }}
                  </div>
                </div>
                <div class="bg-slate-50/80 p-2 rounded-xl border border-slate-100">
                  <div class="text-slate-400 font-medium scale-90 origin-left">CHẤT LIỆU</div>
                  <div class="font-semibold text-slate-700 truncate mt-0.5">
                    {{ product.idChatLieu?.tenChatLieu || 'N/A' }}
                  </div>
                </div>
              </div>
            </div>

            <div
              class="relative z-10 flex items-center justify-between border-t border-slate-100 mt-6 pt-4"
            >
              <span
                class="text-[11px] font-semibold tracking-wider uppercase"
                :class="product.trangThai ? 'text-emerald-600' : 'text-slate-400'"
              >
                {{ product.trangThai ? 'Kinh doanh' : 'Lưu kho' }}
              </span>

              <div class="flex items-center gap-1">
                <button
                  @click="openEditModal(product)"
                  class="p-2 text-slate-400 hover:text-indigo-600 hover:bg-indigo-50 rounded-xl transition-all"
                  title="Xem chi tiết sản phẩm"
                >
                  <svg
                    xmlns="http://www.w3.org/2000/svg"
                    class="w-4 h-4"
                    fill="none"
                    viewBox="0 0 24 24"
                    stroke="currentColor"
                    stroke-width="2.5"
                  >
                    <path
                      stroke-linecap="round"
                      stroke-linejoin="round"
                      d="M2.036 12.322a1.012 1.012 0 010-.644M12 18.75c-5.42 0-9.75-4.25-9.75-9.75s4.33-9.75 9.75-9.75 9.75 4.33 9.75 9.75-4.33 9.75-9.75 9.75z"
                    />
                    <path
                      stroke-linecap="round"
                      stroke-linejoin="round"
                      d="M15 12a3 3 0 11-6 0 3 3 0 016 0z"
                    />
                  </svg>
                </button>

                <button
                  @click="openEditModal(product)"
                  class="p-2 text-slate-400 hover:text-slate-900 hover:bg-slate-50 rounded-xl transition-all"
                  title="Biến đổi thông tin"
                >
                  <svg
                    xmlns="http://www.w3.org/2000/svg"
                    class="w-4 h-4"
                    fill="none"
                    viewTemplate="0 0 24 24"
                    viewBox="0 0 24 24"
                    stroke="currentColor"
                    stroke-width="2.5"
                  >
                    <path
                      stroke-linecap="round"
                      stroke-linejoin="round"
                      d="M16.862 4.487l1.687-1.688a1.875 1.875 0 112.652 2.652L10.582 16.07a4.5 4.5 0 01-1.897 1.13L6 18l.8-2.685a4.5 4.5 0 011.13-1.897l8.932-8.931zm0 0L19.5 7.125M18 14v4.75A2.25 2.25 0 0115.75 21H5.25A2.25 2.25 0 013 18.75V8.25A2.25 2.25 0 015.25 6H10"
                    />
                  </svg>
                </button>

                <button
                  @click="triggerDeleteConfirm(product.id)"
                  class="p-2 text-slate-400 hover:text-rose-600 hover:bg-rose-50 rounded-xl transition-all"
                  title="Hủy bỏ bản ghi"
                >
                  <svg
                    xmlns="http://www.w3.org/2000/svg"
                    class="w-4 h-4"
                    fill="none"
                    viewBox="0 0 24 24"
                    stroke="currentColor"
                    stroke-width="2.5"
                  >
                    <path
                      stroke-linecap="round"
                      stroke-linejoin="round"
                      d="M14.74 9l-.346 9m-4.788 0L9.26 9m9.968-3.21c.342.052.682.107 1.022.166m-1.022-.165L18.16 19.673a2.25 2.25 0 01-2.244 2.077H8.084a2.25 2.25 0 01-2.244-2.077L4.772 5.79m14.456 0a48.108 48.108 0 00-3.478-.397m-12 .562c.34-.059.68-.114 1.022-.165m0 0a48.11 48.11 0 013.478-.397m7.5 0v-.916c0-1.18-.91-2.164-2.09-2.201a51.964 51.964 0 00-3.32 0c-1.18.037-2.09 1.022-2.09 2.201v.916m7.5 0a48.667 48.667 0 00-7.5 0"
                    />
                  </svg>
                </button>
              </div>
            </div>
          </div>

          <div
            v-if="filteredProducts.length === 0"
            class="col-span-full bg-white border border-dashed border-slate-300 rounded-2xl py-20 text-center space-y-3"
          >
            <span class="text-3xl block animate-bounce">📦</span>
            <h4 class="font-bold text-slate-700 text-sm">Không tìm thấy sản phẩm nào</h4>
            <p class="text-xs text-slate-400 max-w-xs mx-auto">
              Không có thực thể nào khớp với bộ lọc hiện tại của bạn. Thử thiết lập lại tiêu chí tìm
              kiếm.
            </p>
          </div>
        </div>
      </div>

      <div class="xl:col-span-3 space-y-6">
        <div
          class="bg-white border border-slate-200 rounded-2xl p-6 shadow-[0_8px_30px_rgb(0,0,0,0.02)] space-y-5"
        >
          <div class="flex justify-between items-center">
            <h3 class="text-xs font-bold uppercase tracking-widest text-slate-400">
              Cấu trúc dữ liệu
            </h3>
            <span
              class="w-2 h-2 rounded-full bg-emerald-500 shadow-sm shadow-emerald-500/50"
            ></span>
          </div>

          <div class="grid grid-cols-1 gap-4">
            <div
              class="bg-slate-50 p-4 rounded-xl border border-slate-100 flex justify-between items-center"
            >
              <div>
                <div class="text-[11px] text-slate-400 font-medium">Danh mục nhóm</div>
                <div class="text-xl font-black text-slate-800 mt-0.5">{{ danhMucs.length }}</div>
              </div>
              <span
                class="text-lg bg-white w-8 h-8 rounded-lg flex items-center justify-center border shadow-xs"
                >📁</span
              >
            </div>

            <div
              class="bg-slate-50 p-4 rounded-xl border border-slate-100 flex justify-between items-center"
            >
              <div>
                <div class="text-[11px] text-slate-400 font-medium">Nhà cung ứng / Nhãn hiệu</div>
                <div class="text-xl font-black text-slate-800 mt-0.5">{{ thuongHieus.length }}</div>
              </div>
              <span
                class="text-lg bg-white w-8 h-8 rounded-lg flex items-center justify-center border shadow-xs"
                >🏷️</span
              >
            </div>

            <div
              class="bg-slate-50 p-4 rounded-xl border border-slate-100 flex justify-between items-center"
            >
              <div>
                <div class="text-[11px] text-slate-400 font-medium">Cơ sở chất liệu</div>
                <div class="text-xl font-black text-slate-800 mt-0.5">{{ chatLieus.length }}</div>
              </div>
              <span
                class="text-lg bg-white w-8 h-8 rounded-lg flex items-center justify-center border shadow-xs"
                >🧵</span
              >
            </div>
          </div>
        </div>

        <div
          class="bg-indigo-950 text-indigo-200 rounded-2xl p-6 shadow-xl relative overflow-hidden"
        >
          <div
            class="absolute right-0 bottom-0 translate-x-4 translate-y-4 text-8xl text-indigo-900/40 font-mono font-bold select-none -z-0"
          >
            i
          </div>
          <div class="relative z-10 space-y-2">
            <h4
              class="text-white text-xs font-bold uppercase tracking-wider flex items-center gap-2"
            >
              <span class="w-1.5 h-1.5 rounded-full bg-indigo-400"></span>
              Lưu lưu vận hành
            </h4>
            <p class="text-[11px] text-indigo-300 leading-relaxed">
              Mã định danh SKU là duy nhất. Khi cập nhật hình ảnh thông qua URL, hãy đảm bảo giao
              thức bảo mật mã hóa HTTPS hoạt động bình thường.
            </p>
          </div>
        </div>
      </div>
    </div>

    <div class="fixed bottom-6 right-6 z-[100] flex flex-col gap-2">
      <transition name="toast">
        <div
          v-if="toast.show"
          :class="[
            'px-5 py-3.5 rounded-2xl shadow-2xl text-xs font-bold flex items-center gap-3 backdrop-blur-md border',
            toast.type === 'success'
              ? 'bg-slate-900/95 border-slate-800 text-emerald-400'
              : 'bg-rose-950/95 border-rose-900 text-rose-400',
          ]"
        >
          <span
            :class="[
              'w-1.5 h-1.5 rounded-full',
              toast.type === 'success' ? 'bg-emerald-400' : 'bg-rose-400',
            ]"
          ></span>
          {{ toast.message }}
        </div>
      </transition>
    </div>

    <div v-if="isModalOpen" class="fixed inset-0 z-50 flex items-center justify-center p-4">
      <div class="absolute inset-0 bg-slate-900/40 backdrop-blur-sm" @click="closeModal"></div>
      <div
        class="bg-white rounded-3xl shadow-2xl w-full max-w-md z-10 p-7 border border-slate-100 animate-scale-up"
      >
        <div class="flex items-center justify-between mb-6 pb-3 border-b border-slate-100">
          <h3 class="text-base font-bold text-slate-900 tracking-tight">
            {{ isEditMode ? '⚡ Chi Tiết & Hiệu Chỉnh' : '✨ Thiết Lập Mặt Hàng Mới' }}
          </h3>
          <button
            @click="closeModal"
            class="text-slate-400 hover:text-slate-900 p-1 rounded-lg transition-colors"
          >
            <svg
              xmlns="http://www.w3.org/2000/svg"
              fill="none"
              viewBox="0 0 24 24"
              stroke-width="2.5"
              stroke="currentColor"
              class="w-4 h-4"
            >
              <path stroke-linecap="round" stroke-linejoin="round" d="M6 18L18 6M6 6l12 12" />
            </svg>
          </button>
        </div>

        <form @submit.prevent="handleSubmit" class="space-y-4">
          <div class="space-y-4">
            <div>
              <label
                class="block text-[11px] font-bold text-slate-400 uppercase tracking-wider mb-1.5"
                >Tên gọi sản phẩm *</label
              >
              <input
                v-model="form.tenSanPham"
                placeholder="Nhập tên hàng hóa chi tiết..."
                class="w-full border border-slate-200 px-4 py-2.5 rounded-xl text-sm focus:border-indigo-600 focus:ring-4 focus:ring-indigo-600/5 outline-none transition-all placeholder:text-slate-300"
                required
              />
            </div>

            <div>
              <label
                class="block text-[11px] font-bold text-slate-400 uppercase tracking-wider mb-1.5"
                >Mã định danh SKU *</label
              >
              <input
                v-model="form.maSanPham"
                placeholder="Ví dụ: QUAN01"
                class="w-full border border-slate-200 px-4 py-2.5 rounded-xl text-sm focus:border-indigo-600 focus:ring-4 focus:ring-indigo-600/5 outline-none transition-all uppercase font-mono placeholder:text-slate-300"
                required
              />
            </div>

            <div class="space-y-2 bg-slate-50 p-4 rounded-2xl border border-slate-100">
              <span class="block text-[10px] font-bold text-slate-400 uppercase tracking-wider mb-1"
                >Mạng lưới liên kết dữ liệu</span
              >

              <div class="space-y-2">
                <select
                  v-model.number="form.idDanhMuc"
                  class="w-full border border-slate-200 px-3 py-2.5 rounded-xl text-xs bg-white text-slate-700 focus:border-indigo-600 outline-none shadow-xs"
                >
                  <option :value="null">-- Chỉ định Danh mục --</option>
                  <option v-for="item in danhMucs" :key="item.id" :value="item.id">
                    {{ item.tenDanhMuc }}
                  </option>
                </select>

                <select
                  v-model.number="form.idThuongHieu"
                  class="w-full border border-slate-200 px-3 py-2.5 rounded-xl text-xs bg-white text-slate-700 focus:border-indigo-600 outline-none shadow-xs"
                >
                  <option :value="null">-- Chỉ định Thương hiệu --</option>
                  <option v-for="item in thuongHieus" :key="item.id" :value="item.id">
                    {{ item.tenThuongHieu }}
                  </option>
                </select>

                <select
                  v-model.number="form.idChatLieu"
                  class="w-full border border-slate-200 px-3 py-2.5 rounded-xl text-xs bg-white text-slate-700 focus:border-indigo-600 outline-none shadow-xs"
                >
                  <option :value="null">-- Chỉ định Chất liệu --</option>
                  <option v-for="item in chatLieus" :key="item.id" :value="item.id">
                    {{ item.tenChatLieu }}
                  </option>
                </select>
              </div>
            </div>

            <div>
              <label
                class="block text-[11px] font-bold text-slate-400 uppercase tracking-wider mb-1.5"
                >Trạng thái vận hành</label
              >
              <div class="grid grid-cols-2 gap-2 bg-slate-100 p-1 rounded-xl">
                <button
                  type="button"
                  @click="form.trangThai = true"
                  :class="[
                    'py-2 text-xs font-bold rounded-lg transition-all',
                    form.trangThai
                      ? 'bg-white text-emerald-600 shadow-xs'
                      : 'text-slate-400 hover:text-slate-600',
                  ]"
                >
                  Kinh doanh
                </button>
                <button
                  type="button"
                  @click="form.trangThai = false"
                  :class="[
                    'py-2 text-xs font-bold rounded-lg transition-all',
                    !form.trangThai
                      ? 'bg-white text-slate-600 shadow-xs'
                      : 'text-slate-400 hover:text-slate-600',
                  ]"
                >
                  Tạm ngưng
                </button>
              </div>
            </div>
          </div>

          <div class="flex justify-end gap-2.5 pt-4 border-t border-slate-100 mt-6">
            <button
              type="button"
              @click="closeModal"
              class="px-4 py-2.5 bg-slate-100 hover:bg-slate-200 text-slate-600 rounded-xl text-xs font-bold transition-all"
            >
              Hủy bỏ
            </button>
            <button
              type="submit"
              class="px-5 py-2.5 bg-indigo-600 hover:bg-indigo-700 text-white rounded-xl text-xs font-bold shadow-md shadow-indigo-600/10 transition-all"
            >
              Lưu cấu trúc
            </button>
          </div>
        </form>
      </div>
    </div>

    <div v-if="confirmModal.show" class="fixed inset-0 z-[60] flex items-center justify-center p-4">
      <div class="absolute inset-0 bg-slate-900/30 backdrop-blur-xs" @click="cancelDelete"></div>
      <div
        class="bg-white rounded-2xl shadow-2xl w-full max-w-xs p-5 z-10 animate-scale-up text-center border border-slate-100"
      >
        <div
          class="w-10 h-10 bg-rose-50 text-rose-600 rounded-full flex items-center justify-center mx-auto mb-3 text-lg"
        >
          ⚠️
        </div>
        <h3 class="text-sm font-bold text-slate-900 mb-1">Xóa vĩnh viễn?</h3>
        <p class="text-xs text-slate-400 mb-5 leading-relaxed">
          Hành động này sẽ ngắt kết nối thực thể khỏi hệ thống dữ liệu chính.
        </p>
        <div class="flex gap-2">
          <button
            @click="cancelDelete"
            class="flex-1 py-2.5 bg-slate-100 rounded-xl text-xs font-bold text-slate-600 hover:bg-slate-200 transition-colors"
          >
            Đóng
          </button>
          <button
            @click="confirmDelete"
            class="flex-1 py-2.5 bg-rose-600 text-white rounded-xl text-xs font-bold hover:bg-rose-700 transition-colors shadow-sm"
          >
            Xác nhận
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue' // Thêm computed từ vue
import { getAllSanpham } from '@/service/SanphamService'
import { getAllDanhMuc } from '@/service/DanhMucService'
import { getAllThuongHieu } from '@/service/ThuongHieuService'
import { getAllChatLieu } from '@/service/ChatLieuService'
import { useRouter } from 'vue-router'

const router = useRouter()
const products = ref([]),
  danhMucs = ref([]),
  thuongHieus = ref([]),
  chatLieus = ref([])

const goToSPCT = (id) => {
  router.push(`/sp/${id}`)
}

const isModalOpen = ref(false),
  isEditMode = ref(false)

const toast = reactive({ show: false, message: '', type: 'success' })
const confirmModal = reactive({ show: false, targetId: null })

// Khởi tạo trạng thái cho các bộ lọc đầu vào
const filters = reactive({
  search: '',
  idDanhMuc: null,
  idThuongHieu: null,
  idChatLieu: null,
  trangThai: null,
})

const form = ref({
  id: null,
  idDanhMuc: null,
  idThuongHieu: null,
  idChatLieu: null,
  maSanPham: '',
  tenSanPham: '',
  trangThai: true,
  image: '',
})

// Kiểm tra xem người dùng có đang chọn bất kỳ bộ lọc nào không
const hasActiveFilters = computed(() => {
  return (
    filters.search.trim() !== '' ||
    filters.idDanhMuc !== null ||
    filters.idThuongHieu !== null ||
    filters.idChatLieu !== null ||
    filters.trangThai !== null
  )
})

// Hàm xử lý lọc dữ liệu Client-side mượt mà
const filteredProducts = computed(() => {
  return products.value.filter((product) => {
    const matchesSearch =
      !filters.search.trim() ||
      product.tenSanPham?.toLowerCase().includes(filters.search.toLowerCase()) ||
      product.maSanPham?.toLowerCase().includes(filters.search.toLowerCase())

    const matchesDanhMuc = filters.idDanhMuc === null || product.idDanhMuc?.id === filters.idDanhMuc

    const matchesThuongHieu =
      filters.idThuongHieu === null || product.idThuongHieu?.id === filters.idThuongHieu

    const matchesChatLieu =
      filters.idChatLieu === null || product.idChatLieu?.id === filters.idChatLieu

    const matchesTrangThai = filters.trangThai === null || product.trangThai === filters.trangThai

    return (
      matchesSearch && matchesDanhMuc && matchesThuongHieu && matchesChatLieu && matchesTrangThai
    )
  })
})

// Hàm xóa nhanh toàn bộ bộ lọc về trạng thái ban đầu
const clearFilters = () => {
  filters.search = ''
  filters.idDanhMuc = null
  filters.idThuongHieu = null
  filters.idChatLieu = null
  filters.trangThai = null
}

const showToast = (message, type = 'success') => {
  toast.message = message
  toast.type = type
  toast.show = true
  setTimeout(() => {
    toast.show = false
  }, 3000)
}

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

const loadData = async () => {
  try {
    ;[products.value, danhMucs.value, thuongHieus.value, chatLieus.value] = await Promise.all([
      getAllSanpham(),
      getAllDanhMuc(),
      getAllThuongHieu(),
      getAllChatLieu(),
    ])
  } catch (err) {
    console.error(err)
  }
}

onMounted(loadData)

const getImageUrl = (path) => (path?.startsWith('http') ? path : `http://localhost:8080${path}`)
const openAddModal = () => {
  resetForm()
  isEditMode.value = false
  isModalOpen.value = true
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

const triggerDeleteConfirm = (id) => {
  confirmModal.targetId = id
  confirmModal.show = true
}

const cancelDelete = () => {
  confirmModal.show = false
  confirmModal.targetId = null
}

const confirmDelete = async () => {
  const id = confirmModal.targetId
  if (!id) return
  try {
    await fetch(`http://localhost:8080/sanpham/delete/${id}`, { method: 'DELETE' })
    cancelDelete()
    await loadData()
    showToast('Đã cập nhật thành công!')
  } catch {
    cancelDelete()
    showToast('Lỗi khi xóa!', 'error')
  }
}

const closeModal = () => {
  isModalOpen.value = false
  resetForm()
}

const handleSubmit = async () => {
  if (!form.value.tenSanPham?.trim() || !form.value.maSanPham?.trim())
    return showToast('Vui lòng điền tên & mã SP', 'error')
  try {
    const url = isEditMode.value
      ? `http://localhost:8080/sanpham/update/${form.value.id}`
      : `http://localhost:8080/sanpham`
    const res = await fetch(url, {
      method: isEditMode.value ? 'PUT' : 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(form.value),
    })

    if (!res.ok) throw new Error()
    await loadData()
    closeModal()
    showToast(isEditMode.value ? 'Cập nhật thành công!' : 'Thêm mới thành công!')
  } catch {
    showToast('Có lỗi xảy ra!', 'error')
  }
}
</script>

<style scoped>
@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(6px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
@keyframes scaleUp {
  from {
    opacity: 0;
    transform: scale(0.97);
  }
  to {
    opacity: 1;
    transform: scale(1);
  }
}

.animate-fade-in {
  animation: fadeIn 0.4s cubic-bezier(0.16, 1, 0.3, 1) forwards;
}
.animate-scale-up {
  animation: scaleUp 0.3s cubic-bezier(0.34, 1.56, 0.64, 1) forwards;
}

.toast-enter-active,
.toast-leave-active {
  transition: all 0.3s cubic-bezier(0.34, 1.56, 0.64, 1);
}
.toast-enter-from,
.toast-leave-to {
  opacity: 0;
  transform: translateY(15px);
}
</style>
