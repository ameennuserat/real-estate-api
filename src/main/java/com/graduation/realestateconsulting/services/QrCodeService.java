package com.graduation.realestateconsulting.services;

import com.google.zxing.WriterException;

import java.io.IOException;

public interface QrCodeService {
    byte[] generateQrCode(Long expertId, int width, int height) throws WriterException, IOException;
}
