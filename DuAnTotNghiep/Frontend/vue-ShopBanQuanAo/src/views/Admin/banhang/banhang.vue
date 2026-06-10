<template>
  <div class="min-h-screen bg-slate-50 text-slate-800 font-sans antialiased">
    <header
      class="sticky top-0 z-40 bg-white border-b border-slate-200 shadow-sm backdrop-blur-md bg-opacity-95"
    >
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 h-16 flex items-center justify-between">
        <div class="flex-1 max-w-md mx-8 hidden md:block">
          <div class="relative">
            <span class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
              <svg
                class="h-5 w-5 text-slate-400"
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
            </span>
            <input
              type="text"
              v-model="searchQuery"
              placeholder="Tìm theo mã SPCT, tên quần, áo..."
              class="w-full pl-10 pr-4 py-2 border border-slate-200 rounded-xl bg-slate-50 focus:bg-white focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 transition-all text-sm"
            />
          </div>
        </div>
        <div class="flex items-center gap-2">
          <button
            @click="createNewOrder"
            class="text-xs font-bold bg-indigo-50 text-indigo-600 px-4 py-2 rounded-xl hover:bg-indigo-100 transition-all"
          >
            + Hóa đơn mới
          </button>
          <div class="relative">
            <button
              @click="showWaitingOrders = !showWaitingOrders"
              class="text-xs font-bold bg-amber-50 text-amber-700 px-4 py-2 rounded-xl hover:bg-amber-100 transition-all"
            >
              ⏳ Chờ ({{ waitingOrders.length }})
            </button>
            <div
              v-if="showWaitingOrders"
              class="absolute right-0 mt-2 w-64 bg-white border border-slate-200 rounded-xl shadow-xl z-50 p-2"
            >
              <div v-if="waitingOrders.length === 0" class="text-xs text-slate-400 p-2 text-center">
                Không có hóa đơn chờ
              </div>
              <div
                v-for="(order, idx) in waitingOrders"
                :key="idx"
                class="p-2 hover:bg-slate-50 rounded-lg cursor-pointer flex justify-between items-center"
                @click="loadOrder(idx)"
              >
                <span class="text-xs font-bold text-slate-700">HĐ Chờ #{{ idx + 1 }}</span>
                <span class="text-[10px] text-slate-400">{{ order.timestamp }}</span>
              </div>
            </div>
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
            v-for="sp in filteredProducts"
            :key="sp.id"
            @click="sp.soLuongTon > 0 ? addToCart(sp) : null"
            :class="[
              'bg-white rounded-2xl border p-3 flex flex-col justify-between transition-all group relative overflow-hidden select-none',
              sp.soLuongTon > 0
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
                v-if="sp.soLuongTon > 0"
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

              <div
                v-else
                class="absolute inset-0 bg-slate-900 bg-opacity-40 flex items-center justify-center"
              >
                <span
                  class="bg-white text-slate-800 text-xs font-black px-2.5 py-1 rounded-lg shadow"
                >
                  HẾT HÀNG
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
                  :title="sp.tenSanPhamChiTiet || sp.tenSanPham"
                >
                  {{ sp.tenSanPhamChiTiet || sp.tenSanPham }}
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
                    sp.soLuongTon <= 0
                      ? 'bg-rose-50 text-rose-600'
                      : sp.soLuongTon <= 10
                        ? 'bg-amber-50 text-amber-700'
                        : 'bg-slate-100 text-slate-600',
                  ]"
                >
                  Kho: {{ sp.soLuongTon }}
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
                  {{ selectedCustomer.soDienThoai }} •
                  <span class="text-indigo-600 font-semibold">{{
                    selectedCustomer.hangThanhVien || 'Thành viên lẻ'
                  }}</span>
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
              v-if="cart.length === 0"
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
              v-for="(item, index) in cart"
              :key="index"
              class="flex items-center gap-3 bg-white p-2.5 rounded-xl border border-slate-100 hover:shadow-sm transition-shadow"
            >
              <img
                :src="getProductImage(item.product)"
                class="w-12 h-12 rounded-lg object-cover bg-slate-50 flex-shrink-0"
                @error="setDefaultImage"
              />
              <div class="flex-1 min-w-0">
                <h4 class="text-xs font-bold text-slate-800 truncate">
                  {{ item.product.tenSanPhamChiTiet || item.product.tenSanPham }}
                </h4>
                <p class="text-[10px] text-slate-500 font-medium">
                  {{ item.product.tenMauSac }} / {{ item.product.tenKichThuoc }}
                </p>
                <p class="text-xs font-black text-indigo-600 mt-0.5">
                  {{ formatPrice(item.product.giaBan) }}
                </p>
              </div>

              <div class="flex items-center bg-slate-100 rounded-lg p-1">
                <button
                  @click="decreaseQty(index)"
                  class="w-5 h-5 flex items-center justify-center text-slate-600 hover:bg-white rounded transition-colors text-xs font-bold"
                >
                  -
                </button>
                <span class="w-6 text-center text-xs font-bold text-slate-800">{{
                  item.soLuong
                }}</span>
                <button
                  @click="increaseQty(index)"
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
                    @mousedown.prevent="vc.trangThai !== 0 ? selectVoucher(vc) : null"
                    :class="[
                      'px-4 py-3 border-b border-slate-50 last:border-none flex flex-col gap-1 transition-colors',
                      vc.trangThai === 0
                        ? 'opacity-40 bg-slate-50 cursor-not-allowed'
                        : 'hover:bg-indigo-50 cursor-pointer',
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
                      v-if="vc.trangThai === 0"
                      class="text-[9px] text-rose-500 font-bold text-right italic mt-0.5"
                    >
                      🔒 Không thể sử dụng
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
              <span>🎟️ Đã áp dụng Voucher ID: {{ appliedVoucher.id }}</span>
              <button @click="removeVoucher" class="text-rose-500 hover:underline font-black">
                Xóa
              </button>
            </div>

            <div class="space-y-1.5 text-xs font-medium text-slate-600">
              <div class="flex justify-between">
                <span>Tổng tiền hàng</span>
                <span class="text-slate-800 font-semibold">{{ formatPrice(totalCartPrice) }}</span>
              </div>
              <div class="flex justify-between text-rose-600">
                <span>Giảm giá Voucher</span>
                <span>- {{ formatPrice(voucherDiscount) }}</span>
              </div>
              <div class="flex justify-between items-center pt-2 border-t border-slate-200">
                <span class="text-sm font-bold text-slate-800">Tổng thanh toán</span>
                <span class="text-lg font-black text-indigo-600">{{
                  formatPrice(finalPaymentPrice)
                }}</span>
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
                  <option value="" disabled>---Chọn phương thức thanh toán---</option>

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
    <InvoiceModal v-if="showInvoiceModal" :hoaDon="hoaDonPrint" @close="showInvoiceModal = false" />
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useToast } from 'vue-toastification'
import InvoiceModal from './InvoiceModal.vue'

// --- 1. IMPORT CÁC SERVICE API ---
import { getAllDanhMuc } from '@/service/DanhMucService'
import { getAllKichThuoc } from '@/service/KichThuocService'
import { getAllMauSac } from '@/service/MauSacService'
import { getAllThuongHieu } from '@/service/ThuongHieuService'
import { getAllSanPhamChiTiet } from '@/service/SanPhamChiTiet'
import { getAllPTTT } from '@/service/PhuongThucThanhToanService'
import { getALLKhachHang } from '@/service/KhachHangService'
import { getAllVoucher } from '@/service/VoucherService'
import { getHoadonById } from '@/service/HoaDonService'
const toast = useToast()

// --- BASE API URL ---
const API_KHACH_HANG = 'http://localhost:8080/khachhang'
const API_BAN_HANG = 'http://localhost:8080/hoadon/ban-hang'
const DEFAULT_PRODUCT_IMAGE = 'https://images.unsplash.com/photo-1523381210434-271e8be1f52b?w=400'

// --- TRẠNG THÁI DANH SÁCH ---
const danhMucList = ref([])
const thuongHieuList = ref([])
const mauSacList = ref([])
const kichThuocList = ref([])
const products = ref([])
const customers = ref([])
const vouchers = ref([])
const ptttList = ref([])
const isLoading = ref(true)

const hoaDonPrint = ref(null)
const showInvoiceModal = ref(false)

// --- TRẠNG THÁI FORM & GIỎ HÀNG ---
const searchQuery = ref('')
const filterCategory = ref('')
const filterBrand = ref('')
const filterColor = ref('')
const filterSize = ref('')
const cart = ref([])
const voucherCode = ref('')
const appliedVoucher = ref(null)
const loaiHoaDon = ref('tai_quay')
const phuongThucThanhToan = ref('tien_mat')
const showCustomerModal = ref(false)
const searchCustomerQuery = ref('')
const selectedCustomer = ref(null)

// --- HÀM TẢI DỮ LIỆU ---
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
  ptttList.value = data
  if (data.length > 0) phuongThucThanhToan.value = data[0].id
}

onMounted(() => {
  loadAllDataFromAPI()
  loadPTTT()
})

onMounted(async () => {
  try {
    const response = await getAllVoucher()
    vouchers.value = response
    console.log(vouchers.value)
  } catch (error) {
    console.error(error)
  }
})

const voucherQuery = ref('')

// Lọc và chỉ lấy tối đa 4 voucher đầu tiên dựa trên tìm kiếm mã
const filteredVouchers = computed(() => {
  let list = vouchers.value

  if (voucherQuery.value) {
    list = list.filter(
      (v) => v.maVoucher && v.maVoucher.toLowerCase().includes(voucherQuery.value.toLowerCase()),
    )
  }

  return list.slice(0, 4)
})

const selectedVoucher = ref(null)
const showVoucherDropdown = ref(false)

// Cập nhật hàm chọn Voucher: Kiểm tra trạng thái === 0 thì chặn không cho chọn
const selectVoucher = (voucher) => {
  if (voucher.trangThai === 0) {
    toast.warning('Voucher này hiện đang bị khóa không thể sử dụng!')
    return
  }

  selectedVoucher.value = voucher
  voucherQuery.value = voucher.maVoucher
  voucherCode.value = voucher.maVoucher // Đồng bộ thêm biến voucherCode phục vụ cho hàm applyVoucher nhập tay nếu cần
  appliedVoucher.value = voucher
  showVoucherDropdown.value = false

  console.log('Voucher đã chọn:', voucher)
}

// --- HÀM XỬ LÝ ẢNH ---
const getProductImage = (sp) => {
  const images = sp.images
  if (images && images.length > 0) {
    const rawPath = images[0]
    if (rawPath.startsWith('http')) return rawPath
    const cleanPath = rawPath.startsWith('/') ? rawPath.substring(1) : rawPath
    return `http://localhost:8080/${cleanPath}`
  }
  return 'https://via.placeholder.com/150'
}

const setDefaultImage = (event) => {
  event.target.src = DEFAULT_PRODUCT_IMAGE
}

// --- BỘ LỌC & COMPUTED ---
const filteredProducts = computed(() => {
  return products.value.filter((sp) => {
    const nameStr = (sp.tenSanPhamChiTiet || sp.tenSanPham || '').toLowerCase()
    const codeStr = (sp.maSanPhamChiTiet || '').toLowerCase()
    const searchStr = searchQuery.value.toLowerCase()
    const matchSearch = nameStr.includes(searchStr) || codeStr.includes(searchStr)
    const selectedDM = danhMucList.value.find((dm) => dm.id == filterCategory.value)
    const selectedTH = thuongHieuList.value.find((th) => th.id == filterBrand.value)
    const matchCategory = !filterCategory.value || sp.tenDanhMuc === selectedDM?.tenDanhMuc
    const matchBrand = !filterBrand.value || sp.tenThuongHieu === selectedTH?.tenThuongHieu
    const matchColor = !filterColor.value || Number(sp.idMauSac) === Number(filterColor.value)
    const matchSize = !filterSize.value || Number(sp.idKichThuoc) === Number(filterSize.value)
    return matchSearch && matchCategory && matchBrand && matchColor && matchSize
  })
})

const resetFilters = () => {
  filterCategory.value = ''
  filterBrand.value = ''
  filterColor.value = ''
  filterSize.value = ''
  searchQuery.value = ''
}

const filteredCustomers = computed(() => {
  return customers.value.filter(
    (kh) =>
      kh.hoTen.toLowerCase().includes(searchCustomerQuery.value.toLowerCase()) ||
      kh.soDienThoai.includes(searchCustomerQuery.value),
  )
})

const totalCartPrice = computed(() =>
  cart.value.reduce((sum, item) => sum + item.product.giaBan * item.soLuong, 0),
)

const voucherDiscount = computed(() => {
  if (!appliedVoucher.value) return 0

  if (totalCartPrice.value < appliedVoucher.value.giaTriDonHangToiThieu) {
    return 0
  }

  let discount = 0
  if (appliedVoucher.value.loaiGiamGia === 'tien_mat') {
    discount = appliedVoucher.value.giaTriGiam
  } else if (appliedVoucher.value.loaiGiamGia === 'phan_tram') {
    discount = (totalCartPrice.value * appliedVoucher.value.giaTriGiam) / 100
    if (appliedVoucher.value.giaTriGiamToiDa && discount > appliedVoucher.value.giaTriGiamToiDa) {
      discount = appliedVoucher.value.giaTriGiamToiDa
    }
  }

  return discount
})

const finalPaymentPrice = computed(() => {
  const result = totalCartPrice.value - voucherDiscount.value
  return result > 0 ? result : 0
})

// --- HÀM XỬ LÝ GIỎ HÀNG ---
const addToCart = (product) => {
  if (product.soLuongTon <= 0) {
    toast.warning('Sản phẩm đã hết hàng!')
    return
  }
  const existingItem = cart.value.find((item) => item.product.id === product.id)
  if (existingItem) {
    if (existingItem.soLuong < product.soLuongTon) {
      existingItem.soLuong++
    } else {
      toast.warning('Đã đạt giới hạn tồn kho!')
    }
  } else {
    cart.value.push({ product, soLuong: 1 })
    toast.success('Đã thêm sản phẩm vào giỏ!')
  }
}

const increaseQty = (index) => {
  if (cart.value[index].soLuong < cart.value[index].product.soLuongTon) {
    cart.value[index].soLuong++
  } else {
    toast.warning('Đã đạt số lượng tối đa!')
  }
}

const decreaseQty = (index) => {
  if (cart.value[index].soLuong > 1) {
    cart.value[index].soLuong--
  } else {
    removeFromCart(index)
  }
}

const removeFromCart = (index) => {
  cart.value.splice(index, 1)
}

// Hàm nhập mã tay đồng bộ chuẩn hóa theo voucherQuery
const applyVoucher = () => {
  const code = (voucherQuery.value || voucherCode.value).toUpperCase().trim()
  const foundVoucher = vouchers.value.find((v) => v.maVoucher && v.maVoucher.toUpperCase() === code)

  if (foundVoucher) {
    if (foundVoucher.trangThai === 0) {
      toast.error('Mã giảm giá này hiện đang bị khóa!')
      return
    }
    if (totalCartPrice.value < foundVoucher.giaTriDonHangToiThieu) {
      toast.warning(
        `Đơn hàng chưa đạt giá trị tối thiểu ${formatPrice(foundVoucher.giaTriDonHangToiThieu)}`,
      )
      return
    }
    appliedVoucher.value = foundVoucher
    selectedVoucher.value = foundVoucher
    voucherQuery.value = foundVoucher.maVoucher
    toast.success('Áp dụng mã thành công!')
  } else {
    toast.error('Mã giảm giá không hợp lệ!')
  }
}

const removeVoucher = () => {
  appliedVoucher.value = null
  selectedVoucher.value = null
  voucherCode.value = ''
  voucherQuery.value = ''
}

const openCustomerModal = () => {
  showCustomerModal.value = true
}
const selectCustomer = (kh) => {
  selectedCustomer.value = kh
  showCustomerModal.value = false
}

// --- THANH TOÁN ---
const submitCheckout = async () => {
  if (cart.value.length === 0) {
    toast.error('Giỏ hàng trống!')
    return
  }
  const payloadHoaDon = {
    idKhachHang: selectedCustomer.value ? selectedCustomer.value.id : null,
    tongTienHang: totalCartPrice.value,
    tongGiamGia: voucherDiscount.value,
    tongThanhToan: finalPaymentPrice.value,
    loaiHoaDon: loaiHoaDon.value,
    phuongThucTt: phuongThucThanhToan.value,
    chiTietMua: cart.value.map((item) => ({
      idSanPhamChiTiet: item.product.id,
      soLuong: item.soLuong,
      donGia: item.product.giaBan,
    })),
    idVoucher: appliedVoucher.value ? appliedVoucher.value.id : null,
  }
  try {
    const res = await fetch(API_BAN_HANG, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(payloadHoaDon),
    })
    const data = await res.json()

    const hoaDonDetail = await getHoadonById(data.id)

    hoaDonPrint.value = hoaDonDetail
    showInvoiceModal.value = true

    if (!res.ok) throw new Error('Lỗi server')
    toast.success(`🎉 Thanh toán thành công!`)
    cart.value = []
    removeVoucher()
    selectedCustomer.value = null
    await loadAllDataFromAPI()
  } catch (error) {
    toast.error('Quá trình lưu hóa đơn thất bại!')
  }
}
const waitingOrders = ref([]) // <--- MỚI: Danh sách hóa đơn chờ
const showWaitingOrders = ref(false)

// ... (Giữ nguyên các hàm loadAllDataFromAPI, loadPTTT, v.v.)

// --- MỚI: HÀM XỬ LÝ HÓA ĐƠN CHỜ ---
const saveOrderToWaiting = () => {
  if (cart.value.length === 0) {
    toast.warning('Giỏ hàng trống, không thể lưu hóa đơn chờ!')
    return
  }

  // Lưu state hiện tại
  waitingOrders.value.push({
    cart: [...cart.value],
    selectedCustomer: selectedCustomer.value,
    appliedVoucher: appliedVoucher.value,
    voucherQuery: voucherQuery.value,
    timestamp: new Date().toLocaleTimeString(),
  })

  toast.success('Đã lưu hóa đơn vào danh sách chờ!')
  resetCart()
}

const loadOrder = (index) => {
  const order = waitingOrders.value[index]

  // Nạp dữ liệu vào giỏ
  cart.value = [...order.cart]
  selectedCustomer.value = order.selectedCustomer
  appliedVoucher.value = order.appliedVoucher
  voucherQuery.value = order.voucherQuery

  // Xóa khỏi danh sách chờ
  waitingOrders.value.splice(index, 1)
  showWaitingOrders.value = false
  toast.info('Đã tải lại hóa đơn chờ!')
}

const createNewOrder = () => {
  if (cart.value.length > 0) {
    if (
      confirm(
        'Giỏ hàng hiện tại đang có sản phẩm. Bạn có muốn lưu vào hóa đơn chờ trước khi tạo mới không?',
      )
    ) {
      saveOrderToWaiting()
      return
    }
  }
  resetCart()
}

// Hàm resetCart của bạn cần đồng bộ để xóa hết các trạng thái
const resetCart = () => {
  cart.value = []
  selectedCustomer.value = null
  removeVoucher()
  // Nếu có các state khác cần reset, thêm vào đây
}

// ... (Giữ nguyên các hàm cũ: submitCheckout, formatPrice, v.v.)

const formatPrice = (value) =>
  new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(value)
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
</style>
