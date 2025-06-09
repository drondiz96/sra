package bip.bip_project.service.user;

import bip.bip_project.exception.user.UserDtoException;
import bip.bip_project.exception.user.UserNotFoundException;
import bip.bip_project.exception.user.WeakPasswordException;
import bip.bip_project.model.user.User;
import bip.bip_project.model.user.UserMapper;
import bip.bip_project.model.user.UserRequestDto;
import bip.bip_project.model.user.UserResponseDto;
import bip.bip_project.repository.user.IUserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService implements IUserService{
    IUserRepository userRepository;
    UserMapper userMapper;

    public UserService(IUserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public UserResponseDto createUserViaGoogle(UserRequestDto userRequestDto) {
        User user = new User();
        BeanUtils.copyProperties(userRequestDto, user, new String[] {"id"});
        userRepository.save(user);
        return userMapper.toDto(user);
    }

    @Override
    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        String password = userRequestDto.getPassword();

        if ((password == null || !isPasswordStrong(password))) {
            throw new WeakPasswordException("Password must be at least 6 characters long and contain both letters and digits");
        }

        User user = new User();
        BeanUtils.copyProperties(userRequestDto, user, new String[] {"id"});
        userRepository.save(user);
        return userMapper.toDto(user);
    }

    private boolean isPasswordStrong(String password) {
        if (password.length() < 6) return false;

        boolean hasLetter = false;
        boolean hasDigit = false;

        for (char c : password.toCharArray()) {
            if (Character.isLetter(c)) hasLetter = true;
            if (Character.isDigit(c)) hasDigit = true;
            if (hasLetter && hasDigit) return true;
        }

        return false;
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
    public UserResponseDto getUserDtoById(Integer id) {
        User user = getUserById(id);
//        UserRequestDto userRequestDto = new UserRequestDto();
//        BeanUtils.copyProperties(user, userRequestDto);
        return userMapper.toDto(user);
    }

    @Override
    public UserResponseDto getUserDtoByEmail(Map<String, Object> data) {
        String email = data.get("email").toString();
        User user = getUserByEmail(email);
        return userMapper.toDto(user);
    }

    @Override
    @Secured("ROLE_ADMIN")
    public List<UserResponseDto> getAllUsers() {
        List<UserResponseDto> usersDto = new ArrayList<>();
        for (User user : userRepository.findAll()){
            usersDto.add(userMapper.toDto(user));
        }
        return usersDto;
    }

    @Override
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public UserResponseDto updateUser(UserRequestDto userRequestDto) {
        if (userRequestDto.getId() == null) {
            throw new UserDtoException("Field 'id' is null");
        }
        User user = getUserById(userRequestDto.getId());

        String currentPassword = user.getPassword();

        BeanUtils.copyProperties(userRequestDto, user, new String[] {"id", "email"});
        userRepository.save(user);

        disablePasswordExpiredIfChanged(user, currentPassword, user.getPassword()); // Снимаем флаг если юзер обновил пароль
        return userMapper.toDto(user);
    }

    void disablePasswordExpiredIfChanged(User user, String odlPassword, String newPassword){
        if (!odlPassword.equals(newPassword)){
            user.setPasswordExpired(false);
        }
        userRepository.save(user);
    }

    @Override
    @Secured("ROLE_ADMIN")
    public void setPasswordExpiredFlag(String email, boolean expired) {
        User user = getUserByEmail(email);
        user.setPasswordExpired(expired);
        userRepository.save(user);
    }


    @Override
    @Secured("ROLE_ADMIN")
    public void deleteUserById(Integer userId) {
        if(userRepository.findById(userId).isEmpty())
            throw new UserNotFoundException("Not found user with such Id");
        userRepository.deleteById(userId);
    }

    @Secured("ROLE_ADMIN")
    public void lockUser(String email) {
        User user = getUserByEmail(email);
        user.setAccountLocked(true);
        userRepository.save(user);
    }

    @Secured("ROLE_ADMIN")
    public void unlockUser(String email) {
        User user = getUserByEmail(email);
        user.setAccountLocked(false);
        userRepository.save(user);
    }

}
