package com.kuretru.web.libra.entity.transfer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kuretru.microservices.web.entity.transfer.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

/**
 * @author 呉真(kuretru) <kuretru@gmail.com>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class LedgerTagDTO extends BaseDTO {

    @JsonIgnore
    private UUID userId;

    @NotEmpty
    @Size(max = 32)
    private String name;

}
