package com.assignment.atm.controller;

import com.assignment.atm.exception.ATMException;
import com.assignment.atm.exception.ErrorResponse;
import com.assignment.atm.model.UserRequest;
import com.assignment.atm.service.IATMService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.*;

class ATMControllerTest {
    @Mock
    IATMService atmService;
    @Mock
    Logger log;
    @InjectMocks
    ATMController aTMController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testCheckBalance() {
        when(atmService.checkBalance(anyString(), anyString())).thenReturn("Current Balance :€800");
        ResponseEntity<String> result = aTMController.checkBalance("123456789", "1234");
        Assertions.assertEquals("Current Balance :€800", result.getBody());
        Assertions.assertEquals(200, result.getStatusCodeValue());
    }

    @Test
    void testWithdraw() {
        StringBuilder response = new StringBuilder();
        response.append("You have successfully withdrawn money!")
                .append("\n€50 : 4")
                .append("\nCurrent Balance : 200");
        UserRequest mockReq = new UserRequest();
        mockReq.setPin("1234");
        mockReq.setAccount("1234456789");
        mockReq.setAmount(200l);
        when(atmService.withdraw(any())).thenReturn(response.toString());
        ResponseEntity<String> result = aTMController.withdraw(mockReq);
        Assertions.assertEquals(200, result.getStatusCodeValue());
        Assertions.assertEquals(response.toString(), result.getBody());
    }


}
