package com.kuretru.web.libra.dashboard.entity.enums.ledger;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.kuretru.microservices.common.entity.enums.BaseEnum;
import com.kuretru.web.libra.dashboard.entity.interfaces.Field;
import com.kuretru.web.libra.dashboard.entity.interfaces.Join;
import lombok.Getter;

import java.util.List;

@Getter
public enum LedgerDimensions implements BaseEnum<LedgerDimensions>, Field {

    LEDGER_ID("ledgerId", "账本ID", "entry.ledger_id", List.of()),
    CATEGORY_ID_L1("categoryIdL1", "一级分类", "entry.category_id_l1", List.of()),
    CATEGORY_ID_L2("categoryIdL2", "二级分类", "entry.category_id_l2", List.of()),
    TYPE("type", "条目类型", "entry.`type`", List.of()),
    USERNAME("username", "用户名", "detail.username", List.of(LedgerJoin.DETAIL)),
    TAG_SET_ID("tagSetId", "标签组ID", "tag_item.set_id", List.of(LedgerJoin.TAG, LedgerJoin.TAG_ITEM)),
    TAG_ITEM_ID("tagItemId", "标签项ID", "tag.tag_id", List.of(LedgerJoin.TAG));

    @EnumValue
    @JsonValue
    private final String value;
    private final String label;
    private final String column;
    private final List<Join> joins;

    LedgerDimensions(String value, String label, String column, List<Join> joins) {
        this.value = value;
        this.label = label;
        this.column = column;
        this.joins = joins;
    }

}
