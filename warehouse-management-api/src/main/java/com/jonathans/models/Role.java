package com.jonathans.models;

import java.util.UUID;
import jakarta.persistence.*;


@Entity
@Table(name = "roles")
public class Role {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;
	
	@Column(name = "role_name", unique = true, nullable = false)
	private String roleName;
	
	

	public Role() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Role(UUID id, String roleName) {
		super();
		this.id = id;
		this.roleName = roleName;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	@Override
	public String toString() {
		return "Role [id=" + id + ", roleName=" + roleName + "]";
	}
	
}
