package com.config;

import com.controller.AppController;
import org.hibernate.SessionFactory;

public class AppConfig {
  public static AppController appController() {
    SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    // Todo: Init Repository, service

    return new AppController();
  }

}
