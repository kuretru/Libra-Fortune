package com.kuretru.web.libra.entity.business;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * @author 呉真(kuretru) <kuretru@gmail.com>
 */
@Data
@TableName(resultMap = "LedgerEntryBO")
public class LedgerEntryBO {

    private Long id;

    private String uuid;

    private String categoryId;

    private LocalDate date;

    private String name;

    private Long total;

    private String currencyType;

    private String remark;

    private List<LedgerEntryDetailBO> details;

}
