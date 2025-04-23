package bip.bip_project.service.device;

import bip.bip_project.model.device.DeviceDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeviceService implements IDeviceService {
    @Override
    public DeviceDto getDeviceById() {
        return null;
    }

    @Override
    public List<DeviceDto> getDevicesByManufacturerId(String manufacturerId) {
        return List.of();
    }

    @Override
    public List<DeviceDto> getDevicesByDeviceType(String deviceType) {
        return List.of();
    }

    @Override
    public DeviceDto getDeviceByModel(String model) {
        return null;
    }

    @Override
    public DeviceDto createDevice(DeviceDto deviceDto) {
        return null;
    }

    @Override
    public DeviceDto updateDevice(DeviceDto deviceDto) {
        return null;
    }

    @Override
    public void deleteDeviceById(Integer id) {

    }
}
