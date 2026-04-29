package com.repository;

import com.config.HibernateUtil;
import com.exception.AppException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.Serializable;
import java.util.List;

public class BaseRepository<T> {
  private final Class<T> entityClass;

  public BaseRepository(Class<T> entityClass) {
    this.entityClass = entityClass;
  }

  public List<T> findAll() {
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
      String sql = "FROM " + entityClass.getName();
      return session.createQuery(sql, entityClass).getResultList();
    } catch (Exception e) {
      throw new AppException("Không thể tải danh sách " + entityClass.getSimpleName() + " từ cơ sở dữ liệu!", e);
    }
  }

  public T findById(Serializable id) {
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
      return session.get(entityClass, id);
    } catch (Exception e) {
      throw new AppException("Lỗi khi tìm kiếm " + entityClass.getSimpleName() + " theo ID: " + id, e);
    }
  }

  public T save(T entity) {
    Transaction transaction = null;
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
      transaction = session.beginTransaction();
      session.save(entity);
      transaction.commit();
      return entity;
    } catch (Exception e) {
      if (transaction != null) transaction.rollback();
      throw new AppException("Lỗi khi thêm mới " + entityClass.getSimpleName() + "!", e);
    }
  }

  public T update(T entity) {
    Transaction transaction = null;
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
      transaction = session.beginTransaction();
      session.merge(entity);
      transaction.commit();
      return entity;
    } catch (Exception e) {
      if (transaction != null) transaction.rollback();
      throw new AppException("Lỗi khi cập nhật thông tin " + entityClass.getSimpleName() + "!", e);
    }
  }

  public void delete(T entity) {
    Transaction transaction = null;
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
      transaction = session.beginTransaction();
      session.delete(entity);
      transaction.commit();
    } catch (Exception e) {
      if (transaction != null) transaction.rollback();
      throw new AppException("Lỗi khi xóa dữ liệu " + entityClass.getSimpleName() + "!", e);
    }
  }
  //Dùng để import những file dữ liệu lớn ( đã tích hợp thêm và cập nhật)
  public void saveAll(List<T> entities) {
    if (entities == null || entities.isEmpty()) return;

    Transaction transaction = null;
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
      transaction = session.beginTransaction();

      // thông số hibernate.jdbc.batch_size trong file cấu hình
      int batchSize = 1000;

      for (int i = 0; i < entities.size(); i++) {
        // Sử dụng merge() để tự động: Insert (nếu mới) hoặc Update đè (nếu đã tồn tại)
        session.merge(entities.get(i));

        // Cứ tích đủ 1000 dòng thì "xả" xuống Database và dọn rác RAM
        if (i > 0 && i % batchSize == 0) {
          session.flush(); // Đẩy lệnh SQL xuống MySQL
          session.clear(); // Xóa bộ nhớ tạm của Hibernate
        }
      }

      // Xả nốt những bản ghi lẻ cuối cùng (Ví dụ: dòng 1001 đến 1050)
      session.flush();
      session.clear();

      transaction.commit();
    } catch (Exception e) {
      if (transaction != null) {
        transaction.rollback();
      }
      throw new AppException("Lỗi khi Import danh sách " + entityClass.getSimpleName() + " số lượng lớn!", e);
    }
  }
}
