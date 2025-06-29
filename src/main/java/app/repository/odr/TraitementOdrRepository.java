package app.repository.odr;

import java.util.Date;

import org.springframework.data.repository.CrudRepository;

import app.entity.odr.TraitementSql;
import app.entity.odr.pk.OdrPk;

//@Repository
public interface TraitementOdrRepository extends CrudRepository<TraitementSql, OdrPk>{

	TraitementSql findByOdrPk(OdrPk traitementPk);

	TraitementSql[] findAllByDateTraitementGreaterThanEqualAndDateTraitementLessThanEqual(Date intervalMin, Date intervalMax);

	TraitementSql[] findAllByDateTraitementGreaterThanEqual(Date intervalMin);

	TraitementSql[] findAllByDateTraitementLessThanEqual(Date intervalMax);
}

