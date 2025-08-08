package com.code.devicemanager.domain.mapper;

import com.code.devicemanager.domain.model.DeviceDocument;
import com.code.devicemanager.model.DeviceRequestDto;
import com.code.devicemanager.model.DeviceResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.AfterMapping;

import java.time.LocalDateTime;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface DeviceMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "creationTime", ignore = true)
    DeviceDocument deviceRequestToDocument(DeviceRequestDto deviceRequestDto);

    @AfterMapping
    default void setIdAndCreationTime(@MappingTarget DeviceDocument deviceDocument) {
        deviceDocument.setId(UUID.randomUUID().toString());
        deviceDocument.setCreationTime(LocalDateTime.now());
    }

    DeviceResponseDto deviceDocumentToResponse(DeviceDocument deviceDocument);
}
