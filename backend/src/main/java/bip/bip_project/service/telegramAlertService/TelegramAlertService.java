package bip.bip_project.service.telegramAlertService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TelegramAlertService {

    @Value("${telegram.bot.token}")
    private String botToken;

    private List<String> adminChatIds = List.of("1202111163", "446780363", "906737008");

    private final RestTemplate restTemplate = new RestTemplate();

    public void sendMessage(String message) {
        String url = "https://api.telegram.org/bot" + botToken + "/sendMessage";

        for (String chatId : adminChatIds) {
            Map<String, String> body = new HashMap<>();
            body.put("chat_id", chatId);
            body.put("text", message);

            try {
                restTemplate.postForEntity(url, body, String.class);
            } catch (Exception e) {
                System.err.println("Ошибка при отправке сообщения в Telegram для chat_id " + chatId + ": " + e.getMessage());
            }
        }
    }
}

