package com.qlsv.serviceimpl;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.qlsv.model.DiemModel;
import com.qlsv.service.DiemService;
import com.qlsv.utils.ConvertUtils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j(topic = "Diem Service")
public class DiemServiceImpl implements DiemService {

    @PersistenceContext
    private EntityManager entityManager;

    // SQL get all sinh vien
    private static final StringBuilder GET_ALL_SINH_VIEN = new StringBuilder()
            .append("select ")
            .append("    sv.ma_sinh_vien,")
            .append("    sv.ten_sinh_vien,")
            .append("    sv.gioi_tinh,")
            .append("    sv.ngay_sinh,")
            .append("    sv.dia_chi,")
            .append("    kq.ma_ket_qua,")
            .append("    kq.ghi_chu,")
            .append("    kq.hanh_kiem,")
            .append("    kq.hoc_ki,")
            .append("    kq.diem_thi_lan1,")
            .append("    kq.diem_thi_lan2,")
            .append("    kq.diem_trung_binh,")
            .append("    kq.diem_tong_ket,")
            .append("    m.ma_mon,")
            .append("    m.ten_mon,")
            .append("    m.so_dvht")
            .append(" from")
            .append("    sinh_vien sv,")
            .append("    ket_qua kq,")
            .append("    mon m")
            .append(" where")
            .append("    sv.ma_sinh_vien = kq.ma_sinh_vien")
            .append("    and m.ma_mon = kq.ma_mon");

    @SuppressWarnings("unchecked")
    @Override
    public List<DiemModel> getAllSinhVien() throws Exception {
        log.info("Get All Sinh Vien");
        List<DiemModel> models = new ArrayList<>();
        try {
            List<Object[]> entities = entityManager.createNativeQuery(GET_ALL_SINH_VIEN.toString()).getResultList();
            for (Object[] object : entities) {
                models.add(this.setData(object));
            }
        } catch (Exception e) {
            log.error("Get Diem Error: {}", e);
            return Collections.emptyList();
        }
        return CollectionUtils.isEmpty(models) ? Collections.emptyList() : models;
    }

    /**
     * Set data from entity to model
     * 
     * @param object Object[]
     * @return model SinhVienModel
     */
    private DiemModel setData(Object[] object) throws Exception {
        DiemModel model = new DiemModel();
        model.setMaSV((int) object[0]);
        model.setTenSinhVien((String) object[1]);
        model.setGioiTinh(ConvertUtils.convertBooleanToString((Boolean) object[2]));
        model.setNgaySinh(ConvertUtils.convertDateToString((Date) object[3]));
        model.setDiaChi((String) object[4]);
        model.setMaKQ((int) object[5]);
        model.setGhiChu((String) object[6]);
        model.setHanhKiem((String) object[7]);
        model.setHocKi((String) object[8]);
        model.setDiemLan1((String) object[9]);
        model.setDiemLan2((String) object[10]);
        model.setDiemTrungBinh((String) object[11]);
        model.setDiemTongKet((String) object[12]);
        model.setMaMon((int) object[13]);
        model.setTenMon((String) object[14]);
        model.setSoDVHT((String) object[15]);

        return model;
    }
}
