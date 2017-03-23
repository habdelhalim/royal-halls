package com.royal.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.search.annotations.Field;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

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

    @Field
    @NotNull
    @Column(name = "customer_name", nullable = false)
    private String customerName;

    @Field
    @Column(name = "identity_id")
    private String identityId;

    @Column(name = "city")
    private String city;

    @Column(name = "country")
    private String country;

    @Field
    @Column(name = "mobile")
    private String mobile;

    @Field
    @Column(name = "telephone")
    private String telephone;

    @Column(name = "nationality")
    private String nationality;

    @OneToOne
    @JoinColumn(name = "customer_status_id")
    private CustomerStatus customerStatus;

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

    public String getMobile() {
        return mobile;
    }

    public Customer mobile(String mobile) {
        this.mobile = mobile;
        return this;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getTelephone() {
        return telephone;
    }

    public Customer telephone(String telephone) {
        this.telephone = telephone;
        return this;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getNationality() {
        return nationality;
    }

    public Customer nationality(String nationality) {
        this.nationality = nationality;
        return this;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public CustomerStatus getCustomerStatus() {
        return customerStatus;
    }

    public void setCustomerStatus(CustomerStatus customerStatus) {
        this.customerStatus = customerStatus;
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
            ", city='" + city + "'" +
            ", country='" + country + "'" +
            ", mobile='" + mobile + "'" +
            ", telephone='" + telephone + "'" +
            ", nationality='" + nationality + "'" +
            '}';
    }
}
