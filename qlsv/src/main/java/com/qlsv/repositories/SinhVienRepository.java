package com.qlsv.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qlsv.entities.SinhVien;

@Repository
public interface SinhVienRepository extends JpaRepository<SinhVien, Integer> {

    SinhVien getById(int id);

}
