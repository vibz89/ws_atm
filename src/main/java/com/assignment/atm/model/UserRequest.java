package com.assignment.atm.model;

import lombok.Data;

@Data
public class UserRequest {

    private String account;
    private String pin;
    private Long amount;
}
