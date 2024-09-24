package com.likelion.lms.Controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
public class CustomErrorController implements ErrorController {
    // error가 발생한 경우 모두 이 부분으로 던짐
    // 그래서 항상 일관되게 404 페이지를 출력하게 됨.
    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute("javax.servlet.error.status_code");
        model.addAttribute("errorMessage", "예기치 않은 오류가 발생했습니다.");
        return "home/404"; // 일반적인 오류 페이지
    }
}