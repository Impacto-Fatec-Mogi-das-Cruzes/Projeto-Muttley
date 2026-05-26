package com.project.muttley.services.storage;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.project.muttley.configuration.S3Properties;
import com.project.muttley.exceptions.FileStorageException;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
@RequiredArgsConstructor
public class S3StorageService {

  private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of(
      "image/png",
      "image/jpeg",
      "image/jpg",
      "image/webp");

  private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;

  private final S3Client s3Client;
  private final S3Properties s3Properties;

  public String uploadEventSignature(MultipartFile file, UUID eventId) {
    validateBucketConfigured();
    validateSignatureFile(file);

    String extension = resolveExtension(file);
    String key = "events/" + eventId + "/signature-" + UUID.randomUUID() + extension;

    try {
      PutObjectRequest request = PutObjectRequest.builder()
          .bucket(s3Properties.bucket())
          .key(key)
          .contentType(file.getContentType())
          .build();

      s3Client.putObject(
          request,
          RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

      return buildPublicUrl(key);
    } catch (IOException ex) {
      throw new FileStorageException("Falha ao ler o arquivo de assinatura", ex);
    } catch (Exception ex) {
      throw new FileStorageException("Falha ao enviar assinatura para o S3", ex);
    }
  }

  public String uploadEventQrCode(byte[] content, UUID eventId, String qrType) {
    validateBucketConfigured();
    String key = "events/" + eventId + "/" + qrType + "-qrcode.png";
    try (InputStream stream = new java.io.ByteArrayInputStream(content)) {
      PutObjectRequest request = PutObjectRequest.builder()
          .bucket(s3Properties.bucket())
          .key(key)
          .contentType("image/png")
          .build();
      s3Client.putObject(request, RequestBody.fromInputStream(stream, content.length));
      return buildPublicUrl(key);
    } catch (IOException ex) {
      throw new FileStorageException("Falha ao ler o QR Code", ex);
    } catch (Exception ex) {
      throw new FileStorageException("Falha ao enviar QR Code para o S3", ex);
    }
  }

  public void deleteByUrl(String fileUrl) {
    if (!StringUtils.hasText(fileUrl)) {
      return;
    }

    String key = extractKeyFromUrl(fileUrl);
    if (key == null) {
      return;
    }

    try {
      s3Client.deleteObject(DeleteObjectRequest.builder()
          .bucket(s3Properties.bucket())
          .key(key)
          .build());
    } catch (Exception ex) {
      throw new FileStorageException("Falha ao remover arquivo do S3", ex);
    }
  }

  private void validateBucketConfigured() {
    if (!StringUtils.hasText(s3Properties.bucket())) {
      throw new FileStorageException("Bucket S3 não configurado (aws.s3.bucket)");
    }
  }

  private void validateSignatureFile(MultipartFile file) {
    if (file == null || file.isEmpty()) {
      throw new FileStorageException("Arquivo de assinatura é obrigatório");
    }
    if (file.getSize() > MAX_FILE_SIZE) {
      throw new FileStorageException("A assinatura deve ter no máximo 5MB");
    }
    String contentType = file.getContentType();
    if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType.toLowerCase())) {
      throw new FileStorageException("A assinatura deve ser PNG, JPEG ou WEBP");
    }
  }

  private String resolveExtension(MultipartFile file) {
    String original = file.getOriginalFilename();
    if (original != null && original.contains(".")) {
      return original.substring(original.lastIndexOf('.')).toLowerCase();
    }
    return switch (file.getContentType()) {
      case "image/png" -> ".png";
      case "image/webp" -> ".webp";
      default -> ".jpg";
    };
  }

  private String buildPublicUrl(String key) {
    if (StringUtils.hasText(s3Properties.publicBaseUrl())) {
      String base = s3Properties.publicBaseUrl().replaceAll("/$", "");
      return base + "/" + key;
    }
    return String.format(
        "https://%s.s3.%s.amazonaws.com/%s/%s",
        s3Properties.bucket(),
        s3Properties.region(),
        s3Properties.bucket(),
        key);
  }

  private String extractKeyFromUrl(String fileUrl) {
    if (StringUtils.hasText(s3Properties.publicBaseUrl())
        && fileUrl.startsWith(s3Properties.publicBaseUrl())) {
      String base = s3Properties.publicBaseUrl().replaceAll("/$", "");
      return fileUrl.substring(base.length() + 1);
    }

    String marker = ".amazonaws.com/";
    int index = fileUrl.indexOf(marker);
    if (index >= 0) {
      String extracted = fileUrl.substring(index + marker.length());
      String bucketPrefix = s3Properties.bucket() + "/";
      if (extracted.startsWith(bucketPrefix)) {
        return extracted.substring(bucketPrefix.length());
      }
      return extracted;
    }

    return null;
  }
}
