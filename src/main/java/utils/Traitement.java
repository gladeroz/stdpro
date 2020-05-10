package utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import enums.Job;
import model.ConfigCollection;
import model.ConfigExportCSV;
import traitement.CodeBarre;
import traitement.ComptagePDF;
import traitement.ExtractZone;
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

			if(config == null) {
				logger.warn("Aucune configuration valide n'a ete selectionnee");
				return;
			}

			switch (action) {
			case COMPTAGE_PDF:
				ComptagePDF.traitement(config.getConfigComptagePdf());
				break;
			case SUFFIX_PREFIX:
				SuffixePrefixe.traitement(config.getConfigSuffixPrefix());
				break;
			case OCR:
				Ocr.traitement(config.getConfigOcr());
				break;
			case EXTRACT_ZONE:
				ExtractZone.traitement(config.getConfigExtractZone());
				break;
			case SEND_MAIL:
				SendMail.traitement(config.getConfigSendMail());
				break;
			case CODE_BARRE:
				CodeBarre.traitement(config.getConfigCodeBarre());
				break;
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
		if(! Traitement.variableExist(path)) return null;

		if(path.charAt(path.length()-1) != File.separatorChar){
			return path + File.separator;
		}
		return path;
	}

	public static String withoutSlash(String path) {
		if(! Traitement.variableExist(path)) return null;

		if(path.charAt(path.length()-1) == File.separatorChar){
			return path.substring(0, path.length() - 1);
		}
		return path;
	}

	public static boolean variableExist(String variable) {
		return ( ! StringUtils.isBlank(variable));
	}

	public static void exportToCsv(String csvFile, ArrayList<ConfigExportCSV> resultat, List<String> entete) throws IOException {
		FileWriter writer = new FileWriter(csvFile);

		if(entete != null) {
			CSVService.writeLine(writer, entete);
		}

		for(ConfigExportCSV line : resultat ) {
			CSVService.writeLine(writer, Arrays.asList(line.getDirectory(), line.getFileName(), line.getNombrePage().toString()));
		}

		writer.flush();
		writer.close();
	}

	public static void exportToCsv(String csvFile, ArrayList<ArrayList<String>> resultat) throws IOException {
		FileWriter writer = new FileWriter(csvFile);

		for(ArrayList<String> line : resultat ) {
			CSVService.writeLine(writer, line);
		}

		writer.flush();
		writer.close();
	}
}
