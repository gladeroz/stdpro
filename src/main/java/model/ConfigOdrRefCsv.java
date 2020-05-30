package model;

import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({
	"Sequence number",
	"Record Type",
	"Subsidiary code",
	"Store Code",
	"Purchase Order Number",
	"Line number",
	"Transaction Type",
	"Store Name",
	"Payment Type",
	"Product Sales Date",
	"Warranty Sales Date",
	"Family Insurance Code",
	"Family Insurance Label",
	"Name of service",
	"Product Code",
	"Quantity sold",
	"PrixUnit -provision",
	"Family-product code",
	"Family-product label",
	"Product Brand Code",
	"Brand name product",
	"Product reference",
	"Codic",
	"Product Qty",
	"PrixUnit -Product",
	"Product-prixtotal",
	"Client-ID",
	"Customer Title",
	"Client name",
	"Customer first name",
	"Nbr in the track",
	"Track code type",
	"Track name",
	"Postal code",
	"Code INSEE",
	"Location",
	"IMEI Number",
	"Type of sale",
	"Sales channel",
	"E-mail adress",
	"Nbr Contract Redbox"
})

public class ConfigOdrRefCsv {
	private String seqNumber;
	private String recordType;
	private String subsidiaryCode;
	private String storeCode;
	private String purchaseOrderNumber;
	private String linenumber;
	private String transactionType;
	private String storeName;
	private String paymentType;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd")
	private Date productSalesDate;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd")
	private Date warrantySalesDate;

	private String familyInsuranceCode;
	private String familyInsuranceLabel;
	private String nameofService;
	private String productCode;
	private String quantitySold;
	private String prixUnitProvision;
	private String familyProductCode;
	private String familyProductLabel;
	private String productBrandCode;
	private String brandNameProduct;
	private String productReference;
	private String codic;
	private String productQty;
	private String prixUnitProduct;
	private String productPrixTotal;
	private String clientID;
	private String customerTitle;
	private String clientName;
	private String customerFirstName;
	private String nbrInTheTrack;
	private String trackCodeType;
	private String trackName;
	private String postalCode;
	private String codeINSEE;
	private String location;
	private String imeiNumber;
	private String typeOfSale;
	private String salesChannel;
	private String emailAdress;
	private String nbrContractRedbox;

	public String getSeqNumber() {
		return seqNumber;
	}

	public void setSeqNumber(String seqNumber) {
		this.seqNumber = seqNumber;
	}

	public String getRecordType() {
		return recordType;
	}

	public void setRecordType(String recordType) {
		this.recordType = recordType;
	}

	public String getSubsidiaryCode() {
		return subsidiaryCode;
	}

	public void setSubsidiaryCode(String subsidiaryCode) {
		this.subsidiaryCode = subsidiaryCode;
	}

	public String getStoreCode() {
		return storeCode;
	}

	public void setStoreCode(String storeCode) {
		this.storeCode = storeCode;
	}

	public String getPurchaseOrderNumber() {
		return purchaseOrderNumber;
	}

	public void setPurchaseOrderNumber(String purchaseOrderNumber) {
		this.purchaseOrderNumber = purchaseOrderNumber;
	}

	public String getLinenumber() {
		return linenumber;
	}

	public void setLinenumber(String linenumber) {
		this.linenumber = linenumber;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public Date getProductSalesDate() {
		return productSalesDate;
	}

	public void setProductSalesDate(Date productSalesDate) {
		this.productSalesDate = productSalesDate;
	}

	public Date getWarrantySalesDate() {
		return warrantySalesDate;
	}

	public void setWarrantySalesDate(Date warrantySalesDate) {
		this.warrantySalesDate = warrantySalesDate;
	}

	public String getFamilyInsuranceCode() {
		return familyInsuranceCode;
	}

	public void setFamilyInsuranceCode(String familyInsuranceCode) {
		this.familyInsuranceCode = familyInsuranceCode;
	}

	public String getFamilyInsuranceLabel() {
		return familyInsuranceLabel;
	}

	public void setFamilyInsuranceLabel(String familyInsuranceLabel) {
		this.familyInsuranceLabel = familyInsuranceLabel;
	}

	public String getNameofService() {
		return nameofService;
	}

	public void setNameofService(String nameofService) {
		this.nameofService = nameofService;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getQuantitySold() {
		return quantitySold;
	}

	public void setQuantitySold(String quantitySold) {
		this.quantitySold = quantitySold;
	}

	public String getPrixUnitProvision() {
		return prixUnitProvision;
	}

	public void setPrixUnitProvision(String prixUnitProvision) {
		this.prixUnitProvision = prixUnitProvision;
	}

	public String getFamilyProductCode() {
		return familyProductCode;
	}

	public void setFamilyProductCode(String familyProductCode) {
		this.familyProductCode = familyProductCode;
	}

	public String getFamilyProductLabel() {
		return familyProductLabel;
	}

	public void setFamilyProductLabel(String familyProductLabel) {
		this.familyProductLabel = familyProductLabel;
	}

	public String getProductBrandCode() {
		return productBrandCode;
	}

	public void setProductBrandCode(String productBrandCode) {
		this.productBrandCode = productBrandCode;
	}

	public String getBrandNameProduct() {
		return brandNameProduct;
	}

	public void setBrandNameProduct(String brandNameProduct) {
		this.brandNameProduct = brandNameProduct;
	}

	public String getProductReference() {
		return productReference;
	}

	public void setProductReference(String productReference) {
		this.productReference = productReference;
	}

	public String getCodic() {
		return codic;
	}

	public void setCodic(String codic) {
		this.codic = codic;
	}

	public String getProductQty() {
		return productQty;
	}

	public void setProductQty(String productQty) {
		this.productQty = productQty;
	}

	public String getPrixUnitProduct() {
		return prixUnitProduct;
	}

	public void setPrixUnitProduct(String prixUnitProduct) {
		this.prixUnitProduct = prixUnitProduct;
	}

	public String getProductPrixTotal() {
		return productPrixTotal;
	}

	public void setProductPrixTotal(String productPrixTotal) {
		this.productPrixTotal = productPrixTotal;
	}

	public String getClientID() {
		return clientID;
	}

	public void setClientID(String clientID) {
		this.clientID = clientID;
	}

	public String getCustomerTitle() {
		return customerTitle;
	}

	public void setCustomerTitle(String customerTitle) {
		this.customerTitle = customerTitle;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getCustomerFirstName() {
		return customerFirstName;
	}

	public void setCustomerFirstName(String customerFirstName) {
		this.customerFirstName = customerFirstName;
	}

	public String getNbrInTheTrack() {
		return nbrInTheTrack;
	}

	public void setNbrInTheTrack(String nbrInTheTrack) {
		this.nbrInTheTrack = nbrInTheTrack;
	}

	public String getTrackCodeType() {
		return trackCodeType;
	}

	public void setTrackCodeType(String trackCodeType) {
		this.trackCodeType = trackCodeType;
	}

	public String getTrackName() {
		return trackName;
	}

	public void setTrackName(String trackName) {
		this.trackName = trackName;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getCodeINSEE() {
		return codeINSEE;
	}

	public void setCodeINSEE(String codeINSEE) {
		this.codeINSEE = codeINSEE;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getImeiNumber() {
		return imeiNumber;
	}

	public void setImeiNumber(String imeiNumber) {
		this.imeiNumber = imeiNumber;
	}

	public String getTypeOfSale() {
		return typeOfSale;
	}

	public void setTypeOfSale(String typeOfSale) {
		this.typeOfSale = typeOfSale;
	}

	public String getSalesChannel() {
		return salesChannel;
	}

	public void setSalesChannel(String salesChannel) {
		this.salesChannel = salesChannel;
	}

	public String getEmailAdress() {
		return emailAdress;
	}

	public void setEmailAdress(String emailAdress) {
		this.emailAdress = emailAdress;
	}

	public String getNbrContractRedbox() {
		return nbrContractRedbox;
	}

	public void setNbrContractRedbox(String nbrContractRedbox) {
		this.nbrContractRedbox = nbrContractRedbox;
	}

	@Override
	public String toString(){
		return ToStringBuilder.reflectionToString(this);
	}
}
