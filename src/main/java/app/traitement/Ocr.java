package app.traitement;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import app.model.ConfigItem;
import app.traitement.config.CustomConfigOcr;
import app.traitement.enums.CustomEnumOcr;
import enums.Extension;
import utils.PdfService;
import utils.RegexService;

public class Ocr {

	private Ocr() {}

	private static final Logger logger = LogManager.getLogger(Ocr.class);

	public static CustomConfigOcr initConfig(Collection<ConfigItem> config) {
		CustomConfigOcr cc = new CustomConfigOcr();

		for(ConfigItem item : config) {
			if(item.getMandatory() && ! Traitement.variableExist(item.getValue())) {
				return null;
			}

			String name = item.getConfigName();

			if(name.equals(CustomEnumOcr.PATH.getValue())) {
				cc.setPath(item.getValue());
			} else if(name.equals(CustomEnumOcr.PATTERN.getValue())) {
				cc.setPattern(item.getValue());
			} else if(name.equals(CustomEnumOcr.SUBSEARCH.getValue())) {
				cc.setSubSearch(item.getValue());
			} else if(name.equals(CustomEnumOcr.Y.getValue())) {
				cc.setY(item.getValue());
			} else if(name.equals(CustomEnumOcr.X.getValue())) {
				cc.setX(item.getValue());
			} else if(name.equals(CustomEnumOcr.WIDTH.getValue())) {
				cc.setWidth(item.getValue());
			} else if(name.equals(CustomEnumOcr.HEIGHT.getValue())) {
				cc.setHeight(item.getValue());
			} else if(name.equals(CustomEnumOcr.RENAME.getValue())) {
				cc.setRename(Boolean.parseBoolean(item.getValue()));
			} else if(name.equals(CustomEnumOcr.OCR.getValue())) {
				cc.setOcr(Boolean.parseBoolean(item.getValue()));
			} else if(name.equals(CustomEnumOcr.TESS4J.getValue())) {
				cc.setTess4j(item.getValue());
			}
		}

		return cc;
	}

	public static void traitement(Collection<ConfigItem> config) throws Exception {
		logger.info("Traitement 'OCR' en cours");

		logger.debug("Configuration en cours de traitement");
		CustomConfigOcr conf = initConfig(config);

		if(conf == null) {
			logger.error("La Configuration comporte des erreurs ou il manque un parametre");
			return;
		}

		logger.debug("Lancement du Traitement : {}", new Date());
		job(conf);
		logger.debug("Fin du Traitement : {}", new Date());
	}

	public static void job(CustomConfigOcr config) throws Exception {
		long startTime = System.nanoTime();

		ocr(config, config.getPath(), "");

		long endTime = System.nanoTime();

		logger.info("Temps de Traitement : {} secondes", TimeUnit.SECONDS.convert((endTime - startTime), TimeUnit.NANOSECONDS));
	}

	private static void ocr(CustomConfigOcr config, String parentDir, String currentDir) throws Exception {
		String dirToList = Traitement.withSlash(parentDir);
		if (!currentDir.isEmpty()) {
			dirToList += Traitement.withSlash(currentDir);
		}

		File f = new File(dirToList);
		File[] subFiles = f.listFiles();
		if (subFiles != null) {
			for (File aFile : subFiles) {
				String currentFileName = aFile.getName();
				if (aFile.isDirectory()) {
					ocr(config, dirToList, currentFileName);
				} else if(StringUtils.endsWithIgnoreCase(currentFileName, Extension.PDF.name())){
					String newFile = Traitement.withSlash(dirToList) + currentFileName;
					logger.info("[Fichier en cours : {}]", newFile);

					String text = PdfService.getText(aFile, config.getX(), config.getY(), config.getWidth(), config.getHeight(), config.getOcr(), config.getTess4j());

					if( ! Traitement.variableExist(config.getPattern())) {
						logger.info("[OCR] {}", text);
					} else {
						if(text == null || text.isEmpty()) {
							logger.error("Texte vide");
							continue;
						}

						Matcher matcher = RegexService.get(config.getPattern(), text);

						if (matcher.find()) {
							String resultat = matcher.group();

							logger.info("Le text ({}) correspond au filtre de recherche", resultat);
							if ( ! Traitement.variableExist(config.getSubSearch())) {
								if (Boolean.TRUE.equals(config.getRename())) {
									Path source = Paths.get(newFile);
									String output = resultat + ".pdf";
									Path cible = searchIfExist(output, 0, resultat, source);
									Files.move(source, cible);
									logger.info("Le fichier ({}) a ete renomme en ({})", newFile, cible);
								}
							}else {
								matcher = RegexService.get(config.getSubSearch(), resultat);

								if (matcher.find()) {
									resultat = matcher.group();
									logger.info("Le text ({}) correspond a la sous recherche", resultat);
									if (Boolean.TRUE.equals(config.getRename())) {
										Path source = Paths.get(newFile);
										String output = cleanString(resultat) + ".pdf";
										Path cible = searchIfExist(output, 0, resultat, source);
										Files.move(source, cible);
										logger.info("Le fichier ({}) a ete renomme en ({})", newFile, cible);
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

	private static String cleanString(String s) {
		return s.replace("/", "");
	}

	private static Path searchIfExist(String output, int number, final String resultat, final Path source) {
		while(source.resolveSibling(cleanString(output)).toFile().exists()) {
			output = cleanString(resultat + "("+ (number++) +").pdf");
		}

		return source.resolveSibling(cleanString(output));
	}
}
