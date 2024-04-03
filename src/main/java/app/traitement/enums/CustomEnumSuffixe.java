package app.traitement.enums;

public enum CustomEnumSuffixe {
	SUFFIXE ("suffixe"),
	PREFIXE ("prefixe"),
	PATH ("path");

	private String value;

	CustomEnumSuffixe(String str) {
		this.value = str;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
