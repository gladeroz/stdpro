package app.model;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import enums.Offre;
import enums.odrodf.BaType;
import enums.odrodf.FactType;
import enums.odrodf.FormType;
import enums.odrodf.RibType;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConfigOdrTraiteCsv {

	private Offre offre;
	private String nbrContractRedbox;
	private String filler;
	private FactType facture;
	private FormType formulaire;
	private BaType bulletin;
	private RibType rib;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd")
	private Date dateReception;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd")
	private Date dateTraitement;

	public ConfigOdrTraiteCsv() {}

	public ConfigOdrTraiteCsv(ConfigOdrRefCsv c) {
		this.nbrContractRedbox = c.getNbrContractRedbox();
		this.filler = "";
		this.formulaire = FormType.NV;
		this.bulletin = BaType.NV;
		this.facture = FactType.NV;
		this.rib = RibType.S;
	}

	@Override
	public String toString(){
		return ToStringBuilder.reflectionToString(this);
	}
}
