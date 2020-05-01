package utils;

import java.io.File;

import org.apache.log4j.Logger;

import net.sourceforge.tess4j.Tesseract;
import traitement.config.CustomConfigOcr;

public class TesseracService {
	private static Logger logger = Logger.getLogger(TesseracService.class);
	
	private static Tesseract tesseract;
	
	private static CustomConfigOcr config;

	private TesseracService(){}

	public static Tesseract getInstance() throws UnsatisfiedLinkError {
		if(tesseract == null) {
			logger.warn("Initialisation de Tesserac");
			
			tesseract = new Tesseract(); 
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
		}
		return tesseract;
	}

	public static void setConfig(CustomConfigOcr config) {
		logger.warn("Initialisation de la configuration de Tesserac");
		TesseracService.config = config;
		tesseract = null;
	}
}
