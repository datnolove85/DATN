<template>
  <div class="p-8 bg-gray-50 min-h-screen font-sans relative">
    <!-- Thông báo dạng Toast -->
    <div
      v-if="notification.message"
      :class="[
        'fixed top-5 right-5 z-[9999] min-w-[320px] p-4 rounded-2xl text-white font-bold shadow-2xl flex items-center space-x-3 transition-all',
        notification.type === 'success' ? 'bg-emerald-600' : 'bg-rose-600',
      ]"
    >
      <span class="text-xl">{{ notification.type === 'success' ? '✨' : '⚠️' }}</span>
      <span>{{ notification.message }}</span>
    </div>

    <!-- Header -->
    <div
      class="flex flex-col sm:flex-row justify-between items-start sm:items-center bg-white rounded-2xl shadow-sm border border-slate-200 px-6 py-4 gap-4 mb-6"
    >
      <div class="flex items-center gap-3">
        <div
          class="w-10 h-10 rounded-xl bg-indigo-50 flex items-center justify-center text-xl text-indigo-600 font-bold"
        >
          ⚙️
        </div>

        <div>
          <h1 class="text-xl font-bold text-slate-800">Quản lý cấu hình hệ thống</h1>
          <p class="text-slate-500 text-xs">
            Thiết lập quy đổi xu, chính sách thanh toán và quản lý hạng thành viên của toàn hệ
            thống.
          </p>
        </div>
      </div>

      <div
        class="px-4 py-2 rounded-xl bg-indigo-50 text-indigo-700 text-sm font-medium flex items-center gap-2"
      >
        <span>💰</span>
        <span>Hệ thống tích xu & phân hạng</span>
      </div>
    </div>

    <!-- Hệ thống Tabs chuyển đổi -->
    <div class="flex border-b border-gray-200 mb-6 bg-white px-4 rounded-t-2xl shadow-sm">
      <button
        @click="activeTab = 'xu'"
        :class="[
          'px-6 py-4 font-bold text-sm border-b-2 flex items-center gap-2 transition-all cursor-pointer',
          activeTab === 'xu'
            ? 'border-indigo-600 text-indigo-600 bg-indigo-50/50'
            : 'border-transparent text-gray-500 hover:text-gray-700',
        ]"
      >
        <span>🪙</span> Cấu Hình Xu & Thanh Toán
      </button>
      <button
        @click="activeTab = 'hang'"
        :class="[
          'px-6 py-4 font-bold text-sm border-b-2 flex items-center gap-2 transition-all cursor-pointer',
          activeTab === 'hang'
            ? 'border-indigo-600 text-indigo-600 bg-indigo-50/50'
            : 'border-transparent text-gray-500 hover:text-gray-700',
        ]"
      >
        <span>👑</span> Cấu Hình Hạng Thành Viên
      </button>
    </div>

    <!-- Loading chung -->
    <div v-if="loading" class="p-12 text-center text-gray-400 flex flex-col items-center gap-2">
      <span class="animate-spin text-2xl">⏳</span> Đang tải dữ liệu...
    </div>

    <div v-else>
      <!-- TAB 1: CẤU HÌNH XU -->
      <div v-show="activeTab === 'xu'" class="space-y-6">
        <!-- 1. Tỷ lệ tích xu -->
        <div class="bg-white rounded-2xl shadow-sm border border-gray-200 p-6">
          <div class="flex items-center justify-between mb-4 pb-4 border-b border-gray-100">
            <div>
              <h3 class="text-base font-bold text-gray-800 flex items-center gap-2">
                <span>💰</span> Định mức quy đổi tích xu tự động
              </h3>
              <p class="text-xs text-gray-500 mt-0.5">
                Thiết lập số tiền mua hàng tương ứng để quy đổi ra 1 xu gốc cho toàn hệ thống.
              </p>
            </div>
          </div>

          <form
            @submit.prevent="handleUpdateConfig('TI_LE_TICH_XU', xuForm.tiLeTichXu)"
            class="grid grid-cols-1 md:grid-cols-3 gap-6 items-end"
          >
            <div>
              <label class="block text-xs font-bold text-gray-700 uppercase mb-1">
                Mức tiền quy đổi (VNĐ / 1 Xu) <span class="text-rose-500">*</span>
              </label>
              <div class="relative flex items-center">
                <input
                  type="text"
                  v-model="xuForm.tiLeTichXu.displayValue"
                  @input="(e) => handleInputNumber(e, xuForm.tiLeTichXu, 'tiLeTichXu')"
                  class="w-full border border-gray-300 rounded-xl px-4 py-2.5 text-sm focus:ring-2 focus:ring-indigo-500 outline-none font-semibold text-gray-800"
                  placeholder="Ví dụ: 300.000"
                  autocomplete="off"
                />
                <span class="absolute right-4 text-sm font-bold text-gray-400">VNĐ</span>
              </div>
              <small
                v-if="xuForm.tiLeTichXu.error"
                class="text-rose-500 text-xs mt-1 block font-medium"
              >
                ⚠️ Giá trị phải lớn hơn 0!
              </small>
            </div>

            <div>
              <label class="block text-xs font-bold text-gray-700 uppercase mb-1"
                >Mô tả hiển thị</label
              >
              <div
                class="p-2.5 bg-indigo-50/60 border border-indigo-100 rounded-xl text-indigo-900 font-medium text-xs flex items-center gap-2 min-h-[42px]"
              >
                <span>💡</span>
                <span>{{ xuForm.tiLeTichXu.moTa }}</span>
              </div>
            </div>

            <div>
              <button
                type="submit"
                class="w-full bg-indigo-600 hover:bg-indigo-700 text-white px-5 py-2.5 rounded-xl text-sm font-medium transition shadow-md flex items-center justify-center gap-2 cursor-pointer"
              >
                <span>💾</span> Lưu Cấu Hình Tích Xu
              </button>
            </div>
          </form>
        </div>

        <!-- 2. Tỷ lệ giảm tối đa xu -->
        <div class="bg-white rounded-2xl shadow-sm border border-gray-200 p-6">
          <div class="flex items-center justify-between mb-4 pb-4 border-b border-gray-100">
            <div>
              <h3 class="text-base font-bold text-gray-800 flex items-center gap-2">
                <span>📉</span> Giới hạn sử dụng xu thanh toán
              </h3>
              <p class="text-xs text-gray-500 mt-0.5">
                Thiết lập tỷ lệ phần trăm tối đa giá trị đơn hàng được phép thanh toán bằng xu.
              </p>
            </div>
          </div>

          <form
            @submit.prevent="handleUpdateConfig('TY_LE_GIAM_TOI_DA_XU', xuForm.tyLeGiamToiDa)"
            class="grid grid-cols-1 md:grid-cols-3 gap-6 items-end"
          >
            <div>
              <label class="block text-xs font-bold text-gray-700 uppercase mb-1">
                Tỷ lệ giảm tối đa (%) <span class="text-rose-500">*</span>
              </label>
              <div class="relative flex items-center">
                <input
                  type="text"
                  v-model="xuForm.tyLeGiamToiDa.displayValue"
                  @input="(e) => handleInputNumber(e, xuForm.tyLeGiamToiDa, 'percent')"
                  class="w-full border border-gray-300 rounded-xl px-4 py-2.5 text-sm focus:ring-2 focus:ring-indigo-500 outline-none font-semibold text-gray-800"
                  placeholder="Ví dụ: 50"
                  autocomplete="off"
                />
                <span class="absolute right-4 text-sm font-bold text-gray-400">%</span>
              </div>
              <small
                v-if="xuForm.tyLeGiamToiDa.error"
                class="text-rose-500 text-xs mt-1 block font-medium"
              >
                ⚠️ Tỷ lệ phải từ 0 đến 100!
              </small>
            </div>

            <div>
              <label class="block text-xs font-bold text-gray-700 uppercase mb-1"
                >Mô tả hiển thị</label
              >
              <div
                class="p-2.5 bg-indigo-50/60 border border-indigo-100 rounded-xl text-indigo-900 font-medium text-xs flex items-center gap-2 min-h-[42px]"
              >
                <span>💡</span>
                <span>{{ xuForm.tyLeGiamToiDa.moTa }}</span>
              </div>
            </div>

            <div>
              <button
                type="submit"
                class="w-full bg-indigo-600 hover:bg-indigo-700 text-white px-5 py-2.5 rounded-xl text-sm font-medium transition shadow-md flex items-center justify-center gap-2 cursor-pointer"
              >
                <span>💾</span> Lưu Cấu Hình Giới Hạn
              </button>
            </div>
          </form>
        </div>

        <!-- 3. Tỷ lệ quy đổi xu -->
        <div class="bg-white rounded-2xl shadow-sm border border-gray-200 p-6">
          <div class="flex items-center justify-between mb-4 pb-4 border-b border-gray-100">
            <div>
              <h3 class="text-base font-bold text-gray-800 flex items-center gap-2">
                <span>🔄</span> Tỷ lệ quy đổi xu ra tiền mặt
              </h3>
              <p class="text-xs text-gray-500 mt-0.5">
                Xác định giá trị bằng tiền khi khách hàng sử dụng 1 xu để thanh toán đơn hàng.
              </p>
            </div>
          </div>

          <form
            @submit.prevent="handleUpdateConfig('TY_LE_QUY_DOI_XU', xuForm.tyLeQuyDoi)"
            class="grid grid-cols-1 md:grid-cols-3 gap-6 items-end"
          >
            <div>
              <label class="block text-xs font-bold text-gray-700 uppercase mb-1">
                Giá trị quy đổi (VNĐ / 1 Xu) <span class="text-rose-500">*</span>
              </label>
              <div class="relative flex items-center">
                <input
                  type="text"
                  v-model="xuForm.tyLeQuyDoi.displayValue"
                  @input="(e) => handleInputNumber(e, xuForm.tyLeQuyDoi, 'currency')"
                  class="w-full border border-gray-300 rounded-xl px-4 py-2.5 text-sm focus:ring-2 focus:ring-indigo-500 outline-none font-semibold text-gray-800"
                  placeholder="Ví dụ: 1.000"
                  autocomplete="off"
                />
                <span class="absolute right-4 text-sm font-bold text-gray-400">VNĐ</span>
              </div>
              <small
                v-if="xuForm.tyLeQuyDoi.error"
                class="text-rose-500 text-xs mt-1 block font-medium"
              >
                ⚠️ Giá trị phải lớn hơn 0!
              </small>
            </div>

            <div>
              <label class="block text-xs font-bold text-gray-700 uppercase mb-1"
                >Mô tả hiển thị</label
              >
              <div
                class="p-2.5 bg-indigo-50/60 border border-indigo-100 rounded-xl text-indigo-900 font-medium text-xs flex items-center gap-2 min-h-[42px]"
              >
                <span>💡</span>
                <span>{{ xuForm.tyLeQuyDoi.moTa }}</span>
              </div>
            </div>

            <div>
              <button
                type="submit"
                class="w-full bg-indigo-600 hover:bg-indigo-700 text-white px-5 py-2.5 rounded-xl text-sm font-medium transition shadow-md flex items-center justify-center gap-2 cursor-pointer"
              >
                <span>💾</span> Lưu Tỷ Lệ Quy Đổi
              </button>
            </div>
          </form>
        </div>

        <!-- 4. Chu kỳ xét hạng (MỚI) -->
        <div class="bg-white rounded-2xl shadow-sm border border-gray-200 p-6">
          <div class="flex items-center justify-between mb-4 pb-4 border-b border-gray-100">
            <div>
              <h3 class="text-base font-bold text-gray-800 flex items-center gap-2">
                <span>📅</span> Chu kỳ xét hạng thành viên
              </h3>
              <p class="text-xs text-gray-500 mt-0.5">
                Thiết lập số tháng định kỳ để hệ thống tự động quét và xét lại hạng cho khách hàng.
              </p>
            </div>
          </div>

          <form
            @submit.prevent="handleUpdateConfig('CHU_KY_XET_HANG', xuForm.chuKyXetHang)"
            class="grid grid-cols-1 md:grid-cols-3 gap-6 items-end"
          >
            <div>
              <label class="block text-xs font-bold text-gray-700 uppercase mb-1">
                Số tháng (Tháng) <span class="text-rose-500">*</span>
              </label>
              <div class="relative flex items-center">
                <input
                  type="text"
                  v-model="xuForm.chuKyXetHang.displayValue"
                  @input="(e) => handleInputNumber(e, xuForm.chuKyXetHang, 'month')"
                  class="w-full border border-gray-300 rounded-xl px-4 py-2.5 text-sm focus:ring-2 focus:ring-indigo-500 outline-none font-semibold text-gray-800"
                  placeholder="Ví dụ: 12"
                  autocomplete="off"
                />
                <span class="absolute right-4 text-sm font-bold text-gray-400">Tháng</span>
              </div>
              <small
                v-if="xuForm.chuKyXetHang.error"
                class="text-rose-500 text-xs mt-1 block font-medium"
              >
                ⚠️ Số tháng phải lớn hơn 0!
              </small>
            </div>

            <div>
              <label class="block text-xs font-bold text-gray-700 uppercase mb-1"
                >Mô tả hiển thị</label
              >
              <div
                class="p-2.5 bg-indigo-50/60 border border-indigo-100 rounded-xl text-indigo-900 font-medium text-xs flex items-center gap-2 min-h-[42px]"
              >
                <span>💡</span>
                <span>{{ xuForm.chuKyXetHang.moTa }}</span>
              </div>
            </div>

            <div>
              <button
                type="submit"
                class="w-full bg-indigo-600 hover:bg-indigo-700 text-white px-5 py-2.5 rounded-xl text-sm font-medium transition shadow-md flex items-center justify-center gap-2 cursor-pointer"
              >
                <span>💾</span> Lưu Chu Kỳ
              </button>
            </div>
          </form>
        </div>
      </div>

      <!-- TAB 2: CẤU HÌNH HẠNG THÀNH VIÊN -->
      <div
        v-show="activeTab === 'hang'"
        class="bg-white rounded-2xl shadow-sm border border-gray-200 overflow-hidden"
      >
        <div class="p-6 border-b border-gray-100 flex justify-between items-center">
          <div>
            <h3 class="text-base font-bold text-gray-800 flex items-center gap-2">
              <span>👑</span> Danh Sách Hạng Thành Viên & Hệ Số Tích Xu
            </h3>
            <p class="text-xs text-gray-500 mt-0.5">
              Quản lý các mức chi tiêu tối thiểu và hệ số nhân xu ưu đãi theo từng cấp bậc.
            </p>
          </div>
          <button
            @click="handleOpenModal(null)"
            class="bg-indigo-600 hover:bg-indigo-700 text-white px-4 py-2 rounded-xl text-sm font-medium shadow-md transition flex items-center gap-2 cursor-pointer"
          >
            <span>➕</span> Thêm Hạng Thành Viên
          </button>
        </div>

        <div class="overflow-x-auto">
          <table class="w-full text-left border-collapse">
            <thead>
              <tr class="bg-gray-50 text-gray-600 uppercase text-xs font-semibold">
                <th class="p-4">Tên Hạng</th>
                <th class="p-4">Ngưỡng Chi Tiêu Tối Thiểu (VNĐ)</th>
                <th class="p-4">Hệ Số Tích Xu</th>
                <th class="p-4">Trạng Thái</th>
                <th class="p-4 text-center">Hành Động</th>
              </tr>
            </thead>
            <tbody class="divide-y divide-gray-200 text-sm">
              <tr v-for="hang in danhSachHang" :key="hang.id" class="hover:bg-gray-50">
                <td class="p-4 font-semibold text-gray-800 flex items-center gap-3">
                  <span
                    :class="[
                      'w-10 h-10 rounded-2xl flex items-center justify-center text-lg shadow-sm',
                      getRankBadgeStyle(hang.tenHang),
                    ]"
                  >
                    {{ getRankIcon(hang.tenHang) }}
                  </span>
                  <div>
                    <span class="text-gray-900 font-bold block text-base">{{ hang.tenHang }}</span>
                    <span class="text-xs text-gray-400 font-normal">Cấp bậc thành viên</span>
                  </div>
                </td>
                <td class="p-4 text-gray-600 font-medium">
                  {{
                    hang.nguongChiTieuToiThieu
                      ? Number(hang.nguongChiTieuToiThieu).toLocaleString('vi-VN')
                      : '0'
                  }}
                  đ
                </td>
                <td class="p-4 text-indigo-600 font-bold">x{{ hang.tyLeTichXu }}</td>
                <td class="p-4">
                  <span
                    :class="[
                      'px-3 py-1 rounded-full text-xs font-semibold inline-flex items-center gap-1.5',
                      hang.trangThai
                        ? 'bg-emerald-100 text-emerald-700'
                        : 'bg-gray-100 text-gray-600',
                    ]"
                  >
                    <span
                      class="w-2 h-2 rounded-full"
                      :class="hang.trangThai ? 'bg-emerald-500' : 'bg-gray-400'"
                    ></span>
                    {{ hang.trangThai ? 'Đang áp dụng' : 'Khóa' }}
                  </span>
                </td>
                <td class="p-4 text-center">
                  <div class="inline-flex items-center justify-center gap-2">
                    <button
                      @click="handleOpenModal(hang)"
                      class="px-3 py-1.5 rounded-xl bg-amber-50 text-amber-700 hover:bg-amber-100 font-medium text-xs transition inline-flex items-center gap-1.5 shadow-sm cursor-pointer"
                      title="Chỉnh sửa hạng"
                    >
                      <span>✏️</span> Sửa
                    </button>
                    <button
                      @click="confirmToggleStatus(hang)"
                      :class="[
                        'px-3 py-1.5 rounded-xl font-medium text-xs transition inline-flex items-center gap-1.5 shadow-sm cursor-pointer',
                        hang.trangThai
                          ? 'bg-rose-50 text-rose-700 hover:bg-rose-100'
                          : 'bg-emerald-50 text-emerald-700 hover:bg-emerald-100',
                      ]"
                    >
                      <span>{{ hang.trangThai ? '🔒 Khóa' : '🔓 Kích hoạt' }}</span>
                    </button>
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>

    <!-- MODAL THÊM / SỬA HẠNG THÀNH VIÊN -->
    <div
      v-if="showModal"
      class="fixed inset-0 bg-black/50 backdrop-blur-sm flex justify-center items-center z-50 p-4"
    >
      <div
        class="bg-white rounded-3xl shadow-2xl w-full max-w-md overflow-hidden transform transition-all"
      >
        <div
          class="bg-indigo-600 px-6 py-4 text-white font-semibold text-lg flex justify-between items-center"
        >
          <span class="flex items-center gap-2">
            <span>{{ editingHang ? '✏️' : '✨' }}</span>
            {{ editingHang ? 'Cập Nhật Hạng Thành Viên' : 'Thêm Mới Hạng Thành Viên' }}
          </span>
          <button
            @click="showModal = false"
            class="text-white hover:text-gray-200 text-2xl font-bold cursor-pointer"
          >
            &times;
          </button>
        </div>
        <form @submit.prevent="handleSaveHang" class="p-6 space-y-4">
          <div>
            <label class="block text-xs font-bold text-gray-700 uppercase mb-1">Tên Hạng</label>
            <input
              type="text"
              required
              v-model="formData.tenHang"
              class="w-full border border-gray-300 rounded-xl px-3.5 py-2.5 text-sm focus:ring-2 focus:ring-indigo-500 outline-none"
              placeholder="Ví dụ: Kim Cương"
            />
          </div>
          <div>
            <label class="block text-xs font-bold text-gray-700 uppercase mb-1"
              >Ngưỡng Chi Tiêu Tối Thiểu (VNĐ)</label
            >
            <input
              type="number"
              step="any"
              min="0"
              required
              v-model.number="formData.nguongChiTieuToiThieu"
              class="w-full border border-gray-300 rounded-xl px-3.5 py-2.5 text-sm focus:ring-2 focus:ring-indigo-500 outline-none"
              placeholder="Ví dụ: 10000000"
            />
          </div>
          <div>
            <label class="block text-xs font-bold text-gray-700 uppercase mb-1"
              >Hệ Số Tỷ Lệ Tích Xu</label
            >
            <input
              type="number"
              step="0.1"
              min="0"
              required
              v-model.number="formData.tyLeTichXu"
              class="w-full border border-gray-300 rounded-xl px-3.5 py-2.5 text-sm focus:ring-2 focus:ring-indigo-500 outline-none"
              placeholder="Ví dụ: 1.5"
            />
          </div>
          <div class="flex items-center space-x-2 pt-2">
            <input
              type="checkbox"
              id="trangThai"
              v-model="formData.trangThai"
              class="w-4 h-4 text-indigo-600 rounded focus:ring-indigo-500 cursor-pointer"
            />
            <label for="trangThai" class="text-sm font-medium text-gray-700 cursor-pointer"
              >Kích hoạt hạng này</label
            >
          </div>
          <div class="flex justify-end space-x-3 pt-4 border-t border-gray-100">
            <button
              type="button"
              @click="showModal = false"
              class="px-4 py-2 bg-gray-100 text-gray-700 text-sm font-medium rounded-xl hover:bg-gray-200 transition flex items-center gap-1.5 cursor-pointer"
            >
              <span>❌</span> Hủy
            </button>
            <button
              type="submit"
              class="px-5 py-2 bg-indigo-600 text-white text-sm font-medium rounded-xl hover:bg-indigo-700 transition shadow-md flex items-center gap-1.5 cursor-pointer"
            >
              <span>💾</span> Lưu Lại
            </button>
          </div>
        </form>
      </div>
    </div>

    <!-- MODAL XÁC NHẬN CUSTOM -->
    <div
      v-if="showConfirmModal"
      class="fixed inset-0 bg-black/50 backdrop-blur-sm flex justify-center items-center z-[9998] p-4"
    >
      <div
        class="bg-white rounded-3xl shadow-2xl w-full max-w-sm overflow-hidden p-6 text-center transform transition-all"
      >
        <div
          class="w-16 h-16 bg-amber-100 text-amber-600 rounded-full flex items-center justify-center text-3xl mx-auto mb-4"
        >
          ⚠️
        </div>
        <h3 class="text-lg font-bold text-gray-800 mb-2">Xác nhận thay đổi trạng thái</h3>
        <p class="text-sm text-gray-500 mb-6">{{ confirmMessage }}</p>
        <div class="flex justify-center space-x-3">
          <button
            @click="showConfirmModal = false"
            class="flex-1 px-4 py-2.5 bg-gray-100 text-gray-700 text-sm font-medium rounded-xl hover:bg-gray-200 transition cursor-pointer"
          >
            Hủy bỏ
          </button>
          <button
            @click="executeConfirmedAction"
            class="flex-1 px-4 py-2.5 bg-indigo-600 text-white text-sm font-medium rounded-xl hover:bg-indigo-700 transition shadow-md cursor-pointer"
          >
            Xác nhận
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { cauHinhService } from '@/service/cauHinhService'

const activeTab = ref('xu')
const danhSachHang = ref([])
const loading = ref(false)

const xuForm = reactive({
  tiLeTichXu: {
    giaTriSo: 300000,
    displayValue: '300.000',
    moTa: 'Cứ 300.000đ mua hàng được 1 xu',
    error: false,
  },
  tyLeGiamToiDa: {
    giaTriSo: 50,
    displayValue: '50',
    moTa: 'Tối đa được dùng xu thanh toán 50% giá trị đơn hàng',
    error: false,
  },
  tyLeQuyDoi: {
    giaTriSo: 1000,
    displayValue: '1.000',
    moTa: '1 xu được quy đổi thành 1.000đ khi thanh toán',
    error: false,
  },
  chuKyXetHang: {
    giaTriSo: 12,
    displayValue: '12',
    moTa: 'Chu kỳ xét hạng thành viên là 12 tháng',
    error: false,
  },
})

const notification = reactive({
  type: '',
  message: '',
})

const showModal = ref(false)
const editingHang = ref(null)
const formData = reactive({
  tenHang: '',
  nguongChiTieuToiThieu: '',
  tyLeTichXu: 1.0,
  trangThai: true,
})

const showConfirmModal = ref(false)
const confirmMessage = ref('')
let pendingAction = null

const getRankIcon = (tenHang) => {
  if (!tenHang) return '⭐'
  const name = tenHang.toLowerCase()
  if (name.includes('kim cương') || name.includes('diamond')) return '💎'
  if (name.includes('vàng') || name.includes('gold')) return '👑'
  if (name.includes('bạc') || name.includes('silver')) return '🥈'
  if (name.includes('đồng') || name.includes('bronze')) return '🥉'
  if (name.includes('platinum') || name.includes('bạch kim')) return '🌟'
  return '⭐'
}

const getRankBadgeStyle = (tenHang) => {
  if (!tenHang) return 'bg-indigo-100 text-indigo-600'
  const name = tenHang.toLowerCase()
  if (name.includes('kim cương') || name.includes('diamond')) return 'bg-cyan-100 text-cyan-600'
  if (name.includes('vàng') || name.includes('gold')) return 'bg-amber-100 text-amber-600'
  if (name.includes('bạc') || name.includes('silver')) return 'bg-slate-200 text-slate-700'
  if (name.includes('đồng') || name.includes('bronze')) return 'bg-orange-100 text-orange-600'
  if (name.includes('platinum') || name.includes('bạch kim')) return 'bg-purple-100 text-purple-600'
  return 'bg-indigo-100 text-indigo-600'
}

const showNotify = (type, message) => {
  notification.type = type
  notification.message = message
  setTimeout(() => {
    notification.message = ''
  }, 3000)
}

const handleInputNumber = (e, targetObj, type) => {
  let raw = e.target.value.replace(/[^0-9]/g, '')
  if (!raw) {
    targetObj.displayValue = ''
    targetObj.giaTriSo = 0
    targetObj.error = true
    return
  }

  let num = parseInt(raw, 10)
  if (type === 'percent') {
    if (num < 0 || num > 100) {
      targetObj.error = true
    } else {
      targetObj.error = false
    }
    targetObj.giaTriSo = num
    targetObj.displayValue = num.toString()
    targetObj.moTa = `Tối đa được dùng xu thanh toán ${targetObj.displayValue}% giá trị đơn hàng`
  } else if (type === 'currency') {
    if (num <= 0) {
      targetObj.error = true
    } else {
      targetObj.error = false
    }
    targetObj.giaTriSo = num
    targetObj.displayValue = num.toLocaleString('vi-VN')
    targetObj.moTa = `1 xu được quy đổi thành ${targetObj.displayValue}đ khi thanh toán`
  } else if (type === 'month') {
    if (num <= 0) {
      targetObj.error = true
    } else {
      targetObj.error = false
    }
    targetObj.giaTriSo = num
    targetObj.displayValue = num.toString()
    targetObj.moTa = `Chu kỳ xét hạng thành viên là ${targetObj.displayValue} tháng`
  } else {
    if (num <= 0) {
      targetObj.error = true
    } else {
      targetObj.error = false
    }
    targetObj.giaTriSo = num
    targetObj.displayValue = num.toLocaleString('vi-VN')
    targetObj.moTa = `Cứ ${targetObj.displayValue}đ mua hàng được 1 xu`
  }
}

const loadData = async () => {
  loading.value = true
  try {
    const [resHeThong, resHang] = await Promise.all([
      cauHinhService.getCauHinhHeThong(),
      cauHinhService.getHangThanhVien(),
    ])

    if (resHeThong && Array.isArray(resHeThong)) {
      resHeThong.forEach((item) => {
        if (item.maCauHinh === 'TI_LE_TICH_XU') {
          xuForm.tiLeTichXu.giaTriSo = Number(item.giaTriSo) || 0
          xuForm.tiLeTichXu.displayValue = xuForm.tiLeTichXu.giaTriSo.toLocaleString('vi-VN')
          xuForm.tiLeTichXu.moTa =
            item.moTa || `Cứ ${xuForm.tiLeTichXu.displayValue}đ mua hàng được 1 xu`
        } else if (item.maCauHinh === 'TY_LE_GIAM_TOI_DA_XU') {
          xuForm.tyLeGiamToiDa.giaTriSo = Number(item.giaTriSo) || 0
          xuForm.tyLeGiamToiDa.displayValue = xuForm.tyLeGiamToiDa.giaTriSo.toString()
          xuForm.tyLeGiamToiDa.moTa =
            item.moTa ||
            `Tối đa được dùng xu thanh toán ${xuForm.tyLeGiamToiDa.displayValue}% giá trị đơn hàng`
        } else if (item.maCauHinh === 'TY_LE_QUY_DOI_XU') {
          xuForm.tyLeQuyDoi.giaTriSo = Number(item.giaTriSo) || 0
          xuForm.tyLeQuyDoi.displayValue = xuForm.tyLeQuyDoi.giaTriSo.toLocaleString('vi-VN')
          xuForm.tyLeQuyDoi.moTa =
            item.moTa || `1 xu được quy đổi thành ${xuForm.tyLeQuyDoi.displayValue}đ khi thanh toán`
        } else if (item.maCauHinh === 'CHU_KY_XET_HANG') {
          xuForm.chuKyXetHang.giaTriSo = Number(item.giaTriSo) || 0
          xuForm.chuKyXetHang.displayValue = xuForm.chuKyXetHang.giaTriSo.toString()
          xuForm.chuKyXetHang.moTa =
            item.moTa || `Chu kỳ xét hạng thành viên là ${xuForm.chuKyXetHang.displayValue} tháng`
        }
      })
    }

    if (resHang) {
      danhSachHang.value = resHang
    }
  } catch (error) {
    showNotify('error', 'Không thể tải dữ liệu từ máy chủ!')
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadData()
})

const handleUpdateConfig = async (maCauHinh, targetObj) => {
  if (
    targetObj.giaTriSo === null ||
    targetObj.giaTriSo === undefined ||
    isNaN(targetObj.giaTriSo) ||
    targetObj.giaTriSo <= 0
  ) {
    targetObj.error = true
    showNotify('error', 'Giá trị cấu hình phải lớn hơn 0!')
    return
  }

  targetObj.error = false

  try {
    await cauHinhService.updateCauHinhHeThong(maCauHinh, targetObj.giaTriSo)
    showNotify('success', 'Cập nhật cấu hình thành công!')
    loadData()
  } catch (error) {
    showNotify('error', 'Lỗi khi cập nhật cấu hình!')
  }
}

const handleOpenModal = (hang) => {
  if (hang) {
    editingHang.value = hang
    formData.tenHang = hang.tenHang
    formData.nguongChiTieuToiThieu = hang.nguongChiTieuToiThieu
    formData.tyLeTichXu = hang.tyLeTichXu
    formData.trangThai = hang.trangThai
  } else {
    editingHang.value = null
    formData.tenHang = ''
    formData.nguongChiTieuToiThieu = ''
    formData.tyLeTichXu = 1.0
    formData.trangThai = true
  }
  showModal.value = true
}

const handleSaveHang = async () => {
  if (!formData.tenHang || !formData.tenHang.trim()) {
    showNotify('error', 'Tên hạng không được để trống!')
    return
  }

  const isDuplicate = danhSachHang.value.some((item) => {
    if (editingHang.value && item.id === editingHang.value.id) return false
    return item.tenHang.trim().toLowerCase() === formData.tenHang.trim().toLowerCase()
  })

  if (isDuplicate) {
    showNotify('error', 'Tên hạng này đã tồn tại, vui lòng chọn tên khác!')
    return
  }

  if (
    formData.nguongChiTieuToiThieu === '' ||
    formData.nguongChiTieuToiThieu === null ||
    isNaN(formData.nguongChiTieuToiThieu) ||
    Number(formData.nguongChiTieuToiThieu) < 0
  ) {
    showNotify('error', 'Ngưỡng chi tiêu tối thiểu phải là số và không được âm!')
    return
  }

  if (
    formData.tyLeTichXu === '' ||
    formData.tyLeTichXu === null ||
    isNaN(formData.tyLeTichXu) ||
    Number(formData.tyLeTichXu) < 0
  ) {
    showNotify('error', 'Hệ số tích xu phải là số và không được âm!')
    return
  }

  try {
    if (editingHang.value) {
      await cauHinhService.updateHangThanhVien(editingHang.value.id, formData)
      showNotify('success', 'Cập nhật hạng thành viên thành công!')
    } else {
      await cauHinhService.createHangThanhVien(formData)
      showNotify('success', 'Thêm mới hạng thành viên thành công!')
    }
    showModal.value = false
    loadData()
  } catch (error) {
    showNotify('error', 'Có lỗi xảy ra khi lưu dữ liệu!')
  }
}

const confirmToggleStatus = (hang) => {
  const newStatus = !hang.trangThai
  const actionText = newStatus ? 'kích hoạt lại' : 'khóa (ngừng áp dụng)'

  confirmMessage.value = `Bạn có chắc chắn muốn ${actionText} hạng thành viên "${hang.tenHang}" này không?`
  pendingAction = async () => {
    try {
      await cauHinhService.updateHangThanhVien(hang.id, {
        ...hang,
        trangThai: newStatus,
      })
      showNotify('success', `Đã ${newStatus ? 'kích hoạt' : 'khóa'} hạng thành viên thành công!`)
      loadData()
    } catch (error) {
      showNotify('error', 'Không thể thay đổi trạng thái hạng thành viên!')
    }
  }
  showConfirmModal.value = true
}

const executeConfirmedAction = async () => {
  showConfirmModal.value = false
  if (pendingAction) {
    await pendingAction()
    pendingAction = null
  }
}
</script>
