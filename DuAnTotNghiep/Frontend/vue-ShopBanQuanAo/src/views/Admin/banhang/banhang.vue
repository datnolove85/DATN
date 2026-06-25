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
            @click="sp.soLuongTon > 0 && sp.trangThai ? addToCart(sp) : null"
            :class="[
              'bg-white rounded-2xl border p-3 flex flex-col justify-between transition-all group relative overflow-hidden select-none',
              sp.soLuongTon > 0 && sp.trangThai
                ? 'border-slate-200 hover:border-indigo-500 hover:shadow-lg cursor-pointer'
                : 'border-slate-200 bg-slate-50 opacity-60 cursor-not-allowed',
            ]"
          >
            <span
              v-if="sp.giaGoc > sp.giaBan"
              class="absolute top-2 left-2 z-10 bg-rose-500 text-white text-[10px] font-black px-2 py-0.5 rounded-lg shadow-sm"
            >
              GIẢM {{ Math.round(((sp.giaGoc - sp.giaBan) / sp.giaGoc) * 100) }}%
            </span>

            <div class="aspect-square w-full rounded-xl bg-slate-100 overflow-hidden relative mb-3">
              <img
                :src="getProductImage(sp)"
                alt="Product image"
                class="w-full h-full object-cover group-hover:scale-105 transition-transform duration-300 relative z-10"
                @error="setDefaultImage"
              />

              <div
                v-if="sp.trangThai === false || sp.soLuongTon <= 0"
                class="absolute inset-0 bg-black/40 flex items-center justify-center z-20"
              >
                <span class="bg-white text-rose-600 text-xs font-black px-3 py-1 rounded-lg">
                  {{ sp.trangThai === false ? 'KHÔNG KHẢ DỤNG' : 'HẾT HÀNG' }}
                </span>
              </div>
              <div
                v-if="sp.soLuongTon > 0 && sp.trangThai"
                class="absolute inset-0 bg-black bg-opacity-0 group-hover:bg-opacity-10 flex items-center justify-center transition-all"
              >
                <span
                  class="bg-indigo-600 text-white p-2 rounded-full opacity-0 group-hover:opacity-100 shadow-md transform translate-y-2 group-hover:translate-y-0 transition-all"
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

            <div class="flex-1 flex flex-col justify-between">
              <div>
                <div class="flex items-center justify-between gap-1">
                  <span
                    class="text-[9px] font-bold text-slate-400 uppercase tracking-wider truncate"
                  >
                    {{ sp.maSanPhamChiTiet }}
                  </span>
                  <span
                    class="text-[9px] font-bold bg-indigo-50 text-indigo-600 px-1.5 py-0.2 rounded"
                  >
                    {{ sp.tenThuongHieu }}
                  </span>
                </div>

                <h3
                  class="text-xs font-bold text-slate-800 line-clamp-2 mt-1 group-hover:text-indigo-600 transition-colors"
                  :title="sp.tenSanPham"
                >
                  {{ sp.tenSanPham }}
                </h3>

                <div class="flex gap-1 mt-2 flex-wrap">
                  <span
                    class="text-[9px] font-medium bg-slate-100 text-slate-600 px-1.5 py-0.5 rounded"
                  >
                    🎨 {{ sp.tenMauSac }}
                  </span>
                  <span
                    class="text-[9px] font-medium bg-slate-100 text-slate-600 px-1.5 py-0.5 rounded"
                  >
                    📏 Size {{ sp.tenKichThuoc }}
                  </span>
                  <span
                    v-if="sp.tenChatLieu"
                    class="text-[9px] font-medium bg-slate-100 text-slate-600 px-1.5 py-0.5 rounded"
                  >
                    🧵 {{ sp.tenChatLieu }}
                  </span>
                </div>
              </div>

              <div class="mt-3 pt-2 border-t border-slate-100 flex items-end justify-between">
                <div>
                  <p
                    class="text-[10px] line-through text-slate-400 font-medium leading-none mb-0.5"
                    v-if="sp.giaGoc > sp.giaBan"
                  >
                    {{ formatPrice(sp.giaGoc) }}
                  </p>
                  <p class="text-sm font-black text-indigo-600 leading-none">
                    {{ formatPrice(sp.giaBan) }}
                  </p>
                </div>

                <span
                  :class="[
                    'text-[10px] px-1.5 py-0.5 rounded font-bold',
                    sp.soLuongTon <= 0 || !sp.trangThai
                      ? 'bg-rose-50 text-rose-600'
                      : sp.soLuongTon <= 10
                        ? 'bg-amber-50 text-amber-700'
                        : 'bg-slate-100 text-slate-600',
                  ]"
                >
                  {{
                    !sp.trangThai
                      ? 'Không khả dụng'
                      : sp.soLuongTon <= 0
                        ? 'Hết hàng'
                        : 'Kho: ' + sp.soLuongTon
                  }}
                </span>
              </div>
            </div>
          </div>
        </div>
      </section>

      <section class="lg:col-span-5 xl:col-span-4 flex flex-col space-y-6">
        <div
          class="bg-white rounded-3xl border border-slate-200 shadow-xl flex flex-col h-[calc(100vh-120px)] sticky top-24 overflow-hidden"
        >
          <div class="p-4 border-b border-slate-100 bg-slate-50 bg-opacity-70">
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
              class="bg-white border border-slate-200 rounded-xl p-3 flex items-center justify-between shadow-sm"
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

          <div class="flex-1 overflow-y-auto p-4 space-y-3 custom-scrollbar">
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
                class="w-12 h-12 rounded-lg object-cover bg-slate-50 flex-shrink-0"
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
                <button
                  @click="decreaseQty(item)"
                  class="w-5 h-5 flex items-center justify-center text-slate-600 hover:bg-white rounded transition-colors text-xs font-bold"
                >
                  -
                </button>
                <span class="w-6 text-center text-xs font-bold text-slate-800">{{
                  item.soLuong
                }}</span>
                <button
                  @click="increaseQty(item)"
                  class="w-5 h-5 flex items-center justify-center text-slate-600 hover:bg-white rounded transition-colors text-xs font-bold"
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

          <div class="border-t border-slate-100 p-4 bg-slate-50 space-y-4">
            <div class="mt-4 flex gap-2 items-end">
              <div class="relative flex-1">
                <label
                  class="text-[10px] font-bold text-slate-400 uppercase tracking-wider block mb-1"
                >
                  Voucher Khuyến Mãi
                </label>

                <div class="relative">
                  <input
                    type="text"
                    v-model="voucherQuery"
                    @focus="showVoucherDropdown = true"
                    @blur="setTimeout(() => (showVoucherDropdown = false), 200)"
                    placeholder="Tìm hoặc nhập mã..."
                    class="w-full bg-slate-50 border border-slate-200 rounded-xl pl-4 pr-10 py-2.5 text-xs font-medium focus:outline-none focus:ring-2 focus:ring-indigo-500 transition-all"
                  />
                  <span
                    @click.stop="showVoucherDropdown = !showVoucherDropdown"
                    class="absolute right-3 top-2.5 cursor-pointer select-none text-sm hover:scale-110 transition-transform"
                  >
                    🎟️
                  </span>
                </div>

                <div
                  v-if="showVoucherDropdown && filteredVouchers.length > 0"
                  class="absolute z-[100] w-full bg-white border border-slate-200 rounded-xl shadow-xl mt-1 max-h-56 overflow-y-auto custom-scrollbar"
                >
                  <div
                    v-for="vc in filteredVouchers"
                    :key="vc.id"
                    @mousedown.prevent="isVoucherValid(vc) ? selectVoucher(vc) : null"
                    :class="[
                      'px-4 py-3 border-b border-slate-50 last:border-none flex flex-col gap-1 transition-colors',
                      isVoucherValid(vc)
                        ? 'hover:bg-indigo-50 cursor-pointer'
                        : 'opacity-50 bg-slate-100 cursor-not-allowed',
                    ]"
                  >
                    <div class="flex justify-between items-center text-[11px]">
                      <span class="font-bold text-slate-500"
                        >ID: <span class="text-slate-800">{{ vc.id }}</span></span
                      >
                      <span
                        class="font-bold text-indigo-600 bg-indigo-50 px-1.5 py-0.5 rounded uppercase"
                      >
                        {{ vc.loaiGiamGia }}
                      </span>
                    </div>
                    <div
                      class="grid grid-cols-2 gap-x-2 gap-y-1 text-[11px] text-slate-600 font-medium pt-1"
                    >
                      <div>
                        Đơn tối thiểu:
                        <span class="font-semibold text-slate-800">{{
                          formatPrice(vc.giaTriDonHangToiThieu)
                        }}</span>
                      </div>
                      <div>
                        Giá trị giảm:
                        <span class="font-semibold text-rose-600">
                          {{
                            vc.loaiGiamGia === 'tien_mat'
                              ? formatPrice(vc.giaTriGiam)
                              : vc.giaTriGiam + '%'
                          }}
                        </span>
                      </div>
                      <div class="col-span-2">
                        Giảm tối đa:
                        <span class="font-semibold text-slate-800">{{
                          vc.giaTriGiamToiDa ? formatPrice(vc.giaTriGiamToiDa) : 'Không giới hạn'
                        }}</span>
                      </div>
                    </div>
                    <div
                      v-if="!isVoucherValid(vc)"
                      class="text-[10px] text-rose-500 font-bold text-right italic mt-1"
                    >
                      🔒 {{ getVoucherError(vc) }}
                    </div>
                  </div>
                </div>
              </div>

              <button
                @click="applyVoucher"
                class="bg-slate-800 text-white text-xs font-bold px-4 py-2.5 rounded-xl hover:bg-slate-900 transition-colors"
              >
                Áp dụng
              </button>
            </div>

            <div
              v-if="appliedVoucher"
              class="bg-indigo-50 text-indigo-700 text-[11px] font-bold p-2 rounded-xl flex justify-between items-center"
            >
              <span> 🎟️ Đã áp dụng: {{ appliedVoucher.maVoucher }} </span>

              <button @click="removeVoucher" class="text-rose-500 hover:underline font-black">
                Xóa
              </button>
            </div>

            <div class="space-y-1.5 text-xs font-medium text-slate-600">
              <div class="flex justify-between">
                <span>Tổng tiền hàng</span>
                <span class="text-slate-800 font-semibold">{{ formatPrice(totalCartPrice) }}</span>
              </div>
              ,
              <div class="flex justify-between text-rose-600">
                <span>Giảm giá Voucher</span>

                <span v-if="appliedVoucher">
                  - {{ formatPrice(voucherDiscount) }}
                  <span class="text-slate-400 text-xs">{{ voucherLabel }}</span>
                </span>
                <span v-else>- 0đ</span>
              </div>
            </div>

            <div class="grid grid-cols-2 gap-2 pt-1">
              <div>
                <label class="text-[10px] font-bold text-slate-400 block mb-1">LOẠI HÓA ĐƠN</label>
                <select
                  v-model="loaiHoaDon"
                  class="w-full bg-white border border-slate-200 rounded-xl p-2 text-xs font-bold text-slate-700 focus:outline-none"
                >
                  <option value="tai_quay">Tại quầy (POS)</option>
                  <option value="online">Giao hàng (Online)</option>
                </select>
              </div>
              <div>
                <label class="text-[10px] font-bold text-slate-400 block mb-1"
                  >PHƯƠNG THỨC TT</label
                >
                <select
                  v-model="phuongThucThanhToan"
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
              class="w-full bg-indigo-600 hover:bg-indigo-700 text-white font-bold text-sm py-3 px-4 rounded-xl shadow-lg shadow-indigo-100 hover:shadow-xl active:scale-[0.99] transition-all flex items-center justify-center gap-2"
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
  </div>
</template>
<script setup>
import { ref, computed, onMounted, watch, onUnmounted } from 'vue'
import { useToast } from 'vue-toastification'
import InvoiceModal from './InvoiceModal.vue'
import Swal from 'sweetalert2'
import stompClient from '@/socket'
import '@/socket'
import { onBeforeUnmount } from 'vue'

onBeforeUnmount(() => {
  if (stompClient) {
    stompClient.deactivate()
  }
})
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
} from '@/service/HoaDonService'

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
  ptttList.value = data

  const defaultId = data?.[1]?.id || data?.[0]?.id
  defaultPTTTId.value = defaultId // 👈 THIẾU CHỖ NÀY

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
    order.appliedVoucher = data.voucher || null
    order.voucherQuery = data.voucher?.maVoucher || ''
    selectedVoucher.value = data.voucher || null

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

  const [voucherData, hoaDonData] = await Promise.all([getAllVoucher(), getHoaDonCho()])

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

  initSocket()
})

let socketTimer = null

function initSocket() {
  stompClient.onConnect = () => {
    stompClient.subscribe('/topic/products', (msg) => {
      const data = JSON.parse(msg.body)

      clearTimeout(socketTimer)

      socketTimer = setTimeout(() => {
        handleSocket(data)
      }, 50)
    })
  }
}
function handleSocket(data) {
  switch (data.type) {
    case 'PRODUCT_STOCK_UPDATED':
      updateProductStock(data.productId, data.newStock)
      break

    case 'ORDER_CREATED':
      addNewOrderToList(data.order)
      break
  }
}

function updateProductStock(productId, newStock) {
  const index = products.value.findIndex(
    (p) => p.idSanPhamChiTiet === productId || p.id === productId,
  )

  if (index === -1) return

  products.value[index] = {
    ...products.value[index],
    stock: newStock,
  }
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
const selectVoucher = (voucher) => {
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

  // CHỈ CHỌN, CHƯA ÁP DỤNG
  selectedVoucher.value = voucher
  voucherQuery.value = voucher.maVoucher
  voucherCode.value = voucher.maVoucher

  showVoucherDropdown.value = false
}

const applyVoucher = async () => {
  const code = (voucherQuery.value || voucherCode.value).trim().toUpperCase()

  const foundVoucher = vouchers.value.find((v) => v.maVoucher?.toUpperCase() === code)

  if (!foundVoucher) {
    toast.error('Mã giảm giá không hợp lệ!')
    return
  }

  const now = new Date()

  if (foundVoucher.trangThai !== 1) {
    toast.error('Voucher đang bị khóa!')
    return
  }

  if (foundVoucher.ngayBatDau && new Date(foundVoucher.ngayBatDau) > now) {
    toast.error('Voucher chưa đến thời gian áp dụng!')
    return
  }

  if (foundVoucher.ngayKetThuc && new Date(foundVoucher.ngayKetThuc) < now) {
    toast.error('Voucher đã hết hạn!')
    return
  }

  if (foundVoucher.soLuongDaDung >= foundVoucher.soLuong) {
    toast.error('Voucher đã hết lượt sử dụng!')
    return
  }

  if (totalCartPrice.value < foundVoucher.giaTriDonHangToiThieu) {
    toast.warning(
      `Đơn hàng tối thiểu ${formatPrice(foundVoucher.giaTriDonHangToiThieu)} mới được áp dụng voucher`,
    )
    return
  }
  try {
    await apVoucher(currentOrder.value.id, foundVoucher.id)

    // reload lại từ server
    await loadChiTietHoaDon(currentOrder.value.id)

    selectedVoucher.value = foundVoucher
    voucherQuery.value = foundVoucher.maVoucher

    toast.success('Áp dụng voucher thành công')
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
  let list = vouchers.value
  if (voucherQuery.value) {
    list = list.filter(
      (v) => v.maVoucher && v.maVoucher.toLowerCase().includes(voucherQuery.value.toLowerCase()),
    )
  }
  return list.slice(0, 4)
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

    // 1. update cart từ DB
    await loadChiTietHoaDon(currentOrder.value.id)

    // 2. 🔥 GIẢM TỒN KHO NGAY TRÊN UI (QUAN TRỌNG)
    const sp = products.value.find(
      (p) => (p.idSanPhamChiTiet || p.id) === (product.idSanPhamChiTiet || product.id),
    )

    if (sp && sp.soLuongTon > 0) {
      sp.soLuongTon -= 1
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
const increaseQty = async (item) => {
  const sp = products.value.find((p) => (p.idSanPhamChiTiet || p.id) === item.product.id)

  if (!sp) return

  // ❌ hết hàng
  if (sp.soLuongTon <= 0) {
    toast.error('Sản phẩm đã hết hàng')
    return
  }

  await tangSoLuongSanPham(item.id)

  updateProductStockUI(item.product.id, -1)

  currentOrder.value.cart = currentOrder.value.cart.map((i) =>
    i.id === item.id ? { ...i, soLuong: i.soLuong + 1 } : i,
  )

  sp.soLuongTon--
}

const decreaseQty = async (item) => {
  // 🔥 nếu chỉ còn 1 thì xóa luôn
  if (item.soLuong <= 1) {
    await removeFromCart(currentOrder.value.cart.indexOf(item))
    return
  }

  await giamSoLuongSanPham(item.id)

  currentOrder.value.cart = currentOrder.value.cart.map((i) =>
    i.id === item.id ? { ...i, soLuong: i.soLuong - 1 } : i,
  )

  const sp = products.value.find((p) => (p.idSanPhamChiTiet || p.id) === item.product.id)

  if (sp) sp.soLuongTon++
}
const removeFromCart = async (index) => {
  try {
    const item = currentOrder.value.cart[index]
    if (!item) return

    await xoaSanPhamKhoiHoaDon(item.id)

    // 🔥 UPDATE UI NGAY
    currentOrder.value.cart = currentOrder.value.cart.filter((_, i) => i !== index)

    // 🔥 HOÀN TRẢ TỒN KHO (GIỐNG increase/decrease logic)
    const sp = products.value.find(
      (p) => (p.idSanPhamChiTiet || p.id) === (item.product.idSanPhamChiTiet || item.product.id),
    )

    if (sp) {
      sp.soLuongTon += item.soLuong
    }

    toast.success('Đã xóa sản phẩm')
  } catch (error) {
    console.error(error)
    toast.error('Xóa thất bại')
  }
}

const max_oder_waiting = 6

const createNewOrder = async () => {
  if (allOrders.value.length >= max_oder_waiting) {
    toast.warning(`Chỉ được tạo tối đa ${max_oder_waiting} hóa đơn chờ`)
    return
  }
  try {
    const hoaDon = await taoHoaDonCho()
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

    // 2. Xóa tab khỏi mảng
    allOrders.value.splice(index, 1)

    // 3. LÀM SẠCH GIỎ HÀNG (Cực kỳ quan trọng)
    // Nếu tab bị xóa là tab đang mở, ta phải clear cart ngay
    if (index === currentOrderIndex.value) {
      currentOrder.value.cart = []
    }

    // 4. Load lại dữ liệu tồn kho để đồng bộ
    await loadAllDataFromAPI()
    if (index <= currentOrderIndex.value) {
      currentOrderIndex.value--
    }
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

// --- 7. LOGIC XỬ LÝ GIỎ HÀNG & TÍNH TOÁN ---
const totalCartPrice = computed(() => {
  return (currentOrder.value?.cart || []).reduce(
    (sum, item) => sum + item.product.giaBan * item.soLuong,
    0,
  )
})

const voucherDiscount = computed(() => {
  if (!appliedVoucher.value) return 0
  const vc = appliedVoucher.value
  let discount = 0
  if (vc.loaiGiamGia === 'tien_mat') {
    discount = vc.giaTriGiam
  } else {
    discount = (totalCartPrice.value * vc.giaTriGiam) / 100
  }
  return vc.giaTriGiamToiDa ? Math.min(discount, vc.giaTriGiamToiDa) : discount
})

const finalPaymentPrice = computed(() => {
  return Math.max(0, totalCartPrice.value - voucherDiscount.value)
})

// --- 8. HÀM GỬI THANH TOÁN ---
// ================= THANH TOAN =================
const submitCheckout = async () => {
  if (!currentOrder.value.id) return toast.error('Hóa đơn không hợp lệ!')

  if (!phuongThucThanhToan.value) {
    return toast.error('Vui lòng chọn phương thức thanh toán!')
  }

  const payload = {
    idHoaDon: currentOrder.value.id,
    idPhuongThucThanhToan: parseInt(phuongThucThanhToan.value),
    idVoucher: appliedVoucher.value ? appliedVoucher.value.id : null,
  }

  try {
    const result = await thanhToanHoaDon(payload)
    console.log(result)
    hoaDonPrint.value = result
    showInvoiceModal.value = true

    // Xóa hóa đơn đã thanh toán
    allOrders.value = allOrders.value.filter((o) => o.id !== currentOrder.value.id)

    // Nếu vẫn còn hóa đơn, chọn tab đầu tiên
    if (allOrders.value.length > 0) {
      currentOrderIndex.value = 0
      await loadChiTietHoaDon(allOrders.value[0].id)
    } else {
      // Nếu không còn hóa đơn, reset index về -1 hoặc giá trị mặc định để UI không chọn tab nào
      currentOrderIndex.value = -1
      toast.info('Đã hết hóa đơn chờ')
    }
  } catch (error) {
    toast.error('Thanh toán thất bại: ' + error.message)
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
    return 'Voucher bị khóa'
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
