package com.royal.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Contract.
 */
@Entity
@Table(name = "contract")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Contract implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "contract_date", nullable = false)
    private ZonedDateTime contractDate;

    @ManyToOne
    private Customer customer;

    @OneToMany(mappedBy = "contract")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Event> events = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getContractDate() {
        return contractDate;
    }

    public Contract contractDate(ZonedDateTime contractDate) {
        this.contractDate = contractDate;
        return this;
    }

    public void setContractDate(ZonedDateTime contractDate) {
        this.contractDate = contractDate;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Contract customer(Customer customer) {
        this.customer = customer;
        return this;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Set<Event> getEvents() {
        return events;
    }

    public Contract events(Set<Event> events) {
        this.events = events;
        return this;
    }

    public Contract addEvent(Event event) {
        events.add(event);
        event.setContract(this);
        return this;
    }

    public Contract removeEvent(Event event) {
        events.remove(event);
        event.setContract(null);
        return this;
    }

    public void setEvents(Set<Event> events) {
        this.events = events;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Contract contract = (Contract) o;
        if (contract.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, contract.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Contract{" +
            "id=" + id +
            ", contractDate='" + contractDate + "'" +
            '}';
    }
}
