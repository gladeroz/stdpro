package app.traitement;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.mailjet.client.transactional.Attachment;
import com.mailjet.client.transactional.SendContact;
import com.mailjet.client.transactional.SendEmailsRequest;
import com.mailjet.client.transactional.TransactionalEmail;

import app.model.ConfigItem;
import app.traitement.config.CustomConfigSendMail;
import app.traitement.enums.CustomEnumSendMail;
import enums.Extension;
import utils.MailJetService;

@Component
public class SendMail {
	private static Logger logger = Logger.getLogger(SendMail.class);

	private static final String HTML_PART  = "<p>Bonne reception</p><p>KRISTINA</p>";

	private static String API_KEY_STATIC;
	private static String API_SECRET_KEY_STATIC;

	@Value("${stdpro.mail.api-key}")
	public void setApiKeyStatic(String name){
		API_KEY_STATIC = name;
	}

	@Value("${stdpro.mail.api-secret-key}")
	public void setApiSecretKeyStatic(String name){
		API_SECRET_KEY_STATIC = name;
	}

	public static CustomConfigSendMail initConfig(Collection<ConfigItem> config) {
		CustomConfigSendMail cc = new CustomConfigSendMail();

		for(ConfigItem item : config) {
			if(item.getConfigName().equals(CustomEnumSendMail.PATH.getValue())) {
				if(item.getMandatory() && ! Traitement.variableExist(item.getValue())) return null;
				cc.setPath(item.getValue());
			}

			if(item.getConfigName().equals(CustomEnumSendMail.MAIL_DESTINATAIRE.getValue())) {
				if(item.getMandatory() && ! Traitement.variableExist(item.getValue())) return null;
				cc.setMailDestinataire(item.getValue());
			}

			if(item.getConfigName().equals(CustomEnumSendMail.MAIL_EMETTEUR.getValue())) {
				if(item.getMandatory() && ! Traitement.variableExist(item.getValue())) return null;
				cc.setMailEmetteur(item.getValue());
			}
		}

		return cc;
	}

	public static void traitement(Collection<ConfigItem> config) throws Exception {
		logger.info("Traitement 'Envoi de mail' en cours");

		logger.debug("Configuration en cours de traitement");
		CustomConfigSendMail conf = initConfig(config);

		if(conf == null) {
			logger.error("La Configuration comporte des erreurs ou il manque un parametre");
			return;
		}

		logger.debug("Lancement du Traitement : " + new Date());
		job(conf);
		logger.debug("Fin du Traitement : " + new Date());
	}

	public static void job(CustomConfigSendMail config) throws Exception {
		long startTime = System.nanoTime();

		sendPdf(config);

		long endTime = System.nanoTime();

		logger.info("Temps de Traitement : " + TimeUnit.SECONDS.convert((endTime - startTime), TimeUnit.NANOSECONDS) + " secondes");
	}

	private static void sendPdf(CustomConfigSendMail config) throws URISyntaxException, Exception {
		//MailService service = new MailService(config.getMailEmetteur(), config.getPassEmetteur());
		final MailJetService client = new MailJetService(API_KEY_STATIC, API_SECRET_KEY_STATIC);

		File f = new File(config.getPath());
		File[] subFiles = f.listFiles();

		if (subFiles != null && subFiles.length > 0) {
			for (File aFile : subFiles) {
				String currentFileName = aFile.getName();
				if (currentFileName.equals(".") || currentFileName.equals("..")) {
					continue;
				}

				if(currentFileName.toUpperCase().endsWith(Extension.PDF.name())){
					String currentPathPj = Traitement.withSlash(config.getPath()) + currentFileName;
					logger.info("[Fichier en cours : " + currentPathPj + "]");

					//sendMail(config, service, currentFileName, currentPathPj);
					sendMail(config, currentFileName, currentPathPj, client);

					logger.info("[Mail envoye : [" + currentPathPj + "]["+ config.getMailEmetteur() +"]["+ config.getMailDestinataire() +"]]");
				}
			}
		}
	}

	private static void sendMail(CustomConfigSendMail config, String pjName, String pjPath, MailJetService client) throws URISyntaxException, Exception {
		final TransactionalEmail message = TransactionalEmail
				.builder()
				.to(new SendContact(config.getMailDestinataire()))
				.from(new SendContact(config.getMailEmetteur()))
				.htmlPart(HTML_PART)
				.subject(pjName)
				.attachment(Attachment.fromFile(pjPath))
				.build();

		SendEmailsRequest request = SendEmailsRequest
				.builder()
				.message(message) // you can add up to 50 messages per request
				.build();

		request.sendWith(client);
		//SendEmailsResponse response = request.sendWith(client);
		//logger.info(response.getMessages()[0]);
	}
}
