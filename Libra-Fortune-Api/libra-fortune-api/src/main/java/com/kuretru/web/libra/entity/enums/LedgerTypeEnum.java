package com.kuretru.web.libra.entity.enums;

import com.kuretru.microservices.common.entity.enums.BaseEnum;
import lombok.Getter;

@Getter
public enum LedgerTypeEnum implements BaseEnum<LedgerTypeEnum> {

    /** 个人账本 */
    COMMON((short)0, "个人账本"),
    /** 合作账本 */
    CO_COMMON((short)1, "合作账本"),
    /** 理财账本 */
    FINANCIAL((short)2, "理财账本"),
    /** 合作理财账本 */
    CO_FINANCIAL((short)3, "合作理财账本");

    /** 枚举编号 */
    private final short code;

    /** 枚举内容 */
    private final String value;

    LedgerTypeEnum(short code, String value) {
        this.code = code;
        this.value = value;
    }

}
