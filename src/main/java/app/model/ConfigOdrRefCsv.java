package app.model;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@Getter
@Setter
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
}
