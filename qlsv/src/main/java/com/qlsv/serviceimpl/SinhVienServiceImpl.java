package com.qlsv.serviceimpl;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qlsv.entities.SinhVien;
import com.qlsv.model.SinhVienModel;
import com.qlsv.repositories.SinhVienRepository;
import com.qlsv.service.SinhVienService;
import com.qlsv.utils.ConvertUtils;
import com.qlsv.utils.DBUtils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.SystemException;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@SuppressWarnings("unchecked")
@Service
@Transactional(rollbackFor = Exception.class)
public class SinhVienServiceImpl implements SinhVienService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private SinhVienRepository repo;

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
    public List<SinhVienModel> getSinhViens() throws Exception {
        List<SinhVienModel> models = new ArrayList<>();
        List<Object[]> sinhVienEnityList = entityManager.createNativeQuery(SQL_GET_ALL_SINH_VIEN.toString())
                .getResultList();
        for (Object[] obj : sinhVienEnityList) {
            models.add(this.setData(obj));
        }

        return CollectionUtils.isEmpty(models) ? Collections.emptyList() : models;
    }

    @Override
    public List<SinhVienModel> getSinhVienByIds(String[] ids) throws Exception {
        List<SinhVienModel> models = new ArrayList<>();
        try {
            List<Object[]> sinhViens = entityManager.createNativeQuery(this.createSQLGetSinhVienByIds(ids))
                    .getResultList();

            for (Object[] obj : sinhViens) {
                models.add(this.setData(obj));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new SystemException("Get Sinh Vien Error!");
        }

        return CollectionUtils.isEmpty(models) ? Collections.emptyList() : models;
    }

    @Override
    public List<SinhVienModel> searchSinhVien(String input) throws Exception {
        List<SinhVienModel> models = new ArrayList<>();
        StringBuilder sql = new StringBuilder().append("SELECT").append("   sv.ma_sinh_vien,")
                .append("   sv.ten_sinh_vien,").append("   sv.tuoi,").append("   sv.phone,").append("   sv.email,")
                .append("   sv.ngay_sinh,").append("   sv.gioi_tinh,").append("   sv.dia_chi,")
                .append("   sv.trang_thai").append(" FROM").append("   Sinh_Vien sv").append("   WHERE")
                .append("   sv.ten_sinh_vien LIKE  '" + '%' + "" + input + "" + '%' + "' ").append(" OR")
                .append("   sv.email LIKE  '" + '%' + "" + input + "" + '%' + "' ").append(" OR")
                .append("   sv.phone LIKE  '" + '%' + "" + input + "" + '%' + "' ").append(" ORDER BY")
                .append("   sv.ten_sinh_vien");

        List<Object[]> sinhviens = entityManager.createNativeQuery(sql.toString()).getResultList();
        for (Object[] obj : sinhviens) {
            models.add(this.setData(obj));
        }
        return CollectionUtils.isEmpty(models) ? Collections.emptyList() : models;
    }

    @Override
    public void createSinhVien(SinhVienModel model) throws Exception {
        SinhVien sv = new SinhVien();
        sv.setTenSinhVien(model.getTenSinhVien());
        sv.setTuoi(model.getTuoi());
        sv.setPhone(model.getPhone());
        sv.setEmail(model.getEmail());
        sv.setDate(ConvertUtils.convertStringToDate(model.getNgaySinh()));
        sv.setGioiTinh(ConvertUtils.convertStringToBoolean(model.getGioiTinh()));
        sv.setDiaChi(model.getDiaChi());
        sv.setTrangThai(model.getTrangThai());

        repo.save(sv);
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
        model.setMaSV((int) obj[0]);
        model.setTenSinhVien((String) DBUtils.checkBlank(obj[1]));
        model.setTuoi((int) DBUtils.checkBlank(obj[2]));
        model.setPhone((String) DBUtils.checkBlank(obj[3]));
        model.setEmail((String) DBUtils.checkBlank(obj[4]));
        model.setNgaySinh(ConvertUtils.convertDateToString((Date) DBUtils.checkBlank(obj[5])));
        model.setGioiTinh(ConvertUtils.convertBooleanToString((boolean) DBUtils.checkBlank(obj[6])));
        model.setDiaChi((String) DBUtils.checkBlank(obj[7]));
        model.setTrangThai((String) DBUtils.checkBlank(obj[8]));

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
        StringBuilder sqlGetSinhVienByIds = new StringBuilder().append("SELECT").append("   sv.ma_sinh_vien,")
                .append("   sv.ten_sinh_vien,").append("   sv.tuoi,").append("   sv.phone,").append("   sv.email,")
                .append("   sv.ngay_sinh,").append("   sv.gioi_tinh,").append("   sv.dia_chi,")
                .append("   sv.trang_thai").append(" FROM").append("   Sinh_Vien sv").append(" WHERE");

        String sqlWhere = "";
        if (ids.length > 1) {
            sqlGetSinhVienByIds.append(" sv.ma_sinh_vien IN (");
            for (int i = 0; i < ids.length; i++) {
                sqlWhere += ("'" + ids[i] + "'");
                sqlWhere += (",");
            }
            sqlGetSinhVienByIds.append(sqlWhere.substring(0, sqlWhere.lastIndexOf(",")));
        } else if (ids.length == 1) {
            sqlGetSinhVienByIds.append(" sv.ma_sinh_vien IN ('" + ids[0] + "'");
        }
        sqlGetSinhVienByIds.append(")");
        sqlGetSinhVienByIds.append(" ORDER BY");
        sqlGetSinhVienByIds.append("   sv.ten_sinh_vien");

        return sqlGetSinhVienByIds.toString();
    }

}
