package gr.hua.dit.distributedsystems.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import gr.hua.dit.distributedsystems.dao.UserDAO;
import gr.hua.dit.distributedsystems.entities.Room;
import gr.hua.dit.distributedsystems.entities.User;
import gr.hua.dit.distributedsystems.repository.UserRepo;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserDAO userDAO;
	
	@Autowired
	private UserRepo userRepo;
	
	@GetMapping("")
	public List<User> getAll() {
		return userDAO.findAll();
	}
	
	@GetMapping("/{id}")
	public User getUser(@PathVariable int id) {
		User user = userDAO.findById(id);
		
		if(user == null) {
			throw new ResponseStatusException(
					HttpStatus.NOT_FOUND, "user not found"
			);
		}
		
		return user;
		
	}
	
	@PostMapping("")
	public User save(@RequestBody User user) {
		user.setId(0);
		userDAO.save(user);
		return user;
	}
	
	@GetMapping("/{id}/member")
	public List<Room> getMemberRooms(@PathVariable int id) {
		User user = userDAO.findById(id);
		
		if(user == null) {
			throw new ResponseStatusException(
					HttpStatus.NOT_FOUND, "user not found"
			);
		}
		
		
		
		return user.getMemberRooms();
		
	}
	
	@GetMapping("/name/{username}")
	public User getUserByName(@PathVariable String username) {
		return userRepo.findByUsername(username).orElseThrow(() -> new ResponseStatusException(
					HttpStatus.NOT_FOUND, "User does not exist"
				));
	}
	
	
}












