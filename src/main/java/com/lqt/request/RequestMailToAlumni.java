package com.lqt.request;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestMailToAlumni {
    private String subject;
    private String body;
    private String from;
}
