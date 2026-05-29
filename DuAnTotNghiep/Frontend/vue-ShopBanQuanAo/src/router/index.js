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
      path: '/shop',
      name: 'shop',
      // component: () => import('../views/Users/ShopView.vue'),
    },

    {
      path: '/new-arrivals',
      name: 'newArrivals',
      // component: () => import('../views/Users/NewArrivalsView.vue'),
    },

    {
      path: '/collections',
      name: 'collections',
      // component: () => import('../views/Users/CollectionsView.vue'),
    },

    {
      path: '/about',
      name: 'about',
      // component: () => import('../views/Users/AboutView.vue'),
    },

    // ================= ADMIN =================
    {
      path: '/nhan-vien',
      name: 'nhanVien',
      component: () => import('../views/Admin/NhanVienView.vue'),
    },

    {
      path: '/nhan-vien/add',
      name: 'addNhanVien',
      component: () => import('../components/FormNhanVienComponents.vue'),
    },

    {
      path: '/nhan-vien/edit/:id',
      name: 'editNhanVien',
      component: () => import('../components/FormNhanVienComponents.vue'),
    },

    {
      path: '/khach-hang',
      name: 'khachHang',
      component: () => import('../views/Admin/KhachHangView.vue'),
    },

    {
      path: '/khach-hang/add',
      name: 'addKhachHang',
      component: () => import('../components/FormKhachHangComponents.vue'),
    },

    {
      path: '/khach-hang/edit/:id',
      name: 'editKhachHang',
      component: () => import('../components/FormKhachHangComponents.vue'),
    },

    {
      path: '/ca-lam-viec',
      name: 'caLamViec',
      component: () => import('../views/Admin/CaLamViecView.vue'),
    },
    {
      path: '/san-pham',
      name: 'sanPham',
      component: () => import('../views/Users/sanpham.vue'),
    },

    // ================= 404 =================
    {
      path: '/:pathMatch(.*)*',
      redirect: '/home',
    },
  ],
})

export default router
