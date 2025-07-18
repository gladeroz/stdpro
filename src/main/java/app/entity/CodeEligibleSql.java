package app.entity;

import java.io.Serializable;

import app.entity.gims.TraitementSql;
import app.entity.gims.pk.SuiviGimsPk;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "CODE_ELIGIBLE")
public class CodeEligibleSql implements Serializable {
	private String codeEligible;
	private Integer odrPrix;
	private Integer odfPrix;

	public CodeEligibleSql() {}

	public CodeEligibleSql(String codeEligible) {
		this.codeEligible = codeEligible;
		this.odrPrix = 30;
		this.odfPrix = 30;
	}

	public CodeEligibleSql(String codeEligible, Integer odrPrix, Integer odfPrix) {
		this.codeEligible = codeEligible;
		this.odrPrix = odrPrix;
		this.odfPrix = odfPrix;
	}

	@Id
	@Column(name = "CODE_ELIGIBLE", nullable = false)
	public String getCodeEligible() {
		return codeEligible;
	}
	public void setCodeEligible(String codeEligible) {
		this.codeEligible = codeEligible;
	}

	@Column(name = "ODR_PRIX", nullable = false, columnDefinition = "int default 30")
	public Integer getOdrPrix() {
		return odrPrix;
	}

	public void setOdrPrix(Integer odrPrix) {
		this.odrPrix = odrPrix;
	}

	@Column(name = "ODF_PRIX", nullable = false, columnDefinition = "int default 0")
	public Integer getOdfPrix() {
		return odfPrix;
	}

	public void setOdfPrix(Integer odfPrix) {
		this.odfPrix = odfPrix;
	}
}
