package edu.utexas.datalet;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DataletRepository extends MongoRepository<Datalet, String>, DataletRepositoryCustom {

	@Query("{ 'timeAvailable': {$ne:null} }")
	List<Datalet> findByTimeAvailableIsSet();

	List<Datalet> findByOwnerID(String ownerID);
}
