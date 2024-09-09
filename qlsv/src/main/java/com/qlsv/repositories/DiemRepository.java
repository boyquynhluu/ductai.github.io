package com.qlsv.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qlsv.entities.KetQua;

@Repository
public interface DiemRepository extends JpaRepository<KetQua, Integer> {

}
