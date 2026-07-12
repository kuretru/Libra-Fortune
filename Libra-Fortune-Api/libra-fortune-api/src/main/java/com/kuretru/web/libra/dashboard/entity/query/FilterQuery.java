package com.kuretru.web.libra.dashboard.entity.query;

import com.kuretru.web.libra.dashboard.entity.interfaces.Filter;
import lombok.Data;

@Data
public class FilterQuery<T extends Filter<V>, V> {

    private T name;
    private String operator;
    private V value;

}
