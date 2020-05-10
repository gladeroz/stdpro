package utils;

import java.io.File;

import org.apache.log4j.Logger;

import net.sourceforge.tess4j.Tesseract;

public class TesseracService {
	//private static Logger logger = Logger.getLogger(TesseracService.class);
	
	private static Tesseract tesseract;
	
	private static String tess4j;
	
	private static int dpi = 300;

	private TesseracService(){}

	public static Tesseract getInstance() throws UnsatisfiedLinkError {
		if(tesseract == null) {
			//logger.warn("Initialisation de Tesserac");
			
			tesseract = new Tesseract(); 
			tesseract.setDatapath(Traitement.withoutSlash(tess4j)); 
			tesseract.setLanguage("fra");
			tesseract.setTessVariable("user_defined_dpi", Integer.toString(dpi));      

			if (System.getProperty("os.name").contains("Windows")) {
				boolean is64bit = (System.getenv("ProgramFiles(x86)") != null);
				if(is64bit) {
					System.load(Traitement.withSlash(tess4j) + "lib"+ File.separator + "win32-x86-64" + File.separator + "gsdll64.dll");
				} else {
					System.load(Traitement.withSlash(tess4j) + "lib"+ File.separator + "win32-x86" + File.separator + "gsdll32.dll");
				}
			} 
		}
		return tesseract;
	}

	public static void setConfig(String tess4j) {
		//logger.warn("Initialisation de la configuration de Tesserac");
		TesseracService.tess4j = tess4j;
		tesseract = null;
	}

	public static int getDpi() {
		return dpi;
	}

	public static void setDpi(int dpi) {
		TesseracService.dpi = dpi;
	}
}
