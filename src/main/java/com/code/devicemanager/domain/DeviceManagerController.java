package com.code.devicemanager.domain;

import com.code.devicemanager.api.DeviceApi;
import com.code.devicemanager.model.DeviceDto;
import com.code.devicemanager.model.DeviceRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DeviceManagerController implements DeviceApi {

    @Override
    public ResponseEntity<DeviceDto> v1CreateDevice(DeviceRequestDto deviceRequestDto) {
        return null;
    }

    @Override
    public ResponseEntity<Void> v1DeleteDevice(String id) {
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<DeviceDto>> v1FetchAllDevices() {
        return null;
    }

    @Override
    public ResponseEntity<DeviceDto> v1FetchDeviceById(String id) {
        return null;
    }

    @Override
    public ResponseEntity<List<DeviceDto>> v1FetchDevicesByCategory(String category) {
        return null;
    }

    @Override
    public ResponseEntity<DeviceDto> v1UpdateDeviceById(String id, DeviceRequestDto deviceRequestDto) {
        return null;
    }
}
