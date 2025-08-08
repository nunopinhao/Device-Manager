package com.code.devicemanager.domain.repository;

import com.code.devicemanager.domain.model.DeviceDocument;
import com.code.devicemanager.model.DeviceState;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeviceManagerRepository extends MongoRepository<DeviceDocument, String> {


}
