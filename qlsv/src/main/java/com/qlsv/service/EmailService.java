package com.qlsv.service;

import com.qlsv.model.EmailDetails;

public interface EmailService {

    public String sendSimpleMail(EmailDetails details);

    public String sendMailWithAttachment(EmailDetails details) ;
}
