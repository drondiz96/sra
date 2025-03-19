package bip.bip_project.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class TwoFactorAuthService {
//    @Value("${BIP_SMTP_PASSWORD:NOT_SET}")
//    private String mailPassword;

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    private Map<String, String> codes = new HashMap<>();

    public String generateCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }

    public void sendCode(String email, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        // System.out.println("BIP_SMTP_PASSWORD = " + mailPassword);
        message.setFrom(fromEmail);  // Указываем отправителя!
        message.setTo(email);
        message.setSubject("Your 2FA Code");
        message.setText("Your 2FA code is: " + code);
        System.out.println("Отправляем письмо от " + fromEmail + " на " + email);
        mailSender.send(message);
    }

    public void storeCode(String email, String code) {
        codes.put(email, code);
    }

    public boolean verifyCode(String email, String code) {
        String storedCode = codes.get(email);
        return storedCode != null && storedCode.equals(code);
    }
}