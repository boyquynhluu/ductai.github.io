package com.qlsv.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "ket_qua")
public class KetQua {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_ket_qua", nullable = false, unique = true)
    private int maKQ;

    @Column(name = "diem_trung_binh", nullable = false, precision = 10, scale = 2)
    private String diemTrungBinh;

    @Column(name = "diem_thi_lan1", nullable = false, precision = 10, scale = 2)
    private String diemLan1;

    @Column(name = "diem_thi_lan2", nullable = false, precision = 10, scale = 2)
    private String diemLan2;

    @Column(name = "diem_tong_ket", nullable = false, precision = 10, scale = 2)
    private String diemTongKet;

    @Column(name = "hanh_kiem", nullable = false, length = 50)
    private String hanhKiem;

    @Column(name = "hoc_ki", nullable = false, length = 50)
    private String hocKi;

    @Column(name = "ghi_chu", nullable = false, length = 50)
    private String ghiChu;

    @ManyToOne
    @JoinColumn(name = "ma_sinh_vien", nullable = false)
    private SinhVien sinhVien;

    @ManyToOne
    @JoinColumn(name = "ma_mon", nullable = false)
    private Mon mon;
}
