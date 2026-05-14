package com.service;

import com.entity.BangQuyDoi;
import com.exception.AppException;
import com.repository.BangQuyDoiRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class BangQuyDoiService {

    private final BangQuyDoiRepository repository = new BangQuyDoiRepository();

    // ===================== READ =====================

    public List<BangQuyDoi> getAll() {
        return repository.findAll();
    }

    public List<BangQuyDoi> search(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return repository.findAll();
        }
        return repository.search(keyword.trim());
    }

    public List<BangQuyDoi> getByPhuongThuc(String phuongthuc) {
        return repository.findByPhuongThuc(phuongthuc);
    }

    public List<BangQuyDoi> getByPhuongThucAndToHop(String phuongthuc, String tohop) {
        return repository.findByPhuongThucAndToHop(phuongthuc, tohop);
    }

    public List<BangQuyDoi> getByPhuongThucAndMon(String phuongthuc, String mon) {
        return repository.findByPhuongThucAndMon(phuongthuc, mon);
    }

    public BangQuyDoi getById(int id) {
        return repository.findById(id);
    }

    public List<String> getDistinctPhuongThuc() {
        return repository.findDistinctPhuongThuc();
    }

    // ===================== CREATE =====================

    public void add(BangQuyDoi bangQuyDoi) {
        validate(bangQuyDoi, null);
        repository.save(bangQuyDoi);
    }

    // ===================== UPDATE =====================

    public void update(BangQuyDoi bangQuyDoi) {
        validate(bangQuyDoi, bangQuyDoi.getIdqd());
        repository.update(bangQuyDoi);
    }

    // ===================== DELETE =====================

    public boolean delete(int id) {
        return repository.deleteById(id);
    }

    // ===================== VALIDATION =====================

    private void validate(BangQuyDoi bqd, Integer excludeId) {
        // Lombok @Data tạo getter theo field name: phuongthuc → getPhuongthuc()
        if (bqd.getPhuongthuc() == null || bqd.getPhuongthuc().trim().isEmpty()) {
            throw new AppException("Phương thức không được để trống.");
        }
        if (bqd.getDiemA() == null || bqd.getDiemB() == null
                || bqd.getDiemC() == null || bqd.getDiemD() == null) {
            throw new AppException("Các giá trị điểm A, B, C, D không được để trống.");
        }
        if (bqd.getDiemA().compareTo(bqd.getDiemB()) > 0) {
            throw new AppException("Điểm A phải nhỏ hơn hoặc bằng Điểm B.");
        }
        if (bqd.getDiemC().compareTo(bqd.getDiemD()) > 0) {
            throw new AppException("Điểm C phải nhỏ hơn hoặc bằng Điểm D.");
        }
        if (bqd.getMaQuydoi() == null || bqd.getMaQuydoi().trim().isEmpty()) {
            throw new AppException("Mã quy đổi không được để trống.");
        }
        if (repository.existsByMaQuydoi(bqd.getMaQuydoi().trim(), excludeId)) {
            throw new AppException("Mã quy đổi '" + bqd.getMaQuydoi() + "' đã tồn tại.");
        }
    }

    // ===================== IMPORT EXCEL =====================

    /**
     * Format cột Excel (header row 0, data từ row 1):
     *   0: d_phuongthuc | 1: d_tohop | 2: d_mon
     *   3: d_diema      | 4: d_diemb | 5: d_diemc | 6: d_diemd
     *   7: d_maquydoi   | 8: d_phanvi
     */
    public ImportSummary importFromExcel(File file) throws Exception {
        int success = 0, skip = 0, error = 0;
        List<String> errorDetails = new ArrayList<>();
        List<BangQuyDoi> batch = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(file);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null || isRowEmpty(row)) continue;

                try {
                    BangQuyDoi bqd = parseRow(row);
                    if (bqd == null) { skip++; continue; }

                    if (repository.existsByMaQuydoi(bqd.getMaQuydoi(), null)) {
                        skip++;
                        continue;
                    }

                    batch.add(bqd);
                    success++;

                } catch (AppException ae) {
                    error++;
                    errorDetails.add("Dòng " + (i + 1) + ": " + ae.getMessage());
                } catch (Exception e) {
                    error++;
                    errorDetails.add("Dòng " + (i + 1) + ": Lỗi không xác định - " + e.getMessage());
                }
            }
        }

        if (!batch.isEmpty()) {
            repository.saveAll(batch);
        }

        return new ImportSummary(success, skip, error, errorDetails);
    }

    private BangQuyDoi parseRow(Row row) {
        String phuongthuc = getCellString(row, 0);
        String tohop      = getCellString(row, 1);
        String mon        = getCellString(row, 2);
        String diemAStr   = getCellString(row, 3);
        String diemBStr   = getCellString(row, 4);
        String diemCStr   = getCellString(row, 5);
        String diemDStr   = getCellString(row, 6);
        String maQuydoi   = getCellString(row, 7);
        String phanVi     = getCellString(row, 8);

        if (phuongthuc.isEmpty() && maQuydoi.isEmpty()) return null;

        if (phuongthuc.isEmpty()) throw new AppException("Thiếu phương thức");
        if (maQuydoi.isEmpty())   throw new AppException("Thiếu mã quy đổi");
        if (diemAStr.isEmpty() || diemBStr.isEmpty() || diemCStr.isEmpty() || diemDStr.isEmpty()) {
            throw new AppException("Thiếu giá trị điểm A/B/C/D");
        }

        // Dùng Lombok @Builder để tạo object gọn hơn
        return BangQuyDoi.builder()
                .phuongthuc(phuongthuc)
                .tohop(tohop.isEmpty() ? null : tohop)
                .mon(mon.isEmpty() ? null : mon)
                .diemA(parseBigDecimal(diemAStr, "Điểm A"))
                .diemB(parseBigDecimal(diemBStr, "Điểm B"))
                .diemC(parseBigDecimal(diemCStr, "Điểm C"))
                .diemD(parseBigDecimal(diemDStr, "Điểm D"))
                .maQuydoi(maQuydoi)
                .phanVi(phanVi.isEmpty() ? null : phanVi)
                .build();
    }

    // ===================== EXCEL HELPERS =====================

    private String getCellString(Row row, int col) {

        Cell cell = row.getCell(
                col,
                Row.MissingCellPolicy.RETURN_BLANK_AS_NULL
        );

        if (cell == null) {
            return "";
        }

        CellType cellType = cell.getCellType();

        if (cellType == CellType.STRING) {

            return cell.getStringCellValue().trim();

        } else if (cellType == CellType.NUMERIC) {

            double d = cell.getNumericCellValue();

            return (d == Math.floor(d) && !Double.isInfinite(d))
                    ? String.valueOf((long) d)
                    : String.valueOf(d);

        } else if (cellType == CellType.BOOLEAN) {

            return String.valueOf(cell.getBooleanCellValue());

        } else if (cellType == CellType.FORMULA) {

            try {

                return cell.getStringCellValue();

            } catch (Exception e) {

                try {

                    return String.valueOf(
                            cell.getNumericCellValue()
                    );

                } catch (Exception ex) {

                    return "";
                }
            }

        } else {

            return "";
        }
    }

    private BigDecimal parseBigDecimal(String value, String fieldName) {
        try {
            return new BigDecimal(value.replace(",", "."));
        } catch (NumberFormatException e) {
            throw new AppException(fieldName + " không hợp lệ: '" + value + "'");
        }
    }

    private boolean isRowEmpty(Row row) {
        for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
            Cell cell = row.getCell(c);
            if (cell != null && cell.getCellType() != CellType.BLANK) return false;
        }
        return true;
    }

    // ===================== INNER CLASS =====================

    public static class ImportSummary {
        public final int success;
        public final int skip;
        public final int error;
        public final List<String> errorDetails;

        public ImportSummary(int success, int skip, int error, List<String> errorDetails) {
            this.success      = success;
            this.skip         = skip;
            this.error        = error;
            this.errorDetails = errorDetails;
        }

        @Override
        public String toString() {
            return String.format("✅ Thành công: %d  |  ⏭ Bỏ qua: %d  |  ❌ Lỗi: %d", success, skip, error);
        }
    }
}