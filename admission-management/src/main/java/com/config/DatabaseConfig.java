package com.config;

import org.hibernate.cfg.Configuration;

public class DatabaseConfig {

  public static Configuration getConfiguration() {
    Configuration configuration = new Configuration();

    configuration.configure("hibernate.cfg.xml");

    configuration.addAnnotatedClass(com.entity.BangQuyDoi.class);
    configuration.addAnnotatedClass(com.entity.DiemCongXetTuyen.class);
    configuration.addAnnotatedClass(com.entity.DiemThiXetTuyen.class);
    configuration.addAnnotatedClass(com.entity.Nganh.class);
    configuration.addAnnotatedClass(com.entity.NganhToHop.class);
    configuration.addAnnotatedClass(com.entity.ThiSinhXetTuyen25.class);
    configuration.addAnnotatedClass(com.entity.ToHopMonThi.class);
    configuration.addAnnotatedClass(com.entity.NguyenVongXetTuyen.class);
    configuration.addAnnotatedClass(com.entity.NguoiDung.class);

    return configuration;
  }
}