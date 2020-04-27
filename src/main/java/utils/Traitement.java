package utils;

import java.io.File;

import enums.Job;
import enums.LogLevel;
import model.ConfigCollection;
import traitement.ComptagePDF;
import traitement.Ocr;
import traitement.SuffixePrefixe;

public class Traitement {
	private static Job action;
	
	private static ConfigCollection config;
	
	public void traitement() {}
	
	public static void doJob() {
		if(action == null) {
			Logger.print(LogLevel.WARNING, "Aucune action valide n'a ete selectionnee");
			return;
		}
		
		switch (action) {
			case COMPTAGE_PDF:
				ComptagePDF.traitement(config.getSpecificConfig(action));
				break;
			case SUFFIX_PREFIX:
				SuffixePrefixe.traitement(config.getSpecificConfig(action));
				break;
			case OCR:
				Ocr.traitement(config.getSpecificConfig(action));
				break;
			default:
				Logger.print(LogLevel.ERROR, "L'action n'est pas implementee");
		}
	}

	public Job getAction() {
		return action;
	}

	public static void setAction(Job action) {
		Traitement.action = action;
	}

	public static ConfigCollection getConfig() {
		return config;
	}

	public static void setConfig(ConfigCollection config) {
		Traitement.config = config;
	}
	
	public static String withSlash(String path) {
		if(path.charAt(path.length()-1) != File.separatorChar){
		    return path + File.separator;
		}
		return path;
	}
	
	public static String withoutSlash(String path) {
		if(path.charAt(path.length()-1) == File.separatorChar){
		    return path.substring(0, path.length() - 1);
		}
		return path;
	}	
}
