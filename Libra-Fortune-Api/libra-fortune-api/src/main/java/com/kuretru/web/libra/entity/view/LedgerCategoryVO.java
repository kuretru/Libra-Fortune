package com.kuretru.web.libra.entity.view;

import com.kuretru.microservices.web.entity.transfer.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author 呉真(kuretru) <kuretru@gmail.com>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class LedgerCategoryVO extends BaseDTO {

    private String name;

}
