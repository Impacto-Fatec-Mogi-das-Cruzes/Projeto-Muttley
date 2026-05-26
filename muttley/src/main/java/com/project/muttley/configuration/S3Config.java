package com.project.muttley.configuration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;

@Configuration
@EnableConfigurationProperties(S3Properties.class)
public class S3Config {

  @Bean
  S3Client s3Client(S3Properties properties) {
    S3ClientBuilder builder = S3Client.builder()
        .region(Region.of(properties.region()));
    System.out.println(properties.endpoint());
    if (StringUtils.hasText(properties.accessKeyId())
        && StringUtils.hasText(properties.secretAccessKey())) {
      builder.credentialsProvider(StaticCredentialsProvider.create(
          AwsBasicCredentials.create(properties.accessKeyId(), properties.secretAccessKey())));
    }

    if (StringUtils.hasText(properties.endpoint())) {
      builder.endpointOverride(java.net.URI.create(properties.endpoint()));
      builder.forcePathStyle(true);
    }

    return builder.build();
  }
}
