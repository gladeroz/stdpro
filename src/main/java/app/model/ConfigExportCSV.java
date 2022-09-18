package app.model;

public class ConfigExportCSV {
	private String directory;
	private String fileName;
	private Integer nombrePage;
	
	public ConfigExportCSV(String directory, String fileName, Integer nombrePage) {
		super();
		this.directory = directory;
		this.fileName = fileName;
		this.nombrePage = nombrePage;
	}
	
	public Integer getNombrePage() {
		return nombrePage;
	}
	public void setNombrePage(Integer nombrePage) {
		this.nombrePage = nombrePage;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getDirectory() {
		return directory;
	}
	public void setDirectory(String directory) {
		this.directory = directory;
	}
}
