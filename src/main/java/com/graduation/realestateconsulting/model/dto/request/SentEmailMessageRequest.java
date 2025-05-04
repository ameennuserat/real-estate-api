package com.graduation.realestateconsulting.model.dto.request;

import lombok.*;
import org.springframework.stereotype.Service;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SentEmailMessageRequest {
    private String to;
    private String subject;
    private String body;
}
