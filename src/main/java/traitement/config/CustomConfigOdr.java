package traitement.config;

public class CustomConfigOdr {
	private String delta;
	private String referential;
	private String docTraite;
	private String exportcsv;

	public String getReferential() {
		return referential;
	}

	public void setReferential(String referential) {
		this.referential = referential;
	}

	public String getDocTraite() {
		return docTraite;
	}

	public void setDocTraite(String docTraite) {
		this.docTraite = docTraite;
	}

	public String getExportcsv() {
		return exportcsv;
	}

	public void setExportcsv(String exportcsv) {
		this.exportcsv = exportcsv;
	}

	public String getDelta() {
		return delta;
	}

	public void setDelta(String delta) {
		this.delta = delta;
	}
}
