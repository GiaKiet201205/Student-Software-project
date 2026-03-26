package com.ui.panel.QuyDoi;

import com.config.AppConfig;
import com.ui.common.BaseButton;
import com.ui.common.BasePanel;
import javax.swing.*;
import java.awt.*;

public class BangQuyDoiDialog extends JDialog {
    
    public BangQuyDoiDialog(Frame owner) {
        super(owner, "Thêm mới bảng quy đổi", true);
        setSize(600, 500);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());

        // Panel chính chứa form
        BasePanel rootPanel = new BasePanel(AppConfig.COLOR_WHITE, 0);
        rootPanel.setLayout(new BorderLayout(15, 15));
        rootPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Tiêu đề form
        JLabel lblHeader = new JLabel("THÔNG TIN QUY ĐỔI");
        lblHeader.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblHeader.setForeground(AppConfig.COLOR_PRIMARY);
        rootPanel.add(lblHeader, BorderLayout.NORTH);

        // Vùng nhập liệu chia 2 cột
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 20, 15));
        formPanel.setOpaque(false);

        // Các trường nhập liệu
        formPanel.add(createLabelField("Phương thức:"));
        formPanel.add(new JComboBox<>(new String[]{"V-SAT", "Học bạ", "ĐGNL"}));

        formPanel.add(createLabelField("Tổ hợp:"));
        formPanel.add(new JTextField());

        formPanel.add(createLabelField("Môn:"));
        formPanel.add(new JTextField());

        formPanel.add(createLabelField("Điểm A (Max):"));
        formPanel.add(new JTextField());

        formPanel.add(createLabelField("Mã quy đổi:"));
        formPanel.add(new JTextField());

        formPanel.add(createLabelField("Phân vị:"));
        formPanel.add(new JTextField());

        rootPanel.add(formPanel, BorderLayout.CENTER);

        // Nút bấm phía dưới
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actionPanel.setOpaque(false);
        BaseButton btnSave = new BaseButton(" Lưu dữ liệu ", new Color(39, 174, 96));
        BaseButton btnCancel = new BaseButton(" Hủy bỏ ", new Color(149, 165, 166));
        
        btnCancel.addActionListener(e -> dispose());
        
        actionPanel.add(btnCancel);
        actionPanel.add(btnSave);
        rootPanel.add(actionPanel, BorderLayout.SOUTH);

        add(rootPanel);
    }

    private JLabel createLabelField(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return label;
    }
}
