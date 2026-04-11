package app.traitement;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import app.entity.odr.CodeEligibleSql;
import app.entity.odr.CsvSql;
import app.model.ConfigCollection;
import lombok.Getter;
import lombok.Setter;
import app.model.ConfigExportCSV;
import app.repository.odr.CodeEligibleRepository;
import app.traitement.config.CustomConfigOdr;
import enums.Job;
import enums.Offre;
import enums.odrodf.BaType;
import enums.odrodf.FactType;
import enums.odrodf.FormType;
import enums.odrodf.RibType;
import utils.CSVService;

@Component
@Getter @Setter
public class Traitement implements Runnable {

	private Job action;
	private ConfigCollection config;

	@Override
	public void run() {
		Logger logger = LogManager.getLogger(Traitement.class);

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
				new BulletinAdhesion().traitement(config.getConfigOdr());
				break;
			case GIMS:
				Gims.traitement(config.getConfigGims());
				break;
			default:
				logger.error("L'action n'est pas implementee");
			}
		}catch (Exception | UnsatisfiedLinkError e) {
			logger.error(e);
		}
	}

	public static String withSlash(String path) {
		if(! Traitement.variableExist(path)) {
			return null;
		}

		if(path.charAt(path.length()-1) != File.separatorChar){
			return path + File.separator;
		}
		return path;
	}

	public static String withoutSlash(String path) {
		if(! Traitement.variableExist(path)) {
			return null;
		}

		if(path.charAt(path.length()-1) == File.separatorChar){
			return path.substring(0, path.length() - 1);
		}
		return path;
	}

	public static boolean variableExist(String variable) {
		return ( ! StringUtils.isBlank(variable));
	}

	public static void exportToCsv(String csvFile, ArrayList<ConfigExportCSV> resultat, List<String> entete) throws IOException {
		try (FileWriter writer = new FileWriter(csvFile)) {
			if(entete != null) {
				CSVService.writeLine(writer, entete);
			}

			for(ConfigExportCSV line : resultat ) {
				CSVService.writeLine(writer, Arrays.asList(line.getDirectory(), line.getFileName(), line.getNombrePage().toString()));
			}

			writer.flush();
		}
	}

	public static void exportToCsv(String csvFile, ArrayList<ArrayList<String>> resultat) throws IOException {
		try (FileWriter writer = new FileWriter(csvFile)) {
			for(ArrayList<String> line : resultat ) {
				CSVService.writeLine(writer, line);
			}

			writer.flush();
		}
	}

	public static void exportToCsvOdr(CodeEligibleRepository codeEligibleRepository, app.entity.odr.TraitementSql[] traitements, String csvFile, CustomConfigOdr config, DateFormat dateFormat) throws IOException, NumberFormatException, ParseException {
		try (FileWriter writer = new FileWriter(csvFile)) {
			CSVService.writeLine(writer, Arrays.asList("NumeroContratRedbox", "DateEffet", "Nom", "Prenom", "Adresse", "CodePostal", "Ville", "Ctry", "IBAN", "Bic", "Montant"));

			for(app.entity.odr.TraitementSql traitement : traitements) {
				CsvSql odr = traitement.getCsv();
				if(valideDateEligible(traitement, config, odr, false)) {
					String montant = getMontant(codeEligibleRepository, traitement.getOffre(), odr.getProductCode());

					CSVService.writeLine(writer,
							Arrays.asList(odr.getOdrPk().getNbrContractRedbox(), dateFormat.format(odr.getProductSalesDate()),	odr.getClientName(),odr.getCustomerFirstName(),
									odr.getNbrInTheTrack() + " " + odr.getTrackCodeType() + " " + odr.getTrackName(),
									odr.getPostalCode(), odr.getLocation(),"FR", "", "", montant)
							);
				}
			}

			writer.flush();
		}
	}

	public static void exportFullToCsvOdr(app.entity.odr.TraitementSql[] traitements, String csvFile, CustomConfigOdr config, DateFormat dateFormat) throws NumberFormatException, IOException, ParseException {
		try (FileWriter writer = new FileWriter(csvFile)) {
			CSVService.writeLine(writer, Arrays.asList("Sequence","Type SPB","Code ste","Code magasin","N vente","N ligne","Type acte",
					"Nom magasin","Type offre","Date operation","Date delivrance",
					"Code famille","Libelle famille","Ref prestation","Code prestation",
					"Qte prestation","PrixUnit prestation","Code famille produit","Libelle famille produit",
					"Code marque produit","Libelle marque produit","Reference produit","Codic",
					"Qte produit","PrixUnit produit","Produit prixtotal","Id client adresse",
					"Titre client","Nom client","Prenom","N voie","Code type voie",
					"Libelle voie","Code postal","Code insee","Localite",
					"N IMEI","Contexte","Canal","Adresse mail","N contrat redbox","Filler","Formulaire",
					"Bulletin d adhesion","Facture","RIB","Date reception"
					));

			for(app.entity.odr.TraitementSql traitement : traitements) {
				CsvSql odr = traitement.getCsv();

				if(valideDateEligible(traitement, config, odr, true)) {
					CSVService.writeLine(writer, Arrays.asList(
							String.format("%07d" , Integer.parseInt(odr.getSeqNumber())),
							Integer.toString((Integer.parseInt(odr.getRecordType()))),
							odr.getSubsidiaryCode(),
							String.format("%03d" , Integer.parseInt(odr.getStoreCode())),
							odr.getPurchaseOrderNumber(),
							String.format("%05d" , Integer.parseInt(odr.getLinenumber())),
							odr.getOdrPk().getTransactionType(),
							odr.getStoreName(),
							odr.getPaymentType(),
							dateFormat.format(odr.getProductSalesDate()),
							dateFormat.format(odr.getWarrantySalesDate()),
							odr.getFamilyInsuranceCode(),
							odr.getFamilyInsuranceLabel(),
							odr.getNameofService(),
							odr.getProductCode(),
							String.format("%05d" , Integer.parseInt(odr.getQuantitySold())),
							odr.getPrixUnitProvision(),
							odr.getFamilyProductCode(),
							odr.getFamilyProductLabel(),
							odr.getProductBrandCode(),
							odr.getBrandNameProduct(),
							odr.getProductReference(),
							odr.getCodic(),
							String.format("%05d" , Integer.parseInt(odr.getProductQty())),
							odr.getPrixUnitProduct(),
							odr.getProductPrixTotal(),
							odr.getClientID(),
							odr.getCustomerTitle(),
							odr.getClientName(),
							odr.getCustomerFirstName(),
							odr.getNbrInTheTrack(),
							odr.getTrackCodeType(),
							odr.getTrackName(),
							odr.getPostalCode(),
							odr.getCodeINSEE(),
							odr.getLocation(),
							odr.getImeiNumber(),
							odr.getTypeOfSale(),
							odr.getSalesChannel(),
							odr.getEmailAdress(),
							odr.getOdrPk().getNbrContractRedbox(),
							traitement.getFiller(),
							traitement.getFormulaire().getOutput().toString(),
							traitement.getBulletin().getOutput().toString(),
							traitement.getFacture().getOutput().toString(),
							traitement.getRib().getOutput().toString(),
							dateFormat.format(traitement.getDateReception())));
				}
			}

			writer.flush();
		}
	}

	public static void exportMailToCsvOdr(CodeEligibleRepository codeEligibleRepository, app.entity.odr.TraitementSql[] traitements, String csvFile, CustomConfigOdr config, DateFormat dateFormat) throws IOException, NumberFormatException, ParseException {
		try (FileWriter writer = new FileWriter(csvFile)) {
			CSVService.writeLine(writer, Arrays.asList("Adresse mail", "Numero Contrat Redbox", "Filler", "Formulaire", "Bulletin d adhesion", "Facture", "RIB", "Date reception", "Date de traitement", "Titre client", "Nom ", "Prenom", "Offre", "Montant", "Code magasin", "Type d acte","Date operation","Date delivrance","Code prestation"));

			for(app.entity.odr.TraitementSql traitement : traitements) {
				CsvSql odr = traitement.getCsv();

				if(valideDateEligible(traitement, config, odr, true)) {
					String montant = getMontant(codeEligibleRepository, traitement.getOffre(), odr.getProductCode());
					CSVService.writeLine(writer,
							Arrays.asList(odr.getEmailAdress(),
									odr.getOdrPk().getNbrContractRedbox(),
									traitement.getFiller(),
									traitement.getFormulaire().toString(),
									traitement.getBulletin().toString(),
									traitement.getFacture().toString(),
									traitement.getRib().toString(),
									dateFormat.format(traitement.getDateReception()),
									dateFormat.format(traitement.getDateTraitement()),
									odr.getCustomerTitle(),
									odr.getClientName(),
									odr.getCustomerFirstName(),
									traitement.getOffre().toString(),
									montant,
									odr.getStoreName(),
									odr.getOdrPk().getTransactionType(),
									dateFormat.format(odr.getProductSalesDate()),
									dateFormat.format(odr.getWarrantySalesDate()),
									odr.getProductCode()
									));
				}
			}

			writer.flush();
		}
	}


	public static void exportToCsvPayeGims(List<app.entity.gims.TraitementSql> ts, String csvFile, DateFormat dateFormat) throws IOException {
		try (FileWriter writer = new FileWriter(csvFile)) {
			CSVService.writeLine(writer, Arrays.asList("t.tierscode", "t.numerofacture", "s.dateaction", "s.action", "t.soldetenuecompte"));

			for(app.entity.gims.TraitementSql t : ts) {
				if(!t.getSuivi().isEmpty()) {
					t.getSuivi().sort(Comparator.comparing(suivi -> suivi.getSuiviGimsPk().getDateAction()));
					for(app.entity.gims.SuiviSql s : t.getSuivi()) {
						CSVService.writeLine(writer, Arrays.asList(
								t.getGimsPk().getTiersCode().toString(),
                                t.getGimsPk().getNumeroFacture(),
								dateFormat.format(s.getSuiviGimsPk().getDateAction()),
								s.getSuiviGimsPk().getAction().toString(),
								t.getSoldeTenueCompte().toString()
								));
					}
				}
			}

			writer.flush();
		}
	}
	
	public static void exportToCsvRafGims(List<app.entity.gims.TraitementSql> ts, String csvFile, DateFormat dateFormat) throws IOException {
		try (FileWriter writer = new FileWriter(csvFile)) {
			CSVService.writeLine(writer, Arrays.asList("t.tierscode", "t.numerofacture", "s.dateaction", "s.action", "t.soldetenuecompte"));

			for(app.entity.gims.TraitementSql t : ts) {
				if(!t.getSuivi().isEmpty()) {
					t.getSuivi().sort(Comparator.comparing(suivi -> suivi.getSuiviGimsPk().getDateAction()));
					for(app.entity.gims.SuiviSql s : t.getSuivi()) {
						CSVService.writeLine(writer, Arrays.asList(
								t.getGimsPk().getTiersCode().toString(),
                                t.getGimsPk().getNumeroFacture(),
								dateFormat.format(s.getSuiviGimsPk().getDateAction()),
								s.getSuiviGimsPk().getAction().toString(),
								t.getSoldeTenueCompte().toString()
						));
					}
				} else {
					CSVService.writeLine(writer, Arrays.asList(
							t.getGimsPk().getTiersCode().toString(),
                            t.getGimsPk().getNumeroFacture(),
							"",
							"",
							t.getSoldeTenueCompte().toString()
					));
				}
			}

			writer.flush();
		}
	}

	private static boolean valideDateEligible(app.entity.odr.TraitementSql trait, CustomConfigOdr config, CsvSql line, boolean full) throws ParseException {
		if((trait == null) || StringUtils.isEmpty(line.getProductCode())) {
			return false;
		}

		if(!full && (trait.getBulletin() == null || trait.getFacture() == null || trait.getFormulaire() == null || trait.getRib() == null)) {
			return false;
		}

		if(!full && (trait.getBulletin().equals(BaType.S)
				&& trait.getFacture().equals(FactType.S)
				&& trait.getFormulaire().equals(FormType.S)
				&& trait.getRib().equals(RibType.S))
				) {
			return true;
		}

        return full && (trait.getBulletin() != null || trait.getFacture() != null || trait.getFormulaire() != null || trait.getRib() != null);
    }

	private static String getMontant(CodeEligibleRepository codeEligibleRepository, Offre offre, String productCode) {
		CodeEligibleSql code = codeEligibleRepository.findByCodeEligible(productCode);
		if(code == null) {
			return "ERROR : CODE NON TROUVE";
		}
		return offre.equals(Offre.ODR) ? code.getOdrPrix().toString() : code.getOdfPrix().toString();
	}
}
