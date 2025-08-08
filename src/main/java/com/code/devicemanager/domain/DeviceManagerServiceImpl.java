package com.code.devicemanager.domain;

import com.code.devicemanager.domain.mapper.DeviceMapper;
import com.code.devicemanager.domain.model.DeviceDocument;
import com.code.devicemanager.domain.repository.DeviceManagerRepository;
import com.code.devicemanager.model.DeviceRequestDto;
import com.code.devicemanager.model.DeviceResponseDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DeviceManagerServiceImpl implements DeviceManagerService {
    private final DeviceMapper deviceMapper;
    private final DeviceManagerRepository deviceManagerRepository;

    @Override
    public DeviceResponseDto createDevice(DeviceRequestDto deviceRequestDto) {
        DeviceDocument deviceDocument = deviceMapper.deviceRequestToDocument(deviceRequestDto);
        //deviceManagerRepository.save(deviceDocument);
        return deviceMapper.deviceDocumentToResponse(deviceDocument);
    }

}
