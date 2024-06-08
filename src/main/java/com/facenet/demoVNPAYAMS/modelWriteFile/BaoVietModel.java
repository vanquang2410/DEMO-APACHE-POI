package com.facenet.demoVNPAYAMS.modelWriteFile;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class BaoVietModel extends  GeneralModel{
        private LocalDateTime valueDate;
        private String transactionCode ;
        private String description;

        public BaoVietModel(LocalDateTime transactionDate, double creditAmount, double debitAmount,  String service,
                            LocalDateTime valueDate, String transactionCode, String description) {
            super(transactionDate, creditAmount, debitAmount, service); // Gọi constructor của lớp cha
            this.valueDate = valueDate;
            this.transactionCode = transactionCode;
            this.description = description;
        }

    public static BaoVietModel createRandomBaoVietModel() {


        // Tạo ngẫu nhiên các giá trị
        LocalDateTime transactionDate = GeneralGenerateInterface.generateRandomDate();
        double creditAmount = GeneralGenerateInterface.generateRandomAmount();
        double debitAmount = GeneralGenerateInterface.generateRandomAmount();
        LocalDateTime valueDate = GeneralGenerateInterface.generateRandomDate();
        String transactionCode = GeneralGenerateInterface.generateRandomTransactionCode();
        String description = GeneralGenerateInterface.generateRandomDescription();
        String service = GeneralGenerateInterface.generateRandomService();

        // Tạo và trả về đối tượng BaoVietModel với các giá trị ngẫu nhiên
        return new BaoVietModel(transactionDate, creditAmount, debitAmount,service ,valueDate, transactionCode, description);
    }



}
