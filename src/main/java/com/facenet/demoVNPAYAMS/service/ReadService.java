package com.facenet.demoVNPAYAMS.service;

import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.xml.transform.Templates;
import java.io.*;
import java.util.*;

@Service
public class ReadService {
    @Autowired
    private JdbcTemplate jdbcTemplate;


    public String readFile(String fileName) {
        Map<String, List<String>> selectedColumnsByBank = new HashMap<>();

        try (InputStream inputStream = new FileInputStream(new File(fileName))) {
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(2); // Lấy sheet đầu tiên, có thể thay đổi tùy ý

            // Lấy hàng đầu tiên để xác định vị trí của các cột được chọn
            List<String> header = getHeader(sheet);

            selectedColumnsByBank = readRows(sheet, header.size(), header);
            List <String>sqlQuery=generateCreateTableSQL(selectedColumnsByBank);
            System.out.println(sqlQuery);
            executeSql(sqlQuery);
            workbook.close();
            return "ok";

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);

        } catch (IOException e) {
            throw new RuntimeException(e);

        }
    }

    private Map<String, List<String>> readRows(Sheet sheet, int numberOfColumns, List<String> header) {
        Map<String, List<String>> selectedColumnsByBank = new HashMap<>();
        // Duyệt qua từng dòng bắt đầu từ dòng thứ hai
        Row row;
        for (int i = 2; i <= sheet.getLastRowNum(); i++) {
            row = sheet.getRow(i);


            String fieldName="";
            String name=getCellValue(1,row);
            String type=getCellValue(2,row);
            String maxLength=getCellValue(4,row);
            String isNull=getCellValue(5,row);
            fieldName+=name+" ";
            fieldName+=type;
            if(!maxLength.isEmpty()){
                fieldName+="("+maxLength+")"+"";
            }



            // Duyệt qua từng cột và đọc dữ liệu
            for (int j = 7; j < numberOfColumns; j++) {
                // Lấy giá trị của cột từ cột thứ 2 trở đi
                Cell cell = row.getCell(j);
                if (cell != null && cell.getCellType() == CellType.STRING) {
                    String value = cell.getStringCellValue();
                    if (value.equals("x")) {
                        if (!selectedColumnsByBank.containsKey(header.get(j))) {
                            List<String> field = new ArrayList<>();
                            field.add(fieldName);
                            selectedColumnsByBank.put(header.get(j).toString(), field);
                        } else {
                            List<String> field = selectedColumnsByBank.get(header.get(j));
                            field.add(fieldName);
                            selectedColumnsByBank.put(header.get(j), field);
                        }
                    }
                }
            }
        }
        return selectedColumnsByBank;
    }
    private String getCellValue(int index,Row row){
        Cell fieldCell = row.getCell(index);
        if (fieldCell != null && fieldCell.getCellType() == CellType.STRING) {
            return  fieldCell.getStringCellValue()+" ";
        }
        else if(fieldCell != null && fieldCell.getCellType() == CellType.NUMERIC){
            int maxlength= (int) fieldCell.getNumericCellValue();
            return maxlength+" ";
        }
        else return "";
    }
    public static List<String> generateCreateTableSQL(Map<String, List<String>> tableInfo) {
        StringBuilder sqlBuilder;
        List<String>listQuery=new ArrayList<>();
        for (Map.Entry<String, List<String>> entry : tableInfo.entrySet()) {
            sqlBuilder = new StringBuilder();
            String tableName = entry.getKey();
            List<String> columns = entry.getValue();

            sqlBuilder.append("CREATE TABLE IF NOT EXISTS ").append(tableName).append(" (");

            for (String column : columns) {
                sqlBuilder.append(column).append(", ");
            }

            // Loại bỏ dấu ',' cuối cùng và thêm dấu đóng ngoặc
            sqlBuilder.setLength(sqlBuilder.length() - 2);
            sqlBuilder.append(");");
            listQuery.add(sqlBuilder.toString());
        }

        return listQuery;
    }
    private void executeSql(List<String> sqlQuery){


        System.out.println(sqlQuery.toString());
        for (String sql : sqlQuery) {
            try {
                jdbcTemplate.execute(sql);
            } catch (DataAccessException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * ........
     * @param sheet
     * @return
     */
    private List<String> getHeader(Sheet sheet) {
        Row row = sheet.getRow(0);
        List<String> columnNames = new ArrayList<>();
        Iterator<Cell> iterator;
        for (iterator = row.cellIterator(); iterator.hasNext(); ) {
            Cell cell = iterator.next();
            String value = cell.getStringCellValue();
            columnNames.add(value);
        }
        return columnNames;
    }
}
