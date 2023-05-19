package com.kuretru.web.libra.entity.query;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.UUID;

/**
 * @author 呉真(kuretru) <kuretru@gmail.com>
 */
@Data
public class LedgerEntryQuery {

    private UUID ledgerId;

    private UUID categoryId;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date;

    private String name;

    private Long total;

    private String currencyType;

}
