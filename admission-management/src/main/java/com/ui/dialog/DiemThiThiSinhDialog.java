package com.ui.dialog;

import com.controller.ThiSinhXetTuyen25Controller;
import com.entity.DiemThiXetTuyen;
import com.ui.common.BaseButton;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;

public class DiemThiThiSinhDialog extends JDialog {

    private final ThiSinhXetTuyen25Controller controller = new ThiSinhXetTuyen25Controller();
    private JTextField txtTHPT, txtDGNL, txtVSAT;
    private final String cccd;
    public DiemThiThiSinhDialog(String cccd, boolean modal) {
        this.cccd = cccd;

        setTitle("Điểm Thí Sinh");
        setSize(1000, 300);
        setLocationRelativeTo(null);
        setModal(modal);
        setResizable(false);

        initComponents();
        loadData();
    }

    private void initComponents() {
        txtTHPT = new JTextField();
        txtTHPT.setEditable(false);
        txtTHPT.setPreferredSize(new Dimension(300, 35));

        txtDGNL = new JTextField();
        txtDGNL.setEditable(false);
        txtDGNL.setPreferredSize(new Dimension(300, 35));

        txtVSAT = new JTextField();
        txtVSAT.setEditable(false);
        txtVSAT.setPreferredSize(new Dimension(300, 35));



        BaseButton btnCancel = new BaseButton("Hủy");
        btnCancel.addActionListener(e -> dispose());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        mainPanel.add(createInputRow("Điểm THPT:", txtTHPT));
        mainPanel.add(Box.createVerticalStrut(15));

        mainPanel.add(createInputRow("Điểm ĐGNL:", txtDGNL));
        mainPanel.add(Box.createVerticalStrut(15));

        mainPanel.add(createInputRow("Điểm V-SAT:", txtVSAT));
        mainPanel.add(Box.createVerticalStrut(15));


        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(null);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonPanel.add(btnCancel);

        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JPanel createInputRow(String labelText, JComponent inputComponent) {
        JPanel row = new JPanel();
        row.setLayout(new BorderLayout(15, 0));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));

        JLabel label = new JLabel(labelText);
        label.setPreferredSize(new Dimension(150, 35));
        label.setFont(new Font("Arial", Font.PLAIN, 14));

        inputComponent.setFont(new Font("Arial", Font.PLAIN, 14));

        row.add(label, BorderLayout.WEST);
        row.add(inputComponent, BorderLayout.CENTER);

        return row;
    }

    private void loadData() {
        DiemThiXetTuyen diemThpt = controller.getDiemThiByCccdAndPhuongThuc(cccd, "3");
        DiemThiXetTuyen diemDgnl = controller.getDiemThiByCccdAndPhuongThuc(cccd, "4");
        DiemThiXetTuyen diemVsat = controller.getDiemThiByCccdAndPhuongThuc(cccd, "5");

        txtTHPT.setText(formatThpt(diemThpt));
        txtDGNL.setText(formatDgnl(diemDgnl));
        txtVSAT.setText(formatVsat(diemVsat));
    }

    private String formatThpt(DiemThiXetTuyen diemThi) {
        if (diemThi == null) return "không có điểm";
        String result = buildMonThpt(diemThi);
        return result.isEmpty() ? "không có điểm" : result;
    }

    private String formatDgnl(DiemThiXetTuyen diemThi) {
        if (diemThi == null || diemThi.getDiemNangLuc() == null) return "không có điểm";
        return diemThi.getDiemNangLuc().toPlainString();
    }

    private String formatVsat(DiemThiXetTuyen diemThi) {
        if (diemThi == null) return "không có điểm";
        String result = buildMonVsat(diemThi);
        return result.isEmpty() ? "không có điểm" : result;
    }

    private String buildMonThpt(DiemThiXetTuyen diemThi) {
        StringBuilder builder = new StringBuilder();
        appendMon(builder, "Toán", diemThi.getToan());
        appendMon(builder, "Lý", diemThi.getLy());
        appendMon(builder, "Hóa", diemThi.getHoa());
        appendMon(builder, "Sinh", diemThi.getSinh());
        appendMon(builder, "Sử", diemThi.getSu());
        appendMon(builder, "Địa", diemThi.getDia());
        appendMon(builder, "Văn", diemThi.getVan());
        appendMon(builder, "Anh Văn", diemThi.getN1Thi());
        appendMon(builder, "NK1", diemThi.getDiemNangKhieu1());
        appendMon(builder, "NK2", diemThi.getDiemNangKhieu2());
        appendMon(builder, "NK3", diemThi.getDiemNangKhieu3());
        appendMon(builder, "NK4", diemThi.getDiemNangKhieu4());
        return builder.toString();
    }

    private String buildMonVsat(DiemThiXetTuyen diemThi) {
        StringBuilder builder = new StringBuilder();
        appendMon(builder, "Toán", diemThi.getToan());
        appendMon(builder, "Lý", diemThi.getLy());
        appendMon(builder, "Hóa", diemThi.getHoa());
        appendMon(builder, "Sinh", diemThi.getSinh());
        appendMon(builder, "Sử", diemThi.getSu());
        appendMon(builder, "Địa", diemThi.getDia());
        appendMon(builder, "Văn", diemThi.getVan());
        appendMon(builder, "Anh Văn", diemThi.getN1Thi());
        return builder.toString();
    }

    private void appendMon(StringBuilder builder, String label, BigDecimal value) {
        if (value == null) return;
        if (builder.length() > 0) {
            builder.append(' ');
        }
        builder.append(label).append(':').append(value.toPlainString());
    }

}
