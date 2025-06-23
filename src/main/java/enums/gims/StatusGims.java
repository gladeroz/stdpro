package enums.gims;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum StatusGims {
	@JsonProperty("actif") 
	ACTIF,
	@JsonProperty("radié") 
	RADIE,
	@JsonProperty("") 
	NULL;
}
