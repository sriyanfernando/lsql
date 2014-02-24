package com.w11k.lsql;

import java.util.List;

public class QueriedRow extends Row {

    private final List<ResultSetColumn> resultSetColumns;

    public QueriedRow(List<ResultSetColumn> resultSetColumns) {
        this.resultSetColumns = resultSetColumns;
    }

    public List<ResultSetColumn> getResultSetColumns() {
        return resultSetColumns;
    }

    public List<QueriedRow> getJoined(String key) {
        //noinspection unchecked
        return (List<QueriedRow>) get(key);
    }

}
