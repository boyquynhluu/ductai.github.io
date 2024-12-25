package com.qlsv.controller;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
import org.springframework.http.HttpStatus;
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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = "/api/sinhviens")
@RequiredArgsConstructor
@Tag(name = "SinhVien Controller")
@Slf4j(topic = "SinhVien-Controller")
public class SinhVienRestController {

    private final SinhVienService service;
    private final ModelMapper mapper;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get All SinhVien", security = @SecurityRequirement(name = "Authorization"))
    public Map<String, Object> getAllSinhVien() {
        log.info("Get All Sinh Vien Start!");
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

    @Operation(summary = "Create Sinh Vien", description = "API add new Sinh Vien to Database")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createSinhVien(@Valid @RequestBody SinhVienResource sinhvien) {
        log.info("Create Sinh Vien Start!");
        SinhVienModel model = mapper.map(sinhvien, SinhVienModel.class);
        service.createSinhVien(model);
        return (ResponseEntity<?>) ResponseEntity.ok();
    }

    @PostMapping("/export/excel")
    public ResponseEntity<?> exportExcel(@RequestBody String[] ids) {
        log.info("Export Excel Start!");

        if (ids == null || ids.length == 0) {
            log.info("Ids is null");
            return ResponseEntity.badRequest().body("Ids is null...");
        }
        try {
            // Create filename
            DateFormat dateFormatter = new SimpleDateFormat(Constants.YYYYMMDD_HHMMSS);
            String currentDateTime = dateFormatter.format(new Date());

            // Create folder name
            DateFormat folderDateFormater = new SimpleDateFormat(Constants.YYYYMMDD);
            String currentDateTimeFolder = folderDateFormater.format(new Date());

         // Định nghĩa đường dẫn file
            Path folderPath = Paths.get(Constants.PATH_EXPORT_EXCEL, currentDateTimeFolder);
            Path filePath = folderPath.resolve(currentDateTime + Constants.EXPORT_EXCEL_XLSX);

            // Tạo thư mục nếu chưa tồn tại
            Files.createDirectories(folderPath);
            // Get all Sinh vien by id
            List<SinhVienModel> listOfStudents = service.getSinhVienByIds(ids);
            if(CollectionUtils.isEmpty(listOfStudents)) {
                log.warn("No students found for given Ids");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body("Sinh Vien Not Exist!");
            }

            try (FileOutputStream fileOut = new FileOutputStream(filePath.toFile());
                    XSSFWorkbook workbook = new XSSFWorkbook()) {
                // Export Excel
                ExcelExport export = new ExcelExport(listOfStudents, workbook);
                export.generateExcelFile(fileOut);
            } catch (IOException e) {
                log.error("Export excel has errro:", e);
                return ResponseEntity.status(500).body("An error occurred while creating the Excel file.");
            }
            log.info("Excel file created successfully at: {}", filePath.toAbsolutePath());
            return ResponseEntity.ok("File created: " + filePath.toAbsolutePath());
        } catch(IOException e) {
            log.error("Error while handling Excel file", e);
            return ResponseEntity.status(500).body("An error occurred while processing the Excel file.");
        } catch (Exception e) {
            log.error("Export Excel Has Error:", e);
            return ResponseEntity.status(500).body("Unexpected error while exporting Excel file.");
        }
    }

    @PostMapping("/export/pdf")
    public ResponseEntity<String> exportPdf(@RequestBody String[] ids) throws Exception {
        log.info("Export Pdf Start!");
        try {
            DateFormat dateFormatter = new SimpleDateFormat(Constants.YYYYMMDD_HHMMSS);
            String currentDateTime = dateFormatter.format(new Date());

            DateFormat folderDateFormatter = new SimpleDateFormat(Constants.YYYYMMDD);
            String currentDateTimeFolder = folderDateFormatter.format(new Date());

            Path folderPath = Paths.get(Constants.PATH_EXPORT_EXCEL, currentDateTimeFolder);
            Path filePath = folderPath.resolve(currentDateTime + Constants.EXPORT_PDF);
            // Tạo thư mục nếu chưa tồn tại
            Files.createDirectories(folderPath);
            // Get Data
            List<SinhVienModel> listOfStudents = service.getSinhVienByIds(ids);
            if(CollectionUtils.isEmpty(listOfStudents)) {
                log.warn("No students found for given Ids");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body("Data not exist.");
            }
            // Export PDF
            try (FileOutputStream outputFile = new FileOutputStream(filePath.toFile())) {
                PdfExporter exporter = new PdfExporter(listOfStudents);
                exporter.export(outputFile);
            } catch (DocumentException | IOException e) {
                log.info("Export PDF has error: {}", e);
                return ResponseEntity.status(500).body("An error occurred while creating the PDF file.");
            }
            log.info("Excel file created successfully at: {}", filePath.toAbsolutePath());
            return ResponseEntity.ok("File created: " + filePath.toAbsolutePath());
        } catch(IOException e) {
            log.error("Error while handling Excel file", e);
            return ResponseEntity.status(500).body("An error occurred while processing the Excel file.");
        } catch (Exception e) {
            log.error("Export PDF Has Error:", e);
            return ResponseEntity.status(500).body("Unexpected error while exporting PDF file.");
        }
    }

    @PostMapping(value = "/search", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<SinhVienResource> search(@RequestBody SinhVienSearch search) {
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
