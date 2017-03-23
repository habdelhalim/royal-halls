package com.royal.app.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.royal.app.domain.enumeration.ContractStatus;
import com.voodoodyne.jackson.jsog.JSOGGenerator;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.search.annotations.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A Contract.
 */
@Indexed
@Entity
@Table(name = "contract")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@JsonIdentityInfo(generator = JSOGGenerator.class)
public class Contract implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Field(analyze = Analyze.NO)
    @DateBridge(resolution = Resolution.DAY, encoding = EncodingType.STRING)
    @NotNull
    @Column(name = "contract_date", nullable = false)
    private ZonedDateTime contractDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "contract_status", nullable = false)
    private ContractStatus contractStatus;

    @Field
    @Column(name = "contract_notes")
    private String contractNotes;

    @Column(name = "total_amount")
    private Double totalAmount;

    @Column(name = "open_amount")
    private Double openAmount;

    @Column(name = "created_date")
    private ZonedDateTime createdDate;

    @Column(name = "created_by")
    private String createdBy;

    @IndexedEmbedded
    @ManyToOne
    private Customer customer;

    @OneToMany(mappedBy = "contract", orphanRemoval = true)
    private Set<Payment> payments = new HashSet<>();

    @IndexedEmbedded
    @OneToMany(mappedBy = "contract", orphanRemoval = true)
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

    public ContractStatus getContractStatus() {
        return contractStatus;
    }

    public Contract contractStatus(ContractStatus contractStatus) {
        this.contractStatus = contractStatus;
        return this;
    }

    public void setContractStatus(ContractStatus contractStatus) {
        this.contractStatus = contractStatus;
    }

    public String getContractNotes() {
        return contractNotes;
    }

    public Contract contractNotes(String contractNotes) {
        this.contractNotes = contractNotes;
        return this;
    }

    public void setContractNotes(String contractNotes) {
        this.contractNotes = contractNotes;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public Contract totalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
        return this;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Double getOpenAmount() {
        return openAmount;
    }

    public Contract openAmount(Double openAmount) {
        this.openAmount = openAmount;
        return this;
    }

    public void setOpenAmount(Double openAmount) {
        this.openAmount = openAmount;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public Contract createdDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public Contract createdBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
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

    public Set<Payment> getPayments() {
        return payments;
    }

    public Contract payments(Set<Payment> payments) {
        this.payments = payments;
        return this;
    }

    public Contract addPayment(Payment payment) {
        payments.add(payment);
        payment.setContract(this);
        return this;
    }

    public Contract removePayment(Payment payment) {
        payments.remove(payment);
        payment.setContract(null);
        return this;
    }

    public void setPayments(Set<Payment> payments) {
        this.payments = payments;
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
            ", contractStatus='" + contractStatus + "'" +
            ", contractNotes='" + contractNotes + "'" +
            ", totalAmount='" + totalAmount + "'" +
            ", openAmount='" + openAmount + "'" +
            ", createdDate='" + createdDate + "'" +
            ", createdBy='" + createdBy + "'" +
            '}';
    }
}
