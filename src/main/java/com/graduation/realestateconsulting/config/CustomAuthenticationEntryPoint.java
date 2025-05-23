package com.graduation.realestateconsulting.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.graduation.realestateconsulting.exceptions.ExceptionDto;
import com.graduation.realestateconsulting.model.dto.response.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException {

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");

        ExceptionDto error = ExceptionDto.builder()
                .timestamp(new Date())
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .message("You must be logged in to access this resource")
                .build();

        ExceptionResponse body = ExceptionResponse.builder()
                .status("Failure")
                .message("Authentication required")
                .errors(List.of(error))
                .build();

        new ObjectMapper().writeValue(response.getOutputStream(), body);
    }
}
