package com.service;

import com.dto.ChartData;
import com.dto.ThongKeNVDTO;
import com.entity.Nganh;
import com.repository.NganhRepository;
import com.repository.ThongKeRepository;

import java.util.List;

import java.util.ArrayList;

public class ThongKeService {

    private final ThongKeRepository thongKeRepository;
    private final NganhRepository nganhRepository;

    public ThongKeService() {
        this.thongKeRepository = new ThongKeRepository();
        this.nganhRepository = new NganhRepository();
    }

    public ChartData getDuLieuKhuVuc() {
        List<Object[]> rawData = thongKeRepository.thongKeTheoKhuVuc();
        return convertToChartData(rawData, "Chưa phân khu vực");
    }

    public ChartData getDuLieuDoiTuong() {
        List<Object[]> rawData = thongKeRepository.thongKeTheoDoiTuong();
        return convertToChartData(rawData, "Không ưu tiên");
    }

    public List<Nganh> getAllNganh() {
        return nganhRepository.findAll().stream().toList();
    }

    private ChartData convertToChartData(List<Object[]> list, String defaultLabel) {
        double total = 0;
        for (Object[] row : list) {
            total += ((Number) row[1]).doubleValue();
        }

        List<String> finalLabels = new ArrayList<>();
        List<Double> finalValues = new ArrayList<>();
        double othersValue = 0;

        final double THRESHOLD = 0.01;

        for (Object[] row : list) {
            String label = (row[0] != null && !row[0].toString().trim().isEmpty()) ? row[0].toString() : defaultLabel;
            double value = ((Number) row[1]).doubleValue();

            if (total > 0 && (value / total) < THRESHOLD) {
                othersValue += value;
            } else {
                finalLabels.add(label);
                finalValues.add(value);
            }
        }

        if (othersValue > 0) {
            finalLabels.add("Khác");
            finalValues.add(othersValue);
        }

        String[] labelsArray = finalLabels.toArray(new String[0]);
        double[] valuesArray = finalValues.stream().mapToDouble(Double::doubleValue).toArray();

        return new ChartData(labelsArray, valuesArray);
    }

    public List<ThongKeNVDTO> getThongKeNguyenVong() {
        List<Object[]> rows = thongKeRepository.thongKeNguyenVongTheoNganh();

        List<ThongKeNVDTO> result = new ArrayList<>();
        for (int i = 0; i < rows.size(); i++) {
            Object[] r = rows.get(i);
            ThongKeNVDTO dto = new ThongKeNVDTO();
            dto.setStt          (i + 1);
            dto.setMaXetTuyen   ((String)  r[0]);
            dto.setTenMaXetTuyen((String)  r[1]);
            dto.setChiTieu2025  (toLong(r[2]));
            dto.setTongNV15     (toLong(r[3]));
            dto.setNv1          (toLong(r[4]));
            dto.setNv2          (toLong(r[5]));
            dto.setNv3          (toLong(r[6]));
            dto.setNv4          (toLong(r[7]));
            dto.setNv5          (toLong(r[8]));
            dto.setNvLon5       (toLong(r[9]));
            dto.setTongTatCaNV  (toLong(r[10]));
            result.add(dto);
        }

        result.add(buildDongTong(result));
        return result;
    }

    private ThongKeNVDTO buildDongTong(List<ThongKeNVDTO> list) {
        ThongKeNVDTO t = new ThongKeNVDTO();
        t.setStt(0);
        t.setMaXetTuyen("");
        t.setTenMaXetTuyen("Tổng");
        t.setIsDongTong(true);
        t.setChiTieu2025 (list.stream().mapToLong(ThongKeNVDTO::getChiTieu2025).sum());
        t.setTongNV15    (list.stream().mapToLong(ThongKeNVDTO::getTongNV15).sum());
        t.setNv1         (list.stream().mapToLong(ThongKeNVDTO::getNv1).sum());
        t.setNv2         (list.stream().mapToLong(ThongKeNVDTO::getNv2).sum());
        t.setNv3         (list.stream().mapToLong(ThongKeNVDTO::getNv3).sum());
        t.setNv4         (list.stream().mapToLong(ThongKeNVDTO::getNv4).sum());
        t.setNv5         (list.stream().mapToLong(ThongKeNVDTO::getNv5).sum());
        t.setNvLon5      (list.stream().mapToLong(ThongKeNVDTO::getNvLon5).sum());
        t.setTongTatCaNV (list.stream().mapToLong(ThongKeNVDTO::getTongTatCaNV).sum());
        return t;
    }

    private long toLong(Object o) {
        return o == null ? 0L : ((Number) o).longValue();
    }
}