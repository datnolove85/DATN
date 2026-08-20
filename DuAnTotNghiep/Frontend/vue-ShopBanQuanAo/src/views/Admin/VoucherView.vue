<template>
  <div class="min-h-screen bg-slate-100 p-4 md:p-6 text-slate-800">
    <div class="max-w-[1700px] mx-auto space-y-6">
      <!-- HEADER -->
      <div
        class="flex flex-col sm:flex-row justify-between items-start sm:items-center bg-white rounded-2xl shadow-sm border border-slate-200 px-6 py-4 gap-4"
      >
        <div class="flex items-center gap-3">
          <div
            class="w-10 h-10 rounded-xl bg-indigo-50 flex items-center justify-center text-xl text-indigo-600 font-bold"
          >
            🎁
          </div>
          <div>
            <h1 class="text-xl font-bold text-slate-800">Phiếu giảm giá</h1>
            <p class="text-slate-500 text-xs">
              Tạo, theo dõi và kiểm soát điều kiện áp dụng voucher cho đơn hàng
            </p>
          </div>
        </div>

        <button
          @click="openCreateForm"
          class="flex items-center gap-2 rounded-xl bg-indigo-600 px-4 py-2.5 font-medium text-white text-sm shadow-sm hover:bg-indigo-700 transition"
        >
          <span class="text-lg leading-none">+</span>
          <span>THÊM PHIẾU GIẢM GIÁ</span>
        </button>
      </div>

      <!-- STATISTICS -->
      <div class="grid grid-cols-2 md:grid-cols-4 gap-4">
        <!-- Card 1: Tổng phiếu -->
        <div class="rounded-2xl bg-white border border-slate-200 p-4 shadow-sm">
          <div class="flex justify-between items-center">
            <div>
              <p class="text-slate-500 text-xs font-medium uppercase tracking-wider">Tổng phiếu</p>
              <h2 class="mt-1 text-2xl font-bold text-slate-800 font-mono">
                {{ vouchers.length }}
              </h2>
              <p class="text-[10px] text-slate-400 mt-0.5">Đang hiển thị trên hệ thống</p>
            </div>
            <div
              class="w-10 h-10 rounded-xl bg-indigo-50 flex items-center justify-center text-lg text-indigo-600"
            >
              📋
            </div>
          </div>
        </div>

        <!-- Card 2: Đang hoạt động -->
        <div class="rounded-2xl bg-white border border-slate-200 p-4 shadow-sm">
          <div class="flex justify-between items-center">
            <div>
              <p class="text-slate-500 text-xs font-medium uppercase tracking-wider">
                Đang hoạt động
              </p>
              <h2 class="mt-1 text-2xl font-bold text-emerald-600 font-mono">{{ activeCount }}</h2>
              <p class="text-[10px] text-slate-400 mt-0.5">Có thể áp dụng ngay</p>
            </div>
            <div
              class="w-10 h-10 rounded-xl bg-emerald-50 flex items-center justify-center text-lg text-emerald-600"
            >
              🟢
            </div>
          </div>
        </div>

        <!-- Card 3: Đã dùng -->
        <div class="rounded-2xl bg-white border border-slate-200 p-4 shadow-sm">
          <div class="flex justify-between items-center">
            <div>
              <p class="text-slate-500 text-xs font-medium uppercase tracking-wider">Đã dùng</p>
              <h2 class="mt-1 text-2xl font-bold text-amber-600 font-mono">{{ usedQuantity }}</h2>
              <p class="text-[10px] text-slate-400 mt-0.5">Lượt sử dụng thực tế</p>
            </div>
            <div
              class="w-10 h-10 rounded-xl bg-amber-50 flex items-center justify-center text-lg text-amber-600"
            >
              ⚡
            </div>
          </div>
        </div>

        <!-- Card 4: Còn lại -->
        <div class="rounded-2xl bg-white border border-slate-200 p-4 shadow-sm">
          <div class="flex justify-between items-center">
            <div>
              <p class="text-slate-500 text-xs font-medium uppercase tracking-wider">Còn lại</p>
              <h2 class="mt-1 text-2xl font-bold text-indigo-600 font-mono">
                {{ remainingQuantity }}
              </h2>
              <p class="text-[10px] text-slate-400 mt-0.5">Lượt khả dụng còn lại</p>
            </div>
            <div
              class="w-10 h-10 rounded-xl bg-indigo-50 flex items-center justify-center text-lg text-indigo-600"
            >
              📦
            </div>
          </div>
        </div>
      </div>

      <!-- FILTER & SEARCH -->
      <div
        class="rounded-2xl border border-slate-200 bg-white shadow-sm p-4 md:p-5 flex flex-col sm:flex-row items-stretch sm:items-center gap-3"
      >
        <div class="relative flex-grow">
          <svg
            class="absolute left-3.5 top-1/2 -translate-y-1/2 w-4 h-4 text-slate-400"
            fill="none"
            stroke="currentColor"
            stroke-width="2"
            viewBox="0 0 24 24"
          >
            <path
              stroke-linecap="round"
              stroke-linejoin="round"
              d="M21 21l-5.2-5.2M10.5 18a7.5 7.5 0 100-15 7.5 7.5 0 000 15z"
            />
          </svg>
          <input
            v-model="keyword"
            type="text"
            placeholder="Tìm kiếm theo mã, tên hoặc loại giảm giá..."
            class="w-full pl-10 pr-4 py-2.5 bg-slate-50/50 border border-slate-200 rounded-xl text-sm text-slate-700 outline-none focus:bg-white focus:border-indigo-500 focus:ring-1 focus:ring-indigo-500"
          />
        </div>

        <span
          class="px-3 py-2 bg-indigo-50 text-indigo-600 rounded-xl text-xs font-bold whitespace-nowrap text-center border border-indigo-100"
        >
          {{ vouchers.length }} kết quả
        </span>

        <button
          @click="resetSearch"
          class="px-4 py-2.5 bg-slate-100 hover:bg-slate-200 text-slate-600 rounded-xl text-xs font-bold transition flex items-center justify-center gap-1.5"
        >
          🔄 Làm mới
        </button>
      </div>

      <!-- FORM MODAL (POPUP) -->
      <Teleport to="body">
        <div
          v-if="showForm"
          class="fixed inset-0 z-[999] flex items-center justify-center p-4 overflow-y-auto"
        >
          <!-- Backdrop -->
          <div
            class="fixed inset-0 bg-black/40 backdrop-blur-xs transition-opacity"
            @click="closeForm"
          ></div>

          <!-- Modal Box -->
          <div
            class="bg-white rounded-3xl border border-slate-200 shadow-2xl w-full max-w-2xl max-h-[90vh] flex flex-col z-10 animate-scale-up overflow-hidden relative my-auto"
          >
            <!-- HEADER FORM -->
            <div
              class="px-6 py-4 bg-slate-50 border-b border-slate-200 flex items-center justify-between shrink-0"
            >
              <h2 class="text-base font-bold text-slate-800 flex items-center gap-2">
                <span class="text-xl">🎁</span>
                {{ form.id ? 'Cập nhật Voucher' : 'Tạo Voucher mới' }}
              </h2>
              <button
                @click="closeForm"
                class="h-8 w-8 rounded-lg bg-slate-200 text-slate-600 hover:bg-slate-300 font-bold flex items-center justify-center transition"
              >
                ✕
              </button>
            </div>

            <!-- NỘI DUNG FORM (Scrollable) -->
            <div class="p-6 space-y-4 overflow-y-auto flex-1">
              <div>
                <label class="block text-xs font-bold text-slate-600 uppercase mb-1"
                  >Tên voucher</label
                >
                <input
                  v-model.trim="form.tenVoucher"
                  placeholder="Nhập tên voucher..."
                  :class="[
                    'w-full h-10 rounded-xl border bg-slate-50/50 px-3 text-sm outline-none transition',
                    errors.tenVoucher
                      ? 'border-rose-500 focus:border-rose-500'
                      : 'border-slate-200 focus:border-indigo-500',
                  ]"
                />
                <p v-if="errors.tenVoucher" class="mt-1 text-[11px] text-rose-500 font-medium">
                  {{ errors.tenVoucher }}
                </p>
              </div>

              <div class="grid grid-cols-1 sm:grid-cols-2 gap-4">
                <div>
                  <label class="block text-xs font-bold text-slate-600 uppercase mb-1"
                    >Loại giảm</label
                  >
                  <select
                    v-model="form.loaiGiamGia"
                    class="w-full h-10 rounded-xl border border-slate-200 bg-slate-50/50 px-3 text-sm outline-none focus:border-indigo-500"
                  >
                    <option value="phan_tram">Phần trăm (%)</option>
                    <option value="tien_mat">Tiền mặt (VNĐ)</option>
                  </select>
                </div>
                <div>
                  <label class="block text-xs font-bold text-slate-600 uppercase mb-1"
                    >Giá trị giảm</label
                  >
                  <div class="relative">
                    <input
                      type="text"
                      v-model="giaTriGiamDisplay"
                      @keydown="preventNegative"
                      :placeholder="form.loaiGiamGia === 'phan_tram' ? 'VD: 10' : 'VD: 50.000'"
                      :class="[
                        'w-full h-10 rounded-xl border bg-slate-50/50 pl-3 pr-10 text-sm outline-none transition',
                        errors.giaTriGiam
                          ? 'border-rose-500 focus:border-rose-500'
                          : 'border-slate-200 focus:border-indigo-500',
                      ]"
                    />
                    <span
                      class="absolute right-3 top-1/2 -translate-y-1/2 text-xs font-bold text-slate-400 pointer-events-none"
                    >
                      {{ form.loaiGiamGia === 'phan_tram' ? '%' : 'đ' }}
                    </span>
                  </div>
                  <p v-if="errors.giaTriGiam" class="mt-1 text-[11px] text-rose-500 font-medium">
                    {{ errors.giaTriGiam }}
                  </p>
                </div>
              </div>

              <div class="grid grid-cols-1 sm:grid-cols-2 gap-4">
                <div>
                  <label class="block text-xs font-bold text-slate-600 uppercase mb-1"
                    >Đơn tối thiểu</label
                  >
                  <input
                    type="text"
                    v-model="donToiThieuDisplay"
                    @keydown="preventNegative"
                    :class="[
                      'w-full h-10 rounded-xl border bg-slate-50/50 px-3 text-sm outline-none transition',
                      errors.giaTriDonHangToiThieu
                        ? 'border-rose-500 focus:border-rose-500'
                        : 'border-slate-200 focus:border-indigo-500',
                    ]"
                  />
                  <p
                    v-if="errors.giaTriDonHangToiThieu"
                    class="mt-1 text-[11px] text-rose-500 font-medium"
                  >
                    {{ errors.giaTriDonHangToiThieu }}
                  </p>
                </div>
                <div v-if="form.loaiGiamGia === 'phan_tram'">
                  <label class="block text-xs font-bold text-slate-600 uppercase mb-1"
                    >Giảm tối đa</label
                  >
                  <input
                    type="text"
                    v-model="giamToiDaDisplay"
                    @keydown="preventNegative"
                    :class="[
                      'w-full h-10 rounded-xl border bg-slate-50/50 px-3 text-sm outline-none transition',
                      errors.giaTriGiamToiDa
                        ? 'border-rose-500 focus:border-rose-500'
                        : 'border-slate-200 focus:border-indigo-500',
                    ]"
                    placeholder="VD: 100.000"
                  />
                  <p
                    v-if="errors.giaTriGiamToiDa"
                    class="mt-1 text-[11px] text-rose-500 font-medium"
                  >
                    {{ errors.giaTriGiamToiDa }}
                  </p>
                </div>
              </div>

              <div class="grid grid-cols-1 sm:grid-cols-2 gap-4">
                <div>
                  <label class="block text-xs font-bold text-slate-600 uppercase mb-1">
                    Số lượng
                  </label>
                  <input
                    type="text"
                    v-model="form.soLuong"
                    @input="form.soLuong = parseMoneyInput($event.target.value)"
                    :class="[
                      'w-full h-10 rounded-xl border bg-slate-50/50 px-3 text-sm outline-none transition',
                      errors.soLuong
                        ? 'border-rose-500 focus:border-rose-500'
                        : 'border-slate-200 focus:border-indigo-500',
                    ]"
                  />
                  <p v-if="errors.soLuong" class="mt-1 text-[11px] text-rose-500 font-medium">
                    {{ errors.soLuong }}
                  </p>
                </div>

                <div>
                  <label class="block text-xs font-bold text-slate-600 uppercase mb-1">
                    Trạng thái
                  </label>
                  <select
                    v-model.number="form.trangThai"
                    class="w-full h-10 rounded-xl border border-slate-200 bg-slate-50/50 px-3 text-sm outline-none focus:border-indigo-500"
                  >
                    <option :value="1">Hoạt động</option>
                    <option :value="0">Ngừng hoạt động</option>
                  </select>
                </div>
              </div>

              <div class="grid grid-cols-1 sm:grid-cols-2 gap-4">
                <div>
                  <label class="block text-xs font-bold text-slate-600 uppercase mb-1"
                    >Ngày bắt đầu</label
                  >
                  <input
                    type="datetime-local"
                    v-model="form.ngayBatDau"
                    :class="[
                      'w-full h-10 rounded-xl border bg-slate-50/50 px-3 text-sm outline-none transition',
                      errors.ngayBatDau
                        ? 'border-rose-500 focus:border-rose-500'
                        : 'border-slate-200 focus:border-indigo-500',
                    ]"
                  />
                  <p v-if="errors.ngayBatDau" class="mt-1 text-[11px] text-rose-500 font-medium">
                    {{ errors.ngayBatDau }}
                  </p>
                </div>
                <div>
                  <label class="block text-xs font-bold text-slate-600 uppercase mb-1"
                    >Ngày kết thúc</label
                  >
                  <input
                    type="datetime-local"
                    v-model="form.ngayKetThuc"
                    :class="[
                      'w-full h-10 rounded-xl border bg-slate-50/50 px-3 text-sm outline-none transition',
                      errors.ngayKetThuc
                        ? 'border-rose-500 focus:border-rose-500'
                        : 'border-slate-200 focus:border-indigo-500',
                    ]"
                  />
                  <p v-if="errors.ngayKetThuc" class="mt-1 text-[11px] text-rose-500 font-medium">
                    {{ errors.ngayKetThuc }}
                  </p>
                </div>
              </div>
            </div>

            <!-- FOOTER FORM -->
            <div
              class="flex justify-end gap-2 px-6 py-4 bg-slate-50 border-t border-slate-200 shrink-0"
            >
              <button
                @click="closeForm"
                class="px-4 py-2.5 rounded-xl border border-slate-200 text-xs font-bold text-slate-600 hover:bg-slate-100 transition"
              >
                Hủy
              </button>
              <button
                @click="requestSaveVoucher"
                class="px-5 py-2.5 rounded-xl bg-indigo-600 text-white text-xs font-bold hover:bg-indigo-700 transition shadow-sm"
              >
                {{ form.id ? 'Lưu thay đổi' : 'Tạo mới' }}
              </button>
            </div>
          </div>
        </div>
      </Teleport>

      <!-- TABLE DANH SÁCH -->
      <div class="rounded-2xl border border-slate-200 bg-white shadow-sm overflow-hidden">
        <div
          class="flex items-center justify-between border-b border-slate-200 px-6 py-4 bg-slate-50/50"
        >
          <h2 class="text-base font-bold text-slate-800">Danh sách phiếu giảm giá</h2>
          <span class="text-xs font-semibold text-slate-500 bg-slate-200/60 px-2.5 py-1 rounded-lg">
            Hiển thị {{ vouchers.length }} bản ghi
          </span>
        </div>

        <div class="overflow-x-auto">
          <table class="min-w-full text-left text-sm text-slate-600 divide-y divide-slate-200">
            <thead
              class="bg-slate-50 text-[11px] uppercase tracking-wider text-slate-500 font-semibold select-none"
            >
              <tr>
                <th class="py-3 px-4 text-center w-12">#</th>
                <th class="py-3 px-4">Mã</th>
                <th class="py-3 px-4 w-64">Thông tin phiếu</th>
                <th class="py-3 px-4 text-center">Loại</th>
                <th class="py-3 px-4 text-center">Giá trị giảm</th>
                <th class="py-3 px-4">Điều kiện áp dụng</th>
                <th class="py-3 px-4 w-40">Tỉ lệ sử dụng</th>
                <th class="py-3 px-4">Thời gian</th>
                <th class="py-3 px-4 text-center">Trạng thái</th>
                <th class="py-3 px-4 text-center">Thao tác</th>
              </tr>
            </thead>
            <tbody class="divide-y divide-slate-100 text-xs">
              <tr v-if="loading">
                <td colspan="10" class="py-16 text-center text-slate-400 font-bold">
                  <div class="flex items-center justify-center gap-2">
                    <div
                      class="w-4 h-4 border-2 border-indigo-200 border-top-indigo-600 rounded-full animate-spin"
                    ></div>
                    Đang tải dữ liệu hệ thống...
                  </div>
                </td>
              </tr>

              <tr
                v-for="(item, index) in vouchers"
                :key="item.id"
                class="hover:bg-slate-50 transition-colors group"
              >
                <td class="py-3.5 px-4 text-center font-bold text-slate-400 font-mono">
                  {{ pageNo * pageSize + index + 1 }}
                </td>
                <td class="py-3.5 px-4">
                  <span
                    class="font-mono text-[11px] font-semibold text-indigo-600 bg-indigo-50 px-2 py-0.5 rounded border border-indigo-100 uppercase"
                  >
                    {{ item.maVoucher }}
                  </span>
                </td>
                <td class="py-3.5 px-4 space-y-0.5">
                  <div class="font-bold text-slate-800 truncate max-w-[240px]">
                    {{ item.tenVoucher }}
                  </div>
                  <div class="text-[11px] text-slate-400 truncate max-w-[240px] font-medium">
                    {{ item.moTa || 'Chưa có mô tả chi tiết' }}
                  </div>
                </td>
                <td class="py-3.5 px-4 text-center">
                  <span
                    :class="[
                      'inline-block px-2 py-0.5 text-[11px] font-medium rounded border',
                      item.loaiGiamGia === 'phan_tram'
                        ? 'bg-blue-50 text-blue-700 border-blue-100'
                        : 'bg-emerald-50 text-emerald-700 border-emerald-100',
                    ]"
                  >
                    {{ item.loaiGiamGia === 'phan_tram' ? 'Phần trăm (%)' : 'Tiền mặt (VNĐ)' }}
                  </span>
                </td>
                <td class="py-3.5 px-4 text-center font-mono font-bold text-indigo-600">
                  {{ formatDiscount(item) }}
                </td>
                <td class="py-3.5 px-4 space-y-0.5 text-[11px] text-slate-500 font-medium">
                  <div>
                    Đơn tối thiểu:
                    <b class="text-slate-700 font-mono">{{
                      formatMoney(item.giaTriDonHangToiThieu)
                    }}</b>
                  </div>
                  <div>
                    Giảm tối đa:
                    <b class="text-slate-700 font-mono">{{
                      item.giaTriGiamToiDa ? formatMoney(item.giaTriGiamToiDa) : '-'
                    }}</b>
                  </div>
                </td>
                <td class="py-3.5 px-4">
                  <div
                    class="flex justify-between items-center text-[10px] font-bold text-slate-400 mb-1 font-mono"
                  >
                    <span>{{ item.soLuongDaDung || 0 }}/{{ item.soLuong || 0 }}</span>
                    <span class="text-slate-700">{{ getUsagePercent(item) }}%</span>
                  </div>
                  <div class="w-full h-1.5 bg-slate-100 rounded-full overflow-hidden">
                    <div
                      class="h-full bg-gradient-to-r from-indigo-500 to-emerald-500 rounded-full transition-all duration-500"
                      :style="{ width: getUsagePercent(item) + '%' }"
                    ></div>
                  </div>
                </td>
                <td class="py-3.5 px-4 text-[10px] font-mono text-slate-400 space-y-0.5">
                  <div class="text-slate-600 font-medium">
                    BD: {{ formatDate(item.ngayBatDau) }}
                  </div>
                  <div>KT: {{ formatDate(item.ngayKetThuc) }}</div>
                </td>
                <td class="py-3.5 px-4 text-center">
                  <span
                    :class="[
                      'inline-flex items-center gap-1.5 px-2.5 py-1 text-[11px] font-medium rounded-full border',
                      item.trangThai === 1
                        ? 'bg-green-100 text-green-700 border-green-200'
                        : 'bg-red-100 text-red-700 border-red-200',
                    ]"
                  >
                    <span class="h-1.5 w-1.5 rounded-full bg-current"></span>
                    {{ item.trangThai === 1 ? 'Đang diễn ra' : 'Ngừng hoạt động' }}
                  </span>
                </td>
                <td class="py-3.5 px-4 text-center">
                  <div class="flex items-center justify-center gap-1.5">
                    <button
                      @click="openEditForm(item)"
                      class="px-2.5 py-1 rounded-lg bg-amber-50 hover:bg-amber-500 hover:text-white text-amber-700 font-medium transition border border-amber-200 text-[11px]"
                      title="Chỉnh sửa"
                    >
                      Sửa
                    </button>
                  </div>
                </td>
              </tr>

              <tr v-if="!loading && vouchers.length === 0">
                <td colspan="10" class="py-16 text-center text-slate-400">
                  <div class="text-3xl mb-1">📭</div>
                  <p class="text-xs font-bold text-slate-800">Không tìm thấy phiếu giảm giá</p>
                  <span class="text-xs"
                    >Hãy tạo voucher mới hoặc tinh chỉnh lại từ khóa tìm kiếm.</span
                  >
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>

      <!-- PAGINATION -->
      <div
        class="flex flex-col sm:flex-row justify-between items-center gap-4 bg-white p-4 rounded-2xl border border-slate-200 shadow-sm text-xs font-bold text-slate-500"
      >
        <div class="flex items-center gap-2">
          <span>Hiển thị</span>
          <select
            v-model.number="pageSize"
            @change="handleSizeChange"
            class="bg-slate-50 border border-slate-200 py-1.5 pl-3 pr-8 rounded-lg outline-none cursor-pointer focus:border-indigo-500 text-slate-700 font-mono"
          >
            <option :value="5">5</option>
            <option :value="10">10</option>
            <option :value="20">20</option>
          </select>
          <span>phiếu trên mỗi trang</span>
        </div>
        <div class="flex items-center gap-3">
          <button
            :disabled="pageNo === 0 || hasKeyword"
            @click="changePage(-1)"
            class="px-4 py-2 bg-slate-100 hover:bg-slate-200 text-slate-600 rounded-xl transition-all disabled:opacity-50 disabled:cursor-not-allowed"
          >
            Trước
          </button>
          <span class="font-mono text-slate-700">Trang {{ pageNo + 1 }}</span>
          <button
            :disabled="vouchers.length < pageSize || hasKeyword"
            @click="changePage(1)"
            class="px-4 py-2 bg-slate-100 hover:bg-slate-200 text-slate-600 rounded-xl transition-all disabled:opacity-50 disabled:cursor-not-allowed"
          >
            Sau
          </button>
        </div>
      </div>

      <!-- CONFIRM MODAL -->
      <Teleport to="body">
        <div
          v-if="confirmModal.show"
          class="fixed inset-0 z-[999] flex items-center justify-center p-4"
        >
          <div
            class="absolute inset-0 bg-black/30 backdrop-blur-xs"
            @click="closeConfirmModal"
          ></div>
          <div
            class="bg-white border border-slate-200 rounded-3xl shadow-2xl w-full max-w-sm p-6 z-10 animate-scale-up text-center relative overflow-hidden"
          >
            <div
              :class="[
                'w-12 h-12 rounded-full flex items-center justify-center mx-auto mb-4 text-white font-black text-lg shadow-sm',
                confirmModal.type === 'update'
                  ? 'bg-emerald-500 shadow-emerald-500/10'
                  : 'bg-rose-500 shadow-rose-500/10',
              ]"
            >
              {{ confirmModal.icon }}
            </div>
            <span
              class="inline-block px-2 py-0.5 bg-slate-50 text-slate-400 border border-slate-200 rounded-lg text-[10px] uppercase font-bold tracking-wider mb-2"
            >
              {{ confirmModal.label }}
            </span>
            <h3 class="text-sm font-bold text-slate-800 mb-1.5 uppercase tracking-tight">
              {{ confirmModal.title }}
            </h3>
            <p class="text-[11px] text-slate-500 px-2 mb-4">
              {{ confirmModal.message }}
            </p>

            <div
              v-if="confirmModal.code"
              class="inline-block px-3 py-1 bg-slate-50 border border-slate-200 rounded-xl font-mono text-xs font-bold text-slate-700 uppercase tracking-wide mb-6"
            >
              {{ confirmModal.code }}
            </div>

            <div class="flex gap-2">
              <button
                @click="closeConfirmModal"
                class="flex-1 py-2.5 bg-slate-100 hover:bg-slate-200 text-slate-600 rounded-2xl text-xs font-bold transition-all"
              >
                HỦY BỎ
              </button>
              <button
                :class="[
                  'flex-1 py-2.5 text-white rounded-2xl text-xs font-bold transition-all shadow-md',
                  confirmModal.type === 'update'
                    ? 'bg-emerald-600 hover:bg-emerald-500 shadow-emerald-600/10'
                    : 'bg-rose-600 hover:bg-rose-700 shadow-rose-600/10',
                ]"
                :disabled="saving"
                @click="handleConfirmAction"
              >
                {{ saving ? 'Đang xử lý...' : confirmModal.confirmText }}
              </button>
            </div>
          </div>
        </div>
      </Teleport>
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted, ref, watch } from 'vue'
import { useToast } from 'vue-toastification'
import {
  addVoucherService,
  deleteVoucherService,
  phanTrangVoucherService,
  searchVoucherService,
  updateVoucherService,
} from '@/service/VoucherService'

const toast = useToast(),
  vouchers = ref([]),
  pageNo = ref(0),
  pageSize = ref(5),
  keyword = ref(''),
  showForm = ref(false),
  loading = ref(false),
  saving = ref(false)

const errors = ref({
  tenVoucher: '',
  giaTriGiam: '',
  giaTriDonHangToiThieu: '',
  giaTriGiamToiDa: '',
  soLuong: '',
  ngayBatDau: '',
  ngayKetThuc: '',
})

const resetErrors = () => {
  errors.value = {
    tenVoucher: '',
    giaTriGiam: '',
    giaTriDonHangToiThieu: '',
    giaTriGiamToiDa: '',
    soLuong: '',
    ngayBatDau: '',
    ngayKetThuc: '',
  }
}

const confirmModal = ref({
  show: false,
  type: 'update',
  icon: '✓',
  label: '',
  title: '',
  message: '',
  code: '',
  confirmText: 'Xác nhận',
  payload: null,
})

const hasKeyword = computed(() => keyword.value.trim().length > 0)
const activeCount = computed(() => vouchers.value.filter((i) => i.trangThai === 1).length)
const usedQuantity = computed(() =>
  vouchers.value.reduce((s, i) => s + Number(i.soLuongDaDung || 0), 0),
)
const totalQuantity = computed(() => vouchers.value.reduce((s, i) => s + Number(i.soLuong || 0), 0))
const remainingQuantity = computed(() => Math.max(totalQuantity.value - usedQuantity.value, 0))

const emptyForm = () => ({
  id: null,
  tenVoucher: '',
  loaiGiamGia: 'phan_tram',
  giaTriGiam: 1,
  giaTriDonHangToiThieu: 0,
  giaTriGiamToiDa: null,
  soLuong: 0,
  ngayBatDau: '',
  ngayKetThuc: '',
  moTa: '',
  trangThai: 1,
})

const form = ref(emptyForm())

const normalizeDateTime = (v) => (v ? (v.length === 16 ? `${v}:00` : v) : null)

const buildPayload = () => {
  const safeNum = (v) => {
    const n = Number(v)
    return isNaN(n) ? 0 : n
  }

  return {
    tenVoucher: form.value.tenVoucher?.trim(),
    loaiGiamGia: form.value.loaiGiamGia,
    giaTriGiam: safeNum(form.value.giaTriGiam),
    giaTriDonHangToiThieu: safeNum(form.value.giaTriDonHangToiThieu),
    giaTriGiamToiDa:
      form.value.giaTriGiamToiDa === null || form.value.giaTriGiamToiDa === ''
        ? null
        : safeNum(form.value.giaTriGiamToiDa),
    soLuong: safeNum(form.value.soLuong),
    ngayBatDau: normalizeDateTime(form.value.ngayBatDau),
    ngayKetThuc: normalizeDateTime(form.value.ngayKetThuc),
    moTa: form.value.moTa,
    trangThai: Number(form.value.trangThai),
  }
}

const fetchVouchers = async () => {
  loading.value = true
  try {
    vouchers.value = hasKeyword.value
      ? await searchVoucherService(keyword.value)
      : await phanTrangVoucherService(pageNo.value, pageSize.value)
  } catch (e) {
    toast.error(e.message || 'Không tải được danh sách phiếu giảm giá')
  } finally {
    loading.value = false
  }
}

const resetSearch = async () => {
  keyword.value = ''
  pageNo.value = 0
  await fetchVouchers()
}

const changePage = async (step) => {
  pageNo.value += step
  await fetchVouchers()
}

const handleSizeChange = async () => {
  pageNo.value = 0
  await fetchVouchers()
}

const openCreateForm = () => {
  form.value = emptyForm()
  resetErrors()
  showForm.value = true
}

const closeForm = () => {
  showForm.value = false
  form.value = emptyForm()
  resetErrors()
}

const openEditForm = (item) => {
  form.value = {
    ...item,
    ngayBatDau: item.ngayBatDau ? item.ngayBatDau.slice(0, 16) : '',
    ngayKetThuc: item.ngayKetThuc ? item.ngayKetThuc.slice(0, 16) : '',
  }
  resetErrors()
  showForm.value = true
}
const validateForm = () => {
  resetErrors()
  const p = buildPayload()
  let isValid = true
  if (!p.tenVoucher) {
    errors.value.tenVoucher = 'Vui lòng nhập tên voucher.'
    isValid = false
  } else {
    // KIỂM TRA TRÙNG TÊN VOUCHER
    const isDuplicate = vouchers.value.some(
      (item) =>
        item.tenVoucher?.trim().toLowerCase() === p.tenVoucher.toLowerCase() &&
        item.id !== form.value.id,
    )

    if (isDuplicate) {
      errors.value.tenVoucher = 'Tên voucher này đã tồn tại, vui lòng chọn tên khác.'
      isValid = false
    }
  }

  if (p.giaTriGiam <= 0) {
    errors.value.giaTriGiam = 'Giá trị giảm phải lớn hơn 0.'
    isValid = false
  }

  if (p.giaTriDonHangToiThieu < 0) {
    errors.value.giaTriDonHangToiThieu = 'Giá trị đơn tối thiểu không được âm.'
    isValid = false
  }

  if (p.loaiGiamGia === 'phan_tram') {
    if (p.giaTriGiam > 100) {
      errors.value.giaTriGiam = 'Phần trăm giảm không được vượt quá 100%.'
      isValid = false
    }
    if (!p.giaTriGiamToiDa || p.giaTriGiamToiDa <= 0) {
      errors.value.giaTriGiamToiDa = 'Vui lòng nhập giá trị giảm tối đa cho voucher phần trăm.'
      isValid = false
    }
  }

  if (p.loaiGiamGia === 'tien_mat') {
    if (p.giaTriGiam > p.giaTriDonHangToiThieu && p.giaTriDonHangToiThieu > 0) {
      errors.value.giaTriGiam = 'Giá trị giảm không được lớn hơn đơn hàng tối thiểu.'
      isValid = false
    }
  }

  if (p.soLuong <= 0) {
    errors.value.soLuong = 'Số lượng phải lớn hơn 0.'
    isValid = false
  }

  if (!p.ngayBatDau) {
    errors.value.ngayBatDau = 'Vui lòng chọn ngày bắt đầu.'
    isValid = false
  }

  if (!p.ngayKetThuc) {
    errors.value.ngayKetThuc = 'Vui lòng chọn ngày kết thúc.'
    isValid = false
  }

  if (p.ngayBatDau && p.ngayKetThuc && new Date(p.ngayBatDau) >= new Date(p.ngayKetThuc)) {
    errors.value.ngayKetThuc = 'Ngày kết thúc phải sau ngày bắt đầu.'
    isValid = false
  }

  // BỔ SUNG: Kiểm tra nếu ngày kết thúc trước hiện tại mà chọn Hoạt động (trangThai = 1)
  if (p.ngayKetThuc && new Date(p.ngayKetThuc) < new Date() && p.trangThai === 1) {
    errors.value.ngayKetThuc = 'Voucher đã hết hạn, không thể chọn trạng thái Đang hoạt động.'
    isValid = false
  }

  return isValid
}

const requestSaveVoucher = async () => {
  if (!validateForm()) {
    toast.warning('Vui lòng kiểm tra lại các trường thông tin lỗi.')
    return
  }

  if (form.value.id) {
    openUpdateVoucherConfirm()
    return
  }

  await executeSaveVoucher()
}

const executeSaveVoucher = async () => {
  saving.value = true
  try {
    if (form.value.id) {
      await updateVoucherService(form.value.id, buildPayload())
      toast.success('Cập nhật phiếu giảm giá thành công')
    } else {
      await addVoucherService(buildPayload())
      toast.success('Thêm phiếu giảm giá thành công')
    }
    closeForm()
    await fetchVouchers()
  } catch (e) {
    const message = e.message || 'Lưu phiếu giảm giá thất bại'
    toast.error(message)
  } finally {
    saving.value = false
  }
}

const removeVoucher = async (item) => {
  try {
    await deleteVoucherService(item.id)
    toast.success('Đã chuyển phiếu giảm giá sang ngừng hoạt động')
    await fetchVouchers()
  } catch (e) {
    toast.error(e.message || 'Xóa phiếu giảm giá thất bại')
  }
}

const openUpdateVoucherConfirm = () => {
  confirmModal.value = {
    show: true,
    type: 'update',
    icon: '✓',
    label: 'Xác nhận cập nhật',
    title: 'Lưu thay đổi phiếu giảm giá?',
    message: 'Các thông tin mới sẽ được cập nhật vào hệ thống và áp dụng ngay.',
    code: buildPayload().maVoucher || '',
    confirmText: 'Lưu cập nhật',
    payload: null,
  }
}

const closeConfirmModal = () => {
  confirmModal.value = {
    show: false,
    type: 'update',
    icon: '✓',
    label: '',
    title: '',
    message: '',
    code: '',
    confirmText: 'Xác nhận',
    payload: null,
  }
}

const handleConfirmAction = async () => {
  const { type, payload } = confirmModal.value
  closeConfirmModal()
  if (type === 'update') await executeSaveVoucher()
  else if (type === 'delete' && payload) await removeVoucher(payload)
}

const formatMoney = (v) => new Intl.NumberFormat('vi-VN').format(Number(v || 0)) + ' đ'
const formatDiscount = (i) =>
  i.loaiGiamGia === 'phan_tram' ? `${i.giaTriGiam}%` : formatMoney(i.giaTriGiam)
const formatDate = (v) => (v ? new Date(v).toLocaleString('vi-VN') : '-')
const getUsagePercent = (i) => {
  const t = Number(i.soLuong || 0)
  return t <= 0 ? 0 : Math.min(Math.round((Number(i.soLuongDaDung || 0) / t) * 100), 100)
}

let searchTimeout = null
watch(keyword, () => {
  clearTimeout(searchTimeout)
  searchTimeout = setTimeout(() => {
    pageNo.value = 0
    fetchVouchers()
  }, 400)
})

watch(
  () => form.value.loaiGiamGia,
  (newVal) => {
    if (newVal === 'tien_mat') {
      form.value.giaTriGiamToiDa = null
      errors.value.giaTriGiamToiDa = ''
    } else if (newVal === 'phan_tram') {
      if (form.value.giaTriGiam > 100) {
        form.value.giaTriGiam = 10
      }
    }
  },
)

const formatMoneyInput = (value) => {
  if (value === null || value === undefined || value === '') return ''
  return new Intl.NumberFormat('vi-VN').format(Number(value))
}

const parseMoneyInput = (value) => {
  if (!value) return 0
  const onlyNumber = value.replace(/\D/g, '')
  return Number(onlyNumber || 0)
}

const preventNegative = (e) => {
  if (['-', 'e', 'E', '+'].includes(e.key)) e.preventDefault()
}

const giaTriGiamDisplay = computed({
  get() {
    if (
      form.value.giaTriGiam === 0 ||
      form.value.giaTriGiam === null ||
      form.value.giaTriGiam === undefined
    )
      return ''
    if (form.value.loaiGiamGia === 'phan_tram') {
      return form.value.giaTriGiam
    }
    return formatMoneyInput(form.value.giaTriGiam)
  },
  set(value) {
    if (form.value.loaiGiamGia === 'phan_tram') {
      const clean = value.replace(/\D/g, '')
      form.value.giaTriGiam = clean === '' ? 0 : Number(clean)
    } else {
      form.value.giaTriGiam = parseMoneyInput(value)
    }
  },
})

const donToiThieuDisplay = computed({
  get() {
    return form.value.giaTriDonHangToiThieu === 0
      ? ''
      : formatMoneyInput(form.value.giaTriDonHangToiThieu)
  },
  set(value) {
    form.value.giaTriDonHangToiThieu = parseMoneyInput(value)
  },
})

const giamToiDaDisplay = computed({
  get() {
    return !form.value.giaTriGiamToiDa ? '' : formatMoneyInput(form.value.giaTriGiamToiDa)
  },
  set(value) {
    form.value.giaTriGiamToiDa = parseMoneyInput(value)
  },
})

onMounted(fetchVouchers)
</script>

<style scoped>
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
.animate-scale-up {
  animation: scaleUp 0.22s cubic-bezier(0.16, 1, 0.3, 1) forwards;
}
select {
  background-image: url("data:image/svg+xml;charset=UTF-8,%3csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24' fill='none' stroke='%2364748b' stroke-width='2.5' stroke-linecap='round' stroke-linejoin='round'%3e%3cpolyline points='6 9 12 15 18 9'%3e%3c/polyline%3e%3c/svg%3e");
  background-repeat: no-repeat;
  background-position: right 0.75rem center;
  background-size: 1em;
  padding-right: 2rem;
  -webkit-appearance: none;
  -moz-appearance: none;
  appearance: none;
}
</style>
