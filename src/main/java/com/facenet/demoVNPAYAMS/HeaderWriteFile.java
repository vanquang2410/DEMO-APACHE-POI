package com.facenet.demoVNPAYAMS;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public interface HeaderWriteFile {
    List<String>headerBaoVietBank= Arrays.asList(
            "Transaction Date",
            "Value Date",
            "Credit Amount",
            "Debit Amount",
            "Transaction Code",
            "Explain",
            "Service"
    );
    List <String>headerSHB = Arrays.asList(
            "Reference Number",
            "Transaction Date",
            "Accounting Date",
            "Debit Amount",
            "Credit Amount"
    );
}
