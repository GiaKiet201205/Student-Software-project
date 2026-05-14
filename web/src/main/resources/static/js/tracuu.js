/* ═══════════════════════════════════════════════
   tracuu.js – logic tra cứu kết quả xét tuyển
   ═══════════════════════════════════════════════ */

/** Xử lý đăng nhập / tra cứu */
function doLogin() {
  const cccd    = document.getElementById('login-cccd').value.trim();
  const pass    = document.getElementById('login-pass').value.trim();
  const errEl   = document.getElementById('login-err');

  /* Reset lỗi */
  errEl.style.display = 'none';
  errEl.textContent   = '';

  /* Validate đầu vào */
  if (!cccd || !pass) {
    showErr('Vui lòng nhập đầy đủ số CCCD và mật khẩu.');
    return;
  }
  if (!/^\d{9,12}$/.test(cccd)) {
    showErr('Số CCCD không hợp lệ (9–12 chữ số).');
    markError('login-cccd');
    return;
  }
  if (!/^\d{8}$/.test(pass)) {
    showErr('Mật khẩu phải là 8 chữ số ngày sinh (ddmmyyyy).');
    markError('login-pass');
    return;
  }

  /* Tra cứu trong DB */
  const st = STUDENTS_DB[cccd];

  if (!st || st.pass !== pass) {
    showErr('Không tìm thấy thí sinh hoặc mật khẩu không đúng.');
    return;
  }

  /* Ẩn form, hiện kết quả */
  document.getElementById('login-form-wrap').style.display   = 'none';
  document.getElementById('tracuu-result-wrap').style.display = 'block';
  renderTracuu(st);
}

/** Đăng xuất – quay về form */
function doLogout() {
  document.getElementById('login-form-wrap').style.display    = 'flex';
  document.getElementById('tracuu-result-wrap').style.display = 'none';
  document.getElementById('login-cccd').value = '';
  document.getElementById('login-pass').value = '';
  clearErrors();
}

/** Render trang kết quả */
function renderTracuu(st) {
  const el       = document.getElementById('tracuu-content');
  const admitted = st.trungtuyenkq;

  el.innerHTML = `
    <!-- Banner thí sinh -->
    <div class="student-banner">
      <div class="sname">👤 ${st.ten}</div>
      <div class="smeta">
        <span>📅 Ngày sinh: ${st.ngaysinh}</span>
        <span>🏫 ${st.truong}</span>
        <span>📚 ${st.nganh} (${st.ma})</span>
      </div>
    </div>

    <!-- Trạng thái trúng tuyển -->
    <div class="admit-status ${admitted ? 'yes' : 'no'}">
      <div class="asi">${admitted ? '✅' : '❌'}</div>
      <div class="ainfo">
        <strong>${admitted ? 'TRÚNG TUYỂN' : 'KHÔNG TRÚNG TUYỂN'}</strong>
        <span>
          ${admitted
            ? `Ngành <strong>${st.nganh}</strong> – ${st.truong}`
            : `Điểm xét tuyển <strong>${st.diem}</strong> thấp hơn điểm chuẩn <strong>${st.diem_chuan}</strong>`}
        </span>
      </div>
    </div>

    <!-- Chi tiết -->
    <div class="card">
      <div class="card-title">
        <span class="ico ${admitted ? 'ico-green' : 'ico-blue'}">📄</span>
        Chi Tiết Kết Quả Xét Tuyển
      </div>
      <table class="result-table">
        <tbody>
          <tr><td>Ngành đăng ký</td>
              <td class="bold">${st.nganh}</td></tr>
          <tr><td>Mã ngành</td>
              <td>${st.ma}</td></tr>
          <tr><td>Trường</td>
              <td>${st.truong}</td></tr>
          <tr><td>Phương thức xét tuyển</td>
              <td><span class="badge badge-blue">${st.phuongthuc}</span></td></tr>
          <tr><td>Tổ hợp xét tuyển</td>
              <td><span class="combo-chip">${st.thm}</span></td></tr>
          <tr><td>Điểm xét tuyển</td>
              <td class="${admitted ? 'col-green' : 'col-red'} bold" style="font-size:1.05rem">
                ${st.diem}
              </td></tr>
          <tr><td>Điểm ngưỡng đầu vào</td>
              <td>${st.nguong}
                <span class="badge badge-green" style="margin-left:.4rem">✓ Đạt</span>
              </td></tr>
          <tr><td>Điểm chuẩn trúng tuyển</td>
              <td>${st.diem_chuan}</td></tr>
          <tr><td>Kết quả so điểm ngưỡng</td>
              <td>
                ${st.diem >= st.nguong
                  ? '<span class="badge badge-green">✓ ĐẠT</span>'
                  : '<span class="badge badge-red">✗ KHÔNG ĐẠT</span>'}
              </td></tr>
          <tr><td>Kết quả so điểm chuẩn</td>
              <td>
                ${admitted
                  ? '<span class="badge badge-green">✓ TRÚNG TUYỂN</span>'
                  : '<span class="badge badge-red">✗ KHÔNG TRÚNG TUYỂN</span>'}
              </td></tr>
        </tbody>
      </table>
    </div>

    ${!admitted ? `
    <div class="alert alert-warn">
      ⚠️ Điểm của bạn đạt ngưỡng xét tuyển nhưng chưa đủ điểm chuẩn
      trúng tuyển năm ${new Date().getFullYear()}.
      Bạn có thể tra cứu các nguyện vọng khác hoặc liên hệ phòng tuyển sinh.
    </div>` : ''}
  `;
}

/* ── Helpers ── */
function showErr(msg) {
  const el = document.getElementById('login-err');
  el.innerHTML = '❌ ' + msg;
  el.style.display = 'flex';
}
function markError(id) {
  document.getElementById(id)?.classList.add('is-error');
}
function clearErrors() {
  document.querySelectorAll('.is-error').forEach(el => el.classList.remove('is-error'));
  const err = document.getElementById('login-err');
  if (err) { err.style.display = 'none'; err.textContent = ''; }
}

/* ── Enter key shortcut ── */
document.addEventListener('DOMContentLoaded', () => {
  document.getElementById('login-pass')
    ?.addEventListener('keydown', e => { if (e.key === 'Enter') doLogin(); });
  document.getElementById('login-cccd')
    ?.addEventListener('keydown', e => {
      if (e.key === 'Enter') document.getElementById('login-pass').focus();
    });
  document.getElementById('login-cccd')
    ?.addEventListener('input', () => clearErrors());
  document.getElementById('login-pass')
    ?.addEventListener('input', () => clearErrors());
});
