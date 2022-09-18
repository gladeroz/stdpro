package app.entity.pk;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Id;

@Embeddable
public class OdrPk implements Serializable {
	protected String nbrContractRedbox;
	protected String transactionType;

	public OdrPk() {}

	public OdrPk(String nbrContractRedbox, String transactionType) {
		this.nbrContractRedbox = nbrContractRedbox;
		this.transactionType = transactionType;
	}

	@Id
	@Column(name = "NBR_CONTRACT_REDBOX", nullable = false)
	public String getNbrContractRedbox() {
		return nbrContractRedbox;
	}
	public void setNbrContractRedbox(String nbrContractRedbox) {
		this.nbrContractRedbox = nbrContractRedbox;
	}

	@Id
	@Column(name = "TRANSACTION_TYPE", nullable = false)
	public String getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
}
