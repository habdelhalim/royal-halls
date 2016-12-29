package com.royal.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A ExtraOptionVariant.
 */
@Entity
@Table(name = "extra_option_variant")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ExtraOptionVariant implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "variant_name", nullable = false)
    private String variantName;

    @Column(name = "price")
    private Double price;

    @ManyToOne
    private ExtraOption option;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVariantName() {
        return variantName;
    }

    public ExtraOptionVariant variantName(String variantName) {
        this.variantName = variantName;
        return this;
    }

    public void setVariantName(String variantName) {
        this.variantName = variantName;
    }

    public Double getPrice() {
        return price;
    }

    public ExtraOptionVariant price(Double price) {
        this.price = price;
        return this;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public ExtraOption getOption() {
        return option;
    }

    public ExtraOptionVariant option(ExtraOption extraOption) {
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
        ExtraOptionVariant extraOptionVariant = (ExtraOptionVariant) o;
        if (extraOptionVariant.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, extraOptionVariant.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ExtraOptionVariant{" +
            "id=" + id +
            ", variantName='" + variantName + "'" +
            ", price='" + price + "'" +
            '}';
    }
}
