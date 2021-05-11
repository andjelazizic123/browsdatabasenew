package com.example.browsdatabase.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ColumnDB implements Comparable{
    private String columnName;
    private String tableName;
    private int columnOrder;
    private String columnDataType;
    private int columnSize;
    private String primaryKey;
    private String nullable;

    @Override
    public int compareTo(Object o) {
        return this.columnOrder - ((ColumnDB)o).columnOrder;
    }
}

