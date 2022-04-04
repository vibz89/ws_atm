package com.assignment.atm.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Data
@Table(name = "ACCDETAILS")
public class AccountInfoEntity {

    @Id
    @GeneratedValue
    @Column(name = "accdetid",updatable = false, nullable = false)
    private Long accdetid;

    @Column(name = "accountnum")
    private String accountnum;

    @Column(name = "pin")
    private String pin;

    @Column(name = "balance")
    private Long balance;

    @Column(name = "overdraft")
    private Long overdraft;

    @CreationTimestamp
    @Column(updatable = false)
    Timestamp datecreated;

}
