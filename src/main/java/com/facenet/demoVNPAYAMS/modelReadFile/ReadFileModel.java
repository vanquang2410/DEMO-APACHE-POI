package com.facenet.demoVNPAYAMS.modelReadFile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public class ReadFileModel {
        private List<String> header;
        private List<Object> maining;
    }
