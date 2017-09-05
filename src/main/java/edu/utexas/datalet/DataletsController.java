package edu.utexas.datalet;

import edu.utexas.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/datalets")
public class DataletsController {
	private static final Logger log = LoggerFactory.getLogger(DataletsController.class);

	@Autowired
	private DataletService dataletService;

	@RequestMapping(method = RequestMethod.GET)
	public List<Datalet> getAllDatalets() {
		List<Datalet> datalets = dataletService.getAllDatalets();
		log.error("GetAllDatalets: " + datalets);
		return datalets;
	}

	@RequestMapping(method = RequestMethod.POST)
	public Datalet createDatalet(@RequestBody Datalet datalet) {
		log.error("CreateDatalet: " + datalet);
		return dataletService.createDatalet(datalet);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public Datalet getDatalet(@PathVariable String id) {
		Datalet datalet = dataletService.getDatalet(id);
		log.error("GetDatalet: " + id + ", " + datalet);
		return datalet;
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	public Datalet updateDatalet(@PathVariable String id,
								 @RequestBody Datalet datalet) {
		log.error("UpdateDatalet: " + datalet);
		return dataletService.updateDatalet(id, datalet);
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public void deleteDatalet(@PathVariable String id) {
		log.error("DeleteDatalet: " + id);
		dataletService.deleteDatalet(id);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{id}/users")
	public List<User> getUsersWithAccessToDatalet(@PathVariable String id) {
		List<User> users = dataletService.getUsersWithAccessTo(id);
		log.error("GetUsersWithAccess: " + users);
		return users;
	}
}
