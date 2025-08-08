package com.code.devicemanager.domain.validators;

import com.code.devicemanager.model.DeviceRequestDto;
import com.code.devicemanager.model.DeviceState;
import org.springframework.stereotype.Component;

@Component
public class CreateDeviceValidator {

    public void validate(DeviceRequestDto deviceRequestDto) {
        if (deviceRequestDto.getName() == null || deviceRequestDto.getName().isEmpty()) {
            throw new IllegalArgumentException("Name must not be empty");
        }
        if (deviceRequestDto.getBrand() == null || deviceRequestDto.getBrand().isEmpty()) {
            throw new IllegalArgumentException("Brand must not be empty");
        }
        if (deviceRequestDto.getState() == null || deviceRequestDto.getState().isEmpty()) {
            //if ommited, set to AVAILABLE
            deviceRequestDto.setState(DeviceState.AVAILABLE.getValue());
        }
        try {
            DeviceState.fromValue(deviceRequestDto.getState());
        } catch (IllegalArgumentException ex) {
            String possibleValues = String.join(", ",
                    java.util.Arrays.stream(DeviceState.values())
                            .map(DeviceState::getValue)
                            .toArray(String[]::new)
            );
            throw new IllegalArgumentException(
                    "Invalid state: " + deviceRequestDto.getState() +
                            ". Possible values are: " + possibleValues
            );
        }
    }
}
