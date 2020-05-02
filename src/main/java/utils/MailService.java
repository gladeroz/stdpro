package utils;

import java.net.URI;
import java.net.URISyntaxException;

import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.enumeration.misc.ExchangeVersion;
import microsoft.exchange.webservices.data.credential.WebCredentials;

public class MailService extends ExchangeService {

	public MailService(String mailEmetteur, String password) throws URISyntaxException {
		super(ExchangeVersion.Exchange2010_SP1);
		this.setUrl(new URI("https://outlook.office365.com/EWS/Exchange.asmx"));
        this.setCredentials(new WebCredentials(mailEmetteur, password));
        this.setTraceEnabled(true);
	}
}
