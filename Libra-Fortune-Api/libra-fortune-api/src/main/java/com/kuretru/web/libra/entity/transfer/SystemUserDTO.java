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
public class SystemUserDTO extends BaseDTO {

    @NotNull
    @Size(max = 16)
    private String username;

    @NotNull
    @Size(max = 64)
    private String password;

    @NotNull
    @Size(max = 64)
    private String salt;

    @NotNull
    @Size(max = 16)
    private String nickname;

    @NotNull
    private String avatar;

}