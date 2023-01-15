package gr.hua.dit.distributedsystems.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import gr.hua.dit.distributedsystems.dao.PostDAO;
import gr.hua.dit.distributedsystems.dao.RoomDAO;
import gr.hua.dit.distributedsystems.dao.UserDAO;
import gr.hua.dit.distributedsystems.entities.Post;
import gr.hua.dit.distributedsystems.entities.Room;
import gr.hua.dit.distributedsystems.entities.User;
import gr.hua.dit.distributedsystems.repository.RoomRepo;
import gr.hua.dit.distributedsystems.service.RoomService;

@RestController
@RequestMapping("/rooms")
public class RoomController {

	@Autowired
	private RoomService roomService;
//	private RoomDAO roomDAO;
	
	@Autowired 
	private PostDAO postDAO;
	
	@Autowired 
	private UserDAO userDAO;
	
	@Autowired
	private RoomRepo roomRepo;
	
	@GetMapping("")
	public List<Room> getAllRooms() {
		return roomService.getRooms();
	}
	
	@GetMapping("/{rid}/user/{uid}")
	public Room getRoom(@PathVariable int rid, @PathVariable int uid) {
		User user = userDAO.findById(uid);

		if(user == null) {
			throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "User does not exist"
            );

		}
		
		//getRoomById because both member and admin can access it
		Room room = roomService.getRoomById(rid, user);
		
		if(room == null) {
			throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Room does not exist"
            );

		}


		
		return room;
		
	}
	
	@PostMapping("/admin/{aid}")
	public Room save(@RequestBody Room room, @PathVariable int aid) {
		User admin = userDAO.findById(aid);
		
		if(admin == null) {
			throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Something Went Wrong"
            );

		}
		
		admin.addAdminRoom(room);
		roomRepo.save(room);
		
		return room;
	}
	
	@PostMapping("/{rid}/postcreator/{cid}")
	public Post addPost(@PathVariable int rid, @PathVariable int cid, @RequestBody Post post) {
		User creator = userDAO.findById(cid);
		
		
		if(creator == null) {
			throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Something Went Wrong"
            );

		}
		
		//getRoomById because members and admin can make a post
		Room room = roomService.getRoomById(rid, creator);
		
		String content = post.getContent();
		
		if(room == null) {
			throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Room does not exist"
            );

		}
		
		
		
		Post apost = new Post(content, creator.getUsername());
		
		room.addPost(apost);
		postDAO.save(apost);
		return apost;
		
	}
	
	@PatchMapping("/{rid}/kick/{mid}/admin/{aid}")
	public User kickMember(@PathVariable int rid, @PathVariable int mid, @PathVariable int aid) {
		User member = userDAO.findById(mid);
		User admin = userDAO.findById(aid);
		
		if(member == null) {
			throw new ResponseStatusException(
					HttpStatus.NOT_FOUND, "User does not exist"
			);
		}
		
		if(admin == null) {
			throw new ResponseStatusException(
					HttpStatus.NOT_FOUND, "User does not exist"
			);
		}
		
		Room room = roomService.getRoomForAdmin(rid, admin);
		
		member.removeMemberRoom(room);
		userDAO.save(member);
		
		return member;
		
	}
	
	@DeleteMapping("/{rid}/admin/{aid}")
	public void deleteRoom(@PathVariable int rid, @PathVariable int aid) {
		User admin = userDAO.findById(aid);
		
		if(admin == null) {
			throw new ResponseStatusException(
					HttpStatus.NOT_FOUND, "User does not exist"
			);
		}
		
		Room room = roomService.getRoomForAdmin(rid, admin);
		
		if(room == null) {
			throw new ResponseStatusException(
					HttpStatus.NOT_FOUND, "Room does not exist"
			);
		}
		
		roomService.deleteRoom(rid);
		
	}
	
	
}






