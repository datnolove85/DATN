<template>
  <div class="space-y-6">
    <!-- Header trang & Chuyển tab chính -->
    <div
      class="bg-white p-6 rounded-2xl border border-slate-200 shadow-xs flex flex-col md:flex-row justify-between items-start md:items-center gap-4"
    >
      <div>
        <h1 class="text-xl font-bold text-slate-800 flex items-center gap-2">
          <i class="fa-solid fa-gamepad text-indigo-600"></i> Quản Trị Minigame & Biến Động Xu
        </h1>
        <p class="text-sm text-slate-500 mt-1">
          Cấu hình phần thưởng vòng quay, lật thẻ, điểm danh, lượt chơi và theo dõi lịch sử giao
          dịch xu toàn hệ thống.
        </p>
      </div>

      <!-- Tabs chuyển đổi chính -->
      <div class="flex bg-slate-100 p-1 rounded-xl w-full md:w-auto overflow-x-auto">
        <button
          @click="currentMainTab = 'rewards'"
          :class="
            currentMainTab === 'rewards'
              ? 'bg-white text-indigo-600 shadow-xs font-semibold'
              : 'text-slate-600 hover:text-slate-900'
          "
          class="flex-1 md:flex-none px-5 py-2 rounded-lg text-sm transition whitespace-nowrap"
        >
          Cấu Hình Phần Thưởng
        </button>
        <button
          @click="currentMainTab = 'checkin_config'"
          :class="
            currentMainTab === 'checkin_config'
              ? 'bg-white text-indigo-600 shadow-xs font-semibold'
              : 'text-slate-600 hover:text-slate-900'
          "
          class="flex-1 md:flex-none px-5 py-2 rounded-lg text-sm transition whitespace-nowrap"
        >
          Cấu Hình Điểm Danh
        </button>
        <button
          @click="currentMainTab = 'history'"
          :class="
            currentMainTab === 'history'
              ? 'bg-white text-indigo-600 shadow-xs font-semibold'
              : 'text-slate-600 hover:text-slate-900'
          "
          class="flex-1 md:flex-none px-5 py-2 rounded-lg text-sm transition whitespace-nowrap"
        >
          Lịch Sử Biến Động Xu
        </button>
      </div>
    </div>

    <!-- TAB 1: CẤU HÌNH PHẦN THƯỞNG & TÍCH HỢP CẤU HÌNH LẬT THẺ -->
    <div v-if="currentMainTab === 'rewards'" class="space-y-6">
      <div
        class="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4 bg-white p-4 rounded-2xl border border-slate-200 shadow-xs"
      >
        <div class="flex gap-2 w-full sm:w-auto">
          <button
            @click="gameSubTab = 'vong_quay'"
            :class="
              gameSubTab === 'vong_quay'
                ? 'bg-indigo-600 text-white shadow-md shadow-indigo-600/20'
                : 'bg-slate-100 text-slate-600 hover:text-slate-900'
            "
            class="flex-1 sm:flex-none px-6 py-2.5 rounded-xl text-sm font-semibold transition flex items-center justify-center gap-2"
          >
            <i class="fa-solid fa-dharmachakra"></i> Vòng Quay May Mắn
          </button>
          <button
            @click="gameSubTab = 'lat_the'"
            :class="
              gameSubTab === 'lat_the'
                ? 'bg-amber-600 text-white shadow-md shadow-amber-600/20'
                : 'bg-slate-100 text-slate-600 hover:text-slate-900'
            "
            class="flex-1 sm:flex-none px-6 py-2.5 rounded-xl text-sm font-semibold transition flex items-center justify-center gap-2"
          >
            <i class="fa-solid fa-clone"></i> Lật Thẻ Nhận Quà
          </button>
        </div>

        <button
          @click="openAddModal"
          class="w-full sm:w-auto bg-emerald-600 hover:bg-emerald-500 text-white text-sm font-medium px-5 py-2.5 rounded-xl shadow-md shadow-emerald-600/20 transition flex items-center justify-center gap-2"
        >
          <i class="fa-solid fa-plus text-xs"></i> Thêm Phần Thưởng Mới
        </button>
      </div>

      <!-- TÍCH HỢP: Quản lý lượt lật thẻ trực tiếp trong sub-tab Lật Thẻ -->
      <div v-if="gameSubTab === 'lat_the'" class="space-y-4">
        <div class="bg-white p-5 rounded-2xl border border-slate-200 shadow-xs">
          <h3 class="text-sm font-bold text-slate-800 flex items-center gap-2 mb-1">
            <i class="fa-solid fa-sliders text-amber-600"></i> Quản Lý & Tùy Chỉnh Số Lượt Lật Thẻ
          </h3>
          <p class="text-xs text-slate-500">
            Cấp phát hoặc thiết lập lại số lượt chơi lật thẻ cho khách hàng cụ thể hoặc toàn bộ hệ
            thống.
          </p>
        </div>

        <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
          <!-- Card 1: Cập nhật cho toàn bộ hệ thống -->
          <div class="bg-white p-5 rounded-2xl border border-slate-200 shadow-xs space-y-3">
            <div class="flex items-center gap-3">
              <div
                class="w-9 h-9 rounded-xl bg-amber-50 text-amber-600 flex items-center justify-center font-bold text-sm"
              >
                <i class="fa-solid fa-globe"></i>
              </div>
              <div>
                <h4 class="font-bold text-slate-800 text-xs uppercase tracking-wider">
                  Cập Nhật Toàn Bộ Hệ Thống
                </h4>
                <p class="text-xs text-slate-500">
                  Thay đổi số lượt lật thẻ cho tất cả khách hàng.
                </p>
              </div>
            </div>

            <div class="space-y-2 pt-1">
              <div>
                <label class="block text-[11px] font-semibold text-slate-500 uppercase mb-1"
                  >Số Lượt Cấp Cho Mỗi Khách Hàng</label
                >
                <input
                  v-model.number="scratchGlobalForm.soLuot"
                  type="number"
                  min="0"
                  class="w-full px-3.5 py-2 rounded-xl bg-slate-50 border border-slate-200 text-sm font-semibold text-slate-800 focus:outline-none focus:border-indigo-600"
                  placeholder="VD: 5"
                />
              </div>
              <button
                @click="handleUpdateSoLuotToanBo"
                class="w-full py-2 bg-amber-600 hover:bg-amber-500 text-white text-xs font-semibold rounded-xl shadow-md transition flex items-center justify-center gap-2"
              >
                <i class="fa-solid fa-check"></i> Áp Dụng Toàn Hệ Thống
              </button>
            </div>
          </div>

          <!-- Card 2: Cập nhật cho 1 khách hàng cụ thể -->
          <div class="bg-white p-5 rounded-2xl border border-slate-200 shadow-xs space-y-3">
            <div class="flex items-center gap-3">
              <div
                class="w-9 h-9 rounded-xl bg-indigo-50 text-indigo-600 flex items-center justify-center font-bold text-sm"
              >
                <i class="fa-solid fa-user"></i>
              </div>
              <div>
                <h4 class="font-bold text-slate-800 text-xs uppercase tracking-wider">
                  Cập Nhật Riêng Khách Hàng
                </h4>
                <p class="text-xs text-slate-500">Tùy chỉnh lượt lật thẻ theo ID khách hàng.</p>
              </div>
            </div>

            <div class="space-y-2 pt-1">
              <div class="grid grid-cols-2 gap-2">
                <div>
                  <label class="block text-[11px] font-semibold text-slate-500 uppercase mb-1"
                    >ID Khách Hàng</label
                  >
                  <input
                    v-model.number="scratchCustomerForm.idKhachHang"
                    type="number"
                    min="1"
                    class="w-full px-3 py-2 rounded-xl bg-slate-50 border border-slate-200 text-xs font-semibold text-slate-800 focus:outline-none focus:border-indigo-600"
                    placeholder="VD: 12"
                  />
                </div>
                <div>
                  <label class="block text-[11px] font-semibold text-slate-500 uppercase mb-1"
                    >Số Lượt Mới</label
                  >
                  <input
                    v-model.number="scratchCustomerForm.soLuot"
                    type="number"
                    min="0"
                    class="w-full px-3 py-2 rounded-xl bg-slate-50 border border-slate-200 text-xs font-semibold text-slate-800 focus:outline-none focus:border-indigo-600"
                    placeholder="VD: 3"
                  />
                </div>
              </div>
              <button
                @click="handleUpdateSoLuotKhachHang"
                class="w-full py-2 bg-indigo-600 hover:bg-indigo-500 text-white text-xs font-semibold rounded-xl shadow-md transition flex items-center justify-center gap-2"
              >
                <i class="fa-solid fa-user-pen"></i> Cập Nhật Cho Khách Hàng
              </button>
            </div>
          </div>
        </div>
      </div>

      <!-- Validator tỷ lệ -->
      <div
        class="bg-white border border-slate-200 p-4 rounded-2xl flex items-center justify-between gap-4 shadow-xs"
      >
        <div>
          <span class="text-xs text-slate-500 uppercase tracking-wider font-semibold">
            Tổng tỷ lệ trúng thưởng hiện tại ({{
              gameSubTab === 'vong_quay' ? 'Vòng Quay' : 'Lật Thẻ'
            }}):
          </span>
          <div
            class="text-lg font-bold"
            :class="totalPercentage === 100 ? 'text-emerald-600' : 'text-amber-600'"
          >
            {{ totalPercentage }}%
            <span class="text-xs font-normal text-slate-500">
              ({{
                totalPercentage === 100
                  ? 'Đạt chuẩn 100%'
                  : 'Cần điều chỉnh tổng tỷ lệ bằng đúng 100%'
              }})
            </span>
          </div>
        </div>
        <div class="w-48 bg-slate-100 h-3 rounded-full overflow-hidden border border-slate-200">
          <div
            class="h-full transition-all duration-500"
            :class="totalPercentage === 100 ? 'bg-emerald-500' : 'bg-amber-500'"
            :style="{ width: Math.min(totalPercentage, 100) + '%' }"
          ></div>
        </div>
      </div>

      <!-- Bảng phần thưởng & Phân trang -->
      <div class="bg-white border border-slate-200 rounded-2xl overflow-hidden shadow-xs">
        <table class="w-full text-left border-collapse">
          <thead>
            <tr
              class="bg-slate-50 border-b border-slate-200 text-xs font-semibold text-slate-500 uppercase tracking-wider"
            >
              <th class="py-4 px-6">ID</th>
              <th class="py-4 px-6">Tên Phần Thưởng</th>
              <th class="py-4 px-6">Loại Quà</th>
              <th class="py-4 px-6">Giá Trị Xu</th>
              <th class="py-4 px-6">Tỷ Lệ Trúng</th>
              <th class="py-4 px-6">Trạng Thái</th>
              <th class="py-4 px-6 text-right">Thao Tác</th>
            </tr>
          </thead>
          <tbody class="divide-y divide-slate-200 text-sm text-slate-700">
            <tr
              v-for="item in paginatedRewards"
              :key="item.id"
              class="hover:bg-slate-50/75 transition"
            >
              <td class="py-4 px-6 font-medium text-slate-400">#{{ item.id }}</td>
              <td class="py-4 px-6 font-semibold text-slate-900">{{ item.tenPhanThuong }}</td>
              <td class="py-4 px-6">
                <span
                  class="px-2.5 py-1 rounded-lg text-xs font-semibold uppercase"
                  :class="{
                    'bg-amber-50 text-amber-700 border border-amber-200':
                      item.loaiPhanThuong === 'xu',
                    'bg-purple-50 text-purple-700 border border-purple-200':
                      item.loaiPhanThuong === 'voucher',
                    'bg-slate-100 text-slate-600': item.loaiPhanThuong === 'khong_trung',
                  }"
                >
                  {{ item.loaiPhanThuong }}
                </span>
              </td>
              <td class="py-4 px-6 font-semibold text-amber-600">
                {{ item.giaTriXu > 0 ? item.giaTriXu + ' Xu' : '-' }}
              </td>
              <td class="py-4 px-6 font-bold text-indigo-600">{{ item.tyLeTrung }}%</td>
              <td class="py-4 px-6">
                <span
                  :class="
                    item.trangThai
                      ? 'bg-emerald-50 text-emerald-700 border border-emerald-200'
                      : 'bg-rose-50 text-rose-700 border border-rose-200'
                  "
                  class="px-2.5 py-0.5 rounded-full text-xs font-semibold"
                >
                  {{ item.trangThai ? 'Hoạt động' : 'Đã khóa' }}
                </span>
              </td>
              <td class="py-4 px-6 text-right space-x-1">
                <button
                  @click="editReward(item)"
                  class="text-slate-400 hover:text-indigo-600 transition p-2"
                  title="Sửa"
                >
                  <i class="fa-solid fa-pen-to-square"></i>
                </button>
              </td>
            </tr>
            <tr v-if="filteredRewards.length === 0">
              <td colspan="7" class="py-12 text-center text-slate-400 italic">
                Chưa có cấu hình phần thưởng nào cho hình thức này.
              </td>
            </tr>
          </tbody>
        </table>

        <!-- THANH PHÂN TRANG PHẦN THƯỞNG -->
        <div
          class="px-6 py-4 bg-slate-50 border-t border-slate-200 flex flex-col sm:flex-row items-center justify-between gap-4"
        >
          <div class="text-xs text-slate-500">
            Hiển thị
            <span class="font-bold text-slate-800">{{
              paginatedRewards.length > 0 ? (rewardCurrentPage - 1) * rewardPageSize + 1 : 0
            }}</span>
            đến
            <span class="font-bold text-slate-800">{{
              Math.min(rewardCurrentPage * rewardPageSize, filteredRewards.length)
            }}</span>
            trong tổng số
            <span class="font-bold text-slate-800">{{ filteredRewards.length }}</span> bản ghi
          </div>

          <div class="flex items-center gap-3">
            <select
              v-model.number="rewardPageSize"
              class="px-2.5 py-1.5 rounded-xl bg-white border border-slate-200 text-xs text-slate-700 focus:outline-none focus:border-indigo-600"
            >
              <option :value="5">5 dòng / trang</option>
              <option :value="10">10 dòng / trang</option>
              <option :value="20">20 dòng / trang</option>
              <option :value="50">50 dòng / trang</option>
            </select>

            <div class="flex items-center gap-1">
              <button
                @click="rewardCurrentPage--"
                :disabled="rewardCurrentPage === 1"
                class="px-3 py-1.5 rounded-xl border border-slate-200 bg-white text-slate-600 text-xs font-medium hover:bg-slate-50 disabled:opacity-40 disabled:cursor-not-allowed transition"
              >
                <i class="fa-solid fa-chevron-left"></i>
              </button>

              <span class="px-3 py-1.5 text-xs font-semibold text-slate-700">
                Trang {{ rewardCurrentPage }} / {{ rewardTotalPages || 1 }}
              </span>

              <button
                @click="rewardCurrentPage++"
                :disabled="rewardCurrentPage >= rewardTotalPages"
                class="px-3 py-1.5 rounded-xl border border-slate-200 bg-white text-slate-600 text-xs font-medium hover:bg-slate-50 disabled:opacity-40 disabled:cursor-not-allowed transition"
              >
                <i class="fa-solid fa-chevron-right"></i>
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- TAB 2: CẤU HÌNH ĐIỂM DANH HẰNG NGÀY -->
    <div v-if="currentMainTab === 'checkin_config'" class="space-y-6">
      <div
        class="bg-white p-6 rounded-2xl border border-slate-200 shadow-xs flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4"
      >
        <div>
          <h2 class="text-base font-bold text-slate-800 flex items-center gap-2">
            <i class="fa-solid fa-calendar-check text-indigo-600"></i> Quản lý mức thưởng điểm danh
            theo chuỗi ngày
          </h2>
          <p class="text-xs text-slate-500 mt-1">
            Thiết lập mức xu thưởng tương ứng cho từng ngày điểm danh liên tiếp. Hỗ trợ sinh tự động
            chuỗi giá trị tăng dần.
          </p>
        </div>

        <div class="flex items-center gap-2 w-full sm:w-auto">
          <button
            @click="openGenerateModal"
            class="flex-1 sm:flex-none bg-amber-600 hover:bg-amber-500 text-white text-xs font-semibold px-4 py-2.5 rounded-xl shadow-md transition flex items-center justify-center gap-2"
          >
            <i class="fa-solid fa-wand-magic-sparkles"></i> Sinh Tự Động (Cấp Số Cộng)
          </button>

          <button
            @click="addNewCheckinDay"
            class="flex-1 sm:flex-none bg-indigo-600 hover:bg-indigo-500 text-white text-xs font-semibold px-4 py-2.5 rounded-xl shadow-md transition flex items-center justify-center gap-2"
          >
            <i class="fa-solid fa-plus"></i> Thêm Mốc Ngày
          </button>
        </div>
      </div>

      <!-- Thống kê nhanh cấu hình điểm danh -->
      <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
        <div
          class="bg-white p-5 rounded-2xl border border-slate-200 shadow-xs flex items-center gap-4"
        >
          <div
            class="w-12 h-12 rounded-xl bg-indigo-50 text-indigo-600 flex items-center justify-center text-lg font-bold"
          >
            <i class="fa-solid fa-layer-group"></i>
          </div>
          <div>
            <p class="text-xs font-semibold text-slate-400 uppercase tracking-wider">
              Tổng Mốc Ngày
            </p>
            <p class="text-xl font-bold text-slate-800">{{ checkinConfigList.length }} Ngày</p>
          </div>
        </div>
        <div
          class="bg-white p-5 rounded-2xl border border-slate-200 shadow-xs flex items-center gap-4"
        >
          <div
            class="w-12 h-12 rounded-xl bg-emerald-50 text-emerald-600 flex items-center justify-center text-lg font-bold"
          >
            <i class="fa-solid fa-coins"></i>
          </div>
          <div>
            <p class="text-xs font-semibold text-slate-400 uppercase tracking-wider">
              Mức Thưởng Cao Nhất
            </p>
            <p class="text-xl font-bold text-emerald-600">{{ maxCheckinReward }} Xu</p>
          </div>
        </div>
        <div
          class="bg-white p-5 rounded-2xl border border-slate-200 shadow-xs flex items-center gap-4"
        >
          <div
            class="w-12 h-12 rounded-xl bg-amber-50 text-amber-600 flex items-center justify-center text-lg font-bold"
          >
            <i class="fa-solid fa-rotate"></i>
          </div>
          <div>
            <p class="text-xs font-semibold text-slate-400 uppercase tracking-wider">
              Cơ Chế Vòng Lặp
            </p>
            <p class="text-sm font-bold text-slate-800 mt-1">Tự động lặp chu kỳ</p>
          </div>
        </div>
      </div>

      <div class="bg-white border border-slate-200 rounded-2xl overflow-hidden shadow-xs">
        <table class="w-full text-left border-collapse">
          <thead>
            <tr
              class="bg-slate-50 border-b border-slate-200 text-xs font-semibold text-slate-500 uppercase tracking-wider"
            >
              <th class="py-4 px-6">Ngày Thứ (Streak)</th>
              <th class="py-4 px-6">Số Xu Thưởng</th>
              <th class="py-4 px-6">Trạng Thái</th>
              <th class="py-4 px-6 text-right">Thao Tác</th>
            </tr>
          </thead>
          <tbody class="divide-y divide-slate-200 text-sm text-slate-700">
            <tr
              v-for="item in checkinConfigList"
              :key="item.id"
              class="hover:bg-slate-50/75 transition"
            >
              <td class="py-4 px-6 font-semibold text-slate-900">
                <span class="px-3 py-1 bg-slate-100 rounded-lg text-indigo-600 font-bold"
                  >Ngày thứ {{ item.ngayThu }}</span
                >
              </td>
              <td class="py-4 px-6">
                <div class="flex items-center gap-2">
                  <input
                    v-model.number="item.soXuThuong"
                    type="number"
                    min="0"
                    class="w-32 px-3 py-1.5 rounded-xl bg-slate-50 border border-slate-200 text-sm font-bold text-amber-600 focus:outline-none focus:border-indigo-600"
                  />
                  <span class="text-xs font-medium text-slate-500">Xu</span>
                </div>
              </td>
              <td class="py-4 px-6">
                <label class="relative inline-flex items-center cursor-pointer">
                  <input type="checkbox" v-model="item.trangThai" class="sr-only peer" />
                  <div
                    class="w-9 h-5 bg-slate-200 peer-focus:outline-none rounded-full peer peer-checked:after:translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:left-[2px] after:bg-white after:border-slate-300 after:border after:rounded-full after:h-4 after:w-4 after:transition-all peer-checked:bg-indigo-600"
                  ></div>
                  <span class="ml-2 text-xs font-medium text-slate-600">{{
                    item.trangThai ? 'Hoạt động' : 'Đã khóa'
                  }}</span>
                </label>
              </td>
              <td class="py-4 px-6 text-right space-x-2">
                <button
                  @click="saveCheckinConfig(item)"
                  class="px-4 py-2 bg-indigo-600 hover:bg-indigo-500 text-white text-xs font-semibold rounded-xl shadow-md transition"
                >
                  Lưu thay đổi
                </button>
              </td>
            </tr>
            <tr v-if="checkinConfigList.length === 0">
              <td colspan="4" class="py-12 text-center text-slate-400 italic">
                Chưa có dữ liệu cấu hình điểm danh nào trong hệ thống.
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <!-- TAB 3: LỊCH SỬ BIẾN ĐỘNG XU -->
    <div v-if="currentMainTab === 'history'" class="space-y-6">
      <div class="bg-white p-4 rounded-2xl border border-slate-200 shadow-xs space-y-3">
        <div class="grid grid-cols-1 lg:grid-cols-12 gap-3 items-center">
          <div class="relative lg:col-span-4">
            <i
              class="fa-solid fa-magnifying-glass absolute left-3.5 top-1/2 -translate-y-1/2 text-slate-400 text-sm"
            ></i>
            <input
              v-model="historySearchKeyword"
              type="text"
              placeholder="Tìm tên KH, mã KH, SĐT..."
              class="w-full pl-10 pr-4 py-2.5 rounded-xl bg-slate-50 border border-slate-200 text-sm focus:outline-none focus:border-indigo-600"
            />
          </div>

          <div class="lg:col-span-3">
            <select
              v-model="selectedLoaiGiaoDich"
              class="w-full px-4 py-2.5 rounded-xl bg-slate-50 border border-slate-200 text-sm font-medium text-slate-700 focus:outline-none focus:border-indigo-600"
            >
              <option value="">Tất cả loại giao dịch</option>
              <option value="diem_danh">Điểm danh hàng ngày</option>
              <option value="phi_vong_quay">Phí vòng quay</option>
              <option value="trung_vong_quay">Trúng vòng quay</option>
              <option value="trung_lat_the">Trúng lật thẻ</option>
              <option value="doi_voucher">Đổi voucher</option>
            </select>
          </div>

          <div
            class="lg:col-span-5 flex items-center justify-end gap-1.5 overflow-x-auto pb-1 lg:pb-0"
          >
            <button
              @click="setDateFilter('all')"
              :class="
                dateFilterType === 'all'
                  ? 'bg-indigo-600 text-white font-medium shadow-xs'
                  : 'bg-slate-100 text-slate-600 hover:bg-slate-200'
              "
              class="px-3.5 py-2 rounded-xl text-xs transition whitespace-nowrap"
            >
              Tất cả
            </button>
            <button
              @click="setDateFilter('today')"
              :class="
                dateFilterType === 'today'
                  ? 'bg-indigo-600 text-white font-medium shadow-xs'
                  : 'bg-slate-100 text-slate-600 hover:bg-slate-200'
              "
              class="px-3.5 py-2 rounded-xl text-xs transition whitespace-nowrap"
            >
              Hôm nay
            </button>
            <button
              @click="setDateFilter('7days')"
              :class="
                dateFilterType === '7days'
                  ? 'bg-indigo-600 text-white font-medium shadow-xs'
                  : 'bg-slate-100 text-slate-600 hover:bg-slate-200'
              "
              class="px-3.5 py-2 rounded-xl text-xs transition whitespace-nowrap"
            >
              7 ngày qua
            </button>
            <button
              @click="setDateFilter('custom')"
              :class="
                dateFilterType === 'custom'
                  ? 'bg-indigo-600 text-white font-medium shadow-xs'
                  : 'bg-slate-100 text-slate-600 hover:bg-slate-200'
              "
              class="px-3.5 py-2 rounded-xl text-xs transition whitespace-nowrap"
            >
              Tùy chỉnh
            </button>
          </div>
        </div>

        <div
          v-if="dateFilterType === 'custom'"
          class="flex items-center gap-2 pt-3 border-t border-slate-100"
        >
          <span class="text-xs font-semibold text-slate-500">Từ ngày:</span>
          <input
            v-model="startDate"
            type="date"
            class="px-3 py-2 rounded-xl bg-slate-50 border border-slate-200 text-xs text-slate-700 focus:outline-none focus:border-indigo-600"
          />
          <span class="text-slate-400 text-xs">đến</span>
          <input
            v-model="endDate"
            type="date"
            class="px-3 py-2 rounded-xl bg-slate-50 border border-slate-200 text-xs text-slate-700 focus:outline-none focus:border-indigo-600"
          />
        </div>

        <div
          class="pt-2.5 border-t border-slate-100 flex justify-between items-center text-xs text-slate-500 font-medium"
        >
          <span
            >Tra cứu lịch sử biến động số dư xu chi tiết của khách hàng trên toàn hệ thống.</span
          >
          <span
            >Hiển thị <span class="font-bold text-slate-800">{{ filteredHistory.length }}</span> bản
            ghi</span
          >
        </div>
      </div>

      <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
        <div
          class="bg-white p-5 rounded-2xl border border-slate-200 shadow-xs flex items-center gap-4"
        >
          <div
            class="w-12 h-12 rounded-xl bg-indigo-50 text-indigo-600 flex items-center justify-center text-lg font-bold"
          >
            <i class="fa-solid fa-receipt"></i>
          </div>
          <div>
            <p class="text-xs font-semibold text-slate-400 uppercase tracking-wider">
              Tổng Giao Dịch
            </p>
            <p class="text-xl font-bold text-slate-800">{{ filteredHistory.length }}</p>
          </div>
        </div>

        <div
          class="bg-white p-5 rounded-2xl border border-slate-200 shadow-xs flex items-center gap-4"
        >
          <div
            class="w-12 h-12 rounded-xl bg-emerald-50 text-emerald-600 flex items-center justify-center text-lg font-bold"
          >
            <i class="fa-solid fa-circle-plus"></i>
          </div>
          <div>
            <p class="text-xs font-semibold text-slate-400 uppercase tracking-wider">
              Tổng Xu Cộng Vào
            </p>
            <p class="text-xl font-bold text-emerald-600">+{{ totalCoinsAdded }} Xu</p>
          </div>
        </div>

        <div
          class="bg-white p-5 rounded-2xl border border-slate-200 shadow-xs flex items-center gap-4"
        >
          <div
            class="w-12 h-12 rounded-xl bg-amber-50 text-amber-600 flex items-center justify-center text-lg font-bold"
          >
            <i class="fa-solid fa-coins"></i>
          </div>
          <div>
            <p class="text-xs font-semibold text-slate-400 uppercase tracking-wider">
              Số Dư Ví Trung Bình
            </p>
            <p class="text-xl font-bold text-slate-800">{{ averageBalanceAfter }} Xu</p>
          </div>
        </div>
      </div>

      <!-- Bảng Lịch sử & Phân trang -->
      <div class="bg-white border border-slate-200 rounded-2xl overflow-hidden shadow-xs">
        <table class="w-full text-left border-collapse">
          <thead>
            <tr
              class="bg-slate-50 border-b border-slate-200 text-xs font-semibold text-slate-500 uppercase tracking-wider"
            >
              <th class="py-4 px-6">ID / Thời Gian</th>
              <th class="py-4 px-6">Khách Hàng</th>
              <th class="py-4 px-6">Loại Giao Dịch</th>
              <th class="py-4 px-6">Nội Dung Mô Tả</th>
              <th class="py-4 px-6 text-center">Biến Động</th>
              <th class="py-4 px-6">Luồng Số Dư (Trước → Sau)</th>
              <th class="py-4 px-6 text-right">Thao Tác</th>
            </tr>
          </thead>
          <tbody class="divide-y divide-slate-200 text-sm text-slate-700">
            <tr v-for="h in paginatedHistory" :key="h.id" class="hover:bg-slate-50/75 transition">
              <td class="py-4 px-6">
                <div class="font-bold text-slate-800">#{{ h.id }}</div>
                <div class="text-xs text-slate-400">{{ formatDate(h.ngayTao) }}</div>
              </td>
              <td class="py-4 px-6">
                <div class="font-semibold text-slate-900 flex items-center gap-2">
                  {{ h.idKhachHang?.hoTen || 'Khách vãng lai' }}
                  <span
                    class="text-xs px-2 py-0.5 rounded-md bg-indigo-50 text-indigo-600 font-mono font-bold"
                  >
                    {{ h.idKhachHang?.maKhachHang || 'N/A' }}
                  </span>
                </div>
                <div class="text-xs text-slate-500 flex items-center gap-2 mt-0.5">
                  <span
                    ><i class="fa-solid fa-phone text-[10px]"></i>
                    {{ h.idKhachHang?.soDienThoai || '---' }}</span
                  >
                </div>
              </td>
              <td class="py-4 px-6">
                <span
                  class="px-2.5 py-1 rounded-lg text-xs font-semibold uppercase tracking-wide border"
                  :class="{
                    'bg-emerald-50 text-emerald-700 border-emerald-200':
                      h.loaiGiaoDich === 'diem_danh',
                    'bg-indigo-50 text-indigo-700 border-indigo-200':
                      h.loaiGiaoDich === 'trung_vong_quay',
                    'bg-amber-50 text-amber-700 border-amber-200':
                      h.loaiGiaoDich === 'trung_lat_the',
                    'bg-rose-50 text-rose-700 border-rose-200': h.loaiGiaoDich === 'phi_vong_quay',
                    'bg-purple-50 text-purple-700 border-purple-200':
                      h.loaiGiaoDich === 'doi_voucher',
                    'bg-slate-100 text-slate-600 border-slate-200': ![
                      'diem_danh',
                      'trung_vong_quay',
                      'trung_lat_the',
                      'phi_vong_quay',
                      'doi_voucher',
                    ].includes(h.loaiGiaoDich),
                  }"
                >
                  {{ formatLoaiGiaoDich(h.loaiGiaoDich) }}
                </span>
              </td>
              <td class="py-4 px-6 text-slate-600 max-w-xs truncate" :title="h.moTa">
                {{ h.moTa }}
              </td>
              <td
                class="py-4 px-6 text-center font-bold"
                :class="h.soXuThayDoi >= 0 ? 'text-emerald-600' : 'text-rose-600'"
              >
                {{ h.soXuThayDoi >= 0 ? '+' : '' }}{{ h.soXuThayDoi }} Xu
              </td>
              <td class="py-4 px-6">
                <div
                  class="bg-slate-50 border border-slate-200/80 px-3.5 py-2 rounded-xl flex items-center justify-between font-mono text-xs shadow-2xs w-full max-w-[220px]"
                >
                  <div>
                    <span class="text-[10px] text-slate-400 block uppercase font-sans font-medium"
                      >Trước</span
                    >
                    <span class="text-slate-600 font-semibold">{{ h.soDuTruoc }}</span>
                  </div>
                  <i class="fa-solid fa-arrow-right-long text-indigo-500 text-xs px-2"></i>
                  <div class="text-right">
                    <span class="text-[10px] text-indigo-500 block uppercase font-sans font-medium"
                      >Sau</span
                    >
                    <span class="text-slate-900 font-bold">{{ h.soDuSau }} Xu</span>
                  </div>
                </div>
              </td>
              <td class="py-4 px-6 text-right">
                <button
                  @click="viewHistoryDetail(h)"
                  class="px-3 py-1.5 rounded-xl bg-slate-100 hover:bg-indigo-50 hover:text-indigo-600 text-slate-600 text-xs font-medium transition flex items-center gap-1.5 ml-auto"
                >
                  <i class="fa-solid fa-eye text-xs"></i> Chi tiết
                </button>
              </td>
            </tr>
            <tr v-if="filteredHistory.length === 0">
              <td colspan="7" class="py-12 text-center text-slate-400 italic">
                Không tìm thấy lịch sử biến động xu nào phù hợp với bộ lọc.
              </td>
            </tr>
          </tbody>
        </table>

        <!-- THANH PHÂN TRANG -->
        <div
          class="px-6 py-4 bg-slate-50 border-t border-slate-200 flex flex-col sm:flex-row items-center justify-between gap-4"
        >
          <div class="text-xs text-slate-500">
            Hiển thị
            <span class="font-bold text-slate-800">{{
              paginatedHistory.length > 0 ? (currentPage - 1) * pageSize + 1 : 0
            }}</span>
            đến
            <span class="font-bold text-slate-800">{{
              Math.min(currentPage * pageSize, filteredHistory.length)
            }}</span>
            trong tổng số
            <span class="font-bold text-slate-800">{{ filteredHistory.length }}</span> bản ghi
          </div>

          <div class="flex items-center gap-3">
            <select
              v-model.number="pageSize"
              class="px-2.5 py-1.5 rounded-xl bg-white border border-slate-200 text-xs text-slate-700 focus:outline-none focus:border-indigo-600"
            >
              <option :value="5">5 dòng / trang</option>
              <option :value="10">10 dòng / trang</option>
              <option :value="20">20 dòng / trang</option>
              <option :value="50">50 dòng / trang</option>
            </select>

            <div class="flex items-center gap-1">
              <button
                @click="currentPage--"
                :disabled="currentPage === 1"
                class="px-3 py-1.5 rounded-xl border border-slate-200 bg-white text-slate-600 text-xs font-medium hover:bg-slate-50 disabled:opacity-40 disabled:cursor-not-allowed transition"
              >
                <i class="fa-solid fa-chevron-left"></i>
              </button>

              <span class="px-3 py-1.5 text-xs font-semibold text-slate-700">
                Trang {{ currentPage }} / {{ totalPages || 1 }}
              </span>

              <button
                @click="currentPage++"
                :disabled="currentPage >= totalPages"
                class="px-3 py-1.5 rounded-xl border border-slate-200 bg-white text-slate-600 text-xs font-medium hover:bg-slate-50 disabled:opacity-40 disabled:cursor-not-allowed transition"
              >
                <i class="fa-solid fa-chevron-right"></i>
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Modal Form Thêm/Sửa Phần Thưởng -->
    <div
      v-if="showModal"
      class="fixed inset-0 bg-slate-900/40 backdrop-blur-xs flex items-center justify-center z-50 p-4"
    >
      <div
        class="bg-white rounded-2xl shadow-xl max-w-lg w-full overflow-hidden border border-slate-200"
      >
        <div
          class="px-6 py-4 border-b border-slate-200 flex justify-between items-center bg-slate-50"
        >
          <h3 class="font-semibold text-slate-800">
            {{ isEditMode ? 'Cập Nhật Phần Thưởng' : 'Thêm Phần Thưởng Mới' }}
          </h3>
          <button @click="closeModal" class="text-slate-400 hover:text-slate-600 transition">
            <i class="fa-solid fa-xmark text-lg"></i>
          </button>
        </div>

        <form @submit.prevent="saveReward" class="p-6 space-y-4">
          <div>
            <label class="block text-xs font-semibold text-slate-500 uppercase mb-1"
              >Tên Phần Thưởng</label
            >
            <input
              v-model="rewardForm.tenPhanThuong"
              type="text"
              required
              class="w-full px-4 py-2.5 rounded-xl bg-slate-50 border border-slate-200 text-slate-800 focus:outline-none focus:border-indigo-600 text-sm"
              placeholder="VD: 50 Xu May Mắn"
            />
          </div>

          <div class="grid grid-cols-2 gap-4">
            <div>
              <label class="block text-xs font-semibold text-slate-500 uppercase mb-1"
                >Loại Game</label
              >
              <select
                v-model="rewardForm.loaiGame"
                class="w-full px-4 py-2.5 rounded-xl bg-slate-50 border border-slate-200 text-slate-800 focus:outline-none focus:border-indigo-600 text-sm"
              >
                <option value="vong_quay">Vòng Quay</option>
                <option value="lat_the">Lật Thẻ</option>
              </select>
            </div>
            <div>
              <label class="block text-xs font-semibold text-slate-500 uppercase mb-1"
                >Loại Quà</label
              >
              <select
                v-model="rewardForm.loaiPhanThuong"
                class="w-full px-4 py-2.5 rounded-xl bg-slate-50 border border-slate-200 text-slate-800 focus:outline-none focus:border-indigo-600 text-sm"
              >
                <option value="xu">Xu</option>
                <option value="voucher">Voucher</option>
                <option value="khong_trung">Không Trúng</option>
              </select>
            </div>
          </div>

          <div v-if="rewardForm.loaiPhanThuong === 'voucher'">
            <label class="block text-xs font-semibold text-slate-500 uppercase mb-1"
              >Chọn Voucher Từ Kho</label
            >
            <select
              v-model.number="rewardForm.id_voucher"
              required
              class="w-full px-4 py-2.5 rounded-xl bg-slate-50 border border-slate-200 text-slate-800 focus:outline-none focus:border-indigo-600 text-sm"
            >
              <option :value="null" disabled>-- Chọn một voucher trong kho --</option>
              <option v-for="v in voucherList" :key="v.id" :value="v.id">
                {{ v.tenVoucher || v.ten_voucher }} (Mã: {{ v.maCode || v.ma_code }} - Còn lại:
                {{ v.soLuongConLai ?? v.so_luong_con_lai }})
              </option>
            </select>
          </div>

          <div class="grid grid-cols-2 gap-4">
            <div v-if="rewardForm.loaiPhanThuong === 'xu'">
              <label class="block text-xs font-semibold text-slate-500 uppercase mb-1"
                >Giá Trị Xu</label
              >
              <input
                v-model.number="rewardForm.giaTriXu"
                type="number"
                min="0"
                class="w-full px-4 py-2.5 rounded-xl bg-slate-50 border border-slate-200 text-slate-800 focus:outline-none focus:border-indigo-600 text-sm"
                placeholder="0"
              />
            </div>
            <div :class="rewardForm.loaiPhanThuong === 'xu' ? '' : 'col-span-2'">
              <label class="block text-xs font-semibold text-slate-500 uppercase mb-1"
                >Tỷ Lệ Trúng (%)</label
              >
              <input
                v-model.number="rewardForm.tyLeTrung"
                type="number"
                step="0.01"
                min="0"
                max="100"
                required
                class="w-full px-4 py-2.5 rounded-xl bg-slate-50 border border-slate-200 text-slate-800 focus:outline-none focus:border-indigo-600 text-sm"
                placeholder="10.0"
              />
            </div>
          </div>

          <div>
            <label class="flex items-center gap-2 cursor-pointer pt-2">
              <input
                v-model="rewardForm.trangThai"
                type="checkbox"
                class="w-4 h-4 text-indigo-600 rounded border-slate-300 focus:ring-indigo-500"
              />
              <span class="text-sm font-medium text-slate-700">Kích hoạt phần thưởng này</span>
            </label>
          </div>

          <div class="flex justify-end gap-3 pt-4 border-t border-slate-200">
            <button
              type="button"
              @click="closeModal"
              class="px-5 py-2.5 rounded-xl border border-slate-200 text-sm font-medium text-slate-600 hover:bg-slate-50 transition"
            >
              Hủy
            </button>
            <button
              type="submit"
              class="px-5 py-2.5 rounded-xl bg-indigo-600 hover:bg-indigo-500 text-white text-sm font-medium shadow-md transition"
            >
              {{ isEditMode ? 'Lưu Thay Đổi' : 'Tạo Mới' }}
            </button>
          </div>
        </form>
      </div>
    </div>

    <!-- Modal Chi Tiết Giao Dịch -->
    <div
      v-if="showDetailModal"
      class="fixed inset-0 bg-slate-900/40 backdrop-blur-xs flex items-center justify-center z-50 p-4"
    >
      <div
        class="bg-white rounded-2xl shadow-xl max-w-md w-full overflow-hidden border border-slate-200"
      >
        <div
          class="px-6 py-4 border-b border-slate-200 flex justify-between items-center bg-slate-50"
        >
          <h3 class="font-semibold text-slate-800 flex items-center gap-2">
            <i class="fa-solid fa-circle-info text-indigo-600"></i> Chi Tiết Giao Dịch #{{
              selectedHistory?.id
            }}
          </h3>
          <button
            @click="showDetailModal = false"
            class="text-slate-400 hover:text-slate-600 transition"
          >
            <i class="fa-solid fa-xmark text-lg"></i>
          </button>
        </div>

        <div class="p-6 space-y-4 text-sm text-slate-700" v-if="selectedHistory">
          <div class="bg-slate-50 p-4 rounded-xl border border-slate-200 space-y-2">
            <div class="text-xs font-bold text-slate-400 uppercase tracking-wider">
              Thông tin khách hàng
            </div>
            <div class="flex justify-between">
              <span class="text-slate-500">Họ tên:</span>
              <span class="font-semibold text-slate-900">{{
                selectedHistory.idKhachHang?.hoTen
              }}</span>
            </div>
            <div class="flex justify-between">
              <span class="text-slate-500">Mã KH:</span>
              <span class="font-mono font-bold text-indigo-600">{{
                selectedHistory.idKhachHang?.maKhachHang
              }}</span>
            </div>
            <div class="flex justify-between">
              <span class="text-slate-500">Số điện thoại:</span>
              <span>{{ selectedHistory.idKhachHang?.soDienThoai }}</span>
            </div>
            <div class="flex justify-between">
              <span class="text-slate-500">Hạng thành viên:</span>
              <span class="font-medium text-purple-600">{{
                selectedHistory.idKhachHang?.hangThanhVien || 'Thường'
              }}</span>
            </div>
            <div class="flex justify-between">
              <span class="text-slate-500">Địa chỉ:</span>
              <span>{{ selectedHistory.idKhachHang?.diaChi || 'Không rõ' }}</span>
            </div>
          </div>

          <div class="space-y-2">
            <div class="flex justify-between py-1 border-b border-slate-100">
              <span class="text-slate-500">Loại giao dịch:</span>
              <span class="font-semibold text-slate-900">{{
                formatLoaiGiaoDich(selectedHistory.loaiGiaoDich)
              }}</span>
            </div>
            <div class="flex justify-between py-1 border-b border-slate-100">
              <span class="text-slate-500">Số xu thay đổi:</span>
              <span
                class="font-bold"
                :class="selectedHistory.soXuThayDoi >= 0 ? 'text-emerald-600' : 'text-rose-600'"
              >
                {{ selectedHistory.soXuThayDoi >= 0 ? '+' : ''
                }}{{ selectedHistory.soXuThayDoi }} Xu
              </span>
            </div>
            <div class="flex justify-between py-1 border-b border-slate-100">
              <span class="text-slate-500">Số dư trước:</span>
              <span class="font-mono">{{ selectedHistory.soDuTruoc }} Xu</span>
            </div>
            <div class="flex justify-between py-1 border-b border-slate-100">
              <span class="text-slate-500">Số dư sau:</span>
              <span class="font-mono font-bold text-slate-900"
                >{{ selectedHistory.soDuSau }} Xu</span
              >
            </div>
            <div class="flex justify-between py-1 border-b border-slate-100">
              <span class="text-slate-500">Thời gian tạo:</span>
              <span class="text-xs text-slate-600">{{ formatDate(selectedHistory.ngayTao) }}</span>
            </div>
          </div>

          <div>
            <span class="text-xs font-bold text-slate-400 uppercase tracking-wider block mb-1"
              >Nội dung mô tả</span
            >
            <div
              class="bg-slate-50 p-3 rounded-xl border border-slate-200 text-xs text-slate-600 italic"
            >
              "{{ selectedHistory.moTa }}"
            </div>
          </div>

          <div class="pt-4 flex justify-end">
            <button
              @click="showDetailModal = false"
              class="px-5 py-2.5 rounded-xl bg-slate-800 hover:bg-slate-700 text-white text-sm font-medium transition"
            >
              Đóng
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- Modal Sinh Tự Động Cấp Số Cộng -->
    <div
      v-if="showGenerateModal"
      class="fixed inset-0 bg-slate-900/40 backdrop-blur-xs flex items-center justify-center z-50 p-4"
    >
      <div
        class="bg-white rounded-2xl shadow-xl max-w-md w-full overflow-hidden border border-slate-200 animate-in fade-in zoom-in duration-200"
      >
        <div
          class="px-6 py-4 border-b border-slate-200 flex justify-between items-center bg-slate-50"
        >
          <h3 class="font-semibold text-slate-800 flex items-center gap-2">
            <i class="fa-solid fa-wand-magic-sparkles text-amber-600"></i> Sinh Cấu Hình Theo Cấp Số
            Cộng
          </h3>
          <button
            @click="showGenerateModal = false"
            class="text-slate-400 hover:text-slate-600 transition"
          >
            <i class="fa-solid fa-xmark text-lg"></i>
          </button>
        </div>

        <form @submit.prevent="submitGenerateCauHinh" class="p-6 space-y-4">
          <div class="bg-amber-50 border border-amber-200 rounded-xl p-3 text-xs text-amber-800">
            Hệ thống sẽ <strong>ghi đè toàn bộ</strong> cấu hình cũ và tự động tạo mới chuỗi ngày
            điểm danh theo công thức: <br />
            <code class="font-bold">Xu ngày thứ n = Giá trị đầu + (n - 1) * Công sai</code>
          </div>

          <div>
            <label class="block text-xs font-semibold text-slate-500 uppercase mb-1"
              >Tổng số ngày cần tạo (Total Days)</label
            >
            <input
              v-model.number="generateForm.totalDays"
              type="number"
              min="1"
              max="365"
              required
              class="w-full px-4 py-2.5 rounded-xl bg-slate-50 border border-slate-200 text-slate-800 focus:outline-none focus:border-indigo-600 text-sm font-semibold"
              placeholder="VD: 30"
            />
          </div>

          <div>
            <label class="block text-xs font-semibold text-slate-500 uppercase mb-1"
              >Số xu ngày đầu tiên (Initial Value)</label
            >
            <input
              v-model.number="generateForm.initialValue"
              type="number"
              min="0"
              required
              class="w-full px-4 py-2.5 rounded-xl bg-slate-50 border border-slate-200 text-slate-800 focus:outline-none focus:border-indigo-600 text-sm font-semibold"
              placeholder="VD: 10"
            />
          </div>

          <div>
            <label class="block text-xs font-semibold text-slate-500 uppercase mb-1"
              >Mức tăng mỗi ngày / Công sai (Step)</label
            >
            <input
              v-model.number="generateForm.step"
              type="number"
              required
              class="w-full px-4 py-2.5 rounded-xl bg-slate-50 border border-slate-200 text-slate-800 focus:outline-none focus:border-indigo-600 text-sm font-semibold"
              placeholder="VD: 5"
            />
          </div>

          <div class="flex justify-end gap-3 pt-4 border-t border-slate-200">
            <button
              type="button"
              @click="showGenerateModal = false"
              class="px-5 py-2.5 rounded-xl border border-slate-200 text-sm font-medium text-slate-600 hover:bg-slate-50 transition"
            >
              Hủy
            </button>
            <button
              type="submit"
              class="px-5 py-2.5 rounded-xl bg-amber-600 hover:bg-amber-500 text-white text-sm font-medium shadow-md transition"
            >
              Tiến Hành Sinh Tự Động
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed, watch, onMounted } from 'vue'
import adminService from '@/service/adminGamificationService.js'

import { ElMessage, ElMessageBox } from 'element-plus'

// Thiết lập mặc định tab chính là Cấu Hình Phần Thưởng
const currentMainTab = ref('rewards')
const gameSubTab = ref('vong_quay')

// Biến quản lý modal cấp số cộng
const showGenerateModal = ref(false)
const generateForm = reactive({
  totalDays: 30,
  initialValue: 10,
  step: 5,
})

const openGenerateModal = () => {
  showGenerateModal.value = true
}

const submitGenerateCauHinh = async () => {
  try {
    await ElMessageBox.confirm(
      `Bạn có chắc muốn làm mới và sinh tự động ${generateForm.totalDays} ngày điểm danh không?`,
      'Xác nhận sinh cấu hình',
      {
        confirmButtonText: 'Xác nhận',
        cancelButtonText: 'Hủy',
        type: 'warning',
      },
    )

    await adminService.generateCauHinhCapSoCong(generateForm)

    ElMessage.success('Sinh cấu hình điểm danh theo cấp số cộng thành công!')

    showGenerateModal.value = false
    await fetchCheckinConfig()
  } catch (error) {
    if (error === 'cancel' || error === 'close') {
      return
    }

    console.error('Lỗi sinh cấu hình:', error)
    ElMessage.error('Có lỗi xảy ra khi sinh tự động, vui lòng thử lại!')
  }
}

// Quản lý form cập nhật lượt lật thẻ
const scratchGlobalForm = reactive({
  soLuot: 5,
})

const scratchCustomerForm = reactive({
  idKhachHang: null,
  soLuot: 3,
})

const handleUpdateSoLuotToanBo = async () => {
  try {
    await ElMessageBox.confirm(
      `Bạn có chắc muốn cập nhật số lượt lật thẻ thành ${scratchGlobalForm.soLuot} cho TOÀN BỘ hệ thống?`,
      'Xác nhận cập nhật',
      {
        confirmButtonText: 'Xác nhận',
        cancelButtonText: 'Hủy',
        type: 'warning',
      },
    )

    await adminService.updateSoLuotLatTheToanBo(scratchGlobalForm.soLuot)

    ElMessage.success('Cập nhật số lượt lật thẻ cho toàn bộ hệ thống thành công!')
  } catch (error) {
    if (error !== 'cancel' && error !== 'close') {
      ElMessage.error('Cập nhật thất bại, vui lòng kiểm tra lại!')
      console.error(error)
    }
  }
}

const handleUpdateSoLuotKhachHang = async () => {
  if (!scratchCustomerForm.idKhachHang) {
    ElMessage.warning('Vui lòng nhập ID khách hàng!')
    return
  }

  try {
    await ElMessageBox.confirm(
      `Cập nhật số lượt lật thẻ thành ${scratchCustomerForm.soLuot} cho khách hàng ID #${scratchCustomerForm.idKhachHang}?`,
      'Xác nhận cập nhật',
      {
        confirmButtonText: 'Xác nhận',
        cancelButtonText: 'Hủy',
        type: 'warning',
      },
    )

    await adminService.updateSoLuotLatTheKhachHang(
      scratchCustomerForm.idKhachHang,
      scratchCustomerForm.soLuot,
    )

    ElMessage.success('Cập nhật số lượt lật thẻ cho khách hàng thành công!')
  } catch (error) {
    if (error !== 'cancel' && error !== 'close') {
      ElMessage.error('Cập nhật thất bại, vui lòng kiểm tra lại ID khách hàng!')
      console.error(error)
    }
  }
}

const rewardList = ref([])
const historyList = ref([])
const voucherList = ref([])
const checkinConfigList = ref([])

const historySearchKeyword = ref('')
const selectedLoaiGiaoDich = ref('')

const dateFilterType = ref('all')
const startDate = ref('')
const endDate = ref('')

// Phân trang cho Lịch sử biến động xu
const currentPage = ref(1)
const pageSize = ref(10)

// Phân trang cho Cấu hình phần thưởng & Đẩy phần thưởng không hoạt động xuống dưới
const rewardCurrentPage = ref(1)
const rewardPageSize = ref(10)

watch([historySearchKeyword, selectedLoaiGiaoDich, dateFilterType, startDate, endDate], () => {
  currentPage.value = 1
})

watch(gameSubTab, () => {
  rewardCurrentPage.value = 1
})

const setDateFilter = (type) => {
  dateFilterType.value = type
  if (type !== 'custom') {
    startDate.value = ''
    endDate.value = ''
  }
}

// Xử lý lọc theo loại game và tự động đẩy phần thưởng không hoạt động xuống dưới
const filteredRewards = computed(() => {
  const list = rewardList.value.filter((item) => item.loaiGame === gameSubTab.value)
  return list.slice().sort((a, b) => {
    if (a.trangThai === b.trangThai) return 0
    return a.trangThai ? -1 : 1 // item đang hoạt động (true) lên trên, không hoạt động (false) xuống dưới
  })
})

const rewardTotalPages = computed(() => {
  return Math.ceil(filteredRewards.value.length / rewardPageSize.value) || 1
})

const paginatedRewards = computed(() => {
  const start = (rewardCurrentPage.value - 1) * rewardPageSize.value
  const end = start + rewardPageSize.value
  return filteredRewards.value.slice(start, end)
})

const filteredHistory = computed(() => {
  return historyList.value.filter((h) => {
    const keyword = historySearchKeyword.value.toLowerCase().trim()
    const customer = h.idKhachHang || {}
    const hoTen = (customer.hoTen || '').toLowerCase()
    const maKhachHang = (customer.maKhachHang || '').toLowerCase()
    const soDienThoai = (customer.soDienThoai || '').toLowerCase()

    const matchKeyword =
      !keyword ||
      hoTen.includes(keyword) ||
      maKhachHang.includes(keyword) ||
      soDienThoai.includes(keyword)

    const matchType = !selectedLoaiGiaoDich.value || h.loaiGiaoDich === selectedLoaiGiaoDich.value

    let matchDate = true
    if (h.ngayTao) {
      const itemDate = new Date(h.ngayTao)
      itemDate.setHours(0, 0, 0, 0)

      if (dateFilterType.value === 'today') {
        const today = new Date()
        today.setHours(0, 0, 0, 0)
        matchDate = itemDate.getTime() === today.getTime()
      } else if (dateFilterType.value === '7days') {
        const d = new Date()
        d.setDate(d.getDate() - 7)
        d.setHours(0, 0, 0, 0)
        matchDate = itemDate >= d
      } else if (dateFilterType.value === 'custom') {
        if (startDate.value) {
          const start = new Date(startDate.value)
          start.setHours(0, 0, 0, 0)
          matchDate = matchDate && itemDate >= start
        }
        if (endDate.value) {
          const end = new Date(endDate.value)
          end.setHours(23, 59, 59, 999)
          matchDate = matchDate && itemDate <= end
        }
      }
    }

    return matchKeyword && matchType && matchDate
  })
})

const totalPages = computed(() => {
  return Math.ceil(filteredHistory.value.length / pageSize.value)
})

const paginatedHistory = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value
  const end = start + pageSize.value
  return filteredHistory.value.slice(start, end)
})

const totalCoinsAdded = computed(() => {
  return filteredHistory.value
    .filter((h) => h.soXuThayDoi > 0)
    .reduce((sum, h) => sum + (Number(h.soXuThayDoi) || 0), 0)
})

const averageBalanceAfter = computed(() => {
  if (filteredHistory.value.length === 0) return 0
  const sum = filteredHistory.value.reduce((acc, h) => acc + (Number(h.soDuSau) || 0), 0)
  return Math.round(sum / filteredHistory.value.length)
})

const maxCheckinReward = computed(() => {
  if (checkinConfigList.value.length === 0) return 0
  return Math.max(...checkinConfigList.value.map((item) => Number(item.soXuThuong) || 0))
})

const showDetailModal = ref(false)
const selectedHistory = ref(null)

const viewHistoryDetail = (item) => {
  selectedHistory.value = item
  showDetailModal.value = true
}

const formatLoaiGiaoDich = (type) => {
  const map = {
    diem_danh: 'Điểm danh',
    phi_vong_quay: 'Phí vòng quay',
    trung_vong_quay: 'Trúng vòng quay',
    trung_lat_the: 'Trúng lật thẻ',
    doi_voucher: 'Đổi voucher',
    trung_minigame: 'Trúng minigame',
  }
  return map[type] || type || 'Khác'
}

const formatDate = (dateStr) => {
  if (!dateStr) return '---'
  try {
    const date = new Date(dateStr)
    return date.toLocaleString('vi-VN')
  } catch {
    return dateStr
  }
}

const totalPercentage = computed(() => {
  return filteredRewards.value
    .filter((item) => item.trangThai)
    .reduce((sum, item) => sum + (Number(item.tyLeTrung) || 0), 0)
})

const showModal = ref(false)
const isEditMode = ref(false)
const currentId = ref(null)

const rewardForm = reactive({
  tenPhanThuong: '',
  loaiGame: 'vong_quay',
  loaiPhanThuong: 'xu',
  giaTriXu: 0,
  id_voucher: null,
  tyLeTrung: 10.0,
  trangThai: true,
})

const fetchCheckinConfig = async () => {
  try {
    const res = await adminService.getCauHinhDiemDanh()
    checkinConfigList.value = res.data
  } catch (error) {
    console.error('Lỗi tải cấu hình điểm danh:', error)
  }
}

const saveCheckinConfig = async (item) => {
  try {
    await adminService.updateCauHinhDiemDanh(item.id, {
      soXuThuong: item.soXuThuong,
      trangThai: item.trangThai,
    })

    ElMessage.success(`Cập nhật cấu hình ngày thứ ${item.ngayThu} thành công!`)
    await fetchCheckinConfig()
  } catch (error) {
    console.error('Lỗi cập nhật cấu hình:', error)
    ElMessage.error('Cập nhật thất bại, vui lòng thử lại!')
  }
}

const addNewCheckinDay = async () => {
  const nextDay = checkinConfigList.value.length + 1

  try {
    await adminService.createCauHinhDiemDanh({
      ngayThu: nextDay,
      soXuThuong: nextDay * 10,
      trangThai: true,
    })

    ElMessage.success(`Đã thêm thành công mốc Ngày thứ ${nextDay}!`)
    await fetchCheckinConfig()
  } catch (error) {
    console.error('Lỗi thêm ngày điểm danh:', error)
    ElMessage.error('Thêm ngày điểm danh thất bại. Vui lòng kiểm tra lại Backend!')
  }
}

const fetchRewards = async () => {
  try {
    const res = await adminService.getAllPhanThuong()
    rewardList.value = res.data
  } catch (error) {
    console.error('Lỗi tải phần thưởng:', error)
  }
}

const fetchHistory = async () => {
  try {
    const res = await adminService.getAllLichSuXu()
    historyList.value = (res.data || []).sort((a, b) => new Date(b.ngayTao) - new Date(a.ngayTao))
  } catch (error) {
    console.error('Lỗi tải lịch sử xu:', error)
  }
}

const fetchVouchers = async () => {
  try {
    const res = await adminService.getAllKhoVoucher()
    voucherList.value = res.data
  } catch (error) {
    console.error('Lỗi tải kho voucher:', error)
  }
}

const openAddModal = () => {
  isEditMode.value = false
  currentId.value = null
  Object.assign(rewardForm, {
    tenPhanThuong: '',
    loaiGame: gameSubTab.value,
    loaiPhanThuong: 'xu',
    giaTriXu: 0,
    id_voucher: null,
    tyLeTrung: 10.0,
    trangThai: true,
  })
  showModal.value = true
}

const editReward = (item) => {
  isEditMode.value = true
  currentId.value = item.id
  Object.assign(rewardForm, item)
  showModal.value = true
}

const closeModal = () => {
  showModal.value = false
}

const saveReward = async () => {
  try {
    if (isEditMode.value) {
      await adminService.updatePhanThuong(currentId.value, rewardForm)
      ElMessage.success('Cập nhật phần thưởng thành công!')
    } else {
      await adminService.createPhanThuong(rewardForm)
      ElMessage.success('Thêm mới phần thưởng thành công!')
    }

    closeModal()
    fetchRewards()
  } catch (error) {
    console.error(error)
    ElMessage.error('Thực thi thất bại, vui lòng kiểm tra lại!')
  }
}

onMounted(() => {
  fetchRewards()
  fetchHistory()
  fetchVouchers()
  fetchCheckinConfig()
})
</script>
