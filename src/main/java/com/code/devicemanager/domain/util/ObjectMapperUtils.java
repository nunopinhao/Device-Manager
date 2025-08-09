package com.code.devicemanager.domain.util;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.cfg.MutableConfigOverride;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@UtilityClass
public class ObjectMapperUtils {

    private static ObjectMapper mapper;

    public static <T> T readValue(File file, TypeReference<T> valueTypeRef) {
        try {
            return buildObjectMapper().readValue(file, valueTypeRef);
        } catch (IOException e) {
            throw new RuntimeException("Error reading value from file: " + file.getAbsolutePath(), e);
        }
    }
    public static String writeValueAsString(Object entity) {
        try {
            return buildObjectMapper().writeValueAsString(entity);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error writing value as string for entity: " + entity, e);
        }
    }

    public static ObjectMapper buildObjectMapper() {

        if (Objects.isNull(mapper)) {
            mapper = new ObjectMapper();

            mapper.disable(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS);
            mapper.disable(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS);
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            mapper.disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);

            mapper.enable(SerializationFeature.WRITE_DATES_WITH_ZONE_ID);

            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);

            JavaTimeModule javaTimeModule = new JavaTimeModule();
            mapper.registerModule(javaTimeModule);


            MutableConfigOverride override = mapper.configOverride(List.class);
            override.setSetterInfo(JsonSetter.Value.forValueNulls(Nulls.AS_EMPTY));
        }

        return mapper;
    }
}
