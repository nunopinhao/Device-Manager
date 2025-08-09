package com.code.devicemanager.domain;

import com.code.devicemanager.domain.exception.DeviceInUseException;
import com.code.devicemanager.domain.mapper.DeviceMapper;
import com.code.devicemanager.domain.model.DeviceDocument;
import com.code.devicemanager.domain.repository.DeviceManagerRepository;
import com.code.devicemanager.domain.util.ObjectMapperUtils;
import com.code.devicemanager.domain.validators.DeviceValidator;
import com.code.devicemanager.model.DeviceRequestDto;
import com.code.devicemanager.model.DeviceResponseDto;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class DeviceManagerServiceImplTest {
    @InjectMocks
    private DeviceManagerServiceImpl deviceManagerService;
    @Mock
    private DeviceMapper deviceMapper;
    @Mock
    private  DeviceManagerRepository deviceManagerRepository;
    @Mock
    private  DeviceValidator validator;

    private final File deviceRequestFile = ResourceUtils.getFile(
            "classpath:deviceRequest.json");
    private final File deviceDocumentResponseFile = ResourceUtils.getFile(
            "classpath:deviceDocumentResponse.json");
    private final File deviceDocumentInUseFile = ResourceUtils.getFile(
            "classpath:deviceDocumentInUse.json");

    public DeviceManagerServiceImplTest() throws FileNotFoundException {
    }

    @Test
    void updateDevice_sucess() {
        // Given
        DeviceRequestDto deviceRequestDto = ObjectMapperUtils
                .readValue(deviceRequestFile, new TypeReference<>() {
                });
        DeviceDocument deviceDocument = ObjectMapperUtils
                .readValue(deviceDocumentResponseFile, new TypeReference<>() {
                });
        DeviceResponseDto deviceResponseDto = ObjectMapperUtils
                .readValue(deviceDocumentResponseFile, new TypeReference<>() {
                });

        Mockito.doNothing().when(validator).validateUpdateDevice(Mockito.any());
        Mockito.when(deviceManagerRepository.findById(Mockito.anyString())).thenReturn(Optional.of(deviceDocument));
        Mockito.when(deviceManagerRepository.save(Mockito.any())).thenReturn(deviceDocument);
        Mockito.when(deviceMapper.deviceDocumentToResponse(Mockito.any()))
                .thenReturn(deviceResponseDto);

        // When
        DeviceResponseDto updatedDevice = deviceManagerService.updateDevice("deviceId", deviceRequestDto);

        //Then
        Assertions.assertEquals(deviceResponseDto, updatedDevice);
    }

    @Test
    void updateDeviceInUse_error() {
        // Given
        DeviceRequestDto deviceRequestDto = ObjectMapperUtils
                .readValue(deviceRequestFile, new TypeReference<>() {
                });
        DeviceDocument deviceDocument = ObjectMapperUtils
                .readValue(deviceDocumentInUseFile, new TypeReference<>() {
                });

        Mockito.doNothing().when(validator).validateUpdateDevice(Mockito.any());
        Mockito.when(deviceManagerRepository.findById(Mockito.anyString())).thenReturn(Optional.of(deviceDocument));

        // When
        //Then
        Assertions.assertThrows(DeviceInUseException.class, () -> {
            deviceManagerService.updateDevice("deviceId", deviceRequestDto);
        });
    }

    @Test
    void deleteDevice_sucess() {
        // Given
        DeviceDocument deviceDocument = ObjectMapperUtils
                .readValue(deviceDocumentResponseFile, new TypeReference<>() {
                });

        Mockito.when(deviceManagerRepository.findById(Mockito.anyString())).thenReturn(Optional.of(deviceDocument));
        Mockito.doNothing().when(deviceManagerRepository).delete(Mockito.any());

        Assertions.assertDoesNotThrow(() -> {
            deviceManagerService.deleteDevice("deviceId");
        });
    }

    @Test
    void deleteDeviceInUse_error() {
        // Given
        DeviceDocument deviceDocument = ObjectMapperUtils
                .readValue(deviceDocumentInUseFile, new TypeReference<>() {
                });

        Mockito.when(deviceManagerRepository.findById(Mockito.anyString())).thenReturn(Optional.of(deviceDocument));

        Assertions.assertThrows(DeviceInUseException.class, () -> {
            deviceManagerService.deleteDevice("deviceId");
        });
    }
}
