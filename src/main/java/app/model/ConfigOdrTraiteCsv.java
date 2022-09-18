package app.model;

import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import enums.Offre;
import enums.odrodf.BaType;
import enums.odrodf.FactType;
import enums.odrodf.FormType;
import enums.odrodf.RibType;

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

	public String getNbrContractRedbox() {
		return nbrContractRedbox;
	}

	public void setNbrContractRedbox(String nbrContractRedbox) {
		this.nbrContractRedbox = nbrContractRedbox;
	}

	public FormType getFormulaire() {
		return formulaire;
	}

	public void setFormulaire(FormType formulaire) {
		this.formulaire = formulaire;
	}

	public BaType getBulletin() {
		return bulletin;
	}

	public void setBulletin(BaType bulletin) {
		this.bulletin = bulletin;
	}

	public RibType getRib() {
		return rib;
	}

	public void setRib(RibType rib) {
		this.rib = rib;
	}

	public Date getDateReception() {
		return dateReception;
	}

	public void setDateReception(Date dateReception) {
		this.dateReception = dateReception;
	}

	public FactType getFacture() {
		return facture;
	}

	public void setFacture(FactType facture) {
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
