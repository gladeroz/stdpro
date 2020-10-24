package traitement;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
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

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectWriter;

import enums.Offre;
import enums.TransactionType;
import enums.odrodf.BaType;
import model.ConfigItem;
import model.ConfigOdrJson;
import model.ConfigOdrRefCsv;
import model.ConfigOdrTraiteCsv;
import model.ConfigStore;
import model.ConfigStoreTraite;
import traitement.config.CustomConfigOdr;
import traitement.enums.CustomEnumOdr;
import utils.CSVService;
import utils.JsonService;
import utils.Traitement;

public class BulletinAdhesion {

	private static Logger logger = Logger.getLogger(BulletinAdhesion.class);

	private static int MAXYEARS = 5;

	public static CustomConfigOdr initConfig(Collection<ConfigItem> config) {
		CustomConfigOdr cc = new CustomConfigOdr();

		for(ConfigItem item : config) {
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

	public static void traitement(Collection<ConfigItem> config) throws Exception, UnsatisfiedLinkError {
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

	public static void job(CustomConfigOdr config) throws Exception {
		long startTime = System.nanoTime();

		odr(config);

		long endTime = System.nanoTime();

		logger.info("Temps de Traitement : " + TimeUnit.SECONDS.convert((endTime - startTime), TimeUnit.NANOSECONDS) + " secondes");
	}

	private static void odr(CustomConfigOdr config) throws Exception, UnsatisfiedLinkError {

		File json = Traitement.variableExist(config.getReferential()) ? new File(config.getReferential()) : prepareCsvToJsonRef() ;
		ConfigStore store = JsonService.getInstance().readValue(json, ConfigStore.class);

		majDelta(config, store);

		majDocTraite(config, store);

		updateJsonRef(json, store);

		exportToCsv(config, store);
	}

	private static void exportToCsv(CustomConfigOdr config, ConfigStore store) throws IOException, ParseException {
		if(Traitement.variableExist(config.getExportcsv())) {
			DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

			logger.info("Export du resultat en CSV : " + config.getExportcsv());
			Traitement.exportToCsvOdr(Traitement.withSlash(config.getExportcsv()) + "ASSURANT_CUSTOMER_BANKINFO_" + dateFormat.format(new Date())+".csv" , store, config, dateFormat);

			logger.info("Export Full du resultat en CSV : " + config.getExportcsv());
			Traitement.exportFullToCsvOdr(Traitement.withSlash(config.getExportcsv()) + "ASSURANT_REPORT_ODR_" + dateFormat.format(new Date())+".csv" , store, config, dateFormat);

			logger.info("Traitement pour les mails du resultat en CSV : " + config.getExportcsv());
			Traitement.exportMailToCsvOdr(Traitement.withSlash(config.getExportcsv()) + "TRAITEMENT_MAIL_" + dateFormat.format(new Date())+".csv" , store, config, dateFormat);
		}
	}

	private static void majDocTraite(CustomConfigOdr config, ConfigStore store) throws JsonParseException, JsonMappingException, UnsatisfiedLinkError, IOException, Exception {
		if(Traitement.variableExist(config.getDocTraite())) {

			logger.info("Mise a jour de la BDD avec les documents traites");
			ConfigStoreTraite traitement = JsonService.getInstance().readValue(prepareCsvToJsonTraite(config.getDocTraite()), ConfigStoreTraite.class);

			List<String> eligiblite = new ArrayList<String>(Arrays.asList(
					"24021","24023","24024","24026","24028",
					"24032","24053","24054","22368","22370",
					"22372","22374","22375","22377","22379",
					"22380","18274","18275","18276","18277",
					"22382","22385","22384","22387","24064",
					"24066","24065","24067"	
					));

			for(ConfigOdrTraiteCsv importCsv : traitement.getStore()) {
				boolean venteExist = false;
				ConfigOdrTraiteCsv tmpTraite = null;
				ConfigOdrJson tmpLine = null;

				boolean exist = false;

				for (ConfigOdrJson line : store.getStore()) {
					if(importCsv.getNbrContractRedbox().equals(line.getContrat())) {

						exist = true;

						if(! eligiblite.contains(line.getOdr().getProductCode())) {
							logger.warn("Le contrat [" + importCsv.getNbrContractRedbox() + "] n est pas eligible [product code :" + line.getOdr().getProductCode() + "]");
							changeValueType(importCsv, BaType.NS_NOT_ELI);
						}

						Calendar dRef = Calendar.getInstance();
						dRef.setTime(line.getOdr().getProductSalesDate());

						Calendar dImport = Calendar.getInstance();
						dImport.setTime(importCsv.getDateReception());

						if(importCsv.getOffre().equals(Offre.ODR)) {
							dRef.add(Calendar.DAY_OF_MONTH, 30);
							if(dImport.after(dRef)) {
								logger.warn("Le contrat [" + importCsv.getNbrContractRedbox() + "] n est pas eligible [date depassee pour le type ODR]");
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
								logger.warn("Le contrat [" + importCsv.getNbrContractRedbox() + "] n est pas eligible [Avant Terme]");
								changeValueType(importCsv, BaType.NS_ODF_AT);
							}
						}

						if(line.getOdr().getTransactionType().equals(TransactionType.RES.toString())) {
							changeValueType(importCsv, BaType.NS_RES);
							line.setTraitement(importCsv);
							venteExist = false;
							continue;
						}else if(line.getOdr().getTransactionType().equals(TransactionType.VTE.toString())){
							tmpTraite = importCsv;
							tmpLine = line;
							venteExist = true;
						}
					}
				}

				if(venteExist) {
					tmpLine.setTraitement(tmpTraite);
				}

				if(! exist) {
					logger.warn("Le numero de contrat "+ importCsv.getNbrContractRedbox() +" n est pas dans la base");
				}
			}
		}
	}

	private static boolean intervalOdf(ConfigOdrTraiteCsv importCsv, Calendar dRef, Calendar dImport, int iteration) {
		//SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");

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

	private static void majDelta(CustomConfigOdr config, ConfigStore store) throws Exception {
		if(Traitement.variableExist(config.getDelta())) {

			logger.info("Mise a jour de la BDD avec un delta");
			ConfigStore delta = JsonService.getInstance().readValue(prepareCsvToJsonRef(config.getDelta(), true), ConfigStore.class);

			for(ConfigOdrJson importCsv : delta.getStore()) {
				ConfigOdrRefCsv odrDelta = importCsv.getOdr();

				String msg = "";
				for (ConfigOdrJson line : store.getStore()) {
					ConfigOdrRefCsv odrLine = line.getOdr();

					//si trouve
					if(odrDelta.getNbrContractRedbox().equals(line.getContrat())) {
						if(odrDelta.getTransactionType().equals(TransactionType.VTE.toString()) && odrLine.getTransactionType().equals(odrDelta.getTransactionType())) {
							// => rejet
							throw new Exception("[ Vente deja presente pour le numero de contrat "+ odrDelta.getNbrContractRedbox() +" ]");
						}

						if(odrDelta.getTransactionType().equals(TransactionType.RES.toString()) && odrLine.getTransactionType().equals(odrDelta.getTransactionType())) {
							// => rejet
							throw new Exception("[ Resilliation deja presente pour le numero de contrat "+ odrDelta.getNbrContractRedbox() +" ]");
						}

						//if(odrDelta.getTransactionType().equals(TransactionType.RES.toString()) && ! odrLine.getTransactionType().equals(odrDelta.getTransactionType())) {
						//	msg = "La vente du contrat " + odrDelta.getNbrContractRedbox() + " a ete resilliee";
						//}
					}
				}

				if(! msg.isEmpty()) {
					logger.warn(msg);
				}

				store.getStore().add(importCsv);
			}
		}
	}

	//apply traitement
	private static File prepareCsvToJsonTraite(String path) throws Exception, UnsatisfiedLinkError {
		return prepareCsvToJsonTraite(CSVService.getOdrdata(path, false, ConfigOdrTraiteCsv.class));
	}

	//apply delta
	private static File prepareCsvToJsonRef(String path, boolean print) throws Exception, UnsatisfiedLinkError {
		return prepareCsvToJsonRef(CSVService.getOdrdata(path, false, ConfigOdrRefCsv.class), print);
	}

	//init referential
	private static File prepareCsvToJsonRef() throws Exception, UnsatisfiedLinkError {
		return prepareCsvToJsonRef(CSVService.getOdrdata(), false);	
	}

	private static File prepareCsvToJsonTraite(MappingIterator<ConfigOdrTraiteCsv> it) throws Exception, UnsatisfiedLinkError {
		ConfigStoreTraite store = new ConfigStoreTraite();
		store.setStore(new ArrayList<ConfigOdrTraiteCsv>());

		int i = 0;
		while (it.hasNext()) {
			i++;

			ConfigOdrTraiteCsv c = it.next();

			if(c.getNbrContractRedbox().isEmpty()) {
				logger.warn("Ligne " + i + " ignoree [cause : contrat red box vide]");
				continue;
			}

			if(c.getDateReception() == null) {
				logger.warn("Ligne " + i + " ignoree [cause : date de contrat vide]");
				continue;
			}

			store.getStore().add(c);
		}

		Path tempDirWithPrefix = Files.createTempFile("std", null);
		logger.info(tempDirWithPrefix);

		updateJsonRef(tempDirWithPrefix.toFile(), store);

		return tempDirWithPrefix.toFile();
	}


	private static File prepareCsvToJsonRef(MappingIterator<ConfigOdrRefCsv> it, boolean print) throws UnsatisfiedLinkError, Exception {
		ConfigStore store = new ConfigStore();
		//store.setStore(it.readAll());

		store.setStore(new ArrayList<ConfigOdrJson>());

		int i = 0;
		while (it.hasNext()) {
			i++;

			ConfigOdrRefCsv c = it.next();

			if(c.getNbrContractRedbox().isEmpty()) {
				if (print) {
					logger.warn("Ligne " + i + " ignoree [cause : contrat red box vide]");
				}
				continue;
			}

			if(c.getProductCode().isEmpty()) {
				logger.warn("Ligne " + i + " ignoree [cause : code produit vide]");
				continue;
			}

			store.getStore().add(new ConfigOdrJson(c.getNbrContractRedbox(), c, new ConfigOdrTraiteCsv()));
		}

		Path tempDirWithPrefix = Files.createTempFile("std", null);
		logger.info(tempDirWithPrefix);

		updateJsonRef(tempDirWithPrefix.toFile(), store);

		return tempDirWithPrefix.toFile();
	}

	private static void updateJsonRef(File output, Object store) throws Exception, UnsatisfiedLinkError {
		logger.info("La BDD a ete mise a jour");

		PrintWriter pwriter = new PrintWriter(output, java.nio.charset.StandardCharsets.UTF_8.name());
		ObjectWriter writer = JsonService.getInstance().writer(new DefaultPrettyPrinter());
		writer.writeValue(pwriter, store);
	}
}
