package com.qlsv.controller;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qlsv.model.EmailDetails;
import com.qlsv.service.EmailService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/sendMail")
@Slf4j(topic = "Email Controller")
public class EmailRestController {

    private final EmailService emailService;

    // Sending a simple Email
    @PostMapping
    public String sendMail(@RequestBody EmailDetails details) {
        log.info("Send Mail Not Attachment...");
        return emailService.sendSimpleMail(details);
    }

    // Sending email with attachment
    @PostMapping("/sendMailWithAttachment")
    public String sendMailWithAttachment(@ModelAttribute EmailDetails details) {
        log.info("Send Mail With Attachment...");
        return emailService.sendMailWithAttachment(details);
    }
}
