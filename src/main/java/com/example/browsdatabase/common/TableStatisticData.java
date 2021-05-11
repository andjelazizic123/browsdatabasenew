package com.example.browsdatabase.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class TableStatisticData {
    private String tableName;
    private Integer numberOfRecord;
    private Double averageRowLength;
    private Integer dataLength;
    private Integer numberOfColumn;
}
