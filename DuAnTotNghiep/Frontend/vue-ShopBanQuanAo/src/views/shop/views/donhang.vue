<template>
  <div class="order-page">
    <!-- ================= HEADER ================= -->
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

      <el-button type="primary" class="refresh-btn" :icon="Refresh" @click="loadOrders">
        Làm mới
      </el-button>
    </div>

    <!-- ================= TAB ================= -->
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

    <!-- ================= LOADING ================= -->
    <div v-if="loading" class="loading-box">
      <el-skeleton :rows="5" animated />
    </div>

    <!-- ================= EMPTY ================= -->
    <el-empty v-else-if="filteredOrders.length == 0" description="Không có đơn hàng nào" />

    <!-- ================= LIST ================= -->
    <div v-else class="order-list">
      <div v-for="order in filteredOrders" :key="order.thongTinDonHang.id" class="order-card">
        <!-- CARD HEADER -->
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

        <!-- PRODUCTS -->
        <div class="product-list-container">
          <div class="product-item" v-for="sp in order.sanPham" :key="sp.idHoaDonChiTiet">
            <img
              :src="imageUrl(sp.anh)"
              :alt="sp.tenSanPham"
              class="product-image"
              @error="$event.target.src = 'https://placehold.co/80x80?text=No+Image'"
            />

            <div class="product-info">
              <h4>{{ sp.tenSanPham }}</h4>

              <div class="product-meta">
                <span>Mã: {{ sp.maSanPham }}</span>
                <span>Màu: {{ sp.mauSac }}</span>
                <span>Size: {{ sp.kichThuoc }}</span>
              </div>
            </div>

            <div class="product-price">
              <span class="unit-price">{{ money(sp.donGia) }}</span>
              <span class="quantity">x{{ sp.soLuong }}</span>
              <strong class="total-price">{{ money(sp.thanhTien) }}</strong>
            </div>
          </div>
        </div>

        <!-- TIMELINE (Cập nhật 6 bước chuẩn) -->
        <!-- TIMELINE (6 bước chuẩn luồng shop) -->
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
            <!-- Đã đổi tên chuẩn quy trình -->
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

        <!-- RETURN BOX -->
        <div class="return-box" v-if="order.traHang && order.traHang.coTraHang">
          <el-alert type="warning" show-icon :closable="false">
            <template #title>Đơn hàng đang có yêu cầu trả hàng</template>
            <p><b>Mã:</b> {{ order.traHang.maTraHang }} | <b>Lý do:</b> {{ order.traHang.lyDo }}</p>
          </el-alert>
        </div>

        <!-- CARD FOOTER / SUMMARY & ACTION -->
        <div class="order-footer">
          <div class="order-summary-mini">
            <span>Tổng số tiền ({{ order.sanPham.length }} sản phẩm): </span>
            <strong class="highlight-total">{{
              money(order.thongTinDonHang.tongThanhToan)
            }}</strong>
          </div>

          <div class="action-buttons">
            <el-button plain @click="openDetail(order)">Xem chi tiết</el-button>

            <!-- Nút Thanh toán ngay nếu chưa thanh toán -->
            <el-button
              type="primary"
              v-if="
                order.thongTinDonHang.trangThaiThanhToan === 'chua_thanh_toan' &&
                order.thongTinDonHang.trangThai !== 'da_huy'
              "
            >
              Thanh toán
            </el-button>

            <!-- Nút Xác nhận đã nhận hàng nếu đang ở giao_thanh_cong -->
            <el-button
              type="success"
              v-if="order.thongTinDonHang.trangThai === 'giao_thanh_cong'"
              @click="confirmReceived(order)"
            >
              Đã nhận được hàng
            </el-button>

            <el-button plain type="primary">Mua lại</el-button>
          </div>
        </div>
      </div>
    </div>

    <!-- ================= DETAIL DIALOG ================= -->
    <el-dialog
      v-model="dialogVisible"
      width="800px"
      title="Chi tiết đơn hàng"
      destroy-on-close
      class="custom-dialog"
    >
      <div v-if="selectedOrder" class="dialog-content">
        <!-- TOP: MÃ ĐƠN & TRẠNG THÁI -->
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
              :type="paymentType(selectedOrder.thongTinDonHang.trangThaiThanhToan)"
              effect="plain"
              round
            >
              {{ selectedOrder.thongTinDonHang.trangThaiThanhToanHienThi }}
            </el-tag>
          </div>
        </div>

        <!-- THÔNG TIN GIAO HÀNG & CHUNG GRID -->
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
                <b>{{ selectedOrder.thongTinDonHang.loaiHoaDon || 'Trực tuyến' }}</b>
              </div>
              <div class="info-row">
                <span>Ngày tạo:</span>
                <span>{{ formatDate(selectedOrder.thongTinDonHang.ngayTao) }}</span>
              </div>
              <div class="info-row">
                <span>Thanh toán:</span>
                <span>{{ selectedOrder.thongTinDonHang.trangThaiThanhToanHienThi }}</span>
              </div>
            </div>
          </div>
        </div>

        <!-- THÔNG TIN TRẢ HÀNG (NẾU CÓ) -->
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

        <!-- DANH SÁCH SẢN PHẨM -->
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
                  <span class="tag-badge">Mã: {{ sp.maSanPham }}</span>
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

        <!-- TỔNG KẾT THANH TOÁN -->
        <div class="detail-section payment-summary-box">
          <div class="row">
            <span>Tiền hàng</span>
            <span>{{ money(selectedOrder.thongTinDonHang.tongTienHang) }}</span>
          </div>
          <div class="row">
            <span>Giảm giá voucher</span>
            <span class="discount">-{{ money(selectedOrder.thongTinDonHang.tongGiamGia) }}</span>
          </div>
          <div class="row">
            <span>Phí vận chuyển</span>
            <span>{{ money(selectedOrder.thongTinDonHang.phiVanChuyen) }}</span>
          </div>
          <el-divider style="margin: 12px 0" />
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
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import {
  Refresh,
  ShoppingBag,
  Location,
  Phone,
  InfoFilled,
  Warning,
  Goods,
} from '@element-plus/icons-vue'

// ======================
// STATE
// ======================
const loading = ref(false)
const activeTab = ref('all')
const orders = ref([])
const dialogVisible = ref(false)
const selectedOrder = ref(null)

const imageUrl = (path) => {
  if (!path) return 'https://placehold.co/80x80?text=No+Image'
  return `http://localhost:8080${path}`
}

// ======================
// API
// ======================
const API = 'http://localhost:8080/don-hang'

async function loadOrders() {
  loading.value = true
  try {
    const token = sessionStorage.getItem('token')
    const response = await fetch(API, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    })
    const data = await response.json()
    if (!response.ok) {
      throw new Error('Không lấy được danh sách đơn hàng')
    }
    orders.value = data
  } catch (e) {
    console.error(e)
    ElMessage.error(e.message)
  } finally {
    loading.value = false
  }
}

// ======================
// HELPERS
// ======================
function money(value) {
  if (value == null) return '0 ₫'
  return Number(value).toLocaleString('vi-VN') + ' ₫'
}

function formatDate(date) {
  if (!date) return ''
  return new Date(date).toLocaleString('vi-VN', {
    hour: '2-digit',
    minute: '2-digit',
    day: '2-digit',
    month: '2-digit',
    year: 'numeric',
  })
}

// ======================
// FILTER
// ======================
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

// ======================
// TAG STATUS (Cập nhật chuẩn 8 trạng thái)
// ======================
function statusType(status) {
  const s = (status || '').toLowerCase()
  switch (s) {
    case 'cho_xac_nhan':
      return 'warning'
    case 'da_xac_nhan':
      return 'primary'
    case 'cho_van_chuyen':
      return 'info' // Sửa từ 'cyan' thành 'info'
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

// ======================
// PAYMENT TAG
// ======================
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

// ======================
// STEP (Cập nhật khớp với 6 nút Timeline)
// ======================
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

// Xử lý nút khách ấn Đã nhận được hàng
async function confirmReceived(order) {
  try {
    const token = sessionStorage.getItem('token')
    const res = await fetch(
      `http://localhost:8080/don-hang/${order.thongTinDonHang.id}/xac-nhan-da-nhan`,
      {
        method: 'PUT',
        headers: {
          Authorization: `Bearer ${token}`,
        },
      },
    )
    if (!res.ok) throw new Error('Không thể cập nhật trạng thái')
    ElMessage.success('Cảm ơn bạn đã xác nhận nhận hàng!')
    loadOrders()
  } catch (e) {
    ElMessage.error(e.message)
  }
}

onMounted(() => {
  loadOrders()
})
</script>

<style scoped>
/* ===========================
   LAYOUT & CONTAINER
=========================== */
.order-page {
  background: #f8fafc;
  min-height: 100vh;
  padding: 24px;
  max-width: 1200px;
  margin: 0 auto;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Helvetica, Arial, sans-serif;
}

/* ===========================
   HEADER
=========================== */
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
  background: #e0f2fe;
  color: #0284c7;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
}

.page-header h2 {
  margin: 0;
  font-size: 24px;
  color: #0f172a;
  font-weight: 700;
}

.page-header p {
  margin: 4px 0 0;
  color: #64748b;
  font-size: 14px;
}

.refresh-btn {
  border-radius: 8px;
  font-weight: 500;
}

/* ===========================
   TABS
=========================== */
.tabs-wrapper {
  background: #ffffff;
  padding: 0 16px;
  border-radius: 12px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
  margin-bottom: 20px;
}

:deep(.el-tabs__header) {
  margin: 0;
}

:deep(.el-tabs__nav-wrap::after) {
  height: 1px;
  background-color: #f1f5f9;
}

/* ===========================
   LOADING & EMPTY
=========================== */
.loading-box {
  background: white;
  border-radius: 12px;
  padding: 24px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
}

/* ===========================
   ORDER CARD
=========================== */
.order-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.order-card {
  background: #ffffff;
  border-radius: 12px;
  border: 1px solid #e2e8f0;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.02);
  transition: all 0.2s ease;
  overflow: hidden;
}

.order-card:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  border-color: #cbd5e1;
}

/* ===========================
   CARD HEADER
=========================== */
.order-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 20px;
  background: #f8fafc;
  border-bottom: 1px solid #f1f5f9;
}

.order-code-date {
  display: flex;
  align-items: center;
  gap: 8px;
}

.order-code {
  font-weight: 600;
  color: #0f172a;
  font-size: 15px;
}

.dot {
  color: #cbd5e1;
}

.date {
  color: #64748b;
  font-size: 13px;
}

.status-group {
  display: flex;
  gap: 8px;
}

/* ===========================
   PRODUCTS CONTAINER
=========================== */
.product-list-container {
  padding: 0 20px;
}

.product-item {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 16px 0;
  border-bottom: 1px solid #f1f5f9;
}

.product-item:last-child {
  border-bottom: none;
}

.product-image {
  width: 72px;
  height: 72px;
  border-radius: 8px;
  object-fit: cover;
  border: 1px solid #e2e8f0;
}

.product-info {
  flex: 1;
}

.product-info h4 {
  margin: 0 0 6px;
  font-size: 15px;
  color: #0f172a;
  font-weight: 600;
}

.product-meta {
  display: flex;
  gap: 16px;
  color: #64748b;
  font-size: 13px;
}

.product-price {
  text-align: right;
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.unit-price {
  color: #94a3b8;
  font-size: 13px;
  text-decoration: line-through;
}

.quantity {
  color: #64748b;
  font-size: 13px;
}

.total-price {
  color: #ef4444;
  font-size: 16px;
  font-weight: 600;
}

/* ===========================
   TIMELINE & RETURN
=========================== */
.timeline-container {
  padding: 20px;
  background: #fafafa;
  border-top: 1px solid #f1f5f9;
  border-bottom: 1px solid #f1f5f9;
}

.return-box {
  padding: 12px 20px;
}

/* ===========================
   CARD FOOTER
=========================== */
.order-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 20px;
  background: #ffffff;
}

.order-summary-mini {
  font-size: 14px;
  color: #475569;
}

.highlight-total {
  color: #ef4444;
  font-size: 18px;
  font-weight: 700;
}

.action-buttons {
  display: flex;
  gap: 10px;
}

.action-buttons .el-button {
  border-radius: 8px;
  padding: 8px 16px;
  font-weight: 500;
}

/* ===========================
   DIALOG STYLING (NEW & POLISHED)
=========================== */
.custom-dialog :deep(.el-dialog__body) {
  padding: 20px;
  background: #f8fafc;
  max-height: 75vh;
  overflow-y: auto;
}

.dialog-content {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.detail-banner {
  background: #ffffff;
  padding: 14px 18px;
  border-radius: 10px;
  border: 1px solid #e2e8f0;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.banner-left {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
  color: #64748b;
}

.banner-left .code {
  color: #0f172a;
  font-weight: 700;
  font-size: 16px;
}

.banner-right {
  display: flex;
  gap: 8px;
}

.detail-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 16px;
}

.detail-section {
  background: #ffffff;
  padding: 16px;
  border-radius: 10px;
  border: 1px solid #e2e8f0;
}

.section-title {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 12px;
  padding-bottom: 8px;
  border-bottom: 1px solid #f1f5f9;
  color: #0284c7;
}

.section-title h4 {
  margin: 0;
  font-size: 14px;
  color: #0f172a;
  font-weight: 600;
}

.warning-title {
  color: #eab308;
}

.receiver-info {
  display: flex;
  flex-direction: column;
  gap: 6px;
  font-size: 13px;
  color: #475569;
}

.receiver-name {
  margin: 0;
  color: #0f172a;
  font-size: 14px;
}

.receiver-phone {
  margin: 0;
  display: flex;
  align-items: center;
  gap: 6px;
}

.receiver-address {
  margin: 0;
  color: #64748b;
  line-height: 1.4;
}

.info-list {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.info-row {
  display: flex;
  justify-content: space-between;
  font-size: 13px;
  color: #475569;
}

.return-detail-section {
  border-color: #fef08a;
  background: #fefce8;
}

.text-danger {
  color: #ef4444;
  font-weight: 500;
}

.dialog-product-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.dialog-product-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding-bottom: 12px;
  border-bottom: 1px solid #f1f5f9;
}

.dialog-product-item:last-child {
  border-bottom: none;
  padding-bottom: 0;
}

.dialog-image {
  width: 64px;
  height: 64px;
  border-radius: 6px;
  object-fit: cover;
  border: 1px solid #e2e8f0;
}

.dialog-info {
  flex: 1;
}

.dialog-info h5 {
  margin: 0 0 6px;
  font-size: 14px;
  color: #0f172a;
  font-weight: 600;
}

.dialog-meta {
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
}

.tag-badge {
  background: #f1f5f9;
  color: #64748b;
  padding: 2px 6px;
  border-radius: 4px;
  font-size: 11px;
}

.dialog-price {
  text-align: right;
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.dialog-price .calc {
  font-size: 12px;
  color: #94a3b8;
}

.dialog-price .subtotal {
  font-size: 15px;
  color: #ef4444;
  font-weight: 600;
}

.payment-summary-box {
  background: #ffffff;
}

.payment-summary-box .row {
  display: flex;
  justify-content: space-between;
  margin-bottom: 8px;
  font-size: 13px;
  color: #475569;
}

.payment-summary-box .discount {
  color: #10b981;
  font-weight: 500;
}

.total-row {
  font-size: 15px;
  font-weight: 600;
  color: #0f172a;
}

/* ===========================
   RESPONSIVE DESIGN
=========================== */
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

  .order-footer {
    flex-direction: column;
    gap: 12px;
    align-items: stretch;
  }

  .action-buttons {
    justify-content: flex-end;
  }

  .detail-grid {
    grid-template-columns: 1fr;
  }
}
</style>
