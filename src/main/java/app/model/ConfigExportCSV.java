package app.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
}
