package com.royal.app.repository;

import com.royal.app.domain.ExtraOption;

import com.royal.app.domain.enumeration.OptionType;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ExtraOption entity.
 */
@SuppressWarnings("unused")
public interface ExtraOptionRepository extends JpaRepository<ExtraOption,Long> {

    List<ExtraOption> findByOptionType(OptionType type);
}
