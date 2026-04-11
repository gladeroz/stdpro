package app.traitement.config;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomConfigOdr {
	private String delta;
	private String referential;
	private String docTraite;
	private String exportcsv;
	private String intervalMin;
	private String intervalMax;
	private boolean migration;
}
