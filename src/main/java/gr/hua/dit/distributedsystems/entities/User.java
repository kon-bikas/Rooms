package gr.hua.dit.distributedsystems.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(	name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "email")
        })
public class User {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank
    @Size(min = 3, max = 20)
    private String username;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    @NotBlank
//    @Size(min = 6, max = 30)
    private String password;
    

	@ManyToMany(fetch = FetchType.LAZY, cascade = {
			CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH
	})
    @JoinTable(name = "member_rooms",
    		joinColumns = @JoinColumn(name = "member_id"),
    		inverseJoinColumns = @JoinColumn(name = "room_id")
    )
    private List<Room> memberRooms = new ArrayList<>();
    
    @OneToMany(fetch = FetchType.LAZY, cascade = {
			CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH
	})
    @JoinColumn(name = "admin_id")
    private List<Room> adminRooms;
    
    public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<Room> getMemberRooms() {
		return memberRooms;
	}

	public void setMemberRooms(List<Room> memberRooms) {
		this.memberRooms = memberRooms;
	}

	public List<Room> getAdminRooms() {
		return adminRooms;
	}

	public void setAdminRooms(List<Room> adminRooms) {
		this.adminRooms = adminRooms;
	}
	
	public User() {
    	
    }
    
    public User(@NotBlank @Size(max = 20) String username, @NotBlank @Size(max = 50) @Email String email,
			@NotBlank @Size(max = 120) String password) {
		this.username = username;
		this.email = email;
		this.password = password;
	}

    public void addMemberRoom(Room room) {
    	memberRooms.add(room);
    }

    public void removeMemberRoom(Room room) {
    	memberRooms.remove(room);
    }
    
    public void addAdminRoom(Room room) {
    	if(adminRooms == null) {
    		adminRooms = new ArrayList<>();
    	}
    	
    	adminRooms.add(room);
    	
    }
    
    public boolean isMemberOf(Room room) {
    	if(memberRooms == null) {
    		return false;
    	}
    	
    	for(Room r: memberRooms) {
    		if(room.equals(r)) {
    			return true;
    		}
    	}
    	
    	return false;
    	
    }
    
    public boolean isAdminOf(Room room) {
    	if(adminRooms == null) {
    		return false;
    	}
    	
    	for(Room r: adminRooms) {
    		if(room.equals(r)) {
    			return true;
    		}
    	}
    	
    	return false;
    	
    }
    
    
    
}







