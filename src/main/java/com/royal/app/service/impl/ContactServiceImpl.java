package com.royal.app.service.impl;

import com.royal.app.service.ContactService;
import com.royal.app.domain.Contact;
import com.royal.app.repository.ContactRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing Contact.
 */
@Service
@Transactional
public class ContactServiceImpl implements ContactService{

    private final Logger log = LoggerFactory.getLogger(ContactServiceImpl.class);
    
    @Inject
    private ContactRepository contactRepository;

    /**
     * Save a contact.
     *
     * @param contact the entity to save
     * @return the persisted entity
     */
    public Contact save(Contact contact) {
        log.debug("Request to save Contact : {}", contact);
        Contact result = contactRepository.save(contact);
        return result;
    }

    /**
     *  Get all the contacts.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Contact> findAll(Pageable pageable) {
        log.debug("Request to get all Contacts");
        Page<Contact> result = contactRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one contact by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Contact findOne(Long id) {
        log.debug("Request to get Contact : {}", id);
        Contact contact = contactRepository.findOne(id);
        return contact;
    }

    /**
     *  Delete the  contact by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Contact : {}", id);
        contactRepository.delete(id);
    }
}
