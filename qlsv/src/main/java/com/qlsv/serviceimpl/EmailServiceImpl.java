package com.qlsv.serviceimpl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.qlsv.constants.Constants;
import com.qlsv.entities.SinhVien;
import com.qlsv.model.EmailDetails;
import com.qlsv.repositories.SinhVienRepository;
import com.qlsv.service.EmailService;
import com.qlsv.utils.ConvertUtils;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j(topic = "Email-Service")
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final SinhVienRepository repo;
    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sender;

    /**
     * Send Mail not attachment
     * 
     * @param details
     * @return String
     * @throws Exception
     */
    @Override
    public String sendSimpleMail(EmailDetails details) {
        // Try block to check for exceptions
        try {
            // Creating a simple mail message
            MimeMessage mailMessage = javaMailSender.createMimeMessage();

            // Setting up necessary details
            mailMessage.setFrom(sender);
            mailMessage.setRecipients(MimeMessage.RecipientType.TO, details.getRecipient());
            mailMessage.setSubject(details.getSubject());

            String htmlTemplate = new String(Files.readAllBytes(Paths.get(
                    "D:\\workspace\\ductai.github.io\\qlsv\\src\\main\\resources\\templates\\mail\\template-mail.html")));
            htmlTemplate = htmlTemplate.replace("${name}", "NamLD");
            mailMessage.setContent(htmlTemplate, "text/html; charset=utf-8");

            // Sending the mail
            javaMailSender.send(mailMessage);
            return "Mail Send Successfully...";
        } catch (Exception e) {
            log.error("Send mail has error: {}", e);
            return "Error Send Mail...";
        }
    }

    /**
     * Send Mail has attachment
     * 
     * @param details
     * @return String
     * @throws IOException 
     * @throws Exception
     */
    @Override
    public String sendMailWithAttachment(EmailDetails details) {
        // Creating a mime message
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper;

        try {
            // Convert String[] ids to List<Integer>
            List<Integer> integerList = Arrays.stream(details.getIds()).map(Integer::parseInt)
                    .collect(Collectors.toList());

            // Use List<Integer> as Iterable<Integer>
            Iterable<Integer> integerIterable = integerList;

            // Get sinh vien by id
            List<SinhVien> sinhviens = Optional.ofNullable(repo.findAllById(integerIterable))
                    .filter(list -> !list.isEmpty())
                    .orElseThrow(() -> new EntityNotFoundException("No SinhVien objects found for the provided IDs."));

            for (SinhVien sv : sinhviens) {
                // Set info send mail
                mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
                mimeMessageHelper.setFrom(sender);
                mimeMessageHelper.setTo(sv.getEmail());
                mimeMessageHelper.setSubject(details.getSubject());

                String htmlTemplate = new String(Files.readAllBytes(Paths.get(
                        "D:\\workspace\\ductai.github.io\\qlsv\\src\\main\\resources\\templates\\mail\\template-mail.html")));
                htmlTemplate = htmlTemplate.replace("${name}", sv.getTenSinhVien());
                htmlTemplate = htmlTemplate.replace("${body}", details.getMsgBody());

                htmlTemplate = htmlTemplate.replace("${maSV}", String.valueOf(sv.getMaSV()));
                htmlTemplate = htmlTemplate.replace("${tenSinhVien}", sv.getTenSinhVien());
                htmlTemplate = htmlTemplate.replace("${tuoi}", String.valueOf(sv.getTuoi()));
                htmlTemplate = htmlTemplate.replace("${phone}", sv.getPhone());
                htmlTemplate = htmlTemplate.replace("${email}", sv.getEmail());
                htmlTemplate = htmlTemplate.replace("${ngaySinh}", String.valueOf(sv.getDate()));
                htmlTemplate = htmlTemplate.replace("${gioiTinh}",
                        ConvertUtils.convertBooleanToString(sv.getGioiTinh()));
                htmlTemplate = htmlTemplate.replace("${diaChi}", sv.getDiaChi());
                htmlTemplate = htmlTemplate.replace("${trangThai}", sv.getTrangThai());

                mimeMessageHelper.setText(Constants.CONTENT_TYPE_TEXT_HTML, htmlTemplate);

                // Check attachment
                if (details.getAttachment() != null) {
                    // Get file attachment
                    File filePath = this.exportFile(details.getAttachment());
                    // Adding the attachment
                    FileSystemResource file = new FileSystemResource(filePath);
                    mimeMessageHelper.addAttachment(file.getFilename(), file);
                }
                log.info("Send Mail...");
                // Sending the mail
                javaMailSender.send(mimeMessage);
            }
            return "Mail sent Successfully";
        } catch (MessagingException e) {
            log.error("Send mail has error: {}", e);
            return "Error Send Mail...";
        } catch (IOException e) {
            log.error("Do not get template send mail...");
            return "Error Send Mail...";
        }
    }

    /**
     * Export file
     * 
     * @param fileInput
     * @return filePath File
     * @throws Exception
     */
    private File exportFile(MultipartFile fileInput) {
        File folderPath = null;
        File filePath = null;
        try {
            InputStream initialStream = fileInput.getInputStream();
            byte[] buffer = new byte[initialStream.available()];
            initialStream.read(buffer);

            // Create folder name
            DateFormat folderDateFormater = new SimpleDateFormat(Constants.YYYYMMDD);
            String currentDateTimeFolder = folderDateFormater.format(new Date());

            folderPath = new File(Constants.PATH_EXPORT_EXCEL + currentDateTimeFolder);
            filePath = new File(folderPath, fileInput.getOriginalFilename());

            // Create folder
            if (!folderPath.exists()) {
                folderPath.mkdirs();
            }

            // Create file
            if (!filePath.exists()) {
                filePath.createNewFile();
            }

            try (OutputStream outStream = new FileOutputStream(filePath)) {
                outStream.write(buffer);
            } catch (IOException e) {
                log.error("Write file error: {}", e);
            }
        } catch (IOException e) {
            log.error("File attachment has error: {}", e);
        } catch (Exception e) {
            log.error("File attachment has error: {}", e);
        }
        return filePath;
    }
}
