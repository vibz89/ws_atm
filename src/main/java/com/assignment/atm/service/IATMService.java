package com.assignment.atm.service;

import com.assignment.atm.model.UserRequest;


public interface IATMService {
    String checkBalance(String account, String pin);

    String withdraw(UserRequest userReq);
}
