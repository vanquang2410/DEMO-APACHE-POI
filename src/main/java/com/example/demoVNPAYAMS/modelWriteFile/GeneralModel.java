package com.example.demoVNPAYAMS.modelWriteFile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GeneralModel {
    private LocalDateTime transactionDate ;
    private double creditAmount;
    private double debitAmount;
    private String service;



}
