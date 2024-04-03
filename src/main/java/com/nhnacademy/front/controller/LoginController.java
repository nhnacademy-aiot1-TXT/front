package com.nhnacademy.front.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nhnacademy.front.adaptor.UserAdapter;
import com.nhnacademy.front.dto.AccessTokenResponse;
import com.nhnacademy.front.dto.LoginRequest;
import com.nhnacademy.front.dto.RefreshTokenResponse;
import com.nhnacademy.front.dto.TokensResponse;
import com.nhnacademy.front.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequiredArgsConstructor
public class LoginController {
    private final UserAdapter userAdapter;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public String login(LoginRequest loginRequest, HttpServletResponse response, @RequestAttribute("_csrf") CsrfToken csrfToken) throws JsonProcessingException {
        TokensResponse tokens = userAdapter.doLogin(loginRequest, csrfToken.getToken());

        AccessTokenResponse accessTokenResponse = tokens.getAccessToken();
        RefreshTokenResponse refreshTokenResponse = tokens.getRefreshToken();

        Authentication authentication = jwtUtil.getAuthentication(accessTokenResponse);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Cookie accessCookie = new Cookie("accessToken", accessTokenResponse.getAccessToken());
        Cookie refreshCookie = new Cookie("refreshToken", refreshTokenResponse.getRefreshToken());

        accessCookie.setHttpOnly(true);
        refreshCookie.setHttpOnly(true);

        response.addCookie(accessCookie);
        response.addCookie(refreshCookie);

        return "redirect:/";
    }
}
