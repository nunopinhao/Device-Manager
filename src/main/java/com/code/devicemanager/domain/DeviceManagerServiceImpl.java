package com.code.devicemanager.domain;

import com.code.devicemanager.domain.mapper.DeviceMapper;
import com.code.devicemanager.domain.model.DeviceDocument;
import com.code.devicemanager.domain.repository.DeviceManagerRepository;
import com.code.devicemanager.domain.validators.DeviceValidator;
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
    private final DeviceValidator validator;

    @Override
    public DeviceResponseDto createDevice(DeviceRequestDto deviceRequestDto) {
        validator.validateCreateDevice(deviceRequestDto);
        DeviceDocument deviceDocument = deviceMapper.deviceRequestToDocument(deviceRequestDto);
        deviceManagerRepository.save(deviceDocument);
        return deviceMapper.deviceDocumentToResponse(deviceDocument);
    }

    @Override
    public DeviceResponseDto updateDevice(String deviceId, DeviceRequestDto deviceRequestDto) {
        validator.validateUpdateDevice(deviceRequestDto);
        DeviceDocument deviceDocument = getDeviceDocumentById(deviceId);

        validateUpdateNotAllowedWhenInUse(deviceDocument, deviceRequestDto);

        // Update allowed fields
        if (deviceRequestDto.getBrand() != null) {
            deviceDocument.setBrand(deviceRequestDto.getBrand());
        }
        if (deviceRequestDto.getName() != null) {
            deviceDocument.setName(deviceRequestDto.getName());
        }
        if (deviceRequestDto.getState() != null) {
            deviceDocument.setState(deviceRequestDto.getState());
        }

        deviceManagerRepository.save(deviceDocument);
        return deviceMapper.deviceDocumentToResponse(deviceDocument);
    }



    @Override
    public DeviceResponseDto fetchDeviceById(String deviceId) {
        return deviceMapper.deviceDocumentToResponse(getDeviceDocumentById(deviceId));
    }

    private DeviceDocument getDeviceDocumentById(String deviceId) {
        return deviceManagerRepository.findById(deviceId)
                .orElseThrow(() -> new IllegalArgumentException("Device not found with id: " + deviceId));
    }

    private void validateUpdateNotAllowedWhenInUse(DeviceDocument deviceDocument, DeviceRequestDto deviceRequestDto) {
        if (DeviceState.IN_USE.getValue().equalsIgnoreCase(deviceDocument.getState())) {
            if ((deviceRequestDto.getBrand() != null && !deviceRequestDto.getBrand().equals(deviceDocument.getBrand())) ||
                    (deviceRequestDto.getName() != null && !deviceRequestDto.getName().equals(deviceDocument.getName()))) {
                throw new IllegalArgumentException("Cannot update brand or name when device state is IN_USE");
            }
        }
    }


}
