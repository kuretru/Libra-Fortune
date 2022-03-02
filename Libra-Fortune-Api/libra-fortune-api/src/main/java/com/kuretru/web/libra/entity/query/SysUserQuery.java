package com.kuretru.web.libra.entity.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Size;


@Data
@Schema(description = "用户-查询条件")
public class SysUserQuery {

    @Size(max = 32)
    @Schema(description = "所属用户ID")
    private String username;

    @Size(max = 64)
    @Schema(description = "用户密码")
    private String password;

    @Size(max = 16)
    @Schema(description = "用户昵称")
    private String nickname;
}
