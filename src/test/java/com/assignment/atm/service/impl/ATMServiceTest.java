package com.assignment.atm.service.impl;

import com.assignment.atm.dao.AccountInfoRepository;
import com.assignment.atm.dao.CurrencyRepository;
import com.assignment.atm.entity.AccountInfoEntity;
import com.assignment.atm.entity.CurrencyEntity;
import com.assignment.atm.model.UserRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

class ATMServiceTest {
    @Mock
    AccountInfoRepository infoRepository;
    @Mock
    CurrencyRepository currencyRepository;
    @Mock
    Logger log;
    @InjectMocks
    ATMService aTMService;
    AccountInfoEntity accountInfo;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        accountInfo = new AccountInfoEntity();
        accountInfo.setBalance(800l);
        accountInfo.setAccountnum("123456789");
        accountInfo.setOverdraft(200l);
        accountInfo.setPin("1234");
    }

    @Test
    void testCheckBalance() {
        when(infoRepository.findByAccountnumAndPin("123456789", "1234")).thenReturn(accountInfo);
        String result = aTMService.checkBalance("123456789", "1234");
        Assertions.assertEquals("Current Balance :€800", result);
    }

    @Test
    void testCheckBalanceWithInvalidCredentials() {
        when(infoRepository.findByAccountnumAndPin("123456789", "1234")).thenReturn(accountInfo);
        String result = aTMService.checkBalance("123456789", "2103");
        Assertions.assertEquals("Account Id and Pin does not match!", result);
    }

    @Test
    void testWithdraw() {
        UserRequest mockReq = new UserRequest();
        mockReq.setPin("1234");
        mockReq.setAccount("1234456789");
        mockReq.setAmount(200l);
        List<CurrencyEntity> currencyEntityList = new ArrayList<>();
        CurrencyEntity currencyEntity = new CurrencyEntity();
        currencyEntity.setFive(20l);
        currencyEntity.setTen(30l);
        currencyEntity.setTwenty(30l);
        currencyEntity.setFifty(10l);
        currencyEntityList.add(currencyEntity);
        StringBuilder expResponse = new StringBuilder();
        expResponse.append("You have successfully withdrawn money!")
                .append("\n€50 : 4")
                .append("\nCurrent Balance : 600");
        when(infoRepository.findByAccountnumAndPin(anyString(), anyString())).thenReturn(accountInfo);
        when(currencyRepository.findAll()).thenReturn(currencyEntityList);

        String result = aTMService.withdraw(mockReq);
        Assertions.assertEquals(expResponse.toString(), result);
    }

    @Test
    void testWithdrawWithAllNotes() {
        UserRequest mockReq = new UserRequest();
        mockReq.setPin("1234");
        mockReq.setAccount("1234456789");
        mockReq.setAmount(700l);
        List<CurrencyEntity> currencyEntityList = new ArrayList<>();
        CurrencyEntity currencyEntity = new CurrencyEntity();
        currencyEntity.setFive(10l);
        currencyEntity.setTen(5l);
        currencyEntity.setTwenty(5l);
        currencyEntity.setFifty(10l);
        currencyEntityList.add(currencyEntity);
        StringBuilder expResponse = new StringBuilder();
        expResponse.append("You have successfully withdrawn money!")
                .append("\n€50 : 10")
                .append("\n€20 : 5")
                .append("\n€10 : 5")
                .append("\n€5 : 10")
                .append("\nCurrent Balance : 100");
        when(infoRepository.findByAccountnumAndPin(anyString(), anyString())).thenReturn(accountInfo);
        when(currencyRepository.findAll()).thenReturn(currencyEntityList);

        String result = aTMService.withdraw(mockReq);
        Assertions.assertEquals(expResponse.toString(), result);
    }

    @Test
    void testWithdrawWithWrongCredentials() {
        UserRequest mockReq = new UserRequest();
        mockReq.setPin("2145");
        mockReq.setAccount("1234456789");
        mockReq.setAmount(200l);
        when(infoRepository.findByAccountnumAndPin("123456789", "1234")).thenReturn(accountInfo);

        String result = aTMService.withdraw(mockReq);
        Assertions.assertEquals("Account Id and Pin does not match!", result);
    }

    @Test
    void testWithdrawWithInsufficientBalance() {
        UserRequest mockReq = new UserRequest();
        mockReq.setPin("1234");
        mockReq.setAccount("1234456789");
        mockReq.setAmount(1200l);
        StringBuilder expResponse = new StringBuilder();
        expResponse.append("Insufficient balance. Please try again with lesser amount")
                .append("\nCurrent Balance : €800");
        when(infoRepository.findByAccountnumAndPin(anyString(),anyString())).thenReturn(accountInfo);

        String result = aTMService.withdraw(mockReq);
        Assertions.assertEquals(expResponse.toString(), result);
    }

    @Test
    void testWithdrawWithInvalidAmount() {
        UserRequest mockReq = new UserRequest();
        mockReq.setPin("1234");
        mockReq.setAccount("1234456789");
        mockReq.setAmount(127l);
        when(infoRepository.findByAccountnumAndPin(anyString(),anyString())).thenReturn(accountInfo);

        String result = aTMService.withdraw(mockReq);
        Assertions.assertEquals("Please select withdrawal amount in multiples of 5 or 10", result);
    }

    @Test
    void testWithdrawInsufficientMoneyToWithdraw() {
        UserRequest mockReq = new UserRequest();
        mockReq.setPin("1234");
        mockReq.setAccount("1234456789");
        mockReq.setAmount(800l);
        List<CurrencyEntity> currencyEntityList = new ArrayList<>();
        CurrencyEntity currencyEntity = new CurrencyEntity();
        currencyEntity.setFive(10l);
        currencyEntity.setTen(10l);
        currencyEntity.setTwenty(10l);
        currencyEntity.setFifty(5l);
        currencyEntityList.add(currencyEntity);
        when(infoRepository.findByAccountnumAndPin(anyString(), anyString())).thenReturn(accountInfo);
        when(currencyRepository.findAll()).thenReturn(currencyEntityList);

        String result = aTMService.withdraw(mockReq);
        Assertions.assertEquals("Insufficient Money to withdraw. Please try again with lesser amount!", result);
    }

}