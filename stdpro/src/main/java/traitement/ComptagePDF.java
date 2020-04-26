package traitement;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import com.itextpdf.text.pdf.PdfReader;

import enums.Extension;
import enums.LogLevel;
import model.ConfigExportCSV;
import model.ConfigItem;
import traitement.config.CustomConfigComptage;
import traitement.enums.CustomEnumComptage;
import utils.CSVUtils;
import utils.Logger;

public class ComptagePDF {

	public static CustomConfigComptage initConfig(Collection<ConfigItem> config) {
		CustomConfigComptage cc = new CustomConfigComptage();

		for(ConfigItem item : config) {
			if(item.getConfigName().equals(CustomEnumComptage.PATH.getValue())) {
				if(item.getValue() == null) return null;
				cc.setPath(item.getValue());
			}
			
			if(item.getConfigName().equals(CustomEnumComptage.EXPORTCSV.getValue())) {
				if(item.getValue() == null) return null;
				cc.setExportcsv(item.getValue());
			}
		}

		return cc;
	}

	public static void traitement(Collection<ConfigItem> config) {
		Logger.print(LogLevel.INFO, "Traitement 'Comptage des PDFs' en cours");

		Logger.print(LogLevel.DEBUG, "Configuration en cours de traitement");
		CustomConfigComptage conf = initConfig(config);
		
		if(conf == null) {
			Logger.print(LogLevel.ERROR, "La Configuration comporte des erreurs ou il manque un parametre");
			return;
		}

		Logger.print(LogLevel.DEBUG, "Lancement du Traitement : " + new Date());
		try{
			job(conf);
		}catch (Exception e) {
			System.err.println(e);
		}
		Logger.print(LogLevel.DEBUG, "Fin du Traitement : " + new Date());
	}

	public static void job(CustomConfigComptage config) throws IOException {
		ArrayList<ConfigExportCSV> resultat = new ArrayList<>();
		long startTime = System.nanoTime();

		listDirectory(config.getPath(), "", resultat);
		
		if(config.getExportcsv() != null) {
			Logger.print(LogLevel.INFO, "Export du resultat en CSV : " + config.getExportcsv());
			exportToCsv(config.getExportcsv(), resultat);
		}

		long endTime = System.nanoTime();

		Logger.print(LogLevel.INFO, "Temps de Traiment : " + TimeUnit.SECONDS.convert((endTime - startTime), TimeUnit.NANOSECONDS) + " secondes");
	}

	private static void exportToCsv(String csvFile, ArrayList<ConfigExportCSV> resultat) throws IOException {
		FileWriter writer = new FileWriter(csvFile);
		
		CSVUtils.writeLine(writer, Arrays.asList("FOLDER", "FILE NAME", "LINE COUNT"));
		for(ConfigExportCSV line : resultat ) {
			CSVUtils.writeLine(writer, Arrays.asList(line.getDirectory(), line.getFileName(), line.getNombrePage().toString()));
		}
		
		writer.flush();
        writer.close();
	}

	public static void listDirectory(String parentDir, String currentDir, ArrayList<ConfigExportCSV> resultat) throws IOException {
		String dirToList = parentDir;
		if (!currentDir.equals("")) {
			dirToList += (currentDir.endsWith(File.separator)) ? currentDir : File.separator + currentDir;
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
					Integer nbTemp = nbPagesPdf(dirToList + File.separator + currentFileName);
					Logger.print(LogLevel.INFO, "[Dossier : " + dirToList + "][Fichier : " + currentFileName + "][Nombre de page : " + nbTemp + "]");
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
