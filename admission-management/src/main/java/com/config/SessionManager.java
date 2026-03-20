package com.config;

import org.hibernate.Session;
import org.hibernate.Transaction;

public class SessionManager {
  private Session session;
  private Transaction transaction;

  public void begin() {
    session = HibernateUtil.getSessionFactory().openSession();
    transaction = session.beginTransaction();
  }

  public Session getSession() {
    if (session == null || !session.isOpen()) {
      begin();
    }
    return session;
  }

  public void commit() {
    try {
      if (transaction != null && transaction.isActive()) {
        transaction.commit();
      }
    } catch (Exception e) {
      rollback();
      throw e;
    }
  }

  public void rollback() {
    if (transaction != null && transaction.isActive()) {
      transaction.rollback();
    }
  }

  public void close() {
    if (session != null && session.isOpen()) {
      session.close();
    }
  }
}