package utils;

import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;

import net.sourceforge.tess4j.TesseractException;
import net.sourceforge.tess4j.util.PdfUtilities;

public class PdfService {
	
	private static Logger logger = Logger.getLogger(PdfService.class);

	public static String getText(File aFile) throws IOException{
		String text = null;

		PDDocument document = PDDocument.load(aFile);
		try{
			PDFTextStripper stripper = new PDFTextStripper();
			stripper.setSortByPosition(true);
			text = stripper.getText(document);
		} finally {
			if(document != null) {
				document.close();
			}
		}

		return text;
	}

	public static String getText(File aFile, String xStr, String yStr, String widthStr, String heightStr) throws IOException {
		String text = null;
		PDDocument document = PDDocument.load(aFile);
		try {
			PDFTextStripperByArea stripper = new PDFTextStripperByArea();
			stripper.setSortByPosition(true);

			int x = (int) getCmToPoints(Integer.parseInt(xStr));
			int y = (int) getCmToPoints(Integer.parseInt(yStr));
			int width = (int) getCmToPoints(Integer.parseInt(widthStr));
			int height = (int) getCmToPoints(Integer.parseInt(heightStr));

			Rectangle rect = new Rectangle(x, y, width - x, height - y);
			stripper.addRegion("class1", rect);
			stripper.extractRegions(document.getPage(0));

			text = stripper.getTextForRegion("class1");
		}
		finally {
			if(document != null) {
				document.close();
			}
		}

		return text;
	}
	
	public static String getTextOcr(File afile) throws IOException, UnsatisfiedLinkError, TesseractException {
		File[] png = PdfUtilities.convertPdf2Png(afile);
		//logger.info("[Fichier convertit en png]");

		// the path of your tess data folder inside the extracted file 
		return TesseracService.getInstance().doOCR(png[0]); 
	}
	
	public static String getTextOcr(File afile, String xStr, String yStr, String widthStr, String heightStr) throws IOException, UnsatisfiedLinkError, TesseractException {
		File[] png = PdfUtilities.convertPdf2Png(afile);
		//logger.info("[Fichier convertit en png]");
		
		int x = (int) getCmToPixels(Integer.parseInt(xStr));
		int y = (int) getCmToPixels(Integer.parseInt(yStr));
		int width = (int) getCmToPixels(Integer.parseInt(widthStr));
		int height = (int) getCmToPixels(Integer.parseInt(heightStr));

		// the path of your tess data folder inside the extracted file 
		return TesseracService.getInstance().doOCR(png[0], new Rectangle(x, y, width - x, height - y)); 
	}

	public static String getText(File aFile, String x, String y, String width, String height, Boolean ocr, String tess4j) throws IOException, UnsatisfiedLinkError, TesseractException {
		if(ocr) {
			//logger.info("Option OCR renseignee");
			
			if(! Traitement.variableExist(tess4j)) {
				logger.error("La localisation de tess4j n'est pas renseigne");
				return null;
			}
			
			TesseracService.setConfig(tess4j);
			
			if(Traitement.variableExist(x) && Traitement.variableExist(y) && Traitement.variableExist(width) && Traitement.variableExist(height)) {
				return getTextOcr(aFile, x, y, width, height);
			} else {
				return getTextOcr(aFile);
			}
		} else {
			if(Traitement.variableExist(x) && Traitement.variableExist(y) && Traitement.variableExist(width) && Traitement.variableExist(height)) {
				return getText(aFile, x, y, width, height);
			} else {
				return getText(aFile);
			}
		}
	}
	
	private static double getCmToPixels(int x) {
		return  x * 118.11023622;
	}

	private static double getCmToPoints(int x) {
		return x * 28.3465;
	}
}
