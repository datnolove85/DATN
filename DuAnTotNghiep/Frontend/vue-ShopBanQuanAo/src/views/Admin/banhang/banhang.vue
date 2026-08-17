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
                @click="openVariantModalFromSearch(sp)"
                class="flex items-center gap-2.5 p-1.5 hover:bg-indigo-50/70 rounded-lg cursor-pointer transition-colors border-b border-slate-100 last:border-0"
              >
                <img
                  :src="getProductImage(sp)"
                  class="w-9 h-9 rounded-md object-cover border border-slate-100 flex-shrink-0"
                />
                <div class="flex-1 min-w-0">
                  <p class="font-bold text-xs text-slate-800 truncate">{{ sp.tenSanPham }}</p>
                  <p class="text-[10px] text-slate-400 font-mono">
                    {{ sp.tenMauSac }} - {{ sp.tenKichThuoc }}
                  </p>
                </div>
                <div class="text-right">
                  <template v-if="sp.dangGiamGia">
                    <p class="text-xs font-black text-rose-600">
                      {{ formatPrice(sp.giaSauGiam) }}
                    </p>
                    <div class="flex items-center justify-end gap-1">
                      <span
                        v-if="getVariantDiscountPercent(sp) > 0"
                        class="text-[9px] text-rose-600 font-bold"
                      >
                        -{{ getVariantDiscountPercent(sp) }}%
                      </span>
                      <span class="text-[10px] text-slate-400 line-through">
                        {{ formatPrice(sp.giaBan) }}
                      </span>
                    </div>
                  </template>
                  <template v-else>
                    <p class="text-xs font-black text-indigo-600">
                      {{ formatPrice(sp.giaBan) }}
                    </p>
                  </template>
                  <span class="text-[9px] text-emerald-600 font-medium block"
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

        <!-- Grid Sản Phẩm (Gom nhóm theo Sản phẩm gốc) -->
        <div class="grid grid-cols-2 sm:grid-cols-3 xl:grid-cols-4 2xl:grid-cols-5 gap-2.5">
          <div
            v-for="master in groupedMasterProducts"
            :key="master.tenSanPham"
            @click="openVariantModal(master)"
            :class="[
              'bg-white rounded-xl border p-2.5 flex flex-col justify-between transition-all duration-200 group relative overflow-hidden select-none cursor-pointer',
              master.totalStock > 0
                ? 'border-slate-200/80 hover:border-indigo-500 hover:shadow-md hover:-translate-y-0.5'
                : 'border-slate-200 bg-slate-50/80 opacity-60',
            ]"
          >
            <!-- Badge Giảm Giá (chỉ hiện khi có giảm giá và % > 0) -->
            <div
              v-if="master.hasDiscount && master.maxDiscountPercent > 0"
              class="absolute top-2 right-2 z-20"
            >
              <span
                class="bg-gradient-to-r from-amber-500 to-rose-500 text-white text-[10px] font-black px-2 py-0.5 rounded-full shadow-xs flex items-center gap-0.5"
              >
                🔥 -{{ master.maxDiscountPercent }}%
              </span>
            </div>

            <!-- Ảnh Sản Phẩm -->
            <div
              class="aspect-square w-full rounded-lg bg-slate-100 overflow-hidden relative mb-2 border border-slate-100"
            >
              <img
                :src="getProductImage(master.representativeProduct)"
                :alt="master.tenSanPham"
                class="w-full h-full object-cover group-hover:scale-105 transition-transform duration-300"
                @error="setDefaultImage"
              />

              <!-- Overlay hết hàng -->
              <div
                v-if="master.totalStock <= 0"
                class="absolute inset-0 bg-slate-900/40 backdrop-blur-[1px] flex items-center justify-center z-20"
              >
                <span
                  class="bg-white/95 text-rose-600 text-[9px] font-black px-2 py-0.5 rounded shadow-sm"
                >
                  HẾT HÀNG
                </span>
              </div>

              <!-- Hover Action Indicator -->
              <div
                v-if="master.totalStock > 0"
                class="absolute inset-0 bg-indigo-900/10 opacity-0 group-hover:opacity-100 transition-opacity flex items-center justify-center z-10"
              >
                <span
                  class="bg-indigo-600 text-white px-2.5 py-1 rounded-lg shadow-lg transform translate-y-2 group-hover:translate-y-0 transition-transform text-[11px] font-bold flex items-center gap-1"
                >
                  <span>Chọn phân loại</span>
                  <svg
                    xmlns="http://www.w3.org/2000/svg"
                    class="h-3 w-3"
                    fill="none"
                    viewBox="0 0 24 24"
                    stroke="currentColor"
                  >
                    <path
                      stroke-linecap="round"
                      stroke-linejoin="round"
                      stroke-width="2.5"
                      d="M9 5l7 7-7 7"
                    />
                  </svg>
                </span>
              </div>
            </div>

            <!-- Detail Product -->
            <div class="flex-1 flex flex-col justify-between">
              <div>
                <div class="flex items-center justify-between gap-1 mb-1">
                  <span
                    class="text-[9px] font-bold bg-slate-100 text-slate-600 px-1.5 py-0.5 rounded truncate"
                  >
                    {{ master.representativeProduct.tenThuongHieu || 'Thời trang' }}
                  </span>
                  <span
                    class="text-[9px] font-bold text-indigo-600 bg-indigo-50 px-1.5 py-0.5 rounded"
                  >
                    {{ master.variants.length }} phân loại
                  </span>
                </div>

                <h3
                  class="text-xs font-bold text-slate-800 line-clamp-2 leading-snug group-hover:text-indigo-600 transition-colors"
                  :title="master.tenSanPham"
                >
                  {{ master.tenSanPham }}
                </h3>
              </div>

              <!-- Price & Stock -->
              <div
                class="mt-2.5 pt-2 border-t border-slate-100 flex items-end justify-between gap-1"
              >
                <div>
                  <p class="text-xs font-black text-indigo-600 leading-none">
                    {{ master.priceFormatted }}
                  </p>
                </div>

                <span
                  :class="[
                    'text-[8px] px-1.5 py-0.5 rounded font-bold flex-shrink-0',
                    master.totalStock <= 0
                      ? 'bg-rose-50 text-rose-600'
                      : master.totalStock <= 10
                        ? 'bg-amber-50 text-amber-600'
                        : 'bg-emerald-50 text-emerald-600',
                  ]"
                >
                  {{ master.totalStock <= 0 ? 'Hết hàng' : 'Kho: ' + master.totalStock }}
                </span>
              </div>
            </div>
          </div>
        </div>
      </section>

      <!-- ================= BÊN PHẢI: GIỎ HÀNG & THANH TOÁN ================= -->
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
                @click="handleRemoveCustomer"
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
                <!-- Hiển thị giá trước và sau giảm trong giỏ hàng -->
                <div class="mt-1">
                  <template v-if="item.product.dangGiamGia">
                    <p class="text-xs font-black text-rose-600 flex items-center gap-1.5 flex-wrap">
                      <span>{{ formatPrice(item.product.giaSauGiam) }}</span>
                      <span
                        v-if="getVariantDiscountPercent(item.product) > 0"
                        class="text-[9px] bg-rose-100 text-rose-700 font-bold px-1 py-0.2 rounded"
                      >
                        -{{ getVariantDiscountPercent(item.product) }}%
                      </span>
                      <span class="text-[10px] text-slate-400 line-through font-normal">
                        {{ formatPrice(item.product.giaBan) }}
                      </span>
                    </p>
                  </template>
                  <template v-else>
                    <p class="text-xs font-black text-indigo-600">
                      {{ formatPrice(item.product.giaBan) }}
                    </p>
                  </template>
                </div>
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

          <!-- Checkout & Total Section -->
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
                    @click.stop="handleRemoveVoucher"
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
                  <div class="flex items-center gap-1.5 flex-wrap">
                    <span
                      v-if="bestVoucher?.id === vc.id"
                      class="bg-amber-100 text-amber-800 text-[10px] px-2 py-0.5 rounded-md font-bold"
                    >
                      ⭐ Khuyên dùng
                    </span>
                    <span
                      v-if="vc.isCustomerVoucher"
                      class="bg-purple-100 text-purple-700 text-[10px] px-2 py-0.5 rounded-md font-bold"
                    >
                      🎁 Voucher của khách
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
                      {{ vc.loaiGiamGia === 'phan_tram' ? 'Phần trăm (%)' : 'Tiền mặt' }}
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
                    <div
                      v-if="vc.giaTriGiamToiDa"
                      class="col-span-2 pt-1 border-t border-slate-200/60 mt-0.5"
                    >
                      Giảm tối đa:
                      <b class="text-slate-700">{{ formatPrice(vc.giaTriGiamToiDa) }}</b>
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

              <!-- Giao diện nhập tiền kết hợp -->
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

            <div
              v-if="!isMultiPayment && isCashPayment"
              class="bg-slate-50 p-2.5 rounded-xl border border-slate-200/80 space-y-2 mt-2"
            >
              <div>
                <label class="text-[10px] font-bold text-slate-500 block mb-0.5"
                  >TIỀN KHÁCH ĐƯA</label
                >
                <input
                  type="text"
                  v-model="displayTienKhachDua"
                  @input="onTienKhachDuaInput"
                  placeholder="Nhập số tiền khách đưa..."
                  class="w-full bg-white border border-slate-200 rounded-lg p-1.5 text-xs font-bold text-slate-800 outline-none focus:border-indigo-500"
                />
              </div>
              <div
                class="flex justify-between items-center text-xs pt-1 border-t border-slate-200/60"
              >
                <span class="font-bold text-slate-500">TIỀN THỐI LẠI:</span>
                <span class="font-black text-emerald-600 text-sm">{{
                  formatPrice(tienThoiLai)
                }}</span>
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

    <!-- ================= MODAL CHỌN PHÂN LOẠI (MÀU SẮC & KÍCH THƯỚC) ================= -->
    <div
      v-if="showVariantModal"
      class="fixed inset-0 bg-slate-900/50 backdrop-blur-xs flex items-center justify-center p-4 z-50 animate-in fade-in duration-200"
    >
      <div
        class="bg-white rounded-2xl max-w-lg w-full p-6 shadow-2xl border border-slate-100 flex flex-col max-h-[90vh]"
      >
        <div class="flex justify-between items-start mb-4 pb-3 border-b border-slate-100">
          <div>
            <span
              class="text-[10px] font-bold text-indigo-600 bg-indigo-50 px-2 py-0.5 rounded uppercase"
            >
              {{ activeMasterProduct?.representativeProduct?.tenThuongHieu }}
            </span>
            <h3 class="text-base font-black text-slate-900 mt-1">
              {{ activeMasterProduct?.tenSanPham }}
            </h3>
          </div>
          <button
            @click="showVariantModal = false"
            class="text-slate-400 hover:text-slate-600 font-bold text-lg p-1 rounded-full hover:bg-slate-100 transition-colors"
          >
            ✕
          </button>
        </div>

        <div class="space-y-4 overflow-y-auto pr-1 custom-scrollbar flex-1">
          <!-- Chọn Màu Sắc -->
          <div>
            <label class="text-xs font-bold text-slate-700 block mb-2">1. Chọn Màu Sắc</label>
            <div class="flex flex-wrap gap-2">
              <button
                v-for="color in availableColorsForActiveMaster"
                :key="color.idMauSac"
                @click="selectedColorId = color.idMauSac"
                :class="[
                  'px-3.5 py-2 rounded-xl text-xs font-bold border transition-all',
                  selectedColorId === color.idMauSac
                    ? 'bg-indigo-600 text-white border-indigo-600 shadow-sm'
                    : 'bg-white text-slate-700 border-slate-200 hover:border-indigo-300 hover:bg-slate-50',
                ]"
              >
                {{ color.tenMauSac }}
              </button>
            </div>
          </div>

          <!-- Chọn Kích Thước -->
          <div>
            <label class="text-xs font-bold text-slate-700 block mb-2">2. Chọn Kích Thước</label>
            <div class="flex flex-wrap gap-2">
              <button
                v-for="size in availableSizesForSelectedColor"
                :key="size.idKichThuoc"
                @click="selectedSizeId = size.idKichThuoc"
                :class="[
                  'px-3.5 py-2 rounded-xl text-xs font-bold border transition-all',
                  selectedSizeId === size.idKichThuoc
                    ? 'bg-indigo-600 text-white border-indigo-600 shadow-sm'
                    : size.soKhaDung <= 0
                      ? 'bg-slate-100 text-slate-400 border-slate-200 opacity-60 cursor-not-allowed'
                      : 'bg-white text-slate-700 border-slate-200 hover:border-indigo-300 hover:bg-slate-50',
                ]"
                :disabled="size.soKhaDung <= 0"
              >
                {{ size.tenKichThuoc }}
                <span class="text-[10px] ml-1 opacity-80">({{ size.soKhaDung }})</span>
              </button>
            </div>
          </div>

          <!-- Thông tin biến thể đang chọn (Hiển thị giá trước & sau giảm rõ ràng) -->
          <div
            v-if="selectedVariant"
            class="bg-indigo-50/70 p-4 rounded-xl border border-indigo-100 flex items-center justify-between mt-2"
          >
            <div>
              <p class="text-[11px] text-slate-500 font-medium">
                Mã SPCT:
                <span class="font-mono font-bold text-slate-700">{{
                  selectedVariant.maSanPhamChiTiet
                }}</span>
              </p>
              <div class="mt-1 flex items-center gap-2">
                <span class="text-xs font-bold text-indigo-700"
                  >🎨 {{ selectedVariant.tenMauSac }}</span
                >
                <span class="text-xs font-bold text-indigo-700"
                  >📏 {{ selectedVariant.tenKichThuoc }}</span
                >
              </div>
            </div>
            <div class="text-right">
              <template v-if="selectedVariant.dangGiamGia">
                <p class="text-sm font-black text-rose-600">
                  {{ formatPrice(selectedVariant.giaSauGiam) }}
                </p>
                <div class="flex items-center justify-end gap-1.5 mt-0.5">
                  <span
                    v-if="getVariantDiscountPercent(selectedVariant) > 0"
                    class="bg-rose-100 text-rose-700 text-[10px] font-bold px-1.5 py-0.2 rounded"
                  >
                    -{{ getVariantDiscountPercent(selectedVariant) }}%
                  </span>
                  <span class="text-xs text-slate-400 line-through font-medium">
                    {{ formatPrice(selectedVariant.giaBan) }}
                  </span>
                </div>
              </template>
              <template v-else>
                <p class="text-sm font-black text-indigo-600">
                  {{ formatPrice(selectedVariant.giaBan) }}
                </p>
              </template>
              <span class="text-[10px] font-bold text-emerald-600 block mt-1"
                >Kho: {{ selectedVariant.soLuongKhaDung ?? selectedVariant.soLuongTon }}</span
              >
            </div>
          </div>
        </div>

        <div class="pt-4 border-t border-slate-100 mt-4 flex gap-3">
          <button
            @click="showVariantModal = false"
            class="flex-1 bg-slate-100 hover:bg-slate-200 text-slate-700 font-bold py-2.5 rounded-xl text-xs transition-colors"
          >
            Hủy bỏ
          </button>
          <button
            @click="confirmAddVariantToCart"
            :disabled="
              !selectedVariant ||
              (selectedVariant.soLuongKhaDung ?? selectedVariant.soLuongTon) <= 0
            "
            class="flex-1 bg-indigo-600 hover:bg-indigo-700 disabled:opacity-50 disabled:cursor-not-allowed text-white font-bold py-2.5 rounded-xl text-xs shadow-md shadow-indigo-200 transition-all flex items-center justify-center gap-1.5"
          >
            <span>Thêm vào giỏ hàng</span>
          </button>
        </div>
      </div>
    </div>

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
  goKhachHang,
} from '@/service/HoaDonService'

const toast = useToast()
const DEFAULT_PRODUCT_IMAGE = 'https://images.unsplash.com/photo-1523381210434-271e8be1f52b?w=400'
const max_oder_waiting = 6
const user = JSON.parse(sessionStorage.getItem('user')) || {}

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

// --- STATE MODAL CHỌN PHÂN LOẠI ---
const showVariantModal = ref(false)
const activeMasterProduct = ref(null)
const selectedColorId = ref(null)
const selectedSizeId = ref(null)

const isMultiPayment = ref(false)
const paymentCash = ref(0)
const paymentBank = ref(0)
const pendingCheckoutPayload = ref(null)

const tienKhachDua = ref(0)
const displayTienKhachDua = ref('')

// Helper tính phần trăm giảm giá chính xác (ưu tiên phanTramGiam từ API, nếu không có tự tính dựa trên giaBan và giaSauGiam)
const getVariantDiscountPercent = (v) => {
  if (!v.dangGiamGia) return 0
  if (v.phanTramGiam && v.phanTramGiam > 0) return v.phanTramGiam
  if (v.giaBan > 0 && v.giaSauGiam < v.giaBan) {
    return Math.round(((v.giaBan - v.giaSauGiam) / v.giaBan) * 100)
  }
  return 0
}

const handleBankInput = () => {
  const bank = Number(paymentBank.value) || 0
  const needed = totalNeedPay.value
  const cash = needed - bank
  paymentCash.value = cash > 0 ? cash : 0
  displayCash.value = formatCurrencyInput(paymentCash.value)
}

const resetPaymentForm = () => {
  isMultiPayment.value = false
  paymentCash.value = 0
  paymentBank.value = 0
  tienKhachDua.value = 0
  displayTienKhachDua.value = ''
  displayCash.value = ''
  displayBank.value = ''

  const cashMethod = ptttList.value.find(
    (p) =>
      ['CASH', 'TIEN_MAT'].includes(p.maPhuongThuc?.toUpperCase()) ||
      p.tenPhuongThuc?.toLowerCase().includes('tiền mặt'),
  )
  const cashId = cashMethod ? cashMethod.id : defaultPTTTId.value

  if (currentOrder.value) {
    currentOrder.value.phuongThucThanhToan = cashId
  }
}

const isCashPayment = computed(() => {
  const selectedPttt = ptttList.value.find((p) => p.id === Number(phuongThucThanhToan.value))
  if (!selectedPttt) return true
  const code = selectedPttt.maPhuongThuc?.toUpperCase()
  const name = selectedPttt.tenPhuongThuc?.toLowerCase() || ''
  return ['CASH', 'TIEN_MAT'].includes(code) || name.includes('tiền mặt')
})

const tienThoiLai = computed(() => {
  const khachDua = Number(tienKhachDua.value) || 0
  const canTra = totalNeedPay.value
  return khachDua > canTra ? khachDua - canTra : 0
})

const onTienKhachDuaInput = (e) => {
  const rawValue = e.target.value.replace(/\D/g, '')
  const numVal = rawValue ? Number(rawValue) : 0
  tienKhachDua.value = numVal
  displayTienKhachDua.value = formatCurrencyInput(numVal)
}

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

const pendingPaidOrderId = ref(null)

const showQrDialog = ref(false)
const qrData = ref({
  qrUrl: '',
  maHoaDon: '',
  tongTien: 0,
})

const totalNeedPay = computed(() => {
  return currentOrder.value?.tongThanhToan || totalCartPrice.value - voucherDiscount.value
})

const mapKhoVoucherToStandardVoucher = (khoVc) => {
  return {
    id: khoVc.id,
    idVoucher: khoVc.idVoucher || khoVc.voucherId || khoVc.idVoucherGoc,
    idVoucherKhachHang: khoVc.idVoucherKhachHang || khoVc.id,
    maCode: khoVc.maCode,
    maVoucher: khoVc.maCode,
    tenVoucher: khoVc.tenVoucher,
    loaiGiamGia: khoVc.loaiGiamGia,
    giaTriGiam: khoVc.giaTriGiam,
    giaTriDonHangToiThieu: khoVc.dieuKienToiThieu,
    giaTriGiamToiDa: khoVc.giaTriGiamToiDa,
    soLuong: khoVc.soLuongConLai,
    soLuongDaDung: 0,
    ngayBatDau: khoVc.ngayBatDau,
    ngayKetThuc: khoVc.ngayHetHan,
    moTa: khoVc.moTa || `Đổi bằng ${khoVc.soXuDoi} xu`,
    trangThai: khoVc.trangThai ? 1 : 0,
    isCustomerVoucher: true,
  }
}

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

// --- GOM NHÓM SẢN PHẨM THEO SẢN PHẨM GỐC ---
const groupedMasterProducts = computed(() => {
  const map = new Map()

  filteredProducts.value.forEach((sp) => {
    const key = sp.tenSanPham || 'Khác'
    if (!map.has(key)) {
      map.set(key, {
        tenSanPham: key,
        representativeProduct: sp,
        variants: [],
        totalStock: 0,
        minPrice: Infinity,
        maxPrice: 0,
        hasDiscount: false,
        maxDiscountPercent: 0,
      })
    }

    const group = map.get(key)
    group.variants.push(sp)

    const stock = sp.soLuongKhaDung ?? sp.soLuongTon ?? 0
    group.totalStock += stock

    const price = sp.dangGiamGia ? sp.giaSauGiam : sp.giaBan
    if (price < group.minPrice) group.minPrice = price
    if (price > group.maxPrice) group.maxPrice = price

    if (sp.dangGiamGia) {
      group.hasDiscount = true
      const percent = getVariantDiscountPercent(sp)
      if (percent > group.maxDiscountPercent) {
        group.maxDiscountPercent = percent
      }
    }
  })

  return Array.from(map.values()).map((group) => {
    let priceFormatted = ''
    if (group.minPrice === group.maxPrice || group.minPrice === Infinity) {
      priceFormatted = formatPrice(group.minPrice === Infinity ? 0 : group.minPrice)
    } else {
      priceFormatted = `${formatPrice(group.minPrice)} - ${formatPrice(group.maxPrice)}`
    }
    return {
      ...group,
      priceFormatted,
    }
  })
})

// --- LOGIC CHO MODAL CHỌN PHÂN LOẠI ---
const openVariantModal = (master) => {
  activeMasterProduct.value = master
  const firstColor = availableColorsForActiveMaster.value[0]
  selectedColorId.value = firstColor ? firstColor.idMauSac : null

  const firstValidSize =
    availableSizesForSelectedColor.value.find((s) => s.soKhaDung > 0) ||
    availableSizesForSelectedColor.value[0]
  selectedSizeId.value = firstValidSize ? firstValidSize.idKichThuoc : null

  showVariantModal.value = true
}

const openVariantModalFromSearch = (sp) => {
  const master = groupedMasterProducts.value.find((m) => m.tenSanPham === sp.tenSanPham)
  if (master) {
    openVariantModal(master)
    selectedColorId.value = sp.idMauSac
    selectedSizeId.value = sp.idKichThuoc
  }
}

const availableColorsForActiveMaster = computed(() => {
  if (!activeMasterProduct.value) return []
  const colorMap = new Map()
  activeMasterProduct.value.variants.forEach((v) => {
    if (!colorMap.has(v.idMauSac)) {
      colorMap.set(v.idMauSac, {
        idMauSac: v.idMauSac,
        tenMauSac: v.tenMauSac,
      })
    }
  })
  return Array.from(colorMap.values())
})

const availableSizesForSelectedColor = computed(() => {
  if (!activeMasterProduct.value || !selectedColorId.value) return []
  return activeMasterProduct.value.variants
    .filter((v) => Number(v.idMauSac) === Number(selectedColorId.value))
    .map((v) => ({
      idKichThuoc: v.idKichThuoc,
      tenKichThuoc: v.tenKichThuoc,
      soKhaDung: v.soLuongKhaDung ?? v.soLuongTon ?? 0,
    }))
})

watch(selectedColorId, () => {
  const firstValidSize = availableSizesForSelectedColor.value.find((s) => s.soKhaDung > 0)
  if (firstValidSize) {
    selectedSizeId.value = firstValidSize.idKichThuoc
  } else if (availableSizesForSelectedColor.value.length > 0) {
    selectedSizeId.value = availableSizesForSelectedColor.value[0].idKichThuoc
  }
})

const selectedVariant = computed(() => {
  if (!activeMasterProduct.value || !selectedColorId.value || !selectedSizeId.value) return null
  return (
    activeMasterProduct.value.variants.find(
      (v) =>
        Number(v.idMauSac) === Number(selectedColorId.value) &&
        Number(v.idKichThuoc) === Number(selectedSizeId.value),
    ) || null
  )
})

const confirmAddVariantToCart = async () => {
  if (!selectedVariant.value) return
  showVariantModal.value = false
  await addToCart(selectedVariant.value)
}

const filteredCustomers = computed(() => {
  return customers.value.filter(
    (kh) =>
      kh.hoTen.toLowerCase().includes(searchCustomerQuery.value.toLowerCase()) ||
      kh.soDienThoai.includes(searchCustomerQuery.value),
  )
})
const customerVouchers = ref([])

const filteredVouchers = computed(() => {
  const combinedMap = new Map()
  vouchers.value.forEach((vc) => combinedMap.set(`sys_${vc.id}`, vc))
  customerVouchers.value.forEach((vc) => combinedMap.set(`cust_${vc.id}`, vc))

  const list = Array.from(combinedMap.values())
  list.sort((a, b) => {
    const validA = isVoucherValid(a)
    const validB = isVoucherValid(b)
    if (validA !== validB) return Number(validB) - Number(validA)
    return tinhTienGiam(b) - tinhTienGiam(a)
  })
  return list
})

const bestVoucher = computed(() => filteredVouchers.value.find(isVoucherValid) ?? null)

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

    order.cart = data.sanPhams.map((item) => {
      const foundSp = products.value.find(
        (p) =>
          Number(p.id) === Number(item.idSanPhamChiTiet) ||
          Number(p.idSanPhamChiTiet) === Number(item.idSanPhamChiTiet),
      )

      return {
        id: item.id,
        product: {
          idSanPhamChiTiet: item.idSanPhamChiTiet,
          maSPCT: item.maSanPhamChiTiet,
          id: item.idSanPhamChiTiet,
          tenSanPhamChiTiet: item.tenSanPham,
          giaBan: foundSp ? foundSp.giaBan : item.donGia,
          giaSauGiam: foundSp ? foundSp.giaSauGiam : item.donGia,
          dangGiamGia: foundSp ? foundSp.dangGiamGia : false,
          phanTramGiam: foundSp ? foundSp.phanTramGiam : 0,
          tenMauSac: item.tenMauSac,
          tenKichThuoc: item.tenKichThuoc,
          image: item.anh,
        },
        soLuong: item.soLuong,
        thanhTien: item.thanhTien,
      }
    })

    order.cart.forEach((item) => {
      editingQty[item.id] = item.soLuong
    })

    order.appliedVoucher = data.voucher || null
    order.voucherQuery = data.voucher?.maVoucher || ''
    selectedVoucher.value = data.voucher || null
    appliedVoucher.value = data.voucher || null
    voucherQuery.value = data.voucher?.maVoucher || data.voucher?.maCode || ''
    voucherCode.value = data.voucher?.maVoucher || data.voucher?.maCode || ''
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

    if (data.idKhachHang) {
      try {
        const res = await ganKhachHang(idHoaDon, data.idKhachHang)
        let rawData = res.data !== undefined ? res.data : res
        if (typeof rawData === 'string') {
          try {
            rawData = JSON.parse(rawData)
          } catch (e) {
            rawData = []
          }
        }
        if (Array.isArray(rawData) && rawData.length > 0) {
          customerVouchers.value = rawData.map(mapKhoVoucherToStandardVoucher)
        } else {
          customerVouchers.value = []
        }
      } catch (err) {
        customerVouchers.value = []
      }
    } else {
      customerVouchers.value = []
    }

    allOrders.value = [...allOrders.value]
  } catch (error) {
    console.error(error)
  }
}

const loadProducts = async () => {
  products.value = await getAllSanPhamChiTiet()
}

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
        } catch (e) {}

        const eventType = isJson ? data.type || data.eventType || data.action : ''

        if (eventType === 'QUANTITY_UPDATED') return
        if (eventType === 'VOUCHER_UPDATED') {
          vouchers.value = await getAllVoucher()
          if (currentOrder.value?.id) await loadChiTietHoaDon(currentOrder.value.id)
          return
        }

        if (eventType === 'DISCOUNT_UPDATED') {
          await loadProducts()
          if (currentOrder.value?.id) {
            await loadChiTietHoaDon(currentOrder.value.id)
          }
          toast.info('Đợt giảm giá vừa được cập nhật!')
          return
        }

        if (eventType === 'VOUCHER_REMOVED') {
          vouchers.value = await getAllVoucher()

          if (currentOrder.value?.id) {
            await loadChiTietHoaDon(currentOrder.value.id)
          }

          toast.warning('Voucher đã bị xóa. Danh sách voucher đã được cập nhật!')
          return
        }
        if (eventType === 'KHO_VOUCHER_UPDATED') {
          vouchers.value = await getAllVoucher()
          if (currentOrder.value?.id) await loadChiTietHoaDon(currentOrder.value.id)
          return
        }
        if (eventType === 'PRODUCT_UPDATED') {
          toast.warning('Thông tin sản phẩm đã thay đổi. Vui lòng kiểm tra lại!')
          return
        }
        if (eventType === 'INVOICE_PAID') {
          toast.success('Thanh toán thành công!')
          return
        }
        if (eventType === 'ORDER_CANCELLED') {
          toast.warning(data.message)
          const hoaDonData = await getHoaDonCho(user.id)
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
          return
        }

        const noiDungThongBao = isJson
          ? data.message || data.noiDung || data.content || msg.body
          : msg.body

        if (noiDungThongBao) toast.info(noiDungThongBao)
      }
    } catch (error) {
      console.error('Lỗi socket:', error)
    }
  })
}

const displayCash = ref('')
const displayBank = ref('')

const formatCurrencyInput = (value) => {
  if (!value && value !== 0) return ''
  const numericValue = String(value).replace(/\D/g, '')
  if (!numericValue) return ''
  return new Intl.NumberFormat('vi-VN').format(Number(numericValue))
}

const onCashInput = (e) => {
  const rawValue = e.target.value.replace(/\D/g, '')
  let numVal = rawValue ? Number(rawValue) : 0
  const needed = totalNeedPay.value

  if (numVal > needed) {
    numVal = needed
    toast.warning('Số tiền nhập không được vượt quá tổng hóa đơn!')
  }

  paymentCash.value = numVal
  displayCash.value = formatCurrencyInput(numVal)

  const bank = needed - numVal
  paymentBank.value = bank > 0 ? bank : 0
  displayBank.value = formatCurrencyInput(paymentBank.value)
}

const onBankInput = (e) => {
  const rawValue = e.target.value.replace(/\D/g, '')
  let numVal = rawValue ? Number(rawValue) : 0
  const needed = totalNeedPay.value

  if (numVal > needed) {
    numVal = needed
    toast.warning('Số tiền nhập không được vượt quá tổng hóa đơn!')
  }

  paymentBank.value = numVal
  displayBank.value = formatCurrencyInput(numVal)

  const cash = needed - numVal
  paymentCash.value = cash > 0 ? cash : 0
  displayCash.value = formatCurrencyInput(cash)
}

const handlePaymentMethod = async () => {
  const pttt = ptttList.value.find((p) => p.id === Number(phuongThucThanhToan.value))
  if (!pttt) return

  const code = pttt.maPhuongThuc?.toUpperCase()

  if (code === 'MIXED' || code === 'KET_HOP') {
    isMultiPayment.value = true
    const totalNeedPayVal = totalNeedPay.value
    paymentCash.value = totalNeedPayVal
    paymentBank.value = 0

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
      pendingCheckoutPayload.value = {
        idHoaDon: currentOrder.value.id,
        idVoucher: appliedVoucher.value
          ? appliedVoucher.value.isCustomerVoucher
            ? appliedVoucher.value.idVoucher
            : appliedVoucher.value.id
          : null,
        idVoucherKhachHang: appliedVoucher.value?.isCustomerVoucher
          ? appliedVoucher.value.idVoucherKhachHang
          : null,
        danhSachThanhToan: [
          {
            idPhuongThucThanhToan: pttt.id,
            soTien: transferAmount,
            maGiaoDich: 'QR-' + Date.now(),
          },
        ],
      }

      const data = await taoQr(currentOrder.value.id, transferAmount)
      qrData.value = { ...data, tongTien: transferAmount }
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

  if (!isMultiPayment.value && isCashPayment.value) {
    const khachDua = Number(tienKhachDua.value) || 0
    if (khachDua < tongTienCanThanhToan) {
      return toast.error(
        `Số tiền khách đưa (${formatPrice(khachDua)}) còn thiếu so với tổng cần trả (${formatPrice(tongTienCanThanhToan)})!`,
      )
    }
  }

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
      if (!cashMethod) return toast.error('Không tìm thấy phương thức Tiền mặt!')
      danhSachThanhToanPayload.push({
        idPhuongThucThanhToan: cashMethod.id,
        soTien: tienMat,
        maGiaoDich: null,
      })
    }

    if (chuyenKhoan > 0) {
      if (!bankMethod) return toast.error('Không tìm thấy phương thức Chuyển khoản!')
      danhSachThanhToanPayload.push({
        idPhuongThucThanhToan: bankMethod.id,
        soTien: chuyenKhoan,
        maGiaoDich: 'CK-' + Date.now(),
      })
    }
  } else if (selectedCode === 'BANK' || selectedCode === 'CHUYEN_KHOAN') {
    if (!bankMethod) return toast.error('Không tìm thấy phương thức Chuyển khoản!')
    danhSachThanhToanPayload.push({
      idPhuongThucThanhToan: bankMethod.id,
      soTien: tongTienCanThanhToan,
      maGiaoDich: 'CK-' + Date.now(),
    })
  } else {
    if (!phuongThucThanhToan.value) return toast.error('Vui lòng chọn phương thức thanh toán!')
    if (!cashMethod) return toast.error('Không tìm thấy phương thức Tiền mặt!')
    danhSachThanhToanPayload.push({
      idPhuongThucThanhToan: parseInt(phuongThucThanhToan.value),
      soTien: tongTienCanThanhToan > 0 ? tongTienCanThanhToan : 0,
      maGiaoDich: null,
    })
  }

  const payload = {
    idHoaDon: currentOrder.value.id,
    idVoucher: appliedVoucher.value
      ? appliedVoucher.value.isCustomerVoucher
        ? appliedVoucher.value.idVoucher
        : appliedVoucher.value.id
      : null,
    idVoucherKhachHang: appliedVoucher.value?.isCustomerVoucher
      ? appliedVoucher.value.idVoucherKhachHang
      : null,
    danhSachThanhToan: danhSachThanhToanPayload,
    tienKhachDua: isCashPayment.value ? Number(tienKhachDua.value) : tongTienCanThanhToan,
    tienThoi: isCashPayment.value ? Number(tienThoiLai.value) : 0,
  }

  const hasBankPayment = danhSachThanhToanPayload.some(
    (item) => item.idPhuongThucThanhToan === bankMethod?.id,
  )

  if (hasBankPayment) {
    pendingCheckoutPayload.value = payload
    try {
      const transferAmount = isMultiPayment.value ? Number(paymentBank.value) : tongTienCanThanhToan
      const data = await taoQr(currentOrder.value.id, transferAmount)
      qrData.value = { ...data, tongTien: transferAmount }
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
    pendingPaidOrderId.value = currentOrder.value?.id
    showInvoiceModal.value = true

    allOrders.value = allOrders.value.filter((o) => o.id !== currentOrder.value.id)

    selectedVoucher.value = null
    appliedVoucher.value = null
    voucherQuery.value = ''
    voucherCode.value = ''
    selectedCustomer.value = null
    customerVouchers.value = []
    vouchers.value = await getAllVoucher()
    isMultiPayment.value = false
    paymentCash.value = 0
    paymentBank.value = 0
    pendingCheckoutPayload.value = null
    tienKhachDua.value = 0
    displayTienKhachDua.value = ''

    if (allOrders.value.length > 0) {
      currentOrderIndex.value = 0
      await loadChiTietHoaDon(allOrders.value[0].id)
      resetPaymentForm()
      const order = allOrders.value[0]
      selectedCustomer.value = order.selectedCustomer
      appliedVoucher.value = order.appliedVoucher
      selectedVoucher.value = order.appliedVoucher
      voucherQuery.value = order.voucherQuery || ''
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
    } catch (syncErr) {}
  }
}

const handleCloseInvoice = () => {
  showInvoiceModal.value = false
}

const xacNhanDaThanhToan = async () => {
  showQrDialog.value = false
  if (pendingCheckoutPayload.value) {
    await executeFinalCheckout(pendingCheckoutPayload.value)
  } else {
    toast.error('Không tìm thấy thông tin hóa đơn thanh toán!')
  }
}

const addToCart = async (product) => {
  try {
    if (!currentOrder.value?.id) {
      return toast.error('Chưa có hóa đơn')
    }

    const payload = {
      idHoaDon: currentOrder.value.id,
      idSanPhamChiTiet: product.idSanPhamChiTiet || product.id,
      soLuong: 1,
    }

    await themSanPhamVaoHoaDon(payload)

    // Thêm thành công thì reload chi tiết hóa đơn
    await loadChiTietHoaDon(currentOrder.value.id)

    toast.success('Đã thêm sản phẩm vào giỏ')
  } catch (error) {
    console.error('Lỗi thêm sản phẩm:', error)

    const errorMessage = error?.response?.data?.message || error?.message || ''

    // BE báo không đủ số lượng khả dụng
    if (errorMessage.includes('không đủ số lượng') || errorMessage.includes('số lượng khả dụng')) {
      // Reload lại danh sách sản phẩm
      // để cập nhật số lượng tồn / số lượng khả dụng
      await loadProducts()

      toast.warning('Sản phẩm không đủ số lượng khả dụng. Danh sách sản phẩm đã được cập nhật!')

      return
    }

    toast.error(errorMessage || 'Không thể thêm sản phẩm')
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

  try {
    await capNhatSoLuong(item.id, newQty)
    await Promise.all([loadChiTietHoaDon(currentOrder.value.id), loadProducts()])
  } catch (error) {
    toast.error('Không thể cập nhật số lượng')
    await loadChiTietHoaDon(currentOrder.value.id)
  }
}, 600)

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
  if (!order?.id) return
  try {
    await huyHoaDon(order.id)
    allOrders.value.splice(index, 1)
    if (allOrders.value.length > 0) {
      currentOrderIndex.value = Math.min(index, allOrders.value.length - 1)
      await loadChiTietHoaDon(allOrders.value[currentOrderIndex.value].id)
    } else {
      allOrders.value = []
      currentOrderIndex.value = -1
    }
    toast.success('Đã hủy hóa đơn')
  } catch (e) {
    toast.error('Hủy hóa đơn thất bại')
  }
}

const tinhTienGiam = (vc) => {
  if (!isVoucherValid(vc)) return 0
  if (vc.loaiGiamGia === 'tien_mat') return vc.giaTriGiam
  let giam = (totalCartPrice.value * vc.giaTriGiam) / 100
  if (vc.giaTriGiamToiDa) giam = Math.min(giam, vc.giaTriGiamToiDa)
  return giam
}

const selectVoucher = async (voucher) => {
  if (!isVoucherValid(voucher)) return
  try {
    let payloadApVoucher = {}
    if (voucher.isCustomerVoucher) {
      payloadApVoucher = {
        idHoaDon: currentOrder.value.id,
        idVoucher: null,
        idVoucherKhachHang: voucher.idVoucherKhachHang,
      }
    } else {
      payloadApVoucher = {
        idHoaDon: currentOrder.value.id,
        idVoucher: voucher.id ? Number(voucher.id) : null,
        idVoucherKhachHang: null,
      }
    }

    await apVoucher(
      payloadApVoucher.idHoaDon,
      payloadApVoucher.idVoucher,
      payloadApVoucher.idVoucherKhachHang,
    )
    await loadChiTietHoaDon(currentOrder.value.id)

    selectedVoucher.value = voucher
    appliedVoucher.value = voucher
    showVoucherDropdown.value = false
    toast.success('Đã áp dụng voucher')
  } catch (error) {
    toast.error('Không thể áp dụng voucher')
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
  if (vc.trangThai !== 1) return 'Voucher đang bị khóa'
  if (totalCartPrice.value < vc.giaTriDonHangToiThieu)
    return `Đơn tối thiểu ${formatPrice(vc.giaTriDonHangToiThieu)}`
  return ''
}

const saveNewCustomer = async () => {
  if (!newCust.value.hoTen || !newCust.value.sdt)
    return toast.warning('Vui lòng nhập đầy đủ Tên và SĐT!')
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
  } catch (e) {
    toast.error('Lỗi khi lưu khách hàng!')
  }
}

const selectCustomer = async (kh) => {
  if (!hasCurrentOrder.value) return toast.error('Vui lòng chọn hoặc tạo hóa đơn trước!')
  try {
    const res = await ganKhachHang(currentOrder.value.id, kh.id)
    selectedCustomer.value = kh
    let rawData = res.data !== undefined ? res.data : res
    if (typeof rawData === 'string') {
      try {
        rawData = JSON.parse(rawData)
      } catch (e) {
        rawData = []
      }
    }
    if (Array.isArray(rawData) && rawData.length > 0) {
      customerVouchers.value = rawData.map(mapKhoVoucherToStandardVoucher)
    } else {
      customerVouchers.value = []
    }
    showCustomerModal.value = false
    toast.success('Đã chọn khách hàng thành công!')
  } catch (e) {
    toast.error('Không thể gán khách hàng')
  }
}

const getProductImage = (product) => {
  if (product?.image) return `http://localhost:8080${product.image}`
  if (product?.images?.length > 0) return `http://localhost:8080${product.images[0]}`
  return DEFAULT_PRODUCT_IMAGE
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

const handleRemoveCustomer = async () => {
  if (!currentOrder.value?.id) return
  try {
    await goKhachHang(currentOrder.value.id)
    await boVoucher(currentOrder.value.id)
    selectedCustomer.value = null
    customerVouchers.value = []
    selectedVoucher.value = null
    appliedVoucher.value = null
    toast.success('Đã gỡ khách hàng')
  } catch (e) {
    toast.error('Gỡ khách hàng thất bại')
  }
}

const handleRemoveVoucher = async () => {
  if (!currentOrder.value?.id) return
  try {
    await boVoucher(currentOrder.value.id)
    await loadChiTietHoaDon(currentOrder.value.id)
    selectedVoucher.value = null
    appliedVoucher.value = null
    toast.success('Đã gỡ voucher')
  } catch (e) {
    toast.error('Gỡ voucher thất bại')
  }
}

const handleKeyDown = (e) => {
  if (e.key === 'F1') {
    e.preventDefault()
    createNewOrder()
  }

  if ((e.ctrlKey || e.metaKey) && e.key.toLowerCase() === 'f') {
    e.preventDefault()
    if (searchInput.value) {
      searchInput.value.focus()
      openDropdown()
    }
  }

  if ((e.ctrlKey || e.metaKey) && e.key.toLowerCase() === 'd') {
    e.preventDefault()
    if (allOrders.value.length > 0 && currentOrderIndex.value >= 0) {
      removeOrder(currentOrderIndex.value)
    }
  }
}

// Xử lý click ra ngoài để đóng dropdown voucher
const handleClickOutside = (event) => {
  if (voucherRef.value && !voucherRef.value.contains(event.target)) {
    showVoucherDropdown.value = false
  }
}

onMounted(async () => {
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
  window.addEventListener('keydown', handleKeyDown)
  document.addEventListener('click', handleClickOutside)
})

onBeforeUnmount(() => {
  if (socketSubscription) socketSubscription.unsubscribe()
  window.removeEventListener('keydown', handleKeyDown)
  document.removeEventListener('click', handleClickOutside)
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
.custom-scrollbar::-webkit-scrollbar-thumb {
  background: #cbd5e1;
  border-radius: 9999px;
}
.no-scrollbar::-webkit-scrollbar {
  display: none;
}
</style>
