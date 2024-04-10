package com.example.demoVNPAYAMS.modelWriteFile;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.concurrent.ThreadLocalRandom;

public interface GeneralGenerateInterface {
     enum ServiceType{
        THU_TOPUP,
        THU_CONG,
        THU_QR,
        SMS,
        BRAND_NAME,
        MOBILE_BANKING,
        XAC_THUC_CCCD,
        THU_BILL,
        THU_HO

    }
     static double generateRandomAmount() {
        // Tạo ngẫu nhiên một số tiền từ 10,000,000 đến 100,000,000 VND
        return 10000000 + ThreadLocalRandom.current().nextDouble(90000000);
    }

     static String generateRandomTransactionCode() {
        // Tạo ngẫu nhiên một mã giao dịch
        return "FT" + ThreadLocalRandom.current().nextInt(100000) + "MXW" +
                ThreadLocalRandom.current().nextInt(100) + "\\BNK";
    }
     static LocalDateTime generateRandomDate() {
        // Tạo ngẫu nhiên một ngày trong tháng 2 năm 2024
        long minDay = LocalDateTime.of(2024, Month.FEBRUARY, 1, 0, 0).atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli();
        long maxDay = LocalDateTime.of(2024, Month.FEBRUARY, 29, 0, 0).atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli();
        long randomDay = ThreadLocalRandom.current().nextLong(minDay, maxDay);
        return LocalDateTime.ofEpochSecond(randomDay / 1000, 0, java.time.ZoneOffset.UTC);
    }

     static String generateRandomDescription() {
        // Tạo ngẫu nhiên một mô tả
        return "TT CAC GD TOPUP NGAY " + generateRandomDate();
    }
     static String generateRandomTransactionType() {
        // Generate random transaction type as needed
        return "Transaction type";
    }
     static String generateRandomService() {
        // Chọn ngẫu nhiên một dịch vụ từ enum ServiceType
        ServiceType[] serviceTypes = ServiceType.values();
        int randomIndex = ThreadLocalRandom.current().nextInt(serviceTypes.length);
        return serviceTypes[randomIndex].toString();
    }
     static String generateRandomReferenceNumber() {
        return "REF" + ThreadLocalRandom.current().nextInt(1000000);
    }

}
