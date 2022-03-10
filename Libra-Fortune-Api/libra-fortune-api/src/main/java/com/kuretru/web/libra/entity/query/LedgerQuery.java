package com.kuretru.web.libra.entity.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Size;


@Data
public class SystemUserQuery {

    @Size(max = 16)
    private String username;

    @Size(max = 64)
    private String password;

    @Size(max = 16)
    private String nickname;
}