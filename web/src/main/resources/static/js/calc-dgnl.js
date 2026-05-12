/* ═══════════════════════════════════════════════
   calc-dgnl.js – logic tính điểm ĐGNL
   ═══════════════════════════════════════════════ */

function dgnlCalc(forceShow) {
  const nganhKey = document.getElementById('dgnl-nganh').value;
  const diemRaw  = parseFloat(document.getElementById('dgnl-diem').value);
  const kv       = parseFloat(document.getElementById('dgnl-kvut').value);
  const dt       = parseFloat(document.getElementById('dgnl-dtut').value);
  const dc       = clamp(parseFloat(document.getElementById('dgnl-cong').value) || 0, 0, 3);
  const area     = document.getElementById('dgnl-result-area');

  if (!nganhKey || isNaN(diemRaw) || diemRaw < 0) {
    if (forceShow) area.style.display = 'none';
    return;
  }

  const nganh   = NGANH_DATA[nganhKey];
  const thmGoc  = DGNL_THM_GOC[nganhKey];
  const diemQD  = dgnlQuyDoi(diemRaw);
  const formula = dgnlFormula(diemRaw);
  const utQD    = kv + dt;
  const diemXT  = clamp(diemQD + dc + utQD, 0, 30);

  const passNguong = diemXT >= nganh.nguong;
  const passChuan  = nganh.diem_chuan !== null ? diemXT >= nganh.diem_chuan : null;

  area.style.display = 'block';
  area.innerHTML = `
    <div class="breakdown-wrap">
      <div class="breakdown-title">
        Tính quy đổi điểm xét tuyển ĐGNL:
        <strong>${nganh.ten}</strong>
      </div>
      <div class="breakdown-note">
        Tổ hợp gốc quy đổi theo ngành:
        <span class="combo-chip">${thmGoc}</span>
        &nbsp;— ngưỡng đầu vào: <strong>${nganh.nguong}</strong>
      </div>
      <div class="breakdown-priority">
        📌 Khu vực ưu tiên: <strong>${fmt(kv)}</strong>
        &nbsp;|&nbsp; Đối tượng ưu tiên: <strong>${fmt(dt)}</strong>
        &nbsp;|&nbsp; Điểm cộng: <strong>${fmt(dc)}</strong>
      </div>
      <table class="result-table">
        <thead>
          <tr><th>Nội dung</th><th>Chi tiết điểm</th></tr>
        </thead>
        <tbody>
          <tr>
            <td>Điểm thi ĐGNL</td>
            <td class="bold">${diemRaw}.00</td>
          </tr>
          <tr>
            <td>Công thức quy đổi về thang 30 (THM gốc: ${thmGoc})</td>
            <td class="formula">${formula}</td>
          </tr>
          <tr>
            <td>Điểm thi quy đổi</td>
            <td class="col-blue">${fmt(diemQD)}</td>
          </tr>
          <tr>
            <td>Điểm cộng (≤ 3.0)</td>
            <td>${fmt(dc)}</td>
          </tr>
          <tr>
            <td>Điểm ưu tiên khu vực</td>
            <td>${fmt(kv)}</td>
          </tr>
          <tr>
            <td>Điểm ưu tiên đối tượng</td>
            <td>${fmt(dt)}</td>
          </tr>
          <tr class="section-row">
            <td>Điểm xét tuyển = điểm quy đổi + điểm cộng + điểm ưu tiên</td>
            <td style="font-size:1.1rem"
                class="${passNguong ? 'col-green' : 'col-red'}">
              ${fmt(diemXT)}
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- So sánh ngưỡng / chuẩn -->
    <div style="margin-top:.75rem">
      <div class="nguong-row ${passNguong ? 'pass' : 'fail'}">
        <span class="ng-label">So sánh với điểm ngưỡng (${nganh.nguong})</span>
        <span>
          ${passNguong
            ? '<span class="badge badge-green">✓ ĐẠT ngưỡng đầu vào</span>'
            : '<span class="badge badge-red">✗ CHƯA ĐẠT ngưỡng</span>'}
        </span>
      </div>
      ${passChuan !== null ? `
      <div class="nguong-row ${passChuan ? 'pass' : 'fail'}">
        <span class="ng-label">So sánh với điểm chuẩn trúng tuyển (${nganh.diem_chuan})</span>
        <span>
          ${passChuan
            ? '<span class="badge badge-green">✓ TRÚNG TUYỂN</span>'
            : '<span class="badge badge-red">✗ Chưa đủ điểm chuẩn</span>'}
        </span>
      </div>` : ''}
    </div>
  `;
}
