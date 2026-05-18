package com.sgu.admission.controller;

import com.sgu.admission.dto.NguyenVongDTO;
import com.sgu.admission.service.NguyenVongService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/result")
@RequiredArgsConstructor
public class NguyenVongController {
    
    private final NguyenVongService nguyenVongService;
    
    @GetMapping
    public String showResult(HttpSession session, Model model) {
        String cccd = (String) session.getAttribute("cccd");
        
        if (cccd == null) {
            return "redirect:/login";
        }
        
        // Lấy danh sách nguyện vọng xét tuyển
        List<NguyenVongDTO> nguyenVongs = nguyenVongService.getNguyenVongByCccd(cccd);
        model.addAttribute("nguyenVongs", nguyenVongs);
        
        // Thống kê: số nguyện vọng đạt/không
        long countDat = nguyenVongs.stream()
                .filter(nv -> "Đạt".equals(nv.getKetQua()))
                .count();
        long countKhong = nguyenVongs.size() - countDat;
        
        model.addAttribute("countDat", countDat);
        model.addAttribute("countKhong", countKhong);
        
        return "result";
    }
}
