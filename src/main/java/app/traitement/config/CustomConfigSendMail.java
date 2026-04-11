package app.traitement.config;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomConfigSendMail {
	private String path;
	private String mailEmetteur;
	private String mailDestinataire;
	private String passEmetteur;
}
