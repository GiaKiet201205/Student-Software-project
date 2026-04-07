package com.service;

import com.config.HibernateUtil;
import com.exception.AppException;
import com.service.mapper.RowMapper;
import com.service.mapper.SimpleRow;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.hibernate.StatelessSession;
import org.hibernate.Transaction;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class GenericImportService {

    private static final int BATCH_SIZE = 1000;
    private static final int QUEUE_CAPACITY = 5000;
    private static final Object POISON_PILL = new Object();

    public <T> boolean importFromExcel(File file, RowMapper<T> mapper) {
        BlockingQueue<Object> queue = new LinkedBlockingQueue<>(QUEUE_CAPACITY);
        AtomicBoolean dbSuccess = new AtomicBoolean(true);

        Thread dbThread = new Thread(() -> {
            long dbStart = System.currentTimeMillis();
            System.out.println("=> DB Thread: Bắt đầu nhận dữ liệu...");
            try (StatelessSession session = HibernateUtil.getSessionFactory().openStatelessSession()) {
                Transaction tx = session.beginTransaction();
                int count = 0;
                while (true) {
                    Object item = queue.take();
                    if (item == POISON_PILL) break;
                    session.insert(item);
                    if (++count % BATCH_SIZE == 0) {
                        tx.commit();
                        System.out.println("=> DB Thread: Đã Insert " + count + " dòng...");
                        tx = session.beginTransaction();
                    }
                }
                tx.commit();
                System.out.println("=> DB Thread: HOÀN THÀNH. Tổng: " + count + " dòng. Thời gian: " + (System.currentTimeMillis() - dbStart) + "ms");
            } catch (Exception e) {
                dbSuccess.set(false);
                e.printStackTrace();
            }
        });

        dbThread.start();

        long t1 = System.currentTimeMillis();
        try (OPCPackage pkg = OPCPackage.open(file, PackageAccess.READ)) {
            System.out.println("1. Mở file (Unzip): " + (System.currentTimeMillis() - t1) + "ms");

            long t2 = System.currentTimeMillis();
            XSSFReader reader = new XSSFReader(pkg);
            System.out.println("2. Xóa reader: " + (System.currentTimeMillis() - t2) + "ms");

            long t3 = System.currentTimeMillis();
            ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(pkg);
            System.out.println("3. Load SharedStringsTable: " + (System.currentTimeMillis() - t3) + "ms");

            long t4 = System.currentTimeMillis();
            InputStream sheet = getFirstSheetStream(reader);
            RawSheetHandler<T> handler = new RawSheetHandler<>(mapper, queue, strings);
            SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();
            saxParser.parse(sheet, handler);
            System.out.println("4. Parse SAX dữ liệu thật: " + (System.currentTimeMillis() - t4) + "ms");

        } catch (SAXException e) {
            if ("STOP_PARSING".equals(e.getMessage())) {
                System.out.println("Đã bỏ qua các dòng trống cuối file. Kết thúc sớm!");
            } else {
                dbSuccess.set(false);
                e.printStackTrace();
            }
        } catch (Exception e) {
            dbSuccess.set(false);
            e.printStackTrace();
        } finally {
            try { queue.put(POISON_PILL); } catch (InterruptedException ignored) {}
        }

        try { dbThread.join(); } catch (InterruptedException ignored) {}

        return dbSuccess.get();
    }

    // ── Raw SAX Handler: chỉ parse <row>, <c>, <v> ─────────────────────────
    private static class RawSheetHandler<T> extends DefaultHandler {

        private final RowMapper<T>              mapper;
        private final BlockingQueue<Object>     queue;
        private final ReadOnlySharedStringsTable strings;

        private final Map<Integer, String> currentRow = new HashMap<>();
        private final StringBuilder        cellValue  = new StringBuilder();

        private int     currentCol  = 0;
        private boolean isSharedStr = false; // type="s" → shared string
        private boolean inValue     = false;
        private boolean firstRow    = true;
        private int emptyRowCount = 0;
        private static final int MAX_EMPTY_ROWS = 50;

        RawSheetHandler(RowMapper<T> mapper, BlockingQueue<Object> queue,
                        ReadOnlySharedStringsTable strings) {
            this.mapper  = mapper;
            this.queue   = queue;
            this.strings = strings;
        }

        @Override
        public void startElement(String uri, String localName,
                                 String qName, Attributes attrs) {
            switch (qName) {
                case "row" -> currentRow.clear();

                case "c" -> {
                    // r="A1" → col index
                    String ref = attrs.getValue("r");
                    currentCol  = ref != null ? colIndex(ref) : 0;
                    isSharedStr = "s".equals(attrs.getValue("t"));
                    cellValue.setLength(0);
                    inValue = false;
                }
                case "v", "t" -> inValue = true;
            }
        }

        @Override
        public void characters(char[] ch, int start, int length) {
            if (inValue) cellValue.append(ch, start, length);
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            switch (qName) {
                case "v", "t" -> {
                    String raw = cellValue.toString().trim();
                    String val = isSharedStr
                            ? strings.getItemAt(Integer.parseInt(raw)).getString()
                            : raw;
                    currentRow.put(currentCol, val);
                    inValue = false;
                }
                case "row" -> {
                    if (firstRow) { firstRow = false; return; }

                    T entity = mapper.map(new SimpleRow(currentRow));

                    if (entity == null) {
                        emptyRowCount++;
                        // NẾU VƯỢT QUÁ SỐ DÒNG TRỐNG -> NÉM EXCEPTION ĐỂ DỪNG SAX PARSER
                        if (emptyRowCount > MAX_EMPTY_ROWS) {
                            throw new SAXException("STOP_PARSING");
                        }
                        return;
                    }

                    // Nếu có dữ liệu thật, reset bộ đếm về 0
                    emptyRowCount = 0;

                    try { queue.put(entity); } catch (InterruptedException ignored) {}
                }
            }
        }

        // "AB12" → 27
        private int colIndex(String ref) {
            int col = 0;
            for (char c : ref.toCharArray()) {
                if (Character.isLetter(c)) col = col * 26 + (c - 'A' + 1);
                else break;
            }
            return col - 1;
        }
    }

    private InputStream getFirstSheetStream(XSSFReader reader) throws Exception {
        XSSFReader.SheetIterator it = (XSSFReader.SheetIterator) reader.getSheetsData();
        if (it.hasNext()) return it.next();
        throw new AppException("Excel không có sheet nào");
    }
}
