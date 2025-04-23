package bip.bip_project.service.user;

import bip.bip_project.exception.user.UserDtoException;
import bip.bip_project.exception.user.UserNotFoundException;
import bip.bip_project.model.user.User;
import bip.bip_project.model.user.UserDto;
import bip.bip_project.repository.user.IUserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class UserService implements IUserService{
    IUserRepository userRepository;

    public UserService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = new User();
        BeanUtils.copyProperties(userDto, user, new String[] {"id"});
        userRepository.save(user);
        return getUserDtoById(user.getId());
    }

    @Override
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public void saveUser(User user) {
        userRepository.save(user);
    }

    @Override
    public void confirmEmail(String email) {
        User user = getUserByEmail(email);
        user.setEmailIsConfirmed(true);
        userRepository.save(user);
    }

    @Override
    public void setCreateViaGoogle(String email) {
        User user = getUserByEmail(email);
        user.setCreatedViaGoogle(Boolean.TRUE);
        userRepository.save(user);
    }

    @Override
    public User identicateAndAuthenticate(String email, String password) {
        User user = getUserByEmail(email);
        if (user.getPassword().equals(password)){
            return user;
        }
        throw new UserNotFoundException("identicate or authenticate failure: email or password incorrect");
    }

    public User getUserById(Integer userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()){
            throw new UserNotFoundException("User not found with such id");
        }
        return user.get();
    }

    @Override
    public User getUserByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()){
            throw new UserNotFoundException("User not found with such email");
        }
        return user.get();
    }

    @Override
    public UserDto getUserDtoById(Integer id) {
        User user = getUserById(id);
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(user, userDto);
        return userDto;
    }

    @Override
    public UserDto getUserDtoByEmail(Map<String, Object> data) {
        String email = data.get("email").toString();
        return userRepository.findUserDtoByEmail(email);
    }

    @Override
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public UserDto updateUser(UserDto userDto) {
        if (userDto.getId() == null) {
            throw new UserDtoException("Field 'id' is null");
        }
        User user = getUserById(userDto.getId());
        BeanUtils.copyProperties(userDto, user, new String[] {"id"});
        userRepository.save(user);
        return getUserDtoById(user.getId());
    }

    @Override
    @Secured("ROLE_ADMIN")
    public void deleteUserById(Integer userId) {
        if(userRepository.findById(userId).isEmpty())
            throw new UserNotFoundException("Not found user with such Id");
        userRepository.deleteById(userId);
    }
}
