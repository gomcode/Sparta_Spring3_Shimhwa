package com.sparta.spring3.service;

import com.sparta.spring3.controller.response.UserResponseDto;
import com.sparta.spring3.controller.request.LoginRequestDto;
import com.sparta.spring3.controller.request.UserRequestDto;
import com.sparta.spring3.controller.response.ResponseDto;
import com.sparta.spring3.controller.request.TokenDto;
import com.sparta.spring3.domain.User;
import com.sparta.spring3.jwt.TokenProvider;
import com.sparta.spring3.repository.UserRepository;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {

  private final UserRepository userRepository;

  private final PasswordEncoder passwordEncoder;
//  private final AuthenticationManagerBuilder authenticationManagerBuilder;
  private final TokenProvider tokenProvider;

  @Transactional
  public ResponseDto<?> createUser(@Valid UserRequestDto requestDto) {
    if (null != isPresentUser(requestDto.getUserName())) {
      return ResponseDto.fail("DUPLICATED_NICKNAME",
          "중복된 닉네임 입니다.");
    }

    if (!requestDto.getLoginPw().equals(requestDto.passwordConfirm())) {
      return ResponseDto.fail("PASSWORDS_NOT_MATCHED",
          "비밀번호와 비밀번호 확인이 일치하지 않습니다.");
    }

    User user = User.builder()
            .userName(requestDto.getUserName())
                .loginPw(passwordEncoder.encode(requestDto.getLoginPw()))
                    .build();
    userRepository.save(user);
    return ResponseDto.success(
        UserResponseDto.builder()
            .userId(user.getUserId())
            .userName(user.getUserName())
            .createdAt(user.getCreatedAt())
            .modifiedAt(user.getModifiedAt())
            .build()
    );
  }

  @Transactional
  public ResponseDto<?> login(LoginRequestDto requestDto, HttpServletResponse response) {
    User user = isPresentUser(requestDto.getUserName());
    if (null == user) {
      return ResponseDto.fail("USER_NOT_FOUND",
          "사용자를 찾을 수 없습니다.");
    }

    if (!user.validatePassword(passwordEncoder, requestDto.getLoginPw())) {
      return ResponseDto.fail("INVALID_USER", "사용자를 찾을 수 없습니다.");
    }

//    UsernamePasswordAuthenticationToken authenticationToken =
//        new UsernamePasswordAuthenticationToken(requestDto.getNickname(), requestDto.getPassword());
//    Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

    TokenDto tokenDto = tokenProvider.generateTokenDto(user);
    tokenToHeaders(tokenDto, response);

    return ResponseDto.success(
        UserResponseDto.builder()
            .userId(user.getUserId())
            .userName(user.getUserName())
            .createdAt(user.getCreatedAt())
            .modifiedAt(user.getModifiedAt())
            .build()
    );
  }

//  @Transactional
//  public ResponseDto<?> reissue(HttpServletRequest request, HttpServletResponse response) {
//    if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
//      return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
//    }
//    Member member = tokenProvider.getMemberFromAuthentication();
//    if (null == member) {
//      return ResponseDto.fail("MEMBER_NOT_FOUND",
//          "사용자를 찾을 수 없습니다.");
//    }
//
//    Authentication authentication = tokenProvider.getAuthentication(request.getHeader("Access-Token"));
//    RefreshToken refreshToken = tokenProvider.isPresentRefreshToken(member);
//
//    if (!refreshToken.getValue().equals(request.getHeader("Refresh-Token"))) {
//      return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
//    }
//
//    TokenDto tokenDto = tokenProvider.generateTokenDto(member);
//    refreshToken.updateValue(tokenDto.getRefreshToken());
//    tokenToHeaders(tokenDto, response);
//    return ResponseDto.success("success");
//  }

  public ResponseDto<?> logout(HttpServletRequest request) {
    if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
      return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
    }
    User user = tokenProvider.getUserFromAuthentication();
    if (null == user) {
      return ResponseDto.fail("USER_NOT_FOUND",
          "사용자를 찾을 수 없습니다.");
    }

    return tokenProvider.deleteRefreshToken(user);
  }

  @Transactional(readOnly = true)
  public User isPresentUser(String nickname) {
    Optional<User> optionalUser = userRepository.findByUserName(nickname);
    return optionalUser.orElse(null);
  }

  public void tokenToHeaders(TokenDto tokenDto, HttpServletResponse response) {
    response.addHeader("Authorization", "Bearer " + tokenDto.getAccessToken());
    response.addHeader("Refresh-Token", tokenDto.getRefreshToken());
    response.addHeader("Access-Token-Expire-Time", tokenDto.getAccessTokenExpiresIn().toString());
  }

}
