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
@Table(name = "lop")
public class Lop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_lop", nullable = false, unique = true)
    private int maLop;

    @Column(name = "ten_lop", nullable = false, length = 100)
    private String tenLop;

    @ManyToOne
    @JoinColumn(name = "ma_khoa", nullable = false)
    private Khoa khoa;

    @OneToMany(mappedBy = "lop")
    private List<SinhVien> sinhViens;
}
