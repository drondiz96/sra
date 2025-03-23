package bip.bip_project.service.user;

import bip.bip_project.model.user.User;
import bip.bip_project.model.user.UserDto;

import java.util.Map;

public interface IUserService {
    // create
    UserDto createUser(UserDto userDto);
    void saveUser(User user);

    // confirm
    void confirmEmail(String email);
    void setCreateViaGoogle(String email);

    User identicateAndAuthenticate(String email, String password);

    // read
    UserDto getUserDtoById(Integer Id);
    User getUserByEmail(String email);
    UserDto getUserDtoByEmail(Map<String, Object> data);

    // update
    UserDto updateUser(UserDto userDto);

    // delete
    void deleteUserById(Integer userId);
}
