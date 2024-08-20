package cl.bch.cloud.repositories;

import cl.bch.cloud.entities.RandomGenerated;
import io.micrometer.observation.annotation.Observed;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
* An interface that uses the CrudRepository definition from the Springframework for implementing a CRUD
* For more complex queries, an implementation should be coded
*/
@Observed
@Repository("randomGeneratedRepository")
public interface RandomGeneratedRepository extends CrudRepository<RandomGenerated, Long> {

}