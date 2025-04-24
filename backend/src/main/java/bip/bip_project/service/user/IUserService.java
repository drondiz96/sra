package bip.bip_project.service.user;

import bip.bip_project.model.user.User;
import bip.bip_project.model.user.UserRequestDto;

import java.util.Map;

public interface IUserService {
    // create
    UserRequestDto createUser(UserRequestDto userRequestDto);
    void saveUser(User user);

    // confirm
    void confirmEmail(String email);
    void setCreateViaGoogle(String email);

    User identicateAndAuthenticate(String email, String password);

    // read
    UserRequestDto getUserDtoById(Integer Id);
    User getUserByEmail(String email);
    UserRequestDto getUserDtoByEmail(Map<String, Object> data);

    // update
    UserRequestDto updateUser(UserRequestDto userRequestDto);

    // delete
    void deleteUserById(Integer userId);
}
