package app.model;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
	"contrat",
	"csv",
	"traitement"
})
public class ConfigOdrJson {

	@JsonProperty("contrat")
	private String contrat;

	@JsonProperty("csv")
	private ConfigOdrRefCsv odr;

	@JsonProperty("traitement")
	private ConfigOdrTraiteCsv traitement;

	public ConfigOdrJson() {}

	public ConfigOdrJson(String contrat, ConfigOdrRefCsv c, ConfigOdrTraiteCsv traitement) {
		this.contrat = contrat;
		this.odr = c;
		this.traitement = traitement;
	}

	@Override
	public String toString(){
		return ToStringBuilder.reflectionToString(this);
	}
}
