package traitement.enums;

public enum CustomEnumOcr {
	PATH ("path"),
	TESS4J ("tess4j"), 
	PATTERN ("pattern");

	private String value;

	CustomEnumOcr(String str) {
		this.value = str;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
