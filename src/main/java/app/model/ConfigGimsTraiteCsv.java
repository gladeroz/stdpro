package app.model;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import enums.gims.StatusGims;
import utils.FrenchBigDecimalDeserializer;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
	"Tiers - Code",
	"Tiers - Nom",
	"Priorité",
	"Règlement Mode - Libellé",
	"Statut",
	"Statut / Date de début",
	"Statut / Date de fin",
	"Date Ecriture",
	"Journal - Code",
	"N°Facture",
	"Date Echéance",
	"Débit Tenue de Compte",
	"Crédit Tenue de Compte",
	"Solde Tenue de Compte",
	"CT_EMail",
	"TI_CTEMAIL",
	"TI_CTTELEPHONE",
	"CT_Telephone",
	"Tiers - Adresse",
	"Tiers - Complément Adresse",
	"Tiers - Code Postal",
	"Tiers - Ville"
})
public class ConfigGimsTraiteCsv {

    private Integer thirdPartyCode;
    private String thirdPartyName;
    private String priority;
    private String paymentMethodLabel;
    private StatusGims status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private Date statusStartDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private Date statusEndDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private Date entryDate;
    private String journalCode;
    private String invoiceNumber;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private Date dueDate;
    @JsonDeserialize(using = FrenchBigDecimalDeserializer.class)
    private BigDecimal debitBalance;
    @JsonDeserialize(using = FrenchBigDecimalDeserializer.class)
    private BigDecimal creditBalance;
    @JsonDeserialize(using = FrenchBigDecimalDeserializer.class)
    private BigDecimal accountBalance;
    private String contactEmail;
    private String thirdPartyContactEmail;
    private String thirdPartyContactPhone;
    private String contactPhone;
    private String thirdPartyAddress;
    private String addressComplement;
    private String postalCode;
    private String city;
}
