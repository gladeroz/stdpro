package traitement.enums;

public enum CustomEnumSendMail {
	PATH ("path"),
	MAIL_EMETTEUR ("mailEmetteur"),
	MAIL_DESTINATAIRE ("mailDestinataire"),
	PASS_EMETTEUR ("passEmetteur"),
	URL_SERVEUR ("serveurMail");
	
	private String value;

	CustomEnumSendMail(String str) {
		this.value = str;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
