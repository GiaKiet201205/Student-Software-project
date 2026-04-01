package com.service;

import com.entity.ThiSinhXetTuyen25;
import com.repository.ThiSinhXetTuyen25Repository;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileInputStream;

public class ThiSinhXetTuyen25Service {
    private final ThiSinhXetTuyen25Repository repository = new ThiSinhXetTuyen25Repository();

    public List<ThiSinhXetTuyen25> getAll() {
        return repository.findAll();
    }

    public List<ThiSinhXetTuyen25> getAllByPage(int page, int pageSize) {
        return repository.findAllByPage(page, pageSize);
    }

    public long countAll() {
        return repository.countAll();
    }

    public List<ThiSinhXetTuyen25> searchByCccdOrNameByPage(String keyword, int page, int pageSize) {
        return repository.searchByCccdOrNameByPage(keyword, page, pageSize);
    }

    public long countSearch(String keyword) {
        return repository.countSearch(keyword);
    }

    public ThiSinhXetTuyen25 getById(Integer id) {
        return repository.findById(id);
    }

    public ThiSinhXetTuyen25 create(ThiSinhXetTuyen25 entity) {
        return repository.save(entity);
    }

    public ThiSinhXetTuyen25 update(ThiSinhXetTuyen25 entity) {

        if (repository.existsByEmailExceptId(entity.getEmail(), entity.getIdThiSinh())) {
            throw new IllegalArgumentException("Email đã tồn tại!");
        }

        if (repository.existsByCccdExceptId(entity.getCccd(), entity.getIdThiSinh())) {
            throw new IllegalArgumentException("CCCD đã tồn tại!");
        }

        return repository.update(entity);
    }

    public void delete(Integer id) {
        ThiSinhXetTuyen25 entity = repository.findById(id);
        if (entity != null) {
            repository.delete(entity);
        }
    }

    // Import Excel
    public int importFromExcel(File file) {
        int count = 0;
        try (FileInputStream fis = new FileInputStream(file);
             XSSFWorkbook workbook = new XSSFWorkbook(fis)) {

            XSSFSheet sheet = workbook.getSheetAt(0);

            // Bỏ qua header (dòng 0)
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                try {
                    String cccd = getCellStringValue(row, 0);
                    String soBaoDanh = getCellStringValue(row, 1);
                    String password = getCellStringValue(row, 2);
                    String ho = getCellStringValue(row, 3);
                    String ten = getCellStringValue(row, 4);
                    String ngaySinh = getCellStringValue(row, 5);
                    String dienThoai = getCellStringValue(row, 6);
                    String email = getCellStringValue(row, 7);
                    String gioiTinh = getCellStringValue(row, 8);
                    String noiSinh = getCellStringValue(row, 9);
                    String doiTuong = getCellStringValue(row, 10);
                    String khuVuc = getCellStringValue(row, 11);


                    ThiSinhXetTuyen25 entity = ThiSinhXetTuyen25.builder()
                            .cccd(cccd)
                            .soBaoDanh(soBaoDanh)
                            .password(password)
                            .ho(ho)
                            .ten(ten)
                            .ngaySinh(ngaySinh)
                            .dienThoai(dienThoai)
                            .email(email)
                            .gioiTinh(gioiTinh)
                            .noiSinh(noiSinh)
                            .doiTuong(doiTuong)
                            .khuVuc(khuVuc)
                            .build();

                    repository.save(entity);
                    count++;
                } catch (Exception ex) {
                    // Bỏ qua dòng có lỗi
                    System.err.println("Lỗi ở dòng " + (i + 1) + ": " + ex.getMessage());
                }
            }
        } catch (Exception e) {
            throw new com.exception.AppException("Lỗi khi import file Excel!", e);
        }
        return count;
    }

    private String getCellStringValue(Row row, int colIndex) {
        Cell cell = row.getCell(colIndex);
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf((int) cell.getNumericCellValue());
            default:
                return "";


        }
    }
}