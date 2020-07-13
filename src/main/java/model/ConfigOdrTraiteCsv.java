package model;

import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import enums.OdrType;
import enums.Offre;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConfigOdrTraiteCsv {

	private Offre offre;
	private String nbrContractRedbox;
	private String filler;
	private OdrType facture;
	private OdrType formulaire;
	private OdrType bulletin;
	private OdrType rib;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd")
	private Date dateReception;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd")
	private Date dateTraitement;

	public ConfigOdrTraiteCsv() {}

	public ConfigOdrTraiteCsv(ConfigOdrRefCsv c) {
		this.nbrContractRedbox = c.getNbrContractRedbox();
		this.filler = "";
		this.formulaire = OdrType.NV;
		this.bulletin = OdrType.NV;
		this.facture = OdrType.NV;
		this.rib = OdrType.S;
	}

	public String getNbrContractRedbox() {
		return nbrContractRedbox;
	}

	public void setNbrContractRedbox(String nbrContractRedbox) {
		this.nbrContractRedbox = nbrContractRedbox;
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

	public Offre getOffre() {
		return offre;
	}

	public void setOffre(Offre offre) {
		this.offre = offre;
	}

	public Date getDateTraitement() {
		return dateTraitement;
	}

	public void setDateTraitement(Date dateTraitement) {
		this.dateTraitement = dateTraitement;
	}

	@Override
	public String toString(){
		return ToStringBuilder.reflectionToString(this);
	}
}
