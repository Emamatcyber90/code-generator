package com.zbss.code.generator.file;

public enum  FileTypeEnum {
    MODEL(1),
    MAPPER(2),
    XML(3),
    SERVICE(4),
    SERVICEIMPL(5),
    CONTROLLER(6),
    ;

    int val;
    FileTypeEnum(int value) {
        val = value;
    }
}
