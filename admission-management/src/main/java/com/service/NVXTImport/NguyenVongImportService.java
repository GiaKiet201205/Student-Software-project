package com.service.NVXTImport;

import com.controller.DiemCongXetTuyenController;
import com.controller.DiemThiXetTuyenController;
import com.controller.NguyenVongXetTuyenController;
import com.entity.DiemCongXetTuyen;
import com.entity.DiemThiXetTuyen;
import com.entity.NguyenVongXetTuyen;
import com.service.NVXTImport.converter.VsatScoreConverter;
import com.service.NVXTImport.model.ImportResult;
import com.service.NVXTImport.model.NguyenVongRaw;
import com.service.NVXTImport.processor.ToHopProcessor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class NguyenVongImportService {

    private static int debugLogCount = 0;
    private static final int BATCH_SIZE = 500;
    private static final BigDecimal THRESHOLD = new BigDecimal("22.5");
    private static final BigDecimal MAX_SCORE = new BigDecimal("30");
    private static final BigDecimal DGNL_MAX = new BigDecimal("1200");
    private static final BigDecimal FACTOR_DIVISOR = new BigDecimal("7.5"); // Hệ số chia


    private final DiemThiXetTuyenController diemThiController;
    private final DiemCongXetTuyenController diemCongController;
    private final NguyenVongXetTuyenController nguyenVongController;
    private final ToHopProcessor toHopProcessor;
    private final VsatScoreConverter vsatConverter = new VsatScoreConverter();

    private final Map<String, DiemThiXetTuyen> diemThiByKey = new ConcurrentHashMap<>();
    private final Map<String, List<DiemThiXetTuyen>> diemThiByCccd = new ConcurrentHashMap<>();
    private final Map<String, List<DiemCongXetTuyen>> diemCongByKey = new ConcurrentHashMap<>();

    public NguyenVongImportService() {
        this.diemThiController = new DiemThiXetTuyenController();
        this.diemCongController = new DiemCongXetTuyenController();
        this.nguyenVongController = new NguyenVongXetTuyenController();
        this.toHopProcessor = new ToHopProcessor();
    }

    public ImportResult importNguyenVong(File nguyenVongFile, File toHopFile, Consumer<Integer> callback) throws Exception {
        long startTime = System.currentTimeMillis();
        System.out.println("=== BẮT ĐẦU IMPORT NGUYỆN VỌNG ===");

        callback(callback, 5);
        toHopProcessor.loadToHopFromFile(toHopFile);
        System.out.println("✓ Đã load tổ hợp môn");

        callback(callback, 10);
        loadCacheData();
        System.out.println("✓ Đã load cache: " + diemThiByKey.size() + " điểm thi, " + diemCongByKey.size() + " điểm cộng");

        callback(callback, 15);
        List<NguyenVongRaw> rawList = readNguyenVongFile(nguyenVongFile);
        System.out.println("✓ Đọc được " + rawList.size() + " nguyện vọng từ file");

        callback(callback, 25);
        ImportResult result = new ImportResult();  // 🔥 TẠO result mới
        processAndSave(rawList, result, callback); // 🔥 TRUYỀN result vào

        result.setElapsedTime(System.currentTimeMillis() - startTime);
        System.out.println("=== KẾT QUẢ: " + result.getSummary());
        return result;
    }

    private void callback(Consumer<Integer> callback, int percent) {
        if (callback != null) callback.accept(percent);
    }

    private void loadCacheData() {
        loadDiemThiCache();
        loadDiemCongCache();
    }

    private void loadDiemThiCache() {
        List<DiemThiXetTuyen> allDiemThi = diemThiController.getAll();
        // Đếm số lượng theo phương thức
        int pt3 = 0, pt4 = 0, pt5 = 0;
        
        for (DiemThiXetTuyen dt : allDiemThi) {
            String normalizedCccd = normalizeCCCD(dt.getCccd());
            String key = normalizedCccd + "_" + dt.getPhuongThuc();
            diemThiByKey.put(key, dt);
            diemThiByCccd.computeIfAbsent(normalizedCccd, k -> new ArrayList<>()).add(dt);
            
            if ("3".equals(dt.getPhuongThuc())) pt3++;
            else if ("4".equals(dt.getPhuongThuc())) pt4++;
            else if ("5".equals(dt.getPhuongThuc())) pt5++;
        }
        
        System.out.println("=== Phân bố điểm thi ===");
        System.out.println("  PT3 (THPT): " + pt3 + " bản ghi");
        System.out.println("  PT4 (ĐGNL): " + pt4 + " bản ghi");
        System.out.println("  PT5 (V-SAT): " + pt5 + " bản ghi");
        System.out.println("  Tổng: " + allDiemThi.size());
    }

    private void loadDiemCongCache() {
        List<DiemCongXetTuyen> allDiemCong = diemCongController.getDiemCongXetTuyen();
        int validCount = 0;
        for (DiemCongXetTuyen dc : allDiemCong) {
            if (isEmpty(dc.getCccd()) || isEmpty(dc.getMaNganh())) continue;

            String cccd = normalizeCCCD(dc.getCccd());
            String key = cccd + "_" + dc.getMaNganh();
            BigDecimal diemTong = dc.getDiemTong();

            if (diemTong != null && diemTong.compareTo(BigDecimal.ZERO) > 0) {
                diemCongByKey.computeIfAbsent(key, k -> new ArrayList<>()).add(dc);
                validCount++;
            }
        }
        System.out.println("  Điểm cộng: " + validCount + " records, " + diemCongByKey.size() + " keys");
    }

    private List<NguyenVongRaw> readNguyenVongFile(File file) throws Exception {
        List<NguyenVongRaw> result = new ArrayList<>();
        DataFormatter formatter = new DataFormatter();

        try (FileInputStream fis = new FileInputStream(file);
             Workbook wb = new XSSFWorkbook(fis)) {

            for (int i = 0; i < wb.getNumberOfSheets(); i++) {
                Sheet sheet = wb.getSheetAt(i);
                String sheetName = sheet.getSheetName();

                if (sheetName.toLowerCase().contains("tk") || sheetName.toLowerCase().contains("thống kê")) {
                    continue;
                }

                readSheetData(sheet, formatter, result);
            }
        }
        return result;
    }

    private void readSheetData(Sheet sheet, DataFormatter formatter, List<NguyenVongRaw> result) {
        int headerRow = findHeaderRow(sheet);
        if (headerRow == -1) return;

        Row header = sheet.getRow(headerRow);
        int colCccd = findColumnIndex(header, "CCCD");
        int colMaNganh = findColumnIndex(header, "Mã xét tuyển");
        int colThuTu = findColumnIndex(header, "Thứ tự NV");

        if (colCccd == -1 || colMaNganh == -1) return;

        for (int i = headerRow + 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;

            String rawCccd = formatter.formatCellValue(row.getCell(colCccd));
            String maNganh = formatter.formatCellValue(row.getCell(colMaNganh));

            if (rawCccd.isEmpty() || maNganh.isEmpty()) continue;

            NguyenVongRaw nv = new NguyenVongRaw();
            nv.setCccd(normalizeCCCD(rawCccd));
            nv.setMaNganh(maNganh.trim());
            nv.setSheetName(sheet.getSheetName());
            nv.setRowIndex(i);
            nv.setThuTu(parseThuTu(formatter.formatCellValue(row.getCell(colThuTu))));

            result.add(nv);
        }
    }

    private int parseThuTu(String value) {
        try {
            return value.isEmpty() ? 999 : Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return 999;
        }
    }

    private void processAndSave(List<NguyenVongRaw> rawList, ImportResult result, 
                                Consumer<Integer> progressCallback) {
        
        int batchSize = BATCH_SIZE;
        List<NguyenVongXetTuyen> batch = new ArrayList<>(batchSize);
        
        int total = rawList.size();
        int processed = 0;
        
        for (NguyenVongRaw raw : rawList) {
            // Chỉ lấy 1 bản ghi tốt nhất cho nguyện vọng này
            NguyenVongXetTuyen nv = processNguyenVong(raw);
            
            if (nv != null) {
                batch.add(nv);
                result.setSuccessRecords(result.getSuccessRecords() + 1);
            } else {
                result.setSkippedNoDiem(result.getSkippedNoDiem() + 1);
            }
            
            // Lưu batch khi đủ
            if (batch.size() >= batchSize) {
                saveBatch(batch);
                batch.clear();
                
                if (progressCallback != null) {
                    int percent = 25 + (int)((double)processed / total * 75);
                    progressCallback.accept(percent);
                }
            }
            
            processed++;
        }
        
        // Lưu batch cuối
        if (!batch.isEmpty()) {
            saveBatch(batch);
        }
    }

    private NguyenVongXetTuyen processNguyenVong(NguyenVongRaw raw) {
        List<DiemThiXetTuyen> diemThiList = diemThiByCccd.get(raw.getCccd());
        
        if (diemThiList == null || diemThiList.isEmpty()) {
            if (debugLogCount < 100) {
                System.out.println("BỎ QUA (không điểm): CCCD=" + raw.getCccd() + 
                                ", mã ngành=" + raw.getMaNganh());
                debugLogCount++;
            }
            return null;
        }
        
        BigDecimal diemCong = getDiemCong(raw.getCccd(), raw.getMaNganh());
        
        NguyenVongXetTuyen bestResult = null;
        BigDecimal bestScore = BigDecimal.ZERO;
        
        for (DiemThiXetTuyen diemThi : diemThiList) {
            String phuongThuc = diemThi.getPhuongThuc();
            
            BigDecimal thxt = BigDecimal.ZERO;
            String maToHop = null;
            
            if ("4".equals(phuongThuc)) {
                // ĐGNL
                thxt = convertDgnlTo30(diemThi.getDiemNangLuc());
                maToHop = "NL1";
            } else if ("5".equals(phuongThuc)) {
                // 🔥 V-SAT: Cần quy đổi từ thang 150 sang thang 10 cho từng môn
                ToHopProcessor.ToHopResult best = toHopProcessor.findBestToHop(diemThi, raw.getMaNganh());
                if (best != null && best.diemThxt.compareTo(BigDecimal.ZERO) > 0) {
                    // Lấy danh sách môn của tổ hợp
                    String[] monList = toHopProcessor.getMonListForToHop(best.maToHop);
                    if (monList != null && monList.length > 0) {
                        double total = 0;
                        for (String mon : monList) {
                            BigDecimal diemVsat = getDiemByMon(diemThi, mon);
                            if (diemVsat != null && diemVsat.compareTo(BigDecimal.ZERO) > 0) {
                                // 🔥 QUAN TRỌNG: Quy đổi từ thang 150 sang thang 10
                                BigDecimal converted = vsatConverter.convert(mon, diemVsat.doubleValue());
                                total += converted.doubleValue();
                            } else {
                                total = 0;
                                break;
                            }
                        }
                        thxt = BigDecimal.valueOf(total).setScale(2, RoundingMode.HALF_UP);
                        maToHop = best.maToHop;
                    }
                }
            } else {
                // PT3 (THPT)
                ToHopProcessor.ToHopResult best = toHopProcessor.findBestToHop(diemThi, raw.getMaNganh());
                if (best != null && best.diemThxt.compareTo(BigDecimal.ZERO) > 0) {
                    thxt = best.diemThxt;
                    maToHop = best.maToHop;
                }
            }
            
            if (thxt.compareTo(BigDecimal.ZERO) == 0) continue;
            
            // 🔥 Đảm bảo THXT không vượt quá 30 (sau quy đổi)
            if (thxt.compareTo(MAX_SCORE) > 0) {
                System.out.println("  WARN: THXT=" + thxt + " > 30, đang giới hạn về 30");
                thxt = MAX_SCORE;
            }
            
            BigDecimal utqd = calculateUuTienQuyDinh(thxt, diemCong);
            BigDecimal diemXetTuyen = thxt.add(utqd);
            
            // Debug
            if (raw.getRowIndex() <= 30) {
                System.out.println("  Xét PT" + phuongThuc + ": THXT=" + thxt + ", UTQD=" + utqd + ", XT=" + diemXetTuyen);
            }
            
            if (diemXetTuyen.compareTo(bestScore) > 0) {
                bestScore = diemXetTuyen;
                bestResult = buildNguyenVongEntity(
                    raw, phuongThuc, maToHop, thxt, utqd, diemCong, diemXetTuyen
                );
            }
        }
        
        return bestResult;
    }


    // Helper lấy điểm V-SAT theo môn
    private BigDecimal getDiemByMon(DiemThiXetTuyen diemThi, String mon) {
        return switch (mon.toUpperCase()) {
            case "TO" -> diemThi.getToan();
            case "LI" -> diemThi.getLy();
            case "HO" -> diemThi.getHoa();
            case "SI" -> diemThi.getSinh();
            case "SU" -> diemThi.getSu();
            case "DI" -> diemThi.getDia();
            case "VA" -> diemThi.getVan();
            case "NN", "N1" -> {
                // Lấy điểm cao nhất giữa N1_THI và N1_CC
                BigDecimal n1Thi = diemThi.getN1Thi() != null ? diemThi.getN1Thi() : BigDecimal.ZERO;
                BigDecimal n1CC = diemThi.getN1CC() != null ? diemThi.getN1CC() : BigDecimal.ZERO;
                yield n1Thi.compareTo(n1CC) > 0 ? n1Thi : n1CC;
            }
            default -> BigDecimal.ZERO;
        };
    }

    private BigDecimal convertDgnlTo30(BigDecimal dgnlScore) {
        if (dgnlScore == null || dgnlScore.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return dgnlScore.multiply(MAX_SCORE).divide(DGNL_MAX, 4, RoundingMode.HALF_UP)
                .setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateUuTienQuyDinh(BigDecimal thxt, BigDecimal diemCong) {
        if (diemCong == null || diemCong.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        if (thxt.compareTo(THRESHOLD) < 0) {
            return diemCong;
        }
        BigDecimal factor = MAX_SCORE.subtract(thxt).divide(new BigDecimal("7.5"), 4, RoundingMode.HALF_UP);
        return factor.multiply(diemCong).setScale(2, RoundingMode.HALF_UP);
    }

    private NguyenVongXetTuyen buildNguyenVongEntity(NguyenVongRaw raw,
                                                   String phuongThuc,
                                                   String maToHop,
                                                   BigDecimal thxt,
                                                   BigDecimal utqd,
                                                   BigDecimal diemCong,
                                                   BigDecimal diemXetTuyen) {
        NguyenVongXetTuyen nv = new NguyenVongXetTuyen();
        nv.setCccd(raw.getCccd());
        nv.setMaNganh(raw.getMaNganh());
        nv.setThuTu(raw.getThuTu());
        nv.setDiemThXT(thxt);
        nv.setDiemUuTienQD(utqd);
        nv.setDiemCong(diemCong);
        nv.setDiemXetTuyen(diemXetTuyen);
        nv.setPhuongThuc(phuongThuc);
        nv.setTtThm(null);
        // Key = CCCD_MANGANH_PHUONGTHUC
        nv.setNvKeys(raw.getCccd() + "_" + raw.getMaNganh() + "_" + phuongThuc);
        nv.setKetQua("CHUA_XET");
        
        return nv;
    }

    private BigDecimal getDiemCong(String cccd, String maNganh) {
        List<DiemCongXetTuyen> list = diemCongByKey.get(cccd + "_" + maNganh);
        if (list == null || list.isEmpty()) return BigDecimal.ZERO;
        BigDecimal diemTong = list.get(0).getDiemTong();
        return diemTong != null ? diemTong : BigDecimal.ZERO;
    }

    private void saveBatch(List<NguyenVongXetTuyen> batch) {
        for (NguyenVongXetTuyen nv : batch) {
            try {
                nguyenVongController.addNguyenVongXetTuyen(nv);
            } catch (Exception e) {
                System.err.println("Lỗi lưu: " + nv.getNvKeys() + " - " + e.getMessage());
            }
        }
    }

    private int findHeaderRow(Sheet sheet) {
        DataFormatter formatter = new DataFormatter();
        for (int i = 0; i <= 10; i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;
            for (Cell cell : row) {
                if ("CCCD".equalsIgnoreCase(formatter.formatCellValue(cell).trim())) {
                    return i;
                }
            }
        }
        return -1;
    }

    private int findColumnIndex(Row header, String columnName) {
        DataFormatter formatter = new DataFormatter();
        for (Cell cell : header) {
            if (columnName.equalsIgnoreCase(formatter.formatCellValue(cell).trim())) {
                return cell.getColumnIndex();
            }
        }
        return -1;
    }

    private String normalizeCCCD(String cccd) {
        if (cccd == null) return null;
        String result = cccd.replace("TS_", "").trim();
        return result.isEmpty() ? null : result;
    }

    private boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
}