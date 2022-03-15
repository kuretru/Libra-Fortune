package com.kuretru.web.libra.entity.enums;

import com.kuretru.api.common.entity.enums.BaseEnum;
import lombok.Getter;

@Getter
public enum LedgerTypeEnum implements BaseEnum<LedgerTypeEnum> {

    /** 普通账本 */
    COMMON((short)0, "普通"),
    /** 普通合作账本 */
    CO_COMMON((short)1, "普通合作"),
    /** 理财账本 */
    FINANCIAL((short)2, "理财"),
    /** 合作理财账本 */
    CO_FINANCIAL((short)3, "合作理财");

    /** 枚举编号 */
    private final short code;

    /** 枚举内容 */
    private final String value;

    LedgerTypeEnum(short code, String value) {
        this.code = code;
        this.value = value;
    }

}
