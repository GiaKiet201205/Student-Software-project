package com.repository;

import com.config.HibernateUtil;
import com.entity.ThiSinhXetTuyen25;
import com.exception.AppException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class ThiSinhXetTuyen25Repository extends BaseRepository<ThiSinhXetTuyen25> {
  public ThiSinhXetTuyen25Repository() {
    super(ThiSinhXetTuyen25.class);
  }

  // Phân trang
  public List<ThiSinhXetTuyen25> findAllByPage(int page, int pageSize){
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
      String sql = "FROM ThiSinhXetTuyen25" ;
      int offset = (page - 1) * pageSize;
      return session.createQuery(sql, ThiSinhXetTuyen25.class)
              .setFirstResult(offset)
              .setMaxResults(pageSize)
              .getResultList();
    } catch (Exception e) {
      throw new AppException("Không thể tải danh sách ThiSinhXetTuyen25 từ cơ sở dữ liệu!", e);
    }
  }

  public long countAll(){
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
      String sql = "SELECT COUNT(*) FROM ThiSinhXetTuyen25" ;
      return session.createQuery(sql, Long.class).getSingleResult();
    } catch (Exception e) {
      throw new AppException("Không thể đếm số lượng ThiSinhXetTuyen25 trong cơ sở dữ liệu!", e);
    }
  }

  public int getTotalPages(int pageSize) {
    long totalRecords = countAll();
    return (int) Math.ceil((double) totalRecords / pageSize);
  }

  //Tìm kiếm
  public List<ThiSinhXetTuyen25> searchByCccdOrNameByPage(String keyword, int page, int pageSize) {
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
      String sql = "FROM ThiSinhXetTuyen25 WHERE cccd LIKE :keyword OR ho LIKE :keyword OR ten LIKE :keyword";
      int offset = (page - 1) * pageSize;
      return session.createQuery(sql, ThiSinhXetTuyen25.class)
              .setParameter("keyword", "%" + keyword + "%")
              .setFirstResult(offset)
              .setMaxResults(pageSize)
              .getResultList();
    } catch (Exception e) {
      throw new AppException("Lỗi search + pagination!", e);
    }
  }

  public long countSearch(String keyword) {
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
      String sql = "SELECT COUNT(*) FROM ThiSinhXetTuyen25 WHERE cccd LIKE :keyword OR ho LIKE :keyword OR ten LIKE :keyword";
      return session.createQuery(sql, Long.class)
              .setParameter("keyword", "%" + keyword + "%")
              .getSingleResult();
    } catch (Exception e) {
      throw new AppException("Lỗi khi đếm kết quả tìm kiếm!", e);
    }
  }

  public boolean existsByEmailExceptId(String email, Integer id) {
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
      String hql = "SELECT COUNT(u) FROM ThiSinhXetTuyen25 u WHERE u.email = :email AND u.id != :id";

      Long count = session.createQuery(hql, Long.class)
              .setParameter("email", email)
              .setParameter("id", id)
              .getSingleResult();

      return count != null && count > 0;
    } catch (Exception e) {
      throw new AppException("Lỗi kiểm tra email đã tồn tại!", e);
    }
  }

  public boolean existsByCccdExceptId(String cccd, Integer id) {
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
      String hql = "SELECT COUNT(u) FROM ThiSinhXetTuyen25 u WHERE u.cccd = :cccd AND u.id != :id";

      Long count = session.createQuery(hql, Long.class)
              .setParameter("cccd", cccd)
              .setParameter("id", id)
              .getSingleResult();

      return count != null && count > 0;
    } catch (Exception e) {
      throw new AppException("Lỗi kiểm tra CCCD đã tồn tại!", e);
    }
  }

  // Save all cho list (dùng cho import Excel)
  public void saveAll(List<ThiSinhXetTuyen25> list) {
    Transaction tx = null;

    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
      tx = session.beginTransaction();

      for (int i = 0; i < list.size(); i++) {
        session.save(list.get(i));

        // batch mỗi 50 record
        if (i % 50 == 0) {
          session.flush();
          session.clear();
        }
      }

      tx.commit();

    } catch (Exception e) {
      if (tx != null) tx.rollback();
      throw new AppException("Lỗi lưu dữ liệu hàng loạt!", e);
    }
  }
}
