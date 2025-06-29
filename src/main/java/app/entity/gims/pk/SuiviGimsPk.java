package app.entity.gims.pk;

import java.io.Serializable;
import java.util.Date;

import enums.gims.ActionGims;
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
public class SuiviGimsPk implements Serializable {
	private static final long serialVersionUID = 1L;
	@EqualsAndHashCode.Include 
	private GimsPk gimsPk;
	@EqualsAndHashCode.Include 
	private Date dateAction;
	@EqualsAndHashCode.Include 
	private ActionGims action;
}

