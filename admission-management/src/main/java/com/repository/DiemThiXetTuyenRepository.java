package com.repository;

import com.config.HibernateUtil;
import com.entity.DiemThiXetTuyen;
import com.exception.AppException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class DiemThiXetTuyenRepository extends BaseRepository<DiemThiXetTuyen> {

    public DiemThiXetTuyenRepository() {
        super(DiemThiXetTuyen.class);
    }

    /**
     * Lấy toàn bộ hồ sơ điểm (THPT, V-SAT, ĐGNL) của 1 thí sinh.
     * Dùng để hiển thị lên bảng chi tiết trên giao diện (UI).
     */
    public List<DiemThiXetTuyen> findByCccd(String cccd) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<DiemThiXetTuyen> query = session.createQuery(
                    "FROM DiemThiXetTuyen WHERE cccd = :cccd", DiemThiXetTuyen.class);
            query.setParameter("cccd", cccd);
            return query.list();
        } catch (Exception e) {
            throw new AppException("Lỗi khi tải danh sách điểm của CCCD: " + cccd, e);
        }
    }

    /**
     * Tìm chính xác 1 dòng hồ sơ theo Khóa tổ hợp (CCCD + Phương thức).
     * RẤT QUAN TRỌNG: Dùng trong logic kiểm tra xem thí sinh đã có điểm phương thức này chưa.
     */
    public DiemThiXetTuyen findByCccdAndPhuongThuc(String cccd, String phuongThuc) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<DiemThiXetTuyen> query = session.createQuery(
                    "FROM DiemThiXetTuyen WHERE cccd = :cccd AND phuongThuc = :phuongThuc",
                    DiemThiXetTuyen.class);
            query.setParameter("cccd", cccd);
            query.setParameter("phuongThuc", phuongThuc);

            // Dùng uniqueResult() vì Khóa của chúng ta là UNIQUE, chắc chắn chỉ ra tối đa 1 dòng
            return query.uniqueResult();
        } catch (Exception e) {
            throw new AppException("Lỗi khi tìm điểm theo CCCD và Phương thức!", e);
        }
    }

    // =========================================================================
    // HÀM XỬ LÝ ĐƠN LẺ TỐI ƯU CHO HIBERNATE 6
    // =========================================================================

    /**
     * Lưu HOẶC Cập nhật đè 1 bản ghi duy nhất.
     * Dùng khi admin nhập tay hoặc chỉnh sửa điểm của 1 thí sinh trực tiếp trên màn hình UI.
     * (Khác với saveAll() của lớp cha dùng để nạp file Excel).
     */
    public DiemThiXetTuyen saveOrUpdate(DiemThiXetTuyen entity) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            // Dùng merge() thay cho save/update đã cũ.
            // JPA sẽ tự biết đối tượng này là Insert mới hay Update đè.
            DiemThiXetTuyen result = session.merge(entity);

            transaction.commit();
            return result;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new AppException("Lỗi khi lưu thông tin điểm thi!", e);
        }
    }

}