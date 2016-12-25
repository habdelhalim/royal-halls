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

/**
 * A Customer.
 */
@Entity
@Table(name = "customer")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "customer_name", nullable = false)
    private String customerName;

    @NotNull
    @Column(name = "identity_id", nullable = false)
    private String identityId;

    @Column(name = "groom_name")
    private String groomName;

    @Column(name = "pride_name")
    private String prideName;

    @Column(name = "city")
    private String city;

    @Column(name = "country")
    private String country;

    @OneToMany(mappedBy = "customer")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Contact> contacts = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public Customer customerName(String customerName) {
        this.customerName = customerName;
        return this;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getIdentityId() {
        return identityId;
    }

    public Customer identityId(String identityId) {
        this.identityId = identityId;
        return this;
    }

    public void setIdentityId(String identityId) {
        this.identityId = identityId;
    }

    public String getGroomName() {
        return groomName;
    }

    public Customer groomName(String groomName) {
        this.groomName = groomName;
        return this;
    }

    public void setGroomName(String groomName) {
        this.groomName = groomName;
    }

    public String getPrideName() {
        return prideName;
    }

    public Customer prideName(String prideName) {
        this.prideName = prideName;
        return this;
    }

    public void setPrideName(String prideName) {
        this.prideName = prideName;
    }

    public String getCity() {
        return city;
    }

    public Customer city(String city) {
        this.city = city;
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public Customer country(String country) {
        this.country = country;
        return this;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Set<Contact> getContacts() {
        return contacts;
    }

    public Customer contacts(Set<Contact> contacts) {
        this.contacts = contacts;
        return this;
    }

    public Customer addContact(Contact contact) {
        contacts.add(contact);
        contact.setCustomer(this);
        return this;
    }

    public Customer removeContact(Contact contact) {
        contacts.remove(contact);
        contact.setCustomer(null);
        return this;
    }

    public void setContacts(Set<Contact> contacts) {
        this.contacts = contacts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Customer customer = (Customer) o;
        if (customer.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, customer.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Customer{" +
            "id=" + id +
            ", customerName='" + customerName + "'" +
            ", identityId='" + identityId + "'" +
            ", groomName='" + groomName + "'" +
            ", prideName='" + prideName + "'" +
            ", city='" + city + "'" +
            ", country='" + country + "'" +
            '}';
    }
}
