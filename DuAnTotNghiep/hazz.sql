CREATE DATABASE hazz
GO

USE hazz
GO

-- =========================
-- 1. vai_tro
-- =========================
CREATE TABLE vai_tro (
    id INT IDENTITY(1,1) PRIMARY KEY,
    ma_vai_tro NVARCHAR(50) NOT NULL,
    ten_vai_tro NVARCHAR(100) NOT NULL,
    trang_thai INT
);

-- =========================
-- 2. tai_khoan
-- =========================
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
    CONSTRAINT FK_tai_khoan_vai_tro
        FOREIGN KEY (id_vai_tro) REFERENCES vai_tro(id)
);

-- =========================
-- 3. nhan_vien
-- =========================
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
    CONSTRAINT FK_nhan_vien_tai_khoan
        FOREIGN KEY (id_tai_khoan) REFERENCES tai_khoan(id)
);

-- =========================
-- 4. khach_hang
-- =========================
CREATE TABLE khach_hang (
    id INT IDENTITY(1,1) PRIMARY KEY,
    id_tai_khoan INT UNIQUE,
    ma_khach_hang NVARCHAR(50)  NULL,
    ho_ten NVARCHAR(150)  NULL,
    so_dien_thoai NVARCHAR(20)  NULL UNIQUE,
    ngay_sinh DATE,
    hang_thanh_vien NVARCHAR(50),
    dia_chi NVARCHAR(255),
    so_lan_mua INT DEFAULT 0,
    ngay_mua_cuoi DATETIME,
    anh NVARCHAR(255),
    gioi_tinh BIT,
    ngay_tao DATETIME DEFAULT GETDATE(),
    ngay_cap_nhat DATETIME,
    nguoi_tao NVARCHAR(100),
    nguoi_cap_nhat NVARCHAR(100),
    trang_thai_xoa BIT DEFAULT 0,
    CONSTRAINT FK_khach_hang_tai_khoan
        FOREIGN KEY (id_tai_khoan) REFERENCES tai_khoan(id)
);

-- =========================
-- 5. dia_chi_khach_hang
-- =========================
CREATE TABLE dia_chi_khach_hang (
    id INT IDENTITY(1,1) PRIMARY KEY,

    ma_dia_chi VARCHAR(50),

    ten_nguoi_nhan NVARCHAR(255),
    so_dien_thoai VARCHAR(15),

    thanh_pho NVARCHAR(100),
    quan NVARCHAR(100),
    phuong NVARCHAR(100),

    -- Mã GHN
    district_id INT,
    ward_code VARCHAR(20),

    -- Tọa độ GPS
    latitude DECIMAL(10,7),
    longitude DECIMAL(10,7),

    dia_chi_cu_the NVARCHAR(MAX),

    mac_dinh BIT DEFAULT 0,

    trang_thai INT DEFAULT 1,

    id_khach_hang INT NOT NULL,

    CONSTRAINT FK_dia_chi_khach_hang
        FOREIGN KEY (id_khach_hang)
        REFERENCES khach_hang(id)
);
-- =========================
-- 6. danh_muc
-- =========================
CREATE TABLE danh_muc (
    id INT IDENTITY(1,1) PRIMARY KEY,
    ma_danh_muc VARCHAR(20) NOT NULL UNIQUE,
    ten_danh_muc NVARCHAR(100) NOT NULL,
    ngay_tao DATETIME DEFAULT GETDATE(),
    ngay_cap_nhat DATETIME DEFAULT GETDATE(),
    trang_thai BIT DEFAULT 1
);
GO

-- =========================
-- 7. thuong_hieu
-- =========================
CREATE TABLE thuong_hieu (
    id INT IDENTITY(1,1) PRIMARY KEY,
    ma_thuong_hieu VARCHAR(20) NOT NULL UNIQUE,
    ten_thuong_hieu NVARCHAR(100) NOT NULL,
    ngay_tao DATETIME DEFAULT GETDATE(),
    ngay_cap_nhat DATETIME DEFAULT GETDATE(),
    trang_thai BIT DEFAULT 1
);
GO

-- =========================
-- 8. chat_lieu
-- =========================
CREATE TABLE chat_lieu (
    id INT IDENTITY(1,1) PRIMARY KEY,
    ma_chat_lieu VARCHAR(20) NOT NULL UNIQUE,
    ten_chat_lieu NVARCHAR(100) NOT NULL,
    ngay_tao DATETIME DEFAULT GETDATE(),
    ngay_cap_nhat DATETIME DEFAULT GETDATE(),
    trang_thai BIT DEFAULT 1
);
GO

-- =========================
-- 9. mau_sac
-- =========================
CREATE TABLE mau_sac (
    id INT IDENTITY(1,1) PRIMARY KEY,
    ma_mau_sac VARCHAR(20) NOT NULL UNIQUE,
    ten_mau_sac NVARCHAR(100) NOT NULL,
    ngay_tao DATETIME DEFAULT GETDATE(),
    ngay_cap_nhat DATETIME DEFAULT GETDATE(),
    trang_thai BIT DEFAULT 1
);
GO

-- =========================
-- 10. kich_thuoc
-- =========================
CREATE TABLE kich_thuoc (
    id INT IDENTITY(1,1) PRIMARY KEY,
    ma_kich_thuoc VARCHAR(20) NOT NULL UNIQUE,
    ten_kich_thuoc NVARCHAR(50) NOT NULL,
    ngay_tao DATETIME DEFAULT GETDATE(),
    ngay_cap_nhat DATETIME DEFAULT GETDATE(),
    trang_thai BIT DEFAULT 1
);
GO

-- =========================
-- 11. san_pham
-- =========================
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

-- =========================
-- 12. san_pham_chi_tiet
-- =========================
CREATE TABLE san_pham_chi_tiet (
    id INT IDENTITY(1,1) PRIMARY KEY,
    id_san_pham INT NOT NULL,
    id_mau_sac INT NOT NULL,
    id_kich_thuoc INT NOT NULL,
    ma_san_pham_chi_tiet VARCHAR(30) NOT NULL UNIQUE,
    ten_san_pham_chi_tiet NVARCHAR(200),
    gia_nhap DECIMAL(18,2) NOT NULL CHECK (gia_nhap >= 0),
    gia_ban DECIMAL(18,2) NOT NULL CHECK (gia_ban >= 0),
    so_luong_ton INT NOT NULL DEFAULT 0 CHECK (so_luong_ton >= 0),
    ngay_tao DATETIME DEFAULT GETDATE(),
    ngay_cap_nhat DATETIME DEFAULT GETDATE(),
    trang_thai BIT DEFAULT 1,
    CONSTRAINT fk_spct_san_pham FOREIGN KEY (id_san_pham) REFERENCES san_pham(id),
    CONSTRAINT fk_spct_mau_sac FOREIGN KEY (id_mau_sac) REFERENCES mau_sac(id),
    CONSTRAINT fk_spct_kich_thuoc FOREIGN KEY (id_kich_thuoc) REFERENCES kich_thuoc(id)
);
GO

-- =========================
-- 13. hinh_anh
-- =========================
CREATE TABLE hinh_anh (
    id INT IDENTITY(1,1) PRIMARY KEY,
    id_san_pham_chi_tiet INT NOT NULL,
    ten_anh NVARCHAR(100),
    link NVARCHAR(500) NOT NULL,
    dinh_dang VARCHAR(20),
    la_anh_chinh BIT DEFAULT 0,
    trang_thai BIT DEFAULT 1,
    CONSTRAINT fk_hinh_anh_spct FOREIGN KEY (id_san_pham_chi_tiet)
        REFERENCES san_pham_chi_tiet(id)
);

-- =========================
-- 14. gio_hang
-- =========================
CREATE TABLE gio_hang (
    id INT IDENTITY(1,1) PRIMARY KEY,
    id_khach_hang INT NOT NULL UNIQUE,

    ngay_tao DATETIME DEFAULT GETDATE(),
    ngay_cap_nhat DATETIME DEFAULT GETDATE(),

    trang_thai BIT DEFAULT 1,

    CONSTRAINT fk_gio_hang_khach_hang
        FOREIGN KEY(id_khach_hang)
        REFERENCES khach_hang(id)
);

CREATE TABLE gio_hang_chi_tiet (
    id INT IDENTITY(1,1) PRIMARY KEY,

    id_gio_hang INT NOT NULL,
    id_san_pham_chi_tiet INT NOT NULL,

    so_luong INT NOT NULL CHECK(so_luong > 0),

    duoc_chon BIT DEFAULT 1,

    ngay_them DATETIME DEFAULT GETDATE(),
    ngay_cap_nhat DATETIME DEFAULT GETDATE(),

    CONSTRAINT fk_ghct_gio_hang
        FOREIGN KEY(id_gio_hang)
        REFERENCES gio_hang(id),

    CONSTRAINT fk_ghct_spct
        FOREIGN KEY(id_san_pham_chi_tiet)
        REFERENCES san_pham_chi_tiet(id),

    CONSTRAINT uq_ghct UNIQUE(id_gio_hang,id_san_pham_chi_tiet)
);

-- =========================
-- 16. voucher
-- =========================
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

-- =========================
-- 17. hoa_don
-- =========================
CREATE TABLE hoa_don (
    id INT IDENTITY(1,1) PRIMARY KEY,

    id_khach_hang INT,
    id_nhan_vien INT,

    ma_hoa_don VARCHAR(30) NOT NULL UNIQUE,

    tong_tien_hang DECIMAL(18,2) DEFAULT 0,
    tong_giam_gia DECIMAL(18,2) DEFAULT 0,
    phi_van_chuyen DECIMAL(18,2) DEFAULT 0,
    tong_thanh_toan DECIMAL(18,2) DEFAULT 0,

    ten_nguoi_nhan NVARCHAR(100),
    so_dien_thoai_nguoi_nhan VARCHAR(15),
    dia_chi_giao_hang NVARCHAR(500),

    loai_hoa_don VARCHAR(20)
        CHECK (
            loai_hoa_don IN (
                'online',
                'tai_quay'
            )
        ),

    -- =========================
    -- TRẠNG THÁI ĐƠN HÀNG
    -- =========================
    trang_thai VARCHAR(30)
        CHECK (
            trang_thai IN (
                'cho_xac_nhan',
                'da_xac_nhan',
                'dang_giao',
                'da_giao',
                'hoan_thanh',
                'da_huy'
            )
        ),

    -- =========================
    -- TRẠNG THÁI THANH TOÁN
    -- =========================
    trang_thai_thanh_toan VARCHAR(30)
        DEFAULT 'chua_thanh_toan'
        CHECK (
            trang_thai_thanh_toan IN (
                'chua_thanh_toan',
                'da_thanh_toan',
                'hoan_tien'
            )
        ),

    ghi_chu NVARCHAR(MAX),

    ngay_tao DATETIME DEFAULT GETDATE(),
    ngay_cap_nhat DATETIME DEFAULT GETDATE(),

    CONSTRAINT fk_hoa_don_khach_hang
        FOREIGN KEY (id_khach_hang)
        REFERENCES khach_hang(id),

    CONSTRAINT fk_hoa_don_nhan_vien
        FOREIGN KEY (id_nhan_vien)
        REFERENCES nhan_vien(id)
);

-- =========================
-- 18. hoa_don_chi_tiet
-- =========================
CREATE TABLE hoa_don_chi_tiet (
    id INT IDENTITY(1,1) PRIMARY KEY,
    id_hoa_don INT NOT NULL,
    id_san_pham_chi_tiet INT NOT NULL,
    so_luong INT NOT NULL CHECK (so_luong > 0),
    don_gia DECIMAL(18,2) NOT NULL CHECK (don_gia >= 0),
    thanh_tien DECIMAL(18,2) NOT NULL CHECK (thanh_tien >= 0),
    CONSTRAINT fk_hdct_hoa_don
        FOREIGN KEY (id_hoa_don) REFERENCES hoa_don(id),
    CONSTRAINT fk_hdct_spct
        FOREIGN KEY (id_san_pham_chi_tiet) REFERENCES san_pham_chi_tiet(id)
);

-- =========================
-- 19. phuong_thuc_thanh_toan
-- =========================
CREATE TABLE phuong_thuc_thanh_toan (
    id INT IDENTITY(1,1) PRIMARY KEY,
    ma_phuong_thuc VARCHAR(20) NOT NULL UNIQUE,
    ten_phuong_thuc NVARCHAR(100) NOT NULL,
    trang_thai BIT DEFAULT 1
);

-- =========================
-- 20. thanh_toan
-- =========================
CREATE TABLE thanh_toan (
    id INT IDENTITY(1,1) PRIMARY KEY,
    id_hoa_don INT NOT NULL,
    id_phuong_thuc_thanh_toan INT NOT NULL,
    ma_giao_dich VARCHAR(100),
    so_tien DECIMAL(18,2) NOT NULL CHECK (so_tien >= 0),
    trang_thai VARCHAR(30)
        CHECK (trang_thai IN ('cho_thanh_toan', 'da_thanh_toan', 'that_bai', 'hoan_tien')),
    ngay_thanh_toan DATETIME,
    CONSTRAINT fk_thanh_toan_hoa_don
        FOREIGN KEY (id_hoa_don) REFERENCES hoa_don(id),
    CONSTRAINT fk_thanh_toan_phuong_thuc
        FOREIGN KEY (id_phuong_thuc_thanh_toan) REFERENCES phuong_thuc_thanh_toan(id)
);

-- =========================
-- 21. hoa_don_voucher
-- =========================
CREATE TABLE hoa_don_voucher (
    id INT IDENTITY(1,1) PRIMARY KEY,
    id_hoa_don INT NOT NULL,
    id_voucher INT NOT NULL,
    so_tien_giam DECIMAL(18,2) NOT NULL CHECK (so_tien_giam >= 0),
    CONSTRAINT fk_hdv_hoa_don
        FOREIGN KEY (id_hoa_don) REFERENCES hoa_don(id),
    CONSTRAINT fk_hdv_voucher
        FOREIGN KEY (id_voucher) REFERENCES voucher(id)
);

-- =========================
-- 22. dot_giam_gia
-- =========================
CREATE TABLE dot_giam_gia (
    id INT IDENTITY(1,1) PRIMARY KEY,

    ma_dot_giam_gia VARCHAR(30) NOT NULL UNIQUE,
    ten_dot_giam_gia NVARCHAR(200) NOT NULL,

    loai_giam_gia VARCHAR(20) NOT NULL
        CHECK (loai_giam_gia IN ('phan_tram', 'tien_mat')),

    gia_tri_giam DECIMAL(18,2) NOT NULL
        CHECK (gia_tri_giam >= 0),

    gia_tri_giam_toi_da DECIMAL(18,2),

    ngay_bat_dau DATETIME NOT NULL,
    ngay_ket_thuc DATETIME NOT NULL,

    mo_ta NVARCHAR(MAX),

    ngay_tao DATETIME DEFAULT GETDATE(),
    ngay_cap_nhat DATETIME DEFAULT GETDATE(),

    trang_thai VARCHAR(20) NOT NULL
        CHECK (trang_thai IN (
            'sap_dien_ra',
            'dang_dien_ra',
            'da_ket_thuc',
            'tam_dung'
        ))
);

-- =========================
-- 23. san_pham_giam_gia
-- =========================
CREATE TABLE san_pham_giam_gia
(
    id INT IDENTITY(1,1) PRIMARY KEY,

    id_dot_giam_gia INT NOT NULL,

    id_san_pham_chi_tiet INT NOT NULL,


    CONSTRAINT FK_SPGG_DOT_GIAM_GIA
    FOREIGN KEY(id_dot_giam_gia)
    REFERENCES dot_giam_gia(id),


    CONSTRAINT FK_SPGG_SAN_PHAM_CHI_TIET
    FOREIGN KEY(id_san_pham_chi_tiet)
    REFERENCES san_pham_chi_tiet(id)

);

IF EXISTS (
    SELECT 1
    FROM sys.indexes
    WHERE name = 'IX_spgg_dot'
    AND object_id = OBJECT_ID('san_pham_giam_gia')
)
BEGIN
    DROP INDEX IX_spgg_dot ON san_pham_giam_gia;
END
GO


IF EXISTS (
    SELECT 1
    FROM sys.indexes
    WHERE name = 'IX_spgg_spct'
    AND object_id = OBJECT_ID('san_pham_giam_gia')
)
BEGIN
    DROP INDEX IX_spgg_spct ON san_pham_giam_gia;
END
GO



CREATE TABLE tra_hang (
    id int IDENTITY(1,1) PRIMARY KEY,
    ma_tra_hang VARCHAR(20) NOT NULL UNIQUE,
    hoa_don_id int NOT NULL,
    ngay_tra DATETIME NOT NULL DEFAULT GETDATE(),
    tong_tien_hoan DECIMAL(18,2) NOT NULL DEFAULT 0,
    ly_do NVARCHAR(255),
    trang_thai VARCHAR(30) NOT NULL,

    CONSTRAINT FK_TRA_HANG_HOA_DON
        FOREIGN KEY (hoa_don_id)
        REFERENCES hoa_don(id)
);

CREATE TABLE tra_hang_chi_tiet (
    id int IDENTITY(1,1) PRIMARY KEY,
    tra_hang_id int NOT NULL,
    hoa_don_chi_tiet_id int NOT NULL,
    so_luong_tra INT NOT NULL,
    don_gia_tra DECIMAL(18,2) NOT NULL,
    thanh_tien DECIMAL(18,2) NOT NULL,

    CONSTRAINT FK_THCT_TRA_HANG
        FOREIGN KEY (tra_hang_id)
        REFERENCES tra_hang(id),

    CONSTRAINT FK_THCT_HDCT
        FOREIGN KEY (hoa_don_chi_tiet_id)
        REFERENCES hoa_don_chi_tiet(id)
);
-- =========================
-- VAI TRO
-- =========================
INSERT INTO vai_tro(ma_vai_tro, ten_vai_tro, trang_thai)
VALUES 
('ADMIN', N'ADMIN', 1),
('NV', N'STAFF', 1),
('KH', N'USERS', 1);

-- =========================
-- TAI KHOAN
-- =========================
INSERT INTO tai_khoan (id_vai_tro, ten_tai_khoan, email, so_dien_thoai, mat_khau, trang_thai)
VALUES 
(1,'admin','admin@gmail.com','0900000001','123456',1),
(2,'nhanvien01','nv01@gmail.com','0900000002','123456',1),
(3,'khachhang01','kh01@gmail.com','0900000003','123456',1),
(3,'khachhang02','kh02@gmail.com','0900000004','123456',1),
(3,'khachhang03','kh03@gmail.com','0900000005','123456',1),
(3,'khachhang04','kh04@gmail.com','0900000006','123456',1),
(3,'khachhang05','kh05@gmail.com','0900000007','123456',1),
(3,N'Khách lẻ','.','','',1);
-- =========================
-- NHAN VIEN
-- =========================
INSERT INTO nhan_vien (id_tai_khoan, ma_nhan_vien, ten_nhan_vien, dia_chi, ngay_sinh, gioi_tinh, can_cuoc_cong_dan, trang_thai)
VALUES 
(2,'NV001',N'Nguyễn Văn A',N'Hà Nội', '2000-01-01',1,'001234567890',1),
(1,'NV002',N'Nguyễn Quang Đạt',N'Hà Nội', '2000-01-01',1,'00123454d7890',1);

-- =========================
-- KHACH HANG
-- =========================
INSERT INTO khach_hang (id_tai_khoan, ma_khach_hang, ho_ten, so_dien_thoai, ngay_sinh, hang_thanh_vien, dia_chi, gioi_tinh)
VALUES 
(3,'KH001',N'Trần Thị B','0911111111', '2002-05-15',N'Bạc',N'Hà Nội',0),
(4,'KH002',N'Nguyễn Thị Mai','0901111111','2001-03-10',N'Bạc',N'Hà Nội',0),
(5,'KH003',N'Lê Văn Cường','0902222222','1999-07-21',N'Vàng',N'Hải Phòng',1),
(6,'KH004',N'Phạm Thị Lan','0903333333','2003-12-05',N'Bạc',N'Đà Nẵng',0),
(7,'KH005',N'Hoàng Minh Đức','0904444444','1998-09-18',N'Kim cương',N'Hồ Chí Minh',1),
(8,'',N'Khách lẻ','','',N' ',N'',1);



-- =========================
-- DIA CHI KHACH HANG
-- =========================
INSERT INTO dia_chi_khach_hang (ma_dia_chi, ten_nguoi_nhan, so_dien_thoai, thanh_pho, quan, phuong, dia_chi_cu_the, mac_dinh, trang_thai, id_khach_hang)
VALUES 
('DC001',N'Trần Thị B','0911111111', N'Hà Nội',N'Cầu Giấy',N'Dịch Vọng', N'123 Trần Thái Tông',1,1,1);

-- =========================
-- DANH MUC
-- =========================
INSERT INTO danh_muc(ma_danh_muc, ten_danh_muc)
VALUES 
('DM01',N'Áo Thun'),
('DM02',N'Áo Sơ Mi'),
('DM03',N'Quần Jean');

-- =========================
-- THUONG HIEU
-- =========================
INSERT INTO thuong_hieu(ma_thuong_hieu, ten_thuong_hieu)
VALUES 
('TH01',N'Nike'),
('TH02',N'Adidas'),
('TH03',N'Local Brand');

-- =========================
-- CHAT LIEU
-- =========================
INSERT INTO chat_lieu(ma_chat_lieu, ten_chat_lieu)
VALUES 
('CL01',N'Cotton'),
('CL02',N'Jean'),
('CL03',N'Kaki');

-- =========================
-- MAU SAC
-- =========================
INSERT INTO mau_sac(ma_mau_sac, ten_mau_sac)
VALUES 
('MS01',N'Trắng'),
('MS02',N'Đen'),
('MS03',N'Xanh');

-- =========================
-- KICH THUOC
-- =========================
INSERT INTO kich_thuoc(ma_kich_thuoc, ten_kich_thuoc)
VALUES 
('S','S'),
('M','M'),
('L','L'),
('XL','XL');

-- =========================
-- SAN PHAM
-- =========================
INSERT INTO san_pham (id_danh_muc,id_thuong_hieu,id_chat_lieu, ma_san_pham,ten_san_pham,mo_ta, id_nhan_vien_tao,id_nhan_vien_cap_nhat)
VALUES 
(1,1,1,'SP001',N'Áo Thun Nike Basic', N'Áo thun cotton cao cấp',1,1),
(2,2,1,'SP002',N'Áo Sơ Mi Adidas', N'Sơ mi nam công sở',1,1),
(3,3,2,'SP003',N'Quần Jean Local', N'Jean form slimfit',1,1);

-- =========================
-- SAN PHAM CHI TIET
-- =========================
INSERT INTO san_pham_chi_tiet (id_san_pham,id_mau_sac,id_kich_thuoc, ma_san_pham_chi_tiet, ten_san_pham_chi_tiet, gia_nhap,gia_ban,so_luong_ton)
VALUES 
(1,1,2,'SPCT001', N'Áo Thun Nike Trắng M', 150000,300000,50),
(1,2,3,'SPCT002', N'Áo Thun Nike Đen L', 150000,300000,40),
(2,1,2,'SPCT003', N'Áo Sơ Mi Adidas Trắng M', 200000,400000,30),
(3,3,3,'SPCT004', N'Quần Jean Local Xanh L', 250000,500000,20);

-- =========================


-- =========================
-- GIO HANG
-- =========================
INSERT INTO gio_hang(id_khach_hang)
VALUES (1);

-- =========================
-- GIO HANG CHI TIET
-- =========================
INSERT INTO gio_hang_chi_tiet (id_gio_hang,id_san_pham_chi_tiet,so_luong)
VALUES 
(1,1,2),
(1,4,1);

-- =========================
-- VOUCHER
-- =========================
INSERT INTO voucher (ma_voucher,ten_voucher,loai_giam_gia, gia_tri_giam,gia_tri_don_hang_toi_thieu, gia_tri_giam_toi_da,so_luong, ngay_bat_dau,ngay_ket_thuc)
VALUES 
('VC001',N'Giảm 10%', 'phan_tram',10,500000,100000,100, GETDATE(),DATEADD(MONTH,1,GETDATE()));

-- =========================
-- HOA DON
-- =========================
INSERT INTO hoa_don (id_khach_hang,id_nhan_vien, ma_hoa_don, tong_tien_hang,tong_giam_gia, phi_van_chuyen,tong_thanh_toan, ten_nguoi_nhan, so_dien_thoai_nguoi_nhan, dia_chi_giao_hang, loai_hoa_don,trang_thai)
VALUES 
(1,1,'HD001', 1100000,100000, 30000,1030000, N'Trần Thị B', '0911111111', N'123 Trần Thái Tông, Hà Nội', 'online','da_giao');

-- =========================
-- HOA DON CHI TIET
-- =========================
INSERT INTO hoa_don_chi_tiet (id_hoa_don,id_san_pham_chi_tiet, so_luong,don_gia,thanh_tien)
VALUES 
(1,1,2,300000,600000),
(1,4,1,500000,500000);

-- =========================
-- PHUONG THUC THANH TOAN
-- =========================
INSERT INTO phuong_thuc_thanh_toan
(ma_phuong_thuc, ten_phuong_thuc)
VALUES
('CASH',   N'Tiền mặt'),
('COD',    N'Thanh toán khi nhận hàng'),
('BANK',   N'Chuyển khoản VietQR'),
('VNPAY',  N'VNPay'),
('MOMO',   N'Ví điện tử MoMo'),
('ZALOPAY',N'Ví điện tử ZaloPay');
-- =========================
-- THANH TOAN
-- =========================
INSERT INTO thanh_toan (id_hoa_don,id_phuong_thuc_thanh_toan, ma_giao_dich,so_tien,trang_thai, ngay_thanh_toan)
VALUES 
(1,1,'GD001',1030000, 'da_thanh_toan',GETDATE());

-- =========================
-- HOA DON VOUCHER
-- =========================
INSERT INTO hoa_don_voucher (id_hoa_don,id_voucher,so_tien_giam)
VALUES 
(1,1,100000);

-- =========================
-- DOT GIAM GIA
INSERT INTO dot_giam_gia
(
    ma_dot_giam_gia,
    ten_dot_giam_gia,
    loai_giam_gia,
    gia_tri_giam,
    gia_tri_giam_toi_da,
    ngay_bat_dau,
    ngay_ket_thuc,
    mo_ta,
    trang_thai
)
VALUES
(
    'DGG001',
    N'Summer Sale 2026',
    'phan_tram',
    30,
    200000,
    '2026-07-01',
    '2026-07-31',
    N'Chương trình giảm giá mùa hè',
    'dang_dien_ra'
),
(
    'DGG002',
    N'Flash Sale Cuối Tuần',
    'phan_tram',
    50,
    300000,
    '2026-07-10',
    '2026-07-12',
    N'Giảm mạnh cuối tuần',
    'sap_dien_ra'
);
-- =========================
-- SAN PHAM GIAM GIA
-- =========================


