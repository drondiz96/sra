package bip.bip_project.model.review;

import bip.bip_project.model.device.Device;
import bip.bip_project.model.device.DeviceMapper;
import bip.bip_project.model.device.DeviceRequestDto;
import bip.bip_project.model.device.DeviceResponseDto;
import bip.bip_project.model.user.User;
import bip.bip_project.model.user.UserMapper;
import bip.bip_project.model.user.UserRequestDto;
import bip.bip_project.model.user.UserResponseDto;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {DeviceMapper.class, UserMapper.class})
public interface ReviewMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dateOfChanged", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "device", source = "device")
    Review toEntity(ReviewRequestDto reviewRequestDto);

    @Mapping(source = "device", target = "device")
    @Mapping(source = "author", target = "author")
    ReviewResponseDto toDto(Review review);

//    Device toEntity(DeviceRequestDto deviceRequestDto);
//    DeviceResponseDto toDto(Device device);

//    User toEntity(UserRequestDto userRequestDto);
//    UserResponseDto toDto(User user);

    // обновление
    // Благодаря BeanMapping, null в reviewRequestDto не перезапишет значение в entity
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dateOfChanged", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "device", ignore = true)
    void updateReviewFromDto(ReviewRequestDto reviewRequestDto, @MappingTarget Review review);
}
