package gr.hua.dit.distributedsystems.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import gr.hua.dit.distributedsystems.entities.Request;
import gr.hua.dit.distributedsystems.entities.Room;
import gr.hua.dit.distributedsystems.entities.User;
import gr.hua.dit.distributedsystems.service.RoomService;
import gr.hua.dit.distributedsystems.dao.RequestDAO;
import gr.hua.dit.distributedsystems.dao.RoomDAO;
import gr.hua.dit.distributedsystems.dao.UserDAO;

@RestController
@RequestMapping("/requests")
public class RequestController {

	@Autowired
	private RequestDAO requestDAO;
	
	@Autowired
	private RoomService roomService;
	
	@Autowired
	private RoomDAO roomDAO;
	
	@Autowired
	private UserDAO userDAO;
	
	@GetMapping("")
	public List<Request> getAllRequests() {
		return requestDAO.findAll();
	}
	
	@PostMapping("/room/{rid}/sender/{sid}")
	public Request makeRequest(@PathVariable int rid, @PathVariable int sid) {
		Room room = roomDAO.findById(rid);
		User sender = userDAO.findById(sid);
		
		if(room == null) {
			throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Room does not exist"
            );
		}
		
		if(sender == null) {
			throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "user does not exist"
            );
		}
		
		Request request = new Request(sender, room);
		room.addRequest(request);
		requestDAO.save(request);
		return request;
		
	}
	
	@GetMapping("/{rid}/admin/{aid}")
	public List<Request> getRoomRequests(@PathVariable int rid, @PathVariable int aid) {
		User admin = userDAO.findById(aid); 
		
		if(admin == null) {
			throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "User does not exist"
            );
		}
		
		Room room = roomService.getRoomForAdmin(rid, admin);
		
		return room.getRequests();
		
	}
	
	@PutMapping("/{req_id}/acceptrequest/{aid}")
	public User acceptRequest(@PathVariable int req_id, @PathVariable int aid) {
		User admin = userDAO.findById(aid);
		Request request = requestDAO.findById(req_id);
		
		
		if(request == null) {
			throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Request does not exist"
            );
		}
		
		if(admin == null) {
			throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "user does not exist"
            );
		}
		
		
		int rid = request.getRoom().getId();
		Room room = roomService.getRoomForAdmin(rid, admin);
		
		User sender = request.getSender();
		sender.addMemberRoom(room);
		userDAO.save(sender);
		
		requestDAO.delete(req_id);
		
		return sender;
		
		
	}
	
	@DeleteMapping("/{req_id}/declinerequest/{aid}")
	public void declineRequest(@PathVariable int req_id, @PathVariable int aid) {
		User admin = userDAO.findById(aid);
		Request request = requestDAO.findById(req_id);
		
		
		if(request == null) {
			throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Request does not exist"
            );
		}
		
		if(admin == null) {
			throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "user does not exist"
            );
		}
		
		
		int rid = request.getRoom().getId();
		Room room = roomService.getRoomForAdmin(rid, admin);
		
		requestDAO.delete(req_id);
	
		
	}
	
}


















