package com.example.demoVNPAYAMS.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ReadService {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private static final Map<String, String> vietnameseToEnglishDictionary = new HashMap<>();
    public static  final Map<String,String> valueHeader= new HashMap<>();

    static {
        valueHeader.put("date","DATETIME");
        valueHeader.put("transaction_id","varchar(255)");
        valueHeader.put("number","int");
        valueHeader.put("code","varchar(255)");
        valueHeader.put("service","text");
        valueHeader.put("debit","double");
        valueHeader.put("credit","double");
        valueHeader.put("balance","double");
        valueHeader.put("channel","varchar(255)");
        valueHeader.put("type","varchar(255)");
        valueHeader.put("amount of money","double");
        valueHeader.put("status","varchar(255)");
        valueHeader.put("deposits","double");
        valueHeader.put("withdrawal","double");
        valueHeader.put("reference","varchar(255)");
        valueHeader.put("id","id INT AUTO_INCREMENT PRIMARY KEY ");
        vietnameseToEnglishDictionary.put("hiệu lực", "value");
        vietnameseToEnglishDictionary.put("stt", "id");
        vietnameseToEnglishDictionary.put("mã", "code");
        vietnameseToEnglishDictionary.put("gd", "transaction");
        vietnameseToEnglishDictionary.put("diễn giải", "description");
        vietnameseToEnglishDictionary.put("mô tả", "description");
        vietnameseToEnglishDictionary.put("giao dịch", "transaction");
        vietnameseToEnglishDictionary.put("ngày", "date");
        vietnameseToEnglishDictionary.put("thời gian", "date");
        vietnameseToEnglishDictionary.put("số", "number");
        vietnameseToEnglishDictionary.put("ghi nợ", "debit");
        vietnameseToEnglishDictionary.put("ghi có", "credit");
        vietnameseToEnglishDictionary.put("số dư", "balance");
        vietnameseToEnglishDictionary.put("loại dv", "service");
        vietnameseToEnglishDictionary.put("dich vụ", "service");
        vietnameseToEnglishDictionary.put("kênh","channel");
        vietnameseToEnglishDictionary.put("số tiền","amount of money");
        vietnameseToEnglishDictionary.put("trạng thái","status");
        vietnameseToEnglishDictionary.put("số tiền gửi","deposits");
        vietnameseToEnglishDictionary.put("số tiền rút","withdrawal");
        vietnameseToEnglishDictionary.put("loại","type");
        vietnameseToEnglishDictionary.put("bút toán","transaction_id");
        vietnameseToEnglishDictionary.put("tiền tệ","currency");
        vietnameseToEnglishDictionary.put("tham chiếu","reference");
        // Thêm các từ khác nếu cần thiết
    }

    public String readFile(String fileName) throws FileNotFoundException {
        try {
            FileInputStream file = new FileInputStream(new File(fileName));
            File getFileName = new File(fileName);
            String nameFile = getFileName.getName().substring(0, getFileName.getName().lastIndexOf('.'));// Tạo FileInputStream từ tệp Excel
            XSSFWorkbook workbook = new XSSFWorkbook(file); // Tạo Workbook từ FileInputStream
            Sheet sheet = workbook.getSheetAt(0);
            List<String> firstRowData = readFirstRow(sheet);
            List<String>  headerConvertToEngLish = new ArrayList<>();
            for (String cellData : firstRowData) {
                headerConvertToEngLish.add(convertToEnglish(cellData));
            }
            String sqlQuery=createTableSql(nameFile,headerConvertToEngLish);
            createTableInSql(sqlQuery);
            int startRow = 2; // Dòng bắt đầu đọc từ dòng thứ 2
            List<Object> rowData = readRows(sheet, startRow,headerConvertToEngLish); // Đọc dữ liệu từ dòng thứ 2 trở đi
            String insertDataSql = createInsertSql(nameFile, rowData, headerConvertToEngLish);
            insertValueInTable(insertDataSql);
            workbook.close(); // Đóng Workbook sau khi đọc xong
            file.close(); // Đóng FileInputStream
            return insertDataSql;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    private String createInsertSql(String tableName, List<Object> rowData, List<String> headerConvertToEnglish) {
        StringBuilder sqlQuery = new StringBuilder("INSERT INTO ");
        sqlQuery.append(tableName).append(" (");

        // Thêm các cột vào câu lệnh INSERT
        for (String columnName : headerConvertToEnglish) {
            sqlQuery.append(formatFieldName(columnName)).append(", ");
        }
        // Xóa dấu phẩy thừa ở cuối
        sqlQuery.delete(sqlQuery.length() - 2, sqlQuery.length() - 1);

        sqlQuery.append(") VALUES (");

        // Thêm giá trị của từng ô vào câu lệnh INSERT

        for (Object row : rowData) {
            List<String> cellDataList = (List<String>) row;
            for (String cellData : cellDataList) {
                if (cellData != null) {
                    // Đối với các giá trị không null, thêm vào câu lệnh INSERT
                    if (cellData.matches("\\d{2}/\\d{2}/\\d{4}")) {
                        cellData = convertDate(cellData);
                    }
                    else if (cellData.matches("\\d{2}-\\w{3}-\\d{4}")) {
                        cellData = convertDateTimeStringInMonth(cellData); // Chuyển đổi ngày
                    }
                    else if (cellData.matches("\\d{2}/\\d{2}/\\d{4} \\d{2}:\\d{2}:\\d{2} [AP]M")) {
                        cellData = convertDateTime(cellData); // Chuyển đổi ngày giờ
                    }

                    sqlQuery.append("'").append(cellData).append("', ");
                } else {
                    // Đối với các giá trị null, thêm NULL vào câu lệnh INSERT
                    sqlQuery.append("NULL, ");
                }
            }
            // Xóa dấu phẩy thừa ở cuối
            sqlQuery.delete(sqlQuery.length() - 2, sqlQuery.length() - 1);
            sqlQuery.append("), (");
        }
        // Xóa dấu phẩy thừa và dấu mở ngoặc ở cuối
        sqlQuery.delete(sqlQuery.length() - 3, sqlQuery.length());

        sqlQuery.append(";");

        return sqlQuery.toString();
    }
    private String convertDate(String date) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date parsedDate = inputFormat.parse(date);
            return outputFormat.format(parsedDate);
        } catch (java.text.ParseException e) {
            throw new RuntimeException(e);
        }
    }
    private String convertDateTime(String dateTime) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date parsedDateTime = inputFormat.parse(dateTime);
            return outputFormat.format(parsedDateTime);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String convertDateTimeStringInMonth(String date) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date parsedDate = inputFormat.parse(date);
            return outputFormat.format(parsedDate);
        } catch ( java.text.ParseException e) {
            e.printStackTrace();
            return null;
        }
    }



    private List<Object> readRows(Sheet sheet, int startRow,List<String> headerConvertToEnglish) {
        List<Object> rowData = new ArrayList<>();

        Row row;
        List<String> cellDataList;
        // Duyệt từ dòng startRow đến hết số dòng có dữ liệu trong sheet
        int rowNum;
        for ( rowNum = startRow; rowNum <= sheet.getLastRowNum(); rowNum++) {
            row = sheet.getRow(rowNum);
            if (row != null) {
                cellDataList = new ArrayList<>();
                int cellNum;
                Cell cell;
                // Duyệt qua các ô trong dòng hiện tại
                for ( cellNum = 0; cellNum < headerConvertToEnglish.size(); cellNum++) {
                    cell = row.getCell(cellNum);
                    if (cell != null) {
                        // Lấy giá trị của ô và thêm vào danh sách cellDataList
                        if (cell.getCellType() == CellType.BLANK || cell.getCellType() == CellType._NONE) {
                            // Nếu ô trống, thêm null vào danh sách cellDataList
                            cellDataList.add(null);
                        } else {
                            // Nếu ô không trống, lấy giá trị của ô và thêm vào danh sách cellDataList
                            cellDataList.add(cell.toString());
                        }
                    } else {
                        // Nếu ô trống, thêm chuỗi rỗng vào danh sách cellDataList
                        cellDataList.add(null);
                    }
                }
                // Thêm danh sách giá trị của các ô trong dòng vào danh sách chứa dữ liệu của các dòng
                rowData.add(cellDataList);
            }
        }
        return rowData;
    }
    private static String createTableSql(String nameTable, List<String> fields) {
        StringBuilder sql = new StringBuilder();
        sql.append("CREATE TABLE ").append(nameTable).append(" (");
        sql.append("\n    id INT AUTO_INCREMENT PRIMARY KEY,");


        for (String field : fields) {
            sql.append("\n    ").append(formatFieldName(field)).append(" ").append(guessDataType(field)).append(",");
        }

        // Xóa dấu phẩy cuối cùng và đóng dấu đóng ngoặc đóng
        sql.deleteCharAt(sql.length() - 1);
        sql.append("\n)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;");

        return sql.toString();
    }
    private static String formatFieldName(String fieldName) {
        // Xóa khoảng trắng và thay thế bằng dấu gạch dưới
        return fieldName.toLowerCase().replace(" ", "_");
    }

    private void createTableInSql(String sqlQuery){
        if(!sqlQuery.isEmpty()){
            try {
                jdbcTemplate.execute(sqlQuery);
                System.out.println("Them thanh cong");
            }
            catch (Exception e){
                System.out.println("Them bang khong thanh cong");
            }
        }

    }
    private static String guessDataType(String fieldName) {
        // Dự đoán kiểu dữ liệu dựa trên từ khóa trong tên trường
        if (fieldName.contains("date")) {
            return "DATETIME";
        }  else if(fieldName.contains("reference")){
            return "TEXT";
        } else if (fieldName.contains("debit") || fieldName.contains("credit") || fieldName.contains("balance")) {
            return "DOUBLE";
        } else if (fieldName.contains("number")) {
            return "INT";
        } else {
            return "TEXT";
        }
    }
    private static String convertToEnglish(String vietnameseWord) {
        // Tạo một StringBuilder để xây dựng chuỗi kết quả
        StringBuilder result = new StringBuilder();

        // Chuyển đổi từ tiếng Việt sang chữ thường để so sánh dễ dàng hơn
        String vietnameseLowercase = vietnameseWord.toLowerCase();

        // Biến để lưu trữ các từ ưu tiên cần đưa xuống cuối chuỗi
        List<String> priorityKeywords = Arrays.asList("number", "type", "date", "status","code");

        // Biến để lưu trữ các từ đã được chuyển đổi
        List<String> convertedWords = new ArrayList<>();

        // Duyệt qua các từ trong từ điển
        for (Map.Entry<String, String> entry : vietnameseToEnglishDictionary.entrySet()) {
            String vietnameseKey = entry.getKey();
            String englishValue = entry.getValue();

            // Nếu từ tiếng Việt có trong từ điển, thêm từ tiếng Anh tương ứng vào chuỗi
            if (vietnameseLowercase.contains(vietnameseKey)) {
                convertedWords.add(englishValue);
            }
        }

        // Nếu không có từ tiếng Việt nào trong từ điển, giữ nguyên từ tiếng Việt
        if (convertedWords.isEmpty()) {
            result.append(vietnameseWord);
        } else {
            // Duyệt qua danh sách các từ đã được chuyển đổi
            for (String word : convertedWords) {
                // Nếu từ đó không thuộc danh sách các từ ưu tiên, thêm vào chuỗi kết quả
                if (!priorityKeywords.contains(word)) {
                    result.append(word);
                    result.append(" ");
                }
            }

            // Duyệt lại danh sách các từ đã được chuyển đổi để thêm các từ ưu tiên vào cuối chuỗi kết quả
            for (String word : convertedWords) {
                // Nếu từ đó thuộc danh sách các từ ưu tiên, thêm vào cuối chuỗi kết quả
                if (priorityKeywords.contains(word)) {
                    result.append(word);
                    result.append(" ");
                }
            }
        }

        return result.toString().trim();
    }
    private static List<String> readFirstRow(Sheet sheet) {
        List<String> rowData = new ArrayList<>();
        Row firstRow = sheet.getRow(0); // Sử dụng lớp Row từ org.apache.poi.ss.usermodel
        if (firstRow != null) {
            Iterator<Cell> cellIterator = firstRow.cellIterator();
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                rowData.add(cell.getStringCellValue());
            }
        }
        return rowData;
    }

    private void insertValueInTable(String sqlQuery){
        try {
            jdbcTemplate.execute(sqlQuery);
            System.out.println("insert data successfully");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

}
