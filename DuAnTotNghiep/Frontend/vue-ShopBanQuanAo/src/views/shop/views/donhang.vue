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
    <el-empty v-else-if="filteredOrders.length === 0" description="Không có đơn hàng nào" />

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
          <!-- Thanh Chọn tất cả sản phẩm trong hóa đơn này -->
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

          <!-- Checkbox group từng sản phẩm -->
          <el-checkbox-group v-model="selectedProductsMap[order.thongTinDonHang.id]">
            <div class="product-item" v-for="sp in order.sanPham" :key="sp.idHoaDonChiTiet">
              <el-checkbox :value="sp.idSanPham" class="product-checkbox" />

              <img
                :src="imageUrl(sp.anh)"
                :alt="sp.tenSanPham"
                class="product-image cursor-pointer hover:opacity-80 transition"
                @click="goToProductDetail(sp.idSanPham)"
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
          </el-checkbox-group>
        </div>

        <!-- TIMELINE (6 bước chuẩn) -->
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

            <!-- Nút Hủy đơn hàng -->
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

            <!-- Nút Xác nhận đã nhận hàng -->
            <el-button
              type="success"
              v-if="order.thongTinDonHang.trangThai === 'giao_thanh_cong'"
              @click="confirmReceived(order)"
            >
              Đã nhận được hàng
            </el-button>

            <el-button plain type="primary" @click="handleRebuy(order)"> Mua lại </el-button>
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

    <!-- ================= DIALOG HỦY ĐƠN HÀNG ================= -->
    <el-dialog
      v-model="cancelDialogVisible"
      title="Xác nhận hủy đơn hàng"
      width="500px"
      destroy-on-close
    >
      <div v-if="orderToCancel" class="cancel-dialog-content">
        <p class="cancel-subtitle">
          Vui lòng chọn lý do bạn muốn hủy đơn hàng
          <b>{{ orderToCancel.thongTinDonHang.maHoaDon }}</b
          >:
        </p>

        <!-- COMBO LÝ DO HỦY -->
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

        <!-- Ô NHẬP LÝ DO RIÊNG NẾU CHỌN KHÁC -->
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
import { ref, computed, onMounted, onUnmounted } from 'vue'
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
  Goods,
} from '@element-plus/icons-vue'
import donHangService from '@/service/DonHangService'

const router = useRouter()

// Alias toast chuẩn Element Plus
const toast = {
  success: (msg) => ElMessage.success(msg),
  info: (msg) => ElMessage.info(msg),
  warning: (msg) => ElMessage.warning(msg),
  error: (msg) => ElMessage.error(msg),
}

// State lưu danh sách idSanPham được chọn cho từng đơn hàng: { [idHoaDon]: [id1, id2] }
const selectedProductsMap = ref({})

// ======================
// STATE
// ======================
const loading = ref(false)
const activeTab = ref('all')
const orders = ref([])

// Chi tiết đơn
const dialogVisible = ref(false)
const selectedOrder = ref(null)

// Hủy đơn
const cancelDialogVisible = ref(false)
const orderToCancel = ref(null)
const selectedCancelReason = ref('Thay đổi địa chỉ nhận hàng')
const customCancelReason = ref('')
const canceling = ref(false)

const imageUrl = (path) => {
  if (!path) return 'https://placehold.co/80x80?text=No+Image'
  if (path.startsWith('http')) return path
  return `http://localhost:8080${path}`
}

// ==========================================
// SOCKET REALTIME (KHÔNG DÙNG SOCKJS -> DÙNG NATIVE WS)
// ==========================================
const stompClient = new Client({
  brokerURL: 'ws://localhost:8080/ws', // Đường dẫn WebSocket trực tiếp từ Spring Boot
  reconnectDelay: 5000,
  onStompError: (frame) => {
    console.error('🔴 Lỗi kết nối WebSocket:', frame.headers['message'])
  },
})

let socketSubscription = null

const subscribePos = () => {
  // 1. Hủy đăng ký cũ nếu đã tồn tại (chống lặp sự kiện)
  if (socketSubscription) {
    socketSubscription.unsubscribe()
  }

  // 2. Đăng ký nhận tin mới từ Socket
  socketSubscription = stompClient.subscribe('/topic/orders', async (msg) => {
    try {
      // Tải lại danh sách đơn hàng ngầm
      await loadOrders()

      // 🔔 XỬ LÝ THÔNG BÁO TỪ BACKEND
      if (msg && msg.body) {
        let data = {}
        let isJson = false

        try {
          data = JSON.parse(msg.body)
          isJson = true
        } catch (e) {
          // Chuỗi Text thuần
        }

        const eventType = isJson ? data.type || data.eventType || data.action : ''

        // Trường hợp bị từ chối hủy đơn
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

        // 🟢 TH1: Riêng sự kiện THANH TOÁN -> Hiện thông báo cố định màu xanh
        if (eventType === 'INVOICE_PAID') {
          toast.success('Thanh toán thành công!')
        }
        // 🔵 TH2: Tất cả sự kiện khác -> Lấy nguyên văn thông báo từ BE gửi sang
        else {
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

// ======================
// LOAD DANH SÁCH ĐƠN HÀNG
// ======================
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

// ======================
// LOGIC CHỌN TẤT CẢ (SELECT ALL)
// ======================
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

// ======================
// XỬ LÝ MUA LẠI
// ======================
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
      const gia = Number(sp.donGia || sp.giaBan || 0)
      const qty = Number(sp.soLuong || 1)

      return {
        productDetailId: sp.idChiTietSanPham || sp.idSanPhamChiTiet || sp.idSanPham,
        quantity: qty,
        tenSanPham: sp.tenSanPham,
        maSanPhamChiTiet: sp.maSanPhamChiTiet || sp.maSanPham || '',
        giaBan: gia,
        mauSac: sp.mauSac || '',
        kichCo: sp.kichCo || sp.kichThuoc || '',
        anh: sp.anh || '',
        soLuongTon: sp.soLuongTon ?? 99,
        thanhTien: gia * qty,
        soLuongKhaDung: sp.soLuongKhaDung ?? 1,
      }
    }),
  }

  sessionStorage.setItem('checkoutData', JSON.stringify(checkoutData))
  router.push('/xacnhan')
}

function goToProductDetail(id) {
  if (!id) return

  router.push({
    name: 'confirmbuy',
    params: {
      id: id,
    },
  })
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
// FILTER & SORT
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
// TAG STATUS
// ======================
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

// ======================
// ACTION HANDLERS
// ======================
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

// ======================
// LIFECYCLE HOOKS
// ======================
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
/* ===========================
   CSS VARIABLES & DESIGN TOKENS
=========================== */
.product-list-container {
  margin-top: 12px;
}

.select-all-bar {
  display: flex;
  align-items: center;
  padding: 8px 12px;
  background-color: #f8f9fa;
  border-radius: 6px;
  margin-bottom: 8px;
  border: 1px solid #ebedf0;
}

.select-all-label {
  font-weight: 600;
  color: #303133;
  font-size: 13px;
}

.product-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px 0;
  border-bottom: 1px solid #f0f0f0;
}

.product-checkbox {
  margin-right: 4px;
}

.el-checkbox-group {
  width: 100%;
}

.order-page {
  --primary-color: #0284c7;
  --primary-hover: #0369a1;
  --primary-bg: #e0f2fe;
  --text-main: #0f172a;
  --text-sub: #475569;
  --text-muted: #94a3b8;
  --bg-main: #f8fafc;
  --border-color: #e2e8f0;
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
  color: var(--text-muted);
  font-size: 14px;
}

.refresh-btn {
  border-radius: 8px;
  font-weight: 500;
  transition: all 0.2s ease;
}

/* ===========================
   TABS STYLING
=========================== */
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
  font-weight: 500;
  color: var(--text-sub);
  font-size: 14px;
  transition: color 0.2s ease;
  height: 48px;
  line-height: 48px;
}

:deep(.el-tabs__item.is-active) {
  color: var(--primary-color);
  font-weight: 600;
}

:deep(.el-tabs__active-bar) {
  background-color: var(--primary-color);
  border-radius: 2px;
  height: 3px;
}

/* ===========================
   LOADING & EMPTY STATE
=========================== */
.loading-box {
  background: #ffffff;
  border-radius: 12px;
  padding: 24px;
  border: 1px solid var(--border-color);
}

/* ===========================
   ORDER LIST & CARD
=========================== */
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
  border-color: #cbd5e1;
}

/* ===========================
   CARD HEADER
=========================== */
.order-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 14px 20px;
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
  color: var(--text-muted);
}

.date {
  color: var(--text-sub);
  font-size: 13px;
}

.status-group {
  display: flex;
  align-items: center;
  gap: 8px;
}

/* ===========================
   PRODUCTS CONTAINER & CHECKBOX
=========================== */
.product-list-container {
  padding: 0 20px;
}

.el-checkbox-group {
  width: 100%;
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
  width: 72px;
  height: 72px;
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
  margin: 0 0 6px;
  font-size: 15px;
  color: var(--text-main);
  font-weight: 600;
  line-height: 1.4;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.product-meta {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  color: var(--text-sub);
  font-size: 13px;
}

/* ===========================
   PHẦN GIÁ SẢN PHẨM
=========================== */
.product-price {
  display: flex !important;
  flex-direction: column !important;
  align-items: flex-end !important;
  justify-content: center;
  gap: 4px !important;
  height: auto !important;
  flex-shrink: 0;
}

.product-price .unit-price,
.product-price .quantity,
.product-price .total-price {
  position: static !important;
  display: block !important;
  margin: 0 !important;
  line-height: 1.4 !important;
  height: auto !important;
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
  font-weight: 700;
}

/* ===========================
   TIMELINE & RETURN BANNER
=========================== */
.timeline-container {
  padding: 20px;
  background: #fafafa;
  border-top: 1px solid #f1f5f9;
  border-bottom: 1px solid #f1f5f9;
}

:deep(.el-step__title) {
  font-size: 13px;
  font-weight: 500;
}

.cancel-banner {
  padding: 12px 20px;
  background: #fef2f2;
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
  color: var(--text-sub);
}

.highlight-total {
  color: var(--danger-color);
  font-size: 18px;
  font-weight: 700;
  margin-left: 4px;
}

.action-buttons {
  display: flex;
  align-items: center;
  gap: 10px;
}

.action-buttons .el-button {
  border-radius: 8px;
  padding: 8px 16px;
  font-weight: 500;
}

/* ===========================
   DIALOG STYLING & POLISH
=========================== */
.custom-dialog :deep(.el-dialog) {
  border-radius: 12px;
  overflow: hidden;
}

.custom-dialog :deep(.el-dialog__header) {
  margin-right: 0;
  padding: 16px 20px;
  border-bottom: 1px solid var(--border-color);
}

.custom-dialog :deep(.el-dialog__title) {
  font-size: 16px;
  font-weight: 700;
  color: var(--text-main);
}

.custom-dialog :deep(.el-dialog__body) {
  padding: 20px;
  background: var(--bg-main);
  max-height: 70vh;
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
  border: 1px solid var(--border-color);
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.banner-left {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
  color: var(--text-sub);
}

.banner-left .code {
  color: var(--text-main);
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
  border: 1px solid var(--border-color);
}

.section-title {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 12px;
  padding-bottom: 8px;
  border-bottom: 1px solid #f1f5f9;
  color: var(--primary-color);
}

.section-title h4 {
  margin: 0;
  font-size: 14px;
  color: var(--text-main);
  font-weight: 600;
}

.warning-title {
  color: var(--warning-color);
}

.receiver-info {
  display: flex;
  flex-direction: column;
  gap: 6px;
  font-size: 13px;
  color: var(--text-sub);
}

.receiver-name {
  margin: 0;
  color: var(--text-main);
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
  color: var(--text-sub);
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
  color: var(--text-sub);
}

.return-detail-section {
  border-color: #fef08a;
  background: #fefce8;
}

.text-danger {
  color: var(--danger-color);
  font-weight: 500;
}

/* LIST ITEM IN DIALOG */
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
  width: 60px;
  height: 60px;
  border-radius: 6px;
  object-fit: cover;
  border: 1px solid var(--border-color);
  flex-shrink: 0;
}

.dialog-info {
  flex: 1;
}

.dialog-info h5 {
  margin: 0 0 6px;
  font-size: 14px;
  color: var(--text-main);
  font-weight: 600;
}

.dialog-meta {
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
}

.tag-badge {
  background: #f1f5f9;
  color: var(--text-sub);
  padding: 2px 8px;
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
  color: var(--text-muted);
}

.dialog-price .subtotal {
  font-size: 14px;
  color: var(--danger-color);
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
  color: var(--text-sub);
}

.payment-summary-box .discount {
  color: var(--success-color);
  font-weight: 500;
}

.total-row {
  font-size: 15px;
  font-weight: 600;
  color: var(--text-main);
}

/* ===========================
   CANCEL DIALOG CUSTOM
=========================== */
.cancel-dialog-content {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.cancel-subtitle {
  font-size: 14px;
  color: var(--text-sub);
  margin-bottom: 8px;
}

.cancel-reasons-list {
  display: flex;
  flex-direction: column;
  gap: 8px;
  width: 100%;
}

.cancel-reasons-list .el-radio {
  width: 100%;
  padding: 10px 14px;
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
  margin-top: 8px;
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
</style>
