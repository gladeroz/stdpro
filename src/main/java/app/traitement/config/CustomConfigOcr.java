package app.traitement.config;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomConfigOcr {
	private String path;
	private String tess4j;
	private String pattern;
	private String subSearch;
	private Boolean rename;
	private Boolean ocr;
	private String x;
	private String y;
	private String width;
	private String height;
}
