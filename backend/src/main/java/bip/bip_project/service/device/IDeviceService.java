package bip.bip_project.service.device;

import bip.bip_project.model.device.DeviceRequestDto;

import java.util.List;


public interface IDeviceService {
    DeviceRequestDto getDeviceById();

    List<DeviceRequestDto> getDevicesByManufacturerId(String manufacturerId);

    List<DeviceRequestDto> getDevicesByDeviceType(String deviceType);

    DeviceRequestDto getDeviceByModel(String model);

    DeviceRequestDto createDevice(DeviceRequestDto deviceRequestDto);

    DeviceRequestDto updateDevice(DeviceRequestDto deviceRequestDto);

    void deleteDeviceById(Integer id);
}
