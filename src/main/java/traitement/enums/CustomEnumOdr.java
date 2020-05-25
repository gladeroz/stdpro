package traitement.enums;

public enum CustomEnumOdr {
	DELTA ("delta"),;

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
