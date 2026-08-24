import { createRouter, createWebHistory } from 'vue-router'
import { ElMessage } from 'element-plus'
import confirmbuy from '@/views/shop/confirmbuy.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),

  routes: [
    {
      path: '/login',
      name: 'login',
      component: () => import('@/views/shop/login.vue'),
    },
    {
      path: '/register',
      name: 'register',
      component: () => import('@/views/shop/register.vue'),
    },
    {
      path: '/forgot-password',
      name: 'forgot-password',
      component: () => import('@/views/shop/forgot-password.vue'),
    },

    // ================= SHOP =================
    {
      path: '/',
      component: () => import('@/views/shop/layouts/ShopLayout.vue'),

      children: [
        {
          path: '',
          redirect: '/home',
        },
        {
          path: 'home',
          name: 'home',
          component: () => import('@/views/shop/home.vue'),
        },
        {
          path: 'yeuthich',
          name: 'yeuthich',
          component: () => import('@/views/shop/components/YeuThich.vue'),
        },
        {
          path: 'thu-do',
          name: 'thu-do',
          component: () => import('@/components/VirtualTryOn.vue'),
        },
        {
          path: 'giohang',
          name: 'giohang',
          component: () => import('../views/shop/components/CarView.vue'),
        },
        {
          path: 'donhang',
          name: 'donhang',
          component: () => import('../views/shop/views/donhang.vue'),
          meta: { requiresAuth: true, roles: ['USERS', 'STAFF', 'ADMIN'] },
        },
        {
          path: 'san-pham',
          name: 'sanPham',
          component: () => import('@/views/shop/sanpham.vue'),
        },
        {
          path: 'minigame',
          name: 'minigame',
          component: () => import('@/views/shop/components/GamificationUserView.vue'),
        },
        {
          path: 'spct/:id',
          name: 'confirmbuy',
          component: confirmbuy,
        },
        {
          path: 'product/:id',
          name: 'product-detail',
          component: () => import('@/views/shop/confirmbuy.vue'),
        },
        {
          path: 'xacnhan',
          name: 'xacnhan',
          component: () => import('@/views/shop/views/xacnhan.vue'),
          meta: { requiresAuth: true, roles: ['USERS', 'STAFF', 'ADMIN'] },
        },
        {
          path: 'payment',
          name: 'payment',
          component: () => import('@/views/shop/views/payment.vue'),
          meta: { requiresAuth: true, roles: ['USERS', 'STAFF', 'ADMIN'] },
        },
        {
          path: 'payment-result',
          name: 'paymentResult',
          component: () => import('@/views/shop/views/PaymentResult.vue'),
        },
      ],
    },

    // ================= QUẢN TRỊ ADMIN =================
    {
      path: '/admin',
      component: () => import('../views/Admin/Layout/AdminLayout.vue'),
      redirect: '/admin/banhang',
      meta: { requiresAuth: true, roles: ['ADMIN', 'STAFF'] },
      children: [
        // ----------------- DÀNH CHO CẢ ADMIN & STAFF (5 NGHIỆP VỤ CHÍNH) -----------------
        {
          path: 'chat',
          name: 'AdminChat',
          component: () => import('../views/Admin/Chat/ChatAdminView.vue'),
          meta: { requiresAuth: true, roles: ['ADMIN', 'STAFF'] },
        },
        {
          path: 'thongke',
          name: 'thongke',
          component: () => import('../views/Admin/thongke/mainn.vue'),
          meta: { requiresAuth: true, roles: ['ADMIN', 'STAFF'] },
        },
        {
          path: 'khachhang',
          name: 'khachHang',
          component: () => import('../views/Admin/KhachHangView.vue'),
          meta: { requiresAuth: true, roles: ['ADMIN', 'STAFF'] },
        },
        {
          path: 'khach-hang/add',
          name: 'addKhachHang',
          component: () => import('../components/FormKhachHangComponents.vue'),
          meta: { requiresAuth: true, roles: ['ADMIN', 'STAFF'] },
        },
        {
          path: 'khach-hang/edit/:id',
          name: 'editKhachHang',
          component: () => import('../components/FormKhachHangComponents.vue'),
          meta: { requiresAuth: true, roles: ['ADMIN', 'STAFF'] },
        },
        {
          path: 'banhang',
          name: 'banghang',
          component: () => import('../views/Admin/banhang/banhang.vue'),
          meta: { requiresAuth: true, roles: ['ADMIN', 'STAFF'] },
        },
        {
          path: 'hoadononline',
          name: 'hoadononline',
          component: () => import('../views/shop/views/OrderManagement.vue'),
          meta: { requiresAuth: true, roles: ['ADMIN', 'STAFF'] },
        },
        {
          path: 'hoadon',
          name: 'hoadon',
          component: () => import('../views/Admin/banhang/hoadon.vue'),
          meta: { requiresAuth: true, roles: ['ADMIN', 'STAFF'] },
        },
        {
          path: 'hdct',
          name: 'hdct',
          component: () => import('../views/Admin/banhang/hdct.vue'),
          meta: { requiresAuth: true, roles: ['ADMIN', 'STAFF'] },
        },
        {
          path: 'hdct/:id',
          name: 'HoaDonChiTiet',
          component: () => import('../views/Admin/banhang/hdct.vue'),
          meta: { requiresAuth: true, roles: ['ADMIN', 'STAFF'] },
        },
        {
          path: '/hoadonview/:id',
          name: 'HoaDonView',
          component: () => import('@/views/Admin/banhang/InvoiceModal.vue'),
          meta: { requiresAuth: true, roles: ['ADMIN', 'STAFF'] },
        },
        {
          path: 'ca-lam-viec',
          name: 'caLamViec',
          component: () => import('../views/Admin/CaLamViecView.vue'),
          meta: { requiresAuth: true, roles: ['ADMIN', 'STAFF'] },
        },

        // ----------------- 🔒 CHỈ DÀNH RIÊNG CHO ADMIN -----------------
        {
          path: 'nhan-vien',
          name: 'nhanVien',
          component: () => import('../views/Admin/NhanVienView.vue'),
          meta: { requiresAuth: true, roles: ['ADMIN'] },
        },
        {
          path: 'nhan-vien/add',
          name: 'addNhanVien',
          component: () => import('../components/FormNhanVienComponents.vue'),
          meta: { requiresAuth: true, roles: ['ADMIN'] },
        },
        {
          path: 'nhan-vien/edit/:id',
          name: 'editNhanVien',
          component: () => import('../components/FormNhanVienComponents.vue'),
          meta: { requiresAuth: true, roles: ['ADMIN'] },
        },
        {
          path: 'cauhinh',
          name: 'AdminCauHinh',
          component: () => import('@/views/Admin/settings/cauhinh.vue'),
          meta: { requiresAuth: true, roles: ['ADMIN'] },
        },
        {
          path: 'gamification',
          name: 'AdminGamification',
          component: () => import('../views/Admin/Layout/wards/GamificationAdminView.vue'),
          meta: { requiresAuth: true, roles: ['ADMIN'] },
        },
        {
          path: 'kho-voucher',
          name: 'AdminKhoVoucher',
          component: () => import('../views/Admin/Layout/wards/QuanLyKhoVoucher.vue'),
          meta: { requiresAuth: true, roles: ['ADMIN'] },
        },
        {
          path: 'sales',
          name: 'sales',
          component: () => import('../views/Admin/Layout/sales/dotgiamgia.vue'),
          meta: { requiresAuth: true, roles: ['ADMIN'] },
        },
        {
          path: 'voucher',
          name: 'Voucher',
          component: () => import('../views/Admin/VoucherView.vue'),
          meta: { requiresAuth: true, roles: ['ADMIN'] },
        },
        {
          path: 'spgg/:id',
          name: 'spgg',
          component: () => import('../views/Admin/Layout/sales/QuanLySanPhamTrongDot.vue'),
          meta: { requiresAuth: true, roles: ['ADMIN'] },
        },
        // Quản lý Sản phẩm & Thuộc tính & Danh mục (Admin)
        {
          path: 'sanpham',
          name: 'AdminSanPham',
          component: () => import('../views/Admin/Layout/product/lstsp.vue'),
          meta: { requiresAuth: true, roles: ['ADMIN'] },
        },
        {
          path: 'spct',
          name: 'spct',
          component: () => import('../views/Admin/Layout/product/spct.vue'),
          meta: { requiresAuth: true, roles: ['ADMIN'] },
        },
        {
          path: '/sp/:id',
          name: 'SanPham',
          component: () => import('@/views/Admin/Layout/product/spct.vue'),
          meta: { requiresAuth: true, roles: ['ADMIN'] },
        },
        {
          path: 'mausac',
          name: 'AdminMauSac',
          component: () => import('../views/Admin/Layout/catalog/colors.vue'),
          meta: { requiresAuth: true, roles: ['ADMIN'] },
        },
        {
          path: 'sizes',
          name: 'AdminSize',
          component: () => import('../views/Admin/Layout/catalog/sizes.vue'),
          meta: { requiresAuth: true, roles: ['ADMIN'] },
        },
        {
          path: 'brands',
          name: 'AdminBrand',
          component: () => import('../views/Admin/Layout/catalog/brands.vue'),
          meta: { requiresAuth: true, roles: ['ADMIN'] },
        },
        {
          path: 'materials',
          name: 'AdminMaterial',
          component: () => import('../views/Admin/Layout/catalog/materials.vue'),
          meta: { requiresAuth: true, roles: ['ADMIN'] },
        },
        {
          path: 'categories',
          name: 'categories',
          component: () => import('../views/Admin/Layout/categories/categories.vue'),
          meta: { requiresAuth: true, roles: ['ADMIN'] },
        },
      ],
    },

    // ================= 404 =================
    {
      path: '/:pathMatch(.*)*',
      redirect: '/home',
    },
  ],
})

// Navigation Guard chuẩn Vue Router 4 (Dùng return thay vì next())
router.beforeEach((to, from) => {
  const token = sessionStorage.getItem('token')
  const userStr = sessionStorage.getItem('user')
  const user = userStr ? JSON.parse(userStr) : null

  // 1. Kiểm tra nếu trang yêu cầu đăng nhập
  if (to.meta.requiresAuth) {
    if (!token || !user) {
      ElMessage.warning('Vui lòng đăng nhập để tiếp tục!')
      return { name: 'login' }
    }

    // 2. Kiểm tra phân quyền vai trò (Role-based access control)
    if (to.meta.roles && !to.meta.roles.includes(user.vaiTro)) {
      ElMessage.error('Bạn không có quyền truy cập trang này!')

      // Điều hướng về trang phù hợp với role thực tế
      if (user.vaiTro === 'USERS') {
        return { name: 'home' }
      } else {
        return { name: 'banghang' }
      }
    }
  }

  // Không cần gọi next(), chỉ cần để trống hoặc return true để cho phép đi tiếp
  return true
})

export default router
