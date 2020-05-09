package traitement.enums;

public enum CustomEnumExtractZone {
	PATH ("path"),
	X ("axisX"), 
	Y ("axisY"),
	HEIGHT("height"),
	WIDTH("width"),
	OCR("ocr"), 
	EXPORTCSV ("exportcsv"),
	TESS4J ("tess4j");

	private String value;

	CustomEnumExtractZone(String str) {
		this.value = str;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
