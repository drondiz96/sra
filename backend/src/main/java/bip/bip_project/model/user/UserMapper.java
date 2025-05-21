package bip.bip_project.model.user;

import bip.bip_project.model.device.Device;
import bip.bip_project.model.device.DeviceRequestDto;
import bip.bip_project.model.device.DeviceResponseDto;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    User toEntity(UserRequestDto dto);

    UserResponseDto toDto(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    void updateDeviceFromDto(UserRequestDto dto, @MappingTarget User user);
}
