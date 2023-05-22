package com.kuretru.web.libra.entity.view;

import com.kuretru.microservices.web.entity.transfer.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

/**
 * @author 呉真(kuretru) <kuretru@gmail.com>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class LedgerEntryVO extends BaseDTO {

    private LocalDate date;

    private String name;

    private Long total;

    private String currencyType;

    private String remark;

    private LedgerCategoryVO category;

    private List<LedgerEntryDetailVO> details;

}
