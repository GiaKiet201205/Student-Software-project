package com.sgu.admission.service;

import com.sgu.admission.dto.ThiSinhDTO;
import com.sgu.admission.entity.ThiSinhXetTuyen25;
import com.sgu.admission.repository.ThiSinhXetTuyen25Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ThiSinhService {
    
    private final ThiSinhXetTuyen25Repository thishinhRepository;
    
    /**
     * Lấy thông tin thí sinh theo CCCD
     */
    public ThiSinhDTO getThiSinhInfo(String cccd) {
        Optional<ThiSinhXetTuyen25> thiSinh = thishinhRepository.findByCccd(cccd);
        
        if (thiSinh.isEmpty()) {
            return null;
        }
        
        ThiSinhXetTuyen25 ts = thiSinh.get();
        
        return ThiSinhDTO.builder()
                .cccd(ts.getCccd())
                .ho(ts.getHo())
                .ten(ts.getTen())
                .ngaySinh(ts.getNgaySinh())
                .dienThoai(ts.getDienThoai())
                .email(ts.getEmail())
                .gioiTinh(ts.getGioiTinh())
                .doiTuong(ts.getDoiTuong())
                .khuVuc(ts.getKhuVuc())
                .build();
    }
    
    /**
     * Lấy entity ThiSinhXetTuyen25 theo CCCD
     */
    public Optional<ThiSinhXetTuyen25> findByCccd(String cccd) {
        return thishinhRepository.findByCccd(cccd);
    }
}
