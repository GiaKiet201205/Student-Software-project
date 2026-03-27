package com.repository;

import com.config.HibernateUtil;
import com.entity.NguoiDung;
import com.entity.ThiSinhXetTuyen25;
import com.exception.AppException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class NguoiDungRepository extends BaseRepository<NguoiDung> {
    public NguoiDungRepository() {
        super(NguoiDung.class);
    }

    // Phân trang
    public List<NguoiDung> findAllByPage(int page, int pageSize){
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String sql = "FROM NguoiDung" ;
            int offset = (page - 1) * pageSize;
            return session.createQuery(sql, NguoiDung.class)
                    .setFirstResult(offset)
                    .setMaxResults(pageSize)
                    .getResultList();
        } catch (Exception e) {
            throw new AppException("Không thể tải danh sách NguoiDung từ cơ sở dữ liệu!", e);
        }
    }

    public long countAll(){
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String sql = "SELECT COUNT(*) FROM NguoiDung" ;
            return session.createQuery(sql, Long.class).getSingleResult();
        } catch (Exception e) {
            throw new AppException("Không thể đếm số lượng NguoiDung trong cơ sở dữ liệu!", e);
        }
    }

    public boolean existsByUsernameExceptId(String username, Integer id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT COUNT(u) FROM NguoiDung u WHERE u.username = :username AND u.id != :id";

            Long count = session.createQuery(hql, Long.class)
                    .setParameter("username", username)
                    .setParameter("id", id)
                    .getSingleResult();

            return count != null && count > 0;
        } catch (Exception e) {
            throw new AppException("Lỗi kiểm tra UserName đã tồn tại!", e);
        }
    }

    // Enable/Disable
    public void toggleActive(Integer id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            String sql = "UPDATE NguoiDung u SET u.active = 1 - u.active WHERE u.id = :id";
            int updated = session.createQuery(sql)
                    .setParameter("id", id)
                    .executeUpdate();

            if (updated == 0) {
                throw new AppException("Không tìm thấy NguoiDung với ID: " + id);
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new AppException("Không thể cập nhật trạng thái NguoiDung!", e);
        }
    }


}
