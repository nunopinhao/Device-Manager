package com.code.devicemanager.domain;

import com.code.devicemanager.domain.repository.DeviceManagerRepository;
import com.code.devicemanager.model.DeviceRequestDto;
import com.code.devicemanager.model.DeviceResponseDto;

public interface DeviceManagerService {

    DeviceResponseDto createDevice(DeviceRequestDto deviceRequestDto);


}
