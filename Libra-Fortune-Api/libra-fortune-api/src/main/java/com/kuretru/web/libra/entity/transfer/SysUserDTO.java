package com.kuretru.web.libra.entity.transfer;

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
@Schema(description = "用户-数据传输实体")
public class SysUserDTO extends BaseDTO {

    @NotNull
    @Size(max = 32)
    @Schema(description = "用户登录名称")
    private String username;

    @NotNull
    @Size(max = 64)
    @Schema(description = "用户密码")
    private String password;

    @Size(max = 8)
    @Schema(description = "盐")
    private String salt;

    @Size(max = 32)
    @Schema(description = "用户昵称")
    private String nickname;

}
