package com.laptop.shopping.domain;

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;

@Entity
@Table(name = "warranty")
public class Warranty implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "create_date")
    private Instant createDate;

    @Column(name = "create_name")
    private String createName;

    @Column(name = "expiration_date")
    private Instant expirationDate;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Warranty id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return this.orderId;
    }

    public Warranty orderId(Long orderId) {
        this.setOrderId(orderId);
        return this;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getCustomerName() {
        return this.customerName;
    }

    public Warranty customerName(String customerName) {
        this.setCustomerName(customerName);
        return this;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public Warranty phoneNumber(String phoneNumber) {
        this.setPhoneNumber(phoneNumber);
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Instant getCreateDate() {
        return this.createDate;
    }

    public Warranty createDate(Instant createDate) {
        this.setCreateDate(createDate);
        return this;
    }

    public void setCreateDate(Instant createDate) {
        this.createDate = createDate;
    }

    public String getCreateName() {
        return this.createName;
    }

    public Warranty createName(String createName) {
        this.setCreateName(createName);
        return this;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public Instant getExpirationDate() {
        return this.expirationDate;
    }

    public Warranty expirationDate(Instant expirationDate) {
        this.setExpirationDate(expirationDate);
        return this;
    }

    public void setExpirationDate(Instant expirationDate) {
        this.expirationDate = expirationDate;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Warranty)) {
            return false;
        }
        return id != null && id.equals(((Warranty) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Warranty{" + "id=" + getId() + ", orderId=" + getOrderId() + ", customerName='" + getCustomerName() + "'" + ", phoneNumber='" + getPhoneNumber() + "'" + ", createDate='" + getCreateDate() + "'" + ", createName='" + getCreateName() + "'" + ", expirationDate='" + getExpirationDate() + "'" + "}";
    }
}
