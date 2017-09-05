package edu.utexas.user;

import edu.utexas.datalet.Criteria;

import java.util.List;

interface UserRepositoryCustom {

	List<User> searchUsersByAvailability(Criteria criteria);
}
