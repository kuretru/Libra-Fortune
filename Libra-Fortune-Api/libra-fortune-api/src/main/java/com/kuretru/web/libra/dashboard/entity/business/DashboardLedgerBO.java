package com.kuretru.web.libra.dashboard.entity.business;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class DashboardLedgerBO {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String year;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String month;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String day;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long ledgerId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long categoryIdL1;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long categoryIdL2;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String type;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String username;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long tagId;

    private BigDecimal sum;

}
