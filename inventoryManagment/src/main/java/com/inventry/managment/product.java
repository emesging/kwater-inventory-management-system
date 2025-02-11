package com.inventry.managment;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Column;

import java.util.Date;

@Entity
@Table(name = "product")
public class product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int pid;

    private String pname;
    private Integer price;
    private Integer pquantity;
    private String plocationField;
    private String pdetailLocationField;
    

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", nullable = false)
    private Date updatedAt;

    public product() {}

    public product(String pname, Integer price, Integer pquantity, String plocationField, String pdetailLocationField ) {
        this.pname = pname;
        this.price = price;
        this.pquantity = pquantity;
        this.plocationField = plocationField;
        this.pdetailLocationField = pdetailLocationField;
        
    }

    @PrePersist
    @PreUpdate
    private void updateTimestamp() {
        updatedAt = new Date();  // 객체 저장 또는 업데이트 시 현재 날짜/시간으로 설정
    }

    // Getter 및 Setter
    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }
 
    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getPquantity() {
        return pquantity;
    }

    public void setPquantity(Integer pquantity) {
        this.pquantity = pquantity;
    }
    
    public String getPlocationField() {
        return plocationField;
    }

    public void setPlocationField(String plocationField) {
        this.plocationField = plocationField;
    }
    
    public String getPdetaillocationField() {
        return pdetailLocationField;
    }

    public void setPdetailLocationField(String pdetailLocationField) {
        this.pdetailLocationField = pdetailLocationField;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
