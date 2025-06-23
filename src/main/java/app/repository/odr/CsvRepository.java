package app.repository.odr;

import org.springframework.data.repository.CrudRepository;

import app.entity.odr.CsvSql;
import app.entity.odr.pk.OdrPk;

//@Repository
public interface CsvRepository extends CrudRepository<CsvSql, OdrPk>{

	CsvSql findByOdrPk(OdrPk csvPk);
}

