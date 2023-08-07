package com.laptop.shopping.domain;

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;

@Entity
@Table(name = "image")
public class Image implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "link_code")
    private String linkCode;

    @Column(name = "path")
    private String path;

    @Column(name = "create_date")
    private Instant createDate;

    @Column(name = "create_name")
    private String createName;

    public Long getId() {
        return this.id;
    }

    public Image id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLinkCode() {
        return this.linkCode;
    }

    public Image linkCode(String linkCode) {
        this.setLinkCode(linkCode);
        return this;
    }

    public void setLinkCode(String linkCode) {
        this.linkCode = linkCode;
    }

    public String getPath() {
        return this.path;
    }

    public Image path(String path) {
        this.setPath(path);
        return this;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Instant getCreateDate() {
        return this.createDate;
    }

    public Image createDate(Instant createDate) {
        this.setCreateDate(createDate);
        return this;
    }

    public void setCreateDate(Instant createDate) {
        this.createDate = createDate;
    }

    public String getCreateName() {
        return this.createName;
    }

    public Image createName(String createName) {
        this.setCreateName(createName);
        return this;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Image)) {
            return false;
        }
        return id != null && id.equals(((Image) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Image{" +
            "id=" + getId() +
            ", linkCode='" + getLinkCode() + "'" +
            ", path='" + getPath() + "'" +
            ", createDate='" + getCreateDate() + "'" +
            ", createName='" + getCreateName() + "'" +
            "}";
    }
}
