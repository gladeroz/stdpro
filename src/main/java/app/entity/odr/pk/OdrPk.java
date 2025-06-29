package app.entity.odr.pk;

import java.io.Serializable;

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
public class OdrPk implements Serializable {
	private static final long serialVersionUID = 1L;
	@EqualsAndHashCode.Include 
	private String nbrContractRedbox;
	@EqualsAndHashCode.Include 
	private String transactionType;
}
