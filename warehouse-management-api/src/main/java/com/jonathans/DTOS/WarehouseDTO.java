package com.jonathans.DTOS;

import java.util.List;
import java.util.UUID;

public class WarehouseDTO {
	private UUID id; 
	private String name; 
	private String location; 
	private int max_capacity;
	private List<WarehousePersonnelDTO> personnel;
	
	 public WarehouseDTO() {
	    }
	 
	 
	public WarehouseDTO(UUID id, String name, String location, int max_capacity) {
		super();
		this.id = id;
		this.name = name;
		this.location = location;
		this.max_capacity = max_capacity;
	}
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public int getMax_capacity() {
		return max_capacity;
	}
	public void setMax_capacity(int max_capacity) {
		this.max_capacity = max_capacity;
	}
	public List<WarehousePersonnelDTO> getPersonnel() {
		return personnel;
	}
	public void setPersonnel(List<WarehousePersonnelDTO> personnel) {
		this.personnel = personnel;
	}
	@Override
	public String toString() {
		return "WarehouseDTO [id=" + id + ", name=" + name + ", location=" + location + ", max_capacity=" + max_capacity
				+ "]";
	} 
	
	
}


