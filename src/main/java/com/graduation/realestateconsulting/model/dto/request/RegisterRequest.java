package com.graduation.realestateconsulting.model.dto.request;

import com.graduation.realestateconsulting.model.enums.Role;
import io.micrometer.common.lang.Nullable;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Data
@Setter
@Getter
@AllArgsConstructor
public class RegisterRequest {
    @NotBlank(message = "First name is required")
    private String firstName;
    @NotBlank(message = "Last name is required")
    private String lastName;
    @NotBlank(message = "Email name is required")
    private String email;
    @NotBlank(message = "Password name is required")
    private String password;
    @NotBlank(message = "Phone name is required")
    private String phone;
    private Role role;
    // for office
    private String location;
    private Double latitude;
    private Double longitude;
    private MultipartFile commercialRegisterImage;
    // for expert
    private MultipartFile idCardImage;
    private MultipartFile degreeCertificateImage;
    private String profession;
    private String experience;
    private Double rating;
    // for both (expert && office)
    private String bio;
}