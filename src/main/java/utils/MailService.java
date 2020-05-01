package utils;

import java.net.URI;
import java.net.URISyntaxException;

import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.enumeration.misc.ExchangeVersion;
import microsoft.exchange.webservices.data.credential.WebCredentials;

public class MailService extends ExchangeService{

	public MailService(String serverMail, String mailEmetteur, String password) throws URISyntaxException {
		super(ExchangeVersion.Exchange2010_SP1);
		this.setUrl(new URI(serverMail));
        this.setCredentials(new WebCredentials(mailEmetteur, password));
        this.setTraceEnabled(true);
	}
}
