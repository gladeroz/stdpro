package traitement.config;

public class CustomConfigOcr {
	private String path;
	private String tess4j;
	private String pattern;
	private String subSearch;
	private Boolean rename;
	
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
}
