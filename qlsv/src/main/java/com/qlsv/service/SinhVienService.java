package com.qlsv.service;

import java.util.List;

import com.qlsv.model.SinhVienModel;

public interface SinhVienService {

    public List<SinhVienModel> getSinhViens() throws Exception;

    public List<SinhVienModel> getSinhVienByIds(String[] ids) throws Exception;

    public List<SinhVienModel> searchSinhVien(String input) throws Exception;
}
