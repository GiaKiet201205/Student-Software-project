package com.repository;

import com.entity.DiemCongXetTuyen;
import com.config.HibernateUtil;
import com.exception.AppException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class DiemCongXetTuyenRepository extends BaseRepository<DiemCongXetTuyen> {
    
  private static final int BATCH_SIZE = 1000;
  
  public DiemCongXetTuyenRepository() {
      super(DiemCongXetTuyen.class);
  }
  
  /**
   * Lưu batch điểm cộng xét tuyển
   */
  public void saveBatchOptimized(List<DiemCongXetTuyen> list, int batchSize) {
      if (list == null || list.isEmpty()) return;
      
      String sql = "INSERT INTO xt_diemcongxetuyen " +
          "(ts_cccd, manganh, matohop, phuongthuc, diemcc, diemutxt, diemtong, ghichu, dc_keys) " +
          "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) " +
          "ON DUPLICATE KEY UPDATE " +
          "diemcc = VALUES(diemcc), " +
          "diemutxt = VALUES(diemutxt), " +
          "diemtong = VALUES(diemtong)";
      
      try (Session session = HibernateUtil.getSessionFactory().openSession()) {
          Transaction tx = session.beginTransaction();
          
          session.doWork(connection -> {
              try (java.sql.PreparedStatement stmt = connection.prepareStatement(sql)) {
                  int count = 0;
                  for (DiemCongXetTuyen dc : list) {
                      stmt.setString(1, dc.getCccd());
                      stmt.setString(2, dc.getMaNganh());
                      stmt.setString(3, dc.getMaToHop());
                      stmt.setString(4, dc.getPhuongThuc());
                      stmt.setBigDecimal(5, dc.getDiemCC());
                      stmt.setBigDecimal(6, dc.getDiemUuTienXT());
                      stmt.setBigDecimal(7, dc.getDiemTong());
                      stmt.setString(8, dc.getGhiChu());
                      stmt.setString(9, dc.getDcKeys());
                      stmt.addBatch();
                      
                      if (++count % batchSize == 0) {
                          stmt.executeBatch();
                      }
                  }
                  stmt.executeBatch();
              }
          });
          
          tx.commit();
          System.out.println("✓ Đã lưu batch điểm cộng: " + list.size() + " bản ghi");
      } catch (Exception e) {
          throw new AppException("Lỗi khi lưu batch DiemCongXetTuyen!", e);
      }
  }
  
}