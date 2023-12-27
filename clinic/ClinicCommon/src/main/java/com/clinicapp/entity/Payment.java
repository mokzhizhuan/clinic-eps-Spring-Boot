package com.clinicapp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "payment")
public class Payment {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "payment_type", length = 20)
	private PaymentType paymenttype;
	
	@Column(name="credit_card_no", length = 20)
	private String creditcardno;
	
	@Column(name="cvc", length = 3)
	private int cvc;
	
	private float price;
	
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public PaymentType getPaymenttype() {
		return paymenttype;
	}

	public void setPaymenttype(PaymentType paymenttype) {
		this.paymenttype = paymenttype;
	}

	public String getCreditcardno() {
		return creditcardno;
	}

	public void setCreditcardno(String creditcardno) {
		this.creditcardno = creditcardno;
	}

	public int getCvc() {
		return cvc;
	}

	public void setCvc(int cvc) {
		this.cvc = cvc;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}
}
