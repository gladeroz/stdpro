package app.entity.odr.pk;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class OdrPk implements Serializable {
	private String nbrContractRedbox;
	private String transactionType;

	public OdrPk() {}

	public OdrPk(String nbrContractRedbox, String transactionType) {
		this.nbrContractRedbox = nbrContractRedbox;
		this.transactionType = transactionType;
	}

	public String getNbrContractRedbox() {
		return nbrContractRedbox;
	}
	public void setNbrContractRedbox(String nbrContractRedbox) {
		this.nbrContractRedbox = nbrContractRedbox;
	}

	public String getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
}
