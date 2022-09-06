package com.sparta.spring3.repository;

import com.sparta.spring3.domain.Member;
import com.sparta.spring3.domain.RefreshToken;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
  Optional<RefreshToken> findByMember(Member member);
}
