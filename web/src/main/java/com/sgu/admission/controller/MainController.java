package com.sgu.admission.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("tinh-diem")
    public String calculate() {
        return "tinh-diem";
    }

    @GetMapping("/tra-cuu")
    public String research() {
        return "tra-cuu";
    }
}
