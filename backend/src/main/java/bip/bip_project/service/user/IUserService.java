package bip.bip_project.service.user;

import bip.bip_project.model.user.User;
import bip.bip_project.model.user.UserRequestDto;
import bip.bip_project.model.user.UserResponseDto;

import java.util.List;
import java.util.Map;

public interface IUserService {
    // create
    UserResponseDto createUser(UserRequestDto userRequestDto);
    void saveUser(User user);

    // confirm
    void confirmEmail(String email);
    void setCreateViaGoogle(String email);

    User identicateAndAuthenticate(String email, String password);

    void setPasswordExpiredFlag(String email, boolean expired);

    // read
    UserResponseDto getUserDtoById(Integer Id);
    User getUserByEmail(String email);
    UserResponseDto getUserDtoByEmail(Map<String, Object> data);
    List<UserResponseDto> getAllUsers();

    // update
    UserResponseDto updateUser(UserRequestDto userRequestDto);

    // delete
    void deleteUserById(Integer userId);
}
