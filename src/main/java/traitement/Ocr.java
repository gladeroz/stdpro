package traitement;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;

import org.apache.log4j.Logger;

import enums.Extension;
import model.ConfigItem;
import net.sourceforge.tess4j.TesseractException;
import net.sourceforge.tess4j.util.PdfUtilities;
import traitement.config.CustomConfigOcr;
import traitement.enums.CustomEnumOcr;
import utils.TesseracService;
import utils.RegexService;
import utils.Traitement;

public class Ocr {

	private static Logger logger = Logger.getLogger(Ocr.class);

	public static CustomConfigOcr initConfig(Collection<ConfigItem> config) {
		CustomConfigOcr cc = new CustomConfigOcr();

		for(ConfigItem item : config) {
			if(item.getConfigName().equals(CustomEnumOcr.PATH.getValue())) {
				if(item.getMandatory() && ! Traitement.variableExist(item.getValue())) return null;
				cc.setPath(item.getValue());
			}

			if(item.getConfigName().equals(CustomEnumOcr.TESS4J.getValue())) {
				if(item.getMandatory() && ! Traitement.variableExist(item.getValue())) return null;
				cc.setTess4j(item.getValue());
			}

			if(item.getConfigName().equals(CustomEnumOcr.PATTERN.getValue())) {
				if(item.getMandatory() && ! Traitement.variableExist(item.getValue())) return null;
				cc.setPattern(item.getValue());
			}
			
			if(item.getConfigName().equals(CustomEnumOcr.SUBSEARCH.getValue())) {
				if(item.getMandatory() && ! Traitement.variableExist(item.getValue())) return null;
				cc.setSubSearch(item.getValue());
			}
			
			if(item.getConfigName().equals(CustomEnumOcr.RENAME.getValue())) {
				if(item.getMandatory() && ! Traitement.variableExist(item.getValue())) return null;
				cc.setRename(new Boolean(item.getValue()));
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
		TesseracService.setConfig(config);
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
						String text = TesseracService.getInstance().doOCR(png[0]); 

						if( ! Traitement.variableExist(config.getPattern())) {
							logger.info("[OCR] " + text); 
						} else {
							if(text == null || text == "") {
								logger.error("Texte vide");
								continue;
							}
							
							Matcher matcher = RegexService.get(config.getPattern(), text);

							if (matcher.find()) {
								String resultat = matcher.group();
								
								logger.info("Le text (" + resultat + ") correspond au filtre de recherche");
								if ( ! Traitement.variableExist(config.getSubSearch())) {
									if (Boolean.TRUE.equals(config.getRename())) {
										Path source = Paths.get(NEWFILE);
										String output = resultat + ".pdf";
										Files.move(source, source.resolveSibling(output));
										logger.info("Le fichier (" + NEWFILE + ") a ete renomme en ("+ output+ ")");
									}
								}else {
									matcher = RegexService.get(config.getSubSearch(), resultat);
									
									if (matcher.find()) {
										resultat = matcher.group();
										logger.info("Le text (" + resultat + ") correspond à la sous recherche");
										if (Boolean.TRUE.equals(config.getRename())) {
											Path source = Paths.get(NEWFILE);
											String output = resultat + ".pdf";
											Files.move(source, source.resolveSibling(output));
											logger.info("Le fichier (" + NEWFILE + ") a ete renomme en ("+ output+ ")");
										}
									} else {
										logger.warn("La sous-chaine n'a pas ete trouvee");
									}
								} 
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
