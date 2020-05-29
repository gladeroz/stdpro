package model;

import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import enums.OdrType;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
	"csv"
})
public class ConfigOdrJson {
	
	@JsonProperty("csv")
	private ConfigOdrCsv odr;
	
	private String filler;
	private OdrType facture;
	private OdrType formulaire;
	private OdrType bulletin;
	private OdrType rib;
	private Date dateReception;
	
	public ConfigOdrJson() {}

	public ConfigOdrJson(ConfigOdrCsv c) {
		this.odr = c;
		this.formulaire = OdrType.NV;
		this.bulletin = OdrType.NV;
		this.facture = OdrType.NV;
		this.rib = OdrType.S;
		this.dateReception = new Date();
	}

	public ConfigOdrCsv getOdr() {
		return odr;
	}
	
	public void setOdr(ConfigOdrCsv odr) {
		this.odr = odr;
	}

	public OdrType getFormulaire() {
		return formulaire;
	}

	public void setFormulaire(OdrType formulaire) {
		this.formulaire = formulaire;
	}

	public OdrType getBulletin() {
		return bulletin;
	}

	public void setBulletin(OdrType bulletin) {
		this.bulletin = bulletin;
	}

	public OdrType getRib() {
		return rib;
	}

	public void setRib(OdrType rib) {
		this.rib = rib;
	}

	public Date getDateReception() {
		return dateReception;
	}

	public void setDateReception(Date dateReception) {
		this.dateReception = dateReception;
	}
	
	public OdrType getFacture() {
		return facture;
	}

	public void setFacture(OdrType facture) {
		this.facture = facture;
	}
	
	public String getFiller() {
		return filler;
	}

	public void setFiller(String filler) {
		this.filler = filler;
	}

	@Override
	public String toString(){
		return ToStringBuilder.reflectionToString(this);
	}
}
