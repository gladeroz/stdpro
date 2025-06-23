package app.entity.gims;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import app.entity.gims.pk.SuiviGimsPk;
import lombok.Data;


@Entity
@Table(name = "SUIVI")
@Data
public class SuiviSql {

	@EmbeddedId private SuiviGimsPk suiviGimsPk;

	@ManyToOne
	TraitementSql traitement;
}
