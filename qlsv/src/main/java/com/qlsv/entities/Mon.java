package com.qlsv.entities;

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
@Table(name = "mon")
public class Mon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_mon", nullable = false, unique = true)
    private int maMon;

    @Column(name = "ten_mon", nullable = false, length = 100)
    private String tenMon;

    @Column(name = "so_dvht", nullable = false, length = 100)
    private String soDVHT;

    @OneToMany(mappedBy = "mon")
    private List<KetQua> ketQuas;

    @ManyToOne
    @JoinColumn(name = "ma_giang_vien", nullable = false)
    private GiangVien giangVien;

}
