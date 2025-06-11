package bip.bip_project.model.device;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface DeviceMapper {

    Device toEntity(DeviceRequestDto dto);
    DeviceResponseDto toDto(Device device);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    // этих полей действительно нет в DeviceRequestDto, однако это подстраховка на случай, если они появятся
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "review", ignore = true)
    void updateDeviceFromDto(DeviceRequestDto dto, @MappingTarget Device entity);
}
