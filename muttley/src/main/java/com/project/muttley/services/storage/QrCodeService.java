package com.project.muttley.services.storage;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

import org.springframework.stereotype.Service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.project.muttley.exceptions.FileStorageException;

import javax.imageio.ImageIO;

@Service
public class QrCodeService {

  private static final int QR_SIZE = 350;

  public byte[] generatePng(String content) {
    try {
      BitMatrix matrix = new QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, QR_SIZE, QR_SIZE);
      BufferedImage image = MatrixToImageWriter.toBufferedImage(matrix);
      ByteArrayOutputStream output = new ByteArrayOutputStream();
      ImageIO.write(image, "PNG", output);
      return output.toByteArray();
    } catch (WriterException | java.io.IOException ex) {
      throw new FileStorageException("Falha ao gerar QR Code", ex);
    }
  }
}
