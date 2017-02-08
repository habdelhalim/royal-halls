package com.royal.app.service.impl;

import com.royal.app.domain.ExtraOption;
import com.royal.app.domain.enumeration.OptionType;
import com.royal.app.repository.ExtraOptionRepository;
import com.royal.app.service.ExtraOptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing ExtraOption.
 */
@Service
@Transactional
public class ExtraOptionServiceImpl implements ExtraOptionService {

    private final Logger log = LoggerFactory.getLogger(ExtraOptionServiceImpl.class);

    @Inject
    private ExtraOptionRepository extraOptionRepository;

    /**
     * Save a extraOption.
     *
     * @param extraOption the entity to save
     * @return the persisted entity
     */
    public ExtraOption save(ExtraOption extraOption) {
        log.debug("Request to save ExtraOption : {}", extraOption);
        ExtraOption result = extraOptionRepository.save(extraOption);
        return result;
    }

    /**
     *  Get all the extraOptions.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ExtraOption> findAll(Pageable pageable) {
        log.debug("Request to get all ExtraOptions");
        Page<ExtraOption> result = extraOptionRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one extraOption by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public ExtraOption findOne(Long id) {
        log.debug("Request to get ExtraOption : {}", id);
        ExtraOption extraOption = extraOptionRepository.findOne(id);
        return extraOption;
    }

    /**
     *  Delete the  extraOption by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ExtraOption : {}", id);
        extraOptionRepository.delete(id);
    }

    @Override public List<ExtraOption> findAllByType(String type) {
        log.debug("Request to get all ExtraOptions by type {} ", type);
        return extraOptionRepository.findByOptionType(Enum.valueOf(OptionType.class, type.toUpperCase()));
    }
}
