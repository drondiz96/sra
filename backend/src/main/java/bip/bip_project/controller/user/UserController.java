package bip.bip_project.controller.user;

import bip.bip_project.model.authenticate.AuthResponse;
import bip.bip_project.model.authenticate.AuthenticationRequest;
import bip.bip_project.model.authenticate.Verify2faRequest;
import bip.bip_project.model.user.User;
import bip.bip_project.model.user.UserRequestDto;
import bip.bip_project.security.JwtUtil;
import bip.bip_project.service.user.IUserService;
import bip.bip_project.service.user.TwoFactorAuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@Tag(name = "Пользователи", description = "Операции с пользователями и авторизацией")
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

    @Operation(summary = "Создать пользователя с 2FA", responses = {
            @ApiResponse(responseCode = "200", description = "Пользователь создан и код отправлен на email"),
            @ApiResponse(responseCode = "400", description = "Ошибка при создании пользователя")
    })
    @PostMapping("/createUser")
    ResponseEntity<?> createUser(@RequestBody UserRequestDto userRequestDto){
        userService.createUser(userRequestDto);

        String code = twoFactorAuthService.generateCode();
        twoFactorAuthService.sendCode(userRequestDto.getEmail(), code);
        twoFactorAuthService.storeCode(userRequestDto.getEmail(), code);

        return ResponseEntity.ok(new AuthResponse("2FA code sent to your email for confirm your email"));
        //return ResponseEntity.ok(userService.createUser(userDto));
    }

    @Operation(summary = "Подтвердить email с 2FA", responses = {
            @ApiResponse(responseCode = "200", description = "Email подтвержден"),
            @ApiResponse(responseCode = "401", description = "Неверный код 2FA")
    })
    @PostMapping("/createUser/register-verify-email")
    public ResponseEntity<?> createUserVerify2fa(@RequestBody Verify2faRequest verify2faRequest) {
        String email = verify2faRequest.getEmail();
        String code = verify2faRequest.getCode();

        if (twoFactorAuthService.verifyCode(email, code)) {
            userService.confirmEmail(email);
            return ResponseEntity.ok(userService.getUserByEmail(email));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid 2FA code");
        }
    }

    @Operation(summary = "Обновить пользователя", responses = {
            @ApiResponse(responseCode = "200", description = "Пользователь обновлен"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @PutMapping("/")
    ResponseEntity<UserRequestDto> updateUser(@RequestBody UserRequestDto userRequestDto){
        return ResponseEntity.ok(userService.updateUser(userRequestDto));
    }

    @Operation(summary = "Получить пользователя по ID", responses = {
            @ApiResponse(responseCode = "200", description = "Пользователь найден"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @GetMapping("/{id}")
    ResponseEntity<UserRequestDto> getUserDtoById(@PathVariable("id") Integer id){
        return ResponseEntity.ok(userService.getUserDtoById(id));
    }

    @Operation(summary = "Получить пользователя по email", responses = {
            @ApiResponse(responseCode = "200", description = "Пользователь найден"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @PostMapping("/byEmail/")
    ResponseEntity<UserRequestDto> getUserDtoByEmail(@RequestBody Map<String, Object> data) {
        return ResponseEntity.ok(userService.getUserDtoByEmail(data));
    }

    @Operation(summary = "Удалить пользователя по ID", responses = {
            @ApiResponse(responseCode = "204", description = "Пользователь удален"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @DeleteMapping("/{id}")
    void deleteUserById(@PathVariable("id") Integer id){
        userService.deleteUserById(id);
    }

    @Operation(summary = "Аутентификация пользователя", responses = {
            @ApiResponse(responseCode = "200", description = "Код 2FA отправлен"),
            @ApiResponse(responseCode = "401", description = "Неверный email или пароль")
    })
    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        final User user = userService.identicateAndAuthenticate(authenticationRequest.getEmail(), authenticationRequest.getPassword());
        String code = twoFactorAuthService.generateCode();
        twoFactorAuthService.sendCode(user.getEmail(), code);
        twoFactorAuthService.storeCode(user.getEmail(), code);

        return ResponseEntity.ok(new AuthResponse("2FA code sent to your email"));
    }

    @Operation(summary = "Подтвердить 2FA и выдать JWT", responses = {
            @ApiResponse(responseCode = "200", description = "Аутентификация прошла успешно"),
            @ApiResponse(responseCode = "401", description = "Неверный код 2FA")
    })
    @PostMapping("/authenticate/auth-verify-2fa")
    public ResponseEntity<?> verify2fa(@RequestBody Verify2faRequest verify2faRequest, HttpServletResponse response) {
        String email = verify2faRequest.getEmail();
        String code = verify2faRequest.getCode();

        if (twoFactorAuthService.verifyCode(email, code)) {
            User user = userService.getUserByEmail(email);
            final String jwt = jwtUtil.generateToken(user);

            // Установка JWT в HttpOnly cookie
            Cookie cookie = new Cookie("jwt", jwt);
            cookie.setHttpOnly(true);
            cookie.setSecure(false); // true надо поставить для prod
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60 * 2); // на 2 часика
            response.addCookie(cookie);

            // это старая версия в которой токен передавался в теле http ответа, и вручную на фронте нужно было его сохранять и постоянно пробрасывать при каждом запросе к бекенду
            // return ResponseEntity.ok(new AuthenticationResponse(jwt));

            // это реализация через cookie, браузер самостоятельно увидит что в этом ответе есть cookie и сохранит jwt в свои cookie и будет всегда автоматически прикреплять его при обращении к бекенду
            return ResponseEntity.ok("2FA verification success");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid 2FA code");
        }
    }
}
