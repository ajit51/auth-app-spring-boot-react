package com.auth.auth_app_backend.repositories;

import com.auth.auth_app_backend.entities.RefreshToken;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {

  Optional<RefreshToken> findByJti(String jti);
}
