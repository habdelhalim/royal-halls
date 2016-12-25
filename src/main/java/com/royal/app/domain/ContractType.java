package com.royal.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A ContractType.
 */
@Entity
@Table(name = "contract_type")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ContractType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "contract_type_name")
    private String contractTypeName;

    @Column(name = "description")
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContractTypeName() {
        return contractTypeName;
    }

    public ContractType contractTypeName(String contractTypeName) {
        this.contractTypeName = contractTypeName;
        return this;
    }

    public void setContractTypeName(String contractTypeName) {
        this.contractTypeName = contractTypeName;
    }

    public String getDescription() {
        return description;
    }

    public ContractType description(String description) {
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
        ContractType contractType = (ContractType) o;
        if (contractType.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, contractType.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ContractType{" +
            "id=" + id +
            ", contractTypeName='" + contractTypeName + "'" +
            ", description='" + description + "'" +
            '}';
    }
}
