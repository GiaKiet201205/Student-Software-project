package com.sgu.admission.controller;

import com.sgu.admission.dto.KetQuaTinhDiemDTO;
import com.sgu.admission.entity.Nganh;
import com.sgu.admission.entity.ToHopMonThi;
import com.sgu.admission.repository.NganhRepository;
import com.sgu.admission.repository.ToHopMonThiRepository;
import com.sgu.admission.service.DiemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/diem")
@RequiredArgsConstructor
public class DiemController {
    
    private final DiemService diemService;
    private final NganhRepository nganhRepository;
    private final ToHopMonThiRepository toHopRepository;
    
    // ==================== ĐGNL ====================
    
    @GetMapping("/calc-dgnl")
    public String showCalcDgnl(Model model) {
        // Load danh sách ngành
        List<Nganh> nganhs = nganhRepository.findAll();
        model.addAttribute("nganhs", nganhs);
        
        return "calc-dgnl";
    }
    
    @PostMapping("/calc-dgnl")
    public String calcDgnl(HttpSession session,
                          @RequestParam String maNganh,
                          @RequestParam(required = false) String maToHop,
                          @RequestParam BigDecimal diemHSA,
                          @RequestParam(required = false) String doiTuong,
                          @RequestParam(required = false) String khuVuc,
                          Model model) {
        
        String cccd = (String) session.getAttribute("cccd");
        if (cccd == null) {
            return "redirect:/login";
        }
        
        KetQuaTinhDiemDTO result = diemService.calculateDgnl(cccd, maNganh, maToHop, diemHSA, doiTuong, khuVuc);
        
        // Load danh sách ngành
        List<Nganh> nganhs = nganhRepository.findAll();
        model.addAttribute("nganhs", nganhs);
        model.addAttribute("result", result);
        
        return "calc-dgnl";
    }
    
    // ==================== THPT ====================
    
    @GetMapping("/calc-thpt")
    public String showCalcThpt(Model model) {
        List<Nganh> nganhs = nganhRepository.findAll();
        List<ToHopMonThi> toHops = toHopRepository.findAll();
        
        model.addAttribute("nganhs", nganhs);
        model.addAttribute("toHops", toHops);
        
        return "calc-thpt";
    }
    
    @PostMapping("/calc-thpt")
    public String calcThpt(HttpSession session,
                          @RequestParam String maNganh,
                          @RequestParam String maToHop,
                          @RequestParam BigDecimal diemMon1,
                          @RequestParam BigDecimal diemMon2,
                          @RequestParam BigDecimal diemMon3,
                          @RequestParam(required = false) String doiTuong,
                          @RequestParam(required = false) String khuVuc,
                          Model model) {
        
        String cccd = (String) session.getAttribute("cccd");
        if (cccd == null) {
            return "redirect:/login";
        }
        
        KetQuaTinhDiemDTO result = diemService.calculateThpt(cccd, maNganh, maToHop, 
                diemMon1, diemMon2, diemMon3, doiTuong, khuVuc);
        
        List<Nganh> nganhs = nganhRepository.findAll();
        List<ToHopMonThi> toHops = toHopRepository.findAll();
        
        model.addAttribute("nganhs", nganhs);
        model.addAttribute("toHops", toHops);
        model.addAttribute("result", result);
        
        return "calc-thpt";
    }
    
    // ==================== VSAT ====================
    
    @GetMapping("/calc-vsat")
    public String showCalcVsat(Model model) {
        List<Nganh> nganhs = nganhRepository.findAll();
        List<ToHopMonThi> toHops = toHopRepository.findAll();
        
        model.addAttribute("nganhs", nganhs);
        model.addAttribute("toHops", toHops);
        
        return "calc-vsat";
    }
    
    @PostMapping("/calc-vsat")
    public String calcVsat(HttpSession session,
                          @RequestParam String maNganh,
                          @RequestParam String maToHop,
                          @RequestParam BigDecimal diemVsat,
                          @RequestParam(required = false) String doiTuong,
                          @RequestParam(required = false) String khuVuc,
                          Model model) {
        
        String cccd = (String) session.getAttribute("cccd");
        if (cccd == null) {
            return "redirect:/login";
        }
        
        KetQuaTinhDiemDTO result = diemService.calculateVsat(cccd, maNganh, maToHop, diemVsat, doiTuong, khuVuc);
        
        List<Nganh> nganhs = nganhRepository.findAll();
        List<ToHopMonThi> toHops = toHopRepository.findAll();
        
        model.addAttribute("nganhs", nganhs);
        model.addAttribute("toHops", toHops);
        model.addAttribute("result", result);
        
        return "calc-vsat";
    }
}
