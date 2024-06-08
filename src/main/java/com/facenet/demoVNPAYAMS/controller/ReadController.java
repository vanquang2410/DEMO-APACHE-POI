package com.facenet.demoVNPAYAMS.controller;

import com.facenet.demoVNPAYAMS.service.ReadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;

@RestController
@RequestMapping("/api/read")
public class ReadController {
    @Autowired
    private ReadService readService;

    @GetMapping("")
    String readFileExcel() throws FileNotFoundException {
        String fileName ="C://Users//Admin//Documents//Cấu hình file excel.xlsx";
        return readService.readFile(fileName);
    }
}
