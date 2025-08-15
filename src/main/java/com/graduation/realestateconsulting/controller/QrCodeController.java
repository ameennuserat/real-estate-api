package com.graduation.realestateconsulting.controller;

import com.graduation.realestateconsulting.services.QrCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/qr-code")
public class QrCodeController {
    private final QrCodeService qrCodeService;


    @GetMapping(value = "/{expertId}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getExpertQrCode(@PathVariable Long expertId) {

        try {
            byte[] qrCodeImage = qrCodeService.generateQrCode(expertId, 250, 250);
            return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(qrCodeImage);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
