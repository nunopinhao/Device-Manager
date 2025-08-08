package com.code.devicemanager.domain;

import com.code.devicemanager.api.DeviceApi;
import com.code.devicemanager.model.DeviceRequestDto;
import com.code.devicemanager.model.DeviceResponseDto;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class DeviceManagerController implements DeviceApi {
    private final DeviceManagerService deviceManagerService;

    @Override
    public ResponseEntity<DeviceResponseDto> v1CreateDevice(DeviceRequestDto deviceRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(deviceManagerService.createDevice(deviceRequestDto));
    }

    @Override
    public ResponseEntity<Void> v1DeleteDevice(String id) {
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<DeviceResponseDto>> v1FetchAllDevices(String brand, String state) {
        return null;
    }

    @Override
    public ResponseEntity<DeviceResponseDto> v1FetchDeviceById(String id) {
        return null;
    }

    @Override
    public ResponseEntity<DeviceResponseDto> v1UpdateDeviceById(String id, DeviceRequestDto deviceRequestDto) {
        return null;
    }
}
