package utils;

import java.io.File;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import enums.Job;
import model.ConfigCollection;
import traitement.ComptagePDF;
import traitement.Ocr;
import traitement.SendMail;
import traitement.SuffixePrefixe;

public class Traitement implements Runnable  {
	
	private static Logger logger = Logger.getLogger(Traitement.class);
	private static ConfigCollection config;
	private static Job action;
	
	@Override
	public void run() {
		try {
			if(action == null) {
				logger.warn("Aucune action valide n'a ete selectionnee");
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
				case SEND_MAIL:
					SendMail.traitement(config.getConfigSendMail());
				default:
					logger.error("L'action n'est pas implementee");
			}
		}catch (Exception | UnsatisfiedLinkError e) {
			logger.error(e);
		}finally {
			Thread.currentThread().interrupt();
		}
	}

	public Job getAction() {
		return action;
	}

	public void setAction(Job action) {
		Traitement.action = action;
	}

	public ConfigCollection getConfig() {
		return config;
	}

	public void setConfig(ConfigCollection config) {
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
	
	public static boolean variableExist(String variable) {
		return ( ! StringUtils.isBlank(variable));
	}
}
