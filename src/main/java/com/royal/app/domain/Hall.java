package com.royal.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

import com.royal.app.domain.enumeration.HallType;

/**
 * A Hall.
 */
@Entity
@Table(name = "hall")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Hall implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "hall_name", nullable = false)
    private String hallName;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "hall_type", nullable = false)
    private HallType hallType;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private Double price;

    @Column(name = "capacity")
    private Integer capacity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHallName() {
        return hallName;
    }

    public Hall hallName(String hallName) {
        this.hallName = hallName;
        return this;
    }

    public void setHallName(String hallName) {
        this.hallName = hallName;
    }

    public HallType getHallType() {
        return hallType;
    }

    public Hall hallType(HallType hallType) {
        this.hallType = hallType;
        return this;
    }

    public void setHallType(HallType hallType) {
        this.hallType = hallType;
    }

    public String getDescription() {
        return description;
    }

    public Hall description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public Hall price(Double price) {
        this.price = price;
        return this;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public Hall capacity(Integer capacity) {
        this.capacity = capacity;
        return this;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Hall hall = (Hall) o;
        if (hall.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, hall.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Hall{" +
            "id=" + id +
            ", hallName='" + hallName + "'" +
            ", hallType='" + hallType + "'" +
            ", description='" + description + "'" +
            ", price='" + price + "'" +
            ", capacity='" + capacity + "'" +
            '}';
    }
}
