package bip.bip_project;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BipProjectApplication {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.load(); // Загружаем .env

		// загружаем данные из переменных среды
		System.setProperty("MAIL_USERNAME", dotenv.get("MAIL_USERNAME"));
		System.setProperty("MAIL_PASSWORD", dotenv.get("MAIL_PASSWORD"));
		System.setProperty("CLIENT_ID", dotenv.get("CLIENT_ID"));
		System.setProperty("CLIENT_SECRET", dotenv.get("CLIENT_SECRET"));
		System.setProperty("TELEGRAM_BOT_TOKEN", dotenv.get("TELEGRAM_BOT_TOKEN"));

		// System.out.println("MAIL_USERNAME: " + System.getProperty("MAIL_USERNAME"));
		// System.out.println("MAIL_PASSWORD: " + System.getProperty("MAIL_PASSWORD"));
		System.out.println("CLIENT_ID: " + System.getProperty("CLIENT_ID"));
		System.out.println("CLIENT_SECRET: " + System.getProperty("CLIENT_SECRET"));

		SpringApplication.run(BipProjectApplication.class, args);
	}

}
