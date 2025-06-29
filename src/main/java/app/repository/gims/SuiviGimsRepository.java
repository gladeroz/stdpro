package app.repository.gims;

import org.springframework.data.repository.CrudRepository;

import app.entity.gims.SuiviSql;
import app.entity.gims.pk.GimsPk;

public interface SuiviGimsRepository extends CrudRepository<SuiviSql, GimsPk>{
}
