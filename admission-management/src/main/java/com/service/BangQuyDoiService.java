package com.service;

import com.repository.BangQuyDoiRepository;
import com.entity.BangQuyDoi;

import java.util.List;

public class BangQuyDoiService {

  private final BangQuyDoiRepository repo = new BangQuyDoiRepository();

  public List<BangQuyDoi> getAll() {
    return repo.findAll();
  }
}
