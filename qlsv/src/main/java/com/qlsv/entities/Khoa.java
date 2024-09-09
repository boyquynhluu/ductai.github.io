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
@Table(name = "khoa")
public class Khoa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_khoa", nullable = false, unique = true)
    private int maKhoa;

    @Column(name = "ten_khoa", nullable = false, length = 50)
    private String tenKhoa;

    @OneToMany(mappedBy = "khoa")
    private List<Lop> lops;
}
