package com.auth.auth_app_backend.security;

import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

@Service
@Getter
public class CookieService {
  private final Logger logger = LoggerFactory.getLogger(CookieService.class);

  private final String refreshTokenCookieName;
  private final boolean cookieSecure;
  private final boolean cookieHttpOnly;
  private final String cookieSameSite;
  private final String cookieDomain;

  public CookieService(
      @Value("${security.jwt.refresh-token-cookie-name}") String refreshTokenCookieName,
      @Value("${security.jwt.cookie-secure}") boolean cookieSecure,
      @Value("${security.jwt.cookie-http-only}") boolean cookieHttpOnly,
      @Value("${security.jwt.cookie-same-site}") String cookieSameSite,
      @Value("${security.jwt.cookie-domain}") String cookieDomain) {
    this.refreshTokenCookieName = refreshTokenCookieName;
    this.cookieSecure = cookieSecure;
    this.cookieHttpOnly = cookieHttpOnly;
    this.cookieSameSite = cookieSameSite;
    this.cookieDomain = cookieDomain;
  }

  // create method to attach cookie to response
  public void attachRefreshCookie(HttpServletResponse response, String value, int maxAge) {
    logger.info("Attaching cookie with name: {} and value: {}", refreshTokenCookieName, value);
    ResponseCookie.ResponseCookieBuilder responseCookieBuilder =
        ResponseCookie.from(refreshTokenCookieName, value)
            .httpOnly(cookieHttpOnly)
            .secure(cookieSecure)
            .path("/")
            .maxAge(maxAge)
            .sameSite(cookieSameSite);

    if (cookieDomain != null && !cookieDomain.isBlank()) {
      responseCookieBuilder.domain(cookieDomain);
    }

    ResponseCookie responseCookie = responseCookieBuilder.build();
    response.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
  }

  // clear refresh cookie
  public void clearRefreshCookie(HttpServletResponse response) {
    logger.info("clearing cookie with name: {}", refreshTokenCookieName);
    ResponseCookie.ResponseCookieBuilder cookieBuilder =
        ResponseCookie.from(refreshTokenCookieName, "")
            .httpOnly(cookieHttpOnly)
            .path("/")
            .maxAge(0)
            .sameSite(cookieSameSite)
            .secure(cookieSecure);

    if (cookieDomain != null && !cookieDomain.isBlank()) {
      cookieBuilder.domain(cookieDomain);
    }

    ResponseCookie responseCookie = cookieBuilder.build();
    response.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
  }

  public void addNoStoreHeaders(HttpServletResponse response) {
    logger.info("Adding no store headers");
    response.addHeader(HttpHeaders.CACHE_CONTROL, "no-store");
    response.addHeader("Pragma", "no-cache");
  }
}
