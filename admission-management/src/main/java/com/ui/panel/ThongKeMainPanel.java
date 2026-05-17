package com.ui.panel;

import com.controller.ThongKeController;
import com.entity.Nganh;
import com.ui.common.BaseButton;
import com.ui.common.BasePanel;
import lombok.Getter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.List;

public class ThongKeMainPanel extends BasePanel {

    @Getter
    private ThongKePanel chartPanel;
    private JComboBox<Nganh> cbNganh;
    private BaseButton btnKhuVuc, btnDoiTuong, xuatThongKe, xuatDSTTTheoNganh, xuatDSTTTheoPT;

    private ThongKeController controller;

    public ThongKeMainPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(15, 15, 15, 15));

        chartPanel = new ThongKePanel();

        add(createTopPanel(), BorderLayout.NORTH);

        add(createCenterArea(), BorderLayout.CENTER);

        controller = new ThongKeController(this);

        initEvents();
        controller.loadNganh();

        SwingUtilities.invokeLater(() -> controller.loadBieuDoKhuVuc());
    }

    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBackground(Color.WHITE);

        JPanel exportPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        exportPanel.setBackground(Color.WHITE);

        // TODO Khởi tạo thêm button
        this.xuatThongKe = new BaseButton("Xuất thống kê nguyện vọng");
        this.xuatDSTTTheoPT = new BaseButton("Xuất SL trúng tuyển PT/Ngành");
        this.xuatDSTTTheoNganh = new BaseButton("Xuất DS trúng tuyển theo ngành");

        exportPanel.add(xuatThongKe);
        exportPanel.add(xuatDSTTTheoNganh);
        exportPanel.add(xuatDSTTTheoPT);

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        filterPanel.setBackground(Color.WHITE);
        filterPanel.add(new JLabel("Chọn ngành đào tạo: "));

        cbNganh = new JComboBox<>();
        cbNganh.setPreferredSize(new Dimension(300, 30));
        filterPanel.add(cbNganh);

        topPanel.add(exportPanel);
        topPanel.add(filterPanel);
        topPanel.add(new JSeparator());

        return topPanel;
    }

    private JPanel createCenterArea() {
        JPanel centerArea = new JPanel(new BorderLayout(0, 10));
        centerArea.setBackground(Color.WHITE);

        JPanel togglePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        togglePanel.setBackground(Color.WHITE);

        btnKhuVuc = new BaseButton("Thống kê theo Khu Vực");
        btnDoiTuong = new BaseButton("Thống kê theo Đối Tượng");

        togglePanel.add(btnKhuVuc);
        togglePanel.add(btnDoiTuong);

        centerArea.add(togglePanel, BorderLayout.NORTH);
        centerArea.add(chartPanel, BorderLayout.CENTER);

        return centerArea;
    }

    private void initEvents() {
        btnKhuVuc.addActionListener(e -> controller.loadBieuDoKhuVuc());
        btnDoiTuong.addActionListener(e -> controller.loadBieuDoDoiTuong());

        cbNganh.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                Nganh selected = (Nganh) cbNganh.getSelectedItem();
                if (selected != null && selected.getIdNganh() != 0) {
                    // controller.xuLyKhiDoiNganh(selected.getIdNganh());
                }
            }
        });

        xuatThongKe.addActionListener(e -> {
            controller.handleExportThongKe();
        });

        xuatDSTTTheoPT.addActionListener(e -> {
            controller.handleExportSoLuongTrungTuyenTheoPTNganh();
        });

    }

    public void setCbNganh(List<Nganh> listNganh) {
        DefaultComboBoxModel<Nganh> model = new DefaultComboBoxModel<>();

        Nganh tatCa = new Nganh();
        tatCa.setIdNganh(0);
        tatCa.setTenNganh("--- Tất cả các ngành ---");
        model.addElement(tatCa);

        for (Nganh n : listNganh) {
            model.addElement(n);
        }

        cbNganh.setModel(model);
    }
}