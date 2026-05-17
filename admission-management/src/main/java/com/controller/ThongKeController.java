package com.controller;

import com.dto.ChartData;
import com.dto.ThongKeNVDTO;
import com.dto.ThongKeSLTrungTuyenDTO;
import com.entity.Nganh;
import com.service.ThongKeNVExporter;
import com.service.ThongKeService;
import com.service.ThongKeSLTrungTuyenExporter;
import com.ui.panel.ThongKeMainPanel;

import javax.swing.*;
import java.util.List;

public class ThongKeController {

    private final ThongKeMainPanel mainPanel;
    private final ThongKeService thongKeService;

    public ThongKeController(ThongKeMainPanel mainPanel) {
        this.mainPanel = mainPanel;
        this.thongKeService = new ThongKeService();
    }

    public void loadNganh() {
        List<Nganh> listNganh = thongKeService.getAllNganh();
        mainPanel.setCbNganh(listNganh);
    }

    public void loadBieuDoKhuVuc() {
        ChartData data = thongKeService.getDuLieuKhuVuc();
        mainPanel.getChartPanel().setData(data);
    }

    public void loadBieuDoDoiTuong() {
        ChartData data = thongKeService.getDuLieuDoiTuong();
        mainPanel.getChartPanel().setData(data);
    }

    public void handleExportThongKe() {
        List<ThongKeNVDTO> data = thongKeService.getThongKeNguyenVong();
        ThongKeNVExporter.exportWithChooser(data, this.mainPanel);
    }

    public void xuLyKhiDoiNganh(Integer idNganh) {

    }

    public void handleExportSLTrungTuyenPTNganh() {
        List<ThongKeSLTrungTuyenDTO> data = thongKeService.getThongKeSLTrungTuyenPTNganh();
        if (data.isEmpty()) {
            JOptionPane.showMessageDialog(mainPanel, "Không có dữ liệu trúng tuyển để xuất.", "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        ThongKeSLTrungTuyenExporter.exportWithChooser(data, this.mainPanel);
    }
}