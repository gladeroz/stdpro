package app.entity.gims;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import app.entity.gims.pk.GimsPk;
import enums.gims.StatusGims;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "TRAITEMENT", indexes = {
	@Index(name = "idx_paye", columnList = "paye")
})
@Data
public class TraitementSql {

    @EmbeddedId private GimsPk gimsPk;

    private String tiersNom; // Tiers - Nom
    private String priorite; // Priorité
    private String reglementModeLibelle; // Règlement Mode - Libellé
    private StatusGims statut; // Statut
    private Date statutDateDebut; // Statut / Date de début
    private Date statutDateFin; // Statut / Date de fin
    private Date dateEcriture; // Date Ecriture
    private String journalCode; // Journal - Code
    private Date dateEcheance; // Date Echéance
    private BigDecimal debitTenueCompte; // Débit Tenue de Compte
    private BigDecimal creditTenueCompte; // Crédit Tenue de Compte
    private BigDecimal soldeTenueCompte; // Solde Tenue de Compte
    private String ctEmail; // CT_EMail
    private String tiCtEmail; // TI_CTEMAIL
    private String tiCtTelephone; // TI_CTTELEPHONE
    private String ctTelephone; // CT_Telephone
    private String tiersAdresse; // Tiers - Adresse
    private String tiersComplementAdresse; // Tiers - Complément Adresse
    private String tiersCodePostal; // Tiers - Code Postal
    private String tiersVille; // Tiers - Ville
    private Boolean paye = false; // Tiers - Ville

    @OneToMany(mappedBy = "traitement", fetch = FetchType.EAGER)
	private List<SuiviSql> suivi;
}
