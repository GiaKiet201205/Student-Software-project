package com.service.mapper;

public interface RowMapper<T> {
    T map(SimpleRow row);
}