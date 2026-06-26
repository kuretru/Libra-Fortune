package com.kuretru.web.libra.ledger.entity.business;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DashboardBO {

    private String year;

    private String month;

    private String day;

    private Long ledgerId;

    private Long categoryIdL1;

    private Long categoryIdL2;

    private String type;

    private String username;

    private Long tagId;

    private BigDecimal sum;

}
