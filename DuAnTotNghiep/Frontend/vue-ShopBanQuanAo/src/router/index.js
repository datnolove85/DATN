import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),

  routes: [
    // ================= USER =================
    {
      path: '/',
      redirect: '/home',
    },
    {
      path: '/home',
      name: 'home',
      component: () => import('../views/Users/home.vue'),
    },
    {
      path: '/login',
      name: 'login',
      component: () => import('../views/Users/login.vue'),
    },
    {
      path: '/san-pham',
      name: 'sanPham',
      component: () => import('../views/Users/sanpham.vue'),
    },
    {
      path: '/confirm',
      name: 'confirm',
      component: () => import('../views/Users/confirmbuy.vue'),
    },
    {
      path: '/product/:id',
      name: 'product-detail',
      component: () => import('../views/Users/confirmbuy.vue'),
    },
    {
      path: '/buy/:id',
      name: 'buy',
      component: () => import('../views/Users/buy.vue'),
    },

    // ================= QUẢN TRỊ ADMIN (GOM VÀO LAYOUT) =================
    {
      path: '/admin',
      component: () => import('../views/Admin/Layout/Layout.vue'), // File Layout tổng chứa Sidebar + Navbar
      redirect: '/admin/banhang', // Nếu vào /admin thì tự động nhảy vào trang sản phẩm
      children: [
        // Quản lý Sản phẩm
        {
          path: 'sanpham', // Đường dẫn thực tế: /admin/sanpham
          name: 'AdminSanPham',
          component: () => import('../views/Admin/Layout/product/lstsp.vue'),
        },
        {
          path: 'spct', // Đường dẫn thực tế: /admin/spct
          name: 'spct',
          component: () => import('../views/Admin/Layout/product/spct.vue'),
        },

        // ✨ QUẢN LÝ MÀU SẮC (ĐÃ THÊM VÀO ĐÂY) ✨
        {
          path: 'mausac', // Đường dẫn thực tế sẽ là: /admin/mausac
          name: 'AdminMauSac',
          component: () => import('../views/Admin/Layout/catalog/colors.vue'),
          // ⚠️ LƯU Ý: Nếu bạn lưu file colors.vue ở chỗ khác (ví dụ: views/Admin/MauSacView.vue),
          // thì bạn hãy sửa lại đường dẫn import bên trên cho chính xác nhé!
        },
        {
          path: 'sizes',
          name: 'AdminSize',
          component: () => import('../views/Admin/Layout/catalog/sizes.vue'),
        },
        {
          path: 'brands',
          name: 'AdminBrand',
          component: () => import('../views/Admin/Layout/catalog/brands.vue'),
        },
        {
          path: 'materials',
          name: 'AdminMaterial',
          component: () => import('../views/Admin/Layout/catalog/materials.vue'),
        },
        // Quản lý Nhân viên
        {
          path: 'nhan-vien', // Đường dẫn thực tế: /admin/nhan-vien
          name: 'nhanVien',
          component: () => import('../views/Admin/NhanVienView.vue'),
        },
        {
          path: 'nhan-vien/add',
          name: 'addNhanVien',
          component: () => import('../components/FormNhanVienComponents.vue'),
        },
        {
          path: 'nhan-vien/edit/:id',
          name: 'editNhanVien',
          component: () => import('../components/FormNhanVienComponents.vue'),
        },

        // Quản lý Khách hàng
        {
          path: 'khachhang', // Đường dẫn thực tế: /admin/khach-hang
          name: 'khachHang',
          component: () => import('../views/Admin/KhachHangView.vue'),
        },
        // {
        //   path: 'khach-hang/add',
        //   name: 'addKhachHang',
        //   component: () => import('../components/FormKhachHangComponents.vue'),
        // },
        {
          path: 'khach-hang/edit/:id',
          name: 'editKhachHang',
          component: () => import('../components/FormKhachHangComponents.vue'),
        },

        // Ca làm việc
        {
          path: 'ca-lam-viec', // Đường dẫn thực tế: /admin/ca-lam-viec
          name: 'caLamViec',
          component: () => import('../views/Admin/CaLamViecView.vue'),
        },
        {
          path: 'categories', // Đường dẫn thực tế: /admin/categories
          name: 'categories',
          component: () => import('../views/Admin/Layout/categories/categories.vue'),
        },
        {
          path: 'sales', // Đường dẫn thực tế: /admin/sales
          name: 'sales',
          component: () => import('../views/Admin/Layout/sales/dotgiamgia.vue'),
        },
        {
          path: 'banhang', // Đường dẫn thực tế: /admin/sales
          name: 'banghang',
          component: () => import('../views/Admin/banhang/banhang.vue'),
        },
        {
          path: 'hoadon', // Đường dẫn thực tế: /admin/sales
          name: 'hoadon',
          component: () => import('../views/Admin/banhang/hoadon.vue'),
        },
        {
          path: 'hdct', // Đường dẫn thực tế: /admin/sales
          name: 'hdct',
          component: () => import('../views/Admin/banhang/hdct.vue'),
        },
        {
          path: 'hdct/:id', // Đường dẫn thực tế: /admin/sales
          name: 'HoaDonChiTiet',
          component: () => import('../views/Admin/banhang/hdct.vue'),
        },
        {
          path: '/hoadonview/:id',
          name: 'HoaDonView',
          component: () => import('@/views/Admin/banhang/InvoiceModal.vue'),
        },
        {
          path: 'voucher',
          name: 'Voucher',
          component: () => import('../views/Admin/VoucherView.vue'),
        },
        {
          path: '/sp/:id',
          name: 'SanPham',
          component: () => import('@/views/Admin/Layout/product/spct.vue'),
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

export default router
