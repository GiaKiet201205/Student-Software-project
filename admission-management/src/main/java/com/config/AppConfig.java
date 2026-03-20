package com.config;

import java.awt.*;

public class AppConfig {

  public static final String APP_NAME = "Hệ thống Quản lý Tuyển sinh";
  public static final String VERSION = "1.0.0";

  public static final int MAIN_WINDOW_WIDTH = 1500;
  public static final int MAIN_WINDOW_HEIGHT = 800;

  public static final String MSG_SAVE_SUCCESS = "Lưu dữ liệu thành công!";
  public static final String MSG_ERROR = "Đã xảy ra lỗi hệ thống. Vui lòng thử lại.";

  // 1. Màu nền chính (Main Background - Xám nhạt hiện đại)
  public static final Color COLOR_BACKGROUND = new Color(240, 242, 245);

  // 2. Màu trắng cho các Card/Panel nội dung
  public static final Color COLOR_WHITE = Color.WHITE;

  // 3. Màu xanh chủ đạo (Primary - Dùng cho Header Table, Button chính)
  public static final Color COLOR_PRIMARY = new Color(32, 136, 203);

  // 4. Màu xanh Sidebar (Dark Sidebar - Midnight Blue)
  public static final Color COLOR_SIDEBAR = new Color(44, 62, 80);

  // 5. Màu xanh bổ trợ (Secondary/Hover)
  public static final Color COLOR_PRIMARY_HOVER = new Color(52, 152, 219);

  // 6. Màu đỏ cho các nút cảnh báo/xóa (Danger)
  public static final Color COLOR_DANGER = new Color(220, 53, 69);

  // 7. Màu chữ (Text Colors)
  public static final Color COLOR_TEXT_MAIN = new Color(51, 51, 51);
  public static final Color COLOR_TEXT_LIGHT = new Color(153, 153, 153);

  // 8. Màu đường kẻ/Zebra (Table Striped)
  public static final Color COLOR_TABLE_ZEBRA = new Color(242, 242, 242);
}