package traitement;

import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectWriter;

import model.ConfigItem;
import model.ConfigOdrCsv;
import model.ConfigOdrJson;
import model.ConfigStore;
import traitement.config.CustomConfigOdr;
import traitement.enums.CustomEnumOdr;
import utils.CSVService;
import utils.JsonService;
import utils.Traitement;

public class Odr {

	private static Logger logger = Logger.getLogger(Odr.class);

	public static CustomConfigOdr initConfig(Collection<ConfigItem> config) {
		CustomConfigOdr cc = new CustomConfigOdr();

		for(ConfigItem item : config) {
			if(item.getConfigName().equals(CustomEnumOdr.REFERENTIAL.getValue())) {
				if(item.getMandatory() && ! Traitement.variableExist(item.getValue())) return null;
				cc.setReferential(item.getValue());
			}

			if(item.getConfigName().equals(CustomEnumOdr.DELTA.getValue())) {
				if(item.getMandatory() && ! Traitement.variableExist(item.getValue())) return null;
				cc.setDelta(item.getValue());
			}

			if(item.getConfigName().equals(CustomEnumOdr.DOC_TRAITE.getValue())) {
				if(item.getMandatory() && ! Traitement.variableExist(item.getValue())) return null;
				cc.setDocTraite(item.getValue());
			}

			if(item.getConfigName().equals(CustomEnumOdr.EXPORTCSV.getValue())) {
				if(item.getMandatory() && ! Traitement.variableExist(item.getValue())) return null;
				cc.setExportcsv(item.getValue());
			}
		}

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

		File json = Traitement.variableExist(config.getReferential()) ? new File(config.getReferential()) : prepareCsvToJsonBdd() ;

		ConfigStore store = JsonService.getInstance().readValue(json, ConfigStore.class);
		//for (ConfigOdrJson tmp : store.getStore()) {
			//ConfigOdrCsv odr = tmp.getOdr();

			//			if(odr.getProductBrandCode().equals("ASUS") && odr.getTransactionType().equals("VTE")) {
			//				logger.info(odr.getNbrContractRedbox());
			//				odr.setTransactionType("RES");
			//			}
		//}

		updateJsonBdd(json, store);
	}

	private static File prepareCsvToJsonBdd() throws Exception, UnsatisfiedLinkError {
		logger.info("La BDD a ete cree");

		List<String> eligiblite = new ArrayList<String>(Arrays.asList(
				"24021","24023","24024","24026","24028",
				"24032","24053","24054","22368","22370",
				"22372","22374","22375","22377","22379",
				"22380","18274","18275","18276","18277",
				"22382","22385","22384","22387","24064",
				"24066","24065","24067"	
		));


		MappingIterator<ConfigOdrCsv> it = CSVService.getOdrdata();
		ConfigStore store = new ConfigStore();
		//store.setStore(it.readAll());

		store.setStore(new ArrayList<ConfigOdrJson>());

		//int i = 0;
		while (it.hasNext()) {
			//i++;
			ConfigOdrCsv c = it.next();

			if(c.getNbrContractRedbox().isEmpty()) {
				//logger.warn("Ligne " + i + " ignoree [cause : contrat red box vide]");
				continue;
			}

			if(c.getCodeINSEE().isEmpty()) {
				//logger.warn("Ligne " + i + " ignoree [cause : code insee vide]");
				continue;
			}

			//if(! eligiblite.contains(c.getProductCode())) {
				//logger.info("Le contrat [" + c.getNbrContractRedbox() + "] n est pas valide [product code :" + c.getProductCode() + " ]");
				//continue;
			//}

			store.getStore().add(new ConfigOdrJson(c));
		}

		Path tempDirWithPrefix = Files.createTempFile("std", null);
		logger.info(tempDirWithPrefix);

		updateJsonBdd(tempDirWithPrefix.toFile(), store);

		return tempDirWithPrefix.toFile();
	}

	private static void updateJsonBdd(File output, ConfigStore store) throws Exception, UnsatisfiedLinkError {
		logger.info("La BDD a ete mise a jour");

		PrintWriter pwriter = new PrintWriter(output, java.nio.charset.StandardCharsets.UTF_8.name());
		ObjectWriter writer = JsonService.getInstance().writer(new DefaultPrettyPrinter());
		writer.writeValue(pwriter, store);
	}
}
