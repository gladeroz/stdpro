package utils;

import com.mailjet.client.ClientOptions;
import com.mailjet.client.MailjetClient;

public class MailJetService extends MailjetClient {
	public MailJetService(String API_KEY_STATIC, String API_SECRET_KEY_STATIC) {
		super(ClientOptions
				.builder()
				.apiKey(API_KEY_STATIC)
				.apiSecretKey(API_SECRET_KEY_STATIC)
				.build());
	}
}
