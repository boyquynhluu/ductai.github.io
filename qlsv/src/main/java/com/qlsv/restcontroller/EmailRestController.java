package com.qlsv.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qlsv.model.EmailDetails;
import com.qlsv.service.EmailService;

@RestController
@RequestMapping(value = "/api/")
public class EmailRestController {

    @Autowired
    private EmailService emailService;

    // Sending a simple Email
    @PostMapping("sendMail")
    public String sendMail(@RequestBody EmailDetails details) throws Exception {
        String status = emailService.sendSimpleMail(details);

        return status;
    }

    // Sending email with attachment
    @PostMapping("sendMailWithAttachment")
    public String sendMailWithAttachment(@ModelAttribute EmailDetails details) throws Exception {
        String status = emailService.sendMailWithAttachment(details);

        return status;
    }
}
