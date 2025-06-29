package app.repository.odr;

import org.springframework.data.repository.CrudRepository;

import app.entity.odr.CodeEligibleSql;

//@Repository
public interface CodeEligibleRepository extends CrudRepository<CodeEligibleSql, Long>{

	CodeEligibleSql findByCodeEligible(String codeProduct);
}

