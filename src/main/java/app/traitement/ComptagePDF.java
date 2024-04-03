package app.traitement;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.itextpdf.text.pdf.PdfReader;

import app.model.ConfigExportCSV;
import app.model.ConfigItem;
import app.traitement.config.CustomConfigComptage;
import app.traitement.enums.CustomEnumComptage;
import enums.Extension;

public class ComptagePDF {
	
	private static Logger logger = Logger.getLogger(ComptagePDF.class);

	public static CustomConfigComptage initConfig(Collection<ConfigItem> config) {
		CustomConfigComptage cc = new CustomConfigComptage();

		for(ConfigItem item : config) {
			if(item.getConfigName().equals(CustomEnumComptage.PATH.getValue())) {
				if(item.getMandatory() && ! Traitement.variableExist(item.getValue())) return null;
				cc.setPath(item.getValue());
			}
			
			if(item.getConfigName().equals(CustomEnumComptage.EXPORTCSV.getValue())) {
				if(item.getMandatory() && ! Traitement.variableExist(item.getValue())) return null;
				cc.setExportcsv(item.getValue());
			}
		}

		return cc;
	}

	public static void traitement(Collection<ConfigItem> config) {
		logger.info("Traitement 'Comptage des PDFs' en cours");

		logger.debug("Configuration en cours de traitement");
		CustomConfigComptage conf = initConfig(config);
		
		if(conf == null) {
			logger.error("La Configuration comporte des erreurs ou il manque un parametre");
			return;
		}

		logger.debug("Lancement du Traitement : " + new Date());
		try{
			job(conf);
		}catch (Exception e) {
			logger.error(e.getMessage());
		}
		logger.debug("Fin du Traitement : " + new Date());
	}

	public static void job(CustomConfigComptage config) throws IOException {
		ArrayList<ConfigExportCSV> resultat = new ArrayList<>();
		long startTime = System.nanoTime();

		listDirectory(Traitement.withSlash(config.getPath()), "", resultat);
		
		if(Traitement.variableExist(config.getExportcsv())) {
			logger.info("Export du resultat en CSV : " + config.getExportcsv());
			Traitement.exportToCsv(config.getExportcsv(), resultat, Arrays.asList("FOLDER", "FILE NAME", "LINE COUNT"));
		}

		long endTime = System.nanoTime();

		logger.info("Temps de Traitement : " + TimeUnit.SECONDS.convert((endTime - startTime), TimeUnit.NANOSECONDS) + " secondes");
	}

	public static void listDirectory(String parentDir, String currentDir, ArrayList<ConfigExportCSV> resultat) throws IOException {
		String dirToList = Traitement.withSlash(parentDir);
		if (!currentDir.equals("")) {
			dirToList += Traitement.withSlash(currentDir);
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
					listDirectory(dirToList, currentFileName, resultat);
				} else if(currentFileName.toUpperCase().endsWith(Extension.PDF.name())){
					Integer nbTemp = nbPagesPdf(Traitement.withSlash(dirToList) + currentFileName);
					logger.info("[Dossier : " + dirToList + "][Fichier : " + currentFileName + "][Nombre de page : " + nbTemp + "]");
					resultat.add(new ConfigExportCSV(dirToList, currentFileName, nbTemp));
				}
			}
		}
	}
	
	private static Integer nbPagesPdf(String pdf) throws IOException{
		PdfReader p = new PdfReader(pdf);
		Integer retur = p.getNumberOfPages();
		p.close();
		return retur;
	}
}
