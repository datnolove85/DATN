<template>
  <div
    class="pos-wrapper min-h-screen bg-slate-100/90 text-slate-800 font-sans antialiased selection:bg-indigo-500 selection:text-white"
  >
    <!-- ================= HEADER POS ================= -->
    <header
      class="sticky top-0 z-40 bg-white/95 backdrop-blur-md border-b border-slate-200/80 shadow-xs"
    >
      <div class="max-w-[1800px] mx-auto px-4 h-14 flex items-center justify-between gap-3">
        <!-- Tab Hóa Đơn Chờ -->
        <div class="flex items-center gap-1.5 overflow-x-auto no-scrollbar py-1">
          <template v-if="allOrders.length > 0">
            <div
              v-for="(order, index) in allOrders.filter((o) => o.id)"
              :key="order.id"
              @click="switchOrder(index)"
              :class="[
                'group relative flex items-center gap-2 px-3 py-1 rounded-lg border transition-all duration-200 cursor-pointer text-xs font-bold select-none',
                currentOrderIndex === index
                  ? 'bg-indigo-50 border-indigo-500 text-indigo-700 shadow-xs ring-1 ring-indigo-500/20'
                  : 'bg-slate-50 border-slate-200/80 text-slate-600 hover:bg-slate-100 hover:border-slate-300',
              ]"
            >
              <span class="flex items-center gap-1.5">
                <span
                  :class="[
                    'w-2 h-2 rounded-full',
                    currentOrderIndex === index ? 'bg-indigo-600 animate-pulse' : 'bg-slate-400',
                  ]"
                ></span>
                HĐ #{{ order.maHoaDon }}
              </span>
              <button
                @click.stop="removeOrder(index)"
                class="ml-1 flex items-center justify-center w-4 h-4 rounded-full bg-slate-200/70 text-slate-500 hover:bg-rose-500 hover:text-white transition-all text-[11px] leading-none"
                title="Hủy hóa đơn (Ctrl+D)"
              >
                ×
              </button>
            </div>
          </template>

          <button
            @click="createNewOrder"
            class="flex items-center gap-1 px-2.5 py-1 rounded-lg bg-indigo-600 hover:bg-indigo-700 text-white transition-all text-xs font-bold shadow-xs active:scale-95"
            title="Tạo hóa đơn mới (F1)"
          >
            <svg
              xmlns="http://www.w3.org/2000/svg"
              class="h-3.5 w-3.5"
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
            <span>Tạo mới (F1)</span>
          </button>
        </div>

        <!-- Thanh Tìm Kiếm Sản Phẩm Nhanh -->
        <div class="flex-1 max-w-md hidden md:block">
          <div class="relative">
            <input
              ref="searchInput"
              v-model="searchQuery"
              @focus="openDropdown"
              @blur="closeDropdown"
              placeholder="Tìm theo tên hoặc mã SP... (Ctrl + F)"
              class="w-full pl-9 pr-12 py-1.5 bg-slate-50 hover:bg-white focus:bg-white border border-slate-200 focus:border-indigo-500 rounded-lg text-xs font-medium focus:outline-none focus:ring-2 focus:ring-indigo-500/20 transition-all"
            />
            <svg
              xmlns="http://www.w3.org/2000/svg"
              class="h-4 w-4 absolute left-3 top-1/2 -translate-y-1/2 text-slate-400"
              fill="none"
              viewBox="0 0 24 24"
              stroke="currentColor"
            >
              <path
                stroke-linecap="round"
                stroke-linejoin="round"
                stroke-width="2"
                d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"
              />
            </svg>
            <kbd
              class="absolute right-2.5 top-1/2 -translate-y-1/2 hidden sm:inline-block px-1.5 py-0.5 text-[9px] font-semibold text-slate-400 bg-slate-100 border border-slate-200 rounded-md"
              >Ctrl+F</kbd
            >

            <!-- Instant Search Dropdown -->
            <div
              v-if="isDropdownVisible && filteredProducts.length > 0"
              class="absolute top-full left-0 right-0 mt-1.5 bg-white border border-slate-200 shadow-2xl rounded-xl z-[60] max-h-72 overflow-y-auto custom-scrollbar p-1"
            >
              <div
                v-for="sp in filteredProducts.slice(0, 7)"
                :key="sp.id"
                @click="addToCart(sp)"
                class="flex items-center gap-2.5 p-1.5 hover:bg-indigo-50/70 rounded-lg cursor-pointer transition-colors border-b border-slate-100 last:border-0"
              >
                <img
                  :src="getProductImage(sp)"
                  class="w-9 h-9 rounded-md object-cover border border-slate-100 flex-shrink-0"
                />
                <div class="flex-1 min-w-0">
                  <p class="font-bold text-xs text-slate-800 truncate">{{ sp.tenSanPham }}</p>
                  <p class="text-[10px] text-slate-400 font-mono">#{{ sp.maSanPhamChiTiet }}</p>
                </div>
                <div class="text-right">
                  <p class="text-xs font-black text-indigo-600">
                    {{ formatPrice(sp.dangGiamGia ? sp.giaSauGiam : sp.giaBan) }}
                  </p>
                  <span class="text-[9px] text-emerald-600 font-medium"
                    >Kho: {{ sp.soLuongKhaDung ?? sp.soLuongTon }}</span
                  >
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </header>

    <!-- Loading Overlay -->
    <div
      v-if="isLoading"
      class="flex flex-col justify-center items-center py-28 text-slate-500 font-medium text-sm gap-3"
    >
      <div
        class="w-10 h-10 border-4 border-indigo-200 border-t-indigo-600 rounded-full animate-spin"
      ></div>
      <span class="animate-pulse text-xs font-semibold">Đang tải dữ liệu hệ thống POS...</span>
    </div>

    <!-- MAIN CONTENT -->
    <main
      v-else
      class="max-w-[1800px] mx-auto px-4 py-3 grid grid-cols-1 lg:grid-cols-12 gap-4 items-start"
    >
      <!-- ================= BÊN TRÁI: DANH SÁCH SẢN PHẨM & BỘ LỌC ================= -->
      <section class="lg:col-span-7 xl:col-span-8 flex flex-col space-y-3">
        <!-- Thanh Bộ Lọc -->
        <div
          class="bg-white p-2.5 rounded-xl border border-slate-200/80 shadow-xs flex flex-wrap gap-2 items-center"
        >
          <select
            v-model="filterCategory"
            class="text-xs font-semibold bg-slate-50 border border-slate-200/80 rounded-lg px-2.5 py-1.5 text-slate-600 focus:outline-none focus:ring-2 focus:ring-indigo-500/20"
          >
            <option value="">Tất cả Danh mục</option>
            <option v-for="dm in danhMucList" :key="dm.id" :value="dm.id">
              {{ dm.tenDanhMuc }}
            </option>
          </select>

          <select
            v-model="filterBrand"
            class="text-xs font-semibold bg-slate-50 border border-slate-200/80 rounded-lg px-2.5 py-1.5 text-slate-600 focus:outline-none focus:ring-2 focus:ring-indigo-500/20"
          >
            <option value="">Tất cả Thương hiệu</option>
            <option v-for="th in thuongHieuList" :key="th.id" :value="th.id">
              {{ th.tenThuongHieu }}
            </option>
          </select>

          <select
            v-model="filterColor"
            class="text-xs font-semibold bg-slate-50 border border-slate-200/80 rounded-lg px-2.5 py-1.5 text-slate-600 focus:outline-none focus:ring-2 focus:ring-indigo-500/20"
          >
            <option value="">Màu sắc</option>
            <option v-for="ms in mauSacList" :key="ms.id" :value="ms.id">{{ ms.tenMauSac }}</option>
          </select>

          <select
            v-model="filterSize"
            class="text-xs font-semibold bg-slate-50 border border-slate-200/80 rounded-lg px-2.5 py-1.5 text-slate-600 focus:outline-none focus:ring-2 focus:ring-indigo-500/20"
          >
            <option value="">Kích thước</option>
            <option v-for="kt in kichThuocList" :key="kt.id" :value="kt.id">
              {{ kt.tenKichThuoc }}
            </option>
          </select>

          <button
            @click="resetFilters"
            class="text-xs font-bold text-rose-600 hover:text-rose-700 bg-rose-50 hover:bg-rose-100 px-2.5 py-1.5 rounded-lg ml-auto transition-colors"
          >
            Xóa bộ lọc
          </button>
        </div>

        <!-- Grid Sản Phẩm -->
        <div class="grid grid-cols-2 sm:grid-cols-3 xl:grid-cols-4 2xl:grid-cols-5 gap-2.5">
          <div
            v-for="sp in sortedProducts"
            :key="sp.id"
            @click="(sp.soLuongKhaDung ?? sp.soLuongTon) > 0 && sp.trangThai ? addToCart(sp) : null"
            :class="[
              'bg-white rounded-xl border p-2 flex flex-col justify-between transition-all duration-200 group relative overflow-hidden select-none',
              (sp.soLuongKhaDung ?? sp.soLuongTon) > 0 && sp.trangThai
                ? 'border-slate-200/80 hover:border-indigo-500 hover:shadow-md cursor-pointer hover:-translate-y-0.5'
                : 'border-slate-200 bg-slate-50/80 opacity-60 cursor-not-allowed',
            ]"
          >
            <!-- Badge Giảm Giá -->
            <div v-if="sp.dangGiamGia" class="absolute top-2 right-2 z-20">
              <span
                class="bg-gradient-to-r from-amber-500 to-rose-500 text-white text-[9px] font-black px-1.5 py-0.5 rounded-full shadow-xs"
              >
                -{{ sp.phanTramGiam }}%
              </span>
            </div>

            <!-- Ảnh Sản Phẩm -->
            <div
              class="aspect-square w-full rounded-lg bg-slate-100 overflow-hidden relative mb-1.5 border border-slate-100"
            >
              <img
                :src="getProductImage(sp)"
                :alt="sp.tenSanPham"
                class="w-full h-full object-cover group-hover:scale-105 transition-transform duration-300"
                @error="setDefaultImage"
              />

              <!-- Overlay hết hàng / ngừng bán -->
              <div
                v-if="!sp.trangThai || (sp.soLuongKhaDung ?? sp.soLuongTon) <= 0"
                class="absolute inset-0 bg-slate-900/40 backdrop-blur-[1px] flex items-center justify-center z-20"
              >
                <span
                  class="bg-white/95 text-rose-600 text-[9px] font-black px-2 py-0.5 rounded shadow-sm"
                >
                  {{ !sp.trangThai ? 'NGỪNG BÁN' : 'HẾT HÀNG' }}
                </span>
              </div>

              <!-- Hover Action Indicator -->
              <div
                v-if="(sp.soLuongKhaDung ?? sp.soLuongTon) > 0 && sp.trangThai"
                class="absolute inset-0 bg-indigo-900/10 opacity-0 group-hover:opacity-100 transition-opacity flex items-center justify-center z-10"
              >
                <span
                  class="bg-indigo-600 text-white p-1.5 rounded-lg shadow-lg transform translate-y-2 group-hover:translate-y-0 transition-transform"
                >
                  <svg
                    xmlns="http://www.w3.org/2000/svg"
                    class="h-3.5 w-3.5"
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

            <!-- Detail Product -->
            <div class="flex-1 flex flex-col justify-between">
              <div>
                <div class="flex items-center justify-between gap-1 mb-0.5">
                  <span class="text-[9px] font-mono font-bold text-slate-400 uppercase truncate"
                    >#{{ sp.maSanPhamChiTiet }}</span
                  >
                  <span
                    class="text-[8px] font-bold bg-slate-100 text-slate-600 px-1 py-0.5 rounded"
                    >{{ sp.tenThuongHieu }}</span
                  >
                </div>

                <h3
                  class="text-xs font-bold text-slate-800 line-clamp-2 leading-snug group-hover:text-indigo-600 transition-colors"
                  :title="sp.tenSanPham"
                >
                  {{ sp.tenSanPham }}
                </h3>

                <div class="flex gap-1 mt-1 flex-wrap">
                  <span
                    class="text-[8px] font-semibold bg-slate-50 text-slate-600 border border-slate-100 px-1 py-0.5 rounded"
                    >🎨 {{ sp.tenMauSac }}</span
                  >
                  <span
                    class="text-[8px] font-semibold bg-slate-50 text-slate-600 border border-slate-100 px-1 py-0.5 rounded"
                    >📏 {{ sp.tenKichThuoc }}</span
                  >
                </div>
              </div>

              <!-- Price & Stock -->
              <div
                class="mt-2 pt-1.5 border-t border-slate-100 flex items-end justify-between gap-1"
              >
                <div>
                  <template v-if="sp.dangGiamGia">
                    <p
                      class="text-[9px] text-slate-400 line-through font-medium leading-none mb-0.5"
                    >
                      {{ formatPrice(sp.giaBan) }}
                    </p>
                    <p class="text-xs font-black text-rose-600 leading-none">
                      {{ formatPrice(sp.giaSauGiam) }}
                    </p>
                  </template>
                  <template v-else>
                    <p class="text-xs font-black text-indigo-600 leading-none">
                      {{ formatPrice(sp.giaBan) }}
                    </p>
                  </template>
                </div>

                <span
                  :class="[
                    'text-[8px] px-1 py-0.5 rounded font-bold flex-shrink-0',
                    (sp.soLuongKhaDung ?? sp.soLuongTon) <= 0 || !sp.trangThai
                      ? 'bg-rose-50 text-rose-600'
                      : (sp.soLuongKhaDung ?? sp.soLuongTon) <= 10
                        ? 'bg-amber-50 text-amber-600'
                        : 'bg-emerald-50 text-emerald-600',
                  ]"
                >
                  {{
                    (sp.soLuongKhaDung ?? sp.soLuongTon) <= 0
                      ? 'Hết'
                      : 'Còn: ' + (sp.soLuongKhaDung ?? sp.soLuongTon)
                  }}
                </span>
              </div>
            </div>
          </div>
        </div>
      </section>

      <!-- ================= BÊN PHẢI: GIỎ HÀNG & THANH TOÁN (CỐ ĐỊNH CHIỀU CAO) ================= -->
      <section
        class="lg:col-span-5 xl:col-span-4 h-[calc(100vh-4.5rem)] sticky top-16 flex flex-col"
      >
        <div
          class="bg-white rounded-2xl border border-slate-200/80 shadow-xl flex flex-col h-full overflow-hidden"
        >
          <!-- Khách hàng Header Bar -->
          <div class="p-4 border-b border-slate-100 bg-slate-50/70 flex-shrink-0 space-y-2.5">
            <div class="flex items-center justify-between">
              <div class="flex items-center gap-2">
                <label class="text-xs font-bold text-slate-500 uppercase tracking-wider">
                  Giỏ Hàng & Khách Hàng
                </label>
                <span
                  v-if="currentOrder?.cart?.length"
                  class="bg-indigo-100 text-indigo-700 text-xs font-extrabold px-2 py-0.5 rounded-full"
                >
                  {{ currentOrder.cart.reduce((sum, item) => sum + item.soLuong, 0) }} món
                </span>
              </div>
              <button
                @click="openCustomerModal"
                class="text-xs font-bold text-indigo-600 hover:text-indigo-700 hover:underline flex items-center gap-1 transition-colors"
              >
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  class="h-4 w-4"
                  viewBox="0 0 20 20"
                  fill="currentColor"
                >
                  <path
                    d="M8 9a3 3 0 100-6 3 3 0 000 6zM8 11a6 6 0 016 6H2a6 6 0 016-6zM16 7a1 1 0 10-2 0v1h-1a1 1 0 100 2h1v1a1 1 0 102 0v-1h1a1 1 0 100-2h-1V7z"
                  />
                </svg>
                Chọn / Thêm
              </button>
            </div>

            <div
              class="bg-white border border-slate-200/80 rounded-xl px-3 py-2 flex items-center justify-between shadow-2xs"
            >
              <div v-if="selectedCustomer">
                <p class="text-xs font-bold text-slate-800">{{ selectedCustomer.hoTen }}</p>
                <p class="text-xs text-slate-500 font-medium mt-0.5">
                  {{ selectedCustomer.soDienThoai }}
                </p>
              </div>
              <div v-else>
                <p class="text-xs font-bold text-slate-400">Khách lẻ</p>
              </div>
              <button
                v-if="selectedCustomer"
                @click="selectedCustomer = null"
                class="text-slate-400 hover:text-rose-500 p-1 transition-colors"
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

          <!-- Danh Sách Sản Phẩm Trong Giỏ -->
          <div class="flex-1 overflow-y-auto min-h-0 p-4 space-y-3 custom-scrollbar">
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
                  stroke-width="1.5"
                  d="M3 3h2l.4 2M7 13h10l4-8H5.4M7 13L5.4 5M7 13l-2.293 2.293c-.63.63-.184 1.707.707 1.707H17m0 0a2 2 0 100 4 2 2 0 000-4zm-8 0a2 2 0 11-4 0 2 2 0 014 0z"
                />
              </svg>
              <p class="text-xs font-semibold">Giỏ hàng trống. Chọn sản phẩm để bắt đầu.</p>
            </div>

            <div
              v-for="(item, index) in currentOrder?.cart || []"
              :key="item.id"
              class="flex items-center gap-3 bg-white p-2.5 rounded-xl border border-slate-100 hover:border-slate-200 shadow-2xs transition-all"
            >
              <img
                :src="getProductImage(item.product)"
                class="w-12 h-12 rounded-lg object-cover bg-slate-50 flex-shrink-0 border border-slate-100"
                @error="setDefaultImage"
              />

              <div class="flex-1 min-w-0">
                <h4 class="text-xs font-bold text-slate-800 truncate leading-snug">
                  {{ item.product.tenSanPhamChiTiet }}
                </h4>
                <div class="flex items-center gap-1.5 mt-1">
                  <span
                    class="text-[10px] font-bold text-indigo-700 bg-indigo-50 px-1.5 py-0.5 rounded truncate max-w-[80px]"
                    :title="item.product.maSanPhamChiTiet"
                  >
                    {{ item.product.maSPCT }}
                  </span>
                  <span class="text-xs text-slate-400 truncate">
                    {{ item.product.tenMauSac }} / {{ item.product.tenKichThuoc }}
                  </span>
                </div>
                <p class="text-xs font-black text-indigo-600 mt-1">
                  {{ formatPrice(item.product.giaBan) }}
                </p>
              </div>

              <!-- Tăng / Giảm Số Lượng -->
              <div
                class="flex items-center bg-slate-100/80 rounded-lg p-1 border border-slate-200/60"
              >
                <button
                  @click="decreaseQty(item)"
                  :disabled="item.soLuong <= 1"
                  class="w-5 h-5 flex items-center justify-center text-slate-600 hover:bg-white rounded transition-colors text-xs font-bold disabled:opacity-40 disabled:cursor-not-allowed select-none"
                >
                  -
                </button>
                <input
                  type="number"
                  min="1"
                  v-model.number="item.soLuong"
                  @input="debounceChangeQty(item)"
                  class="w-8 h-5 text-center text-xs font-extrabold text-slate-800 bg-transparent outline-none [appearance:textfield] [&::-webkit-outer-spin-button]:appearance-none [&::-webkit-inner-spin-button]:appearance-none"
                />
                <button
                  @click="increaseQty(item)"
                  class="w-5 h-5 flex items-center justify-center text-slate-600 hover:bg-white rounded transition-colors text-xs font-bold select-none"
                >
                  +
                </button>
              </div>

              <button
                @click="removeFromCart(index)"
                class="text-slate-300 hover:text-rose-500 transition-colors p-1"
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

          <!-- Checkout & Total Section (Ghim đáy) -->
          <div
            class="flex-shrink-0 border-t border-slate-200/80 p-4 bg-slate-50/90 space-y-3 z-10 shadow-lg"
          >
            <!-- Voucher Dropdown -->
            <div class="relative" ref="voucherRef">
              <label class="text-xs font-bold text-slate-400 uppercase tracking-wider block mb-1">
                Voucher Khuyến Mãi
              </label>
              <div
                class="w-full bg-white border border-slate-200 hover:border-indigo-300 rounded-xl px-3 py-2 text-xs font-bold text-slate-700 flex justify-between items-center transition-all shadow-2xs"
              >
                <div
                  @click="showVoucherDropdown = !showVoucherDropdown"
                  class="flex-1 cursor-pointer flex justify-between items-center pr-1"
                >
                  <div v-if="selectedVoucher || appliedVoucher" class="flex flex-col">
                    <span class="font-bold text-indigo-600 flex items-center gap-1.5 text-xs">
                      <span>🎟️</span>
                      {{
                        (selectedVoucher || appliedVoucher).loaiGiamGia === 'phan_tram'
                          ? `Giảm ${(selectedVoucher || appliedVoucher).giaTriGiam}% (${(selectedVoucher || appliedVoucher).maVoucher})`
                          : `Giảm ${formatPrice((selectedVoucher || appliedVoucher).giaTriGiam)} (${(selectedVoucher || appliedVoucher).maVoucher})`
                      }}
                    </span>
                  </div>
                  <span v-else class="text-slate-400 font-normal text-xs">
                    Chọn mã khuyến mãi...
                  </span>
                </div>

                <div class="flex items-center gap-1.5 pl-2 border-l border-slate-100">
                  <button
                    v-if="selectedVoucher || appliedVoucher"
                    @click.stop="removeVoucher"
                    class="text-slate-400 hover:text-rose-600 transition-colors px-0.5"
                    title="Xóa voucher"
                  >
                    ✕
                  </button>
                  <span
                    @click="showVoucherDropdown = !showVoucherDropdown"
                    class="text-slate-400 text-xs cursor-pointer"
                  >
                    {{ showVoucherDropdown ? '▲' : '▼' }}
                  </span>
                </div>
              </div>

              <!-- List Vouchers Dropdown -->
              <div
                v-if="showVoucherDropdown && filteredVouchers.length > 0"
                class="absolute bottom-full left-0 right-0 mb-2 bg-white border border-slate-200 rounded-xl shadow-2xl z-[100] max-h-60 overflow-y-auto custom-scrollbar p-2 space-y-2"
              >
                <div
                  v-for="vc in filteredVouchers"
                  :key="vc.id"
                  @mousedown.prevent="isVoucherValid(vc) ? selectVoucher(vc) : null"
                  :class="[
                    'p-2.5 rounded-lg border transition-all flex flex-col gap-1.5',
                    isVoucherValid(vc)
                      ? 'cursor-pointer bg-white hover:border-indigo-300 hover:bg-indigo-50/40'
                      : 'opacity-60 bg-slate-50 border-slate-200 cursor-not-allowed',
                    appliedVoucher?.id === vc.id || selectedVoucher?.id === vc.id
                      ? 'border-indigo-500 bg-indigo-50/60 shadow-xs'
                      : 'border-slate-100',
                  ]"
                >
                  <div v-if="bestVoucher?.id === vc.id" class="flex items-center">
                    <span
                      class="bg-amber-100 text-amber-800 text-[10px] px-2 py-0.5 rounded-md font-bold"
                    >
                      ⭐ Khuyên dùng
                    </span>
                  </div>

                  <div class="flex justify-between items-center text-xs">
                    <span class="font-bold text-slate-700">
                      Mã:
                      <span class="bg-slate-100 px-1.5 py-0.5 rounded font-mono">{{
                        vc.maVoucher || vc.id
                      }}</span>
                    </span>
                    <span
                      class="font-bold text-indigo-600 bg-indigo-50 px-2 py-0.5 rounded-full text-xs"
                    >
                      {{ vc.loaiGiamGia === 'phan_tram' ? '%' : 'Tiền mặt' }}
                    </span>
                  </div>

                  <div
                    class="grid grid-cols-2 gap-1.5 text-xs text-slate-500 bg-slate-50 p-1.5 rounded-md"
                  >
                    <div>
                      Tối thiểu:
                      <b class="text-slate-700">{{ formatPrice(vc.giaTriDonHangToiThieu) }}</b>
                    </div>
                    <div>
                      Mức giảm:
                      <b class="text-rose-600">{{
                        vc.loaiGiamGia === 'tien_mat'
                          ? formatPrice(vc.giaTriGiam)
                          : vc.giaTriGiam + '%'
                      }}</b>
                    </div>
                  </div>

                  <div
                    v-if="!isVoucherValid(vc)"
                    class="text-xs text-rose-600 font-semibold text-right"
                  >
                    🔒 {{ getVoucherError(vc) }}
                  </div>
                </div>
              </div>
            </div>

            <!-- Total Price Calculation -->
            <div class="space-y-1.5 text-xs font-medium text-slate-600">
              <div class="flex justify-between">
                <span>Tổng tiền hàng</span>
                <span class="text-slate-800 font-bold">{{ formatPrice(totalCartPrice) }}</span>
              </div>

              <div class="flex justify-between text-rose-600">
                <span>Giảm giá Voucher</span>
                <span v-if="appliedVoucher" class="font-bold">
                  -{{ formatPrice(voucherDiscount) }}
                </span>
                <span v-else>- 0đ</span>
              </div>

              <div
                class="bg-gradient-to-br from-indigo-50 to-blue-50/80 rounded-xl border border-indigo-100 p-3 mt-2"
              >
                <div class="flex justify-between items-center">
                  <span class="font-extrabold text-slate-700 text-xs uppercase"
                    >Tổng thanh toán</span
                  >
                  <span class="text-xl sm:text-2xl font-black text-indigo-600">
                    {{
                      formatPrice(
                        totalCartPrice - voucherDiscount < 0 ? 0 : totalCartPrice - voucherDiscount,
                      )
                    }}
                  </span>
                </div>
              </div>
            </div>

            <!-- Payment Methods & Type -->
            <!-- Payment Methods & Type -->
            <div class="space-y-2 pt-1">
              <div class="grid grid-cols-2 gap-2.5">
                <div>
                  <label class="text-xs font-bold text-slate-400 block mb-1">LOẠI HÓA ĐƠN</label>
                  <select
                    v-model="loaiHoaDon"
                    disabled
                    class="w-full bg-slate-100 border border-slate-200 rounded-xl p-2 text-xs font-bold text-slate-700 cursor-not-allowed h-[34px]"
                  >
                    <option value="tai_quay">Tại quầy (POS)</option>
                  </select>
                </div>

                <div>
                  <label class="text-xs font-bold text-slate-400 block mb-1">HÌNH THỨC TT</label>
                  <select
                    v-model="phuongThucThanhToan"
                    @change="handlePaymentMethod"
                    class="w-full bg-white border border-slate-200 rounded-xl p-2 text-xs font-bold text-slate-700 focus:ring-2 focus:ring-indigo-500/20 outline-none h-[34px]"
                  >
                    <option v-for="pt in ptttList" :key="pt.id" :value="pt.id">
                      {{ pt.tenPhuongThuc }}
                    </option>
                  </select>
                </div>
              </div>

              <!-- Giao diện nhập tiền kết hợp (Tiền mặt + Chuyển khoản) khi chọn phương thức MIXED -->
              <!-- Giao diện nhập tiền kết hợp (Tiền mặt + Chuyển khoản) khi chọn phương thức MIXED -->
              <!-- Giao diện nhập tiền kết hợp (Tiền mặt + Chuyển khoản) khi chọn phương thức MIXED -->
              <div
                v-if="isMultiPayment"
                class="bg-indigo-50/60 p-2.5 rounded-xl border border-indigo-100 space-y-2"
              >
                <div class="flex justify-between text-[11px] font-bold text-slate-600">
                  <span>Phân bổ tiền thanh toán:</span>
                  <span class="text-indigo-600"
                    >Cần trả: {{ formatPrice(totalCartPrice - voucherDiscount) }}</span
                  >
                </div>

                <div class="grid grid-cols-2 gap-2">
                  <div>
                    <label class="text-[10px] font-bold text-slate-500 block mb-0.5"
                      >Tiền mặt (CASH)</label
                    >
                    <input
                      type="text"
                      v-model="displayCash"
                      @input="onCashInput"
                      placeholder="0"
                      class="w-full bg-white border border-slate-200 rounded-lg p-1.5 text-xs font-bold text-slate-800 outline-none focus:border-indigo-500"
                    />
                  </div>
                  <div>
                    <label class="text-[10px] font-bold text-slate-500 block mb-0.5"
                      >Chuyển khoản (BANK)</label
                    >
                    <input
                      type="text"
                      v-model="displayBank"
                      @input="onBankInput"
                      placeholder="0"
                      class="w-full bg-white border border-slate-200 rounded-lg p-1.5 text-xs font-bold text-slate-800 outline-none focus:border-indigo-500"
                    />
                  </div>
                </div>
              </div>
            </div>

            <!-- Submit Button -->
            <button
              @click="submitCheckout"
              class="w-full bg-indigo-600 hover:bg-indigo-700 active:scale-[0.99] text-white font-extrabold text-sm py-3.5 px-5 rounded-xl shadow-md shadow-indigo-200 hover:shadow-lg transition-all flex items-center justify-center gap-2 mt-2"
            >
              <span>XUẤT HÓA ĐƠN & THANH TOÁN</span>
            </button>
          </div>
        </div>
      </section>
    </main>

    <!-- ================= MODAL CHỌN KHÁCH HÀNG ================= -->
    <div
      v-if="showCustomerModal"
      class="fixed inset-0 bg-slate-900/40 backdrop-blur-xs flex items-center justify-center p-4 z-50"
    >
      <div
        class="bg-white rounded-2xl max-w-md w-full p-5 shadow-2xl border border-slate-100 animate-in fade-in zoom-in-95 duration-150"
      >
        <div class="flex justify-between items-center mb-3">
          <h3 class="text-sm font-bold text-slate-900">Chọn Khách Hàng Hệ Thống</h3>
          <button
            @click="showCustomerModal = false"
            class="text-slate-400 hover:text-slate-600 font-bold text-sm"
          >
            ✕
          </button>
        </div>

        <input
          type="text"
          v-model="searchCustomerQuery"
          placeholder="Tìm theo tên hoặc SĐT..."
          class="w-full bg-slate-50 border border-slate-200 rounded-lg px-3 py-1.5 text-xs mb-2.5 focus:outline-none focus:ring-2 focus:ring-indigo-500/20 font-medium"
        />

        <div class="max-h-60 overflow-y-auto space-y-2 mb-1 custom-scrollbar">
          <!-- Form Thêm Nhanh -->
          <div class="bg-indigo-50/50 p-2.5 rounded-xl border border-indigo-100 mb-2">
            <p class="text-[9px] font-bold text-indigo-700 uppercase mb-1.5">Thêm khách hàng mới</p>
            <input
              v-model="newCust.hoTen"
              placeholder="Họ tên khách hàng"
              class="w-full p-1.5 mb-1.5 rounded-lg border border-slate-200 bg-white text-xs font-medium"
            />
            <input
              v-model="newCust.sdt"
              placeholder="Số điện thoại"
              class="w-full p-1.5 rounded-lg border border-slate-200 bg-white text-xs font-medium"
            />
            <button
              @click="saveNewCustomer"
              class="w-full mt-2 bg-indigo-600 hover:bg-indigo-700 text-white py-1.5 rounded-lg text-xs font-bold transition-colors"
            >
              Lưu & Chọn khách hàng
            </button>
          </div>

          <!-- Customer List -->
          <div
            v-for="kh in filteredCustomers"
            :key="kh.id"
            @click="selectCustomer(kh)"
            class="p-2 border border-slate-100 rounded-lg hover:bg-indigo-50/60 cursor-pointer flex justify-between items-center transition-colors"
          >
            <div>
              <p class="text-xs font-bold text-slate-800">{{ kh.hoTen }}</p>
              <p class="text-[10px] text-slate-500 font-medium">{{ kh.soDienThoai }}</p>
            </div>
            <span class="text-[9px] font-bold px-1.5 py-0.5 rounded bg-amber-100 text-amber-800">
              {{ kh.hangThanhVien || 'Thành viên' }}
            </span>
          </div>
        </div>
      </div>
    </div>

    <!-- Modals Phụ Khác -->
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
import { ref, reactive, computed, watch, onMounted, onBeforeUnmount } from 'vue'
import { useToast } from 'vue-toastification'
import Swal from 'sweetalert2'
import { debounce } from 'lodash-es'

import stompClient from '@/socket'
import InvoiceModal from './InvoiceModal.vue'
import PaymentQrDialog from '@/views/shop/components/PaymentQrDialog.vue'

// --- SERVICES ---
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
  tangSoLuongSanPham,
  ganKhachHang,
  thanhToanHoaDon,
  huyHoaDon,
  apVoucher,
  boVoucher,
  taoQr,
  capNhatSoLuong,
  xoaSanPhamKhoiHoaDon,
} from '@/service/HoaDonService'

// --- BIẾN KHỞI TẠO & DEFAULTS ---
const toast = useToast()
const DEFAULT_PRODUCT_IMAGE = 'https://images.unsplash.com/photo-1523381210434-271e8be1f52b?w=400'
const max_oder_waiting = 6
const user = JSON.parse(sessionStorage.getItem('user')) || {}

// --- STATE DANH SÁCH TỪ API ---
const danhMucList = ref([])
const thuongHieuList = ref([])
const mauSacList = ref([])
const kichThuocList = ref([])
const products = ref([])
const customers = ref([])
const vouchers = ref([])
const ptttList = ref([])
const isLoading = ref(true)
const defaultPTTTId = ref(null)

// --- STATE THANH TOÁN KẾT HỢP & QR ---
const isMultiPayment = ref(false)
const paymentCash = ref(0)
const paymentBank = ref(0)
const pendingCheckoutPayload = ref(null) // Lưu tạm hóa đơn chờ quét QR xong mới chốt

// --- STATE POS & GIỎ HÀNG ---
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
const editingQty = reactive({})

// --- STATE SEARCH, FILTER & MODALS ---
const searchQuery = ref('')
const filterCategory = ref('')
const filterBrand = ref('')
const filterColor = ref('')
const filterSize = ref('')
const voucherCode = ref('')
const searchCustomerQuery = ref('')
const newCust = ref({ hoTen: '', sdt: '' })

const hoaDonPrint = ref(null)
const showInvoiceModal = ref(false)
const showCustomerModal = ref(false)
const showVoucherDropdown = ref(false)
const isDropdownVisible = ref(false)

const searchInput = ref(null)
const voucherRef = ref(null)

// --- STATE QR PAYMENT ---
const showQrDialog = ref(false)
const qrData = ref({
  qrUrl: '',
  maHoaDon: '',
  tongTien: 0,
})

const totalNeedPay = computed(() => {
  return currentOrder.value?.tongThanhToan || totalCartPrice.value - voucherDiscount.value
})

// --- TỰ ĐỘNG TÍNH TOÁN KHI NHẬP TIỀN KẾT HỢP ---
const handleCashInput = () => {
  const cash = Number(paymentCash.value) || 0
  const needed = totalNeedPay.value
  const bank = needed - cash
  paymentBank.value = bank > 0 ? bank : 0
}

const handleBankInput = () => {
  const bank = Number(paymentBank.value) || 0
  const needed = totalNeedPay.value
  const cash = needed - bank
  paymentCash.value = cash > 0 ? cash : 0
}

// --- COMPUTED PROPERTIES ---
const currentOrder = computed(() => {
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
const totalCartPrice = computed(() => currentOrder.value?.tongTienHang || 0)
const voucherDiscount = computed(() => currentOrder.value?.tongGiamGia || 0)

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

const sortedProducts = computed(() => {
  return [...filteredProducts.value].sort((a, b) => {
    if (a.soLuongTon <= 0 && b.soLuongTon > 0) return 1
    if (a.soLuongTon > 0 && b.soLuongTon <= 0) return -1
    return 0
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
    if (validA !== validB) return Number(validB) - Number(validA)
    return tinhTienGiam(b) - tinhTienGiam(a)
  })
  return list
})

const bestVoucher = computed(() => filteredVouchers.value.find(isVoucherValid) ?? null)

// --- API LOADING & INITIALIZATION ---
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

const loadPTTT = async () => {
  const data = await getAllPTTT()
  ptttList.value = data.filter(
    (pt) =>
      pt.trangThai &&
      ['CASH', 'BANK', 'MIXED', 'TIEN_MAT', 'CHUYEN_KHOAN', 'KET_HOP'].includes(
        pt.maPhuongThuc?.toUpperCase(),
      ),
  )

  const defaultId =
    ptttList.value.find((pt) => ['CASH', 'TIEN_MAT'].includes(pt.maPhuongThuc?.toUpperCase()))
      ?.id ?? ptttList.value[0]?.id
  defaultPTTTId.value = defaultId

  if (currentOrder.value && !currentOrder.value.phuongThucThanhToan) {
    currentOrder.value.phuongThucThanhToan = defaultId
  }
}

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

const loadProducts = async () => {
  products.value = await getAllSanPhamChiTiet()
}

// --- SOCKET REALTIME ---
let socketSubscription = null

const connectSocket = () => {
  if (stompClient.connected) {
    subscribePos()
  } else {
    stompClient.onConnect = () => {
      subscribePos()
    }
  }
}

const subscribePos = () => {
  if (socketSubscription) {
    socketSubscription.unsubscribe()
  }

  socketSubscription = stompClient.subscribe('/topic/pos', async (msg) => {
    try {
      await loadProducts()
      if (currentOrder.value?.id) {
        await loadChiTietHoaDon(currentOrder.value.id)
      }

      if (msg && msg.body) {
        let data = {}
        let isJson = false

        try {
          data = JSON.parse(msg.body)
          isJson = true
        } catch (e) {
          // Chuỗi Text thuần
        }

        const eventType = isJson ? data.type || data.eventType || data.action : ''

        // Không hiện toast khi chỉ cập nhật số lượng
        if (eventType === 'QUANTITY_UPDATED') {
          return
        }
        if (eventType === 'VOUCHER_UPDATED') {
          vouchers.value = await getAllVoucher() // Load lại danh sách voucher mới nhất
          if (currentOrder.value?.id) {
            await loadChiTietHoaDon(currentOrder.value.id) // Reload lại chi tiết hóa đơn để cập nhật số tiền giảm giá nếu đang áp dụng
          }

          return
        }

        // Thông báo khi sản phẩm thay đổi
        if (eventType === 'PRODUCT_UPDATED') {
          toast.warning(
            'Đợt giảm giá hoặc thông tin sản phẩm đã thay đổi. Vui lòng kiểm tra lại hóa đơn trước khi thanh toán!',
          )
          return
        }

        if (eventType === 'INVOICE_PAID') {
          toast.success('Thanh toán thành công!')
          return
        }

        if (eventType === 'VOUCHER_REMOVED') {
          toast.warning(
            data.message ||
              data.noiDung ||
              'Voucher vừa được cập nhật. Vui lòng kiểm tra lại hóa đơn trước khi thanh toán!',
          )
          return
        }

        if (eventType === 'DISCOUNT_UPDATED') {
          toast.warning(
            data.message ||
              data.noiDung ||
              'Đợt giảm giá sản phẩm có thay đổi. Vui lòng kiểm tra lại hóa đơn!',
          )
          return
        }

        const noiDungThongBao = isJson
          ? data.message || data.noiDung || data.content || msg.body
          : msg.body

        if (noiDungThongBao) {
          toast.info(noiDungThongBao)
        }
      }
    } catch (error) {
      console.error('Lỗi cập nhật dữ liệu từ Socket:', error)
    }
  })
}
// --- BIẾN HIỂN THỊ FORMAT TIỀN TỆ ---
const displayCash = ref('')
const displayBank = ref('')

// --- HÀM FORMAT SỐ THÀNH DẠNG 600.000 ---
const formatCurrencyInput = (value) => {
  if (!value && value !== 0) return ''
  const numericValue = String(value).replace(/\D/g, '')
  if (!numericValue) return ''
  return new Intl.NumberFormat('vi-VN').format(Number(numericValue))
}
// --- XỬ LÝ NHẬP TIỀN MẶT ---
const onCashInput = (e) => {
  const rawValue = e.target.value.replace(/\D/g, '')
  let numVal = rawValue ? Number(rawValue) : 0
  const needed = totalNeedPay.value

  // 🌟 VALIDATION: Kiểm tra nếu nhập quá tổng tiền cần thanh toán
  if (numVal > needed) {
    numVal = needed
    // (Tùy chọn) Hiện thông báo cảnh báo nếu bạn dùng vue3-toastify
    toast.warning('Số tiền nhập không được vượt quá tổng hóa đơn!')
  }

  paymentCash.value = numVal
  displayCash.value = formatCurrencyInput(numVal)

  // Tự động tính phần chuyển khoản còn thiếu
  const bank = needed - numVal
  paymentBank.value = bank > 0 ? bank : 0
  displayBank.value = formatCurrencyInput(paymentBank.value)
}

// --- XỬ LÝ NHẬP CHUYỂN KHOẢN ---
const onBankInput = (e) => {
  const rawValue = e.target.value.replace(/\D/g, '')
  let numVal = rawValue ? Number(rawValue) : 0
  const needed = totalNeedPay.value

  // 🌟 VALIDATION: Kiểm tra nếu nhập quá tổng tiền cần thanh toán
  if (numVal > needed) {
    numVal = needed
    toast.warning('Số tiền nhập không được vượt quá tổng hóa đơn!')
  }

  paymentBank.value = numVal
  displayBank.value = formatCurrencyInput(numVal)

  // Tự động tính phần tiền mặt còn thiếu
  const cash = needed - numVal
  paymentCash.value = cash > 0 ? cash : 0
  displayCash.value = formatCurrencyInput(paymentCash.value)
}

// --- CẬP NHẬT TRONG handlePaymentMethod KHI CHỌN MIXED ---
const handlePaymentMethod = async () => {
  const pttt = ptttList.value.find((p) => p.id === Number(phuongThucThanhToan.value))
  if (!pttt) return

  const code = pttt.maPhuongThuc?.toUpperCase()

  if (code === 'MIXED' || code === 'KET_HOP') {
    isMultiPayment.value = true
    const totalNeedPayVal = totalNeedPay.value
    paymentCash.value = totalNeedPayVal
    paymentBank.value = 0

    // Gán giá trị hiển thị đã format
    displayCash.value = formatCurrencyInput(totalNeedPayVal)
    displayBank.value = '0'
  } else if (code === 'BANK' || code === 'CHUYEN_KHOAN') {
    isMultiPayment.value = false
    paymentCash.value = 0
    paymentBank.value = 0
    displayCash.value = ''
    displayBank.value = ''
    try {
      const transferAmount = totalNeedPay.value
      const data = await taoQr(currentOrder.value.id, transferAmount)
      qrData.value = {
        ...data,
        tongTien: transferAmount,
      }
      showQrDialog.value = true
    } catch (e) {
      toast.error('Không thể tạo mã QR thanh toán!')
    }
  } else {
    isMultiPayment.value = false
    paymentCash.value = 0
    paymentBank.value = 0
    displayCash.value = ''
    displayBank.value = ''
  }
}

// --- XỬ LÝ XUẤT HÓA ĐƠN & GỌI QR NẾU CÓ CHUYỂN KHOẢN ---
const submitCheckout = async () => {
  if (!currentOrder.value?.id) return toast.error('Hóa đơn không hợp lệ!')

  const tongTienCanThanhToan = totalNeedPay.value
  let danhSachThanhToanPayload = []

  const cashMethod = ptttList.value.find(
    (p) =>
      ['CASH', 'TIEN_MAT'].includes(p.maPhuongThuc?.toUpperCase()) ||
      p.tenPhuongThuc?.toLowerCase().includes('tiền mặt'),
  )
  const bankMethod = ptttList.value.find(
    (p) =>
      ['BANK', 'CHUYEN_KHOAN'].includes(p.maPhuongThuc?.toUpperCase()) ||
      p.tenPhuongThuc?.toLowerCase().includes('chuyển khoản'),
  )
  const selectedPttt = ptttList.value.find((p) => p.id === Number(phuongThucThanhToan.value))
  const selectedCode = selectedPttt?.maPhuongThuc?.toUpperCase()

  if (isMultiPayment.value) {
    const tienMat = Number(paymentCash.value) || 0
    const chuyenKhoan = Number(paymentBank.value) || 0
    const tongKhachDua = tienMat + chuyenKhoan

    if (tongKhachDua < tongTienCanThanhToan) {
      return toast.error(
        `Số tiền thanh toán (${formatPrice(tongKhachDua)}) chưa đủ so với tổng hóa đơn (${formatPrice(tongTienCanThanhToan)})!`,
      )
    }

    if (tienMat > 0) {
      if (!cashMethod) return toast.error('Không tìm thấy phương thức Tiền mặt trong hệ thống!')
      danhSachThanhToanPayload.push({
        idPhuongThucThanhToan: cashMethod.id,
        soTien: tienMat,
        maGiaoDich: null,
      })
    }

    if (chuyenKhoan > 0) {
      if (!bankMethod) return toast.error('Không tìm thấy phương thức Chuyển khoản trong hệ thống!')
      danhSachThanhToanPayload.push({
        idPhuongThucThanhToan: bankMethod.id,
        soTien: chuyenKhoan,
        maGiaoDich: 'CK-' + Date.now(),
      })
    }

    if (danhSachThanhToanPayload.length === 0) {
      return toast.error('Vui lòng nhập số tiền thanh toán!')
    }
  } else if (selectedCode === 'BANK' || selectedCode === 'CHUYEN_KHOAN') {
    if (!bankMethod) return toast.error('Không tìm thấy phương thức Chuyển khoản trong hệ thống!')
    danhSachThanhToanPayload.push({
      idPhuongThucThanhToan: bankMethod.id,
      soTien: tongTienCanThanhToan,
      maGiaoDich: 'CK-' + Date.now(),
    })
  } else {
    if (!phuongThucThanhToan.value) return toast.error('Vui lòng chọn phương thức thanh toán!')
    if (!cashMethod) return toast.error('Không tìm thấy phương thức Tiền mặt trong hệ thống!')
    danhSachThanhToanPayload.push({
      idPhuongThucThanhToan: parseInt(phuongThucThanhToan.value),
      soTien: tongTienCanThanhToan > 0 ? tongTienCanThanhToan : 0,
      maGiaoDich: null,
    })
  }

  const payload = {
    idHoaDon: currentOrder.value.id,
    idVoucher: appliedVoucher.value ? appliedVoucher.value.id : null,
    danhSachThanhToan: danhSachThanhToanPayload,
  }

  // Kiểm tra nếu có phần chuyển khoản thì bật QR lên
  const hasBankPayment = danhSachThanhToanPayload.some(
    (item) => item.idPhuongThucThanhToan === bankMethod?.id,
  )

  if (hasBankPayment) {
    pendingCheckoutPayload.value = payload
    try {
      // 🌟 ĐOẠN QUAN TRỌNG: Lấy đúng số tiền chuyển khoản (nếu thanh toán kết hợp lấy paymentBank, còn chuyển khoản thuần lấy tổng tiền)
      const transferAmount = isMultiPayment.value ? Number(paymentBank.value) : tongTienCanThanhToan

      const data = await taoQr(currentOrder.value.id, transferAmount)
      qrData.value = {
        ...data,
        tongTien: transferAmount,
      }
      showQrDialog.value = true
    } catch (e) {
      toast.error('Không thể tạo mã QR thanh toán!')
    }
    return
  }

  await executeFinalCheckout(payload)
}
const executeFinalCheckout = async (payload) => {
  try {
    const result = await thanhToanHoaDon(payload)
    hoaDonPrint.value = result
    showInvoiceModal.value = true

    allOrders.value = allOrders.value.filter((o) => o.id !== currentOrder.value.id)

    // Reset lại trạng thái
    isMultiPayment.value = false
    paymentCash.value = 0
    paymentBank.value = 0
    pendingCheckoutPayload.value = null

    if (allOrders.value.length > 0) {
      currentOrderIndex.value = 0
      await loadChiTietHoaDon(allOrders.value[0].id)
    } else {
      currentOrderIndex.value = -1
      toast.info('Đã hết hóa đơn chờ')
    }
  } catch (error) {
    const errorMessage = error.response?.data?.message || error.message || 'Thanh toán thất bại!'
    toast.error(errorMessage)
    try {
      await loadProducts()
      if (currentOrder.value?.id) await loadChiTietHoaDon(currentOrder.value.id)
      vouchers.value = await getAllVoucher()
    } catch (syncErr) {
      console.error('Lỗi đồng bộ POS:', syncErr)
    }
  }
}

const xacNhanDaThanhToan = async () => {
  showQrDialog.value = false
  if (pendingCheckoutPayload.value) {
    await executeFinalCheckout(pendingCheckoutPayload.value)
  } else {
    // Fallback cho quét QR thông thường
    await thanhToanHoaDon({
      idHoaDon: currentOrder.value.id,
      idPhuongThucThanhToan: parseInt(phuongThucThanhToan.value),
      danhSachThanhToan: [
        {
          idPhuongThucThanhToan: parseInt(phuongThucThanhToan.value),
          soTien: currentOrder.value.tongThanhToan || totalCartPrice.value - voucherDiscount.value,
          maGiaoDich: 'QR-' + Date.now(),
        },
      ],
    })
    toast.success('Thanh toán thành công')
  }
}

// --- HÀM GIỎ HÀNG & THAO TÁC SẢN PHẨM ---
const addToCart = async (product) => {
  try {
    if (!currentOrder.value?.id) return toast.error('Chưa có hóa đơn')

    const payload = {
      idHoaDon: currentOrder.value.id,
      idSanPhamChiTiet: product.idSanPhamChiTiet || product.id,
      soLuong: 1,
    }

    await themSanPhamVaoHoaDon(payload)
    await loadChiTietHoaDon(currentOrder.value.id)

    const sp = products.value.find(
      (p) => (p.idSanPhamChiTiet || p.id) === (product.idSanPhamChiTiet || product.id),
    )
    if (sp) {
      if (sp.soLuongKhaDung === undefined || sp.soLuongKhaDung === null) {
        sp.soLuongKhaDung = sp.soLuongTon
      }
      if (sp.soLuongKhaDung > 0) sp.soLuongKhaDung -= 1
    }

    toast.success('Đã thêm sản phẩm')
  } catch (error) {
    console.error(error)
    toast.error(error?.message || 'Không thể thêm sản phẩm')
  }
}

const increaseQty = async (item) => {
  try {
    await tangSoLuongSanPham(item.id)
    await loadChiTietHoaDon(currentOrder.value.id)
    await loadProducts()
  } catch (e) {
    toast.error(e.message)
  }
}

const decreaseQty = async (item) => {
  try {
    if (item.soLuong <= 1) {
      await removeFromCart(currentOrder.value.cart.indexOf(item))
      return
    }
    await giamSoLuongSanPham(item.id)
    await loadChiTietHoaDon(currentOrder.value.id)
    await loadProducts()
  } catch (e) {
    toast.error(e.message)
  }
}

const removeFromCart = async (index) => {
  const item = currentOrder.value?.cart[index]
  if (!item) return
  try {
    await xoaSanPhamKhoiHoaDon(item.id)
    await loadChiTietHoaDon(currentOrder.value.id)
    await loadProducts()
    toast.success('Đã xóa sản phẩm khỏi giỏ')
  } catch (error) {
    toast.error('Không thể xóa sản phẩm')
  }
}

const debounceChangeQty = debounce(async (item) => {
  let newQty = Number(item.soLuong)

  if (!newQty || newQty <= 0 || !Number.isInteger(newQty)) {
    toast.warning('Số lượng phải là số nguyên lớn hơn 0')
    await loadChiTietHoaDon(currentOrder.value.id)
    return
  }

  const sp = products.value.find(
    (p) => (p.idSanPhamChiTiet || p.id) === (item.product.idSanPhamChiTiet || item.product.id),
  )

  if (sp) {
    const khaDungHienTai = sp.soLuongKhaDung ?? 0
    const soLuongCuTrongGio = editingQty[item.id] || 0
    const tongKhaDungCoTheDung = khaDungHienTai + soLuongCuTrongGio

    if (newQty > tongKhaDungCoTheDung) {
      toast.error(`Số lượng khả dụng không đủ! (Tối đa: ${tongKhaDungCoTheDung})`)
      await loadChiTietHoaDon(currentOrder.value.id)
      return
    }
  }

  try {
    await capNhatSoLuong(item.id, newQty)
    await Promise.all([loadChiTietHoaDon(currentOrder.value.id), loadProducts()])
  } catch (error) {
    toast.error(error?.response?.data || error?.message || 'Không thể cập nhật số lượng')
    await loadChiTietHoaDon(currentOrder.value.id)
    await loadProducts()
  }
}, 600)

// --- HÀM XỬ LÝ HÓA ĐƠN ---
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
  if (!order?.id) return toast.warning('Không tìm thấy hóa đơn')

  try {
    await huyHoaDon(order.id)
    await removeOrderFromUI(order.id)
    await loadProducts()

    if (allOrders.value.length > 0) {
      if (currentOrderIndex.value >= allOrders.value.length) {
        currentOrderIndex.value = allOrders.value.length - 1
      }
      await loadChiTietHoaDon(allOrders.value[currentOrderIndex.value].id)
    } else {
      resetPOSState()
    }
    toast.success('Đã hủy hóa đơn')
  } catch (error) {
    console.error(error)
    toast.error(error.message || 'Hủy hóa đơn thất bại')
  }
}

const removeOrderFromUI = async (orderId) => {
  const index = allOrders.value.findIndex((o) => o.id === orderId)
  if (index === -1) return

  const isCurrent = currentOrder.value?.id === orderId
  allOrders.value.splice(index, 1)

  if (allOrders.value.length === 0) {
    resetPOSState()
    return
  }

  if (isCurrent) {
    currentOrderIndex.value = Math.min(index, allOrders.value.length - 1)
    await loadChiTietHoaDon(allOrders.value[currentOrderIndex.value].id)
  } else if (index < currentOrderIndex.value) {
    currentOrderIndex.value--
  }
}

const resetPOSState = () => {
  allOrders.value = []
  currentOrderIndex.value = -1
}

// --- VOUCHER & KHÁCH HÀNG ---
const tinhTienGiam = (vc) => {
  if (!isVoucherValid(vc)) return 0
  if (vc.loaiGiamGia === 'tien_mat') return vc.giaTriGiam

  let giam = (totalCartPrice.value * vc.giaTriGiam) / 100
  if (vc.giaTriGiamToiDa) giam = Math.min(giam, vc.giaTriGiamToiDa)
  return giam
}

const selectVoucher = async (voucher) => {
  const now = new Date()
  if (voucher.trangThai !== 1) return toast.warning('Voucher đang bị khóa!')
  if (voucher.ngayBatDau && new Date(voucher.ngayBatDau) > now)
    return toast.warning('Voucher chưa đến thời gian áp dụng!')
  if (voucher.ngayKetThuc && new Date(voucher.ngayKetThuc) < now)
    return toast.warning('Voucher đã hết hạn!')
  if (voucher.soLuongDaDung >= voucher.soLuong) return toast.warning('Voucher đã hết lượt sử dụng!')
  if (totalCartPrice.value < voucher.giaTriDonHangToiThieu)
    return toast.warning(`Đơn hàng phải từ ${formatPrice(voucher.giaTriDonHangToiThieu)}`)

  try {
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

const isVoucherValid = (vc) => {
  const now = new Date()
  if (vc.trangThai !== 1) return false
  if (vc.ngayBatDau && new Date(vc.ngayBatDau) > now) return false
  if (vc.ngayKetThuc && new Date(vc.ngayKetThuc) < now) return false
  if (vc.soLuongDaDung >= vc.soLuong) return false
  if (totalCartPrice.value < vc.giaTriDonHangToiThieu) return false
  return true
}

const getVoucherError = (vc) => {
  const now = new Date()
  if (vc.trangThai !== 1) return 'Voucher đang bị khóa'
  if (vc.ngayBatDau && new Date(vc.ngayBatDau) > now) return 'Chưa tới ngày áp dụng'
  if (vc.ngayKetThuc && new Date(vc.ngayKetThuc) < now) return 'Voucher đã hết hạn'
  if (vc.soLuongDaDung >= vc.soLuong) return 'Đã hết lượt sử dụng'
  if (totalCartPrice.value < vc.giaTriDonHangToiThieu)
    return `Đơn tối thiểu ${formatPrice(vc.giaTriDonHangToiThieu)}`
  return ''
}

const saveNewCustomer = async () => {
  if (!newCust.value.hoTen || !newCust.value.sdt)
    return toast.warning('Vui lòng nhập đầy đủ Tên và SĐT!')
  const phoneRegex = /^[0-9]{10}$/
  if (!phoneRegex.test(newCust.value.sdt))
    return toast.error('Số điện thoại phải gồm đúng 10 chữ số!')

  if (customers.value.some((kh) => kh.soDienThoai === newCust.value.sdt)) {
    return toast.error('Số điện thoại này đã tồn tại!')
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
    toast.error('Lỗi xảy ra khi lưu khách hàng!')
  }
}

const selectCustomer = async (kh) => {
  if (!hasCurrentOrder.value) return toast.error('Vui lòng chọn hoặc tạo hóa đơn trước!')
  try {
    await ganKhachHang(currentOrder.value.id, kh.id)
    selectedCustomer.value = kh
    showCustomerModal.value = false
    toast.success('Đã chọn khách hàng')
  } catch (error) {
    toast.error('Không thể gán khách hàng')
  }
}

// --- UTILS & EVENT HANDLERS ---
const getProductImage = (product) => {
  if (product?.image) return `http://localhost:8080${product.image}`
  if (product?.images?.length > 0) return `http://localhost:8080${product.images[0]}`
  return '/default-image.png'
}

const setDefaultImage = (event) => {
  event.target.src = DEFAULT_PRODUCT_IMAGE
}

const formatPrice = (value) =>
  new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(value || 0)

const resetFilters = () => {
  filterCategory.value = ''
  filterBrand.value = ''
  filterColor.value = ''
  filterSize.value = ''
  searchQuery.value = ''
}

const openDropdown = () => (isDropdownVisible.value = true)
const closeDropdown = () => setTimeout(() => (isDropdownVisible.value = false), 200)
const openCustomerModal = () => (showCustomerModal.value = true)

const handleCloseInvoice = () => {
  showInvoiceModal.value = false
  if (allOrders.value.length === 0) currentOrderIndex.value = -1
}

const handleKeyDown = async (e) => {
  if (e.ctrlKey && e.key.toLowerCase() === 'f') {
    e.preventDefault()
    searchInput.value?.focus()
    searchInput.value?.select()
  }

  if (e.key === 'F1') {
    e.preventDefault()
    createNewOrder()
  }

  if (e.ctrlKey && e.key.toLowerCase() === 'd') {
    e.preventDefault()
    if (!currentOrder.value?.id) return

    const result = await Swal.fire({
      title: 'Hủy hóa đơn?',
      html: `
      <div style="font-size:14px;color:#64748b">
        Hóa đơn hiện tại sẽ bị <b style="color:#dc2626">xóa vĩnh viễn</b>.<br>
        Hành động này không thể hoàn tác.
      </div>`,
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
      width: 420,
      customClass: {
        popup: 'rounded-3xl shadow-2xl',
        title: 'text-lg font-bold',
        confirmButton: 'px-4 py-2.5 rounded-xl',
        cancelButton: 'px-4 py-2.5 rounded-xl',
      },
    })

    if (result.isConfirmed) {
      await removeOrder(currentOrderIndex.value)
    }
  }
}

const handleClickOutside = (e) => {
  if (voucherRef.value && !voucherRef.value.contains(e.target)) {
    showVoucherDropdown.value = false
  }
}

// --- WATCHERS & LIFECYCLE HOOKS ---
watch(
  () => currentOrder.value,
  (order) => {
    if (order && !order.phuongThucThanhToan) {
      order.phuongThucThanhToan = defaultPTTTId.value
    }
  },
  { immediate: true, deep: true },
)

watch(totalCartPrice, () => {
  if (!appliedVoucher.value) return
  if (totalCartPrice.value < appliedVoucher.value.giaTriDonHangToiThieu) {
    toast.warning('Đơn hàng không còn đủ điều kiện áp dụng voucher')
    removeVoucher()
  }
})

onMounted(async () => {
  document.addEventListener('click', handleClickOutside)
  window.addEventListener('keydown', handleKeyDown)

  await loadAllDataFromAPI()
  const [voucherData, hoaDonData] = await Promise.all([getAllVoucher(), getHoaDonCho(user.id)])
  await loadPTTT()

  vouchers.value = voucherData
  allOrders.value = hoaDonData.map((hd) => ({
    id: hd.id,
    maHoaDon: hd.maHoaDon,
    cart: [],
    selectedCustomer: null,
    appliedVoucher: hd.voucher || null,
    voucherQuery: hd.voucher?.maVoucher || '',
    loaiHoaDon: 'tai_quay',
    phuongThucThanhToan: defaultPTTTId.value,
  }))

  if (allOrders.value.length > 0) {
    currentOrderIndex.value = 0
    await loadChiTietHoaDon(allOrders.value[0].id)
  }

  connectSocket()
})

onBeforeUnmount(() => {
  document.removeEventListener('click', handleClickOutside)
  window.removeEventListener('keydown', handleKeyDown)
  if (socketSubscription) {
    socketSubscription.unsubscribe()
  }
})
</script>

<style scoped>
.pos-wrapper {
  zoom: 0.82;
}

@supports not (zoom: 0.82) {
  .pos-wrapper {
    transform: scale(0.82);
    transform-origin: top left;
    width: 121.95%;
  }
}

.custom-scrollbar::-webkit-scrollbar {
  width: 4px;
  height: 4px;
}
.custom-scrollbar::-webkit-scrollbar-track {
  background: transparent;
}
.custom-scrollbar::-webkit-scrollbar-thumb {
  background: #cbd5e1;
  border-radius: 9999px;
}
.custom-scrollbar::-webkit-scrollbar-thumb:hover {
  background: #94a3b8;
}

.no-scrollbar::-webkit-scrollbar {
  display: none;
}
.no-scrollbar {
  -ms-overflow-style: none;
  scrollbar-width: none;
}

.swal2-popup {
  border-radius: 20px !important;
}
.swal2-confirm,
.swal2-cancel {
  border-radius: 10px !important;
  font-weight: 700 !important;
  font-size: 12px !important;
}
</style>
