package com.config;

import com.exception.AppException;
import org.hibernate.SessionFactory;

public class HibernateUtil {
  private static final SessionFactory sessionFactory = buildSessionFactory();

  private static SessionFactory buildSessionFactory() {
    try {
      return DatabaseConfig.getConfiguration().buildSessionFactory();
    } catch (Throwable ex) {
      System.err.println("Lỗi khởi tạo SessionFactory: " + ex);
      throw new AppException("Lỗi khởi tạo SessionFactory", ex);
    }
  }

  public static SessionFactory getSessionFactory() {
    return sessionFactory;
  }

  public static void shutdown() {
    if (sessionFactory != null && !sessionFactory.isClosed()) {
      sessionFactory.close();
    }
  }
}