package utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import enums.Job;
import enums.OdrType;
import model.ConfigCollection;
import model.ConfigExportCSV;
import model.ConfigOdrJson;
import model.ConfigOdrRefCsv;
import model.ConfigOdrTraiteCsv;
import model.ConfigStore;
import traitement.CodeBarre;
import traitement.ComptagePDF;
import traitement.ExtractZone;
import traitement.Ocr;
import traitement.Odr;
import traitement.SendMail;
import traitement.SuffixePrefixe;
import traitement.config.CustomConfigOdr;

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
			case ODR:
				Odr.traitement(config.getConfigOdr());
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

	public static void exportToCsvOdr(String csvFile, ConfigStore store, CustomConfigOdr config, DateFormat dateFormat) throws IOException, ParseException {
		FileWriter writer = new FileWriter(csvFile);
		
		CSVService.writeLine(writer, Arrays.asList("NumeroContratRedBox", "DateEffet", "Nom", "Prenom", "Adresse", "CodePostal", "Ville", "Ctry", "IBAN", "Bic", "Montant"));

		for(ConfigOdrJson line : store.getStore() ) {
			ConfigOdrRefCsv odr = line.getOdr();

			if(valideDateEligible(config, line, new SimpleDateFormat("yyyy-MM-dd"))) {

				CSVService.writeLine(writer,
						Arrays.asList(odr.getNbrContractRedbox(), dateFormat.format(odr.getProductSalesDate()),	odr.getClientName(),odr.getCustomerFirstName(),
								odr.getNbrInTheTrack() + " " + odr.getTrackCodeType() + " " + odr.getTrackName(),
								odr.getPostalCode(), odr.getLocation(),"FR",	"",	"",	"30")
						);
			}
		}

		writer.flush();
		writer.close();
	}

	private static boolean valideDateEligible(CustomConfigOdr config, ConfigOdrJson line, DateFormat dateFormat) throws ParseException {
		
		Calendar cInterval = Calendar.getInstance();
		Calendar cCurrent = Calendar.getInstance();

		boolean minExist = Traitement.variableExist(config.getIntervalMin());
		boolean maxExist = Traitement.variableExist(config.getIntervalMax());
		
		ConfigOdrTraiteCsv trait = line.getTraitement();
		
		boolean dateReception = trait.getDateReception() != null;

		if(trait == null 
				|| trait.getBulletin() == null 
				|| trait.getFacture() == null 
				|| trait.getFormulaire() == null 
				|| trait.getRib() == null) {
			return false;
		}
		
		if(minExist && dateReception) {
			cInterval.setTime(dateFormat.parse(config.getIntervalMin()));
			cCurrent.setTime(trait.getDateReception());
			
			if(cCurrent.before(cInterval)) {
				return false;
			}
		}
		
		if(maxExist && dateReception) {
			cInterval.setTime(dateFormat.parse(config.getIntervalMax()));
			cCurrent.setTime(trait.getDateReception());
			
			if(cCurrent.after(cInterval)) {
				return false;
			}
		}

		if(trait.getBulletin().equals(OdrType.S) 
				&& trait.getFacture().equals(OdrType.S) 
				&& trait.getFormulaire().equals(OdrType.S) 
				&& trait.getRib().equals(OdrType.S) 
				) {
			return true;
		}

		return false;
	}
}
