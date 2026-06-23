package com.kuretru.web.libra.ledger.entity.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.kuretru.microservices.common.entity.enums.BaseEnum;
import lombok.Getter;

@Getter
public enum DetailLockType implements BaseEnum<DetailLockType> {

    UNLOCK("unlock", "未锁定"),
    LOCK_RATIO("lock_ratio", "锁比例"),
    LOCK_AMOUNT("local_amount", "锁金额");

    @EnumValue
    @JsonValue
    private final String value;
    private final String label;

    DetailLockType(String value, String label) {
        this.value = value;
        this.label = label;
    }
}
