package com.code.devicemanager.domain;

import com.code.devicemanager.model.DeviceRequestDto;
import com.code.devicemanager.model.DeviceResponseDto;

public interface DeviceManagerService {

    DeviceResponseDto createDevice(DeviceRequestDto deviceRequestDto);

    DeviceResponseDto updateDevice(String deviceId, DeviceRequestDto deviceRequestDto);

    DeviceResponseDto fetchDeviceById(String deviceId);
}
