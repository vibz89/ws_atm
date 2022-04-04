package com.assignment.atm.dao;

import com.assignment.atm.entity.CurrencyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface CurrencyRepository extends JpaRepository<CurrencyEntity, Long> {
}
