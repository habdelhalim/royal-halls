package com.royal.app.repository;

import com.royal.app.domain.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Customer entity.
 */
@SuppressWarnings("unused")
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Page<Customer> findByCustomerNameStartingWith(String customerName, Pageable pageable);
}
