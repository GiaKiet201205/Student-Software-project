package com.sgu.admission.controller;

import com.sgu.admission.dto.ThiSinhDTO;
import com.sgu.admission.service.ThiSinhService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/home")
@RequiredArgsConstructor
public class ThiSinhController {
    
    private final ThiSinhService thishinhService;
    
    @GetMapping
    public String showHome(HttpSession session, Model model) {
        String cccd = (String) session.getAttribute("cccd");
        
        if (cccd == null) {
            return "redirect:/login";
        }
        
        // Lấy thông tin thí sinh
        ThiSinhDTO thiSinh = thishinhService.getThiSinhInfo(cccd);
        model.addAttribute("thiSinh", thiSinh);
        
        return "home";
    }
    
    @GetMapping("/profile")
    public String showProfile(HttpSession session, Model model) {
        String cccd = (String) session.getAttribute("cccd");
        
        if (cccd == null) {
            return "redirect:/login";
        }
        
        ThiSinhDTO thiSinh = thishinhService.getThiSinhInfo(cccd);
        model.addAttribute("thiSinh", thiSinh);
        
        return "profile";
    }
}
