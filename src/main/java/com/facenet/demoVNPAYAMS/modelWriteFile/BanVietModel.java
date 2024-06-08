package com.facenet.demoVNPAYAMS.modelWriteFile;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class BanVietModel extends  GeneralModel{
    private String transactionCode ;
    private String transactionType;
    private String description;


    public BanVietModel(LocalDateTime transactionDate, double creditAmount, double debitAmount, String service,
                        String transactionCode, String transactionType, String description) {
        super(transactionDate, creditAmount, debitAmount, service);
        this.transactionCode = transactionCode;
        this.transactionType = transactionType;
        this.description = description;
    }

    public static BanVietModel createRandomBanVietModel() {
        LocalDateTime transactionDate = GeneralGenerateInterface.generateRandomDate();
        double creditAmount = GeneralGenerateInterface.generateRandomAmount();
        double debitAmount = GeneralGenerateInterface.generateRandomAmount();
        String service = GeneralGenerateInterface.generateRandomService(); // You may set a default service here or generate randomly as well
        String transactionCode = GeneralGenerateInterface.generateRandomTransactionCode();
        String transactionType = GeneralGenerateInterface.generateRandomTransactionType();
        String description = GeneralGenerateInterface.generateRandomDescription();

        return new BanVietModel(transactionDate, creditAmount, debitAmount, service,
                transactionCode, transactionType, description);
    }

}
