package com.repository;

import com.config.SessionManager;
import com.entity.ToHopMonThi;
import org.hibernate.query.Query;

import java.util.List;

public class ToHopMonThiRepository {

    public List<ToHopMonThi> findAll() {
        SessionManager sessionManager = new SessionManager();
        try {
            sessionManager.begin();
            Query<ToHopMonThi> query = sessionManager.getSession().createQuery("FROM ToHopMonThi", ToHopMonThi.class);
            List<ToHopMonThi> ketQua = query.list();
            sessionManager.commit();
            return ketQua;
        } catch (Exception exception) {
            sessionManager.rollback();
            throw exception;
        } finally {
            sessionManager.close();
        }
    }

    public void save(ToHopMonThi toHopMonThi) {
        SessionManager sessionManager = new SessionManager();
        try {
            sessionManager.begin();
            sessionManager.getSession().save(toHopMonThi);
            sessionManager.commit();
        } catch (Exception exception) {
            sessionManager.rollback();
            throw exception;
        } finally {
            sessionManager.close();
        }
    }

    public void update(ToHopMonThi toHopMonThi) {
        SessionManager sessionManager = new SessionManager();
        try {
            sessionManager.begin();
            sessionManager.getSession().update(toHopMonThi);
            sessionManager.commit();
        } catch (Exception exception) {
            sessionManager.rollback();
            throw exception;
        } finally {
            sessionManager.close();
        }
    }

    public void delete(String maToHop) {
        SessionManager sessionManager = new SessionManager();
        try {
            sessionManager.begin();
            ToHopMonThi toHopCanXoa = sessionManager.getSession().get(ToHopMonThi.class, maToHop);
            if (toHopCanXoa != null) {
                sessionManager.getSession().delete(toHopCanXoa);
            }
            sessionManager.commit();
        } catch (Exception exception) {
            sessionManager.rollback();
            throw exception;
        } finally {
            sessionManager.close();
        }
    }
}