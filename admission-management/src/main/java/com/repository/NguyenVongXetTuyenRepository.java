package com.repository;

import com.entity.NguyenVongXetTuyen;
import com.config.HibernateUtil;
import com.exception.AppException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;

import java.util.List;

public class NguyenVongXetTuyenRepository extends BaseRepository<NguyenVongXetTuyen> {
    
    private static final int BATCH_SIZE = 1000;
    
    public NguyenVongXetTuyenRepository() {
        super(NguyenVongXetTuyen.class);
    }
    
    // ==================== BATCH INSERT OPTIMIZED ====================
    
    /**
     * Lưu batch với JDBC batch - nhanh hơn Hibernate save nhiều lần
     * @param list Danh sách entity cần lưu
     * @param batchSize Kích thước batch
     */
    public void saveBatchOptimized(List<NguyenVongXetTuyen> list, int batchSize) {
        if (list == null || list.isEmpty()) return;
        
        String sql = "INSERT INTO xt_nguyenvongxettuyen " +
            "(nn_cccd, nv_manganh, nv_tt, diem_thxt, diem_utqd, diem_cong, diem_xettuyen, " +
            "nv_keys, tt_phuongthuc, tt_thm, nv_ketqua, nv_rank) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
            "ON DUPLICATE KEY UPDATE " +
            "diem_thxt = VALUES(diem_thxt), " +
            "diem_utqd = VALUES(diem_utqd), " +
            "diem_cong = VALUES(diem_cong), " +
            "diem_xettuyen = VALUES(diem_xettuyen), " +
            "nv_ketqua = VALUES(nv_ketqua), " +
            "nv_rank = VALUES(nv_rank)";
        
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            
            session.doWork(connection -> {
                try (java.sql.PreparedStatement stmt = connection.prepareStatement(sql)) {
                    int count = 0;
                    for (NguyenVongXetTuyen nv : list) {
                        stmt.setString(1, nv.getCccd());
                        stmt.setString(2, nv.getMaNganh());
                        stmt.setInt(3, nv.getThuTu());
                        stmt.setBigDecimal(4, nv.getDiemThXT());
                        stmt.setBigDecimal(5, nv.getDiemUuTienQD());
                        stmt.setBigDecimal(6, nv.getDiemCong());
                        stmt.setBigDecimal(7, nv.getDiemXetTuyen());
                        stmt.setString(8, nv.getNvKeys());
                        stmt.setString(9, nv.getPhuongThuc());
                        stmt.setString(10, nv.getTtThm());
                        stmt.setString(11, nv.getKetQua());
                        stmt.setInt(12, nv.getThuTuXetTuyen() != null ? nv.getThuTuXetTuyen() : 0);
                        stmt.addBatch();
                        
                        if (++count % batchSize == 0) {
                            stmt.executeBatch();
                        }
                    }
                    stmt.executeBatch();
                }
            });
            
            tx.commit();
            System.out.println("✓ Đã lưu batch " + list.size() + " bản ghi");
        } catch (Exception e) {
            throw new AppException("Lỗi khi lưu batch NguyenVongXetTuyen!", e);
        }
    }
    
    // ==================== BATCH UPDATE OPTIMIZED ====================
    
    /**
     * Cập nhật batch kết quả xét tuyển
     * @param list Danh sách entity cần cập nhật
     * @param batchSize Kích thước batch
     */
    public void updateKetQuaBatch(List<NguyenVongXetTuyen> list, int batchSize) {
        if (list == null || list.isEmpty()) return;
        
        String sql = "UPDATE xt_nguyenvongxettuyen SET nv_ketqua = ?, nv_rank = ? WHERE idnv = ?";
        
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            
            session.doWork(connection -> {
                try (java.sql.PreparedStatement stmt = connection.prepareStatement(sql)) {
                    int count = 0;
                    for (NguyenVongXetTuyen nv : list) {
                        if (nv.getIdNv() == null) continue;
                        
                        stmt.setString(1, nv.getKetQua());
                        stmt.setInt(2, nv.getThuTuXetTuyen() != null ? nv.getThuTuXetTuyen() : 0);
                        stmt.setInt(3, nv.getIdNv());
                        stmt.addBatch();
                        
                        if (++count % batchSize == 0) {
                            stmt.executeBatch();
                        }
                    }
                    stmt.executeBatch();
                }
            });
            
            tx.commit();
            System.out.println("✓ Đã cập nhật batch " + list.size() + " bản ghi");
        } catch (Exception e) {
            throw new AppException("Lỗi khi cập nhật batch NguyenVongXetTuyen!", e);
        }
    }
    
    // ==================== BATCH DELETE OPTIMIZED ====================
    
    /**
     * Xóa batch theo ID
     * @param ids Danh sách ID cần xóa
     */
    public void deleteByIds(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) return;
        
        String sql = "DELETE FROM xt_nguyenvongxettuyen WHERE idnv IN (:ids)";
        
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            
            // Chia thành nhiều batch nhỏ để tránh query quá dài
            for (int i = 0; i < ids.size(); i += BATCH_SIZE) {
                int end = Math.min(i + BATCH_SIZE, ids.size());
                List<Integer> subIds = ids.subList(i, end);
                
                Query<?> query = session.createNativeQuery(sql);
                query.setParameter("ids", subIds);
                query.executeUpdate();
                
                session.flush();
                session.clear();
            }
            
            tx.commit();
            System.out.println("✓ Đã xóa batch " + ids.size() + " bản ghi");
        } catch (Exception e) {
            throw new AppException("Lỗi khi xóa batch NguyenVongXetTuyen!", e);
        }
    }
    
    /**
     * Xóa batch theo key (nv_keys)
     * @param keys Danh sách key cần xóa
     */
    public void deleteByKeys(List<String> keys) {
        if (keys == null || keys.isEmpty()) return;
        
        String sql = "DELETE FROM xt_nguyenvongxettuyen WHERE nv_keys IN (:keys)";
        
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            
            for (int i = 0; i < keys.size(); i += BATCH_SIZE) {
                int end = Math.min(i + BATCH_SIZE, keys.size());
                List<String> subKeys = keys.subList(i, end);
                
                Query<?> query = session.createNativeQuery(sql);
                query.setParameter("keys", subKeys);
                query.executeUpdate();
                
                session.flush();
                session.clear();
            }
            
            tx.commit();
            System.out.println("✓ Đã xóa batch " + keys.size() + " bản ghi theo key");
        } catch (Exception e) {
            throw new AppException("Lỗi khi xóa batch NguyenVongXetTuyen theo key!", e);
        }
    }
    
    // ==================== COUNT ====================
    
    /**
     * Đếm số lượng bản ghi
     */
    public long count() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("SELECT COUNT(*) FROM NguyenVongXetTuyen", Long.class).uniqueResult();
        } catch (Exception e) {
            throw new AppException("Lỗi khi đếm số lượng NguyenVongXetTuyen!", e);
        }
    }
    
    /**
     * Đếm số lượng bản ghi theo CCCD
     */
    public long countByCccd(String cccd) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT COUNT(*) FROM NguyenVongXetTuyen WHERE cccd = :cccd";
            return session.createQuery(hql, Long.class).setParameter("cccd", cccd).uniqueResult();
        } catch (Exception e) {
            throw new AppException("Lỗi khi đếm số lượng NguyenVongXetTuyen theo CCCD!", e);
        }
    }
}