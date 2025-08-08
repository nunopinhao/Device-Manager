package com.code.devicemanager.domain.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    INVALID_INPUT("DM-001")
    ;

    private final String code;

    ErrorCode(String code) {
        this.code = code;
    }

}