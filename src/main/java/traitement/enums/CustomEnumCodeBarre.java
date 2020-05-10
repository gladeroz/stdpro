package traitement.enums;

public enum CustomEnumCodeBarre {
	PATH("path"), 
	TESS4J ("tess4j"),
	RENAME ("rename");
	
	private String value;

	CustomEnumCodeBarre(String str) {
		this.value = str;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
