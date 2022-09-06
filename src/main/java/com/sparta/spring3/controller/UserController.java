package com.sparta.spring3.controller;

import com.sparta.spring3.controller.request.LoginRequestDto;
import com.sparta.spring3.controller.request.UserRequestDto;
import com.sparta.spring3.controller.response.ResponseDto;
import com.sparta.spring3.service.UserService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class UserController {

  private final UserService userService;

  @RequestMapping(value = "/api/member/signup", method = RequestMethod.POST)
  public ResponseDto<?> signup(@RequestBody @Valid UserRequestDto requestDto) {
    return userService.createUser(requestDto);
  }

  @RequestMapping(value = "/api/member/login", method = RequestMethod.POST)
  public ResponseDto<?> login(@RequestBody @Valid LoginRequestDto requestDto,
      HttpServletResponse response
  ) {
    return userService.login(requestDto, response);
  }


//  @RequestMapping(value = "/api/auth/member/logout", method = RequestMethod.POST)
//  public ResponseDto<?> logout(HttpServletRequest request) {
//    return userService.logout(request);
//  }
}
