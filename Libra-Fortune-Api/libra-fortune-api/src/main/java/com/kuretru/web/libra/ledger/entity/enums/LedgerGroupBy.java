package com.kuretru.web.libra.ledger.entity.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.kuretru.microservices.common.entity.enums.BaseEnum;
import lombok.Getter;

@Getter
public enum LedgerGroupBy implements BaseEnum<LedgerGroupBy> {

    YEAR("year", "年", "YEAR(entry.`date`) AS year", "YEAR(entry.`date`)"),
    MONTH("month", "月", "MONTH(entry.`date`) AS month", "MONTH(entry.`date`)"),
    DAY("day", "日", "entry.`date` AS day", "entry.`date`"),
    LEDGER_ID("ledgerId", "账本ID", "entry.ledger_id AS ledger_id", "entry.ledger_id"),
    CATEGORY_ID_L1("categoryIdL1", "一级分类ID", "entry.category_id_l1 AS category_id_l1", "entry.category_id_l1"),
    CATEGORY_ID_L2("categoryIdL2", "二级分类ID", "entry.category_id_l2 AS category_id_l2", "entry.category_id_l2"),
    TYPE("type", "条目类型", "entry.`type` AS type", "entry.`type`"),
    USERNAME("username", "用户名", "detail.username AS username", "detail.username"),
    TAG_ID("tagId", "标签ID", "tag.tag_id AS tag_id", "tag.tag_id");

    @EnumValue
    @JsonValue
    private final String value;
    private final String label;
    private final String select;
    private final String groupBy;

    LedgerGroupBy(String value, String label, String select, String groupBy) {
        this.value = value;
        this.label = label;
        this.select = select;
        this.groupBy = groupBy;
    }

}
