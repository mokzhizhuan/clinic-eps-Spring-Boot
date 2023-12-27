package com.clinicapp.entity;

import java.util.HashSet;
import java.util.Set;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "prescription")
public class Prescription {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(length = 128, nullable = false)
	private String title;
	
	@Column(length = 128, nullable = false)
	private String description;
	
	@ManyToMany
	@JoinTable(
			name = "prescription_medicine",
			joinColumns = @JoinColumn(name = "prescription_id"),
			inverseJoinColumns = @JoinColumn(name = "medicine_id")
			)
	private Set<Medicine> medicine = new HashSet<>();
	
	private float cost;
	
	private float totalcost;
	
	private boolean verfication;
	
	private String verficationcode;
	
	@OneToOne
	@JoinColumn(name = "patient_id")
	private User patient;
	
	private String name;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<Medicine> getMedicine() {
		return medicine;
	}

	public void setMedicine(Set<Medicine> medicine) {
		this.medicine = medicine;
	}

	public float getTotalcost() {
		return totalcost;
	}

	public void setTotalcost(float totalcost) {
		this.totalcost = totalcost;
	}

	public User getPatient() {
		return patient;
	}

	public void setPatient(User patient) {
		this.patient = patient;
	}

	public float getCost() {
		return cost;
	}

	public void setCost(float cost) {
		this.cost = cost;
	}

	public boolean isVerfication() {
		return verfication;
	}

	public void setVerfication(boolean verfication) {
		this.verfication = verfication;
	}

	public String getVerficationcode() {
		return verficationcode;
	}

	public void setVerficationcode(String verficationcode) {
		this.verficationcode = verficationcode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
