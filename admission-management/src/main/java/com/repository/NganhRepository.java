package com.repository;

import com.config.SessionManager;
import com.entity.Nganh;
import org.hibernate.query.Query;

import java.util.List;

public class NganhRepository {

    public List<Nganh> findAll() {
        SessionManager sessionManager = new SessionManager();
        try {
            sessionManager.begin();
            Query<Nganh> query = sessionManager.getSession().createQuery("FROM Nganh", Nganh.class);
            List<Nganh> ketQua = query.list();
            sessionManager.commit();
            return ketQua;
        } catch (Exception exception) {
            sessionManager.rollback();
            throw exception;
        } finally {
            sessionManager.close();
        }
    }

    public void save(Nganh nganh) {
        SessionManager sessionManager = new SessionManager();
        try {
            sessionManager.begin();
            sessionManager.getSession().save(nganh);
            sessionManager.commit();
        } catch (Exception exception) {
            sessionManager.rollback();
            throw exception;
        } finally {
            sessionManager.close();
        }
    }

    public void update(Nganh nganh) {
        SessionManager sessionManager = new SessionManager();
        try {
            sessionManager.begin();
            sessionManager.getSession().update(nganh);
            sessionManager.commit();
        } catch (Exception exception) {
            sessionManager.rollback();
            throw exception;
        } finally {
            sessionManager.close();
        }
    }

    public void delete(String maNganh) {
        SessionManager sessionManager = new SessionManager();
        try {
            sessionManager.begin();
            Nganh nganhCachXoa = sessionManager.getSession().get(Nganh.class, maNganh);
            if (nganhCachXoa != null) {
                sessionManager.getSession().delete(nganhCachXoa);
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