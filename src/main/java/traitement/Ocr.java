package traitement;

import java.io.File;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import enums.Extension;
import model.ConfigItem;
import net.sourceforge.tess4j.TesseractException;
import net.sourceforge.tess4j.util.PdfUtilities;
import traitement.config.CustomConfigOcr;
import traitement.enums.CustomEnumOcr;
import utils.CustomTesserac;
import utils.Traitement;

public class Ocr {

	private static Logger logger = Logger.getLogger(Ocr.class);

	public static CustomConfigOcr initConfig(Collection<ConfigItem> config) {
		CustomConfigOcr cc = new CustomConfigOcr();

		for(ConfigItem item : config) {
			if(item.getConfigName().equals(CustomEnumOcr.PATH.getValue())) {
				if(item.getValue() == null) return null;
				cc.setPath(item.getValue());
			}

			if(item.getConfigName().equals(CustomEnumOcr.TESS4J.getValue())) {
				if(item.getValue() == null) return null;
				cc.setTess4j(item.getValue());
			}

			if(item.getConfigName().equals(CustomEnumOcr.PATTERN.getValue())) {
				if(item.getValue() == null) return null;
				cc.setPattern(item.getValue());
			}
		}

		return cc;
	}

	public static void traitement(Collection<ConfigItem> config) throws Exception, UnsatisfiedLinkError {
		logger.info("Traitement 'OCR' en cours");

		logger.debug("Configuration en cours de traitement");
		CustomConfigOcr conf = initConfig(config);

		if(conf == null) {
			logger.error("La Configuration comporte des erreurs ou il manque un parametre");
			return;
		}

		logger.debug("Lancement du Traitement : " + new Date());
		job(conf);
		logger.debug("Fin du Traitement : " + new Date());
	}

	public static void job(CustomConfigOcr config) throws Exception {
		long startTime = System.nanoTime();

		ocr(config);

		long endTime = System.nanoTime();

		logger.info("Temps de Traiment : " + TimeUnit.SECONDS.convert((endTime - startTime), TimeUnit.NANOSECONDS) + " secondes");
	}

	private static void ocr(CustomConfigOcr config) throws Exception, UnsatisfiedLinkError {
		CustomTesserac.setConfig(config);
		try { 
			File f = new File(config.getPath()); 
			File[] subFiles = f.listFiles();

			if (subFiles != null && subFiles.length > 0) {
				for (File aFile : subFiles) {
					String currentFileName = aFile.getName();
					if (currentFileName.equals(".") || currentFileName.equals("..")) {
						continue;
					}

					if(currentFileName.toUpperCase().endsWith(Extension.PDF.name())){
						String NEWFILE = Traitement.withSlash(config.getPath()) + currentFileName;
						logger.info("[Fichier en cours : " + NEWFILE + "]");
						File[] png = PdfUtilities.convertPdf2Png(new File(NEWFILE));

						logger.info("[Fichier convertit en png]");

						// the path of your tess data folder 
						// inside the extracted file 
						String text = CustomTesserac.getInstance().doOCR(png[0]); 

						if(config.getPattern() == "") {
							// path of your image file 
							logger.info("[OCR] " + text); 
						} else {
							if(text == null || text == "") {
								logger.error("Texte vide");
								continue;
							}
							
							String regex = config.getPattern();
							Pattern pattern = Pattern.compile(regex);
							Matcher matcher = pattern.matcher(text);

							if (matcher.find()) {
								logger.info("Le text (" + matcher.group() + ") correspond au filtre de recherche");
							}else {
								logger.warn("La chaine n'a pas ete retrouvee dans le document pdf");
							}
						}
					}
				}
			}
		} catch (TesseractException e) { 
			logger.error(e); 
		}
	}
}
