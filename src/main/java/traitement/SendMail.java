package traitement;

import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import microsoft.exchange.webservices.data.core.service.item.EmailMessage;
import microsoft.exchange.webservices.data.property.complex.EmailAddress;
import microsoft.exchange.webservices.data.property.complex.MessageBody;
import model.ConfigItem;
import traitement.config.CustomConfigSendMail;
import traitement.enums.CustomEnumSendMail;
import utils.MailService;
import utils.Traitement;

public class SendMail {
	private static Logger logger = Logger.getLogger(SuffixePrefixe.class);

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
			
			if(item.getConfigName().equals(CustomEnumSendMail.PASS_EMETTEUR.getValue())) {
				if(item.getMandatory() && ! Traitement.variableExist(item.getValue())) return null;
				cc.setPassEmetteur(item.getValue());
			}
			
			if(item.getConfigName().equals(CustomEnumSendMail.URL_SERVEUR.getValue())) {
				if(item.getMandatory() && ! Traitement.variableExist(item.getValue())) return null;
				cc.setServeurMail(item.getValue());
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

		MailService service = new MailService(config.getServeurMail(), config.getMailEmetteur(), config.getPassEmetteur());
		sendMail(config, service);

		long endTime = System.nanoTime();

		logger.info("Temps de Traiment : " + TimeUnit.SECONDS.convert((endTime - startTime), TimeUnit.NANOSECONDS) + " secondes");
	}

	private static void sendMail(CustomConfigSendMail config, MailService service) throws URISyntaxException, Exception {
        EmailMessage replymessage = new EmailMessage(service);
        EmailAddress fromEmailAddress = new EmailAddress(config.getMailEmetteur());
        replymessage.setFrom(fromEmailAddress);
        replymessage.getToRecipients().add(config.getMailDestinataire());
        replymessage.setSubject("Test");
        replymessage.setBody(new MessageBody("Test"));
        replymessage.send();
	}
}
