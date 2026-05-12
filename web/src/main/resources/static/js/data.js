/* ═══════════════════════════════════════════════
   data.js – dữ liệu dùng chung (ngành, thang điểm…)
   ═══════════════════════════════════════════════ */

// ── Danh sách ngành ──────────────────────────────────────────────────────────
const NGANH_DATA = {
  CNTT: { ten: 'Công nghệ thông tin',  ma: '7480201', thm: ['A00','A01','D01'], nguong: 22, diem_chuan: 27.5 },
  KTPM: { ten: 'Kỹ thuật phần mềm',   ma: '7480103', thm: ['A00','A01'],       nguong: 22, diem_chuan: 27.1 },
  QTKD: { ten: 'Quản trị kinh doanh', ma: '7340101', thm: ['A00','A01','D01'], nguong: 18, diem_chuan: 26.2 },
  QLGD: { ten: 'Quản lý giáo dục',    ma: '7140114', thm: ['D01','B03','C01'], nguong: 18, diem_chuan: 24.5 },
  LUAT: { ten: 'Luật',                ma: '7380101', thm: ['C00','D01','A01'], nguong: 18, diem_chuan: 25.3 },
  YHOC: { ten: 'Y đa khoa',           ma: '7720101', thm: ['B00'],             nguong: 24, diem_chuan: 29.3 },
};

// ── Môn của từng tổ hợp ──────────────────────────────────────────────────────
const THM_MON = {
  A00: ['Toán', 'Vật Lý', 'Hóa Học'],
  A01: ['Toán', 'Vật Lý', 'Tiếng Anh'],
  B00: ['Toán', 'Hóa Học', 'Sinh Học'],
  B03: ['Toán', 'Sinh Học', 'Ngữ Văn'],
  C00: ['Ngữ Văn', 'Lịch Sử', 'Địa Lý'],
  C01: ['Ngữ Văn', 'Toán', 'Vật Lý'],
  D01: ['Toán', 'Ngữ Văn', 'Tiếng Anh'],
};

// ── ID input cho từng môn ─────────────────────────────────────────────────────
const MON_ID = {
  'Toán':       'toan',
  'Ngữ Văn':    'van',
  'Vật Lý':     'ly',
  'Hóa Học':    'hoa',
  'Sinh Học':   'sinh',
  'Lịch Sử':   'su',
  'Địa Lý':    'dia',
  'Tiếng Anh':  'anh',
};

// ── Tổ hợp gốc dùng cho ĐGNL ─────────────────────────────────────────────────
const DGNL_THM_GOC = {
  CNTT: 'D01', KTPM: 'A01', QTKD: 'D01',
  QLGD: 'D01', LUAT: 'D01', YHOC: 'B00',
};

// ── Bảng quy đổi điểm ĐGNL → thang 30 ──────────────────────────────────────
// Mỗi phần tử: [diemRaw, diemQD30]  — sắp xếp giảm dần
const DGNL_SCALE = [
  [1200,30.00],[1195,29.75],[1190,29.50],[1185,29.25],[1180,29.00],
  [1170,28.75],[1160,28.50],[1150,28.25],[1140,28.00],[1120,27.75],
  [1100,27.50],[1080,27.25],[1060,27.00],[1040,26.75],[1020,26.50],
  [1000,26.25],[ 980,26.00],[ 960,25.75],[ 940,25.50],[ 920,25.25],
  [ 900,25.00],[ 895,24.75],[ 890,22.25],[ 885,22.00],[ 880,21.75],
  [ 870,21.50],[ 860,21.25],[ 850,21.00],[ 830,20.75],[ 810,20.50],
  [ 790,20.25],[ 770,20.00],[ 750,19.75],[ 730,19.50],[ 700,19.00],
  [ 680,18.75],[ 660,18.50],[ 640,18.25],[ 620,18.00],[ 600,17.50],
  [ 580,17.00],[ 560,16.50],[ 540,16.00],[ 520,15.50],[ 500,15.00],
  [ 480,14.50],[ 460,14.00],[ 440,13.50],[ 420,13.00],[ 400,12.00],
  [   0, 0.00],
];

// ── Dữ liệu thí sinh (demo) ───────────────────────────────────────────────────
// Trong ứng dụng Spring MVC thực tế, dữ liệu này lấy từ DB / REST API.
const STUDENTS_DB = {
  '012345678901': {
    pass: '05032006', ten: 'Nguyễn Văn An', ngaysinh: '05/03/2006',
    truong: 'ĐH Bách Khoa Hà Nội', nganh: 'Công nghệ thông tin',
    ma: '7480201', thm: 'A00', phuongthuc: 'VSAT',
    diem: 27.5, nguong: 22, diem_chuan: 27.1, trungtuyenkq: true,
  },
  '098765432109': {
    pass: '15092006', ten: 'Trần Thị Bình', ngaysinh: '15/09/2006',
    truong: 'ĐH Ngoại Thương', nganh: 'Kinh doanh quốc tế',
    ma: '7340101', thm: 'D01', phuongthuc: 'THPT',
    diem: 24.8, nguong: 18, diem_chuan: 27.6, trungtuyenkq: false,
  },
  '001234567890': {
    pass: '22112005', ten: 'Lê Minh Tuấn', ngaysinh: '22/11/2005',
    truong: 'ĐH Y Hà Nội', nganh: 'Y đa khoa',
    ma: '7720101', thm: 'B00', phuongthuc: 'THPT',
    diem: 29.5, nguong: 24, diem_chuan: 29.3, trungtuyenkq: true,
  },
};

// ═══════════════════════════════════════════
//  UTILITY FUNCTIONS
// ═══════════════════════════════════════════

/** Quy đổi điểm ĐGNL (thang 1200) → thang 30 theo bảng tra */
function dgnlQuyDoi(raw) {
  for (let i = 0; i < DGNL_SCALE.length - 1; i++) {
    const [hi, hiV] = DGNL_SCALE[i];
    const [lo, loV] = DGNL_SCALE[i + 1];
    if (raw >= lo && raw <= hi) {
      if (hi === lo) return hiV;
      return loV + (raw - lo) / (hi - lo) * (hiV - loV);
    }
  }
  return 0;
}

/** Công thức nội suy tuyến tính (hiển thị) */
function dgnlFormula(raw) {
  for (let i = 0; i < DGNL_SCALE.length - 1; i++) {
    const [hi, hiV] = DGNL_SCALE[i];
    const [lo, loV] = DGNL_SCALE[i + 1];
    if (raw >= lo && raw <= hi) {
      return `${fmt(loV)} + ( ${raw} - ${lo} ) / ( ${hi} - ${lo} ) * ( ${fmt(hiV)} - ${fmt(loV)} )`;
    }
  }
  return `${raw} / 1200 × 30`;
}

/** Quy đổi điểm VSAT (thang 150) → thang 10 */
function vsatQuyDoi(d) { return Math.min(10, d / 150 * 10); }

/** Định dạng số 2 chữ số thập phân */
function fmt(v) { return Number(v).toFixed(2); }

/** Clamp */
function clamp(v, min, max) { return Math.min(max, Math.max(min, v)); }

/** Populate select ngành */
function populateNganhSelect(selectId) {
  const sel = document.getElementById(selectId);
  if (!sel) return;
  sel.innerHTML = '<option value="">-- Chọn ngành --</option>';
  Object.entries(NGANH_DATA).forEach(([key, ng]) => {
    const opt = document.createElement('option');
    opt.value = key;
    opt.textContent = `${ng.ten} (${ng.ma})`;
    sel.appendChild(opt);
  });
}
