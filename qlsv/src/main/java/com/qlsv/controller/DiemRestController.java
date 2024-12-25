package com.qlsv.controller;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qlsv.model.DiemModel;
import com.qlsv.resource.DiemResource;
import com.qlsv.service.DiemService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/")
public class DiemRestController {

    private final DiemService service;
    private final ModelMapper mapper;

    @GetMapping(value = "diems", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<DiemResource> getAllSinhVien() throws Exception {
        List<DiemResource> resources = new ArrayList<>();

        // Get All Sinh Vien
        List<DiemModel> models = service.getAllSinhVien();

        // Mapper Models to Resources
        if (!CollectionUtils.isEmpty(models)) {
            Type listType = new TypeToken<List<DiemResource>>() {
            }.getType();

            resources = mapper.map(models, listType);
        }
        return resources;
    }
}
