package com.project.muttley.security;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

  @Value("${jwt.secret}")
  private String secret;

  @Value("${jwt.expiration}")
  private long expirationMs;

  private Key getSignKey() {
    return Keys.hmacShaKeyFor(resolveKeyBytes(secret));
  }

  private static byte[] resolveKeyBytes(String value) {
    try {
      byte[] decoded = Decoders.BASE64.decode(value);
      if (decoded.length >= 32) {
        return decoded;
      }
    } catch (RuntimeException ignored) {
    }
    return sha256(value.getBytes(StandardCharsets.UTF_8));
  }

  private static byte[] sha256(byte[] input) {
    try {
      return MessageDigest.getInstance("SHA-256").digest(input);
    } catch (NoSuchAlgorithmException e) {
      throw new IllegalStateException(e);
    }
  }

  public String generateToken(UserDetails userDetails) {

    return Jwts.builder()
        .subject(userDetails.getUsername())
        .issuedAt(new Date())
        .expiration(new Date(System.currentTimeMillis() + expirationMs))
        .signWith(getSignKey())
        .compact();
  }

  public String extractUsername(String token) {
    return Jwts.parser()
        .verifyWith((SecretKey) getSignKey())
        .build()
        .parseSignedClaims(token)
        .getPayload()
        .getSubject();
  }
}