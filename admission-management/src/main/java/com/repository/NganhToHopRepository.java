package com.repository;

import com.config.HibernateUtil;
import com.entity.NganhToHop;
import com.exception.AppException;
import org.hibernate.Session;

import java.util.List;

public class NganhToHopRepository extends BaseRepository<NganhToHop> {

    public NganhToHopRepository() {
        super(NganhToHop.class);
    }

    public List<NganhToHop> searchByKeyword(String keyword) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM NganhToHop WHERE maNganh LIKE :kw OR maToHop LIKE :kw OR tbKeys LIKE :kw";
            return session.createQuery(hql, NganhToHop.class)
                    .setParameter("kw", "%" + keyword + "%")
                    .getResultList();
        } catch (Exception e) {
            throw new AppException("Lỗi khi tìm kiếm Ngành - Tổ hợp theo từ khóa: " + keyword, e);
        }
    }
}