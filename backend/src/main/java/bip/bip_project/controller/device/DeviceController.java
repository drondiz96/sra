package bip.bip_project.controller.device;

import bip.bip_project.model.device.DeviceRequestDto;
import bip.bip_project.service.device.IDeviceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Устройства", description = "Операции с устройствами (смартфоны, ноутбуки и т.д.)")
@RestController
@RequestMapping("/devices")
public class DeviceController {
    IDeviceService deviceService;

    public DeviceController(IDeviceService deviceService) {
        this.deviceService = deviceService;
    }

}

