package com.jonathans.services;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.jonathans.DTOS.RoleDTO;
import com.jonathans.models.Role;
import com.jonathans.repositories.RoleRepository;


@Service
public class RoleService {
	
	private RoleRepository repo;

	public RoleService(RoleRepository repo) {
		super();
		this.repo = repo;
	} 
	
//	GET ALL 
	public Iterable<Role> findAll() { 
		return repo.findAll();
	}
	
//	GET ALL BY ID 
	 public ResponseEntity<Role> findById(UUID roleId) {
	        return repo.findById(roleId)
	                   .map(role -> ResponseEntity.ok(role))  // Return 200 if found
	                   .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)); // 404 if not found
	    }
	 
//	 POST - CREATE 
	 public ResponseEntity<Role> addOne(RoleDTO roleDTO) { 
		 Role newRole = new Role(null, roleDTO.getRoleName());
		 Role savedRole = repo.save(newRole);
		 return ResponseEntity.status(HttpStatus.CREATED).body(savedRole);
	 }
	 
//	 PUT - UPDATE 
	 public ResponseEntity<Role> updateOne(UUID roleId, RoleDTO roleDTO ) { 
		 if (repo.existsById(roleId))
			 return ResponseEntity.status(HttpStatus.OK)
					              .body(repo.save(new Role(roleId, roleDTO.getRoleName())));
		 else
			 return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
	 }
	 
	
//DELETE
	 public ResponseEntity<Void> deleteById(UUID roleId) {
	        if (!repo.existsById(roleId)) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	        }
	        repo.deleteById(roleId);
	        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();  // ✅ 204 No Content if deleted
	    }
	
	
}
