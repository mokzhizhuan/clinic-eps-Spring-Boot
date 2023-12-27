package com.clinicapp.admin.payment;

import org.springframework.data.repository.CrudRepository;

import com.clinicapp.entity.Payment;


public interface PaymentRepository extends CrudRepository<Payment, Integer> {

}
