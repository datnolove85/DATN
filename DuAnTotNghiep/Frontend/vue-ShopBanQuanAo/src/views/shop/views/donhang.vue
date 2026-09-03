<template>
  <div class="order-page">
    <div class="page-header">
      <div class="header-title-box">
        <div class="header-icon">
          <el-icon><ShoppingBag /></el-icon>
        </div>
        <div>
          <h2>Đơn hàng của tôi</h2>
          <p>Quản lý và theo dõi trạng thái các đơn hàng của bạn</p>
        </div>
      </div>
    </div>

    <div class="tabs-wrapper">
      <el-tabs v-model="activeTab" class="custom-tabs">
        <el-tab-pane label="Tất cả" name="all" />
        <el-tab-pane label="Chờ xác nhận" name="cho_xac_nhan" />
        <el-tab-pane label="Đã xác nhận" name="da_xac_nhan" />
        <el-tab-pane label="Chờ vận chuyển" name="cho_van_chuyen" />
        <el-tab-pane label="Đang giao" name="dang_giao" />
        <el-tab-pane label="Giao thành công" name="giao_thanh_cong" />
        <el-tab-pane label="Giao thất bại" name="giao_that_bai" />
        <el-tab-pane label="Hoàn thành" name="hoan_thanh" />
        <el-tab-pane label="Đã hủy" name="da_huy" />
      </el-tabs>
    </div>

    <div v-if="loading" class="loading-box">
      <el-skeleton :rows="5" animated />
    </div>

    <el-empty v-else-if="filteredOrders.length === 0" description="Không có đơn hàng nào" />

    <div v-else class="order-list-wrapper">
      <div class="order-list">
        <div v-for="order in paginatedOrders" :key="order.thongTinDonHang.id" class="order-card">
          <div class="order-header">
            <div class="order-code-date">
              <span class="order-code">{{ order.thongTinDonHang.maHoaDon }}</span>
              <span class="dot">•</span>
              <span class="date">{{ formatDate(order.thongTinDonHang.ngayTao) }}</span>
            </div>

            <div class="status-group">
              <el-tag :type="statusType(order.thongTinDonHang.trangThai)" effect="light" round>
                {{ order.thongTinDonHang.trangThaiHienThi }}
              </el-tag>
              <el-tag
                :type="paymentType(order.thongTinDonHang.trangThaiThanhToan)"
                effect="plain"
                round
              >
                {{ order.thongTinDonHang.trangThaiThanhToanHienThi }}
              </el-tag>
            </div>
          </div>

          <div class="product-list-container">
            <div class="select-all-bar">
              <el-checkbox
                :model-value="isAllSelected(order)"
                :indeterminate="isIndeterminate(order)"
                @change="(val) => toggleSelectAll(val, order)"
              >
                <span class="select-all-label"
                  >Chọn tất cả sản phẩm ({{ order.sanPham.length }})</span
                >
              </el-checkbox>
            </div>

            <el-checkbox-group v-model="selectedProductsMap[order.thongTinDonHang.id]">
              <div class="product-item" v-for="sp in order.sanPham" :key="sp.idHoaDonChiTiet">
                <el-checkbox :value="sp.idSanPham" class="product-checkbox" />

                <!-- Dòng 69 trong file donhang.vue -->
                <img
                  :src="imageUrl(sp.anh)"
                  :alt="sp.tenSanPham"
                  class="product-image cursor-pointer hover:opacity-80 transition"
                  @click="goToProductDetail(sp)"
                  @error="$event.target.src = 'https://placehold.co/80x80?text=No+Image'"
                />

                <div class="product-info">
                  <h4>{{ sp.tenSanPham }}</h4>
                  <div class="product-meta">
                    <span>Mã SP: {{ sp.maSanPham }}</span>
                    <span v-if="sp.maSPCT">Mã SPCT: {{ sp.maSPCT }}</span>
                    <span>Màu: {{ sp.mauSac }}</span>
                    <span>Size: {{ sp.kichThuoc }}</span>
                  </div>
                </div>

                <div class="product-price">
                  <div class="price-row">
                    <span class="unit-price">
                      {{ money(sp.donGia) }}
                    </span>
                    <span class="quantity"> × {{ sp.soLuong }} </span>
                  </div>
                  <strong class="total-price">
                    {{ money(sp.thanhTien) }}
                  </strong>
                </div>
              </div>
            </el-checkbox-group>
          </div>

          <div class="timeline-container" v-if="order.thongTinDonHang.trangThai !== 'da_huy'">
            <el-steps
              :active="getStep(order)"
              :status="order.thongTinDonHang.trangThai === 'giao_that_bai' ? 'error' : 'finish'"
              finish-status="success"
              align-center
            >
              <el-step title="Chờ xác nhận" />
              <el-step title="Đã xác nhận" />
              <el-step title="Chuẩn bị hàng" />
              <el-step title="Đang giao" />
              <el-step
                :title="
                  order.thongTinDonHang.trangThai === 'giao_that_bai'
                    ? 'Giao thất bại'
                    : 'Giao thành công'
                "
              />
              <el-step title="Hoàn thành" />
            </el-steps>
          </div>

          <div class="cancel-banner" v-else>
            <el-alert title="Đơn hàng này đã bị hủy" type="error" :closable="false" show-icon />
          </div>

          <div class="return-box" v-if="order.traHang && order.traHang.coTraHang">
            <el-alert type="warning" show-icon :closable="false">
              <template #title>Đơn hàng đang có yêu cầu trả hàng</template>
              <p>
                <b>Mã:</b> {{ order.traHang.maTraHang }} | <b>Lý do:</b> {{ order.traHang.lyDo }}
              </p>
            </el-alert>
          </div>

          <div class="order-footer">
            <div class="order-summary-mini">
              <span>Tổng số tiền ({{ order.sanPham.length }} sản phẩm): </span>
              <strong class="highlight-total">{{
                money(order.thongTinDonHang.tongThanhToan)
              }}</strong>
            </div>

            <div class="action-buttons">
              <el-button plain @click="openDetail(order)">Xem chi tiết</el-button>
              <el-button
                v-if="canPayOrder(order)"
                class="pay-btn"
                :icon="CreditCard"
                @click="handlePayOrder(order)"
              >
                Thanh toán
              </el-button>
              <el-button
                type="danger"
                plain
                v-if="
                  order.thongTinDonHang.trangThai === 'cho_xac_nhan' ||
                  order.thongTinDonHang.trangThai === 'da_xac_nhan'
                "
                @click="openCancelDialog(order)"
              >
                Hủy đơn
              </el-button>

              <el-button
                type="success"
                v-if="order.thongTinDonHang.trangThai === 'giao_thanh_cong'"
                @click="confirmReceived(order)"
              >
                Đã nhận được hàng
              </el-button>

              <el-button type="warning" @click="handleRebuy(order)"> Mua lại </el-button>
            </div>
          </div>
        </div>
      </div>

      <div class="pagination-wrapper" v-if="filteredOrders.length > 0">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[5, 10, 15, 20]"
          background
          layout="total, sizes, prev, pager, next, jumper"
          :total="filteredOrders.length"
        />
      </div>
    </div>

    <el-backtop :right="30" :bottom="30">
      <el-icon><Top /></el-icon>
    </el-backtop>

    <el-dialog
      v-model="dialogVisible"
      width="720px"
      title="Chi tiết đơn hàng"
      destroy-on-close
      class="custom-dialog"
      align-center
    >
      <div v-if="selectedOrder" class="dialog-content">
        <div class="detail-banner">
          <div class="banner-left">
            <span class="label">Mã hóa đơn:</span>
            <span class="value code">{{ selectedOrder.thongTinDonHang.maHoaDon }}</span>
          </div>
          <div class="banner-right">
            <el-tag :type="statusType(selectedOrder.thongTinDonHang.trangThai)" effect="dark" round>
              {{ selectedOrder.thongTinDonHang.trangThaiHienThi }}
            </el-tag>

            <el-tag
              v-if="selectedOrder.thongTinDonHang.trangThai !== 'da_huy'"
              :type="paymentType(selectedOrder.thongTinDonHang.trangThaiThanhToan)"
              effect="plain"
              round
            >
              {{ selectedOrder.thongTinDonHang.trangThaiThanhToanHienThi }}
            </el-tag>
          </div>
        </div>

        <div class="detail-grid">
          <div class="detail-section">
            <div class="section-title">
              <el-icon><Location /></el-icon>
              <h4>Địa chỉ nhận hàng</h4>
            </div>
            <div class="receiver-info" v-if="selectedOrder.nguoiNhan">
              <p class="receiver-name">
                <b>{{ selectedOrder.nguoiNhan.tenNguoiNhan }}</b>
              </p>
              <p class="receiver-phone">
                <el-icon><Phone /></el-icon> {{ selectedOrder.nguoiNhan.soDienThoai }}
              </p>
              <p class="receiver-address">{{ selectedOrder.nguoiNhan.diaChi }}</p>
            </div>
          </div>

          <div class="detail-section">
            <div class="section-title">
              <el-icon><InfoFilled /></el-icon>
              <h4>Thông tin chung</h4>
            </div>
            <div class="info-list">
              <div class="info-row">
                <span>Loại đơn:</span>
                <b>
                  {{
                    selectedOrder.thongTinDonHang.loaiHoaDon
                      ? selectedOrder.thongTinDonHang.loaiHoaDon.charAt(0).toUpperCase() +
                        selectedOrder.thongTinDonHang.loaiHoaDon.slice(1)
                      : 'Online'
                  }}
                </b>
              </div>
              <div class="info-row">
                <span>Ngày tạo:</span>
                <span>{{ formatDate(selectedOrder.thongTinDonHang.ngayTao) }}</span>
              </div>
              <div class="info-row">
                <span>Trạng thái TT:</span>
                <span>{{ selectedOrder.thongTinDonHang.trangThaiThanhToanHienThi }}</span>
              </div>
            </div>
          </div>
        </div>

        <div class="detail-section">
          <div class="section-title">
            <el-icon><CreditCard /></el-icon>
            <h4>Thông tin thanh toán chi tiết</h4>
          </div>

          <div class="info-list" v-if="selectedOrder.thanhToan">
            <div class="info-row">
              <span>Phương thức thanh toán:</span>
              <b>{{ selectedOrder.thanhToan.phuongThucThanhToan || 'Chưa cập nhật' }}</b>
            </div>
            <div class="info-row">
              <span>Trạng thái giao dịch:</span>
              <el-tag
                size="small"
                :type="
                  selectedOrder.thongTinDonHang.trangThai === 'da_huy'
                    ? 'danger'
                    : selectedOrder.thanhToan.trangThai === 'da_thanh_toan'
                      ? 'success'
                      : 'warning'
                "
              >
                {{
                  selectedOrder.thongTinDonHang.trangThai === 'da_huy'
                    ? 'Đã hủy'
                    : formatPaymentStatus(selectedOrder.thanhToan.trangThai)
                }}
              </el-tag>
            </div>

            <div class="info-row" v-if="selectedOrder.thongTinDonHang.trangThai === 'da_huy'">
              <span>Thời gian hủy:</span>
              <span>{{
                formatDate(
                  selectedOrder.thongTinDonHang.ngayHuy ||
                    selectedOrder.thongTinDonHang.ngaySua ||
                    selectedOrder.thongTinDonHang.ngayCapNhat,
                )
              }}</span>
            </div>
            <div
              class="info-row"
              v-if="
                selectedOrder.thongTinDonHang.trangThai === 'da_huy' &&
                selectedOrder.thongTinDonHang.ghiChu
              "
            >
              <span>Lý do hủy:</span>
              <strong class="text-danger">{{ selectedOrder.thongTinDonHang.ghiChu }}</strong>
            </div>

            <div
              class="info-row"
              v-else-if="
                selectedOrder.thanhToan.trangThai === 'da_thanh_toan' &&
                selectedOrder.thanhToan.ngayThanhToan
              "
            >
              <span>Thời gian thanh toán:</span>
              <span>{{ formatDate(selectedOrder.thanhToan.ngayThanhToan) }}</span>
            </div>

            <div class="info-row" v-if="selectedOrder.thanhToan.maGiaoDich">
              <span>Mã giao dịch:</span>
              <span class="code-text">{{ selectedOrder.thanhToan.maGiaoDich }}</span>
            </div>
            <div class="info-row">
              <span>Số tiền thanh toán:</span>
              <strong class="text-danger">{{ money(selectedOrder.thanhToan.soTien) }}</strong>
            </div>
          </div>

          <div class="info-list" v-else>
            <div class="info-row">
              <span>Trạng thái thanh toán:</span>
              <el-tag
                size="small"
                :type="
                  selectedOrder.thongTinDonHang.trangThai === 'da_huy'
                    ? 'danger'
                    : paymentType(selectedOrder.thongTinDonHang.trangThaiThanhToan)
                "
              >
                {{
                  selectedOrder.thongTinDonHang.trangThai === 'da_huy'
                    ? 'Đã hủy'
                    : selectedOrder.thongTinDonHang.trangThaiThanhToanHienThi
                }}
              </el-tag>
            </div>

            <div class="info-row" v-if="selectedOrder.thongTinDonHang.trangThai === 'da_huy'">
              <span>Thời gian hủy:</span>
              <span>{{
                formatDate(
                  selectedOrder.thongTinDonHang.ngayHuy ||
                    selectedOrder.thongTinDonHang.ngaySua ||
                    selectedOrder.thongTinDonHang.ngayCapNhat,
                )
              }}</span>
            </div>
            <div
              class="info-row"
              v-if="
                selectedOrder.thongTinDonHang.trangThai === 'da_huy' &&
                selectedOrder.thongTinDonHang.ghiChu
              "
            >
              <span>Lý do hủy:</span>
              <strong class="text-danger">{{ selectedOrder.thongTinDonHang.ghiChu }}</strong>
            </div>

            <div class="info-row" v-if="selectedOrder.thongTinDonHang.trangThai !== 'da_huy'">
              <span>Hình thức thanh toán:</span>
              <b>{{
                selectedOrder.thongTinDonHang.loaiHoaDon === 'online'
                  ? 'Thanh toán trực tuyến (Chưa thanh toán)'
                  : 'Thanh toán khi nhận hàng (COD)'
              }}</b>
            </div>
            <div class="info-row">
              <span>Tổng tiền cần thanh toán:</span>
              <strong class="text-danger">{{
                money(selectedOrder.thongTinDonHang.tongThanhToan)
              }}</strong>
            </div>
          </div>
        </div>

        <div
          class="detail-section return-detail-section"
          v-if="selectedOrder.traHang && selectedOrder.traHang.coTraHang"
        >
          <div class="section-title warning-title">
            <el-icon><Warning /></el-icon>
            <h4>Thông tin trả hàng</h4>
          </div>
          <div class="info-list">
            <div class="info-row">
              <span>Mã trả hàng:</span>
              <b>{{ selectedOrder.traHang.maTraHang }}</b>
            </div>
            <div class="info-row">
              <span>Lý do trả hàng:</span>
              <span class="text-danger">{{ selectedOrder.traHang.lyDo }}</span>
            </div>
          </div>
        </div>

        <div class="detail-section">
          <div class="section-title">
            <el-icon><Goods /></el-icon>
            <h4>Sản phẩm đã mua ({{ selectedOrder.sanPham?.length || 0 }})</h4>
          </div>
          <div class="dialog-product-list">
            <div
              v-for="sp in selectedOrder.sanPham"
              :key="sp.idHoaDonChiTiet"
              class="dialog-product-item"
            >
              <img
                :src="imageUrl(sp.anh)"
                :alt="sp.tenSanPham"
                class="dialog-image"
                @error="$event.target.src = 'https://placehold.co/80x80?text=No+Image'"
              />

              <div class="dialog-info">
                <h5>{{ sp.tenSanPham }}</h5>
                <div class="dialog-meta">
                  <span class="tag-badge">Mã SP: {{ sp.maSanPham }}</span>
                  <span class="tag-badge" v-if="sp.maSPCT">Mã SPCT: {{ sp.maSPCT }}</span>
                  <span class="tag-badge">Màu: {{ sp.mauSac }}</span>
                  <span class="tag-badge">Size: {{ sp.kichThuoc }}</span>
                </div>
              </div>

              <div class="dialog-price">
                <span class="calc">{{ money(sp.donGia) }} x {{ sp.soLuong }}</span>
                <strong class="subtotal">{{ money(sp.thanhTien) }}</strong>
              </div>
            </div>
          </div>
        </div>

        <div class="detail-section payment-summary-box">
          <div class="row">
            <span>Tiền hàng</span>
            <span>{{ money(selectedOrder.thongTinDonHang.tongTienHang) }}</span>
          </div>

          <div class="row" v-if="selectedOrder.voucher">
            <span
              >Giảm giá voucher ({{ selectedOrder.voucher.maCode }} -
              {{ selectedOrder.voucher.tenVoucher }})</span
            >
            <span class="discount">-{{ money(selectedOrder.voucher.soTienGiam) }}</span>
          </div>

          <div class="row" v-if="selectedOrder.thongTinDonHang.tienGiamDoXu > 0">
            <span>Giảm do xu ({{ selectedOrder.thongTinDonHang.soXuSuDung || 0 }} xu)</span>
            <span class="discount">-{{ money(selectedOrder.thongTinDonHang.tienGiamDoXu) }}</span>
          </div>

          <div class="row">
            <span>Phí vận chuyển</span>
            <span>{{ money(selectedOrder.thongTinDonHang.phiVanChuyen) }}</span>
          </div>
          <el-divider style="margin: 8px 0" />
          <div class="row total-row">
            <span>Tổng thanh toán</span>
            <span class="highlight-total">{{
              money(selectedOrder.thongTinDonHang.tongThanhToan)
            }}</span>
          </div>
        </div>
      </div>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">Đóng</el-button>
        </span>
      </template>
    </el-dialog>

    <el-dialog
      v-model="cancelDialogVisible"
      title="Xác nhận hủy đơn hàng"
      width="500px"
      destroy-on-close
      align-center
    >
      <div v-if="orderToCancel" class="cancel-dialog-content">
        <p class="cancel-subtitle">
          Vui lòng chọn lý do bạn muốn hủy đơn hàng
          <b>{{ orderToCancel.thongTinDonHang.maHoaDon }}</b
          >:
        </p>

        <el-radio-group v-model="selectedCancelReason" class="cancel-reasons-list">
          <el-radio
            label="Thay đổi địa chỉ nhận hàng"
            value="Thay đổi địa chỉ nhận hàng"
            size="large"
          >
            Thay đổi địa chỉ nhận hàng
          </el-radio>
          <el-radio
            label="Muốn thay đổi sản phẩm (màu sắc, size, số lượng)"
            value="Muốn thay đổi sản phẩm (màu sắc, size, số lượng)"
            size="large"
          >
            Muốn thay đổi sản phẩm (màu sắc, size, số lượng)
          </el-radio>
          <el-radio
            label="Tìm thấy sản phẩm giá tốt hơn"
            value="Tìm thấy sản phẩm giá tốt hơn"
            size="large"
          >
            Tìm thấy sản phẩm giá tốt hơn ở nơi khác
          </el-radio>
          <el-radio
            label="Đặt trùng / đặt nhầm đơn hàng"
            value="Đặt trùng / đặt nhầm đơn hàng"
            size="large"
          >
            Đặt trùng / đặt nhầm đơn hàng
          </el-radio>
          <el-radio
            label="Thủ tục thanh toán quá rắc rối"
            value="Thủ tục thanh toán quá rắc rối"
            size="large"
          >
            Thủ tục thanh toán quá rắc rối
          </el-radio>
          <el-radio label="Khác" value="Khác" size="large"> Lý do khác... </el-radio>
        </el-radio-group>

        <div v-if="selectedCancelReason === 'Khác'" class="custom-reason-input">
          <el-input
            v-model="customCancelReason"
            type="textarea"
            :rows="3"
            placeholder="Mời bạn nhập lý do chi tiết..."
            maxlength="200"
            show-word-limit
          />
        </div>
      </div>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="cancelDialogVisible = false">Quay lại</el-button>
          <el-button type="danger" :loading="canceling" @click="handleHuyDonHang">
            Xác nhận hủy
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, watch, onMounted, onUnmounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useRouter } from 'vue-router'
import { Client } from '@stomp/stompjs'
import {
  Refresh,
  ShoppingBag,
  Location,
  Phone,
  InfoFilled,
  Warning,
  CreditCard,
  Goods,
  Top,
} from '@element-plus/icons-vue'
import donHangService from '@/service/DonHangService'

const router = useRouter()

const toast = {
  success: (msg) => ElMessage.success(msg),
  info: (msg) => ElMessage.info(msg),
  warning: (msg) => ElMessage.warning(msg),
  error: (msg) => ElMessage.error(msg),
}

const selectedProductsMap = ref({})
const loading = ref(false)
const activeTab = ref('all')
const orders = ref([])

// Trạng thái phân trang
const currentPage = ref(1)
const pageSize = ref(5)

const dialogVisible = ref(false)
const selectedOrder = ref(null)

const cancelDialogVisible = ref(false)
const orderToCancel = ref(null)
const selectedCancelReason = ref('Thay đổi địa chỉ nhận hàng')
const customCancelReason = ref('')
const canceling = ref(false)

// Tự động chuyển về trang 1 khi đổi tab
watch(activeTab, () => {
  currentPage.value = 1
})

const imageUrl = (path) => {
  if (!path) return 'https://placehold.co/80x80?text=No+Image'
  if (path.startsWith('http')) return path
  return `http://localhost:8080${path}`
}

const stompClient = new Client({
  brokerURL: 'ws://localhost:8080/ws',
  reconnectDelay: 5000,
  onStompError: (frame) => {
    console.error('🔴 Lỗi kết nối WebSocket:', frame.headers['message'])
  },
})

let socketSubscription = null

const subscribePos = () => {
  if (socketSubscription) {
    socketSubscription.unsubscribe()
  }

  socketSubscription = stompClient.subscribe('/topic/orders', async (msg) => {
    try {
      await loadOrders()
      if (msg && msg.body) {
        let data = {}
        let isJson = false

        try {
          data = JSON.parse(msg.body)
          isJson = true
        } catch (e) {}

        const eventType = isJson ? data.type || data.eventType || data.action : ''

        if (eventType === 'CANCEL_SUCCESS') {
          return
        }

        if (eventType === 'CANCEL_REJECTED') {
          cancelDialogVisible.value = false
          canceling.value = false
          ElMessageBox.alert(
            data.message || 'Đơn hàng của bạn đã chuyển trạng thái, không thể hủy!',
            'Thông báo đơn hàng',
            { confirmButtonText: 'Đã hiểu', type: 'warning' },
          )
          return
        }

        if (eventType === 'INVOICE_PAID') {
          toast.success('Thanh toán thành công!')
        } else {
          const noiDungThongBao = isJson
            ? data.message || data.noiDung || data.content || msg.body
            : msg.body

          if (noiDungThongBao) {
            toast.info(noiDungThongBao)
          }
        }
      }
    } catch (error) {
      console.error('Lỗi cập nhật dữ liệu từ Socket:', error)
    }
  })
}

const connectSocket = () => {
  if (stompClient.connected) {
    subscribePos()
  } else {
    stompClient.onConnect = () => {
      subscribePos()
    }
    stompClient.activate()
  }
}

async function loadOrders() {
  loading.value = true
  try {
    const data = await donHangService.layDanhSachDonHang()
    orders.value = data

    data.forEach((order) => {
      const orderId = order.thongTinDonHang.id
      selectedProductsMap.value[orderId] = []
    })
  } catch (e) {
    console.error(e)
    toast.error(e.message || 'Không lấy được danh sách đơn hàng')
  } finally {
    loading.value = false
  }
}

function isAllSelected(order) {
  const orderId = order.thongTinDonHang.id
  const selected = selectedProductsMap.value[orderId] || []
  return selected.length > 0 && selected.length === order.sanPham.length
}

function isIndeterminate(order) {
  const orderId = order.thongTinDonHang.id
  const selected = selectedProductsMap.value[orderId] || []
  return selected.length > 0 && selected.length < order.sanPham.length
}

function toggleSelectAll(val, order) {
  const orderId = order.thongTinDonHang.id
  if (val) {
    selectedProductsMap.value[orderId] = order.sanPham.map((sp) => sp.idSanPham)
  } else {
    selectedProductsMap.value[orderId] = []
  }
}

const handleRebuy = (order) => {
  const orderId = order.thongTinDonHang.id
  const selectedIds = selectedProductsMap.value[orderId] || []

  if (selectedIds.length === 0) {
    toast.warning('Vui lòng chọn ít nhất một sản phẩm để mua lại!')
    return
  }

  const selectedItems = order.sanPham.filter((sp) => selectedIds.includes(sp.idSanPham))

  const checkoutData = {
    items: selectedItems.map((sp) => {
      const gia = Number(sp.giaBan ?? sp.donGia ?? 0)
      const qty = Number(sp.soLuong ?? 1)

      return {
        productDetailId: sp.idChiTietSanPham || sp.idSanPhamChiTiet || sp.idSanPham,
        quantity: qty,
        tenSanPham: sp.tenSanPham,
        maSanPhamChiTiet: sp.maSanPhamChiTiet || sp.maSPCT || sp.maSanPham || '',
        giaBan: gia,
        mauSac: sp.mauSac || '',
        kichCo: sp.kichCo || sp.kichThuoc || '',
        anh: sp.anh || '',
        soLuongTon: sp.soLuongTon ?? 99,
        soLuongKhaDung: sp.soLuongKhaDung ?? 1,
        thanhTien: gia * qty,
      }
    }),
  }

  sessionStorage.setItem('checkoutData', JSON.stringify(checkoutData))
  router.push('/xacnhan')
}

const canPayOrder = (order) => {
  const info = order?.thongTinDonHang
  if (!info) return false

  if (info.trangThai !== 'cho_xac_nhan') {
    return false
  }

  if (info.trangThaiThanhToan === 'da_thanh_toan') {
    return false
  }

  const thanhToan = order?.thanhToan
  if (thanhToan) {
    const phuongThuc = String(thanhToan.phuongThucThanhToan || '').toLowerCase()

    if (
      phuongThuc.includes('cod') ||
      phuongThuc.includes('tiền mặt') ||
      phuongThuc.includes('tien mat') ||
      phuongThuc.includes('cash') ||
      phuongThuc.includes('khi nhận hàng')
    ) {
      return false
    }

    if (thanhToan.trangThai === 'da_thanh_toan' || thanhToan.trangThai === 'hoan_tat') {
      return false
    }
  }

  return true
}

const handlePayOrder = (order) => {
  const info = order?.thongTinDonHang

  if (!info?.id) {
    toast.error('Không tìm thấy hóa đơn!')
    return
  }

  router.push({
    path: '/payment',
    query: {
      id: info.id,
      maHoaDon: info.maHoaDon,
    },
  })
}

// Dòng 408 trong file donhang.vue
function goToProductDetail(sp) {
  if (!sp) return

  // Lấy ID sản phẩm (Ưu tiên idSanPham để đến trang chi tiết sản phẩm gốc)
  const productId = sp.idSanPham || sp.idChiTietSanPham || sp.idSanPhamChiTiet

  if (!productId) {
    toast.error('Không tìm thấy thông tin sản phẩm!')
    return
  }

  router.push({
    name: 'confirmbuy', // Lưu ý: Kiểm tra name này có đúng với router/index.js của bạn không
    params: {
      id: productId,
    },
  })
}
function money(value) {
  if (value == null) return '0 ₫'
  return Number(value).toLocaleString('vi-VN') + ' ₫'
}

function formatDate(date) {
  if (!date) return ''
  return new Date(date).toLocaleString('vi-VN', {
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit',
    day: '2-digit',
    month: '2-digit',
    year: 'numeric',
  })
}

function formatPaymentStatus(status) {
  if (!status) return 'Chờ thanh toán'
  const s = String(status).toLowerCase()
  switch (s) {
    case 'cho_thanh_toan':
      return 'Chờ thanh toán'
    case 'da_thanh_toan':
      return 'Đã thanh toán'
    case 'chua_thanh_toan':
      return 'Chưa thanh toán'
    case 'hoan_tat':
      return 'Hoàn tất'
    case 'that_bai':
      return 'Thất bại'
    case 'da_huy':
      return 'Đã hủy'
    default:
      return status
  }
}

// Danh sách sau khi lọc tab
const filteredOrders = computed(() => {
  let list = orders.value

  if (activeTab.value !== 'all') {
    list = orders.value.filter((order) => {
      const status = (order.thongTinDonHang.trangThai || '').toLowerCase()
      return status === activeTab.value.toLowerCase()
    })
  }

  return [...list].sort((a, b) => {
    const dateA = new Date(a.thongTinDonHang.ngayTao).getTime() || 0
    const dateB = new Date(b.thongTinDonHang.ngayTao).getTime() || 0
    return dateB - dateA
  })
})

// Danh sách hiển thị theo từng trang
const paginatedOrders = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value
  const end = start + pageSize.value
  return filteredOrders.value.slice(start, end)
})

function statusType(status) {
  const s = (status || '').toLowerCase()
  switch (s) {
    case 'cho_xac_nhan':
      return 'warning'
    case 'da_xac_nhan':
      return 'primary'
    case 'cho_van_chuyen':
      return 'info'
    case 'dang_giao':
      return 'primary'
    case 'giao_thanh_cong':
    case 'hoan_thanh':
      return 'success'
    case 'giao_that_bai':
    case 'da_huy':
      return 'danger'
    default:
      return 'info'
  }
}

function paymentType(status) {
  const s = (status || '').toLowerCase()
  switch (s) {
    case 'da_thanh_toan':
      return 'success'
    case 'chua_thanh_toan':
      return 'danger'
    default:
      return 'warning'
  }
}

function getStep(order) {
  const t = (order.thongTinDonHang.trangThai || '').toLowerCase()
  switch (t) {
    case 'cho_xac_nhan':
      return 1
    case 'da_xac_nhan':
      return 2
    case 'cho_van_chuyen':
      return 3
    case 'dang_giao':
      return 4
    case 'giao_thanh_cong':
    case 'giao_that_bai':
      return 5
    case 'hoan_thanh':
      return 6
    default:
      return 1
  }
}

function openDetail(order) {
  selectedOrder.value = order
  dialogVisible.value = true
}

async function confirmReceived(order) {
  try {
    await ElMessageBox.confirm(
      'Bạn xác nhận đã nhận đủ hàng và sản phẩm không có vấn đề gì chứ?',
      'Xác nhận nhận hàng',
      {
        confirmButtonText: 'Đã nhận đủ',
        cancelButtonText: 'Hủy',
        type: 'success',
      },
    )

    await donHangService.xacNhanDaNhan(order.thongTinDonHang.id)
    toast.success('Cảm ơn bạn đã xác nhận nhận hàng!')
    loadOrders()
  } catch (e) {
    if (e !== 'cancel') {
      toast.error(e.message || 'Không thể cập nhật trạng thái')
    }
  }
}

function openCancelDialog(order) {
  orderToCancel.value = order
  selectedCancelReason.value = 'Thay đổi địa chỉ nhận hàng'
  customCancelReason.value = ''
  cancelDialogVisible.value = true
}

async function handleHuyDonHang() {
  let lyDoFinal = selectedCancelReason.value

  if (lyDoFinal === 'Khác') {
    if (!customCancelReason.value.trim()) {
      toast.warning('Vui lòng nhập lý do hủy chi tiết!')
      return
    }
    lyDoFinal = customCancelReason.value.trim()
  }

  canceling.value = true
  try {
    const idHoaDon = orderToCancel.value.thongTinDonHang.id
    await donHangService.huyDonHang(idHoaDon, lyDoFinal)

    toast.success('Hủy đơn hàng thành công!')
    cancelDialogVisible.value = false
    loadOrders()
  } catch (e) {
    toast.error(e.message || 'Hủy đơn hàng thất bại!')
  } finally {
    canceling.value = false
  }
}

onMounted(() => {
  loadOrders()
  connectSocket()
})

onUnmounted(() => {
  if (socketSubscription) {
    socketSubscription.unsubscribe()
  }
  if (stompClient) {
    stompClient.deactivate()
  }
})
</script>

<style scoped>
.order-list-wrapper {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.pagination-wrapper {
  display: flex;
  justify-content: center;
  padding: 16px;
  background: #ffffff;
  border-radius: 12px;
  border: 1px solid var(--border-color);
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.03);
}

.refresh-btn {
  background-color: #e9a02e !important;
  border-color: #e9a02e !important;
  color: #fff !important;
  border-radius: 12px;
  font-weight: 600;
  height: 40px;
  padding: 0 20px;
  transition: all 0.2s ease;
}

.refresh-btn:hover {
  background-color: #d99124 !important;
  border-color: #d99124 !important;
  color: #fff !important;
}

.refresh-btn:active {
  background-color: #c98520 !important;
  border-color: #c98520 !important;
}

.refresh-btn :deep(.el-icon) {
  color: #fff !important;
}

.product-list-container {
  margin-top: 10px;
}

.select-all-bar {
  display: flex;
  align-items: center;
  padding: 6px 10px;
  background-color: #f8f9fa;
  border-radius: 6px;
  margin-bottom: 6px;
  border: 1px solid #ebedf0;
}

.select-all-label {
  font-weight: 600;
  color: #1e293b;
  font-size: 13px;
}

.product-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 10px 0;
  border-bottom: 1px solid #f0f0f0;
}

.product-checkbox {
  margin-right: 4px;
}

:deep(.el-checkbox__inner) {
  border-color: #475569 !important;
  border-width: 1.5px !important;
  width: 16px;
  height: 16px;
}

:deep(.el-checkbox__input.is-checked .el-checkbox__inner),
:deep(.el-checkbox__input.is-indeterminate .el-checkbox__inner) {
  border-color: var(--primary-color) !important;
}

:deep(.el-checkbox:hover .el-checkbox__inner) {
  border-color: var(--primary-color) !important;
}

.el-checkbox-group {
  width: 100%;
}

.order-page {
  --primary-color: #0284c7;
  --primary-hover: #0369a1;
  --primary-bg: #e0f2fe;
  --text-main: #0f172a;
  --text-sub: #334155;
  --text-muted: #475569;
  --bg-main: #f8fafc;
  --border-color: #cbd5e1;
  --danger-color: #ef4444;
  --success-color: #10b981;
  --warning-color: #f59e0b;

  background: var(--bg-main);
  min-height: 100vh;
  padding: 24px;
  max-width: 1200px;
  margin: 0 auto;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Helvetica, Arial, sans-serif;
  color: var(--text-main);
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.header-title-box {
  display: flex;
  align-items: center;
  gap: 16px;
}

.header-icon {
  width: 48px;
  height: 48px;
  background: var(--primary-bg);
  color: var(--primary-color);
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  flex-shrink: 0;
}

.page-header h2 {
  margin: 0;
  font-size: 22px;
  color: var(--text-main);
  font-weight: 700;
  line-height: 1.2;
}

.page-header p {
  margin: 4px 0 0;
  color: var(--text-sub);
  font-size: 14px;
  font-weight: 500;
}

.tabs-wrapper {
  background: #ffffff;
  padding: 4px 16px 0 16px;
  border-radius: 12px;
  border: 1px solid var(--border-color);
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.03);
  margin-bottom: 20px;
}

:deep(.el-tabs__header) {
  margin: 0;
}

:deep(.el-tabs__nav-wrap::after) {
  height: 1px;
  background-color: var(--border-color);
}

:deep(.el-tabs__item) {
  font-weight: 600;
  color: var(--text-sub);
  font-size: 14px;
  transition: color 0.2s ease;
  height: 48px;
  line-height: 48px;
}

:deep(.el-tabs__item.is-active) {
  color: var(--primary-color);
  font-weight: 700;
}

:deep(.el-tabs__active-bar) {
  background-color: var(--primary-color);
  border-radius: 2px;
  height: 3px;
}

.loading-box {
  background: #ffffff;
  border-radius: 12px;
  padding: 24px;
  border: 1px solid var(--border-color);
}

.order-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.order-card {
  background: #ffffff;
  border-radius: 12px;
  border: 1px solid var(--border-color);
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.02);
  transition: all 0.25s ease-in-out;
  overflow: hidden;
}

.order-card:hover {
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.06);
  border-color: #94a3b8;
}

.order-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 16px;
  background: #f8fafc;
  border-bottom: 1px solid var(--border-color);
}

.order-code-date {
  display: flex;
  align-items: center;
  gap: 8px;
}

.order-code {
  font-weight: 700;
  color: var(--text-main);
  font-size: 15px;
  letter-spacing: 0.3px;
}

.dot {
  color: var(--text-sub);
}

.date {
  color: var(--text-sub);
  font-size: 13px;
  font-weight: 500;
}

.status-group {
  display: flex;
  align-items: center;
  gap: 8px;
}

.product-list-container {
  padding: 0 16px;
}

.product-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px 0;
  border-bottom: 1px solid #f1f5f9;
}

.product-item:last-child {
  border-bottom: none;
}

.product-checkbox {
  margin-right: 0;
  display: flex;
  align-items: center;
  justify-content: center;
}

:deep(.product-checkbox .el-checkbox__label) {
  display: none;
}

.product-image {
  width: 64px;
  height: 64px;
  border-radius: 8px;
  object-fit: cover;
  border: 1px solid var(--border-color);
  background-color: #f1f5f9;
  flex-shrink: 0;
}

.product-info {
  flex: 1;
  min-width: 0;
}

.product-info h4 {
  margin: 0 0 4px;
  font-size: 14px;
  color: var(--text-main);
  font-weight: 700;
  line-height: 1.4;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.product-meta {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  color: var(--text-sub);
  font-size: 12px;
  font-weight: 500;
}

.product-price {
  width: 140px;
  min-width: 140px;
  display: flex !important;
  flex-direction: column !important;
  align-items: flex-end !important;
  justify-content: center;
  gap: 4px !important;
  flex-shrink: 0;
}

.unit-price {
  color: #334155;
  font-size: 12px;
  font-weight: 600;
  line-height: 1.4;
  white-space: nowrap;
}

.quantity {
  color: #334155;
  font-size: 12px;
  font-weight: 600;
}

.total-price {
  color: #ef4444;
  font-size: 15px;
  font-weight: 700;
  line-height: 1.4;
  white-space: nowrap;
}

.price-row {
  display: flex;
  align-items: center;
  justify-content: flex-end;
  gap: 5px;
  white-space: nowrap;
}

.timeline-container {
  padding: 14px 16px;
  background: #fafafa;
  border-top: 1px solid #f1f5f9;
  border-bottom: 1px solid #f1f5f9;
}

:deep(.el-step__title) {
  font-size: 12px;
  font-weight: 600;
}

.cancel-banner {
  padding: 10px 16px;
  background: #fef2f2;
}

.return-box {
  padding: 10px 16px;
}

.order-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 16px;
  background: #ffffff;
}

.order-summary-mini {
  font-size: 13px;
  color: var(--text-sub);
  font-weight: 500;
}

.highlight-total {
  color: var(--danger-color);
  font-size: 16px;
  font-weight: 700;
  margin-left: 4px;
}

.action-buttons {
  display: flex;
  align-items: center;
  gap: 8px;
}

.action-buttons .el-button {
  border-radius: 8px;
  padding: 6px 14px;
  font-weight: 600;
}

.custom-dialog :deep(.el-dialog) {
  border-radius: 12px;
  overflow: hidden;
  max-height: 85vh;
  display: flex;
  flex-direction: column;
}

.custom-dialog :deep(.el-dialog__header) {
  margin-right: 0;
  padding: 12px 16px;
  border-bottom: 1px solid var(--border-color);
}

.custom-dialog :deep(.el-dialog__title) {
  font-size: 15px;
  font-weight: 700;
  color: var(--text-main);
}

.custom-dialog :deep(.el-dialog__body) {
  padding: 12px 16px;
  background: var(--bg-main);
  overflow-y: auto;
  flex: 1;
}

.custom-dialog :deep(.el-dialog__footer) {
  padding: 10px 16px;
  border-top: 1px solid var(--border-color);
}

.dialog-content {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.detail-banner {
  background: #ffffff;
  padding: 10px 14px;
  border-radius: 8px;
  border: 1px solid var(--border-color);
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.banner-left {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 13px;
  color: var(--text-sub);
  font-weight: 500;
}

.banner-left .code {
  color: var(--text-main);
  font-weight: 700;
  font-size: 14px;
}

.banner-right {
  display: flex;
  gap: 6px;
}

.detail-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 10px;
}

.detail-section {
  background: #ffffff;
  padding: 10px 12px;
  border-radius: 8px;
  border: 1px solid var(--border-color);
}

.section-title {
  display: flex;
  align-items: center;
  gap: 6px;
  margin-bottom: 8px;
  padding-bottom: 6px;
  border-bottom: 1px solid #f1f5f9;
  color: var(--primary-color);
}

.section-title h4 {
  margin: 0;
  font-size: 13px;
  color: var(--text-main);
  font-weight: 700;
}

.warning-title {
  color: var(--warning-color);
}

.receiver-info {
  display: flex;
  flex-direction: column;
  gap: 4px;
  font-size: 12px;
  color: var(--text-sub);
  font-weight: 500;
}

.receiver-name {
  margin: 0;
  color: var(--text-main);
  font-size: 13px;
}

.receiver-phone {
  margin: 0;
  display: flex;
  align-items: center;
  gap: 4px;
}

.receiver-address {
  margin: 0;
  color: var(--text-sub);
  line-height: 1.3;
}

.info-list {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.info-row {
  display: flex;
  justify-content: space-between;
  font-size: 12px;
  color: var(--text-sub);
  font-weight: 500;
}

.return-detail-section {
  border-color: #fef08a;
  background: #fefce8;
}

.text-danger {
  color: var(--danger-color);
  font-weight: 600;
}

.dialog-product-list {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.dialog-product-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding-bottom: 8px;
  border-bottom: 1px solid #f1f5f9;
}

.dialog-product-item:last-child {
  border-bottom: none;
  padding-bottom: 0;
}

.dialog-image {
  width: 50px;
  height: 50px;
  border-radius: 6px;
  object-fit: cover;
  border: 1px solid var(--border-color);
  flex-shrink: 0;
}

.dialog-info {
  flex: 1;
}

.dialog-info h5 {
  margin: 0 0 4px;
  font-size: 13px;
  color: var(--text-main);
  font-weight: 700;
}

.dialog-meta {
  display: flex;
  flex-wrap: wrap;
  gap: 4px;
}

.tag-badge {
  background: #f1f5f9;
  color: var(--text-sub);
  padding: 1px 6px;
  border-radius: 4px;
  font-size: 11px;
  font-weight: 500;
}

.dialog-price {
  text-align: right;
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.dialog-price .calc {
  font-size: 11px;
  color: var(--text-sub);
  font-weight: 500;
}

.dialog-price .subtotal {
  font-size: 13px;
  color: var(--danger-color);
  font-weight: 700;
}

.payment-summary-box {
  background: #ffffff;
}

.payment-summary-box .row {
  display: flex;
  justify-content: space-between;
  margin-bottom: 6px;
  font-size: 12px;
  color: var(--text-sub);
  font-weight: 500;
}

.payment-summary-box .discount {
  color: var(--success-color);
  font-weight: 600;
}

.total-row {
  font-size: 14px;
  font-weight: 700;
  color: var(--text-main);
}

.cancel-dialog-content {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.cancel-subtitle {
  font-size: 13px;
  color: var(--text-sub);
  margin-bottom: 6px;
  font-weight: 500;
}

.cancel-reasons-list {
  display: flex;
  flex-direction: column;
  gap: 6px;
  width: 100%;
}

.cancel-reasons-list .el-radio {
  width: 100%;
  padding: 8px 12px;
  border-radius: 8px;
  border: 1px solid var(--border-color);
  background-color: #ffffff;
  transition: all 0.2s ease;
  margin-right: 0;
  white-space: normal;
  height: auto;
  line-height: 1.4;
  box-sizing: border-box;
}

.cancel-reasons-list .el-radio:hover {
  border-color: var(--primary-color);
  background-color: #f0f9ff;
}

.cancel-reasons-list .el-radio.is-checked {
  border-color: var(--primary-color);
  background-color: #f0f9ff;
}

.custom-reason-input {
  margin-top: 6px;
}

@media (max-width: 768px) {
  .order-page {
    padding: 12px;
  }

  .page-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
  }

  .order-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
  }

  .product-item {
    align-items: flex-start;
  }

  .product-price {
    text-align: left;
    margin-top: 4px;
  }

  .order-footer {
    flex-direction: column;
    gap: 12px;
    align-items: stretch;
  }

  .order-summary-mini {
    text-align: right;
  }

  .action-buttons {
    justify-content: flex-end;
    flex-wrap: wrap;
  }

  .detail-grid {
    grid-template-columns: 1fr;
  }

  .timeline-container {
    padding: 12px 8px;
    overflow-x: auto;
  }
}

.pay-btn {
  background: linear-gradient(135deg, #22c55e, #16a34a);
  border: none;
  color: white;
  font-weight: 600;
  border-radius: 8px;
  transition: all 0.25s;
}

.pay-btn:hover {
  background: linear-gradient(135deg, #16a34a, #15803d);
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(34, 197, 94, 0.35);
}
</style>
