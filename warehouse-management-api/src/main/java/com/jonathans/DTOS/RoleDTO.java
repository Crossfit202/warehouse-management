package com.jonathans.DTOS;

public class RoleDTO {
	
	private String roleName;


	public RoleDTO(String roleName) {
		super();
		this.roleName = roleName;
	}


	public String getRoleName() {
		return roleName;
	}


	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}


	@Override
	public String toString() {
		return "RoleDTO [roleName=" + roleName + "]";
	}
	
	
	
	
	
}
