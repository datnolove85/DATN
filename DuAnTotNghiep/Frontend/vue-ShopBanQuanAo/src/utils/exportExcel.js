import ExcelJS from 'exceljs'
import { saveAs } from 'file-saver'

export const exportDashboardToExcel = async (data) => {
  const { dashboard, orderStatus, topProducts, topCustomers } = data
  const workbook = new ExcelJS.Workbook()

  // --- CẤU HÌNH SHEET 1: BÁO CÁO TỔNG QUAN ---
  const sheet = workbook.addWorksheet('Tổng Quan')
  sheet.columns = [{ width: 30 }, { width: 25 }, { width: 10 }]

  // Helper để thêm section title
  const addSection = (title) => {
    const row = sheet.addRow([title])
    row.font = { bold: true, size: 12, color: { argb: 'FFFFFFFF' } }
    row.fill = { type: 'pattern', pattern: 'solid', fgColor: { argb: 'FF334155' } }
    sheet.mergeCells(`A${row.number}:B${row.number}`)
  }

  // 1. TÀI CHÍNH
  addSection('TÀI CHÍNH & DOANH THU')
  sheet.addRow(['Tổng doanh thu', dashboard?.tongDoanhThu || 0])
  sheet.addRow(['Tiền mặt', dashboard?.doanhThuTienMat || 0])
  sheet.addRow(['Chuyển khoản', dashboard?.doanhThuChuyenKhoan || 0])
  sheet.addRow(['Giá vốn hàng bán', dashboard?.giaVon || 0])
  sheet.addRow(['Lợi nhuận gộp', dashboard?.loiNhuanGop || 0])

  // 2. PHÂN TÍCH ĐƠN HÀNG
  sheet.addRow([])
  addSection('PHÂN TÍCH ĐƠN HÀNG')
  sheet.addRow(['Tổng đơn hàng', dashboard?.tongDonHang || 0])
  sheet.addRow(['Đơn Online', dashboard?.donHangOnline || 0])
  sheet.addRow(['Đơn tại quầy (POS)', dashboard?.donHangTaiQuay || 0])

  // Trạng thái đơn chi tiết
  orderStatus?.forEach((item) => {
    sheet.addRow([`Trạng thái: ${item.status}`, item.total])
  })

  // Format tiền tệ cho cột B
  sheet.eachRow((row, rowNumber) => {
    if (rowNumber > 1 && row.getCell(2).value !== '' && typeof row.getCell(2).value === 'number') {
      row.getCell(2).numFmt = '#,##0 "₫"'
    }
  })

  // --- CẤU HÌNH SHEET 2: TOP SẢN PHẨM ---
  const sheetProducts = workbook.addWorksheet('Sản Phẩm Bán Chạy')
  sheetProducts.columns = [
    { header: 'Mã SP', key: 'maSanPham', width: 15 },
    { header: 'Tên sản phẩm', key: 'tenSanPham', width: 35 },
    { header: 'Thương hiệu', key: 'thuongHieu', width: 20 },
    { header: 'Doanh thu', key: 'doanhThu', width: 20 },
    { header: 'Lợi nhuận', key: 'loiNhuan', width: 20 },
    { header: 'Số lượng bán', key: 'soLuongBan', width: 15 },
    { header: 'Tồn kho', key: 'tongTonKho', width: 15 },
  ]
  sheetProducts.addRows(topProducts)
  formatSheetProfessional(sheetProducts, [4, 5])

  // --- CẤU HÌNH SHEET 3: KHÁCH HÀNG VIP ---
  const sheetCustomers = workbook.addWorksheet('Khách Hàng Thân Thiết')
  sheetCustomers.columns = [
    { header: 'Tên khách hàng', key: 'customerName', width: 30 },
    { header: 'Tổng chi tiêu', key: 'totalSpent', width: 20 },
    { header: 'Số đơn đã mua', key: 'totalOrders', width: 15 },
  ]
  sheetCustomers.addRows(topCustomers)
  formatSheetProfessional(sheetCustomers, [2])

  // Xuất file
  const buffer = await workbook.xlsx.writeBuffer()
  saveAs(new Blob([buffer]), `Bao_Cao_Chi_Tiet_${new Date().toLocaleDateString()}.xlsx`)
}

// Hàm hỗ trợ format chuyên nghiệp
const formatSheetProfessional = (sheet, moneyCols) => {
  sheet.getRow(1).font = { bold: true, color: { argb: 'FFFFFFFF' } }
  sheet.getRow(1).fill = { type: 'pattern', pattern: 'solid', fgColor: { argb: 'FF4F46E5' } }
  sheet.eachRow((row, rowNumber) => {
    if (rowNumber > 1) {
      moneyCols.forEach((col) => {
        if (typeof row.getCell(col).value === 'number') row.getCell(col).numFmt = '#,##0 "₫"'
      })
    }
  })
}
