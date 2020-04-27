package traitement;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import enums.Extension;
import enums.LogLevel;
import model.ConfigItem;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import net.sourceforge.tess4j.util.PdfUtilities;
import traitement.config.CustomConfigOcr;
import traitement.enums.CustomEnumOcr;
import utils.Logger;
import utils.Traitement;

public class Ocr {

	public static CustomConfigOcr initConfig(Collection<ConfigItem> config) {
		CustomConfigOcr cc = new CustomConfigOcr();

		for(ConfigItem item : config) {
			if(item.getConfigName().equals(CustomEnumOcr.PATH.getValue())) {
				if(item.getValue() == null) return null;
				cc.setPath(item.getValue());
			}
			
			if(item.getConfigName().equals(CustomEnumOcr.TESS4J.getValue())) {
				if(item.getValue() == null) return null;
				cc.setTess4j(item.getValue());
			}
		}

		return cc;
	}

	public static void traitement(Collection<ConfigItem> config) {
		Logger.print(LogLevel.INFO, "Traitement 'OCR' en cours");

		Logger.print(LogLevel.DEBUG, "Configuration en cours de traitement");
		CustomConfigOcr conf = initConfig(config);
		
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

	public static void job(CustomConfigOcr config) throws IOException {
		long startTime = System.nanoTime();

		ocr(config);

		long endTime = System.nanoTime();

		Logger.print(LogLevel.INFO, "Temps de Traiment : " + TimeUnit.SECONDS.convert((endTime - startTime), TimeUnit.NANOSECONDS) + " secondes");
	}

	private static void ocr(CustomConfigOcr config) throws IOException {
		Tesseract tesseract = new Tesseract(); 
        tesseract.setDatapath(Traitement.withoutSlash(config.getTess4j())); 
        tesseract.setLanguage("fra");
        tesseract.setTessVariable("user_defined_dpi", "300");      
        
        if (System.getProperty("os.name").contains("Windows")) {
        	boolean is64bit = (System.getenv("ProgramFiles(x86)") != null);
        	if(is64bit) {
                System.load(Traitement.withSlash(config.getTess4j()) + "lib"+ File.separator + "win32-x86-64" + File.separator + "gsdll64.dll");
        	} else {
                System.load(Traitement.withSlash(config.getTess4j()) + "lib"+ File.separator + "win32-x86" + File.separator + "gsdll32.dll");
        	}
        } 
        

        try { 
        	
        	File f =  new File(config.getPath()); 
    		File[] subFiles = f.listFiles();
  
    		if (subFiles != null && subFiles.length > 0) {
    			for (File aFile : subFiles) {
    				String currentFileName = aFile.getName();
    				if (currentFileName.equals(".") || currentFileName.equals("..")) {
    					continue;
    				}
    				
    				if(currentFileName.toUpperCase().endsWith(Extension.PDF.name())){
    					String NEWFILE = Traitement.withSlash(config.getPath()) + currentFileName;
    					Logger.print(LogLevel.INFO, "[Fichier en cours : " + NEWFILE + "]");
    		            File[] png = PdfUtilities.convertPdf2Png(new File(NEWFILE));
    		            
    		            Logger.print(LogLevel.INFO, "[Fichier convertit en png]");
    		            
    		            // the path of your tess data folder 
    		            // inside the extracted file 
    		            String text = tesseract.doOCR(png[0]); 
    		  
    		            // path of your image file 
    		            Logger.print(LogLevel.INFO, "[OCR] " + text); 
    				}
    			}
    		}
        } catch (TesseractException e) { 
            Logger.print(LogLevel.ERROR, e.getMessage()); 
        } 
	}
}
