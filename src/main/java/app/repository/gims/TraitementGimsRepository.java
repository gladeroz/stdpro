package app.repository.gims;

import java.util.List;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import app.entity.gims.TraitementSql;
import app.entity.gims.pk.GimsPk;

public interface TraitementGimsRepository extends CrudRepository<TraitementSql, GimsPk>{

	TraitementSql findByGimsPk(GimsPk traitementPk);
	
	List<TraitementSql> findByPayeTrue();
	
	List<TraitementSql> findByPayeFalse();
	
    @Modifying
    @Transactional
    @Query("UPDATE TraitementSql s SET s.paye = :paye")
    int updateAllPaye(@Param("paye") Boolean paye);
}

