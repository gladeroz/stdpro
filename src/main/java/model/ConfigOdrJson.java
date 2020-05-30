package model;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
	"contrat",
	"csv",
	"traitement"
})
public class ConfigOdrJson {
	
	@JsonProperty("contrat")
	private String nbrContractRedbox;

	@JsonProperty("csv")
	private ConfigOdrRefCsv odr;

	@JsonProperty("traitement")
	private ConfigOdrTraiteCsv traitement;

	public ConfigOdrJson() {}

	public ConfigOdrJson(String contrat, ConfigOdrRefCsv c, ConfigOdrTraiteCsv traitement) {
		this.nbrContractRedbox = contrat;
		this.odr = c;
		this.traitement = traitement;
	}

	public ConfigOdrRefCsv getOdr() {
		return odr;
	}

	public void setOdr(ConfigOdrRefCsv odr) {
		this.odr = odr;
	}

	public ConfigOdrTraiteCsv getTraitement() {
		return traitement;
	}

	public void setTraitement(ConfigOdrTraiteCsv traitement) {
		this.traitement = traitement;
	}
	
	public String getContrat() {
		return nbrContractRedbox;
	}

	public void setContrat(String contrat) {
		this.nbrContractRedbox = contrat;
	}

	@Override
	public String toString(){
		return ToStringBuilder.reflectionToString(this);
	}
}
