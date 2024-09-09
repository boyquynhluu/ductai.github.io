package com.qlsv.utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.qlsv.model.SinhVienModel;

public class ExcelExport {

    private List<SinhVienModel> sinhVienList;
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;

    public ExcelExport(List<SinhVienModel> sinhVienList, XSSFWorkbook workbook) {
        this.sinhVienList = sinhVienList;
        this.workbook = workbook;
    }

    /**
     * Write header
     */
    private void writeHeader() {
        sheet = workbook.createSheet("Sinh Viên");
        Row row = sheet.createRow(0);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);

        // Create Header
        createCell(row, 0, "STT", style);
        createCell(row, 1, "Mã Sinh Viên", style);
        createCell(row, 2, "Tên Sinh Viên", style);
        createCell(row, 3, "Tuổi", style);
        createCell(row, 4, "Số Điện Thoại", style);
        createCell(row, 5, "Email", style);
        createCell(row, 6, "Ngày Sinh", style);
        createCell(row, 7, "Giới Tính", style);
        createCell(row, 8, "Địa Chỉ", style);
        createCell(row, 9, "Trạng Thái", style);
    }

    /**
     * Create header
     * 
     * @param row
     * @param columnCount
     * @param valueOfCell
     * @param style
     */
    private void createCell(Row row, int columnCount, Object valueOfCell, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (valueOfCell instanceof Integer) {
            cell.setCellValue((Integer) valueOfCell);
        } else if (valueOfCell instanceof Long) {
            cell.setCellValue((Long) valueOfCell);
        } else if (valueOfCell instanceof String) {
            cell.setCellValue((String) valueOfCell);
        } else {
            cell.setCellValue((Boolean) valueOfCell);
        }
        cell.setCellStyle(style);
    }

    /**
     * Write data to file excel
     */
    private void write() {
        int rowCount = 1;
        int index = 1;
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);
        for (SinhVienModel record : sinhVienList) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            createCell(row, columnCount++, index, style);
            createCell(row, columnCount++, record.getMaSV(), style);
            createCell(row, columnCount++, record.getTenSinhVien(), style);
            createCell(row, columnCount++, record.getTuoi(), style);
            createCell(row, columnCount++, record.getPhone(), style);
            createCell(row, columnCount++, record.getEmail(), style);
            createCell(row, columnCount++, record.getNgaySinh(), style);
            createCell(row, columnCount++, record.getGioiTinh(), style);
            createCell(row, columnCount++, record.getDiaChi(), style);
            createCell(row, columnCount++, record.getTrangThai(), style);
            
            index++;
        }
    }

    /**
     * Export excel
     * 
     * @param fileOut FileOutputStream
     * @throws IOException
     */
    public void generateExcelFile(FileOutputStream fileOut) throws IOException {
        writeHeader();
        write();
        try {
            workbook.write(fileOut);
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException("Export excel error!");
        }
    }
}
