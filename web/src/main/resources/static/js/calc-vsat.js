/* ═══════════════════════════════════════════════
   calc-vsat.js – logic tính điểm VSAT / THPT
   ═══════════════════════════════════════════════ */

/** Đổi method → cập nhật placeholder, hiện/ẩn ô quy đổi TA */
function vsatMethodChange() {
  const isVSAT = document.getElementById('vsat-method').value === 'vsat';

  document.getElementById('thang-label').textContent =
    isVSAT ? '(Thang 150 – V-SAT)' : '(Thang 10)';

  document.getElementById('anh-qd-wrap').style.display =
    isVSAT ? 'block' : 'none';

  const maxVal  = isVSAT ? 150 : 10;
  const ph      = isVSAT ? '0 – 150' : '0 – 10';
  Object.values(MON_ID).forEach(id => {
    const el = document.getElementById('m-' + id);
    if (el) { el.max = maxVal; el.placeholder = ph; }
  });

  buildBonusFields();
  vsatCalc();
}

/** Tạo các ô nhập điểm cộng theo môn */
function buildBonusFields() {
  const wrap = document.getElementById('bonus-fields');
  if (!wrap) return;
  const monList = Object.keys(MON_ID);   // ['Toán','Ngữ Văn',…]
  wrap.innerHTML = monList.map(m => `
    <div class="field">
      <label>+ ${m}</label>
      <input type="number" id="bonus-${MON_ID[m]}"
             placeholder="0.0" min="0" max="2" step="0.25" value="0"
             oninput="vsatCalc()">
    </div>
  `).join('');
}

/** Lấy điểm môn (raw input → thang 10 nếu VSAT) */
function getMonDiem10(id, isVSAT) {
  const raw = parseFloat(document.getElementById('m-' + id).value) || 0;
  return isVSAT ? vsatQuyDoi(raw) : raw;
}
function getMonRaw(id) {
  return parseFloat(document.getElementById('m-' + id).value) || 0;
}
function getBonusMon(id) {
  const el = document.getElementById('bonus-' + id);
  return el ? (parseFloat(el.value) || 0) : 0;
}

/** Tính điểm – entry point */
function vsatCalc(forceShow) {
  const nganhKey = document.getElementById('vsat-nganh').value;
  const isVSAT   = document.getElementById('vsat-method').value === 'vsat';
  const kv       = parseFloat(document.getElementById('vsat-kvut').value);
  const dt       = parseFloat(document.getElementById('vsat-dtut').value);
  const dc       = clamp(parseFloat(document.getElementById('vsat-cong').value) || 0, 0, 3);
  const area     = document.getElementById('vsat-result-area');

  if (!nganhKey) { if (forceShow) area.style.display = 'none'; return; }

  const nganh   = NGANH_DATA[nganhKey];
  const utTotal = kv + dt;

  /* Tiếng Anh – ưu tiên ô quy đổi nếu được điền */
  const anhQDEl  = document.getElementById('m-anh-qd');
  const anhQDVal = anhQDEl ? (parseFloat(anhQDEl.value) || 0) : 0;
  const anhDiem10 = anhQDVal > 0
    ? anhQDVal
    : getMonDiem10('anh', isVSAT);

  /* Tập hợp điểm 10 của tất cả các môn */
  const diem10 = {};
  Object.entries(MON_ID).forEach(([tenMon, id]) => {
    diem10[tenMon] = (tenMon === 'Tiếng Anh') ? anhDiem10 : getMonDiem10(id, isVSAT);
  });

  /* Xây dựng kết quả từng tổ hợp */
  let bestDiem = 0, bestTHM = '';

  const rowsHTML = nganh.thm.map((thm, idx) => {
    const monTen = THM_MON[thm] || [];

    const monDetail = monTen.map(m => {
      const id   = MON_ID[m];
      const raw  = getMonRaw(id);
      const d10  = diem10[m] || 0;
      const bonus= getBonusMon(id);
      const qdTxt = (isVSAT && raw > 0)
        ? `${fmt(raw)}/150 × 10 = <strong>${fmt(d10)}</strong>`
        : `<strong>${fmt(d10)}</strong>`;
      return `
        <tr>
          <td>Điểm môn ${m}${isVSAT ? ' (VSAT)' : ''}: <strong>${raw}</strong></td>
          <td class="formula">
            ${qdTxt}
            ${bonus > 0 ? ` + cộng <strong>${bonus}</strong>` : ''}
          </td>
        </tr>`;
    }).join('');

    /* Điểm tổ hợp: trọng số M1×1, M2×2, M3×1 → /4×3 (theo ảnh PDF) */
    const m1 = (diem10[monTen[0]] || 0) + getBonusMon(MON_ID[monTen[0]]);
    const m2 = (diem10[monTen[1]] || 0) + getBonusMon(MON_ID[monTen[1]]);
    const m3 = monTen[2] ? ((diem10[monTen[2]] || 0) + getBonusMon(MON_ID[monTen[2]])) : 0;
    const dthxt   = (m1 * 1 + m2 * 2 + m3 * 1) / 4 * 3;
    const sumRaw  = (diem10[monTen[0]] || 0) + (diem10[monTen[1]] || 0) + (diem10[monTen[2]] || 0);
    const dxt     = clamp(dthxt + dc + utTotal, 0, 30);
    const passNG  = dxt >= nganh.nguong;

    if (dxt > bestDiem) { bestDiem = dxt; bestTHM = thm; }

    return `
      <tr class="section-row">
        <td colspan="2">
          <strong>${idx + 1}) Tổ hợp: ${thm} – ${monTen.join(', ')}</strong>
        </td>
      </tr>
      ${monDetail}
      <tr><td>Khu vực ưu tiên</td><td>${fmt(kv)}</td></tr>
      <tr><td>Đối tượng ưu tiên</td><td>${fmt(dt)}</td></tr>
      <tr><td>Điểm cộng (≤ 3)</td><td>${fmt(dc)}</td></tr>
      <tr>
        <td>Xét ngưỡng = Tổng 3 môn + điểm ưu tiên</td>
        <td class="${passNG ? 'col-green' : 'col-red'}">
          ${fmt(sumRaw + utTotal)}
        </td>
      </tr>
      <tr>
        <td>Điểm tổ hợp xét tuyển (ĐTHXT) = (M1×1 + M2×2 + M3×1) / 4 × 3</td>
        <td class="col-blue">${fmt(dthxt)}</td>
      </tr>
      <tr>
        <td>Điểm ưu tiên (ĐUT)</td>
        <td>${fmt(utTotal)}</td>
      </tr>
      <tr class="section-row">
        <td>Điểm xét tuyển (DXT) = ĐTHXT + ĐC + ĐUT (≤ 30)</td>
        <td style="font-size:1rem;font-weight:700"
            class="${passNG ? 'col-green' : 'col-red'}">
          ${fmt(dxt)}
        </td>
      </tr>`;
  }).join('');

  const passChuan  = nganh.diem_chuan !== null ? bestDiem >= nganh.diem_chuan : null;
  const passNguong = bestDiem >= nganh.nguong;

  area.style.display = 'block';
  area.innerHTML = `
    <div class="breakdown-wrap">
      <div class="breakdown-title">
        Tính điểm vào ngành xét tuyển:
        <strong>${nganh.ten} (${nganh.ma})</strong>
      </div>
      <div class="breakdown-note">
        Tổ hợp có điểm xét tuyển cao nhất:
        <span class="combo-chip">${bestTHM || '—'}</span>
        &nbsp;— điểm <strong>${fmt(bestDiem)}</strong>.
        Tổ hợp gốc: <span class="combo-chip">${DGNL_THM_GOC[nganhKey]}</span>,
        ngưỡng đầu vào: <strong>${nganh.nguong}</strong>
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
        <tbody>${rowsHTML}</tbody>
      </table>
    </div>

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
        <span class="ng-label">
          So sánh với điểm chuẩn trúng tuyển (${nganh.diem_chuan})
        </span>
        <span>
          ${passChuan
            ? '<span class="badge badge-green">✓ TRÚNG TUYỂN</span>'
            : '<span class="badge badge-red">✗ Chưa đủ điểm chuẩn</span>'}
        </span>
      </div>` : ''}
    </div>
  `;
}
