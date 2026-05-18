package com.sgu.admission.service;

import com.sgu.admission.dto.NguyenVongDTO;
import com.sgu.admission.entity.NguyenVongXetTuyen;
import com.sgu.admission.entity.Nganh;
import com.sgu.admission.entity.ToHopMonThi;
import com.sgu.admission.repository.NganhRepository;
import com.sgu.admission.repository.NguyenVongXetTuyenRepository;
import com.sgu.admission.repository.ToHopMonThiRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NguyenVongService {
    
    private final NguyenVongXetTuyenRepository nguyenVongRepository;
    private final NganhRepository nganhRepository;
    private final ToHopMonThiRepository toHopRepository;
    
    /**
     * Lấy danh sách nguyện vọng xét tuyển của thí sinh
     */
    public List<NguyenVongDTO> getNguyenVongByCccd(String cccd) {
        List<NguyenVongXetTuyen> nguyenVongs = nguyenVongRepository.findByCccdOrderByThuTu(cccd);
        List<NguyenVongDTO> dtos = new ArrayList<>();
        
        for (NguyenVongXetTuyen nv : nguyenVongs) {
            NguyenVongDTO dto = convertToDTO(nv);
            dtos.add(dto);
        }
        
        return dtos;
    }
    
    /**
     * Chuyển entity sang DTO với thông tin ngành và tổ hợp
     */
    private NguyenVongDTO convertToDTO(NguyenVongXetTuyen nv) {
        // Lấy thông tin ngành
        Optional<Nganh> nganh = nganhRepository.findByMaNganh(nv.getMaNganh());
        String tenNganh = nganh.map(Nganh::getTenNganh).orElse("");
        
        // Lấy thông tin tổ hợp
        String tenToHop = "";
        if (nv.getMaToHop() != null) {
            Optional<ToHopMonThi> toHop = toHopRepository.findByMaToHop(nv.getMaToHop());
            tenToHop = toHop.map(ToHopMonThi::getTenToHop).orElse("");
        }
        
        return NguyenVongDTO.builder()
                .idNv(nv.getIdNv())
                .maNganh(nv.getMaNganh())
                .tenNganh(tenNganh)
                .thuTu(nv.getThuTu())
                .phuongThuc(nv.getPhuongThuc())
                .maToHop(nv.getMaToHop())
                .diemXetTuyen(nv.getDiemXetTuyen())
                .ketQua(nv.getKetQua())
                .ttThm(nv.getTtThm())
                .build();
    }
}
