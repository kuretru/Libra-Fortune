package com.kuretru.web.libra.dashboard.entity.interfaces;

import java.util.List;

public interface Field {

    String getValue();

    String getColumn();

    List<Join> getJoins();

}
