package app.traitement;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

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

	private static Logger logger = Logger.getLogger(Gims.class);

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

		exportToCsv(traitementRepository, suiviGimsRepository, config);
	}

	private static void exportToCsv(TraitementGimsRepository traitementRepository, SuiviGimsRepository suiviGimsRepository, CustomConfigGims config) throws IOException {
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

	private static void majDocTraite(TraitementGimsRepository traitementRepository, CustomConfigGims config) throws IOException, ParseException {
		if(!Traitement.variableExist(config.getDocTraite())) return;

		MappingIterator<ConfigGimsTraiteCsv> traitement = CSVService.getCsvData(config.getDocTraite(), false, ConfigGimsTraiteCsv.class);
		List<TraitementSql> all = new ArrayList<>();

		logger.info("Reset tous les flags de paiement");
		traitementRepository.updateAllPaye(Boolean.TRUE);
		
		while(traitement.hasNext()) {
			ConfigGimsTraiteCsv importCsv = traitement.next();
			Optional<TraitementSql> t = traitementRepository.findById(new GimsPk(importCsv.getThirdPartyCode(), importCsv.getInvoiceNumber()));

			if(checkDataInvalid(importCsv)) {
				continue;
			}

			if(t.isEmpty()) {
				TraitementSql item = new TraitementSql();
				item.setGimsPk(new GimsPk(importCsv.getThirdPartyCode(), importCsv.getInvoiceNumber()));

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
				//logger.info("Ajout de " + importCsv.getThirdPartyCode() + " en base de données");
			} else {
				//logger.info("[" + importCsv.getThirdPartyCode() + " est passé en payer]");
				TraitementSql a = t.get();
				a.setDebitTenueCompte(importCsv.getDebitBalance());
				a.setCreditTenueCompte(importCsv.getCreditBalance());
				a.setSoldeTenueCompte(importCsv.getAccountBalance());
				a.setPaye(Boolean.FALSE);
				all.add(a);
			}
		}
		
		if(!all.isEmpty()) {
			traitementRepository.saveAll(all);
		}
	}

	private static boolean checkDataInvalid(ConfigGimsTraiteCsv importCsv) {
		if(importCsv.getThirdPartyCode() == null) return true;
		if(StringUtils.isBlank(importCsv.getInvoiceNumber())) return true;

		if(!importCsv.getInvoiceNumber().startsWith("FA24") 
				&& !importCsv.getInvoiceNumber().startsWith("FA25")
				&& !importCsv.getInvoiceNumber().startsWith("AV24")
				&& !importCsv.getInvoiceNumber().startsWith("AV25")
				) return true;

		if(importCsv.getDueDate() == null) return true;
		if(!StringUtils.isBlank(importCsv.getPriority())) return true;
		if(!StringUtils.isBlank(importCsv.getPaymentMethodLabel()) && importCsv.getPaymentMethodLabel().equals("Prélèvement")) return true;
		if(!importCsv.getStatus().equals(StatusGims.ACTIF)) return true;

		return false;
	}

	public static void job(CustomConfigGims config) throws Exception {
		long startTime = System.nanoTime();

		gims(config);

		long endTime = System.nanoTime();

		logger.info("Temps de Traitement : " + TimeUnit.SECONDS.convert((endTime - startTime), TimeUnit.NANOSECONDS) + " secondes");
	}
}
