package com.kuretru.web.libra.metadata.entity.data;

import com.baomidou.mybatisplus.annotation.TableName;
import com.kuretru.microservices.web.entity.data.BaseCreateUpdateDO;
import com.kuretru.microservices.web.entity.interfaces.Sequenced;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@TableName("metadata_currency")
public class MetadataCurrencyDO extends BaseCreateUpdateDO implements Sequenced {

    /** 货币代码 */
    private String code;

    /** 货币符号 */
    private String symbol;

    /** 货币名称 */
    private String name;

    /** 排序标识 */
    private Integer sequence;

}
