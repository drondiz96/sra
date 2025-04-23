package bip.bip_project.service.device;

import bip.bip_project.model.device.Device;
import bip.bip_project.model.device.DeviceDto;
import org.springframework.stereotype.Service;

import java.util.List;


public interface IDeviceService {
    DeviceDto getDeviceById();

    List<DeviceDto> getDevicesByManufacturerId(String manufacturerId);

    List<DeviceDto> getDevicesByDeviceType(String deviceType);

    DeviceDto getDeviceByModel(String model);

    DeviceDto createDevice(DeviceDto deviceDto);

    DeviceDto updateDevice(DeviceDto deviceDto);

    void deleteDeviceById(Integer id);
}
