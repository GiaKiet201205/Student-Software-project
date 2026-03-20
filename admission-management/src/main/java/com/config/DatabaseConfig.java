package com.config;

import org.hibernate.cfg.Configuration;

public class DatabaseConfig {

  public static Configuration getConfiguration() {
    Configuration configuration = new Configuration();

    configuration.configure("hibernate.cfg.xml");

    // TODO: Add entity
    // configuration.addAnnotatedClass(com.entity.ThiSinh.class);

    return configuration;
  }
}