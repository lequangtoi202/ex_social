package com.lqt.service;

import com.lqt.request.MailRequest;

public interface MailService {
    void sendMailForgotPassword(String recipients, String resetPasswordLink);
    void sendMailToEachPersonGroup(MailRequest mailRequest);
}
