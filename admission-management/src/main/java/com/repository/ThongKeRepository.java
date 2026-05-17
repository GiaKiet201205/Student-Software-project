package com.repository;

import com.config.HibernateUtil;
import com.dto.ThongKeNVDTO;
import com.entity.ThiSinhXetTuyen25;
import com.exception.AppException;
import org.hibernate.Session;

import java.util.List;
import java.util.stream.Collectors;

public class ThongKeRepository extends BaseRepository<ThiSinhXetTuyen25> {

    public ThongKeRepository() {
        super(ThiSinhXetTuyen25.class);
    }

    public List<Object[]> thongKeTheoKhuVuc() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT t.khuVuc, COUNT(t.idThiSinh) FROM ThiSinhXetTuyen25 t GROUP BY t.khuVuc";
            return session.createQuery(hql, Object[].class).getResultList();
        } catch (Exception e) {
            throw new AppException("Lỗi khi thống kê thí sinh theo khu vực!", e);
        }
    }

    public List<Object[]> thongKeTheoDoiTuong() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT t.doiTuong, COUNT(t.idThiSinh) FROM ThiSinhXetTuyen25 t GROUP BY t.doiTuong";
            return session.createQuery(hql, Object[].class).getResultList();
        } catch (Exception e) {
            throw new AppException("Lỗi khi thống kê thí sinh theo đối tượng!", e);
        }
    }

    public List<Object[]> thongKeNguyenVongTheoNganh() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = """
                SELECT
                    n.maNganh,
                    n.tenNganh,
                    n.chiTieu,
                    SUM(CASE WHEN nv.thuTu BETWEEN 1 AND 5 THEN 1 ELSE 0 END),
                    SUM(CASE WHEN nv.thuTu = 1 THEN 1 ELSE 0 END),
                    SUM(CASE WHEN nv.thuTu = 2 THEN 1 ELSE 0 END),
                    SUM(CASE WHEN nv.thuTu = 3 THEN 1 ELSE 0 END),
                    SUM(CASE WHEN nv.thuTu = 4 THEN 1 ELSE 0 END),
                    SUM(CASE WHEN nv.thuTu = 5 THEN 1 ELSE 0 END),
                    SUM(CASE WHEN nv.thuTu > 5  THEN 1 ELSE 0 END),
                    COUNT(nv.idNv)
                FROM Nganh n
                LEFT JOIN NguyenVongXetTuyen nv ON nv.maNganh = n.maNganh
                GROUP BY n.maNganh, n.tenNganh, n.chiTieu
                ORDER BY n.maNganh ASC
                """;
            return session.createQuery(hql, Object[].class).getResultList();
        } catch (Exception e) {
            throw new AppException("Lỗi khi thống kê nguyện vọng theo ngành!", e);
        }
    }

    public List<Object[]> thongKeSoLuongTrungTuyenTheoPTNganh() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = """
                SELECT
                    n.maNganh,
                    n.tenNganh,
                    SUM(CASE WHEN nv.phuongThuc = '3' AND UPPER(nv.ketQua) = 'YES' THEN 1 ELSE 0 END),
                    SUM(CASE WHEN nv.phuongThuc = '4' AND UPPER(nv.ketQua) = 'YES' THEN 1 ELSE 0 END),
                    SUM(CASE WHEN nv.phuongThuc = '5' AND UPPER(nv.ketQua) = 'YES' THEN 1 ELSE 0 END)
                FROM Nganh n
                LEFT JOIN NguyenVongXetTuyen nv ON nv.maNganh = n.maNganh
                GROUP BY n.maNganh, n.tenNganh
                ORDER BY n.maNganh ASC
                """;
            return session.createQuery(hql, Object[].class).getResultList();
        } catch (Exception e) {
            throw new AppException("Lỗi khi thống kê số lượng trúng tuyển theo phương thức/ngành!", e);
        }
    }

}
