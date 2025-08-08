package com.code.devicemanager.domain.mapper;

import com.code.devicemanager.domain.model.DeviceDocument;
import com.code.devicemanager.model.DeviceRequestDto;
import com.code.devicemanager.model.DeviceResponseDto;
import com.code.devicemanager.model.DeviceState;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.AfterMapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Qualifier;

import java.time.LocalDateTime;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface DeviceMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "creationTime", ignore = true)
    @Mapping(target = "state", source = "state", qualifiedByName = "mapStringToDeviceState")
    DeviceDocument deviceRequestToDocument(DeviceRequestDto deviceRequestDto);

    DeviceResponseDto deviceDocumentToResponse(DeviceDocument deviceDocument);

    @AfterMapping
    default void setIdAndCreationTime(@MappingTarget DeviceDocument deviceDocument) {
        deviceDocument.setId(UUID.randomUUID().toString());
        deviceDocument.setCreationTime(LocalDateTime.now());
    }

    @Named("mapStringToDeviceState")
    default DeviceState mapStringToDeviceState(String value) {
        return DeviceState.fromValue(value);
    }

}


