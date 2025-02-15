package com.jonathans.controllers;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jonathans.DTOS.RoleDTO;
import com.jonathans.models.Role;
import com.jonathans.services.RoleService;

@RestController
@RequestMapping("/roles")
public class RoleController {
	
	private RoleService service;

	public RoleController(RoleService service) {
		super();
		this.service = service;
	} 
	
//	GET ALL 
	@GetMapping
	public Iterable<Role> findAll() { 
		return service.findAll();
	}
	
//	GET ALL BY ID 
	@GetMapping("/{roleId}")
	public ResponseEntity<Role> findById(@PathVariable UUID roleId) {
		return service.findById(roleId);
	}
	
//	POST - CREATE 
	@PostMapping
	public ResponseEntity<Role> addOne(@RequestBody RoleDTO roleDTO) {
		return service.addOne(roleDTO);
	}
	
//	PUT - UPDATE
	@PutMapping("/{roleId}")
	public ResponseEntity<Role> updateOne(@PathVariable UUID roleId, @RequestBody RoleDTO roleDTO) { 
		return service.updateOne(roleId, roleDTO);
	}
	
	
//	DELETE
	@DeleteMapping("/{roleId}")
	public ResponseEntity<Void> deleteById(@PathVariable("roleId") UUID roleId) {
		return service.deleteById(roleId);
	}
	
}

