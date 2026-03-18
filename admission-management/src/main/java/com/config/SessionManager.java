package com.config;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class SessionManager {

  private static final SessionFactory sessionFactory =
      HibernateUtil.getSessionFactory();

  public static Session openSession() {
    return sessionFactory.openSession();
  }
}