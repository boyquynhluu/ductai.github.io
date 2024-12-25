package com.qlsv.resource;

import com.qlsv.exceptionhandler.ValidEmail;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SinhVienResource {
    private int maSV;
    private String tenSinhVien;
    private int tuoi;
    private String phone;
    @ValidEmail
    private String email;
    private String ngaySinh;
    private String gioiTinh;
    private String diaChi;
    private String trangThai;
    private String search;
}
