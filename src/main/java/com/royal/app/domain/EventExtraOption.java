package com.royal.app.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * A EventExtraOption.
 */
@Entity
@Table(name = "event_extra_option")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id")
public class EventExtraOption implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "option_qty")
    private Double optionQty;

    @Column(name = "option_notes")
    private String optionNotes;

    @Column(name = "price")
    private Double price;

    @NotNull
    @ManyToOne
    private Event event;

    @NotNull
    @ManyToOne
    private ExtraOption option;

    @ManyToOne
    private ExtraOptionVariant variant;

    @ManyToOne
    private ExtraOptionColor color;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getOptionQty() {
        return optionQty;
    }

    public EventExtraOption optionQty(Double optionQty) {
        this.optionQty = optionQty;
        return this;
    }

    public void setOptionQty(Double optionQty) {
        this.optionQty = optionQty;
    }

    public String getOptionNotes() {
        return optionNotes;
    }

    public EventExtraOption optionNotes(String optionNotes) {
        this.optionNotes = optionNotes;
        return this;
    }

    public void setOptionNotes(String optionNotes) {
        this.optionNotes = optionNotes;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Event getEvent() {
        return event;
    }

    public EventExtraOption event(Event event) {
        this.event = event;
        return this;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public ExtraOption getOption() {
        return option;
    }

    public EventExtraOption option(ExtraOption extraOption) {
        this.option = extraOption;
        return this;
    }

    public void setOption(ExtraOption extraOption) {
        this.option = extraOption;
    }

    public ExtraOptionVariant getVariant() {
        return variant;
    }

    public EventExtraOption variant(ExtraOptionVariant extraOptionVariant) {
        this.variant = extraOptionVariant;
        return this;
    }

    public void setVariant(ExtraOptionVariant extraOptionVariant) {
        this.variant = extraOptionVariant;
    }

    public ExtraOptionColor getColor() {
        return color;
    }

    public EventExtraOption color(ExtraOptionColor extraOptionColor) {
        this.color = extraOptionColor;
        return this;
    }

    public void setColor(ExtraOptionColor extraOptionColor) {
        this.color = extraOptionColor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EventExtraOption eventExtraOption = (EventExtraOption) o;
        if (eventExtraOption.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, eventExtraOption.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "EventExtraOption{" +
            "id=" + id +
            ", optionQty='" + optionQty + "'" +
            ", optionNotes='" + optionNotes + "'" +
            '}';
    }
}
