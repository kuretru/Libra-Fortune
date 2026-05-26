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
@TableName("metadata_category")
public class MetadataCategoryDO extends BaseDO implements Sequenced {

    /** 父分类ID */
    private Long parentId;

    /** 分类名称 */
    private String name;

    /** 图标 */
    private String icon;

    /** 排序标识 */
    private Integer sequence;

}
