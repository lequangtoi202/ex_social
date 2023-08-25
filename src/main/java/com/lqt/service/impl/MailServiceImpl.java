package com.lqt.service.impl;

import com.lqt.controllers.GroupController;
import com.lqt.request.MailRequest;
import com.lqt.service.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class MailServiceImpl implements MailService {
    @Autowired
    private JavaMailSender mailSender;

    private static final Logger logger = LoggerFactory.getLogger(GroupController.class);

    @Override
    @Async
    public void sendMailForgotPassword(String recipients, String resetPasswordLink) throws MessagingException {
        logger.debug("RUN JOB: sendMailForgotPassword");
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(recipients);
        helper.setFrom("2051052140toi@ou.edu.vn");
        String subject = "Here is the link to reset your password";
        String content = "<p>Hello,</p>" +
                "<p>You have request to reset your password.</p>"
                + "<p>Click link below to change your password:</p>"
                + "<p><b><a href=\"" + resetPasswordLink + "\">Change my password</a></b></p>"
                + "<p>Ignore this email if you do remember your password, or you have not make a request</p>";
        helper.setSubject(subject);
        helper.setText(content, true);

        mailSender.send(message);
        logger.debug("END JOB: sendMailForgotPassword");
    }

    @Override
    @Async
    public void sendMailToEachPersonGroup(MailRequest mailRequest) {
        logger.debug("RUN JOB: sendMailToEachPersonGroup");
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mailRequest.getRecipients());
        message.setFrom(mailRequest.getFrom());
        message.setSubject(mailRequest.getSubject());
        message.setText(mailRequest.getBody());

        mailSender.send(message);
        logger.debug("END JOB: sendMailToEachPersonGroup");
    }

    @Override
    public void sendMailAccountToLecturer(MailRequest mailRequest) {
        logger.debug("RUN JOB: sendMailToEachPersonGroup");
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mailRequest.getRecipients());
        message.setFrom(mailRequest.getFrom());
        message.setSubject(mailRequest.getSubject());
        message.setText(mailRequest.getBody());

        mailSender.send(message);
        logger.debug("END JOB: sendMailToEachPersonGroup");
    }
}
