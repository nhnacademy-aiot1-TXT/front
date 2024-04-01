package com.nhnacademy.front.adaptor;

import com.nhnacademy.front.dto.AccessTokenResponse;
import com.nhnacademy.front.dto.LoginRequest;
import com.nhnacademy.front.dto.TokensResponse;
import com.nhnacademy.front.dto.UserRegisterRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;


@FeignClient(value = "user-management", url = "${gateway.url}")
public interface UserAdapter {
    @PostMapping(value = "/api/auth/login", headers = {"content-type=application/json", "accept=application/json"})
    TokensResponse doLogin(LoginRequest loginRequest);
    @PostMapping("/api/auth/logout")
    void doLogout(String refreshToken);
    @PostMapping("/api/user/register")
    void createUser(UserRegisterRequest userRegisterRequest);
    @GetMapping("/api/auth/reissue")
    AccessTokenResponse reissue(@RequestHeader("X-REFRESH-TOKEN") String refreshToken);
}
