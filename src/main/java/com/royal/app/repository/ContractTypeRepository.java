package com.royal.app.repository;

import com.royal.app.domain.ContractType;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ContractType entity.
 */
@SuppressWarnings("unused")
public interface ContractTypeRepository extends JpaRepository<ContractType,Long> {

}
