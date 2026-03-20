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
      session.update(entity);
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
}
