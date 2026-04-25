package com.kuretru.web.libra.entity.query;

import lombok.Data;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;

/**
 * @author 呉真(kuretru) <kuretru@gmail.com>
 */
@Data
public class PaymentChannelQuery {

    @NotNull
    private UUID userId;

    @Size(max = 16)
    private String name;

}
