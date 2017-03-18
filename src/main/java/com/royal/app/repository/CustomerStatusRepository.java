package com.royal.app.repository;

import com.royal.app.domain.CustomerStatus;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the CustomerStatus entity.
 */
@SuppressWarnings("unused")
public interface CustomerStatusRepository extends JpaRepository<CustomerStatus,Long> {

}
