package com.code.devicemanager.domain;

import com.code.devicemanager.model.DeviceRequestDto;
import com.code.devicemanager.model.DeviceResponseDto;

import java.util.List;

public interface DeviceManagerService {

    DeviceResponseDto createDevice(DeviceRequestDto deviceRequestDto);

    DeviceResponseDto updateDevice(String deviceId, DeviceRequestDto deviceRequestDto);

    DeviceResponseDto fetchDeviceById(String deviceId);

    List<DeviceResponseDto> fetchDevices(String brand, String state);

    void deleteDevice(String deviceId);
}
