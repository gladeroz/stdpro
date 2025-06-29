package app.traitement;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.google.zxing.NotFoundException;

import app.model.BarcodeInfo;
import app.model.ConfigItem;
import app.traitement.config.CustomConfigCodeBarre;
import app.traitement.enums.CustomEnumCodeBarre;
import enums.Extension;
import utils.PdfService;

public class CodeBarre {

	private static Logger logger = Logger.getLogger(CodeBarre.class);

	public static CustomConfigCodeBarre initConfig(Collection<ConfigItem> config) {
		CustomConfigCodeBarre cc = new CustomConfigCodeBarre();

		for(ConfigItem item : config) {
			if(item.getConfigName().equals(CustomEnumCodeBarre.PATH.getValue())) {
				if(item.getMandatory() && ! Traitement.variableExist(item.getValue())) {
					return null;
				}
				cc.setPath(item.getValue());
			}

			if(item.getConfigName().equals(CustomEnumCodeBarre.TESS4J.getValue())) {
				if(item.getMandatory() && ! Traitement.variableExist(item.getValue())) {
					return null;
				}
				cc.setTess4j(item.getValue());
			}

			if(item.getConfigName().equals(CustomEnumCodeBarre.RENAME.getValue())) {
				if(item.getMandatory() && ! Traitement.variableExist(item.getValue())) {
					return null;
				}
				cc.setRename(Boolean.valueOf(item.getValue()));
			}
		}

		return cc;
	}

	public static void traitement(Collection<ConfigItem> config) throws Exception, UnsatisfiedLinkError {
		logger.info("Traitement 'Code Barre' en cours");

		logger.debug("Configuration en cours de traitement");
		CustomConfigCodeBarre conf = initConfig(config);

		if(conf == null) {
			logger.error("La Configuration comporte des erreurs ou il manque un parametre");
			return;
		}

		logger.debug("Lancement du Traitement : " + new Date());
		job(conf);
		logger.debug("Fin du Traitement : " + new Date());
	}

	public static void job(CustomConfigCodeBarre config) throws Exception {
		long startTime = System.nanoTime();

		codeBarre(config);

		long endTime = System.nanoTime();

		logger.info("Temps de Traitement : " + TimeUnit.SECONDS.convert((endTime - startTime), TimeUnit.NANOSECONDS) + " secondes");
	}

	private static void codeBarre(CustomConfigCodeBarre config) throws Exception, UnsatisfiedLinkError {
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

					try {
						BarcodeInfo resultat = PdfService.decodeOneBarcodeWithMBC(new File(NEWFILE), config.getTess4j());
						logger.info(resultat.print());

						if (Boolean.TRUE.equals(config.getRename())) {
							Path source = Paths.get(NEWFILE);
							String output = resultat.getText() + ".pdf";
							Files.move(source, source.resolveSibling(output));
							logger.info("Le fichier (" + NEWFILE + ") a ete renomme en ("+ output+ ")");
						}
					} catch (NotFoundException e) {logger.error("Aucun code barre n'a ete trouve dans ce document");}

				}
			}
		}
	}
}
