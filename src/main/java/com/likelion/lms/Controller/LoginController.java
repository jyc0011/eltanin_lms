package com.likelion.lms.Controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    // 운영진 로그인 (예시)
    @GetMapping("/login/admin")
    public String adminLogin(HttpSession session) {
        // 아이디와 권한상태를 세션에 저장하여 앞으로도 로그인 정보를 확인할 때 이용하게 할 것
        long userId = 1;
        boolean isAdmin = true;

        // 세션에 정보 저장
        session.setAttribute("id", userId);
        session.setAttribute("is_admin", isAdmin);

        // 로그인 작업이 모두 수행되었으니, 게시판 1페이지로 리다이렉트
        return "redirect:/list/1";
    }

    // 일반 유저(아기 사자) 로그인 (예시)
    @GetMapping("/login/baby")
    public String babyLogin(HttpSession session) {

        long userId = 2;
        boolean isAdmin = false;

        // 세션에 정보 저장
        session.setAttribute("id", userId);
        session.setAttribute("is_admin", isAdmin);

        return "redirect:/list/1";
    }
}
