package com.kuretru.web.libra.dashboard.entity.enums.ledger;

import com.kuretru.microservices.dashboard.entity.interfaces.Join;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.kuretru.microservices.common.entity.enums.BaseEnum;
import lombok.Getter;

@Getter
public enum LedgerJoin implements BaseEnum<LedgerJoin>, Join {

    DETAIL("detail", "账本详情表", "LEFT JOIN ledger_v2_entry_detail detail ON entry.id = detail.entry_id"),
    TAG("tag", "账本标签关联标", "LEFT JOIN ledger_v2_entry_tag tag ON entry.id = tag.entry_id"),
    TAG_ITEM("tagItem", "标签项表", "LEFT JOIN metadata_tag_set_item tag_item ON tag.tag_id = tag_item.id");

    @EnumValue
    @JsonValue
    private final String value;
    private final String label;
    private final String sql;

    LedgerJoin(String value, String label, String sql) {
        this.value = value;
        this.label = label;
        this.sql = sql;
    }

}
