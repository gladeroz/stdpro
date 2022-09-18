package app.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import app.entity.CsvSql;
import app.entity.pk.OdrPk;

@Repository
public interface CsvRepository extends CrudRepository<CsvSql, Long>{

	CsvSql findByOdrPk(OdrPk csvPk);
}

