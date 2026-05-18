package com.sgu.admission.controller;

import com.sgu.admission.dto.LoginDTO;
import com.sgu.admission.entity.ThiSinhXetTuyen25;
import com.sgu.admission.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
public class AuthController {
    
    private final AuthService authService;
    
    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }
    
    @PostMapping("/authenticate")
    public String authenticate(@RequestParam String cccd, @RequestParam String password, 
                              HttpSession session, Model model) {
        LoginDTO loginDTO = new LoginDTO(cccd, password);
        ThiSinhXetTuyen25 thiSinh = authService.authenticate(loginDTO);
        
        if (thiSinh != null) {
            // Lưu vào session
            session.setAttribute("cccd", thiSinh.getCccd());
            session.setAttribute("ho", thiSinh.getHo());
            session.setAttribute("ten", thiSinh.getTen());
            session.setAttribute("idThiSinh", thiSinh.getIdThiSinh());
            
            return "redirect:/home";
        } else {
            model.addAttribute("error", "CCCD hoặc mật khẩu không đúng");
            return "login";
        }
    }
    
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
