package edu.utexas.user;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
interface UserRepository extends MongoRepository<User, String>, UserRepositoryCustom {

	User findByEmail(String email);

	User deleteByEmail(String email);

	User findByDeviceID(String deviceID);
}
