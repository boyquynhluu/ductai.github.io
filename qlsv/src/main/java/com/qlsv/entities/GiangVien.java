package com.qlsv.entities;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "giang_vien")
public class GiangVien {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_giang_vien", nullable = false, unique = true)
    private int maGV;

    @Column(name = "ten_giang_vien", nullable = false, length = 100)
    private String tenGV;

    @Column(name = "gioi_tinh", nullable = false, length = 1)
    private boolean gioiTinh;

    @Column(name = "phone", nullable = false, length = 10)
    private int phone;

    @Column(name = "email", nullable = false, length = 50)
    private String email;

    @OneToMany(mappedBy = "giangVien")
    private List<Mon> mons;
}
