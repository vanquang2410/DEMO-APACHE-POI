package com.example.demoVNPAYAMS.controller;

import com.example.demoVNPAYAMS.service.ReadService;
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
    public String readFileExcel() throws FileNotFoundException {
        String fileName ="C://Users//Admin//Downloads//Telegram Desktop//Vietcombank.xlsx";
        return readService.readFile(fileName);
    }
}
