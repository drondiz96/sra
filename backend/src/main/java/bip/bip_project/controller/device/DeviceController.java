package bip.bip_project.controller.device;

import bip.bip_project.model.device.DeviceDto;
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

    @Operation(summary = "Получить устройства по производителю",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Устройства найдены")
            })
    @GetMapping("/{manufacturer}")
    ResponseEntity<List<DeviceDto>> getDevicesByManufacturer(@PathVariable("manufacturer") String manufacturer){
        return ResponseEntity.ok(deviceService.getDevicesByManufacturerId(manufacturer));
    }

    @Operation(summary = "Получить устройства по типу",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Устройства найдены")
            })
    @GetMapping("/{deviceType}")
    ResponseEntity<List<DeviceDto>> getDevicesByDeviceType(@PathVariable("deviceType") String deviceType){
        return ResponseEntity.ok(deviceService.getDevicesByDeviceType(deviceType));
    }

    @Operation(summary = "Получить устройство по модели",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Устройство найдено"),
                    @ApiResponse(responseCode = "404", description = "Устройство не найдено")
            })
    @GetMapping("/{model}")
    ResponseEntity<DeviceDto> getDeviceByModel(@PathVariable("model") String model){
        return ResponseEntity.ok(deviceService.getDeviceByModel(model));
    }

    @Operation(summary = "Создать устройство",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Устройство создано")
            })
    @PostMapping("/create")
    ResponseEntity<DeviceDto> createDevice(@RequestBody DeviceDto deviceDto){
        return  ResponseEntity.ok(deviceService.createDevice(deviceDto));
    }

    @Operation(summary = "Обновить устройство",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Устройство обновлено")
            })
    @PutMapping("/update")
    ResponseEntity<DeviceDto> updateDevice(@RequestBody DeviceDto deviceDto){
        return  ResponseEntity.ok(deviceService.updateDevice(deviceDto));
    }

    @Operation(summary = "Удалить устройство по ID",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Устройство удалено")
            })
    @DeleteMapping("/delete/{deviceId}")
    void deleteDeviceById (@PathVariable("deviceId") Integer deviceId){
        deviceService.deleteDeviceById(deviceId);
    }
}

