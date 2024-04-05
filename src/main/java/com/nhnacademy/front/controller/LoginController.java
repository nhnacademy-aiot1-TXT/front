package com.nhnacademy.front.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nhnacademy.front.adaptor.UserAdapter;
import com.nhnacademy.front.dto.AccessTokenResponse;
import com.nhnacademy.front.dto.LoginRequest;
import com.nhnacademy.front.dto.RefreshTokenResponse;
import com.nhnacademy.front.dto.TokensResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * 로그인 처리 Controller 클래스
 */
@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {
    private final UserAdapter userAdapter;

    /**
     * 로그인 처리 핸들러 메서드
     *
     * @param loginRequest 로그인 요청 정보
     * @param response     HTTP 응답 객체
     * @param csrfToken    CSRF 토큰
     * @return redirect 할 URL 문자열
     */
    @PostMapping("/login")
    public String login(LoginRequest loginRequest, HttpServletResponse response, @RequestAttribute("_csrf") CsrfToken csrfToken) throws JsonProcessingException {
        TokensResponse tokens = userAdapter.doLogin(loginRequest, csrfToken.getToken());

        AccessTokenResponse accessTokenResponse = tokens.getAccessToken();
        RefreshTokenResponse refreshTokenResponse = tokens.getRefreshToken();

        Cookie accessCookie = new Cookie("accessToken", accessTokenResponse.getAccessToken());
        Cookie refreshCookie = new Cookie("refreshToken", refreshTokenResponse.getRefreshToken());

        accessCookie.setHttpOnly(true);
        refreshCookie.setHttpOnly(true);

        response.addCookie(accessCookie);
        response.addCookie(refreshCookie);

        return "redirect:/";
    }
}
