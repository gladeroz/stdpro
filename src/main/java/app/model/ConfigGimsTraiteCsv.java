package app.model;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import enums.gims.StatusGims;
import utils.FrenchBigDecimalDeserializer;

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
    
	public Integer getThirdPartyCode() {
		return thirdPartyCode;
	}
	public void setThirdPartyCode(Integer thirdPartyCode) {
		this.thirdPartyCode = thirdPartyCode;
	}
	public String getThirdPartyName() {
		return thirdPartyName;
	}
	public void setThirdPartyName(String thirdPartyName) {
		this.thirdPartyName = thirdPartyName;
	}
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	public String getPaymentMethodLabel() {
		return paymentMethodLabel;
	}
	public void setPaymentMethodLabel(String paymentMethodLabel) {
		this.paymentMethodLabel = paymentMethodLabel;
	}
	public StatusGims getStatus() {
		return status;
	}
	public void setStatus(StatusGims status) {
		this.status = status;
	}
	public Date getStatusStartDate() {
		return statusStartDate;
	}
	public void setStatusStartDate(Date statusStartDate) {
		this.statusStartDate = statusStartDate;
	}
	public Date getStatusEndDate() {
		return statusEndDate;
	}
	public void setStatusEndDate(Date statusEndDate) {
		this.statusEndDate = statusEndDate;
	}
	public Date getEntryDate() {
		return entryDate;
	}
	public void setEntryDate(Date entryDate) {
		this.entryDate = entryDate;
	}
	public String getJournalCode() {
		return journalCode;
	}
	public void setJournalCode(String journalCode) {
		this.journalCode = journalCode;
	}
	public String getInvoiceNumber() {
		return invoiceNumber;
	}
	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}
	public Date getDueDate() {
		return dueDate;
	}
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}
	public BigDecimal getDebitBalance() {
		return debitBalance;
	}
	public void setDebitBalance(BigDecimal debitBalance) {
		this.debitBalance = debitBalance;
	}
	public BigDecimal getCreditBalance() {
		return creditBalance;
	}
	public void setCreditBalance(BigDecimal creditBalance) {
		this.creditBalance = creditBalance;
	}
	public BigDecimal getAccountBalance() {
		return accountBalance;
	}
	public void setAccountBalance(BigDecimal accountBalance) {
		this.accountBalance = accountBalance;
	}
	public String getContactEmail() {
		return contactEmail;
	}
	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}
	public String getThirdPartyContactEmail() {
		return thirdPartyContactEmail;
	}
	public void setThirdPartyContactEmail(String thirdPartyContactEmail) {
		this.thirdPartyContactEmail = thirdPartyContactEmail;
	}
	public String getThirdPartyContactPhone() {
		return thirdPartyContactPhone;
	}
	public void setThirdPartyContactPhone(String thirdPartyContactPhone) {
		this.thirdPartyContactPhone = thirdPartyContactPhone;
	}
	public String getContactPhone() {
		return contactPhone;
	}
	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}
	public String getThirdPartyAddress() {
		return thirdPartyAddress;
	}
	public void setThirdPartyAddress(String thirdPartyAddress) {
		this.thirdPartyAddress = thirdPartyAddress;
	}
	public String getAddressComplement() {
		return addressComplement;
	}
	public void setAddressComplement(String addressComplement) {
		this.addressComplement = addressComplement;
	}
	public String getPostalCode() {
		return postalCode;
	}
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
}
