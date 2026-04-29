package com.service;

import com.entity.DiemThiXetTuyen;
import com.exception.AppException;
import com.repository.DiemThiXetTuyenRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DiemThiXetTuyenService {

    private final DiemThiXetTuyenRepository diemRepo;

    public DiemThiXetTuyenService() {
        this.diemRepo = new DiemThiXetTuyenRepository();
    }

    public List<DiemThiXetTuyen> layTatCaDiem() {
        return diemRepo.findAll();
    }

    // =========================================================================
    // 1. IMPORT THPT - CẤU TRÚC NGANG (Đọc Sheet 0 của file THPT)
    // =========================================================================
    public String importDiemThpt(File file, String phuongThuc) {
        List<DiemThiXetTuyen> danhSachImport = new ArrayList<>();
        int soDongLoi = 0;

        // Cấu hình Index cột THPT (Giới hạn đến NK4)
        final int COL_CCCD = 1;         // Cột B
        final int COL_SBD = 2;          // Cột C
        final int COL_TOAN = 7;         // Cột H
        final int COL_VAN = 8;          // Cột I
        final int COL_NGOAI_NGU = 9;    // Cột J
        final int COL_LY = 10;          // Cột K
        final int COL_HOA = 11;         // Cột L
        final int COL_SINH = 12;        // Cột M
        final int COL_SU = 13;          // Cột N
        final int COL_DIA = 14;         // Cột O
        final int COL_GDCD = 15;        // Cột P
        final int COL_DGNL = 16;        // Cột Q
        final int COL_NK1 = 17;         // Cột R
        final int COL_NK2 = 18;         // Cột S
        final int COL_NK3 = 19;         // Cột T
        final int COL_NK4 = 20;         // Cột U

        try (FileInputStream fis = new FileInputStream(file);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0); // Vì là file lẻ nên luôn đọc Sheet 0

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                try {
                    String cccd = getCellValue(row.getCell(COL_CCCD));
                    if (cccd.isEmpty()) continue;

                    DiemThiXetTuyen profile = diemRepo.findByCccdAndPhuongThuc(cccd, phuongThuc);
                    if (profile == null) {
                        profile = DiemThiXetTuyen.builder()
                                .cccd(cccd)
                                .soBaoDanh(getCellValue(row.getCell(COL_SBD)))
                                .phuongThuc(phuongThuc)
                                .build();
                    }

                    profile.setToan(parseToBigDecimal(getCellValue(row.getCell(COL_TOAN))));
                    profile.setVan(parseToBigDecimal(getCellValue(row.getCell(COL_VAN))));
                    profile.setN1Thi(parseToBigDecimal(getCellValue(row.getCell(COL_NGOAI_NGU))));
                    profile.setLy(parseToBigDecimal(getCellValue(row.getCell(COL_LY))));
                    profile.setHoa(parseToBigDecimal(getCellValue(row.getCell(COL_HOA))));
                    profile.setSinh(parseToBigDecimal(getCellValue(row.getCell(COL_SINH))));
                    profile.setSu(parseToBigDecimal(getCellValue(row.getCell(COL_SU))));
                    profile.setDia(parseToBigDecimal(getCellValue(row.getCell(COL_DIA))));
                    profile.setKtpl(parseToBigDecimal(getCellValue(row.getCell(COL_GDCD))));

                    profile.setDiemNangLuc(parseToBigDecimal(getCellValue(row.getCell(COL_DGNL))));
                    profile.setDiemNangKhieu1(parseToBigDecimal(getCellValue(row.getCell(COL_NK1))));
                    profile.setDiemNangKhieu2(parseToBigDecimal(getCellValue(row.getCell(COL_NK2))));
                    profile.setDiemNangKhieu3(parseToBigDecimal(getCellValue(row.getCell(COL_NK3))));
                    profile.setDiemNangKhieu4(parseToBigDecimal(getCellValue(row.getCell(COL_NK4))));

                    danhSachImport.add(profile);
                } catch (Exception ex) {
                    soDongLoi++;
                }
            }
            diemRepo.saveAll(danhSachImport);
            return "Import file THPT thành công " + danhSachImport.size() + " hồ sơ!";
        } catch (Exception e) {
            throw new AppException("Lỗi đọc file Excel THPT!", e);
        }
    }

    // =========================================================================
    // 2. IMPORT V-SAT/ĐGNL - CẤU TRÚC DỌC (Đọc Sheet 0 của file VSAT)
    // =========================================================================
    public String importDiemDgnlVsat(File file, String phuongThuc) {
        Map<String, DiemThiXetTuyen> mapHoSo = new HashMap<>();
        int soDongLoi = 0;

        final int COL_CCCD = 1;         // Cột B
        final int COL_MAMONTHI = 5;     // Cột F
        final int COL_DIEM = 7;         // Cột H

        try (FileInputStream fis = new FileInputStream(file);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0); // Luôn đọc Sheet 0 của file được chọn

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                try {
                    String cccd = getCellValue(row.getCell(COL_CCCD));
                    if (cccd.isEmpty() || cccd.equalsIgnoreCase("CMND")) continue;

                    String maMon = getCellValue(row.getCell(COL_MAMONTHI)).toUpperCase();
                    BigDecimal diem = parseToBigDecimal(getCellValue(row.getCell(COL_DIEM)));

                    String key = cccd + "_" + phuongThuc;
                    DiemThiXetTuyen profile = mapHoSo.get(key);

                    if (profile == null) {
                        profile = diemRepo.findByCccdAndPhuongThuc(cccd, phuongThuc);
                        if (profile == null) {
                            profile = DiemThiXetTuyen.builder().cccd(cccd).phuongThuc(phuongThuc).build();
                        }
                        mapHoSo.put(key, profile);
                    }

                    if (diem != null) {
                        updateScoreIfHigher(profile, maMon, diem);
                    }
                } catch (Exception ex) {
                    soDongLoi++;
                }
            }
            List<DiemThiXetTuyen> danhSach = new ArrayList<>(mapHoSo.values());
            diemRepo.saveAll(danhSach);
            return "Import file V-SAT/ĐGNL thành công " + danhSach.size() + " hồ sơ thí sinh!";
        } catch (Exception e) {
            throw new AppException("Lỗi đọc file V-SAT/ĐGNL!", e);
        }
    }

    private void updateScoreIfHigher(DiemThiXetTuyen p, String code, BigDecimal val) {
        switch (code) {
            case "TO_VS": if (p.getToan() == null || val.compareTo(p.getToan()) > 0) p.setToan(val); break;
            case "LI_VS": if (p.getLy() == null || val.compareTo(p.getLy()) > 0) p.setLy(val); break;
            case "HO_VS": if (p.getHoa() == null || val.compareTo(p.getHoa()) > 0) p.setHoa(val); break;
            case "SI_VS": if (p.getSinh() == null || val.compareTo(p.getSinh()) > 0) p.setSinh(val); break;
            case "N1_VS": if (p.getN1Thi() == null || val.compareTo(p.getN1Thi()) > 0) p.setN1Thi(val); break;
            case "SU_VS": if (p.getSu() == null || val.compareTo(p.getSu()) > 0) p.setSu(val); break;
            case "DI_VS": if (p.getDia() == null || val.compareTo(p.getDia()) > 0) p.setDia(val); break;
            case "TONG_DGNL": if (p.getDiemNangLuc() == null || val.compareTo(p.getDiemNangLuc()) > 0) p.setDiemNangLuc(val); break;
        }
    }

    private String getCellValue(Cell cell) {
        if (cell == null) return "";
        DataFormatter formatter = new DataFormatter();
        return formatter.formatCellValue(cell).trim();
    }

    private BigDecimal parseToBigDecimal(String value) {
        if (value == null || value.isEmpty()) return null;
        try { return new BigDecimal(value.replace(",", ".")); } catch (Exception e) { return null; }
    }
}