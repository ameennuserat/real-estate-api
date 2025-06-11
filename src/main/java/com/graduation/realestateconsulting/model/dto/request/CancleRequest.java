package com.graduation.realestateconsulting.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CancleRequest {
    private Long id;
    private String cancellationReason;
}
