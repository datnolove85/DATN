<template>
  <div
    class="space-y-6 max-w-full mx-auto p-4 animate-fade-in bg-slate-50 text-slate-800 rounded-2xl selection:bg-indigo-100 selection:text-indigo-900 overflow-hidden"
  >
    <div
      class="relative p-8 bg-[#0b0f19] rounded-3xl text-white flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4 shadow-sm overflow-hidden"
    >
      <div class="relative z-10">
        <span class="text-[10px] tracking-widest uppercase font-bold text-indigo-400"
          >Quản lý khuyến mãi</span
        >
        <h2 class="text-2xl font-bold tracking-tight mt-1 text-white">Phiếu giảm giá</h2>
        <p class="text-xs text-slate-400 mt-1 flex items-center gap-2">
          <span class="relative flex h-2 w-2">
            <span
              class="animate-ping absolute inline-flex h-full w-full rounded-full bg-indigo-400 opacity-75"
            ></span>
            <span class="relative inline-block rounded-full h-2 w-2 bg-indigo-500"></span>
          </span>
          Tạo, theo dõi và kiểm soát điều kiện áp dụng voucher cho đơn hàng
        </p>
      </div>
      <button
        @click="openCreateForm"
        class="relative z-10 flex items-center gap-2 px-5 py-3 bg-indigo-600 hover:bg-indigo-500 text-white rounded-2xl text-xs font-bold shadow-lg shadow-indigo-600/20 transition-all active:scale-95 whitespace-nowrap"
      >
        <svg
          xmlns="http://www.w3.org/2000/svg"
          fill="none"
          viewBox="0 0 24 24"
          stroke-width="2.5"
          stroke="currentColor"
          class="w-4 h-4"
        >
          <path stroke-linecap="round" stroke-linejoin="round" d="M12 4.5v15m7.5-7.5h-15" />
        </svg>
        THÊM PHIẾU GIẢM GIÁ
      </button>
    </div>

    <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
      <div
        class="bg-white px-5 py-4 rounded-2xl border border-slate-100 shadow-sm relative overflow-hidden group"
      >
        <span class="text-[10px] font-bold text-slate-400 uppercase tracking-wider">
          Tổng phiếu
        </span>

        <strong class="block text-xl font-black text-slate-800 mt-1.5 font-mono">
          {{ vouchers.length }}
        </strong>

        <small class="block text-[10px] text-slate-400 mt-0.5 font-medium">
          Đang hiển thị trên hệ thống
        </small>

        <div
          class="absolute -right-3 -top-3 w-10 h-10 rounded-full bg-slate-50 group-hover:scale-110 transition-transform"
        ></div>
      </div>

      <div
        class="bg-white px-5 py-4 rounded-2xl border border-slate-100 shadow-sm relative overflow-hidden group"
      >
        <span class="text-[10px] font-bold text-emerald-600 uppercase tracking-wider">
          Đang hoạt động
        </span>

        <strong class="block text-xl font-black text-emerald-600 mt-1.5 font-mono">
          {{ activeCount }}
        </strong>

        <small class="block text-[10px] text-slate-400 mt-0.5 font-medium">
          Có thể áp dụng ngay
        </small>

        <div
          class="absolute -right-3 -top-3 w-10 h-10 rounded-full bg-emerald-50 group-hover:scale-110 transition-transform"
        ></div>
      </div>

      <div
        class="bg-white px-5 py-4 rounded-2xl border border-slate-100 shadow-sm relative overflow-hidden group"
      >
        <span class="text-[10px] font-bold text-amber-600 uppercase tracking-wider"> Đã dùng </span>

        <strong class="block text-xl font-black text-amber-600 mt-1.5 font-mono">
          {{ usedQuantity }}
        </strong>

        <small class="block text-[10px] text-slate-400 mt-0.5 font-medium">
          Lượt sử dụng thực tế
        </small>

        <div
          class="absolute -right-3 -top-3 w-10 h-10 rounded-full bg-amber-50 group-hover:scale-110 transition-transform"
        ></div>
      </div>

      <div
        class="bg-white px-5 py-4 rounded-2xl border border-slate-100 shadow-sm relative overflow-hidden group"
      >
        <span class="text-[10px] font-bold text-indigo-600 uppercase tracking-wider">
          Còn lại
        </span>

        <strong class="block text-xl font-black text-indigo-600 mt-1.5 font-mono">
          {{ remainingQuantity }}
        </strong>

        <small class="block text-[10px] text-slate-400 mt-0.5 font-medium">
          Lượt khả dụng còn lại
        </small>

        <div
          class="absolute -right-3 -top-3 w-10 h-10 rounded-full bg-indigo-50 group-hover:scale-110 transition-transform"
        ></div>
      </div>
    </div>

    <div
      class="flex flex-col sm:flex-row items-stretch sm:items-center gap-3 bg-white p-3 rounded-2xl border border-slate-100 shadow-sm"
    >
      <div class="relative flex-grow">
        <input
          v-model="keyword"
          type="text"
          placeholder="Tìm mã, tên hoặc loại giảm giá..."
          class="w-full pl-4 pr-4 h-11 bg-slate-50 border border-slate-100 rounded-xl"
        />
      </div>

      <span
        class="px-3 py-2 bg-indigo-50 text-indigo-600 rounded-xl text-xs font-bold whitespace-nowrap"
      >
        {{ vouchers.length }} kết quả
      </span>

      <button
        @click="resetSearch"
        class="px-4 h-11 bg-slate-100 hover:bg-slate-200 rounded-xl text-xs font-bold"
      >
        Làm mới
      </button>
    </div>

    <transition name="slide-fade">
      <div
        v-if="showForm"
        class="bg-white rounded-2xl border border-slate-200 shadow-xl overflow-hidden animate-scale-up"
      >
        <!-- HEADER SIÊU MỎNG -->
        <div
          class="px-5 py-3 bg-gradient-to-r from-indigo-600 to-violet-500 text-white flex items-center justify-between"
        >
          <h2 class="text-base font-bold flex items-center gap-2">
            <span class="text-xl">🎁</span> {{ form.id ? 'Cập nhật Voucher' : 'Tạo Voucher' }}
          </h2>
          <button @click="closeForm" class="hover:bg-white/20 p-1 rounded-lg transition">✕</button>
        </div>

        <!-- NỘI DUNG NÉN DỌC -->
        <div class="p-4 space-y-4">
          <!-- Hàng 1: Tên -->
          <div>
            <label class="block text-[10px] font-bold text-slate-400 uppercase mb-0.5"
              >Tên voucher</label
            >
            <input
              v-model.trim="form.tenVoucher"
              class="w-full h-9 rounded-lg border border-slate-200 px-3 text-sm focus:ring-2 focus:ring-indigo-100 outline-none"
            />
          </div>

          <!-- Hàng 2: Grid loại & giá trị -->
          <!-- Hàng 2: Grid loại & giá trị -->
          <div class="grid grid-cols-2 gap-3">
            <div>
              <label class="block text-[10px] font-bold text-slate-400 uppercase mb-0.5"
                >Loại giảm</label
              >
              <select
                v-model="form.loaiGiamGia"
                class="w-full h-9 rounded-lg border border-slate-200 px-2 text-sm outline-none"
              >
                <option value="phan_tram">Phần trăm (%)</option>
                <option value="tien_mat">Tiền mặt (VNĐ)</option>
              </select>
            </div>
            <div>
              <label class="block text-[10px] font-bold text-slate-400 uppercase mb-0.5"
                >Giá trị giảm</label
              >
              <input
                type="text"
                v-model="giaTriGiamDisplay"
                class="w-full h-9 rounded-lg border border-slate-200 px-3 text-sm outline-none"
              />
            </div>
          </div>

          <!-- Hàng 3: Điều kiện (Đơn tối thiểu luôn có, Tối đa chỉ hiện khi là %) -->
          <div class="grid grid-cols-2 gap-3">
            <div>
              <label class="block text-[10px] font-bold text-slate-400 uppercase mb-0.5"
                >Đơn tối thiểu</label
              >
              <input
                type="number"
                v-model.number="form.giaTriDonHangToiThieu"
                class="w-full h-9 rounded-lg border border-slate-200 px-3 text-sm outline-none"
              />
            </div>
            <div v-if="form.loaiGiamGia === 'phan_tram'">
              <label class="block text-[10px] font-bold text-slate-400 uppercase mb-0.5"
                >Giảm tối đa</label
              >
              <input
                type="number"
                v-model.number="form.giaTriGiamToiDa"
                class="w-full h-9 rounded-lg border border-slate-200 px-3 text-sm outline-none"
                placeholder="VD: 50.000"
              />
            </div>
          </div>
          <div class="grid grid-cols-2 gap-3">
            <div>
              <label class="block text-[10px] font-bold text-slate-400 uppercase mb-0.5">
                Số lượng
              </label>

              <input
                type="number"
                min="1"
                v-model.number="form.soLuong"
                class="w-full h-9 rounded-lg border border-slate-200 px-3 text-sm outline-none"
              />
            </div>

            <div>
              <label class="block text-[10px] font-bold text-slate-400 uppercase mb-0.5">
                Trạng thái
              </label>

              <select
                v-model.number="form.trangThai"
                class="w-full h-9 rounded-lg border border-slate-200 px-2 text-sm outline-none"
              >
                <option :value="1">Hoạt động</option>
                <option :value="0">Ngừng hoạt động</option>
              </select>
            </div>
          </div>

          <!-- Hàng 4: Thời gian -->
          <div class="grid grid-cols-2 gap-3">
            <div>
              <label class="block text-[10px] font-bold text-slate-400 uppercase mb-0.5"
                >Ngày bắt đầu</label
              >
              <input
                type="datetime-local"
                v-model="form.ngayBatDau"
                class="w-full h-9 rounded-lg border border-slate-200 px-3 text-sm outline-none"
              />
            </div>
            <div>
              <label class="block text-[10px] font-bold text-slate-400 uppercase mb-0.5"
                >Ngày kết thúc</label
              >
              <input
                type="datetime-local"
                v-model="form.ngayKetThuc"
                class="w-full h-9 rounded-lg border border-slate-200 px-3 text-sm outline-none"
              />
            </div>
          </div>
        </div>

        <!-- FOOTER MỎNG -->
        <div class="flex justify-end gap-2 px-4 py-3 bg-slate-50 border-t border-slate-100">
          <button
            @click="closeForm"
            class="px-4 h-9 rounded-lg border border-slate-200 text-xs font-bold text-slate-600 hover:bg-slate-100 transition"
          >
            Hủy
          </button>
          <button
            @click="requestSaveVoucher"
            class="px-4 h-9 rounded-lg bg-indigo-600 text-white text-xs font-bold hover:bg-indigo-700 transition"
          >
            {{ form.id ? 'Lưu thay đổi' : 'Tạo mới' }}
          </button>
        </div>
      </div>
    </transition>

    <div class="space-y-3">
      <div class="overflow-x-auto bg-white border border-slate-100 rounded-2xl shadow-sm">
        <table class="w-full border-collapse text-left min-w-[1000px]">
          <thead>
            <tr
              class="bg-slate-50 border-b border-slate-100 text-[11px] font-bold text-slate-400 uppercase tracking-wider select-none"
            >
              <th class="py-3.5 px-4 text-center w-12">#</th>
              <th class="py-3.5 px-4">Mã</th>
              <th class="py-3.5 px-4 w-64">Thông tin phiếu</th>
              <th class="py-3.5 px-4 text-center">Loại</th>
              <th class="py-3.5 px-4 text-center">Giá trị giảm</th>
              <th class="py-3.5 px-4">Điều kiện áp dụng</th>
              <th class="py-3.5 px-4 w-40">Tỉ lệ sử dụng</th>
              <th class="py-3.5 px-4">Thời gian</th>
              <th class="py-3.5 px-4 text-center">Trạng thái</th>
              <th class="py-3.5 px-4 text-center">Thao tác</th>
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
              class="hover:bg-slate-50/60 transition-colors group"
            >
              <td class="py-3.5 px-4 text-center font-bold text-slate-400 font-mono">
                {{ pageNo * pageSize + index + 1 }}
              </td>
              <td class="py-3.5 px-4">
                <span
                  class="inline-block px-2 py-0.5 bg-slate-50 text-indigo-600 font-bold rounded-xl border border-slate-100 font-mono tracking-wide uppercase group-hover:bg-white group-hover:border-indigo-400 transition-all"
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
                    'inline-block px-2 py-0.5 text-[10px] font-bold uppercase rounded-xl border',
                    item.loaiGiamGia === 'phan_tram'
                      ? 'bg-purple-50 text-purple-600 border-purple-100'
                      : 'bg-emerald-50 text-emerald-600 border-emerald-100',
                  ]"
                >
                  {{ item.loaiGiamGia === 'phan_tram' ? 'Phần trăm' : 'Tiền mặt' }}
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
                <div class="text-slate-600 font-medium">BD: {{ formatDate(item.ngayBatDau) }}</div>
                <div>KT: {{ formatDate(item.ngayKetThuc) }}</div>
              </td>
              <td class="py-3.5 px-4 text-center">
                <span
                  :class="[
                    'inline-flex items-center gap-1 px-2.5 py-0.5 text-[10px] font-bold uppercase tracking-wider rounded-xl border',
                    item.trangThai === 1
                      ? 'bg-emerald-50 text-emerald-600 border-emerald-100'
                      : 'bg-rose-50 text-rose-600 border-rose-100',
                  ]"
                >
                  <span
                    :class="[
                      'w-1 h-1 rounded-full',
                      item.trangThai === 1 ? 'bg-emerald-500 animate-pulse' : 'bg-rose-500',
                    ]"
                  ></span>
                  {{ item.trangThai === 1 ? 'Đang diễn ra' : 'Ngừng hoạt động' }}
                </span>
              </td>
              <td class="py-3.5 px-4">
                <div class="flex items-center justify-center gap-1">
                  <button
                    @click="openEditForm(item)"
                    class="p-1.5 text-slate-400 hover:text-indigo-600 hover:bg-indigo-50 rounded-xl transition-all"
                    title="Chỉnh sửa"
                  >
                    <svg
                      xmlns="http://www.w3.org/2000/svg"
                      class="w-4 h-4"
                      viewBox="0 0 24 24"
                      fill="none"
                      stroke="currentColor"
                      stroke-width="2.5"
                    >
                      <path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7" />
                      <path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z" />
                    </svg>
                  </button>
                  <button
                    @click="openDeleteVoucherConfirm(item)"
                    class="p-1.5 text-slate-400 hover:text-rose-600 hover:bg-rose-50 rounded-xl transition-all"
                    title="Dừng hoạt động"
                  >
                    <svg
                      xmlns="http://www.w3.org/2000/svg"
                      class="w-4 h-4"
                      viewBox="0 0 24 24"
                      fill="none"
                      stroke="currentColor"
                      stroke-width="2.5"
                    >
                      <polyline points="3 6 5 6 21 6" />
                      <path
                        d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"
                      />
                    </svg>
                  </button>
                </div>
              </td>
            </tr>

            <tr v-if="!loading && vouchers.length === 0">
              <td colspan="10" class="py-20 text-center">
                <div class="flex flex-col items-center justify-center gap-2 text-slate-400">
                  <div
                    class="w-12 h-12 rounded-2xl bg-slate-50 text-indigo-600 border border-slate-100 flex items-center justify-center text-lg font-black"
                  >
                    %
                  </div>
                  <strong class="text-sm font-bold text-slate-800 mt-1"
                    >Không tìm thấy phiếu giảm giá</strong
                  >
                  <span class="text-xs"
                    >Hãy tạo voucher mới hoặc tinh chỉnh lại từ khóa tìm kiếm.</span
                  >
                </div>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <div
      class="flex flex-col sm:flex-row justify-between items-center gap-4 bg-white p-4 rounded-2xl border border-slate-100 shadow-sm text-xs font-bold text-slate-500"
    >
      <div class="flex items-center gap-2">
        <span>Hiển thị</span>
        <select
          v-model.number="pageSize"
          @change="handleSizeChange"
          class="bg-slate-50 border border-slate-100 py-1.5 pl-3 pr-8 rounded-lg outline-none cursor-pointer focus:border-indigo-500 text-slate-700 font-mono"
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

    <Teleport to="body">
      <div
        v-if="confirmModal.show"
        class="fixed inset-0 z-[999] flex items-center justify-center p-4"
      >
        <div
          class="absolute inset-0 bg-slate-900/30 backdrop-blur-sm"
          @click="closeConfirmModal"
        ></div>
        <div
          class="bg-white border border-slate-100 rounded-3xl shadow-xl w-full max-w-sm p-6 z-10 animate-scale-up text-center relative overflow-hidden"
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
            class="inline-block px-2 py-0.5 bg-slate-50 text-slate-400 border border-slate-100 rounded-lg text-[10px] uppercase font-bold tracking-wider mb-2"
          >
            {{ confirmModal.label }}
          </span>
          <h3 class="text-sm font-bold text-slate-800 mb-1.5 uppercase tracking-tight">
            {{ confirmModal.title }}
          </h3>
          <p class="text-[11px] text-slate-400 line-height-relaxed px-2 mb-4">
            {{ confirmModal.message }}
          </p>

          <div
            v-if="confirmModal.code"
            class="inline-block px-3 py-1 bg-slate-50 border border-slate-100 rounded-xl font-mono text-xs font-bold text-slate-700 uppercase tracking-wide mb-6"
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
  errorMessage = ref(''),
  loading = ref(false),
  saving = ref(false)
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
const normalizeNumber = (v, d = 0) => (!v && v !== 0 ? d : Number(v))

const buildPayload = () => {
  // Đảm bảo số lượng và giá trị không bao giờ là NaN
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
  errorMessage.value = ''
  showForm.value = true
}
const closeForm = () => {
  showForm.value = false
  form.value = emptyForm()
  errorMessage.value = ''
}

const openEditForm = (item) => {
  form.value = {
    ...item,
    ngayBatDau: item.ngayBatDau ? item.ngayBatDau.slice(0, 16) : '',
    ngayKetThuc: item.ngayKetThuc ? item.ngayKetThuc.slice(0, 16) : '',
  }
  errorMessage.value = ''
  showForm.value = true
}
const validateForm = () => {
  const p = buildPayload()

  if (!p.tenVoucher) return 'Vui lòng nhập tên voucher.'
  if (p.giaTriGiam <= 0) return 'Giá trị giảm phải lớn hơn 0.'

  // Kiểm tra Đơn tối thiểu
  if (p.giaTriDonHangToiThieu < 0) return 'Giá trị đơn tối thiểu không được âm.'

  if (p.loaiGiamGia === 'phan_tram') {
    if (p.giaTriGiam > 100) return 'Phần trăm giảm không được vượt quá 100%.'
    // Bắt buộc có giảm tối đa khi chọn %
    if (!p.giaTriGiamToiDa || p.giaTriGiamToiDa <= 0) {
      return 'Vui lòng nhập giá trị giảm tối đa cho voucher phần trăm.'
    }
  }

  if (p.loaiGiamGia === 'tien_mat') {
    if (p.giaTriGiam > p.giaTriDonHangToiThieu) {
      return 'Giá trị giảm không được lớn hơn đơn hàng tối thiểu.'
    }
  }

  if (!p.ngayBatDau || !p.ngayKetThuc) return 'Vui lòng chọn thời gian.'
  if (new Date(p.ngayBatDau) >= new Date(p.ngayKetThuc)) {
    return 'Ngày kết thúc phải sau ngày bắt đầu.'
  }

  return ''
}
const requestSaveVoucher = async () => {
  errorMessage.value = validateForm()
  if (errorMessage.value) return
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
    errorMessage.value = e.message || 'Lưu phiếu giảm giá thất bại'
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

const openDeleteVoucherConfirm = (item) => {
  confirmModal.value = {
    show: true,
    type: 'delete',
    icon: '✕',
    label: 'Xác nhận xóa',
    title: 'Chuyển phiếu sang ngừng hoạt động?',
    message: `Phiếu "${item.tenVoucher || item.maVoucher}" sẽ dừng áp dụng cho đơn hàng mới.`,
    code: item.maVoucher || '',
    confirmText: 'Xóa phiếu',
    payload: item,
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

watch(keyword, (value) => {
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
      form.value.giaTriGiamToiDa = null // Xóa giá trị tối đa khi chuyển sang tiền mặt
    }
  },
)

const formatMoneyInput = (value) => {
  if (value === null || value === undefined || value === '') return ''

  return new Intl.NumberFormat('vi-VN').format(Number(value))
}

const parseMoneyInput = (value) => {
  if (!value) return 0

  return Number(value.replace(/\./g, '').replace(/,/g, ''))
}
const giaTriGiamDisplay = computed({
  get() {
    return form.value.giaTriGiam === 0 ? '' : formatMoneyInput(form.value.giaTriGiam)
  },
  set(value) {
    const parsed = parseMoneyInput(value)
    form.value.giaTriGiam = parsed
  },
})
onMounted(fetchVouchers)
</script>

<style scoped>
@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(8px);
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
  animation: fadeIn 0.35s ease-out forwards;
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
