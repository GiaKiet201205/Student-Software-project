package com.repository;

import com.config.HibernateUtil;
import com.dto.ThiSinhTrungTuyenDTO;
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

    public List<Object[]> thongKeSLTrungTuyenPTNganh() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = """
                    SELECT
                        nv.maNganh,
                        n.tenNganh,
                        nv.phuongThuc,
                        COUNT(nv.idNv)
                    FROM NguyenVongXetTuyen nv
                    LEFT JOIN Nganh n ON nv.maNganh = n.maNganh
                    WHERE nv.ketQua = 'YES'
                    GROUP BY nv.maNganh, n.tenNganh, nv.phuongThuc
                    ORDER BY nv.maNganh ASC, nv.phuongThuc ASC
                    """;
            return session.createQuery(hql, Object[].class).getResultList();
        } catch (Exception e) {
            throw new AppException("Lỗi khi thống kê số lượng trúng tuyển theo phương thức và ngành", e);
        }
    }

    public List<ThiSinhTrungTuyenDTO> getThiSinhTrungTuyenTheoNganh(String maNganh) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = """
                    SELECT
                        ts.cccd,
                        ts.ho,
                        ts.ten,
                        ts.ngaySinh,
                        ts.email,
                        ts.dienThoai,
                        ts.gioiTinh,
                        ts.noiSinh
                    FROM NguyenVongXetTuyen nv
                    JOIN ThiSinhXetTuyen25 ts ON nv.cccd = ts.cccd
                    WHERE nv.maNganh = :maNganh
                      AND nv.ketQua = 'YES'
                    GROUP BY ts.cccd, ts.ho, ts.ten, ts.ngaySinh, ts.email, ts.dienThoai, ts.gioiTinh, ts.noiSinh
                    ORDER BY ts.ho ASC, ts.ten ASC
                    """;
            List<Object[]> rows = session.createQuery(hql, Object[].class)
                    .setParameter("maNganh", maNganh)
                    .getResultList();
            return rows.stream()
                    .map(ThiSinhTrungTuyenDTO::new)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("DEBUG: Exception in getThiSinhTrungTuyenTheoNganh: " + e.getMessage());
            e.printStackTrace();
            throw new AppException("Lỗi khi lấy danh sách thí sinh trúng tuyển theo ngành", e);
        }
    }

}
