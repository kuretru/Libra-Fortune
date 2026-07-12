package com.kuretru.web.libra.dashboard.entity.enums.ledger;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.kuretru.microservices.common.entity.enums.BaseEnum;
import com.kuretru.web.libra.dashboard.entity.interfaces.Dimension;
import com.kuretru.web.libra.dashboard.entity.interfaces.Filter;
import com.kuretru.web.libra.dashboard.entity.interfaces.Join;
import lombok.Getter;

@Getter
public enum LedgerDimensions implements BaseEnum<LedgerDimensions>, Dimension, Filter<String> {

    LEDGER_ID("ledgerId", "账本ID", "entry.ledger_id AS ledger_id", "entry.ledger_id", null),
    CATEGORY_ID_L1("categoryIdL1", "一级分类", "entry.category_id_l1 AS category_id_l1", "entry.category_id_l1", null),
    CATEGORY_ID_L2("categoryIdL2", "二级分类", "entry.category_id_l2 AS category_id_l2", "entry.category_id_l2", null),
    TYPE("type", "条目类型", "entry.`type` AS type", "entry.`type`", null),
    USERNAME("username", "用户名", "detail.username AS username", "detail.username", LedgerJoin.DETAIL),
    TAG_ID("tagId", "标签ID", "tag.tag_id AS tag_id", "tag.tag_id", LedgerJoin.TAG);

    @EnumValue
    @JsonValue
    private final String value;
    private final String label;
    private final String select;
    private final String groupBy;
    private final Join join;

    LedgerDimensions(String value, String label, String select, String groupBy, Join join) {
        this.value = value;
        this.label = label;
        this.select = select;
        this.groupBy = groupBy;
        this.join = join;
    }

}
