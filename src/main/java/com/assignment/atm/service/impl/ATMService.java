package com.assignment.atm.service.impl;

import com.assignment.atm.dao.AccountInfoRepository;
import com.assignment.atm.dao.CurrencyRepository;
import com.assignment.atm.entity.AccountInfoEntity;
import com.assignment.atm.entity.CurrencyEntity;
import com.assignment.atm.exception.ATMException;
import com.assignment.atm.model.UserRequest;
import com.assignment.atm.service.IATMService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Service
@Slf4j
public class ATMService implements IATMService {

    @Autowired
    private AccountInfoRepository infoRepository;

    @Autowired
    private CurrencyRepository currencyRepository;

    private final String FIFTY = "fifty";
    private final String TWENTY = "twenty";
    private final String TEN = "ten";
    private final String FIVE = "five";

    @Override
    public String checkBalance(String account, String pin) {
        log.info("Initiating balance check");
        StringBuilder strResponse = new StringBuilder();
        try {
            AccountInfoEntity accountInfoEntity = infoRepository.findByAccountnumAndPin(account, pin);

            if (null != accountInfoEntity) {
                strResponse.append("Current Balance :")
                        .append("€")
                        .append(accountInfoEntity.getBalance());

            } else {
                strResponse.append("Account Id and Pin does not match!");
            }
        } catch (Exception ae) {
            log.error("Error while checking balance", ae);
            throw new ATMException("System error.Please check after sometime!!", ae);
        }
        return strResponse.toString();
    }

    @Override
    public String withdraw(UserRequest userReq) {
        log.info("Initiating withdraw process!!");
        StringBuilder strResponse = new StringBuilder();
        try {
            if (userReq.getAmount() % 5 != 0) {
                strResponse.append("Please select withdrawal amount in multiples of 5 or 10");
            } else {
                AccountInfoEntity accountInfoEntity = infoRepository.findByAccountnumAndPin(userReq.getAccount(), userReq.getPin());

                if (null != accountInfoEntity) {
                    if (userReq.getAmount() > accountInfoEntity.getBalance()) {
                        strResponse.append("Insufficient balance. Please try again with lesser amount")
                                .append("\n")
                                .append("Current Balance : ")
                                .append("€")
                                .append(accountInfoEntity.getBalance());
                    } else {
                        strResponse = checkAndWithdraw(accountInfoEntity, userReq.getAmount());
                    }

                } else {
                    strResponse.append("Account Id and Pin does not match!");
                }
            }
        } catch (Exception ae) {
            log.error("Error during withdrawal process", ae);
            throw new ATMException("System error.Please check after sometime!!", ae);
        }
        return strResponse.toString();
    }

    private StringBuilder checkAndWithdraw(AccountInfoEntity accountInfoEntity, Long amount) {
        StringBuilder response = new StringBuilder();
        Long currentBal = accountInfoEntity.getBalance();
        Map<String, Integer> noteCounter = new HashMap<>();
        CurrencyEntity currencyEntity = currencyRepository.findAll().get(0);
        if (null != currencyEntity) {
            if (currencyEntity.getTotal() < amount) {
                response.append("Insufficient Money to withdraw. Please try again with lesser amount!");
            } else {
                accountInfoEntity.setBalance(currentBal - amount);
                calculateNoOfNotes(currencyEntity, amount, noteCounter);
                currencyRepository.save(currencyEntity);
                infoRepository.save(accountInfoEntity);
                updateNoteDetails(response, noteCounter);
                response.append("\n")
                        .append("Current Balance : ")
                        .append(accountInfoEntity.getBalance());
            }
        } else {
            response.append("No money available. Sorry for the inconvenience!");
        }


        return response;
    }

    private void updateNoteDetails(StringBuilder response, Map<String, Integer> noteCounter) {
        response.append("You have successfully withdrawn money!");
        if (null != noteCounter.get(FIFTY)) {
            response.append("\n€50 : ")
                    .append(noteCounter.get(FIFTY));
        }
        if (null != noteCounter.get(TWENTY)) {
            response.append("\n€20 : ")
                    .append(noteCounter.get(TWENTY));
        }
        if (null != noteCounter.get(TEN)) {
            response.append("\n€10 : ")
                    .append(noteCounter.get(TEN));
        }
        if (null != noteCounter.get(FIVE)) {
            response.append("\n€5 : ")
                    .append(noteCounter.get(FIVE));
        }
    }

    private void calculateNoOfNotes(CurrencyEntity currencyEntity, Long amount, Map<String, Integer> noteCounter) {
        if (amount > 0) {
            if (amount >= 50 && currencyEntity.getTotalPerCurrency(FIFTY) > 0) {
                amount = amount - 50;
                currencyEntity.setFifty(currencyEntity.getFifty() - 1);
                if (noteCounter.get(FIFTY) == null) {
                    noteCounter.put(FIFTY, 1);
                } else {
                    noteCounter.put(FIFTY, noteCounter.get(FIFTY) + 1);
                }
                calculateNoOfNotes(currencyEntity, amount, noteCounter);
            } else if (amount >= 20 && currencyEntity.getTotalPerCurrency(TWENTY) > 0) {
                amount = amount - 20;
                currencyEntity.setTwenty(currencyEntity.getTwenty() - 1);
                if (noteCounter.get(TWENTY) == null) {
                    noteCounter.put(TWENTY, 1);
                } else {
                    noteCounter.put(TWENTY, noteCounter.get(TWENTY) + 1);
                }
                calculateNoOfNotes(currencyEntity, amount, noteCounter);
            } else if (amount >= 10 && currencyEntity.getTotalPerCurrency(TEN) > 0) {
                amount = amount - 10;
                currencyEntity.setTen(currencyEntity.getTen() - 1);
                if (noteCounter.get(TEN) == null) {
                    noteCounter.put(TEN, 1);
                } else {
                    noteCounter.put(TEN, noteCounter.get(TEN) + 1);
                }
                calculateNoOfNotes(currencyEntity, amount, noteCounter);
            } else if (amount >= 5 && currencyEntity.getTotalPerCurrency(FIVE) > 0) {
                amount = amount - 5;
                currencyEntity.setFive(currencyEntity.getFive() - 1);
                if (noteCounter.get(FIVE) == null) {
                    noteCounter.put(FIVE, 1);
                } else {
                    noteCounter.put(FIVE, noteCounter.get(FIVE) + 1);
                }
                calculateNoOfNotes(currencyEntity, amount, noteCounter);
            }
        }

    }
}
