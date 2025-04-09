package com.jonathans.models;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.*;

@Entity
@Table(name = "warehouse")
public class Warehouse {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;

	@Column(unique = true, nullable = false)
	private String name;

	@Column(nullable = false)
	private String location;

	@Column(nullable = false)
	private int max_capacity;

	@Column(nullable = false, updatable = false)
	private LocalDateTime created_at = LocalDateTime.now();

	public Warehouse() {
		super();
	}

	public Warehouse(String name, String location, int maxCapacity) {
		this.name = name;
		this.location = location;
		this.max_capacity = maxCapacity;
		this.created_at = LocalDateTime.now(); // Set automatically
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

	public LocalDateTime getCreated_at() {
		return created_at;
	}

	public void setCreated_at(LocalDateTime created_at) {
		this.created_at = created_at;
	}

	@Override
	public String toString() {
		return "Warehouse [id=" + id + ", name=" + name + ", location=" + location + ", max_capacity=" + max_capacity
				+ ", created_at=" + created_at + "]";
	}

}
