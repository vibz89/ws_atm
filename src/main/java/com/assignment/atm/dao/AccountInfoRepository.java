package com.assignment.atm.dao;

import com.assignment.atm.entity.AccountInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface AccountInfoRepository extends JpaRepository<AccountInfoEntity, Long> {

    AccountInfoEntity findByAccountnumAndPin(String account, String pin);
}
