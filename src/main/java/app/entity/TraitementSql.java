package app.entity;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import app.entity.pk.OdrPk;
import app.model.ConfigOdrTraiteCsv;
import enums.Offre;
import enums.odrodf.BaType;
import enums.odrodf.FactType;
import enums.odrodf.FormType;
import enums.odrodf.RibType;
import utils.DateService;

@Entity
@Table(name = "Traitement")
public class TraitementSql implements Serializable {

	@EmbeddedId private OdrPk odrPk;
	private Offre offre;
	private String filler;
	private FactType facture;
	private FormType formulaire;
	private BaType bulletin;
	private RibType rib;
	private Date dateReception;
	private Date dateTraitement;

	@OneToOne
	@JoinColumn(name="nbrContractRedbox", referencedColumnName="nbrContractRedbox")
	@JoinColumn(name="transactionType", referencedColumnName="transactionType")
	private CsvSql csv;

	public TraitementSql() {}

	public TraitementSql(ConfigOdrTraiteCsv t, String transactionType) {
		/** ID **/
		this.odrPk = new OdrPk(t.getNbrContractRedbox(), transactionType);

		/** Other **/
		this.bulletin = t.getBulletin();
		this.dateReception = t.getDateReception();
		this.dateTraitement = t.getDateTraitement();
		this.facture = t.getFacture();
		this.filler = t.getFiller();
		this.formulaire = t.getFormulaire();
		this.offre = t.getOffre();
		this.rib = t.getRib();
	}

	@Column(name = "OFFRE", nullable = false)
	public Offre getOffre() {
		return offre;
	}
	public void setOffre(Offre offre) {
		this.offre = offre;
	}

	@Column(name = "FILLER", nullable = false)
	public String getFiller() {
		return filler;
	}
	public void setFiller(String filler) {
		this.filler = filler;
	}

	@Column(name = "FACTURE", nullable = false)
	public FactType getFacture() {
		return facture;
	}
	public void setFacture(FactType facture) {
		this.facture = facture;
	}

	@Column(name = "FORMULAIRE", nullable = false)
	public FormType getFormulaire() {
		return formulaire;
	}
	public void setFormulaire(FormType formulaire) {
		this.formulaire = formulaire;
	}

	@Column(name = "BULLETIN", nullable = false)
	public BaType getBulletin() {
		return bulletin;
	}
	public void setBulletin(BaType bulletin) {
		this.bulletin = bulletin;
	}

	@Column(name = "RIB", nullable = false)
	public RibType getRib() {
		return rib;
	}
	public void setRib(RibType rib) {
		this.rib = rib;
	}

	@Column(name = "DATE_RECEPTION", nullable = false)
	public Date getDateReception() {
		return DateService.zeroTime(dateReception);
	}
	public void setDateReception(Date dateReception) {
		this.dateReception = DateService.zeroTime(dateReception);
	}

	@Column(name = "DATE_TRAITEMENT", nullable = false)
	public Date getDateTraitement() {
		return DateService.zeroTime(dateTraitement);
	}

	public void setDateTraitement(Date dateTraitement) {
		this.dateTraitement = DateService.zeroTime(dateTraitement);
	}

	public CsvSql getCsv() {
		return csv;
	}

	public void setCsv(CsvSql csv) {
		this.csv = csv;
	}

	public OdrPk getOdrPk() {
		return odrPk;
	}

	public void setOdrPk(OdrPk odrPk) {
		this.odrPk = odrPk;
	}
}
