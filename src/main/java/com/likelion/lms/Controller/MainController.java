package com.likelion.lms.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class MainController {

    @GetMapping("/")
    public String home() { return "user/login";}

    @GetMapping("/about")
    public String about() { return "home/aboutme";}

    @GetMapping("/main")
    public String credit() { return "home/12th_credit";}

}