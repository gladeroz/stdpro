package app.traitement.config;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomConfigExtractZone {
	private String path;
	private String tess4j;
	private String x;
	private String y;
	private String width;
	private String height;
	private String exportcsv;
	private Boolean ocr;
}
