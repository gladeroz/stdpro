package app.traitement.enums;

public enum CustomEnumComptage {
	PATH("path"),
	EXPORTCSV ("exportcsv");

	private String value;

	CustomEnumComptage(String str) {
		this.value = str;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
