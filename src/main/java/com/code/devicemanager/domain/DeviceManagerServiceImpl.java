package com.code.devicemanager.domain;

import com.code.devicemanager.domain.exception.DeviceInUseException;
import com.code.devicemanager.domain.exception.DeviceNotFoundException;
import com.code.devicemanager.domain.mapper.DeviceMapper;
import com.code.devicemanager.domain.model.DeviceDocument;
import com.code.devicemanager.domain.repository.DeviceManagerRepository;
import com.code.devicemanager.domain.validators.DeviceValidator;
import com.code.devicemanager.model.DeviceRequestDto;
import com.code.devicemanager.model.DeviceResponseDto;
import com.code.devicemanager.model.DeviceState;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
            deviceDocument.setState(DeviceState.fromValue(deviceRequestDto.getState()));
        }

        deviceManagerRepository.save(deviceDocument);
        return deviceMapper.deviceDocumentToResponse(deviceDocument);
    }

    @Override
    public DeviceResponseDto fetchDeviceById(String deviceId) {
        return deviceMapper.deviceDocumentToResponse(getDeviceDocumentById(deviceId));
    }

    @Override
    public List<DeviceResponseDto> fetchDevices(String brand, String state) {
        DeviceState deviceState = convertState(state);
        List<DeviceDocument> devices;
        if (brand != null && state != null) {
            devices = deviceManagerRepository.findByBrandAndState(brand, deviceState);
        } else if (brand != null) {
            devices = deviceManagerRepository.findByBrand(brand);
        } else if (state != null) {
            devices = deviceManagerRepository.findByState(deviceState);
        } else {
            devices = deviceManagerRepository.findAll();
        }
        return devices.stream()
                .map(deviceMapper::deviceDocumentToResponse)
                .toList();
    }

    @Override
    public void deleteDevice(String deviceId) {
        DeviceDocument deviceDocument = getDeviceDocumentById(deviceId);
        if (DeviceState.IN_USE.equals(deviceDocument.getState())) {
            throw new DeviceInUseException("Cannot delete device that is currently in use");
        }
        deviceManagerRepository.delete(deviceDocument);
    }

    private static DeviceState convertState(String state) {
        if (state != null) {
            try {
                return DeviceState.fromValue(state);
            } catch (IllegalArgumentException ex) {
                String possibleValues = String.join(", ",
                        java.util.Arrays.stream(DeviceState.values())
                                .map(DeviceState::getValue)
                                .toArray(String[]::new)
                );
                throw new IllegalArgumentException(
                        "Invalid state: " + state +
                                ". Possible values are: " + possibleValues
                );
            }
        }
        return null;
    }

    private DeviceDocument getDeviceDocumentById(String deviceId) {
        return deviceManagerRepository.findById(deviceId)
                .orElseThrow(() -> new DeviceNotFoundException("Device not found with id: " + deviceId));
    }

    private void validateUpdateNotAllowedWhenInUse(DeviceDocument deviceDocument, DeviceRequestDto deviceRequestDto) {
        if (DeviceState.IN_USE.equals(deviceDocument.getState())) {
            if ((deviceRequestDto.getBrand() != null && !deviceRequestDto.getBrand().equals(deviceDocument.getBrand())) ||
                    (deviceRequestDto.getName() != null && !deviceRequestDto.getName().equals(deviceDocument.getName()))) {
                throw new DeviceInUseException("Cannot update brand or name when device state is in use.");
            }
        }
    }


}
