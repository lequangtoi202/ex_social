package com.lqt.service;

import com.lqt.request.MailRequest;

import javax.mail.MessagingException;

public interface MailService {
    void sendMailForgotPassword(String recipients, String resetPasswordLink) throws MessagingException;

    void sendMailToEachPersonGroup(MailRequest mailRequest);
}
