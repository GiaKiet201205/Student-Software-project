package com.sgu.admission.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpSession;

@Controller
public class MainController {

    @GetMapping("/")
    public String index(HttpSession session) {
        // Nếu chưa login, redirect về login page
        if (session.getAttribute("cccd") == null) {
            return "redirect:/login";
        }
        return "redirect:/home";
    }

    @GetMapping("/tinh-diem")
    public String calculate(HttpSession session) {
        if (session.getAttribute("cccd") == null) {
            return "redirect:/login";
        }
        return "redirect:/diem/calc-dgnl";
    }

    @GetMapping("/tra-cuu")
    public String research(HttpSession session) {
        if (session.getAttribute("cccd") == null) {
            return "redirect:/login";
        }
        return "redirect:/result";
    }
}
