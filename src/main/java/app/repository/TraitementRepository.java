package app.repository;

import java.util.Date;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import app.entity.TraitementSql;
import app.entity.pk.OdrPk;

@Repository
public interface TraitementRepository extends CrudRepository<TraitementSql, OdrPk>{

	TraitementSql findByOdrPk(OdrPk traitementPk);

	TraitementSql[] findAllByDateTraitementGreaterThanEqualAndDateTraitementLessThanEqual(Date intervalMin, Date intervalMax);

	TraitementSql[] findAllByDateTraitementGreaterThanEqual(Date intervalMin);

	TraitementSql[] findAllByDateTraitementLessThanEqual(Date intervalMax);
}

