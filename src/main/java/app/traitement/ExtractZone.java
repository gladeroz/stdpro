package app.traitement;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;

import app.model.ConfigItem;
import app.traitement.config.CustomConfigExtractZone;
import app.traitement.enums.CustomEnumExtractZone;
import app.traitement.enums.CustomEnumOcr;
import enums.Extension;
import utils.PdfService;

public class ExtractZone {

	private static Logger logger = Logger.getLogger(ExtractZone.class);

	public static CustomConfigExtractZone initConfig(Collection<ConfigItem> config) {
		CustomConfigExtractZone cc = new CustomConfigExtractZone();

		for(ConfigItem item : config) {
			if(item.getConfigName().equals(CustomEnumExtractZone.PATH.getValue())) {
				if(item.getMandatory() && ! Traitement.variableExist(item.getValue())) return null;
				cc.setPath(item.getValue());
			}

			if(item.getConfigName().equals(CustomEnumExtractZone.Y.getValue())) {
				if(item.getMandatory() && ! Traitement.variableExist(item.getValue())) return null;
				cc.setY(item.getValue());
			}

			if(item.getConfigName().equals(CustomEnumExtractZone.X.getValue())) {
				if(item.getMandatory() && ! Traitement.variableExist(item.getValue())) return null;
				cc.setX(item.getValue());
			}

			if(item.getConfigName().equals(CustomEnumExtractZone.WIDTH.getValue())) {
				if(item.getMandatory() && ! Traitement.variableExist(item.getValue())) return null;
				cc.setWidth(item.getValue());
			}

			if(item.getConfigName().equals(CustomEnumExtractZone.HEIGHT.getValue())) {
				if(item.getMandatory() && ! Traitement.variableExist(item.getValue())) return null;
				cc.setHeight(item.getValue());
			}

			if(item.getConfigName().equals(CustomEnumExtractZone.EXPORTCSV.getValue())) {
				if(item.getMandatory() && ! Traitement.variableExist(item.getValue())) return null;
				cc.setExportcsv(item.getValue());
			}
			
			if(item.getConfigName().equals(CustomEnumExtractZone.OCR.getValue())) {
				if(item.getMandatory() && ! Traitement.variableExist(item.getValue())) return null;
				cc.setOcr(new Boolean(item.getValue()));
			}
			
			if(item.getConfigName().equals(CustomEnumOcr.TESS4J.getValue())) {
				if(item.getMandatory() && ! Traitement.variableExist(item.getValue())) return null;
				cc.setTess4j(item.getValue());
			}
		}

		return cc;
	}

	public static void traitement(Collection<ConfigItem> config) throws Exception, UnsatisfiedLinkError {
		logger.info("Traitement 'Extract Zone' en cours");

		logger.debug("Configuration en cours de traitement");
		CustomConfigExtractZone conf = initConfig(config);

		if(conf == null) {
			logger.error("La Configuration comporte des erreurs ou il manque un parametre");
			return;
		}

		logger.debug("Lancement du Traitement : " + new Date());
		job(conf);
		logger.debug("Fin du Traitement : " + new Date());
	}

	public static void job(CustomConfigExtractZone config) throws Exception {
		long startTime = System.nanoTime();

		extractZone(config);

		long endTime = System.nanoTime();

		logger.info("Temps de Traitement : " + TimeUnit.SECONDS.convert((endTime - startTime), TimeUnit.NANOSECONDS) + " secondes");
	}

	private static void extractZone(CustomConfigExtractZone config) throws Exception, UnsatisfiedLinkError {
		File f = new File(config.getPath()); 
		File[] subFiles = f.listFiles();

		ArrayList<ArrayList<String>> output = new ArrayList<ArrayList<String>>();

		if (subFiles != null && subFiles.length > 0) {
			for (File aFile : subFiles) {
				String currentFileName = aFile.getName();
				if (currentFileName.equals(".") || currentFileName.equals("..")) {
					continue;
				}

				if(currentFileName.toUpperCase().endsWith(Extension.PDF.name())){
					String result = PdfService.getText(aFile, config.getX(), config.getY(), config.getWidth(), config.getHeight(), config.getOcr(), config.getTess4j());

					if(! Traitement.variableExist(result)) {
						logger.error("Aucun resultat dans cette zone de recherche");
						continue;
					}

					logger.info(result);

					if(Traitement.variableExist(config.getExportcsv())) {
						output.add(new ArrayList<String>(Arrays.asList(result.split("\\r?\\n"))));
					}
				}
			}
		}
		if(Traitement.variableExist(config.getExportcsv())) {
			Traitement.exportToCsv(config.getExportcsv(), output);
		}
	}
}
