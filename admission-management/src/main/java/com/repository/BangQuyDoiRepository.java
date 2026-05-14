package com.repository;

import com.config.SessionManager;
import com.entity.BangQuyDoi;
import com.exception.AppException;
import org.hibernate.query.Query;

import java.util.List;

public class BangQuyDoiRepository {

    // ===================== READ =====================

    public List<BangQuyDoi> findAll() {
        SessionManager sessionManager = new SessionManager();
        try {
            sessionManager.begin();
            Query<BangQuyDoi> query = sessionManager.getSession().createQuery(
                    "FROM BangQuyDoi ORDER BY phuongthuc, tohop, mon, phanVi",
                    BangQuyDoi.class);
            List<BangQuyDoi> result = query.list();
            sessionManager.commit();
            return result;
        } catch (Exception e) {
            sessionManager.rollback();
            throw new AppException("Lỗi khi tải danh sách bảng quy đổi!", e);
        } finally {
            sessionManager.close();
        }
    }

    public BangQuyDoi findById(int id) {
        SessionManager sessionManager = new SessionManager();
        try {
            sessionManager.begin();
            BangQuyDoi result = sessionManager.getSession().get(BangQuyDoi.class, id);
            sessionManager.commit();
            return result;
        } catch (Exception e) {
            sessionManager.rollback();
            throw new AppException("Lỗi khi tìm bảng quy đổi theo ID: " + id, e);
        } finally {
            sessionManager.close();
        }
    }

    public List<BangQuyDoi> search(String keyword) {
        SessionManager sessionManager = new SessionManager();
        try {
            sessionManager.begin();
            String hql = "FROM BangQuyDoi b WHERE " +
                    "LOWER(b.phuongthuc) LIKE :kw OR " +
                    "LOWER(b.tohop)      LIKE :kw OR " +
                    "LOWER(b.mon)        LIKE :kw OR " +
                    "LOWER(b.maQuydoi)   LIKE :kw OR " +
                    "LOWER(b.phanVi)     LIKE :kw " +
                    "ORDER BY b.phuongthuc, b.tohop, b.mon";
            Query<BangQuyDoi> query = sessionManager.getSession().createQuery(hql, BangQuyDoi.class);
            query.setParameter("kw", "%" + keyword.toLowerCase() + "%");
            List<BangQuyDoi> result = query.list();
            sessionManager.commit();
            return result;
        } catch (Exception e) {
            sessionManager.rollback();
            throw new AppException("Lỗi khi tìm kiếm bảng quy đổi!", e);
        } finally {
            sessionManager.close();
        }
    }

    public List<BangQuyDoi> findByPhuongThuc(String phuongthuc) {
        SessionManager sessionManager = new SessionManager();
        try {
            sessionManager.begin();
            Query<BangQuyDoi> query = sessionManager.getSession().createQuery(
                    "FROM BangQuyDoi b WHERE b.phuongthuc = :pt ORDER BY b.tohop, b.mon, b.phanVi",
                    BangQuyDoi.class);
            query.setParameter("pt", phuongthuc);
            List<BangQuyDoi> result = query.list();
            sessionManager.commit();
            return result;
        } catch (Exception e) {
            sessionManager.rollback();
            throw new AppException("Lỗi khi tìm bảng quy đổi theo phương thức!", e);
        } finally {
            sessionManager.close();
        }
    }

    public List<BangQuyDoi> findByPhuongThucAndToHop(String phuongthuc, String tohop) {
        SessionManager sessionManager = new SessionManager();
        try {
            sessionManager.begin();
            Query<BangQuyDoi> query = sessionManager.getSession().createQuery(
                    "FROM BangQuyDoi b WHERE b.phuongthuc = :pt AND b.tohop = :th ORDER BY b.phanVi",
                    BangQuyDoi.class);
            query.setParameter("pt", phuongthuc);
            query.setParameter("th", tohop);
            List<BangQuyDoi> result = query.list();
            sessionManager.commit();
            return result;
        } catch (Exception e) {
            sessionManager.rollback();
            throw new AppException("Lỗi khi tìm bảng quy đổi theo phương thức và tổ hợp!", e);
        } finally {
            sessionManager.close();
        }
    }

    public List<BangQuyDoi> findByPhuongThucAndMon(String phuongthuc, String mon) {
        SessionManager sessionManager = new SessionManager();
        try {
            sessionManager.begin();
            Query<BangQuyDoi> query = sessionManager.getSession().createQuery(
                    "FROM BangQuyDoi b WHERE b.phuongthuc = :pt AND b.mon = :mon ORDER BY b.phanVi",
                    BangQuyDoi.class);
            query.setParameter("pt", phuongthuc);
            query.setParameter("mon", mon);
            List<BangQuyDoi> result = query.list();
            sessionManager.commit();
            return result;
        } catch (Exception e) {
            sessionManager.rollback();
            throw new AppException("Lỗi khi tìm bảng quy đổi theo phương thức và môn!", e);
        } finally {
            sessionManager.close();
        }
    }

    public BangQuyDoi findByMaQuydoi(String maQuydoi) {
        SessionManager sessionManager = new SessionManager();
        try {
            sessionManager.begin();
            Query<BangQuyDoi> query = sessionManager.getSession().createQuery(
                    "FROM BangQuyDoi b WHERE b.maQuydoi = :maqd",
                    BangQuyDoi.class);
            query.setParameter("maqd", maQuydoi);
            BangQuyDoi result = query.uniqueResult();
            sessionManager.commit();
            return result;
        } catch (Exception e) {
            sessionManager.rollback();
            throw new AppException("Lỗi khi tìm bảng quy đổi theo mã: " + maQuydoi, e);
        } finally {
            sessionManager.close();
        }
    }

    public List<String> findDistinctPhuongThuc() {
        SessionManager sessionManager = new SessionManager();
        try {
            sessionManager.begin();
            Query<String> query = sessionManager.getSession().createQuery(
                    "SELECT DISTINCT b.phuongthuc FROM BangQuyDoi b WHERE b.phuongthuc IS NOT NULL ORDER BY b.phuongthuc",
                    String.class);
            List<String> result = query.list();
            sessionManager.commit();
            return result;
        } catch (Exception e) {
            sessionManager.rollback();
            throw new AppException("Lỗi khi lấy danh sách phương thức!", e);
        } finally {
            sessionManager.close();
        }
    }

    /**
     * excludeId = null khi thêm mới, = idqd hiện tại khi sửa
     */
    public boolean existsByMaQuydoi(String maQuydoi, Integer excludeId) {
        SessionManager sessionManager = new SessionManager();
        try {
            sessionManager.begin();
            String hql = excludeId != null
                    ? "SELECT COUNT(b) FROM BangQuyDoi b WHERE b.maQuydoi = :maqd AND b.idqd <> :id"
                    : "SELECT COUNT(b) FROM BangQuyDoi b WHERE b.maQuydoi = :maqd";
            Query<Long> query = sessionManager.getSession().createQuery(hql, Long.class);
            query.setParameter("maqd", maQuydoi);
            if (excludeId != null) query.setParameter("id", excludeId);
            boolean result = query.uniqueResult() > 0;
            sessionManager.commit();
            return result;
        } catch (Exception e) {
            sessionManager.rollback();
            throw new AppException("Lỗi khi kiểm tra mã quy đổi!", e);
        } finally {
            sessionManager.close();
        }
    }

    // ===================== CREATE =====================

    public void save(BangQuyDoi bangQuyDoi) {
        SessionManager sessionManager = new SessionManager();
        try {
            sessionManager.begin();
            sessionManager.getSession().save(bangQuyDoi);
            sessionManager.commit();
        } catch (Exception e) {
            sessionManager.rollback();
            throw new AppException("Lỗi khi thêm bản ghi quy đổi!", e);
        } finally {
            sessionManager.close();
        }
    }

    // ===================== UPDATE =====================

    public void update(BangQuyDoi bangQuyDoi) {
        SessionManager sessionManager = new SessionManager();
        try {
            sessionManager.begin();
            sessionManager.getSession().update(bangQuyDoi);
            sessionManager.commit();
        } catch (Exception e) {
            sessionManager.rollback();
            throw new AppException("Lỗi khi cập nhật bản ghi quy đổi!", e);
        } finally {
            sessionManager.close();
        }
    }

    // ===================== DELETE =====================

    public boolean deleteById(int id) {
        SessionManager sessionManager = new SessionManager();
        try {
            sessionManager.begin();
            BangQuyDoi entity = sessionManager.getSession().get(BangQuyDoi.class, id);
            if (entity != null) {
                sessionManager.getSession().delete(entity);
                sessionManager.commit();
                return true;
            }
            sessionManager.commit();
            return false;
        } catch (Exception e) {
            sessionManager.rollback();
            throw new AppException("Lỗi khi xóa bản ghi quy đổi ID: " + id, e);
        } finally {
            sessionManager.close();
        }
    }

    // ===================== IMPORT BATCH =====================

    public void saveAll(List<BangQuyDoi> list) {
        if (list == null || list.isEmpty()) return;
        SessionManager sessionManager = new SessionManager();
        try {
            sessionManager.begin();
            int batchSize = 500;
            for (int i = 0; i < list.size(); i++) {
                sessionManager.getSession().merge(list.get(i));
                if (i > 0 && i % batchSize == 0) {
                    sessionManager.getSession().flush();
                    sessionManager.getSession().clear();
                }
            }
            sessionManager.getSession().flush();
            sessionManager.getSession().clear();
            sessionManager.commit();
        } catch (Exception e) {
            sessionManager.rollback();
            throw new AppException("Lỗi khi import hàng loạt bảng quy đổi!", e);
        } finally {
            sessionManager.close();
        }
    }
}