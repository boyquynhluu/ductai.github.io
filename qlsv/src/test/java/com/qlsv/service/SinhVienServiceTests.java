package com.qlsv.service;

import static org.mockito.Mockito.when;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.qlsv.model.SinhVienModel;
import com.qlsv.repositories.SinhVienRepository;
import com.qlsv.serviceimpl.SinhVienServiceImpl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

/*@ExtendWith(MockitoExtension.class)*/
@SpringBootTest
@Slf4j(topic = "Service SinhVien Tests")
@Transactional
class SinhVienServiceTests {

    private static final StringBuilder SQL_GET_ALL_SINH_VIEN = new StringBuilder()
            .append("SELECT")
            .append("   sv.ma_sinh_vien,")
            .append("   sv.ten_sinh_vien,")
            .append("   sv.tuoi,")
            .append("   sv.phone,")
            .append("   sv.email,")
            .append("   sv.ngay_sinh,")
            .append("   sv.gioi_tinh,")
            .append("   sv.dia_chi,")
            .append("   sv.trang_thai")
            .append(" FROM")
            .append("   sinh_vien sv")
            .append(" ORDER BY")
            .append("   sv.ten_sinh_vien");

    @Autowired
    private SinhVienService service;

    @Mock
    private SinhVienRepository repository;

    @MockBean
    private EntityManager entityManager;

    @Mock
    private Query query;

    private static SinhVienModel sinhvien1;
    private static SinhVienModel sinhvien2;

    @BeforeAll
    static void beforeAll() {
        sinhvien1 = new SinhVienModel();
        sinhvien1.setMaSV(100);
        sinhvien1.setTenSinhVien("Tesst1");
        sinhvien1.setTuoi(20);
        sinhvien1.setPhone("11111");
        sinhvien1.setEmail("vanductai.dhv@gmail.com");
        sinhvien1.setNgaySinh("1992-10-10");
        sinhvien1.setGioiTinh("Nam");
        sinhvien1.setDiaChi("Nghệ An");
        sinhvien1.setTrangThai("Đang Học");

        sinhvien2 = new SinhVienModel();
        sinhvien2.setMaSV(1);
        sinhvien2.setTenSinhVien("Tesst2");
    }

    @BeforeEach
    void setUp() {
        service = new SinhVienServiceImpl(entityManager, repository);
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getSinhViens_Success() throws Exception {
        log.info("Start Unit Get All Sinh Vien........");
        List<Object> objects = Arrays.asList(
                new Object[] { 1, "Test1", 20, "0111", "test@gmail.com", Date.valueOf(LocalDate.parse("1992-10-10")),
                        Boolean.FALSE, "Nghệ An", "Đang Học" },
                new Object[] { 2, "Test2", 20, "0111", "test@gmail.com", Date.valueOf(LocalDate.parse("1992-10-10")),
                        Boolean.FALSE, "Nghệ An", "Đang Học" });

        // Mock createNativeQuery
        when(entityManager.createNativeQuery(SQL_GET_ALL_SINH_VIEN.toString())).thenReturn(query);
        // Mock getResultList
        when(query.getResultList()).thenReturn(objects);

        List<SinhVienModel> result = service.getSinhViens();

        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals("Test1", result.get(0).getTenSinhVien());
    }

    @Test
    void getSinhViens_Error() throws Exception {
        /*
         * log.info("Start Unit Get All Sinh Vien Error........"); List<Object> objects
         * = Arrays.asList(new Object[] { 1, "Test1", 20, "0111", "test@gmail.com",
         * Date.valueOf(LocalDate.parse("1992-10-10")), Boolean.FALSE, "Nghệ An",
         * "Đang Học" }, new Object[] { 2, "Test2", 20, "0111", "test@gmail.com",
         * "1992-10-10", Boolean.FALSE, "Nghệ An", "Đang Học" }); // Mock
         * createNativeQuery
         * when(entityManager.createNativeQuery(SQL_GET_ALL_SINH_VIEN.toString())).
         * thenReturn(query); // Mock getResultList
         * when(query.getResultList()).thenReturn(objects);
         * 
         * service.getSinhViens();
         */
    }

    @Test
    void getSinhVienByIds_Success() throws Exception {
        log.info("Start Unit Get Sinh Vien By Id........");
        List<Object> objects = Arrays.asList(
                new Object[] { 1, "Test1", 20, "0111", "test@gmail.com", Date.valueOf(LocalDate.parse("1992-10-10")),
                        Boolean.FALSE, "Nghệ An", "Đang Học" },
                new Object[] { 2, "Test2", 20, "0111", "test@gmail.com", Date.valueOf(LocalDate.parse("1992-10-10")), Boolean.FALSE, "Nghệ An",
                        "Đang Học" });

        when(entityManager.createNativeQuery(this.createSQLGetSinhVienByIds(new String[] { "1", "2" }))).thenReturn(query);

        when(query.getResultList()).thenReturn(objects);

        List<SinhVienModel> result = service.getSinhVienByIds(new String[] { "1", "2" });

        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(1, result.get(0).getMaSV());
        Assertions.assertEquals(2, result.get(1).getMaSV());

    }

    @Test
    void searchSinhVien() throws Exception {
        log.info("Start Unit Search Sinh Vien........");
        List<Object> objects = Arrays.asList(
                new Object[] { 1, "Test1", 20, "0111", "test@gmail.com", Date.valueOf(LocalDate.parse("1992-10-10")),
                        Boolean.FALSE, "Nghệ An", "Đang Học" },
                new Object[] { 2, "Test2", 20, "0111", "test@gmail.com", Date.valueOf(LocalDate.parse("1992-10-10")), Boolean.FALSE, "Nghệ An",
                        "Đang Học" });
        when(entityManager.createNativeQuery(this.searchSQL("test@gmail.com"))).thenReturn(query);
        when(query.getResultList()).thenReturn(objects);

         List<SinhVienModel> results = service.searchSinhVien("test@gmail.com");

         Assertions.assertNotNull(results);
         Assertions.assertEquals(2, results.size());
         Assertions.assertEquals(1, results.get(0).getMaSV());
         Assertions.assertEquals(2, results.get(1).getMaSV());
    }

    @Test
    void createSinhVien_Success() throws Exception {
        log.info("Start Unit Get Sinh Vien By Id........");
        service.createSinhVien(sinhvien1);
    }

    /**
     * Get Sinh Vien by ids
     * 
     * @param ids
     * @return sqlGetSinhVienByIds
     */
    private String createSQLGetSinhVienByIds(String[] ids) {
        // Create sql get sinh vien by id
        StringBuilder sqlGetSinhVienByIds = new StringBuilder()
                .append("SELECT")
                .append("   sv.ma_sinh_vien,")
                .append("   sv.ten_sinh_vien,")
                .append("   sv.tuoi,")
                .append("   sv.phone,")
                .append("   sv.email,")
                .append("   sv.ngay_sinh,")
                .append("   sv.gioi_tinh,")
                .append("   sv.dia_chi,")
                .append("   sv.trang_thai")
                .append(" FROM").append("   Sinh_Vien sv")
                .append(" WHERE");

        StringBuilder sqlWhere = new StringBuilder();
        if (ids.length > 1) {
            sqlGetSinhVienByIds.append(" sv.ma_sinh_vien IN (");
            for (int i = 0; i < ids.length; i++) {
                sqlWhere.append("'" + ids[i] + "'");
                sqlWhere.append(",");
            }
            sqlGetSinhVienByIds.append(sqlWhere.toString().substring(0, sqlWhere.toString().lastIndexOf(",")));
        } else if (ids.length == 1) {
            sqlGetSinhVienByIds.append(" sv.ma_sinh_vien IN ('" + ids[0] + "'");
        }
        sqlGetSinhVienByIds.append(")");
        sqlGetSinhVienByIds.append(" ORDER BY");
        sqlGetSinhVienByIds.append("   sv.ten_sinh_vien");

        return sqlGetSinhVienByIds.toString();
    }
    
    private String searchSQL(String input) {
        return new StringBuilder()
                .append("SELECT")
                .append("   sv.ma_sinh_vien,")
                .append("   sv.ten_sinh_vien,")
                .append("   sv.tuoi,")
                .append("   sv.phone,")
                .append("   sv.email,")
                .append("   sv.ngay_sinh,")
                .append("   sv.gioi_tinh,")
                .append("   sv.dia_chi,")
                .append("   sv.trang_thai")
                .append(" FROM")
                .append("   Sinh_Vien sv")
                .append("   WHERE")
                .append("   sv.ten_sinh_vien LIKE  '" + '%' + "" + input + "" + '%' + "' ")
                .append(" OR")
                .append("   sv.email LIKE  '" + '%' + "" + input + "" + '%' + "' ")
                .append(" OR")
                .append("   sv.phone LIKE  '" + '%' + "" + input + "" + '%' + "' ")
                .append(" ORDER BY")
                .append("   sv.ten_sinh_vien").toString();
    }
}
