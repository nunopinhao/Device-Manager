package com.code.devicemanager.domain.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Getter
@Setter
@Document(collection = "devices")
public class DeviceDocument {
    @Id
    private String id;
    private String name;
    private String brand;
    private String state;
    private LocalDateTime creationTime;
}
