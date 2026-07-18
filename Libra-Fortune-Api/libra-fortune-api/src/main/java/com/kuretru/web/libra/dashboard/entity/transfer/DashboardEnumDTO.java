package com.kuretru.web.libra.dashboard.entity.transfer;

import com.kuretru.microservices.common.entity.enums.EnumDTO;
import lombok.Data;

import java.util.List;

@Data
public class DashboardEnumDTO {

    private List<EnumDTO<String>> timeDimensions;

    private List<EnumDTO<String>> metrics;

    private List<EnumDTO<String>> dimensions;

    private List<EnumDTO<String>> filterLogics;

    private List<EnumDTO<String>> filterOperators;

    private List<EnumDTO<String>> orderByTypes;

    private List<EnumDTO<String>> orderByModes;

}
