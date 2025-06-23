package app.traitement.config;

public class CustomConfigExtractZone {
	private String path;
	private String tess4j;
	private String x;
	private String y;
	private String width;
	private String height;
	private String exportcsv;
	private Boolean ocr;
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getTess4j() {
		return tess4j;
	}
	public void setTess4j(String tess4j) {
		this.tess4j = tess4j;
	}
	public String getX() {
		return x;
	}
	public void setX(String x) {
		this.x = x;
	}
	public String getY() {
		return y;
	}
	public void setY(String y) {
		this.y = y;
	}
	public String getWidth() {
		return width;
	}
	public void setWidth(String width) {
		this.width = width;
	}
	public String getHeight() {
		return height;
	}
	public void setHeight(String height) {
		this.height = height;
	}
	public String getExportcsv() {
		return exportcsv;
	}
	public void setExportcsv(String exportcsv) {
		this.exportcsv = exportcsv;
	}
	public Boolean getOcr() {
		return ocr;
	}
	public void setOcr(Boolean ocr) {
		this.ocr = ocr;
	}
}
