package bip.bip_project.repository.device;

import bip.bip_project.model.device.Device;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IDeviceRepository extends JpaRepository<Device, Integer> {
}
