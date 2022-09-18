package app.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import app.entity.TraitementSql;
import app.entity.pk.OdrPk;

@Repository
public interface TraitementRepository extends CrudRepository<TraitementSql, Long>{

	TraitementSql findByOdrPk(OdrPk traitementPk);
}

