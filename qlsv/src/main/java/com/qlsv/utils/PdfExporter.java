package com.qlsv.utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.qlsv.constants.Constants;
import com.qlsv.model.SinhVienModel;

public class PdfExporter {
    private List<SinhVienModel> sinhViens;

    public PdfExporter(List<SinhVienModel> sinhViens) {
        this.sinhViens = sinhViens;
    }

    /**
     * Write Header
     * 
     * @param table
     */
    private void writeTableHeader(PdfPTable table) {
        // Define header font
        Font headerFont = FontFactory.getFont(Constants.FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 12, Font.BOLD,
                BaseColor.WHITE);

        addCellToTable(table, "Mã SV", headerFont, BaseColor.GRAY);
        addCellToTable(table, "Tên Sinh Viên", headerFont, BaseColor.GRAY);
        addCellToTable(table, "Tuổi", headerFont, BaseColor.GRAY);
        addCellToTable(table, "Số Điện Thoại", headerFont, BaseColor.GRAY);
        addCellToTable(table, "E-Mail", headerFont, BaseColor.GRAY);
        addCellToTable(table, "Ngày Sinh", headerFont, BaseColor.GRAY);
        addCellToTable(table, "Giới Tính", headerFont, BaseColor.GRAY);
        addCellToTable(table, "Địa Chỉ", headerFont, BaseColor.GRAY);
        addCellToTable(table, "Trạng Thái", headerFont, BaseColor.GRAY);
    }

    /**
     * Write data
     * 
     * @param table
     */
    private void writeTableData(PdfPTable table) {
        // Define data font
        Font dataFont = FontFactory.getFont(Constants.FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 10, Font.NORMAL,
                BaseColor.BLACK);
        for (SinhVienModel sinhvien : sinhViens) {
            addCellToTable(table, String.valueOf(sinhvien.getMaSV()), dataFont, BaseColor.WHITE);
            addCellToTable(table, sinhvien.getTenSinhVien(), dataFont, BaseColor.WHITE);
            addCellToTable(table, String.valueOf(sinhvien.getTuoi()), dataFont, BaseColor.WHITE);
            addCellToTable(table, sinhvien.getPhone(), dataFont, BaseColor.WHITE);
            addCellToTable(table, sinhvien.getEmail(), dataFont, BaseColor.WHITE);
            addCellToTable(table, sinhvien.getNgaySinh(), dataFont, BaseColor.WHITE);
            addCellToTable(table, sinhvien.getGioiTinh(), dataFont, BaseColor.WHITE);
            addCellToTable(table, sinhvien.getDiaChi(), dataFont, BaseColor.WHITE);
            addCellToTable(table, sinhvien.getTrangThai(), dataFont, BaseColor.WHITE);
        }
    }

    private void addCellToTable(PdfPTable table, String text, Font font, BaseColor backgroundColor) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBackgroundColor(backgroundColor);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);

        table.addCell(cell);
    }

    /**
     * Export PDF
     * 
     * @param outputFile
     * @throws DocumentException
     * @throws IOException
     * @throws Exception
     */
    public void export(FileOutputStream outputFile) throws DocumentException {
        Document document = null;
        try {
            // Create Document instance
            document = new Document(PageSize.A3);
            PdfWriter.getInstance(document, outputFile);
            document.open();

            document.open();
            // Create title font
            Font titleFont = FontFactory.getFont(Constants.FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 18, Font.BOLD,
                    BaseColor.BLUE);
            // Add title
            Paragraph p = new Paragraph("Danh Sách Sinh Viên", titleFont);
            p.setAlignment(Paragraph.ALIGN_CENTER);

            document.add(p);

            // Setup table
            PdfPTable table = new PdfPTable(9);
            table.setWidthPercentage(100f);
            table.setWidths(new float[] { 1.5f, 4.5f, 2.0f, 4.0f, 7.5f, 3.5f, 3.5f, 3.5f, 3.5f });
            table.setSpacingBefore(10);

            // Write table header and data
            writeTableHeader(table);
            writeTableData(table);
            // Add table to document
            document.add(table);
        } catch (DocumentException e) {
            e.printStackTrace();
            throw new DocumentException("Write data pdf error!");
        } finally {
            if (!Objects.isNull(document)) {
                document.close();
            }
        }
    }
}
