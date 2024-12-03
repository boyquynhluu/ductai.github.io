package com.qlsv.restcontroller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lowagie.text.DocumentException;
import com.qlsv.constants.Constants;
import com.qlsv.model.SinhVienModel;
import com.qlsv.resource.SinhVienResource;
import com.qlsv.resource.SinhVienSearch;
import com.qlsv.service.SinhVienService;
import com.qlsv.utils.ExcelExport;
import com.qlsv.utils.PdfExporter;

import jakarta.transaction.SystemException;

@RestController
@RequestMapping(value = "/api/")
public class SinhVienRestController {

    private SinhVienService service;

    public SinhVienRestController(SinhVienService service) {
        this.service = service;
    }

    @Autowired
    private ModelMapper mapper;

    @GetMapping(value = "sinhviens", produces = MediaType.APPLICATION_JSON_VALUE)
    /* @PreAuthorize("hasAuthority('ROLE_ADMIN')") */
    public Map<String, Object> getAllSinhVien() throws Exception {
        List<SinhVienResource> resources = new ArrayList<>();
        Map<String, Object> sinhviens = new HashedMap<>();

        List<SinhVienModel> models = service.getSinhViens();
        // Set data Models to Resources
        if (!CollectionUtils.isEmpty(models)) {
            Type listType = new TypeToken<List<SinhVienResource>>() {
            }.getType();
            resources = mapper.map(models, listType);
        }
        sinhviens.put("data", resources);
        sinhviens.put("total", resources.size());
        return sinhviens;
    }

    @PostMapping(value = "sinhviens", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createSinhVien(@RequestBody SinhVienResource sinhvien) throws Exception {
        SinhVienModel model = mapper.map(sinhvien, SinhVienModel.class);
        service.createSinhVien(model);
        return (ResponseEntity<?>) ResponseEntity.ok();
    }

    @PostMapping("sinhviens/export/excel")
    public ResponseEntity<?> exportExcel(@RequestBody String[] ids) throws Exception {
        if (ids.length == 0) {
            throw new SystemException("Id is null!");
        }

        // Create filename
        DateFormat dateFormatter = new SimpleDateFormat(Constants.YYYYMMDD_HHMMSS);
        String currentDateTime = dateFormatter.format(new Date());

        // Create folder name
        DateFormat folderDateFormater = new SimpleDateFormat(Constants.YYYYMMDD);
        String currentDateTimeFolder = folderDateFormater.format(new Date());

        File folderPath = new File(Constants.PATH_EXPORT_EXCEL + currentDateTimeFolder);
        File filePath = new File(folderPath, currentDateTime + Constants.EXPORT_EXCEL_XLSX);

        // Create folder
        if (!folderPath.exists()) {
            folderPath.mkdirs();
        }

        // Create file
        if (!filePath.exists()) {
            filePath.createNewFile();
        }

        try (FileOutputStream fileOut = new FileOutputStream(filePath); XSSFWorkbook workbook = new XSSFWorkbook()) {

            // Get all Sinh vien by id
            List<SinhVienModel> listOfStudents = service.getSinhVienByIds(ids);

            // Export Excel
            ExcelExport export = new ExcelExport(listOfStudents, workbook);
            export.generateExcelFile(fileOut);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("An error occurred while creating the Excel file.");
        }

        return ResponseEntity.ok("File created: " + filePath.getAbsolutePath());
    }

    @PostMapping("sinhviens/export/pdf")
    public ResponseEntity<String> exportPdf(@RequestBody String[] ids) throws Exception {
        DateFormat dateFormatter = new SimpleDateFormat(Constants.YYYYMMDD_HHMMSS);
        String currentDateTime = dateFormatter.format(new Date());

        DateFormat folderDateFormatter = new SimpleDateFormat(Constants.YYYYMMDD);
        String currentDateTimeFolder = folderDateFormatter.format(new Date());

        File folderPath = new File(Constants.PATH_EXPORT_EXCEL + currentDateTimeFolder);
        File filePath = new File(folderPath, currentDateTime + Constants.EXPORT_PDF);

        // Create folder if it does not exist
        if (!folderPath.exists()) {
            if (!folderPath.mkdirs()) {
                return ResponseEntity.status(500).body("Failed to create directory.");
            }
        }
        // Create file
        if (!filePath.exists()) {
            if (!filePath.createNewFile()) {
                return ResponseEntity.status(500).body("Failed to create file.");
            }
        }

        try {
            // Export PDF
            try (FileOutputStream outputFile = new FileOutputStream(filePath)) {
                List<SinhVienModel> listOfStudents = service.getSinhVienByIds(ids);

                PdfExporter exporter = new PdfExporter(listOfStudents);
                exporter.export(outputFile);
            } catch (DocumentException | IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("An error occurred while creating the PDF file.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("An error occurred with file operations.");
        }

        return ResponseEntity.ok("File created: " + filePath.getAbsolutePath());
    }

    @PostMapping(value = "sinhvien/search", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<SinhVienResource> search(@RequestBody SinhVienSearch search) throws Exception {
        List<SinhVienResource> resources = new ArrayList<>();
        List<SinhVienModel> models = service.searchSinhVien(search.getSearch());
        // Set data Models to Resources
        if (!CollectionUtils.isEmpty(models)) {
            Type listType = new TypeToken<List<SinhVienResource>>() {
            }.getType();
            resources = mapper.map(models, listType);
        }
        return resources;
    }
}
