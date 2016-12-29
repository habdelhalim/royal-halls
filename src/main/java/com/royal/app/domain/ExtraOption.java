package com.royal.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.royal.app.domain.enumeration.OptionType;

/**
 * A ExtraOption.
 */
@Entity
@Table(name = "extra_option")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ExtraOption implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "option_name", nullable = false)
    private String optionName;

    @Enumerated(EnumType.STRING)
    @Column(name = "option_type")
    private OptionType optionType;

    @Column(name = "price")
    private Double price;

    @Column(name = "has_variances")
    private Boolean hasVariances;

    @Column(name = "has_colors")
    private Boolean hasColors;

    @OneToMany(mappedBy = "option")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ExtraOptionVariant> variants = new HashSet<>();

    @OneToMany(mappedBy = "option")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ExtraOptionColor> colors = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOptionName() {
        return optionName;
    }

    public ExtraOption optionName(String optionName) {
        this.optionName = optionName;
        return this;
    }

    public void setOptionName(String optionName) {
        this.optionName = optionName;
    }

    public OptionType getOptionType() {
        return optionType;
    }

    public ExtraOption optionType(OptionType optionType) {
        this.optionType = optionType;
        return this;
    }

    public void setOptionType(OptionType optionType) {
        this.optionType = optionType;
    }

    public Double getPrice() {
        return price;
    }

    public ExtraOption price(Double price) {
        this.price = price;
        return this;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Boolean isHasVariances() {
        return hasVariances;
    }

    public ExtraOption hasVariances(Boolean hasVariances) {
        this.hasVariances = hasVariances;
        return this;
    }

    public void setHasVariances(Boolean hasVariances) {
        this.hasVariances = hasVariances;
    }

    public Boolean isHasColors() {
        return hasColors;
    }

    public ExtraOption hasColors(Boolean hasColors) {
        this.hasColors = hasColors;
        return this;
    }

    public void setHasColors(Boolean hasColors) {
        this.hasColors = hasColors;
    }

    public Set<ExtraOptionVariant> getVariants() {
        return variants;
    }

    public ExtraOption variants(Set<ExtraOptionVariant> extraOptionVariants) {
        this.variants = extraOptionVariants;
        return this;
    }

    public ExtraOption addVariant(ExtraOptionVariant extraOptionVariant) {
        variants.add(extraOptionVariant);
        extraOptionVariant.setOption(this);
        return this;
    }

    public ExtraOption removeVariant(ExtraOptionVariant extraOptionVariant) {
        variants.remove(extraOptionVariant);
        extraOptionVariant.setOption(null);
        return this;
    }

    public void setVariants(Set<ExtraOptionVariant> extraOptionVariants) {
        this.variants = extraOptionVariants;
    }

    public Set<ExtraOptionColor> getColors() {
        return colors;
    }

    public ExtraOption colors(Set<ExtraOptionColor> extraOptionColors) {
        this.colors = extraOptionColors;
        return this;
    }

    public ExtraOption addColor(ExtraOptionColor extraOptionColor) {
        colors.add(extraOptionColor);
        extraOptionColor.setOption(this);
        return this;
    }

    public ExtraOption removeColor(ExtraOptionColor extraOptionColor) {
        colors.remove(extraOptionColor);
        extraOptionColor.setOption(null);
        return this;
    }

    public void setColors(Set<ExtraOptionColor> extraOptionColors) {
        this.colors = extraOptionColors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ExtraOption extraOption = (ExtraOption) o;
        if (extraOption.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, extraOption.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ExtraOption{" +
            "id=" + id +
            ", optionName='" + optionName + "'" +
            ", optionType='" + optionType + "'" +
            ", price='" + price + "'" +
            ", hasVariances='" + hasVariances + "'" +
            ", hasColors='" + hasColors + "'" +
            '}';
    }
}
