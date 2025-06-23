package app.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import enums.gims.ActionGims;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
	"client",
	"N°Facture",
	"Date",
	"Action"
})
public class ConfigGimsSuiviCsv {
	
    private Integer thirdPartyCode;
    private String numeroFacture;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private Date dateAction;
    private ActionGims action;
    
	public Integer getThirdPartyCode() {
		return thirdPartyCode;
	}
	public void setThirdPartyCode(Integer thirdPartyCode) {
		this.thirdPartyCode = thirdPartyCode;
	}
	public String getNumeroFacture() {
		return numeroFacture;
	}
	public void setNumeroFacture(String numeroFacture) {
		this.numeroFacture = numeroFacture;
	}
	public Date getDateAction() {
		return dateAction;
	}
	public void setDateAction(Date dateAction) {
		this.dateAction = dateAction;
	}
	public ActionGims getAction() {
		return action;
	}
	public void setAction(ActionGims action) {
		this.action = action;
	}
}
