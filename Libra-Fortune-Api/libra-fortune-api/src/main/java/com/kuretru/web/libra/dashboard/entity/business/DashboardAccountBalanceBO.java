package com.kuretru.web.libra.dashboard.entity.business;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class DashboardAccountBalanceBO {

    private LocalDate date;

    private BigDecimal totalBalance;

}
