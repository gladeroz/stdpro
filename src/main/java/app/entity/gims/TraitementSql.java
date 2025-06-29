package app.entity.gims;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import app.entity.gims.pk.GimsPk;
import enums.gims.StatusGims;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "TRAITEMENT", indexes = {
	@Index(name = "idx_paye", columnList = "paye")
})
@Getter
@Setter
@NoArgsConstructor
public class TraitementSql {

    @EmbeddedId private GimsPk gimsPk;

    @Column(name = "tiers_nom")
    private String tiersNom;

    @Column(name = "priorite")
    private String priorite;

    @Column(name = "reglement_mode_libelle")
    private String reglementModeLibelle;

    @Column(name = "statut")
    private StatusGims statut;

    @Column(name = "statut_date_debut")
    private Date statutDateDebut;

    @Column(name = "statut_date_fin")
    private Date statutDateFin;

    @Column(name = "date_ecriture")
    private Date dateEcriture;

    @Column(name = "journal_code")
    private String journalCode;

    @Column(name = "date_echeance")
    private Date dateEcheance;

    @Column(name = "debit_tenue_compte")
    private BigDecimal debitTenueCompte;

    @Column(name = "credit_tenue_compte")
    private BigDecimal creditTenueCompte;

    @Column(name = "solde_tenue_compte")
    private BigDecimal soldeTenueCompte;

    @Column(name = "ct_email")
    private String ctEmail;

    @Column(name = "ti_ct_email")
    private String tiCtEmail;

    @Column(name = "ti_ct_telephone")
    private String tiCtTelephone;

    @Column(name = "ct_telephone")
    private String ctTelephone;

    @Column(name = "tiers_adresse")
    private String tiersAdresse;

    @Column(name = "tiers_complement_adresse")
    private String tiersComplementAdresse;

    @Column(name = "tiers_code_postal")
    private String tiersCodePostal;

    @Column(name = "tiers_ville")
    private String tiersVille;

    @Column(name = "paye")
    private Boolean paye;

    @OneToMany(mappedBy = "traitement", fetch = FetchType.EAGER)
	private List<SuiviSql> suivi;
}
