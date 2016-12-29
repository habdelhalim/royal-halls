package com.royal.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A ExtraOptionColor.
 */
@Entity
@Table(name = "extra_option_color")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ExtraOptionColor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "color_name", nullable = false)
    private String colorName;

    @ManyToOne
    private ExtraOption option;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getColorName() {
        return colorName;
    }

    public ExtraOptionColor colorName(String colorName) {
        this.colorName = colorName;
        return this;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }

    public ExtraOption getOption() {
        return option;
    }

    public ExtraOptionColor option(ExtraOption extraOption) {
        this.option = extraOption;
        return this;
    }

    public void setOption(ExtraOption extraOption) {
        this.option = extraOption;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ExtraOptionColor extraOptionColor = (ExtraOptionColor) o;
        if (extraOptionColor.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, extraOptionColor.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ExtraOptionColor{" +
            "id=" + id +
            ", colorName='" + colorName + "'" +
            '}';
    }
}
