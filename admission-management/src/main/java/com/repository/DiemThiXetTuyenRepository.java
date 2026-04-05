package com.repository;

import com.config.HibernateUtil;
import com.entity.DiemThiXetTuyen;
import com.exception.AppException;
import org.hibernate.Session;

import java.util.List;

public class DiemThiXetTuyenRepository extends BaseRepository<DiemThiXetTuyen> {

    public DiemThiXetTuyenRepository() {
        super(DiemThiXetTuyen.class);
    }

    // Override lại findAll để giới hạn 500 dòng tránh lag khi db có nhiều điểm
    @Override
    public List<DiemThiXetTuyen> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM DiemThiXetTuyen";
            return session.createQuery(hql, DiemThiXetTuyen.class)
                    .setMaxResults(500)
                    .getResultList();
        } catch (Exception e) {
            throw new AppException("Không thể tải danh sách Điểm thí sinh từ cơ sở dữ liệu!", e);
        }
    }

    public List<DiemThiXetTuyen> searchByKeyword(String keyword) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM DiemThiXetTuyen WHERE cccd LIKE :kw OR soBaoDanh LIKE :kw";
            return session.createQuery(hql, DiemThiXetTuyen.class)
                    .setParameter("kw", "%" + keyword + "%")
                    .getResultList();
        } catch (Exception e) {
            throw new AppException("Lỗi khi tìm kiếm Điểm thí sinh theo từ khóa: " + keyword, e);
        }
    }
}