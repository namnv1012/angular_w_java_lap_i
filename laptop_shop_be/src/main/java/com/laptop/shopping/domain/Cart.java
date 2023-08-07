package com.laptop.shopping.domain;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "cart")
public class Cart implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_code")
    private String userCode;

    @Column(name = "product_code")
    private String productCode;

    @Column(name = "quantity")
    private Integer quantity;

    public Long getId() {
        return this.id;
    }

    public Cart id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserCode() {
        return this.userCode;
    }

    public Cart userCode(String userCode) {
        this.setUserCode(userCode);
        return this;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getProductCode() {
        return this.productCode;
    }

    public Cart productCode(String productId) {
        this.setProductCode(productId);
        return this;
    }

    public void setProductCode(String productId) {
        this.productCode = productId;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public Cart quantity(Integer quantity) {
        this.setQuantity(quantity);
        return this;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cart)) {
            return false;
        }
        return id != null && id.equals(((Cart) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Cart{" +
            "id=" + getId() +
            ", userCode='" + getUserCode() + "'" +
            ", productId='" + getProductCode() + "'" +
            ", quantity=" + getQuantity() +
            "}";
    }
}
