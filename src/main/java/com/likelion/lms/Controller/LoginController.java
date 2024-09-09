package com.likelion.lms.Controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    // 운영진 로그인 (예시)
    @GetMapping("/login/admin")
    public String adminLogin(HttpSession session) {
        long userId = 1;
        boolean isAdmin = true;

        // 세션에 정보 저장
        session.setAttribute("id", userId);
        session.setAttribute("is_admin", isAdmin);

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
