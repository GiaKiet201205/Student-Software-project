package com.sgu.admission.service;

import com.sgu.admission.dto.LoginDTO;
import com.sgu.admission.entity.ThiSinhXetTuyen25;
import com.sgu.admission.repository.ThiSinhXetTuyen25Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final ThiSinhXetTuyen25Repository thishinhRepository;
    
    /**
     * Đăng nhập với CCCD + password (ddMMyyyy)
     * @param loginDTO CCCD và password
     * @return ThiSinhXetTuyen25 nếu đúng, null nếu sai
     */
    public ThiSinhXetTuyen25 authenticate(LoginDTO loginDTO) {
        Optional<ThiSinhXetTuyen25> thiSinh = thishinhRepository.findByCccd(loginDTO.getCccd());
        
        if (thiSinh.isEmpty()) {
            return null;
        }
        
        ThiSinhXetTuyen25 ts = thiSinh.get();
        
        // Kiểm tra password
        // Password = ngày sinh dạng ddMMyyyy
        // Ví dụ: ngaySinh = "01/01/2005" => password = "01012005"
        String expectedPassword = formatNgaySinhToPassword(ts.getNgaySinh());
        
        if (loginDTO.getPassword().equals(expectedPassword)) {
            return ts;
        }
        
        return null;
    }
    
    /**
     * Chuyển ngày sinh từ format "dd/MM/yyyy" thành "ddMMyyyy"
     */
    private String formatNgaySinhToPassword(String ngaySinh) {
        if (ngaySinh == null || ngaySinh.isEmpty()) {
            return "";
        }
        
        // Xóa tất cả ký tự không phải số
        return ngaySinh.replaceAll("[^0-9]", "");
    }
}
