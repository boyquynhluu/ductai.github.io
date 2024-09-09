package com.qlsv.service;

import com.qlsv.model.EmailDetails;

public interface EmailService {

    public String sendSimpleMail(EmailDetails details) throws Exception;

    public String sendMailWithAttachment(EmailDetails details) throws Exception;
}
