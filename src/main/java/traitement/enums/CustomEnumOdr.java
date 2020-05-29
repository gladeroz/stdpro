package traitement.enums;

public enum CustomEnumOdr {
	DELTA ("delta"), 
	DOC_TRAITE("doc_traite"),
	EXPORTCSV("exportcsv"), 
	REFERENTIAL("referential");

	private String value;

	CustomEnumOdr(String str) {
		this.value = str;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
