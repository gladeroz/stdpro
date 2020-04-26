package traitement;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import enums.Extension;
import enums.LogLevel;
import model.ConfigItem;
import traitement.config.CustomConfigSuffixe;
import traitement.enums.CustomEnumSuffixe;
import utils.Logger;

public class SuffixePrefixe {

	public static CustomConfigSuffixe initConfig(Collection<ConfigItem> config) {
		CustomConfigSuffixe cc = new CustomConfigSuffixe();

		for(ConfigItem item : config) {
			if(item.getConfigName().equals(CustomEnumSuffixe.PATH.getValue())) {
				if(item.getValue() == null) return null;
				cc.setPath(item.getValue());
			}

			if(item.getConfigName().equals(CustomEnumSuffixe.SUFFIXE.getValue())) {
				if(item.getValue() == null) return null;
				cc.setSuffixe(item.getValue());
			}

			if(item.getConfigName().equals(CustomEnumSuffixe.PREFIXE.getValue())) {
				if(item.getValue() == null) return null;
				cc.setPrefixe(item.getValue());
			}
		}

		return cc;
	}

	public static void traitement(Collection<ConfigItem> config) {
		Logger.print(LogLevel.INFO, "Traitement 'Suffixe et Prefixe' en cours");

		Logger.print(LogLevel.DEBUG, "Configuration en cours de traitement");
		CustomConfigSuffixe conf = initConfig(config);

		if(conf == null) {
			Logger.print(LogLevel.ERROR, "La Configuration comporte des erreurs ou il manque un parametre");
			return;
		}

		Logger.print(LogLevel.DEBUG, "Lancement du Traitement : " + new Date());
		try{
			job(conf);
		}catch (Exception e) {
			System.err.println(e);
		}
		Logger.print(LogLevel.DEBUG, "Fin du Traitement : " + new Date());
	}

	public static void job(CustomConfigSuffixe config) throws IOException {
		long startTime = System.nanoTime();

		if(config.getPrefixe() == null && config.getPrefixe() == null) {
			Logger.print(LogLevel.INFO, "Aucun suffixe ou prefixe n'a ete renseigne");
			return;
		} 
		
		if(config.getPath() == null) {
			Logger.print(LogLevel.INFO, "Le chemin de traitement n'est pas valide");
			return;
		}

		listDirectory(config, config.getPath(), "");

		long endTime = System.nanoTime();

		Logger.print(LogLevel.INFO, "Temps de Traiment : " + TimeUnit.SECONDS.convert((endTime - startTime), TimeUnit.NANOSECONDS) + " secondes");
	}

	public static void listDirectory(CustomConfigSuffixe config, String parentDir, String currentDir) throws IOException {
		String dirToList = parentDir;
		if (!currentDir.equals("")) {
			dirToList += checkTrailingSlash(currentDir);
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
					Logger.print(LogLevel.INFO, "OLD : [Dossier : " + dirToList + "][Fichier : " + currentFileName + "]");
					String NEWFILE = checkTrailingSlash(dirToList) + currentFileName;

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
						System.err.println("FAILED to rename " + aFile.getName() + " to " + NEWFILE);
					} else {
						Logger.print(LogLevel.INFO, "NEW : [Deplace vers : " + NEWFILE + "]");
					}
				}
			}
		}
	}
	
	private static String checkTrailingSlash(String path) {
	    return path.endsWith(File.separator) ? path : path + File.separator;
	}
}
