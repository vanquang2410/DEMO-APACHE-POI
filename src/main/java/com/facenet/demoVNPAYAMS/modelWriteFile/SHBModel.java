package com.facenet.demoVNPAYAMS.modelWriteFile;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

@Getter
@Setter
@NoArgsConstructor
public class SHBModel extends GeneralModel {
  private String referenceNumber;
  private LocalDateTime accountingDay;
  private String description;

  public SHBModel(LocalDateTime transactionDate, double creditAmount, double debitAmount, String service,
                  String referenceNumber, LocalDateTime accountingDay, String description) {
    super(transactionDate, creditAmount, debitAmount, service);
    this.referenceNumber = referenceNumber;
    this.accountingDay = accountingDay;
    this.description = description;
  }

  public static SHBModel createRandomSHBModel() {
    LocalDateTime transactionDate = GeneralGenerateInterface.generateRandomDate();
    double creditAmount = GeneralGenerateInterface.generateRandomAmount();
    ;
    double debitAmount = GeneralGenerateInterface.generateRandomAmount();
    ;
    String service = GeneralGenerateInterface.generateRandomService();// You may set a default service here or generate randomly as well
    String referenceNumber = GeneralGenerateInterface.generateRandomReferenceNumber();
    LocalDateTime accountingDay = generateRandomDateTime();
    String description = generateRandomDescription();

    return new SHBModel(
          transactionDate, creditAmount, debitAmount, service,
          referenceNumber, accountingDay, description);
  }

  // Helper methods for generating random values
  private static LocalDateTime generateRandomDateTime() {
    long minDay = LocalDateTime.of(2024, 2, 1, 0, 0).atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli();
    long maxDay = LocalDateTime.of(2024, 2, 29, 23, 59).atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli();
    long randomDay = ThreadLocalRandom.current().nextLong(minDay, maxDay);
    return LocalDateTime.ofEpochSecond(randomDay / 1000, 0, java.time.ZoneOffset.UTC);
  }

  private static String generateRandomDescription() {
    // Generate random description as needed
    return "TT CAC GD TOPUP NGAY " + generateRandomDateTime();
  }

}
