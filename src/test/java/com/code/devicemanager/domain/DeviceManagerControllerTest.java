package com.code.devicemanager.domain;

import com.code.devicemanager.DeviceManagerApplication;
import com.code.devicemanager.domain.util.ObjectMapperUtils;
import com.code.devicemanager.model.DeviceRequestDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.ResourceUtils;

import java.io.File;

@ActiveProfiles("test")
@SpringBootTest(classes = DeviceManagerApplication.class)
@AutoConfigureMockMvc
class DeviceManagerControllerTest {
    @Autowired
    private MockMvc mockMvc;

    private static final String AVAILABLE = "available";
    private static final String IN_USE = "in-use";
    private static final String INACTIVE = "inactive";
    private static final String SAMSUNG = "Samsung";
    private static final String APPLE = "Apple";
    private static final String GALAXY_S21 = "Galaxy S21";
    private static final String GALAXY_S21_ULTRA = "Galaxy S21 Ultra";
    private static final String IPHONE_13 = "iPhone 13";


    DeviceManagerControllerTest() throws Exception {
    }

    @Test
    void deviceManagerController_integration() throws Exception {
        String id = createDeviceAndReturnId(AVAILABLE, SAMSUNG, GALAXY_S21);
        assertDeviceDetails(id, AVAILABLE, SAMSUNG, GALAXY_S21);

        updateDeviceState(id, IN_USE, 200);
        updateDeviceNameWhileInUse(id, GALAXY_S21_ULTRA, 409);

        deleteDeviceWhileInUse(id, 409);

        assertDeviceListSize(null, null, 1);
        assertDeviceListSize(APPLE, null, 0);
        assertDeviceListSize(SAMSUNG, null, 1);
        assertDeviceListSize(null, AVAILABLE, 0);
        assertDeviceListSize(null, IN_USE, 1);

        updateDeviceState(id, INACTIVE, 200);
        updateDeviceNameAndBrand(id, IPHONE_13, APPLE, 200);

        deleteDevice(id, 204);
        assertDeviceNotFound(id);
    }

    private String createDeviceAndReturnId(String state, String brand, String name) throws Exception {
        DeviceRequestDto dto = new DeviceRequestDto();
        dto.setState(state);
        dto.setBrand(brand);
        dto.setName(name);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .post("/v1/device")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ObjectMapperUtils.writeValueAsString(dto)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();

        return extractIdFromResponse(result);
    }

    private void assertDeviceDetails(String id, String state, String brand, String name) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/device/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(id))
                .andExpect(MockMvcResultMatchers.jsonPath("$.state").value(state))
                .andExpect(MockMvcResultMatchers.jsonPath("$.brand").value(brand))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(name));
    }

    private void updateDeviceState(String id, String state, int expectedStatus) throws Exception {
        DeviceRequestDto dto = new DeviceRequestDto();
        dto.setState(state);

        mockMvc.perform(MockMvcRequestBuilders.patch("/v1/device/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ObjectMapperUtils.writeValueAsString(dto)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(expectedStatus));
    }

    private void updateDeviceNameWhileInUse(String id, String name, int expectedStatus) throws Exception {
        DeviceRequestDto dto = new DeviceRequestDto();
        dto.setName(name);

        mockMvc.perform(MockMvcRequestBuilders.patch("/v1/device/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ObjectMapperUtils.writeValueAsString(dto)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(expectedStatus));
    }

    private void deleteDeviceWhileInUse(String id, int expectedStatus) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/device/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(expectedStatus));
    }

    private void assertDeviceListSize(String brand, String state, int expectedSize) throws Exception {
        var request = MockMvcRequestBuilders.get("/v1/device")
                .contentType(MediaType.APPLICATION_JSON);
        if (brand != null) request.param("brand", brand);
        if (state != null) request.param("state", state);

        mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(expectedSize));
    }

    private void updateDeviceNameAndBrand(String id, String name, String brand, int expectedStatus) throws Exception {
        DeviceRequestDto dto = new DeviceRequestDto();
        dto.setName(name);
        dto.setBrand(brand);

        mockMvc.perform(MockMvcRequestBuilders.patch("/v1/device/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ObjectMapperUtils.writeValueAsString(dto)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(expectedStatus));
    }

    private void deleteDevice(String id, int expectedStatus) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/device/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(expectedStatus));
    }

    private void assertDeviceNotFound(String id) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/device/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    private String extractIdFromResponse(MvcResult result) throws Exception {
        String content = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(content);
        return jsonNode.get("id").asText();
    }


}
