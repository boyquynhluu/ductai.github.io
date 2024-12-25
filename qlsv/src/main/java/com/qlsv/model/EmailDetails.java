package com.qlsv.model;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailDetails {
    private String[] ids;
    private String recipient;
    private String msgBody;
    private String subject;
    private MultipartFile attachment;
}
