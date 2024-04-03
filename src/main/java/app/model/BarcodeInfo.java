package app.model;

public class BarcodeInfo {
	private final String text;
	private final String format;

	public BarcodeInfo(String text, String format) {
		this.text = text;
		this.format = format;
	}

	public String print() {
		return (text + "/" + format);
	}

	public String getText() {
		return text;
	}

	public String getFormat() {
		return format;
	}
}