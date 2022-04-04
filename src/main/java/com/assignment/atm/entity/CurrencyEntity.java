package com.assignment.atm.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name="CASHBOX")
public class CurrencyEntity {

    @Id
    @GeneratedValue
    @Column(name = "id",updatable = false, nullable = false)
    private Long id;

    @Column(name = "fifty")
    private Long fifty;

    @Column(name = "twenty")
    private Long twenty;

    @Column(name = "ten")
    private Long ten;

    @Column(name = "five")
    private Long five;

    @Transient
    private Long total;

    @Transient
    public Long getTotal() {
        return (5 * this.five) +(10 * this.ten) + (20 * this.twenty) + (50 * this.fifty);
    }

    @Transient
    public Long getTotalPerCurrency(String denominator){
        Long sum =0l;
        switch(denominator){
            case "five" : sum = 5 * this.five;
                break;
            case "ten" : sum = 10 * this.ten;
                break;
            case "twenty" : sum = 20 * this.twenty;
                break;
            case "fifty" : sum = 50 * this.fifty;
                break;
        }
        return sum;
    }
}
