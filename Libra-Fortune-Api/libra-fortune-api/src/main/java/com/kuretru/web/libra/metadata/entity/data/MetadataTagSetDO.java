package com.kuretru.web.libra.metadata.entity.data;

import com.baomidou.mybatisplus.annotation.TableName;
import com.kuretru.microservices.web.entity.interfaces.Sequenced;
import com.kuretru.microservices.web.v2.entity.data.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@TableName("metadata_tag_set")
public class MetadataTagSetDO extends BaseDO implements Sequenced {

    /** 标签组名称 */
    private String name;

    /** 该标签组是否必选 */
    private Boolean required;

    /** 是否允许多选 */
    private Boolean allowMultiple;

    /** 排序标识 */
    private Integer sequence;

}
