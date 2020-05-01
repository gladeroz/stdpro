package traitement;

import java.io.File;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import enums.Extension;
import model.ConfigItem;
import traitement.config.CustomConfigSuffixe;
import traitement.enums.CustomEnumSuffixe;
import utils.Traitement;

public class SuffixePrefixe {
	
	private static Logger logger = Logger.getLogger(SuffixePrefixe.class);

	public static CustomConfigSuffixe initConfig(Collection<ConfigItem> config) {
		CustomConfigSuffixe cc = new CustomConfigSuffixe();

		for(ConfigItem item : config) {
			if(item.getConfigName().equals(CustomEnumSuffixe.PATH.getValue())) {
				if(item.getMandatory() && ! Traitement.variableExist(item.getValue())) return null;
				cc.setPath(item.getValue());
			}

			if(item.getConfigName().equals(CustomEnumSuffixe.SUFFIXE.getValue())) {
				if(item.getMandatory() && ! Traitement.variableExist(item.getValue())) return null;
				cc.setSuffixe(item.getValue());
			}

			if(item.getConfigName().equals(CustomEnumSuffixe.PREFIXE.getValue())) {
				if(item.getMandatory() && ! Traitement.variableExist(item.getValue())) return null;
				cc.setPrefixe(item.getValue());
			}
		}

		return cc;
	}

	public static void traitement(Collection<ConfigItem> config) throws Exception {
		logger.info("Traitement 'Suffixe et Prefixe' en cours");

		logger.debug("Configuration en cours de traitement");
		CustomConfigSuffixe conf = initConfig(config);

		if(conf == null) {
			logger.error("La Configuration comporte des erreurs ou il manque un parametre");
			return;
		}

		logger.debug("Lancement du Traitement : " + new Date());
		job(conf);
		logger.debug("Fin du Traitement : " + new Date());
	}

	public static void job(CustomConfigSuffixe config) throws Exception {
		long startTime = System.nanoTime();

		if( ! Traitement.variableExist(config.getPrefixe()) && ! Traitement.variableExist(config.getSuffixe())) {
			logger.info("Aucun suffixe ou prefixe n'a ete renseigne");
			return;
		} 
		
		if( ! Traitement.variableExist(config.getPath())) {
			logger.info("Le chemin de traitement n'est pas valide");
			return;
		}

		listDirectory(config, Traitement.withoutSlash(config.getPath()), "");

		long endTime = System.nanoTime();

		logger.info("Temps de Traiment : " + TimeUnit.SECONDS.convert((endTime - startTime), TimeUnit.NANOSECONDS) + " secondes");
	}

	public static void listDirectory(CustomConfigSuffixe config, String parentDir, String currentDir) throws Exception {
		String dirToList = parentDir;
		if (!currentDir.equals("")) {
			dirToList += Traitement.withSlash(currentDir);
		}

		File f =  new File(dirToList); 
		File[] subFiles = f.listFiles();
		if (subFiles != null && subFiles.length > 0) {
			for (File aFile : subFiles) {
				String currentFileName = aFile.getName();
				if (currentFileName.equals(".") || currentFileName.equals("..")) {
					continue;
				}
				if (aFile.isDirectory()) {
					listDirectory(config, dirToList, currentFileName);
				} else if(currentFileName.toUpperCase().endsWith(Extension.PDF.name())){
					logger.info("OLD : [Dossier : " + dirToList + "][Fichier : " + currentFileName + "]");
					String NEWFILE = Traitement.withSlash(dirToList) + currentFileName;

					/** Partie Prefixe **/
					if(config.getPrefixe() != null) {
						NEWFILE = NEWFILE.replace(currentFileName, config.getPrefixe() + currentFileName);
					}
					
					/** Partie Suffixe **/
					if(config.getSuffixe() != null) {
						int where = NEWFILE.lastIndexOf(".");
						NEWFILE = NEWFILE.substring(0, where) + config.getSuffixe() + NEWFILE.substring(where);
					}
				
					/** save new file **/
					boolean success = aFile.renameTo(new File(NEWFILE));
					if (!success) {
						logger.error("FAILED to rename " + aFile.getName() + " to " + NEWFILE);
					} else {
						logger.info("NEW : [Deplace vers : " + NEWFILE + "]");
					}
				}
			}
		}
	}
}
