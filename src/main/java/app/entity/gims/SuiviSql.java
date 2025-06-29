package app.entity.gims;

import app.entity.gims.pk.SuiviGimsPk;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "SUIVI")
@Getter
@Setter
@NoArgsConstructor
public class SuiviSql {

	@EmbeddedId private SuiviGimsPk suiviGimsPk;

	@ManyToOne
	TraitementSql traitement;
}
