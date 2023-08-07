package com.laptop.shopping.domain;

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;

@Entity
@Table(name = "warranty_details")
public class WarrantyDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "warranty_id")
    private Long warrantyId;

    @Column(name = "product_code")
    private String productCode;

    @Column(name = "note")
    private String note;

    @Column(name = "received_date")
    private Instant receivedDate;

    @Column(name = "pay_date")
    private Instant payDate;


    public Long getId() {
        return this.id;
    }

    public WarrantyDetails id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getWarrantyId() {
        return this.warrantyId;
    }

    public WarrantyDetails warrantyId(Long warrantyId) {
        this.setWarrantyId(warrantyId);
        return this;
    }

    public void setWarrantyId(Long warrantyId) {
        this.warrantyId = warrantyId;
    }

    public String getProductCode() {
        return this.productCode;
    }

    public WarrantyDetails productCode(String productCode) {
        this.setProductCode(productCode);
        return this;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getNote() {
        return this.note;
    }

    public WarrantyDetails note(String note) {
        this.setNote(note);
        return this;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Instant getReceivedDate() {
        return this.receivedDate;
    }

    public WarrantyDetails receivedDate(Instant receivedDate) {
        this.setReceivedDate(receivedDate);
        return this;
    }

    public void setReceivedDate(Instant receivedDate) {
        this.receivedDate = receivedDate;
    }

    public Instant getPayDate() {
        return this.payDate;
    }

    public WarrantyDetails payDate(Instant payDate) {
        this.setPayDate(payDate);
        return this;
    }

    public void setPayDate(Instant payDate) {
        this.payDate = payDate;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WarrantyDetails)) {
            return false;
        }
        return id != null && id.equals(((WarrantyDetails) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "WarrantyDetails{" +
            "id=" + getId() +
            ", warrantyId=" + getWarrantyId() +
            ", productCode='" + getProductCode() + "'" +
            ", note='" + getNote() + "'" +
            ", receivedDate='" + getReceivedDate() + "'" +
            ", payDate='" + getPayDate() + "'" +
            "}";
    }
}
