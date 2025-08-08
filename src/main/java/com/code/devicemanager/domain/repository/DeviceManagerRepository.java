package com.code.devicemanager.domain.repository;

import com.code.devicemanager.domain.model.DeviceDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DeviceManagerRepository extends MongoRepository<DeviceDocument, String> {

}
