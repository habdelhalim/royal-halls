package com.royal.app.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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
 * A Event.
 */
@Entity
@Table(name = "event")
@NamedQueries(@NamedQuery(name = "Event.findEventsInInterval",
    query = "select e from Event e where  e.hall = :hall and  ( e.eventStartDate between :start and :end or e.eventEndDate between :start and :end )"))
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id")
public class Event implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Field
    @Column(name = "event_name")
    private String eventName;

    @Field(analyze = Analyze.NO)
    @DateBridge(resolution = Resolution.DAY, encoding = EncodingType.STRING)
    @NotNull
    @Column(name = "event_start_date", nullable = false)
    private ZonedDateTime eventStartDate;

    @NotNull
    @Column(name = "event_end_date", nullable = false)
    private ZonedDateTime eventEndDate;

    @Column(name = "created_date")
    private ZonedDateTime createdDate;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "base_price")
    private Double basePrice;

    @OneToMany(mappedBy = "event", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private Set<EventExtraOption> options = new HashSet<>();

    @NotNull
    @ManyToOne
    private EventType eventType;

    @NotNull
    @ManyToOne
    private Hall hall;

    @NotNull
    @ManyToOne
    private Contract contract;

    @ManyToOne
    @JoinColumn(name = "first_beneficiary")
    private Customer firstBeneficiary;

    @ManyToOne
    @JoinColumn(name = "second_beneficiary")
    private Customer secondBeneficiary;

    @Column(name = "google_event_id")
    private String googleEventId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEventName() {
        return eventName;
    }

    public Event eventName(String eventName) {
        this.eventName = eventName;
        return this;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public ZonedDateTime getEventStartDate() {
        return eventStartDate;
    }

    public Event eventStartDate(ZonedDateTime eventStartDate) {
        this.eventStartDate = eventStartDate;
        return this;
    }

    public void setEventStartDate(ZonedDateTime eventStartDate) {
        this.eventStartDate = eventStartDate;
    }

    public ZonedDateTime getEventEndDate() {
        return eventEndDate;
    }

    public Event eventEndDate(ZonedDateTime eventEndDate) {
        this.eventEndDate = eventEndDate;
        return this;
    }

    public void setEventEndDate(ZonedDateTime eventEndDate) {
        this.eventEndDate = eventEndDate;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public Event createdDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public Event createdBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(Double basePrice) {
        this.basePrice = basePrice;
    }

    public Set<EventExtraOption> getOptions() {
        return options;
    }

    public Event options(Set<EventExtraOption> eventExtraOptions) {
        this.options = eventExtraOptions;
        return this;
    }

    public Event addOption(EventExtraOption eventExtraOption) {
        options.add(eventExtraOption);
        eventExtraOption.setEvent(this);
        return this;
    }

    public Event removeOption(EventExtraOption eventExtraOption) {
        options.remove(eventExtraOption);
        eventExtraOption.setEvent(null);
        return this;
    }

    public void setOptions(Set<EventExtraOption> eventExtraOptions) {
        this.options = eventExtraOptions;
    }

    public EventType getEventType() {
        return eventType;
    }

    public Event eventType(EventType eventType) {
        this.eventType = eventType;
        return this;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public Hall getHall() {
        return hall;
    }

    public Event hall(Hall hall) {
        this.hall = hall;
        return this;
    }

    public void setHall(Hall hall) {
        this.hall = hall;
    }

    public Contract getContract() {
        return contract;
    }

    public Event contract(Contract contract) {
        this.contract = contract;
        return this;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    public Customer getFirstBeneficiary() {
        return firstBeneficiary;
    }

    public void setFirstBeneficiary(Customer firstBeneficiary) {
        this.firstBeneficiary = firstBeneficiary;
    }

    public Customer getSecondBeneficiary() {
        return secondBeneficiary;
    }

    public void setSecondBeneficiary(Customer secondBeneficiary) {
        this.secondBeneficiary = secondBeneficiary;
    }

    public String getGoogleEventId() {
        return googleEventId;
    }

    public void setGoogleEventId(String googleEventId) {
        this.googleEventId = googleEventId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Event event = (Event) o;
        if (event.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, event.id);
    }

    @PrePersist
    void onCreate() {
        this.setCreatedDate(ZonedDateTime.now());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Event{" +
            "id=" + id +
            ", eventName='" + eventName + "'" +
            ", eventStartDate='" + eventStartDate + "'" +
            ", eventEndDate='" + eventEndDate + "'" +
            ", createdDate='" + createdDate + "'" +
            ", createdBy='" + createdBy + "'" +
            '}';
    }
}
