package app.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "CODE_ELIGIBLE")
public class CodeEligibleSql implements Serializable {
	private String codeEligible;

	public CodeEligibleSql() {}

	public CodeEligibleSql(String codeEligible) {
		this.codeEligible = codeEligible;
	}

	@Id
	@Column(name = "CODE_ELIGIBLE", nullable = false)
	public String getCodeEligible() {
		return codeEligible;
	}
	public void setCodeEligible(String codeEligible) {
		this.codeEligible = codeEligible;
	}
}
