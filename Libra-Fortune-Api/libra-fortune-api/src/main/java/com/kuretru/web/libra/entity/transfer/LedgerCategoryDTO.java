package com.kuretru.web.libra.entity.transfer;

import com.baomidou.mybatisplus.annotation.TableName;
import com.kuretru.api.common.entity.data.BaseDO;
import com.kuretru.api.common.entity.transfer.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;


@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Schema(description = "账目的主分类-数据传输实体")
public class LedgerCategoryDTO extends BaseDTO {

    @NotNull
    @Schema(description = "所属用户ID")
    private String userId;

    @NotNull
    @Schema(description = "所属账本ID")
    private String ledgerId;

    @NotNull
    @Size(max = 16)
    @Schema(description = "主分类的名称")
    private String name;

}
