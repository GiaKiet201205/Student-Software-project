package com.repository;

import com.config.HibernateUtil;
import com.entity.BangQuyDoi;
import org.hibernate.Session;

import java.util.List;

public class BangQuyDoiRepository {
  public List<BangQuyDoi> findAll() {
    Session session = HibernateUtil.getSessionFactory().openSession();

    List<BangQuyDoi> list =
        session.createQuery("FROM BangQuyDoi", BangQuyDoi.class)
            .getResultList();

    session.close();

    return list;
  }
}
