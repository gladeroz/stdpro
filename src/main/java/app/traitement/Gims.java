package app.traitement;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.MappingIterator;

import app.entity.gims.SuiviSql;
import app.entity.gims.TraitementSql;
import app.entity.gims.pk.GimsPk;
import app.entity.gims.pk.SuiviGimsPk;
import app.model.ConfigGimsSuiviCsv;
import app.model.ConfigGimsTraiteCsv;
import app.model.ConfigItem;
import app.repository.gims.SuiviGimsRepository;
import app.repository.gims.TraitementGimsRepository;
import app.service.MainRepository;
import app.traitement.config.CustomConfigGims;
import app.traitement.enums.CustomEnumGims;
import enums.gims.StatusGims;
import utils.CSVService;
import utils.DateService;

public class Gims {

	private static final Logger logger = LogManager.getLogger(Gims.class);

	public static CustomConfigGims initConfig(Collection<ConfigItem> config) {
		CustomConfigGims cc = new CustomConfigGims();

		for(ConfigItem item : config) {
			if(item.getConfigName().equals(CustomEnumGims.DOC_TRAITE.getValue())) {
				if(item.getMandatory() && ! Traitement.variableExist(item.getValue())) {
					return null;
				}
				cc.setDocTraite(item.getValue());
			}
			if(item.getConfigName().equals(CustomEnumGims.DOC_SUIVI.getValue())) {
				if(item.getMandatory() && ! Traitement.variableExist(item.getValue())) {
					return null;
				}
				cc.setDocSuivi(item.getValue());
			}
			if(item.getConfigName().equals(CustomEnumGims.EXPORTCSV.getValue())) {
				if(item.getMandatory() && ! Traitement.variableExist(item.getValue())) {
					return null;
				}
				cc.setExportcsv(item.getValue());
			}
		}
		return cc;
	}

	public static void traitement(Collection<ConfigItem> config) throws Exception, UnsatisfiedLinkError {
		logger.info("Traitement 'Gims' en cours");

		logger.debug("Configuration en cours de traitement");
		CustomConfigGims conf = initConfig(config);

		if(conf == null) {
			logger.error("La Configuration comporte des erreurs ou il manque un parametre");
			return;
		}

		logger.debug("Lancement du Traitement : " + new Date());
		job(conf);
		logger.debug("Fin du Traitement : " + new Date());
	}

	private static void gims(CustomConfigGims config) throws Exception, UnsatisfiedLinkError {
		TraitementGimsRepository traitementRepository = MainRepository.getTraitementGimsRepository();
		SuiviGimsRepository suiviGimsRepository = MainRepository.getSuiviGimsRepository();

		majDocTraite(traitementRepository, config);

		majDocSuivi(traitementRepository, suiviGimsRepository, config);

		exportToCsv(traitementRepository, config);
	}

	private static void exportToCsv(TraitementGimsRepository traitementRepository, CustomConfigGims config) throws IOException {
		if(!Traitement.variableExist(config.getExportcsv())) return;

		DateFormat exportFormat = DateService.getDateFormat();
		DateFormat varFormat = new SimpleDateFormat("dd/MM/yyyy");
		List<TraitementSql> ts = traitementRepository.findByPayeTrue();
		ts.sort(Comparator.comparing((TraitementSql t) -> t.getGimsPk().getTiersCode()).thenComparing(t -> t.getGimsPk().getNumeroFacture()));
		
		logger.info("Export du resultat en CSV : " + config.getExportcsv());
		Traitement.exportToCsvPayeGims(ts, Traitement.withSlash(config.getExportcsv()) + "PAYE_GIMS_" + exportFormat.format(new Date())+".csv", varFormat);
		
		
		ts = traitementRepository.findByPayeFalse();
		Traitement.exportToCsvRafGims(ts, Traitement.withSlash(config.getExportcsv()) + "RAF_GIMS_" + exportFormat.format(new Date())+".csv", varFormat);
	}


	private static void majDocSuivi(TraitementGimsRepository traitementGimsRepository, SuiviGimsRepository suiviGimsRepository, CustomConfigGims config) throws IOException {
		if(!Traitement.variableExist(config.getDocSuivi())) return;

		MappingIterator<ConfigGimsSuiviCsv> traitement = CSVService.getCsvData(config.getDocSuivi(), false, ConfigGimsSuiviCsv.class);
		while(traitement.hasNext()) {
			ConfigGimsSuiviCsv importCsv = traitement.next();
			if(importCsv.getAction() != null) {
				SuiviSql item = new SuiviSql();

				GimsPk key = new GimsPk(importCsv.getThirdPartyCode(), importCsv.getNumeroFacture());
				SuiviGimsPk suiviKey = new SuiviGimsPk(key, importCsv.getDateAction(), importCsv.getAction());
				item.setSuiviGimsPk(suiviKey);

				Optional<TraitementSql> t = traitementGimsRepository.findById(key);
				if(t.isEmpty()) {
					logger.info("[" + importCsv.getThirdPartyCode() + "|" + importCsv.getNumeroFacture() + "] est en anomalie");
					continue;
				} else {
					item.setTraitement(t.get());
				}

				suiviGimsRepository.save(item);
				//logger.info("Ajout de " + importCsv.getThirdPartyCode() + " en base de données");
			}
		}
	}

	private static void majDocTraite(TraitementGimsRepository traitementRepository, CustomConfigGims config) throws IOException {
		if(!Traitement.variableExist(config.getDocTraite())) return;

		MappingIterator<ConfigGimsTraiteCsv> traitement = CSVService.getCsvData(config.getDocTraite(), false, ConfigGimsTraiteCsv.class);

		List<ConfigGimsTraiteCsv> validRows = new ArrayList<>();
		while(traitement.hasNext()) {
			ConfigGimsTraiteCsv importCsv = traitement.next();
			if(!checkDataInvalid(importCsv)) {
				validRows.add(importCsv);
			}
		}

		Map<GimsPk, TraitementSql> existingMap = StreamSupport
				.stream(traitementRepository.findAll().spliterator(), false)
				.collect(Collectors.toMap(TraitementSql::getGimsPk, Function.identity()));

		List<TraitementSql> all = new ArrayList<>();
		for(ConfigGimsTraiteCsv importCsv : validRows) {
			GimsPk pk = new GimsPk(importCsv.getThirdPartyCode(), importCsv.getInvoiceNumber());
			TraitementSql existing = existingMap.get(pk);

			if(existing == null) {
				TraitementSql item = new TraitementSql();
				item.setGimsPk(pk);

				item.setTiersNom(importCsv.getThirdPartyName());
				item.setPriorite(importCsv.getPriority());
				item.setReglementModeLibelle(importCsv.getPaymentMethodLabel());

				item.setStatut(importCsv.getStatus());
				item.setStatutDateDebut(importCsv.getStatusStartDate());
				item.setStatutDateFin(importCsv.getStatusEndDate());
				item.setDateEcriture(importCsv.getEntryDate());

				item.setJournalCode(importCsv.getJournalCode());
				item.setDateEcheance(importCsv.getDueDate());

				item.setDebitTenueCompte(importCsv.getDebitBalance());
				item.setCreditTenueCompte(importCsv.getCreditBalance());
				item.setSoldeTenueCompte(importCsv.getAccountBalance());

				item.setCtEmail(importCsv.getContactEmail());
				item.setTiCtEmail(importCsv.getThirdPartyContactEmail());
				item.setTiCtTelephone(importCsv.getThirdPartyContactPhone());
				item.setCtTelephone(importCsv.getContactPhone());
				item.setTiersAdresse(importCsv.getThirdPartyAddress());
				item.setTiersComplementAdresse(importCsv.getAddressComplement());
				item.setTiersCodePostal(importCsv.getPostalCode());
				item.setTiersVille(importCsv.getCity());

				all.add(item);
			} else {
				existing.setDebitTenueCompte(importCsv.getDebitBalance());
				existing.setCreditTenueCompte(importCsv.getCreditBalance());
				existing.setSoldeTenueCompte(importCsv.getAccountBalance());
				existing.setPaye(Boolean.FALSE);
				all.add(existing);
			}
		}

		logger.info("Reset flags de paiement");
		traitementRepository.updateAllPaye(Boolean.TRUE);

		if(!all.isEmpty()) {
			traitementRepository.saveAll(all);
		}
	}

	private static boolean checkDataInvalid(ConfigGimsTraiteCsv importCsv) {
		if(importCsv.getThirdPartyCode() == null) return true;
		if(StringUtils.isBlank(importCsv.getInvoiceNumber())) return true;

		int currentYear = java.time.Year.now().getValue() % 100;
		int previousYear = currentYear - 1;
		String currentYearStr = String.format("%02d", currentYear);
		String previousYearStr = String.format("%02d", previousYear);

		if(!importCsv.getInvoiceNumber().startsWith("FA" + previousYearStr)
				&& !importCsv.getInvoiceNumber().startsWith("FA" + currentYearStr)
				&& !importCsv.getInvoiceNumber().startsWith("AV" + previousYearStr)
				&& !importCsv.getInvoiceNumber().startsWith("AV" + currentYearStr)
				) return true;

		if(importCsv.getDueDate() == null) return true;
		if(!StringUtils.isBlank(importCsv.getPriority())) return true;
		if(!StringUtils.isBlank(importCsv.getPaymentMethodLabel()) && importCsv.getPaymentMethodLabel().equalsIgnoreCase("Prélèvement")) return true;
        return !importCsv.getStatus().equals(StatusGims.ACTIF);
    }

	public static void job(CustomConfigGims config) throws Exception {
		long startTime = System.nanoTime();

		gims(config);

		long endTime = System.nanoTime();

		logger.info("Temps de Traitement : " + TimeUnit.SECONDS.convert((endTime - startTime), TimeUnit.NANOSECONDS) + " secondes");
	}
}
