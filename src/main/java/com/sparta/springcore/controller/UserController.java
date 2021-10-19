package com.sparta.springcore.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    // 회원 로그인 페이지
    ///user/login으로 요청이 들어오면 login 페이지를 내려줌.
    //thymeleaf에 의해 login String만 내려주면 application.properties에서 thymeleaf 루트 위치로 설정해준 /static/login.html파일을 찾아서 내려줌.
    @GetMapping("/user/login")
    public String login() {
        return "login";
    }

    //model로 loginError 정보를 같이 담아서 login 페이지를 내려줌.
    @GetMapping("/user/login/error")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        return "login";
    }

    // 회원 가입 페이지
    @GetMapping("/user/signup")
    public String signup() {
        return "signup";
    }
}