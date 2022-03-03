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
public class SysUserDTO extends BaseDTO {

    @NotNull
    @Size(max = 32)
    private String username;

    @NotNull
    @Size(max = 64)
    private String password;

    @Size(max = 8)
    private String salt;

    @Size(max = 32)
    private String nickname;

}
