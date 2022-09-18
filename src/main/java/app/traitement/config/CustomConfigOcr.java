package app.traitement.config;

public class CustomConfigOcr {
	private String path;
	private String tess4j;
	private String pattern;
	private String subSearch;
	private Boolean rename;
	private Boolean ocr;
	private String x;
	private String y;
	private String width;
	private String height;
	
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
	public String getPattern() {
		return pattern;
	}
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
	public String getSubSearch() {
		return subSearch;
	}
	public void setSubSearch(String subSearch) {
		this.subSearch = subSearch;
	}
	public Boolean getRename() {
		return rename;
	}
	public void setRename(Boolean rename) {
		this.rename = rename;
	}
	public Boolean getOcr() {
		return ocr;
	}
	public void setOcr(Boolean ocr) {
		this.ocr = ocr;
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
}
