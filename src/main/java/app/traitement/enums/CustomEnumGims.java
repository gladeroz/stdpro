package app.traitement.enums;

public enum CustomEnumGims {
	DOC_TRAITE("doc_traite"), DOC_SUIVI("doc_suivi"), EXPORTCSV("exportcsv");

	private String value;

	CustomEnumGims(String str) {
		this.value = str;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
