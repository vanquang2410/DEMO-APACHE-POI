package com.facenet.demoVNPAYAMS.controller;

import com.facenet.demoVNPAYAMS.HeaderWriteFile;
import com.facenet.demoVNPAYAMS.service.ExcelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/api/write")
public class WriteController {
    @Autowired
    private ExcelService excelService;

    private HeaderWriteFile headerWriteFile;

    @GetMapping("/{name}")
    public String test(@PathVariable String name){
        excelService.writeFileRandom(name);
        return "ok";
    }
}
