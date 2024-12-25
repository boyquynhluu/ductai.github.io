package com.qlsv.service;

import java.util.List;

import com.qlsv.model.SinhVienModel;

public interface SinhVienService {

    public List<SinhVienModel> getSinhViens();

    public List<SinhVienModel> getSinhVienByIds(String[] ids);

    public List<SinhVienModel> searchSinhVien(String input);
    
    public void createSinhVien(SinhVienModel model);
}
