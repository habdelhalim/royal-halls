package com.royal.app.repository;

import com.royal.app.domain.Contract;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Contract entity.
 */
@SuppressWarnings("unused")
public interface ContractRepository extends JpaRepository<Contract, Long> {
    Page<Contract> findByCustomerCustomerNameContainsOrId(Pageable pageable, String customerName, Long id);
}
