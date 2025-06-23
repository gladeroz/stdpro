package app.entity.gims.pk;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class GimsPk implements Serializable {
	private String numeroFacture;
	private Integer tiersCode;
	
	public GimsPk() {}

	public GimsPk(Integer tiersCode, String numeroFacture) {
		this.numeroFacture = numeroFacture;
		this.tiersCode = tiersCode;
	}

	public String getNumeroFacture() {
		return numeroFacture;
	}

	public void setNumeroFacture(String numeroFacture) {
		this.numeroFacture = numeroFacture;
	}

	public Integer getTiersCode() {
		return tiersCode;
	}

	public void setTiersCode(Integer tiersCode) {
		this.tiersCode = tiersCode;
	}
}

