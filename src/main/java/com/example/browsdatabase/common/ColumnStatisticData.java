package com.example.browsdatabase.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ColumnStatisticData {
     private String tableName;
     private String columnName;
     private BigDecimal maxValue;
     private BigDecimal minValue;
     private BigDecimal avgValue;
     private BigDecimal median;
}
