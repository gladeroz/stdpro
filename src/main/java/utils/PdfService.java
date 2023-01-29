package utils;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.multi.GenericMultipleBarcodeReader;
import com.google.zxing.multi.MultipleBarcodeReader;

import app.model.BarcodeInfo;
import app.traitement.Traitement;
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
		//File[] png = PdfUtilities.convertPdf2Png(afile);
		//logger.info("[Fichier convertit en png]");

		// the path of your tess data folder inside the extracted file 
		//return TesseracService.getInstance().doOCR(png[0]); 
		return TesseracService.getInstance().doOCR(afile); 
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

	public static BarcodeInfo decodeOneBarcodeWithMBC(File path, String tess4j) throws IOException, NotFoundException  {
		TesseracService.setConfig(tess4j);
		TesseracService.getInstance();

		try {
			File[] png = PdfUtilities.convertPdf2Png(path);
			BufferedImage img = ImageIO.read(png[0]);
			BinaryBitmap bb = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(img)));
			MultipleBarcodeReader mbReader = new GenericMultipleBarcodeReader(new MultiFormatReader());
			Hashtable<DecodeHintType, Object> hints = new Hashtable<>();
			hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
			hints.put(DecodeHintType.POSSIBLE_FORMATS, Arrays.asList(BarcodeFormat.CODE_128, BarcodeFormat.CODE_39));
			Result[] result = mbReader.decodeMultiple(bb, hints);
			
			if(result.length == 0) {
				logger.error("Aucun code barre n'a ete trouve dans ce document");
				return null;
			}
			
			return new BarcodeInfo(result[0].getText(), result[0].getBarcodeFormat().name());
		}catch(IOException e) {
			logger.error(e);
		}
		return null;
	}

	public static List<BarcodeInfo> decodeBarcodeWithMBC(File path, String tess4j) throws IOException  {
		TesseracService.setConfig(tess4j);
		TesseracService.getInstance();

		try {
			File[] png = PdfUtilities.convertPdf2Png(path);
			BufferedImage img = ImageIO.read(png[0]);
			BinaryBitmap bb = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(img)));
			MultipleBarcodeReader mbReader = new GenericMultipleBarcodeReader(new MultiFormatReader());
			Hashtable<DecodeHintType, Object> hints = new Hashtable<>();
			hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
			hints.put(DecodeHintType.POSSIBLE_FORMATS, Arrays.asList(BarcodeFormat.CODE_128, BarcodeFormat.CODE_39));
			List<BarcodeInfo> list = new ArrayList<>();
			for (Result result : mbReader.decodeMultiple(bb, hints)) {
				list.add(new BarcodeInfo(result.getText(), result.getBarcodeFormat().name()));
			}
			return list;
		}catch(IOException e) {
			logger.error(e);
		}catch (NotFoundException e) {logger.error("Aucun code barre n'a ete trouve dans ce document");}
		return new ArrayList<BarcodeInfo>();
	}

	private static double getCmToPixels(int x) {
		return  x * 118.11023622;
	}

	private static double getCmToPoints(int x) {
		return x * 28.3465;
	}
}
