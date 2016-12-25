package com.royal.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A HallType.
 */
@Entity
@Table(name = "hall_type")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class HallType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "hall_type_name")
    private String hallTypeName;

    @Column(name = "description")
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHallTypeName() {
        return hallTypeName;
    }

    public HallType hallTypeName(String hallTypeName) {
        this.hallTypeName = hallTypeName;
        return this;
    }

    public void setHallTypeName(String hallTypeName) {
        this.hallTypeName = hallTypeName;
    }

    public String getDescription() {
        return description;
    }

    public HallType description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HallType hallType = (HallType) o;
        if (hallType.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, hallType.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "HallType{" +
            "id=" + id +
            ", hallTypeName='" + hallTypeName + "'" +
            ", description='" + description + "'" +
            '}';
    }
}
