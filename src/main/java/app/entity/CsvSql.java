package app.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import app.entity.pk.OdrPk;
import app.model.ConfigOdrRefCsv;

@Entity
@Table(name = "CSV")
public class CsvSql implements Serializable {

	@EmbeddedId private OdrPk odrPk;

	private String seqNumber;
	private String recordType;
	private String subsidiaryCode;
	private String storeCode;
	private String purchaseOrderNumber;
	private String linenumber;
	private String storeName;
	private String paymentType;

	private Date productSalesDate;
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

	@OneToOne(optional = true, cascade = CascadeType.ALL)
	private TraitementSql traitement;

	public CsvSql() {}

	public CsvSql(ConfigOdrRefCsv c, String transactionType) {
		/** ID **/
		this.odrPk = new OdrPk(c.getNbrContractRedbox(), transactionType);

		/** OTHER **/
		this.brandNameProduct = c.getBrandNameProduct();
		this.clientID = c.getClientID();
		this.clientName = c.getClientName();
		this.codeINSEE = c.getCodeINSEE();
		this.codic = c.getCodic();
		this.customerFirstName = c.getCustomerFirstName();
		this.customerTitle = c.getCustomerTitle();
		this.emailAdress = c.getEmailAdress();
		this.familyInsuranceCode = c.getFamilyInsuranceCode();
		this.familyInsuranceLabel = c.getFamilyInsuranceLabel();
		this.familyProductCode = c.getFamilyProductCode();
		this.familyProductLabel = c.getFamilyProductLabel();
		this.imeiNumber = c.getImeiNumber();
		this.linenumber = c.getLinenumber();
		this.location = c.getLocation();
		this.nameofService = c.getNameofService();
		this.nbrInTheTrack = c.getNbrInTheTrack();
		this.paymentType = c.getPaymentType();
		this.postalCode = c.getPostalCode();
		this.prixUnitProduct = c.getPrixUnitProduct();
		this.prixUnitProvision = c.getPrixUnitProvision();
		this.productBrandCode = c.getProductBrandCode();
		this.productCode = c.getProductCode();
		this.productPrixTotal = c.getProductPrixTotal();
		this.productQty = c.getProductQty();
		this.productReference = c.getProductReference();
		this.productSalesDate = c.getProductSalesDate();
		this.purchaseOrderNumber = c.getPurchaseOrderNumber();
		this.quantitySold= c.getQuantitySold();
		this.recordType = c.getRecordType();
		this.salesChannel = c.getSalesChannel();
		this.seqNumber = c.getSeqNumber();
		this.storeCode = c.getStoreCode();
		this.storeName = c.getStoreName();
		this.subsidiaryCode = c.getSubsidiaryCode();
		this.trackCodeType = c.getTrackCodeType();
		this.trackName = c.getTrackName();
		this.typeOfSale = c.getTypeOfSale();
		this.warrantySalesDate = c.getWarrantySalesDate();
	}

	@Column(name = "SEQ_NUMBER", nullable = false)
	public String getSeqNumber() {
		return seqNumber;
	}
	public void setSeqNumber(String seqNumber) {
		this.seqNumber = seqNumber;
	}


	@Column(name = "RECORD_TYPE", nullable = false)
	public String getRecordType() {
		return recordType;
	}
	public void setRecordType(String recordType) {
		this.recordType = recordType;
	}


	@Column(name = "SUBSIDIARY_CODE", nullable = false)
	public String getSubsidiaryCode() {
		return subsidiaryCode;
	}
	public void setSubsidiaryCode(String subsidiaryCode) {
		this.subsidiaryCode = subsidiaryCode;
	}


	@Column(name = "STORE_CODE", nullable = false)
	public String getStoreCode() {
		return storeCode;
	}
	public void setStoreCode(String storeCode) {
		this.storeCode = storeCode;
	}


	@Column(name = "PURCHASE_ORDER_NUMBER", nullable = false)
	public String getPurchaseOrderNumber() {
		return purchaseOrderNumber;
	}
	public void setPurchaseOrderNumber(String purchaseOrderNumber) {
		this.purchaseOrderNumber = purchaseOrderNumber;
	}


	@Column(name = "LINENUMBER", nullable = false)
	public String getLinenumber() {
		return linenumber;
	}
	public void setLinenumber(String linenumber) {
		this.linenumber = linenumber;
	}

	@Column(name = "STORE_NAME", nullable = false)
	public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}


	@Column(name = "PAIEMENT_TYPE", nullable = false)
	public String getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}


	@Column(name = "PRODUCT_SALES_DATE", nullable = false)
	public Date getProductSalesDate() {
		return productSalesDate;
	}
	public void setProductSalesDate(Date productSalesDate) {
		this.productSalesDate = productSalesDate;
	}


	@Column(name = "WARRANTY_SALES_DATE", nullable = false)
	public Date getWarrantySalesDate() {
		return warrantySalesDate;
	}
	public void setWarrantySalesDate(Date warrantySalesDate) {
		this.warrantySalesDate = warrantySalesDate;
	}


	@Column(name = "FAMILY_INSURANCE_CODE", nullable = false)
	public String getFamilyInsuranceCode() {
		return familyInsuranceCode;
	}
	public void setFamilyInsuranceCode(String familyInsuranceCode) {
		this.familyInsuranceCode = familyInsuranceCode;
	}


	@Column(name = "FAMILY_INSURANCE_LABEL", nullable = false)
	public String getFamilyInsuranceLabel() {
		return familyInsuranceLabel;
	}
	public void setFamilyInsuranceLabel(String familyInsuranceLabel) {
		this.familyInsuranceLabel = familyInsuranceLabel;
	}


	@Column(name = "NAME_OF_SERVICE", nullable = false)
	public String getNameofService() {
		return nameofService;
	}
	public void setNameofService(String nameofService) {
		this.nameofService = nameofService;
	}


	@Column(name = "PRODUCT_CODE", nullable = false)
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}


	@Column(name = "QUANTITY_SOLD", nullable = false)
	public String getQuantitySold() {
		return quantitySold;
	}
	public void setQuantitySold(String quantitySold) {
		this.quantitySold = quantitySold;
	}


	@Column(name = "PRIX_UNIT_PROVISION", nullable = false)
	public String getPrixUnitProvision() {
		return prixUnitProvision;
	}
	public void setPrixUnitProvision(String prixUnitProvision) {
		this.prixUnitProvision = prixUnitProvision;
	}


	@Column(name = "FAMILY_PRODUCT_CODE", nullable = false)
	public String getFamilyProductCode() {
		return familyProductCode;
	}
	public void setFamilyProductCode(String familyProductCode) {
		this.familyProductCode = familyProductCode;
	}


	@Column(name = "FAMILY_PRODUCT_LABEL", nullable = false)
	public String getFamilyProductLabel() {
		return familyProductLabel;
	}
	public void setFamilyProductLabel(String familyProductLabel) {
		this.familyProductLabel = familyProductLabel;
	}


	@Column(name = "PRODUCT_BRAND_CODE", nullable = false)
	public String getProductBrandCode() {
		return productBrandCode;
	}
	public void setProductBrandCode(String productBrandCode) {
		this.productBrandCode = productBrandCode;
	}


	@Column(name = "BRAND_NAME_PRODUCT", nullable = false)
	public String getBrandNameProduct() {
		return brandNameProduct;
	}
	public void setBrandNameProduct(String brandNameProduct) {
		this.brandNameProduct = brandNameProduct;
	}


	@Column(name = "PRODUCT_REFERENCE", nullable = false)
	public String getProductReference() {
		return productReference;
	}
	public void setProductReference(String productReference) {
		this.productReference = productReference;
	}


	@Column(name = "CODIC", nullable = false)
	public String getCodic() {
		return codic;
	}
	public void setCodic(String codic) {
		this.codic = codic;
	}


	@Column(name = "PRODUCT_QTY", nullable = false)
	public String getProductQty() {
		return productQty;
	}
	public void setProductQty(String productQty) {
		this.productQty = productQty;
	}


	@Column(name = "PRIX_UNIT_PRODUCT", nullable = false)
	public String getPrixUnitProduct() {
		return prixUnitProduct;
	}
	public void setPrixUnitProduct(String prixUnitProduct) {
		this.prixUnitProduct = prixUnitProduct;
	}


	@Column(name = "PRODUCT_PRIX_TOTAL", nullable = false)
	public String getProductPrixTotal() {
		return productPrixTotal;
	}
	public void setProductPrixTotal(String productPrixTotal) {
		this.productPrixTotal = productPrixTotal;
	}


	@Column(name = "CLIENT_ID", nullable = false)
	public String getClientID() {
		return clientID;
	}
	public void setClientID(String clientID) {
		this.clientID = clientID;
	}


	@Column(name = "CUSTOMER_TITLE", nullable = false)
	public String getCustomerTitle() {
		return customerTitle;
	}
	public void setCustomerTitle(String customerTitle) {
		this.customerTitle = customerTitle;
	}


	@Column(name = "CLIENT_NAME", nullable = false)
	public String getClientName() {
		return clientName;
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}


	@Column(name = "CUSTOMER_FIRST_NAME", nullable = false)
	public String getCustomerFirstName() {
		return customerFirstName;
	}
	public void setCustomerFirstName(String customerFirstName) {
		this.customerFirstName = customerFirstName;
	}


	@Column(name = "NBR_IN_THE_TRACK", nullable = false)
	public String getNbrInTheTrack() {
		return nbrInTheTrack;
	}
	public void setNbrInTheTrack(String nbrInTheTrack) {
		this.nbrInTheTrack = nbrInTheTrack;
	}


	@Column(name = "TRACK_CODE_TYPE", nullable = false)
	public String getTrackCodeType() {
		return trackCodeType;
	}
	public void setTrackCodeType(String trackCodeType) {
		this.trackCodeType = trackCodeType;
	}


	@Column(name = "TRACK_NAME", nullable = false)
	public String getTrackName() {
		return trackName;
	}
	public void setTrackName(String trackName) {
		this.trackName = trackName;
	}


	@Column(name = "POSTAL_CODE", nullable = false)
	public String getPostalCode() {
		return postalCode;
	}
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}


	@Column(name = "CODE_INSEE", nullable = false)
	public String getCodeINSEE() {
		return codeINSEE;
	}
	public void setCodeINSEE(String codeINSEE) {
		this.codeINSEE = codeINSEE;
	}


	@Column(name = "LOCATION", nullable = false)
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}


	@Column(name = "IMEI_NUMBER", nullable = false)
	public String getImeiNumber() {
		return imeiNumber;
	}
	public void setImeiNumber(String imeiNumber) {
		this.imeiNumber = imeiNumber;
	}


	@Column(name = "TYPE_OF_SALE", nullable = false)
	public String getTypeOfSale() {
		return typeOfSale;
	}
	public void setTypeOfSale(String typeOfSale) {
		this.typeOfSale = typeOfSale;
	}


	@Column(name = "SALES_CHANNEL", nullable = false)
	public String getSalesChannel() {
		return salesChannel;
	}
	public void setSalesChannel(String salesChannel) {
		this.salesChannel = salesChannel;
	}


	@Column(name = "EMAIL_ADRESS", nullable = false)
	public String getEmailAdress() {
		return emailAdress;
	}
	public void setEmailAdress(String emailAdress) {
		this.emailAdress = emailAdress;
	}

	public OdrPk getOdrPk() {
		return odrPk;
	}
	public void setOdrPk(OdrPk odrPk) {
		this.odrPk = odrPk;
	}

	public TraitementSql getTraitement() {
		return traitement;
	}
	public void setTraitement(TraitementSql traitement) {
		this.traitement = traitement;
	}
}