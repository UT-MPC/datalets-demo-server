package edu.utexas.user;

import edu.utexas.datalet.Criteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.List;

class UserRepositoryImpl implements UserRepositoryCustom {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public List<User> searchUsersByAvailability(Criteria criteria) {
		//TODO: fill out how to create custom query
		return null;
	}
}
