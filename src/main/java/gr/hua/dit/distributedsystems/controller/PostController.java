package gr.hua.dit.distributedsystems.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import gr.hua.dit.distributedsystems.dao.PostDAO;
import gr.hua.dit.distributedsystems.entities.Post;

@RestController
@RequestMapping("/posts")
public class PostController {

	@Autowired
	private PostDAO postDAO;
	
	@GetMapping("")
	public List<Post> getAllPosts() {
		return postDAO.findAll();
	}
	
	@GetMapping("/{id}")
	public Post getPost(@PathVariable int id) {
		Post post = postDAO.findById(id);
		
		if(post == null) {
			throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Room does not exist"
            );

		}
		
		return post;
		
	}
	
	
	@PostMapping("")
	public Post save(@RequestBody Post post) {
		post.setId(0);
		postDAO.save(post);
		return post;
	}
	
	
}
