
CREATE DATABASE datnolove;
GO

USE datnolove;
GO

CREATE TABLE vai_tro (
    id INT IDENTITY(1,1) PRIMARY KEY,
    ma_vai_tro NVARCHAR(50) NOT NULL,
    ten_vai_tro NVARCHAR(100) NOT NULL,
    trang_thai INT
);


CREATE TABLE danh_muc (
    id INT IDENTITY(1,1) PRIMARY KEY,
    ma_danh_muc VARCHAR(20) NOT NULL UNIQUE,
    ten_danh_muc NVARCHAR(100) NOT NULL,
    ngay_tao DATETIME DEFAULT GETDATE(),
    ngay_cap_nhat DATETIME DEFAULT GETDATE(),
    trang_thai BIT DEFAULT 1
);
GO


CREATE TABLE thuong_hieu (
    id INT IDENTITY(1,1) PRIMARY KEY,
    ma_thuong_hieu VARCHAR(20) NOT NULL UNIQUE,
    ten_thuong_hieu NVARCHAR(100) NOT NULL,
    ngay_tao DATETIME DEFAULT GETDATE(),
    ngay_cap_nhat DATETIME DEFAULT GETDATE(),
    trang_thai BIT DEFAULT 1
);
GO


CREATE TABLE chat_lieu (
    id INT IDENTITY(1,1) PRIMARY KEY,
    ma_chat_lieu VARCHAR(20) NOT NULL UNIQUE,
    ten_chat_lieu NVARCHAR(100) NOT NULL,
    ngay_tao DATETIME DEFAULT GETDATE(),
    ngay_cap_nhat DATETIME DEFAULT GETDATE(),
    trang_thai BIT DEFAULT 1
);
GO


CREATE TABLE mau_sac (
    id INT IDENTITY(1,1) PRIMARY KEY,
    ma_mau_sac VARCHAR(20) NOT NULL UNIQUE,
    ten_mau_sac NVARCHAR(100) NOT NULL,
    ngay_tao DATETIME DEFAULT GETDATE(),
    ngay_cap_nhat DATETIME DEFAULT GETDATE(),
    trang_thai BIT DEFAULT 1
);
GO


CREATE TABLE kich_thuoc (
    id INT IDENTITY(1,1) PRIMARY KEY,
    ma_kich_thuoc VARCHAR(20) NOT NULL UNIQUE,
    ten_kich_thuoc NVARCHAR(50) NOT NULL,
    ngay_tao DATETIME DEFAULT GETDATE(),
    ngay_cap_nhat DATETIME DEFAULT GETDATE(),
    trang_thai BIT DEFAULT 1
);
GO


CREATE TABLE voucher (
    id INT IDENTITY(1,1) PRIMARY KEY,
    ma_voucher VARCHAR(30) NOT NULL UNIQUE,
    ten_voucher NVARCHAR(200) NOT NULL,
    loai_giam_gia VARCHAR(20) NOT NULL CHECK (loai_giam_gia IN ('phan_tram', 'tien_mat')),
    gia_tri_giam DECIMAL(18,2) NOT NULL CHECK (gia_tri_giam >= 0),
    gia_tri_don_hang_toi_thieu DECIMAL(18,2) DEFAULT 0,
    gia_tri_giam_toi_da DECIMAL(18,2),
    so_luong INT DEFAULT 0 CHECK (so_luong >= 0),
    so_luong_da_dung INT DEFAULT 0 CHECK (so_luong_da_dung >= 0),
    ngay_bat_dau DATETIME,
    ngay_ket_thuc DATETIME,
    mo_ta NVARCHAR(MAX),
    ngay_tao DATETIME DEFAULT GETDATE(),
    ngay_cap_nhat DATETIME DEFAULT GETDATE(),
    trang_thai BIT DEFAULT 1
);
GO


CREATE TABLE phuong_thuc_thanh_toan (
    id INT IDENTITY(1,1) PRIMARY KEY,
    ma_phuong_thuc VARCHAR(20) NOT NULL UNIQUE,
    ten_phuong_thuc NVARCHAR(100) NOT NULL,
    trang_thai BIT DEFAULT 1
);


CREATE TABLE dot_giam_gia (
    id INT IDENTITY(1,1) PRIMARY KEY,
    ma_dot_giam_gia VARCHAR(30) NOT NULL UNIQUE,
    ten_dot_giam_gia NVARCHAR(200) NOT NULL,
    loai_giam_gia VARCHAR(20) NOT NULL CHECK (loai_giam_gia IN ('phan_tram', 'tien_mat')),
    gia_tri_giam DECIMAL(18,2) NOT NULL CHECK (gia_tri_giam >= 0),
    gia_tri_giam_toi_da DECIMAL(18,2),
    ngay_bat_dau DATETIME NOT NULL,
    ngay_ket_thuc DATETIME NOT NULL,
    mo_ta NVARCHAR(MAX),
    ngay_tao DATETIME DEFAULT GETDATE(),
    ngay_cap_nhat DATETIME DEFAULT GETDATE(),
    trang_thai VARCHAR(20) NOT NULL CHECK (trang_thai IN ('sap_dien_ra', 'dang_dien_ra', 'da_ket_thuc', 'tam_dung'))
);


CREATE TABLE voucher_minigame (
    id INT IDENTITY(1,1) PRIMARY KEY,
    ten_voucher NVARCHAR(255) NOT NULL,
    ma_code VARCHAR(50) UNIQUE NOT NULL,
    
    -- Bổ sung loại giảm giá ('tien_mat' hoặc 'phan_tram')
    loai_giam_gia VARCHAR(20) NOT NULL DEFAULT 'tien_mat', 
    
    gia_tri_giam DECIMAL(18,2) NOT NULL,
    
    -- Bổ sung mức giảm tối đa (Dùng khi giảm theo %, nếu giảm tiền mặt có thể để NULL)
    gia_tri_giam_toi_da DECIMAL(18,2) NULL, 
    
    dieu_kien_toi_thieu DECIMAL(18,2) DEFAULT 0,
    so_xu_doi INT NOT NULL,
    so_luong_con_lai INT NOT NULL,
    
    -- Bổ sung thời gian bắt đầu hiệu lực
    ngay_bat_dau DATETIME NULL, 
    ngay_het_han DATETIME NOT NULL,
    
    -- Bổ sung mô tả chi tiết
    mo_ta NVARCHAR(MAX) NULL, 
    
    -- Bổ sung thời gian quản lý hệ thống
    ngay_tao DATETIME DEFAULT GETDATE(),
    ngay_cap_nhat DATETIME DEFAULT GETDATE(),
    
    trang_thai BIT DEFAULT 1 -- 1: Đang hoạt động, 0: Ngừng/Hết hạn
);
go


CREATE TABLE tai_khoan (
    id INT IDENTITY(1,1) PRIMARY KEY,
    id_vai_tro INT NOT NULL,
    ten_tai_khoan VARCHAR(100),
    email NVARCHAR(150) NOT NULL UNIQUE,
    so_dien_thoai NVARCHAR(20) NOT NULL UNIQUE,
    mat_khau NVARCHAR(255) NOT NULL,
    ngay_tao DATETIME DEFAULT GETDATE(),
    ngay_cap_nhat DATETIME,
    nguoi_tao NVARCHAR(100),
    nguoi_cap_nhat NVARCHAR(100),
    trang_thai INT,
    CONSTRAINT FK_tai_khoan_vai_tro FOREIGN KEY (id_vai_tro) REFERENCES vai_tro(id)
);


CREATE TABLE nhan_vien (
    id INT IDENTITY(1,1) PRIMARY KEY,
    id_tai_khoan INT NOT NULL UNIQUE,
    ma_nhan_vien NVARCHAR(50) NOT NULL,
    ten_nhan_vien NVARCHAR(150) NOT NULL,
    dia_chi NVARCHAR(255),
    ngay_sinh DATE,
    gioi_tinh BIT,
    can_cuoc_cong_dan NVARCHAR(20) UNIQUE,
    anh NVARCHAR(255),
    ngay_tao DATETIME DEFAULT GETDATE(),
    ngay_cap_nhat DATETIME,
    nguoi_tao NVARCHAR(100),
    nguoi_cap_nhat NVARCHAR(100),
    trang_thai INT,
    CONSTRAINT FK_nhan_vien_tai_khoan FOREIGN KEY (id_tai_khoan) REFERENCES tai_khoan(id)
);


CREATE TABLE khach_hang (
    id INT IDENTITY(1,1) PRIMARY KEY,
    id_tai_khoan INT UNIQUE,
    ma_khach_hang NVARCHAR(50) NULL,
    ho_ten NVARCHAR(150) NULL,
    so_dien_thoai NVARCHAR(20) NULL UNIQUE,
    ngay_sinh DATE,
    hang_thanh_vien NVARCHAR(50),
    dia_chi NVARCHAR(255),
    so_lan_mua INT DEFAULT 0,
    tong_chi_tieu DECIMAL(18,2) DEFAULT 0, -- CỘT MỚI ĐÃ THÊM
	ngay_bat_dau_chu_ky DATETIME NULL, -- Ngày bắt đầu chu kỳ tính hạng hiện tại
    ngay_het_han_hang DATETIME NULL,
    ngay_mua_cuoi DATETIME,
    anh NVARCHAR(255),
    gioi_tinh BIT,
    
    -- Quản lý xu & điểm danh chuỗi
    so_du_xu INT DEFAULT 0 CHECK (so_du_xu >= 0),
    chuoi_diem_danh INT DEFAULT 0 CHECK (chuoi_diem_danh >= 0),
    ngay_diem_danh_gan_nhat DATE NULL,

	so_luot_lat_the INT DEFAULT 3 CHECK (so_luot_lat_the >= 0),
    
    ngay_tao DATETIME DEFAULT GETDATE(),
    ngay_cap_nhat DATETIME,
    nguoi_tao NVARCHAR(100),
    nguoi_cap_nhat NVARCHAR(100),
    trang_thai_xoa BIT DEFAULT 0,
    CONSTRAINT FK_khach_hang_tai_khoan FOREIGN KEY (id_tai_khoan) REFERENCES tai_khoan(id)
);
GO

-- Lệnh chạy thêm cột nếu bảng đã tồn tại
-- Sửa lại bỏ từ 'COLUMN' đi thế này cho SQL Server:

-- Bảng cấu hình hạng thành viên
CREATE TABLE cau_hinh_hang_thanh_vien (
    id INT IDENTITY(1,1) PRIMARY KEY,
    ten_hang NVARCHAR(50) NOT NULL,
    nguong_chi_tieu_toi_thieu DECIMAL(18,2) DEFAULT 0,
    ty_le_tich_xu FLOAT DEFAULT 1.0, -- Ví dụ: 1.0 là tỷ lệ chuẩn, 1.5 là tích nhanh hơn
    trang_thai BIT DEFAULT 1
);
GO

-- Bảng cấu hình chung
CREATE TABLE cau_hinh_he_thong (
    id INT IDENTITY(1,1) PRIMARY KEY,
    ma_cau_hinh VARCHAR(50) UNIQUE,
    gia_tri_so DECIMAL(18,2),
    mo_ta NVARCHAR(255)
);
GO

-- Seed dữ liệu mẫu cho cấu hình
INSERT INTO cau_hinh_hang_thanh_vien (ten_hang, nguong_chi_tieu_toi_thieu, ty_le_tich_xu) VALUES
(N'Đồng', 0, 1.0),
(N'Bạc', 2000000, 1.2),
(N'Vàng', 5000000, 1.5);

INSERT INTO cau_hinh_he_thong (ma_cau_hinh, gia_tri_so, mo_ta) VALUES
('TI_LE_TICH_XU', 10000, N'Cứ 10.000đ mua hàng được 1 xu'),
('TY_LE_GIAM_TOI_DA_XU', 50, N'Tối đa được dùng xu thanh toán 50% giá trị đơn hàng'),
('TY_LE_QUY_DOI_XU', 10000, N'1 xu được quy đổi thành 10.000đ khi thanh toán'),
('CHU_KY_XET_HANG', '12', N'Số tháng tính chu kỳ xét hạng thành viên');



GO

CREATE TABLE dia_chi_khach_hang (
    id INT IDENTITY(1,1) PRIMARY KEY,
    ma_dia_chi VARCHAR(50),
    ten_nguoi_nhan NVARCHAR(255),
    so_dien_thoai VARCHAR(15),
    thanh_pho NVARCHAR(100),
    quan NVARCHAR(100),
    phuong NVARCHAR(100),
    district_id INT,
    ward_code VARCHAR(20),
    latitude DECIMAL(10,7),
    longitude DECIMAL(10,7),
    dia_chi_cu_the NVARCHAR(MAX),
    mac_dinh BIT DEFAULT 0,
    trang_thai INT DEFAULT 1,
    id_khach_hang INT NOT NULL,
    CONSTRAINT FK_dia_chi_khach_hang FOREIGN KEY (id_khach_hang) REFERENCES khach_hang(id)
);


CREATE TABLE lich_su_xu (
    id INT IDENTITY(1,1) PRIMARY KEY,
    id_khach_hang INT NOT NULL,
    so_xu_thay_doi INT NOT NULL, 
    so_du_truoc INT NOT NULL,
    so_du_sau INT NOT NULL,
    loai_giao_dich VARCHAR(50) NOT NULL,
    mo_ta NVARCHAR(255),
    ngay_tao DATETIME DEFAULT GETDATE(),
    CONSTRAINT fk_lich_su_xu_khach_hang FOREIGN KEY (id_khach_hang) REFERENCES khach_hang(id)
);
GO


CREATE TABLE diem_danh_hang_ngay (
    id INT IDENTITY(1,1) PRIMARY KEY,
    id_khach_hang INT NOT NULL,
    ngay_diem_danh DATE NOT NULL,
    so_xu_nhan INT NOT NULL,
    so_ngay_lien_tiep INT NOT NULL,
    ngay_tao DATETIME DEFAULT GETDATE(),
    CONSTRAINT fk_danh_danh_khach_hang FOREIGN KEY (id_khach_hang) REFERENCES khach_hang(id),
    CONSTRAINT uq_khach_hang_ngay UNIQUE (id_khach_hang, ngay_diem_danh)
);


CREATE TABLE cau_hinh_diem_danh (
    id INT IDENTITY(1,1) PRIMARY KEY,
    ngay_thu INT NOT NULL UNIQUE,      -- Ngày thứ mấy trong chuỗi (1, 2, 3,...)
    so_xu_thuong INT NOT NULL,         -- Số xu thưởng tương ứng cho ngày đó
    trang_thai BIT DEFAULT 1           -- Trạng thái (1: Hoạt động, 0: Tạm khóa)
);


CREATE TABLE san_pham (
    id INT IDENTITY(1,1) PRIMARY KEY,
    id_danh_muc INT NOT NULL,
    id_thuong_hieu INT NOT NULL,
    id_chat_lieu INT NOT NULL,
    ma_san_pham VARCHAR(20) UNIQUE,
    ten_san_pham NVARCHAR(200) NOT NULL,
    mo_ta NVARCHAR(MAX),
    ngay_tao DATETIME DEFAULT GETDATE(),
    ngay_cap_nhat DATETIME DEFAULT GETDATE(),
    id_nhan_vien_tao INT,
    id_nhan_vien_cap_nhat INT,
    trang_thai BIT DEFAULT 1,
    CONSTRAINT fk_san_pham_danh_muc FOREIGN KEY (id_danh_muc) REFERENCES danh_muc(id),
    CONSTRAINT fk_san_pham_thuong_hieu FOREIGN KEY (id_thuong_hieu) REFERENCES thuong_hieu(id),
    CONSTRAINT fk_san_pham_chat_lieu FOREIGN KEY (id_chat_lieu) REFERENCES chat_lieu(id),
    CONSTRAINT fk_san_pham_nv_tao FOREIGN KEY (id_nhan_vien_tao) REFERENCES nhan_vien(id),
    CONSTRAINT fk_san_pham_nv_cap_nhat FOREIGN KEY (id_nhan_vien_cap_nhat) REFERENCES nhan_vien(id)
);
GO

CREATE TABLE san_pham_chi_tiet (
    id INT IDENTITY(1,1) PRIMARY KEY,
    id_san_pham INT NOT NULL,
    id_mau_sac INT NOT NULL,
    id_kich_thuoc INT NOT NULL,
    ma_san_pham_chi_tiet VARCHAR(200) NOT NULL UNIQUE,
    ten_san_pham_chi_tiet NVARCHAR(200),
    gia_nhap DECIMAL(18,2) NOT NULL CHECK (gia_nhap >= 0),
    gia_ban DECIMAL(18,2) NOT NULL CHECK (gia_ban >= 0),
    so_luong_ton INT NOT NULL DEFAULT 0 CHECK (so_luong_ton >= 0),
    so_luong_tam_giu INT NOT NULL DEFAULT 0 CHECK (so_luong_tam_giu >= 0),
    so_luong_kha_dung AS (so_luong_ton - so_luong_tam_giu),
    ngay_tao DATETIME DEFAULT GETDATE(),
    ngay_cap_nhat DATETIME DEFAULT GETDATE(),
    trang_thai BIT DEFAULT 1,
    CONSTRAINT fk_spct_san_pham FOREIGN KEY (id_san_pham) REFERENCES san_pham(id),
    CONSTRAINT fk_spct_mau_sac FOREIGN KEY (id_mau_sac) REFERENCES mau_sac(id),
    CONSTRAINT fk_spct_kich_thuoc FOREIGN KEY (id_kich_thuoc) REFERENCES kich_thuoc(id)
);


CREATE TABLE hinh_anh (
    id INT IDENTITY(1,1) PRIMARY KEY,
    id_san_pham_chi_tiet INT NOT NULL,
    ten_anh NVARCHAR(100),
    link NVARCHAR(500) NOT NULL,
    dinh_dang VARCHAR(20),
    la_anh_chinh BIT DEFAULT 0,
    trang_thai BIT DEFAULT 1,
    CONSTRAINT fk_hinh_anh_spct FOREIGN KEY (id_san_pham_chi_tiet) REFERENCES san_pham_chi_tiet(id)
);


CREATE TABLE gio_hang (
    id INT IDENTITY(1,1) PRIMARY KEY,
    id_khach_hang INT NOT NULL UNIQUE,
    ngay_tao DATETIME DEFAULT GETDATE(),
    ngay_cap_nhat DATETIME DEFAULT GETDATE(),
    trang_thai BIT DEFAULT 1,
    CONSTRAINT fk_gio_hang_khach_hang FOREIGN KEY(id_khach_hang) REFERENCES khach_hang(id)
);


CREATE TABLE gio_hang_chi_tiet (
    id INT IDENTITY(1,1) PRIMARY KEY,
    id_gio_hang INT NOT NULL,
    id_san_pham_chi_tiet INT NOT NULL,
    so_luong INT NOT NULL CHECK(so_luong > 0),
    duoc_chon BIT DEFAULT 1,
    ngay_them DATETIME DEFAULT GETDATE(),
    ngay_cap_nhat DATETIME DEFAULT GETDATE(),
    CONSTRAINT fk_ghct_gio_hang FOREIGN KEY(id_gio_hang) REFERENCES gio_hang(id),
    CONSTRAINT fk_ghct_spct FOREIGN KEY(id_san_pham_chi_tiet) REFERENCES san_pham_chi_tiet(id),
    CONSTRAINT uq_ghct UNIQUE(id_gio_hang, id_san_pham_chi_tiet)
);


CREATE TABLE hoa_don (
    id INT IDENTITY(1,1) PRIMARY KEY,
    id_khach_hang INT,
    id_nhan_vien INT,
    ma_hoa_don VARCHAR(30) NOT NULL UNIQUE,
    tong_tien_hang DECIMAL(18,2) DEFAULT 0,
    tong_giam_gia DECIMAL(18,2) DEFAULT 0,
    phi_van_chuyen DECIMAL(18,2) DEFAULT 0,
    tong_thanh_toan DECIMAL(18,2) DEFAULT 0,
    
    -- TÍCH HỢP: Lưu thông tin dùng xu thanh toán đơn hàng
    so_xu_su_dung INT DEFAULT 0,
    tien_giam_do_xu DECIMAL(18,2) DEFAULT 0,
    
    -- BỔ SUNG MỚI: Quản lý tiền mặt khách đưa & thối lại
    tien_khach_dua DECIMAL(18,2) DEFAULT 0,
    tien_thoi_lai DECIMAL(18,2) DEFAULT 0,
    
    ten_nguoi_nhan NVARCHAR(100),
    so_dien_thoai_nguoi_nhan VARCHAR(15),
    dia_chi_giao_hang NVARCHAR(500),
    loai_hoa_don VARCHAR(20) CHECK (loai_hoa_don IN ('online', 'tai_quay')),
    trang_thai VARCHAR(30) CHECK (trang_thai IN ('cho_xac_nhan', 'da_xac_nhan', 'cho_van_chuyen', 'dang_giao', 'giao_thanh_cong', 'giao_that_bai', 'hoan_thanh', 'da_huy')),
    trang_thai_thanh_toan VARCHAR(30) DEFAULT 'chua_thanh_toan' CHECK (trang_thai_thanh_toan IN ('chua_thanh_toan', 'da_thanh_toan', 'hoan_tien')),
    ghi_chu NVARCHAR(MAX),
    ngay_tao DATETIME DEFAULT GETDATE(),
    ngay_cap_nhat DATETIME DEFAULT GETDATE(),
    CONSTRAINT fk_hoa_don_khach_hang FOREIGN KEY (id_khach_hang) REFERENCES khach_hang(id),
    CONSTRAINT fk_hoa_don_nhan_vien FOREIGN KEY (id_nhan_vien) REFERENCES nhan_vien(id)
);


-- =========================================
-- LICH SU HOA DON
-- Lưu lịch sử thay đổi trạng thái của hóa đơn
-- =========================================

-- 2. Tạo lại bảng lịch sử hóa đơn tối ưu cho cả Online & Offline
CREATE TABLE lich_su_hoa_don (
    id INT IDENTITY(1,1) PRIMARY KEY,
    id_hoa_don INT NOT NULL,
    trang_thai_cu VARCHAR(30) NULL,
    trang_thai_moi VARCHAR(30) NOT NULL,
    thoi_gian DATETIME NOT NULL DEFAULT GETDATE(),
    
    -- Mở rộng các trường định danh người/hệ thống thực hiện hành động
    id_nhan_vien INT NULL,          -- Nhân viên thao tác (nếu nhân viên đổi trạng thái/xác nhận)
    id_khach_hang INT NULL,         -- Khách hàng thao tác (nếu khách tự hủy đơn online)
    nguon_thao_tac VARCHAR(50) DEFAULT 'SYSTEM', -- Phân loại nguồn: 'STAFF', 'CUSTOMER', 'SYSTEM'
    
    ghi_chu NVARCHAR(500) NULL,     -- Lý do hủy đơn, ghi chú đóng gói, v.v.
    
    -- Ràng buộc khóa ngoại
    CONSTRAINT fk_lich_su_hoa_don_hoa_don
        FOREIGN KEY (id_hoa_don) REFERENCES hoa_don(id) ON DELETE CASCADE,
    CONSTRAINT fk_lich_su_hoa_don_nhan_vien
        FOREIGN KEY (id_nhan_vien) REFERENCES nhan_vien(id),
    CONSTRAINT fk_lich_su_hoa_don_khach_hang
        FOREIGN KEY (id_khach_hang) REFERENCES khach_hang(id)
);
GO


CREATE TABLE hoa_don_chi_tiet (
    id INT IDENTITY(1,1) PRIMARY KEY,
    id_hoa_don INT NOT NULL,
    id_san_pham_chi_tiet INT NOT NULL,
    so_luong INT NOT NULL CHECK (so_luong > 0),
    gia_nhap DECIMAL(18,2) NOT NULL CHECK (gia_nhap >= 0),
    don_gia DECIMAL(18,2) NOT NULL CHECK (don_gia >= 0),
    thanh_tien DECIMAL(18,2) NOT NULL CHECK (thanh_tien >= 0),
    CONSTRAINT fk_hdct_hoa_don FOREIGN KEY (id_hoa_don) REFERENCES hoa_don(id),
    CONSTRAINT fk_hdct_spct FOREIGN KEY (id_san_pham_chi_tiet) REFERENCES san_pham_chi_tiet(id)
);


CREATE TABLE thanh_toan (
    id INT IDENTITY(1,1) PRIMARY KEY,
    id_hoa_don INT NOT NULL,
    id_phuong_thuc_thanh_toan INT NOT NULL,
    ma_giao_dich VARCHAR(100),
    so_tien DECIMAL(18,2) NOT NULL CHECK (so_tien >= 0),
    trang_thai VARCHAR(30) CHECK (trang_thai IN ('cho_thanh_toan', 'da_thanh_toan', 'that_bai', 'hoan_tien')),
    ngay_thanh_toan DATETIME,
    CONSTRAINT fk_thanh_toan_hoa_don FOREIGN KEY (id_hoa_don) REFERENCES hoa_don(id),
    CONSTRAINT fk_thanh_toan_phuong_thuc FOREIGN KEY (id_phuong_thuc_thanh_toan) REFERENCES phuong_thuc_thanh_toan(id)
);


CREATE TABLE voucher_cua_khach_hang (
    id INT IDENTITY(1,1) PRIMARY KEY,
    id_khach_hang INT NOT NULL,
    id_voucher_minigame INT NOT NULL,
    trang_thai VARCHAR(50) DEFAULT 'CHUA_DUNG',
    ngay_doi DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (id_voucher_minigame) REFERENCES voucher_minigame(id)
);


CREATE TABLE hoa_don_voucher (
    id INT IDENTITY(1,1) PRIMARY KEY,

    id_hoa_don INT NOT NULL,

    id_voucher INT NULL,                -- Voucher hệ thống

    id_voucher_minigame INT NULL,            -- Kho voucher (để tính giảm giá)

    id_voucher_khach_hang INT NULL,     -- Voucher cụ thể của khách

    so_tien_giam DECIMAL(18,2) NOT NULL
        CHECK (so_tien_giam >= 0),

    da_consume BIT NOT NULL DEFAULT 0,  -- Đã chính thức sử dụng voucher hay chưa

    CONSTRAINT fk_hdv_hoa_don
        FOREIGN KEY (id_hoa_don)
        REFERENCES hoa_don(id),

    CONSTRAINT fk_hdv_voucher
        FOREIGN KEY (id_voucher)
        REFERENCES voucher(id),

    CONSTRAINT fk_hdv_voucher_minigame
        FOREIGN KEY (id_voucher_minigame)
        REFERENCES voucher_minigame(id),

    CONSTRAINT fk_hdv_voucher_khach_hang
        FOREIGN KEY (id_voucher_khach_hang)
        REFERENCES voucher_cua_khach_hang(id)
);


CREATE TABLE san_pham_giam_gia (
    id INT IDENTITY(1,1) PRIMARY KEY,
    id_dot_giam_gia INT NOT NULL,
    id_san_pham_chi_tiet INT NOT NULL,
    CONSTRAINT FK_SPGG_DOT_GIAM_GIA FOREIGN KEY(id_dot_giam_gia) REFERENCES dot_giam_gia(id),
    CONSTRAINT FK_SPGG_SAN_PHAM_CHI_TIET FOREIGN KEY(id_san_pham_chi_tiet) REFERENCES san_pham_chi_tiet(id)
);


CREATE TABLE phan_thuong_minigame (
    id INT IDENTITY(1,1) PRIMARY KEY,
    ten_phan_thuong NVARCHAR(150) NOT NULL,
    loai_game VARCHAR(30) NOT NULL CHECK (loai_game IN ('vong_quay', 'lat_the')), 
    loai_phan_thuong VARCHAR(30) NOT NULL CHECK (loai_phan_thuong IN ('xu', 'voucher', 'khong_trung', 'vat_pham')),
    gia_tri_xu INT DEFAULT 0, 
    id_voucher INT NULL, -- Mã voucher trúng (liên kết bảng kho_voucher)
    ty_le_trung DECIMAL(5,2) NOT NULL, 
    so_luong_gioi_han INT DEFAULT -1, 
    so_luong_da_trung INT DEFAULT 0,
    anh NVARCHAR(255),
    trang_thai BIT DEFAULT 1,
    
    -- SỬA THÀNH KHO_VOUCHER Ở ĐÂY 👇
    CONSTRAINT fk_minigame_voucher FOREIGN KEY (id_voucher) REFERENCES voucher_minigame(id)
);


CREATE TABLE lich_su_minigame (
    id INT IDENTITY(1,1) PRIMARY KEY,
    id_khach_hang INT NOT NULL,
    id_phan_thuong INT NOT NULL,
    loai_game VARCHAR(30) NOT NULL CHECK (loai_game IN ('vong_quay', 'lat_the')),
    ngay_tao DATETIME DEFAULT GETDATE(),
    trang_thai_nhan VARCHAR(30) DEFAULT 'da_nhan' CHECK (trang_thai_nhan IN ('chua_nhan', 'da_nhan', 'het_han')),
    CONSTRAINT fk_ls_minigame_khach FOREIGN KEY (id_khach_hang) REFERENCES khach_hang(id),
    CONSTRAINT fk_ls_minigame_thuong FOREIGN KEY (id_phan_thuong) REFERENCES phan_thuong_minigame(id)
);
GO

CREATE TABLE yeu_thich (
    id INT IDENTITY(1,1) PRIMARY KEY,
    id_khach_hang INT NOT NULL,
    id_san_pham INT NOT NULL,
    ngay_tao DATETIME DEFAULT GETDATE(),
    
    -- Khóa ngoại liên kết với bảng khách hàng và sản phẩm hiện tại
    CONSTRAINT fk_yeu_thich_khach_hang FOREIGN KEY (id_khach_hang) REFERENCES khach_hang(id) ON DELETE CASCADE,
    CONSTRAINT fk_yeu_thich_san_pham FOREIGN KEY (id_san_pham) REFERENCES san_pham(id) ON DELETE CASCADE,
    
    -- Đảm bảo 1 khách hàng không thể thích trùng 1 sản phẩm nhiều lần
    CONSTRAINT uq_khach_hang_san_pham UNIQUE (id_khach_hang, id_san_pham)
);
GO


-- =========================================
-- PHẦN INSERT DỮ LIỆU MẪU (SEED DATA)
-- =========================================

-- =========================================
-- 0. Cấu hình điểm danh
-- =========================================
INSERT INTO cau_hinh_diem_danh (ngay_thu, so_xu_thuong, trang_thai) VALUES
(1, 10, 1),
(2, 15, 1),
(3, 20, 1),
(4, 25, 1),
(5, 30, 1),
(6, 40, 1),
(7, 50, 1);
GO

-- 1. Vai trò
INSERT INTO vai_tro (ma_vai_tro, ten_vai_tro, trang_thai) VALUES
('ADMIN', N'ADMIN', 1),
('NV', N'STAFF', 1),
('KH', N'USERS', 1);
GO

-- 2. Danh mục
INSERT INTO danh_muc (ma_danh_muc, ten_danh_muc, trang_thai) VALUES
('DM01', N'Áo Thun', 1),
('DM02', N'Áo Sơ Mi', 1),
('DM03', N'Quần Jeans', 1);
GO

-- 3. Thương hiệu
INSERT INTO thuong_hieu (ma_thuong_hieu, ten_thuong_hieu, trang_thai) VALUES
('TH01', N'Nike', 1),
('TH02', N'Adidas', 1),
('TH03', N'Uniqlo', 1);
GO

-- 4. Chất liệu
INSERT INTO chat_lieu (ma_chat_lieu, ten_chat_lieu, trang_thai) VALUES
('CL01', N'Cotton 100%', 1),
('CL02', N'Kaki', 1),
('CL03', N'Denim', 1);
GO

-- 5. Màu sắc
INSERT INTO mau_sac (ma_mau_sac, ten_mau_sac, trang_thai) VALUES
('MS01', N'Đen', 1),
('MS02', N'Trắng', 1),
('MS03', N'Xanh Navy', 1);
GO

-- 6. Kích thước
INSERT INTO kich_thuoc (ma_kich_thuoc, ten_kich_thuoc, trang_thai) VALUES
('SZ_S', N'S', 1),
('SZ_M', N'M', 1),
('SZ_L', N'L', 1),
('SZ_XL', N'XL', 1);
GO

-- 7. Phương thức thanh toán
INSERT INTO phuong_thuc_thanh_toan
(ma_phuong_thuc, ten_phuong_thuc)
VALUES
('CASH',   N'Tiền mặt'),
('COD',    N'Thanh toán khi nhận hàng'),
('BANK',   N'Chuyển khoản VietQR'),
('VNPAY',  N'VNPay'),
('MOMO',   N'Ví điện tử MoMo'),
('ZALOPAY',N'Ví điện tử ZaloPay');
INSERT INTO phuong_thuc_thanh_toan
(ma_phuong_thuc, ten_phuong_thuc)
VALUES
('MIXED', N'Thanh toán kết hợp');
GO


-- 8. Tài khoản (id_vai_tro: 1=Admin, 2=Nhân viên, 3=Khách hàng)
INSERT INTO tai_khoan (id_vai_tro, ten_tai_khoan, email, so_dien_thoai, mat_khau, trang_thai, nguoi_tao) VALUES
(1, 'admin01', 'admin@hazz.com', '0901111111', '123456', 1, 'System'),
(2, 'nhanvien01', 'staff01@hazz.com', '0902222222', '123456', 1, 'System'),
(3, 'khachhang01', 'customer01@hazz.com', '0903333333', '123456', 1, 'System');
GO

-- 9. Nhân viên (id_tai_khoan tương ứng 1 và 2)
INSERT INTO nhan_vien (id_tai_khoan, ma_nhan_vien, ten_nhan_vien, dia_chi, ngay_sinh, gioi_tinh, can_cuoc_cong_dan, trang_thai, nguoi_tao) VALUES
(1, 'NV01', N'Nguyễn Văn Quản Trị', N'Hà Nội', '1995-01-01', 1, '001195000001', 1, 'System'),
(2, 'NV02', N'Trần Thị Nhân Viên', N'Hồ Chí Minh', '1998-05-12', 0, '001198000002', 1, 'System');
GO

-- 10. Khách hàng (id_tai_khoan = 3, có sẵn 50 xu và chuỗi điểm danh = 3 ngày)
INSERT INTO khach_hang (id_tai_khoan, ma_khach_hang, ho_ten, so_dien_thoai, ngay_sinh, hang_thanh_vien, dia_chi, so_du_xu, chuoi_diem_danh, ngay_diem_danh_gan_nhat, gioi_tinh, trang_thai_xoa) VALUES
(3, 'KH01', N'Lê Văn Khách Hàng', '0903333333', '2000-10-20', N'Thân thiết', N'Đà Nẵng', 50, 3, GETDATE(), 1, 0);
GO

-- 10.1. Voucher minigame
INSERT INTO voucher_minigame
(
    ten_voucher,
    ma_code,
    loai_giam_gia,
    gia_tri_giam,
    gia_tri_giam_toi_da,
    dieu_kien_toi_thieu,
    so_xu_doi,
    so_luong_con_lai,
    ngay_bat_dau,
    ngay_het_han,
    mo_ta,
    ngay_tao,
    ngay_cap_nhat,
    trang_thai
)
VALUES
(N'Giảm 20.000đ', 'MG20K', 'tien_mat', 20000, NULL, 100000, 100, 100,
 '2026-01-01 00:00:00', '2026-12-31 23:59:59',
 N'Voucher giảm 20.000đ cho đơn từ 100.000đ', GETDATE(), GETDATE(), 1),
(N'Giảm 50.000đ', 'MG50K', 'tien_mat', 50000, NULL, 250000, 250, 100,
 '2026-01-01 00:00:00', '2026-12-31 23:59:59',
 N'Voucher giảm 50.000đ cho đơn từ 250.000đ', GETDATE(), GETDATE(), 1),
(N'Giảm 10%', 'MG10P', 'phan_tram', 10, 50000, 200000, 300, 100,
 '2026-01-01 00:00:00', '2026-12-31 23:59:59',
 N'Voucher giảm 10%, tối đa 50.000đ', GETDATE(), GETDATE(), 1),
(N'Giảm 15%', 'MG15P', 'phan_tram', 15, 75000, 300000, 500, 50,
 '2026-01-01 00:00:00', '2026-12-31 23:59:59',
 N'Voucher giảm 15%, tối đa 75.000đ', GETDATE(), GETDATE(), 1),
(N'Giảm 100.000đ', 'MG100K', 'tien_mat', 100000, NULL, 500000, 800, 30,
 '2026-01-01 00:00:00', '2026-12-31 23:59:59',
 N'Voucher đặc biệt giảm 100.000đ', GETDATE(), GETDATE(), 1);
GO

-- 10.2. Voucher của khách hàng
INSERT INTO voucher_cua_khach_hang
(id_khach_hang, id_voucher_minigame, trang_thai, ngay_doi)
VALUES
(1, 1, 'CHUA_DUNG', GETDATE()),
(1, 2, 'CHUA_DUNG', GETDATE()),
(1, 3, 'DA_DUNG', DATEADD(DAY, -2, GETDATE())),
(1, 4, 'CHUA_DUNG', GETDATE()),
(1, 5, 'CHUA_DUNG', GETDATE());
GO

-- 10.3. Hóa đơn voucher
-- Chưa seed vì file gốc không có dữ liệu hóa đơn mẫu.
-- Khi có hóa đơn, id_voucher_minigame sẽ tham chiếu voucher_minigame(id).

-- 11. Sản phẩm (id_danh_muc, id_thuong_hieu, id_chat_lieu, id_nhan_vien_tao)
INSERT INTO san_pham (id_danh_muc, id_thuong_hieu, id_chat_lieu, ma_san_pham, ten_san_pham, mo_ta, id_nhan_vien_tao, trang_thai) VALUES
(1, 1, 1, 'SP01', N'Áo Thun Nam Cotton Thể Thao', N'Áo thun thoáng mát, co giãn 4 chiều cực tốt.', 1, 1),
(2, 3, 2, 'SP02', N'Áo Sơ Mi Nam Công Sở', N'Chất liệu kaki mềm mịn, form lịch lãm sang trọng.', 1, 1);
GO

-- 12. Sản phẩm chi tiết (Biến thể theo Màu sắc & Kích thước)
INSERT INTO san_pham_chi_tiet (id_san_pham, id_mau_sac, id_kich_thuoc, ma_san_pham_chi_tiet, ten_san_pham_chi_tiet, gia_nhap, gia_ban, so_luong_ton, so_luong_tam_giu, trang_thai) VALUES
(1, 1, 2, 'SPCT01', N'Áo Thun Nam - Đen / M', 100000, 180000, 50, 0, 1),
(1, 1, 3, 'SPCT02', N'Áo Thun Nam - Đen / L', 100000, 180000, 30, 0, 1),
(1, 2, 2, 'SPCT03', N'Áo Thun Nam - Trắng / M', 100000, 180000, 40, 0, 1),
(2, 3, 2, 'SPCT04', N'Áo Sơ Mi Nam - Xanh Navy / M', 150000, 250000, 25, 0, 1);
GO


-- Chèn phần thưởng mẫu cho Vòng quay may mắn
INSERT INTO phan_thuong_minigame (ten_phan_thuong, loai_game, loai_phan_thuong, gia_tri_xu, id_voucher, ty_le_trung, so_luong_gioi_han, so_luong_da_trung, trang_thai) VALUES
(N'10 Xu May Mắn', 'vong_quay', 'xu', 10, NULL, 40.00, -1, 0, 1),
(N'50 Xu Thưởng Nóng', 'vong_quay', 'xu', 50, NULL, 15.00, -1, 0, 1),
(N'Chúc Bạn May Mắn Lần Sau', 'vong_quay', 'khong_trung', 0, NULL, 35.00, -1, 0, 1),
(N'Voucher Giảm Giá 20K', 'vong_quay', 'voucher', 0, null, 10.00, 50, 0, 1); 
GO

use datnolove

select * from hoa_don_chi_tiet
select * from san_pham_chi_tiet
select * from thanh_toan
select * from hoa_don

select * from hoa_don_voucher
select * from lich_su_xu

select * from lich_su_hoa_don
select * from phan_thuong_minigame

select * from voucher_cua_khach_hang

select * from cau_hinh_he_thong
select * from cau_hinh_hang_thanh_vien

select * from tai_khoan
select * from khach_hang
select * from nhan_vien

select * from voucher_minigame
update khach_hang set ma_khach_hang='KH02' where id=4
