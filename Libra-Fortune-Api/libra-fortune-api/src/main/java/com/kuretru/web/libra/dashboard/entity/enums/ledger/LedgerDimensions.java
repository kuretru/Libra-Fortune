package com.kuretru.web.libra.dashboard.entity.enums.ledger;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.kuretru.microservices.common.entity.enums.BaseEnum;
import com.kuretru.web.libra.dashboard.entity.interfaces.Field;
import com.kuretru.web.libra.dashboard.entity.interfaces.Filter;
import com.kuretru.web.libra.dashboard.entity.interfaces.Join;
import lombok.Getter;

@Getter
public enum LedgerDimensions implements BaseEnum<LedgerDimensions>, Field, Filter<String> {

    LEDGER_ID("ledgerId", "账本ID", "entry.ledger_id", null),
    CATEGORY_ID_L1("categoryIdL1", "一级分类", "entry.category_id_l1", null),
    CATEGORY_ID_L2("categoryIdL2", "二级分类", "entry.category_id_l2", null),
    TYPE("type", "条目类型", "entry.`type`", null),
    USERNAME("username", "用户名", "detail.username", LedgerJoin.DETAIL),
    TAG_ID("tagId", "标签ID", "tag.tag_id", LedgerJoin.TAG);

    @EnumValue
    @JsonValue
    private final String value;
    private final String label;
    private final String column;
    private final Join join;

    LedgerDimensions(String value, String label, String column, Join join) {
        this.value = value;
        this.label = label;
        this.column = column;
        this.join = join;
    }

}
