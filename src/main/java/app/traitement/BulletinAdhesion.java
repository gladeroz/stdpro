package app.traitement;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections.IteratorUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.MappingIterator;

import app.entity.CodeEligibleSql;
import app.entity.CsvSql;
import app.entity.TraitementSql;
import app.entity.pk.OdrPk;
import app.model.ConfigItem;
import app.model.ConfigOdrJson;
import app.model.ConfigOdrRefCsv;
import app.model.ConfigOdrTraiteCsv;
import app.model.ConfigStore;
import app.repository.CodeEligibleRepository;
import app.repository.CsvRepository;
import app.repository.TraitementRepository;
import app.service.MainRepository;
import app.traitement.config.CustomConfigOdr;
import app.traitement.enums.CustomEnumOdr;
import enums.Offre;
import enums.TransactionType;
import enums.odrodf.BaType;
import utils.CSVService;
import utils.JsonService;

@Component
public class BulletinAdhesion {

	private static Logger logger = Logger.getLogger(BulletinAdhesion.class);

	private static int MAXYEARS = 5;

	public static CustomConfigOdr initConfig(Collection<ConfigItem> config) {
		CustomConfigOdr cc = new CustomConfigOdr();

		for(ConfigItem item : config) {
			if(item.getConfigName().equals(CustomEnumOdr.MIGRATION.getValue())) {
				if(item.getMandatory() && ! Traitement.variableExist(item.getValue())) return null;
				cc.setMigration(new Boolean(item.getValue()));
			}

			if(item.getConfigName().equals(CustomEnumOdr.REFERENTIAL.getValue())) {
				if(item.getMandatory() && ! Traitement.variableExist(item.getValue())) return null;
				cc.setReferential(item.getValue());
			}

			if(item.getConfigName().equals(CustomEnumOdr.DELTA.getValue())) {
				if(item.getMandatory() && ! Traitement.variableExist(item.getValue())) return null;
				cc.setDelta(item.getValue());
			}

			if(item.getConfigName().equals(CustomEnumOdr.DOC_TRAITE.getValue())) {
				if(item.getMandatory() && ! Traitement.variableExist(item.getValue())) return null;
				cc.setDocTraite(item.getValue());
			}

			if(item.getConfigName().equals(CustomEnumOdr.EXPORTCSV.getValue())) {
				if(item.getMandatory() && ! Traitement.variableExist(item.getValue())) return null;
				cc.setExportcsv(item.getValue());
			}

			if(item.getConfigName().equals(CustomEnumOdr.INTERVALMIN.getValue())) {
				if(item.getMandatory() && ! Traitement.variableExist(item.getValue())) return null;
				cc.setIntervalMin(item.getValue());
			}

			if(item.getConfigName().equals(CustomEnumOdr.INTERVALMAX.getValue())) {
				if(item.getMandatory() && ! Traitement.variableExist(item.getValue())) return null;
				cc.setIntervalMax(item.getValue());
			}
		}

		return cc;
	}

	public void traitement(Collection<ConfigItem> config) throws Exception, UnsatisfiedLinkError {
		logger.info("Traitement 'Odr' en cours");

		logger.debug("Configuration en cours de traitement");
		CustomConfigOdr conf = initConfig(config);

		if(conf == null) {
			logger.error("La Configuration comporte des erreurs ou il manque un parametre");
			return;
		}

		logger.debug("Lancement du Traitement : " + new Date());
		job(conf);
		logger.debug("Fin du Traitement : " + new Date());
	}

	public void job(CustomConfigOdr config) throws Exception {
		long startTime = System.nanoTime();

		odr(config);

		long endTime = System.nanoTime();

		logger.info("Temps de Traitement : " + TimeUnit.SECONDS.convert((endTime - startTime), TimeUnit.NANOSECONDS) + " secondes");
	}

	private void odr(CustomConfigOdr config) throws Exception, UnsatisfiedLinkError {

		//int countAlreadyExist = 0, countAdd = 0;

		TraitementRepository traitementRepository = MainRepository.getTraitementRepository();
		CsvRepository csvRepository = MainRepository.getCsvRepository();
		CodeEligibleRepository codeEligibleRepository = MainRepository.getCodeEligibleRepository();

		if(config.getMigration()) {
			if(!Traitement.variableExist(config.getReferential())) {
				logger.error("Vous devez specifie le chemin vers l ancienne base de donnees");
				return;
			}

			File json = new File(config.getReferential());
			ConfigStore store = JsonService.getInstance().readValue(json, ConfigStore.class);

			/** Ajout des donn�es dans la table traitement**/
			logger.info("Ajout des donnees dans la base de donnees");
			for (ConfigOdrJson line : store.getStore()) {
				CsvSql c = csvRepository.findByOdrPk(new OdrPk(line.getContrat(), line.getOdr().getTransactionType()));

				if(c == null) {
					c = new CsvSql(line.getOdr(), line.getOdr().getTransactionType());

					if(line.getTraitement().getNbrContractRedbox() != null) {
						c.setTraitement(new TraitementSql(line.getTraitement(), line.getOdr().getTransactionType()));
					}

					csvRepository.save(c);
				}
			}
		}

		logger.info("Nombre totale de traitements en base : " + traitementRepository.count());
		logger.info("Nombre totale de odr/odt en base : " + csvRepository.count());

		//majDelta(config, store);
		majDelta(csvRepository, config);

		//majDocTraite(config, store);
		majDocTraite(codeEligibleRepository, traitementRepository, csvRepository, config);

		//updateJsonRef(json, store);

		//exportToCsv(config, store);
		exportToCsv(traitementRepository, csvRepository, config);
	}

	private void exportToCsv(TraitementRepository traitementRepository, CsvRepository csvRepository, CustomConfigOdr config) throws NumberFormatException, IOException, ParseException {
		if(Traitement.variableExist(config.getExportcsv())) {
			DateFormat exportFormat = new SimpleDateFormat("yyyyMMdd");
			DateFormat varFormat = new SimpleDateFormat("yyyy-MM-dd");

			boolean minExist = Traitement.variableExist(config.getIntervalMin());
			boolean maxExist = Traitement.variableExist(config.getIntervalMax());

			TraitementSql[] odrs = null;
			if(minExist && maxExist) {
				odrs  = traitementRepository.findAllByDateTraitementGreaterThanEqualAndDateTraitementLessThanEqual(varFormat.parse(config.getIntervalMin()), varFormat.parse(config.getIntervalMax()));
			}else if(minExist) {
				odrs  = traitementRepository.findAllByDateTraitementGreaterThanEqual(varFormat.parse(config.getIntervalMin()));
			}else{
				odrs  = traitementRepository.findAllByDateTraitementLessThanEqual(varFormat.parse(config.getIntervalMax()));
			}

			logger.info("Export du resultat en CSV : " + config.getExportcsv());
			Traitement.exportToCsvOdr(odrs, Traitement.withSlash(config.getExportcsv()) + "ASSURANT_CUSTOMER_BANKINFO_" + exportFormat.format(new Date())+".csv" , config, varFormat);

			logger.info("Export Full du resultat en CSV : " + config.getExportcsv());
			Traitement.exportFullToCsvOdr(odrs, Traitement.withSlash(config.getExportcsv()) + "ASSURANT_REPORT_ODR_" + exportFormat.format(new Date())+".csv" , config, varFormat);

			logger.info("Traitement pour les mails du resultat en CSV : " + config.getExportcsv());
			Traitement.exportMailToCsvOdr(odrs, Traitement.withSlash(config.getExportcsv()) + "TRAITEMENT_MAIL_" + exportFormat.format(new Date())+".csv" , config, varFormat);
		}
	}

	private void majDocTraite(CodeEligibleRepository codeEligibleRepository, TraitementRepository traitementRepository, CsvRepository csvRepository, CustomConfigOdr config) throws JsonParseException, JsonMappingException, UnsatisfiedLinkError, IOException, Exception {
		if(Traitement.variableExist(config.getDocTraite())) {

			logger.info("Mise a jour de la BDD avec les documents traites");
			MappingIterator<ConfigOdrTraiteCsv> traitement = CSVService.getOdrdata(config.getDocTraite(), false, ConfigOdrTraiteCsv.class);

			if(codeEligibleRepository.count() == 0) {
				initTableCodeEligible(codeEligibleRepository);
			}

			@SuppressWarnings("unchecked")
			List<CodeEligibleSql> eligiblite = IteratorUtils.toList(codeEligibleRepository.findAll().iterator());

			while(traitement.hasNext()) {
				ConfigOdrTraiteCsv importCsv = traitement.next();
				if(importCsv.getNbrContractRedbox().isEmpty()) continue;

				logger.info("Import du numero de contrat traite : {"+ importCsv.getNbrContractRedbox()+"}");

				CsvSql itemVte = csvRepository.findByOdrPk(new OdrPk(importCsv.getNbrContractRedbox(), TransactionType.VTE.toString()));
				CsvSql itemRes = csvRepository.findByOdrPk(new OdrPk(importCsv.getNbrContractRedbox(), TransactionType.RES.toString()));

				boolean venteExist = itemVte != null;
				boolean resExist = itemRes != null;

				if(resExist) {
					String numeroContrat = importCsv.getNbrContractRedbox();
					String productCode = itemRes.getProductCode();

					if(! eligiblite.contains(productCode)) {
						logger.warn("Le contrat [" + numeroContrat + "] n est pas eligible [product code :" + productCode + "]");
						changeValueType(importCsv, BaType.NS_NOT_ELI);
					}

					Calendar dRef = Calendar.getInstance();
					dRef.setTime(itemRes.getProductSalesDate());

					Calendar dImport = Calendar.getInstance();
					dImport.setTime(importCsv.getDateReception());

					if(importCsv.getOffre().equals(Offre.ODR)) {
						dRef.add(Calendar.DAY_OF_MONTH, 30);
						if(dImport.after(dRef)) {
							logger.warn("Le contrat [" + numeroContrat + "] n est pas eligible [date depassee pour le type ODR]");
							changeValueType(importCsv, BaType.NS_ODR_HD);
						}
					} else if(importCsv.getOffre().equals(Offre.ODF)) {
						boolean found = false;

						//On itere sur 5 ans
						for(int i = 1; i <= MAXYEARS; i++) {
							found = intervalOdf(importCsv, dRef, dImport, i);
							if(found) break;
						}

						if(!found) {
							logger.warn("Le contrat [" + numeroContrat + "] n est pas eligible [Avant Terme]");
							changeValueType(importCsv, BaType.NS_ODF_AT);
						}
					}
				}

				if(resExist) {
					changeValueType(importCsv, BaType.NS_RES);
					traitementRepository.save(new TraitementSql(importCsv, TransactionType.RES.toString()));
				}

				if(venteExist) {
					traitementRepository.save(new TraitementSql(importCsv, TransactionType.VTE.toString()));
				}

				if(!venteExist) {
					logger.warn("Le numero de contrat "+ importCsv.getNbrContractRedbox() +" n est pas dans la base");
				}
			}
		}
	}


	private void initTableCodeEligible(CodeEligibleRepository codeEligibleRepository) {
		logger.info("Injection des codes Eligibles");
		List<String> eligiblite = new ArrayList<String>(Arrays.asList(
				"24021","24023","24024","24026","24028",
				"24032","24053","24054","22368","22370",
				"22372","22374","22375","22377","22379",
				"22380","18274","18275","18276","18277",
				"22382","22385","22384","22387","24064",
				"24066","24065","24067","25699","25698",
				"25700","25701","25702","25703","25704",
				"25705","25706","25707","25708","25709",
				"25710","25711","25712","25713"
				));

		for(String i : eligiblite) {
			codeEligibleRepository.save(new CodeEligibleSql(i));
		}
	}

	private void majDelta(CsvRepository csvRepository, CustomConfigOdr config) throws Exception {
		if(Traitement.variableExist(config.getDelta())) {

			logger.info("Mise a jour de la BDD avec un delta");
			MappingIterator<ConfigOdrRefCsv> delta = CSVService.getOdrdata(config.getDelta(), false, ConfigOdrRefCsv.class);

			while(delta.hasNext()) {
				ConfigOdrRefCsv odrDelta = delta.next();
				if(odrDelta.getNbrContractRedbox().isEmpty()) continue;

				logger.info("Import du numero de contrat en delta : {"+ odrDelta.getNbrContractRedbox()+"}");

				CsvSql c = csvRepository.findByOdrPk(new OdrPk(odrDelta.getNbrContractRedbox(), odrDelta.getTransactionType()));
				if(c != null) {
					String msg = "";

					if(odrDelta.getTransactionType().equals(TransactionType.VTE.toString()) && c.getOdrPk().getTransactionType().equals(odrDelta.getTransactionType())) {
						// => rejet
						throw new Exception("[ Vente deja presente pour le numero de contrat "+ odrDelta.getNbrContractRedbox() +" ]");
					}

					if(odrDelta.getTransactionType().equals(TransactionType.RES.toString()) && c.getOdrPk().getTransactionType().equals(odrDelta.getTransactionType())) {
						// => rejet
						throw new Exception("[ Resilliation deja presente pour le numero de contrat "+ odrDelta.getNbrContractRedbox() +" ]");
					}

					if(! msg.isEmpty()) {
						logger.warn(msg);
					}
				}

				csvRepository.save(new CsvSql(odrDelta, odrDelta.getTransactionType()));
			}
		}
	}

	private static boolean intervalOdf(ConfigOdrTraiteCsv importCsv, Calendar dRef, Calendar dImport, int iteration) {

		Calendar dbefore = Calendar.getInstance();
		dbefore.setTime(dRef.getTime());
		dbefore.add(Calendar.YEAR, iteration);

		if(dImport.after(dbefore) || dImport.equals(dbefore)) {
			Calendar dafter = Calendar.getInstance();
			dafter.setTime(dbefore.getTime());
			dafter.add(Calendar.MONTH, 2);

			if(dImport.before(dafter) || dImport.equals(dafter)) {
				//logger.warn("Le contrat [" + importCsv.getNbrContractRedbox() + "] a ete trouve ["+ s.format(dbefore.getTime()) +" < "+ s.format(dImport.getTime()) +" < "+ s.format(dafter.getTime()) +"]");
				return true;
			}
		}

		return false;
	}

	private static void changeValueType (ConfigOdrTraiteCsv line, BaType type) {
		if(!type.equals(BaType.NV)) {
			line.setBulletin(type);
		}
	}
}
