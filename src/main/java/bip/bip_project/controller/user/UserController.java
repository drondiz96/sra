package bip.bip_project.controller.user;

import bip.bip_project.model.authenticate.AuthenticationRequest;
import bip.bip_project.model.authenticate.AuthenticationResponse;
import bip.bip_project.model.authenticate.Verify2faRequest;
import bip.bip_project.model.user.User;
import bip.bip_project.model.user.UserDto;
import bip.bip_project.security.JwtUtil;
import bip.bip_project.service.user.IUserService;
import bip.bip_project.service.user.TwoFactorAuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    IUserService userService;
    JwtUtil jwtUtil;
    TwoFactorAuthService twoFactorAuthService;

    public UserController(IUserService userService, JwtUtil jwtUtil, TwoFactorAuthService twoFactorAuthService) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.twoFactorAuthService = twoFactorAuthService;
    }

    @PostMapping("/createUser")
    ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto){
        return ResponseEntity.ok(userService.createUser(userDto));
    }

    @PutMapping("/")
    ResponseEntity<UserDto> updateUser(@RequestBody UserDto userDto){
        return ResponseEntity.ok(userService.updateUser(userDto));
    }

    @GetMapping("/{id}")
    ResponseEntity<UserDto> getUserDtoById(@PathVariable("id") Integer id){
        return ResponseEntity.ok(userService.getUserDtoById(id));
    }

    @PostMapping("/byEmail/")
    ResponseEntity<UserDto> getUserDtoByEmail(@RequestBody Map<String, Object> data) {
        return ResponseEntity.ok(userService.getUserDtoByEmail(data));
    }

    @DeleteMapping("/{id}")
    void deleteUserById(@PathVariable("id") Integer id){
        userService.deleteUserById(id);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        final User user = userService.identicateAndAuthenticate(authenticationRequest.getEmail(), authenticationRequest.getPassword());
        String code = twoFactorAuthService.generateCode();
        twoFactorAuthService.sendCode(user.getEmail(), code);
        twoFactorAuthService.storeCode(user.getEmail(), code);

        return ResponseEntity.ok(new AuthenticationResponse("2FA code sent to your email"));
    }

    @PostMapping("/verify-2fa")
    public ResponseEntity<?> verify2fa(@RequestBody Verify2faRequest verify2faRequest) {
        String email = verify2faRequest.getEmail();
        String code = verify2faRequest.getCode();

        if (twoFactorAuthService.verifyCode(email, code)) {
            User user = userService.getUserByEmail(email);
            final String jwt = jwtUtil.generateToken(user);
            return ResponseEntity.ok(new AuthenticationResponse(jwt));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid 2FA code");
        }
    }
}
