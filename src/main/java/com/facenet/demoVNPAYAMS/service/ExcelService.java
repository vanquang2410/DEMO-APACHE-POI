package com.facenet.demoVNPAYAMS.service;

import com.facenet.demoVNPAYAMS.modelWriteFile.BanVietModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Date;


@Service
@Slf4j
public class ExcelService {
    public void writeFileRandom(String name)
    {
        // Tạo workbook mới
        SXSSFWorkbook workbook = new SXSSFWorkbook(100);
        // Tạo một trang mới
        Row headerRow;
        Cell cellTitle;
        Field[] fields;
        for (int page = 1; page <= 5; page++) {
            Sheet sheet = workbook.createSheet("page " + page);

            // Lấy danh sách các trường của lớp BaoVietModel
            fields = getAllFields(BanVietModel.class);

            // Ghi tiêu đề
            headerRow = sheet.createRow(0);
            int i;
            for ( i = 0; i < fields.length; i++) {
                cellTitle = headerRow.createCell(i);
                cellTitle.setCellValue(fields[i].getName());
            }

            // Ghi dữ liệu
            for ( i = 0; i < 1000000; i++) {
                BanVietModel model = BanVietModel.createRandomBanVietModel(); // Tạo đối tượng BaoVietModel ngẫu nhiên
                Row row = sheet.createRow(i + 1);
                log.info("index:" + i);
                Cell cell;
                // Ghi dữ liệu từ đối tượng BaoVietModel vào file Excel
                int j;
                for (j = 0; j < fields.length; j++) {
                    try {
                        fields[j].setAccessible(true); // Cho phép truy cập vào trường private
                        Object value = fields[j].get(model);
                        cell = row.createCell(j);
                        if (value instanceof Date) {
                            cell.setCellValue(value.toString());
                        } else if (value instanceof Double) {
                            cell.setCellValue((Double) value);
                        } else {
                            cell.setCellValue(value.toString());
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        String pathFile = "D://java//learnCollection//" + name + ".xlsx";
        try (FileOutputStream fileOut = new FileOutputStream(pathFile, true)) {
            workbook.write(fileOut);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Đóng workbook sau khi ghi
            try {
                workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        workbook.dispose();
    }
    private Field[] getAllFields(Class<?> clazz) {
        // Lấy danh sách các trường của lớp hiện tại
        Field[] currentFields = clazz.getDeclaredFields();
        // Lấy lớp cha của lớp hiện tại
        Class<?> superclass = clazz.getSuperclass();
        // Nếu lớp cha không phải là Object, lấy danh sách các trường của lớp cha và thêm vào danh sách các trường của lớp hiện tại
        if (superclass != null && !superclass.equals(Object.class)) {
            Field[] parentFields = getAllFields(superclass);
            Field[] allFields = new Field[currentFields.length + parentFields.length];
            System.arraycopy(currentFields, 0, allFields, 0, currentFields.length);
            System.arraycopy(parentFields, 0, allFields, currentFields.length, parentFields.length);
            return allFields;
        } else {
            return currentFields;
        }
    }
}
