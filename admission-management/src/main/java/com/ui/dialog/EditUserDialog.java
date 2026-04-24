package com.ui.dialog;

import com.controller.NguoiDungController;
import com.dto.Status;
import com.entity.NguoiDung;
import com.ui.common.BaseButton;

import javax.swing.*;
import java.awt.*;

public class EditUserDialog extends JDialog {

    private final NguoiDungController controller = new NguoiDungController();
    private Integer userId;
    private JTextField txtUsername, txtPassword, txtId;
    private JComboBox<Status> comboBox;


    public EditUserDialog(Integer id) {
        this.userId = id;

        setTitle("Sửa Người Dùng");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setModal(true);
        setResizable(false);

        initComponents();
        loadData();
    }

     private void initComponents() {
         txtId = new JTextField();
         txtId.setEditable(false);
         txtId.setPreferredSize(new Dimension(300, 35));

         txtUsername = new JTextField();
         txtUsername.setPreferredSize(new Dimension(300, 35));

         txtPassword = new JTextField();
         txtPassword.setPreferredSize(new Dimension(300, 35));

         comboBox = new JComboBox<>();
         comboBox.addItem(new Status(0, "ADMIN"));
         comboBox.addItem(new Status(1, "USER"));
         comboBox.setPreferredSize(new Dimension(300, 35));

         BaseButton btnSave = new BaseButton("Lưu");
         BaseButton btnCancel = new BaseButton("Hủy");
         btnSave.addActionListener(e -> updateUser());
         btnCancel.addActionListener(e -> dispose());

         // Panel chính với padding
         JPanel mainPanel = new JPanel();
         mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
         mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

         // Panel cho từng hàng input
         JPanel idPanel = createInputRow("ID:", txtId);
         JPanel usernamePanel = createInputRow("Tài khoản:", txtUsername);
         JPanel passwordPanel = createInputRow("Mật khẩu:", txtPassword);
         JPanel rolePanel = createInputRow("Quyền:", comboBox);

         // Thêm các panel vào main panel
         mainPanel.add(idPanel);
         mainPanel.add(Box.createVerticalStrut(20));
         mainPanel.add(usernamePanel);
         mainPanel.add(Box.createVerticalStrut(20));
         mainPanel.add(passwordPanel);
         mainPanel.add(Box.createVerticalStrut(20));
         mainPanel.add(rolePanel);
         mainPanel.add(Box.createVerticalGlue());

         // Panel cho buttons
         JPanel buttonPanel = new JPanel();
         buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
         buttonPanel.add(btnSave);
         buttonPanel.add(btnCancel);

         add(mainPanel, BorderLayout.CENTER);
         add(buttonPanel, BorderLayout.SOUTH);
     }

    private JPanel createInputRow(String labelText, JComponent inputComponent) {
        JPanel row = new JPanel();
        row.setLayout(new BorderLayout(15, 0));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));

        JLabel label = new JLabel(labelText);
        label.setPreferredSize(new Dimension(100, 35));
        label.setFont(new Font("Arial", Font.PLAIN, 14));

        inputComponent.setFont(new Font("Arial", Font.PLAIN, 14));

        row.add(label, BorderLayout.WEST);
        row.add(inputComponent, BorderLayout.CENTER);

        return row;
    }

     private void loadData(){
         NguoiDung nd = controller.getById(userId);

         if (nd != null) {
             txtId.setText(nd.getIdNguoiDung().toString());
             txtUsername.setText(nd.getUsername() != null ? nd.getUsername() : "");
             txtPassword.setText(nd.getPassword() != null ? nd.getPassword() : "");
             comboBox.setSelectedItem(new Status(nd.getRole(), nd.getRole() == 0 ? "ADMIN" : "USER"));
         } else {
             JOptionPane.showMessageDialog(this, "Không tìm thấy người dùng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
             dispose();
         }
     }

    private void updateUser() {

        if (txtUsername.getText().trim().isEmpty()) {
            showError("Tài khoản không được để trống!");
            return;
        }
        if (txtPassword.getText().trim().isEmpty()) {
            showError("Mật khẩu không được để trống!");
            return;
        }

        try {
            NguoiDung nd = controller.getById(userId);

            if (nd == null) {
                JOptionPane.showMessageDialog(this, "Người dùng không tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            nd.setUsername(txtUsername.getText().trim());
            nd.setPassword(txtPassword.getText().trim());
            Status selectedStatus = (Status) comboBox.getSelectedItem();
            if (selectedStatus != null) {
                nd.setRole(selectedStatus.getId());
            }
           controller.update(nd);

            JOptionPane.showMessageDialog(this, "Cập nhật thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        }catch (IllegalArgumentException e){
            showError(e.getMessage());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }
}
