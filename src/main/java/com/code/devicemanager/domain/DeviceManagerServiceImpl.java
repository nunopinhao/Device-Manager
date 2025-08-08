package com.code.devicemanager.domain;

import com.code.devicemanager.domain.mapper.DeviceMapper;
import com.code.devicemanager.domain.model.DeviceDocument;
import com.code.devicemanager.domain.repository.DeviceManagerRepository;
import com.code.devicemanager.domain.validators.CreateDeviceValidator;
import com.code.devicemanager.model.DeviceRequestDto;
import com.code.devicemanager.model.DeviceResponseDto;
import com.code.devicemanager.model.DeviceState;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DeviceManagerServiceImpl implements DeviceManagerService {
    private final DeviceMapper deviceMapper;
    private final DeviceManagerRepository deviceManagerRepository;
    private final CreateDeviceValidator createDeviceValidator;

    @Override
    public DeviceResponseDto createDevice(DeviceRequestDto deviceRequestDto) {
        validateDeviceRequest(deviceRequestDto);
        DeviceDocument deviceDocument = deviceMapper.deviceRequestToDocument(deviceRequestDto);
        deviceManagerRepository.save(deviceDocument);
        return deviceMapper.deviceDocumentToResponse(deviceDocument);
    }

    private void validateDeviceRequest(DeviceRequestDto deviceRequestDto) {
        createDeviceValidator.validate(deviceRequestDto);
    }


}
