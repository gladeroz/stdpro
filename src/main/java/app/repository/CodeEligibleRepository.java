package app.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import app.entity.CodeEligibleSql;
import app.entity.TraitementSql;
import app.entity.pk.OdrPk;

@Repository
public interface CodeEligibleRepository extends CrudRepository<CodeEligibleSql, Long>{
	
	CodeEligibleSql findByCodeEligible(String codeProduct);
}

