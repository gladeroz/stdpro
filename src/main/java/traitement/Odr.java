package traitement;

import java.util.Collection;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.MappingIterator;

import model.ConfigItem;
import model.ConfigOdr;
import traitement.config.CustomConfigOdr;
import utils.CSVService;

public class Odr {

	private static Logger logger = Logger.getLogger(Odr.class);

	public static CustomConfigOdr initConfig(Collection<ConfigItem> config) {
		CustomConfigOdr cc = new CustomConfigOdr();

		//for(ConfigItem item : config) {}

		return cc;
	}

	public static void traitement(Collection<ConfigItem> config) throws Exception, UnsatisfiedLinkError {
		logger.info("Traitement 'Odr' en cours");

		logger.debug("Configuration en cours de traitement");
		CustomConfigOdr conf = initConfig(config);

		if(conf == null) {
			logger.error("La Configuration comporte des erreurs ou il manque un parametre");
			return;
		}

		logger.debug("Lancement du Traitement : " + new Date());
		job(conf);
		logger.debug("Fin du Traitement : " + new Date());
	}

	public static void job(CustomConfigOdr config) throws Exception {
		long startTime = System.nanoTime();

		odr(config);

		long endTime = System.nanoTime();

		logger.info("Temps de Traitement : " + TimeUnit.SECONDS.convert((endTime - startTime), TimeUnit.NANOSECONDS) + " secondes");
	}

	private static void odr(CustomConfigOdr config) throws Exception, UnsatisfiedLinkError {
		MappingIterator<ConfigOdr> it = CSVService.getOdrdata();

		// or, alternatively all in one go
		//List<Line> all = it.readAll();

		while (it.hasNext()) {
			ConfigOdr p = it.next();
			//logger.info("[" + p.getNbrContractRedbox() + "][" + p.getTransactionType() + "]");
			p.setBrandNameProduct("1");
		}
	}
}
