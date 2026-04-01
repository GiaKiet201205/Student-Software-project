package com.ui.panel;

import com.controller.NguoiDungController;
import com.dto.Status;
import com.entity.NguoiDung;
import com.entity.ThiSinhXetTuyen25;
import com.service.NguoiDungService;
import com.ui.common.BaseButton;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class EditUserDialog extends JDialog {

    private final NguoiDungController controller = new NguoiDungController();
    private Integer userId;
    private JTextField txtUsername, txtPassword, txtId;
    private JComboBox<Status> comboBox;


    public EditUserDialog(Integer id) {
        this.userId = id;

        setTitle("Sửa Người Dùng");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setModal(true);

        initComponents();
        loadData();
    }

     private void initComponents() {

         txtId = new JTextField();
         txtId.setEditable(false);
         txtUsername = new JTextField();
         txtPassword = new JTextField();
         comboBox = new JComboBox<>();
         comboBox.addItem(new Status(0, "ADMIN"));
         comboBox.addItem(new Status(1, "USER"));

         BaseButton btnSave = new BaseButton("Lưu");
         BaseButton btnCancel = new BaseButton("Hủy");
         btnSave.addActionListener(e -> updateUser());
         btnCancel.addActionListener(e -> dispose());

         JPanel mainPanel = new JPanel();
         mainPanel.setLayout(new GridLayout(7, 2, 10, 10));

         mainPanel.add(new JLabel("ID"));
         mainPanel.add(txtId);
         mainPanel.add(new JLabel("Tài khoản"));
         mainPanel.add(txtUsername);
         mainPanel.add(new JLabel("Mật khẩu"));
         mainPanel.add(txtPassword);
         mainPanel.add(new JLabel("Quyền"));
         mainPanel.add(comboBox);


         JPanel buttonPanel = new JPanel();
         buttonPanel.add(btnSave);
         buttonPanel.add(btnCancel);

         add(mainPanel, BorderLayout.CENTER);
         add(buttonPanel, BorderLayout.SOUTH);
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
