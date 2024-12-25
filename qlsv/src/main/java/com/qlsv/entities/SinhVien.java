package com.qlsv.entities;

import java.sql.Date;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "sinh_vien")
public class SinhVien {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_sinh_vien", nullable = false, unique = true)
    private int maSV;

    @Column(name = "ten_sinh_vien", nullable = false, length = 100)
    private String tenSinhVien;

    @Column(name = "tuoi", nullable = false, length = 10)
    private int tuoi;

    @Column(name = "phone", nullable = false, length = 10)
    private String phone;

    @Column(name = "email", nullable = false, length = 50)
    private String email;
    
    @Column(name = "trang_thai", nullable = false, length = 50)
    private String trangThai;

    @Column(name = "ngay_sinh", nullable = false, length = 100)
    private Date date;

    @Column(name = "gioi_tinh", nullable = false, length = 1)
    private Boolean gioiTinh;

    @Column(name = "dia_chi", nullable = false, length = 250)
    private String diaChi;

    @ManyToOne
    @JoinColumn(name = "ma_lop", nullable = false)
    private Lop lop;

    @OneToMany(mappedBy = "sinhVien")
    private List<KetQua> ketQuas;
}
