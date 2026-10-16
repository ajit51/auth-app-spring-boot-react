package com.auth.auth_app_backend.security;

import com.auth.auth_app_backend.entities.Role;
import com.auth.auth_app_backend.entities.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.crypto.SecretKey;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Getter
@Setter
public class JwtService {
  private final SecretKey key;
  private final long accessTtlSeconds;
  private final long refreshTtlSeconds;
  private final String issuer;

  public JwtService(
      @Value("${security.jwt.secret}") String secret,
      @Value("${security.jwt.access-ttl-seconds}") long accessTtlSeconds,
      @Value("${security.jwt.refresh-ttl-seconds}") long refreshTtlSeconds,
      @Value("${security.jwt.issuer}") String issuer) {
    if (secret == null || secret.length() < 64) {
      throw new IllegalArgumentException("Invalid exception");
    }
    this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    this.accessTtlSeconds = accessTtlSeconds;
    this.refreshTtlSeconds = refreshTtlSeconds;
    this.issuer = issuer;
  }

  // generate token
  public String generateAccessToken(User user) {
    Instant now = Instant.now();
    List<?> roles =
        user.getRoles() == null ? List.of() : user.getRoles().stream().map(Role::getName).toList();

    return Jwts.builder()
        .id(UUID.randomUUID().toString())
        .subject(user.getId().toString())
        .issuer(issuer)
        .issuedAt(Date.from(now))
        .expiration(Date.from(now.plusSeconds(accessTtlSeconds)))
        .claims(Map.of("email", user.getEmail(), "roles", roles, "type", "access"))
        .signWith(key, SignatureAlgorithm.HS512)
        .compact();
  }

  // generate refresh token
  public String generateRefreshToken(User user, String jti) {
    Instant now = Instant.now();
    List<?> roles =
        user.getRoles() == null ? List.of() : user.getRoles().stream().map(Role::getName).toList();

    return Jwts.builder()
        .id(jti)
        .subject(user.getId().toString())
        .issuer(issuer)
        .issuedAt(Date.from(now))
        .expiration(Date.from(now.plusSeconds(refreshTtlSeconds)))
        .claim("type", "refresh")
        .signWith(key, SignatureAlgorithm.HS512)
        .compact();
  }

  // parse the token
  public Jws<Claims> parse(String token) {
    return Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
  }

  public boolean isAccessToken(String token) {
    Claims claims = parse(token).getPayload();
    return "access".equals(claims.get("type"));
  }

  public boolean isRefreshToken(String token) {
    Claims claims = parse(token).getPayload();
    return "refresh".equals(claims.get("type"));
  }

  public UUID getUserId(String token) {
    Claims claims = parse(token).getPayload();
    return UUID.fromString(claims.getSubject());
  }

  public String getJti(String token) {
    return parse(token).getPayload().getId();
  }
}
