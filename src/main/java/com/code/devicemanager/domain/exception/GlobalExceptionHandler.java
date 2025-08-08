package com.code.devicemanager.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.code.devicemanager.model.Error;


@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Error> handleIllegalArgumentException(IllegalArgumentException ex) {
        Error error = new Error(ErrorCode.INVALID_INPUT.getCode(), "Invalid input provided");
        error.setMessage(ex.getMessage());
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(error);
    }

    @ExceptionHandler(DeviceNotFoundException.class)
    public ResponseEntity<Object> handleDeviceNotFound(DeviceNotFoundException ex) {
        Error error = new Error(ErrorCode.DEVICE_NOT_FOUND.getCode(), "Device not found");
        error.setMessage(ex.getMessage());
        error.setStatus(HttpStatus.NOT_FOUND.value());
        return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(error);
    }

    @ExceptionHandler(DeviceInUseException.class)
    public ResponseEntity<Object> handleDeviceInUse(DeviceInUseException ex) {
        Error error = new Error(ErrorCode.DEVICE_IN_USE.getCode(), "Device is in use.");
        error.setMessage(ex.getMessage());
        error.setStatus(HttpStatus.CONFLICT.value());
        return ResponseEntity.status(HttpStatus.CONFLICT.value()).body(error);
    }
}