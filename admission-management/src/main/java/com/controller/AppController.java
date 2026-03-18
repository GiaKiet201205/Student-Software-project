package com.controller;

import com.service.BangQuyDoiService;
import com.entity.BangQuyDoi;

import java.util.List;

public class AppController {

  private final BangQuyDoiService service = new BangQuyDoiService();

  public List<BangQuyDoi> getBangQuyDoi() {
    return service.getAll();
  }
}