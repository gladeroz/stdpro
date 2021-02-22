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
import traitement.config.CustomConfigOcr;
import traitement.enums.CustomEnumOcr;
import utils.PdfService;
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

			if(item.getConfigName().equals(CustomEnumOcr.PATTERN.getValue())) {
				if(item.getMandatory() && ! Traitement.variableExist(item.getValue())) return null;
				cc.setPattern(item.getValue());
			}

			if(item.getConfigName().equals(CustomEnumOcr.SUBSEARCH.getValue())) {
				if(item.getMandatory() && ! Traitement.variableExist(item.getValue())) return null;
				cc.setSubSearch(item.getValue());
			}

			if(item.getConfigName().equals(CustomEnumOcr.Y.getValue())) {
				if(item.getMandatory() && ! Traitement.variableExist(item.getValue())) return null;
				cc.setY(item.getValue());
			}

			if(item.getConfigName().equals(CustomEnumOcr.X.getValue())) {
				if(item.getMandatory() && ! Traitement.variableExist(item.getValue())) return null;
				cc.setX(item.getValue());
			}

			if(item.getConfigName().equals(CustomEnumOcr.WIDTH.getValue())) {
				if(item.getMandatory() && ! Traitement.variableExist(item.getValue())) return null;
				cc.setWidth(item.getValue());
			}

			if(item.getConfigName().equals(CustomEnumOcr.HEIGHT.getValue())) {
				if(item.getMandatory() && ! Traitement.variableExist(item.getValue())) return null;
				cc.setHeight(item.getValue());
			}

			if(item.getConfigName().equals(CustomEnumOcr.RENAME.getValue())) {
				if(item.getMandatory() && ! Traitement.variableExist(item.getValue())) return null;
				cc.setRename(new Boolean(item.getValue()));
			}

			if(item.getConfigName().equals(CustomEnumOcr.OCR.getValue())) {
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

		ocr(config, config.getPath(), "");

		long endTime = System.nanoTime();

		logger.info("Temps de Traitement : " + TimeUnit.SECONDS.convert((endTime - startTime), TimeUnit.NANOSECONDS) + " secondes");
	}

	private static void ocr(CustomConfigOcr config, String parentDir, String currentDir) throws Exception, UnsatisfiedLinkError {
		String dirToList = Traitement.withSlash(parentDir);
		if (!currentDir.equals("")) {
			dirToList += Traitement.withSlash(currentDir);
		}

		File f = new File(dirToList); 
		File[] subFiles = f.listFiles();
		if (subFiles != null && subFiles.length > 0) {
			for (File aFile : subFiles) {
				String currentFileName = aFile.getName();
				if (currentFileName.equals(".") || currentFileName.equals("..")) {
					continue;
				}
				if (aFile.isDirectory()) {
					ocr(config, dirToList, currentFileName);
				}else  if(currentFileName.toUpperCase().endsWith(Extension.PDF.name())){
					String NEWFILE = Traitement.withSlash(dirToList) + currentFileName;
					logger.info("[Fichier en cours : " + NEWFILE + "]");

					String text = PdfService.getText(aFile, config.getX(), config.getY(), config.getWidth(), config.getHeight(), config.getOcr(), config.getTess4j());

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
									Path cible = searchIfExist(output, 0, resultat, source);
									Files.move(source, cible);
									logger.info("Le fichier (" + NEWFILE + ") a ete renomme en ("+ cible + ")");
								}
							}else {
								matcher = RegexService.get(config.getSubSearch(), resultat);

								if (matcher.find()) {
									resultat = matcher.group();
									logger.info("Le text (" + resultat + ") correspond à la sous recherche");
									if (Boolean.TRUE.equals(config.getRename())) {
										Path source = Paths.get(NEWFILE);
										String output = resultat + ".pdf";
										Path cible = searchIfExist(output, 0, resultat, source);
										Files.move(source, cible);
										logger.info("Le fichier (" + NEWFILE + ") a ete renomme en ("+ cible + ")");
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
	}

	private static Path searchIfExist(String output, int number, final String resultat, final Path source) {
		while(source.resolveSibling(output).toFile().exists()) {
			output = resultat + "("+ (number++) +").pdf";
		}

		return source.resolveSibling(output);
	}
}
