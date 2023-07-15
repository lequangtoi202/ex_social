package com.lqt.request;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MailRequest {
    private String subject;
    private String body;
    private String date;
    private String from;
    private String recipients;
}
