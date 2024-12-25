package com.qlsv.serviceimpl;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.qlsv.entities.SinhVien;
import com.qlsv.model.SinhVienModel;
import com.qlsv.repositories.SinhVienRepository;
import com.qlsv.service.SinhVienService;
import com.qlsv.utils.ConvertUtils;
import com.qlsv.utils.DBUtils;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@SuppressWarnings("unchecked")
@Service
@Slf4j(topic = "SINHVIEN-SERVICE-IMPL")
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class SinhVienServiceImpl implements SinhVienService {

    private final EntityManager entityManager;
    private final SinhVienRepository repo;

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

    @Override
    public List<SinhVienModel> getSinhViens() {
        List<SinhVienModel> models = new ArrayList<>();
        try {
            List<Object[]> sinhVienEnityList = entityManager.createNativeQuery(SQL_GET_ALL_SINH_VIEN.toString())
                    .getResultList();

            for (Object[] obj : sinhVienEnityList) {
                models.add(this.setData(obj));
            }
        } catch (Exception e) {
            log.error("Error By Get All SinhVien: {}", e);
            return Collections.emptyList();
        }
        return CollectionUtils.isEmpty(models) ? Collections.emptyList() : models;
    }

    @Override
    public List<SinhVienModel> getSinhVienByIds(String[] ids) {
        List<SinhVienModel> models = new ArrayList<>();
        try {
            List<Object[]> sinhViens = entityManager.createNativeQuery(this.createSQLGetSinhVienByIds(ids))
                    .getResultList();

            for (Object[] obj : sinhViens) {
                models.add(this.setData(obj));
            }
        } catch (Exception e) {
            log.error("Get List Sinh Vien By Ids Has Error: {}", e);
            return Collections.emptyList();
        }
        return CollectionUtils.isEmpty(models) ? Collections.emptyList() : models;
    }

    @Override
    public List<SinhVienModel> searchSinhVien(String input) {
        List<SinhVienModel> models = new ArrayList<>();
        StringBuilder sql = new StringBuilder()
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
                .append("   sv.ten_sinh_vien");

        try {
            log.info("Search Sinh Vien:");
            List<Object[]> sinhviens = entityManager.createNativeQuery(sql.toString()).getResultList();
            for (Object[] obj : sinhviens) {
                models.add(this.setData(obj));
            }
        } catch (Exception e) {
            log.error("Search SinhVien has error: {}", e);
            return Collections.emptyList();
        }
        return CollectionUtils.isEmpty(models) ? Collections.emptyList() : models;
    }

    @Override
    public void createSinhVien(SinhVienModel model) {
        SinhVien sv = new SinhVien();
        try {
            sv.setTenSinhVien(model.getTenSinhVien());
            sv.setTuoi(model.getTuoi());
            sv.setPhone(model.getPhone());
            sv.setEmail(model.getEmail());
            sv.setDate(ConvertUtils.convertStringToDate(model.getNgaySinh()));
            sv.setGioiTinh(ConvertUtils.convertStringToBoolean(model.getGioiTinh()));
            sv.setDiaChi(model.getDiaChi());
            sv.setTrangThai(model.getTrangThai());
            //Save
            log.info("Add Sinh Vien: {}", sv);
            repo.save(sv);
        } catch (Exception e) {
            log.error("Create SinhVien Has Error: {}", e);
        }
    }

    /**
     * Set data from entity to model
     * 
     * @param obj
     * @return model
     * @throws Exception
     */
    private SinhVienModel setData(Object[] obj) throws Exception {
        SinhVienModel model = new SinhVienModel();
        try {
            model.setMaSV((int) obj[0]);
            model.setTenSinhVien((String) DBUtils.checkBlank(obj[1]));
            model.setTuoi((int) DBUtils.checkBlank(obj[2]));
            model.setPhone((String) DBUtils.checkBlank(obj[3]));
            model.setEmail((String) DBUtils.checkBlank(obj[4]));
            model.setNgaySinh(ConvertUtils.convertDateToString((Date) DBUtils.checkBlank(obj[5])));
            model.setGioiTinh(ConvertUtils.convertBooleanToString((boolean) DBUtils.checkBlank(obj[6])));
            model.setDiaChi((String) DBUtils.checkBlank(obj[7]));
            model.setTrangThai((String) DBUtils.checkBlank(obj[8]));
        } catch (Exception e) {
            log.error("Set Data Has Error: {}", e);
            return new SinhVienModel();
        }
        return model;
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
                .append("SELECT").append("   sv.ma_sinh_vien,")
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
                .append(" WHERE");

        StringBuilder sqlWhere = new StringBuilder();
        if (ids.length == 1) {
            sqlGetSinhVienByIds.append(" sv.ma_sinh_vien IN ('" + ids[0] + "'");
        } else {
            sqlGetSinhVienByIds.append(" sv.ma_sinh_vien IN (");
            for (int i = 0; i < ids.length; i++) {
                sqlWhere.append("'" + ids[i] + "'");
                sqlWhere.append(",");
            }
            sqlGetSinhVienByIds.append(sqlWhere.toString().substring(0, sqlWhere.toString().lastIndexOf(",")));
        }
        sqlGetSinhVienByIds.append(")");
        sqlGetSinhVienByIds.append(" ORDER BY");
        sqlGetSinhVienByIds.append("   sv.ten_sinh_vien");

        return sqlGetSinhVienByIds.toString();
    }

}
