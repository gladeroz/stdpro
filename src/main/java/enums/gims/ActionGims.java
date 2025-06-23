package enums.gims;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum ActionGims {
	@JsonProperty("Mail STD") 
	MAIL_STD,
	@JsonProperty("Phone STD") 
	PHONE_STD,
	@JsonProperty("Phone Client") 
	PHONE_CLIENT,
	@JsonProperty("Mail Client") 
	MAIL_CLIENT,
	@JsonProperty("Litige") 
	LITIGE,
	@JsonProperty("J+8") 
	JOURS_PLUS_8,
	@JsonProperty("MAJ") 
	MAJ,
	@JsonProperty("") 
	NULL;
}
