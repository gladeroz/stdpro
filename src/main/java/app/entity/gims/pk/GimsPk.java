package app.entity.gims.pk;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GimsPk implements Serializable {
	private static final long serialVersionUID = 1L;
	@EqualsAndHashCode.Include 
	@Column(name = "tiers_code", nullable = false)
	private Integer tiersCode;
	@EqualsAndHashCode.Include 
	@Column(name = "numero_facture", nullable = false)
	private String numeroFacture;
}

