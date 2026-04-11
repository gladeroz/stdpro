package app.model;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import enums.gims.ActionGims;

@Getter
@Setter
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
}
