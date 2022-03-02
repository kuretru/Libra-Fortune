package com.kuretru.web.libra.entity.query;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;


@Data
@Schema(description = "账目主分类-查询条件")
public class LedgerCategoryQuery {

    @Schema(description = "所属用户ID")
    private String userId;

    @Schema(description = "所属账本ID")
    private String ledgerId;

    @Size(max = 16)
    @Schema(description = "主分类的名称")
    private String name;

}
