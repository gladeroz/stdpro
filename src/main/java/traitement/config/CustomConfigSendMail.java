package traitement.config;

public class CustomConfigSendMail {
	private String path;
	private String mailEmetteur;
	private String mailDestinataire;
	private String passEmetteur;
	private String serveurMail;
	
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getMailEmetteur() {
		return mailEmetteur;
	}
	public void setMailEmetteur(String mailEmetteur) {
		this.mailEmetteur = mailEmetteur;
	}
	public String getMailDestinataire() {
		return mailDestinataire;
	}
	public void setMailDestinataire(String mailDestinataire) {
		this.mailDestinataire = mailDestinataire;
	}
	public String getPassEmetteur() {
		return passEmetteur;
	}
	public void setPassEmetteur(String passEmetteur) {
		this.passEmetteur = passEmetteur;
	}
	public String getServeurMail() {
		return serveurMail;
	}
	public void setServeurMail(String serveurMail) {
		this.serveurMail = serveurMail;
	}
}
