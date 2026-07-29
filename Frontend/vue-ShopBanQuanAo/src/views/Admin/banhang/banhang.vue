<template>
  <div class="min-h-screen bg-slate-50 text-slate-800 font-sans antialiased">
    <header class="sticky top-0 z-40 bg-white border-b border-slate-200 shadow-sm">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 h-16 flex items-center justify-between">
        <div class="flex items-center gap-2 overflow-x-auto no-scrollbar">
          <template v-if="allOrders.length > 0">
            <div
              v-for="(order, index) in allOrders.filter((o) => o.id)"
              :key="order.id"
              @click="switchOrder(index)"
              :class="[
                'group flex items-center gap-2 px-4 py-1.5 rounded-t-xl border-t-2 transition-all cursor-pointer text-xs font-bold',
                currentOrderIndex === index
                  ? 'bg-white border-indigo-500 text-indigo-600'
                  : 'bg-slate-50 border-transparent text-slate-500 hover:bg-slate-100',
              ]"
            >
              <span>HĐ #{{ order.maHoaDon }}</span>
              <button
                @click.stop="removeOrder(index)"
                class="ml-1 flex items-center justify-center w-5 h-5 rounded-full bg-slate-200 text-slate-600 hover:bg-rose-500 hover:text-white transition-all"
              >
                ×
              </button>
            </div>
          </template>

          <button
            @click="createNewOrder"
            class="ml-2 w-8 h-8 flex items-center justify-center rounded-full bg-slate-100 hover:bg-indigo-600 hover:text-white transition-all text-slate-400"
            title="Tạo hóa đơn mới"
          >
            +
          </button>
        </div>

        <div class="flex-1 max-w-xs hidden md:block ml-8">
          <div class="relative">
            <input
              ref="searchInput"
              v-model="searchQuery"
              @focus="openDropdown"
              @blur="closeDropdown"
              placeholder="Tìm sản phẩm..."
              class="w-full pl-9 pr-4 py-2 bg-slate-50 border border-slate-200 rounded-xl text-xs focus:ring-2 focus:ring-indigo-500"
            />

            <div
              v-if="isDropdownVisible && filteredProducts.length > 0"
              class="absolute top-10 left-0 w-full bg-white border border-slate-200 shadow-xl rounded-lg z-[60] max-h-80 overflow-y-auto"
            >
              <div
                v-for="sp in filteredProducts.slice(0, 7)"
                :key="sp.id"
                @click="addToCart(sp)"
                class="flex items-center gap-2 p-2 hover:bg-indigo-50 cursor-pointer border-b"
              >
                <img :src="getProductImage(sp)" class="w-8 h-8 rounded object-cover" />
                <div class="flex-1 truncate text-xs">
                  <p class="font-bold">{{ sp.tenSanPham }}</p>
                  <p class="text-indigo-600">{{ formatPrice(sp.giaBan) }}</p>
                </div>
              </div>
            </div>

            <span class="absolute left-3 top-1/2 -translate-y-1/2 text-slate-400">🔍</span>
          </div>
        </div>
      </div>
    </header>

    <div
      v-if="isLoading"
      class="flex justify-center items-center py-20 text-slate-500 font-medium text-sm"
    >
      <span class="animate-pulse">🔄 Đang tải dữ liệu sản phẩm từ hệ thống...</span>
    </div>

    <main
      v-else
      class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-6 grid grid-cols-1 lg:grid-cols-12 gap-6"
    >
      <section class="lg:col-span-7 xl:col-span-8 flex flex-col space-y-6">
        <div
          class="bg-white p-4 rounded-2xl border border-slate-200 shadow-sm flex flex-wrap gap-3 items-center mb-4 flex-shrink-0"
        >
          <select
            v-model="filterCategory"
            class="text-xs font-medium bg-slate-50 border border-slate-200 rounded-lg px-3 py-2 text-slate-600 focus:outline-none focus:ring-2 focus:ring-indigo-500"
          >
            <option value="">Tất cả Danh mục</option>
            <option v-for="dm in danhMucList" :key="dm.id" :value="dm.id">
              {{ dm.tenDanhMuc }}
            </option>
          </select>

          <select
            v-model="filterBrand"
            class="text-xs font-medium bg-slate-50 border border-slate-200 rounded-lg px-3 py-2 text-slate-600 focus:outline-none focus:ring-2 focus:ring-indigo-500"
          >
            <option value="">Tất cả Thương hiệu</option>
            <option v-for="th in thuongHieuList" :key="th.id" :value="th.id">
              {{ th.tenThuongHieu }}
            </option>
          </select>

          <select
            v-model="filterColor"
            class="text-xs font-medium bg-slate-50 border border-slate-200 rounded-lg px-3 py-2 text-slate-600 focus:outline-none focus:ring-2 focus:ring-indigo-500"
          >
            <option value="">Màu sắc</option>
            <option v-for="ms in mauSacList" :key="ms.id" :value="ms.id">{{ ms.tenMauSac }}</option>
          </select>

          <select
            v-model="filterSize"
            class="text-xs font-medium bg-slate-50 border border-slate-200 rounded-lg px-3 py-2 text-slate-600 focus:outline-none focus:ring-2 focus:ring-indigo-500"
          >
            <option value="">Kích thước</option>
            <option v-for="kt in kichThuocList" :key="kt.id" :value="kt.id">
              {{ kt.tenKichThuoc }}
            </option>
          </select>

          <button
            @click="resetFilters"
            class="text-xs font-semibold text-rose-600 hover:text-rose-700 ml-auto transition-colors"
          >
            Xóa bộ lọc
          </button>
        </div>

        <div class="grid grid-cols-2 sm:grid-cols-3 xl:grid-cols-4 gap-4">
          <div
            v-for="sp in sortedProducts"
            :key="sp.id"
            @click="(sp.soLuongKhaDung ?? sp.soLuongTon) > 0 && sp.trangThai ? addToCart(sp) : null"
            :class="[
              'bg-white rounded-2xl border p-3 flex flex-col justify-between transition-all duration-200 group relative overflow-hidden select-none h-full',
              (sp.soLuongKhaDung ?? sp.soLuongTon) > 0 && sp.trangThai
                ? 'border-slate-200 hover:border-indigo-400 hover:shadow-md cursor-pointer'
                : 'border-slate-200 bg-slate-50/80 opacity-60 cursor-not-allowed',
            ]"
          >
            <!-- Tag Giảm Giá -->
            <div v-if="sp.dangGiamGia" class="absolute top-2 right-2 z-20">
              <span
                class="bg-gradient-to-r from-amber-500 to-rose-500 text-white text-[10px] font-black px-2 py-0.5 rounded-full shadow-sm"
              >
                -{{ sp.phanTramGiam }}%
              </span>
            </div>

            <!-- Khung Ảnh Sản Phẩm -->
            <div
              class="aspect-square w-full rounded-xl bg-slate-100 overflow-hidden relative mb-2.5"
            >
              <img
                :src="getProductImage(sp)"
                :alt="sp.tenSanPham"
                class="w-full h-full object-cover group-hover:scale-105 transition-transform duration-300"
                @error="setDefaultImage"
              />

              <!-- Overlay khi hết hàng / ngưng bán -->
              <div
                v-if="!sp.trangThai || (sp.soLuongKhaDung ?? sp.soLuongTon) <= 0"
                class="absolute inset-0 bg-slate-900/40 backdrop-blur-[1px] flex items-center justify-center z-20"
              >
                <span
                  class="bg-white/90 text-rose-600 text-[11px] font-black px-2.5 py-1 rounded-lg shadow-sm"
                >
                  {{ !sp.trangThai ? 'NGỪNG BÁN' : 'HẾT HÀNG' }}
                </span>
              </div>

              <!-- Nút Thêm Nhanh trên Hover -->
              <div
                v-if="(sp.soLuongKhaDung ?? sp.soLuongTon) > 0 && sp.trangThai"
                class="absolute inset-0 bg-indigo-900/10 opacity-0 group-hover:opacity-100 transition-opacity flex items-center justify-center z-10"
              >
                <span
                  class="bg-indigo-600 text-white p-2 rounded-xl shadow-lg transform translate-y-2 group-hover:translate-y-0 transition-transform"
                >
                  <svg
                    xmlns="http://www.w3.org/2000/svg"
                    class="h-4 w-4"
                    fill="none"
                    viewBox="0 0 24 24"
                    stroke="currentColor"
                  >
                    <path
                      stroke-linecap="round"
                      stroke-linejoin="round"
                      stroke-width="2.5"
                      d="M12 4v16m8-8H4"
                    />
                  </svg>
                </span>
              </div>
            </div>

            <!-- Thông Tin Sản Phẩm -->
            <div class="flex-1 flex flex-col justify-between">
              <div>
                <!-- Mã & Thương hiệu -->
                <div class="flex items-center justify-between gap-1 mb-1">
                  <span
                    class="text-[9px] font-mono font-bold text-slate-400 uppercase tracking-tight truncate"
                  >
                    #{{ sp.maSanPhamChiTiet }}
                  </span>
                  <span
                    class="text-[9px] font-bold bg-slate-100 text-slate-600 px-1.5 py-0.5 rounded"
                  >
                    {{ sp.tenThuongHieu }}
                  </span>
                </div>

                <!-- Tên sản phẩm -->
                <h3
                  class="text-xs font-bold text-slate-800 line-clamp-2 leading-snug group-hover:text-indigo-600 transition-colors"
                  :title="sp.tenSanPham"
                >
                  {{ sp.tenSanPham }}
                </h3>

                <!-- Thuộc tính (Màu, Size) -->
                <div class="flex gap-1 mt-1.5 flex-wrap">
                  <span
                    class="text-[9px] font-medium bg-slate-50 text-slate-600 border border-slate-100 px-1.5 py-0.5 rounded-md"
                  >
                    🎨 {{ sp.tenMauSac }}
                  </span>
                  <span
                    class="text-[9px] font-medium bg-slate-50 text-slate-600 border border-slate-100 px-1.5 py-0.5 rounded-md"
                  >
                    📏 Size {{ sp.tenKichThuoc }}
                  </span>
                </div>
              </div>

              <!-- Giá & Số lượng khả dụng -->
              <div class="mt-3 pt-2 border-t border-slate-100 flex items-end justify-between gap-1">
                <div>
                  <template v-if="sp.dangGiamGia">
                    <p
                      class="text-[10px] text-slate-400 line-through font-medium leading-none mb-0.5"
                    >
                      {{ formatPrice(sp.giaBan) }}
                    </p>
                    <p class="text-sm font-black text-rose-600 leading-none">
                      {{ formatPrice(sp.giaSauGiam) }}
                    </p>
                  </template>

                  <template v-else>
                    <p class="text-sm font-black text-indigo-600 leading-none">
                      {{ formatPrice(sp.giaBan) }}
                    </p>
                  </template>
                </div>

                <!-- Tag Số lượng khả dụng -->
                <span
                  :class="[
                    'text-[9px] px-1.5 py-0.5 rounded-md font-bold flex-shrink-0',
                    (sp.soLuongKhaDung ?? sp.soLuongTon) <= 0 || !sp.trangThai
                      ? 'bg-rose-50 text-rose-600'
                      : (sp.soLuongKhaDung ?? sp.soLuongTon) <= 10
                        ? 'bg-amber-50 text-amber-600'
                        : 'bg-emerald-50 text-emerald-600',
                  ]"
                >
                  {{
                    (sp.soLuongKhaDung ?? sp.soLuongTon) <= 0
                      ? 'Hết hàng'
                      : 'Khả dụng: ' + (sp.soLuongKhaDung ?? sp.soLuongTon)
                  }}
                </span>
              </div>
            </div>
          </div>
        </div>
      </section>

      <section class="lg:col-span-5 xl:col-span-4 flex flex-col space-y-4">
        <div
          class="bg-white rounded-3xl border border-slate-200 shadow-xl flex flex-col h-[78vh] sticky top-20 overflow-hidden"
        >
          <div class="px-3 py-2 border-b border-slate-100 bg-slate-50/70">
            <div class="flex items-center justify-between mb-2">
              <label class="text-xs font-bold text-slate-500 uppercase tracking-wider"
                >Khách hàng</label
              >
              <button
                @click="openCustomerModal"
                class="text-xs font-bold text-indigo-600 hover:underline flex items-center gap-1"
              >
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  class="h-3 w-3"
                  viewBox="0 0 20 20"
                  fill="currentColor"
                >
                  <path
                    d="M8 9a3 3 0 100-6 3 3 0 000 6zM8 11a6 6 0 016 6H2a6 6 0 016-6zM16 7a1 1 0 10-2 0v1h-1a1 1 0 100 2h1v1a1 1 0 102 0v-1h1a1 1 0 100-2h-1V7z"
                  />
                </svg>
                Chọn / Thêm mới
              </button>
            </div>

            <div
              class="bg-white border border-slate-200 rounded-xl px-3 py-2 flex items-center justify-between shadow-sm"
            >
              <div v-if="selectedCustomer">
                <p class="text-sm font-bold text-slate-800">{{ selectedCustomer.hoTen }}</p>
                <p class="text-xs text-slate-500 font-medium">
                  {{ selectedCustomer.soDienThoai }}
                </p>
              </div>
              <div v-else>
                <p class="text-sm font-semibold text-slate-400">Khách lẻ</p>
              </div>
              <button
                v-if="selectedCustomer"
                @click="selectedCustomer = null"
                class="text-slate-400 hover:text-rose-500"
              >
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  class="h-4 w-4"
                  fill="none"
                  viewBox="0 0 24 24"
                  stroke="currentColor"
                >
                  <path
                    stroke-linecap="round"
                    stroke-linejoin="round"
                    stroke-width="2"
                    d="M6 18L18 6M6 6l12 12"
                  />
                </svg>
              </button>
            </div>
          </div>

          <div class="flex-1 overflow-y-auto px-3 py-2 space-y-2 custom-scrollbar">
            <div
              v-if="currentOrder?.cart?.length === 0"
              class="h-full flex flex-col items-center justify-center text-slate-400 py-12"
            >
              <svg
                xmlns="http://www.w3.org/2000/svg"
                class="h-12 w-12 text-slate-300 mb-2"
                fill="none"
                viewBox="0 0 24 24"
                stroke="currentColor"
              >
                <path
                  stroke-linecap="round"
                  stroke-linejoin="round"
                  stroke-width="2"
                  d="M3 3h2l.4 2M7 13h10l4-8H5.4M7 13L5.4 5M7 13l-2.293 2.293c-.63.63-.184 1.707.707 1.707H17m0 0a2 2 0 100 4 2 2 0 000-4zm-8 0a2 2 0 11-4 0 2 2 0 014 0z"
                />
              </svg>
              <p class="text-xs font-semibold">Giỏ hàng trống. Click sản phẩm để thêm.</p>
            </div>

            <div
              v-for="(item, index) in currentOrder?.cart || []"
              :key="item.id"
              class="flex items-center gap-3 bg-white p-2.5 rounded-xl border border-slate-100 hover:shadow-sm transition-shadow"
            >
              <img
                :src="getProductImage(item.product)"
                class="w-10 h-10 rounded-lg object-cover bg-slate-50 flex-shrink-0"
                @error="setDefaultImage"
              />
              <div class="flex-1 min-w-0">
                <h4 class="text-xs font-bold text-slate-800 truncate">
                  {{ item.product.tenSanPhamChiTiet }}
                </h4>

                <div class="flex items-center gap-2 mt-0.5 min-w-0">
                  <span
                    class="text-[10px] font-bold text-indigo-700 bg-indigo-50 px-1.5 py-0.5 rounded truncate max-w-[90px]"
                    :title="item.product.maSanPhamChiTiet"
                  >
                    {{ item.product.maSPCT }}
                  </span>

                  <span class="text-[10px] text-slate-500 truncate">
                    {{ item.product.tenMauSac }} / {{ item.product.tenKichThuoc }}
                  </span>
                </div>

                <p class="text-xs font-black text-indigo-600 mt-1">
                  {{ formatPrice(item.product.giaBan) }}
                </p>
              </div>

              <div class="flex items-center bg-slate-100 rounded-lg p-1">
                <!-- Nút Giảm (-) -->
                <button
                  @click="decreaseQty(item)"
                  :disabled="item.soLuong <= 1"
                  class="w-5 h-5 flex items-center justify-center text-slate-600 hover:bg-white rounded transition-colors text-xs font-bold disabled:opacity-40 disabled:cursor-not-allowed select-none"
                >
                  -
                </button>

                <!-- Ô Input Nhập Số Lượng -->
                <input
                  type="number"
                  min="1"
                  v-model.number="item.soLuong"
                  @input="debounceChangeQty(item)"
                  class="w-10 h-6 text-center text-xs font-bold text-slate-800 bg-white rounded border border-slate-200 outline-none [appearance:textfield] [&::-webkit-outer-spin-button]:appearance-none [&::-webkit-inner-spin-button]:appearance-none"
                />

                <!-- Nút Tăng (+) -->
                <button
                  @click="increaseQty(item)"
                  class="w-5 h-5 flex items-center justify-center text-slate-600 hover:bg-white rounded transition-colors text-xs font-bold select-none"
                >
                  +
                </button>
              </div>

              <button
                @click="removeFromCart(index)"
                class="text-slate-300 hover:text-rose-500 transition-colors"
              >
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  class="h-4 w-4"
                  fill="none"
                  viewBox="0 0 24 24"
                  stroke="currentColor"
                >
                  <path
                    stroke-linecap="round"
                    stroke-linejoin="round"
                    stroke-width="2"
                    d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16"
                  />
                </svg>
              </button>
            </div>
          </div>

          <div class="border-t border-slate-100 p-3 bg-slate-50 space-y-3">
            <div class="mt-1 flex gap-2 items-end">
              <div class="relative flex-1">
                <label
                  class="text-[10px] font-bold text-slate-400 uppercase tracking-wider block mb-1"
                >
                  Voucher Khuyến Mãi
                </label>

                <div class="relative flex-1" ref="voucherRef">
                  <!-- Main Dropdown Trigger -->
                  <div class="relative">
                    <div
                      class="w-full bg-white border border-slate-200 hover:border-indigo-300 rounded-xl px-4 py-2.5 text-xs font-bold text-slate-700 flex justify-between items-center transition-all shadow-sm"
                    >
                      <!-- Phần click mở dropdown -->
                      <div
                        @click="showVoucherDropdown = !showVoucherDropdown"
                        class="flex-1 cursor-pointer flex justify-between items-center pr-2"
                      >
                        <div v-if="selectedVoucher || appliedVoucher" class="flex flex-col gap-0.5">
                          <span class="font-bold text-indigo-600 flex items-center gap-1.5">
                            <span class="text-sm">🎟️</span>
                            {{
                              (selectedVoucher || appliedVoucher).loaiGiamGia === 'phan_tram'
                                ? `Giảm ${(selectedVoucher || appliedVoucher).giaTriGiam}% (${(selectedVoucher || appliedVoucher).maVoucher})`
                                : `Giảm ${formatPrice((selectedVoucher || appliedVoucher).giaTriGiam)} (${(selectedVoucher || appliedVoucher).maVoucher})`
                            }}
                          </span>
                          <span class="text-[10px] text-slate-500 font-medium">
                            Tiết kiệm khoảng
                            <span class="text-emerald-600 font-bold">{{
                              formatPrice(tinhTienGiam(selectedVoucher || appliedVoucher))
                            }}</span>
                          </span>
                        </div>

                        <span v-else class="text-slate-400 font-normal"
                          >Chọn hoặc nhập mã khuyến mãi</span
                        >
                      </div>

                      <!-- Nút Xóa nhanh hoặc Mũi tên Dropdown -->
                      <div class="flex items-center gap-2 pl-2 border-l border-slate-100">
                        <button
                          v-if="selectedVoucher || appliedVoucher"
                          @click.stop="removeVoucher"
                          class="text-slate-400 hover:text-rose-600 transition-colors p-1 rounded-lg hover:bg-rose-50"
                          title="Xóa voucher"
                        >
                          ✕
                        </button>

                        <span
                          @click="showVoucherDropdown = !showVoucherDropdown"
                          class="text-slate-400 text-xs cursor-pointer px-1 transition-transform duration-200"
                        >
                          {{ showVoucherDropdown ? '▲' : '▼' }}
                        </span>
                      </div>
                    </div>
                  </div>
                </div>

                <!-- Dropdown List -->
                <div
                  v-if="showVoucherDropdown && filteredVouchers.length > 0"
                  class="absolute z-[100] w-full bg-white border border-slate-200 rounded-2xl shadow-xl mt-1.5 max-h-72 overflow-y-auto custom-scrollbar p-1.5 space-y-1.5"
                >
                  <div
                    v-for="vc in filteredVouchers"
                    :key="vc.id"
                    @mousedown.prevent="isVoucherValid(vc) ? selectVoucher(vc) : null"
                    :class="[
                      'relative p-3 rounded-xl border transition-all flex flex-col gap-2',
                      isVoucherValid(vc)
                        ? 'cursor-pointer bg-white hover:border-indigo-300 hover:bg-indigo-50/40'
                        : 'opacity-60 bg-slate-50 border-slate-200 cursor-not-allowed',
                      appliedVoucher?.id === vc.id || selectedVoucher?.id === vc.id
                        ? 'border-indigo-500 bg-indigo-50/60 shadow-sm'
                        : 'border-slate-100',
                    ]"
                  >
                    <!-- Badge Đề xuất tốt nhất -->
                    <div v-if="bestVoucher?.id === vc.id" class="flex items-center gap-1.5">
                      <span
                        class="bg-amber-100 text-amber-800 text-[10px] px-2 py-0.5 rounded-md font-bold flex items-center gap-1 shadow-2xs"
                      >
                        ⭐ Đề xuất tốt nhất
                      </span>
                    </div>

                    <!-- Header Card: Mã & Loại giảm giá -->
                    <div class="flex justify-between items-center text-[11px]">
                      <span class="font-bold text-slate-500">
                        Mã:
                        <span class="text-slate-900 bg-slate-100 px-1.5 py-0.5 rounded">{{
                          vc.maVoucher || vc.id
                        }}</span>
                      </span>
                      <span
                        class="font-bold text-indigo-600 bg-indigo-50 px-2 py-0.5 rounded-full uppercase text-[10px]"
                      >
                        {{ vc.loaiGiamGia === 'phan_tram' ? 'Giảm theo %' : 'Giảm tiền mặt' }}
                      </span>
                    </div>

                    <!-- Thông tin chi tiết giá trị -->
                    <div
                      class="grid grid-cols-2 gap-x-2 gap-y-1 text-[11px] text-slate-600 bg-slate-50/70 p-2 rounded-lg"
                    >
                      <div>
                        Đơn tối thiểu:
                        <span class="font-bold text-slate-800">{{
                          formatPrice(vc.giaTriDonHangToiThieu)
                        }}</span>
                      </div>
                      <div>
                        Mức giảm:
                        <span class="font-bold text-rose-600">
                          {{
                            vc.loaiGiamGia === 'tien_mat'
                              ? formatPrice(vc.giaTriGiam)
                              : vc.giaTriGiam + '%'
                          }}
                        </span>
                      </div>
                      <div class="col-span-2 pt-0.5 border-t border-slate-200/60 mt-0.5">
                        Giảm tối đa:
                        <span class="font-bold text-slate-800">
                          {{
                            vc.giaTriGiamToiDa ? formatPrice(vc.giaTriGiamToiDa) : 'Không giới hạn'
                          }}
                        </span>
                      </div>
                    </div>

                    <!-- Trạng thái lỗi nếu không hợp lệ -->
                    <div
                      v-if="!isVoucherValid(vc)"
                      class="text-[10px] text-rose-600 font-semibold flex items-center gap-1 justify-end pt-0.5"
                    >
                      <span>🔒</span> {{ getVoucherError(vc) }}
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <div class="space-y-1.5 text-xs font-medium text-slate-600">
              <div class="flex justify-between">
                <span>Tổng tiền hàng</span>
                <span class="text-slate-800 font-semibold">{{ formatPrice(totalCartPrice) }}</span>
              </div>

              <div class="flex justify-between text-rose-600">
                <span>Giảm giá Voucher</span>

                <span v-if="appliedVoucher">
                  - {{ formatPrice(voucherDiscount) }}
                  <span class="text-slate-400 text-xs">{{ voucherLabel }}</span>
                </span>
                <span v-else>- 0đ</span>
              </div>
              <div
                class="bg-gradient-to-r from-indigo-50 to-blue-50 rounded-2xl border border-indigo-100 p-3"
              >
                <div class="flex justify-between text-sm">
                  <span class="font-bold text-slate-700"> Tổng thanh toán </span>

                  <span class="text-2xl font-black text-indigo-600">
                    {{
                      formatPrice(
                        totalCartPrice - voucherDiscount < 0 ? 0 : totalCartPrice - voucherDiscount,
                      )
                    }}
                  </span>
                </div>
              </div>
            </div>

            <div class="grid grid-cols-2 gap-2 pt-1">
              <div>
                <label class="text-[10px] font-bold text-slate-400 block mb-1">
                  LOẠI HÓA ĐƠN
                </label>

                <select
                  v-model="loaiHoaDon"
                  disabled
                  class="w-full bg-slate-100 border border-slate-200 rounded-xl p-2 text-xs font-bold text-slate-700 cursor-not-allowed"
                >
                  <option value="tai_quay">Tại quầy (POS)</option>
                </select>
              </div>
              <div>
                <label class="text-[10px] font-bold text-slate-400 block mb-1"
                  >PHƯƠNG THỨC TT</label
                >
                <select
                  v-model="phuongThucThanhToan"
                  @change="handlePaymentMethod"
                  class="w-full bg-white border border-slate-200 rounded-xl p-2 text-xs font-bold text-slate-700 focus:outline-none"
                >
                  <option v-for="pt in ptttList" :key="pt.id" :value="pt.id">
                    {{ pt.tenPhuongThuc }}
                  </option>
                </select>
              </div>
            </div>

            <button
              @click="submitCheckout"
              class="w-full bg-indigo-600 hover:bg-indigo-700 text-white font-bold text-sm py-2.5 px-4 rounded-xl shadow-lg shadow-indigo-100 hover:shadow-xl active:scale-[0.99] transition-all flex items-center justify-center gap-2"
            >
              XUẤT HÓA ĐƠN & THANH TOÁN
            </button>
          </div>
        </div>
      </section>
    </main>

    <div
      v-if="showCustomerModal"
      class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4 z-50"
    >
      <div class="bg-white rounded-3xl max-w-md w-full p-6 shadow-2xl">
        <div class="flex justify-between items-center mb-4">
          <h3 class="text-base font-bold text-slate-900">Chọn Khách Hàng Từ Hệ Thống</h3>
          <button @click="showCustomerModal = false" class="text-slate-400 hover:text-slate-600">
            ✕
          </button>
        </div>
        <input
          type="text"
          v-model="searchCustomerQuery"
          placeholder="Tìm tên hoặc số điện thoại..."
          class="w-full bg-slate-50 border border-slate-200 rounded-xl px-3 py-2 text-sm mb-3 focus:outline-none focus:ring-2 focus:ring-indigo-500"
        />

        <div class="max-h-60 overflow-y-auto space-y-2 mb-4">
          <div class="bg-slate-50 p-3 rounded-xl border border-dashed border-slate-300 mb-4">
            <p class="text-[10px] font-bold text-slate-500 uppercase mb-2">Thêm khách mới</p>
            <input
              v-model="newCust.hoTen"
              placeholder="Tên khách hàng"
              class="w-full p-2 mb-2 rounded border text-xs"
            />
            <input
              v-model="newCust.sdt"
              placeholder="Số điện thoại"
              class="w-full p-2 rounded border text-xs"
            />
            <button
              @click="saveNewCustomer"
              class="w-full mt-2 bg-indigo-600 text-white py-1.5 rounded text-xs font-bold"
            >
              Lưu khách hàng
            </button>
          </div>
          <div
            v-for="kh in filteredCustomers"
            :key="kh.id"
            @click="selectCustomer(kh)"
            class="p-3 border border-slate-100 rounded-xl hover:bg-indigo-50 cursor-pointer flex justify-between items-center transition-colors"
          >
            <div>
              <p class="text-xs font-bold text-slate-800">{{ kh.hoTen }}</p>
              <p class="text-[11px] text-slate-500 font-medium">{{ kh.soDienThoai }}</p>
            </div>
            <span class="text-[10px] font-bold px-2 py-0.5 rounded bg-amber-100 text-amber-800">
              {{ kh.hangThanhVien || 'Thành viên' }}
            </span>
          </div>
        </div>
      </div>
    </div>

    <InvoiceModal v-if="showInvoiceModal" :hoaDon="hoaDonPrint" @close="handleCloseInvoice" />
    <PaymentQrDialog
      v-model:visible="showQrDialog"
      :qr-url="qrData.qrUrl"
      :ma-hoa-don="qrData.maHoaDon"
      :tong-tien="qrData.tongTien"
      @paid="xacNhanDaThanhToan"
    />
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch, onUnmounted } from 'vue'
import { useToast } from 'vue-toastification'
import InvoiceModal from './InvoiceModal.vue'
import Swal from 'sweetalert2'
import stompClient from '@/socket'
import { onBeforeUnmount } from 'vue'
// --- 1. IMPORT CÁC SERVICE API ---
import { getAllDanhMuc } from '@/service/DanhMucService'
import { getAllKichThuoc } from '@/service/KichThuocService'
import { getAllMauSac } from '@/service/MauSacService'
import { getAllThuongHieu } from '@/service/ThuongHieuService'
import { getAllSanPhamChiTiet } from '@/service/SanPhamChiTiet'
import { getAllPTTT } from '@/service/PhuongThucThanhToanService'
import { getALLKhachHang, addKhachHangQuickService } from '@/service/KhachHangService'
import { getAllVoucher } from '@/service/VoucherService'
import {
  getHoadonById,
  getHoaDonCho,
  taoHoaDonCho,
  themSanPhamVaoHoaDon,
  giamSoLuongSanPham,
  getChiTietHoaDon,
  xoaSanPhamKhoiHoaDon,
  tangSoLuongSanPham,
  ganKhachHang,
  thanhToanHoaDon,
  huyHoaDon,
  apVoucher,
  boVoucher,
  taoQr,
  capNhatSoLuong,
} from '@/service/HoaDonService'
import { ElNotification } from 'element-plus'
import PaymentQrDialog from '@/views/shop/components/PaymentQrDialog.vue'

import { debounce } from 'lodash-es'

// --- 2. KHỞI TẠO BIẾN CƠ BẢN ---
const toast = useToast()
const API_KHACH_HANG = 'http://localhost:8080/khachhang'
const DEFAULT_PRODUCT_IMAGE = 'https://images.unsplash.com/photo-1523381210434-271e8be1f52b?w=400'

// --- 3. TRẠNG THÁI DANH SÁCH (DATA TỪ API) ---
const dsHoaDon = ref([])
const danhMucList = ref([])
const thuongHieuList = ref([])
const mauSacList = ref([])
const kichThuocList = ref([])
const products = ref([])
const customers = ref([])

const vouchers = ref([])
const ptttList = ref([])
const isLoading = ref(true)

const oldQty = ref(1)

const voucherRef = ref(null)

// --- 4. TRẠNG THÁI FORM & GIỎ HÀNG ---
const newCust = ref({ hoTen: '', sdt: '' })
const hoaDonPrint = ref(null)
const showInvoiceModal = ref(false)
const showCustomerModal = ref(false)
const showVoucherDropdown = ref(false)
const isDropdownVisible = ref(false)

const searchQuery = ref('')
const filterCategory = ref('')
const filterBrand = ref('')
const filterColor = ref('')
const filterSize = ref('')
const voucherCode = ref('')
const searchCustomerQuery = ref('')
const searchInput = ref(null)

import { reactive } from 'vue'

const editingQty = reactive({})
const debounceMap = {}

const syncEditingQty = () => {
  currentOrder.value?.cart?.forEach((item) => {
    editingQty[item.id] = item.soLuong
  })
}

//QR
const qrUrl = ref('')
const showQrDialog = ref(false)
const qrData = ref({
  qrUrl: '',
  maHoaDon: '',
  tongTien: 0,
})

const handlePaymentMethod = async () => {
  const pttt = ptttList.value.find((p) => p.id === phuongThucThanhToan.value)

  console.log(pttt)

  if (!pttt) return
  if (pttt.maPhuongThuc !== 'BANK') {
    return
  }

  const data = await taoQr(currentOrder.value.id)

  console.log(data)

  qrData.value = data

  showQrDialog.value = true
}

const xacNhanDaThanhToan = async () => {
  await thanhToanHoaDon({
    idHoaDon: currentOrder.id,
    method: 'BANK',
  })

  showQrDialog.value = false

  toast.success('Thanh toán thành công')
}

const handleKeyDown = async (e) => {
  if (e.ctrlKey && e.key.toLowerCase() === 'f') {
    e.preventDefault() // chặn trình duyệt mở Find

    searchInput.value?.focus()

    // chọn toàn bộ nội dung cũ nếu có
    searchInput.value?.select()
  }
  // F1 => tạo hóa đơn mới
  if (e.key === 'F1') {
    e.preventDefault() // chặn mở Help của trình duyệt
    createNewOrder()
  }

  // Ctrl + D => hủy hóa đơn hiện tại
  if (e.ctrlKey && e.key.toLowerCase() === 'd') {
    e.preventDefault()

    if (!currentOrder.value?.id) return

    const result = await Swal.fire({
      title: 'Hủy hóa đơn?',
      html: `
      <div style="font-size:14px;color:#64748b">
        Hóa đơn hiện tại sẽ bị <b style="color:#dc2626">xóa vĩnh viễn</b>.<br>
        Hành động này không thể hoàn tác.
      </div>
    `,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: '🗑️ Hủy hóa đơn',
      cancelButtonText: 'Tiếp tục bán',
      reverseButtons: true,
      focusCancel: true,
      confirmButtonColor: '#ef4444',
      cancelButtonColor: '#334155',
      background: '#fff',
      color: '#0f172a',
      width: 430,
      customClass: {
        popup: 'rounded-3xl shadow-2xl',
        title: 'text-xl font-bold',
        confirmButton: 'px-5 py-3 rounded-2xl',
        cancelButton: 'px-5 py-3 rounded-2xl',
      },
    })

    if (result.isConfirmed) {
      await removeOrder(currentOrderIndex.value)

      Swal.fire({
        toast: true,
        position: 'top-end',
        icon: 'success',
        title: 'Đã hủy hóa đơn',
        showConfirmButton: false,
        timer: 1500,
        timerProgressBar: true,
      })
    }
  }
}
const handleClickOutside = (e) => {
  if (voucherRef.value && !voucherRef.value.contains(e.target)) {
    showVoucherDropdown.value = false
  }
}

onMounted(() => {
  document.addEventListener('click', handleClickOutside)
})

onBeforeUnmount(() => {
  document.removeEventListener('click', handleClickOutside)
})

onMounted(() => {
  window.addEventListener('keydown', handleKeyDown)
})

onUnmounted(() => {
  window.removeEventListener('keydown', handleKeyDown)
})

const allOrders = ref([
  {
    cart: [],
    selectedCustomer: null,
    appliedVoucher: null,
    voucherQuery: '',
    loaiHoaDon: 'tai_quay',
    phuongThucThanhToan: '',
  },
])

const currentOrderIndex = ref(0)

const selectedVoucher = ref(null)

// --- 5. COMPUTED PROPERTIES (LẤY DỮ LIỆU TỪ ORDER HIỆN TẠI) ---
const currentOrder = computed(() => {
  // Nếu không có hóa đơn hoặc index không hợp lệ, trả về một object rỗng có cart là []
  if (allOrders.value.length === 0 || currentOrderIndex.value < 0) {
    return { cart: [] }
  }
  return allOrders.value[currentOrderIndex.value] || { cart: [] }
})

const selectedCustomer = computed({
  get: () => currentOrder.value?.selectedCustomer || null,
  set: (v) => {
    if (currentOrder.value) currentOrder.value.selectedCustomer = v
  },
})

const appliedVoucher = computed({
  get: () => currentOrder.value?.appliedVoucher || null,
  set: (v) => {
    if (currentOrder.value) currentOrder.value.appliedVoucher = v
  },
})

const voucherQuery = computed({
  get: () => currentOrder.value?.voucherQuery || '',
  set: (v) => {
    if (currentOrder.value) currentOrder.value.voucherQuery = v
  },
})

const loaiHoaDon = computed({
  get: () => currentOrder.value?.loaiHoaDon || 'tai_quay',
  set: (v) => {
    if (currentOrder.value) currentOrder.value.loaiHoaDon = v
  },
})

const phuongThucThanhToan = computed({
  get: () => currentOrder.value?.phuongThucThanhToan || '',
  set: (v) => {
    if (currentOrder.value) currentOrder.value.phuongThucThanhToan = v
  },
})

const hasCurrentOrder = computed(() => !!currentOrder.value?.id)

// --- 6. HÀM TẢI & KHỞI TẠO DỮ LIỆU ---
const loadAllDataFromAPI = async () => {
  try {
    isLoading.value = true
    const [resDM, resKT, resMS, resTH, resSPCT, resKH, resVoucher] = await Promise.all([
      getAllDanhMuc(),
      getAllKichThuoc(),
      getAllMauSac(),
      getAllThuongHieu(),
      getAllSanPhamChiTiet(),
      getALLKhachHang(),
      getAllVoucher(),
    ])
    danhMucList.value = resDM
    kichThuocList.value = resKT
    mauSacList.value = resMS
    thuongHieuList.value = resTH
    products.value = resSPCT
    customers.value = resKH
    vouchers.value = resVoucher
  } catch (error) {
    console.error(error)
    toast.error('Không thể tải dữ liệu. Hãy kiểm tra lại Server!')
  } finally {
    isLoading.value = false
  }
}

const defaultPTTTId = ref(null)
const loadPTTT = async () => {
  const data = await getAllPTTT()

  // Chỉ giữ Tiền mặt và Chuyển khoản VietQR
  ptttList.value = data.filter((pt) => pt.trangThai && ['CASH', 'BANK'].includes(pt.maPhuongThuc))

  // Mặc định chọn Tiền mặt
  const defaultId =
    ptttList.value.find((pt) => pt.maPhuongThuc === 'CASH')?.id ?? ptttList.value[0]?.id

  defaultPTTTId.value = defaultId

  if (currentOrder.value && !currentOrder.value.phuongThucThanhToan) {
    currentOrder.value.phuongThucThanhToan = defaultId
  }
}
watch(
  () => currentOrder.value,
  (order) => {
    if (order && !order.phuongThucThanhToan) {
      order.phuongThucThanhToan = defaultPTTTId.value
    }
  },
  { immediate: true, deep: true },
)
const loadChiTietHoaDon = async (idHoaDon) => {
  try {
    const data = await getHoadonById(idHoaDon)

    const order = allOrders.value.find((o) => o.id === idHoaDon)
    if (!order) return

    order.cart = data.sanPhams.map((item) => ({
      id: item.id,
      product: {
        idSanPhamChiTiet: item.idSanPhamChiTiet,
        maSPCT: item.maSanPhamChiTiet,
        id: item.idSanPhamChiTiet,
        tenSanPhamChiTiet: item.tenSanPham,
        giaBan: item.donGia,
        tenMauSac: item.tenMauSac,
        tenKichThuoc: item.tenKichThuoc,
        image: item.anh,
      },
      soLuong: item.soLuong,
      thanhTien: item.thanhTien,
    }))

    order.cart.forEach((item) => {
      editingQty[item.id] = item.soLuong
    })

    order.appliedVoucher = data.voucher || null
    order.voucherQuery = data.voucher?.maVoucher || ''
    selectedVoucher.value = data.voucher || null
    order.tongTienHang = data.tongTienHang

    order.tongGiamGia = data.tongGiamGia

    order.tongThanhToan = data.tongThanhToan

    order.selectedCustomer = data.idKhachHang
      ? {
          id: data.idKhachHang,
          hoTen: data.tenKhachHang,
          soDienThoai: data.soDienThoaiKhachHang,
        }
      : null

    allOrders.value = [...allOrders.value]
  } catch (error) {
    console.error(error)
  }
}

onMounted(async () => {
  await loadAllDataFromAPI()

  const [voucherData, hoaDonData] = await Promise.all([getAllVoucher(), getHoaDonCho(user.id)])

  await loadPTTT() // 👈 lấy default trước

  vouchers.value = voucherData

  allOrders.value = hoaDonData.map((hd) => ({
    id: hd.id,
    maHoaDon: hd.maHoaDon,
    cart: [],
    selectedCustomer: null,
    appliedVoucher: hd.voucher || null,
    voucherQuery: hd.voucher?.maVoucher || '',
    loaiHoaDon: 'tai_quay',
    phuongThucThanhToan: defaultPTTTId.value, // 👈 dùng tại đây
  }))

  if (allOrders.value.length > 0) {
    currentOrderIndex.value = 0
    await loadChiTietHoaDon(allOrders.value[0].id)
  }

  connectSocket()
})
async function loadProducts() {
  products.value = await getAllSanPhamChiTiet()
}

function connectSocket() {
  if (stompClient.connected) {
    subscribePos()
  } else {
    stompClient.onConnect = () => {
      console.log('✅ Connected')

      subscribePos()
    }
  }
}

function subscribePos() {
  stompClient.subscribe('/topic/pos', async (msg) => {
    const event = JSON.parse(msg.body)

    console.log('POS Event received:', event)

    switch (event.type) {
      case 'STOCK_FORCE_ADJUSTED':
        // 1. Giảm delay xuống 100ms vì Backend đã bắn Socket ở phase afterCommit() (DB đã ghi xong)
        await new Promise((resolve) => setTimeout(resolve, 100))

        // 2. Cập nhật lại danh sách sản phẩm ở ô chọn hàng
        await loadProducts()

        // 3. 🟢 LẤY TRỰC TIẾP THÔNG BÁO TỪ BACKEND GỬI QUA (event.message)
        ElNotification({
          title: 'Cập nhật tồn kho',
          message: event.message || 'Tồn kho vừa được Admin điều chỉnh!',
          type: 'warning',
          duration: 5000,
        })

        // 4. Nếu đang mở một hóa đơn chờ bất kỳ -> Load lại chi tiết để cập nhật giỏ hàng & tổng tiền
        if (currentOrder.value?.id) {
          await loadChiTietHoaDon(currentOrder.value.id)
        }
        break

      case 'PRODUCT_UPDATED':
        await loadProducts()

        if (currentOrder.value?.id) {
          await loadChiTietHoaDon(currentOrder.value.id)
        }
        break

      case 'PAYMENT_SUCCESS':
        await loadProducts()

        if (currentOrder.value?.id) {
          await loadChiTietHoaDon(currentOrder.value.id)
        }
        break

      case 'VOUCHER_UPDATED':
        vouchers.value = await getAllVoucher()

        if (currentOrder.value?.id) {
          await loadChiTietHoaDon(currentOrder.value.id)
        }

        ElNotification({
          title: 'Voucher đã cập nhật',
          message: event.message || 'Voucher trong hóa đơn đã được cập nhật theo thay đổi mới',
          type: 'warning',
          duration: 5000,
        })

        break

      case 'DISCOUNT_UPDATED':
        ElNotification({
          title: 'Cập nhật giảm giá',
          message: event.message || 'Đợt giảm giá đã có thay đổi, giá sản phẩm đã được cập nhật.',
          type: 'warning',
          duration: 5000,
        })

        await loadProducts()

        if (currentOrder.value?.id) {
          await loadChiTietHoaDon(currentOrder.value.id)
        }

        break

      case 'VOUCHER_REMOVED':
        console.log('VOUCHER_REMOVED event received:', event)

        // 🟢 Thêm optional chaining currentOrder.value?.id để tránh lỗi crash nếu chưa chọn hóa đơn
        if (currentOrder.value?.id && event.orderId === currentOrder.value.id) {
          toast.warning(event.message || 'Voucher đã được gỡ vì không còn đủ điều kiện áp dụng.')
          await loadChiTietHoaDon(currentOrder.value.id)
        }

        break

      case 'ORDER_CANCELLED':
        await removeOrderFromUI(event.orderId)

        await loadProducts()

        ElNotification({
          title: 'Hóa đơn đã hủy',
          message: event.message || 'Hóa đơn đã bị hủy',
          type: 'warning',
          duration: 5000,
        })

        break
    }
  })
}

function addNewOrderToList(order) {
  if (allOrders.value.some((o) => o.id === order.id)) return

  allOrders.value.push({
    id: order.id,
    maHoaDon: order.maHoaDon,
    cart: [],
    selectedCustomer: null,
    appliedVoucher: order.voucher || null,
    voucherQuery: order.voucher?.maVoucher || '',
    loaiHoaDon: 'tai_quay',
    phuongThucThanhToan: defaultPTTTId.value,
  })
}
// --- 7. HÀM XỬ LÝ KHÁCH HÀNG & VOUCHER ---
const saveNewCustomer = async () => {
  if (!newCust.value.hoTen || !newCust.value.sdt) {
    toast.warning('Vui lòng nhập đầy đủ Tên và SĐT!')
    return
  }
  const phoneRegex = /^[0-9]{10}$/
  if (!phoneRegex.test(newCust.value.sdt)) {
    toast.error('Số điện thoại phải là dãy số và có đúng 10 chữ số!')
    return
  }
  const isDuplicate = customers.value.some((kh) => kh.soDienThoai === newCust.value.sdt)
  if (isDuplicate) {
    toast.error('Số điện thoại này đã tồn tại trong hệ thống!')
    return
  }
  try {
    const data = await addKhachHangQuickService({
      hoTen: newCust.value.hoTen,
      soDienThoai: newCust.value.sdt,
    })
    customers.value.push(data)
    selectedCustomer.value = data
    newCust.value = { hoTen: '', sdt: '' }
    showCustomerModal.value = false
    toast.success('Thêm khách hàng thành công!')
  } catch (error) {
    console.error(error)
    toast.error('Có lỗi xảy ra khi lưu khách hàng!')
  }
}

const selectVoucher = async (voucher) => {
  const now = new Date()

  if (voucher.trangThai !== 1) {
    toast.warning('Voucher đang bị khóa!')
    return
  }

  if (voucher.ngayBatDau && new Date(voucher.ngayBatDau) > now) {
    toast.warning('Voucher chưa đến thời gian áp dụng!')
    return
  }

  if (voucher.ngayKetThuc && new Date(voucher.ngayKetThuc) < now) {
    toast.warning('Voucher đã hết hạn!')
    return
  }

  if (voucher.soLuongDaDung >= voucher.soLuong) {
    toast.warning('Voucher đã hết lượt sử dụng!')
    return
  }

  if (totalCartPrice.value < voucher.giaTriDonHangToiThieu) {
    toast.warning(`Đơn hàng phải từ ${formatPrice(voucher.giaTriDonHangToiThieu)}`)
    return
  }

  try {
    /**
     * Nếu hóa đơn đã có voucher
     * thì backend nên xử lý update
     * không tạo bản ghi mới
     */
    await apVoucher(currentOrder.value.id, voucher.id)

    await loadChiTietHoaDon(currentOrder.value.id)

    selectedVoucher.value = voucher

    appliedVoucher.value = voucher

    voucherQuery.value = voucher.maVoucher

    voucherCode.value = voucher.maVoucher

    showVoucherDropdown.value = false

    toast.success(`Đã áp dụng ${voucher.maVoucher}`)
  } catch (error) {
    toast.error(error.message)
  }
}

const removeVoucher = async () => {
  try {
    await boVoucher(currentOrder.value.id)
    await loadChiTietHoaDon(currentOrder.value.id)

    appliedVoucher.value = null
    selectedVoucher.value = null
    voucherCode.value = ''
    voucherQuery.value = ''

    toast.success('Đã bỏ voucher')
  } catch (error) {
    toast.error(error.message)
  }
}
const filteredProducts = computed(() => {
  return products.value.filter((sp) => {
    const nameStr = (sp.tenSanPham || '').toLowerCase()
    const codeStr = (sp.maSanPhamChiTiet || '').toLowerCase()
    const searchStr = searchQuery.value.toLowerCase().trim()

    const matchSearch = !searchStr || nameStr.includes(searchStr) || codeStr.includes(searchStr)

    const selectedDM = danhMucList.value.find((dm) => dm.id == filterCategory.value)
    const selectedTH = thuongHieuList.value.find((th) => th.id == filterBrand.value)

    const matchCategory = !filterCategory.value || sp.tenDanhMuc === selectedDM?.tenDanhMuc
    const matchBrand = !filterBrand.value || sp.tenThuongHieu === selectedTH?.tenThuongHieu
    const matchColor = !filterColor.value || Number(sp.idMauSac) === Number(filterColor.value)
    const matchSize = !filterSize.value || Number(sp.idKichThuoc) === Number(filterSize.value)

    return matchSearch && matchCategory && matchBrand && matchColor && matchSize
  })
})
const filteredCustomers = computed(() => {
  return customers.value.filter(
    (kh) =>
      kh.hoTen.toLowerCase().includes(searchCustomerQuery.value.toLowerCase()) ||
      kh.soDienThoai.includes(searchCustomerQuery.value),
  )
})
const filteredVouchers = computed(() => {
  const list = [...vouchers.value]

  list.sort((a, b) => {
    const validA = isVoucherValid(a)
    const validB = isVoucherValid(b)

    if (validA !== validB) {
      return Number(validB) - Number(validA)
    }

    return tinhTienGiam(b) - tinhTienGiam(a)
  })

  return list
})
function tinhTienGiam(vc) {
  if (!isVoucherValid(vc)) {
    return 0
  }

  if (vc.loaiGiamGia === 'tien_mat') {
    return vc.giaTriGiam
  }

  let giam = (totalCartPrice.value * vc.giaTriGiam) / 100

  if (vc.giaTriGiamToiDa) {
    giam = Math.min(giam, vc.giaTriGiamToiDa)
  }

  return giam
}
const bestVoucher = computed(() => {
  return filteredVouchers.value.find(isVoucherValid) ?? null
})
// --- 9. HÀM XỬ LÝ GIỎ HÀNG & HÓA ĐƠN ---
const addToCart = async (product) => {
  try {
    if (!currentOrder.value?.id) {
      toast.error('Chưa có hóa đơn')
      return
    }

    const payload = {
      idHoaDon: currentOrder.value.id,
      idSanPhamChiTiet: product.idSanPhamChiTiet || product.id,
      soLuong: 1,
    }

    await themSanPhamVaoHoaDon(payload)

    // 1. Update cart từ DB
    await loadChiTietHoaDon(currentOrder.value.id)

    // 2. 🔥 GIẢM SỐ LƯỢNG KHẢ DỤNG NGAY TRÊN UI
    const sp = products.value.find(
      (p) => (p.idSanPhamChiTiet || p.id) === (product.idSanPhamChiTiet || product.id),
    )

    if (sp) {
      // Khởi tạo soLuongKhaDung nếu dữ liệu từ API chưa có sẵn field này
      if (sp.soLuongKhaDung === undefined || sp.soLuongKhaDung === null) {
        sp.soLuongKhaDung = sp.soLuongTon
      }

      if (sp.soLuongKhaDung > 0) {
        sp.soLuongKhaDung -= 1
      }
    }

    toast.success('Đã thêm sản phẩm')
  } catch (error) {
    console.error(error)
    toast.error(error?.message || 'Không thể thêm sản phẩm')
  }
}
const updateProductStockUI = (productId, change) => {
  const index = products.value.findIndex((p) => (p.idSanPhamChiTiet || p.id) === productId)

  if (index === -1) {
    console.log('❌ NOT FOUND PRODUCT', productId)
    return
  }

  const updated = {
    ...products.value[index],
    soLuongTon: products.value[index].soLuongTon + change,
  }

  products.value.splice(index, 1, updated)
}

// --- HÀM TĂNG SỐ LƯỢNG ---
const increaseQty = async (item) => {
  try {
    await tangSoLuongSanPham(item.id)

    await loadChiTietHoaDon(currentOrder.value.id)
    await loadProducts() // 🔄 Cập nhật lại danh sách kho
  } catch (e) {
    toast.error(e.message)
  }
}

// --- HÀM GIẢM SỐ LƯỢNG ---
const decreaseQty = async (item) => {
  try {
    if (item.soLuong <= 1) {
      await removeFromCart(currentOrder.value.cart.indexOf(item))
      return
    }

    await giamSoLuongSanPham(item.id)

    await loadChiTietHoaDon(currentOrder.value.id)
    await loadProducts() // 🔄 Cập nhật lại danh sách kho
  } catch (e) {
    toast.error(e.message)
  }
}

const debounceChangeQty = debounce(async (item) => {
  let newQty = Number(item.soLuong)

  // 1. Kiểm tra số nhập vào phải hợp lệ
  if (!newQty || newQty <= 0 || !Number.isInteger(newQty)) {
    toast.warning('Số lượng phải là số nguyên lớn hơn 0')
    await loadChiTietHoaDon(currentOrder.value.id)
    return
  }

  // 2. Tìm sản phẩm trong danh sách để kiểm tra soLuongKhaDung
  const sp = products.value.find(
    (p) => (p.idSanPhamChiTiet || p.id) === (item.product.idSanPhamChiTiet || item.product.id),
  )

  // Nếu tìm thấy SP, kiểm tra nhanh ở FE trước khi gửi API
  if (sp) {
    const khaDungHienTai = sp.soLuongKhaDung ?? 0
    const soLuongCuTrongGio = editingQty[item.id] || 0
    const tongKhaDungCoTheDung = khaDungHienTai + soLuongCuTrongGio

    if (newQty > tongKhaDungCoTheDung) {
      toast.error(`Số lượng khả dụng không đủ! (Tối đa còn lại: ${tongKhaDungCoTheDung})`)
      await loadChiTietHoaDon(currentOrder.value.id)
      return
    }
  }

  try {
    // 3. Gọi API cập nhật (Backend sẽ tự trừ/cộng soLuongKhaDung)
    await capNhatSoLuong(item.id, newQty)

    // 4. 🔥 Tải lại giỏ hàng & tải lại danh sách SP để làm mới soLuongKhaDung trên UI
    await Promise.all([
      loadChiTietHoaDon(currentOrder.value.id),
      loadProducts(), // 👈 Gọi lại API này để cập nhật soLuongKhaDung mới nhất
    ])
  } catch (error) {
    toast.error(error?.response?.data || error?.message || 'Không thể cập nhật số lượng')

    // Nếu BE báo lỗi (ví dụ hết soLuongKhaDung), reload lại để trả UI về số cũ
    await loadChiTietHoaDon(currentOrder.value.id)
    await loadProducts()
  }
}, 600)

const max_oder_waiting = 6
const user = JSON.parse(sessionStorage.getItem('user'))
const createNewOrder = async () => {
  if (allOrders.value.length >= max_oder_waiting) {
    toast.warning(`Chỉ được tạo tối đa ${max_oder_waiting} hóa đơn chờ`)
    return
  }
  try {
    const hoaDon = await taoHoaDonCho(user.id)

    allOrders.value.push({
      id: hoaDon.id,
      maHoaDon: hoaDon.maHoaDon,

      cart: [],
      selectedCustomer: null,
      appliedVoucher: null,
      voucherQuery: '',
      loaiHoaDon: 'tai_quay',
      phuongThucThanhToan: defaultPTTTId.value,
    })
    currentOrderIndex.value = allOrders.value.length - 1
    toast.success('Tạo hóa đơn thành công')
  } catch (error) {
    console.error(error)
    toast.error('Không thể tạo hóa đơn')
  }
}

const switchOrder = async (index) => {
  const order = allOrders.value[index]
  if (!order?.id) return
  currentOrderIndex.value = index
  await loadChiTietHoaDon(order.id)

  selectedCustomer.value = order.selectedCustomer
  appliedVoucher.value = order.appliedVoucher
  voucherQuery.value = order.voucherQuery
}

const removeOrder = async (index) => {
  const order = allOrders.value[index]

  if (!order?.id) {
    toast.warning('Không tìm thấy hóa đơn')
    return
  }

  try {
    // 1. Gọi backend hủy hóa đơn
    await huyHoaDon(order.id)

    await removeOrderFromUI(order.id)

    await loadProducts()

    toast.success('Đã hủy hóa đơn')
    // 2. Xóa tab khỏi mảng
    // allOrders.value.splice(index, 1)

    // // 3. LÀM SẠCH GIỎ HÀNG (Cực kỳ quan trọng)
    // // Nếu tab bị xóa là tab đang mở, ta phải clear cart ngay
    // if (index === currentOrderIndex.value) {
    //   currentOrder.value.cart = []
    // }

    // // 4. Load lại dữ liệu tồn kho để đồng bộ
    // await loadAllDataFromAPI()
    // if (index <= currentOrderIndex.value) {
    //   currentOrderIndex.value--
    // }
    // 5. Chuyển tab hoặc reset
    if (allOrders.value.length > 0) {
      // Điều chỉnh chỉ số nếu cần
      if (currentOrderIndex.value >= allOrders.value.length) {
        currentOrderIndex.value = allOrders.value.length - 1
      }
      // Tải lại chi tiết của tab mới trỏ tới
      await loadChiTietHoaDon(allOrders.value[currentOrderIndex.value].id)
    } else {
      // Không còn tab nào, reset về trạng thái trống
      resetPOSState()
    }

    toast.success('Đã hủy hóa đơn và xóa giỏ hàng liên quan')
  } catch (error) {
    console.error(error)
    toast.error(error.message || 'Hủy hóa đơn thất bại')
  }
}
const removeOrderFromUI = async (orderId) => {
  const index = allOrders.value.findIndex((o) => o.id === orderId)

  if (index === -1) return

  const isCurrent = currentOrder.value?.id === orderId

  // Xóa tab
  allOrders.value.splice(index, 1)

  // Không còn hóa đơn
  if (allOrders.value.length === 0) {
    resetPOSState()
    return
  }

  // Nếu tab bị xóa đang được mở
  if (isCurrent) {
    currentOrderIndex.value = Math.min(index, allOrders.value.length - 1)

    await loadChiTietHoaDon(allOrders.value[currentOrderIndex.value].id)
  } else {
    // Nếu xóa tab đứng trước tab hiện tại
    if (index < currentOrderIndex.value) {
      currentOrderIndex.value--
    }
  }
}

const resetPOSState = () => {
  allOrders.value = []
  currentOrderIndex.value = -1
}

// --- 10. TIỆN ÍCH & FORMATTER ---
const getProductImage = (product) => {
  // dữ liệu từ hóa đơn
  if (product?.image) {
    return `http://localhost:8080${product.image}`
  }

  // dữ liệu từ danh sách SPCT
  if (product?.images?.length > 0) {
    return `http://localhost:8080${product.images[0]}`
  }
  return '/default-image.png'
}

const setDefaultImage = (event) => {
  event.target.src = DEFAULT_PRODUCT_IMAGE
}
const formatPrice = (value) =>
  new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(value)
const resetFilters = () => {
  filterCategory.value = ''
  filterBrand.value = ''
  filterColor.value = ''
  filterSize.value = ''
  searchQuery.value = ''
}
const openDropdown = () => {
  isDropdownVisible.value = true
}
const closeDropdown = () => {
  setTimeout(() => {
    isDropdownVisible.value = false
  }, 200)
}
const openCustomerModal = () => {
  showCustomerModal.value = true
}
const selectCustomer = async (kh) => {
  // Thêm chốt chặn
  if (!hasCurrentOrder.value) {
    toast.error('Vui lòng chọn hoặc tạo hóa đơn trước!')
    return
  }
  try {
    await ganKhachHang(currentOrder.value.id, kh.id)
    selectedCustomer.value = kh
    showCustomerModal.value = false
    toast.success('Đã chọn khách hàng')
  } catch (error) {
    toast.error('Không thể gán khách hàng')
  }
}

const handleCloseInvoice = () => {
  showInvoiceModal.value = false

  if (allOrders.value.length === 0) {
    currentOrderIndex.value = -1
  }
}

const totalCartPrice = computed(() => {
  return currentOrder.value?.tongTienHang || 0
})

const voucherDiscount = computed(() => {
  return currentOrder.value?.tongGiamGia || 0
})

const finalPaymentPrice = computed(() => {
  return currentOrder.value?.tongThanhToan || 0
})

// --- 8. HÀM GỬI THANH TOÁN ---
// ================= THANH TOAN =================
// ================= THANH TOÁN =================
const submitCheckout = async () => {
  if (!currentOrder.value?.id) return toast.error('Hóa đơn không hợp lệ!')

  if (!phuongThucThanhToan.value) {
    return toast.error('Vui lòng chọn phương thức thanh toán!')
  }

  const payload = {
    idHoaDon: currentOrder.value.id,
    idPhuongThucThanhToan: parseInt(phuongThucThanhToan.value),
    idVoucher: appliedVoucher.value ? appliedVoucher.value.id : null,
  }

  try {
    // 1. Gọi API thanh toán
    const result = await thanhToanHoaDon(payload)

    // 2. Xử lý khi THÀNH CÔNG
    hoaDonPrint.value = result
    showInvoiceModal.value = true

    // Xóa hóa đơn đã thanh toán khỏi danh sách tab chờ
    allOrders.value = allOrders.value.filter((o) => o.id !== currentOrder.value.id)

    // Chuyển tab hóa đơn khác nếu còn
    if (allOrders.value.length > 0) {
      currentOrderIndex.value = 0
      await loadChiTietHoaDon(allOrders.value[0].id)
    } else {
      currentOrderIndex.value = -1
      toast.info('Đã hết hóa đơn chờ')
    }
  } catch (error) {
    // 3. Xử lý khi THẤT BẠI
    // Lấy câu message từ Backend (Java) trả về, nếu không có mới lấy error.message mặc định
    const errorMessage = error.response?.data?.message || error.message || 'Thanh toán thất bại!'

    // Hiển thị toast thông báo cho thu ngân
    toast.error(errorMessage)

    // 4. TỰ ĐỘNG ĐỒNG BỘ LẠI MÀN HÌNH POS
    try {
      await loadProducts() // Cập nhật lại tồn kho sản phẩm bên danh sách
      if (currentOrder.value?.id) {
        await loadChiTietHoaDon(currentOrder.value.id) // Cập nhật lại giỏ hàng/đơn giá
      }
      vouchers.value = await getAllVoucher() // Cập nhật lại danh sách Voucher
    } catch (syncErr) {
      console.error('Lỗi khi đồng bộ lại dữ liệu POS:', syncErr)
    }
  }
}

watch(totalCartPrice, () => {
  if (!appliedVoucher.value) return

  if (totalCartPrice.value < appliedVoucher.value.giaTriDonHangToiThieu) {
    toast.warning('Đơn hàng không còn đủ điều kiện áp dụng voucher')

    removeVoucher()
  }
})

const isVoucherValid = (vc) => {
  const now = new Date()

  if (vc.trangThai !== 1) return false

  if (vc.ngayBatDau && new Date(vc.ngayBatDau) > now) {
    return false
  }

  if (vc.ngayKetThuc && new Date(vc.ngayKetThuc) < now) {
    return false
  }

  if (vc.soLuongDaDung >= vc.soLuong) {
    return false
  }

  if (totalCartPrice.value < vc.giaTriDonHangToiThieu) {
    return false
  }

  return true
}
const getVoucherError = (vc) => {
  const now = new Date()

  if (vc.trangThai !== 1) {
    return 'Voucher đang bị khóa'
  }

  if (vc.ngayBatDau && new Date(vc.ngayBatDau) > now) {
    return 'Chưa tới ngày áp dụng'
  }

  if (vc.ngayKetThuc && new Date(vc.ngayKetThuc) < now) {
    return 'Voucher đã hết hạn'
  }

  if (vc.soLuongDaDung >= vc.soLuong) {
    return 'Đã hết lượt sử dụng'
  }

  if (totalCartPrice.value < vc.giaTriDonHangToiThieu) {
    return `Đơn tối thiểu ${formatPrice(vc.giaTriDonHangToiThieu)}`
  }

  return ''
}
const sortedProducts = computed(() => {
  return [...filteredProducts.value].sort((a, b) => {
    if (a.soLuongTon <= 0 && b.soLuongTon > 0) return 1
    if (a.soLuongTon > 0 && b.soLuongTon <= 0) return -1
    return 0
  })
})
const voucherLabel = computed(() => {
  if (!appliedVoucher.value) return null
  return appliedVoucher.value.loaiGiamGia === 'tien_mat' ? '(VNĐ)' : '(%)'
})
</script>

<style scoped>
.custom-scrollbar::-webkit-scrollbar {
  width: 5px;
}
.custom-scrollbar::-webkit-scrollbar-track {
  background: transparent;
}
.custom-scrollbar::-webkit-scrollbar-thumb {
  background: #cbd5e1;
  border-radius: 10px;
}
.custom-scrollbar::-webkit-scrollbar-thumb:hover {
  background: #94a3b8;
}

.swal2-popup {
  border-radius: 24px !important;
}

.swal2-confirm,
.swal2-cancel {
  border-radius: 16px !important;
  font-weight: 600 !important;
}

.swal2-title {
  color: #0f172a !important;
}

.swal2-html-container {
  color: #64748b !important;
}
</style>
