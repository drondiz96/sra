package bip.bip_project.service.device;

import bip.bip_project.model.device.DeviceRequestDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeviceService implements IDeviceService {
    @Override
    public DeviceRequestDto getDeviceById() {
        return null;
    }

    @Override
    public List<DeviceRequestDto> getDevicesByManufacturerId(String manufacturerId) {
        return List.of();
    }

    @Override
    public List<DeviceRequestDto> getDevicesByDeviceType(String deviceType) {
        return List.of();
    }

    @Override
    public DeviceRequestDto getDeviceByModel(String model) {
        return null;
    }

    @Override
    public DeviceRequestDto createDevice(DeviceRequestDto deviceRequestDto) {
        return null;
    }

    @Override
    public DeviceRequestDto updateDevice(DeviceRequestDto deviceRequestDto) {
        return null;
    }

    @Override
    public void deleteDeviceById(Integer id) {

    }
}
