<template>
  <div
    class="space-y-4 max-w-screen-2xl mx-auto px-4 sm:px-6 lg:px-10 py-4 bg-slate-50/50 min-h-screen text-slate-800 antialiased"
  >
    <!-- HEADER BANNER ĐỒNG BỘ -->
    <div
      class="relative p-6 bg-white rounded-2xl border border-slate-200 flex flex-col md:flex-row justify-between items-start md:items-center gap-4 shadow-sm"
    >
      <div>
        <span class="text-xs tracking-wide uppercase font-semibold text-indigo-600">
          Hệ thống vận hành
        </span>
        <h1 class="text-2xl font-bold tracking-tight mt-1 text-slate-800">
          Quản lý đơn hàng Online
        </h1>
        <p class="text-sm text-slate-500 mt-1">
          Theo dõi, kiểm soát quy trình giao dịch và xử lý sự cố hàng hóa thời gian thực.
        </p>
      </div>

      <!-- ACTION BUTTON -->
      <div class="flex items-center gap-3 w-full md:w-auto justify-end">
        <button
          @click="openDefectiveModal"
          class="px-4 py-2 bg-rose-600 hover:bg-rose-500 text-white font-bold text-xs rounded-xl shadow-sm transition-all flex items-center gap-2 cursor-pointer active:scale-95 whitespace-nowrap"
        >
          <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path
              stroke-linecap="round"
              stroke-linejoin="round"
              stroke-width="2"
              d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z"
            />
          </svg>
          ⚡ Xử lý SP Lỗi / Hủy hàng loạt
        </button>
      </div>
    </div>

    <!-- QUICK STATS BAR -->
    <div class="grid grid-cols-2 lg:grid-cols-4 gap-4">
      <div
        class="bg-white p-4 rounded-2xl border border-slate-200/80 shadow-xs flex items-center justify-between"
      >
        <div>
          <p class="text-[11px] font-bold text-slate-400 uppercase">Chờ xác nhận</p>
          <p class="text-xl font-black text-amber-600 mt-1">{{ stats.choXacNhan || 0 }}</p>
        </div>
        <div
          class="w-10 h-10 rounded-xl bg-amber-50 text-amber-600 flex items-center justify-center font-bold"
        >
          ⏳
        </div>
      </div>
      <div
        class="bg-white p-4 rounded-2xl border border-slate-200/80 shadow-xs flex items-center justify-between"
      >
        <div>
          <p class="text-[11px] font-bold text-slate-400 uppercase">Đã xác nhận</p>
          <p class="text-xl font-black text-indigo-600 mt-1">{{ stats.daXacNhan || 0 }}</p>
        </div>
        <div
          class="w-10 h-10 rounded-xl bg-indigo-50 text-indigo-600 flex items-center justify-center font-bold"
        >
          ✅
        </div>
      </div>
      <div
        class="bg-white p-4 rounded-2xl border border-slate-200/80 shadow-xs flex items-center justify-between"
      >
        <div>
          <p class="text-[11px] font-bold text-slate-400 uppercase">Đang vận chuyển</p>
          <p class="text-xl font-black text-blue-600 mt-1">{{ stats.dangGiao || 0 }}</p>
        </div>
        <div
          class="w-10 h-10 rounded-xl bg-blue-50 text-blue-600 flex items-center justify-center font-bold"
        >
          🚚
        </div>
      </div>
      <div
        class="bg-white p-4 rounded-2xl border border-slate-200/80 shadow-xs flex items-center justify-between"
      >
        <div>
          <p class="text-[11px] font-bold text-slate-400 uppercase">Đơn đã hủy</p>
          <p class="text-xl font-black text-rose-600 mt-1">{{ stats.daHuy || 0 }}</p>
        </div>
        <div
          class="w-10 h-10 rounded-xl bg-rose-50 text-rose-600 flex items-center justify-center font-bold"
        >
          ❌
        </div>
      </div>
    </div>

    <!-- MAIN CARD CONTAINER -->
    <div class="bg-white rounded-3xl border border-slate-200/80 shadow-sm overflow-hidden">
      <!-- TOP STATUS TABS -->
      <div class="px-6 pt-4 pb-2 border-b border-slate-100 bg-slate-50/30">
        <div class="flex items-center gap-2 overflow-x-auto pb-1.5 scrollbar-none">
          <button
            v-for="tab in tabs"
            :key="tab.key"
            @click="changeTab(tab.key)"
            :class="[
              'px-3.5 py-2 text-xs font-bold rounded-xl whitespace-nowrap transition-all duration-200 flex items-center gap-2 cursor-pointer',
              filters.trangThai === tab.key
                ? 'bg-indigo-600 text-white shadow-md shadow-indigo-200'
                : 'bg-white text-slate-600 hover:bg-slate-100 hover:text-slate-900 border border-slate-200/80',
            ]"
          >
            <span>{{ tab.label }}</span>
          </button>
        </div>
      </div>

      <!-- FILTER BAR (GỌN GÀNG, GIẢM KHOẢNG TRẮNG) -->
      <div class="p-3.5 border-b border-slate-100 bg-white">
        <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-2.5 items-center">
          <!-- Tim kiem tu khoa -->
          <div
            class="relative flex items-center bg-slate-50 border border-slate-200 focus-within:border-indigo-500 focus-within:ring-2 focus-within:ring-indigo-500/10 rounded-xl px-3 py-1.5 transition-all"
          >
            <svg
              class="w-4 h-4 text-slate-400 mr-2 shrink-0"
              fill="none"
              stroke="currentColor"
              viewBox="0 0 24 24"
            >
              <path
                stroke-linecap="round"
                stroke-linejoin="round"
                stroke-width="2"
                d="M21 21l-6-6m2-5a7 7 0 10-14 0 7 7 0 0014 0z"
              />
            </svg>
            <input
              v-model="filters.keyword"
              @keyup.enter="fetchOrders"
              type="text"
              placeholder="Mã đơn / Tên / SĐT..."
              class="bg-transparent text-slate-800 placeholder-slate-400 text-xs font-medium outline-none w-full"
            />
          </div>

          <!-- Loc thanh toan -->
          <div class="relative">
            <select
              v-model="filters.thanhToan"
              @change="fetchOrders"
              class="w-full bg-slate-50 border border-slate-200 focus:border-indigo-500 focus:ring-2 focus:ring-indigo-500/10 rounded-xl px-3 py-1.5 text-xs font-semibold text-slate-700 outline-none cursor-pointer appearance-none"
            >
              <option value="">Tất cả thanh toán</option>
              <option value="da_thanh_toan">Đã thanh toán</option>
              <option value="chua_thanh_toan">Chưa thanh toán</option>
            </select>
          </div>

          <!-- Loc thoi gian -->
          <div class="relative">
            <select
              v-model="filters.quickDate"
              @change="fetchOrders"
              class="w-full bg-slate-50 border border-slate-200 focus:border-indigo-500 focus:ring-2 focus:ring-indigo-500/10 rounded-xl px-3 py-1.5 text-xs font-semibold text-slate-700 outline-none cursor-pointer appearance-none"
            >
              <option value="">Tất cả thời gian</option>
              <option value="today">Hôm nay</option>
              <option value="yesterday">Hôm qua</option>
              <option value="7days">7 ngày gần đây</option>
              <option value="30days">30 ngày gần đây</option>
            </select>
          </div>

          <button
            @click="resetFilters"
            class="inline-flex items-center justify-center gap-2 text-xs font-bold px-3 py-1.5 rounded-xl border border-slate-200 bg-white text-slate-700 hover:bg-slate-50 transition shadow-xs cursor-pointer"
          >
            Reset bộ lọc
          </button>
        </div>
      </div>

      <!-- TABLE AREA -->
      <div class="overflow-x-auto min-h-[400px]">
        <table class="w-full text-left border-collapse">
          <thead>
            <tr
              class="bg-slate-50/80 border-b border-slate-100 text-slate-400 uppercase text-[10px] font-extrabold tracking-wider"
            >
              <th class="py-3 px-6">Mã Đơn</th>
              <th class="py-3 px-6">Khách Hàng</th>
              <th class="py-3 px-6">Địa Chỉ</th>
              <th class="py-3 px-6">Tổng Tiền</th>
              <th class="py-3 px-6">Thanh Toán</th>
              <th class="py-3 px-6">Trạng Thái</th>
              <th class="py-3 px-6 text-center">Thao Tác</th>
            </tr>
          </thead>
          <tbody class="divide-y divide-slate-100 text-xs">
            <tr v-if="orders.length === 0">
              <td colspan="7" class="py-16 text-center text-slate-400 font-semibold">
                Không tìm thấy đơn hàng nào
              </td>
            </tr>

            <tr v-for="o in orders" :key="o.id" class="hover:bg-slate-50/80 transition-colors">
              <!-- Mã Đơn -->
              <td class="py-3.5 px-6 font-bold text-indigo-600 font-mono">#{{ o.maHoaDon }}</td>

              <!-- Khách Hàng -->
              <td class="py-3.5 px-6">
                <div class="font-bold text-slate-800 text-xs">{{ o.tenNguoiNhan }}</div>
                <div class="text-[10px] text-slate-400">{{ o.soDienThoaiNguoiNhan }}</div>
              </td>

              <!-- Địa Chỉ -->
              <td
                class="py-3.5 px-6 text-slate-600 max-w-[150px] truncate text-[11px]"
                :title="o.diaChiGiaoHang"
              >
                {{ o.diaChiGiaoHang }}
              </td>

              <!-- Tổng Tiền -->
              <td class="py-3.5 px-6 font-black text-slate-900 font-mono text-sm">
                {{ formatMoney(o.tongThanhToan) }}đ
              </td>

              <!-- Thanh Toán -->
              <td class="py-3.5 px-6">
                <div class="flex flex-col gap-1">
                  <span class="text-[10px] font-bold text-slate-500 uppercase">
                    {{ o.phuongThucThanhToan || 'N/A' }}
                  </span>
                  <span
                    class="inline-flex w-fit items-center gap-1.5 px-2 py-0.5 rounded-md text-[10px] font-bold border"
                    :class="paymentClass(o.trangThaiThanhToan)"
                  >
                    <span class="w-1 h-1 rounded-full bg-current"></span>
                    {{ getPaymentLabel(o.trangThaiThanhToan) }}
                  </span>
                </div>
              </td>

              <!-- Trạng Thái Đơn -->
              <td class="py-3.5 px-6">
                <span
                  class="inline-flex items-center px-2.5 py-1 rounded-full text-[10px] font-bold border"
                  :class="statusClass(o.trangThai)"
                >
                  {{ getStatusLabel(o.trangThai) }}
                </span>
              </td>

              <td class="py-3.5 px-6 text-center action-menu-container">
                <div class="relative inline-block text-left">
                  <button
                    @click.stop="toggleMenu(o)"
                    class="px-3 py-1 rounded-xl bg-slate-900 hover:bg-indigo-600 text-white text-xs font-bold transition-all shadow-xs inline-flex items-center gap-1 cursor-pointer"
                  >
                    Thao tác ▾
                  </button>

                  <div
                    v-if="o.open"
                    class="absolute right-0 mt-2 w-52 bg-white border border-slate-200 rounded-2xl shadow-xl z-50 py-1.5 divide-y divide-slate-100"
                  >
                    <div class="py-1">
                      <button
                        @click="openDetail(o.id)"
                        class="w-full text-left px-4 py-2 text-xs font-semibold text-slate-700 hover:bg-indigo-50 hover:text-indigo-600 flex items-center gap-2"
                      >
                        👁️ Xem chi tiết
                      </button>
                    </div>

                    <div class="py-1">
                      <button
                        v-if="canChangeStatus(o.trangThai, 'da_xac_nhan')"
                        @click="handleAction(o, 'da_xac_nhan')"
                        class="w-full text-left px-4 py-2 text-xs font-semibold text-indigo-600 hover:bg-indigo-50"
                      >
                        ✅ Xác nhận đơn
                      </button>
                      <button
                        v-if="canChangeStatus(o.trangThai, 'cho_van_chuyen')"
                        @click="handleAction(o, 'cho_van_chuyen')"
                        class="w-full text-left px-4 py-2 text-xs font-semibold text-cyan-600 hover:bg-cyan-50"
                      >
                        📦 Chuẩn bị hàng
                      </button>
                      <button
                        v-if="canChangeStatus(o.trangThai, 'dang_giao')"
                        @click="handleAction(o, 'dang_giao')"
                        class="w-full text-left px-4 py-2 text-xs font-semibold text-blue-600 hover:bg-blue-50"
                      >
                        🚚 Giao hàng
                      </button>
                      <button
                        v-if="canChangeStatus(o.trangThai, 'giao_thanh_cong')"
                        @click="handleAction(o, 'giao_thanh_cong')"
                        class="w-full text-left px-4 py-2 text-xs font-semibold text-emerald-600 hover:bg-emerald-50"
                      >
                        🎉 Giao thành công
                      </button>
                      <button
                        v-if="canChangeStatus(o.trangThai, 'giao_that_bai')"
                        @click="handleAction(o, 'giao_that_bai')"
                        class="w-full text-left px-4 py-2 text-xs font-semibold text-red-600 hover:bg-red-50"
                      >
                        ❌ Giao thất bại
                      </button>
                      <button
                        v-if="canChangeStatus(o.trangThai, 'hoan_thanh')"
                        @click="handleAction(o, 'hoan_thanh')"
                        class="w-full text-left px-4 py-2 text-xs font-semibold text-green-600 hover:bg-green-50"
                      >
                        🏁 Hoàn thành
                      </button>
                    </div>

                    <div v-if="canChangeStatus(o.trangThai, 'da_huy')" class="py-1">
                      <button
                        @click="openCancelSingleModal(o)"
                        class="w-full text-left px-4 py-2 text-xs font-semibold text-rose-600 hover:bg-rose-50"
                      >
                        ❌ Hủy đơn
                      </button>
                    </div>
                  </div>
                </div>
              </td>
            </tr>
          </tbody>
        </table>
      </div>

      <!-- FOOTER PAGINATION -->
      <div
        class="px-6 py-3.5 bg-slate-50/50 border-t border-slate-100 flex justify-between items-center"
        v-if="totalPages > 0"
      >
        <p class="text-xs text-slate-500">
          Trang <span class="font-bold text-slate-800">{{ page + 1 }}</span> / {{ totalPages }}
        </p>
        <div class="flex gap-1">
          <button
            :disabled="page === 0"
            @click="goPage(page - 1)"
            class="px-3 py-1.5 rounded-xl border bg-white text-xs font-bold disabled:opacity-40"
          >
            Trước
          </button>
          <button
            :disabled="page >= totalPages - 1"
            @click="goPage(page + 1)"
            class="px-3 py-1.5 rounded-xl border bg-white text-xs font-bold disabled:opacity-40"
          >
            Sau
          </button>
        </div>
      </div>
    </div>

    <!-- DRAWER DETAIL -->
    <OrderDetailDrawer
      v-if="showDrawer"
      :idHoaDon="selectedOrderId"
      @close="showDrawer = false"
      @reload="fetchOrders"
    />

    <!-- MODAL SP LỖI -->
    <div
      v-if="showDefectiveModal"
      class="fixed inset-0 bg-slate-900/60 backdrop-blur-xs z-50 flex items-center justify-center p-4"
    >
      <div
        class="bg-white rounded-3xl max-w-3xl w-full shadow-2xl border border-slate-100 overflow-hidden animate-in zoom-in-95 duration-150"
      >
        <div
          class="p-6 bg-gradient-to-r from-rose-950 via-slate-900 to-slate-900 text-white flex justify-between items-center"
        >
          <div>
            <h3 class="font-extrabold text-lg flex items-center gap-2">
              ⚠️ Tra Cứu SP Lỗi & Hủy Đơn Hàng Loạt
            </h3>
            <p class="text-xs text-rose-300 mt-1">
              Nhập Mã SP/Tên SP - Hệ thống tự động lọc các đơn Đã xác nhận/Chờ giao để hủy nhanh
              cùng lúc.
            </p>
          </div>
          <button
            @click="showDefectiveModal = false"
            class="text-slate-400 hover:text-white text-xl cursor-pointer"
          >
            ✕
          </button>
        </div>

        <div class="p-6 space-y-5">
          <div>
            <div class="flex justify-between items-center mb-2">
              <label class="block text-xs font-bold uppercase text-slate-500">
                1. Nhập Mã Sản Phẩm hoặc Tên Sản Phẩm bị lỗi
              </label>
              <span
                v-if="loadingDefectiveSearch"
                class="text-xs font-bold text-rose-600 flex items-center gap-1 animate-pulse"
              >
                ⏳ Đang tự động quét đơn...
              </span>
            </div>
            <div class="relative">
              <input
                v-model="defectiveProductQuery"
                type="text"
                placeholder="Ví dụ: SP00923, Áo Sơ Mi Trắng M... (Tự động tải khi bạn dừng gõ)"
                class="w-full bg-slate-50 border border-slate-200 rounded-xl px-4 py-3 text-xs font-semibold outline-none focus:border-rose-500 focus:bg-white transition-all shadow-inner"
              />
            </div>
          </div>

          <div>
            <div class="flex justify-between items-center mb-2">
              <label class="block text-xs font-bold uppercase text-slate-500">
                2. Danh sách Đơn Hàng Đang Chờ / Đã Xác Nhận Có Chứa SP này ({{
                  affectedOrders.length
                }})
              </label>
              <button
                v-if="affectedOrders.length > 0"
                @click="toggleSelectAllAffected"
                class="text-xs text-indigo-600 font-bold hover:underline cursor-pointer"
              >
                {{
                  selectedAffectedIds.length === affectedOrders.length
                    ? 'Bỏ chọn tất cả'
                    : 'Chọn tất cả 100%'
                }}
              </button>
            </div>

            <div
              class="max-h-56 overflow-y-auto border border-slate-200 rounded-2xl divide-y divide-slate-100 bg-slate-50/50"
            >
              <div
                v-if="!defectiveProductQuery.trim()"
                class="p-8 text-center text-slate-400 text-xs"
              >
                💡 Hãy nhập Mã SP hoặc Tên SP lỗi vào ô trên để hệ thống tự động lọc đơn.
              </div>
              <div
                v-else-if="loadingDefectiveSearch"
                class="p-8 text-center text-slate-400 text-xs"
              >
                Đang tìm kiếm đơn hàng phù hợp...
              </div>
              <div
                v-else-if="affectedOrders.length === 0"
                class="p-8 text-center text-slate-400 text-xs font-medium"
              >
                Sản phẩm này không nằm trong đơn hàng nào đang ở trạng thái có thể hủy.
              </div>

              <div
                v-else
                v-for="item in affectedOrders"
                :key="item.id"
                class="p-3 flex items-center justify-between hover:bg-white transition cursor-pointer"
                @click="toggleCheckOrder(item.id)"
              >
                <div class="flex items-center gap-3">
                  <input
                    type="checkbox"
                    :value="item.id"
                    v-model="selectedAffectedIds"
                    @click.stop
                    class="w-4 h-4 text-rose-600 border-slate-300 rounded cursor-pointer"
                  />
                  <div>
                    <div class="font-bold text-slate-800 text-xs">#{{ item.maHoaDon }}</div>
                    <div class="text-[11px] text-slate-500">
                      Khách: {{ item.tenNguoiNhan }} | SĐT: {{ item.soDienThoaiNguoiNhan }}
                    </div>
                  </div>
                </div>
                <div class="font-bold text-slate-900 text-xs">
                  {{ formatMoney(item.tongThanhToan) }}đ
                </div>
              </div>
            </div>
          </div>

          <div>
            <label class="block text-xs font-bold uppercase text-slate-500 mb-1"
              >3. Lý do hủy đơn gửi khách hàng</label
            >
            <textarea
              v-model="cancelReasonBulk"
              rows="2"
              class="w-full bg-slate-50 border border-slate-200 rounded-xl p-3 text-xs focus:outline-none focus:border-rose-500"
              placeholder="Ví dụ: Sản phẩm phát hiện lỗi kỹ thuật hàng loạt khi kiểm tra xuất kho..."
            ></textarea>
          </div>
        </div>

        <div class="p-4 bg-slate-50 border-t border-slate-100 flex justify-end gap-3">
          <button
            @click="showDefectiveModal = false"
            class="px-4 py-2 bg-slate-200 text-slate-700 text-xs font-bold rounded-xl cursor-pointer"
          >
            Đóng
          </button>
          <button
            @click="executeBulkCancel"
            :disabled="selectedAffectedIds.length === 0 || !cancelReasonBulk.trim()"
            class="px-5 py-2 bg-rose-600 hover:bg-rose-700 disabled:opacity-50 text-white text-xs font-bold rounded-xl cursor-pointer shadow-md"
          >
            Thực Hiện Hủy ({{ selectedAffectedIds.length }}) Đơn Hàng
          </button>
        </div>
      </div>
    </div>

    <!-- MODAL HỦY LẺ 1 ĐƠN -->
    <div
      v-if="showSingleCancelModal"
      class="fixed inset-0 bg-slate-900/50 backdrop-blur-xs z-50 flex items-center justify-center p-4"
    >
      <div
        class="bg-white rounded-3xl max-w-md w-full p-6 shadow-2xl border border-slate-100 space-y-4"
      >
        <h3 class="font-bold text-base text-slate-800">
          Xác nhận Hủy Đơn Hàng #{{ targetCancelOrder?.maHoaDon }}
        </h3>

        <div class="space-y-2">
          <button
            v-for="reason in presetReasons"
            :key="reason"
            @click="cancelReasonSingle = reason"
            class="w-full text-left px-3 py-2 text-xs rounded-xl border transition font-medium cursor-pointer"
            :class="
              cancelReasonSingle === reason
                ? 'border-rose-500 bg-rose-50 text-rose-700 font-bold'
                : 'border-slate-200 text-slate-700'
            "
          >
            {{ reason }}
          </button>
        </div>

        <textarea
          v-model="cancelReasonSingle"
          rows="2"
          placeholder="Lý do khác..."
          class="w-full bg-slate-50 border border-slate-200 rounded-xl p-3 text-xs outline-none focus:border-rose-500"
        ></textarea>

        <div class="flex justify-end gap-2 pt-2">
          <button
            @click="showSingleCancelModal = false"
            class="px-4 py-2 bg-slate-100 text-slate-600 text-xs font-bold rounded-xl cursor-pointer"
          >
            Đóng
          </button>
          <button
            @click="confirmSingleCancel"
            :disabled="!cancelReasonSingle.trim()"
            class="px-4 py-2 bg-rose-600 text-white text-xs font-bold rounded-xl cursor-pointer"
          >
            Thực hiện Hủy
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, watch, onMounted } from 'vue'
import OrderDetailDrawer from './OrderDetailDrawer.vue'
import { searchHoadonOnline, hoaDonService } from '@/service/HoaDonService'
import { donHangLoiService } from '@/service/donHangLoiService'
import { useToast } from 'vue-toastification'
import { ElMessageBox } from 'element-plus'

const toast = useToast()

const orders = ref([])
const showDrawer = ref(false)
const selectedOrderId = ref(null)
const page = ref(0)
const size = ref(10)
const totalPages = ref(0)

const stats = ref({ choXacNhan: 0, daXacNhan: 0, dangGiao: 0, daHuy: 0 })
const filters = ref({ keyword: '', trangThai: '', thanhToan: '', quickDate: '' })

// Modal SP Lỗi
const showDefectiveModal = ref(false)
const defectiveProductQuery = ref('')
const affectedOrders = ref([])
const selectedAffectedIds = ref([])
const cancelReasonBulk = ref('Sản phẩm phát hiện bị lỗi kỹ thuật hàng loạt khi soạn hàng.')
const loadingDefectiveSearch = ref(false)
let searchDebounceTimer = null

// Modal Hủy lẻ
const showSingleCancelModal = ref(false)
const targetCancelOrder = ref(null)
const cancelReasonSingle = ref('')
const presetReasons = [
  'Sản phẩm bị lỗi kỹ thuật / hỏng hóc',
  'Hết hàng trong kho',
  'Khách hàng yêu cầu hủy',
]

const tabs = [
  { key: '', label: 'Tất cả' },
  { key: 'cho_xac_nhan', label: 'Chờ xác nhận' },
  { key: 'da_xac_nhan', label: 'Đã xác nhận' },
  { key: 'cho_van_chuyen', label: 'Chờ vận chuyển' },
  { key: 'dang_giao', label: 'Đang giao' },
  { key: 'giao_thanh_cong', label: 'Giao thành công' },
  { key: 'hoan_thanh', label: 'Hoàn thành' },
  { key: 'da_huy', label: 'Đã hủy' },
]

const statusMap = {
  cho_xac_nhan: 'Chờ xác nhận',
  da_xac_nhan: 'Đã xác nhận',
  cho_van_chuyen: 'Chờ vận chuyển',
  dang_giao: 'Đang giao',
  giao_thanh_cong: 'Giao thành công',
  hoan_thanh: 'Hoàn thành',
  da_huy: 'Đã hủy',
  giao_that_bai: 'Giao thất bại',
}

const formatMoney = (val) => (val != null ? val.toLocaleString('vi-VN') : '0')
const getStatusLabel = (st) => statusMap[st] || st || '---'
const getPaymentLabel = (pm) => (pm === 'da_thanh_toan' ? 'Đã thanh toán' : 'Chưa thanh toán')

const calculateDateRange = (quickDateKey) => {
  if (!quickDateKey) return { fromDate: null, toDate: null }

  const now = new Date()

  const formatDate = (d) => {
    const year = d.getFullYear()
    const month = String(d.getMonth() + 1).padStart(2, '0')
    const day = String(d.getDate()).padStart(2, '0')
    return `${year}-${month}-${day}`
  }

  let startDate = new Date(now)
  let endDate = new Date(now)

  if (quickDateKey === 'yesterday') {
    startDate.setDate(now.getDate() - 1)
    endDate.setDate(now.getDate() - 1)
  } else if (quickDateKey === '7days') {
    startDate.setDate(now.getDate() - 7)
  } else if (quickDateKey === '30days') {
    startDate.setDate(now.getDate() - 30)
  }

  return {
    fromDate: formatDate(startDate),
    toDate: formatDate(endDate),
  }
}

const fetchOrders = async () => {
  try {
    const dateRange = calculateDateRange(filters.value.quickDate)

    const params = {
      loaiHoaDon: 'online',
      keyword: filters.value.keyword || null,
      trangThai: filters.value.trangThai || null,
      trangThaiThanhToan: filters.value.thanhToan || null,
      fromDate: dateRange.fromDate,
      toDate: dateRange.toDate,
    }

    const res = await searchHoadonOnline(params, page.value, size.value)

    const list = res?.content || []
    orders.value = list.map((item) => ({
      ...item,
      trangThai: item.trangThai?.toLowerCase(),
      trangThaiThanhToan: item.trangThaiThanhToan?.toLowerCase(),
      open: false,
    }))
    totalPages.value = res?.totalPages || 0

    stats.value = {
      choXacNhan: list.filter((i) => i.trangThai === 'cho_xac_nhan').length,
      daXacNhan: list.filter((i) => i.trangThai === 'da_xac_nhan').length,
      dangGiao: list.filter((i) => i.trangThai === 'dang_giao').length,
      daHuy: list.filter((i) => i.trangThai === 'da_huy').length,
    }
  } catch (err) {
    console.error('Lỗi fetch đơn hàng:', err)
  }
}

watch(defectiveProductQuery, (newVal) => {
  if (searchDebounceTimer) clearTimeout(searchDebounceTimer)

  if (!newVal.trim()) {
    affectedOrders.value = []
    selectedAffectedIds.value = []
    loadingDefectiveSearch.value = false
    return
  }

  loadingDefectiveSearch.value = true
  searchDebounceTimer = setTimeout(async () => {
    try {
      const res = await donHangLoiService.timKiemDonHang(newVal.trim(), 0, 100)
      const list = res?.content || []
      const cancelableStatuses = ['cho_xac_nhan', 'da_xac_nhan', 'cho_van_chuyen']
      affectedOrders.value = list.filter((o) =>
        cancelableStatuses.includes(o.trangThai?.toLowerCase()),
      )
      selectedAffectedIds.value = affectedOrders.value.map((o) => o.id)
    } catch (e) {
      console.error('Lỗi khi tra cứu sản phẩm lỗi:', e)
    } finally {
      loadingDefectiveSearch.value = false
    }
  }, 400)
})

const openDefectiveModal = () => {
  defectiveProductQuery.value = ''
  affectedOrders.value = []
  selectedAffectedIds.value = []
  showDefectiveModal.value = true
}

const toggleCheckOrder = (id) => {
  const index = selectedAffectedIds.value.indexOf(id)
  if (index > -1) {
    selectedAffectedIds.value.splice(index, 1)
  } else {
    selectedAffectedIds.value.push(id)
  }
}

const toggleSelectAllAffected = () => {
  if (selectedAffectedIds.value.length === affectedOrders.value.length) {
    selectedAffectedIds.value = []
  } else {
    selectedAffectedIds.value = affectedOrders.value.map((o) => o.id)
  }
}

const executeBulkCancel = async () => {
  if (selectedAffectedIds.value.length === 0) {
    toast.warning('Vui lòng chọn đơn hàng!')
    return
  }

  try {
    await ElMessageBox.confirm(
      `Bạn có chắc muốn hủy ${selectedAffectedIds.value.length} đơn hàng dính SP lỗi này?`,
      'Xác nhận',
      {
        confirmButtonText: 'Hủy',
        cancelButtonText: 'Đóng',
        type: 'warning',
      },
    )

    const res = await donHangLoiService.huyHangLoatLoi(
      selectedAffectedIds.value,
      cancelReasonBulk.value,
    )

    toast.success(res.message || 'Đã hủy thành công các đơn hàng chọn!')
    showDefectiveModal.value = false
    fetchOrders()
  } catch (err) {
    if (err !== 'cancel' && err !== 'close') {
      toast.error('Có lỗi xảy ra khi thực hiện hủy hàng loạt!')
    }
  }
}

const openCancelSingleModal = (order) => {
  targetCancelOrder.value = order
  cancelReasonSingle.value = presetReasons[0]
  showSingleCancelModal.value = true
  order.open = false
}

const confirmSingleCancel = async () => {
  if (!targetCancelOrder.value) return

  try {
    await donHangLoiService.huyDonLoiLe(targetCancelOrder.value.id, cancelReasonSingle.value)

    toast.success('Hủy đơn thành công!')
    showSingleCancelModal.value = false
    fetchOrders()
  } catch (err) {
    toast.error(err.response?.data?.message || 'Không thể hủy đơn!')
  }
}

const changeTab = (key) => {
  filters.value.trangThai = key
  page.value = 0
  fetchOrders()
}

const resetFilters = () => {
  filters.value = { keyword: '', trangThai: '', thanhToan: '', quickDate: '' }
  page.value = 0
  fetchOrders()
}

const canChangeStatus = (current, target) => {
  current = current?.toLowerCase()
  const map = {
    cho_xac_nhan: ['da_xac_nhan', 'da_huy'],
    da_xac_nhan: ['cho_van_chuyen', 'da_huy'],
    cho_van_chuyen: ['dang_giao', 'da_huy'],
    dang_giao: ['giao_thanh_cong', 'giao_that_bai'],
    giao_thanh_cong: ['hoan_thanh'],
  }
  return map[current]?.includes(target)
}

const handleAction = async (o, status) => {
  o.open = false
  await hoaDonService.updateTrangThai(o.id, status)
  await fetchOrders()
}

const openDetail = (id) => {
  selectedOrderId.value = id
  showDrawer.value = true
}

const toggleMenu = (o) => {
  orders.value.forEach((item) => (item.open = item.id === o.id ? !item.open : false))
}

const goPage = (p) => {
  page.value = p
  fetchOrders()
}

const statusClass = (status) => {
  switch (status) {
    case 'cho_xac_nhan':
      return 'bg-amber-50 text-amber-700 border-amber-200'
    case 'da_xac_nhan':
      return 'bg-indigo-50 text-indigo-700 border-indigo-200'
    case 'dang_giao':
      return 'bg-blue-50 text-blue-700 border-blue-200'
    case 'hoan_thanh':
      return 'bg-emerald-50 text-emerald-800 border-emerald-300'
    case 'da_huy':
      return 'bg-rose-50 text-rose-700 border-rose-200'
    default:
      return 'bg-slate-50 text-slate-600 border-slate-200'
  }
}

const paymentClass = (status) =>
  status === 'da_thanh_toan'
    ? 'bg-emerald-50 text-emerald-700 border-emerald-200'
    : 'bg-amber-50 text-amber-700 border-amber-200'

onMounted(() => {
  fetchOrders()
})

let keywordDebounceTimer = null
watch(
  () => filters.value.keyword,
  () => {
    if (keywordDebounceTimer) clearTimeout(keywordDebounceTimer)
    keywordDebounceTimer = setTimeout(() => {
      page.value = 0
      fetchOrders()
    }, 400) // Đợi 400ms sau khi ngừng gõ để gọi API
  },
)
</script>
